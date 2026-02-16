package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpEstadosCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "estadosCorteController")
@ViewScoped
public class ExpEstadosCorteController extends AbstractController<ExpEstadosCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpEstadosCorteController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosCorte Entity
        super(ExpEstadosCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
