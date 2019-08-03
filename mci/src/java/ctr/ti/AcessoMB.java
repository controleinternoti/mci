/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import dao.Dao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import model.ti.AcessoUnifi;
import model.ti.Redirecionamento;
import model.ti.Usuario;
import util.FacesUtil;

/**
 *
 * @author enascimento
 */
@ManagedBean
@ViewScoped
public class AcessoMB implements Serializable {

    private Dao dao;
    private AcessoUnifi acessoUnifi;
    private Redirecionamento redirecionamento;
    private Usuario usuario;
    private List<AcessoUnifi> listaAcessoUnifi = new ArrayList<AcessoUnifi>();
    private String ip, mac, macId, macAp, t, teste;
    private String nome, perfil, user;
    private BigDecimal idUsuario;
    private Integer start, end = 1564442465;
    private boolean habi;

    public AcessoMB() throws IOException {
        dao = (Dao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dao");
        novo();
    }

    public void novo() throws IOException {
        usuario = new Usuario();
        acessoUnifi = new AcessoUnifi();
        redirecionamento = new Redirecionamento();
        acessoUnifi.setUserUnifi(new Usuario());
        setTeste("");

        //selectMongo();
        setHabi(true);
    }

    public void redirecionar() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("./liberacaoAcesso.xhtml");
        usuarioLogado3();
        //insertMongo();
        //buscaMac();
    }

    public void habilitar() {
        setHabi(false);
    }

    public void usuarioLogado3() throws SocketException, IOException {
        List<Object[]> results = dao.usuarioLogado2();
        if (results.isEmpty()) {
            setNome("Administrador");
        } else {
            for (Object[] result : results) {
                setUser((String) result[0]);
                setIdUsuario((BigDecimal) result[1]);
                setPerfil((String) result[2]);
            }

        }
        System.out.println("entrou em usuairo logado");
        getRemoteAddress();
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

        setIp(ip);

        System.out.println("pegou ip");
        getMacs(ip);
        return ip;

    }

    Pattern macpt = null;

    private String getMacs(String ip) {

        // Find OS and set command according to OS
        String OS = System.getProperty("os.name").toLowerCase();

        String[] cmd;

        System.out.println("os: " + OS.contains(OS));
        if (OS.contains("win")) {
            // Windows
            macpt = Pattern
                    .compile("[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+-[0-9a-f]+");
            //.compile("[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+:[0-9a-f]+");
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
                    setMac(m.group(0));
                    System.out.println("pegou o mac");
                    String s = m.group(0);
                    String s2 = s.replaceAll("-", ":");

                    buscaMac(s2);
                    //insertOracle();
                    //insertMongo();
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

    public void buscaMac(String mac) throws IOException {
        //setMac("98:39:8e:dd:b3:e1");
        System.out.println("---- " + getMac());
        List<Object[]> results = dao.buscaMac(mac);
        BigDecimal id;
        for (Object[] result : results) {
            id = (BigDecimal) result[0];
            setMacId((String) result[1]);
            setMacAp((String) result[2]);
            setT((String) result[3]);
        }
        System.out.println("---- " + getT());
        int a = Integer.valueOf(getT());
        setStart(a);
        System.out.println("a: " + a);
        System.out.println("entrou no busca mac");
        insertMongo(a);
        insertOracle();
    }

    public void selectMongo() {
        MongoClient mongoCliente = new MongoClient("localhost", 27117);
        DB db = mongoCliente.getDB("ace");
        DBCollection coll = db.getCollection("guest");
        BasicDBObject fields = new BasicDBObject().append("start", 1); // SELECT name
        //BasicDBObject query = new BasicDBObject().append("name", "Jon"); // WHERE name = "Jon"
        DBCursor results = coll.find(fields).sort(new BasicDBObject("start", 1)).limit(1); // FROM yourCollection
        for (DBObject dbObject : results) {
            System.out.println(dbObject);
        }

        //System.out.println("result " + results);
    }

    public void insertMongo(int end) {
        try {

            MongoClient mongoCliente = new MongoClient("localhost", 27117);
            DB db = mongoCliente.getDB("ace");
            DBCollection coll = db.getCollection("guest");
            System.out.println("CONECTOU");
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            setEnd(end + 28000);
            System.out.println("mongo - mac " + getMacId());
            System.out.println("mongo - macAP " + getMacAp());
            System.out.println("mongo - Start " + getStart());
            System.out.println("mongo - fim " + getEnd());
            //String json  ="{'mac':'"+getMac()+"','name':'"+getUser()+"','network_id':'5d14a07339ea348224553605','site_id':'5d138e7ba51e4442f41d1bbb'}";  
            String json = "{'mac':'" + getMacId() + "','ap_mac':'" + getMacAp() + "','start':" + getStart() + ",'site_id':'5d3c31b2805ddd15c8b545b9','authorized_by':'api','end':" + getEnd() + "}";
            //String json = "{'mac':'80:2a:a8:d3:1d:ec','ap_mac':'80:2a:a8:d3:1d:ec','start':1564413665,'site_id':'5d3c31b2805ddd15c8b545b9','authorized_by':'api','end':1564442465}";
            DBObject dbObject = (DBObject) JSON.parse(json);
            coll.insert(dbObject);
            System.out.println("inseriu no mongo");
            //FacesContext.getCurrentInstance().getExternalContext().redirect("www.usinacerradao.com.br");
            DBCursor cursorDocJSON = coll.find();
            while (cursorDocJSON.hasNext()) {
                System.out.println(cursorDocJSON.next());
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public void insertOracle() throws IOException {

        try {
            Date date = new Date();
            String m = getMac();
            String m2 = m.replaceAll("-", ":");
            usuario.setIdUsuario(getIdUsuario().intValue());
            acessoUnifi.setUserUnifi(usuario);
            acessoUnifi.setData(date);
            acessoUnifi.setIp(getIp());
            acessoUnifi.setMac(m2);

            System.out.println("idUsuario: " + acessoUnifi.getUserUnifi());
            System.out.println("date: " + acessoUnifi.getData());
            System.out.println("ip: " + acessoUnifi.getIp());
            System.out.println("mac: " + acessoUnifi.getMac());
            dao.gravar(acessoUnifi);
            System.out.println("inseriu no oracle");
            //novo();

        } catch (Exception ex) {
            FacesUtil.addErrorMessage("Erro", "Entre em contato com suporte!");
            ex.printStackTrace();
        }

    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public AcessoUnifi getAcessoUnifi() {
        return acessoUnifi;
    }

    public void setAcessoUnifi(AcessoUnifi acessoUnifi) {
        this.acessoUnifi = acessoUnifi;
    }

    public List<AcessoUnifi> getListaAcessoUnifi() {
        return listaAcessoUnifi;
    }

    public void setListaAcessoUnifi(List<AcessoUnifi> listaAcessoUnifi) {
        this.listaAcessoUnifi = listaAcessoUnifi;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Pattern getMacpt() {
        return macpt;
    }

    public void setMacpt(Pattern macpt) {
        this.macpt = macpt;
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

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public BigDecimal getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigDecimal idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean isHabi() {
        return habi;
    }

    public void setHabi(boolean habi) {
        this.habi = habi;
    }

    public String getMacAp() {
        return macAp;
    }

    public void setMacAp(String macAp) {
        this.macAp = macAp;
    }

    public String getTeste() {
        return teste;
    }

    public void setTeste(String teste) {
        this.teste = teste;
    }

    public Redirecionamento getRedirecionamento() {
        return redirecionamento;
    }

    public void setRedirecionamento(Redirecionamento redirecionamento) {
        this.redirecionamento = redirecionamento;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

}
