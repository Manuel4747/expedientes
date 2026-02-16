package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpProcesosDocumentoJudicialCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "procesosDocumentoJudicialCorteController")
@ViewScoped
public class ExpProcesosDocumentoJudicialCorteController extends AbstractController<ExpProcesosDocumentoJudicialCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpProcesosDocumentoJudicialCorteController() {
        // Inform the Abstract parent controller of the concrete ExpProcesosDocumentoJudicialCorte Entity
        super(ExpProcesosDocumentoJudicialCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
