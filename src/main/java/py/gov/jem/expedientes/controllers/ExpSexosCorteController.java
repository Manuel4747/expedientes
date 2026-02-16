package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpSexosCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "sexosCorteController")
@ViewScoped
public class ExpSexosCorteController extends AbstractController<ExpSexosCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpSexosCorteController() {
        // Inform the Abstract parent controller of the concrete ExpSexosCorte Entity
        super(ExpSexosCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
