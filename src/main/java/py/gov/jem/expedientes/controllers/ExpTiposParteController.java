package py.gov.jem.expedientes.controllers;


import java.util.Collection;
import py.gov.jem.expedientes.models.ExpTiposParte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "expTiposParteController")
@ViewScoped
public class ExpTiposParteController extends AbstractController<ExpTiposParte> {

    @Inject
    private EmpresasController empresaController;

    public ExpTiposParteController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpTiposParte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    @Override
    public Collection<ExpTiposParte> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findAll", ExpTiposParte.class).getResultList();
    }

}
