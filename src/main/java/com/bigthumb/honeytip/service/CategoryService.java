package com.bigthumb.honeytip.service;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  public Long add(Category category, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADMIN)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    checkDuplicateName(category.getName());
    category.validateInfo();
    categoryRepository.save(category);
    return category.getId();
  }

  @Transactional(readOnly = true)
  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public void deleteCategory(Long categoryId, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADMIN)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    Category deletableCategory = categoryRepository.findById(categoryId);
    categoryRepository.delete(deletableCategory);
  }

  private void checkDuplicateName(String name) {
    if (categoryRepository.findByName(name) != null) {
      throw new IllegalArgumentException("Already existing category name.");
    }
  }
}
