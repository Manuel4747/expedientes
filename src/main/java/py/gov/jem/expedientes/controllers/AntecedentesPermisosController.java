package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.AntecedentesPermisos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "antecedentesPermisosController")
@ViewScoped
public class AntecedentesPermisosController extends AbstractController<AntecedentesPermisos> {

    @Inject
    private EmpresasController empresaController;

    public AntecedentesPermisosController() {
        // Inform the Abstract parent controller of the concrete FormPermisos Entity
        super(AntecedentesPermisos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
