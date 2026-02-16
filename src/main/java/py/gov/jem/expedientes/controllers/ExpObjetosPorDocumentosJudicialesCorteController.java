package py.gov.jem.expedientes.controllers;

import java.util.Date;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.ExpObjetosPorDocumentosJudicialesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "objetosPorDocumentosJudicialesCorteController")
@ViewScoped
public class ExpObjetosPorDocumentosJudicialesCorteController extends AbstractController<ExpObjetosPorDocumentosJudicialesCorte> {

    @Inject
    private DocumentosJudicialesController documentosJudicialesController;

    public ExpObjetosPorDocumentosJudicialesCorteController() {
        // Inform the Abstract parent controller of the concrete RolesPorUsuarios Entity
        super(ExpObjetosPorDocumentosJudicialesCorte.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getPartesPorDocumentosJudicialesCortePK().setPersonaCorte(this.getSelected().getObjetoCorte().getId());
        this.getSelected().getPartesPorDocumentosJudicialesCortePK().setDocumentoJudicialCorte(this.getSelected().getDocumentoJudicialCorte().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setPartesPorDocumentosJudicialesPK(new py.gov.jem.expedientes.models.ExpObjetosPorDocumentosJudicialesCortePK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        documentosJudicialesController.setSelected(null);
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
