package com.tccmaster.projectccmaster.aplication.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime; // 1. IMPORTE O LOCALDATETIME
import java.util.UUID;

@Entity
@Table(name = "TB_FEEDBACK")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private int avaliacao;
    private String comentario;

    // 2. ADICIONE O NOVO CAMPO
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private TecnicoEntity tecnico;

    // Getters e Setters
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

    // 3. ADICIONE OS GETTERS E SETTERS PARA O NOVO CAMPO
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}

