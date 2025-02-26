package com.example.application.configuration;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

  private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    setLoginView(http, LoginView.class); // Varmitetaan oikea kirjautumissivu

    // Määritellään kirjautumisformi
    http.formLogin(form -> form
      .loginPage("/login") // Asetetaan kirjautumissivu
      .successHandler(successHandler()) // Kustomoitu käsittelijä onnistuneeseen kirjautumiseen
      .permitAll()); // Sallitaan pääsy kirjautumissivulle kaikille

    // Määritetään uloskirjautuminen
    http.logout(logout -> logout
      .logoutUrl("/logout") // Asetetaan logout sivu
      .logoutSuccessUrl("/login") // Uudelleenohjaus kirjautumissivulle uloskirjautumisen jälkeen
      .invalidateHttpSession(true) // Mitätöidään kyseinen istunto
      .deleteCookies("JSESSIONID") // Poistetaan istunnon cookiet
      .addLogoutHandler(customLogoutHandler())); // Kustomoitu uloskirjautumiskäsittelijä
  }

  // Yllä käytetty sisäänkirjautumiskäsittelijä
  @Bean
  public AuthenticationSuccessHandler successHandler() {
    SimpleUrlAuthenticationSuccessHandler delegate = new SimpleUrlAuthenticationSuccessHandler("/koti");
    return (request, response, authentication) -> {
      logger.info("Käyttäjä kirjautunut sisään: " + authentication.getName());
      System.out.println("Käyttäjä kirjautunut sisään: " + authentication.getName());
      delegate.onAuthenticationSuccess(request, response, authentication);
    };
  }

  // Ja uloskirjautumiskäsittelijä
  @Bean
  public LogoutHandler customLogoutHandler() {
    return (request, response, authentication) -> {
      if (authentication != null) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        logger.info("Käyttäjä kirjautunut ulos: " + authentication.getName());
        System.out.println("Käyttäjä kirjautunut ulos: " + authentication.getName());
      } else {
        logger.info("Uloskirjautumisyritys, ei autentikointia.");
        System.out.println("Uloskirjautumisyritys, ei autentikointia.");
      }
    };
  }

  // Yksinkertainen (ja aika turvaton) käyttäjähallinta, mutta luodaan projektiin helpoksi esimerkiksi
  @Bean
  public UserDetailsManager userDetailsManager() {
    return new InMemoryUserDetailsManager(
      User.withUsername("user")
        .password("{noop}user") // No-op salasanaencoder
        .roles("USER").build(),
      User.withUsername("admin")
        .password("{noop}admin") // No-op salasanaencoder
        .roles("ADMIN").build());
  }
}