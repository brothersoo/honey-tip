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
public class UserSignupDto {

  @NotNull
  @NotEmpty
  private String username;

  @NotNull
  @NotEmpty
  private String password;

  @NotNull
  @NotEmpty
  private String passwordConfirm;

  @NotNull
  @NotEmpty
  private String nickname;

  @Builder
  public UserSignupDto(String username, String password, String passwordConfirm, String nickname) {
    this.username = username;
    this.password = password;
    this.passwordConfirm = passwordConfirm;
    this.nickname = nickname;
  }
}
