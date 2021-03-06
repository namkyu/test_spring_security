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

    // ????????? ??????????????? ???????????? ???????????? ????????? ?????? ??????
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // ????????? ??????????????? ???????????? ?????? URL ??????
    // Security ??????????????? ???????????? ?????? ?????? ???????????? ?????? ????????? ??????????????? ignore() ??? ??????
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
