package py.gov.jem.expedientes.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpAuditorias;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "auditoriasController")
@ViewScoped
public class ExpAuditoriasController extends AbstractController<ExpAuditorias> {

    @Inject
    private PersonasController personasController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;

    private Date fechaDesde;
    private Date fechaHasta;
    private Personas personaUsuario;
    private List<ExpAuditorias> listaAuditorias;
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

    public List<ExpAuditorias> getListaAuditorias() {
        return listaAuditorias;
    }

    public void setListaAuditorias(List<ExpAuditorias> listaAuditorias) {
        this.listaAuditorias = listaAuditorias;
    }

    public List<DocumentosJudiciales> getListaExpedientes() {
        return listaExpedientes;
    }

    public void setListaExpedientes(List<DocumentosJudiciales> listaExpedientes) {
        this.listaExpedientes = listaExpedientes;
    }

    public ExpAuditoriasController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(ExpAuditorias.class);
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

    public List<ExpAuditorias> obtenerAuditorias(Date fecha) {

        List<ExpAuditorias> lista = null;
        //if (!deshabilitarAdminOrdenDia()) {
            lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpAuditorias.findByAuditTimestamp", ExpAuditorias.class).setParameter("fechaDesde", fechaDesde).getResultList();
        /*} else {
            lista = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_ordenes_dia as a, exp_detalles_orden_dia as d \n"
                    + "where a.id = d.orden_dia and d.documento_judicial in (select b.documento_judicial from exp_partes_por_documentos_judiciales as b where b.persona = ?1 union select c.documento_judicial from personas_por_documentos_judiciales as c where c.persona = ?2) \n"
                    + "and a.visible = true and a.fecha_sesion >= ?3 order by a.fecha_hora_alta", ExpAuditorias.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).setParameter(3, fecha).getResultList();
        }*/
        
        return lista;

    }
    
    public void buscarPorFecha() {
        listaAuditorias = obtenerAuditorias(fechaDesde);
        setSelected(null);

    }
    
    @Override
    public Collection<ExpAuditorias> getItems() {
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
}
