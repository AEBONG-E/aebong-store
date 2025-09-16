package com.aebong.store.service.user.dto;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.domain.entity.user.UserDetailEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class UserReadInfo {

    private Long userId;
    private Long userDetailId;
    private UserType userType;
    private String userAccount;
    private UserAccountType userAccountType;
    private String userPassword;
    private UserStatus userStatus;
    private Boolean passwordInitYn;
    private int failPasswordCount;
    private LocalDateTime accountLockedDatetime;
    private LocalDateTime lastLoginDatetime;
    private LocalDateTime lastPasswordChangeDatetime;
    private LocalDateTime requiredPasswordChangeDatetime;
    private LocalDate loginAvailableDate;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String mobileNumber;
    private String rrn;
    private String nickName;
    private String telNumber;
    private String email;
    private Address address;
    private LocalDateTime joinDatetime;
    private LocalDateTime activatedDatetime;
    private LocalDateTime inactivatedDatetime;
    private LocalDateTime withdrawalDatetime;
    private LocalDateTime dormantDatetime;

    @Builder
    public UserReadInfo(Long userId,
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
                        LocalDateTime joinDatetime,
                        LocalDateTime activatedDatetime,
                        LocalDateTime inactivatedDatetime,
                        LocalDateTime withdrawalDatetime,
                        LocalDateTime dormantDatetime) {
        this.userId = userId;
        this.userDetailId = userDetailId;
        this.userType = userType;
        this.userAccount = userAccount;
        this.userAccountType = userAccountType;
        this.userPassword = userPassword;
        this.userStatus = userStatus;
        this.passwordInitYn = passwordInitYn;
        this.failPasswordCount = failPasswordCount;
        this.accountLockedDatetime = accountLockedDatetime;
        this.lastLoginDatetime = lastLoginDatetime;
        this.lastPasswordChangeDatetime = lastPasswordChangeDatetime;
        this.requiredPasswordChangeDatetime = requiredPasswordChangeDatetime;
        this.loginAvailableDate = loginAvailableDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.rrn = rrn;
        this.nickName = nickName;
        this.telNumber = telNumber;
        this.email = email;
        this.address = address;
        this.joinDatetime = joinDatetime;
        this.activatedDatetime = activatedDatetime;
        this.inactivatedDatetime = inactivatedDatetime;
        this.withdrawalDatetime = withdrawalDatetime;
        this.dormantDatetime = dormantDatetime;
    }

    public static UserReadInfo from(UserEntity user, UserDetailEntity userDetail) {
        if (user == null) return null;

        return UserReadInfo.builder()
                .userId(user.getId())
                .userDetailId(userDetail.getId())
                .userType(user.getUserType())
                .userAccount(user.getUserAccount())
                .userAccountType(user.getUserAccountType())
                .userPassword(user.getUserPassword())
                .userStatus(user.getUserStatus())
                .passwordInitYn(user.getPasswordInitYn())
                .failPasswordCount(user.getFailPasswordCount())
                .accountLockedDatetime(user.getAccountLockedDatetime())
                .lastLoginDatetime(user.getLastLoginDatetime())
                .lastPasswordChangeDatetime(user.getLastPasswordChangeDatetime())
                .requiredPasswordChangeDatetime(user.getRequiredPasswordChangeDatetime())
                .loginAvailableDate(user.getLoginAvailableDate())
                .firstName(userDetail.getFirstName())
                .lastName(userDetail.getLastName())
                .birthDate(userDetail.getBirthDate())
                .gender(userDetail.getGender())
                .mobileNumber(userDetail.getMobileNumber())
                .rrn(userDetail.getRrn())
                .nickName(userDetail.getNickName())
                .telNumber(userDetail.getTelNumber())
                .email(userDetail.getEmail())
                .address(userDetail.getAddress())
                .joinDatetime(userDetail.getJoinDatetime())
                .activatedDatetime(userDetail.getActivatedDatetime())
                .inactivatedDatetime(userDetail.getInactivatedDatetime())
                .withdrawalDatetime(userDetail.getWithdrawalDatetime())
                .dormantDatetime(userDetail.getDormantDatetime())
                .build();
    }

}