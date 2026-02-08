package de.iu.fallstudie.likeherotozero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguration für Spring Security.
 * Erlaubt den öffentlichen Zugriff auf die Startseite.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**")
          .permitAll()
          .requestMatchers("/add")
          .authenticated() // Diese Zeile hinzufügen!
          .anyRequest()
          .permitAll()
      )
      .formLogin(form ->
        form.loginPage("/login").defaultSuccessUrl("/", true).permitAll()
      )
      .logout(logout -> logout.logoutSuccessUrl("/").permitAll());

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    // Standard-Benutzer für Testzwecke
    @SuppressWarnings("deprecation")
    UserDetails user = User.withDefaultPasswordEncoder()
      .username("admin")
      .password("password123")
      .roles("USER")
      .build();

    return new InMemoryUserDetailsManager(user);
  }
}
