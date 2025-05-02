package com.pharma.config;

import com.pharma.filter.JwtFilter;
import com.pharma.service.auth.UserDetailsServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtFilter jwtFilter;

    public SpringSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());        // Set the password encoder
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless JWT authentication
                .cors(cors -> cors.configurationSource(new CorsConfig().corsConfigurationSource()))// Enable CORS with a custom source
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Permit CORS preflight request
                        .requestMatchers("/error").permitAll()          // Allow error endpoint without authentication
                        .requestMatchers("/login/**", "/register/**","/api/v1/health-check").permitAll()  // Public login & registration
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/doc/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/public/**",
                                "/pharma/inventory/**",
                                "/pharma/stock/getByItemId/**"
                        ).permitAll()  // Allow Swagger and public resources
                        .anyRequest().authenticated()  // All other requests must be authenticated
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless (JWT) sessions
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}




