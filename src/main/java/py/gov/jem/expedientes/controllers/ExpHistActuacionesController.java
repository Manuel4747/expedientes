package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpHistActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "expHistActuacionesController")
@ViewScoped
public class ExpHistActuacionesController extends AbstractController<ExpHistActuaciones> {

    @Inject
    private EmpresasController empresaController;

    public ExpHistActuacionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpHistActuaciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }
    

}
