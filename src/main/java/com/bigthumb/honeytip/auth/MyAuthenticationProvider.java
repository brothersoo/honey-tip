package com.bigthumb.honeytip.auth;


import com.bigthumb.honeytip.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyAuthenticationProvider implements AuthenticationProvider {

  private final AuthService authService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    MyUserDetails user = (MyUserDetails) authService.loadUserByUsername(username);

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Boolean passowrdMatches = passwordEncoder.matches(password, user.getPassword());

    if (!passowrdMatches) return null;
    else return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
