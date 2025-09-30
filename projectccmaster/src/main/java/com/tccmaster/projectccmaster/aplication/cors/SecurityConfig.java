package com.tccmaster.projectccmaster.aplication.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints de usuário e técnico
                        .requestMatchers(HttpMethod.GET, "/usuarios", "/tecnicos", "/tecnicos/disponiveis").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios", "/tecnicos").permitAll()

                        // Endpoints do chat
                        .requestMatchers(HttpMethod.POST, "/chat/registrar-conversa").permitAll()

                        // Libera os endpoints do WebSocket
                        .requestMatchers("/chat-socket/**").permitAll()

                        // NOVA REGRA: Libera a busca de chamados para os técnicos
                        .requestMatchers(HttpMethod.GET, "/chat/usuarios/**").permitAll()

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}