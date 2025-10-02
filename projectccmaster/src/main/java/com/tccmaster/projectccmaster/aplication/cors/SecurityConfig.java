// Em: src/main/java/com/tccmaster/projectccmaster/aplication/cors/SecurityConfig.java
package com.tccmaster.projectccmaster.aplication.cors;

import com.tccmaster.projectccmaster.aplication.controller.JwtAuthFilter; // IMPORTAR
import org.springframework.beans.factory.annotation.Autowired; // IMPORTAR
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // IMPORTAR

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // INJEÇÃO DO NOSSO NOVO FILTRO
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos (não precisam de login)
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios", "/tecnicos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tecnicos/disponiveis").permitAll()
                        .requestMatchers("/chat-socket/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/chat/registrar-conversa").permitAll()
                        .requestMatchers(HttpMethod.GET, "/chat/usuarios/**").permitAll()

                        // Agora, qualquer requisição para /api/feedbacks/** será verificada pelo nosso filtro
                        .requestMatchers("/api/feedbacks/**").authenticated()

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                // ADICIONA O NOSSO FILTRO NA CADEIA DE SEGURANÇA
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}