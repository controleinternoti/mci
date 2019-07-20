/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ti;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Eder
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_USUARIO", allocationSize = 1, name = "USUARIO_SEQ")
    @Column(name = "id_usuario", unique = true, nullable = false)
    private Integer idUsuario;
    
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @Column(name = "senha", nullable = false)
    private String senha;
    
    @Column(name = "perfil", nullable = false)
    private String perfil;
    
    @Column(name = "apelido", nullable = true)
    private String apelido;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
    
    
    
}
