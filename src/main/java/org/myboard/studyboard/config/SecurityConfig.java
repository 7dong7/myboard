package org.myboard.studyboard.config;

import org.myboard.studyboard.domain.user.entity.UserRoleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 암호화 클래스 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 시큐리티 권한 계층 설정, role 수직적 계층 적용
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role(UserRoleType.ADMIN.name()).implies(UserRoleType.USER.name())
                .build();
    }
    

    // 시큐리티 필터체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // csrf 보안 해제 (개발 환경에서 설정시 복잡성)
        http
                .csrf(csrf -> csrf.disable());

        // 접근 경로별 인가 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/join", "/").permitAll()
                        .requestMatchers("/user/update/**", "/board/**").hasRole(UserRoleType.USER.name())
                        .requestMatchers("/**").permitAll());

        // 로그인 방식 설정 Form 로그인 방식
        http
                .formLogin(Customizer.withDefaults());
        
        return http.build();
    }
}
