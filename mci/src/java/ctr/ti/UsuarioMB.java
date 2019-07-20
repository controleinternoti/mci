/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import dao.Dao;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.ti.Usuario;

/**
 *
 * @author enascimento
 */
@ManagedBean
@ViewScoped
public class UsuarioMB implements Serializable{
    private Dao dao;
    private Usuario usuario;
    private List<Usuario> listaUsuario = new ArrayList<Usuario>();

    //Variaveis
    private String nome, perfil, user;

    public UsuarioMB() {
        dao = (Dao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dao");
        novo();
    }
     public void novo() {
        usuarioLogado3();
        System.out.println("Apelido" + getNome());
    }

//    public void usuarioLogado3() {
//        listaUsuario =(List<Usuario>) dao.usuarioLogado2();
//        setNome(listaUsuario.get(0).getApelido());
//    }
     public void usuarioLogado3() {
        List<Object[]> results = dao.usuarioLogado2();
        if (results.isEmpty()) {
            setNome("Administrador");
        } else {
            for (Object[] result : results) {
                setUser((String) result[0]);
                setNome((String) result[1]);
                setPerfil((String) result[2]);
            }

        }
    }
     public void logout() throws IOException, ServletException {
         System.out.println("aquiiiiii");
        //getDao().close();
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        session.invalidate();
        RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_logout");
        dispatcher.forward(request, response);
        FacesContext.getCurrentInstance().responseComplete();
        
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

    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
}
