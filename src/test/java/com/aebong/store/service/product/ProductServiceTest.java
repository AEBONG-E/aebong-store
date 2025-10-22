package com.aebong.store.service.product;

import com.aebong.store.common.enums.CustomErrorType;
import com.aebong.store.common.enums.product.ContentType;
import com.aebong.store.common.enums.product.DiscountType;
import com.aebong.store.common.enums.product.ImageType;
import com.aebong.store.common.enums.product.ProductType;
import com.aebong.store.common.exceptions.ProductApplicationException;
import com.aebong.store.domain.entity.product.ImageEntity;
import com.aebong.store.domain.entity.product.PriceEntity;
import com.aebong.store.domain.entity.product.ProductDetailEntity;
import com.aebong.store.domain.entity.product.ProductEntity;
import com.aebong.store.domain.repository.product.ImageRepository;
import com.aebong.store.domain.repository.product.PriceRepository;
import com.aebong.store.domain.repository.product.ProductDetailRepository;
import com.aebong.store.domain.repository.product.ProductRepository;
import com.aebong.store.service.product.dto.ProductGetInfo;
import com.aebong.store.service.product.dto.ProductRegisterInfo;
import com.aebong.store.service.product.dto.ProductRegisterRequest;
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
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(request);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

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
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(request);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

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
    void 상품등록_실패_유효하지않은_금액이_등록된_케이스_상품판매금액() {

        // given
        ProductRegisterRequest request = createInvalidProductRegisterInfo();
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(request);
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

        // when
        assertThatThrownBy(() -> service.registerProduct(request))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(registerInfo.getSalesAmount() + " this amount is invalid");

        assertThatThrownBy(() -> service.registerProduct(request))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(registerInfo.getPurchaseAmount() + " this amount is invalid");

        assertThatThrownBy(() -> service.registerProduct(request))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(registerInfo.getDiscountAmount() + " this amount is invalid");

        // then
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
    void 상품조회_실패_상품순번_불일치_케이스() {

        // given
        ProductGetInfo productGetInfo = createProductGetInfo();

        Long productId = 1L;

        given(productRepository.findByproductId(productId)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.getProduct(productId))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(CustomErrorType.NOT_FOUND_PRODUCT.getMessage());

        // then
        then(productRepository).should(times(1)).findByproductId(productId);
        assertThat(productId).isNotEqualTo(productGetInfo.getProductId());

    }

    @Test
    void 상품상세조회_정상_케이스() {

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
    void 상품목록조회_정상_케이스() {

        // given
        ProductGetInfo productGetInfo = createProductGetInfo();

        Long productId = 1L;

        given(productRepository.findByproductId(productId)).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> service.getProduct(productId))
                .isInstanceOf(ProductApplicationException.class)
                .hasMessage(CustomErrorType.NOT_FOUND_PRODUCT.getMessage());

        // then
        then(productRepository).should(times(1)).findByproductId(productId);
        assertThat(productId).isNotEqualTo(productGetInfo.getProductId());

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
                        .imageFileName("TEST_001A_BLACK.png")
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
                        .imageFileName("TEST_002A_BLACK.png")
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
                        .imageFileName("TEST_001A_BLACK.png")
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
                        .imageFileName("TEST_002A_BLACK.png")
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

    private List<ProductGetInfo.PriceListGetInfo> createPriceGetInfo() {
        return List.of(
                ProductGetInfo.PriceListGetInfo.builder()
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

    private List<ProductGetInfo.ImageListGetInfo> createImageGetInfo() {
        return List.of(
                ProductGetInfo.ImageListGetInfo.builder()
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
                ProductGetInfo.ImageListGetInfo.builder()
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
                ProductGetInfo.ImageListGetInfo.builder()
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
                .priceList(createPriceGetInfo())
                .imageList(createImageGetInfo())
                .build();
    }

    private List<ProductGetInfo.PriceListGetInfo> createPriceGetInfo2() {
        return List.of(
                ProductGetInfo.PriceListGetInfo.builder()
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

    private List<ProductGetInfo.ImageListGetInfo> createImageGetInfo2() {
        return List.of(
                ProductGetInfo.ImageListGetInfo.builder()
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
                ProductGetInfo.ImageListGetInfo.builder()
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
                ProductGetInfo.ImageListGetInfo.builder()
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