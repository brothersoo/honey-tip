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
  LocalDate from;
  LocalDate to;

  @Builder
  public TipFilter(String nickname, String title, String content, String category,
      LocalDate from, LocalDate to) {
    this.nickname = (nickname != null) ? "%" + nickname + "%" : null;
    this.title = (title != null) ? "%" + title + "%" : null;
    this.content = (content != null) ? "%" + content + "%" : null;
    this.category = category;
    this.from = from;
    this.to = to;
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
    if (!ObjectUtils.isEmpty(this.from) && !ObjectUtils.isEmpty(this.to)) {
      return tip.createdAt.between(this.from.atStartOfDay(), this.to.atStartOfDay());
    } else if (!ObjectUtils.isEmpty(this.from)) {
      return tip.createdAt.after(this.from.atStartOfDay());
    } else if (!ObjectUtils.isEmpty(this.to)) {
      return tip.createdAt.before(this.to.atStartOfDay());
    } else return null;
  }
}
