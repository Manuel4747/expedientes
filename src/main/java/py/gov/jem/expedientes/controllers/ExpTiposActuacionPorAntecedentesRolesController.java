package py.gov.jem.expedientes.controllers;

import java.util.Date;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.ExpTiposActuacionPorAntecedentesRoles;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "expTiposActuacionPorAntecedentesRolesController")
@ViewScoped
public class ExpTiposActuacionPorAntecedentesRolesController extends AbstractController<ExpTiposActuacionPorAntecedentesRoles> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosController estadoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private DocumentosJudicialesController documentosJudicialesController;
    @Inject
    private UsuariosController usuariosController;

    public ExpTiposActuacionPorAntecedentesRolesController() {
        // Inform the Abstract parent controller of the concrete RolesPorUsuarios Entity
        super(ExpTiposActuacionPorAntecedentesRoles.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getExpTiposActuacionPorAntecedentesRolesPK().setRol(this.getSelected().getRol().getId());
        this.getSelected().getExpTiposActuacionPorAntecedentesRolesPK().setTipoActuacion(this.getSelected().getTipoActuacion().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setExpTiposActuacionPorAntecedentesRolesPK(new py.gov.jem.expedientes.models.ExpTiposActuacionPorAntecedentesRolesPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        documentosJudicialesController.setSelected(null);
        usuariosController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Estados controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }
    
}
