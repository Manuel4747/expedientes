package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.Resuelve;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "resuelveController")
@ViewScoped
public class ResuelveController extends AbstractController<Resuelve> {

    @Inject
    private EmpresasController empresaController;

    public ResuelveController() {
        // Inform the Abstract parent controller of the concrete Sentencia Entity
        super(Resuelve.class);
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
        Resuelve selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

}
