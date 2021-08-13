package com.bigthumb.honeytip.domain;

import com.bigthumb.honeytip.dto.UserSignupDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, insertable = false, updatable = false, columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String password;

  @Enumerated(value = EnumType.STRING)
  private UserType type;

  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Tip> tips;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Report> reports;

  public User(UserSignupDto userRequestDto) {
    this.username = userRequestDto.getUsername();
    this.password = userRequestDto.getPassword();
    this.nickname = userRequestDto.getNickname();
    this.type = UserType.MEMBER;
    this.status = UserStatus.ACTIVATE;
    this.tips = new ArrayList<>();
    this.reports = new ArrayList<>();
  }

  @Builder
  public User(String username, String nickname, String password, UserType type,
      UserStatus status) {
    Assert.notNull(username, "User name should not be null");
    Assert.notNull(nickname, "User nickname should not be null");
    Assert.notNull(password, "User password should not be null");

    if (type == null) {
      type = UserType.MEMBER;
    }
    if (status == null) {
      status = UserStatus.ACTIVATE;
    }

    this.username = username;
    this.nickname = nickname;
    this.password = password;
    this.type = type;
    this.status = status;

    this.tips = new ArrayList<>();
    this.reports = new ArrayList<>();
  }

  public void encryptPassword(String encryptedPassword) {
    this.password = encryptedPassword;
  }

  public void updateStatus(UserStatus status) {
    this.status = status;
  }
}
