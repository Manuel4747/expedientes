package py.gov.jem.expedientes.controllers;


import java.util.Collection;
import py.gov.jem.expedientes.models.ExpTiposSesion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "expTiposSesionController")
@ViewScoped
public class ExpTiposSesionController extends AbstractController<ExpTiposSesion> {

    @Inject
    private EmpresasController empresaController;

    public ExpTiposSesionController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpTiposSesion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }
    
    @Override
    public Collection<ExpTiposSesion> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpTiposSesion.findAll", ExpTiposSesion.class).getResultList();
    }

}
