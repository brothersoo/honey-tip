package com.bigthumb.Honeytip.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

@Entity @Getter
public class Category {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Tip> tips;
}
