// Em: src/main/java/com/tccmaster/projectccmaster/aplication/Service/CustomUserDetailsService.java
package com.tccmaster.projectccmaster.aplication.Service;

import com.tccmaster.projectccmaster.aplication.entity.TecnicoEntity;
import com.tccmaster.projectccmaster.aplication.repository.TecnicoRepository; // <-- IMPORTAR
import com.tccmaster.projectccmaster.aplication.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional; // <-- IMPORTAR

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- NOVA INJEÇÃO ---
    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tenta encontrar um usuário comum
        Optional<UserDetails> usuario = usuarioRepository.findByEmail(username).map(UserDetails.class::cast);

        if (usuario.isPresent()) {
            return usuario.get();
        }

        // 2. Se não encontrar, tenta encontrar um técnico
        Optional<UserDetails> tecnico = tecnicoRepository.findByEmail(username).map(UserDetails.class::cast);

        // 3. Se encontrou um técnico, o retorna. Senão, lança o erro.
        return tecnico.orElseThrow(() -> new UsernameNotFoundException("Usuário ou Técnico não encontrado com o email: " + username));
    }
}