package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "antecedentesRolesPorUsuariosController")
@ViewScoped
public class AntecedentesRolesPorPersonasController extends AbstractController<AntecedentesRolesPorPersonas> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosController estadoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private UsuariosController usuariosController;

    public AntecedentesRolesPorPersonasController() {
        // Inform the Abstract parent controller of the concrete FormRolesPorUsuarios Entity
        super(AntecedentesRolesPorPersonas.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getAntecedentesRolesPorPersonasPK().setPersona(this.getSelected().getPersonas().getId());
        this.getSelected().getAntecedentesRolesPorPersonasPK().setRol(this.getSelected().getRoles().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setAntecedentesRolesPorPersonasPK(new py.gov.jem.expedientes.models.AntecedentesRolesPorPersonasPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        usuariosController.setSelected(null);
    }
    
    @Override
    public void save(ActionEvent event) {
        super.save(event);
    }
    
    public void saveNew2() {
        super.saveNew(null);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {
            super.saveNew(event);
        }

    }
}
