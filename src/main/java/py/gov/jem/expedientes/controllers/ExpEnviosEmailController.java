package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import py.gov.jem.expedientes.models.ExpEnviosEmail;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "enviosEmailController")
@ViewScoped
public class ExpEnviosEmailController extends AbstractController<ExpEnviosEmail> {

    public ExpEnviosEmailController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosActuacion Entity
        super(ExpEnviosEmail.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<ExpEnviosEmail> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpEnviosEmail.findAll", ExpEnviosEmail.class).getResultList();
    }
}
