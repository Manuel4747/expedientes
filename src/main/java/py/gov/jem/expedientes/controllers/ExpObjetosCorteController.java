package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpObjetosCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "objetosCorteController")
@ViewScoped
public class ExpObjetosCorteController extends AbstractController<ExpObjetosCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpObjetosCorteController() {
        // Inform the Abstract parent controller of the concrete ExpObjetosCorte Entity
        super(ExpObjetosCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
