package com.aebong.store.controller.req;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.service.user.dto.UserGetInfo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserModifyRequest {

    private Long userId;
    private Long userDetailId;
    private String userAccount;
    private String userPassword;
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
    public UserModifyRequest(Long userId,
                             Long userDetailId,
                             String userAccount,
                             String userPassword,
                             String firstName,
                             String lastName,
                             LocalDate birthDate,
                             Gender gender,
                             String mobileNumber,
                             String rrn,
                             String nickName,
                             String telNumber,
                             String email,
                             String address1,
                             String address2,
                             String zipcode,
                             LocalDateTime joinDatetime) {
        this.userId = userId;
        this.userDetailId = userDetailId;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
//        this.rrn = rrn;
        this.nickName = nickName;
//        this.telNumber = telNumber;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
        this.joinDatetime = joinDatetime;
    }

}
