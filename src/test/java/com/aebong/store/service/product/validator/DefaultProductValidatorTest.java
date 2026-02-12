package com.aebong.store.service.product.validator;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.controller.req.ProductImageRequest;
import com.aebong.store.controller.req.ProductModifyRequest;
import com.aebong.store.controller.req.ProductPriceRequest;
import com.aebong.store.controller.req.ProductRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DefaultProductValidatorTest {

    private ProductValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DefaultProductValidator();
    }

    @Test
    void 유효성검증_실패_유효하지않은_금액_등록_수정_통합_케이스() {
        ProductRegisterRequest registerRequest = createInvalidProductRegisterRequest();
        ProductModifyRequest modifyRequest = createInvalidProductModifyRequest();

        assertThatThrownBy(() -> validator.validateForRegister(registerRequest))
                .isInstanceOf(ProductApplicationException.class)
                .satisfies(e -> assertError((ProductApplicationException) e, "-1 this amount is invalid", CustomErrorType.INVALID_AMOUNT));

        assertThatThrownBy(() -> validator.validateForModify(modifyRequest))
                .isInstanceOf(ProductApplicationException.class)
                .satisfies(e -> assertError((ProductApplicationException) e, "-1 this amount is invalid", CustomErrorType.INVALID_AMOUNT));
    }

    @Test
    void 상품등록_유효성검증_실패_요청정보가_null인_케이스() {
        assertThatThrownBy(() -> validator.validateForRegister(null))
                .isInstanceOf(ProductApplicationException.class)
                .satisfies(e -> assertError((ProductApplicationException) e, "registerInfo must not be null", CustomErrorType.BAD_REQUEST));
    }

    @Test
    void 상품수정_유효성검증_실패_요청정보가_null인_케이스() {
        assertThatThrownBy(() -> validator.validateForModify(null))
                .isInstanceOf(ProductApplicationException.class)
                .satisfies(e -> assertError((ProductApplicationException) e, "modifyRequest must not be null", CustomErrorType.BAD_REQUEST));
    }

    private void assertError(ProductApplicationException exception, String message, CustomErrorType errorType) {
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getErrorType()).isEqualTo(errorType);
    }

    private ProductRegisterRequest createInvalidProductRegisterRequest() {
        return ProductRegisterRequest.builder()
                .productCode("0000000000")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_블랙")
                .productEnglishName("TEST_001A_BLACK")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_블랙 상품입니다.")
                .detailDescription("테스트 상품입니다.")
                .manufacturerCountry("대한민국")
                .releaseDatetime(LocalDateTime.now())
                .price(createInvalidPrice())
                .imageList(createImageList())
                .build();
    }

    private ProductModifyRequest createInvalidProductModifyRequest() {
        return ProductModifyRequest.builder()
                .productId(1L)
                .productDetailId(1L)
                .productCode("0000000000")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_블랙")
                .productEnglishName("TEST_001A_BLACK")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_블랙 상품입니다.")
                .detailDescription("테스트 상품입니다.")
                .manufacturerCountry("대한민국")
                .releaseDatetime(LocalDateTime.now())
                .price(createInvalidPrice())
                .imageList(createImageList())
                .build();
    }

    private ProductPriceRequest createInvalidPrice() {
        return ProductPriceRequest.builder()
                .priceId(1L)
                .applyStartDate(LocalDate.now())
                .applyEndDate(LocalDate.of(9999, 12, 31))
                .salesAmount(BigDecimal.valueOf(-1))
                .purchaseAmount(BigDecimal.valueOf(-1))
                .discountType(DiscountType.NONE)
                .discountAmount(BigDecimal.valueOf(-1))
                .discount(0L)
                .build();
    }

    private List<ProductImageRequest> createImageList() {
        return List.of(
                ProductImageRequest.builder()
                        .imageId(1L)
                        .adminImageFileName("TEST_0001.png")
                        .originalImageFileName("TEST_0001.png")
                        .imageFileName("TEST_0001.png")
                        .imageFileUrl("/static/images/TEST_0001.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build()
        );
    }

}
