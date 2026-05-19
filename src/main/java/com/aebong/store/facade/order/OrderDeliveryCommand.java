package com.aebong.store.facade.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDeliveryCommand {

    private String recipientName;
    private String phone;
    private String address;
    private String detailAddress;
    private String zipCode;

    public OrderDeliveryCommand(String recipientName, String phone,
                                String address, String detailAddress, String zipCode) {
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipCode = zipCode;
    }
}
