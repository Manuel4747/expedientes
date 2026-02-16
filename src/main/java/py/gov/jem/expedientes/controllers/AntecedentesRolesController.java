package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.AntecedentesRoles;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "antecedentesRolesController")
@ViewScoped
public class AntecedentesRolesController extends AbstractController<AntecedentesRoles> {

    public AntecedentesRolesController() {
        // Inform the Abstract parent controller of the concrete FormPermisos Entity
        super(AntecedentesRoles.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
