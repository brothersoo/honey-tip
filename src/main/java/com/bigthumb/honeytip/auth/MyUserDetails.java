package com.bigthumb.honeytip.auth;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserStatus;
import com.bigthumb.honeytip.domain.UserType;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
  private String username;
  private String nickname;
  private String password;
  private UserStatus userStatus;
  private UserType userType;

  public MyUserDetails(User user) {
    this.username = user.getUsername();
    this.nickname = user.getNickname();
    this.password = user.getPassword();
    this.userStatus = user.getStatus();
    this.userType = user.getType();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + this.userType.toString()));
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
