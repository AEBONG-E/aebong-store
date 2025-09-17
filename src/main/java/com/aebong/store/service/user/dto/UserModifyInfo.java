package com.aebong.store.service.user.dto;

import com.aebong.store.common.enums.user.Gender;
import com.aebong.store.controller.req.UserModifyRequest;
import com.aebong.store.controller.req.UserRegisterRequest;
import com.aebong.store.domain.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
public class UserModifyInfo {

    private Long userId;
    private Long userDetailId;
    private String userPassword;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String mobileNumber;
//    private String rrn;
    private String nickName;
    private String telNumber;
//    private String email;
    private Address address;

    @Builder
    public UserModifyInfo(Long userId,
                          Long userDetailId,
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
                          String zipcode) {
        this.userId = userId;
        this.userDetailId = userDetailId;
        this.userPassword = userPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
//        this.rrn = rrn;
        this.nickName = nickName;
        this.telNumber = telNumber;
//        this.email = email;
        this.address = Address.builder()
                .address1(address1)
                .address2(address2)
                .zipcode(zipcode)
                .build();
    }

    public static UserModifyInfo to(UserModifyRequest modifyRequest) {
        if (modifyRequest == null) return null;
        return UserModifyInfo.builder()
                .userId(modifyRequest.getUserId())
                .userDetailId(modifyRequest.getUserDetailId())
                .userPassword(modifyRequest.getUserPassword())
                .firstName(modifyRequest.getFirstName())
                .lastName(modifyRequest.getLastName())
                .birthDate(modifyRequest.getBirthDate())
                .gender(modifyRequest.getGender())
                .mobileNumber(modifyRequest.getMobileNumber())
                .nickName(modifyRequest.getNickName())
                .address1(modifyRequest.getAddress1())
                .address2(Objects.nonNull(modifyRequest.getAddress2()) ? modifyRequest.getAddress2() : null)
                .zipcode(modifyRequest.getZipcode())
                .build();
    }

}