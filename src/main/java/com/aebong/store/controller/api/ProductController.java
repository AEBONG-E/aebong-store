package com.aebong.store.controller.api;

import com.aebong.store.common.api.ApiResponse;
import com.aebong.store.service.product.ProductService;
import com.aebong.store.service.product.dto.ProductGetInfo;
import com.aebong.store.service.product.dto.ProductRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerProduct(@RequestBody ProductRegisterRequest registerRequest) {
        productService.registerProduct(registerRequest);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductGetInfo>> getProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(ApiResponse.success(productService.getProduct(productId)), HttpStatus.OK);
    }

    @PostMapping("/paging")
    public ResponseEntity<ApiResponse<Page<ProductGetInfo>>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(ApiResponse.success(productService.getProducts(pageable)), HttpStatus.OK);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> modifyProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.OK);
    }

}
