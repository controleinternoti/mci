/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import dao.Dao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.ti.Dashboard;
import model.ti.Usuario;

/**
 *
 * @author enascimento
 */
@ManagedBean
@ViewScoped
public class DashboardMB implements Serializable {

    private Usuario usuario;
    private List<Usuario> listaUsuario = new ArrayList<Usuario>();
    private Dao dao;
     //Variaveis
    private String nome, perfil, user, mac;


    public DashboardMB() throws IOException {
        dao = (Dao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dao");
        novo();

    }
    public void novo() throws SocketException, IOException{
     usuario = new Usuario();
     listaUsuario = new ArrayList<Usuario>();
     usuarioLogado3();
     
    }

    public void usuarioLogado3() throws SocketException, IOException {
        List<Object[]> results = dao.usuarioLogado2();
        if (results.isEmpty()) {
            setNome("Administrador");
        } else {
            for (Object[] result : results) {
                setUser((String) result[0]);
                //setNome((String) result[1]);
                setPerfil((String) result[2]);
            }

        }
        mac();
        insertMongo();
    }
    
    public void insertMongo() throws IOException{
        MongoClient mongoCliente = new MongoClient("localhost",27117);
            DB db=mongoCliente.getDB("ace");
            DBCollection coll = db.getCollection("user");
            System.out.println("CONECTOU");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      
            String json  ="{'mac':'"+getMac()+"','name':'"+getUser()+"','network_id':'5d14a07339ea348224553605','site_id':'5d138e7ba51e4442f41d1bbb'}";  
            DBObject dbObject = (DBObject)JSON.parse(json);
            coll.insert(dbObject);
        
        
    }
    public void mac() throws SocketException{
        try {         
           InetAddress address = InetAddress.getLocalHost();  
           NetworkInterface ni = NetworkInterface.getByInetAddress(address);  
           byte[] mac = ni.getHardwareAddress();
           String macAddress = "";
           for (int i = 0; i < mac.length; i++) {             
               setMac(macAddress += (String.format("%02X:", mac[i])));  
           }
           System.out.println(macAddress.substring(0, macAddress.length()-1));
        } catch (UnknownHostException e) {  
           e.printStackTrace();
        } catch (SocketException e) {  
           e.printStackTrace();  
        }
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

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

   
}
