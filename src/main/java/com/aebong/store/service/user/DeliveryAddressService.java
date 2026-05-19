package com.aebong.store.service.user;

import com.aebong.store.common.exception.ForbiddenException;
import com.aebong.store.common.exception.ResourceNotFoundException;
import com.aebong.store.controller.req.AddressRequest;
import com.aebong.store.controller.res.DeliveryAddressResponse;
import com.aebong.store.domain.entity.Address;
import com.aebong.store.domain.entity.user.UserDeliveryAddressEntity;
import com.aebong.store.domain.entity.user.UserEntity;
import com.aebong.store.domain.repository.user.UserDeliveryAddressRepository;
import com.aebong.store.domain.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final UserRepository userRepository;
    private final UserDeliveryAddressRepository userDeliveryAddressRepository;

    @Transactional(readOnly = true)
    public List<DeliveryAddressResponse> getAddresses(String account) {
        validateUser(account);
        return userDeliveryAddressRepository.findAllByUser_UserAccountOrderByIdDesc(account).stream()
                .map(DeliveryAddressResponse::from)
                .toList();
    }

    @Transactional
    public DeliveryAddressResponse addAddress(String account, AddressRequest request) {
        UserEntity userEntity = validateUser(account);

        boolean makeDefault = Boolean.TRUE.equals(request.getIsDefault())
                || userDeliveryAddressRepository.findAllByUser_UserAccountOrderByIdDesc(account).isEmpty();

        if (makeDefault) {
            userDeliveryAddressRepository.findByUser_UserAccountAndIsDefaultTrue(account)
                    .ifPresent(UserDeliveryAddressEntity::unsetDefaultAddress);
        }

        UserDeliveryAddressEntity entity = UserDeliveryAddressEntity.builder()
                .user(userEntity)
                .addressName(request.getAddressName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobileNumber(request.getMobileNumber())
                .telNumber(request.getTelNumber())
                .address(toAddress(request))
                .deliveryMessage(request.getDeliveryMessage())
                .isDefault(makeDefault)
                .isAddressValid(request.getIsAddressValid() == null ? Boolean.TRUE : request.getIsAddressValid())
                .build();

        return DeliveryAddressResponse.from(userDeliveryAddressRepository.save(entity));
    }

    @Transactional
    public DeliveryAddressResponse modifyAddress(String account, Long addressId, AddressRequest request) {
        UserDeliveryAddressEntity entity = getOwnedAddress(account, addressId);

        entity.update(
                request.getAddressName(),
                request.getFirstName(),
                request.getLastName(),
                request.getMobileNumber(),
                request.getTelNumber(),
                toAddress(request),
                request.getDeliveryMessage(),
                request.getIsAddressValid() == null ? entity.getIsAddressValid() : request.getIsAddressValid()
        );

        return DeliveryAddressResponse.from(entity);
    }

    @Transactional
    public void deleteAddress(String account, Long addressId) {
        UserDeliveryAddressEntity entity = getOwnedAddress(account, addressId);
        userDeliveryAddressRepository.delete(entity);
    }

    @Transactional
    public DeliveryAddressResponse setDefaultAddress(String account, Long addressId) {
        UserDeliveryAddressEntity entity = getOwnedAddress(account, addressId);

        userDeliveryAddressRepository.findByUser_UserAccountAndIsDefaultTrue(account)
                .filter(defaultAddress -> !Objects.equals(defaultAddress.getId(), addressId))
                .ifPresent(UserDeliveryAddressEntity::unsetDefaultAddress);

        entity.setDefaultAddress();
        return DeliveryAddressResponse.from(entity);
    }

    private UserEntity validateUser(String account) {
        return userRepository.findByUserAccount(account)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다. account=" + account));
    }

    private UserDeliveryAddressEntity getOwnedAddress(String account, Long addressId) {
        UserDeliveryAddressEntity entity = userDeliveryAddressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("배송지를 찾을 수 없습니다. addressId=" + addressId));

        if (!entity.getUser().getUserAccount().equals(account)) {
            throw new ForbiddenException("해당 배송지에 접근할 수 없습니다.");
        }

        return entity;
    }

    private Address toAddress(AddressRequest request) {
        return Address.builder()
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .zipcode(request.getZipcode())
                .build();
    }
}
