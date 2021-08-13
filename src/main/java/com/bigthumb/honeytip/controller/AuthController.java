package com.bigthumb.honeytip.controller;

import com.bigthumb.honeytip.validator.SignupValidator;

import com.bigthumb.honeytip.dto.UserSignupDto;
import com.bigthumb.honeytip.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final SignupValidator signupValidator;

  @GetMapping("/signup")
  public String signupForm(Model model) {
    model.addAttribute("signupForm", new UserSignupDto());
    return "/auth/signup";
  }

  @PostMapping("/signupProcess")
  public String signupProcess(@ModelAttribute("signupForm") UserSignupDto signupForm,
      BindingResult result, Model model) {
    signupValidator.validate(signupForm, result);
    if (result.hasErrors()) {
      return "/auth/signup";
    }
    userService.join(signupForm);
    return "redirect:/auth/login";
  }

  @GetMapping("/login")
  public String loginForm() {
    return "/auth/login";
  }

  @PostMapping("/loginProcess")
  public String loginProcess() {
    return "redirect:/tip/tips";
  }
}
