package py.gov.jem.expedientes.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import py.gov.jem.expedientes.datasource.CantidadAntecedentesPorAnio;
import py.gov.jem.expedientes.datasource.CantidadItem;
import py.gov.jem.expedientes.datasource.ChartExpedientesPorDia;
import py.gov.jem.expedientes.datasource.ChartUsuariosPorDia;

@Named(value = "chartsController")
@ViewScoped
public class ChartsController extends AbstractController<ChartUsuariosPorDia> {

    @Inject
    private EmpresasController empresaController;

    private BarChartModel modelUsuariosPorDiaBar;
    private BarChartModel modelExpedientesPorDiaBar;
    private BarChartModel modelAntecedentesPorAnio;
    private Date fechaDesde;
    private Date fechaHasta;
    private String tipoGrafico;
    private List<String> coloresFondo;
    private List<String> coloresBorde;
    private Integer usuariosCreados;
    private Integer magistradosTotal;
    private Integer expedientesTotal;
    private Integer usuariosCreadosRango;
    private Integer magistradosTotalRango;
    private Integer expedientesTotalRango;
    private String titulo;

    public Integer getUsuariosCreadosRango() {
        return usuariosCreadosRango;
    }

    public void setUsuariosCreadosRango(Integer usuariosCreadosRango) {
        this.usuariosCreadosRango = usuariosCreadosRango;
    }

    public Integer getMagistradosTotalRango() {
        return magistradosTotalRango;
    }

    public void setMagistradosTotalRango(Integer magistradosTotalRango) {
        this.magistradosTotalRango = magistradosTotalRango;
    }

    public Integer getExpedientesTotalRango() {
        return expedientesTotalRango;
    }

    public void setExpedientesTotalRango(Integer expedientesTotalRango) {
        this.expedientesTotalRango = expedientesTotalRango;
    }

    public Integer getExpedientesTotal() {
        return expedientesTotal;
    }

    // "31a00d,fffb2f,4a148c,827717,0d47a1,f57f17,FFFF00,bf360c,3e2723,880e4f,b71c1c"
    public void setExpedientesTotal(Integer expedientesTotal) {
        this.expedientesTotal = expedientesTotal;
    }

    public String getTipoGrafico() {
        return tipoGrafico;
    }

    public void setTipoGrafico(String tipoGrafico) {
        this.tipoGrafico = tipoGrafico;
    }

    public BarChartModel getModelExpedientesPorDiaBar() {
        return modelExpedientesPorDiaBar;
    }

    public void setModelExpedientesPorDiaBar(BarChartModel modelExpedientesPorDiaBar) {
        this.modelExpedientesPorDiaBar = modelExpedientesPorDiaBar;
    }

    public BarChartModel getModelUsuariosPorDiaBar() {
        return modelUsuariosPorDiaBar;
    }

    public void setModelUsuariosPorDiaBar(BarChartModel modelUsuariosPorDiaBar) {
        this.modelUsuariosPorDiaBar = modelUsuariosPorDiaBar;
    }

    public BarChartModel getModelAntecedentesPorAnio() {
        return modelAntecedentesPorAnio;
    }

    public void setModelAntecedentesPorAnio(BarChartModel modelAntecedentesPorAnio) {
        this.modelAntecedentesPorAnio = modelAntecedentesPorAnio;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Integer getUsuariosCreados() {
        return usuariosCreados;
    }

    public void setUsuariosCreados(Integer usuariosCreados) {
        this.usuariosCreados = usuariosCreados;
    }

    public Integer getMagistradosTotal() {
        return magistradosTotal;
    }

    public void setMagistradosTotal(Integer magistradosTotal) {
        this.magistradosTotal = magistradosTotal;
    }

    public ChartsController() {
        // Inform the Abstract parent controller of the concrete VComprasPorRubroPorMes Entity
        super(ChartUsuariosPorDia.class);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    @PostConstruct
    public void init() {

        coloresFondo = new ArrayList<>();
        /*
        COLORES_CHART.add("#31a00d");
        COLORES_CHART.add("#fffb2f");
        COLORES_CHART.add("#4a148c");
        COLORES_CHART.add("#827717");
        COLORES_CHART.add("#0d47a1");
        COLORES_CHART.add("#f57f17");
        COLORES_CHART.add("#FFFF00");
        COLORES_CHART.add("#bf360c");
        COLORES_CHART.add("#3e2723");
        COLORES_CHART.add("#880e4f");
        COLORES_CHART.add("#b71c1c");
         */

        coloresFondo.add("rgba(255, 159, 64, 0.2)");
        coloresFondo.add("rgba(75, 192, 192, 0.2)");
        coloresFondo.add("rgba(255, 99, 132, 0.2)");
        coloresFondo.add("rgba(54, 162, 235, 0.2)");
        coloresFondo.add("rgba(255, 205, 86, 0.2)");
        coloresFondo.add("rgba(153, 102, 255, 0.2)");
        coloresFondo.add("rgba(201, 203, 207, 0.2)");

        coloresBorde = new ArrayList<>();

        coloresBorde.add("rgb(255, 159, 64)");
        coloresBorde.add("rgb(75, 192, 192)");
        coloresBorde.add("rgb(255, 99, 132)");
        coloresBorde.add("rgb(54, 162, 235)");
        coloresBorde.add("rgb(255, 205, 86)");
        coloresBorde.add("rgb(153, 102, 255)");
        coloresBorde.add("rgb(201, 203, 207)");

        tipoGrafico = "Barras";
        fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        fechaHasta = ejbFacade.getSystemDateOnly();
        todos();

    }

    public void todos() {
        usuariosPorDia();
        expedientesPorDia();
        antecedentesPorAnio();
    }

    public void expedientesPorDia() {
        expedientesPorDiaBarras();
    }

    public void expedientesPorDiaBarras() {
        List<ChartExpedientesPorDia> lista = null;

        Date fechaActual = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaCero = null;
        try {
            fechaCero = sdf.parse("1900-01-01");
        } catch (ParseException ex) {
        }

        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select count(*) as cantidad from documentos_judiciales where expediente_viejo = false;",
                 CantidadItem.class);
        CantidadItem cantidadItem = (CantidadItem) query.getSingleResult();
        expedientesTotal = cantidadItem.getCantidad();

        javax.persistence.Query query2 = ejbFacade.getEntityManager().createNativeQuery(
                "select count(*) as cantidad from documentos_judiciales where expediente_viejo = false and fecha_hora_alta >= '" + sdf.format(fechaDesde) + "' and fecha_hora_alta <= '" + sdf.format(fechaHasta) + "'",
                 CantidadItem.class);
        CantidadItem cantidadItem2 = (CantidadItem) query2.getSingleResult();
        expedientesTotalRango = cantidadItem2.getCantidad();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaHasta);
        cal2.add(Calendar.DATE, 1);
        Date fechaHastaNueva = cal2.getTime();

        if (fechaDesde == null) {
            fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        }

        if (fechaHasta == null) {
            fechaHasta = ejbFacade.getSystemDateOnly();
        }

        titulo = "Expedientes ingresados entre " + sdf2.format(fechaDesde) + " y " + sdf2.format(fechaHasta);

        ChartExpedientesPorDia envio = null;

        Integer cantMax = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");

        Calendar cal = Calendar.getInstance();

        ChartExpedientesPorDia nodo = null;

        ChartExpedientesPorDia nodoCero = new ChartExpedientesPorDia();

        nodoCero.setCantidad(0);

        modelExpedientesPorDiaBar = new BarChartModel();
        ChartData data = new ChartData();

        /*
        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
         
        List<String> borderColor = new ArrayList<>();
        
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
         */
        List<String> labels = new ArrayList<>();

        List<ChartExpedientesPorDia> listaTemp;

        BarChartDataSet barDataSet;

        lista = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        boolean completarLabels = true;

        int posColor = 0;

        //while (it3.hasNext()) {
        listaTemp = ejbFacade.getEntityManager().createNamedQuery("ChartExpedientesPorDia.findByRangoFechaAlta", ChartExpedientesPorDia.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();
        lista.addAll(listaTemp);

        Iterator<ChartExpedientesPorDia> it2 = listaTemp.iterator();

        envio = new ChartExpedientesPorDia();
        envio.setFechaAlta(fechaCero);

        boolean iterar = true;

        fechaActual.setTime(fechaDesde.getTime());

        barDataSet = new BarChartDataSet();
        barDataSet.setBackgroundColor(coloresFondo.get(posColor));

        barDataSet.setBorderColor(coloresBorde.get(posColor));
        barDataSet.setBorderWidth(1);

        posColor++;
        if (posColor >= coloresFondo.size()) {
            posColor = 0;
        }

        values = new ArrayList<>();
        while (fechaActual.compareTo(fechaHasta) <= 0) {

            if (iterar) {
                if (it2.hasNext()) {
                    envio = it2.next();
                }
                iterar = false;
            }

            if (envio.getFechaAlta().compareTo(fechaActual) != 0) {
                nodoCero.setFechaAlta(fechaActual);
                nodo = nodoCero;
            } else {
                nodo = envio;
                iterar = true;
            }

            // series1.set(format.format(nodo.getFechaPresentacion()), nodo.getCantidad());
            barDataSet.setLabel("Expedientes");

            values.add(nodo.getCantidad());

            if (completarLabels) {
                labels.add(format.format(nodo.getFechaAlta()));
            }

            if (cantMax < nodo.getCantidad()) {
                cantMax = nodo.getCantidad();
            }

            cal.setTime(fechaActual);
            cal.add(Calendar.DATE, 1);
            fechaActual = cal.getTime();
        }

        completarLabels = false;

        barDataSet.setData(values);
        data.addChartDataSet(barDataSet);

        //}
        data.setLabels(labels);
        modelExpedientesPorDiaBar.setData(data);
        /*
       cantMax = ((Double) (cantMax * 1.5)).intValue();

       //cantMax = cantMax * 2;
       
        // modelGestionesMesaPorDia.setSeriesColors("A30303,58BA27,FFCC33,F74A4A,F52F2F");
        modelGestionesMesaPorDia.setSeriesColors(COLORES_CHART);
        // modelSmsHora.setTitle("Cantidad de SMS's promerio por hora en los ultimos 30 dias");

        modelGestionesMesaPorDia.setLegendPosition(
                "e");
        modelGestionesMesaPorDia.setLegendPlacement(LegendPlacement.OUTSIDE);

        modelGestionesMesaPorDia.setLegendCols(
                2);
        modelGestionesMesaPorDia.setZoom(
                true);
        modelGestionesMesaPorDia.setShowPointLabels(
                true);
        modelGestionesMesaPorDia.getAxes()
                .put(AxisType.X, new CategoryAxis("Dia"));
        Axis yAxis = modelGestionesMesaPorDia.getAxis(AxisType.Y);

        yAxis.setTickInterval("2");
        yAxis.setLabel(
                "Cantidad");
        yAxis.setMin(
                0);
        yAxis.setTickFormat(
                "%'.0f");
        // yAxis.setMax(2100);
        yAxis.setMax(cantMax);

        Axis xAxis = modelGestionesMesaPorDia.getAxis(AxisType.X);

        xAxis.setTickAngle(
                30);
         */
    }

    public void usuariosPorDia() {
        usuariosPorDiaBarras();
    }

    public void usuariosPorDiaBarras() {
        List<ChartUsuariosPorDia> lista = null;

        Date fechaActual = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaCero = null;
        try {
            fechaCero = sdf.parse("1900-01-01");
        } catch (ParseException ex) {
        }

        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select count(*) as cantidad from personas where id in (select distinct(p.persona) from antecedentes_roles_por_personas as p where p.rol not in (1)) and estado = 'AC' and usuario is not null",
                 CantidadItem.class);
        CantidadItem cantidadItem = (CantidadItem) query.getSingleResult();
        usuariosCreados = cantidadItem.getCantidad();

        javax.persistence.Query query2 = ejbFacade.getEntityManager().createNativeQuery(
                "select count(*) as cantidad from personas where id in (select distinct(p.persona) from antecedentes_roles_por_personas as p where p.rol in (2,13)) and estado = 'AC' and usuario is not null",
                 CantidadItem.class);
        CantidadItem cantidadItem2 = (CantidadItem) query2.getSingleResult();
        magistradosTotal = cantidadItem2.getCantidad();

        javax.persistence.Query query3 = ejbFacade.getEntityManager().createNativeQuery(
                "select count(*) as cantidad from personas where id in (select distinct(p.persona) from antecedentes_roles_por_personas as p where p.rol not in (1)) and estado = 'AC' and usuario is not null and fecha_hora_alta >= '" + sdf.format(fechaDesde) + "' and fecha_hora_alta <= '" + sdf.format(fechaHasta) + "'",
                 CantidadItem.class);
        CantidadItem cantidadItem3 = (CantidadItem) query3.getSingleResult();
        usuariosCreadosRango = cantidadItem3.getCantidad();

        javax.persistence.Query query4 = ejbFacade.getEntityManager().createNativeQuery(
                "select count(*) as cantidad from personas where id in (select distinct(p.persona) from antecedentes_roles_por_personas as p where p.rol in (2,13)) and estado = 'AC' and usuario is not null and fecha_hora_alta >= '" + sdf.format(fechaDesde) + "' and fecha_hora_alta <= '" + sdf.format(fechaHasta) + "'",
                 CantidadItem.class);
        CantidadItem cantidadItem4 = (CantidadItem) query4.getSingleResult();
        magistradosTotalRango = cantidadItem4.getCantidad();

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaHasta);
        cal2.add(Calendar.DATE, 1);
        Date fechaHastaNueva = cal2.getTime();

        if (fechaDesde == null) {
            fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        }

        if (fechaHasta == null) {
            fechaHasta = ejbFacade.getSystemDateOnly();
        }

        titulo = "Usuarios Generados entre " + sdf2.format(fechaDesde) + " y " + sdf2.format(fechaHasta);

        ChartUsuariosPorDia envio = null;

        Integer cantMax = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");

        Calendar cal = Calendar.getInstance();

        ChartUsuariosPorDia nodo = null;

        ChartUsuariosPorDia nodoCero = new ChartUsuariosPorDia();

        nodoCero.setCantidad(0);

        modelUsuariosPorDiaBar = new BarChartModel();
        ChartData data = new ChartData();

        /*
        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
         
        List<String> borderColor = new ArrayList<>();
        
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
         */
        List<String> labels = new ArrayList<>();

        List<ChartUsuariosPorDia> listaTemp;

        BarChartDataSet barDataSet;

        lista = new ArrayList<>();
        List<Number> values = new ArrayList<>();

        boolean completarLabels = true;

        int posColor = 0;

        //while (it3.hasNext()) {
        listaTemp = ejbFacade.getEntityManager().createNamedQuery("ChartUsuariosPorDia.findByRangoFechaAlta", ChartUsuariosPorDia.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();
        lista.addAll(listaTemp);

        Iterator<ChartUsuariosPorDia> it2 = listaTemp.iterator();

        envio = new ChartUsuariosPorDia();
        envio.setFechaAlta(fechaCero);

        boolean iterar = true;

        fechaActual.setTime(fechaDesde.getTime());

        barDataSet = new BarChartDataSet();
        barDataSet.setBackgroundColor(coloresFondo.get(posColor));

        barDataSet.setBorderColor(coloresBorde.get(posColor));
        barDataSet.setBorderWidth(1);

        posColor++;
        if (posColor >= coloresFondo.size()) {
            posColor = 0;
        }

        values = new ArrayList<>();
        while (fechaActual.compareTo(fechaHasta) <= 0) {

            if (iterar) {
                if (it2.hasNext()) {
                    envio = it2.next();
                }
                iterar = false;
            }

            if (envio.getFechaAlta().compareTo(fechaActual) != 0) {
                nodoCero.setFechaAlta(fechaActual);
                nodo = nodoCero;
            } else {
                nodo = envio;
                iterar = true;
            }

            // series1.set(format.format(nodo.getFechaPresentacion()), nodo.getCantidad());
            barDataSet.setLabel("Usuarios");

            values.add(nodo.getCantidad());

            if (completarLabels) {
                labels.add(format.format(nodo.getFechaAlta()));
            }

            if (cantMax < nodo.getCantidad()) {
                cantMax = nodo.getCantidad();
            }

            cal.setTime(fechaActual);
            cal.add(Calendar.DATE, 1);
            fechaActual = cal.getTime();
        }

        completarLabels = false;

        barDataSet.setData(values);
        data.addChartDataSet(barDataSet);

        //}
        data.setLabels(labels);
        modelUsuariosPorDiaBar.setData(data);
        /*
       cantMax = ((Double) (cantMax * 1.5)).intValue();

       //cantMax = cantMax * 2;
       
        // modelGestionesMesaPorDia.setSeriesColors("A30303,58BA27,FFCC33,F74A4A,F52F2F");
        modelGestionesMesaPorDia.setSeriesColors(COLORES_CHART);
        // modelSmsHora.setTitle("Cantidad de SMS's promerio por hora en los ultimos 30 dias");

        modelGestionesMesaPorDia.setLegendPosition(
                "e");
        modelGestionesMesaPorDia.setLegendPlacement(LegendPlacement.OUTSIDE);

        modelGestionesMesaPorDia.setLegendCols(
                2);
        modelGestionesMesaPorDia.setZoom(
                true);
        modelGestionesMesaPorDia.setShowPointLabels(
                true);
        modelGestionesMesaPorDia.getAxes()
                .put(AxisType.X, new CategoryAxis("Dia"));
        Axis yAxis = modelGestionesMesaPorDia.getAxis(AxisType.Y);

        yAxis.setTickInterval("2");
        yAxis.setLabel(
                "Cantidad");
        yAxis.setMin(
                0);
        yAxis.setTickFormat(
                "%'.0f");
        // yAxis.setMax(2100);
        yAxis.setMax(cantMax);

        Axis xAxis = modelGestionesMesaPorDia.getAxis(AxisType.X);

        xAxis.setTickAngle(
                30);
         */
    }
    
    public void antecedentesPorAnio() {
        antecedentesPorAnioBarras();
    }
    
    public void antecedentesPorAnioBarras() {
        List<CantidadAntecedentesPorAnio> antecedentes = ejbFacade.getEntityManager().createNativeQuery("select YEAR(fecha_hora_respuesta) as anio, count(*) cantidad\n"
                + "from exp_pedidos_antecedente\n"
                + "where estado <> 'AC'\n"
                + "group by YEAR(fecha_hora_respuesta);", CantidadAntecedentesPorAnio.class)
                .getResultList();
        
        modelAntecedentesPorAnio = new BarChartModel();
        ChartData data = new ChartData();
        
        BarChartDataSet barDataSet;
        
        List<String> labels = new ArrayList<>();
        
        int posColor = 0;
        
        posColor++;
        if (posColor >= coloresFondo.size()) {
            posColor = 0;
        }

        barDataSet = new BarChartDataSet();
        barDataSet.setBackgroundColor(coloresFondo.get(posColor));

        barDataSet.setBorderColor(coloresBorde.get(posColor));
        barDataSet.setBorderWidth(1);
        
        Iterator<CantidadAntecedentesPorAnio> iter = antecedentes.iterator();
        
        ArrayList<Number> values = new ArrayList<>();
        while(iter.hasNext()) {
            CantidadAntecedentesPorAnio item = iter.next();
            values.add(item.getCantidad());
            labels.add(String.valueOf(item.getAnio()));
        }
        
        barDataSet.setData(values);
        data.addChartDataSet(barDataSet);
        

        //}
        data.setLabels(labels);
        modelAntecedentesPorAnio.setData(data);
    }
    /*
    public void usuariosPorDiaBarras() {
        List<ChartUsuariosPorDia> lista = null;

        Date fechaActual = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaCero = null;
        try {
            fechaCero = sdf.parse("1900-01-01");
        } catch (ParseException ex) {
        }

        if (fechaDesde == null) {
            fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        }

        if (fechaHasta == null) {
            fechaHasta = ejbFacade.getSystemDateOnly();
        }

        ChartUsuariosPorDia envio = null;

        Integer cantMax = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM");


        Calendar cal = Calendar.getInstance();
        
        ChartUsuariosPorDia nodo = null;

        ChartUsuariosPorDia nodoCero = new ChartUsuariosPorDia();

        nodoCero.setCantidad(0);

        modelUsuariosPorDiaBar = new BarChartModel();
        ChartData data = new ChartData();
         
         
        List<String> labels = new ArrayList<>();

        List<ChartUsuariosPorDia> listaTemp;
        
        BarChartDataSet barDataSet;

        lista = new ArrayList<>();
        List<Number> values = new ArrayList<>();
        
        boolean completarLabels = true;

        int posColor = 0;
        
        //while (it3.hasNext()) {

            listaTemp = ejbFacade.getEntityManager().createNamedQuery("ChartUsuariosPorDia.findByRangoFechaAlta", ChartUsuariosPorDia.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList();
            lista.addAll(listaTemp);

            Iterator<ChartUsuariosPorDia> it2 = listaTemp.iterator();

            envio = new ChartUsuariosPorDia();
            envio.setFechaAlta(fechaCero);

            boolean iterar = true;

            fechaActual.setTime(fechaDesde.getTime());
            
            barDataSet = new BarChartDataSet();
            barDataSet.setBackgroundColor(coloresFondo.get(posColor));
            
            barDataSet.setBorderColor(coloresBorde.get(posColor));
            barDataSet.setBorderWidth(1);
            
            posColor++;
            if(posColor >= coloresFondo.size()){
                posColor = 0;
            }

            values = new ArrayList<>();
            while (fechaActual.compareTo(fechaHasta) <= 0) {

                if (iterar) {
                    if (it2.hasNext()) {
                        envio = it2.next();
                    }
                    iterar = false;
                }

                if (envio.getFechaAlta().compareTo(fechaActual) != 0) {
                    nodoCero.setFechaAlta(fechaActual);
                    nodo = nodoCero;
                } else {
                    nodo = envio;
                    iterar = true;
                }

                // series1.set(format.format(nodo.getFechaPresentacion()), nodo.getCantidad());
            
                barDataSet.setLabel("Antecedentes");
                
                values.add(nodo.getCantidad());
                
                if(completarLabels){
                    labels.add(format.format(nodo.getFechaAlta()));
                }
                
                if (cantMax < nodo.getCantidad()) {
                    cantMax = nodo.getCantidad();
                }

                cal.setTime(fechaActual);
                cal.add(Calendar.DATE, 1);
                fechaActual = cal.getTime();
            }
            
            
            completarLabels = false;
            
            
            barDataSet.setData(values);
            data.addChartDataSet(barDataSet);

        //}
        
        
        data.setLabels(labels);
        modelUsuariosPorDiaBar.setData(data);
        
    }
     */
}
