package com.aebong.store.service.stock;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.OrderApplicationException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

/**
 * Optimistic Locking 재고 차감 재시도 오케스트레이터.
 *
 * StockTransactionalOperations를 REQUIRES_NEW 트랜잭션으로 호출하여 재시도 시 매번
 * 최신 version을 읽도록 보장한다. 재고 비즈니스 검증(재고 없음/부족)은 동일 메서드 내에서
 * 수행하므로 재시도 전 클라이언트에게 즉시 오류를 반환한다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {

    private static final int MAX_RETRY = 3;

    private final StockTransactionalOperations stockOps;

    @Override
    public void decreaseStock(Long productId, int quantity) {
        int attempt = 0;
        while (attempt < MAX_RETRY) {
            try {
                stockOps.decreaseStock(productId, quantity);
                return;
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
                attempt++;
                log.warn("재고 차감 Optimistic Lock 충돌 (productId={}, attempt={})", productId, attempt);
                if (attempt >= MAX_RETRY) {
                    throw new OrderApplicationException(
                            CustomErrorType.STOCK_OPTIMISTIC_LOCK,
                            String.format("productId=%d 재고 차감 실패 (재시도 초과)", productId));
                }
            }
        }
    }

    @Override
    public void increaseStock(Long productId, int quantity) {
        stockOps.increaseStock(productId, quantity);
    }
}
