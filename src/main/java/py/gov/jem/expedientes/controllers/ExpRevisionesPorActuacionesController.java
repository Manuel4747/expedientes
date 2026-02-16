package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpRevisionesPorActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "expRevisionesPorActuacionesController")
@ViewScoped
public class ExpRevisionesPorActuacionesController extends AbstractController<ExpRevisionesPorActuaciones> {

    @Inject
    private EmpresasController empresaController;

    public ExpRevisionesPorActuacionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpRevisionesPorActuaciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }
    

}
