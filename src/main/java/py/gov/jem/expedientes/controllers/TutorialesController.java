package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.Tutoriales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "tutorialesController")
@ViewScoped
public class TutorialesController extends AbstractController<Tutoriales> {

    public TutorialesController() {
        // Inform the Abstract parent controller of the concrete FormPermisos Entity
        super(Tutoriales.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
