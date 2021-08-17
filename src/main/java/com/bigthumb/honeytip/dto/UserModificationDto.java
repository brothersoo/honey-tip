package com.bigthumb.honeytip.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserModificationDto {

  private String nickname;
  private String password;
  private String passwordConfirm;
  private boolean passwordMaintain;
}
