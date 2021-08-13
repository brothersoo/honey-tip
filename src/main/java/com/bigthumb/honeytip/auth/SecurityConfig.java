package com.bigthumb.honeytip.auth;

import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.service.AuthService;
import com.bigthumb.honeytip.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final AuthenticationProvider authenticationProvider;
  private final AuthenticationSuccessHandler authenticationSuccessHandler;
  private final AuthenticationFailureHandler authenticationFailureHandler;
  private final AuthService authService;

  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /* Ant Path Style
   * Single character can be set as a wildcard with '?' eg) /directory/an*.jsp -> both /directory/ant.jsp and /directory/and.jsp will be selected
   * Every file with a specified name can be customized with '*' eg) /directory/*.jsp -> every file with name extension .jsp will be selected
   * Every file and directory under a directory can be set with '**' eg) '/directory/**' -> every file and directory under directory 'directory' will be selected
   */

  /*
  Every request paths can be ignored by web.ignoring().antMatchers()

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("");
  }
  */

  /**
   * These settings can be chained by .and(), but have separated for readability
   */
  @Override
  public void configure(HttpSecurity http) throws Exception {
    /**
     * API can be used by anyone by permitAll()
     * API authorizations can be specified with hasRole()
     * API can be accessed by any anonymous users by anonymous()
     * API can be accessed by any authenticated user by authenticated()
     */
    http.authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/auth/mypage").hasRole(UserType.MEMBER.toString())
        .antMatchers("/tip/new").hasRole(UserType.MEMBER.toString())
        .antMatchers("/auth/signup").anonymous()

        /* Path, param settings for user authentication */
        .and()
        .formLogin()
        .loginPage("/auth/login")
        .loginProcessingUrl("/auth/loginProcess")
        .usernameParameter("username")
        .passwordParameter("password")
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)

        /* Path, param settings for user log out */
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
        .logoutSuccessUrl("/tip/tips")
        .invalidateHttpSession(true)

        /* Provider used on authentication request */
        .and()
        .authenticationProvider(authenticationProvider);

        /* cross site request forgery is disabled */
        http.csrf().disable();
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(authService).passwordEncoder(bCryptPasswordEncoder());
  }
}
