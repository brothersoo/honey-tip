package com.bigthumb.honeytip.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

@Entity @Getter
public class Report {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Tip tip;
}
