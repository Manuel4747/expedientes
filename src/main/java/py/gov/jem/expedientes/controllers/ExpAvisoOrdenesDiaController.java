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

@Named(value = "avisoOrdenesDiaController")
@ViewScoped
public class ExpAvisoOrdenesDiaController extends AbstractController<ExpDetallesOrdenDia> {

    @Inject
    private PersonasController personasController;
    @Inject
    private ExpDetallesOrdenDiaController detalleOrdenDiaController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;

    private Date fechaDesde;
    private Date fechaHasta;
    private Personas personaUsuario;
    private List<ExpDetallesOrdenDia> listaDetallesOrdenDiaPend;
    private List<ExpDetallesOrdenDia> listaDetallesOrdenDiaAnt;
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

    public List<ExpDetallesOrdenDia> getListaDetallesOrdenDiaPend() {
        return listaDetallesOrdenDiaPend;
    }

    public void setListaDetallesOrdenDiaPend(List<ExpDetallesOrdenDia> listaDetallesOrdenDiaPend) {
        this.listaDetallesOrdenDiaPend = listaDetallesOrdenDiaPend;
    }

    public List<ExpDetallesOrdenDia> getListaDetallesOrdenDiaAnt() {
        return listaDetallesOrdenDiaAnt;
    }

    public void setListaDetallesOrdenDiaAnt(List<ExpDetallesOrdenDia> listaDetallesOrdenDiaAnt) {
        this.listaDetallesOrdenDiaAnt = listaDetallesOrdenDiaAnt;
    }

    public ExpAvisoOrdenesDiaController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(ExpDetallesOrdenDia.class);
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

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");

        fechaDesde = ejbFacade.getSystemDateOnly();
        fechaHasta = ejbFacade.getSystemDateOnly();

        buscarPorFecha();
    }

    public List<ExpDetallesOrdenDia> obtenerOrdenesDelDiaPend(Date fecha) {

        List<ExpDetallesOrdenDia> lista = this.ejbFacade.getEntityManager().createNativeQuery("select d.* from exp_ordenes_dia as a, exp_detalles_orden_dia as d \n"
                    + "where a.id = d.orden_dia and d.documento_judicial in (select b.documento_judicial from exp_partes_por_documentos_judiciales as b where b.persona = ?1 union select c.documento_judicial from personas_por_documentos_judiciales as c where c.persona = ?2) \n"
                    + "and a.visible = true and a.fecha_sesion >= ?3 order by a.fecha_hora_alta", ExpDetallesOrdenDia.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).setParameter(3, fecha).getResultList();
        
        return lista;

    }

    public List<ExpDetallesOrdenDia> obtenerOrdenesDelDiaAnt(Date fecha) {

        List<ExpDetallesOrdenDia> lista = this.ejbFacade.getEntityManager().createNativeQuery("select d.* from exp_ordenes_dia as a, exp_detalles_orden_dia as d \n"
                    + "where a.id = d.orden_dia and d.documento_judicial in (select b.documento_judicial from exp_partes_por_documentos_judiciales as b where b.persona = ?1 union select c.documento_judicial from personas_por_documentos_judiciales as c where c.persona = ?2) \n"
                    + "and a.visible = true and a.fecha_sesion < ?3 order by a.fecha_hora_alta", ExpDetallesOrdenDia.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).setParameter(3, fecha).getResultList();
        
        return lista;

    }
    
    public void buscarPorFecha() {
        listaDetallesOrdenDiaPend = obtenerOrdenesDelDiaPend(fechaDesde);
        listaDetallesOrdenDiaAnt = obtenerOrdenesDelDiaAnt(fechaDesde);
    }

    @Override
    public Collection<ExpDetallesOrdenDia> getItems() {
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

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(personaUsuario);
        }

        super.save(event);
    }

}
