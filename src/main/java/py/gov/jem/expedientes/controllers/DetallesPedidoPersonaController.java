package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.DetallesPedidoPersona;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

@Named(value = "detallesPedidoPersonaController")
@ViewScoped
public class DetallesPedidoPersonaController extends AbstractController<DetallesPedidoPersona> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;

    public DetallesPedidoPersonaController() {
        // Inform the Abstract parent controller of the concrete Archivos Entity
        super(DetallesPedidoPersona.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("Archivos_items");
        if (paramItems != null) {
            setItems((Collection<DetallesPedidoPersona>) paramItems);
            setLazyItems((Collection<DetallesPedidoPersona>) paramItems);
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
    
    @Override
    public Collection<DetallesPedidoPersona> getItems() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return this.ejbFacade.getEntityManager().createNamedQuery("Archivos.findAll", DetallesPedidoPersona.class).getResultList();
    }
}
