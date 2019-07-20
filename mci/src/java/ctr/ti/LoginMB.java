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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.ti.Usuario;
import util.FacesUtil;

/**
 *
 * @author Eder
 */
@ManagedBean
@SessionScoped
public class LoginMB implements Serializable{
    private Dao dao;
    private Usuario usuario;
    private List<Usuario> listaUsuario = new ArrayList<Usuario>();
    
    public LoginMB() {
        
        
    }
    
    public void login() throws ServletException, IOException{
        dao = new Dao();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dao", dao);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");
        dispatcher.forward(request, response);
        FacesContext.getCurrentInstance().responseComplete();
    } 
    
    public void preRender() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dao", dao);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if ("true".equals(request.getParameter("invalid"))) {
            FacesUtil.addErrorMessage("Usuário ou senha inválida.", "");
        }
    }
      public void logout() throws IOException, ServletException {
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("dao", dao);
        // getDao().close();
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
      
}
