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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tip {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Column()
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @Enumerated(EnumType.STRING)
  private TipStatus status = TipStatus.PST;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "tip")
  private List<Report> reports;

  @Builder
  public Tip(String title, String content, User user, Category category, TipStatus status) {
    Assert.notNull(title, "Tip title should not be null");
    Assert.notNull(content, "Tip content should not be null");
    Assert.notNull(user, "Tip user should not be null");
    Assert.notNull(category, "Tip category should not be null");

    if (status == null) {
      status = TipStatus.PST;
    }

    this.title = title;
    this.content = content;
    this.user = user;
    this.category = category;
    this.status = status;

    this.reports = new ArrayList<>();
  }

  public void update(String title, String content, Category category) {
    if (title != null) {
      this.title = title;
    }
    if (content != null) {
      this.content = content;
    }
    if (category != null) {
      this.category = category;
      this.category.getTips().add(this);
    }
  }
}
