package py.gov.jem.expedientes.controllers;


import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpRolesFirmantesPorActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.ExpNotificaciones;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "expRolesFirmantesPorActuacionesController")
@ViewScoped
public class ExpRolesFirmantesPorActuacionesController extends AbstractController<ExpRolesFirmantesPorActuaciones> {

    @Inject
    private EmpresasController empresaController;

    private List<ExpRolesFirmantesPorActuaciones> listaFirmantes;
    private HttpSession session;
    private Personas personaUsuario = null;
    private String sessionId;
    private AntecedentesRoles rolElegido;
    private String endpoint;

    public List<ExpRolesFirmantesPorActuaciones> getListaFirmantes() {
        return listaFirmantes;
    }

    public void setListaFirmantes(List<ExpRolesFirmantesPorActuaciones> listaFirmantes) {
        this.listaFirmantes = listaFirmantes;
    }

    public EmpresasController getEmpresaController() {
        return empresaController;
    }

    public void setEmpresaController(EmpresasController empresaController) {
        this.empresaController = empresaController;
    }

    public ExpRolesFirmantesPorActuacionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpRolesFirmantesPorActuaciones.class);
    }
    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();
        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];
        
        setItems(ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByRolFirmanteFirmado", ExpRolesFirmantesPorActuaciones.class).setParameter("rolFirmante", rolElegido).setParameter("firmado", true).getResultList());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }
    
    public void navigateActuacion(ExpNotificaciones not){
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", String.valueOf(not.getActuacion().getId()));
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudiciales/index.xhtml?tipo=consulta");
        } catch (IOException ex) {
        }
    }

}
