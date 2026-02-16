package py.gov.jem.expedientes.controllers;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.RootRenderer;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.DocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.controllers.util.MontoALetras;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.Departamentos;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.EntradasDocumentosJudiciales;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.EstadosDocumento;
import py.gov.jem.expedientes.models.EstadosProcesalesDocumentosJudiciales;
import py.gov.jem.expedientes.models.EstadosProceso;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpCategoriasActuacion;
import py.gov.jem.expedientes.models.ExpEstadosNotificacion;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.ExpNotificaciones;
import py.gov.jem.expedientes.models.ExpObjetosActuacion;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesPK;
import py.gov.jem.expedientes.models.ExpPersonasHabilitadasPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpRevisionesPorActuaciones;
import py.gov.jem.expedientes.models.ExpRolesFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.ExpTiposActuacion;
import py.gov.jem.expedientes.models.ExpTiposParte;
import py.gov.jem.expedientes.models.Firmas;
import py.gov.jem.expedientes.models.ObservacionesDocumentosJudiciales;
import py.gov.jem.expedientes.models.ObservacionesDocumentosJudicialesAntecedentes;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.PersonasPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.Resoluciones;
import py.gov.jem.expedientes.models.Resuelve;
import py.gov.jem.expedientes.models.ResuelvePorResolucionesPorPersonas;
import py.gov.jem.expedientes.models.SubtiposResolucion;
import py.gov.jem.expedientes.models.TiposDocumentosJudiciales;
import py.gov.jem.expedientes.models.TiposExpediente;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "documentosJudicialesViejosController")
@ViewScoped
public class DocumentosJudicialesViejosController extends AbstractController<DocumentosJudiciales> {

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
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    @Inject
    private ObservacionesDocumentosJudicialesController obsController;
    @Inject
    private ObservacionesDocumentosJudicialesAntecedentesController obsAntecedenteController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesController estadosProcesalesDocumentosJudicialesController;
    @Inject
    private EntradasDocumentosJudicialesController entradasDocumentosJudicialesController;
    @Inject
    private PersonasPorDocumentosJudicialesController personasPorDocumentosJudicialesController;
    @Inject
    private ExpPartesPorDocumentosJudicialesController partesPorDocumentosJudicialesController;
    @Inject
    private ExpActuacionesController expActuacionesController;
    @Inject
    private ExpHistActuacionesController expHistActuacionesController;
    @Inject
    private ParametrosSistemaController parametrosSistemaController;
    @Inject
    private FirmasController firmasController;
    @Inject
    private ExpNotificacionesController expNotificacionController;
    @Inject
    private ResolucionesController resolucionesController;
    @Inject
    ResuelvePorResolucionesPorPersonasController resuelvePorResolucionesPorPersonasController;
    @Inject
    ExpPersonasFirmantesPorActuacionesController personasFirmantesPorActuacionesController;
    @Inject
    ExpRolesFirmantesPorActuacionesController rolesFirmantesPorActuacionesController;
    @Inject
    ExpRevisionesPorActuacionesController revisionesPorActuacionesController;
    @Inject
    PersonasController personasController;

    private EntradasDocumentosJudiciales entradaDocumentoJudicial;
    private String nuevaCausa;
    private String nroCausa;
    private String nombreJuez;
    private String nombreEstado;
    private String ultimaObservacion;
    private CanalesEntradaDocumentoJudicial canal;
    private TiposDocumentosJudiciales tipoDoc;
    private Usuarios usuario;
    private Departamentos departamento;
    private Date fechaDesde;
    private Date fechaHasta;
    private String pantalla;
    private String updateCerrar;
    private String nombreVariable;
    private final FiltroURL filtroURL = new FiltroURL();
    private List<Personas> listaPersonas;
    private List<ResuelvePorResolucionesPorPersonas> listaPersonasResuelve;
    private Personas personaSelected;
    private Personas personaFirmanteSelected;
    private Personas revisionSelected;
    private Personas parteSelected;
    private Personas personaOrigenSelected;
    private Personas persona;
    private Personas personaFirmante;
    private AntecedentesRoles rolFirmante;
    private AntecedentesRoles rolFirmanteSelected;
    private List<AntecedentesRoles> listaRoles;
    private List<Personas> listaSorteo;
    private Personas sorteado;
    private String imagenSorteo;

    private Personas revision;
    private Personas parte;
    private List<Personas> listaPartesAlta;
    private ExpTiposParte tipoParte;
    private String caratula;
    private String caratulaAnterior;
    private EstadosProceso estadoProceso;
    private EstadosProceso estadoProcesoAnterior;
    private String causa;
    private TiposExpediente tipoExpedienteActual;
    private TiposExpediente tipoExpediente;
    private TiposExpediente tipoExpedienteAnterior;
    private boolean cambioAEnjuiciamiento;
    private Personas personaUsuario;
    private List<ExpActuaciones> listaActuaciones;
    private ExpActuaciones selectedActuacion;
    private ExpActuaciones docImprimir;
    private String content;
    private String nombre;
    private ParametrosSistema par;
    private HttpSession session;
    private Personas personaOrigen;
    private List<Personas> listaPersonasOrigen;
    private List<Personas> listaAbogados;
    private Personas abogado;
    private Personas abogadoSelected;
    private List<Personas> listaMagistrados;
    private Personas abogadoOrigen;
    private String ano;
    private String accion;
    private ExpActuaciones actuacion;
    private Integer idFirma;
    private String sessionId;
    private List<ExpTiposActuacion> listaTiposActuacion;
    private List<ExpObjetosActuacion> listaObjetosActuacion;
    private AntecedentesRoles rolElegido;
    private boolean esPrimerEscrito;
    private ExpActuaciones actuacionPadre;
    private String caratulaMostrar;
    private Integer fojas;
    private String titulo;
    private String tituloActuacion;
    private String tituloVerActuacion;
    private String botonRegistrar;
    private ExpCategoriasActuacion categoriaActuacion;
    private String labelTipo;
    private String caratulaBusqueda;
    private String url;
    private Integer itemsSize;
    private Integer newItemIx;
    private List<ExpPartesPorDocumentosJudiciales> listaPartes;
    private Personas destinatario;
    private Resoluciones resolucion;
    private List<Resuelve> listaResuelve;
    private List<SubtiposResolucion> listaSubtiposResolucion;
    private List<Personas> listaPersonasFirmantes;
    private List<AntecedentesRoles> listaRolesFirmantes;
    private List<Personas> listaRevisiones;
    private List<Personas> listaPartesAdmin;
    private boolean visible;
    private List<ExpTiposParte> listaTiposParte;
    private List<Personas> listaPosiblesFirmantes;
    private String cedula;
    private String nombresApellidos;
    private String cedulaAbogado;
    private String nombresApellidosAbogado;
    private String cedulaPersona;
    private String nombresApellidosPersona;
    private boolean esParte;
    private String endpoint;
    private boolean esApp;
    private String estadoProcesal;
    private String observacion;
    private List<EstadosProcesalesDocumentosJudiciales> listaEstadosProcesales;
    private List<ObservacionesDocumentosJudiciales> listaObservaciones;
    

    private UploadedFile file;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Personas getAbogadoSelected() {
        return abogadoSelected;
    }

    public void setAbogadoSelected(Personas abogadoSelected) {
        this.abogadoSelected = abogadoSelected;
    }

    public String getEstadoProcesal() {
        return estadoProcesal;
    }

    public void setEstadoProcesal(String estadoProcesal) {
        this.estadoProcesal = estadoProcesal;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<EstadosProcesalesDocumentosJudiciales> getListaEstadosProcesales() {
        return listaEstadosProcesales;
    }

    public void setListaEstadosProcesales(List<EstadosProcesalesDocumentosJudiciales> listaEstadosProcesales) {
        this.listaEstadosProcesales = listaEstadosProcesales;
    }

    public List<ObservacionesDocumentosJudiciales> getListaObservaciones() {
        return listaObservaciones;
    }

    public void setListaObservaciones(List<ObservacionesDocumentosJudiciales> listaObservaciones) {
        this.listaObservaciones = listaObservaciones;
    }

    public String getCedulaAbogado() {
        return cedulaAbogado;
    }

    public void setCedulaAbogado(String cedulaAbogado) {
        this.cedulaAbogado = cedulaAbogado;
    }

    public String getNombresApellidosAbogado() {
        return nombresApellidosAbogado;
    }

    public void setNombresApellidosAbogado(String nombresApellidosAbogado) {
        this.nombresApellidosAbogado = nombresApellidosAbogado;
    }

    public Personas getSorteado() {
        return sorteado;
    }

    public void setSorteado(Personas sorteado) {
        this.sorteado = sorteado;
    }

    public List<Personas> getListaPosiblesFirmantes() {
        return listaPosiblesFirmantes;
    }

    public void setListaPosiblesFirmantes(List<Personas> listaPosiblesFirmantes) {
        this.listaPosiblesFirmantes = listaPosiblesFirmantes;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getCaratulaAnterior() {
        return caratulaAnterior;
    }

    public void setCaratulaAnterior(String caratulaAnterior) {
        this.caratulaAnterior = caratulaAnterior;
    }

    public String getCaratulaMostrar() {
        return caratulaMostrar;
    }

    public void setCaratulaMostrar(String caratulaMostrar) {
        this.caratulaMostrar = caratulaMostrar;
    }

    public EstadosProceso getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(EstadosProceso estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public EstadosProceso getEstadoProcesoAnterior() {
        return estadoProcesoAnterior;
    }

    public void setEstadoProcesoAnterior(EstadosProceso estadoProcesoAnterior) {
        this.estadoProcesoAnterior = estadoProcesoAnterior;
    }

    public TiposExpediente getTipoExpedienteActual() {
        return tipoExpedienteActual;
    }

    public void setTipoExpedienteActual(TiposExpediente tipoExpedienteActual) {
        this.tipoExpedienteActual = tipoExpedienteActual;
    }

    public TiposExpediente getTipoExpediente() {
        return tipoExpediente;
    }

    public void setTipoExpediente(TiposExpediente tipoExpediente) {
        this.tipoExpediente = tipoExpediente;
    }

    public TiposExpediente getTipoExpedienteAnterior() {
        return tipoExpedienteAnterior;
    }

    public void setTipoExpedienteAnterior(TiposExpediente tipoExpedienteAnterior) {
        this.tipoExpedienteAnterior = tipoExpedienteAnterior;
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

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getNombreJuez() {
        return nombreJuez;
    }

    public void setNombreJuez(String nombreJuez) {
        this.nombreJuez = nombreJuez;
    }

    public String getNuevaCausa() {
        return nuevaCausa;
    }

    public void setNuevaCausa(String nuevaCausa) {
        this.nuevaCausa = nuevaCausa;
    }

    public String getCedulaPersona() {
        return cedulaPersona;
    }

    public void setCedulaPersona(String cedulaPersona) {
        this.cedulaPersona = cedulaPersona;
    }

    public String getNombresApellidosPersona() {
        return nombresApellidosPersona;
    }

    public void setNombresApellidosPersona(String nombresApellidosPersona) {
        this.nombresApellidosPersona = nombresApellidosPersona;
    }
    

    public EntradasDocumentosJudiciales getEntradaDocumentoJudicial() {
        return entradaDocumentoJudicial;
    }

    public void setEntradaDocumentoJudicial(EntradasDocumentosJudiciales entradaDocumentoJudicial) {
        this.entradaDocumentoJudicial = entradaDocumentoJudicial;
    }

    public String getUpdateCerrar() {
        return updateCerrar;
    }

    public void setUpdateCerrar(String updateCerrar) {
        this.updateCerrar = updateCerrar;
    }

    public String getNombreVariable() {
        return nombreVariable;
    }

    public void setNombreVariable(String nombreVariable) {
        this.nombreVariable = nombreVariable;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public Personas getPersonaSelected() {
        return personaSelected;
    }

    public void setPersonaSelected(Personas personaSelected) {
        this.personaSelected = personaSelected;
    }

    public Personas getPersonaFirmanteSelected() {
        return personaFirmanteSelected;
    }

    public void setPersonaFirmanteSelected(Personas personaFirmanteSelected) {
        this.personaFirmanteSelected = personaFirmanteSelected;
    }

    public AntecedentesRoles getRolFirmante() {
        return rolFirmante;
    }

    public void setRolFirmante(AntecedentesRoles rolFirmante) {
        this.rolFirmante = rolFirmante;
    }

    public AntecedentesRoles getRolFirmanteSelected() {
        return rolFirmanteSelected;
    }

    public void setRolFirmanteSelected(AntecedentesRoles rolFirmanteSelected) {
        this.rolFirmanteSelected = rolFirmanteSelected;
    }

    public List<AntecedentesRoles> getListaRolesFirmantes() {
        return listaRolesFirmantes;
    }

    public void setListaRolesFirmantes(List<AntecedentesRoles> listaRolesFirmantes) {
        this.listaRolesFirmantes = listaRolesFirmantes;
    }

    public Personas getParteSelected() {
        return parteSelected;
    }

    public void setParteSelected(Personas parteSelected) {
        this.parteSelected = parteSelected;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public ExpTiposParte getTipoParte() {
        return tipoParte;
    }

    public void setTipoParte(ExpTiposParte tipoParte) {
        this.tipoParte = tipoParte;
    }

    public String getNroCausa() {
        return nroCausa;
    }

    public void setNroCausa(String nroCausa) {
        this.nroCausa = nroCausa;
    }

    public List<Personas> getListaMagistrados() {
        return listaMagistrados;
    }

    public void setListaMagistrados(List<Personas> listaMagistrados) {
        this.listaMagistrados = listaMagistrados;
    }

    public List<ExpActuaciones> getListaActuaciones() {
        return listaActuaciones;
    }

    public void setListaActuaciones(List<ExpActuaciones> listaActuaciones) {
        this.listaActuaciones = listaActuaciones;
    }

    public ExpActuaciones getSelectedActuacion() {
        return selectedActuacion;
    }

    public void setSelectedActuacion(ExpActuaciones selectedActuacion) {
        this.selectedActuacion = selectedActuacion;
    }

    public Personas getPersonaOrigen() {
        return personaOrigen;
    }

    public void setPersonaOrigen(Personas personaOrigen) {
        this.personaOrigen = personaOrigen;
    }

    public Personas getAbogadoOrigen() {
        return abogadoOrigen;
    }

    public void setAbogadoOrigen(Personas abogadoOrigen) {
        this.abogadoOrigen = abogadoOrigen;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ExpTiposActuacion> getListaTiposActuacion() {
        return listaTiposActuacion;
    }

    public void setListaTiposActuacion(List<ExpTiposActuacion> listaTiposActuacion) {
        this.listaTiposActuacion = listaTiposActuacion;
    }

    public List<ExpObjetosActuacion> getListaObjetosActuacion() {
        return listaObjetosActuacion;
    }

    public void setListaObjetosActuacion(List<ExpObjetosActuacion> listaObjetosActuacion) {
        this.listaObjetosActuacion = listaObjetosActuacion;
    }

    public Personas getPersonaOrigenSelected() {
        return personaOrigenSelected;
    }

    public void setPersonaOrigenSelected(Personas personaOrigenSelected) {
        this.personaOrigenSelected = personaOrigenSelected;
    }

    public List<Personas> getListaPersonasOrigen() {
        return listaPersonasOrigen;
    }

    public void setListaPersonasOrigen(List<Personas> listaPersonasOrigen) {
        this.listaPersonasOrigen = listaPersonasOrigen;
    }

    public List<Personas> getListaAbogados() {
        return listaAbogados;
    }

    public void setListaAbogados(List<Personas> listaAbogados) {
        this.listaAbogados = listaAbogados;
    }

    public Integer getFojas() {
        return fojas;
    }

    public void setFojas(Integer fojas) {
        this.fojas = fojas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloActuacion() {
        return tituloActuacion;
    }

    public void setTituloActuacion(String tituloActuacion) {
        this.tituloActuacion = tituloActuacion;
    }

    public String getLabelTipo() {
        return labelTipo;
    }

    public void setLabelTipo(String labelTipo) {
        this.labelTipo = labelTipo;
    }

    public String getTituloVerActuacion() {
        return tituloVerActuacion;
    }

    public void setTituloVerActuacion(String tituloVerActuacion) {
        this.tituloVerActuacion = tituloVerActuacion;
    }

    public String getBotonRegistrar() {
        return botonRegistrar;
    }

    public void setBotonRegistrar(String botonRegistrar) {
        this.botonRegistrar = botonRegistrar;
    }

    public String getCaratulaBusqueda() {
        return caratulaBusqueda;
    }

    public void setCaratulaBusqueda(String caratulaBusqueda) {
        this.caratulaBusqueda = caratulaBusqueda;
    }

    public Integer getItemsSize() {
        return itemsSize;
    }

    public void setItemsSize(Integer itemsSize) {
        this.itemsSize = itemsSize;
    }

    public Integer getNewItemIx() {
        return newItemIx;
    }

    public void setNewItemIx(Integer newItemIx) {
        this.newItemIx = newItemIx;
    }

    public List<ExpPartesPorDocumentosJudiciales> getListaPartes() {
        return listaPartes;
    }

    public void setListaPartes(List<ExpPartesPorDocumentosJudiciales> listaPartes) {
        this.listaPartes = listaPartes;
    }

    public Personas getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Personas destinatario) {
        this.destinatario = destinatario;
    }

    public Resoluciones getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resoluciones resolucion) {
        this.resolucion = resolucion;
    }

    public List<SubtiposResolucion> getListaSubtiposResolucion() {
        return listaSubtiposResolucion;
    }

    public void setListaSubtiposResolucion(List<SubtiposResolucion> listaSubtiposResolucion) {
        this.listaSubtiposResolucion = listaSubtiposResolucion;
    }

    public List<ResuelvePorResolucionesPorPersonas> getListaPersonasResuelve() {
        return listaPersonasResuelve;
    }

    public void setListaPersonasResuelve(List<ResuelvePorResolucionesPorPersonas> listaPersonasResuelve) {
        this.listaPersonasResuelve = listaPersonasResuelve;
    }

    public List<Resuelve> getListaResuelve() {
        return listaResuelve;
    }

    public void setListaResuelve(List<Resuelve> listaResuelve) {
        this.listaResuelve = listaResuelve;
    }

    public Personas getPersonaFirmante() {
        return personaFirmante;
    }

    public void setPersonaFirmante(Personas personaFirmante) {
        this.personaFirmante = personaFirmante;
    }

    public Personas getParte() {
        return parte;
    }

    public void setParte(Personas parte) {
        this.parte = parte;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<ExpTiposParte> getListaTiposParte() {
        return listaTiposParte;
    }

    public void setListaTiposParte(List<ExpTiposParte> listaTiposParte) {
        this.listaTiposParte = listaTiposParte;
    }

    public List<Personas> getListaPartesAlta() {
        return listaPartesAlta;
    }

    public void setListaPartesAlta(List<Personas> listaPartesAlta) {
        this.listaPartesAlta = listaPartesAlta;
    }

    public Personas getRevisionSelected() {
        return revisionSelected;
    }

    public void setRevisionSelected(Personas revisionSelected) {
        this.revisionSelected = revisionSelected;
    }

    public Personas getRevision() {
        return revision;
    }

    public void setRevision(Personas revision) {
        this.revision = revision;
    }

    public List<AntecedentesRoles> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<AntecedentesRoles> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<Personas> getListaSorteo() {
        return listaSorteo;
    }

    public void setListaSorteo(List<Personas> listaSorteo) {
        this.listaSorteo = listaSorteo;
    }

    public String getImagenSorteo() {
        return imagenSorteo;
    }

    public void setImagenSorteo(String imagenSorteo) {
        this.imagenSorteo = imagenSorteo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public List<Personas> getListaPersonasActivas() {
        List<Personas> listaPersonasActivas = new ArrayList<>();
        if (listaPersonas != null) {
            for (Personas per : listaPersonas) {
                if ("AC".equals(per.getEstado())) {
                    listaPersonasActivas.add(per);
                }
            }
        }

        return listaPersonasActivas;
    }

    public List<Personas> getListaPersonasFirmantesActivas() {
        List<Personas> listaPersonasFirmantesActivas = new ArrayList<>();
        if (listaPersonasFirmantes != null) {
            for (Personas per : listaPersonasFirmantes) {
                if ("AC".equals(per.getEstado())) {
                    listaPersonasFirmantesActivas.add(per);
                }
            }
        }

        return listaPersonasFirmantesActivas;
    }

    public List<AntecedentesRoles> getListaRolesFirmantesActivas() {
        List<AntecedentesRoles> listaRolesFirmantesActivas = new ArrayList<>();
        if (listaRolesFirmantes != null) {
            for (AntecedentesRoles per : listaRolesFirmantes) {
                if ("AC".equals(per.getEstado())) {
                    listaRolesFirmantesActivas.add(per);
                }
            }
        }

        return listaRolesFirmantesActivas;
    }

    public List<Personas> getListaRevisionesActivas() {
        List<Personas> listaRevisionesActivas = new ArrayList<>();
        if (listaRevisiones != null) {
            for (Personas per : listaRevisiones) {
                if ("AC".equals(per.getEstado())) {
                    listaRevisionesActivas.add(per);
                }
            }
        }

        return listaRevisionesActivas;
    }

    public List<Personas> getListaPartesActivas() {
        List<Personas> listaPartesActivas = new ArrayList<>();
        if (listaPartesAdmin != null) {
            for (Personas per : listaPartesAdmin) {
                if ("AC".equals(per.getEstado())) {
                    listaPartesActivas.add(per);
                }
            }
        }

        return listaPartesActivas;
    }

    public List<Personas> getListaPersonasOrigenActivas() {
        List<Personas> listaPersonasOrigenActivas = new ArrayList<>();
        if (listaPersonasOrigen != null) {
            for (Personas per : listaPersonasOrigen) {
                if ("AC".equals(per.getEstado())) {
                    listaPersonasOrigenActivas.add(per);
                }
            }
        }

        return listaPersonasOrigenActivas;
    }

    public List<Personas> getListaAbogadosActivos() {
        List<Personas> listaAbogadosActivos = new ArrayList<>();
        if (listaAbogados != null) {
            for (Personas per : listaAbogados) {
                if ("AC".equals(per.getEstado())) {
                    listaAbogadosActivos.add(per);
                }
            }
        }

        return listaAbogadosActivos;
    }

    public DocumentosJudicialesViejosController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudiciales.class);
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getArchivo() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + docImprimir.getArchivo()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }
                }

                if (fileByte != null) {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);

                    String partes[] = docImprimir.getArchivo().split("[.]");
                    String ext = "pdf";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
                    outputStream.write(fileByte);

                    outputStream.close();

                    // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void refrescar() {
        buscar();
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        imagenSorteo = "images/sorteo_inicio2.jpg";
        sorteado = null;

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        accion = params.get("tipo");

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];

        if (Constantes.ACCION_CONSULTA.equals(accion)) {
            titulo = "CONSULTA EXPEDIENTES";
            visible = true;
            categoriaActuacion = null;
        } else if (Constantes.ACCION_REGISTRAR.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR PRESENTACION";
            labelTipo = "Tipo Documento";
            categoriaActuacion = new ExpCategoriasActuacion(1);
            visible = true;
        } else if (Constantes.ACCION_CREAR.equals(accion)) {
            titulo = tituloActuacion = "CREAR EXPEDIENTE";
            labelTipo = "Tipo documento";
            categoriaActuacion = new ExpCategoriasActuacion(1);
            visible = true;
        } else if (Constantes.ACCION_CREAR_VIEJO.equals(accion)) {
            titulo = tituloActuacion = "CREAR EXPEDIENTE VIEJO";
            labelTipo = "Tipo documento";
            categoriaActuacion = new ExpCategoriasActuacion(1);
            visible = true;
        } else if (Constantes.ACCION_REGISTRAR_RECURSO.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR RECURSO";
            labelTipo = "Tipo recurso";
            categoriaActuacion = new ExpCategoriasActuacion(2);
            visible = true;
        } else if (Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR INCIDENTE/RECUSACIÓN";
            labelTipo = "Tipo incidente";
            categoriaActuacion = new ExpCategoriasActuacion(3);
            visible = true;
        } else if (Constantes.ACCION_REGISTRAR_PROVIDENCIA.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR PROVIDENCIA";
            labelTipo = "Tipo providencia";
            categoriaActuacion = new ExpCategoriasActuacion(4);
            visible = false;
        } else if (Constantes.ACCION_REGISTRAR_OFICIO.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR OFICIO";
            labelTipo = "Tipo oficio";
            categoriaActuacion = new ExpCategoriasActuacion(5);
            visible = false;
        } else if (Constantes.ACCION_REGISTRAR_NOTIFICACION.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR NOTIFICACION";
            labelTipo = "Tipo notificacion";
            categoriaActuacion = new ExpCategoriasActuacion(6);
            visible = false;
        } else if (Constantes.ACCION_REGISTRAR_RESOLUCION.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR RESOLUCION";
            labelTipo = "Tipo Resolucion";
            categoriaActuacion = new ExpCategoriasActuacion(7);
            visible = false;
        } else {
            JsfUtil.addErrorMessage("Accion no permitida: " + accion);
            return;
        }

        listaObjetosActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findByCategoriaActuacion", ExpObjetosActuacion.class).setParameter("categoriaActuacion", categoriaActuacion).getResultList();

        departamento = departamentoController.prepareCreate(null);
        departamento.setId(40); // Secretaria
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE);
        tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_JU);
        nroCausa = "";

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();

        String act = (String) session.getAttribute("actuacionId");

        session.removeAttribute("actuacionId");

        Object esAppObj = session.getAttribute("esApp");

        esApp = (esAppObj == null ? false : (boolean) esAppObj);

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");
        usuario = (Usuarios) session.getAttribute("Usuarios");

        pantalla = Constantes.NO;

        // fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        // fechaHasta = ejbFacade.getSystemDateOnly();
        fechaDesde = null;
        fechaHasta = null;

        // listaTiposActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findByRolCategoriaActuacion", ExpTiposActuacion.class).setParameter("categoriaActuacion", categoriaActuacion).setParameter("rol", rolElegido.getId()).getResultList();
        listaTiposActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findByCategoriaActuacion", ExpTiposActuacion.class).setParameter("categoriaActuacion", categoriaActuacion).getResultList();

        if (act != null) {
            verActuacion(act);
            return;
        }

        if (filtroURL.verifPermiso(Constantes.GENERAR_ACUSACION, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            prepareCreateAcusacion();
        } else if (filtroURL.verifPermiso(Constantes.GENERAR_ACUSACION_VIEJA, rolElegido.getId()) && Constantes.ACCION_CREAR_VIEJO.equals(accion)) {
            prepareCreateAcusacion();
        } else if (filtroURL.verifPermiso(Constantes.GENERAR_INVESTIGACION_PRELIMINAR, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            prepareCreateInvestigacionPreliminar();
        } else if (filtroURL.verifPermiso(Constantes.GENERAR_PEDIDO_DESAFUERO, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            prepareCreatePedidoDesafuero();
        }

    }
    
    public boolean renderedMostrarExpedienteWeb(){
        return filtroURL.verifPermiso(Constantes.PERMISO_EXPONER_EXPEDIENTE_PDF, rolElegido.getId());
    }
    
    public void actualizarMostrarExpedienteWeb(){
        if(getSelected() != null){
            super.save(null);
        }
    }

    /*
    public void obtenerInstalador1() {

        nombre = "";
        try {

                byte[] fileByte = null;

                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + "firmajem-1.0.msi").toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }


            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=firmajem-1.0.msi");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        
    }
     */
    public String obtenerInstalador1() {

        return url + "/instaladores/firmajem-1.0.exe";
    }

    public String obtenerInstalador2() {

        // return url + "/instaladores/registro_firma.cmd";
//        
//                    try {
//                        fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + docImprimir.getArchivo()).toPath());
//                    } catch (IOException ex) {
//                        JsfUtil.addErrorMessage("No tiene documento adjunto");
//                        content = "";
//                    }
        File file = new File(par.getRutaArchivos() + "/registro_firma.cmd");
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setHeader("Content-Disposition", "attachment;filename=registro_firma.cmd");
        response.setContentLength((int) file.length());
        ServletOutputStream out = null;

        try {
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            int i = 0;
            while ((i = input.read(buffer)) != -1) {
                out.write(buffer);
                out.flush();
            }
            FacesContext.getCurrentInstance().getResponseComplete();
        } catch (IOException err) {
            err.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
        return null;
    }

    public void resetearSorteo() {
        imagenSorteo = "images/sorteo_final2.jpg";
    }

    public void sortear() {
        if (listaSorteo != null) {
            imagenSorteo = "images/sorteo2.gif";
            sorteado = getRandomElement(listaSorteo);
            PrimeFaces.current().executeScript("startTimer();");
        }
    }

    /*

    public void sortearTodo() {
        sortear();
        sortear2();
    }
    
    public void actualizarSorteo(){
        PrimeFaces.current().ajax().update("SorteoForm");
    }


    public void sortear2() { 
        int contador = 4000;
        while (contador > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
            }
            contador--;
        }
        if(listaSorteo != null){
        sorteado = getRandomElement(listaSorteo);
        // PrimeFaces.current().ajax().update("SorteoForm:sorteado");
        }
    }
     */
    public Personas getRandomElement(List<Personas> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public void prepareSorteo() {
        sorteado = null;
        imagenSorteo = "images/sorteo_inicio2.jpg";
        listaSorteo = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_FISCAL_ACUSADOR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
    }

    private void verActuacion(String actId) {
        selectedActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", Integer.valueOf(actId)).getSingleResult();
        nroCausa = selectedActuacion.getDocumentoJudicial().getCausa();
        buscar();

        setSelected(selectedActuacion.getDocumentoJudicial());

        prepareVerActuaciones();

        if (listaActuaciones != null) {
            itemsSize = listaActuaciones.size();
            newItemIx = listaActuaciones.indexOf(selectedActuacion);
        }

        PrimeFaces.current().executeScript("getNewItemPos()");

        // RequestContext.getCurrentInstance().execute("PF('widgetVar').getPaginator().setPage(pageno)");
    }

    /*
    public void actualizarPartes() {
        if (getSelected() != null) {
            if (tipoParte != null) {
                parte = null;
                if (Constantes.TIPO_PARTE_FISCAL_ACUSADOR == tipoParte.getId()) {
                    List<ExpFiscalesJurado> lista = ejbFacade.getEntityManager().createNamedQuery("ExpFiscalesJurado.findAll", ExpFiscalesJurado.class).getResultList();
                    listaPartesAlta = new ArrayList<>();
                    for (ExpFiscalesJurado per : lista) {
                        listaPartesAlta.add(per.getPersona());
                    }
                } else {
                    listaPartesAlta = ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList();
                }

            }
        }
    }
     */
    public void actualizarPartes() {
        if (getSelected() != null) {
            if (tipoParte != null) {
                parte = null;
                if (Constantes.TIPO_PARTE_FISCAL_ACUSADOR == tipoParte.getId()) {
                    listaPartesAlta = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_FISCAL_ACUSADOR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                } else {
                    listaPartesAlta = ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList();
                }

            }
        }
    }

    public void actualizarAcusados(DocumentosJudiciales doc) {
        if (doc != null) {
            try {
                List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();

                Personas per = null;
                listaPersonasResuelve = new ArrayList<>();
                ResuelvePorResolucionesPorPersonas res = null;
                for (int i = 0; i < listaPersonasActual.size(); i++) {
                    per = listaPersonasActual.get(i).getPersona();
                    res = resuelvePorResolucionesPorPersonasController.prepareCreate(null);
                    res.setPersona(per);
                    listaPersonasResuelve.add(res);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public List<ResuelvePorResolucionesPorPersonas> obtenerListaPersonasResuelve(Resoluciones resolucion, DocumentosJudiciales doc) {
        List<ResuelvePorResolucionesPorPersonas> listaPer = null;
        try {
            listaPer = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByActuacion", ResuelvePorResolucionesPorPersonas.class).setParameter("resolucion", resolucion).setParameter("estado", new Estados("AC")).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            listaPer = new ArrayList<>();
        }

        return listaPer;

    }

    public void prepareEditFirmantes(ExpActuaciones act) {

        if (act != null) {

            selectedActuacion = act;

            listaPosiblesFirmantes = ejbFacade.getEntityManager().createNamedQuery("Personas.findByPermisoEstado", Personas.class).setParameter("funcion", Constantes.PERMISO_FIRMAR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("estado", new Estados("AC")).getResultList();

            listaPersonasFirmantes = new ArrayList<>();
            personaFirmante = null;

            for (ExpPersonasFirmantesPorActuaciones per : lista) {
                per.getPersonaFirmante().setEstado("AC");
                per.getPersonaFirmante().setFirmado(per.isFirmado());
                listaPersonasFirmantes.add(per.getPersonaFirmante());
            }
            /*
            listaRoles = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRoles.findButId", AntecedentesRoles.class).setParameter("id", Constantes.ROL_ANTECEDENTES).getResultList();

            List<ExpRolesFirmantesPorActuaciones> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacionEstado", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("estado", new Estados("AC")).getResultList();

            listaRolesFirmantes = new ArrayList<>();
            rolFirmante = null;

            for (ExpRolesFirmantesPorActuaciones per : lista2) {
                per.getRolFirmante().setEstado("AC");
                per.getRolFirmante().setFirmado(per.isFirmado());
                listaRolesFirmantes.add(per.getRolFirmante());
            }
             */
        }

    }

    public void prepareEditRevisiones(ExpActuaciones act) {

        if (act != null) {

            selectedActuacion = act;

            List<ExpRevisionesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpRevisionesPorActuaciones.findByActuacionEstado", ExpRevisionesPorActuaciones.class).setParameter("actuacion", act).setParameter("estado", new Estados("AC")).getResultList();

            listaRevisiones = new ArrayList<>();
            revision = null;

            for (ExpRevisionesPorActuaciones per : lista) {
                per.getRevisionPor().setEstado("AC");
                per.getRevisionPor().setFirmado(per.isRevisado());
                listaRevisiones.add(per.getRevisionPor());
            }
        }

    }

    public void prepareListaEstadosProcesales() {

        if (getSelected() != null) {
            listaEstadosProcesales = ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByDocumentoJudicial", EstadosProcesalesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();
        }
    }

    public void prepareListaObservaciones() {

        if (getSelected() != null) {
            listaObservaciones = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();
        }
    }

    public void prepareEditPartes() {

        if (getSelected() != null) {

            parte = null;
            tipoParte = null;
            listaPartesAlta = null;

            List<ExpPartesPorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("estado", new Estados("AC")).getResultList();

            listaPartesAdmin = new ArrayList<>();

            for (ExpPartesPorDocumentosJudiciales per : lista) {
                per.getPersona().setEstado("AC");
                per.getPersona().setTipoParte(per.getTipoParte());
                listaPartesAdmin.add(per.getPersona());
            }
            List<PersonasPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("estado", new Estados("AC")).getResultList();

            ExpTiposParte tipoParteAcusado = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ACUSADO).getSingleResult();

            for (PersonasPorDocumentosJudiciales per : lista2) {

                per.getPersona().setTipoParte(tipoParteAcusado);
                listaPartesAdmin.add(per.getPersona());
            }

            listaTiposParte = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findAll", ExpTiposParte.class).getResultList();
        }
    }

    public Resoluciones prepareCreateResolucion() {
        if (getSelected() != null) {
            observacion = "";
            estadoProcesal = "";
            resolucion = resolucionesController.prepareCreate(null);
            actuacion = expActuacionesController.prepareCreate(null);
            ExpObjetosActuacion obj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_PRESENTACION).getSingleResult();
            actuacion.setObjetoActuacion(obj);
            resolucion.setCaratula(getSelected().getCaratulaString());
            ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();
            actuacion.setTipoActuacion(tipoActuacion);

            actualizarAcusados(getSelected());

            PrimeFaces.current().ajax().update("ResolucionesCreateForm");
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('ResolucionesCreateDialog').show();");
        }

        return resolucion;
    }

    public ExpActuaciones prepareCreateActuacion(boolean parEsPrimerEscrito) {

        if (getSelected() != null) {
            actuacion = expActuacionesController.prepareCreate(null);

            observacion = "";
            estadoProcesal = "";

            actuacion.setVisible(visible);

            /*
            if (Constantes.ACCION_REGISTRAR.equals(accion)) {
                esPrimerEscrito = true;
            } else if (Constantes.ACCION_REGISTRAR_RESURSO.equals(accion)) {
                esPrimerEscrito = false;
            } else if (Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion)) {
                esPrimerEscrito = false;
            }
             */
            esPrimerEscrito = parEsPrimerEscrito;

            if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR.equals(accion)) {
                actuacionPadre = null;
            } else {
                ExpObjetosActuacion obj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_PRESENTACION).getSingleResult();
                actuacion.setObjetoActuacion(obj);
            }

            caratulaMostrar = getSelected().getCaratulaString();

            ExpTiposActuacion tipoActuacion = null;
            if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR.equals(accion)) {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_ESCRITO_PRESENTACION).getSingleResult();
            } else if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR_PROVIDENCIA.equals(accion)) {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PROVIDENCIA).getSingleResult();
            } else if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR_OFICIO.equals(accion)) {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_OFICIO).getSingleResult();
            } else if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR_RECURSO.equals(accion)) {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RECURSO).getSingleResult();
            } else if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion)) {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_INCIDENTE).getSingleResult();
            } else if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR_NOTIFICACION.equals(accion)) {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_NOTIFICACION).getSingleResult();
            } else {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_DOCUMENTAL).getSingleResult();
            }

            actuacion.setTipoActuacion(tipoActuacion);

            PrimeFaces.current().ajax().update("DocumentosCreateActuacionForm");
            // PrimeFaces.current().ajax().update("DocumentosCreateActuacionForm2");

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('DocumentosCreateActuacionDialog').show();");

        }
        return actuacion;
    }

    // update=":ExpActuacionesViewForm" oncomplete="PF('ExpActuacionesViewDialog').show();"
    public void prepareVerActuaciones() {
        if (getSelected() != null) {
            Personas per = null;

            esParte = false;
            listaPersonas = new ArrayList<>();
            List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();
            for (int i = 0; i < listaPersonasActual.size(); i++) {
                per = listaPersonasActual.get(i).getPersona();
                per.setEstado(listaPersonasActual.get(i).getEstado().getCodigo());
                per.setTipoExpedienteAnterior(listaPersonasActual.get(i).isTipoExpedienteAnterior());
                listaPersonas.add(per);

                if (!esParte) {
                    esParte = personaUsuario.equals(per);
                }
            }

            List<ExpPartesPorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicial", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            listaPartes = new ArrayList<>();
            for (ExpPartesPorDocumentosJudiciales parte : lista) {
                if (!esParte) {
                    esParte = personaUsuario.equals(parte.getPersona());
                }
                listaPartes.add(new ExpPartesPorDocumentosJudiciales(new ExpPartesPorDocumentosJudicialesPK(parte.getPersona().getId(), getSelected().getId()), parte.getPersona(), getSelected(), parte.getTipoParte()));
            }

            List<PersonasPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            ExpTiposParte tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ACUSADO).getSingleResult();

            for (PersonasPorDocumentosJudiciales parte : lista2) {
                listaPartes.add(new ExpPartesPorDocumentosJudiciales(new ExpPartesPorDocumentosJudicialesPK(parte.getPersona().getId(), getSelected().getId()), parte.getPersona(), getSelected(), tipo));
            }

            buscarActuaciones();

            PrimeFaces.current().ajax().update("ExpActuacionesViewForm");

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('ExpActuacionesViewDialog').show();");
        }
    }

    public DocumentosJudiciales prepareCreateAcusacion() {
        return prepareCreate(Constantes.TIPO_EXPEDIENTE_ACUSACION);
    }

    public DocumentosJudiciales prepareCreateDenuncia() {
        return prepareCreate(Constantes.TIPO_EXPEDIENTE_DENUNCIA);
    }

    public DocumentosJudiciales prepareCreateInvestigacionPreliminar() {
        return prepareCreate(Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR);
    }

    public DocumentosJudiciales prepareCreatePedidoDesafuero() {
        return prepareCreate(Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO);
    }

    private DocumentosJudiciales prepareCreate(Integer parTipoExpediente) {
        DocumentosJudiciales doc = super.prepareCreate(null);

        doc.setFechaPresentacion(generarFechaPresentacion(ejbFacade.getSystemDate()));

        observacion = "";
        estadoProcesal = "";
        esPrimerEscrito = true;

        fojas = null;

        listaPersonasOrigen = new ArrayList<>();

        if (Constantes.TIPO_EXPEDIENTE_ACUSACION == parTipoExpediente) {
            abogadoOrigen = personaUsuario;
            personaOrigen = null;
        } else if (Constantes.TIPO_EXPEDIENTE_DENUNCIA == parTipoExpediente) {
            abogadoOrigen = null;
            listaPersonasOrigen.add(personaUsuario);
        } else if (Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR == parTipoExpediente) {
            abogadoOrigen = null;
            personaOrigen = null;
        } else if (Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO == parTipoExpediente) {
            abogadoOrigen = null;
            personaOrigen = null;
        } else {
            JsfUtil.addErrorMessage("No se puede procesar tipo expediente " + parTipoExpediente);
            return null;
        }

        entradaDocumentoJudicial = entradasDocumentosJudicialesController.prepareCreate(null);
        entradaDocumentoJudicial.setRecibidoPor(usuario);
        EstadosProceso est = null;
        est = ejbFacade.getEntityManager().createNamedQuery("EstadosProceso.findByCodigo", EstadosProceso.class).setParameter("codigo", Constantes.ESTADO_PROCESO_EN_TRAMITE).getSingleResult();

        doc.setMostrarWeb(Constantes.SI);
        doc.setMostrarExpedienteWeb(Constantes.NO);

        doc.setEstadoProceso(est);

        doc.setTipoExpediente(ejbFacade.getEntityManager().createNamedQuery("TiposExpediente.findById", TiposExpediente.class).setParameter("id", parTipoExpediente).getSingleResult());

        nuevaCausa = null;
        nombreJuez = null;
        nombreEstado = null;
        ultimaObservacion = null;
        file = null;
        listaPersonas = new ArrayList<>();
        persona = null;

        if (Constantes.TIPO_EXPEDIENTE_ACUSACION == parTipoExpediente) {
            PrimeFaces.current().ajax().update("DocumentosCreateAcusacionForm");
            PrimeFaces.current().executeScript("PF('DocumentosCreateAcusacionDialog').show();");
        } else if (Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR == parTipoExpediente) {
            PrimeFaces.current().ajax().update("DocumentosCreateInvestigacionPreliminarForm");
            PrimeFaces.current().ajax().update("DocumentosCreateInvestigacionPreliminarForm2");
            PrimeFaces.current().executeScript("PF('DocumentosCreateInvestigacionPreliminarDialog').show();");
        } else if (Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO == parTipoExpediente) {
            PrimeFaces.current().ajax().update("DocumentosCreatePedidoDesafueroForm");
            PrimeFaces.current().ajax().update("DocumentosCreatePedidoDesafueroForm2");
            PrimeFaces.current().executeScript("PF('DocumentosCreatePedidoDesafueroDialog').show();");
        }

        return doc;
    }

    public void verDoc(ExpActuaciones doc) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            if (doc.getArchivo() != null) {
                try {
                    fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + doc.getArchivo()).toPath());
                } catch (IOException ex) {
                    JsfUtil.addErrorMessage("No tiene documento adjunto");
                }
            }

            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");
        }

    }

    public void prepareDialogoVerDoc(ExpActuaciones doc) {
        docImprimir = doc;

        PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public void actualizarResuelve() {
        if (getSelected() != null) {
            if (resolucion != null) {
                if (resolucion.getTipoResolucion() != null) {
                    if (Constantes.TIPO_RESOLUCION_SD.equals(resolucion.getTipoResolucion().getId())) {
                        listaResuelve = ejbFacade.getEntityManager().createNamedQuery("Resuelve.findByTipoResolucionOCancelacion", Resuelve.class).setParameter("tipoResolucion", resolucion.getTipoResolucion()).getResultList();
                    } else {
                        listaResuelve = ejbFacade.getEntityManager().createNamedQuery("Resuelve.findByTipoResolucion", Resuelve.class).setParameter("tipoResolucion", resolucion.getTipoResolucion()).getResultList();
                    }
                    listaSubtiposResolucion = ejbFacade.getEntityManager().createNamedQuery("SubtiposResolucion.findByTipoResolucion", SubtiposResolucion.class).setParameter("tipoResolucion", resolucion.getTipoResolucion()).getResultList();
                } else {
                    listaResuelve = new ArrayList<>();
                    listaSubtiposResolucion = new ArrayList<>();
                }
            }
        } else {
            listaResuelve = new ArrayList<>();
            listaSubtiposResolucion = new ArrayList<>();
        }
    }

    public String obtenerPersonas(DocumentosJudiciales doc) {
        Personas per = null;

        String respuesta = "";

        if (doc != null) {
            try {
                List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                for (int i = 0; i < listaPersonasActual.size(); i++) {
                    per = listaPersonasActual.get(i).getPersona();
                    if ("".equals(respuesta)) {
                        respuesta = per.getNombresApellidos();
                    } else {
                        respuesta += ", " + per.getNombresApellidos();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return respuesta;
    }

    public String obtenerPersonaAcusadora(DocumentosJudiciales doc) {
        Personas per = obtenerPersona(doc, Constantes.TIPO_PARTE_ACUSADOR);
        doc.setAcusador(per);
        return (per == null) ? "" : per.getNombresApellidos();
    }

    public String obtenerAbogadoAcusador(DocumentosJudiciales doc) {
        Personas per = obtenerPersona(doc, Constantes.TIPO_PARTE_ABOGADO_ACUSADOR);
        doc.setAbogadoAcusador(per);
        return (per == null) ? "" : per.getNombresApellidos();
    }

    public Personas obtenerPersona(DocumentosJudiciales doc, Integer tipoParte) {
        Personas per = null;

        String respuesta = "";
        ExpPartesPorDocumentosJudiciales acusador = null;

        if (doc != null) {
            try {
                acusador = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstadoTipoParte", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).setParameter("tipoParte", tipoParte).getSingleResult();
            } catch (Exception ex) {

            }
        }

        return (acusador != null) ? acusador.getPersona() : null;
    }

    public void generarCaratula(List<Personas> acusadores, List<Personas> acusados, DocumentosJudiciales doc) {

        if (doc != null) {
            String caratula = "";

            if (Constantes.TIPO_EXPEDIENTE_ACUSACION == doc.getTipoExpediente().getId() || Constantes.TIPO_EXPEDIENTE_DENUNCIA == doc.getTipoExpediente().getId()) {
                if (acusadores != null) {
                    boolean primero = true;
                    for (Personas per : acusadores) {
                        if (!primero) {
                            caratula += ", ";
                        }
                        caratula += Utils.capitalize(per.getNombresApellidos());
                        primero = false;
                    }
                    caratula += " c/ ";
                }
            } else if (Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR == doc.getTipoExpediente().getId()) {
                caratula = "JEM ";
            } else if (Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO == doc.getTipoExpediente().getId()) {
                caratula = personaUsuario.getNombresApellidos() + ", ";
            }

            if (acusados != null) {
                boolean primero = true;
                for (Personas per : acusados) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                        if (!primero) {
                            caratula += ", ";
                        }

                        caratula += "Abg. " + per.getNombresApellidos() + ((per.getDespachoPersona() == null) ? "" : (", " + Utils.capitalize(per.getDespachoPersona().getDescripcion()))) + ((per.getDepartamentoPersona() == null) ? "" : (", Circuscripción Judicial " + Utils.capitalize(per.getDepartamentoPersona().getDescripcion())));
                        primero = false;
                    }
                }

                if (!"".equals(caratula)) {
                    caratula += " s/ " + doc.getTipoExpediente().getDescripcion().toLowerCase();
                }
            }
            doc.setCaratula(caratula);
        }
    }

    private String generarNroCausa() throws Exception {
        String nroCau;
        ParametrosSistema param = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_GESTION).getSingleResult();
        String[] array = param.getUltimoNroCausa().split("-");

        if (array.length != 2) {
            JsfUtil.addErrorMessage("No se pudo generar Nro Causa");
            throw new Exception("No se pudo generar Nro Causa");
        }

        DateFormat format = new SimpleDateFormat("yy");

        Date fecha = ejbFacade.getSystemDate();

        String anoActual = format.format(fecha);

        if (anoActual.equals(array[1])) {
            Integer nro = Integer.valueOf(array[0]);
            nro++;
            nroCau = nro + "-" + array[1];

            param.setUltimoNroCausa(nroCau);
        } else {
            nroCau = "1-" + anoActual;
            param.setUltimoNroCausa(nroCau);
        }

        parametrosSistemaController.setSelected(param);
        parametrosSistemaController.save(null);

        return nroCau;
    }

    public void borrarPersona(Personas personaActual) {

        if (listaPersonas != null) {

            listaPersonas.remove(personaActual);

            personaActual.setEstado("IN");

            listaPersonas.add(personaActual);

        }

        generarCaratula(listaPersonasOrigen, listaPersonas, getSelected());
    }
    
    public void agregarNuevaPersona() {
        if (persona == null) {
            Personas per = personasController.prepareCreate(null);

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            per.setId(-1 * Integer.valueOf(format.format(fecha)));
            per.setCi(cedulaPersona);
            per.setNombresApellidos(nombresApellidosPersona);
            per.setEstado(Constantes.ESTADO_USUARIO_AC);
            per.setFechaHoraAlta(ejbFacade.getSystemDate());
            per.setUsuarioAlta(new Usuarios(2));
            per.setFechaHoraUltimoEstado(ejbFacade.getSystemDate());
            per.setUsuarioUltimoEstado(new Usuarios(2));
            per.setEmpresa(new Empresas(1));

            //personaOrigen = personasController.getSelected();
            persona = per;
        }

        cedulaPersona = "";
        nombresApellidosPersona = "";

        agregarPersona();

    }

    public void agregarPersona() {

        if (persona != null) {

            if (listaPersonas == null) {
                listaPersonas = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaPersonas) {
                if (per.equals(persona)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                persona.setTipoExpedienteAnterior(false);
                listaPersonas.add(persona);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }

        generarCaratula(listaPersonasOrigen, listaPersonas, getSelected());
    }

    public void agregarPersonaFirmante() {

        if (personaFirmante != null) {

            if (listaPersonasFirmantes == null) {
                listaPersonasFirmantes = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaPersonasFirmantes) {
                if (per.equals(personaFirmante)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaPersonasFirmantes.add(personaFirmante);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }
    }

    public void borrarPersonaFirmante(Personas personaActual) {

        if (listaPersonasFirmantes != null) {

            listaPersonasFirmantes.remove(personaActual);

            personaActual.setEstado("IN");

            listaPersonasFirmantes.add(personaActual);

        }
    }

    public void agregarRolFirmante() {

        if (rolFirmante != null) {

            if (listaRolesFirmantes == null) {
                listaRolesFirmantes = new ArrayList<>();
            }

            boolean encontro = false;
            AntecedentesRoles perActual = null;
            for (AntecedentesRoles per : listaRolesFirmantes) {
                if (per.equals(rolFirmante)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                rolFirmante.setEstado("AC");
                listaRolesFirmantes.add(rolFirmante);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }
    }

    public void borrarRolFirmante(AntecedentesRoles rolActual) {

        if (listaRolesFirmantes != null) {

            listaRolesFirmantes.remove(rolActual);

            rolActual.setEstado("IN");

            listaRolesFirmantes.add(rolActual);

        }
    }

    public void agregarRevision() {

        if (revision != null) {

            if (listaRevisiones == null) {
                listaRevisiones = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaRevisiones) {
                if (per.equals(revision)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                revision.setTipoExpedienteAnterior(false);
                listaRevisiones.add(revision);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }
    }

    public void borrarRevision(Personas personaActual) {

        if (listaRevisiones != null) {

            listaRevisiones.remove(personaActual);

            personaActual.setEstado("IN");

            listaRevisiones.add(personaActual);

        }
    }

    public void agregarParte() {

        if (parte != null && tipoParte != null) {

            if (listaPartesAdmin == null) {
                listaPartesAdmin = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaPartesAdmin) {
                if (per.equals(parte)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                parte.setTipoParte(tipoParte);
                listaPartesAdmin.add(parte);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }
    }

    public void borrarParte(Personas personaActual) {

        if (listaPartesAdmin != null) {

            listaPartesAdmin.remove(personaActual);

            personaActual.setEstado("IN");

            listaPartesAdmin.add(personaActual);

        }
    }

    public void borrarPersonaOrigen(Personas personaActual) {

        if (listaPersonasOrigen != null) {

            listaPersonasOrigen.remove(personaActual);

            personaActual.setEstado("IN");

            listaPersonasOrigen.add(personaActual);

        }

        generarCaratula(listaPersonasOrigen, listaPersonas, getSelected());
    }

    public void borrarAbogado(Personas personaActual) {

        if (listaAbogados != null) {

            listaAbogados.remove(personaActual);

            personaActual.setEstado("IN");

            listaAbogados.add(personaActual);

        }

    }

    public void buscarPersona() {
        if (cedula != null) {
            if (!"".equals(cedula)) {
                List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCi", Personas.class).setParameter("ci", cedula).getResultList();
                if (lista.size() > 0) {
                    personaOrigen = lista.get(0);
                    nombresApellidos = personaOrigen.getNombresApellidos();
                }
            }
        }
    }

    public void buscarAbogado() {
        if (cedulaAbogado != null) {
            if (!"".equals(cedulaAbogado)) {
                List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCi", Personas.class).setParameter("ci", cedulaAbogado).getResultList();
                if (lista.size() > 0) {
                    abogado = lista.get(0);
                    nombresApellidosAbogado = abogado.getNombresApellidos();
                }
            }
        }
    }

    public void buscarPersonaM() {
        if (cedulaPersona != null) {
            if (!"".equals(cedulaPersona)) {
                List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCi", Personas.class).setParameter("ci", cedulaPersona).getResultList();
                if (lista.size() > 0) {
                    persona = lista.get(0);
                    nombresApellidosPersona = persona.getNombresApellidos();
                }
            }
        }
    }

    public void agregarNuevaPersonaOrigen() {
        if (personaOrigen == null) {
            Personas per = personasController.prepareCreate(null);

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            per.setId(-1 * Integer.valueOf(format.format(fecha)));
            per.setCi(cedula);
            per.setNombresApellidos(nombresApellidos);
            per.setEstado(Constantes.ESTADO_USUARIO_AC);
            per.setFechaHoraAlta(ejbFacade.getSystemDate());
            per.setUsuarioAlta(new Usuarios(2));
            per.setFechaHoraUltimoEstado(ejbFacade.getSystemDate());
            per.setUsuarioUltimoEstado(new Usuarios(2));
            per.setEmpresa(new Empresas(1));

            //personaOrigen = personasController.getSelected();
            personaOrigen = per;
        }

        cedula = "";
        nombresApellidos = "";

        agregarPersonaOrigen();

    }
    
    public void agregarNuevoAbogado() {
        if (abogado == null) {
            Personas per = personasController.prepareCreate(null);

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            per.setId(-1 * Integer.valueOf(format.format(fecha)));
            per.setCi(cedulaAbogado);
            per.setNombresApellidos(nombresApellidosAbogado);
            per.setEstado(Constantes.ESTADO_USUARIO_AC);
            per.setFechaHoraAlta(ejbFacade.getSystemDate());
            per.setUsuarioAlta(new Usuarios(2));
            per.setFechaHoraUltimoEstado(ejbFacade.getSystemDate());
            per.setUsuarioUltimoEstado(new Usuarios(2));
            per.setEmpresa(new Empresas(1));

            //personaOrigen = personasController.getSelected();
            abogado = per;
        }

        cedulaAbogado = "";
        nombresApellidosAbogado = "";

        agregarAbogado();

    }

    public void agregarPersonaOrigen() {

        if (personaOrigen != null) {

            if (listaPersonasOrigen == null) {
                listaPersonasOrigen = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaPersonasOrigen) {
                if (per.equals(personaOrigen)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                personaOrigen.setTipoExpedienteAnterior(false);
                listaPersonasOrigen.add(personaOrigen);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }

        generarCaratula(listaPersonasOrigen, listaPersonas, getSelected());
    }
    
    public void agregarAbogado() {

        if (abogado != null) {

            if (listaAbogados == null) {
                listaAbogados = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaAbogados) {
                if (per.equals(abogado)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                abogado.setTipoExpedienteAnterior(false);
                listaAbogados.add(abogado);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }
    }

    public void prepareObs() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setUltimaObservacionAux(null);
            getSelected().setUltimaObservacion(null);
        }
    }

    public void prepareEstadoProcesal() {
        ultimaObservacion = null;
        if (getSelected() != null) {
            getSelected().setEstadoProcesalAux(null);
            getSelected().setEstadoProcesal(null);
        }
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

    public String navigateObservacionesDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosJudiciales/index");

        }
        return "/pages/observacionesDocumentosJudiciales/index";
    }

    public String navigateObservacionesDocumentosJudicialesAntecedentesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosJudiciales/index");

        }
        return "/pages/observacionesDocumentosJudicialesAntecedentes/index";
    }

    public String navigateEstadosProcesalesDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("EstadosProcesalesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByDocumentoJudicial", EstadosProcesalesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/entradaDirectaDocumentosJudiciales/index");
        }
        return "/pages/estadosProcesalesDocumentosJudiciales/index";
    }
    
    private boolean personaHabilitada(Personas per, DocumentosJudiciales doc){
        boolean resp = false;
        
        if(doc != null){
            List<ExpPersonasHabilitadasPorDocumentosJudiciales> listaHabilitadasActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasHabilitadasPorDocumentosJudiciales.findByDocumentoJudicial", ExpPersonasHabilitadasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();

            for (ExpPersonasHabilitadasPorDocumentosJudiciales pers : listaHabilitadasActual) {
                if("AC".equals(pers.getEstado().getCodigo()) && pers.getPersona().equals(per)){
                    resp = true;
                    break;
                }
            }
        }
        
        return resp;
    }


    private void buscarActuaciones() {
        if (getSelected() != null) {
            
            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMER_ESCRITO, rolElegido.getId()) || (getSelected().isRestringido() && !personaHabilitada(personaUsuario, getSelected()))) {
                listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicialVisibleTipoActuacion", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).setParameter("visible", true).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).getResultList();
            }else if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicial", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).getResultList();
            } else {
                listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicialVisible", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).setParameter("visible", true).getResultList();
            }
        } else {
            listaActuaciones = null;
        }
    }

    /*

    public void prepareDialogoVerDoc(ExpActuaciones doc) {
        docImprimir = doc;

        desabilitarPdfViewer = false;
        if (docImprimir != null) {
            String[] parts = docImprimir.getArchivo().split("[.]");

            if ("pdf".equals(parts[parts.length - 1]) || "PDF".equals(parts[parts.length - 1])) {
                desabilitarPdfViewer = false;
            } else if ("png".equals(parts[parts.length - 1]) || "PNG".equals(parts[parts.length - 1])
                    || "jpg".equals(parts[parts.length - 1]) || "JPG".equals(parts[parts.length - 1])
                    || "jpeg".equals(parts[parts.length - 1]) || "JPEG".equals(parts[parts.length - 1])) {
                desabilitarPdfViewer = true;
            }

        }
    }
     */
    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    /*
    @Override
    public Collection<DocumentosJudiciales> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAlta", DocumentosJudiciales.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", fechaAltaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).getResultList();
    }
     */
    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public String datePattern2() {
        return "yyyy";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }

    public String datePattern3() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String datePattern4() {
        return "dd/MM/yyyy HH:mm";
    }

    public String customFormatDate4(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern4());
            return format.format(date);
        }
        return "";
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    public boolean mostrarBanderin(Date fecha) {
        if (fecha != null) {
            Date now = ejbFacade.getSystemDateOnly(0);

            long diff = now.getTime() - fecha.getTime();

            long dias = TimeUnit.MILLISECONDS.toDays(diff);

            if (dias < 8) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Collection<DocumentosJudiciales> getItems() {
        return super.getItems2();
    }

    /*
    @Override
    public Collection<DocumentosJudiciales> getItems() {
        setItems(ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findAll", DocumentosJudiciales.class).getResultList());
        return getItems2();
    }
     */
    public boolean renderedBotonBorrarDoc() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_PRESENTACION, rolElegido.getId())) {
            return true;
        }
        return false;
    }

    public void borrarDoc(ExpActuaciones act) {

        // ExpHistActuaciones hact = 
        // expHistActuacionesController.setSelected(selected);
        List<ObservacionesDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByActuacion", ObservacionesDocumentosJudiciales.class).setParameter("actuacion", act).getResultList();

        for (ObservacionesDocumentosJudiciales obs : lista) {
            obsController.setSelected(obs);
            obsController.delete(null);
        }

        List<EstadosProcesalesDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByActuacion", EstadosProcesalesDocumentosJudiciales.class).setParameter("actuacion", act).getResultList();

        for (EstadosProcesalesDocumentosJudiciales est : lista2) {
            estadosProcesalesDocumentosJudicialesController.setSelected(est);
            estadosProcesalesDocumentosJudicialesController.delete(null);
        }

        expActuacionesController.setSelected(act);
        expActuacionesController.delete(null);
        buscarActuaciones();
    }

    public boolean renderedVisible() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_VISIBLE_EXPEDIENTE, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_VER_VISIBLE_EXPEDIENTE, rolElegido.getId())) {
            return true;
        }
        return false;
    }

    public boolean deshabilitarFechaHoraAlta() {
        return esParte;
    }

    public boolean deshabilitarUsuario() {
        return esParte;
    }

    public boolean deshabilitarNombresApellidos() {
        return personaOrigen != null;
    }

    public boolean deshabilitarNombresApellidosAbogado() {
        return abogado != null;
    }

    public boolean deshabilitarNombresApellidosPersona() {
        return persona != null;
    }

    public boolean deshabilitarBotonBorrarDoc(ExpActuaciones act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_PRESENTACION, rolElegido.getId())) {
            return act.isVisible();
        }
        return false;
    }

    public boolean deshabilitarVisible() {
        return deshabilitarVisible(null);
    }

    public boolean deshabilitarVisible(ExpActuaciones act) {

        if (act != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_VISIBLE_EXPEDIENTE, rolElegido.getId()) && !act.isVisible()) {
                return false;
            }
        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_VISIBLE_EXPEDIENTE, rolElegido.getId())) {
                return false;
            }
        }
        return true;
    }

    public boolean deshabilitarBotonVerEnVentana() {
        return deshabilitarBotonVerEnVentana(docImprimir);
    }

    public boolean deshabilitarBotonVerEnVentana(ExpActuaciones act) {
        // return false;
        return !(renderedFirma(act) || !deshabilitarAutenticar() || esApp);
    }

    public boolean deshabilitarFirmantes(ExpActuaciones act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_FIRMANTES, rolElegido.getId())) {
            return false;
        }
        return true;
    }

    public boolean deshabilitarRevisiones(ExpActuaciones act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_REVISIONES, rolElegido.getId())) {
            return false;
        }
        return true;
    }

    public boolean deshabilitarPartes() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_PARTES, rolElegido.getId())) {
                return false;
            }
        }
        return true;
    }

    public boolean deshabilitarListaPartesAlta() {
        return tipoParte == null;
    }

    public boolean deshabilitarDestinatario() {
        if (categoriaActuacion != null) {
            if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
                return false;
            }
        }
        return true;
    }

    public boolean deshabilitarObjetoActuacion() {
        return !esPrimerEscrito || !(Constantes.ACCION_REGISTRAR.equals(accion) || Constantes.ACCION_REGISTRAR_RECURSO.equals(accion) || Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion));
    }

    public boolean deshabilitarTipoActuacion() {
        // return esPrimerEscrito && Constantes.ACCION_REGISTRAR.equals(accion);
        // return Constantes.ACCION_REGISTRAR.equals(accion) || Constantes.ACCION_CREAR.equals(accion);
        return true;
    }

    public boolean deshabilitarAcusacion() {
        /*
        if (filtroURL.verifPermiso(Constantes.GENERAR_ACUSACION, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            return false;
        }
         */
        return true;
    }

    public boolean deshabilitarEstadoProcesal() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_ESTADO_PROCESAL, rolElegido.getId())) {
            return false;
        }
        return true;
    }

    public boolean deshabilitarObservacion() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_OBSERVACION, rolElegido.getId())) {
            return false;
        }
        return true;
    }

    public boolean renderedTipoActuacion() {
        // return esPrimerEscrito && Constantes.ACCION_REGISTRAR.equals(accion);
        // return !Constantes.ACCION_REGISTRAR_PROVIDENCIA.equals(accion);
        return false;
    }

    private boolean verificarFirmantes(ExpActuaciones act, Personas firmante, AntecedentesRoles rol) {

        ExpPersonasFirmantesPorActuaciones firm = null;
        ExpRolesFirmantesPorActuaciones firm2 = null;
        try {
            firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", firmante).setParameter("estado", new Estados("AC")).getSingleResult();
            return firm.isFirmado();
        } catch (Exception e) {
            try {
                firm2 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacionRolFirmanteEstado", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("rolFirmante", rol).setParameter("estado", new Estados("AC")).getSingleResult();
                firm2.isFirmado();
            } catch (Exception ex) {

            }
        }

        return false;
    }

    public boolean renderedFirma() {
        return renderedFirma(docImprimir);
    }

    public boolean renderedFirma(ExpActuaciones act) {

        if (act != null) {
            // ExpPersonasFirmantesPorActuaciones firm = verificarFirmantes(docImprimir, personaUsuario, rolElegido);
            // return firm != null;

            ExpPersonasFirmantesPorActuaciones firm = null;
            ExpRolesFirmantesPorActuaciones firm2 = null;
            try {
                firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getSingleResult();
                return true;
            } catch (Exception e) {
                try {
                    firm2 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacionRolFirmanteEstado", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("rolFirmante", rolElegido).setParameter("estado", new Estados("AC")).getSingleResult();
                    return true;
                } catch (Exception ex) {

                }
            }

            return false;
        }

        return false;
    }

    public boolean deshabilitarFirma() {

        if (docImprimir != null) {
            // ExpPersonasFirmantesPorActuaciones firm = verificarFirmantes(docImprimir, personaUsuario, rolElegido);
            ExpPersonasFirmantesPorActuaciones firm = null;
            ExpRolesFirmantesPorActuaciones firm2 = null;
            try {
                firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getSingleResult();
                return firm.isFirmado();
            } catch (Exception e) {
                try {
                    firm2 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacionRolFirmanteEstado", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("rolFirmante", rolElegido).setParameter("estado", new Estados("AC")).getSingleResult();
                    return firm2.isFirmado();
                } catch (Exception ex) {

                }
            }

            return true;
        }

        return true;
    }

    private ExpRevisionesPorActuaciones verificarRevisiones(ExpActuaciones act, Personas revisionPor) {

        try {
            return ejbFacade.getEntityManager().createNamedQuery("ExpRevisionesPorActuaciones.findByActuacionRevisionPorEstado", ExpRevisionesPorActuaciones.class).setParameter("actuacion", act).setParameter("revisionPor", revisionPor).setParameter("estado", new Estados("AC")).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean renderedRevision() {
        if (docImprimir != null) {
            ExpRevisionesPorActuaciones firm = verificarRevisiones(docImprimir, personaUsuario);
            return firm != null;
        }

        return true;

    }

    public boolean deshabilitarRevision() {

        if (docImprimir != null) {
            ExpRevisionesPorActuaciones firm = verificarRevisiones(docImprimir, personaUsuario);
            if (firm != null) {
                return firm.isRevisado();
            }
        }

        return true;
    }

    public boolean deshabilitarAutenticar() {
        if (docImprimir != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_AUTENTICAR, rolElegido.getId())) {
                return false;
            }
        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_AUTENTICAR, rolElegido.getId())) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarDenuncia() {
        if (filtroURL.verifPermiso(Constantes.GENERAR_DENUNCIA, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            return false;
        }

        return true;
    }

    public boolean deshabilitarInvestigacionPreliminar() {
        /*
        if (filtroURL.verifPermiso(Constantes.GENERAR_INVESTIGACION_PRELIMINAR, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            return false;
        }
         */
        return true;
    }

    public boolean deshabilitarPedidoDesafuero() {
        /*
        if (filtroURL.verifPermiso(Constantes.GENERAR_PEDIDO_DESAFUERO, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            return false;
        }
         */
        return true;
    }

    public boolean deshabilitarCreateActuaciones() {
        if (Constantes.ACCION_REGISTRAR.equals(accion)
                || Constantes.ACCION_REGISTRAR_RECURSO.equals(accion)
                || Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion)
                || Constantes.ACCION_REGISTRAR_NOTIFICACION.equals(accion)
                || Constantes.ACCION_REGISTRAR_OFICIO.equals(accion)
                || Constantes.ACCION_REGISTRAR_PROVIDENCIA.equals(accion)) {
            return false;
        }

        return true;
    }

    public boolean deshabilitarCreateResoluciones() {
        if (Constantes.ACCION_REGISTRAR_RESOLUCION.equals(accion)) {
            return false;
        }

        return true;
    }

    public boolean deshabilitarFirmado() {
        if (filtroURL.verifPermiso(Constantes.PERMITIR_FIRMADO, rolElegido.getId())) {
            return false;
        }

        return true;
    }

    public boolean deshabilitarBorrar() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE, rolElegido.getId()) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO, rolElegido.getId())) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarVerEstadoProcesal() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.ESTADOS_PROCESALES_DOCUMENTOS_JUDICIALES, rolElegido.getId())) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarVerObs() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_OBSERVACIONES_DOCUMENTOS_JUDICIALES, rolElegido.getId())) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarVerObsAntecedente() {
        /*
        if(getSelected() != null){
            if(filtroURL.verifPermiso(Constantes.PERMITIR_OBSERVACIONES_DOCUMENTOS_JUDICIALES_ANTECEDENTES)){
                    return false;
            }
        }
         */
        return true;
    }

    public boolean deshabilitarEstadoProceso() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA, rolElegido.getId())) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarEstadoProcesoNuevo() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA, rolElegido.getId())) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarMostrarWeb() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_MOSTRAR_WEB, rolElegido.getId())) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarEdit() {
        if (getSelected() != null) {

            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMERA_PROVIDENCIA, rolElegido.getId())) {
                if (getSelected().getEstadoProceso() != null) {
                    if (Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA.equals(getSelected().getEstadoProceso().getCodigo())) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarSubtipoResolucion() {
        if (resolucion != null) {
            if (resolucion.getTipoResolucion() != null) {
                return false;
            }
        }
        return true;
    }

    public void actualizarActuacion(ExpActuaciones act) {
        Date fecha = ejbFacade.getSystemDate();
        Date fechaHora = generarFechaPresentacion(fecha);

        act.setFechaPresentacion(fechaHora);
        act.setFechaHoraVisible(fecha);
        act.setPersonaVisible(personaUsuario);

        expActuacionesController.setSelected(act);

        expActuacionesController.save(null);

        EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();

        estadoProc.setPersonaAlta(personaUsuario);
        estadoProc.setPersonaUltimoEstado(personaUsuario);
        estadoProc.setFechaHoraAlta(fecha);
        estadoProc.setFechaHoraUltimoEstado(fecha);
        estadoProc.setEstadoProcesal(act.getDescripcion());
        estadoProc.setDocumentoJudicial(getSelected());
        estadoProc.setEmpresa(personaUsuario.getEmpresa());
        estadoProc.setUsuarioAlta(usuario);
        estadoProc.setUsuarioUltimoEstado(usuario);
        // estadoProc.setVisible(expActuacionesController.getSelected().isVisible());
        estadoProc.setVisible(true);
        estadoProc.setActuacion(act);
        //if(expActuacionesController.getSelected().isVisible()){
        estadoProc.setPersonaVisible(personaUsuario);
        estadoProc.setFechaHoraVisible(fecha);
        //}

        estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
        estadosProcesalesDocumentosJudicialesController.saveNew(null);

        getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
        getSelected().setFechaHoraEstadoProcesal(fecha);
        getSelected().setUsuarioEstadoProcesal(usuario);
        getSelected().setPersonaUltimoEstadoProcesal(personaUsuario);
        getSelected().setEstadoProcesal(act.getDescripcion());

        super.save(null);

        /*
        
        List<ObservacionesDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByActuacion", ObservacionesDocumentosJudiciales.class).setParameter("actuacion", act).getResultList();
        
        for(ObservacionesDocumentosJudiciales obs : lista){
            obs.setVisible(true);
            obs.setPersonaVisible(personaUsuario);
            obs.setFechaHoraVisible(fecha);
            obsController.setSelected(obs);
            obsController.save(null);
            
            getSelected().setObservacionDocumentoJudicial(obs);
            getSelected().setFechaUltimaObservacion(fecha);
            getSelected().setPersonaUltimaObservacion(personaUsuario);
            super.save(null);

        }
        
        List<EstadosProcesalesDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByActuacion", EstadosProcesalesDocumentosJudiciales.class).setParameter("actuacion", act).getResultList();
        
        for(EstadosProcesalesDocumentosJudiciales est : lista2){
            est.setVisible(true);
            est.setPersonaVisible(personaUsuario);
            est.setFechaHoraVisible(fecha);
            estadosProcesalesDocumentosJudicialesController.setSelected(est);
            estadosProcesalesDocumentosJudicialesController.save(null);
            
            getSelected().setEstadoProcesalDocumentoJudicial(est);
            getSelected().setFechaHoraEstadoProcesal(fecha);
            getSelected().setPersonaUltimoEstadoProcesal(personaUsuario);
            super.save(null);
        }
         */
        buscarActuaciones();
        
        if (act.isVisible()) {
            String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                    + "     Se a presentado una presentación del tipo " + act.getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                    + getSelected().getCaratulaString()
                    + "<br><br>"
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);

            if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getCategoriaActuacion().getId())
                    || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(act.getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId())) {

                try {
                    ExpNotificaciones not = ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByActuacion", ExpNotificaciones.class).setParameter("actuacion", act).getSingleResult();

                    String texto2 = "<p>Hola " + not.getDestinatario().getNombresApellidos() + "<br> "
                            + "     Se ha presentado una presentación del tipo " + act.getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(not.getDestinatario().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto2);
                } catch (Exception e) {

                }

            }

        }
    }

    public void autenticar() {
        autenticar(docImprimir, true);
    }

    private void autenticar(ExpActuaciones act, boolean primeraPagina) {
        //if (firmar()) {

        try {

            File file = new File(par.getRutaArchivos() + File.separator + act.getArchivo());

            // File (or directory) with new name
            File file2 = new File(par.getRutaArchivos() + File.separator + act.getArchivo() + "_temp");

            if (file2.exists()) {
                JsfUtil.addErrorMessage("No se pudo autenticar correctamente");
                return;
            }

            boolean success = file.renameTo(file2);

            if (!success) {
                JsfUtil.addErrorMessage("No se pudo autenticar correctamente.");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();
            String hash = "";
            try {
                hash = Utils.generarHash(fecha, personaUsuario.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                return;
            }

            agregarQR(par.getRutaArchivos() + File.separator + act.getArchivo(), hash, primeraPagina);
            File archivo = new File(par.getRutaArchivos() + File.separator + act.getArchivo() + "_temp");
            archivo.delete();

            act.setHash(hash);

            expActuacionesController.setSelected(act);
            expActuacionesController.save(null);

        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo autenticar correctamente..");
        }

        //}
    }

    private void actualizarEstadoFirma(Firmas firma, AntecedentesRoles rol) {
        try {
            ExpPersonasFirmantesPorActuaciones act = null;
            ExpRolesFirmantesPorActuaciones act2 = null;
            boolean esPersona = false;
            try {
                act = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", firma.getActuacion()).setParameter("personaFirmante", firma.getPersona()).setParameter("estado", new Estados("AC")).getSingleResult();
                esPersona = true;
            } catch (Exception e) {
                try {
                    act2 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacionRolFirmanteEstado", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", firma.getActuacion()).setParameter("rolFirmante", rol).setParameter("estado", new Estados("AC")).getSingleResult();
                } catch (Exception ex) {

                }
            }

            if (act != null) {
                act.setFechaHoraFirma(ejbFacade.getSystemDate());
                act.setFirmado(true);

                personasFirmantesPorActuacionesController.setSelected(act);
                personasFirmantesPorActuacionesController.save(null);
            }

            if (act2 != null) {
                act2.setFechaHoraFirma(ejbFacade.getSystemDate());
                act2.setFirmado(true);

                if (act2.getPersonaFirmante() == null) {
                    act2.setPersonaFirmante(firma.getPersona());
                }

                rolesFirmantesPorActuacionesController.setSelected(act2);
                rolesFirmantesPorActuacionesController.save(null);
            }

            String texto = "<p>Hola " + ((act == null) ? act2.getPersonaAlta().getNombresApellidos() : act.getPersonaAlta().getNombresApellidos()) + "<br> "
                    + "     Una presentacion ha sido firmada por " + ((act == null) ? act2.getPersonaFirmante().getNombresApellidos() : act.getPersonaFirmante().getNombresApellidos()) + " en la causa nro " + docImprimir.getDocumentoJudicial().getCausa() + " caratulada: <br><br>"
                    + docImprimir.getDocumentoJudicial().getCaratulaString()
                    + "<br><br>"
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + docImprimir.getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(((act == null) ? act2.getPersonaAlta().getEmail() : act.getPersonaAlta().getEmail()), "Se ha firmada una presentacion en el Sistema de Expediente Electrónico JEM", texto);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean firmar() {

        Date fecha = ejbFacade.getSystemDate();
        Firmas firma = null;

        boolean resp = false;
        /*
        try{
            firma = ejbFacade.getEntityManager().createNamedQuery("Firmas.findBySesion", Firmas.class).setParameter("sesion", sessionId).setParameter("estado", "IN").setParameter("fechaHora", fecha).getSingleResult();
        }catch(Exception ex){
            
        }
         */

        if (firma == null) {
            firma = firmasController.prepareCreate(null);

            firma.setActuacion(docImprimir);
            firma.setEmpresa(usuario.getEmpresa());
            firma.setFechaHora(fecha);
            firma.setEstado("PE");
            firma.setDocumentoJudicial(docImprimir.getDocumentoJudicial());
            firma.setSesion(sessionId);
            firma.setPersona(personaUsuario);

            firmasController.setSelected(firma);
            firmasController.saveNew(null);

        }

        int cont = 60;
        Firmas firma2 = null;

        while (cont >= 0) {

            try {
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                firma2 = ejbFacade.getEntityManager().createNamedQuery("Firmas.findById", Firmas.class).setParameter("id", firma.getId()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                cont = 0;
                break;
            }

            System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);

            if (!firma2.getEstado().equals("PE")) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            cont--;
        }

        if (cont > 0) {

            if (firma2 != null) {
                if (firma2.getEstado().equals("AC")) {
                    docImprimir = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", docImprimir.getId()).getSingleResult();

                    // autenticar(docImprimir);
                    actualizarEstadoFirma(firma2, rolElegido);

                    resp = true;
                } else {
                    JsfUtil.addErrorMessage("Error en proceso");
                }
            } else {
                JsfUtil.addErrorMessage("Error en proceso.");
            }
        } else {
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasController.setSelected(firma2);
                firmasController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }

        return resp;

    }

    public void revision() {
        try {
            ExpRevisionesPorActuaciones act = ejbFacade.getEntityManager().createNamedQuery("ExpRevisionesPorActuaciones.findByActuacionRevisionPorEstado", ExpRevisionesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("revisionPor", personaUsuario).setParameter("estado", new Estados("AC")).getSingleResult();
            act.setFechaHoraRevision(ejbFacade.getSystemDate());
            act.setRevisado(true);

            revisionesPorActuacionesController.setSelected(act);
            revisionesPorActuacionesController.save(null);

            String texto = "<p>Hola " + act.getPersonaAlta().getNombresApellidos() + "<br> "
                    + "     Una presentacion ha sido revisada por " + act.getRevisionPor().getNombresApellidos() + " en la causa nro " + docImprimir.getDocumentoJudicial().getCausa() + " caratulada: <br><br>"
                    + docImprimir.getDocumentoJudicial().getCaratulaString()
                    + "<br><br>"
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + docImprimir.getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Se ha revisado una presentacion en el Sistema de Expediente Electrónico JEM", texto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buscar() {
        if (nroCausa == null) {
            nroCausa = "";
        }
        if (ano == null) {
            ano = "";
        }
        if (caratulaBusqueda == null) {
            caratulaBusqueda = "";
        }

        if ("".equals(caratulaBusqueda) && "".equals(nroCausa) && "".equals(ano) && fechaDesde == null && fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar algun criterio de busqueda");
            return;
        }

        if (!"".equals(nroCausa)) {
            buscarPorNroCausa();
        } else if (!"".equals(caratulaBusqueda)) {
            buscarPorCaratula();
        } else if (!"".equals(ano)) {
            buscarPorAno();
        } else {
            buscarPorFechaAlta();
        }
    }

    private void buscarPorNroCausa() {
        if (nroCausa == null) {
            JsfUtil.addErrorMessage("Debe ingresar Nro de Causa");
            return;
        } else if ("".equals(nroCausa)) {
            JsfUtil.addErrorMessage("Debe ingresar Nro de Causa");
            return;
        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("causa", "%" + nroCausa + "%").setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausaPersona", DocumentosJudiciales.class).setParameter("causa", "%" + nroCausa + "%").setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            }
        }
    }

    private void buscarPorCaratula() {
        if (caratulaBusqueda == null) {
            JsfUtil.addErrorMessage("Debe ingresar Caratula");
            return;
        } else if ("".equals(caratulaBusqueda)) {
            JsfUtil.addErrorMessage("Debe ingresar Caratula");
            return;
        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCaratula", DocumentosJudiciales.class).setParameter("caratula", "%" + caratulaBusqueda + "%").setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCaratulaPersona", DocumentosJudiciales.class).setParameter("caratula", "%" + caratulaBusqueda + "%").setParameter("persona1", personaUsuario).setParameter("persona2", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            }
        }
    }

    /*
    public void buscarPorFechaPresentacion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            busquedaPorFechaAlta = false;
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrdered", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
        }
    }
     */
    public void buscarPorFechaAlta() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else if ("".equals(fechaDesde) || "".equals(fechaHasta)) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
            if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByFechaAlta", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByPersonaFechaAlta", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            }
        }
    }

    public void buscarPorAno() {
        if (ano == null) {
            JsfUtil.addErrorMessage("Debe ingresar Año");
        } else if ("".equals(ano)) {
            JsfUtil.addErrorMessage("Debe ingresar Año");
        } else {
            Calendar myCal = Calendar.getInstance();
            myCal.set(Calendar.YEAR, Integer.valueOf(ano));
            myCal.set(Calendar.MONTH, 0);
            myCal.set(Calendar.DAY_OF_MONTH, 1);
            myCal.set(Calendar.HOUR, 0);
            myCal.set(Calendar.MINUTE, 0);
            myCal.set(Calendar.SECOND, 0);
            Date fechaDesdeAno = myCal.getTime();

            Calendar myCal2 = Calendar.getInstance();
            myCal2.set(Calendar.YEAR, Integer.valueOf(ano) + 1);
            myCal2.set(Calendar.MONTH, 0);
            myCal2.set(Calendar.DAY_OF_MONTH, 1);
            myCal.set(Calendar.HOUR, 0);
            myCal.set(Calendar.MINUTE, 0);
            myCal.set(Calendar.SECOND, 0);
            Date fechaHastaAno = myCal2.getTime();
            if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByFechaPresentacion", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesdeAno).setParameter("fechaHasta", fechaHastaAno).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByPersonaFechaPresentacion", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesdeAno).setParameter("fechaHasta", fechaHastaAno).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            }
        }
    }

    public void editarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesDocumentosJudiciales obs = getSelected().getObservacionDocumentoJudicial();

                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.save(null);

                getSelected().setObservacionDocumentoJudicial(obs);

                super.save(null);
            }

        }
    }

    public void agregarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);
                getSelected().setPersonaUltimaObservacion(personaUsuario);

                ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAux());
                obs.setVisible(true);
                obs.setPersonaAlta(personaUsuario);
                obs.setPersonaUltimoEstado(personaUsuario);
                obs.setPersonaVisible(personaUsuario);
                obs.setActuacion(null);

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionDocumentoJudicial(obs);

                super.save(null);

                obs.setDocumentoJudicial(getSelected());

                obsController.save(null);
            }

        }
    }

    public void editarObsAntecedente() {

        if (getSelected() != null) {
            if (getSelected().verifObsAntecedente()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObsAntecedente();

                getSelected().setUsuarioUltimaObservacionAntecedente(usuario);

                ObservacionesDocumentosJudicialesAntecedentes obs = getSelected().getObservacionDocumentoJudicialAntecedente();

                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setObservacion(getSelected().getUltimaObservacionAntecedenteAux());

                obsAntecedenteController.setSelected(obs);
                obsAntecedenteController.save(null);

                getSelected().setObservacionDocumentoJudicialAntecedente(obs);

                super.save(null);
            }

        }
    }

    public void agregarObsAntecedente() {

        if (getSelected() != null) {
            if (getSelected().verifObsAntecedente()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacionAntecedente(fecha);

                getSelected().transferirObsAntecedente();

                getSelected().setUsuarioUltimaObservacionAntecedente(usuario);

                ObservacionesDocumentosJudicialesAntecedentes obs = obsAntecedenteController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAntecedenteAux());

                obsAntecedenteController.setSelected(obs);
                obsAntecedenteController.saveNew(null);

                getSelected().setObservacionDocumentoJudicialAntecedente(obs);

                super.save(null);

                obs.setDocumentoJudicial(getSelected());

                obsAntecedenteController.save(null);
            }

        }
    }

    public void editarEstadoProcesal() {

        if (getSelected() != null) {
            if (getSelected().verifEstadoProcesal()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraEstadoProcesal(fecha);

                getSelected().transferirEstadoProcesal();

                getSelected().setUsuarioEstadoProcesal(usuario);

                EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();

                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());

                estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesController.save(null);

                getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);

                super.save(null);
            }

        }
    }

    public void agregarEstadoProcesal() {

        if (getSelected() != null) {
            if (getSelected().verifEstadoProcesal()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraEstadoProcesal(fecha);

                getSelected().transferirEstadoProcesal();

                getSelected().setUsuarioEstadoProcesal(usuario);

                EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                estadoProc.setUsuarioAlta(usuario);
                estadoProc.setUsuarioUltimoEstado(usuario);
                estadoProc.setFechaHoraAlta(fecha);
                estadoProc.setFechaHoraUltimoEstado(fecha);
                estadoProc.setEmpresa(usuario.getEmpresa());
                estadoProc.setEstadoProcesal(getSelected().getEstadoProcesalAux());
                estadoProc.setDocumentoJudicial(getSelected());
                estadoProc.setPersonaAlta(personaUsuario);
                estadoProc.setPersonaUltimoEstado(personaUsuario);
                estadoProc.setPersonaVisible(personaUsuario);
                estadoProc.setFechaHoraVisible(fecha);
                estadoProc.setVisible(true);

                estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesController.saveNew(null);

                getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                getSelected().setPersonaUltimoEstadoProcesal(personaUsuario);

                super.save(null);
            }

        }
    }

    public void actualizarDatosAnteriores() {
        if (tipoExpedienteActual != null) {
            if (tipoExpediente != null) {
                if (tipoExpediente.getId().equals(Constantes.TIPO_EXPEDIENTE_ENJUICIAMIENTO)) {
                    if (!tipoExpedienteActual.getId().equals(tipoExpediente)) {
                        tipoExpedienteAnterior = tipoExpedienteActual;
                        caratulaAnterior = caratula;
                        //caratula = "";
                        estadoProcesoAnterior = estadoProceso;
                        cambioAEnjuiciamiento = true;
                    }
                } else if (cambioAEnjuiciamiento) {
                    cambioAEnjuiciamiento = false;
                    tipoExpedienteAnterior = null;
                    caratula = caratulaAnterior;
                    caratulaAnterior = "";
                    estadoProcesoAnterior = null;
                }
            }

        }
    }

    @Override
    public void delete(ActionEvent event) {
        if (getSelected().getCanalEntradaDocumentoJudicial().equals(canal)) {
            if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId()) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO)) {
                Date fecha = ejbFacade.getSystemDateOnly();
                if (getSelected().getFechaHoraAlta().after(fecha) || filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_EXPEDIENTE_HISTORICO, rolElegido.getId())) {

                    List<PersonasPorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

                    for (PersonasPorDocumentosJudiciales per : lista) {
                        personasPorDocumentosJudicialesController.setSelected(per);
                        per.setFechaHoraUltimoEstado(fecha);
                        per.setUsuarioUltimoEstado(usuario);
                        personasPorDocumentosJudicialesController.save(event);
                        personasPorDocumentosJudicialesController.delete(null);
                    }

                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setUsuarioUltimoEstado(usuario);
                    super.save(event);
                    super.delete(event);

                    buscarPorFechaAlta();
                } else {
                    JsfUtil.addErrorMessage("Solo se pueden borrar expedientes creados en el dia");
                }
            } else {
                JsfUtil.addErrorMessage("Solo un funcionario de Secretaria puede borrarlo");
            }
        } else {
            JsfUtil.addErrorMessage("Solo puede borrar documentos que fueron ingresados directamente en Secretaria");
        }
    }

    public boolean validarSaveNew() {
        if (getSelected() != null) {
            /*
            if (getSelected().getFechaPresentacion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_fechaPresentacion"));
                return;
            }
             */

 /*
            if (getSelected().getCausa() != null) {
                if (!"".equals(getSelected().getCausa())) {
                    List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", nroCausa).getResultList();
                    if (!listaDocs.isEmpty()) {
                        JsfUtil.addErrorMessage("Ya existe una causa con nro " + getSelected().getCausa());
                        return false;
                    }
                }
            }
             */
 /*
            if (nroCausa == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_nroCausa"));
                return false;
            } else if ("".equals(nroCausa)) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_nroCausa"));
                return false;
            } else {
                List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", nroCausa).getResultList();
                if (!listaDocs.isEmpty()) {
                    JsfUtil.addErrorMessage("Ya existe una causa con nro " + getSelected().getCausa());
                    return false;
                }
            }
             */
            if (getSelected().getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_caratula"));
                return false;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_caratula"));
                return false;
            }
            /*
            if (getSelected().getMotivoProceso() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_motivoProceso"));
                return;
            } else if ("".equals(getSelected().getMotivoProceso())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_motivoProceso"));
                return;
            }
             */
 /*
            if (getSelected().getEstadoProcesal() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_estadoProcesal"));
                return;
            } else if ("".equals(getSelected().getEstadoProcesal())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_estadoProcesal"));
                return;
            }
             */

 
            if (listaPersonas == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_persona"));
                return false;
            } else if (listaPersonas.size() < 1) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_persona"));
                return false;
            }
 
            /*
            if (tipoExpediente == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_tipoExpediente"));
                return false;
            }
             */
            if (fojas == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_fojas"));
                return false;
            } else if (fojas == 0) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_fojas"));
                return false;
            }

            /*
            if (listaPersonasOrigen.isEmpty()) {
                if (null == getSelected().getTipoExpediente().getId()) {
                    JsfUtil.addErrorMessage("Persona: campo requerido");
                    return false;
                } else {
                    switch (getSelected().getTipoExpediente().getId()) {
                        case Constantes.TIPO_EXPEDIENTE_ACUSACION:
                            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_personaAcusadora"));
                            return false;
                        case Constantes.TIPO_EXPEDIENTE_DENUNCIA:
                            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_personaDenunciante"));
                            return false;
                        case Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR:
                        case Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO:
                            break;
                        default:
                            JsfUtil.addErrorMessage("Persona: campo requerido");
                            return false;
                    }
                }
            }
            */

            //PrimeFaces current = PrimeFaces.current();
            //current.executeScript("alert('hola'); $('#DocumentosCreateForm:botonGuardar').click();");
            return true;
        }

        return false;
    }

    public boolean validarSaveNewActuacion() {
        if (getSelected() != null) {
            /*
            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar documento");
                return false;
            }
             */
            if (actuacion.getDescripcion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_descripcion"));
                return false;
            } else if ("".equals(actuacion.getDescripcion())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_descripcion"));
                return false;
            }
            if (actuacion.getFojas() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_fojas"));
                return false;
            } else if (actuacion.getFojas() == 0) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_fojas"));
                return false;
            }
            if (actuacion.getObjetoActuacion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_objetoActuacion"));
                return false;
            }
            if (actuacion.getTipoActuacion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateExpActuacionesHelpText_tipoActuacion"));
                return false;
            }

            return true;
        }

        return false;
    }

    private void guardarNotificacion(ExpActuaciones actuacion, Personas remitente, Personas destinatario) {
        ExpNotificaciones noti = expNotificacionController.prepareCreate(null);
        noti.setActuacion(actuacion);
        noti.setRemitente(remitente);
        noti.setDestinatario(destinatario);

        ExpEstadosNotificacion estado = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosNotificacion.findByCodigo", ExpEstadosNotificacion.class).setParameter("codigo", Constantes.ESTADO_NOTIFICACION_NO_LEIDO).getSingleResult();

        noti.setEstado(estado);

        noti.setFechaHoraAlta(actuacion.getFechaHoraAlta());
        noti.setDocumentoJudicial(actuacion.getDocumentoJudicial());
        noti.setFechaHoraLectura(null);
        noti.setTexto(actuacion.getPersonaAlta().getNombresApellidos() + "\n\n\nUd ha recibido una notificacion de " + remitente.getNombresApellidos() + ". Favor verificar en el sistema");

        expNotificacionController.setSelected(noti);
        expNotificacionController.saveNew(null);

    }

    public void enviarEmailAviso(String email, String asunto, String msg) {

        BodyPart texto = new MimeBodyPart();
        try {
            texto.setContent(msg, "text/html; charset=utf-8");

            Utils.sendEmailAsync(par.getIpServidorEmail(),
                    par.getPuertoServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    par.getContrasenaServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    email,
                    asunto,
                    texto);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return;
        }

    }

    private void guardarRolFirmantePorActuacion(ExpActuaciones act, Integer rol) {
        ExpRolesFirmantesPorActuaciones firm = rolesFirmantesPorActuacionesController.prepareCreate(null);
        firm.setActuacion(act);
        firm.setDocumentoJudicial(act.getDocumentoJudicial());

        Estados est = ejbFacade.getEntityManager().createNamedQuery("Estados.findByCodigo", Estados.class).setParameter("codigo", Constantes.ESTADO_USUARIO_AC).getSingleResult();

        firm.setEstado(est);
        firm.setFechaHoraAlta(ejbFacade.getSystemDate());
        firm.setFirmado(false);
        firm.setPersonaAlta(personaUsuario);

        AntecedentesRoles antRol = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRoles.findById", AntecedentesRoles.class).setParameter("id", rol).getSingleResult();

        firm.setRolFirmante(antRol);

        rolesFirmantesPorActuacionesController.setSelected(firm);
        rolesFirmantesPorActuacionesController.save(null);
    }

    private boolean guardarActuacion(UploadedFile file, Date fecha, String hash, DocumentosJudiciales doc, ExpActuaciones act, boolean validar, Personas per) {

        String nombreAbogado = per.getNombresApellidos();
        String ci = per.getCi() == null ? "" : per.getCi();
        if (file != null) {
            if (file.getContent().length > 0) {

                if (par == null) {
                    JsfUtil.addErrorMessage("Error al obtener parametros");
                    return false;
                }

                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file.getInputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error al leer archivo");
                    return false;
                }

                expActuacionesController.setSelected(act);

                expActuacionesController.saveNew(null);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String nombreArchivo = simpleDateFormat.format(actuacion.getFechaHoraAlta());
                nombreArchivo += "_" + doc.getId() + "_" + act.getId() + ".pdf";

                act.setArchivo(nombreArchivo);

                FileOutputStream fos = null;
                try {
                    /*
                    fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                     */

                    if (validar) {
                        if (Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(act.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())) {
                            fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                            fos.write(bytes);
                            fos.flush();
                            fos.close();
                            autenticar(act, false);

                            List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                            List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

                            lista.addAll(lista2);

                            savePersonasFirmantes(act, lista, new ArrayList<>());
                        } else {
                            fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp");
                            fos.write(bytes);
                            fos.flush();
                            fos.close();
                            Path path = Paths.get(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp");
                            long tamano = Files.size(path);

                            agregarSelloDeCargoQR(par.getRutaArchivos() + File.separator + nombreArchivo, nombreAbogado, ci, doc.getId() + "-" + act.getId(), tamano / (1024 * 1024), fecha, hash, fecha);
                            File archivo = new File(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp");
                            archivo.delete();
                        }

                    } else {
                        fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                        fos.write(bytes);
                        fos.flush();
                        fos.close();
                    }

                    if (Constantes.TIPO_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getId())) {
                        List<Personas> lista = new ArrayList<>();

                        lista.add(per);
                        savePersonasFirmantes(act, lista, new ArrayList<>());
                    }

                } catch (FileNotFoundException ex) {
                    JsfUtil.addErrorMessage("No se pudo guardar archivo");
                    fos = null;
                } catch (IOException ex) {
                    JsfUtil.addErrorMessage("No se pudo guardar archivo");
                    fos = null;
                }

                if (fos != null) {
                    expActuacionesController.save(null);

                    if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())
                            || Constantes.TIPO_ACTUACION_RECUSACION.equals(act.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())) {
                        guardarNotificacion(actuacion, personaUsuario, destinatario);
                    }

                    if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
                        guardarRolFirmantePorActuacion(actuacion, Constantes.ROL_UJIER);
                    }

                    return true;

                } else {
                    expActuacionesController.delete(null);
                    return false;
                }

            }
        }
        return false;
    }

    private void agregarSelloDeCargoQR(String nombre, String nombreAbogado, String ci, String nombreCodigo, float tamano, Date fechaHoraAlta, String hash, Date fecha) {
        PdfWriter writer;
        try {

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(nombre + "_temp"), new PdfWriter(nombre));

            // qrCode.createAwtImage(Color.darkGray, Color.darkGray);
            PageSize ps = new PageSize(pdfDoc.getFirstPage().getPageSize());
            pdfDoc.addNewPage(pdfDoc.getNumberOfPages() + 1, ps);

            PdfPage page = pdfDoc.getPage(pdfDoc.getNumberOfPages());

            float fontSize = 12.0f;
            float allowedWidth = 500;

            String fechaString = ejbFacade.getSystemDateString(fecha, 0);

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            String mensaje = "DOCUMENTO PRESENTADO ELECTRÓNICAMENTE CON FECHA DE SELLO DE CARGO: " + fechaString + " A LAS " + format.format(fecha) + ", CONFORME EL PROTOCOLO DE TRAMITACIÓN ELECTRÓNICA DEl JURADO DE ENJUICIAMIENTO DE MAGISTRADOS. QUEDA CERTIFICADA SU RECEPCIÓN";

            Paragraph paragraph = new Paragraph(mensaje).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);

            Canvas canvas = new Canvas(new PdfCanvas(page), pdfDoc, page.getMediaBox());
            RootRenderer canvasRenderer = canvas.getRenderer();
            // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
            paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 750))));

            paragraph.setWidth(500);
            canvas.showTextAligned(paragraph, 50, 350, TextAlignment.LEFT);

            String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_ESCRITO;
            BarcodeQRCode qrCode = new BarcodeQRCode(path + "?hash=" + hash);
            java.awt.Image imagen = qrCode.createAwtImage(Color.black, new Color(0, 0, 0, 0));
            PdfCanvas under = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
            Rectangle rect = new Rectangle(20, 100, 200, 200);
            under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);

            Paragraph paragraph1 = new Paragraph("Registrado electrónicamente por: " + nombreAbogado + " CI. " + ci).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph1.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph1.setWidth(500);
            canvas.showTextAligned(paragraph1, 50, 300, TextAlignment.LEFT);

            Paragraph paragraph2 = new Paragraph("NOMBRE: ").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph2.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph2.setWidth(130);
            canvas.showTextAligned(paragraph2, 250, 250, TextAlignment.LEFT);

            Paragraph paragraph2_ = new Paragraph(nombreCodigo).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph2_.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph2_.setWidth(200);
            canvas.showTextAligned(paragraph2_, 400, 250, TextAlignment.LEFT);

            Paragraph paragraph3 = new Paragraph("TAMAÑO: ").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph3.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph3.setWidth(130);
            canvas.showTextAligned(paragraph3, 250, 230, TextAlignment.LEFT);

            Paragraph paragraph3_ = new Paragraph(DecimalFormat.getNumberInstance().format(tamano) + " MB").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph3_.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph3_.setWidth(200);
            canvas.showTextAligned(paragraph3_, 400, 230, TextAlignment.LEFT);

            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Paragraph paragraph4 = new Paragraph("FECHA REGISTRO ELECTRONICO: ").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph4.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(500, 500))));

            paragraph4.setWidth(130);
            canvas.showTextAligned(paragraph4, 250, 190, TextAlignment.LEFT);

            Paragraph paragraph4_ = new Paragraph(format2.format(fecha)).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph4_.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(100, 500))));

            paragraph4_.setWidth(200);
            canvas.showTextAligned(paragraph4_, 400, 190, TextAlignment.LEFT);

            Paragraph paragraph5 = new Paragraph(hash + hash + hash + hash).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(fontSize);
            paragraph5.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(500, 500))));

            paragraph5.setWidth(300);
            canvas.showTextAligned(paragraph5, 250, 130, TextAlignment.LEFT);

            canvas.close();

            /*
            //PdfCanvas under = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(), new PdfResources(), pdfDoc);
            for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {
                PdfPage pagina = pdfDoc.getPage(i + 1);
                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);

                // PdfXObject xObject = PdfXObject();
                Rectangle rect = new Rectangle(page.getPageSize().getRight() - 100, 1, 50, 50);
                under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);
                //under.addImage(ImageDataFactory.create(imagen, Color.white), 50, 0f, 0f, 50, 0, 0, false);

                Paragraph paragraph2 = new Paragraph("Para conocer la validez del documento verifique aqui").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE)).setFontSize(8);
                Canvas canvas2 = new Canvas(new PdfCanvas(pagina), pdfDoc, pagina.getMediaBox());
                RootRenderer canvasRenderer2 = canvas.getRenderer();
                paragraph2.createRendererSubTree().setParent(canvasRenderer2).layout(new LayoutContext(new LayoutArea(0, new Rectangle(page.getPageSize().getRight() - 100, 6 * 2))));
                paragraph2.setWidth(60);
                canvas2.showTextAligned(paragraph2, page.getPageSize().getRight() - 100, 10, TextAlignment.RIGHT);
                canvas2.close();

                under.saveState();
            }
             */
            pdfDoc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentosJudicialesViejosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesViejosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void agregarQR(String nombre, String hash, boolean primeraPagina) {
        PdfWriter writer;
        try {
            /*
            writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            BarcodeQRCode qrCode = new BarcodeQRCode(hash);
            PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDoc);
            Image barcodeImage = new Image(barcodeObject).setWidth(100f).setHeight(100f);
            //document.add(new Paragraph().add(barcodeImage));
            document.add(barcodeImage);
            
            document.close();
             */

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(nombre + "_temp"), new PdfWriter(nombre));

            Document document = new Document(pdfDoc);

            String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_ACTUACION;
            BarcodeQRCode qrCode = new BarcodeQRCode(path + "?hash=" + hash);
            // PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDoc);
            // Image barcodeImage = new Image(barcodeObject).setWidth(50f).setHeight(50f);

            java.awt.Image imagen = qrCode.createAwtImage(Color.black, new Color(0, 0, 0, 0));

            if (primeraPagina) {
                // qrCode.createAwtImage(Color.darkGray, Color.darkGray);
                PageSize ps = new PageSize(pdfDoc.getFirstPage().getPageSize());
                pdfDoc.addNewPage(1, ps);

                PdfPage page = pdfDoc.getPage(1);

                float fontSize = 50.0f;
                float allowedWidth = 500;

                Paragraph paragraph = new Paragraph("Es copia del instrumento que tuve a la vista").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(fontSize);

                Canvas canvas = new Canvas(new PdfCanvas(page), pdfDoc, page.getMediaBox());
                RootRenderer canvasRenderer = canvas.getRenderer();
                // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
                while (paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2)))).getStatus() != LayoutResult.FULL) {
                    paragraph.setFontSize(--fontSize);
                }

                float xCoord = page.getPageSize().getRight() / 2;
                float yCoord = 120;

                paragraph.setWidth(allowedWidth);
                canvas.showTextAligned(paragraph, xCoord, yCoord, TextAlignment.CENTER);
                canvas.close();
            }

            //PdfCanvas under = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(), new PdfResources(), pdfDoc);
            for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {

                PdfPage pagina = pdfDoc.getPage(i + 1);

                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);

                // PdfXObject xObject = PdfXObject();
                Rectangle rect = new Rectangle(pagina.getPageSize().getRight() - 100, 1, 80, 80);
                under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);
                //under.addImage(ImageDataFactory.create(imagen, Color.white), 50, 0f, 0f, 50, 0, 0, false);

                Paragraph paragraph2 = new Paragraph("Para conocer la validez del documento verifique aqui").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE)).setFontSize(8);
                Canvas canvas2 = new Canvas(new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc), pdfDoc, pagina.getMediaBox());

                RootRenderer canvasRenderer2 = canvas2.getRenderer();
                paragraph2.createRendererSubTree().setParent(canvasRenderer2).layout(new LayoutContext(new LayoutArea(0, new Rectangle(pagina.getPageSize().getRight() - 100, 6 * 2))));
                paragraph2.setWidth(60);
                canvas2.showTextAligned(paragraph2, pagina.getPageSize().getRight() - 100, 10, TextAlignment.RIGHT);
                canvas2.close();

                under.saveState();
            }

            pdfDoc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentosJudicialesViejosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesViejosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean guardarActuacion(UploadedFile file, String nombreArchivo, Date fecha, DocumentosJudiciales doc, Resoluciones res, Personas per, String hash, ExpTiposActuacion tipoActuacion, Integer parFojas, boolean parVisible, boolean validar, String nombresAbogado, String ci) {

        if (file != null) {
            if (file.getContent().length > 0) {

                if (par == null) {
                    JsfUtil.addErrorMessage("Error al obtener parametros");
                    return false;
                }

                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file.getInputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error al leer archivo");
                    return false;
                }

                ExpObjetosActuacion obj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_PRESENTACION).getSingleResult();

                ExpActuaciones act = expActuacionesController.prepareCreate(null);
                act.setFechaHoraAlta(fecha);
                act.setFechaHoraUltimoEstado(fecha);
                act.setFechaPresentacion(generarFechaPresentacion(fecha));
                act.setDocumentoJudicial(doc);
                act.setTipoActuacion(tipoActuacion);
                act.setObjetoActuacion(obj);
                act.setFojas(parFojas);
                act.setEmpresa(usuario.getEmpresa());
                act.setPersonaAlta(per);
                act.setPersonaUltimoEstado(per);
                act.setDescripcion(tipoActuacion.getDescripcion());
                act.setHash(hash);
                act.setResolucion(res);
                act.setVisible(parVisible);

                expActuacionesController.setSelected(act);

                expActuacionesController.saveNew(null);

                if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getCategoriaActuacion().getId())
                        || Constantes.TIPO_ACTUACION_RECUSACION.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())) {
                    guardarNotificacion(act, personaUsuario, null);
                }

                if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
                    guardarRolFirmantePorActuacion(actuacion, Constantes.ROL_UJIER);
                }

                FileOutputStream fos = null;
                try {
                    /*
                    fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                     */

                    if (validar) {
                        fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp");
                        fos.write(bytes);
                        fos.flush();
                        fos.close();
                        Path path = Paths.get(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp");
                        long tamano = Files.size(path);

                        agregarSelloDeCargoQR(par.getRutaArchivos() + File.separator + nombreArchivo, nombresAbogado, ci, doc.getId() + "-" + act.getId(), tamano / (1024 * 1024), fecha, hash, fecha);
                        File archivo = new File(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp");
                        archivo.delete();
                    } else {
                        fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                        fos.write(bytes);
                        fos.flush();
                        fos.close();
                    }

                } catch (FileNotFoundException ex) {
                    JsfUtil.addErrorMessage("No se pudo guardar archivo");
                    fos = null;
                } catch (IOException ex) {
                    JsfUtil.addErrorMessage("No se pudo guardar archivo");
                    fos = null;
                }

                if (fos != null) {
                    act.setArchivo(nombreArchivo);
                    expActuacionesController.save(null);
                    return true;
                } else {
                    expActuacionesController.delete(null);
                    return false;
                }

            }
        }
        return false;
    }

    /*
    private void guardarActuacion(String hechos, Date fecha, DocumentosJudiciales doc, Personas per, List<Personas> listaPersonas, Personas abog, Integer tipoActuacionId) {

        ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", tipoActuacionId).getSingleResult();

        ExpActuaciones act = expActuacionesController.prepareCreate(null);
        act.setFechaHoraAlta(fecha);
        act.setFechaHoraUltimoEstado(fecha);
        act.setDocumentoJudicial(doc);
        act.setTipoActuacion(tipoActuacion);
        act.setEmpresa(usuario.getEmpresa());
        act.setPersonaAlta(per);
        act.setPersonaUltimoEstado(per);
        act.setDescripcion(tipoActuacion.getDescripcion());

        expActuacionesController.setSelected(act);

        expActuacionesController.saveNew(null);

        try {
            HashMap map = new HashMap();
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);

            map.put("fecha", fecha);

            
            if (listaPersonas != null) {
                if(!listaPersonas.isEmpty()){
                    String labelPersona = "";
                    String nombres = "";
                    for(int i = 0; i < listaPersonas.size(); i++){
                        Personas p = listaPersonas.get(i);

                        if(i > 0){
                            nombres += ", ";
                        }else{
                            if(doc.getTipoExpediente().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA){
                                if(listaPersonas.size() > 1){
                                   labelPersona = "Denunciantes:";
                                }else{
                                   labelPersona = "Denunciante:";
                                }
                            }else if(doc.getTipoExpediente().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION){
                                if(listaPersonas.size() > 1){
                                   labelPersona = "Acusadores:";
                                }else{
                                   labelPersona = "Acusador:";
                                }
                            }else{
                                if(listaPersonas.size() > 1){
                                   labelPersona = "Personas:";
                                }else{
                                   labelPersona = "Persona:";
                                }
                            }
                        }
                        
                        nombres += p.getNombresApellidos();
                    }

                    map.put("labelPersona", labelPersona);
                    map.put("nombrePersona", nombres);
                }
            }
            
            if (abog != null) {
                map.put("labelAbogado", "Abogado");
                map.put("nombreAbogado", abog.getNombresApellidos());
            }

            map.put("hechos", hechos);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + doc.getId() + "_" + act.getId() + ".pdf";

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/expDeclaracionHechos.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint, par.getRutaArchivos() + "/" + nombreArchivo);
            act.setArchivo(nombreArchivo);
            expActuacionesController.save(null);
        } catch (Exception e) {
            e.printStackTrace();
            expActuacionesController.delete(null);
        }

    }
     */
    private Date generarFechaPresentacion(Date fecha) {
        // Aqui analizar dias habiles y feriados

        LocalDateTime localDateTime = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (localDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
            localDateTime = localDateTime.plusDays(2);
        } else if (localDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
            localDateTime = localDateTime.plusDays(1);
        }

        int hora = localDateTime.getHour();
        int minuto = localDateTime.getMinute();
        int segundo = localDateTime.getSecond();

        int horaPresentacion = hora;
        int minutoPresentacion = minuto;
        int segundoPresentacion = segundo;

        if (hora < par.getHoraInicio() || (hora == par.getHoraInicio() && minuto < par.getMinutoInicio())) {
            horaPresentacion = par.getHoraInicio();
            minutoPresentacion = par.getMinutoInicio();
            segundoPresentacion = 0;
        }

        if (hora > par.getHoraFin() || (hora == par.getHoraFin() && minuto >= par.getMinutoFin())) {
            localDateTime = localDateTime.plusDays(1);
            horaPresentacion = par.getHoraInicio();
            minutoPresentacion = par.getMinutoInicio();
            segundoPresentacion = 0;
        }

        //if(horaPresentacion - hora > 0){
        localDateTime = localDateTime.plusHours(horaPresentacion - hora);
        /*}else{
            localDateTime = localDateTime.minusHours(hora - horaPresentacion);
        }*/

        //if(minutoPresentacion - minuto > 0){
        localDateTime = localDateTime.plusMinutes(minutoPresentacion - minuto);
        /*}else{
            localDateTime = localDateTime.minusMinutes(minuto - minutoPresentacion);
        }*/

        //if(segundoPresentacion - segundo > 0){
        localDateTime = localDateTime.plusSeconds(segundoPresentacion - segundo);
        /*}else{
            localDateTime = localDateTime.minusHours(segundo - segundoPresentacion);
        }*/

        Date currentDatePlusOneDay = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

        return currentDatePlusOneDay;
    }

    private Date generarFechaPresentacion2(Date fecha) {
        // Aqui analizar dias habiles y feriados

        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            cal.add(Calendar.DATE, 2);
            //return cal.getTime();
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.DATE, 1);
            //return cal.getTime();
        }

        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);

        int horaPresentacion = hora;
        int minutoPresentacion = minuto;

        if (hora < par.getHoraInicio() && minuto < par.getMinutoInicio()) {
            horaPresentacion = par.getHoraInicio();
            minutoPresentacion = par.getMinutoInicio();
        }

        if (hora > par.getHoraFin() && minuto > par.getMinutoFin()) {
            cal.add(Calendar.DATE, 1);
            horaPresentacion = par.getHoraInicio();
            minutoPresentacion = par.getMinutoInicio();
        }

        cal.set(Calendar.HOUR, horaPresentacion);
        cal.set(Calendar.MINUTE, minutoPresentacion);

        Date fecha2 = cal.getTime();

        return cal.getTime();
    }

    private int cantDiasProxDiaHabil(Date fecha) {
        // Aqui analizar dias habiles y feriados

        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return 2;
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return 1;
        }

        return 0;
    }

    public void saveFirmantes() {
        List<ExpPersonasFirmantesPorActuaciones> listaFirmantesActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacion", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", selectedActuacion).getResultList();
        savePersonasFirmantes(selectedActuacion, listaPersonasFirmantes, listaFirmantesActual);
        // saveRolesFirmantes();
    }

    public void savePersonasFirmantes(ExpActuaciones act, List<Personas> listaPersonasFirm, List<ExpPersonasFirmantesPorActuaciones> listaFirmantesActual) {

        Date fecha = ejbFacade.getSystemDate();
        Personas per2 = null;
        ExpPersonasFirmantesPorActuaciones perDocActual = null;
        boolean encontro = false;

        boolean enviarTextoNuevaParte;
        boolean enviarTextoBorrarParte;

        for (Personas per : listaPersonasFirm) {
            enviarTextoNuevaParte = false;
            enviarTextoBorrarParte = false;
            encontro = false;
            perDocActual = null;
            for (int i = 0; i < listaFirmantesActual.size(); i++) {
                per2 = listaFirmantesActual.get(i).getPersonaFirmante();
                if (per2.equals(per)) {
                    encontro = true;
                    perDocActual = listaFirmantesActual.get(i);
                    break;
                }
            }
            if (!encontro) {
                enviarTextoNuevaParte = true;
                ExpPersonasFirmantesPorActuaciones perDoc = personasFirmantesPorActuacionesController.prepareCreate(null);
                perDoc.setPersonaFirmante(per);
                perDoc.setActuacion(act);
                perDoc.setDocumentoJudicial(getSelected());
                perDoc.setPersonaAlta(personaUsuario);
                perDoc.setFechaHoraAlta(fecha);
                perDoc.setFirmado(false);
                perDoc.setFechaHoraFirma(null);
                perDoc.setEstado(new Estados("AC"));
                personasFirmantesPorActuacionesController.setSelected(perDoc);
                personasFirmantesPorActuacionesController.saveNew(null);
            } else {
                if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                        enviarTextoNuevaParte = true;
                    } else {
                        enviarTextoBorrarParte = true;
                    }

                    perDocActual.setEstado(new Estados(per.getEstado()));
                    perDocActual.setFechaHoraAlta(fecha);
                    perDocActual.setPersonaAlta(personaUsuario);
                    perDocActual.setFirmado(per.isFirmado());
                    personasFirmantesPorActuacionesController.setSelected(perDocActual);
                    personasFirmantesPorActuacionesController.save(null);
                }

            }

            if (enviarTextoNuevaParte) {
                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Tiene una nueva presentacion que firmar en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Firma pendiente en Expediente Electrónico JEM", texto);
            }

            if (enviarTextoBorrarParte) {
                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Ud dejo de ser firmante en una presentacion en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Dejo de ser firmante en Expediente Electrónico JEM", texto);
            }
        }

        for (int i = 0; i < listaFirmantesActual.size(); i++) {
            per2 = listaFirmantesActual.get(i).getPersonaFirmante();
            encontro = false;
            for (Personas per : listaPersonasFirm) {
                if (per2.equals(per)) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                listaFirmantesActual.get(i).setEstado(new Estados("IN"));
                listaFirmantesActual.get(i).setFechaHoraAlta(fecha);
                listaFirmantesActual.get(i).setPersonaAlta(personaUsuario);
                listaFirmantesActual.get(i).setFirmado(per2.isFirmado());
                personasFirmantesPorActuacionesController.setSelected(listaFirmantesActual.get(i));
                personasFirmantesPorActuacionesController.save(null);
            }
        }
    }

    public void saveRolesFirmantes() {

        Date fecha = ejbFacade.getSystemDate();
        AntecedentesRoles per2 = null;
        ExpRolesFirmantesPorActuaciones perDocActual = null;
        boolean encontro = false;
        List<ExpRolesFirmantesPorActuaciones> listaFirmantesActual = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacion", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", selectedActuacion).getResultList();

        boolean enviarTextoNuevaParte;
        boolean enviarTextoBorrarParte;

        for (AntecedentesRoles per : listaRolesFirmantes) {
            enviarTextoNuevaParte = false;
            enviarTextoBorrarParte = false;
            encontro = false;
            perDocActual = null;
            for (int i = 0; i < listaFirmantesActual.size(); i++) {
                per2 = listaFirmantesActual.get(i).getRolFirmante();
                if (per2.equals(per)) {
                    encontro = true;
                    perDocActual = listaFirmantesActual.get(i);
                    break;
                }
            }
            if (!encontro) {
                enviarTextoNuevaParte = true;
                ExpRolesFirmantesPorActuaciones perDoc = rolesFirmantesPorActuacionesController.prepareCreate(null);
                perDoc.setRolFirmante(per);
                perDoc.setActuacion(selectedActuacion);
                perDoc.setDocumentoJudicial(getSelected());
                perDoc.setPersonaAlta(personaUsuario);
                perDoc.setFechaHoraAlta(fecha);
                perDoc.setFirmado(false);
                perDoc.setFechaHoraFirma(null);
                perDoc.setEstado(new Estados("AC"));
                rolesFirmantesPorActuacionesController.setSelected(perDoc);
                rolesFirmantesPorActuacionesController.saveNew(null);
            } else {
                if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                        enviarTextoNuevaParte = true;
                    } else {
                        enviarTextoBorrarParte = true;
                    }

                    perDocActual.setEstado(new Estados(per.getEstado()));
                    perDocActual.setFechaHoraAlta(fecha);
                    perDocActual.setPersonaAlta(personaUsuario);
                    perDocActual.setFirmado(per.isFirmado());
                    rolesFirmantesPorActuacionesController.setSelected(perDocActual);
                    rolesFirmantesPorActuacionesController.save(null);
                }

            }

            List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_UJIER).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            for (Personas pers : listaPer) {

                if (enviarTextoNuevaParte) {
                    String texto = "<p>Hola " + pers.getNombresApellidos() + "<br> "
                            + "     Tiene una nueva presentacion que firmar en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + selectedActuacion.getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(pers.getEmail(), "Firma pendiente en Expediente Electrónico JEM", texto);
                }

                if (enviarTextoBorrarParte) {
                    String texto = "<p>Hola " + pers.getNombresApellidos() + "<br> "
                            + "     Ud dejo de ser firmante en una presentacion en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + selectedActuacion.getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(pers.getEmail(), "Dejo de ser firmante en Expediente Electrónico JEM", texto);
                }
            }
        }

        for (int i = 0; i < listaFirmantesActual.size(); i++) {
            per2 = listaFirmantesActual.get(i).getRolFirmante();
            encontro = false;
            for (AntecedentesRoles per : listaRolesFirmantes) {
                if (per2.equals(per)) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                listaFirmantesActual.get(i).setEstado(new Estados("IN"));
                listaFirmantesActual.get(i).setFechaHoraAlta(fecha);
                listaFirmantesActual.get(i).setPersonaAlta(personaUsuario);
                listaFirmantesActual.get(i).setFirmado(per2.isFirmado());
                rolesFirmantesPorActuacionesController.setSelected(listaFirmantesActual.get(i));
                rolesFirmantesPorActuacionesController.save(null);
            }
        }
    }

    public void saveRevisiones() {

        Date fecha = ejbFacade.getSystemDate();
        Personas per2 = null;
        ExpRevisionesPorActuaciones perDocActual = null;
        boolean encontro = false;
        List<ExpRevisionesPorActuaciones> listaRevisionesActual = ejbFacade.getEntityManager().createNamedQuery("ExpRevisionesPorActuaciones.findByActuacion", ExpRevisionesPorActuaciones.class).setParameter("actuacion", selectedActuacion).getResultList();

        boolean enviarTextoNuevaRevision;
        boolean enviarTextoBorrarRevision;

        for (Personas per : listaRevisiones) {
            enviarTextoNuevaRevision = false;
            enviarTextoBorrarRevision = false;
            encontro = false;
            perDocActual = null;
            for (int i = 0; i < listaRevisionesActual.size(); i++) {
                per2 = listaRevisionesActual.get(i).getRevisionPor();
                if (per2.equals(per)) {
                    encontro = true;
                    perDocActual = listaRevisionesActual.get(i);
                    break;
                }
            }
            if (!encontro) {
                enviarTextoNuevaRevision = true;
                ExpRevisionesPorActuaciones perDoc = revisionesPorActuacionesController.prepareCreate(null);
                perDoc.setRevisionPor(per);
                perDoc.setActuacion(selectedActuacion);
                perDoc.setDocumentoJudicial(getSelected());
                perDoc.setPersonaAlta(personaUsuario);
                perDoc.setFechaHoraAlta(fecha);
                perDoc.setRevisado(false);
                perDoc.setFechaHoraRevision(null);
                perDoc.setEstado(new Estados("AC"));
                revisionesPorActuacionesController.setSelected(perDoc);
                revisionesPorActuacionesController.saveNew(null);
            } else {
                if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                        enviarTextoNuevaRevision = true;
                    } else {
                        enviarTextoBorrarRevision = true;
                    }

                    perDocActual.setEstado(new Estados(per.getEstado()));
                    perDocActual.setFechaHoraAlta(fecha);
                    perDocActual.setPersonaAlta(personaUsuario);
                    perDocActual.setRevisado(per.isRevisado());
                    revisionesPorActuacionesController.setSelected(perDocActual);
                    revisionesPorActuacionesController.save(null);
                }

            }

            if (enviarTextoNuevaRevision) {
                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Ud ha sido añadido para revisar una presentacion en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + selectedActuacion.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Revision pendiente en Expediente Electrónico JEM", texto);
            }

            if (enviarTextoBorrarRevision) {
                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Ud ya no debe mas revisar una presentacion en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + selectedActuacion.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Ya no debe revisar presentacion en Expediente Electrónico JEM", texto);
            }
        }

        for (int i = 0; i < listaRevisionesActual.size(); i++) {
            per2 = listaRevisionesActual.get(i).getRevisionPor();
            encontro = false;
            for (Personas per : listaRevisiones) {
                if (per2.equals(per)) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                listaRevisionesActual.get(i).setEstado(new Estados("IN"));
                listaRevisionesActual.get(i).setFechaHoraAlta(fecha);
                listaRevisionesActual.get(i).setPersonaAlta(personaUsuario);
                listaRevisionesActual.get(i).setRevisado(per2.isRevisado());
                revisionesPorActuacionesController.setSelected(listaRevisionesActual.get(i));
                revisionesPorActuacionesController.save(null);
            }
        }
    }

    public void savePartes() {

        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            Personas per2 = null;
            ExpPartesPorDocumentosJudiciales perDocActual = null;
            ExpPartesPorDocumentosJudiciales perDoc = null;
            PersonasPorDocumentosJudiciales perDoc2 = null;
            boolean encontro = false;
            List<ExpPartesPorDocumentosJudiciales> listaPartesActual = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicial", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();
            List<PersonasPorDocumentosJudiciales> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            ExpTiposParte tipoParteAcusado = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ACUSADO).getSingleResult();

            for (PersonasPorDocumentosJudiciales per : listaPersonasActual) {
                ExpPartesPorDocumentosJudiciales perParte = partesPorDocumentosJudicialesController.prepareCreate(null);
                perParte.setDocumentoJudicial(per.getDocumentoJudicial());
                perParte.setEmpresa(per.getEmpresa());
                perParte.setEstado(per.getEstado());
                perParte.setFechaHoraAlta(per.getFechaHoraAlta());
                perParte.setFechaHoraUltimoEstado(per.getFechaHoraUltimoEstado());
                perParte.setPersona(per.getPersona());
                perParte.setTipoParte(tipoParteAcusado);
                listaPartesActual.add(perParte);
            }

            boolean enviarTextoNuevaParte;
            boolean enviarTextoBorrarParte;
            boolean modificarCaratula = false;

            for (Personas per : listaPartesAdmin) {
                enviarTextoNuevaParte = false;
                enviarTextoBorrarParte = false;
                encontro = false;
                perDocActual = null;
                for (int i = 0; i < listaPartesActual.size(); i++) {
                    per2 = listaPartesActual.get(i).getPersona();
                    if (per2.equals(per)) {
                        encontro = true;
                        perDocActual = listaPartesActual.get(i);
                        break;
                    }
                }

                if (!encontro) {
                    enviarTextoNuevaParte = true;
                    if (Constantes.TIPO_PARTE_ACUSADO == per.getTipoParte().getId()) {
                        modificarCaratula = true;
                        perDoc2 = personasPorDocumentosJudicialesController.prepareCreate(null);
                        perDoc2.setPersona(per);
                        perDoc2.setDocumentoJudicial(getSelected());
                        perDoc2.setPersonaAlta(personaUsuario);
                        perDoc2.setUsuarioAlta(usuario);
                        perDoc2.setUsuarioUltimoEstado(usuario);
                        perDoc2.setFechaHoraAlta(fecha);
                        perDoc2.setFechaHoraUltimoEstado(fecha);
                        perDoc2.setEmpresa(per.getEmpresa());
                        perDoc2.setEstado(new Estados("AC"));
                        personasPorDocumentosJudicialesController.setSelected(perDoc2);
                        personasPorDocumentosJudicialesController.saveNew(null);
                    } else {
                        if (Constantes.TIPO_PARTE_ACUSADOR == per.getTipoParte().getId()) {
                            modificarCaratula = true;
                        }
                        perDoc = partesPorDocumentosJudicialesController.prepareCreate(null);
                        perDoc.setPersona(per);
                        perDoc.setDocumentoJudicial(getSelected());
                        perDoc.setPersonaAlta(personaUsuario);
                        perDoc.setFechaHoraAlta(fecha);
                        perDoc.setTipoParte(per.getTipoParte());
                        perDoc.setEstado(new Estados("AC"));
                        partesPorDocumentosJudicialesController.setSelected(perDoc);
                        partesPorDocumentosJudicialesController.saveNew(null);
                    }
                } else {
                    if (Constantes.TIPO_PARTE_ACUSADO == per.getTipoParte().getId()) {

                        perDoc2 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialPersona", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("persona", per.getId()).getSingleResult();

                        if (!perDoc2.getEstado().getCodigo().equals(per.getEstado())) {
                            if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                                enviarTextoNuevaParte = true;
                            } else {
                                enviarTextoBorrarParte = true;
                            }

                            modificarCaratula = true;

                            perDoc2.setEstado(new Estados(per.getEstado()));
                            personasPorDocumentosJudicialesController.setSelected(perDoc2);
                            personasPorDocumentosJudicialesController.save(null);
                        }
                    } else {
                        if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                            if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                                enviarTextoNuevaParte = true;
                            } else {
                                enviarTextoBorrarParte = true;
                            }

                            if (Constantes.TIPO_PARTE_ACUSADOR == per.getTipoParte().getId()) {
                                modificarCaratula = true;
                            }
                            perDocActual.setEstado(new Estados(per.getEstado()));
                            perDocActual.setFechaHoraAlta(fecha);
                            perDocActual.setPersonaAlta(personaUsuario);
                            perDocActual.setTipoParte(per.getTipoParte());
                            partesPorDocumentosJudicialesController.setSelected(perDocActual);
                            partesPorDocumentosJudicialesController.save(null);
                        }
                    }

                }

                if (enviarTextoNuevaParte) {
                    String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                            + "     Ud es parte en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + " Su parte en dicha causa es la de " + per.getTipoParte().getDescripcion() + "<br>"
                            + "Para ver la causa, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?doc=" + getSelected().getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(per.getEmail(), "Agregado como Parte en Expediente Electrónico JEM", texto);
                }

                if (enviarTextoBorrarParte) {
                    String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                            + "     Ud ya no es parte en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + " Ha sido borrado de la causa<br>";

                    enviarEmailAviso(per.getEmail(), "Borrado como Parte en Expediente Electrónico JEM", texto);
                }

            }

            for (int i = 0; i < listaPartesActual.size(); i++) {
                per2 = listaPartesActual.get(i).getPersona();
                encontro = false;
                for (Personas per : listaPartesAdmin) {
                    if (per2.equals(per)) {
                        encontro = true;
                        break;
                    }
                }

                if (!encontro) {
                    if (Constantes.TIPO_PARTE_ACUSADO != listaPartesActual.get(i).getTipoParte().getId()) {
                        listaPartesActual.get(i).setEstado(new Estados("IN"));
                        listaPartesActual.get(i).setFechaHoraAlta(fecha);
                        listaPartesActual.get(i).setPersonaAlta(personaUsuario);
                        listaPartesActual.get(i).setTipoParte(listaPartesActual.get(i).getTipoParte());
                        partesPorDocumentosJudicialesController.setSelected(listaPartesActual.get(i));
                        partesPorDocumentosJudicialesController.save(null);
                    } else {
                        // perDoc2 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialPersona", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("persona", per2.getId()).getSingleResult();

                        perDoc2 = personasPorDocumentosJudicialesController.prepareCreate(null);

                        perDoc2.setPersona(per2);
                        perDoc2.setDocumentoJudicial(getSelected());
                        perDoc2.setEmpresa(per2.getEmpresa());
                        perDoc2.setEstado(new Estados("IN"));
                        perDoc2.setFechaHoraAlta(fecha);
                        perDoc2.setFechaHoraUltimoEstado(fecha);
                        perDoc2.setPersonaAlta(personaUsuario);
                        perDoc2.setUsuarioAlta(usuario);
                        perDoc2.setUsuarioUltimoEstado(usuario);

                        personasPorDocumentosJudicialesController.setSelected(perDoc2);
                        personasPorDocumentosJudicialesController.save(null);
                    }
                }
            }
            /*
            for (int i = 0; i < listaPersonasActual.size(); i++) {
                per2 = listaPersonasActual.get(i).getPersona();
                encontro = false;
                for (Personas per : listaPartesAdmin) {
                    if (per2.equals(per) && Constantes.TIPO_PARTE_ACUSADO == per.getTipoParte().getId()) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    listaPartesActual.get(i).setEstado(new Estados("IN"));
                    listaPartesActual.get(i).setFechaHoraAlta(fecha);
                    listaPartesActual.get(i).setPersonaAlta(personaUsuario);
                    listaPartesActual.get(i).setTipoParte(per2.getTipoParte());

                    perDoc2 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialPersona", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("persona", per2.getId()).getSingleResult();

                    perDoc2.setEstado(new Estados("IN"));
                    perDoc2.setFechaHoraAlta(fecha);
                    perDoc2.setFechaHoraUltimoEstado(fecha);
                    perDoc2.setPersonaAlta(personaUsuario);
                    perDoc2.setUsuarioAlta(usuario);

                    personasPorDocumentosJudicialesController.setSelected(perDoc2);
                    personasPorDocumentosJudicialesController.save(null);
                }
            }
             */
            if (modificarCaratula) {
                List<PersonasPorDocumentosJudiciales> listaPersonasFinal = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("estado", new Estados("AC")).getResultList();

                List<Personas> listaAcusados = new ArrayList<>();
                for (PersonasPorDocumentosJudiciales per : listaPersonasFinal) {
                    listaAcusados.add(per.getPersona());
                }
                List<ExpPartesPorDocumentosJudiciales> listaPartesFinal = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicial", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

                List<Personas> listaAcusadores = new ArrayList<>();
                for (ExpPartesPorDocumentosJudiciales per : listaPartesFinal) {
                    if (Constantes.TIPO_PARTE_ACUSADOR == per.getTipoParte().getId()) {
                        listaAcusadores.add(per.getPersona());
                    }
                }

                generarCaratula(listaAcusadores, listaAcusados, getSelected());
                super.save(null);
            }
        }
    }

    public void saveNewActuacion() {
        if (actuacion != null && getSelected() != null) {

            if (!validarSaveNewActuacion()) {
                return;
            }

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un documento");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();
            actuacion.setFechaHoraAlta(fecha);
            actuacion.setFechaHoraUltimoEstado(fecha);

            actuacion.setFechaPresentacion(generarFechaPresentacion(fecha));

            if (Constantes.TIPO_ACTUACION_INCIDENTE.equals(actuacion.getTipoActuacion().getId())) {
                if (Constantes.OBJETO_ACTUACION_RECUSACION.equals(actuacion.getObjetoActuacion().getId())) {
                    ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RECUSACION).getSingleResult();

                    actuacion.setTipoActuacion(tipoActuacion);
                }
            }

            actuacion.setDocumentoJudicial(getSelected());
            actuacion.setPersonaAlta(personaUsuario);
            actuacion.setPersonaUltimoEstado(personaUsuario);
            actuacion.setEmpresa(personaUsuario.getEmpresa());

            if (actuacionPadre != null) {
                actuacion.setActuacionPadre(actuacionPadre);
            }

            //expActuacionesController.setSelected(actuacion);
            //expActuacionesController.save(null);
            boolean guardado = false;
            if (file != null) {
                if (file.getContent().length > 0) {
                    String hash = "";
                    try {
                        hash = Utils.generarHash(fecha, personaUsuario.getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                        return;
                    }

                    // guardado = guardarActuacion(file, fecha, hash, getSelected(), actuacion, esPrimerEscrito, personaUsuario.getNombresApellidos(), personaUsuario.getCi()==null?"":personaUsuario.getCi());
                    guardado = guardarActuacion(file, fecha, hash, getSelected(), actuacion, esPrimerEscrito, personaUsuario);
                }
            }

            if (guardado) {
                if (esPrimerEscrito) {
                    actuacionPadre = expActuacionesController.getSelected();
                }

                String estadoProcesalFinal = "";
                if (estadoProcesal != null) {
                    if (!"".equals(estadoProcesal)) {
                        estadoProcesalFinal = estadoProcesal;
                    }
                }

                if (actuacion.isVisible() && "".equals(estadoProcesalFinal)) {
                    estadoProcesalFinal = actuacion.getDescripcion();
                }

                if (!"".equals(estadoProcesalFinal)) {

                    EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();

                    estadoProc.setPersonaAlta(personaUsuario);
                    estadoProc.setPersonaUltimoEstado(personaUsuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEstadoProcesal(estadoProcesalFinal);
                    estadoProc.setDocumentoJudicial(getSelected());
                    estadoProc.setEmpresa(personaUsuario.getEmpresa());
                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    // estadoProc.setVisible(actuacion.isVisible());
                    estadoProc.setVisible(true);
                    estadoProc.setActuacion(actuacion);

                    //if(actuacion.isVisible()){
                    estadoProc.setPersonaVisible(personaUsuario);
                    estadoProc.setFechaHoraVisible(fecha);
                    //}

                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                    estadosProcesalesDocumentosJudicialesController.saveNew(null);

                    //if(actuacion.isVisible()){
                    getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                    getSelected().setFechaHoraEstadoProcesal(fecha);
                    getSelected().setPersonaUltimoEstadoProcesal(personaUsuario);
                    getSelected().setEstadoProcesal(estadoProcesalFinal);
                    super.save(null);
                    //}
                }

                if (observacion != null) {
                    if (!"".equals(observacion)) {
                        ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                        obs.setUsuarioAlta(usuario);
                        obs.setUsuarioUltimoEstado(usuario);
                        obs.setFechaHoraAlta(fecha);
                        obs.setFechaHoraUltimoEstado(fecha);
                        obs.setEmpresa(usuario.getEmpresa());
                        obs.setObservacion(observacion);
                        obs.setDocumentoJudicial(getSelected());
                        obs.setPersonaAlta(personaUsuario);
                        obs.setPersonaUltimoEstado(personaUsuario);
                        // obs.setVisible(actuacion.isVisible());
                        obs.setVisible(true);
                        obs.setActuacion(actuacion);

                        // if(actuacion.isVisible()){
                        obs.setPersonaVisible(personaUsuario);
                        obs.setFechaHoraVisible(fecha);
                        // }

                        obsController.setSelected(obs);
                        obsController.saveNew(null);

                        //if(actuacion.isVisible()){
                        getSelected().setObservacionDocumentoJudicial(obs);
                        getSelected().setFechaUltimaObservacion(fecha);
                        getSelected().setUsuarioUltimaObservacion(usuario);
                        getSelected().setPersonaUltimaObservacion(personaUsuario);
                        getSelected().setUltimaObservacion(observacion);

                        super.save(null);
                        // }
                    }
                }

                buscarActuaciones();

                if (!(Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_OFICIO.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_RESOLUCION.equals(expActuacionesController.getSelected().getTipoActuacion().getId()))) {
                    String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                            + "     Se a presentado una presentación del tipo " + expActuacionesController.getSelected().getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);
                    /*
                    if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {

                        String texto2 = "<p>Hola " + destinatario.getNombresApellidos() + "<br> "
                                + "     Se ha presentado una presentación del tipo " + expActuacionesController.getSelected().getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                                + getSelected().getCaratulaString()
                                + "<br><br>"
                                + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                                + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                        enviarEmailAviso(destinatario.getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto2);
                    }
                     */
                }
            }

            personasController.badgePresentacionesPendientes();

            // Providencia no necesita de un segundo documento
            if (!Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(expActuacionesController.getSelected().getTipoActuacion().getId())) {
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('ExpAcusacionesOtroArchivoDialog').show();");
            }
        }
    }

    public void saveNewExpediente() {

        if (getSelected() != null) {
            try {
                getSelected().setCausa(generarNroCausa());
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("No se pudo generar nro causa");
                return;
            }

            if (!validarSaveNew()) {
                return;
            }

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            String hash = "";
            try {
                hash = Utils.generarHash(fecha, personaUsuario.getId());;
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                return;
            }

            getSelected().setPersonaAlta(personaUsuario);
            getSelected().setFolios("");
            getSelected().setFechaPresentacion(generarFechaPresentacion(fecha));
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setDescripcionMesaEntrada(getSelected().getCaratula());
            //getSelected().setUltimaObservacion(ultimaObservacion);
            getSelected().setCanalEntradaDocumentoJudicial(canal);
            getSelected().setTipoDocumentoJudicial(tipoDoc);
            getSelected().setResponsable(usuario);
            getSelected().setDepartamento(departamento);

            //  getSelected().setMostrarWeb("SI");
            EstadosDocumento estado = estadoController.prepareCreate(null);
            estado.setCodigo("0");

            getSelected().setEstado(estado);

            EntradasDocumentosJudiciales doc = entradasDocumentosJudicialesController.prepareCreate(null);

            doc.setFechaHoraUltimoEstado(fecha);
            doc.setUsuarioUltimoEstado(usuario);
            doc.setFechaHoraAlta(fecha);
            doc.setUsuarioAlta(usuario);
            doc.setEmpresa(usuario.getEmpresa());
            doc.setRecibidoPor(usuario);
            doc.setEntregadoPor(entradaDocumentoJudicial.getEntregadoPor());
            doc.setTelefono(entradaDocumentoJudicial.getTelefono());
            doc.setNroCedulaRuc(entradaDocumentoJudicial.getNroCedulaRuc());
            javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(CONVERT(substring(nro_mesa_entrada,6),UNSIGNED INTEGER)),0) as VALOR from entradas_documentos_judiciales WHERE substring(nro_mesa_entrada,1,4) = 'AUTO';", NroMesaEntrada.class);

            NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

            doc.setNroMesaEntrada("AUTO-" + String.valueOf(cod.getCodigo() + 1));

            //entradasDocumentosJudicialesController.setSelected(doc);
            //entradasDocumentosJudicialesController.saveNew(null);
            getSelected().setEntradaDocumento(doc);

            super.saveNew(null);

            boolean guardado = false;
            if (file != null) {
                if (file.getContent().length > 0) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String nombreArchivo = simpleDateFormat.format(fecha);
                    nombreArchivo += "_" + getSelected().getId() + "_" + getSelected().getId() + ".pdf";

                    ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).getSingleResult();

                    guardado = guardarActuacion(file, nombreArchivo, fecha, getSelected(), null, personaUsuario, hash, tipoActuacion, fojas, true, true, personaUsuario.getNombresApellidos(), personaUsuario.getCi() == null ? "" : personaUsuario.getCi());
                }
            }

            if (esPrimerEscrito && guardado) {
                actuacionPadre = expActuacionesController.getSelected();
            }

            /*
            if (getSelected().getHechos() != null) {
                if (!"".equals(getSelected().getHechos())) {
                    guardarActuacion(getSelected().getHechos(), fecha, getSelected(), personaUsuario, listaPersonasOrigen, abogadoOrigen, Constantes.TIPO_ACTUACION_HECHO);
                }
            }
             */
            if (!this.isErrorPersistencia()) {

                for (Personas per : listaPersonas) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                        /*
                        Personas perso = null;
                        try{
                            perso = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", per.getId()).getSingleResult();
                        }catch(Exception e){
                            
                        }
                        
                        if(perso == null){           
                            personasController.setSelected(per);
                            personasController.saveNew(null);
                        }
                         */

                        PersonasPorDocumentosJudiciales perDoc = personasPorDocumentosJudicialesController.prepareCreate(null);
                        perDoc.setPersona(per);
                        perDoc.setDocumentoJudicial(getSelected());
                        perDoc.setEmpresa(usuario.getEmpresa());
                        perDoc.setFechaHoraAlta(fecha);
                        perDoc.setFechaHoraUltimoEstado(fecha);
                        perDoc.setUsuarioAlta(usuario);
                        perDoc.setUsuarioUltimoEstado(usuario);
                        perDoc.setTipoExpedienteAnterior(per.isTipoExpedienteAnterior());
                        perDoc.setEstado(new Estados("AC"));
                        personasPorDocumentosJudicialesController.setSelected(perDoc);
                        personasPorDocumentosJudicialesController.saveNew(null);
                    }
                }

                if (!this.isErrorPersistencia()) {
                    if (Constantes.SI.equals(pantalla)) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombreVariable, getSelected());
                    }
                }

                if (!listaPersonasOrigen.isEmpty()) {

                    for (Personas per : listaPersonasOrigen) {
                        // Agregar partes

                        if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                            Personas perso = null;
                            try {
                                perso = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", per.getId()).getSingleResult();
                            } catch (Exception e) {

                            }

                            if (perso == null) {
                                personasController.setSelected(per);
                                personasController.saveNew(null);
                            }

                            ExpPartesPorDocumentosJudiciales parte = partesPorDocumentosJudicialesController.prepareCreate(null);

                            parte.setDocumentoJudicial(getSelected());
                            parte.setEmpresa(usuario.getEmpresa());
                            parte.setEstado(new Estados(Constantes.ESTADO_USUARIO_AC));
                            parte.setFechaHoraAlta(fecha);
                            parte.setPersonaAlta(personaUsuario);
                            parte.setFechaHoraUltimoEstado(fecha);
                            parte.setPersonaUltimoEstado(personaUsuario);
                            parte.setPersona(per);

                            ExpTiposParte tipoParte = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ACUSADOR).getSingleResult();

                            parte.setTipoParte(tipoParte);

                            ExpPartesPorDocumentosJudicialesPK pk = new ExpPartesPorDocumentosJudicialesPK(parte.getPersona().getId(), parte.getDocumentoJudicial().getId());
                            parte.setPartesPorDocumentosJudicialesPK(pk);

                            partesPorDocumentosJudicialesController.setSelected(parte);
                            partesPorDocumentosJudicialesController.saveNew(null);
                        }
                    }
                }

                if (abogadoOrigen != null) {
                    // Agregar partes
                    ExpPartesPorDocumentosJudiciales parte2 = partesPorDocumentosJudicialesController.prepareCreate(null);

                    parte2.setDocumentoJudicial(getSelected());
                    parte2.setEmpresa(usuario.getEmpresa());
                    parte2.setEstado(new Estados(Constantes.ESTADO_USUARIO_AC));
                    parte2.setFechaHoraAlta(fecha);
                    parte2.setPersonaAlta(personaUsuario);
                    parte2.setFechaHoraUltimoEstado(fecha);
                    parte2.setPersonaUltimoEstado(personaUsuario);
                    parte2.setPersona(abogadoOrigen);

                    ExpTiposParte tipoParte2 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ABOGADO_ACUSADOR).getSingleResult();

                    parte2.setTipoParte(tipoParte2);

                    ExpPartesPorDocumentosJudicialesPK pk2 = new ExpPartesPorDocumentosJudicialesPK(parte2.getPersona().getId(), parte2.getDocumentoJudicial().getId());
                    parte2.setPartesPorDocumentosJudicialesPK(pk2);

                    partesPorDocumentosJudicialesController.setSelected(parte2);
                    partesPorDocumentosJudicialesController.saveNew(null);
                }

                if (observacion != null) {
                    if (!"".equals(observacion)) {

                        ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                        obs.setUsuarioAlta(usuario);
                        obs.setUsuarioUltimoEstado(usuario);
                        obs.setFechaHoraAlta(fecha);
                        obs.setFechaHoraUltimoEstado(fecha);
                        obs.setEmpresa(usuario.getEmpresa());
                        obs.setObservacion(observacion);
                        obs.setDocumentoJudicial(getSelected());
                        obs.setVisible(true);
                        obs.setPersonaAlta(personaUsuario);
                        obs.setPersonaUltimoEstado(personaUsuario);
                        obs.setPersonaVisible(personaUsuario);
                        obs.setFechaHoraVisible(fecha);
                        obs.setActuacion(expActuacionesController.getSelected());

                        obsController.setSelected(obs);
                        obsController.saveNew(null);

                        getSelected().setObservacionDocumentoJudicial(obs);
                        getSelected().setFechaUltimaObservacion(fecha);
                        getSelected().setUsuarioUltimaObservacion(usuario);
                        getSelected().setPersonaUltimaObservacion(personaUsuario);
                        getSelected().setUltimaObservacion(observacion);

                        super.save(null);
                    }

                }

                if (estadoProcesal != null) {

                    switch (getSelected().getTipoExpediente().getId()) {
                        case Constantes.TIPO_EXPEDIENTE_ACUSACION:
                            estadoProcesal = "Presentación de Acusación";
                            break;
                        case Constantes.TIPO_EXPEDIENTE_DENUNCIA:
                            estadoProcesal = "Presentación de Denuncia";
                            break;
                        case Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR:
                            estadoProcesal = "Investigación Preliminar";
                            break;
                        case Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO:
                            estadoProcesal = "Pedido de Desafuero";
                            break;
                        default:
                            break;
                    }
                }

                if (estadoProcesal != null) {
                    if (!"".equals(estadoProcesal)) {
                        EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                        estadoProc.setPersonaAlta(personaUsuario);
                        estadoProc.setPersonaUltimoEstado(personaUsuario);
                        estadoProc.setUsuarioAlta(usuario);
                        estadoProc.setUsuarioUltimoEstado(usuario);
                        estadoProc.setFechaHoraAlta(fecha);
                        estadoProc.setFechaHoraUltimoEstado(fecha);
                        estadoProc.setEmpresa(usuario.getEmpresa());
                        estadoProc.setEstadoProcesal(estadoProcesal);
                        estadoProc.setDocumentoJudicial(getSelected());
                        estadoProc.setVisible(true);
                        estadoProc.setPersonaVisible(personaUsuario);
                        estadoProc.setFechaHoraVisible(fecha);
                        estadoProc.setActuacion(expActuacionesController.getSelected());

                        estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                        estadosProcesalesDocumentosJudicialesController.saveNew(null);

                        getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                        getSelected().setFechaHoraEstadoProcesal(fecha);
                        getSelected().setUsuarioEstadoProcesal(usuario);
                        getSelected().setPersonaUltimoEstadoProcesal(personaUsuario);
                        getSelected().setEstadoProcesal(estadoProcesal);

                        super.save(null);
                    }
                }

            }

            fechaDesde = ejbFacade.getSystemDateOnly();
            fechaHasta = ejbFacade.getSystemDateOnly();

            if (guardado) {

                String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                        + "     Se ha hecho nueva una presentación del tipo " + expActuacionesController.getSelected().getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);

                /*
                List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_FUNCIONARIO_JEM).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

                for (Personas pers : listaPer) {

                    texto = "<p>Hola " + pers.getNombresApellidos() + "<br> "
                            + "     Una nueva en la causa fue ingresada con nro causa " + getSelected().getCausa() + " y caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);
                }
                 */
            }

            buscar();
            //buscarPorFechaAlta();

            //buscarActuaciones();
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('ExpAcusacionesOtroArchivoDialog').show();");

        }
    }
    
    public void saveNewExpedienteViejo() {

        if (getSelected() != null) {
            try {
                getSelected().setCausa(generarNroCausa());
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("No se pudo generar nro causa");
                return;
            }

            if (!validarSaveNew()) {
                return;
            }

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            String hash = "";
            try {
                hash = Utils.generarHash(fecha, personaUsuario.getId());;
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                return;
            }

            getSelected().setPersonaAlta(personaUsuario);
            getSelected().setFolios("");
            getSelected().setFechaPresentacion(generarFechaPresentacion(fecha));
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setDescripcionMesaEntrada(getSelected().getCaratula());
            //getSelected().setUltimaObservacion(ultimaObservacion);
            getSelected().setCanalEntradaDocumentoJudicial(canal);
            getSelected().setTipoDocumentoJudicial(tipoDoc);
            getSelected().setResponsable(usuario);
            getSelected().setDepartamento(departamento);

            //  getSelected().setMostrarWeb("SI");
            EstadosDocumento estado = estadoController.prepareCreate(null);
            estado.setCodigo("0");

            getSelected().setEstado(estado);

            EntradasDocumentosJudiciales doc = entradasDocumentosJudicialesController.prepareCreate(null);

            doc.setFechaHoraUltimoEstado(fecha);
            doc.setUsuarioUltimoEstado(usuario);
            doc.setFechaHoraAlta(fecha);
            doc.setUsuarioAlta(usuario);
            doc.setEmpresa(usuario.getEmpresa());
            doc.setRecibidoPor(usuario);
            doc.setEntregadoPor(entradaDocumentoJudicial.getEntregadoPor());
            doc.setTelefono(entradaDocumentoJudicial.getTelefono());
            doc.setNroCedulaRuc(entradaDocumentoJudicial.getNroCedulaRuc());
            javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(CONVERT(substring(nro_mesa_entrada,6),UNSIGNED INTEGER)),0) as VALOR from entradas_documentos_judiciales WHERE substring(nro_mesa_entrada,1,4) = 'AUTO';", NroMesaEntrada.class);

            NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

            doc.setNroMesaEntrada("AUTO-" + String.valueOf(cod.getCodigo() + 1));

            //entradasDocumentosJudicialesController.setSelected(doc);
            //entradasDocumentosJudicialesController.saveNew(null);
            getSelected().setEntradaDocumento(doc);

            super.saveNew(null);

            boolean guardado = false;
            if (file != null) {
                if (file.getContent().length > 0) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String nombreArchivo = simpleDateFormat.format(fecha);
                    nombreArchivo += "_" + getSelected().getId() + "_" + getSelected().getId() + ".pdf";

                    ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).getSingleResult();

                    guardado = guardarActuacion(file, nombreArchivo, fecha, getSelected(), null, personaUsuario, hash, tipoActuacion, fojas, true, true, personaUsuario.getNombresApellidos(), personaUsuario.getCi() == null ? "" : personaUsuario.getCi());
                }
            }

            if (esPrimerEscrito && guardado) {
                actuacionPadre = expActuacionesController.getSelected();
            }

            /*
            if (getSelected().getHechos() != null) {
                if (!"".equals(getSelected().getHechos())) {
                    guardarActuacion(getSelected().getHechos(), fecha, getSelected(), personaUsuario, listaPersonasOrigen, abogadoOrigen, Constantes.TIPO_ACTUACION_HECHO);
                }
            }
             */
            if (!this.isErrorPersistencia()) {

                for (Personas per : listaPersonas) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                        /*
                        Personas perso = null;
                        try{
                            perso = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", per.getId()).getSingleResult();
                        }catch(Exception e){
                            
                        }
                        
                        if(perso == null){           
                            personasController.setSelected(per);
                            personasController.saveNew(null);
                        }
                         */

                        PersonasPorDocumentosJudiciales perDoc = personasPorDocumentosJudicialesController.prepareCreate(null);
                        perDoc.setPersona(per);
                        perDoc.setDocumentoJudicial(getSelected());
                        perDoc.setEmpresa(usuario.getEmpresa());
                        perDoc.setFechaHoraAlta(fecha);
                        perDoc.setFechaHoraUltimoEstado(fecha);
                        perDoc.setUsuarioAlta(usuario);
                        perDoc.setUsuarioUltimoEstado(usuario);
                        perDoc.setTipoExpedienteAnterior(per.isTipoExpedienteAnterior());
                        perDoc.setEstado(new Estados("AC"));
                        personasPorDocumentosJudicialesController.setSelected(perDoc);
                        personasPorDocumentosJudicialesController.saveNew(null);
                    }
                }

                if (!this.isErrorPersistencia()) {
                    if (Constantes.SI.equals(pantalla)) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombreVariable, getSelected());
                    }
                }

                if (!listaPersonasOrigen.isEmpty()) {

                    for (Personas per : listaPersonasOrigen) {
                        // Agregar partes

                        if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                            Personas perso = null;
                            try {
                                perso = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", per.getId()).getSingleResult();
                            } catch (Exception e) {

                            }

                            if (perso == null) {
                                personasController.setSelected(per);
                                personasController.saveNew(null);
                            }

                            ExpPartesPorDocumentosJudiciales parte = partesPorDocumentosJudicialesController.prepareCreate(null);

                            parte.setDocumentoJudicial(getSelected());
                            parte.setEmpresa(usuario.getEmpresa());
                            parte.setEstado(new Estados(Constantes.ESTADO_USUARIO_AC));
                            parte.setFechaHoraAlta(fecha);
                            parte.setPersonaAlta(personaUsuario);
                            parte.setFechaHoraUltimoEstado(fecha);
                            parte.setPersonaUltimoEstado(personaUsuario);
                            parte.setPersona(per);

                            ExpTiposParte tipoParte = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ACUSADOR).getSingleResult();

                            parte.setTipoParte(tipoParte);

                            ExpPartesPorDocumentosJudicialesPK pk = new ExpPartesPorDocumentosJudicialesPK(parte.getPersona().getId(), parte.getDocumentoJudicial().getId());
                            parte.setPartesPorDocumentosJudicialesPK(pk);

                            partesPorDocumentosJudicialesController.setSelected(parte);
                            partesPorDocumentosJudicialesController.saveNew(null);
                        }
                    }
                }

                if (abogadoOrigen != null) {
                    // Agregar partes
                    ExpPartesPorDocumentosJudiciales parte2 = partesPorDocumentosJudicialesController.prepareCreate(null);

                    parte2.setDocumentoJudicial(getSelected());
                    parte2.setEmpresa(usuario.getEmpresa());
                    parte2.setEstado(new Estados(Constantes.ESTADO_USUARIO_AC));
                    parte2.setFechaHoraAlta(fecha);
                    parte2.setPersonaAlta(personaUsuario);
                    parte2.setFechaHoraUltimoEstado(fecha);
                    parte2.setPersonaUltimoEstado(personaUsuario);
                    parte2.setPersona(abogadoOrigen);

                    ExpTiposParte tipoParte2 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ABOGADO_ACUSADOR).getSingleResult();

                    parte2.setTipoParte(tipoParte2);

                    ExpPartesPorDocumentosJudicialesPK pk2 = new ExpPartesPorDocumentosJudicialesPK(parte2.getPersona().getId(), parte2.getDocumentoJudicial().getId());
                    parte2.setPartesPorDocumentosJudicialesPK(pk2);

                    partesPorDocumentosJudicialesController.setSelected(parte2);
                    partesPorDocumentosJudicialesController.saveNew(null);
                }

                if (observacion != null) {
                    if (!"".equals(observacion)) {

                        ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                        obs.setUsuarioAlta(usuario);
                        obs.setUsuarioUltimoEstado(usuario);
                        obs.setFechaHoraAlta(fecha);
                        obs.setFechaHoraUltimoEstado(fecha);
                        obs.setEmpresa(usuario.getEmpresa());
                        obs.setObservacion(observacion);
                        obs.setDocumentoJudicial(getSelected());
                        obs.setVisible(true);
                        obs.setPersonaAlta(personaUsuario);
                        obs.setPersonaUltimoEstado(personaUsuario);
                        obs.setPersonaVisible(personaUsuario);
                        obs.setFechaHoraVisible(fecha);
                        obs.setActuacion(expActuacionesController.getSelected());

                        obsController.setSelected(obs);
                        obsController.saveNew(null);

                        getSelected().setObservacionDocumentoJudicial(obs);
                        getSelected().setFechaUltimaObservacion(fecha);
                        getSelected().setUsuarioUltimaObservacion(usuario);
                        getSelected().setPersonaUltimaObservacion(personaUsuario);
                        getSelected().setUltimaObservacion(observacion);

                        super.save(null);
                    }

                }

                if (estadoProcesal != null) {

                    switch (getSelected().getTipoExpediente().getId()) {
                        case Constantes.TIPO_EXPEDIENTE_ACUSACION:
                            estadoProcesal = "Presentación de Acusación";
                            break;
                        case Constantes.TIPO_EXPEDIENTE_DENUNCIA:
                            estadoProcesal = "Presentación de Denuncia";
                            break;
                        case Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR:
                            estadoProcesal = "Investigación Preliminar";
                            break;
                        case Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO:
                            estadoProcesal = "Pedido de Desafuero";
                            break;
                        default:
                            break;
                    }
                }

                if (estadoProcesal != null) {
                    if (!"".equals(estadoProcesal)) {
                        EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                        estadoProc.setPersonaAlta(personaUsuario);
                        estadoProc.setPersonaUltimoEstado(personaUsuario);
                        estadoProc.setUsuarioAlta(usuario);
                        estadoProc.setUsuarioUltimoEstado(usuario);
                        estadoProc.setFechaHoraAlta(fecha);
                        estadoProc.setFechaHoraUltimoEstado(fecha);
                        estadoProc.setEmpresa(usuario.getEmpresa());
                        estadoProc.setEstadoProcesal(estadoProcesal);
                        estadoProc.setDocumentoJudicial(getSelected());
                        estadoProc.setVisible(true);
                        estadoProc.setPersonaVisible(personaUsuario);
                        estadoProc.setFechaHoraVisible(fecha);
                        estadoProc.setActuacion(expActuacionesController.getSelected());

                        estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                        estadosProcesalesDocumentosJudicialesController.saveNew(null);

                        getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                        getSelected().setFechaHoraEstadoProcesal(fecha);
                        getSelected().setUsuarioEstadoProcesal(usuario);
                        getSelected().setPersonaUltimoEstadoProcesal(personaUsuario);
                        getSelected().setEstadoProcesal(estadoProcesal);

                        super.save(null);
                    }
                }

            }

            fechaDesde = ejbFacade.getSystemDateOnly();
            fechaHasta = ejbFacade.getSystemDateOnly();

            if (guardado) {

                String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                        + "     Se ha hecho nueva una presentación del tipo " + expActuacionesController.getSelected().getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);

                /*
                List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_FUNCIONARIO_JEM).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

                for (Personas pers : listaPer) {

                    texto = "<p>Hola " + pers.getNombresApellidos() + "<br> "
                            + "     Una nueva en la causa fue ingresada con nro causa " + getSelected().getCausa() + " y caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);
                }
                 */
            }

            buscar();
            //buscarPorFechaAlta();

            //buscarActuaciones();
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('ExpAcusacionesOtroArchivoDialog').show();");

        }
    }

    public void saveNewResolucion() {
        if (resolucion != null) {

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un documento");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }

            Date fechaActual = ejbFacade.getSystemDate();

            resolucion.setFechaHoraUltimoEstado(fechaActual);
            resolucion.setFechaHoraAlta(fechaActual);
            if (usuario != null) {
                resolucion.setEmpresa(usuario.getEmpresa());
            } else {
                resolucion.setEmpresa(personaUsuario.getEmpresa());
            }

            resolucion.setUsuarioUltimoEstado(usuario);
            resolucion.setUsuarioAlta(usuario);
            resolucion.setPersonaUltimoEstado(personaUsuario);
            resolucion.setPersonaAlta(personaUsuario);

            resolucion.setDocumentoJudicial(getSelected());
            resolucion.setFirmado(false);

            resolucion.setMostrarWeb("NO");

            resolucion.setCanalEntradaDocumentoJudicial(canal);

            if (resolucion.getNroResolucion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_nroResolucion"));
                return;
            } else if ("".equals(resolucion.getNroResolucion())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_nroResolucion"));
                return;
            }

            /* #DOCUMENTO JUDICIAL OPCIONAL
            if (getSelected().getDocumentoJudicial() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_documentoJudicial"));
                return;
            }

            if (tipoExpediente == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_tipoExpediente"));
                return;
            }

            if (listaPersonas == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_persona"));
                return;
            }
             */
            if (resolucion.getTipoResolucion() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_tipoResolucion"));
                return;
            }

            if (resolucion.getFecha() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_fecha"));
                return;
            }

            if (resolucion.getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_caratula"));
                return;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_caratula"));
                return;
            }

            if (listaPersonas != null) {
                for (ResuelvePorResolucionesPorPersonas res : listaPersonasResuelve) {
                    if (res.getResuelve() == null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_resuelve"));
                        return;
                    }
                }
            }

            resolucionesController.setSelected(resolucion);
            resolucionesController.saveNew(null);

            boolean guardado = false;
            String nombreArchivo = "";

            ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();

            if (file != null) {
                if (file.getContent().length > 0) {

                    String hash = "";
                    try {
                        hash = Utils.generarHash(fechaActual, personaUsuario.getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                        return;
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    nombreArchivo = simpleDateFormat.format(fechaActual);
                    nombreArchivo += "_" + getSelected().getId() + "_" + getSelected().getId() + ".pdf";

                    guardado = guardarActuacion(file, nombreArchivo, fechaActual, getSelected(), resolucion, personaUsuario, hash, tipoActuacion, resolucion.getFojas(), visible, false, personaUsuario.getNombresApellidos(), personaUsuario.getCi() == null ? "" : personaUsuario.getCi());


                    /*
                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputstream());
                    } catch (IOException ex) {
                    }
                    // getSelected().setDocumento(bytes);
                    ResolucionesEscaneadas docEsc = null;

                    if (getSelected().getResolucionEscaneada() == null) {
                        docEsc = resolucionesEscaneadasController.prepareCreate(null);

                        docEsc.setEmpresa(usu.getEmpresa());
                        docEsc.setDocumento(bytes);

                        resolucionesEscaneadasController.setSelected(docEsc);
                        resolucionesEscaneadasController.saveNew(null);

                        getSelected().setResolucionEscaneada(resolucionesEscaneadasController.getSelected().getId());

                    } else {
                        docEsc = this.ejbFacade.getEntityManager().createNamedQuery("ResolucionesEscaneadas.findById", ResolucionesEscaneadas.class).setParameter("id", getSelected().getResolucionEscaneada()).getSingleResult();
                        docEsc.setDocumento(bytes);
                        resolucionesEscaneadasController.setSelected(docEsc);
                        resolucionesEscaneadasController.save(null);
                    }
                     */
                }
            }

            if (guardado) {
                resolucion.setArchivo(nombreArchivo);
                resolucionesController.setSelected(resolucion);
                resolucionesController.save(null);
            }

            boolean guardar = false;

            if (listaPersonasResuelve != null) {
                ResuelvePorResolucionesPorPersonas res = null;
                for (int i = 0; i < listaPersonasResuelve.size(); i++) {
                    res = new ResuelvePorResolucionesPorPersonas();

                    res.setResolucion(resolucion);
                    res.setEmpresa(usuario.getEmpresa());
                    res.setFechaHoraAlta(fechaActual);
                    res.setFechaHoraUltimoEstado(fechaActual);
                    res.setUsuarioAlta(usuario);
                    res.setUsuarioUltimoEstado(usuario);
                    res.setEstado(new Estados("AC"));
                    res.setPersona(listaPersonasResuelve.get(i).getPersona());
                    res.setResuelve(listaPersonasResuelve.get(i).getResuelve());
                    resuelvePorResolucionesPorPersonasController.setSelected(res);
                    resuelvePorResolucionesPorPersonasController.saveNew(null);
                }
            }

            String estadoProcesalFinal = "";
            if (estadoProcesal != null) {
                if (!"".equals(estadoProcesal)) {
                    estadoProcesalFinal = estadoProcesal;
                }
            }

            if (actuacion.isVisible() && "".equals(estadoProcesalFinal)) {
                estadoProcesalFinal = tipoActuacion.getDescripcion();
            }

            if (!"".equals(estadoProcesalFinal)) {
                EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();

                estadoProc.setPersonaAlta(personaUsuario);
                estadoProc.setPersonaUltimoEstado(personaUsuario);
                estadoProc.setFechaHoraAlta(fechaActual);
                estadoProc.setFechaHoraUltimoEstado(fechaActual);
                estadoProc.setEstadoProcesal(estadoProcesalFinal);
                estadoProc.setDocumentoJudicial(getSelected());
                estadoProc.setEmpresa(personaUsuario.getEmpresa());
                estadoProc.setUsuarioAlta(usuario);
                estadoProc.setUsuarioUltimoEstado(usuario);
                // estadoProc.setVisible(expActuacionesController.getSelected().isVisible());
                estadoProc.setVisible(true);
                estadoProc.setActuacion(expActuacionesController.getSelected());
                //if(expActuacionesController.getSelected().isVisible()){
                estadoProc.setPersonaVisible(personaUsuario);
                estadoProc.setFechaHoraVisible(fechaActual);
                //}

                estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                estadosProcesalesDocumentosJudicialesController.saveNew(null);

                getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                getSelected().setFechaHoraEstadoProcesal(fechaActual);
                getSelected().setUsuarioEstadoProcesal(usuario);
                getSelected().setPersonaUltimoEstadoProcesal(personaUsuario);
                getSelected().setEstadoProcesal(estadoProcesalFinal);

                super.save(null);
            }

            if (observacion != null) {
                if (!"".equals(observacion)) {
                    ObservacionesDocumentosJudiciales obs = obsController.prepareCreate(null);

                    obs.setUsuarioAlta(usuario);
                    obs.setUsuarioUltimoEstado(usuario);
                    obs.setFechaHoraAlta(fechaActual);
                    obs.setFechaHoraUltimoEstado(fechaActual);
                    obs.setEmpresa(usuario.getEmpresa());
                    obs.setObservacion(observacion);
                    obs.setDocumentoJudicial(getSelected());
                    obs.setPersonaAlta(personaUsuario);
                    obs.setPersonaUltimoEstado(personaUsuario);
                    // obs.setVisible(expActuacionesController.getSelected().isVisible());
                    obs.setVisible(true);
                    obs.setActuacion(expActuacionesController.getSelected());

                    //if(expActuacionesController.getSelected().isVisible()){
                    obs.setPersonaVisible(personaUsuario);
                    obs.setFechaHoraVisible(fechaActual);
                    //}

                    obsController.setSelected(obs);
                    obsController.saveNew(null);

                    getSelected().setObservacionDocumentoJudicial(obs);
                    getSelected().setFechaUltimaObservacion(fechaActual);
                    getSelected().setUsuarioUltimaObservacion(usuario);
                    getSelected().setPersonaUltimaObservacion(personaUsuario);
                    getSelected().setUltimaObservacion(observacion);

                    super.save(null);
                }
            }

            buscarActuaciones();

            String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                    + "     Ha recibido una presentación del tipo " + tipoActuacion + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                    + getSelected().getCaratulaString()
                    + "<br><br>"
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);
        }
    }

    public boolean validarSave() {
        if (getSelected().getFechaPresentacion() == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_fechaPresentacion"));
            return false;
        }

        // if (getSelected().getTipoExpediente() == null) {
        if (tipoExpediente == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_tipoExpediente"));
            return false;
        }
        /*
        if (getSelected().getCausa() == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_nroCausa"));
            return false;
        } else if ("".equals(getSelected().getCausa())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_nroCausa"));
            return false;
        } else {
            if (causa != null) {
                if (!causa.equals(getSelected().getCausa())) {
                    List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", getSelected().getCausa()).getResultList();
                    if (!listaDocs.isEmpty()) {
                        JsfUtil.addErrorMessage("Ya existe una causa con nro " + getSelected().getCausa());
                        return false;
                    }
                }
            }
        }}
         */

        // if (getSelected().getCaratula() == null) {
        if (caratula == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_caratula"));
            return false;
        } else if ("".equals(caratula)) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_caratula"));
            return false;
        }

        if (getSelected().getMotivoProceso() == null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_motivoProceso"));
            return false;
        } else if ("".equals(getSelected().getMotivoProceso())) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_motivoProceso"));
            return false;
        }
        /*
                if (getSelected().getEstadoProcesal() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_estadoProcesal"));
                    guardar = false;
                } else if ("".equals(getSelected().getEstadoProcesal())) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_estadoProcesal"));
                    guardar = false;
                }
         */
 /*
                if (getSelected().getPersona() == null) {
                    JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDocumentosJudicialesHelpText_persona"));
                    return;
                }
         */

        return true;
    }

}
