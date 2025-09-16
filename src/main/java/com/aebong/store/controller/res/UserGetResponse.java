package com.aebong.store.controller.res;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.service.user.dto.UserGetInfo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserGetResponse {

    private Long userId;
    private Long userDetailId;
//    private UserType userType;
    private String userAccount;
//    private UserAccountType userAccountType;
    private String userPassword;
//    private UserStatus userStatus;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String mobileNumber;
//    private String rrn;
    private String nickName;
//    private String telNumber;
    private String email;
    private String address1;
    private String address2;
    private String zipcode;
    private LocalDateTime joinDatetime;

    @Builder
    public UserGetResponse(Long userId,
                           Long userDetailId,
                           UserType userType,
                           String userAccount,
                           UserAccountType userAccountType,
                           String userPassword,
                           UserStatus userStatus,
                           Boolean passwordInitYn,
                           int failPasswordCount,
                           LocalDateTime accountLockedDatetime,
                           LocalDateTime lastLoginDatetime,
                           LocalDateTime lastPasswordChangeDatetime,
                           LocalDateTime requiredPasswordChangeDatetime,
                           LocalDate loginAvailableDate,
                           String firstName,
                           String lastName,
                           LocalDate birthDate,
                           Gender gender,
                           String mobileNumber,
                           String rrn,
                           String nickName,
                           String telNumber,
                           String email,
                           Address address,
                           LocalDateTime joinDatetime) {
        this.userId = userId;
        this.userDetailId = userDetailId;
//        this.userType = userType;
        this.userAccount = userAccount;
//        this.userAccountType = userAccountType;
        this.userPassword = userPassword;
//        this.userStatus = userStatus;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
//        this.rrn = rrn;
        this.nickName = nickName;
//        this.telNumber = telNumber;
        this.email = email;
        this.address1 = address.getAddress1();
        this.address2 = address.getAddress2();
        this.zipcode = address.getZipcode();
        this.joinDatetime = joinDatetime;
    }

    public static UserGetResponse to(UserGetInfo userGetInfo) {
        if (userGetInfo == null) return null;

        return UserGetResponse.builder()
                .userId(userGetInfo.getUserId())
                .userDetailId(userGetInfo.getUserDetailId())
                .userAccount(userGetInfo.getUserAccount())
                .userPassword(userGetInfo.getUserPassword())
                .firstName(userGetInfo.getFirstName())
                .lastName(userGetInfo.getLastName())
                .birthDate(userGetInfo.getBirthDate())
                .gender(userGetInfo.getGender())
                .mobileNumber(userGetInfo.getMobileNumber())
                .nickName(userGetInfo.getNickName())
                .email(userGetInfo.getEmail())
                .address(userGetInfo.getAddress())
                .joinDatetime(userGetInfo.getJoinDatetime())
                .build();
    }

}
