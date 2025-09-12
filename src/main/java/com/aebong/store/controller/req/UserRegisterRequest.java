package com.aebong.store.controller.req;

import com.aebong.store.common.enums.user.Gender;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserRegisterRequest {

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
    private String address1;
    private String address2;
    private String zipcode;

    @Builder
    public UserRegisterRequest(String userAccount,
                               String userPassword,
                               String firstName,
                               String lastName,
                               LocalDate birthDate,
                               Gender gender,
                               String mobileNumber,
                               String nickName,
                               String telNumber,
                               String address1,
                               String address2,
                               String zipcode) {
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.nickName = nickName;
        this.telNumber = telNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
    }

}
