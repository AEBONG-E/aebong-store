package com.aebong.store.service.stock;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.OrderApplicationException;
import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.domain.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * StockServiceImpl의 재시도 루프에서 호출되는 실제 DB 조작 Bean.
 * REQUIRES_NEW 트랜잭션으로 독립 실행하여 재시도 시 version을 새로 읽는다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class StockTransactionalOperations {

    private final ProductRepository productRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decreaseStock(Long productId, int quantity) {
        ProductEntity product = findOrThrow(productId);

        if (product.getStock() <= 0) {
            throw new OrderApplicationException(CustomErrorType.OUT_OF_STOCK,
                    String.format("productId=%d 재고 없음", productId));
        }
        if (product.getStock() < quantity) {
            throw new OrderApplicationException(CustomErrorType.INSUFFICIENT_STOCK,
                    String.format("productId=%d 재고 부족 (현재=%d, 요청=%d)", productId, product.getStock(), quantity));
        }

        product.decreaseStock(quantity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increaseStock(Long productId, int quantity) {
        ProductEntity product = findOrThrow(productId);
        product.increaseStock(quantity);
        log.info("재고 복원 완료 (productId={}, qty={})", productId, quantity);
    }

    private ProductEntity findOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new OrderApplicationException(
                        CustomErrorType.NOT_FOUND_PRODUCT,
                        String.format("productId=%d 상품을 찾을 수 없습니다.", productId)));
    }
}
