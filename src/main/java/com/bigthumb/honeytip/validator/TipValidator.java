package com.bigthumb.honeytip.validator;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserStatus;
import com.bigthumb.honeytip.dto.TipDto;
import com.bigthumb.honeytip.dto.UserSignupDto;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TipValidator implements Validator {

  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return TipDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
  }

  public void validateWriter(User writer, Tip tip) {
    if (writer.getStatus().equals(UserStatus.BANNED)) {
      throw new IllegalArgumentException("신고누적으로 적성할 수 없습니다");
    } else {
      tip.setWriter(writer);
      writer.getTips().add(tip);
    }
  }

  public void validateCategory(Category category, Tip tip) {
    if (category == null) {
      throw new IllegalArgumentException("존재하지 않는 카테고리입니다");
    } else {
      tip.setCategory(category);
      category.getTips().add(tip);
    }
  }
}
