package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import py.gov.jem.expedientes.models.ExpEstadosActuacion;
import py.gov.jem.expedientes.facades.ExpEstadosActuacionFacade;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import py.gov.jem.expedientes.models.DocumentosJudiciales;

@Named(value = "expEstadosActuacionController")
@ViewScoped
public class ExpEstadosActuacionController extends AbstractController<ExpEstadosActuacion> {

    @Inject
    private EmpresasController empresaController;

    public ExpEstadosActuacionController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosActuacion Entity
        super(ExpEstadosActuacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    @Override
    public Collection<ExpEstadosActuacion> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findAll", ExpEstadosActuacion.class).getResultList();
    }
}
