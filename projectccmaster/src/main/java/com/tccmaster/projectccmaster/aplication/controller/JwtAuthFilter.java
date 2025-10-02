// Em: src/main/java/com/tccmaster/projectccmaster/aplication/controller/JwtAuthFilter.java
package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.Service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // IMPORTAR
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Adicionando log para ver qual URL está passando pelo filtro
        System.out.println("--- JwtAuthFilter ---");
        System.out.println("Processando requisição para: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        final String userEmail;
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Cabeçalho de autorização ausente ou não começa com 'Bearer '");
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        System.out.println("Token extraído: " + jwtToken);

        userEmail = jwtUtil.extractEmail(jwtToken);
        System.out.println("Email extraído do token: " + userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Tentando carregar usuário: " + userEmail);
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                System.out.println("Usuário encontrado no banco: " + userDetails.getUsername());

                if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
                    System.out.println("Token é válido. Configurando autenticação no contexto de segurança...");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Autenticação configurada com sucesso!");
                } else {
                    System.out.println("Validação do token falhou.");
                }
            } catch (UsernameNotFoundException e) {
                // Adicionando log específico se o usuário não for encontrado
                System.out.println("ERRO: Usuário não encontrado no banco de dados com o email: " + userEmail);
            } catch (Exception e) {
                // Log para qualquer outro erro inesperado
                System.out.println("ERRO inesperado ao processar o token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
        System.out.println("--- Fim do JwtAuthFilter ---");
    }
}