package com.aebong.store.service.product;

import com.aebong.store.common.enums.CustomErrorType;
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
import com.aebong.store.service.product.dto.ProductModifyInfo;
import com.aebong.store.service.product.dto.ProductRegisterInfo;
import com.aebong.store.service.product.validator.ProductValidator;
import com.aebong.store.controller.req.ProductRegisterRequest;
import com.aebong.store.controller.req.ProductModifyRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final ImageRepository imageRepository;
    private final PriceRepository priceRepository;
    private final ProductValidator productValidator;

    @Transactional
    @Override
    public void registerProduct(ProductRegisterRequest registerRequest) {

        // check valid
        productValidator.validateForRegister(registerRequest);
        validateDuplicateForRegister(registerRequest);

        // todo: check valid image format

        // request -> dto mapping
        ProductRegisterInfo registerInfo = ProductRegisterInfo.to(registerRequest);

        // entity save
        ProductEntity product = ProductEntity.create(registerInfo);
        ProductDetailEntity productDetail = ProductDetailEntity.create(product, registerInfo);
        PriceEntity price = PriceEntity.create(product, registerInfo);
        List<ImageEntity> imageList = ImageEntity.create(product, registerInfo);

        productRepository.save(Objects.requireNonNull(product));
        productDetailRepository.save(Objects.requireNonNull(productDetail));
        priceRepository.save(Objects.requireNonNull(price));
        imageRepository.saveAll(Objects.requireNonNull(imageList));

    }

    @Override
    public ProductGetInfo getProduct(Long productId) {

        // check bad request
        if (Objects.isNull(productId)) {
            throw new ProductApplicationException(CustomErrorType.BAD_REQUEST, "productId must not be null");
        }

        return ProductGetInfo.to(findByProductId(productId));

    }

    @Override
    public Page<ProductGetInfo> getProducts(Pageable pageable) {
        return productRepository.findAllProducts(pageable);
    }

    @Transactional
    @Override
    public void modifyProduct(Long productId, ProductModifyRequest modifyRequest) {

        // check valid
        ProductEntity product = findByProductId(productId);
        productValidator.validateForModify(modifyRequest);
        validateDuplicateForModify(product, modifyRequest);

        // request -> dto mapping
        ProductModifyInfo modifyInfo = ProductModifyInfo.to(modifyRequest);

        ProductDetailEntity productDetail = product.getProductDetail();
        PriceEntity price = product.getPrices().stream()
                .filter(p -> p.getId().equals(modifyInfo.getPrice().getPriceId()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        List<ImageEntity> images = product.getImages();

        // todo: check valid image format

        // entity save
        product.modify(modifyInfo);
        productDetail.modify(modifyInfo);
        price.modify(modifyInfo);
        images.forEach(p -> p.modify(modifyInfo));

        imageRepository.saveAll(Objects.requireNonNull(images));
        priceRepository.save(Objects.requireNonNull(price));
        productDetailRepository.save(productDetail);
        productRepository.save(product);

    }

    private ProductEntity findByProductId(Long productId) {
        return productRepository.findByProductId(productId).orElseThrow(
                () -> new ProductApplicationException(CustomErrorType.NOT_FOUND_PRODUCT, CustomErrorType.NOT_FOUND_PRODUCT.getMessage()));
    }

    private void validateDuplicateForRegister(ProductRegisterRequest registerRequest) {
        if (productRepository.existsByProductCode(registerRequest.getProductCode())) {
            throw new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_CODE,
                    String.format("%s already exists productCode", registerRequest.getProductCode()));
        }

        if (productDetailRepository.existsByProductName(registerRequest.getProductName())) {
            throw new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_NAME,
                    String.format("%s already exists productName", registerRequest.getProductName()));
        }
    }

    private void validateDuplicateForModify(ProductEntity product, ProductModifyRequest modifyRequest) {
        if (!Objects.equals(product.getProductCode(), modifyRequest.getProductCode())
                && productRepository.existsByProductCode(modifyRequest.getProductCode())) {
            throw new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_CODE,
                    String.format("%s already exists productCode", modifyRequest.getProductCode()));
        }

        if (!Objects.equals(product.getProductDetail().getProductName(), modifyRequest.getProductName())
                && productDetailRepository.existsByProductName(modifyRequest.getProductName())) {
            throw new ProductApplicationException(CustomErrorType.IS_EXIST_PRODUCT_NAME,
                    String.format("%s already exists productName", modifyRequest.getProductName()));
        }
    }

}
