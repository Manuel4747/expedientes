package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.ExpActuacionesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "expActuacionesCorteController")
@ViewScoped
public class ExpActuacionesCorteController extends AbstractController<ExpActuacionesCorte> {

    public ExpActuacionesCorteController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpActuacionesCorte.class);
    }

}
