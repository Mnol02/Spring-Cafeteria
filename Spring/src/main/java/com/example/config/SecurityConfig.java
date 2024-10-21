/*
 * package com.example.config;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.Customizer; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.web.SecurityFilterChain;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class SecurityConfig {
 * 
 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
 * Exception { http .authorizeHttpRequests((authz) -> authz
 * .anyRequest().permitAll() // 모든 요청 허용
 * 
 * .requestMatchers("/index").permitAll() // /login 경로를 허용
 * .anyRequest().authenticated()
 * 
 * ) .formLogin(form -> form .loginPage("/login") // 로그인 페이지 경로 설정 .permitAll()
 * // 모든 사용자가 로그인 페이지에 접근할 수 있도록 허용 ) .httpBasic(Customizer.withDefaults());
 * return http.build(); } }
 */