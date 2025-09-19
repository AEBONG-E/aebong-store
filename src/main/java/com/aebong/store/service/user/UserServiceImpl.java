package com.aebong.store.service.user;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.exceptions.UserApplicationException;
import com.aebong.store.controller.req.UserModifyRequest;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDetailRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import com.aebong.store.service.user.dto.UserGetInfo;
import com.aebong.store.service.user.dto.UserModifyInfo;
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


    @Transactional
    @Override
    public void registerUser(UserRegisterRequest registerRequest) {

        if (Objects.isNull(registerRequest)) {
            throw new UserApplicationException(CustomErrorType.INTERNAL_SERVER_ERROR, "registerInfo must not be null");
        }

        // check if user exists
        if (validateUserAccountIsExists(registerRequest.getUserAccount())) {
            throw new UserApplicationException(CustomErrorType.IS_EXIST_USER,
                    String.format("%s already exists userAccount", registerRequest.getUserAccount()));
        }

//        if (validateEmailIsExists(registerRequest.getEmail())) {
//            throw new UserApplicationException(CustomErrorType.IS_EXIST_USER,
//                    String.format("%s already exists email", registerRequest.getEmail()));
//        }

        UserRegisterInfo registerInfo = UserRegisterInfo.to(registerRequest);

        //  todo: 회원 기본 정보 DB 저장 전 비밀번호 암호화 처리 필요

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
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessage()));

        UserDetailEntity userDetail = userDetailRepository.findByUser(user).orElseThrow(
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessage()));

        return UserGetInfo.to(user, userDetail);

    }

    @Transactional
    @Override
    public void modifyUser(String userAccount, UserModifyRequest userModifyRequest) {

        if (Objects.isNull(userAccount))
            throw new UserApplicationException(CustomErrorType.INTERNAL_SERVER_ERROR, "userAccount is not null");

        if (Objects.isNull(userModifyRequest)) {
            throw new UserApplicationException(CustomErrorType.INTERNAL_SERVER_ERROR, "modifyInfo must not be null");
        }

        // find user entity, userDetail entity
        UserEntity user = userRepository.findByUserAccount(userAccount).orElseThrow(
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessage()));
        UserDetailEntity userDetail = userDetailRepository.findByUser(user).orElseThrow(
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessage()));

        // request -> dto mapping
        UserModifyInfo userModifyInfo = UserModifyInfo.to(userModifyRequest);

        // update user entity, userDetail entity (dirty checking)
        user.update(userModifyInfo);
        userDetail.update(userModifyInfo);

    }

    @Transactional
    @Override
    public void deleteUser(String userAccount) {

        if (Objects.isNull(userAccount))
            throw new UserApplicationException(CustomErrorType.INTERNAL_SERVER_ERROR, "userAccount is not null");

        // find user entity, userDetail entity
        UserEntity user = userRepository.findByUserAccount(userAccount).orElseThrow(
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessage()));

        UserDetailEntity userDetail = userDetailRepository.findByUser(user).orElseThrow(
                () -> new UserApplicationException(CustomErrorType.NOT_FOUND_USER, CustomErrorType.NOT_FOUND_USER.getMessage()));

        userDetail.delete();
        user.delete();
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
