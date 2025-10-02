// Em: src/main/java/com/tccmaster/projectccmaster/aplication/entity/FeedbackEntity.java
package com.tccmaster.projectccmaster.aplication.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_FEEDBACK")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // ESTE CAMPO SERÁ REMOVIDO OU REAPROVEITADO
    private String nome;

    private int avaliacao;
    private String comentario;
    private LocalDateTime dataCriacao;

    // --- NOVO CAMPO ---
    private String tipo; // Ex: "ATENDIMENTO", "PLATAFORMA"

    @ManyToOne // Já é opcional por padrão
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @ManyToOne // Já é opcional por padrão
    @JoinColumn(name = "tecnico_id")
    private TecnicoEntity tecnico;

    // Getters e Setters...
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getAvaliacao() { return avaliacao; }
    public void setAvaliacao(int avaliacao) { this.avaliacao = avaliacao; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public UsuarioEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioEntity usuario) { this.usuario = usuario; }
    public TecnicoEntity getTecnico() { return tecnico; }
    public void setTecnico(TecnicoEntity tecnico) { this.tecnico = tecnico; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    // --- GETTERS E SETTERS PARA O NOVO CAMPO ---
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}