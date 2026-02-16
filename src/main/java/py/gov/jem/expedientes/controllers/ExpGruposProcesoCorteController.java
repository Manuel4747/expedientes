package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpGruposProcesoCorte;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "gruposProcesoCorteController")
@ViewScoped
public class ExpGruposProcesoCorteController extends AbstractController<ExpGruposProcesoCorte> {

    @Inject
    private EmpresasController empresaController;

    public ExpGruposProcesoCorteController() {
        // Inform the Abstract parent controller of the concrete ExpProcesosCorte Entity
        super(ExpGruposProcesoCorte.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

}
