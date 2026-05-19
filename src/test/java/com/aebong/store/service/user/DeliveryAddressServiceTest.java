package com.aebong.store.service.user;

import com.aebong.store.common.enums.user.UserAccountType;
import com.aebong.store.common.enums.user.UserRole;
import com.aebong.store.common.enums.user.UserStatus;
import com.aebong.store.common.enums.user.UserType;
import com.aebong.store.common.exception.ForbiddenException;
import com.aebong.store.controller.req.AddressRequest;
import com.aebong.store.controller.res.DeliveryAddressResponse;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.domain.entity.user.UserDeliveryAddressEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDeliveryAddressRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryAddressServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDeliveryAddressRepository userDeliveryAddressRepository;

    @InjectMocks
    private DeliveryAddressService deliveryAddressService;

    private UserEntity userEntity;
    private UserDeliveryAddressEntity addressEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("tester")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("password")
                .userStatus(UserStatus.ACTIVATED)
                .userRole(UserRole.CUSTOMER)
                .build();

        addressEntity = UserDeliveryAddressEntity.builder()
                .user(userEntity)
                .addressName("집")
                .firstName("길동")
                .lastName("홍")
                .mobileNumber("01012345678")
                .telNumber("0212345678")
                .address(Address.builder().address1("서울시").address2("101동").zipcode("12345").build())
                .deliveryMessage("문 앞")
                .isDefault(Boolean.TRUE)
                .isAddressValid(Boolean.TRUE)
                .build();
    }

    @Test
    void 배송지목록_조회_성공() {
        when(userRepository.findByUserAccount("tester")).thenReturn(Optional.of(userEntity));
        when(userDeliveryAddressRepository.findAllByUser_UserAccountOrderByIdDesc("tester"))
                .thenReturn(List.of(addressEntity));

        List<DeliveryAddressResponse> responses = deliveryAddressService.getAddresses("tester");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getAddressName()).isEqualTo("집");
    }

    @Test
    void 배송지_추가_성공() {
        AddressRequest request = createAddressRequest();

        when(userRepository.findByUserAccount("tester")).thenReturn(Optional.of(userEntity));
        when(userDeliveryAddressRepository.save(any(UserDeliveryAddressEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryAddressResponse response = deliveryAddressService.addAddress("tester", request);

        verify(userDeliveryAddressRepository).findByUser_UserAccountAndIsDefaultTrue("tester");
        assertThat(response.getAddressName()).isEqualTo("회사");
        assertThat(response.getIsDefault()).isTrue();
    }

    @Test
    void 배송지_수정_성공() {
        AddressRequest request = createAddressRequest();
        request.setAddressName("수정된 배송지");

        when(userDeliveryAddressRepository.findById(1L)).thenReturn(Optional.of(addressEntity));

        DeliveryAddressResponse response = deliveryAddressService.modifyAddress("tester", 1L, request);

        assertThat(response.getAddressName()).isEqualTo("수정된 배송지");
        assertThat(addressEntity.getAddressName()).isEqualTo("수정된 배송지");
    }

    @Test
    void 배송지_삭제_성공() {
        when(userDeliveryAddressRepository.findById(1L)).thenReturn(Optional.of(addressEntity));

        deliveryAddressService.deleteAddress("tester", 1L);

        verify(userDeliveryAddressRepository).delete(addressEntity);
    }

    @Test
    void 기본배송지_설정_성공() {
        UserDeliveryAddressEntity oldDefault = UserDeliveryAddressEntity.builder()
                .user(userEntity)
                .addressName("기존 기본")
                .firstName("길동")
                .lastName("홍")
                .mobileNumber("01012345678")
                .address(Address.builder().address1("서울시").zipcode("12345").build())
                .deliveryMessage("경비실")
                .isDefault(Boolean.TRUE)
                .isAddressValid(Boolean.TRUE)
                .build();

        when(userDeliveryAddressRepository.findById(1L)).thenReturn(Optional.of(addressEntity));
        when(userDeliveryAddressRepository.findByUser_UserAccountAndIsDefaultTrue("tester"))
                .thenReturn(Optional.of(oldDefault));

        DeliveryAddressResponse response = deliveryAddressService.setDefaultAddress("tester", 1L);

        assertThat(response.getIsDefault()).isTrue();
        assertThat(oldDefault.getIsDefault()).isFalse();
    }

    @Test
    void 배송지_수정_실패_타인배송지() {
        UserEntity anotherUser = UserEntity.builder()
                .userType(UserType.REGULAR_MEMBER)
                .userAccount("other")
                .userAccountType(UserAccountType.EMAIL)
                .userPassword("password")
                .userStatus(UserStatus.ACTIVATED)
                .userRole(UserRole.CUSTOMER)
                .build();

        UserDeliveryAddressEntity anotherAddress = UserDeliveryAddressEntity.builder()
                .user(anotherUser)
                .addressName("타인 주소")
                .address(Address.builder().address1("부산시").zipcode("54321").build())
                .isDefault(Boolean.FALSE)
                .isAddressValid(Boolean.TRUE)
                .build();

        when(userDeliveryAddressRepository.findById(99L)).thenReturn(Optional.of(anotherAddress));

        assertThatThrownBy(() -> deliveryAddressService.modifyAddress("tester", 99L, createAddressRequest()))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("해당 배송지에 접근할 수 없습니다.");
    }

    private AddressRequest createAddressRequest() {
        AddressRequest request = new AddressRequest();
        request.setAddressName("회사");
        request.setFirstName("길동");
        request.setLastName("홍");
        request.setMobileNumber("01099998888");
        request.setTelNumber("0211112222");
        request.setAddress1("판교로");
        request.setAddress2("100");
        request.setZipcode("13487");
        request.setDeliveryMessage("보안실");
        request.setIsDefault(Boolean.TRUE);
        request.setIsAddressValid(Boolean.TRUE);
        return request;
    }
}
