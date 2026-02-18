package com.aebong.store.service.product;

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
import com.aebong.store.controller.res.ProductImageResponse;
import com.aebong.store.controller.res.ProductPriceResponse;
import com.aebong.store.domain.entity.product.ImageEntity;
import com.aebong.store.domain.entity.product.PriceEntity;
import com.aebong.store.domain.entity.product.ProductDetailEntity;
import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.domain.repository.product.ImageRepository;
import com.aebong.store.domain.repository.product.PriceRepository;
import com.aebong.store.domain.repository.product.ProductDetailRepository;
import com.aebong.store.domain.repository.product.ProductRepository;
import com.aebong.store.service.product.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired private ProductService service;
    @MockBean private ProductRepository productRepository;
    @MockBean private ProductDetailRepository productDetailRepository;
    @MockBean private PriceRepository priceRepository;
    @MockBean private ImageRepository imageRepository;


    @Test
    void 상품등록_실패_중복된_productCode가_들어온_케이스() {

        // given
        ProductRegisterRequest request = createProductRegisterInfo();

        String validateProductCode = "0000000000";

        given(productRepository.existsByProductCode(validateProductCode)).willReturn(true);

        // when
        assertThatThrownBy(() -> service.registerProduct(request))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(validateProductCode + " already exists productCode");

        // then
        then(productRepository).should(times(1)).existsByProductCode(validateProductCode);
        then(productRepository).should(never()).save(any());
        then(productDetailRepository).shouldHaveNoInteractions();
        then(imageRepository).shouldHaveNoInteractions();

    }

    @Test
    void 상품등록_실패_중복된_productName이_들어온_케이스() {

        // given
        ProductRegisterRequest request = createProductRegisterInfo();

        String validateProductName = "TEST_001A_블랙";

        given(productDetailRepository.existsByProductName(validateProductName)).willReturn(true);

        // when
        assertThatThrownBy(() -> service.registerProduct(request))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(validateProductName + " already exists productName");

        // then
        then(productRepository).should(times(1)).existsByProductCode(any());
        then(productDetailRepository).should(times(1)).existsByProductName(validateProductName);
        then(productRepository).should(never()).save(any());
        then(productDetailRepository).should(never()).save(any());
        then(imageRepository).shouldHaveNoInteractions();

    }

    @Test
    void 상품등록_정상_케이스() {

        // given
        ProductRegisterRequest request = createProductRegisterInfo();
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(request);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        PriceEntity price = PriceEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

        given(productRepository.existsByProductCode(registerInfo.getProductCode())).willReturn(false);
        given(productDetailRepository.existsByProductName(registerInfo.getProductName())).willReturn(false);
        given(productRepository.save(any(ProductEntity.class))).willReturn(product);
        given(productDetailRepository.save(any(ProductDetailEntity.class))).willReturn(productDetail);
        given(priceRepository.save(any(PriceEntity.class))).willReturn(price);
        given(imageRepository.saveAll(anyList())).willReturn(imageList);

        // when
        service.registerProduct(request);

        // then
        then(productRepository).should(times(1)).existsByProductCode(registerInfo.getProductCode());
        then(productDetailRepository).should(times(1)).existsByProductName(registerInfo.getProductName());
        then(productRepository).should(times(1)).save(any(ProductEntity.class));
        then(productDetailRepository).should(times(1)).save(any(ProductDetailEntity.class));
        then(priceRepository).should(times(1)).save(any(PriceEntity.class));
        then(imageRepository).should(times(1)).saveAll(any(List.class));

        // 추가 값 검증
        assertThat(product.getProductCode()).isEqualTo("0000000000");
        assertThat(price.getSalesAmount()).isEqualByComparingTo(BigDecimal.valueOf(75000.0));
        assertThat(imageList).hasSize(3);
        assertThat(imageList.get(0).getOriginalImageFileName()).isEqualTo("TEST_001A_BLACK.png");

    }

    @Test
    void 상품상세조회_실패_상품순번_불일치_케이스() {

        // given
        ProductGetInfo productGetInfo = createProductGetInfo();

        Long productId = 1L;

        given(productRepository.findByProductId(productId)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.getProduct(productId))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(CustomErrorType.NOT_FOUND_PRODUCT.getMessage());

        // then
        then(productRepository).should(times(1)).findByProductId(productId);
        assertThat(productId).isNotEqualTo(productGetInfo.getProductId());

    }

    @Test
    void 상품상세조회_정상_케이스() {

        // given
        ProductGetInfo productGetInfo = createProductGetInfo();

        Long productId = 1L;

        given(productRepository.findByProductId(productId)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.getProduct(productId))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(CustomErrorType.NOT_FOUND_PRODUCT.getMessage());

        // then
        then(productRepository).should(times(1)).findByProductId(productId);
        assertThat(productId).isNotEqualTo(productGetInfo.getProductId());

    }

    @Test
    void 상품목록조회_정상_케이스() {

        // given
        List<ProductGetInfo> productGetInfoList = List.of(createProductGetInfo(), createProductGetInfo2());
        Pageable pageable = PageRequest.of(0,20);
        Page<ProductGetInfo> productGetInfos = new PageImpl<>(productGetInfoList, pageable, productGetInfoList.size());

        given(productRepository.findAllProducts(pageable)).willReturn(productGetInfos);

        // when
        Page<ProductGetInfo> products = service.getProducts(pageable);

        // then
        then(productRepository).should().findAllProducts(pageable);

        assertThat(products).isNotEmpty();

    }

    @Test
    void 상품수정_실패_중복된_productCode가_들어온_케이스() {

        // given
        Long productId = 0L;
        ProductModifyRequest request = createProductModifyInfo();
        String validateProductCode = "0000000000";

        ProductEntity product = mock(ProductEntity.class);
        ProductDetailEntity productDetail = mock(ProductDetailEntity.class);
        PriceEntity price = mock(PriceEntity.class);
        ImageEntity image = mock(ImageEntity.class);

        given(productRepository.findByProductId(productId)).willReturn(Optional.of(product));
        given(product.getProductDetail()).willReturn(productDetail);
        given(product.getPrices()).willReturn(List.of(price));
        given(product.getImages()).willReturn(List.of(image));
        given(price.getId()).willReturn(0L);
        given(productRepository.existsByProductCode(validateProductCode)).willReturn(true);

        // when
        assertThatThrownBy(() -> service.modifyProduct(productId, request))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(validateProductCode + " already exists productCode");

        // then
        then(productRepository).should(times(1)).findByProductId(productId);
        then(productRepository).should(times(1)).existsByProductCode(validateProductCode);
        then(productDetailRepository).should(never()).existsByProductName(any());
        then(priceRepository).should(never()).save(any());
        then(productRepository).should(never()).save(any());
        then(productDetailRepository).should(never()).save(any());
        then(imageRepository).should(never()).saveAll(any());

    }

    @Test
    void 상품수정_정상_자기자신_productCode_productName_유지_케이스() {

        // given
        Long productId = 0L;
        ProductModifyRequest request = createProductModifyInfo();

        ProductEntity product = mock(ProductEntity.class);
        ProductDetailEntity productDetail = mock(ProductDetailEntity.class);
        PriceEntity price = mock(PriceEntity.class);
        ImageEntity image = mock(ImageEntity.class);
        List<ImageEntity> imageList = List.of(image);

        given(productRepository.findByProductId(productId)).willReturn(Optional.of(product));
        given(product.getProductDetail()).willReturn(productDetail);
        given(product.getPrices()).willReturn(List.of(price));
        given(product.getImages()).willReturn(imageList);
        given(product.getProductCode()).willReturn(request.getProductCode());
        given(productDetail.getProductName()).willReturn(request.getProductName());
        given(price.getId()).willReturn(0L);
        given(imageRepository.saveAll(anyList())).willReturn(imageList);
        given(priceRepository.save(any(PriceEntity.class))).willReturn(price);
        given(productDetailRepository.save(any(ProductDetailEntity.class))).willReturn(productDetail);
        given(productRepository.save(any(ProductEntity.class))).willReturn(product);

        // when
        service.modifyProduct(productId, request);

        // then
        then(productRepository).should(times(1)).findByProductId(productId);
        then(productRepository).should(never()).existsByProductCode(any());
        then(productDetailRepository).should(never()).existsByProductName(any());
        then(product).should(times(1)).modify(any(ProductModifyInfo.class));
        then(productDetail).should(times(1)).modify(any(ProductModifyInfo.class));
        then(price).should(times(1)).modify(any(ProductModifyInfo.class));
        then(image).should(times(1)).modify(any(ProductModifyInfo.class));
        then(imageRepository).should(times(1)).saveAll(anyList());
        then(priceRepository).should(times(1)).save(any(PriceEntity.class));
        then(productDetailRepository).should(times(1)).save(any(ProductDetailEntity.class));
        then(productRepository).should(times(1)).save(any(ProductEntity.class));

    }

    @Test
    void 상품수정_실패_상품이_없는_케이스() {

        // given
        Long productId = 999L;
        ProductModifyRequest request = createProductModifyInfo();

        given(productRepository.findByProductId(productId)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.modifyProduct(productId, request))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(CustomErrorType.NOT_FOUND_PRODUCT.getMessage());

        // then
        then(productRepository).should(times(1)).findByProductId(productId);
        then(productRepository).should(never()).existsByProductCode(any());
        then(productDetailRepository).should(never()).existsByProductName(any());
        then(productRepository).should(never()).save(any());
        then(productDetailRepository).should(never()).save(any());
        then(priceRepository).should(never()).save(any());
        then(imageRepository).should(never()).saveAll(any());

    }

    @Test
    void 상품수정_실패_요청_가격순번과_상품가격목록이_불일치한_케이스() {

        // given
        Long productId = 0L;
        ProductModifyRequest request = createProductModifyInfo();

        ProductEntity product = mock(ProductEntity.class);
        ProductDetailEntity productDetail = mock(ProductDetailEntity.class);
        PriceEntity price = mock(PriceEntity.class);

        given(productRepository.findByProductId(productId)).willReturn(Optional.of(product));
        given(product.getProductDetail()).willReturn(productDetail);
        given(product.getProductCode()).willReturn(request.getProductCode());
        given(productDetail.getProductName()).willReturn(request.getProductName());
        given(product.getPrices()).willReturn(List.of(price));
        given(product.getImages()).willReturn(List.of());
        given(price.getId()).willReturn(999L);

        // when
        assertThatThrownBy(() -> service.modifyProduct(productId, request))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(productRepository).should(times(1)).findByProductId(productId);
        then(productRepository).should(never()).existsByProductCode(any());
        then(productDetailRepository).should(never()).existsByProductName(any());
        then(productRepository).should(never()).save(any());
        then(productDetailRepository).should(never()).save(any());
        then(priceRepository).should(never()).save(any());
        then(imageRepository).should(never()).saveAll(any());

    }

    @Test
    void 상품수정_정상_케이스() {

        // given
        Long productId = 0L;
        ProductModifyRequest request = createProductModifyInfo();

        ProductEntity product = mock(ProductEntity.class);
        ProductDetailEntity productDetail = mock(ProductDetailEntity.class);
        PriceEntity price = mock(PriceEntity.class);
        ImageEntity image = mock(ImageEntity.class);
        List<ImageEntity> imageList = List.of(image);

        given(productRepository.findByProductId(productId)).willReturn(Optional.of(product));
        given(product.getProductDetail()).willReturn(productDetail);
        given(product.getPrices()).willReturn(List.of(price));
        given(product.getImages()).willReturn(imageList);
        given(price.getId()).willReturn(0L);
        given(productRepository.existsByProductCode(request.getProductCode())).willReturn(false);
        given(productDetailRepository.existsByProductName(request.getProductName())).willReturn(false);
        given(imageRepository.saveAll(anyList())).willReturn(imageList);
        given(priceRepository.save(any(PriceEntity.class))).willReturn(price);
        given(productDetailRepository.save(any(ProductDetailEntity.class))).willReturn(productDetail);
        given(productRepository.save(any(ProductEntity.class))).willReturn(product);

        // when
        service.modifyProduct(productId, request);

        // then
        then(productRepository).should(times(1)).findByProductId(productId);
        then(productRepository).should(times(1)).existsByProductCode(request.getProductCode());
        then(productDetailRepository).should(times(1)).existsByProductName(request.getProductName());
        then(product).should(times(1)).modify(any(ProductModifyInfo.class));
        then(productDetail).should(times(1)).modify(any(ProductModifyInfo.class));
        then(price).should(times(1)).modify(any(ProductModifyInfo.class));
        then(image).should(times(1)).modify(any(ProductModifyInfo.class));
        then(imageRepository).should(times(1)).saveAll(anyList());
        then(priceRepository).should(times(1)).save(any(PriceEntity.class));
        then(productDetailRepository).should(times(1)).save(any(ProductDetailEntity.class));
        then(productRepository).should(times(1)).save(any(ProductEntity.class));

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
                .manufacturerCountry("대한민국")
                .releaseDatetime(LocalDateTime.now())
                .price(createPriceRegisterInfo())
                .imageList(createImageRegisterInfo())
                .build();
    }

    private ProductModifyRequest createProductModifyInfo() {
        return ProductModifyRequest.builder()
                .productId(0L)
                .productDetailId(0L)
                .productCode("0000000000")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_블랙")
                .productEnglishName("TEST_001A_BLACK")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_블랙 상품입니다.")
                .detailDescription("테스트 상품입니다. 테스트 상품입니다. 테스트 상품입니다.")
                .manufacturerCountry("대한민국")
                .releaseDatetime(LocalDateTime.now())
                .price(createPriceRegisterInfo())
                .imageList(createImageRegisterInfo())
                .build();
    }

    private ProductModifyRequest createInvalidProductModifyInfo() {
        return ProductModifyRequest.builder()
                .productId(0L)
                .productDetailId(0L)
                .productCode("0000000000")
                .productType(ProductType.STANDARD)
                .productName("TEST_001A_블랙")
                .productEnglishName("TEST_001A_BLACK")
                .productShortName("TEST_001A")
                .basicDescription("TEST_001A_블랙 상품입니다.")
                .detailDescription("테스트 상품입니다. 테스트 상품입니다. 테스트 상품입니다.")
                .manufacturerCountry("대한민국")
                .releaseDatetime(LocalDateTime.now())
                .price(createInvalidPriceModifyInfo())
                .imageList(createImageRegisterInfo())
                .build();
    }

    private ProductPriceRequest createPriceRegisterInfo() {
        return ProductPriceRequest.builder()
                .priceId(0L)
                .applyStartDate(LocalDate.now())
                .applyEndDate(LocalDate.of(9999, 12, 31))
                .salesAmount(BigDecimal.valueOf(75000.0))
                .purchaseAmount(BigDecimal.valueOf(70000.0))
                .discountType(DiscountType.NONE)
                .discountAmount(BigDecimal.ZERO)
                .discount(0L)
                .build();
    }

    private ProductPriceRequest createInvalidPriceModifyInfo() {
        return ProductPriceRequest.builder()
                .priceId(0L)
                .applyStartDate(LocalDate.now())
                .applyEndDate(LocalDate.of(9999, 12, 31))
                .salesAmount(BigDecimal.valueOf(-1))
                .purchaseAmount(BigDecimal.valueOf(-1))
                .discountType(DiscountType.NONE)
                .discountAmount(BigDecimal.valueOf(-1))
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
                .manufacturerCountry("대한민국")
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
                .manufacturerCountry("대한민국")
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
                .manufacturerCountry("대한민국")
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
