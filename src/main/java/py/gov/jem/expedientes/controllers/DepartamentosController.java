package py.gov.jem.expedientes.controllers;

import java.util.Date;

import py.gov.jem.expedientes.models.Departamentos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@Named(value = "departamentosController")
@ViewScoped
public class DepartamentosController extends AbstractController<Departamentos> {

    @Inject
    private EmpresasController empresaController;

    public DepartamentosController() {
        // Inform the Abstract parent controller of the concrete Departamentos Entity
        super(Departamentos.class);
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

    /**
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Departamentos?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Usuarios page
     */
    public String navigateUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection());
        }
        return "/pages/usuarios/index";
    }
    
}