package py.gov.jem.expedientes.controllers;

import py.gov.jem.expedientes.models.CargosPersona;
import py.gov.jem.expedientes.models.Personas;
import java.util.Collection;
import py.gov.jem.expedientes.facades.CargosPersonaFacade;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "cargosPersonaController")
@ViewScoped
public class CargosPersonaController extends AbstractController<CargosPersona> {

    @Inject
    private EmpresasController empresaController;

    // Flags to indicate if child collections are empty
    private boolean isPersonasCollectionEmpty;

    public CargosPersonaController() {
        // Inform the Abstract parent controller of the concrete CargosPersona Entity
        super(CargosPersona.class);
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
        CargosPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

    public boolean getIsPersonasCollectionEmpty() {
        return this.isPersonasCollectionEmpty;
    }

}
