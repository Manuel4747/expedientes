package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import py.gov.jem.expedientes.models.ExpEstadosActaSesion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "estadosActaSesionController")
@ViewScoped
public class ExpEstadosActasSesionController extends AbstractController<ExpEstadosActaSesion> {

    @Inject
    private EmpresasController empresaController;

    public ExpEstadosActasSesionController() {
        // Inform the Abstract parent controller of the concrete ExpEstadosActaSesion Entity
        super(ExpEstadosActaSesion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    @Override
    public Collection<ExpEstadosActaSesion> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findAll", ExpEstadosActaSesion.class).getResultList();
    }
}
