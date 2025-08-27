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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class UserRegisterInfo {

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
    private LocalDate rejoinPossibleDate;
    private Boolean isRejoin;
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
    public UserRegisterInfo(UserType userType, String userAccount,
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
        this.userStatus = UserStatus.ACTIVATED;
        this.lastPasswordChangeDatetime = lastPasswordChangeDatetime;
        this.requiredPasswordChangeDatetime = requiredPasswordChangeDatetime;
        this.loginAvailableDate = LocalDate.now();
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.rrn = rrn;
        this.nickName = nickName;
        this.telNumber = telNumber;
        this.email = email;
        this.address = Address.builder()
                .address1(address1)
                .address2(address2)
                .zipcode(zipcode)
                .build();
        this.joinDatetime = LocalDateTime.now();
        this.activatedDatetime = LocalDateTime.now();
    }

    public UserEntity toUserEntity() {
        return UserEntity.builder()
                .userType(this.userType)
                .userAccount(this.userAccount)
                .userAccountType(this.userAccountType)
                .userPassword(this.userPassword)
                .userStatus(this.userStatus)
                .requiredPasswordChangeDatetime(this.requiredPasswordChangeDatetime)
                .build();
    }

    public UserDetailEntity toUserDetailEntity(UserEntity userEntity) {
        if (userEntity == null) return null;
        return UserDetailEntity.builder()
                .user(userEntity)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .birthDate(this.birthDate)
                .gender(this.gender)
                .mobileNumber(this.mobileNumber)
                .rrn(this.rrn)
                .nickName(this.nickName)
                .telNumber(this.telNumber)
                .email(this.email)
                .address(this.address)
                .joinDatetime(this.joinDatetime)
                .activatedDatetime(this.activatedDatetime)
                .inactivatedDatetime(this.inactivatedDatetime)
                .withdrawalDatetime(this.withdrawalDatetime)
                .dormantDatetime(this.dormantDatetime)
                .build();
    }

    public UserRegisterRequest toRequest(UserRegisterInfo userRegisterInfo) {
        if (userRegisterInfo == null) return null;
        return UserRegisterRequest.builder()
                .userType(userRegisterInfo.getUserType())
                .userAccount(userRegisterInfo.getUserAccount())
                .userAccountType(userRegisterInfo.getUserAccountType())
                .userPassword(userRegisterInfo.getUserPassword())
                .firstName(userRegisterInfo.getFirstName())
                .lastName(userRegisterInfo.getLastName())
                .birthDate(userRegisterInfo.getBirthDate())
                .gender(userRegisterInfo.getGender())
                .mobileNumber(userRegisterInfo.getMobileNumber())
                .nickName(userRegisterInfo.getNickName())
                .telNumber(userRegisterInfo.getTelNumber())
                .email(userRegisterInfo.getEmail())
                .address1(userRegisterInfo.getAddress().getAddress1())
                .address2(Objects.nonNull(userRegisterInfo.getAddress().getAddress2()) ? userRegisterInfo.getAddress().getAddress2() : null)
                .zipcode(userRegisterInfo.getAddress().getZipcode())
                .build();
    }

}