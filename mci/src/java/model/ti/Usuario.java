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
import javax.persistence.Transient;

/**
 *
 * @author Eder
 */
@Entity
@Table(name = "web_prop_usu")
public class Usuario implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_USUARIO", allocationSize = 1, name = "USUARIO_SEQ")
    @Column(name = "webpropusu_id", unique = true, nullable = false)
    private Integer idUsuario;
    
    @Column(name = "cd_usu_bd", length = 30, nullable = false)
    private String usuario;
    
    @Column(name = "senha", length = 30, nullable = false)
    private String senha;
    
    @Column(name = "prop_id", nullable = false)
    private Integer propId;
    
    @Column(name = "qtde_acesso", nullable = false)
    private Integer qtdeAcesso;
    
    @Temporal(value = TemporalType.DATE)
    @Column(name = "ult_acesso", nullable = false)
    private Date ultAcesso;
    
    @Transient
    private String perfil;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    public Integer getQtdeAcesso() {
        return qtdeAcesso;
    }

    public void setQtdeAcesso(Integer qtdeAcesso) {
        this.qtdeAcesso = qtdeAcesso;
    }

    public Date getUltAcesso() {
        return ultAcesso;
    }

    public void setUltAcesso(Date ultAcesso) {
        this.ultAcesso = ultAcesso;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    
   
   
    
}
