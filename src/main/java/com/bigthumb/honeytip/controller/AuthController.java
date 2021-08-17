package com.bigthumb.honeytip.controller;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.dto.UserModificationDto;
import com.bigthumb.honeytip.exception.InvalidLoginCredentialException;
import com.bigthumb.honeytip.repository.UserRepository;
import com.bigthumb.honeytip.validator.SignupValidator;

import com.bigthumb.honeytip.dto.UserSignupDto;
import com.bigthumb.honeytip.service.UserService;
import com.bigthumb.honeytip.validator.UserInfoModificationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  private final UserRepository userRepository;
  private final UserService userService;
  private final SignupValidator signupValidator;
  private final UserInfoModificationValidator userInfoModificationValidator;

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

  @GetMapping("/mypage")
  public String mypage(@AuthenticationPrincipal String requestUsername, Model model) {
    User user = userRepository.findByUsername(requestUsername);
    model.addAttribute("nickname", user.getNickname());
    return "/auth/mypage";
  }

  @GetMapping("/modification")
  public String modificationForm(@AuthenticationPrincipal String requestUsername, Model model) {
    User user = userRepository.findByUsername(requestUsername);
    UserModificationDto modificationForm = new UserModificationDto();
    modificationForm.setNickname(user.getNickname());
    model.addAttribute("modificationForm", modificationForm);
    model.addAttribute("username", user.getUsername());
    return "/auth/modification";
  }

  @PostMapping("/modificationProcess")
  public String modificationProcess(@AuthenticationPrincipal String requestUsername,
      @ModelAttribute("modificationForm") UserModificationDto modificationForm, BindingResult result) {
    userInfoModificationValidator.validateWithRequestUser(modificationForm, result, requestUsername);
    if (result.hasErrors()) {
      return "/auth/modification";
    }
    userService.modifyUserInfo(requestUsername, modificationForm);
    return "redirect:/auth/mypage";
  }
}
