package com.aebong.store.service.stock;

public interface StockService {

    /**
     * Optimistic Locking 기반 재고 차감.
     * OptimisticLockException 발생 시 최대 3회 재시도.
     *
     * @param productId 상품 ID
     * @param quantity  차감할 수량
     */
    void decreaseStock(Long productId, int quantity);

    /**
     * 주문 취소 시 재고 복원.
     *
     * @param productId 상품 ID
     * @param quantity  복원할 수량
     */
    void increaseStock(Long productId, int quantity);
}
