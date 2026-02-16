package py.gov.jem.expedientes.controllers;

import java.util.Date;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.ExpDocumentosJudicialesCortePorDocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "expDocumentosJudicialesCortePorDocumentosJudicialesController")
@ViewScoped
public class ExpDocumentosJudicialesCortePorDocumentosJudicialesController extends AbstractController<ExpDocumentosJudicialesCortePorDocumentosJudiciales> {

    public ExpDocumentosJudicialesCortePorDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete RolesPorUsuarios Entity
        super(ExpDocumentosJudicialesCortePorDocumentosJudiciales.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getDocumentosJudicialesCortePorDocumentosJudicialesPK().setDocumentoJudicialCorte(this.getSelected().getDocumentoJudicialCorte().getId());
        this.getSelected().getDocumentosJudicialesCortePorDocumentosJudicialesPK().setDocumentoJudicial(this.getSelected().getDocumentoJudicial().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setExpDocumentosJudicialesCortePorDocumentosJudicialesPK(new py.gov.jem.expedientes.models.ExpDocumentosJudicialesCortePorDocumentosJudicialesPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
    
     @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Personas per = (Personas) session.getAttribute("Persona");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(per);
        }

        super.save(event);
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
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Personas per = (Personas) session.getAttribute("Persona");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(per);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setPersonaAlta(per);

            super.saveNew(event);
        }

    }
}
