package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.DocumentosJudicialesCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "documentosJudicialesCorteController")
@ViewScoped
public class DocumentosJudicialesCorteController extends AbstractController<DocumentosJudicialesCorte> {

    public DocumentosJudicialesCorteController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudicialesCorte Entity
        super(DocumentosJudicialesCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
