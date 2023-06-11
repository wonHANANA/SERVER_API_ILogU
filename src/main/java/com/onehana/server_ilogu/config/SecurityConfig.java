package com.onehana.server_ilogu.config;

import com.onehana.server_ilogu.service.UserService;
import com.onehana.server_ilogu.util.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${one-hana.swagger-id}")
    private String swaggerId;

    @Value("${one-hana.swagger-pw}")
    private String swaggerPw;

    @Value("${jwt.access-token.secret-key}")
    private String key;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        ));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        BasicAuthenticationEntryPoint basicAuthenticationEntryPoint = new BasicAuthenticationEntryPoint();
        basicAuthenticationEntryPoint.setRealmName("default");

        return http
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**").authenticated()
                        .requestMatchers("/**").permitAll()
                )
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(basicAuthenticationEntryPoint)
                )
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        List<UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add(User.builder()
                .username(swaggerId)
                .password(passwordEncoder.encode(swaggerPw))
                .roles("ADMIN")
                .build());
        return new InMemoryUserDetailsManager(userDetailsList);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                //from
//                .allowedOrigins("http://localhost:3000, http://i-log-u.site")
                //to
                .allowedOrigins("http://localhost:3000", "http://i-log-u.site")
                //from
                //.allowedMethods(GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name())
                //to
                .allowedMethods("*")

                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
