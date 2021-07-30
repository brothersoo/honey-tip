package com.bigthumb.honeytip.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

@Entity @Getter
public class User {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private String nickName;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(value = EnumType.STRING)
  private UserType type = UserType.MEM;

  @Enumerated(EnumType.STRING)
  private UserStatus status = UserStatus.NON;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Tip> tips;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Report> reports;

  public void setUserData(String name, String nickName, String email, String password) {
    this.name = name;
    this.nickName = nickName;
    this.email = email;
    this.password = password;
  }
}
