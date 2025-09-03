package com.aebong.store.controller.view;

import com.aebong.store.controller.req.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Controller
public class UserViewController {

    @GetMapping("/sign-up")
    public String getRegisterForm(Model model) {
        model.addAttribute("userRegisterRequest", new UserRegisterRequest());
        return "/sign-up";
    }

}
