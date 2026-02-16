package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.Roles;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "rolesController")
@ViewScoped
public class RolesController extends AbstractController<Roles> {

    @Inject
    private EmpresasController empresaController;

    public RolesController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(Roles.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

}
