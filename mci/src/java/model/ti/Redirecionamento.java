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
 * @author enascimento
 */
@Entity
@Table(name = "redirecionamento")
public class Redirecionamento implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REDIRECIONAMENTO_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_REDIRECIONAMENTO", allocationSize = 1, name = "REDIRECIONAMENTO_SEQ")
    @Column(name = "id_redirecionamento", unique = true, nullable = false)
    private Integer id;
    @Column(name = "mac", nullable = true)
    private String mac;
    @Column(name = "mac_ap", nullable = true)
    private String macAp;
    @Column(name = "t", nullable = true)
    private String start;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMacAp() {
        return macAp;
    }

    public void setMacAp(String macAp) {
        this.macAp = macAp;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
    
}
