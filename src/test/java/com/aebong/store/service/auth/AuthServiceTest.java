package com.aebong.store.service.auth;

import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserRole;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.common.exception.ResourceNotFoundException;
import com.aebong.store.common.exception.UnauthorizedException;
import com.aebong.store.common.security.jwt.JwtTokenProvider;
import com.aebong.store.domain.entity.user.RefreshTokenEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.RefreshTokenRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.auth.dto.TokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("tester")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("plain-password")
                .userStatus(UserStatus.ACTIVATED)
                .userRole(UserRole.CUSTOMER)
                .build();
    }

    @Test
    void 로그인_성공() {
        when(userRepository.findByUserAccount("tester")).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.createAccessToken(any())).thenReturn("access-token");
        when(jwtTokenProvider.createRefreshToken(any())).thenReturn("refresh-token");
        when(jwtTokenProvider.getExpiration("refresh-token")).thenReturn(LocalDateTime.now().plusDays(7));

        TokenResponse response = authService.login("tester", "plain-password");

        ArgumentCaptor<RefreshTokenEntity> refreshTokenCaptor = ArgumentCaptor.forClass(RefreshTokenEntity.class);
        verify(refreshTokenRepository).deleteByUserAccount("tester");
        verify(refreshTokenRepository).save(refreshTokenCaptor.capture());

        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(refreshTokenCaptor.getValue().getUserAccount()).isEqualTo("tester");
        assertThat(refreshTokenCaptor.getValue().getTokenHash()).hasSize(64);
    }

    @Test
    void 로그인_실패_존재하지_않는_계정() {
        when(userRepository.findByUserAccount("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("missing", "password"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }

    @Test
    void 로그인_실패_비밀번호_불일치() {
        UserEntity encodedUser = UserEntity.builder()
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("tester")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("$2a$encoded")
                .userStatus(UserStatus.ACTIVATED)
                .userRole(UserRole.CUSTOMER)
                .build();

        when(userRepository.findByUserAccount("tester")).thenReturn(Optional.of(encodedUser));
        when(passwordEncoder.matches("wrong-password", "$2a$encoded")).thenReturn(false);

        assertThatThrownBy(() -> authService.login("tester", "wrong-password"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("계정 또는 비밀번호가 올바르지 않습니다.");
    }

    @Test
    void 토큰_재발급_성공() {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userAccount("tester")
                .tokenHash("hashed-token")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .isRevoked(Boolean.FALSE)
                .build();

        when(jwtTokenProvider.validateToken("refresh-token")).thenReturn(true);
        when(refreshTokenRepository.findByTokenHash(anyString())).thenReturn(Optional.of(refreshTokenEntity));
        when(userRepository.findByUserAccount("tester")).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.createAccessToken(any())).thenReturn("new-access-token");

        TokenResponse response = authService.refresh("refresh-token");

        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isNull();
        assertThat(response.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void 로그아웃_성공() {
        authService.logout("tester");

        verify(refreshTokenRepository).deleteByUserAccount("tester");
        verifyNoMoreInteractions(refreshTokenRepository);
    }
}
