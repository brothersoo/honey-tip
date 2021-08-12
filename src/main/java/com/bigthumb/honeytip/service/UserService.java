package com.bigthumb.honeytip.service;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

  private final UserRepository userRepository;

  public Long join(User user) {
    checkDuplicateUsername(user.getUsername());
    user.validateInfo();
    user.encryptPassword();
    userRepository.save(user);
    return user.getId();
  }

  @Transactional(readOnly = true)
  public List<User> findAllUser(Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADMIN)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<User> findAllMember(Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADMIN)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    return userRepository.findAllMember();
  }

  @Transactional(readOnly = true)
  public User searchUserById(Long userId) {
    return userRepository.findById(userId);
  }

  @Transactional(readOnly = true)
  public List<User> searchByNickname(String nickname, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADMIN)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    return userRepository.findByNickname(nickname);
  }

  @Transactional(readOnly = true)
  public List<User> searchMemberByNickname(String nickname) {
    return userRepository.findMemberByNickname(nickname);
  }

  public void dropOut(Long requestUserId) {
    User user = userRepository.findById(requestUserId);
    userRepository.remove(user);
  }

  public void ban(Long userId, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADMIN)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    User banableUser = userRepository.findById(userId);
    userRepository.ban(banableUser);
  }

  public void delete(Long userId, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADMIN)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    User deletableUser = userRepository.findById(userId);
    userRepository.remove(deletableUser);
  }

  private void checkDuplicateUsername(String username) {
    if (userRepository.findByUsername(username) != null) {
      throw new IllegalStateException("Already existing email");
    }
  }
}