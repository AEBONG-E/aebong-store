package com.aebong.store.service.user;

import com.aebong.store.controller.req.UserModifyRequest;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.service.user.dto.UserGetInfo;

public interface UserService {

    /**
     * 사용자 등록
     * @param registerRequest 사용자 등록 정보(api 통해 request 로 부터 받아올 매개변수)
     */
    void registerUser(UserRegisterRequest registerRequest);

    /**
     * 사용자 조회
     * @param userAccount 로그인 시 받은 회원 계정
     * @return UserGetInfo
     */
    UserGetInfo getUser(String userAccount);

    /**
     * 사용자 정보 수정
     * @param userModifyRequest 사용자 조회 response
     */
    void modifyUser(UserModifyRequest userModifyRequest);

}
