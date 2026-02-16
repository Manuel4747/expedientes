package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpEnviosCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "enviosCorteController")
@ViewScoped
public class ExpEnviosCorteController extends AbstractController<ExpEnviosCorte> {

    public ExpEnviosCorteController() {
        // Inform the Abstract parent controller of the concrete ExpEnviosCorte Entity
        super(ExpEnviosCorte.class);
    }
}
