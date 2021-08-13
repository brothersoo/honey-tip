package com.bigthumb.honeytip.validator;

import com.bigthumb.honeytip.dto.UserSignupDto;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignupValidator implements Validator {

  private final UserRepository userRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return UserSignupDto.class.equals(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    UserSignupDto user = (UserSignupDto) obj;
    if (user.getUsername().length() < 3 || user.getUsername().length() > 30) {
      errors.rejectValue("username", "key", "3자 이상 30자 미만이어야 합니다");
    } else if (userRepository.findByUsername(user.getUsername()) != null) {
      errors.rejectValue("username", "key", "이미 존재하는 아이디입니다");
    }

    if (user.getPassword().length() < 8 || user.getPassword().length() > 23) {
      errors.rejectValue("password", "key", "8자 이상 23자 이하이어야 합니다.");
    } else if (!Objects.equals(user.getPassword(), user.getPasswordConfirm())) {
      errors.rejectValue("passwordConfirm", "key", "다시한번 확인해주세요");
    }

    if (user.getNickname().length() < 2 || user.getNickname().length() > 16) {
      errors.rejectValue("nickname", "key", "2자 이상 16자 이하이어야 합니다.");
    } else if (userRepository.findByNickname(user.getNickname()) != null) {
      errors.rejectValue("nickname", "key", "이미 존재하는 닉네임입니다.");
    }
  }
}
