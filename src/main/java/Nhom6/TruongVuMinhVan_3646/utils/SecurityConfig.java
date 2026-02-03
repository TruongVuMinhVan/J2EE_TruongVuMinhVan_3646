package Nhom6.TruongVuMinhVan_3646.utils;

import Nhom6.TruongVuMinhVan_3646.services.UserService;
import Nhom6.TruongVuMinhVan_3646.services.OAuthService;
import Nhom6.TruongVuMinhVan_3646.handlers.OAuth2LoginSuccessHandler;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
        private final OAuthService oAuthService;
        private final UserService userService;
        private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

        @Bean
        public UserDetailsService userDetailsService() {
                return userService;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                var auth = new DaoAuthenticationProvider();
                auth.setUserDetailsService(userDetailsService());
                auth.setPasswordEncoder(passwordEncoder());
                return auth;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/js/**", "/bootstrap/**", "/",
                                                                "/oauth/**", "/register", "/error", "/login", "/logout")
                                                .permitAll()

                                                .requestMatchers("/books/edit/**",

                                                                "/books/add", "/books/delete")
                                                .hasAnyAuthority("ADMIN")
                                                .requestMatchers("/books", "/cart", "/cart/**")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                .requestMatchers("/api/**")
                                                .hasAnyAuthority("ADMIN", "USER")
                                                .anyRequest().authenticated())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/login")
                                                .deleteCookies("JSESSIONID")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .permitAll())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/")
                                                .failureUrl("/login?error")
                                                .permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .successHandler(oauth2LoginSuccessHandler)
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error=true")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(oAuthService)))
                                .rememberMe(rememberMe -> rememberMe
                                                .key("hutech")
                                                .rememberMeCookieName("hutech")
                                                .tokenValiditySeconds(24 * 60 * 60)
                                                .userDetailsService(userDetailsService()))
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .accessDeniedPage("/403"))
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .maximumSessions(1)
                                                .expiredUrl("/login"))
                                .build();
        }
}