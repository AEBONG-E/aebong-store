package com.aebong.store.controller.req;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.service.user.dto.UserRegisterInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserRegisterRequest {

    private UserType userType;
    private String userAccount;
//    private UserAccountType userAccountType; // todo: 추후 SNS 소셜 계정 가입 기능 추가 시 필요
    private String userPassword;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String mobileNumber;
    private String nickName;
    private String telNumber;
    private String email;
    private Address address;

    @Builder
    public UserRegisterRequest(String userAccount,
                               UserAccountType userAccountType,
                               String userPassword,
                               String firstName,
                               String lastName,
                               LocalDate birthDate,
                               Gender gender,
                               String mobileNumber,
                               String nickName,
                               String telNumber,
                               String email,
                               String address1,
                               String address2,
                               String zipcode) {
        this.userType = UserType.REGULAR_MEMBER;
        this.userAccount = userAccount;
//        this.userAccountType = userAccountType;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.nickName = nickName;
        this.telNumber = telNumber;
        this.email = email;
        this.address = Address.builder()
                .address1(address1)
                .address2(address2)
                .zipcode(zipcode)
                .build();
    }

}
