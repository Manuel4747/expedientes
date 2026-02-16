package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpDetallesOrdenDia;
import py.gov.jem.expedientes.models.ExpOrdenesDia;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "ordenesDiaController")
@ViewScoped
public class ExpOrdenesDiaController extends AbstractController<ExpOrdenesDia> {

    @Inject
    private PersonasController personasController;
    @Inject
    private ExpDetallesOrdenDiaController detalleOrdenDiaController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;

    private Date fechaDesde;
    private Date fechaHasta;
    private Personas personaUsuario;
    private List<ExpOrdenesDia> listaOrdenesDia;
    private List<ExpDetallesOrdenDia> listaDetallesOrdenDia;
    private ExpDetallesOrdenDia detalleOrdenDiaSelected;
    private List<DocumentosJudiciales> listaExpedientes;
    private CanalesEntradaDocumentoJudicial canal;
    private final FiltroURL filtroURL = new FiltroURL();
    private AntecedentesRoles rolElegido;
    private String endpoint;

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

    public PersonasController getPersonasController() {
        return personasController;
    }

    public void setPersonasController(PersonasController personasController) {
        this.personasController = personasController;
    }

    public List<ExpOrdenesDia> getListaOrdenesDia() {
        return listaOrdenesDia;
    }

    public void setListaOrdenesDia(List<ExpOrdenesDia> listaOrdenesDia) {
        this.listaOrdenesDia = listaOrdenesDia;
    }

    public List<ExpDetallesOrdenDia> getListaDetallesOrdenDia() {
        return listaDetallesOrdenDia;
    }

    public void setListaDetallesOrdenDia(List<ExpDetallesOrdenDia> listaDetallesOrdenDia) {
        this.listaDetallesOrdenDia = listaDetallesOrdenDia;
    }

    public ExpDetallesOrdenDia getDetalleOrdenDiaSelected() {
        return detalleOrdenDiaSelected;
    }

    public void setDetalleOrdenDiaSelected(ExpDetallesOrdenDia detalleOrdenDiaSelected) {
        this.detalleOrdenDiaSelected = detalleOrdenDiaSelected;
    }

    public List<DocumentosJudiciales> getListaExpedientes() {
        return listaExpedientes;
    }

    public void setListaExpedientes(List<DocumentosJudiciales> listaExpedientes) {
        this.listaExpedientes = listaExpedientes;
    }

    public ExpOrdenesDiaController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(ExpOrdenesDia.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE);

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");

        fechaDesde = ejbFacade.getSystemDateOnly();
        fechaHasta = ejbFacade.getSystemDateOnly();

        buscarPorFecha();
    }

    public boolean deshabilitarAdminOrdenDia() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_ORDEN_DIA, rolElegido.getId())) {
            return false;
        }
        return true;
    }

    public List<ExpOrdenesDia> obtenerOrdenesDelDia(Date fecha) {

        List<ExpOrdenesDia> lista = null;
        //if (!deshabilitarAdminOrdenDia()) {
            lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpOrdenesDia.findByFechaSesion", ExpOrdenesDia.class).setParameter("fechaDesde", fechaDesde).getResultList();
        /*} else {
            lista = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_ordenes_dia as a, exp_detalles_orden_dia as d \n"
                    + "where a.id = d.orden_dia and d.documento_judicial in (select b.documento_judicial from exp_partes_por_documentos_judiciales as b where b.persona = ?1 union select c.documento_judicial from personas_por_documentos_judiciales as c where c.persona = ?2) \n"
                    + "and a.visible = true and a.fecha_sesion >= ?3 order by a.fecha_hora_alta", ExpOrdenesDia.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).setParameter(3, fecha).getResultList();
        }*/
        
        return lista;

    }
    
    public void buscarPorFecha() {
        listaOrdenesDia = obtenerOrdenesDelDia(fechaDesde);
        setSelected(null);
        listaDetallesOrdenDia = null;
        detalleOrdenDiaSelected = null;

    }
/*
    public void buscarPorFecha() {
        if (fechaDesde == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
             
            if (deshabilitarAdminOrdenDia()) {
                listaOrdenesDia = obtenerOrdenesDelDia(fechaDesde);
            } else {
                listaOrdenesDia = this.ejbFacade.getEntityManager().createNamedQuery("ExpOrdenesDia.findByFechaSesion", ExpOrdenesDia.class).setParameter("fechaDesde", fechaDesde).getResultList();
            }

            setSelected(null);
            listaDetallesOrdenDia = null;
            detalleOrdenDiaSelected = null;
        }
    }
        */

    public void buscarDetalles() {
        if (getSelected() != null) {
            listaDetallesOrdenDia = this.ejbFacade.getEntityManager().createNamedQuery("ExpDetallesOrdenDia.findByOrdenDia", ExpDetallesOrdenDia.class).setParameter("ordenDia", getSelected()).getResultList();
        } else {
            listaDetallesOrdenDia = null;
        }
    }

    @Override
    public Collection<ExpOrdenesDia> getItems() {
        return super.getItems2();
    }

    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public ExpOrdenesDia prepareCreate() {
        ExpOrdenesDia doc = super.prepareCreate(null);
        doc.setFechaSesion(ejbFacade.getSystemDate());
        return doc;
    }

    public ExpDetallesOrdenDia prepareCreateDetalle() {
        detalleOrdenDiaSelected = detalleOrdenDiaController.prepareCreate(null);
        listaExpedientes = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
        return detalleOrdenDiaSelected;
    }

    public void prepareEditDetalle() {
        listaExpedientes = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
    }

    public void navigateActuacion(ExpDetallesOrdenDia item) {
        List<ExpActuaciones> act = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicial", ExpActuaciones.class).setParameter("documentoJudicial", item.getDocumentoJudicial()).getResultList();
        if (!act.isEmpty()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", String.valueOf(act.get(0).getId()));
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudiciales/index.xhtml?tipo=consulta");
            } catch (IOException ex) {
            }

        }
    }

    public void actualizarOrdenDia(ExpOrdenesDia item) {
        setSelected(item);
        super.save(null);
    }

    public void borrarOrden(ExpOrdenesDia item) {
        List<ExpDetallesOrdenDia> lista = ejbFacade.getEntityManager().createNamedQuery("ExpDetallesOrdenDia.findByOrdenDia", ExpDetallesOrdenDia.class).setParameter("ordenDia", item).getResultList();
        for (ExpDetallesOrdenDia uno : lista) {
            detalleOrdenDiaController.setSelected(uno);
            detalleOrdenDiaController.delete(null);
        }

        setSelected(item);
        super.delete(null);
        buscarPorFecha();
    }

    public void borrarDetalle(ExpDetallesOrdenDia item) {
        detalleOrdenDiaController.setSelected(item);
        detalleOrdenDiaController.delete(null);
        buscarDetalles();
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(personaUsuario);
        }

        super.save(event);
    }

    public void saveNew() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(personaUsuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setPersonaAlta(personaUsuario);

            super.saveNew(null);

            buscarPorFecha();
        }
    }

    public void saveNewDetalle() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            detalleOrdenDiaSelected.setOrdenDia(getSelected());
            detalleOrdenDiaSelected.setFechaHoraUltimoEstado(fecha);
            detalleOrdenDiaSelected.setPersonaUltimoEstado(personaUsuario);
            detalleOrdenDiaSelected.setFechaHoraAlta(fecha);
            detalleOrdenDiaSelected.setPersonaAlta(personaUsuario);

            detalleOrdenDiaController.setSelected(detalleOrdenDiaSelected);
            detalleOrdenDiaController.saveNew(null);

            buscarDetalles();
        }
    }

    public void saveEditDetalle() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            detalleOrdenDiaSelected.setFechaHoraUltimoEstado(fecha);
            detalleOrdenDiaSelected.setPersonaUltimoEstado(personaUsuario);

            detalleOrdenDiaController.setSelected(detalleOrdenDiaSelected);
            detalleOrdenDiaController.save(null);

            buscarDetalles();
        }
    }
}
