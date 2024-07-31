package com.example.travel.config;

import com.example.travel.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userDetailService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomRequestCacheFilter customRequestCacheFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer (){
        return web -> web.ignoring()
                .requestMatchers("/js", "/css", "images");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/", "/join", "/test").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/seller/**").hasRole("SELLER")
                .anyRequest().authenticated()
        ).addFilterBefore(customRequestCacheFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(in -> in
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler) // Custom success handler
                .defaultSuccessUrl("/")
        ).logout(out -> out
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        ).csrf(csrf -> csrf
                .disable()
        ).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());

        return provider;
    }
}
