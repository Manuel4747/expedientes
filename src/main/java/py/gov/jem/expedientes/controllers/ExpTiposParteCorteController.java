package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpTiposParteCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "tiposParteCorteController")
@ViewScoped
public class ExpTiposParteCorteController extends AbstractController<ExpTiposParteCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpTiposParteCorteController() {
        // Inform the Abstract parent controller of the concrete ExpTiposParteCorte Entity
        super(ExpTiposParteCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
