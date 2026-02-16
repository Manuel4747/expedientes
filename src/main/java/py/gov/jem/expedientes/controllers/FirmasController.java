package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.Firmas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "firmasController")
@ViewScoped
public class FirmasController extends AbstractController<Firmas> {

    public FirmasController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(Firmas.class);
    }
}
