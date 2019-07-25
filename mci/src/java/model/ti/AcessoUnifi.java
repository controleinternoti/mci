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
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author enascimento
 */
@Entity
@Table(name = "acesso_unifi")
public class AcessoUnifi implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACESSO_UNIFI_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_ACESSO_UNIFI", allocationSize = 1, name = "ACESSO_UNIFI_SEQ")
    @Column(name = "id_acesso", unique = true, nullable = false)
    private Integer idAcesso;
    @Column(name = "mac", nullable = false)
    private String mac;
    @Column(name = "ip", nullable = false)
    private String ip;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "id_acesso_usuarior"))
    private Usuario userUnifi;
    @Temporal(value = TemporalType.DATE)
    @Column(name = "data", nullable = true)
    private Date data;

    public Integer getIdAcesso() {
        return idAcesso;
    }

    public void setIdAcesso(Integer idAcesso) {
        this.idAcesso = idAcesso;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Usuario getUserUnifi() {
        return userUnifi;
    }

    public void setUserUnifi(Usuario userUnifi) {
        this.userUnifi = userUnifi;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
}
