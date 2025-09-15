package com.aebong.store.service.user;

import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.service.user.dto.UserReadInfo;
import com.aebong.store.service.user.dto.UserRegisterInfo;

public interface UserService {

    void registerUser(UserRegisterRequest registerRequest);

    UserReadInfo getUser(String userAccount);

}
