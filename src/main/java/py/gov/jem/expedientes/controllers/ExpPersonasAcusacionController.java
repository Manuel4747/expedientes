package py.gov.jem.expedientes.controllers;


import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpPersonasAcusacion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "personasAcusacionController")
@ViewScoped
public class ExpPersonasAcusacionController extends AbstractController<ExpPersonasAcusacion> {
    private HttpSession session;
    private Personas personaUsuario;
    

    public ExpPersonasAcusacionController() {
        // Inform the Abstract parent controller of the concrete ExpPersonasAcusacion Entity
        super(ExpPersonasAcusacion.class);
    }
    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        personaUsuario = (Personas) session.getAttribute("Persona");
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
    
    public void refrescar() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAcusacion.findAll", ExpPersonasAcusacion.class).getResultList());
    }
    
    public void saveNew() {
        if (getSelected() != null) {
            
            Date fecha = ejbFacade.getSystemDate();
            
            getSelected().setEstado(Constantes.ESTADO_USUARIO_AC);
            
            getSelected().setPersonaAlta(personaUsuario);
            getSelected().setPersonaUltimoEstado(personaUsuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setFechaHoraUltimoEstado(fecha);
            
            super.save(null);
            
            refrescar();
        }
    }
    
    public void saveEdit() {
        if (getSelected() != null) {
            
            Date fecha = ejbFacade.getSystemDate();
            
            getSelected().setPersonaUltimoEstado(personaUsuario);
            getSelected().setFechaHoraUltimoEstado(fecha);
            
            super.save(null);
            refrescar();
        }
    }
}