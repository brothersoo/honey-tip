package com.bigthumb.honeytip.auth;


import com.bigthumb.honeytip.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyAuthenticationProvider implements AuthenticationProvider {

  private final AuthService authService;
//  private final PasswordEncoder bCryptPasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    MyUserDetails user = (MyUserDetails) authService.loadUserByUsername(username);

    // TODO: password validation
//    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
//      throw new BadCredentialsException("Incorrect password");
//    }

    return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
