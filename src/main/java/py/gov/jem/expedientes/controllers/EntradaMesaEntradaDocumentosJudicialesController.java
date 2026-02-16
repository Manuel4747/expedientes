package py.gov.jem.expedientes.controllers;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.DocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.Departamentos;
import py.gov.jem.expedientes.models.EntradasDocumentosJudiciales;
import py.gov.jem.expedientes.models.EstadosDocumento;
import py.gov.jem.expedientes.models.EstadosProcesalesDocumentosJudiciales;
import py.gov.jem.expedientes.models.TiposDocumentosJudiciales;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "entradaMesaEntradaDocumentosJudicialesController")
@ViewScoped
public class EntradaMesaEntradaDocumentosJudicialesController extends AbstractController<DocumentosJudiciales> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EstadosDocumentoController estadoController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    private EntradasDocumentosJudicialesController entradasDocumentosJudicialesController;
    @Inject
    private EstadosDocumentoController estadosDocumentoController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesController estadosProcesalesDocumentosJudicialesController;

    private String descripcionMesaEntrada;
    private String folios;
    private Collection<DocumentosJudiciales> detalles;
    private DocumentosJudiciales detalleSelected;
    private String nuevoRecurrente;
    private CanalesEntradaDocumentoJudicial canal;
    private EntradasDocumentosJudiciales entradaDocumentoJudicial;
    private Departamentos departamento;
    private Usuarios usuario;
    private Date fechaDesde;
    private Date fechaHasta;
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    private Collection<Usuarios> listaUsuariosTransf;
    private String denunciaEnContraDe;
    private String denunciante;
    private String patrocinio;
    private String nroOficio;
    private String acusacion;
    private Date fechaHoraAlta;
    private Usuarios responsableDestino;

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
    }

    public String getAcusacion() {
        return acusacion;
    }

    public void setAcusacion(String acusacion) {
        this.acusacion = acusacion;
    }

    public String getNroOficio() {
        return nroOficio;
    }

    public void setNroOficio(String nroOficio) {
        this.nroOficio = nroOficio;
    }

    public String getDenunciaEnContraDe() {
        return denunciaEnContraDe;
    }

    public void setDenunciaEnContraDe(String denunciaEnContraDe) {
        this.denunciaEnContraDe = denunciaEnContraDe;
    }

    public String getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(String denunciante) {
        this.denunciante = denunciante;
    }

    public String getPatrocinio() {
        return patrocinio;
    }

    public void setPatrocinio(String patrocinio) {
        this.patrocinio = patrocinio;
    }

    public String obtenerSgteNroMesaEntradaJudicial() {
        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select ifnull(max(CONVERT(nro_mesa_entrada,UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'JU' AND nro_mesa_entrada not like 'AUTO%';", NroMesaEntrada.class);

        NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

        return String.valueOf(cod.getCodigo() + 1);
    }

    public EntradaMesaEntradaDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudiciales.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        estadoController.setSelected(null);
        departamentoController.setSelected(null);
        tiposDocumentosJudicialesController.setSelected(null);
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
     * Sets the "selected" attribute of the EstadosDocumento controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
        }
    }
}
