package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpEstadosActuacionPorRoles;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "expEstadosActuacionPorRolesController")
@ViewScoped
public class ExpEstadosActuacionPorRolesController extends AbstractController<ExpEstadosActuacionPorRoles> {


    public ExpEstadosActuacionPorRolesController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosActuacionPorRoles Entity
        super(ExpEstadosActuacionPorRoles.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
