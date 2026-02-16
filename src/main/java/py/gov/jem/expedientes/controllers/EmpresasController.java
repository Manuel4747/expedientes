package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.Empresas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named(value = "empresasController")
@ViewScoped
public class EmpresasController extends AbstractController<Empresas> {


    public EmpresasController() {
        // Inform the Abstract parent controller of the concrete Empresas Entity
        super(Empresas.class);
    }

    /**
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Empresas?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Roles page
     */
    public String navigateRolesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection());
        }
        return "/pages/roles/index";
    }

    /**
     * Sets the "items" attribute with a collection of TiposPersona entities
     * that are retrieved from Empresas?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for TiposPersona page
     */
    public String navigateTiposPersonaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("TiposPersona_items", this.getSelected().getTiposPersonaCollection());
        }
        return "/pages/tiposPersona/index";
    }
     
}
