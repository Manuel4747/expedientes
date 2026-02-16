package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "expPersonasAsociadasController")
@ViewScoped
public class ExpPersonasAsociadasController extends AbstractController<ExpPersonasAsociadas> {

    public ExpPersonasAsociadasController() {
        // Inform the Abstract parent controller of the concrete FormPermisosPorRoles Entity
        super(ExpPersonasAsociadas.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getExpPersonasAsociadasPK().setPersona(this.getSelected().getPersona().getId());
        this.getSelected().getExpPersonasAsociadasPK().setPersonaAsociada(this.getSelected().getPersonaAsociada().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setExpPersonasAsociadasPK(new py.gov.jem.expedientes.models.ExpPersonasAsociadasPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
    

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {
            super.saveNew(event);
        }

    }
}
