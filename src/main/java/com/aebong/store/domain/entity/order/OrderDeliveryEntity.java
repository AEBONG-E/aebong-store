package com.aebong.store.domain.entity.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_deliveries")
@Entity
public class OrderDeliveryEntity {

    @Comment("배송 정보 PK")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Comment("주문 (1:1)")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private OrderEntity order;

    @Comment("수령인 이름")
    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    @Comment("연락처")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Comment("주소")
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Comment("상세 주소")
    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    @Comment("우편번호")
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @Builder
    private OrderDeliveryEntity(OrderEntity order, String recipientName, String phone,
                                 String address, String detailAddress, String zipCode) {
        this.order = order;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
    }

    public static OrderDeliveryEntity create(OrderEntity order, String recipientName, String phone,
                                              String address, String detailAddress, String zipCode) {
        return OrderDeliveryEntity.builder()
                .order(order)
                .recipientName(recipientName)
                .phone(phone)
                .address(address)
                .detailAddress(detailAddress)
                .zipCode(zipCode)
                .build();
    }
}
