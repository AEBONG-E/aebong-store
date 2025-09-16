package com.aebong.store.service.user;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.UserApplicationException;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserGetInfo;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    /**
     * 회원 등록
     * @param registerRequest 회원 등록 정보(api 통해 request 로 부터 받아올 매개변수)
     */
    @Transactional
    @Override
    public void registerUser(UserRegisterRequest registerRequest) {

        if (Objects.isNull(registerRequest)) {
            throw new IllegalArgumentException("registerInfo must not be null");
        }

        // 회원 계정 중복 검증
        if (validateUserAccountIsExists(registerRequest.getUserAccount())) {
            throw new UserApplicationException(CustomErrorType.IS_EXIST_USER,
                    String.format("%s already exists userAccount", registerRequest.getUserAccount()));
        }

//        if (validateEmailIsExists(registerRequest.getEmail())) {
//            throw new UserApplicationException(CustomErrorType.IS_EXIST_USER,
//                    String.format("%s already exists email", registerRequest.getEmail()));
//        }

        UserRegisterInfo registerInfo = UserRegisterInfo.to(registerRequest);

        // 회원 DB 저장 데이터 생성 (todo: 회원 기본 정보 DB 저장 전 비밀번호 암호화 처리 필요)
        UserEntity user = registerInfo.toUserEntity();
        userRepository.save(user);

        UserDetailEntity userDetail = registerInfo.toUserDetailEntity(user);
        userDetailRepository.save(userDetail);

    }

    @Override
    public UserGetInfo getUser(String userAccount) {

        if (Objects.isNull(userAccount))
            throw new UserApplicationException(CustomErrorType.INTERNAL_SERVER_ERROR, "userAccount is not null");

        // find user entity, userDetail entity
        UserEntity user = userRepository.findByUserAccount(userAccount).orElseThrow(
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER.getMessage()));

        UserDetailEntity userDetail = userDetailRepository.findByUser(user).orElseThrow(
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER.getMessage()));

        return UserGetInfo.to(user, userDetail);

    }

    private boolean validateUserAccountIsExists(String userAccount) {
        if (Objects.isNull(userAccount)) {
            throw new IllegalArgumentException("userAccount is null");
        }
        return userRepository.existsByUserAccount(userAccount);
    }

    private boolean validateEmailIsExists(String email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("userAccount is null");
        }
        return userDetailRepository.existsByEmail(email);
    }

}
