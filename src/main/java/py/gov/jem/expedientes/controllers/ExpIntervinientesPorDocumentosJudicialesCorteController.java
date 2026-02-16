package py.gov.jem.expedientes.controllers;


import java.util.Collection;
import py.gov.jem.expedientes.models.ExpIntervinientesPorDocumentosJudicialesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "expIntervinientesPorDocumentosJudicialesController")
@ViewScoped
public class ExpIntervinientesPorDocumentosJudicialesCorteController extends AbstractController<ExpIntervinientesPorDocumentosJudicialesCorte> {

    public ExpIntervinientesPorDocumentosJudicialesCorteController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpIntervinientesPorDocumentosJudicialesCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<ExpIntervinientesPorDocumentosJudicialesCorte> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpIntervinientesPorDocumentosJudicialesCorte.findAll", ExpIntervinientesPorDocumentosJudicialesCorte.class).getResultList();
    }

}
