package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpDetallesActaSesion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "detallesActaSesionController")
@ViewScoped
public class ExpDetallesActaSesionController extends AbstractController<ExpDetallesActaSesion> {

    @Inject
    private EmpresasController empresaController;

    public ExpDetallesActaSesionController() {
        // Inform the Abstract parent controller of the concrete AgDetallesAgendamiento Entity
        super(ExpDetallesActaSesion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }


}
