package com.aebong.store.controller.api;

import com.aebong.store.common.api.ApiResponse;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.controller.res.UserGetResponse;
import com.aebong.store.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody UserRegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @GetMapping("/{userAccount}")
    public ResponseEntity<ApiResponse<UserGetResponse>> getUser(@PathVariable String userAccount) {
        return new ResponseEntity<>(ApiResponse.success(UserGetResponse.to(userService.getUser(userAccount))), HttpStatus.OK);
    }

}
