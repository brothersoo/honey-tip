package com.bigthumb.honeytip.service;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.TipRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TipService {

  private final TipRepository tipRepository;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  public void createTip(Tip tip) {
    tipRepository.save(tip);
  }

  /**
   * for admin page
   */
  @Transactional(readOnly = true)
  public List<Tip> findAll() {
    return tipRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Tip findById(Long id) {
    return tipRepository.findOne(id);
  }

  /**
   * for user page
   */
  @Transactional(readOnly = true)
  public List<Tip> findByCondition(String keyword) {

    return tipRepository.findByCondition(keyword);
  }

  public Tip updateTip(Long id, Long requestUserId, String title, String content, String categoryName) {
    Tip updatableTip = tipRepository.findOne(id);
    if (!updatableTip.getUser().getId().equals(requestUserId)) {
      throw new IllegalArgumentException("No permission for this request user");
    }
    Category changableCategory = categoryRepository.findByName(categoryName);
    if (changableCategory == null) {
      throw new IllegalArgumentException("No category name matching");
    }
    updatableTip.updateInfo(title, content, changableCategory);
    return updatableTip;
  }

  /**
   * self removal
   */
  public void removeById(Long tipId, Long requestUserId) {
    Tip removableTip = tipRepository.findOne(tipId);
    if (!removableTip.getUser().getId().equals(requestUserId)) {
      throw new IllegalArgumentException("No permission for this request user");
    }
    tipRepository.remove(removableTip);
  }

  /**
   * for admin page
   */
  public void deleteById(Long tipId, Long requestUserId) {
    Tip deletableTip = tipRepository.findOne(tipId);
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADM)) {
      throw new IllegalArgumentException("This user has no permission"); // TODO: modify HTTP response status
    }
    tipRepository.delete(deletableTip);
  }
}