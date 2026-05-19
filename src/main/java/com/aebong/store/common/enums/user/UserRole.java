package com.aebong.store.common.enums.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ADMIN("ROLE_ADMIN"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String role;

}
