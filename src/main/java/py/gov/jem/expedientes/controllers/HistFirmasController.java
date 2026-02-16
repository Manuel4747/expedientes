package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.HistFirmas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "histFirmasController")
@ViewScoped
public class HistFirmasController extends AbstractController<HistFirmas> {

    public HistFirmasController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(HistFirmas.class);
    }
}
