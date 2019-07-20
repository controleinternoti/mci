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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.LinearAxis;

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
    private BarChartModel barModel;
    private HorizontalBarChartModel horizontalBarModel;
    private LineChartModel multiAxisModel;
    private CartesianChartModel combinedModelMineral;
    private BigDecimal valorCota;

    public DashboardMB() {
        dao = (Dao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dao");
        novo();
    }

    public void novo() {
        createBarModels();
        combinedModelMineral();
    }

    private void createBarModels() {
        cota();
        //createHorizontalBarModel();
    }

    private void cota() {
        barModel = initCota();
        barModel.setTitle("Cota");
        barModel.setLegendPosition("ne");

        if (getValorCota() == null) {
            barModel.setSeriesColors("e30910");
        } else if (getValorCota().intValue() < 3) {
            barModel.setSeriesColors("03A9F4");
        } else {
            barModel.setSeriesColors("e30910");
        }
//        Axis xAxis = barModel.getAxis(AxisType.X);
//        xAxis.setLabel("Gender");
    
        barModel.setLegendPosition("e");
        barModel.setAnimate(true);
        barModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("");
        yAxis.setMin(8);
        yAxis.setMax(0);
        barModel.setExtender("skinBarCota");
    }

    private BarChartModel initCota() {
        String data = null;
        List<Object[]> results = dao.cota();
        BarChartModel model = new BarChartModel();
//        ChartSeries fixo = new ChartSeries();
//        fixo.setLabel("Fixo");
        ChartSeries apurado = new ChartSeries();
        
        int valor = 0;
        for (Object[] result : results) {
            data = ((String) result[0]);
            setValorCota((BigDecimal) result[2]);
            if (getValorCota().intValue() > 7) {
                valor = 7;
            } else {
                valor = getValorCota().intValue();
            }
            apurado.set(data, valor);
        }
         if (getValorCota().intValue() < 3) {
             apurado.setLabel("Apurado");
         }else{
             apurado.setLabel("Apurado");
         }

        System.out.println("============== " + getValorCota());
        System.out.println("============== " + valor);
//        ChartSeries girls = new ChartSeries();
//        girls.setLabel("Girls");
//        girls.set("2004", 4);

        model.addSeries(apurado);
        //model.addSeries(fixo);
        return model;
    }

    private void combinedModelMineral() {
        combinedModelMineral = new BarChartModel();
        String data;
        BigDecimal vlApurado, vlFrente, meta;
        List<Object[]> results = dao.mineral();

        BarChartSeries apurado = new BarChartSeries();
        apurado.setLabel("Apurado");

        BarChartSeries frente = new BarChartSeries();
        frente.setLabel("Frente");

        LineChartSeries girls = new LineChartSeries();
        girls.setLabel("Meta");

        for (Object[] result : results) {
            data = ((String) result[0]);
            vlApurado = ((BigDecimal) result[1]);
            vlFrente = ((BigDecimal) result[2]);
            meta = ((BigDecimal) result[3]);
            apurado.set(data, vlApurado);
            frente.set(data, vlFrente);
            girls.set(data, meta);

        }

        combinedModelMineral.addSeries(apurado);
        combinedModelMineral.addSeries(frente);
        combinedModelMineral.addSeries(girls);

        combinedModelMineral.setTitle("Impurezas Minerais");
        combinedModelMineral.setLegendPosition("n");
        combinedModelMineral.setLegendRows(1);
        combinedModelMineral.setAnimate(true);
        combinedModelMineral.setLegendPlacement(LegendPlacement.OUTSIDE);
        combinedModelMineral.setMouseoverHighlight(false);
        combinedModelMineral.setShowDatatip(true);
        combinedModelMineral.setShowPointLabels(true);
        Axis yAxis = combinedModelMineral.getAxis(AxisType.Y);
        yAxis.setMin(9);
        yAxis.setMax(0);
        combinedModelMineral.setExtender("skinBarAndLineMinerais");
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
        this.horizontalBarModel = horizontalBarModel;
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

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public BigDecimal getValorCota() {
        return valorCota;
    }

    public void setValorCota(BigDecimal valorCota) {
        this.valorCota = valorCota;
    }

    public LineChartModel getMultiAxisModel() {
        return multiAxisModel;
    }

    public void setMultiAxisModel(LineChartModel multiAxisModel) {
        this.multiAxisModel = multiAxisModel;
    }

    public CartesianChartModel getCombinedModelMineral() {
        return combinedModelMineral;
    }

    public void setCombinedModelMineral(CartesianChartModel combinedModelMineral) {
        this.combinedModelMineral = combinedModelMineral;
    }

}
