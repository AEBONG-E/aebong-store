package com.aebong.store.controller.req;

import lombok.Data;

@Data
public class AuthRefreshRequest {

    private String refreshToken;
}
