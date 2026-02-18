package com.aebong.store.controller;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.controller.api.ProductController;
import com.aebong.store.controller.req.ProductImageRequest;
import com.aebong.store.controller.req.ProductPriceRequest;
import com.aebong.store.controller.req.ProductRegisterRequest;
import com.aebong.store.controller.res.ProductImageResponse;
import com.aebong.store.controller.res.ProductPriceResponse;
import com.aebong.store.domain.entity.product.ImageEntity;
import com.aebong.store.domain.entity.product.ProductDetailEntity;
import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.service.product.ProductService;
import com.aebong.store.service.product.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
    void 상품등록_실패_중복상품코드() throws Exception {

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
                                fieldWithPath("productCode").type(JsonFieldType.STRING).description("상품코드"),
                                fieldWithPath("productType").type(JsonFieldType.STRING).description("상품유형"),
                                fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description("상품 영문명"),
                                fieldWithPath("productShortName").type(JsonFieldType.STRING).description("상품 짧은이름"),
                                fieldWithPath("basicDescription").type(JsonFieldType.STRING).description("상품 기본설명"),
                                fieldWithPath("detailDescription").type(JsonFieldType.STRING).description("상품 상세설명"),
                                fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description("제조국"),
                                fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("출시일시"),
                                fieldWithPath("price").type(JsonFieldType.OBJECT).description("가격"),
                                fieldWithPath("price.priceId").type(JsonFieldType.NUMBER).description("가격순번").optional(),
                                fieldWithPath("price.applyStartDate").type(JsonFieldType.STRING).description("적용 시작일자"),
                                fieldWithPath("price.applyEndDate").type(JsonFieldType.STRING).description("적용 종료일자"),
                                fieldWithPath("price.salesAmount").type(JsonFieldType.NUMBER).description("상품 판매금액"),
                                fieldWithPath("price.purchaseAmount").type(JsonFieldType.NUMBER).description("상품 매입금액"),
                                fieldWithPath("price.discountType").type(JsonFieldType.STRING).description("할인유형"),
                                fieldWithPath("price.discountAmount").type(JsonFieldType.NUMBER).description("할인율 적용가"),
                                fieldWithPath("price.discount").type(JsonFieldType.NUMBER).description("할인율"),
                                fieldWithPath("imageList").type(JsonFieldType.ARRAY).description("이미지 리스트"),
                                fieldWithPath("imageList[].imageId").type(JsonFieldType.NUMBER).description("이미지 순번").optional(),
                                fieldWithPath("imageList[].adminImageFileName").type(JsonFieldType.STRING).description("이미지파일 관리이름"),
                                fieldWithPath("imageList[].originalImageFileName").type(JsonFieldType.STRING).description("이미지파일 원본이름"),
                                fieldWithPath("imageList[].imageFileName").type(JsonFieldType.STRING).description("이미지파일 이름"),
                                fieldWithPath("imageList[].imageFileUrl").type(JsonFieldType.STRING).description("이미지파일 url"),
                                fieldWithPath("imageList[].imageType").type(JsonFieldType.STRING).description("이미지유형"),
                                fieldWithPath("imageList[].contentType").type(JsonFieldType.STRING).description("컨텐츠유형"),
                                fieldWithPath("imageList[].width").type(JsonFieldType.NUMBER).description("너비"),
                                fieldWithPath("imageList[].height").type(JsonFieldType.NUMBER).description("높이"),
                                fieldWithPath("imageList[].fileSize").type(JsonFieldType.NUMBER).description("파일크기")
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
    void 상품등록_실패_중복상품명() throws Exception {

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
                .andDo(document("product-register-fail-2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productCode").type(JsonFieldType.STRING).description("상품코드"),
                                fieldWithPath("productType").type(JsonFieldType.STRING).description("상품유형"),
                                fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description("상품 영문명"),
                                fieldWithPath("productShortName").type(JsonFieldType.STRING).description("상품 짧은이름"),
                                fieldWithPath("basicDescription").type(JsonFieldType.STRING).description("상품 기본설명"),
                                fieldWithPath("detailDescription").type(JsonFieldType.STRING).description("상품 상세설명"),
                                fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description("제조국"),
                                fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("출시일시"),
                                fieldWithPath("price").type(JsonFieldType.OBJECT).description("가격"),
                                fieldWithPath("price.priceId").type(JsonFieldType.NUMBER).description("가격순번").optional(),
                                fieldWithPath("price.applyStartDate").type(JsonFieldType.STRING).description("적용 시작일자"),
                                fieldWithPath("price.applyEndDate").type(JsonFieldType.STRING).description("적용 종료일자"),
                                fieldWithPath("price.salesAmount").type(JsonFieldType.NUMBER).description("상품 판매금액"),
                                fieldWithPath("price.purchaseAmount").type(JsonFieldType.NUMBER).description("상품 매입금액"),
                                fieldWithPath("price.discountType").type(JsonFieldType.STRING).description("할인유형"),
                                fieldWithPath("price.discountAmount").type(JsonFieldType.NUMBER).description("할인율 적용가"),
                                fieldWithPath("price.discount").type(JsonFieldType.NUMBER).description("할인율"),
                                fieldWithPath("imageList").type(JsonFieldType.ARRAY).description("이미지 리스트"),
                                fieldWithPath("imageList[].imageId").type(JsonFieldType.NUMBER).description("이미지 순번").optional(),
                                fieldWithPath("imageList[].adminImageFileName").type(JsonFieldType.STRING).description("이미지파일 관리이름"),
                                fieldWithPath("imageList[].originalImageFileName").type(JsonFieldType.STRING).description("이미지파일 원본이름"),
                                fieldWithPath("imageList[].imageFileName").type(JsonFieldType.STRING).description("이미지파일 이름"),
                                fieldWithPath("imageList[].imageFileUrl").type(JsonFieldType.STRING).description("이미지파일 url"),
                                fieldWithPath("imageList[].imageType").type(JsonFieldType.STRING).description("이미지유형"),
                                fieldWithPath("imageList[].contentType").type(JsonFieldType.STRING).description("컨텐츠유형"),
                                fieldWithPath("imageList[].width").type(JsonFieldType.NUMBER).description("너비"),
                                fieldWithPath("imageList[].height").type(JsonFieldType.NUMBER).description("높이"),
                                fieldWithPath("imageList[].fileSize").type(JsonFieldType.NUMBER).description("파일크기")
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
    void 상품등록_실패_유효하지않은금액() throws Exception {

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
                .andDo(document("product-register-fail-3",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productCode").type(JsonFieldType.STRING).description("상품코드"),
                                fieldWithPath("productType").type(JsonFieldType.STRING).description("상품유형"),
                                fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description("상품 영문명"),
                                fieldWithPath("productShortName").type(JsonFieldType.STRING).description("상품 짧은이름"),
                                fieldWithPath("basicDescription").type(JsonFieldType.STRING).description("상품 기본설명"),
                                fieldWithPath("detailDescription").type(JsonFieldType.STRING).description("상품 상세설명"),
                                fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description("제조국"),
                                fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("출시일시"),
                                fieldWithPath("price").type(JsonFieldType.OBJECT).description("가격"),
                                fieldWithPath("price.priceId").type(JsonFieldType.NUMBER).description("가격순번").optional(),
                                fieldWithPath("price.applyStartDate").type(JsonFieldType.STRING).description("적용 시작일자"),
                                fieldWithPath("price.applyEndDate").type(JsonFieldType.STRING).description("적용 종료일자"),
                                fieldWithPath("price.salesAmount").type(JsonFieldType.NUMBER).description("상품 판매금액"),
                                fieldWithPath("price.purchaseAmount").type(JsonFieldType.NUMBER).description("상품 매입금액"),
                                fieldWithPath("price.discountType").type(JsonFieldType.STRING).description("할인유형"),
                                fieldWithPath("price.discountAmount").type(JsonFieldType.NUMBER).description("할인율 적용가"),
                                fieldWithPath("price.discount").type(JsonFieldType.NUMBER).description("할인율"),
                                fieldWithPath("imageList").type(JsonFieldType.ARRAY).description("이미지 리스트"),
                                fieldWithPath("imageList[].imageId").type(JsonFieldType.NUMBER).description("이미지 순번").optional(),
                                fieldWithPath("imageList[].adminImageFileName").type(JsonFieldType.STRING).description("이미지파일 관리이름"),
                                fieldWithPath("imageList[].originalImageFileName").type(JsonFieldType.STRING).description("이미지파일 원본이름"),
                                fieldWithPath("imageList[].imageFileName").type(JsonFieldType.STRING).description("이미지파일 이름"),
                                fieldWithPath("imageList[].imageFileUrl").type(JsonFieldType.STRING).description("이미지파일 url"),
                                fieldWithPath("imageList[].imageType").type(JsonFieldType.STRING).description("이미지유형"),
                                fieldWithPath("imageList[].contentType").type(JsonFieldType.STRING).description("컨텐츠유형"),
                                fieldWithPath("imageList[].width").type(JsonFieldType.NUMBER).description("너비"),
                                fieldWithPath("imageList[].height").type(JsonFieldType.NUMBER).description("높이"),
                                fieldWithPath("imageList[].fileSize").type(JsonFieldType.NUMBER).description("파일크기")
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
                                fieldWithPath("productCode").type(JsonFieldType.STRING).description("상품코드"),
                                fieldWithPath("productType").type(JsonFieldType.STRING).description("상품유형"),
                                fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("productEnglishName").type(JsonFieldType.STRING).description("상품 영문명"),
                                fieldWithPath("productShortName").type(JsonFieldType.STRING).description("상품 짧은이름"),
                                fieldWithPath("basicDescription").type(JsonFieldType.STRING).description("상품 기본설명"),
                                fieldWithPath("detailDescription").type(JsonFieldType.STRING).description("상품 상세설명"),
                                fieldWithPath("manufacturerCountry").type(JsonFieldType.STRING).description("제조국"),
                                fieldWithPath("releaseDatetime").type(JsonFieldType.STRING).description("출시일시"),
                                fieldWithPath("price").type(JsonFieldType.OBJECT).description("가격"),
                                fieldWithPath("price.priceId").type(JsonFieldType.NUMBER).description("가격순번").optional(),
                                fieldWithPath("price.applyStartDate").type(JsonFieldType.STRING).description("적용 시작일자"),
                                fieldWithPath("price.applyEndDate").type(JsonFieldType.STRING).description("적용 종료일자"),
                                fieldWithPath("price.salesAmount").type(JsonFieldType.NUMBER).description("상품 판매금액"),
                                fieldWithPath("price.purchaseAmount").type(JsonFieldType.NUMBER).description("상품 매입금액"),
                                fieldWithPath("price.discountType").type(JsonFieldType.STRING).description("할인유형"),
                                fieldWithPath("price.discountAmount").type(JsonFieldType.NUMBER).description("할인율 적용가"),
                                fieldWithPath("price.discount").type(JsonFieldType.NUMBER).description("할인율"),
                                fieldWithPath("imageList").type(JsonFieldType.ARRAY).description("이미지 리스트"),
                                fieldWithPath("imageList[].imageId").type(JsonFieldType.NUMBER).description("이미지 순번").optional(),
                                fieldWithPath("imageList[].adminImageFileName").type(JsonFieldType.STRING).description("이미지파일 관리이름"),
                                fieldWithPath("imageList[].originalImageFileName").type(JsonFieldType.STRING).description("이미지파일 원본이름"),
                                fieldWithPath("imageList[].imageFileName").type(JsonFieldType.STRING).description("이미지파일 이름"),
                                fieldWithPath("imageList[].imageFileUrl").type(JsonFieldType.STRING).description("이미지파일 url"),
                                fieldWithPath("imageList[].imageType").type(JsonFieldType.STRING).description("이미지유형"),
                                fieldWithPath("imageList[].contentType").type(JsonFieldType.STRING).description("컨텐츠유형"),
                                fieldWithPath("imageList[].width").type(JsonFieldType.NUMBER).description("너비"),
                                fieldWithPath("imageList[].height").type(JsonFieldType.NUMBER).description("높이"),
                                fieldWithPath("imageList[].fileSize").type(JsonFieldType.NUMBER).description("파일크기")
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

    @Test
    void 상품상세조회_실패() throws Exception {

        // given
        Long productId = 0L;

        doThrow(new ProductApplicationException(CustomErrorType.NOT_FOUND_PRODUCT, CustomErrorType.NOT_FOUND_PRODUCT.getMessageKr()))
                .when(productService).getProduct(any(Long.class));

        // when & then
        mockMvc.perform(get("/api/v1/products/{productId}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_PRODUCT"))
                .andExpect(jsonPath("$.message").value("상품을 찾을 수 없습니다."))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(document("product-get-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("productId").description("조회할 상품순번")
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
    void 상품상세조회_성공() throws Exception {

        // given
        Long productId = 0L;

        ProductGetInfo productGetInfo = createProductGetInfo();

        given(productService.getProduct(productId)).willReturn(productGetInfo);

        // when & then
        mockMvc.perform(get("/api/v1/products/{productId}", productId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-get",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("productId").description("조회할 상품순번")
                                ),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지").optional(),
                                        subsectionWithPath("data").type(JsonFieldType.OBJECT).description("error 발생시 null"),
                                        fieldWithPath("data.productId").type(JsonFieldType.NUMBER).description("상품순번"),
                                        fieldWithPath("data.productDetailId").type(JsonFieldType.NUMBER).description("상품상세순번"),
                                        fieldWithPath("data.productCode").type(JsonFieldType.STRING).description("상품코드"),
                                        fieldWithPath("data.productType").type(JsonFieldType.STRING).description("상품유형"),
                                        fieldWithPath("data.productName").type(JsonFieldType.STRING).description("상품명"),
                                        fieldWithPath("data.productEnglishName").type(JsonFieldType.STRING).description("상품 영문 명"),
                                        fieldWithPath("data.productShortName").type(JsonFieldType.STRING).description("상품 짧은 이름"),
                                        fieldWithPath("data.basicDescription").type(JsonFieldType.STRING).description("상품 기본 설명"),
                                        fieldWithPath("data.detailDescription").type(JsonFieldType.STRING).description("상품 상세설명"),
                                        fieldWithPath("data.manufacturerCountry").type(JsonFieldType.STRING).description("제조국"),
                                        fieldWithPath("data.releaseDatetime").type(JsonFieldType.STRING).description("출시 일시"),
                                        fieldWithPath("data.priceList").type(JsonFieldType.ARRAY).description("가격 리스트"),
                                        fieldWithPath("data.priceList[].priceId").type(JsonFieldType.NUMBER).description("가격순번"),
                                        fieldWithPath("data.priceList[].applyStartDate").type(JsonFieldType.STRING).description("적용 시작일자"),
                                        fieldWithPath("data.priceList[].applyEndDate").type(JsonFieldType.STRING).description("적용 종료일자"),
                                        fieldWithPath("data.priceList[].salesAmount").type(JsonFieldType.NUMBER).description("상품 판매금액"),
                                        fieldWithPath("data.priceList[].purchaseAmount").type(JsonFieldType.NUMBER).description("상품 매입금액"),
                                        fieldWithPath("data.priceList[].discountType").type(JsonFieldType.STRING).description("할인유형"),
                                        fieldWithPath("data.priceList[].discountAmount").type(JsonFieldType.NUMBER).description("할인율 적용가"),
                                        fieldWithPath("data.priceList[].discount").type(JsonFieldType.NUMBER).description("할인율"),
                                        fieldWithPath("data.imageList").type(JsonFieldType.ARRAY).description("이미지 리스트"),
                                        fieldWithPath("data.imageList[].imageId").type(JsonFieldType.NUMBER).description("이미지순번"),
                                        fieldWithPath("data.imageList[].adminImageFileName").type(JsonFieldType.STRING).description("이미지파일 관리이름"),
                                        fieldWithPath("data.imageList[].originalImageFileName").type(JsonFieldType.STRING).description("이미지파일 원본이름"),
                                        fieldWithPath("data.imageList[].imageFileName").type(JsonFieldType.STRING).description("이미지파일 이름"),
                                        fieldWithPath("data.imageList[].imageFileUrl").type(JsonFieldType.STRING).description("이미지파일 url"),
                                        fieldWithPath("data.imageList[].imageType").type(JsonFieldType.STRING).description("이미지유형"),
                                        fieldWithPath("data.imageList[].contentType").type(JsonFieldType.STRING).description("컨텐츠유형"),
                                        fieldWithPath("data.imageList[].width").type(JsonFieldType.NUMBER).description("너비"),
                                        fieldWithPath("data.imageList[].height").type(JsonFieldType.NUMBER).description("높이"),
                                        fieldWithPath("data.imageList[].fileSize").type(JsonFieldType.NUMBER).description("파일크기")
                                ),
                                requestBody(),
                                responseBody()
                ));
    }

    @Test
    void 상품목록조회_성공() throws Exception {

        // given
        Pageable pageable = PageRequest.of(0, 20);
        List<ProductGetInfo> content = List.of(createProductGetInfo(), createProductGetInfo2());
        Page<ProductGetInfo> products = new PageImpl<>(content, pageable, content.size());

        given(productService.getProducts(pageable)).willReturn(products);

        // when & then
        mockMvc.perform(post("/api/v1/products/paging")
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("products-get",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지").optional(),
                                        subsectionWithPath("data.content").type(JsonFieldType.ARRAY).description("데이터 리스트"),
                                        fieldWithPath("data.content[].productId").type(JsonFieldType.NUMBER).description("상품순번"),
                                        fieldWithPath("data.content[].productDetailId").type(JsonFieldType.NUMBER).description("상품상세순번"),
                                        fieldWithPath("data.content[].productCode").type(JsonFieldType.STRING).description("상품코드"),
                                        fieldWithPath("data.content[].productType").type(JsonFieldType.STRING).description("상품유형"),
                                        fieldWithPath("data.content[].productName").type(JsonFieldType.STRING).description("상품명"),
                                        fieldWithPath("data.content[].productEnglishName").type(JsonFieldType.STRING).description("상품 영문 명"),
                                        fieldWithPath("data.content[].productShortName").type(JsonFieldType.STRING).description("상품 짧은 이름"),
                                        fieldWithPath("data.content[].basicDescription").type(JsonFieldType.STRING).description("상품 기본 설명"),
                                        fieldWithPath("data.content[].detailDescription").type(JsonFieldType.STRING).description("상품 상세설명"),
                                        fieldWithPath("data.content[].manufacturerCountry").type(JsonFieldType.STRING).description("제조국"),
                                        fieldWithPath("data.content[].releaseDatetime").type(JsonFieldType.STRING).description("출시 일시"),
                                        fieldWithPath("data.content[].priceList").type(JsonFieldType.ARRAY).description("가격 리스트"),
                                        fieldWithPath("data.content[].priceList[].priceId").type(JsonFieldType.NUMBER).description("가격순번"),
                                        fieldWithPath("data.content[].priceList[].applyStartDate").type(JsonFieldType.STRING).description("적용 시작일자"),
                                        fieldWithPath("data.content[].priceList[].applyEndDate").type(JsonFieldType.STRING).description("적용 종료일자"),
                                        fieldWithPath("data.content[].priceList[].salesAmount").type(JsonFieldType.NUMBER).description("상품 판매금액"),
                                        fieldWithPath("data.content[].priceList[].purchaseAmount").type(JsonFieldType.NUMBER).description("상품 매입금액"),
                                        fieldWithPath("data.content[].priceList[].discountType").type(JsonFieldType.STRING).description("할인유형"),
                                        fieldWithPath("data.content[].priceList[].discountAmount").type(JsonFieldType.NUMBER).description("할인율 적용가"),
                                        fieldWithPath("data.content[].priceList[].discount").type(JsonFieldType.NUMBER).description("할인율"),
                                        fieldWithPath("data.content[].imageList").type(JsonFieldType.ARRAY).description("이미지 리스트"),
                                        fieldWithPath("data.content[].imageList[].imageId").type(JsonFieldType.NUMBER).description("이미지순번"),
                                        fieldWithPath("data.content[].imageList[].adminImageFileName").type(JsonFieldType.STRING).description("이미지파일 관리이름"),
                                        fieldWithPath("data.content[].imageList[].originalImageFileName").type(JsonFieldType.STRING).description("이미지파일 원본이름"),
                                        fieldWithPath("data.content[].imageList[].imageFileName").type(JsonFieldType.STRING).description("이미지파일 이름"),
                                        fieldWithPath("data.content[].imageList[].imageFileUrl").type(JsonFieldType.STRING).description("이미지파일 url"),
                                        fieldWithPath("data.content[].imageList[].imageType").type(JsonFieldType.STRING).description("이미지유형"),
                                        fieldWithPath("data.content[].imageList[].contentType").type(JsonFieldType.STRING).description("컨텐츠유형"),
                                        fieldWithPath("data.content[].imageList[].width").type(JsonFieldType.NUMBER).description("너비"),
                                        fieldWithPath("data.content[].imageList[].height").type(JsonFieldType.NUMBER).description("높이"),
                                        fieldWithPath("data.content[].imageList[].fileSize").type(JsonFieldType.NUMBER).description("파일크기"),
                                        fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 객체"),
                                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬정보"),
                                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬조건이 없으면 true"),
                                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 됬는지 여부"),
                                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안됬는지 여부"),
                                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("offset"),
                                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징이 적용된 요청인지 여부"),
                                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징이 적용안된 요청인지 여부"),
                                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("현재 페이지가 마지막 페이지인지 여부"),
                                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수 (totalElements / pageSize)"),
                                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
                                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("현재 페이지가 첫 페이지인지 여부"),
                                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬정보"),
                                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬조건이 없으면 true"),
                                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 됬는지 여부"),
                                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 안됬는지 여부"),
                                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("현재 페이지에 포함된 데이터 수"),
                                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("현재 페이지가 비어있는지 여부")
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

    private ProductPriceRequest createPriceRegisterInfo() {
        return ProductPriceRequest.builder()
                .applyStartDate(LocalDate.now())
                .applyEndDate(LocalDate.of(9999, 12, 31))
                .salesAmount(BigDecimal.valueOf(75000.0))
                .purchaseAmount(BigDecimal.valueOf(70000.0))
                .discountType(DiscountType.NONE)
                .discountAmount(BigDecimal.ZERO)
                .discount(0L)
                .build();
    }

    private List<ProductImageRequest> createImageRegisterInfo() {
        return List.of(
                ProductImageRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0001.png")
                        .originalImageFileName("TEST_001A_BLACK.png")
                        .imageFileName("TEST_001A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_001A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0002.png")
                        .originalImageFileName("TEST_002A_BLACK.png")
                        .imageFileName("TEST_002A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_002A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0003.png")
                        .originalImageFileName("TEST_003A_BLACK.png")
                        .imageFileName("TEST_003A_BLACK.png")
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

    private ProductPriceRequest createInvalidPriceRegisterInfo() {
        return ProductPriceRequest.builder()
                .applyStartDate(LocalDate.now())
                .applyEndDate(LocalDate.of(9999, 12, 31))
                .salesAmount(BigDecimal.valueOf(-1))
                .purchaseAmount(BigDecimal.valueOf(-1))
                .discountType(DiscountType.NONE)
                .discountAmount(BigDecimal.valueOf(-1))
                .discount(0L)
                .build();
    }

    private List<ProductImageRequest> createInvalidImageRegisterInfo() {
        return List.of(
                ProductImageRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0001.png")
                        .originalImageFileName("TEST_001A_BLACK.png")
                        .imageFileName("TEST_001A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_001A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0002.png")
                        .originalImageFileName("TEST_002A_BLACK.png")
                        .imageFileName("TEST_002A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_002A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageRequest.builder()
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0003.png")
                        .originalImageFileName("TEST_003A_BLACK.png")
                        .imageFileName("TEST_003A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_003A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build()
        );

    }

    private ProductGetInfo createProductGetInfo() {
        return ProductGetInfo.builder()
                .productId(0L)
                .productDetailId(0L)
                .productCode("0000000000")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_블랙")
                .productEnglishName("TEST_001A_BLACK")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_블랙 상품입니다.")
                .detailDescription("테스트 상품입니다. 테스트 상품입니다. 테스트 상품입니다.")
                .manufacturerCountry("Republic of Korea")
                .releaseDatetime(LocalDateTime.now())
                .priceList(createPriceGetInfo())
                .imageList(createImageGetInfo())
                .build();
    }

    private List<ProductPriceResponse> createPriceGetInfo() {
        return List.of(
                ProductPriceResponse.builder()
                        .priceId(0L)
                        .applyStartDate(LocalDate.now())
                        .applyEndDate(LocalDate.of(9999, 12, 31))
                        .salesAmount(BigDecimal.valueOf(75000.0))
                        .purchaseAmount(BigDecimal.valueOf(70000.0))
                        .discountType(DiscountType.NONE)
                        .discountAmount(BigDecimal.ZERO)
                        .discount(0L)
                        .build()
        );
    }

    private List<ProductImageResponse> createImageGetInfo() {
        return List.of(
                ProductImageResponse.builder()
                        .imageId(0L)
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0001.png")
                        .originalImageFileName("TEST_001A_BLACK.png")
                        .imageFileName("TEST_001A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_001A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageResponse.builder()
                        .imageId(1L)
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0002.png")
                        .originalImageFileName("TEST_002A_BLACK.png")
                        .imageFileName("TEST_002A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_002A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageResponse.builder()
                        .imageId(2L)
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0003.png")
                        .originalImageFileName("TEST_003A_BLACK.png")
                        .imageFileName("TEST_003A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_003A_BLACK.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build()
        );

    }

    private ProductGetInfo createProductGetInfo2() {
        return ProductGetInfo.builder()
                .productId(1L)
                .productDetailId(1L)
                .productCode("0000000001")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_화이트")
                .productEnglishName("TEST_001A_WHITE")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_화이트 상품입니다.")
                .detailDescription("테스트 상품입니다. 테스트 상품입니다. 테스트 상품입니다.")
                .manufacturerCountry("Republic of Korea")
                .releaseDatetime(LocalDateTime.now())
                .priceList(createPriceGetInfo2())
                .imageList(createImageGetInfo2())
                .build();
    }

    private List<ProductPriceResponse> createPriceGetInfo2() {
        return List.of(
                ProductPriceResponse.builder()
                        .priceId(1L)
                        .applyStartDate(LocalDate.now())
                        .applyEndDate(LocalDate.of(9999, 12, 31))
                        .salesAmount(BigDecimal.valueOf(75000.0))
                        .purchaseAmount(BigDecimal.valueOf(70000.0))
                        .discountType(DiscountType.NONE)
                        .discountAmount(BigDecimal.ZERO)
                        .discount(0L)
                        .build()
        );
    }

    private List<ProductImageResponse> createImageGetInfo2() {
        return List.of(
                ProductImageResponse.builder()
                        .imageId(3L)
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0004.png")
                        .originalImageFileName("TEST_001A_BLACK.png")
                        .imageFileName("TEST_001A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_001A_WHITE.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageResponse.builder()
                        .imageId(4L)
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0005.png")
                        .originalImageFileName("TEST_002A_BLACK.png")
                        .imageFileName("TEST_002A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_002A_WHITE.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build(),
                ProductImageResponse.builder()
                        .imageId(5L)
                        .adminImageFileName("TEST_0123_4567_89ab_cdef_0000_0000_0000_0006.png")
                        .originalImageFileName("TEST_003A_BLACK.png")
                        .imageFileName("TEST_003A_BLACK.png")
                        .imageFileUrl("/static/images/image_1/TEST_003A_WHITE.png")
                        .imageType(ImageType.FILE)
                        .contentType(ContentType.IMAGE)
                        .width(500)
                        .height(500)
                        .fileSize(1048576)
                        .build()
        );

    }

}