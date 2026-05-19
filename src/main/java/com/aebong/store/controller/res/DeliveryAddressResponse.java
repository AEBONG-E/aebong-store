package com.aebong.store.controller.res;

import com.aebong.store.domain.entity.user.UserDeliveryAddressEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryAddressResponse {

    private Long addressId;
    private String userAccount;
    private String addressName;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String telNumber;
    private String address1;
    private String address2;
    private String zipcode;
    private String deliveryMessage;
    private Boolean isDefault;
    private Boolean isAddressValid;

    public static DeliveryAddressResponse from(UserDeliveryAddressEntity entity) {
        return DeliveryAddressResponse.builder()
                .addressId(entity.getId())
                .userAccount(entity.getUser().getUserAccount())
                .addressName(entity.getAddressName())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .mobileNumber(entity.getMobileNumber())
                .telNumber(entity.getTelNumber())
                .address1(entity.getAddress() == null ? null : entity.getAddress().getAddress1())
                .address2(entity.getAddress() == null ? null : entity.getAddress().getAddress2())
                .zipcode(entity.getAddress() == null ? null : entity.getAddress().getZipcode())
                .deliveryMessage(entity.getDeliveryMessage())
                .isDefault(entity.getIsDefault())
                .isAddressValid(entity.getIsAddressValid())
                .build();
    }
}
