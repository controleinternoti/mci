/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import dao.Dao;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import model.ti.Usuario;
import util.FacesUtil;

/**
 *
 * @author enascimento
 */
@ManagedBean
@ViewScoped
public class CadastroUsuarioMB implements Serializable {
    private Dao dao;
    private Usuario usuario;
    private String nome, cpf, email,confSenha;
    private Date dataNasc;
    
    public CadastroUsuarioMB(){
        novo();
       
    }
    
    
    public void novo(){
        usuario = new Usuario();
         setConfSenha("");
    }
    public void gravar(ActionEvent evt){
        dao = new Dao();
        try {
            usuario.setPerfil("VISITANTE");
            dao.gravar(usuario);
            novo();
            FacesUtil.addInfoMessage("Informação", "Cadastro realizado com sucesso!");
            
        }  catch (Exception ex) {
            FacesUtil.addErrorMessage("Erro", "Entre em contato TI!");
            ex.printStackTrace();
        }
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getConfSenha() {
        return confSenha;
    }

    public void setConfSenha(String confSenha) {
        this.confSenha = confSenha;
    }
    
    
    
}
