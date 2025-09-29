package com.aebong.store.controller;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.controller.api.ProductController;
import com.aebong.store.domain.entity.product.ImageEntity;
import com.aebong.store.domain.entity.product.ProductDetailEntity;
import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.service.product.ProductService;
import com.aebong.store.service.product.dto.ProductRegisterInfo;
import com.aebong.store.service.product.dto.ProductRegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ProductService productService;


    @Test
    void 상품등록_실패_1() throws Exception {

        // given
        ProductRegisterRequest registerRequest = createProductRegisterInfo();
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(registerRequest);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

        doThrow(new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_CODE, CustomErrorType.IS_EXIST_PRODUCT_CODE.getMessageKr()))
                .when(productService).registerProduct(any(ProductRegisterRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("IS_EXIST_PRODUCT_CODE"))
                .andExpect(jsonPath("$.message").value("존재하는 상품 코드입니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("product-register-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("productCode").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productType").type(JsonFieldType.OBJECT).description(""),
                                        fieldWithPath("productName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productShortName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("basicDescription").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("detailDescription").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
                                        subsectionWithPath("data").type(JsonFieldType.OBJECT).description("error 발생시 null").optional()
                                ),
                                requestBody(),
                                responseBody()
                ));
    }

    @Test
    void 상품등록_실패_2() throws Exception {

        // given
        ProductRegisterRequest registerRequest = createProductRegisterInfo();
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(registerRequest);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

        doThrow(new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_NAME, CustomErrorType.IS_EXIST_PRODUCT_NAME.getMessageKr()))
                .when(productService).registerProduct(any(ProductRegisterRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("IS_EXIST_PRODUCT_NAME"))
                .andExpect(jsonPath("$.message").value("존재하는 상품명입니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("product-register-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("productCode").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productType").type(JsonFieldType.OBJECT).description(""),
                                        fieldWithPath("productName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productShortName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("basicDescription").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("detailDescription").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
                                        subsectionWithPath("data").type(JsonFieldType.OBJECT).description("error 발생시 null").optional()
                                ),
                                requestBody(),
                                responseBody()
                ));
    }

    @Test
    void 상품등록_실패_3() throws Exception {

        // given
        ProductRegisterRequest registerRequest = createProductRegisterInfo();
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(registerRequest);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

        doThrow(new ProductApplicationException(CustomErrorType.INVALID_AMOUNT, CustomErrorType.INVALID_AMOUNT.getMessageKr()))
                .when(productService).registerProduct(any(ProductRegisterRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("INVALID_AMOUNT"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 금액입니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("product-register-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("productCode").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productType").type(JsonFieldType.OBJECT).description(""),
                                        fieldWithPath("productName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("productShortName").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("basicDescription").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("detailDescription").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description(""),
                                        fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
                                        subsectionWithPath("data").type(JsonFieldType.OBJECT).description("error 발생시 null").optional()
                                ),
                                requestBody(),
                                responseBody()
                ));
    }

    @Test
    void 상품등록_성공() throws Exception {

        // given
        ProductRegisterRequest registerRequest = createProductRegisterInfo();
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(registerRequest);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

        doNothing().when(productService).registerProduct(any(ProductRegisterRequest.class));

        // when & then
        mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productCode").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("productType").type(JsonFieldType.OBJECT).description(""),
                                fieldWithPath("productName").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("productShortName").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("basicDescription").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("detailDescription").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description(""),
                                fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메시지").optional(),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("응답데이터").optional()
                        ),
                        requestBody(),
                        responseBody()
                ));
    }


    private ProductRegisterRequest createProductRegisterInfo() {
        return ProductRegisterRequest.builder()
                .productCode("0000000000")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_블랙")
                .productEnglishName("TEST_001A_BLACK")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_블랙 상품입니다.")
                .detailDescription("테스트 상품입니다. 테스트 상품입니다. 테스트 상품입니다.")
                .manufacturerCountry("Republic of Korea")
                .releaseDatetime(LocalDateTime.now())
                .price(createPriceRegisterInfo())
                .imageList(createImageRegisterInfo())
                .build();
    }

    private ProductRegisterRequest.PriceRegisterRequest createPriceRegisterInfo() {
        return ProductRegisterRequest.PriceRegisterRequest.builder()
                .applyStartDate(LocalDate.now())
                .applyEndDate(LocalDate.of(9999, 12, 31))
                .salesAmount(BigDecimal.valueOf(75000.0))
                .purchaseAmount(BigDecimal.valueOf(70000.0))
                .discountType(DiscountType.NONE)
                .discountAmount(BigDecimal.ZERO)
                .discount(0L)
                .build();
    }

    private List<ProductRegisterRequest.ImageRegisterRequest> createImageRegisterInfo() {
        return List.of(
                ProductRegisterRequest.ImageRegisterRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0001.png")
                        .originalImageFileName("TEST_001A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_001A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductRegisterRequest.ImageRegisterRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0002.png")
                        .originalImageFileName("TEST_002A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_002A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductRegisterRequest.ImageRegisterRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0003.png")
                        .originalImageFileName("TEST_003A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_003A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build()
        );

    }

    private ProductRegisterRequest createInvalidProductRegisterInfo() {
        return ProductRegisterRequest.builder()
                .productCode("0000000000")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_블랙")
                .productEnglishName("TEST_001A_BLACK")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_블랙 상품입니다.")
                .detailDescription("테스트 상품입니다. 테스트 상품입니다. 테스트 상품입니다.")
                .manufacturerCountry("Republic of Korea")
                .releaseDatetime(LocalDateTime.now())
                .price(createInvalidPriceRegisterInfo())
                .imageList(createInvalidImageRegisterInfo())
                .build();
    }

    private ProductRegisterRequest.PriceRegisterRequest createInvalidPriceRegisterInfo() {
        return ProductRegisterRequest.PriceRegisterRequest.builder()
                .applyStartDate(LocalDate.now())
                .applyEndDate(LocalDate.of(9999, 12, 31))
                .salesAmount(BigDecimal.valueOf(-1))
                .purchaseAmount(BigDecimal.valueOf(-1))
                .discountType(DiscountType.NONE)
                .discountAmount(BigDecimal.valueOf(-1))
                .discount(0L)
                .build();
    }

    private List<ProductRegisterRequest.ImageRegisterRequest> createInvalidImageRegisterInfo() {
        return List.of(
                ProductRegisterRequest.ImageRegisterRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0001.png")
                        .originalImageFileName("TEST_001A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_001A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductRegisterRequest.ImageRegisterRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0002.png")
                        .originalImageFileName("TEST_002A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_002A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductRegisterRequest.ImageRegisterRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0003.png")
                        .originalImageFileName("TEST_003A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_003A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build()
        );

    }


}