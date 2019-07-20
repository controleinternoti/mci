/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import dao.Dao;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author enascimento
 */
@ManagedBean
@ViewScoped
public class DashboardMB implements Serializable {

    private Dashboard dashboard;
    private List<Dashboard> listaDashboard = new ArrayList<Dashboard>();
    private Dao dao;
    private BigDecimal WindowsOpen, WindowsOem, officeOpen, officeFpp;
    private int opcao, totWinDiponivel = 0, totProject, totAgrocad, totAutocad;
    private boolean coluna;
    private String sofAdd;

    public DashboardMB() {
        dao = (Dao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dao");
        novo();

    }

    public void novo() {
        dashboard = new Dashboard();
        listaDashboard = new ArrayList<Dashboard>();
        Dashb();
        Windows();
        Office();
        totEmail();
        officeDisponivel();
        windowsDisponivel();
        setColuna(false);
        softAdd();

    }
    
    public void Dashb() {
        List<Object[]> pc = dao.totComputador();
        List<Object[]> note = dao.totNotebook();

        dashboard.setTotComputador(pc.size());
        dashboard.setTotNotebook(note.size());

        System.out.println("--------" + pc.size());
        System.out.println("--------" + note.size());

    }

    public void selecionarOpcaoOffice(int a) {
        List<Object[]> results = null;
        switch (a) {
            case 2019:
                results = dao.office2019();
                break;
            case 2016:
                results = dao.office2016();
                break;
            case 2013:
                results = dao.office2013();
                break;
            case 2010:
                results = dao.office2010();
                break;
            case 2007:
                results = dao.office2007();
                break;
        }
        setListaDashboard(new ArrayList<Dashboard>());
        Dashboard dash;
        for (Object[] result : results) {
            dash = new Dashboard();
            System.out.println("--------------a------ " + results.size());
            dash.setMicro((String) result[0]);
            dash.setSerial((String) result[1]);
            dash.setContrato((BigDecimal) result[2]);
            getListaDashboard().add(dash);
        }

    }

    public void selecionarOpcaoWindows(int a) {
        List<Object[]> results = null;
        switch (a) {
            case 10:
                results = dao.windows10();
                break;
            case 81:
                results = dao.windows8_1();
                break;
            case 8:
                results = dao.windows8();
                break;
            case 7:
                results = dao.windows7();
                break;
            case 6:
                results = dao.windowsVista();
                break;
            case 5:
                results = dao.windowsXp();
                break;
        }
        setListaDashboard(new ArrayList<Dashboard>());
        Dashboard dash;
        for (Object[] result : results) {
            dash = new Dashboard();
            System.out.println("--------------a------ " + results.size());
            dash.setMicro((String) result[0]);
            dash.setSerial((String) result[1]);
            dash.setContrato((BigDecimal) result[2]);
            getListaDashboard().add(dash);
        }

    }

    public void selecionarOpcaoSofAdd(String a) {
        List<Object[]> results = null;
        switch (a) {
            case "AUTOCAD":
                results = dao.autoCad();
                break;
            case "PROJECT":
                results = dao.project();
                break;
            case "AGROCAD":
                results = dao.agroCad();
                break;
        }
        setColuna(true);
        setListaDashboard(new ArrayList<Dashboard>());
        Dashboard dash;
        for (Object[] result : results) {
            dash = new Dashboard();
            System.out.println("--------------a------ " + results.size());
            dash.setMicro((String) result[0]);
            dash.setSerial((String) result[1]);
            dash.setContrato((BigDecimal) result[2]);
            dash.setNomeSoft((String) result[3]);
            getListaDashboard().add(dash);
        }

    }

    public void softAdd() {
        List<Object[]> autocad = dao.autoCad();
        setTotAutocad(autocad.size());
        List<Object[]> project1 = dao.project();
        setTotProject(project1.size());
        System.out.println("project: " + project1.size());
        List<Object[]> agroCad1 = dao.agroCad();
        setTotAgrocad(agroCad1.size());
    }

    public void Office() {
        List<Object[]> office = dao.totOffice();
        List<Object[]> o2019 = dao.office2019();
        List<Object[]> o2016 = dao.office2016();
        List<Object[]> o2013 = dao.office2013();
        List<Object[]> o2010 = dao.office2010();
        List<Object[]> o2007 = dao.office2007();
        List<Object[]> ousado = dao.officeUsado();
        List<Object[]> open = dao.officeOpen();
        List<Object[]> oem = dao.officeFpp();
        BigDecimal a;
        for (Object[] result : office) {
            a = ((BigDecimal) result[0]);
            System.out.println("---office-----" + a);
            dashboard.setTotOffice(a.intValue());
        }
        System.out.println("--------" + office.size());
        for (Object[] result : open) {
            setOfficeOpen((BigDecimal) result[0]);
            System.out.println("tot officeOpen: " + getOfficeOpen());
        }
        for (Object[] result : oem) {
            setOfficeFpp((BigDecimal) result[1]);
            System.out.println("tot officeFpp: " + getOfficeFpp());
        }
        System.out.println("-------- o2019: " + o2019.size());
        System.out.println("-------- o2016: " + o2016.size());
        System.out.println("-------- o2013: " + o2013.size());
        System.out.println("-------- o2010: " + o2010.size());
        System.out.println("-------- o2007: " + o2007.size());
        System.out.println("-------- ousado: " + ousado.size());
        dashboard.setOffice2019(o2019.size());
        dashboard.setOffice2016(o2016.size());
        dashboard.setOffice2013(o2013.size());
        dashboard.setOffice2010(o2010.size());
        dashboard.setOffice2007(o2007.size());
        dashboard.setOfficeUsado(ousado.size());

    }

    public void Windows() {
        List<Object[]> results = dao.totWindows();
        List<Object[]> w10 = dao.windows10();
        List<Object[]> w8_1 = dao.windows8_1();
        List<Object[]> w8 = dao.windows8();
        List<Object[]> w7 = dao.windows7();
        List<Object[]> wv = dao.windowsVista();
        List<Object[]> wx = dao.windowsXp();
        List<Object[]> wusado = dao.WindowsUsado();
        List<Object[]> open = dao.windowsOpen();
        List<Object[]> oem = dao.windowsOem();
        BigDecimal a;

        for (Object[] result : results) {
            a = ((BigDecimal) result[0]);
            System.out.println("---windows-----" + a);
            dashboard.setTotWindows(a.intValue());
        }
        for (Object[] result : open) {
            setWindowsOpen((BigDecimal) result[0]);
            System.out.println("tot windowsOpen: " + getWindowsOpen());
        }
        for (Object[] result : oem) {
            setWindowsOem((BigDecimal) result[1]);
            System.out.println("tot windowsOem: " + getWindowsOem());
        }

        //System.out.println("--------" + results.size());
        System.out.println("-------- w10: " + w10.size());
        System.out.println("-------- w8_1: " + w8_1.size());
        System.out.println("-------- w8: " + w8.size());
        System.out.println("-------- w7: " + w7.size());
        System.out.println("-------- wv: " + wv.size());
        System.out.println("-------- wx:" + wx.size());
        System.out.println("-------- wusado:" + wusado.size());
        dashboard.setWindows10(w10.size());
        dashboard.setWindows8_1(w8_1.size());
        dashboard.setWindows8(w8.size());
        dashboard.setWindows7(w7.size());
        dashboard.setWindowsVista(wv.size());
        dashboard.setWindowsXp(wx.size());
        dashboard.setWindowsUsado(wusado.size());
    }

    public void totEmail() {
        List<Object[]> results = dao.totEmail();
        System.out.println("--------" + results.size());
        dashboard.setTotEmail(results.size());
    }

    public void officeDisponivel() {
        setListaDashboard(new ArrayList<Dashboard>());
        List<Object[]> results = dao.officeDisponivel();
        dashboard.setOfficeDisponiveis(results.size());
        System.out.println("--------------OFFICE DISPONIVEL: " + results.size());
        Dashboard dash;
        for (Object[] result : results) {
            dash = new Dashboard();
            System.out.println("--------------a------ " + results.size());
            dash.setIdSoft((BigDecimal) result[0]);
            dash.setNomeSoft((String) result[1]);
            dash.setSerial((String) result[2]);
            dash.setData((Date) result[3]);
            dash.setNf((BigDecimal) result[4]);
            getListaDashboard().add(dash);
        }
    }

    public void windowsDisponivel() {
        setListaDashboard(new ArrayList<Dashboard>());
        List<Object[]> results = dao.windowsDisponivel();
        dashboard.setWindowsDisponiveis(results.size());
        System.out.println("--------------WINDOWS DISPONIVEL: " + results.size());
        Dashboard dash;
        int tot = 0;
        for (Object[] result : results) {
            dash = new Dashboard();
            //System.out.println("--------------a------ " + results.size());
            dash.setContrato((BigDecimal) result[0]);
            //dash.setSerial((String) result[1]);
            dash.setQtde((BigDecimal) result[1]);
            dash.setUsado((BigDecimal) result[2]);
            int res = dash.getQtde().intValue() - dash.getUsado().intValue();
            if (dash.getQtde() != dash.getUsado()) {
                dash.setWindowsDisponiveis(res);
                getListaDashboard().add(dash);
                System.out.println("--------------CONTRATO: " + dash.getContrato());
                tot = tot + dash.getWindowsDisponiveis();
                System.out.println("--------------TOT: " + dash.getWindowsDisponiveis());
            }

        }
        setTotWinDiponivel(tot);
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public List<Dashboard> getListaDashboard() {
        return listaDashboard;
    }

    public void setListaDashboard(List<Dashboard> listaDashboard) {
        this.listaDashboard = listaDashboard;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public BigDecimal getWindowsOpen() {
        return WindowsOpen;
    }

    public void setWindowsOpen(BigDecimal WindowsOpen) {
        this.WindowsOpen = WindowsOpen;
    }

    public BigDecimal getWindowsOem() {
        return WindowsOem;
    }

    public void setWindowsOem(BigDecimal WindowsOem) {
        this.WindowsOem = WindowsOem;
    }

    public BigDecimal getOfficeOpen() {
        return officeOpen;
    }

    public void setOfficeOpen(BigDecimal officeOpen) {
        this.officeOpen = officeOpen;
    }

    public BigDecimal getOfficeFpp() {
        return officeFpp;
    }

    public void setOfficeFpp(BigDecimal officeFpp) {
        this.officeFpp = officeFpp;
    }

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }

    public int getTotWinDiponivel() {
        return totWinDiponivel;
    }

    public void setTotWinDiponivel(int totWinDiponivel) {
        this.totWinDiponivel = totWinDiponivel;
    }

    public boolean isColuna() {
        return coluna;
    }

    public void setColuna(boolean coluna) {
        this.coluna = coluna;
    }

    public String getSofAdd() {
        return sofAdd;
    }

    public void setSofAdd(String sofAdd) {
        this.sofAdd = sofAdd;
    }

    public int getTotProject() {
        return totProject;
    }

    public void setTotProject(int totProject) {
        this.totProject = totProject;
    }

    public int getTotAgrocad() {
        return totAgrocad;
    }

    public void setTotAgrocad(int totAgrocad) {
        this.totAgrocad = totAgrocad;
    }

    public int getTotAutocad() {
        return totAutocad;
    }

    public void setTotAutocad(int totAutocad) {
        this.totAutocad = totAutocad;
    }

}
