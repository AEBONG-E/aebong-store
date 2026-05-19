package com.aebong.store.controller.req;

import lombok.Data;

@Data
public class AddressRequest {

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
}
