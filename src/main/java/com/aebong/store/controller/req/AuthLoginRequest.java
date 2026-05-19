package com.aebong.store.controller.req;

import lombok.Data;

@Data
public class AuthLoginRequest {

    private String account;
    private String password;
}
