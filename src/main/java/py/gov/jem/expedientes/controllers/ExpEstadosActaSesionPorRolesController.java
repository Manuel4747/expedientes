package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpEstadosActaSesionPorRoles;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "expEstadosActaSesionPorRolesController")
@ViewScoped
public class ExpEstadosActaSesionPorRolesController extends AbstractController<ExpEstadosActaSesionPorRoles> {


    public ExpEstadosActaSesionPorRolesController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosActaSesionPorRoles Entity
        super(ExpEstadosActaSesionPorRoles.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
