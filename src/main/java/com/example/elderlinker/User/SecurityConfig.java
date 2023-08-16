package com.example.elderlinker.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // SecurityFilterChain 설정
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 모든 요청에 대해 인증하지 않고 허용 (임시적인 설정, 실제로는 인증을 적절히 설정해야 함)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                // h2-console을 위한 CSRF 토큰 무시 설정
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                // X-Frame-Options 헤더 설정 (Clickjacking 공격 방지)
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                // 사용자 로그인 관련 설정
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login") // 로그인 페이지 URL 설정
                        .defaultSuccessUrl("/h2-console")) // 로그인 성공 후 이동할 URL 설정 필요
                // 사용자 로그아웃 관련 설정
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 요청 URL 설정
                        .logoutSuccessUrl("/h2-console") // 로그아웃 성공 후 이동할 URL 설정
                        .invalidateHttpSession(true)) // HttpSession 무효화
        ;
        return http.build();
    }

    // PasswordEncoder 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 알고리즘을 사용한 비밀번호 암호화 빈 등록
    }
}
