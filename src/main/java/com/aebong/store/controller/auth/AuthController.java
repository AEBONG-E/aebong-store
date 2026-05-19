package com.aebong.store.controller.auth;

import com.aebong.store.common.api.ApiResponse;
import com.aebong.store.common.security.userdetails.CustomUserDetails;
import com.aebong.store.controller.req.AuthLoginRequest;
import com.aebong.store.controller.req.AuthRefreshRequest;
import com.aebong.store.service.auth.AuthService;
import com.aebong.store.service.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody AuthLoginRequest request) {
        return new ResponseEntity<>(ApiResponse.success(authService.login(request.getAccount(), request.getPassword())), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@RequestBody AuthRefreshRequest request) {
        return new ResponseEntity<>(ApiResponse.success(authService.refresh(request.getRefreshToken())), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }
}
