// Em: src/main/java/com/tccmaster/projectccmaster/aplication/entity/TecnicoEntity.java
package com.tccmaster.projectccmaster.aplication.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // IMPORTAR

import java.util.Collection; // IMPORTAR
import java.util.Collections; // IMPORTAR
import java.util.UUID;

@Entity
@Table(name = "tecnicos")
public class TecnicoEntity implements UserDetails { // <-- IMPLEMENTAR UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "especialidade", nullable = false)
    private String especialidade;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean ativo = true;

    // Construtores...
    public TecnicoEntity() {
    }

    public TecnicoEntity(String nome, String email, String cpf, String senha, String especialidade) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.especialidade = especialidade;
        this.ativo = true;
    }

    // Getters e Setters existentes...
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }


    // --- MÉTODOS UserDetails ADICIONADOS ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Nenhum papel específico (role) por enquanto
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // O "username" para o Spring Security será o email
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo; // A conta está ativa se o técnico estiver ativo
    }
}