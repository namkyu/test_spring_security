package com.example.demo.config;

import com.example.demo.common.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = false)
public class SecurityJwtConfig extends WebSecurityConfigurerAdapter {

    public static final String TARGET_URL = "/index.html";
    public static final String ERROR_URL = "/denied";

    private final TokenHelper tokenHelper;
    private final UserDetailsServiceImp userDetailsService;
    private final JwtProperties jwtProperties;

    @Bean
    public TokenSuccessHandler tokenSuccessHandler() {
        return new TokenSuccessHandler(tokenHelper, TARGET_URL, jwtProperties);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenHelper, jwtProperties);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler customAccessDeniedHandler = new CustomAccessDeniedHandler(ERROR_URL);
        return customAccessDeniedHandler;
    }

    // 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // 스프링 시큐리티를 무시하게 하는 URL 규칙
    // Security 필터체인을 적용하고 싶지 않은 리소스에 대해 설정을 할수있도록 ignore() 를 제공
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**"
                , "/fonts/**"
                , "/images/**"
                , "/img/**"
                , "/js/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/logout.html").permitAll()
                .antMatchers("/denied").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**").authenticated()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .successHandler(tokenSuccessHandler())

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())

                .and()
                .logout()
                .deleteCookies(jwtProperties.getCookieName())
                .logoutSuccessUrl("/logout.html");
    }
}
