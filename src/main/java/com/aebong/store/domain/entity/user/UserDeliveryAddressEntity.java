package com.aebong.store.domain.entity.user;

import com.aebong.store.common.util.BooleanToYnConverter;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.domain.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_delivery_addresses")
@Entity
public class UserDeliveryAddressEntity extends AuditingEntity {

    @Comment("회원배송지 순번 PK")
    @Column(name = "user_delivery_addresses_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("회원순번")
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Comment("배송지 별칭")
    @Column(name = "address_name", length = 100)
    private String addressName;

    @Comment("수취인 이름")
    @Column(name = "first_name", length = 100)
    private String firstName;

    @Comment("수취인 성")
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Comment("수취인 휴대폰번호")
    @Column(name = "mobile_number", length = 11)
    private String mobileNumber;

    @Comment("수취인 전화번호")
    @Column(name = "tel_number", length = 11)
    private String telNumber;

    @Embedded
    private Address address;

    @Comment("배송메시지")
    @Column(name = "delivery_message", length = 11)
    private String deliveryMessage;

    @Comment("기본배송지 여부")
    @Convert(converter = BooleanToYnConverter.class)
    @Column(name = "default_yn")
    private Boolean isDefault = Boolean.FALSE;

    @Comment("기본배송지 여부")
    @Convert(converter = BooleanToYnConverter.class)
    @Column(name = "address_valid_yn")
    private Boolean isAddressValid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDeliveryAddressEntity that = (UserDeliveryAddressEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Builder
    private UserDeliveryAddressEntity(UserEntity user, String addressName, String firstName, String lastName, String mobileNumber, String telNumber, Address address, String deliveryMessage, Boolean isDefault, Boolean isAddressValid) {
        this.user = user;
        this.addressName = addressName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.telNumber = telNumber;
        this.address = address;
        this.deliveryMessage = deliveryMessage;
        this.isDefault = isDefault;
        this.isAddressValid = isAddressValid;
    }

}
