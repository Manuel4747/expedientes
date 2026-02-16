package py.gov.jem.expedientes.controllers;


import java.util.Collection;
import py.gov.jem.expedientes.models.ExpCaracter;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "caracterController")
@ViewScoped
public class ExpCaracterController extends AbstractController<ExpCaracter> {

    @Inject
    private EmpresasController empresaController;

    public ExpCaracterController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpCaracter.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    @Override
    public Collection<ExpCaracter> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpCaracter.findAll", ExpCaracter.class).getResultList();
    }

}
