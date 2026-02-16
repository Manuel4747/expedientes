package py.gov.jem.expedientes.controllers;

import java.util.List;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import py.gov.jem.expedientes.models.ExpTiposActuacion;

@Named(value = "cargaActuacionesController")
@ViewScoped
public class CargaActuacionesController extends AbstractController<DocumentosJudiciales> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    private List<ExpTiposActuacion> listaTiposActuacion;

    public List<ExpTiposActuacion> getListaTiposActuacion() {
        return listaTiposActuacion;
    }

    public void setListaTiposActuacion(List<ExpTiposActuacion> listaTiposActuacion) {
        this.listaTiposActuacion = listaTiposActuacion;
    }

    public CargaActuacionesController() {
        // Inform the Abstract parent controller of the concrete DespachosPersona Entity
        super(DocumentosJudiciales.class);
    }

    

}
