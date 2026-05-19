package com.aebong.store.service.auth;

import com.aebong.store.common.exception.ResourceNotFoundException;
import com.aebong.store.common.exception.UnauthorizedException;
import com.aebong.store.common.security.jwt.JwtTokenProvider;
import com.aebong.store.common.security.userdetails.CustomUserDetails;
import com.aebong.store.domain.entity.user.RefreshTokenEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.RefreshTokenRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse login(String account, String password) {
        UserEntity userEntity = userRepository.findByUserAccount(account)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다. account=" + account));

        if (!matchesPassword(password, userEntity.getUserPassword())) {
            throw new UnauthorizedException("계정 또는 비밀번호가 올바르지 않습니다.");
        }

        CustomUserDetails userDetails = new CustomUserDetails(userEntity);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenRepository.deleteByUserAccount(account);
        refreshTokenRepository.save(RefreshTokenEntity.builder()
                .userAccount(account)
                .tokenHash(hashToken(refreshToken))
                .expiresAt(jwtTokenProvider.getExpiration(refreshToken))
                .isRevoked(Boolean.FALSE)
                .build());

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public TokenResponse refresh(String refreshToken) {
        validateRefreshToken(refreshToken);

        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByTokenHash(hashToken(refreshToken))
                .orElseThrow(() -> new UnauthorizedException("유효하지 않은 리프레시 토큰입니다."));

        if (Boolean.TRUE.equals(tokenEntity.getIsRevoked()) || LocalDateTime.now().isAfter(tokenEntity.getExpiresAt())) {
            throw new UnauthorizedException("만료되었거나 폐기된 리프레시 토큰입니다.");
        }

        UserEntity userEntity = userRepository.findByUserAccount(tokenEntity.getUserAccount())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다. account=" + tokenEntity.getUserAccount()));

        CustomUserDetails userDetails = new CustomUserDetails(userEntity);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return TokenResponse.accessTokenOnly(jwtTokenProvider.createAccessToken(authentication));
    }

    @Transactional
    public void logout(String account) {
        if (!StringUtils.hasText(account)) {
            throw new UnauthorizedException("로그인 정보가 필요합니다.");
        }
        refreshTokenRepository.deleteByUserAccount(account);
    }

    private void validateRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

    private boolean matchesPassword(String rawPassword, String savedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(savedPassword)) {
            return false;
        }

        if (savedPassword.startsWith("$2a$") || savedPassword.startsWith("$2b$") || savedPassword.startsWith("$2y$")) {
            return passwordEncoder.matches(rawPassword, savedPassword);
        }
        return rawPassword.equals(savedPassword);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 해시 생성에 실패했습니다.", e);
        }
    }
}
