package py.gov.jem.expedientes.controllers;


import py.gov.jem.expedientes.models.EntradasDocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "entradasDocumentosJudicialesController")
@ViewScoped
public class EntradasDocumentosJudicialesController extends AbstractController<EntradasDocumentosJudiciales> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    public EntradasDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete EntradasDocumentosJudiciales Entity
        super(EntradasDocumentosJudiciales.class);
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
    }
}
