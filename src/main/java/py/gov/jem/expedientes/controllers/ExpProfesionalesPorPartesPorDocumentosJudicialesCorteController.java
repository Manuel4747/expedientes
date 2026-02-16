package py.gov.jem.expedientes.controllers;


import java.util.Collection;
import py.gov.jem.expedientes.models.ExpProfesionalesPorPartesPorDocumentosJudicialesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "profesionalesPorPartesPorDocumentosJudicialesCorteController")
@ViewScoped
public class ExpProfesionalesPorPartesPorDocumentosJudicialesCorteController extends AbstractController<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> {

    public ExpProfesionalesPorPartesPorDocumentosJudicialesCorteController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findAll", ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.class).getResultList();
    }

}
