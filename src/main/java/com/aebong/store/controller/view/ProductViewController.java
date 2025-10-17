package com.aebong.store.controller.view;

import com.aebong.store.service.product.dto.ProductRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/products")
@Controller
public class ProductViewController {

    @GetMapping("/register")
    public String getRegisterForm(Model model) {
        model.addAttribute("registerForm", new ProductRegisterRequest());
        return "products/product-register";
    }

}
