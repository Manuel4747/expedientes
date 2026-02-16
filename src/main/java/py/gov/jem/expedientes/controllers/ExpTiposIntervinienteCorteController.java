package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpTiposIntervinienteCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "tiposIntervinienteCorteController")
@ViewScoped
public class ExpTiposIntervinienteCorteController extends AbstractController<ExpTiposIntervinienteCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpTiposIntervinienteCorteController() {
        // Inform the Abstract parent controller of the concrete ExpTiposIntervinienteCorte Entity
        super(ExpTiposIntervinienteCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
