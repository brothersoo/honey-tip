package com.bigthumb.honeytip.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, insertable = false, updatable = false, columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
  @UpdateTimestamp
  private LocalDateTime updateAt;

  @Enumerated(EnumType.STRING)
  private ReportStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Tip tip;

  public void changeStatus(ReportStatus status) {
    this.status = status;
  }

  @Builder
  public Report(User user, Tip tip) {
    this.status = ReportStatus.PENDING;
    this.user = user;
    this.tip = tip;
  }
}
