package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpProcesosCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "procesosCorteController")
@ViewScoped
public class ExpProcesosCorteController extends AbstractController<ExpProcesosCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpProcesosCorteController() {
        // Inform the Abstract parent controller of the concrete ExpProcesosCorte Entity
        super(ExpProcesosCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
