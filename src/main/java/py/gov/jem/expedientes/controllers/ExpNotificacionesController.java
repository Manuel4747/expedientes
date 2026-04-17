package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpNotificaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.PrimeFaces;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.RepRecusaciones;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpEstadosNotificacion;
import py.gov.jem.expedientes.models.ExpTiposActuacion;
import py.gov.jem.expedientes.models.Firmas;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "expNotificacionesController")
@ViewScoped
public class ExpNotificacionesController extends AbstractController<ExpNotificaciones> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private FirmasController firmasController;
    private HttpSession session;
    private Personas personaUsuario;
    private ExpNotificaciones docImprimir;
    private String content;
    private String nombre;
    private ParametrosSistema par;
    private String sessionId;
    private Usuarios usuario;
    private AntecedentesRoles rolElegido;
    private final FiltroURL filtroURL = new FiltroURL();
    private String accion;
    private String titulo;
    private ExpTiposActuacion tipoActuacion;
    private ExpEstadosNotificacion estadoLE;
    private ExpEstadosNotificacion estadoNL;
    private String endpoint;
    private Date fechaDesde;
    private Date fechaHasta;
    private String tituloReporte;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getTituloReporte() {
        return tituloReporte;
    }

    public void setTituloReporte(String tituloReporte) {
        this.tituloReporte = tituloReporte;
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {
                if (docImprimir.getActuacion() != null) {

                    byte[] fileByte = null;

                    if (docImprimir.getActuacion().getArchivo() != null) {
                        try {
                            fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + docImprimir.getActuacion().getArchivo()).toPath());
                        } catch (IOException ex) {
                            JsfUtil.addErrorMessage("No tiene documento adjunto");
                            content = "";
                        }
                    }

                    if (fileByte != null) {
                        Date fecha = ejbFacade.getSystemDate();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);

                        String partes[] = docImprimir.getActuacion().getArchivo().split("[.]");
                        String ext = "pdf";

                        if (partes.length > 1) {
                            ext = partes[partes.length - 1];
                        }

                        nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                        FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
                        outputStream.write(fileByte);

                        outputStream.close();

                        // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        // return "http://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;
    }

    public ExpNotificacionesController() {
        // Inform the Abstract parent controller of the concrete ExpNotificaciones Entity
        super(ExpNotificaciones.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        accion = params.get("tipo");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        if (Constantes.ACCION_NOTIFICACION.equals(accion)) {
            titulo = "NOTIFICACIONES";
           // tituloReporte = "Nuevas Causas Ingresadas";
            tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_NOTIFICACION).getSingleResult();
        } else if (Constantes.ACCION_PRIMER.equals(accion)) {
            titulo = "NUEVAS CAUSAS";
            tituloReporte = "Nuevas Causas Ingresadas";
            tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).getSingleResult();
        } else if (Constantes.ACCION_RECUSACION.equals(accion)) {
            titulo = "RECUSACIONES";
            tituloReporte = "Recusaciones";
            tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RECUSACION).getSingleResult();
        } else {
            // JsfUtil.addErrorMessage("Accion no permitida:: " + accion);
            return;
        }

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

        estadoLE = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosNotificacion.findByCodigo", ExpEstadosNotificacion.class).setParameter("codigo", Constantes.ESTADO_NOTIFICACION_LEIDO).getSingleResult();
        estadoNL = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosNotificacion.findByCodigo", ExpEstadosNotificacion.class).setParameter("codigo", Constantes.ESTADO_NOTIFICACION_NO_LEIDO).getSingleResult();

        // ExpEstadosNotificacion estado = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosNotificacion.findByCodigo", ExpEstadosNotificacion.class).setParameter("codigo", Constantes.ESTADO_NOTIFICACION_NO_LEIDO).getSingleResult();
        // setItems(this.ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByEstado", ExpNotificaciones.class).setParameter("estado", estado).getResultList());
        buscar();
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    @Override
    public Collection<ExpNotificaciones> getItems() {
        return super.getItems2();
    }

    public String color(ExpNotificaciones not) {
        return (not == null) ? "" : (Constantes.ESTADO_NOTIFICACION_NO_LEIDO.equals(not.getEstado().getCodigo()) ? "red" : "green");
    }

    public void prepareDialogoVerDoc(ExpNotificaciones doc) {

        if (doc != null) {

            Date fecha = ejbFacade.getSystemDate();

            doc.setFechaHoraLectura(fecha);

            ExpEstadosNotificacion estado = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosNotificacion.findByCodigo", ExpEstadosNotificacion.class).setParameter("codigo", Constantes.ESTADO_NOTIFICACION_LEIDO).getSingleResult();
            doc.setEstado(estado);

            setSelected(doc);
            this.save(null);
            setSelected(null);
        }

        docImprimir = doc;
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    public void navigateActuacion(ExpNotificaciones not) {
        try {
            Date fecha = ejbFacade.getSystemDate();

            not.setFechaHoraLectura(fecha);
            not.setEstado(estadoLE);
            setSelected(not);
            this.save(null);
            setSelected(null);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", String.valueOf(not.getActuacion().getId()));
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudiciales/index.xhtml?tipo=consulta");
        } catch (IOException ex) {
        }
    }

    public List<ExpNotificaciones> obtenerNotificaciones(ExpTiposActuacion tipoActuacion, ExpEstadosNotificacion estadoNL, Personas per, AntecedentesRoles rol) {

        List<ExpNotificaciones> lista = null;
        if (rol != null && filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rol.getId())) {
            if (fechaDesde != null && fechaHasta != null) {
                lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findAllTipoActuacionEstadoFiltroFecha", ExpNotificaciones.class)
                        .setParameter("tipoActuacion", tipoActuacion)
                        .setParameter("estado", estadoNL)
                        .setParameter("fechaDesde", fechaDesde)
                        .setParameter("fechaHasta", fechaHasta)
                        .getResultList();
            } else {
                lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findAllTipoActuacionEstado", ExpNotificaciones.class).setParameter("tipoActuacion", tipoActuacion).setParameter("estado", estadoNL).getResultList();
            }
        } else {
            if (fechaDesde != null && fechaHasta != null) {
                lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByDestinatarioTipoActuacionEstadoVisibleFiltroFecha", ExpNotificaciones.class)
                        .setParameter("destinatario", per)
                        .setParameter("tipoActuacion", tipoActuacion)
                        .setParameter("estado", estadoNL)
                        .setParameter("visible", true)
                        .setParameter("fechaDesde", fechaDesde)
                        .setParameter("fechaHasta", fechaHasta)
                        .getResultList();

            } else {
                lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByDestinatarioTipoActuacionEstadoVisible", ExpNotificaciones.class).setParameter("destinatario", per).setParameter("tipoActuacion", tipoActuacion).setParameter("estado", estadoNL).setParameter("visible", true).getResultList();

            }
        }

        return lista;
    }

    public void buscar() {

        setItems(obtenerNotificaciones(tipoActuacion, estadoNL, personaUsuario, rolElegido));

        /*
        if(filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())){
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findAllTipoActuacionEstado", ExpNotificaciones.class).setParameter("tipoActuacion", tipoActuacion).setParameter("estado", estadoNL).getResultList());
        }else{
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByDestinatarioTipoActuacionEstadoVisible", ExpNotificaciones.class).setParameter("destinatario", personaUsuario).setParameter("tipoActuacion", tipoActuacion).setParameter("estado", estadoNL).setParameter("visible", true).getResultList());
        }
         */
    }

    public void firmar() {

        Date fecha = ejbFacade.getSystemDate();
        Firmas firma = null;
        /*
        try{
            firma = ejbFacade.getEntityManager().createNamedQuery("Firmas.findBySesion", Firmas.class).setParameter("sesion", sessionId).setParameter("estado", "IN").setParameter("fechaHora", fecha).getSingleResult();
        }catch(Exception ex){
            
        }
         */

        if (firma == null) {
            firma = firmasController.prepareCreate(null);

            firma.setActuacion(docImprimir.getActuacion());
            firma.setEmpresa(usuario.getEmpresa());
            firma.setFechaHora(fecha);
            firma.setEstado("PE");
            firma.setDocumentoJudicial(docImprimir.getDocumentoJudicial());
            firma.setSesion(sessionId);
            firma.setPersona(personaUsuario);

            firmasController.setSelected(firma);
            firmasController.saveNew(null);

        }

        int cont = 30;
        Firmas firma2 = null;

        while (cont >= 0) {

            try {
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                firma2 = ejbFacade.getEntityManager().createNamedQuery("Firmas.findById", Firmas.class).setParameter("id", firma.getId()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                cont = 0;
                break;
            }

            System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);

            if (!firma2.getEstado().equals("PE")) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            cont--;
        }

        if (cont > 0) {

            if (firma2 != null) {
                if (firma2.getEstado().equals("AC")) {
                    if (docImprimir != null) {
                        docImprimir.setActuacion(ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", docImprimir.getId()).getSingleResult());
                    }

                } else {
                    JsfUtil.addErrorMessage("Error en proceso");
                }
            } else {
                JsfUtil.addErrorMessage("Error en proceso.");
            }
        } else {
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasController.setSelected(firma2);
                firmasController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }

    }

    public void Recusaciones(boolean generarPdf) {

        HttpServletResponse httpServletResponse = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = null;

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            Collection<ExpNotificaciones> lista = getItems();
            /* ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByDestinatarioTipoActuacionEstadoVisibleFiltroFecha", ExpNotificaciones.class)
                        .setParameter("destinatario", per)
                        .setParameter("tipoActuacion", tipoActuacion)
                        .setParameter("estado", estadoNL)
                        .setParameter("visible", true)
                        .setParameter("fechaDesde", fechaDesde)
                        .setParameter("fechaHasta", fechaHasta)
                        .getResultList();*/

            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            List<RepRecusaciones> listafinal2 = new ArrayList<>();
            RepRecusaciones item = null;
            for (ExpNotificaciones det : lista) {

                item = new RepRecusaciones();
                item.setFechaHoraAlta(det.getFechaHoraAlta());
                item.setCausa(det.getDocumentoJudicial().getCausa());
                item.setCaratula(det.getDocumentoJudicial() == null ? "" : (det.getDocumentoJudicial() == null ? "" : det.getDocumentoJudicial().getCaratula()));
                item.setMotivoProceso(det.getDocumentoJudicial() == null ? "" : (det.getDocumentoJudicial() == null ? "" : det.getDocumentoJudicial().getMotivoProcesoString()));
                item.setRemitente(det.getRemitente().getNombresApellidos());

                listafinal2.add(item);

            }

            beanCollectionDataSource = new JRBeanCollectionDataSource(listafinal2);

            HashMap map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date fecha = ejbFacade.getSystemDate();

            map.put("fecha", format.format(fecha));
            map.put("fechaDesde", format2.format(fechaDesde));
            map.put("fechaHasta", format2.format(fechaHasta));
            map.put("tituloReporte", tituloReporte);

            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;
            if (generarPdf) {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repRecusaciones.jasper");
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                servletOutputStream = httpServletResponse.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

            } else {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repRecusaciones.jasper");
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                servletOutputStream = httpServletResponse.getOutputStream();

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                exporter.exportReport();
            }

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }
    }

}
