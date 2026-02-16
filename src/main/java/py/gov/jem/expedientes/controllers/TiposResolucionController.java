package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.TiposResolucion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "tiposResolucionController")
@ViewScoped
public class TiposResolucionController extends AbstractController<TiposResolucion> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;

    public TiposResolucionController() {
        // Inform the Abstract parent controller of the concrete TiposResolucion Entity
        super(TiposResolucion.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("TiposResolucion_items");
        if (paramItems != null) {
            setItems((Collection<TiposResolucion>) paramItems);
            setLazyItems((Collection<TiposResolucion>) paramItems);
        }
    }
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
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

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }
    
    @Override
    public Collection<TiposResolucion> getItems() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return this.ejbFacade.getEntityManager().createNamedQuery("TiposResolucion.findAll", TiposResolucion.class).getResultList();
    }
}
