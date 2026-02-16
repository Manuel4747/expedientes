package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.HistPersonasAntecedente;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "histPersonasAntecedenteController")
@ViewScoped
public class HistPersonasAntecedenteController extends AbstractController<HistPersonasAntecedente> {

    public HistPersonasAntecedenteController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(HistPersonasAntecedente.class);
    }
}
