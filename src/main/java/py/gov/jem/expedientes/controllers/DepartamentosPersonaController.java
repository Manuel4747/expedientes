package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.DepartamentosPersona;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "departamentosPersonaController")
@ViewScoped
public class DepartamentosPersonaController extends AbstractController<DepartamentosPersona> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private EmpresasController empresaController;

    public DepartamentosPersonaController() {
        // Inform the Abstract parent controller of the concrete DespachosPersona Entity
        super(DepartamentosPersona.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        cargoPersonaController.setSelected(null);
        empresaController.setSelected(null);
    }


    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        DepartamentosPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

}
