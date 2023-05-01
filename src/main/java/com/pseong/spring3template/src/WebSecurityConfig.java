package com.pseong.spring3template.src;

import com.pseong.spring3template.security.CustomAccessDeniedHandler;
import com.pseong.spring3template.security.CustomAuthenticationEntryPoint;
import com.pseong.spring3template.security.JwtAuthenticationFilter;
import com.pseong.spring3template.security.SecurityUserDetailsService;
import com.pseong.spring3template.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 사용
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final SecurityUserDetailsService securityUserDetailsService;
    private final JwtService jwtService;
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(securityUserDetailsService, jwtService, handlerExceptionResolver);
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/**").permitAll()
                //.requestMatchers("/user/login").permitAll()
                //.requestMatchers("/forum").permitAll()
                //.requestMatchers("/swagger-ui/**").permitAll()
                //.requestMatchers("/swagger-resources/**").permitAll()
                //.requestMatchers("/v3/api-docs/**").permitAll()
                //.requestMatchers("/rank/**").permitAll()
                //.requestMatchers("/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}