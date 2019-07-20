/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ti;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author enascimento
 */
public class Dashboard implements Serializable{
    
    private Integer totOffice, totWindows, totEmail, totServidorFisisco, totServidorVirtual, totComputador, totNotebook;
    private Integer office2019, office2016, office2013, office2010, office2007, office2003, officeUsado, officeDisponiveis;
    private Integer windows10, windows8_1, windows8, windows7, windowsVista, windowsXp, windowsUsado, windowsDisponiveis;
    private Integer totRemoteDesktop;
    private BigDecimal WindowsOpen, WindowsOem, contrato, nf, idSoft, qtde, usado;
    private String micro, serial, nomeOffice, nomeWindows,nomeSoft;
    private Date data;

    public Integer getTotOffice() {
        return totOffice;
    }

    public void setTotOffice(Integer totOffice) {
        this.totOffice = totOffice;
    }

    public Integer getTotWindows() {
        return totWindows;
    }

    public void setTotWindows(Integer totWindows) {
        this.totWindows = totWindows;
    }

    public Integer getTotEmail() {
        return totEmail;
    }

    public void setTotEmail(Integer totEmail) {
        this.totEmail = totEmail;
    }

    public Integer getTotServidorFisisco() {
        return totServidorFisisco;
    }

    public void setTotServidorFisisco(Integer totServidorFisisco) {
        this.totServidorFisisco = totServidorFisisco;
    }

    public Integer getTotServidorVirtual() {
        return totServidorVirtual;
    }

    public void setTotServidorVirtual(Integer totServidorVirtual) {
        this.totServidorVirtual = totServidorVirtual;
    }

    public Integer getTotComputador() {
        return totComputador;
    }

    public void setTotComputador(Integer totComputador) {
        this.totComputador = totComputador;
    }

    public Integer getTotNotebook() {
        return totNotebook;
    }

    public void setTotNotebook(Integer totNotebook) {
        this.totNotebook = totNotebook;
    }

    public Integer getOffice2019() {
        return office2019;
    }

    public void setOffice2019(Integer office2019) {
        this.office2019 = office2019;
    }

    public Integer getOffice2016() {
        return office2016;
    }

    public void setOffice2016(Integer office2016) {
        this.office2016 = office2016;
    }

    public Integer getOffice2013() {
        return office2013;
    }

    public void setOffice2013(Integer office2013) {
        this.office2013 = office2013;
    }

    public Integer getOffice2010() {
        return office2010;
    }

    public void setOffice2010(Integer office2010) {
        this.office2010 = office2010;
    }

    public Integer getOffice2007() {
        return office2007;
    }

    public void setOffice2007(Integer office2007) {
        this.office2007 = office2007;
    }

    public Integer getOffice2003() {
        return office2003;
    }

    public void setOffice2003(Integer office2003) {
        this.office2003 = office2003;
    }

    

    public Integer getWindows10() {
        return windows10;
    }

    public void setWindows10(Integer windows10) {
        this.windows10 = windows10;
    }

    public Integer getWindows8_1() {
        return windows8_1;
    }

    public void setWindows8_1(Integer windows8_1) {
        this.windows8_1 = windows8_1;
    }

    public Integer getWindows8() {
        return windows8;
    }

    public void setWindows8(Integer windows8) {
        this.windows8 = windows8;
    }

    public Integer getWindows7() {
        return windows7;
    }

    public void setWindows7(Integer windows7) {
        this.windows7 = windows7;
    }

    public Integer getWindowsVista() {
        return windowsVista;
    }

    public void setWindowsVista(Integer windowsVista) {
        this.windowsVista = windowsVista;
    }

    public Integer getWindowsXp() {
        return windowsXp;
    }

    public void setWindowsXp(Integer windowsXp) {
        this.windowsXp = windowsXp;
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

    public BigDecimal getContrato() {
        return contrato;
    }

    public void setContrato(BigDecimal contrato) {
        this.contrato = contrato;
    }

    public String getMicro() {
        return micro;
    }

    public void setMicro(String micro) {
        this.micro = micro;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getWindowsUsado() {
        return windowsUsado;
    }

    public void setWindowsUsado(Integer windowsUsado) {
        this.windowsUsado = windowsUsado;
    }

    public Integer getOfficeUsado() {
        return officeUsado;
    }

    public void setOfficeUsado(Integer officeUsado) {
        this.officeUsado = officeUsado;
    }

    public BigDecimal getNf() {
        return nf;
    }

    public void setNf(BigDecimal nf) {
        this.nf = nf;
    }

    public String getNomeOffice() {
        return nomeOffice;
    }

    public void setNomeOffice(String nomeOffice) {
        this.nomeOffice = nomeOffice;
    }

    public String getNomeWindows() {
        return nomeWindows;
    }

    public void setNomeWindows(String nomeWindows) {
        this.nomeWindows = nomeWindows;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getIdSoft() {
        return idSoft;
    }

    public void setIdSoft(BigDecimal idSoft) {
        this.idSoft = idSoft;
    }

    public Integer getOfficeDisponiveis() {
        return officeDisponiveis;
    }

    public void setOfficeDisponiveis(Integer officeDisponiveis) {
        this.officeDisponiveis = officeDisponiveis;
    }

    public Integer getWindowsDisponiveis() {
        return windowsDisponiveis;
    }

    public void setWindowsDisponiveis(Integer windowsDisponiveis) {
        this.windowsDisponiveis = windowsDisponiveis;
    }

    public BigDecimal getQtde() {
        return qtde;
    }

    public void setQtde(BigDecimal qtde) {
        this.qtde = qtde;
    }

    public BigDecimal getUsado() {
        return usado;
    }

    public void setUsado(BigDecimal usado) {
        this.usado = usado;
    }

    public String getNomeSoft() {
        return nomeSoft;
    }

    public void setNomeSoft(String nomeSoft) {
        this.nomeSoft = nomeSoft;
    }

 
    
    
}
