// Em: src/main/java/com/tccmaster/projectccmaster/aplication/controller/AuthController.java

package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.repository.UsuarioRepository; // IMPORTAR
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORTAR
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// DTO simples para receber os dados de login
record LoginRequest(String email, String senha) {}

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // INJEÇÕES ADICIONADAS PARA A LÓGICA DE MIGRAÇÃO
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ATENÇÃO: Em um projeto real, você usaria uma biblioteca para gerar um JWT (JSON Web Token) aqui.
    // Para simplificar e fazer funcionar, vamos criar um token "falso" mas funcional.
    private String gerarTokenSimples(String email) {
        return "fake-jwt-token-for-" + email;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. Tenta autenticar da forma padrão (esperando senha criptografada)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha())
            );

            UsuarioEntity usuario = (UsuarioEntity) authentication.getPrincipal();
            String token = gerarTokenSimples(usuario.getEmail());
            return ResponseEntity.ok(criarRespostaDeSucesso(token, usuario));

        } catch (Exception e) {
            // 2. SE A AUTENTICAÇÃO PADRÃO FALHAR, TENTA A MIGRAÇÃO
            Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(loginRequest.email());

            if (usuarioOpt.isPresent()) {
                UsuarioEntity usuario = usuarioOpt.get();

                // Compara a senha digitada com a senha em texto plano do banco
                if (loginRequest.senha().equals(usuario.getSenha())) {

                    // SE BATER, A SENHA ESTÁ EM TEXTO PLANO. MIGRA E AUTENTICA.
                    // Criptografa a senha e atualiza o usuário no banco
                    usuario.setSenha(passwordEncoder.encode(loginRequest.senha()));
                    usuarioRepository.save(usuario);

                    // Gera o token e retorna sucesso
                    String token = gerarTokenSimples(usuario.getEmail());
                    return ResponseEntity.ok(criarRespostaDeSucesso(token, usuario));
                }
            }

            // 3. Se nenhuma das tentativas funcionar, as credenciais estão realmente erradas.
            return ResponseEntity.status(401).body("Erro de autenticação: Credenciais inválidas");
        }
    }

    // Método auxiliar para criar a resposta JSON de sucesso
    private Map<String, Object> criarRespostaDeSucesso(String token, UsuarioEntity usuario) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", Map.of(
                "id", usuario.getId(),
                "nome", usuario.getNome(),
                "email", usuario.getEmail()
        ));
        return response;
    }
}