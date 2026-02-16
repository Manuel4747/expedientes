package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpNotificacionesUjieres;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "notificacionesUjieresController")
@ViewScoped
public class ExpNotificacionesUjieresController extends AbstractController<ExpNotificacionesUjieres> {

    private final FiltroURL filtroURL = new FiltroURL();
    private String endpoint;
    private HttpSession session;
    private Personas personaUsuario = null;
    private AntecedentesRoles rolElegido;
    private String accion;
    private String titulo;
    private List<ExpNotificacionesUjieres> listaPendientes;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<ExpNotificacionesUjieres> getListaPendientes() {
        return listaPendientes;
    }

    public void setListaPendientes(List<ExpNotificacionesUjieres> listaPendientes) {
        this.listaPendientes = listaPendientes;
    }


    public ExpNotificacionesUjieresController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(ExpNotificacionesUjieres.class);
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
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    public String datePattern3() {
        return "yyyy/MM/dd HH:mm:ss";
    }

    public void navigateActuacion(ExpActuaciones not) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", String.valueOf(not.getId()));

            if (filtroURL.verifPermiso(Constantes.PERMISO_REGISTRAR_ACTUACION_POR_SECRETARIA)) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudicialesPorSecretaria/index.xhtml?tipo=" + Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA + "&pestanas=" + accion);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudiciales/index.xhtml?tipo=" + Constantes.ACCION_CONSULTA);
            }
            //}
        } catch (IOException ex) {
        }
    }
}
