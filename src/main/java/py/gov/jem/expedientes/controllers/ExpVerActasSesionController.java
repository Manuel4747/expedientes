package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpActasSesion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActasSesion;
import py.gov.jem.expedientes.models.PersonasPorDocumentosJudiciales;

@Named(value = "expVerActasSesionController")
@ViewScoped
public class ExpVerActasSesionController extends AbstractController<ExpActasSesion> {
    private HttpSession session;
    private Personas personaUsuario = null;
    private AntecedentesRoles rolElegido;
    private final FiltroURL filtroURL = new FiltroURL();
    private List<ExpActasSesion> listaPendientes;
    private List<Personas> listaPersonas;
    private Personas personaSelected;
    private String accion;
    private String titulo;
    private String endpoint;

    public List<ExpActasSesion> getListaPendientes() {
        return listaPendientes;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public Personas getPersonaSelected() {
        return personaSelected;
    }

    public void setPersonaSelected(Personas personaSelected) {
        this.personaSelected = personaSelected;
    }

    public void setListaPendientes(List<ExpActasSesion> listaPendientes) {
        this.listaPendientes = listaPendientes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ExpVerActasSesionController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpActasSesion.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        accion = params.get("tipo");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];
        
        if (Constantes.ACCION_ACTUACIONES_EN_PROYECTO.equals(accion)) {
            titulo = "ACTAS DE SESIÓN EN ELABORACION";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)) {
            titulo = "ACTAS DE SESIÓN P/ LA FIRMA DE LOS MIEMBROS";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion)) {
            titulo = "ACTAS DE SESIÓN P/ LA FIRMA DEL SECRETARIO";
        } else if (Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion)) {
            titulo = "ACTAS DE SESIÓN FINALIZADAS";
        } else {
            // JsfUtil.addErrorMessage("Accion no permitida:: " + accion);
            return;
        }

        listaPersonas = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_MIEMBRO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        List<Personas> listaPre = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        listaPersonas.addAll(listaPre);

        if (Constantes.ROL_MIEMBRO.equals(rolElegido.getId()) || Constantes.ROL_PRESIDENTE.equals(rolElegido.getId())) {
            personaSelected = personaUsuario;
        } else {
            personaSelected = null;
        }

        // buscar(personaSelected, rolElegido);

    }

    public String datePattern() {
        return "yyyy/MM/dd";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public String datePattern2() {
        return "yyyy";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }

    public String datePattern3() {
        return "yyyy/MM/dd HH:mm:ss";
    }

    public String datePattern4() {
        return "yyyy/MM/dd HH:mm";
    }

    public String customFormatDate4(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern4());
            return format.format(date);
        }
        return "";
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    @Override
    public Collection<ExpActasSesion> getItems() {
        return super.getItems2();
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public void navigateActuacion(ExpActasSesion not) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actaSesionId", String.valueOf(not.getId()));
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expActasSesion/index.xhtml");
        } catch (IOException ex) {
        }
    }

    public boolean renderedFiltroPersonas() {
        if (Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)) {
            return true;
        }
        return false;
    }

    public boolean deshabilitarDatosSecretaria() {
        return filtroURL.verifPermiso(Constantes.PERMISO_VER_DATOS_SECRETARIA);
    }
/*
    public void buscar() {
        buscar(personaSelected, rolElegido);
    }

    private void buscar(Personas persona, AntecedentesRoles rol) {

        if (Constantes.ACCION_ACTUACIONES_EN_PROYECTO.equals(accion)) {
            listaPendientes = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTUACION_EN_PROYECTO, persona);
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)) {
            listaPendientes = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS, persona);
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion)) {
            listaPendientes = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO, persona);
        } else if (Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion)) {
            listaPendientes = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTUACION_FINALIZADA, persona);
        } else {
            JsfUtil.addErrorMessage("Accion no permitida: " + accion);
            return;
        }
    }
*/
}
