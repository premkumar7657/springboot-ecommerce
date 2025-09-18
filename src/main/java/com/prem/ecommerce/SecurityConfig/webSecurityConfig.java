package com.prem.ecommerce.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prem.ecommerce.JWTSecurity.AuthEntryPointJwt;
import com.prem.ecommerce.JWTSecurity.AuthTokenFilter;
import com.prem.ecommerce.JWTSecurity.UserDetailsImpl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class webSecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter()
    {
        return new AuthTokenFilter();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
        
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
    {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
                                            .requestMatchers("v3/api-docs/**").permitAll()
                                            .requestMatchers("/swagger-ui/**").permitAll()
                                            .requestMatchers("/api/public/**").permitAll()
                                            .requestMatchers("/api/test/**").permitAll()
                                            .requestMatchers("/api/admin/**").permitAll()
                                            .requestMatchers("/images/**").permitAll()
                                            .anyRequest().authenticated());

        http.csrf(csrf -> csrf.disable())
            .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            http.authenticationProvider(authenticationProvider());
            

       // http.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); 

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // Allow frames from the same origin (for H2 console)

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
        
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer()
    {
        return (web ->  web.ignoring()
        .requestMatchers("v2/api-docs/**","/configuration/ui","/swagger-resources/**","/configuration/security","/swagger-ui.html","/webjars/**"));
    }
}
