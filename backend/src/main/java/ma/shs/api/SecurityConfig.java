package ma.shs.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ma.shs.api.auth.JwtAuthEntryPoint;
import ma.shs.api.auth.JwtAuthFilter;
import ma.shs.api.auth.JwtService;

/** Squelette : seules l'authentification et la santé sont publiques. Le filtre JWT arrive à J5. */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
    SecurityFilterChain chain(HttpSecurity http, JwtService jwtService, JwtAuthEntryPoint entryPoint) throws Exception {
        http.csrf(c -> c.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
            	.requestMatchers("/auth/register", "/auth/login", "/auth/refresh", "/auth/password-reset", "/actuator/health").permitAll()
                .anyRequest().authenticated())
            .exceptionHandling(e -> e.authenticationEntryPoint(entryPoint))
            .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

