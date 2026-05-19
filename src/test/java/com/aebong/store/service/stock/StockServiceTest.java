package com.aebong.store.service.stock;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.common.exceptions.OrderApplicationException;
import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.domain.repository.product.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockTransactionalOperations stockOps;

    private ProductEntity product;

    @BeforeEach
    void setUp() {
        product = ProductEntity.builder()
                .productCode("TEST-001")
                .productType(ProductType.STANDARD)
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("재고 차감 성공")
    void decreaseStock_성공() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        stockOps.decreaseStock(1L, 3);

        assertThat(product.getStock()).isEqualTo(7);
    }

    @Test
    @DisplayName("재고 없음 — OUT_OF_STOCK 예외")
    void decreaseStock_재고없음_예외() {
        ProductEntity emptyStock = ProductEntity.builder()
                .productCode("TEST-002")
                .productType(ProductType.STANDARD)
                .stock(0)
                .build();
        given(productRepository.findById(2L)).willReturn(Optional.of(emptyStock));

        assertThatThrownBy(() -> stockOps.decreaseStock(2L, 1))
                .isInstanceOf(OrderApplicationException.class)
                .satisfies(e -> assertThat(((OrderApplicationException) e).getErrorType())
                        .isEqualTo(CustomErrorType.OUT_OF_STOCK));
    }

    @Test
    @DisplayName("재고 부족 — INSUFFICIENT_STOCK 예외")
    void decreaseStock_재고부족_예외() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        assertThatThrownBy(() -> stockOps.decreaseStock(1L, 15))
                .isInstanceOf(OrderApplicationException.class)
                .satisfies(e -> assertThat(((OrderApplicationException) e).getErrorType())
                        .isEqualTo(CustomErrorType.INSUFFICIENT_STOCK));
    }

    @Test
    @DisplayName("상품 없음 — NOT_FOUND_PRODUCT 예외")
    void decreaseStock_상품없음_예외() {
        given(productRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> stockOps.decreaseStock(99L, 1))
                .isInstanceOf(OrderApplicationException.class)
                .satisfies(e -> assertThat(((OrderApplicationException) e).getErrorType())
                        .isEqualTo(CustomErrorType.NOT_FOUND_PRODUCT));
    }

    @Test
    @DisplayName("재고 복원 성공")
    void increaseStock_성공() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));

        stockOps.increaseStock(1L, 5);

        assertThat(product.getStock()).isEqualTo(15);
    }

    @Test
    @DisplayName("Optimistic Lock 충돌 — MAX_RETRY 후 STOCK_OPTIMISTIC_LOCK 예외")
    void decreaseStock_OptimisticLock_재시도_초과() {
        StockTransactionalOperations mockOps = Mockito.mock(StockTransactionalOperations.class);
        willThrow(new ObjectOptimisticLockingFailureException(ProductEntity.class, 1L))
                .given(mockOps).decreaseStock(anyLong(), anyInt());

        StockServiceImpl serviceWithMockOps = new StockServiceImpl(mockOps);

        assertThatThrownBy(() -> serviceWithMockOps.decreaseStock(1L, 3))
                .isInstanceOf(OrderApplicationException.class)
                .satisfies(e -> assertThat(((OrderApplicationException) e).getErrorType())
                        .isEqualTo(CustomErrorType.STOCK_OPTIMISTIC_LOCK));

        then(mockOps).should(times(3)).decreaseStock(anyLong(), anyInt());
    }
}
