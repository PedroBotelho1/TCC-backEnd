// Em: src/main/java/com/tccmaster/projectccmaster/aplication/controller/AuthController.java

package com.tccmaster.projectccmaster.aplication.controller;

import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import com.tccmaster.projectccmaster.aplication.entity.UsuarioEntity;
import com.tccmaster.projectccmaster.aplication.repository.TecnicoRepository; // IMPORTADO
import com.tccmaster.projectccmaster.aplication.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- NOVAS INJEÇÕES ---
    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint para login de USUÁRIOS
    @PostMapping("/login/usuario")
    public ResponseEntity<?> authenticateUsuario(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha())
            );

            UsuarioEntity usuario = (UsuarioEntity) authentication.getPrincipal();
            String token = gerarTokenSimples(usuario.getEmail());
            return ResponseEntity.ok(criarRespostaDeSucessoUsuario(token, usuario));

        } catch (Exception e) {
            // Lógica de migração de senha (mantida para usuários existentes)
            Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(loginRequest.email());
            if (usuarioOpt.isPresent() && loginRequest.senha().equals(usuarioOpt.get().getSenha())) {
                UsuarioEntity usuario = usuarioOpt.get();
                usuario.setSenha(passwordEncoder.encode(loginRequest.senha()));
                usuarioRepository.save(usuario);
                String token = gerarTokenSimples(usuario.getEmail());
                return ResponseEntity.ok(criarRespostaDeSucessoUsuario(token, usuario));
            }
            return ResponseEntity.status(401).body("Erro de autenticação: Credenciais de usuário inválidas");
        }
    }

    // --- NOVO ENDPOINT PARA LOGIN DE TÉCNICOS ---
    @PostMapping("/login/tecnico")
    public ResponseEntity<?> authenticateTecnico(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<TecnicoEntity> tecnicoOpt = tecnicoRepository.findByEmail(loginRequest.email());

            if (tecnicoOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Erro de autenticação: Técnico não encontrado.");
            }

            TecnicoEntity tecnico = tecnicoOpt.get();
            // Compara a senha enviada com a senha criptografada no banco
            if (passwordEncoder.matches(loginRequest.senha(), tecnico.getSenha())) {
                String token = gerarTokenSimples(tecnico.getEmail());
                return ResponseEntity.ok(criarRespostaDeSucessoTecnico(token, tecnico));
            } else {
                return ResponseEntity.status(401).body("Erro de autenticação: Credenciais de técnico inválidas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor: " + e.getMessage());
        }
    }

    // Método auxiliar para criar um token simples
    private String gerarTokenSimples(String email) {
        return "fake-jwt-token-for-" + email;
    }

    // Método auxiliar para a resposta de sucesso do USUÁRIO
    private Map<String, Object> criarRespostaDeSucessoUsuario(String token, UsuarioEntity usuario) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", Map.of(
                "id", usuario.getId(),
                "nome", usuario.getNome(),
                "email", usuario.getEmail(),
                "role", "USUARIO" // Adiciona um campo de role/tipo
        ));
        return response;
    }

    // --- NOVO MÉTODO AUXILIAR PARA A RESPOSTA DE SUCESSO DO TÉCNICO ---
    private Map<String, Object> criarRespostaDeSucessoTecnico(String token, TecnicoEntity tecnico) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("usuario", Map.of( // Usamos a chave 'usuario' para manter a consistência no frontend
                "id", tecnico.getId(),
                "nome", tecnico.getNome(),
                "email", tecnico.getEmail(),
                "especialidade", tecnico.getEspecialidade(),
                "role", "TECNICO" // Adiciona um campo de role/tipo
        ));
        return response;
    }
}