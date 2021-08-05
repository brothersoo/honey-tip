package com.bigthumb.honeytip.service;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public Long join(User user) {
    checkDuplicateEmail(user.getEmail());
    user.validateInfo();
    user.encryptPassword();
    userRepository.save(user);
    return user.getId();
  }

  public List<User> findAllUser(Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADM)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    return userRepository.findAll();
  }

  public User searchUserById(Long userId) {
    return userRepository.findById(userId);
  }

  public List<User> searchUserByNickname(String nickname) {
    return userRepository.findByNickName(nickname);
  }

  public void dropOut(Long requestUserId) {
    User user = userRepository.findById(requestUserId);
    userRepository.remove(user);
  }

  public void ban(Long userId, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADM)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    User banableUser = userRepository.findById(userId);
    userRepository.ban(banableUser);
  }

  public void delete(Long userId, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADM)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    User deletableUser = userRepository.findById(userId);
    userRepository.remove(deletableUser);
  }

  private void checkDuplicateEmail(String email) {
    if (userRepository.findByEmail(email) != null) {
      throw new IllegalStateException("Already existing email");
    }
  }
}