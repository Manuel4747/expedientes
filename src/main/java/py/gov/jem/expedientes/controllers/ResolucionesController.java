package py.gov.jem.expedientes.controllers;

import java.util.Date;
import java.util.List;

import py.gov.jem.expedientes.models.Resoluciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.EstadosProceso;
import py.gov.jem.expedientes.models.Resuelve;
import py.gov.jem.expedientes.models.SubtiposResolucion;
import py.gov.jem.expedientes.models.TiposExpediente;
import py.gov.jem.expedientes.models.TiposResolucion;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "resolucionesController")
@ViewScoped
public class ResolucionesController extends AbstractController<Resoluciones> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    DocumentosJudicialesController docController;

    private UploadedFile file;
    private Date fechaDesde;
    private Date fechaHasta;

    private Date fechaAltaDesde;
    private Date fechaAltaHasta;

    private String nroResolucion;
    private TiposResolucion tipoResolucion;
    private Date fecha;
    private Date fechaSesion;
    private String caratula;
    private DocumentosJudiciales documentoJudicial;
    private boolean firmado;

    private EstadosProceso estadoProceso;

    private boolean busquedaPorFechaAlta;
    private List<Resuelve> listaResuelve;
    private List<DocumentosJudiciales> listaDocumentosJudiciales;
    private HttpSession session;
    private Usuarios usu;
    private CanalesEntradaDocumentoJudicial canal;
    private String ultimaObservacion;
    private String ultimaObservacionActual;
    private TiposExpediente tipoExpediente;
    private List<TiposExpediente> listaTiposExpediente;
    private String mostrarWeb;
    private final FiltroURL filtroURL = new FiltroURL();
    private SubtiposResolucion subtipoResolucion;
    private List<SubtiposResolucion> listaSubtiposResolucion;

    public Date getFechaAltaDesde() {
        return fechaAltaDesde;
    }

    public void setFechaAltaDesde(Date fechaAltaDesde) {
        this.fechaAltaDesde = fechaAltaDesde;
    }

    public Date getFechaAltaHasta() {
        return fechaAltaHasta;
    }

    public void setFechaAltaHasta(Date fechaAltaHasta) {
        this.fechaAltaHasta = fechaAltaHasta;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(Date fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public EstadosProceso getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(EstadosProceso estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public List<Resuelve> getListaResuelve() {
        return listaResuelve;
    }

    public void setListaResuelve(List<Resuelve> listaResuelve) {
        this.listaResuelve = listaResuelve;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }

    public TiposResolucion getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(TiposResolucion tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public SubtiposResolucion getSubtipoResolucion() {
        return subtipoResolucion;
    }

    public void setSubtipoResolucion(SubtiposResolucion subtipoResolucion) {
        this.subtipoResolucion = subtipoResolucion;
    }

    public List<SubtiposResolucion> getListaSubtiposResolucion() {
        return listaSubtiposResolucion;
    }

    public void setListaSubtiposResolucion(List<SubtiposResolucion> listaSubtiposResolucion) {
        this.listaSubtiposResolucion = listaSubtiposResolucion;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public TiposExpediente getTipoExpediente() {
        return tipoExpediente;
    }

    public void setTipoExpediente(TiposExpediente tipoExpediente) {
        this.tipoExpediente = tipoExpediente;
    }

    public List<TiposExpediente> getListaTiposExpediente() {
        return listaTiposExpediente;
    }

    public void setListaTiposExpediente(List<TiposExpediente> listaTiposExpediente) {
        this.listaTiposExpediente = listaTiposExpediente;
    }

    public String getMostrarWeb() {
        return mostrarWeb;
    }

    public void setMostrarWeb(String mostrarWeb) {
        this.mostrarWeb = mostrarWeb;
    }

    public ResolucionesController() {
        // Inform the Abstract parent controller of the concrete Resoluciones Entity
        super(Resoluciones.class);
    }

}
