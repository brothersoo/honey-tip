package com.bigthumb.honeytip.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TipDto {

  @NotNull
  @NotEmpty
  private String title;

  @NotNull
  @NotEmpty
  private String content;

  @NotNull
  @NotEmpty
  private String categoryName;

  @Builder
  public TipDto(String title, String content, String categoryName) {
    this.title = title;
    this.content = content;
    this.categoryName = categoryName;
  }
}
