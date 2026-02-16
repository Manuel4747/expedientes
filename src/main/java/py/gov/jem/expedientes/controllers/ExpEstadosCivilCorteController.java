package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpEstadosCivilCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "estadosCivilCorteController")
@ViewScoped
public class ExpEstadosCivilCorteController extends AbstractController<ExpEstadosCivilCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpEstadosCivilCorteController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosCivilCorte Entity
        super(ExpEstadosCivilCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
