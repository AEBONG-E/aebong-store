package com.aebong.store.facade.order;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.order.OrderStatus;
import com.aebong.store.common.exceptions.OrderApplicationException;
import com.aebong.store.domain.entity.cart.CartEntity;
import com.aebong.store.domain.entity.cart.CartItemEntity;
import com.aebong.store.domain.entity.order.OrderDeliveryEntity;
import com.aebong.store.domain.entity.order.OrderEntity;
import com.aebong.store.domain.entity.order.OrderItemEntity;
import com.aebong.store.domain.entity.order.OrderStatusHistoryEntity;
import com.aebong.store.domain.entity.product.PriceEntity;
import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.cart.CartRepository;
import com.aebong.store.domain.repository.order.OrderRepository;
import com.aebong.store.domain.repository.order.OrderStatusHistoryRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Cart → Order 변환을 단일 @Transactional로 처리하는 Facade.
 *
 * 재고 차감은 JPA @Version Optimistic Locking에 의존한다.
 * 동시 주문 경합 시 flush 시점에 OptimisticLockException 발생 → 전체 트랜잭션 롤백.
 * 클라이언트는 409 CONFLICT를 받아 재시도한다.
 *
 * 독립적인 재고 복원(주문 취소) 등은 StockService를 사용한다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrderFacade {

    private static final AtomicLong ORDER_SEQ = new AtomicLong(0);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    /**
     * 장바구니 기반 주문 생성.
     *
     * 1. 장바구니 및 사용자 조회
     * 2. 각 CartItem의 재고 검증 및 차감 (@Version Optimistic Locking)
     * 3. OrderEntity + OrderItemEntity 생성 (가격/상품명 스냅샷 포함)
     * 4. OrderDeliveryEntity 생성
     * 5. 초기 상태 이력 기록
     * 6. 장바구니 비우기
     *
     * @param userAccount     주문하는 회원 계정
     * @param deliveryCommand 배송지 정보
     * @return 생성된 주문번호
     */
    @Transactional
    public String placeOrder(String userAccount, OrderDeliveryCommand deliveryCommand) {
        UserEntity user = userRepository.findByUserAccount(userAccount)
                .orElseThrow(() -> new OrderApplicationException(
                        CustomErrorType.NOT_FOUND_USER,
                        String.format("userAccount=%s 사용자를 찾을 수 없습니다.", userAccount)));

        CartEntity cart = cartRepository.findByUserAccountWithItems(userAccount)
                .orElseThrow(() -> new OrderApplicationException(
                        CustomErrorType.NOT_FOUND_CART,
                        String.format("userAccount=%s 장바구니를 찾을 수 없습니다.", userAccount)));

        List<CartItemEntity> cartItems = cart.getItems();
        if (cartItems.isEmpty()) {
            throw new OrderApplicationException(CustomErrorType.EMPTY_CART, "장바구니가 비어있습니다.");
        }

        String orderNumber = generateOrderNumber();
        BigDecimal totalAmount = BigDecimal.ZERO;

        OrderEntity order = OrderEntity.create(orderNumber, user, BigDecimal.ZERO);
        orderRepository.save(order);

        for (CartItemEntity cartItem : cartItems) {
            ProductEntity product = cartItem.getProduct();
            int requestedQty = cartItem.getQuantity();

            // @Version Optimistic Locking: flush 시 UPDATE ... WHERE id=? AND version=? 실행
            product.decreaseStock(requestedQty);

            BigDecimal unitPrice = resolveCurrentPrice(product);
            String productName = product.getProductDetail() != null
                    ? product.getProductDetail().getProductName()
                    : product.getProductCode();

            OrderItemEntity orderItem = OrderItemEntity.create(order, product, productName, unitPrice, requestedQty);
            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount.add(unitPrice.multiply(BigDecimal.valueOf(requestedQty)));
        }

        // 총액 반영 (create 후 setter 없으므로 새로운 OrderEntity로 대체하지 않고 직접 업데이트)
        updateOrderTotalAmount(order, totalAmount);

        OrderDeliveryEntity delivery = OrderDeliveryEntity.create(
                order,
                deliveryCommand.getRecipientName(),
                deliveryCommand.getPhone(),
                deliveryCommand.getAddress(),
                deliveryCommand.getDetailAddress(),
                deliveryCommand.getZipCode()
        );
        order.getOrderItems();  // ensure cascade persists via flush

        orderStatusHistoryRepository.save(
                OrderStatusHistoryEntity.record(order, OrderStatus.PENDING, "주문 접수"));

        cart.clearItems();

        log.info("주문 생성 완료 (orderNumber={}, userAccount={}, totalAmount={})",
                orderNumber, userAccount, totalAmount);
        return orderNumber;
    }

    /**
     * 주문 취소.
     * SHIPPING / DELIVERED 상태는 취소 불가 (OrderEntity.cancel() 에서 검증).
     */
    @Transactional
    public void cancelOrder(Long orderId, String userAccount) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderApplicationException(
                        CustomErrorType.NOT_FOUND_ORDER,
                        String.format("orderId=%d 주문을 찾을 수 없습니다.", orderId)));

        if (!order.getUser().getUserAccount().equals(userAccount)) {
            throw new OrderApplicationException(CustomErrorType.BAD_REQUEST, "본인의 주문만 취소할 수 있습니다.");
        }

        order.cancel();

        // 재고 복원
        for (OrderItemEntity item : order.getOrderItems()) {
            if (item.getProduct() != null) {
                item.getProduct().increaseStock(item.getQuantity());
            }
        }

        orderStatusHistoryRepository.save(
                OrderStatusHistoryEntity.record(order, OrderStatus.CANCELLED, "주문 취소"));

        log.info("주문 취소 완료 (orderId={}, userAccount={})", orderId, userAccount);
    }

    private BigDecimal resolveCurrentPrice(ProductEntity product) {
        LocalDate today = LocalDate.now();
        return product.getPrices().stream()
                .filter(p -> !today.isBefore(p.getApplyStartDate()) && !today.isAfter(p.getApplyEndDate()))
                .map(PriceEntity::getSalesAmount)
                .findFirst()
                .orElseGet(() -> product.getPrices().isEmpty()
                        ? BigDecimal.ZERO
                        : product.getPrices().get(product.getPrices().size() - 1).getSalesAmount());
    }

    private String generateOrderNumber() {
        String date = LocalDateTime.now().format(DATE_FMT);
        long seq = ORDER_SEQ.incrementAndGet();
        return String.format("ORD-%s-%05d", date, seq);
    }

    /**
     * OrderEntity.totalAmount는 생성 후 변경 시나리오를 위한 업데이트 메서드.
     * 엔티티에 setter 없이 리플렉션을 쓰지 않으려면 OrderEntity에 updateTotalAmount() 추가 필요.
     */
    private void updateOrderTotalAmount(OrderEntity order, BigDecimal totalAmount) {
        order.updateTotalAmount(totalAmount);
    }
}
