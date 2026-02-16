package py.gov.jem.expedientes.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.ExpFiscalesJurado;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "expFiscalesJuradoController")
@ViewScoped
public class ExpFiscalesJuradoController extends AbstractController<ExpFiscalesJurado> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    private HttpSession session;
    private Personas per;
    private Usuarios usu;
    private List<Personas> listaPersonas;

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public ExpFiscalesJuradoController() {
        // Inform the Abstract parent controller of the concrete ExpFiscalesJurado Entity
        super(ExpFiscalesJurado.class);
    }
    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        
        per = (Personas) session.getAttribute("Persona");
        usu = (Usuarios) session.getAttribute("Usuarios");
        
        listaPersonas = ejbFacade.getEntityManager().createNamedQuery("Personas.findByEstado", Personas.class).setParameter("estado", "AC").getResultList();
    }


    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    @Override
    public Collection<ExpFiscalesJurado> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpFiscalesJurado.findAll", ExpFiscalesJurado.class).getResultList();
    }

    public void save() {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setPersonaUltimoEstado(per);
        }

        super.save(null);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    
    public void saveNew() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setPersonaAlta(per);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setPersonaUltimoEstado(per);
            getSelected().setEstado(new Estados("AC"));

            super.saveNew(null);

        }

    }
}
