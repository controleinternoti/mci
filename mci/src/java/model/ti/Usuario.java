/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ti;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

/**
 *
 * @author Eder
 */
@Entity
@Table(name = "usuario_unifi")
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
    
    @CPF
    @Column(name = "cpf", nullable = false)
    private String cpf;
    
    @Temporal(value = TemporalType.DATE)
    @Column(name = "data_nasc", nullable = false)
    private Date dataNasc;
    
    @Email(message = "Endereço de email inválido")
    @Column(name = "email", nullable = false)
    private String email;
    

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    
    
    
}
