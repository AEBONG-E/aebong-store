package com.aebong.store.controller.user;

import com.aebong.store.common.api.ApiResponse;
import com.aebong.store.common.security.userdetails.CustomUserDetails;
import com.aebong.store.controller.req.AddressRequest;
import com.aebong.store.controller.res.DeliveryAddressResponse;
import com.aebong.store.service.user.DeliveryAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userAccount}/addresses")
@RequiredArgsConstructor
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryAddressResponse>>> getAddresses(@PathVariable String userAccount,
                                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        validateAccount(userAccount, userDetails);
        return new ResponseEntity<>(ApiResponse.success(deliveryAddressService.getAddresses(userAccount)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryAddressResponse>> addAddress(@PathVariable String userAccount,
                                                                           @RequestBody AddressRequest request,
                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        validateAccount(userAccount, userDetails);
        return new ResponseEntity<>(ApiResponse.success(deliveryAddressService.addAddress(userAccount, request)), HttpStatus.OK);
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<ApiResponse<DeliveryAddressResponse>> modifyAddress(@PathVariable String userAccount,
                                                                              @PathVariable Long addressId,
                                                                              @RequestBody AddressRequest request,
                                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        validateAccount(userAccount, userDetails);
        return new ResponseEntity<>(ApiResponse.success(deliveryAddressService.modifyAddress(userAccount, addressId, request)), HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable String userAccount,
                                                           @PathVariable Long addressId,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        validateAccount(userAccount, userDetails);
        deliveryAddressService.deleteAddress(userAccount, addressId);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<DeliveryAddressResponse>> setDefaultAddress(@PathVariable String userAccount,
                                                                                  @PathVariable Long addressId,
                                                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        validateAccount(userAccount, userDetails);
        return new ResponseEntity<>(ApiResponse.success(deliveryAddressService.setDefaultAddress(userAccount, addressId)), HttpStatus.OK);
    }

    private void validateAccount(String userAccount, CustomUserDetails userDetails) {
        if (userDetails == null || !userAccount.equals(userDetails.getUsername())) {
            throw new com.aebong.store.common.exception.ForbiddenException("본인 배송지만 접근할 수 있습니다.");
        }
    }
}
