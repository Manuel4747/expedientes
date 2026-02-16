package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.ExpDetallesOrdenDia;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "detallesOrdenDiaController")
@ViewScoped
public class ExpDetallesOrdenDiaController extends AbstractController<ExpDetallesOrdenDia> {

    @Inject
    private EmpresasController empresaController;

    public ExpDetallesOrdenDiaController() {
        // Inform the Abstract parent controller of the concrete AgDetallesAgendamiento Entity
        super(ExpDetallesOrdenDia.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }


}
