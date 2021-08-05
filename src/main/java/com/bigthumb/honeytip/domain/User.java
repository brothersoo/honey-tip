package com.bigthumb.honeytip.domain;

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
  private String name;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, insertable = false, updatable = false, columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  private String email;

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

  @Builder
  public User(String name, String nickname, String email, String password, UserType type,
      UserStatus status) {
    Assert.notNull(name, "User name should not be null");
    Assert.notNull(nickname, "User nickname should not be null");
    Assert.notNull(email, "User email should not be null");
    Assert.notNull(password, "User password should not be null");

    if (type == null) {
      type = UserType.MEM;
    }
    if (status == null) {
      status = UserStatus.NON;
    }

    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
    this.type = type;
    this.status = status;

    this.tips = new ArrayList<>();
    this.reports = new ArrayList<>();
  }

  public void updateStatus(UserStatus status) {
    this.status = status;
  }

  public void encryptPassword() {
    BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder(10);
    password = bcpe.encode(password);
  }

  public void validateInfo() {
    if (email.length() > 50) {
      throw new IllegalStateException("User email length should be under 50");
    }
    if (name.length() < 2 || name.length() > 30) {
      throw new IllegalStateException("User name should be length between 2 to 30");
    }
    if (password.length() < 8 || password.length() > 23) {
      throw new IllegalStateException("User password should be length between 8 to 23");
    }
    if (nickname.length() < 2 || nickname.length() > 16) {
      throw new IllegalStateException("User nickname should be length between 2 to 16");
    }
  }
}
