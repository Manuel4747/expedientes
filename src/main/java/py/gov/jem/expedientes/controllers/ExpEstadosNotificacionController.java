package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.ExpEstadosNotificacion;
import py.gov.jem.expedientes.facades.ExpEstadosNotificacionFacade;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "expEstadosNotificacionController")
@ViewScoped
public class ExpEstadosNotificacionController extends AbstractController<ExpEstadosNotificacion> {

    @Inject
    private EmpresasController empresaController;

    public ExpEstadosNotificacionController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosNotificacion Entity
        super(ExpEstadosNotificacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
