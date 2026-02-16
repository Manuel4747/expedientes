package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpTiposPersonaCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "tiposPersonaCorteController")
@ViewScoped
public class ExpTiposPersonaCorteController extends AbstractController<ExpTiposPersonaCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpTiposPersonaCorteController() {
        // Inform the Abstract parent controller of the concrete ExpTiposPersonaCorte Entity
        super(ExpTiposPersonaCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
