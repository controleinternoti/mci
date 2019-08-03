/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import dao.Dao;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import model.ti.Redirecionamento;
import util.FacesUtil;

/**
 *
 * @author enascimento
 */
@ViewScoped
@ManagedBean
public class RedirecionamentoMB implements Serializable {

    private Dao dao;
    private Redirecionamento redirecionamento;
    private String a, mac, macAp, t;

    public RedirecionamentoMB() {
        novo();

    }

    public void novo() {

        setA("teste");
        dao = new Dao();
        redirecionamento = new Redirecionamento();
        pegaParamentroUrl();
    }

    public void pegaParamentroUrl() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        setMac(request.getParameter("id"));
        setMacAp(request.getParameter("ap"));
        setT(request.getParameter("t"));
        gravar();

    }

    public void gravar() {
        //dao = new Dao();
        try {
            System.out.println("===ap--" + getMacAp());
            System.out.println("===id--" + getMac());
            System.out.println("===t--" + getT());
            redirecionamento.setMac(getMac());
            redirecionamento.setMacAp(getMacAp());
            redirecionamento.setStart(getT());
            dao.gravar(redirecionamento);
            System.out.println("gravou");
            redirecionamento = new Redirecionamento();
            
            //FacesContext.getCurrentInstance().getExternalContext().redirect("./login.xhtml");
            //FacesContext.getCurrentInstance().getExternalContext().redirect("./login.xhtml");
            //FacesUtil.addInfoMessage("Informação", "Cadastro realizado com sucesso!");

        } catch (Exception ex) {
            //FacesUtil.addErrorMessage("Erro", "Entre em contato TI!");
            ex.printStackTrace();
        }
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Redirecionamento getRedirecionamento() {
        return redirecionamento;
    }

    public void setRedirecionamento(Redirecionamento redirecionamento) {
        this.redirecionamento = redirecionamento;
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

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

}
