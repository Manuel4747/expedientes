package py.gov.jem.expedientes.controllers;

import java.util.Date;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "expPersonasPorDocumentosJudicialesController")
@ViewScoped
public class ExpPartesPorDocumentosJudicialesController extends AbstractController<ExpPartesPorDocumentosJudiciales> {

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

    public ExpPartesPorDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete RolesPorUsuarios Entity
        super(ExpPartesPorDocumentosJudiciales.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getPartesPorDocumentosJudicialesPK().setPersona(this.getSelected().getPersona().getId());
        this.getSelected().getPartesPorDocumentosJudicialesPK().setDocumentoJudicial(this.getSelected().getDocumentoJudicial().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setPartesPorDocumentosJudicialesPK(new py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesPK());
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
    
     @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Personas per = (Personas) session.getAttribute("Persona");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(per);
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Personas per = (Personas) session.getAttribute("Persona");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(per);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setPersonaAlta(per);
            getSelected().setEmpresa(per.getEmpresa());

            super.saveNew(event);
        }

    }
}
