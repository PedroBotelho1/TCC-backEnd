// Em: src/main/java/com/tccmaster/projectccmaster/aplication/entity/FeedbackEntity.java
package com.tccmaster.projectccmaster.aplication.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;
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
    private LocalDateTime dataCriacao;
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id") // Representa o AUTOR, se for um usuário
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UsuarioEntity usuario;

    // --- NOVO CAMPO ADICIONADO ---
    @ManyToOne
    @JoinColumn(name = "autor_tecnico_id") // Representa o AUTOR, se for um técnico
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TecnicoEntity autorTecnico;


    @ManyToOne
    @JoinColumn(name = "tecnico_id") // Representa quem está sendo AVALIADO
    @OnDelete(action = OnDeleteAction.CASCADE)
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
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // --- GETTERS E SETTERS PARA O NOVO CAMPO ---
    public TecnicoEntity getAutorTecnico() { return autorTecnico; }
    public void setAutorTecnico(TecnicoEntity autorTecnico) { this.autorTecnico = autorTecnico; }
}