package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpOcupacionesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "ocupacionesCorteController")
@ViewScoped
public class ExpOcupacionesCorteController extends AbstractController<ExpOcupacionesCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpOcupacionesCorteController() {
        // Inform the Abstract parent controller of the concrete ExpOcupacionesCorte Entity
        super(ExpOcupacionesCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
