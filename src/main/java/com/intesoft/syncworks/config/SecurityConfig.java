package com.intesoft.syncworks.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthProvider userAuthProvider;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
                .addFilterBefore(new JwtAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/users/myuser/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/users/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/drive/**").hasAnyRole("COMERCIAL","INGENIERIA")
                                .requestMatchers(HttpMethod.GET, "/drive/**").hasAnyRole("COMERCIAL", "INGENIERIA")
                                .anyRequest().authenticated()
                );
        return http.build();
    }


}