/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import dao.Dao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.jar.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
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
    private List<Usuario> listaCpfCadastrado = new ArrayList<Usuario>();
    private String nome, cpf, email, confSenha;
    private Date dataNasc;
    private String mac, ip;
    private boolean cpfCadastrado;

    public CadastroUsuarioMB() throws SocketException {
        novo();
        // ips();
        //mac();
        getRemoteAddress();

    }

    public void novo() {
        dao = new Dao();
        usuario = new Usuario();
        listaCpfCadastrado = new ArrayList<Usuario>();
        setConfSenha("");
        setCpfCadastrado(false);
    }

    public void verificaCpf() {
        listaCpfCadastrado = new ArrayList<Usuario>();
        System.out.println(usuario.getCpf());
        List<Object[]> results = dao.verificaCpfCadastrado(usuario.getCpf());
        if (results.size() > 0) {
            FacesUtil.addWarnMessage("Informação", "CPF ja cadastrado!");
            setCpfCadastrado(true);
        } else {
            setCpfCadastrado(false);
        }

    }


    public String getRemoteAddress() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        System.out.println("ip: " + ip);
        getMac(ip);
        return ip;

    }

    Pattern macpt = null;

    private String getMac(String ip) {

        // Find OS and set command according to OS
        String OS = System.getProperty("os.name").toLowerCase();

        String[] cmd;

        System.out.println("os: " + OS.contains(OS));
        if (OS.contains("win")) {
            // Windows
            macpt = Pattern
                    .compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
            String[] a = {"arp", "-a", ip};
            cmd = a;
        } else {
            // Mac OS X, Linux
            macpt = Pattern
                    .compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
            String[] a = {"arp", ip};
            cmd = a;
        }

        try {

            // Run command
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            // read output with BufferedReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line = reader.readLine();

            // Loop trough lines
            System.out.println("cmd: " + line);
            while (line != null) {
                Matcher m = macpt.matcher(line);

                // when Matcher finds a Line then return it as result
                if (m.find()) {
                    System.out.println("Found");
                    System.out.println("MAC: " + m.group(0));
                    return m.group(0);
                }

                line = reader.readLine();
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return empty string if no MAC is found
        return "";
    }

//    public String ips() {
////              setIp(InetAddress.getLocalHost().getHostAddress());
////              usuario.setNome(getIp());
////            System.out.println(InetAddress.getLocalHost().getHostAddress());
////            FacesContext context = FacesContext.getCurrentInstance();
////            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
////            request = (HttpServletRequest) context.getExternalContext().getRequest();
////            String ipDaMaquina = InetAddress.getLocalHost().getHostAddress();
////            System.out.println(ipDaMaquina);
////            //nome da maquina.
////            String nomeDaMaquina = InetAddress.getLocalHost().getHostName();
////            System.out.println(nomeDaMaquina);
//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//
//            String remoteAddr = "";
//            String ipAddress = request.getRemoteAddr();
//            if (request != null) {
//                remoteAddr = request.getHeader("X-FORWARDED-FOR");
//                if (remoteAddr == null || "".equals(remoteAddr)) {
//                    remoteAddr = request.getRemoteAddr();
//                    setIp(remoteAddr);
//                }
//            }
//
//            System.out.println(remoteAddr);
//            //System.out.println(getIp());
//            getMACAddress(remoteAddr);
//
//        return remoteAddr;
//        
//    }
//    public String getMACAddress(String ip) {
//		String str = "";
//		String macAddress = "";
//		try {
//			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
//			InputStreamReader ir = new InputStreamReader(p.getInputStream());
//			LineNumberReader input = new LineNumberReader(ir);
//                        System.out.println("passou 1" );
//			for (int i = 1; i < 100; i++) {
//				str = input.readLine();
//                                System.out.println("str: " + str );
//				if (str != null) {
//					if (str.indexOf("MAC Address") > 1) {
//						macAddress = str.substring(
//								str.indexOf("MAC Address") + 14, str.length());
//						break;
//					}
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace(System.out);
//		}
//                System.out.println("mac: " +macAddress );
//		return macAddress;
//	}
//    
//
//    public String getMACAddresss(String remoteAddr){ 
//        String str = ""; 
//        String macAddress = ""; 
//        try { 
//            Process p = Runtime.getRuntime().exec("nbtstat -A " + remoteAddr); 
//            InputStreamReader ir = new InputStreamReader(p.getInputStream()); 
//            LineNumberReader input = new LineNumberReader(ir); 
//            for (int i = 1; i <100; i++) { 
//                str = input.readLine(); 
//                if (str != null) { 
//                    if (str.indexOf("MAC Address") > 1) { 
//                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length()); 
//                        break; 
//                    } 
//                } 
//            } 
//        } catch (IOException e) { 
//            e.printStackTrace(System.out); 
//        } 
//        System.out.println(macAddress);
//        return macAddress; 
//    }
//
//    public void mac() throws SocketException {
//        try {
//            InetAddress address = InetAddress.getLocalHost();
//            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
//            byte[] mac = ni.getHardwareAddress();
//            String macAddress = "";
//            for (int i = 0; i < mac.length; i++) {
//                setMac(macAddress += (String.format("%02X:", mac[i])));
//            }
//            usuario.setEmail(getMac());
//            System.out.println(macAddress.substring(0, macAddress.length() - 1));
//            System.out.println(getEmail());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//    }
    public void gravar(ActionEvent evt) {
        //dao = new Dao();
        try {
            usuario.setPerfil("VISITANTE");
            dao.gravar(usuario);
            novo();
            FacesContext.getCurrentInstance().getExternalContext().redirect("./login.xhtml");
            FacesUtil.addInfoMessage("Informação", "Cadastro realizado com sucesso!");

        } catch (Exception ex) {
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

    public boolean isCpfCadastrado() {
        return cpfCadastrado;
    }

    public void setCpfCadastrado(boolean cpfCadastrado) {
        this.cpfCadastrado = cpfCadastrado;
    }

    public Pattern getMacpt() {
        return macpt;
    }

    public void setMacpt(Pattern macpt) {
        this.macpt = macpt;
    }

}
