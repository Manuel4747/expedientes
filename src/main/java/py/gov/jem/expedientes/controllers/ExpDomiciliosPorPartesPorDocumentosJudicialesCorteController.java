package py.gov.jem.expedientes.controllers;


import java.util.Collection;
import py.gov.jem.expedientes.models.ExpDomiciliosPorPartesPorDocumentosJudicialesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "domiciliosPorPartesPorDocumentosJudicialesCorteController")
@ViewScoped
public class ExpDomiciliosPorPartesPorDocumentosJudicialesCorteController extends AbstractController<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> {

    public ExpDomiciliosPorPartesPorDocumentosJudicialesCorteController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findAll", ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.class).getResultList();
    }

}
