package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpTiposDocumentoCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "tiposDocumentoCorteController")
@ViewScoped
public class ExpTiposDocumentoCorteController extends AbstractController<ExpTiposDocumentoCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpTiposDocumentoCorteController() {
        // Inform the Abstract parent controller of the concrete ExpTiposDocumentoCorte Entity
        super(ExpTiposDocumentoCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
