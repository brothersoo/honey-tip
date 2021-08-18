package com.bigthumb.honeytip.filter;

import static com.bigthumb.honeytip.domain.QTip.tip;

import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

@Builder
@Getter
public class TipFilter {
  String nickname;
  String title;
  String content;
  String category;
  LocalDate createdAtFrom;
  LocalDate createdAtTo;

  @Builder
  public TipFilter(String nickname, String title, String content, String category,
      LocalDate createdAtFrom, LocalDate createdAtTo) {
    this.nickname = (nickname != null) ? "%" + nickname + "%" : null;
    this.title = (title != null) ? "%" + title + "%" : null;
    this.content = (content != null) ? "%" + content + "%" : null;
    this.category = category;
    this.createdAtFrom = createdAtFrom;
    this.createdAtTo = createdAtTo;
  }

  public BooleanExpression likeIgnoreCaseTitle() {
    return ObjectUtils.isEmpty(this.title) ? null : tip.title.likeIgnoreCase(this.title);
  }

  public BooleanExpression likeIgnoreCaseNickname() {
    return ObjectUtils.isEmpty(this.nickname) ? null : tip.user.nickname.likeIgnoreCase(this.nickname);
  }

  public BooleanExpression likeIgnoreCaseContent() {
    return ObjectUtils.isEmpty(this.content) ? null : tip.content.likeIgnoreCase(this.content);
  }

  public BooleanExpression eqCategory() {
    return ObjectUtils.isEmpty(this.category) ? null : tip.category.name.eq(this.category);
  }

  public BooleanExpression betweenCreatedAt() {
    if (!ObjectUtils.isEmpty(this.createdAtFrom) && !ObjectUtils.isEmpty(this.createdAtTo)) {
      return tip.createdAt.between(this.createdAtFrom.atStartOfDay(), this.createdAtTo.atStartOfDay());
    } else if (!ObjectUtils.isEmpty(this.createdAtFrom)) {
      return tip.createdAt.after(this.createdAtFrom.atStartOfDay());
    } else if (!ObjectUtils.isEmpty(this.createdAtTo)) {
      return tip.createdAt.before(this.createdAtTo.atStartOfDay());
    } else return null;
  }
}
