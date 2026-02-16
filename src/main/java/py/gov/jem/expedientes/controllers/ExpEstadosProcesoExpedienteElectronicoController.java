package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.ExpEstadosProcesoExpedienteElectronico;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "estadosProcesoExpedienteElectronicoController")
@ViewScoped
public class ExpEstadosProcesoExpedienteElectronicoController extends AbstractController<ExpEstadosProcesoExpedienteElectronico> {

    public ExpEstadosProcesoExpedienteElectronicoController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosProcesoExpedienteElectronico Entity
        super(ExpEstadosProcesoExpedienteElectronico.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
