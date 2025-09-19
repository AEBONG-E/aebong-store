package com.aebong.store.controller.req;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginRequest {

    private String userAccount;
    private String userPassword;

    @Builder
    public UserLoginRequest(String userAccount, String userPassword) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
    }

}
