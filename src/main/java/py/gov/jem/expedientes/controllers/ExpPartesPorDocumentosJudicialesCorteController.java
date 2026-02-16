package py.gov.jem.expedientes.controllers;


import java.util.Collection;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "partesPorDocumentosJudicialesCorteController")
@ViewScoped
public class ExpPartesPorDocumentosJudicialesCorteController extends AbstractController<ExpPartesPorDocumentosJudicialesCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpPartesPorDocumentosJudicialesCorteController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpPartesPorDocumentosJudicialesCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    @Override
    public Collection<ExpPartesPorDocumentosJudicialesCorte> getItems() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findAll", ExpPartesPorDocumentosJudicialesCorte.class).getResultList();
    }

}
