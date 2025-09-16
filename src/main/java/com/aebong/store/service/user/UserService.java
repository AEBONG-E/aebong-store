package com.aebong.store.service.user;

import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.service.user.dto.UserGetInfo;

public interface UserService {

    void registerUser(UserRegisterRequest registerRequest);

    UserGetInfo getUser(String userAccount);

}
