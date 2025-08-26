package com.aebong.store.controller;

import com.aebong.store.common.api.ApiResponse;
import com.aebong.store.service.user.UserService;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/public")
    ApiResponse<Void> registerUser(@RequestBody UserRegisterInfo registerInfo) {
        userService.registerUser(registerInfo);
        return ApiResponse.success();
    }

}
