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
    private UserAccountType userAccountType;
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
    public UserRegisterRequest(UserType userType, String userAccount,
                               UserAccountType userAccountType, String userPassword,
                               UserStatus userStatus, Boolean passwordInitYn,
                               int failPasswordCount, LocalDateTime accountLockedDatetime,
                               LocalDateTime lastLoginDatetime, LocalDateTime lastPasswordChangeDatetime,
                               LocalDateTime requiredPasswordChangeDatetime, LocalDate loginAvailableDate,
                               LocalDate rejoinPossibleDate, Boolean isRejoin,
                               String firstName, String lastName,
                               LocalDate birthDate, Gender gender,
                               String mobileNumber, String rrn,
                               String nickName, String telNumber,
                               String email, String address1,
                               String address2, String zipcode,
                               LocalDateTime joinDatetime, LocalDateTime activatedDatetime,
                               LocalDateTime inactivatedDatetime, LocalDateTime withdrawalDatetime,
                               LocalDateTime dormantDatetime) {
        this.userType = userType;
        this.userAccount = userAccount;
        this.userAccountType = userAccountType;
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
