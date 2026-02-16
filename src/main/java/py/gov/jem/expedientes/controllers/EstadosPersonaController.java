package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.EstadosPersona;
import py.gov.jem.expedientes.facades.EstadosPersonaFacade;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "estadosPersonaController")
@ViewScoped
public class EstadosPersonaController extends AbstractController<EstadosPersona> {

    @Inject
    private EmpresasController empresaController;

    public EstadosPersonaController() {
        // Inform the Abstract parent controller of the concrete EstadosPersona Entity
        super(EstadosPersona.class);
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
        EstadosPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

}
