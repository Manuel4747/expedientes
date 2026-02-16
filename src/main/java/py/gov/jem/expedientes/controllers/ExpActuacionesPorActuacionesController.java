package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import py.gov.jem.expedientes.models.ExpActuacionesPorActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "expActuacionesPorActuacionesController")
@ViewScoped
public class ExpActuacionesPorActuacionesController extends AbstractController<ExpActuacionesPorActuaciones> {

    public ExpActuacionesPorActuacionesController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosActuacion Entity
        super(ExpActuacionesPorActuaciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<ExpActuacionesPorActuaciones> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findAll", ExpActuacionesPorActuaciones.class).getResultList();
    }
}
