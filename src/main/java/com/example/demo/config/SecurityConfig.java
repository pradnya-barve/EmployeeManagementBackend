package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.demo.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Define the PasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    // Define the SecurityFilterChain bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing (enable for production)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/employees","/api/users/register","/", "/index.html","/login", "/register", "/css/**", "/js/**", "/image/**").permitAll() // Allow access to these routes
                .anyRequest().authenticated() // All other routes need authentication
            )
            .formLogin(form -> form
                .loginPage("/login") // Custom login page
                .successHandler((request, response, authentication) -> {
                    String role = authentication.getAuthorities().toString();
                    if (role.contains("ADMIN")) {
                        response.sendRedirect("/admin-dashboard");
                    } else if (role.contains("EMPLOYEE")) {
                        response.sendRedirect("/employee-dashboard");
                    } else {
                        response.sendRedirect("/dashboard"); // Default dashboard for other roles
                    }
                })
                .permitAll() // Allow access to the login page for everyone
                
            )
            
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout").permitAll() // Redirect to login after logout
            );
        return http.build();
    }

}