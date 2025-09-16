package com.aebong.store.controller.view;

import com.aebong.store.controller.req.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserViewController {

    @GetMapping("/sign-up")
    public String getRegisterForm(Model model) {
        model.addAttribute("registerForm", new UserRegisterRequest());
        return "users/user-register";
    }

    @GetMapping("/info")
    public String getUserInfo(@RequestParam String userAccount, Model model) {
        model.addAttribute("userAccount", userAccount);
        return "users/user-get";
    }

}
