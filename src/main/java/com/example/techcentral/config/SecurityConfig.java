package com.example.techcentral.config;



import com.example.techcentral.config.jwtConfig.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager () throws Exception{
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }


    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http)throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/category/**", "/api/v1/brand/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/category/**", "/api/v1/brand/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/category/**", "/api/v1/brand/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/order").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/order/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/product/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/product/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/product/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET,"/api/v1/product/**", "/api/v1/category/**", "/api/v1/brand/**").permitAll()
                        .requestMatchers("/api/v1/login", "/api/v1/register").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/user/myProfile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/user/myProfile").authenticated()
                        .requestMatchers("/api/v1/user/**").hasRole("ADMIN")


                        .anyRequest().authenticated()


                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();

    }
}
