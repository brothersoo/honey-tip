package com.bigthumb.honeytip.validator;

import com.bigthumb.honeytip.dto.UserModificationDto;
import com.bigthumb.honeytip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserInfoModificationValidator implements Validator {

  private final UserRepository userRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return UserModificationDto.class.equals(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
  }

  public void validateWithRequestUser(Object obj, Errors errors, String requestUserNickname) {
    final String nickname = "nickname";
    final String password = "password";
    final String passwordConfirm = "passwordConfirm";

    UserModificationDto form = (UserModificationDto) obj;

    boolean passwordEmpty = form.getPassword().trim().isEmpty();
    boolean nicknameEmpty = form.getNickname().trim().isEmpty();
    boolean nicknameChanged = (!nicknameEmpty && !form.getNickname().equals(requestUserNickname));
    boolean nicknameMaintain = (form.getNickname().equals(requestUserNickname));

    if (form.isPasswordMaintain()) {  // 비밀번호 유지를 원하는 경우
      form.setPassword(null);
      form.setPasswordConfirm(null);
      if (nicknameMaintain) {  // 모두 변경사항 없는 경우
        errors.rejectValue(nickname, "key", "변경할 닉네임을 입력해주세요");
      }
    } else if (passwordEmpty) {  // 유지하지 않는데 새로운 비밀번호 입력이 없는 경우
      errors.rejectValue(password, "key", "변경할 비밀번호를 입력해주세요");
    } else { // 새로운 비밀번호 입력이 있을 경우
      if (form.getPassword().length() < 8 || form.getPassword().length() > 23) {
        errors.rejectValue(password, "key", "8자 이상 23자 이하이어야 합니다");
      }
      if (!form.getPasswordConfirm().equals(form.getPassword())) {
        errors.rejectValue(passwordConfirm, "key", "다시 한번 확인해주세요");
      }
    }

    if (nicknameChanged) {
      if (form.getNickname().length() < 2 || form.getNickname().length() > 16) {
        errors.rejectValue(nickname, "key", "2자 이상 16자 이하이어야 합니다");
      }
    } else if (nicknameEmpty) {
      errors.rejectValue(nickname, "key", "변경할 닉네임을 입력해주세요");
    }
  }
}
