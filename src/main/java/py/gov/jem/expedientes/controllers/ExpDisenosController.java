package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpDisenosActuacion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "disenosActuacionController")
@ViewScoped
public class ExpDisenosController extends AbstractController<ExpDisenosActuacion> {

    public ExpDisenosController() {
        // Inform the Abstract parent controller of the concrete FormPermisos Entity
        super(ExpDisenosActuacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
