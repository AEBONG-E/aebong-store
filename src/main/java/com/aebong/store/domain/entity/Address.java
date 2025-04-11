package com.aebong.store.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {

    @Comment("주소")
    @Column
    private String address1;

    @Comment("상세주소")
    @Column
    private String address2;

    @Comment("시")
    @Column(length = 100)
    private String city;

    @Comment("도")
    @Column(length = 100)
    private String state;

    @Comment("우편번호")
    @Column(length = 6)
    private String zipCode;

    @Builder
    public Address(String address1, String address2, String city, String state, String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

}
