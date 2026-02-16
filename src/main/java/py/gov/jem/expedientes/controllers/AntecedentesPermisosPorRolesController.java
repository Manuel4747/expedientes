package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.AntecedentesPermisosPorRoles;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "antecedentesPermisosPorRolesController")
@ViewScoped
public class AntecedentesPermisosPorRolesController extends AbstractController<AntecedentesPermisosPorRoles> {

    @Inject
    private AntecedentesPermisosController permisosController;
    @Inject
    private RolesController rolesController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosController estadoController;

    public AntecedentesPermisosPorRolesController() {
        // Inform the Abstract parent controller of the concrete FormPermisosPorRoles Entity
        super(AntecedentesPermisosPorRoles.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getAntecedentesPermisosPorRolesPK().setPermiso(this.getSelected().getFormPermisos().getId());
        this.getSelected().getAntecedentesPermisosPorRolesPK().setRol(this.getSelected().getRoles().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setAntecedentesPermisosPorRolesPK(new py.gov.jem.expedientes.models.AntecedentesPermisosPorRolesPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        permisosController.setSelected(null);
        rolesController.setSelected(null);
        empresaController.setSelected(null);
        estadoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FormPermisos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareFormPermisos(ActionEvent event) {
        if (this.getSelected() != null && permisosController.getSelected() == null) {
            permisosController.setSelected(this.getSelected().getFormPermisos());
        }
    }

    /**
     * Sets the "selected" attribute of the Roles controller in order to display
     * its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRoles(ActionEvent event) {
        if (this.getSelected() != null && rolesController.getSelected() == null) {
            rolesController.setSelected(this.getSelected().getRoles());
        }
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
