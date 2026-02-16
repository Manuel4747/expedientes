package py.gov.jem.expedientes.controllers;

import com.itextpdf.barcodes.BarcodeQRCode;
import static com.itextpdf.forms.xfdf.XfdfConstants.INTENT;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.RootRenderer;
import com.itextpdf.pdfa.PdfADocument;
import com.lowagie.text.DocumentException;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.DocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.persistence.TemporalType;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.Departamentos;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.EntradasDocumentosJudiciales;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.EstadosProcesalesDocumentosJudiciales;
import py.gov.jem.expedientes.models.EstadosProceso;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpCategoriasActuacion;
import py.gov.jem.expedientes.models.ExpEstadosNotificacion;
import py.gov.jem.expedientes.models.ExpFormatos;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.ExpNotificaciones;
import py.gov.jem.expedientes.models.ExpObjetosActuacion;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesPK;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
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
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.FlowChartConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.endpoint.RectangleEndPoint;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;
import org.xhtmlrenderer.extend.FontResolver;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;
import py.gov.jem.expedientes.datasource.Elemento;
import py.gov.jem.expedientes.datasource.Metadatos;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.ExpCambiosEstadoActuacion;
import py.gov.jem.expedientes.models.ExpCambiosTextoActuacion;
import py.gov.jem.expedientes.models.ExpEstadosActuacion;
import py.gov.jem.expedientes.models.ExpEstadosActuacionPorRoles;
import py.gov.jem.expedientes.models.ExpFeriados;
import py.gov.jem.expedientes.models.TiposResolucion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;
import py.gov.jem.expedientes.datasource.DatosIntervinientes;
import py.gov.jem.expedientes.datasource.DatosObjetosCausa;
import py.gov.jem.expedientes.datasource.DatosPartes;
import py.gov.jem.expedientes.datasource.RespuestaConsultaActuacionesCausa;
import py.gov.jem.expedientes.datasource.RespuestaConsultaCausas;
import py.gov.jem.expedientes.datasource.RespuestaConsultaPdf;
import py.gov.jem.expedientes.datasource.RespuestaEnviarCorte;
import py.gov.jem.expedientes.models.DepartamentosPersona;
import py.gov.jem.expedientes.models.DocumentosJudicialesCorte;
import py.gov.jem.expedientes.models.ExpActuacionesCorte;
import py.gov.jem.expedientes.models.ExpConexiones;
import py.gov.jem.expedientes.models.ExpDocumentosJudicialesCortePorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpDocumentosJudicialesCortePorDocumentosJudicialesPK;
import py.gov.jem.expedientes.models.ExpDomiciliosPorPartesPorDocumentosJudicialesCorte;
import py.gov.jem.expedientes.models.ExpEnviosCorte;
import py.gov.jem.expedientes.models.ExpEnviosEmail;
import py.gov.jem.expedientes.models.ExpEstadosCivilCorte;
import py.gov.jem.expedientes.models.ExpEstadosCorte;
import py.gov.jem.expedientes.models.ExpEstadosProcesoExpedienteElectronico;
import py.gov.jem.expedientes.models.ExpSexosCorte;
import py.gov.jem.expedientes.models.ExpMaterias;
import py.gov.jem.expedientes.models.ExpGruposProcesoCorte;
import py.gov.jem.expedientes.models.ExpIntervinientesPorDocumentosJudicialesCorte;
import py.gov.jem.expedientes.models.ExpObjetosCorte;
import py.gov.jem.expedientes.models.ExpObjetosPorDocumentosJudicialesCorte;
import py.gov.jem.expedientes.models.ExpObjetosPorDocumentosJudicialesCortePK;
import py.gov.jem.expedientes.models.ExpOcupacionesCorte;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesCorte;
import py.gov.jem.expedientes.models.ExpPersonasAcusacion;
import py.gov.jem.expedientes.models.ExpPersonasAcusacionPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpPersonasHabilitadasPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpPersonasInhibidasPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpProcesosCorte;
import py.gov.jem.expedientes.models.ExpProcesosDocumentoJudicialCorte;
import py.gov.jem.expedientes.models.ExpProfesionalesPorPartesPorDocumentosJudicialesCorte;
import py.gov.jem.expedientes.models.ExpTiposDocumentoCorte;
import py.gov.jem.expedientes.models.ExpTiposIntervinienteCorte;
import py.gov.jem.expedientes.models.ExpTiposPersonaCorte;
import py.gov.jem.expedientes.models.ExpTiposParteCorte;

@Named(value = "documentosJudicialesPorSecretariaController")
@ViewScoped
public class DocumentosJudicialesPorSecretariaController extends AbstractController<DocumentosJudiciales> {

    @Inject
    UserTransaction ut;
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
    private ExpDocumentosJudicialesCortePorDocumentosJudicialesController documentosJudicialesCortePorDocumentosJudicialesController;
    @Inject
    private ExpPartesPorDocumentosJudicialesController partesPorDocumentosJudicialesController;
    @Inject
    private ExpPersonasInhibidasPorDocumentosJudicialesController personasInhibidasPorDocumentosJudicialesController;
    @Inject
    private ExpPersonasHabilitadasPorDocumentosJudicialesController personasHabilitadasPorDocumentosJudicialesController;
    @Inject
    private ExpActuacionesController expActuacionesController;
    @Inject
    private ExpHistActuacionesController expHistActuacionesController;
    @Inject
    private ExpHistPersonasFirmantesPorActuacionesController expHistPersonasFirmantesPorActuacionesController;
    @Inject
    private ParametrosSistemaController parametrosSistemaController;
    @Inject
    private FirmasController firmasController;
    @Inject
    private HistFirmasController histFirmasController;
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
    @Inject
    private ExpTiposActuacionController tipoActuacionController;
    @Inject
    private ExpCambiosEstadoActuacionController expCambiosEstadoActuacionController;
    @Inject
    private ExpCambiosTextoActuacionController expCambiosTextoActuacionController;
    @Inject
    private DocumentosJudicialesCorteController documentoJudicialCorteController;
    @Inject
    private ExpActuacionesCorteController expActuacionesCorteController;
    @Inject
    private ExpObjetosCorteController objetosCorteController;
    @Inject
    private ExpSexosCorteController sexosCorteController;
    @Inject
    private ExpTiposPersonaCorteController tiposPersonaCorteController;
    @Inject
    private ExpTiposIntervinienteCorteController tiposIntervinienteCorteController;
    @Inject
    private ExpTiposParteCorteController tiposParteCorteController;
    @Inject
    private ExpTiposDocumentoCorteController tiposDocumentoCorteController;
    @Inject
    private ExpEstadosCivilCorteController estadosCivilCorteController;
    @Inject
    private ExpEstadosCorteController estadosCorteController;
    @Inject
    private ExpOcupacionesCorteController ocupacionesCorteController;
    @Inject
    private ExpObjetosPorDocumentosJudicialesCorteController objetosPorDocumentosJudicialesCorteController;
    @Inject
    private ExpIntervinientesPorDocumentosJudicialesCorteController intervinientesPorDocumentosJudicialesCorteController;
    @Inject
    private ExpPartesPorDocumentosJudicialesCorteController partesPorDocumentosJudicialesCorteController;
    @Inject
    private ExpDomiciliosPorPartesPorDocumentosJudicialesCorteController domiciliosPorPartesPorDocumentosJudicialesCorteController;
    @Inject
    private ExpProfesionalesPorPartesPorDocumentosJudicialesCorteController profesionalesPorPartesPorDocumentosJudicialesCorteController;
    @Inject
    private ExpGruposProcesoCorteController expGruposProcesoCorteController;
    @Inject
    private ExpProcesosCorteController expProcesosCorteController;
    @Inject
    private ExpProcesosDocumentoJudicialCorteController expProcesosDocumentoJudicialCorteController;
    @Inject
    private ExpEnviosCorteController enviosCorteController;
    @Inject
    private ExpEnviosEmailController enviosEmailController;

    private EntradasDocumentosJudiciales entradaDocumentoJudicial;
    private String nuevaCausa;
    private String nroCausa;
    private String nroCausaExp;
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
    private List<ExpPersonasAcusacion> listaPersonasInformadas;
    private List<ResuelvePorResolucionesPorPersonas> listaPersonasResuelve;
    private Personas personaSelected;
    private ExpPersonasAsociadas personaFirmanteSelected;
    private Personas revisionSelected;
    private Personas parteSelected;
    private Personas inhibidoSelected;
    private Personas restringidoSelected;
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
    private Personas inhibido;
    private List<Personas> listaInhibidosAlta;
    private Personas restringido;
    private List<Personas> listaRestringidosAlta;
    private List<Personas> listaPreopinantes;
    private List<Personas> listaSecretarios;
    private ExpTiposParte tipoParte;
    private String caratula;
    private String caratulaAnterior;
    private List<ExpEstadosProcesoExpedienteElectronico> estadosProcesoFiltro;
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
    private String paramPestana;
    private boolean vieneDePestanas;
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
    private List<ExpPersonasFirmantesPorActuaciones> listaEstadosFirmas;
    private List<AntecedentesRoles> listaRolesFirmantes;
    private List<Personas> listaRevisiones;
    private List<Personas> listaPartesAdmin;
    private List<Personas> listaInhibidosAdmin;
    private List<Personas> listaRestringidosAdmin;
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
    private String otro;
    private String mensajeAviso;
    private ExpPersonasFirmantesPorActuaciones selectedPersonaFirmantePorActuacion;
    private List<ExpFormatos> listaFormatos;
    private final String inicio = "#";
    private final String fin = "#";
    private List<ExpEstadosActuacion> listaEstadosActuacion;
    private boolean esRelator;
    private ExpEstadosActuacion estadoActuacion;
    private ExpEstadosActuacion estadoActuacionOriginal;
    private String textoAnterior;
    private String styleHistorial;
    private DefaultDiagramModel model;
    private String asociarCaratula;
    private Integer asociarCausa;
    private Integer asociarAno;
    private List<DepartamentosPersona> listaCircunscripcionesCorte;
    private DepartamentosPersona circunscripcionSelected;
    private List<ExpMaterias> listaMateriasCorte;
    private ExpMaterias materiaSelected;
    private List<RespuestaConsultaCausas> listaRespuestaConsultaCausas;
    private List<RespuestaConsultaCausas> listaRespuestaConsultaCausasAsociadas;
    private List<RespuestaConsultaActuacionesCausa> listaRespuestaConsultaActuacionesCausa;
    private RespuestaConsultaCausas selectedCausa;
    private String secuencia;
    private Date fechaFinal;
    private String textoFinal;
    private List<DocumentosJudicialesCorte> expedienteCorte;
    private List<ExpActuacionesCorte> listaActuacionesCorte;
    private List<ExpActuacionesCorte> listaRecursosCorte;
    private List<ExpActuacionesCorte> listaExcepcionesCorte;
    private List<ExpActuacionesCorte> listaRegulacionCorte;
    private List<ExpProcesosDocumentoJudicialCorte> listaProcesosActuacionesCorte;
    private List<ExpProcesosDocumentoJudicialCorte> listaProcesosRecursosCorte;
    private List<ExpProcesosDocumentoJudicialCorte> listaProcesosExcepcionesCorte;
    private List<ExpProcesosDocumentoJudicialCorte> listaProcesosRegulacionCorte;

    private ExpProcesosDocumentoJudicialCorte selectedProcesosActuacionesCorte;
    private ExpProcesosDocumentoJudicialCorte selectedProcesosRecursosCorte;
    private ExpProcesosDocumentoJudicialCorte selectedProcesosExcepcionesCorte;
    private ExpProcesosDocumentoJudicialCorte selectedProcesosRegulacionCorte;

    private List<ExpIntervinientesPorDocumentosJudicialesCorte> listaIntervinientesCorte;
    private List<ExpPartesPorDocumentosJudicialesCorte> listaPartesCorte;
    private List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> listaDomiciliosCorte;
    private List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> listaProfesionalesCorte;
    private ExpPartesPorDocumentosJudicialesCorte selectedParteCorte;
    private ExpIntervinientesPorDocumentosJudicialesCorte selectedIntervinienteCorte;
    private List<ExpObjetosPorDocumentosJudicialesCorte> listaObjetosCorte;
    private String filtroRadio;
    private String filtroOrden;
    private DocumentosJudicialesCorte selectedDocumentoJudicialCorte;
    private List<DocumentosJudicialesCorte> listaDocumentosJudicialesCorte;
    private Integer docId;
    private List<ExpPersonasFirmantesPorActuaciones> listaCorregirFirmantes;
    private List<ExpActuaciones> listaActuacionesDesglose;
    private List<ExpActuaciones> actuacionDesglose;

    public List<ExpActuaciones> getActuacionDesglose() {
        return actuacionDesglose;
    }

    public void setActuacionDesglose(List<ExpActuaciones> actuacionDesglose) {
        this.actuacionDesglose = actuacionDesglose;
    }

    public List<ExpActuaciones> getListaActuacionesDesglose() {
        return listaActuacionesDesglose;
    }

    public void setListaActuacionesDesglose(List<ExpActuaciones> listaActuacionesDesglose) {
        this.listaActuacionesDesglose = listaActuacionesDesglose;
    }

    public List<ExpPersonasFirmantesPorActuaciones> getListaCorregirFirmantes() {
        return listaCorregirFirmantes;
    }

    public void setListaCorregirFirmantes(List<ExpPersonasFirmantesPorActuaciones> listaCorregirFirmantes) {
        this.listaCorregirFirmantes = listaCorregirFirmantes;
    }

    public String getFiltroOrden() {
        return filtroOrden;
    }

    public void setFiltroOrden(String filtroOrden) {
        this.filtroOrden = filtroOrden;
    }

    public List<ExpPersonasAcusacion> getListaPersonasInformadas() {
        return listaPersonasInformadas;
    }

    public void setListaPersonasInformadas(List<ExpPersonasAcusacion> listaPersonasInformadas) {
        this.listaPersonasInformadas = listaPersonasInformadas;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Personas getRestringidoSelected() {
        return restringidoSelected;
    }

    public void setRestringidoSelected(Personas restringidoSelected) {
        this.restringidoSelected = restringidoSelected;
    }

    public Personas getRestringido() {
        return restringido;
    }

    public void setRestringido(Personas restringido) {
        this.restringido = restringido;
    }

    public List<Personas> getListaRestringidosAlta() {
        return listaRestringidosAlta;
    }

    public void setListaRestringidosAlta(List<Personas> listaRestringidosAlta) {
        this.listaRestringidosAlta = listaRestringidosAlta;
    }

    public List<Personas> getListaRestringidosAdmin() {
        return listaRestringidosAdmin;
    }

    public void setListaRestringidosAdmin(List<Personas> listaRestringidosAdmin) {
        this.listaRestringidosAdmin = listaRestringidosAdmin;
    }

    public Personas getInhibidoSelected() {
        return inhibidoSelected;
    }

    public void setInhibidoSelected(Personas inhibidoSelected) {
        this.inhibidoSelected = inhibidoSelected;
    }

    public Personas getInhibido() {
        return inhibido;
    }

    public void setInhibido(Personas inhibido) {
        this.inhibido = inhibido;
    }

    public List<Personas> getListaInhibidosAlta() {
        return listaInhibidosAlta;
    }

    public void setListaInhibidosAlta(List<Personas> listaInhibidosAlta) {
        this.listaInhibidosAlta = listaInhibidosAlta;
    }

    public List<DocumentosJudicialesCorte> getListaDocumentosJudicialesCorte() {
        return listaDocumentosJudicialesCorte;
    }

    public void setListaDocumentosJudicialesCorte(List<DocumentosJudicialesCorte> listaDocumentosJudicialesCorte) {
        this.listaDocumentosJudicialesCorte = listaDocumentosJudicialesCorte;
    }

    public DocumentosJudicialesCorte getSelectedDocumentoJudicialCorte() {
        return selectedDocumentoJudicialCorte;
    }

    public void setSelectedDocumentoJudicialCorte(DocumentosJudicialesCorte selectedDocumentoJudicialCorte) {
        this.selectedDocumentoJudicialCorte = selectedDocumentoJudicialCorte;
    }

    public List<RespuestaConsultaCausas> getListaRespuestaConsultaCausasAsociadas() {
        return listaRespuestaConsultaCausasAsociadas;
    }

    public void setListaRespuestaConsultaCausasAsociadas(List<RespuestaConsultaCausas> listaRespuestaConsultaCausasAsociadas) {
        this.listaRespuestaConsultaCausasAsociadas = listaRespuestaConsultaCausasAsociadas;
    }

    public ExpProcesosDocumentoJudicialCorte getSelectedProcesosActuacionesCorte() {
        return selectedProcesosActuacionesCorte;
    }

    public void setSelectedProcesosActuacionesCorte(ExpProcesosDocumentoJudicialCorte selectedProcesosActuacionesCorte) {
        this.selectedProcesosActuacionesCorte = selectedProcesosActuacionesCorte;
    }

    public ExpProcesosDocumentoJudicialCorte getSelectedProcesosRecursosCorte() {
        return selectedProcesosRecursosCorte;
    }

    public void setSelectedProcesosRecursosCorte(ExpProcesosDocumentoJudicialCorte selectedProcesosRecursosCorte) {
        this.selectedProcesosRecursosCorte = selectedProcesosRecursosCorte;
    }

    public ExpProcesosDocumentoJudicialCorte getSelectedProcesosExcepcionesCorte() {
        return selectedProcesosExcepcionesCorte;
    }

    public void setSelectedProcesosExcepcionesCorte(ExpProcesosDocumentoJudicialCorte selectedProcesosExcepcionesCorte) {
        this.selectedProcesosExcepcionesCorte = selectedProcesosExcepcionesCorte;
    }

    public ExpProcesosDocumentoJudicialCorte getSelectedProcesosRegulacionCorte() {
        return selectedProcesosRegulacionCorte;
    }

    public void setSelectedProcesosRegulacionCorte(ExpProcesosDocumentoJudicialCorte selectedProcesosRegulacionCorte) {
        this.selectedProcesosRegulacionCorte = selectedProcesosRegulacionCorte;
    }

    public List<ExpProcesosDocumentoJudicialCorte> getListaProcesosActuacionesCorte() {
        return listaProcesosActuacionesCorte;
    }

    public void setListaProcesosActuacionesCorte(List<ExpProcesosDocumentoJudicialCorte> listaProcesosActuacionesCorte) {
        this.listaProcesosActuacionesCorte = listaProcesosActuacionesCorte;
    }

    public List<ExpProcesosDocumentoJudicialCorte> getListaProcesosRecursosCorte() {
        return listaProcesosRecursosCorte;
    }

    public void setListaProcesosRecursosCorte(List<ExpProcesosDocumentoJudicialCorte> listaProcesosRecursosCorte) {
        this.listaProcesosRecursosCorte = listaProcesosRecursosCorte;
    }

    public List<ExpProcesosDocumentoJudicialCorte> getListaProcesosExcepcionesCorte() {
        return listaProcesosExcepcionesCorte;
    }

    public void setListaProcesosExcepcionesCorte(List<ExpProcesosDocumentoJudicialCorte> listaProcesosExcepcionesCorte) {
        this.listaProcesosExcepcionesCorte = listaProcesosExcepcionesCorte;
    }

    public List<ExpProcesosDocumentoJudicialCorte> getListaProcesosRegulacionCorte() {
        return listaProcesosRegulacionCorte;
    }

    public void setListaProcesosRegulacionCorte(List<ExpProcesosDocumentoJudicialCorte> listaProcesosRegulacionCorte) {
        this.listaProcesosRegulacionCorte = listaProcesosRegulacionCorte;
    }

    public List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> getListaDomiciliosCorte() {
        return listaDomiciliosCorte;
    }

    public void setListaDomiciliosCorte(List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> listaDomiciliosCorte) {
        this.listaDomiciliosCorte = listaDomiciliosCorte;
    }

    public List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> getListaProfesionalesCorte() {
        return listaProfesionalesCorte;
    }

    public void setListaProfesionalesCorte(List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> listaProfesionalesCorte) {
        this.listaProfesionalesCorte = listaProfesionalesCorte;
    }

    public ExpPartesPorDocumentosJudicialesCorte getSelectedParteCorte() {
        return selectedParteCorte;
    }

    public void setSelectedParteCorte(ExpPartesPorDocumentosJudicialesCorte selectedParteCorte) {
        this.selectedParteCorte = selectedParteCorte;
    }

    public ExpIntervinientesPorDocumentosJudicialesCorte getSelectedIntervinienteCorte() {
        return selectedIntervinienteCorte;
    }

    public void setSelectedIntervinienteCorte(ExpIntervinientesPorDocumentosJudicialesCorte selectedIntervinienteCorte) {
        this.selectedIntervinienteCorte = selectedIntervinienteCorte;
    }

    public List<ExpObjetosPorDocumentosJudicialesCorte> getListaObjetosCorte() {
        return listaObjetosCorte;
    }

    public void setListaObjetosCorte(List<ExpObjetosPorDocumentosJudicialesCorte> listaObjetosCorte) {
        this.listaObjetosCorte = listaObjetosCorte;
    }

    public List<ExpPartesPorDocumentosJudicialesCorte> getListaPartesCorte() {
        return listaPartesCorte;
    }

    public void setListaPartesCorte(List<ExpPartesPorDocumentosJudicialesCorte> listaPartesCorte) {
        this.listaPartesCorte = listaPartesCorte;
    }

    public List<ExpIntervinientesPorDocumentosJudicialesCorte> getListaIntervinientesCorte() {
        return listaIntervinientesCorte;
    }

    public void setListaIntervinientesCorte(List<ExpIntervinientesPorDocumentosJudicialesCorte> listaIntervinientesCorte) {
        this.listaIntervinientesCorte = listaIntervinientesCorte;
    }

    public List<ExpActuacionesCorte> getListaRecursosCorte() {
        return listaRecursosCorte;
    }

    public void setListaRecursosCorte(List<ExpActuacionesCorte> listaRecursosCorte) {
        this.listaRecursosCorte = listaRecursosCorte;
    }

    public List<ExpActuacionesCorte> getListaExcepcionesCorte() {
        return listaExcepcionesCorte;
    }

    public void setListaExcepcionesCorte(List<ExpActuacionesCorte> listaExcepcionesCorte) {
        this.listaExcepcionesCorte = listaExcepcionesCorte;
    }

    public List<ExpActuacionesCorte> getListaRegulacionCorte() {
        return listaRegulacionCorte;
    }

    public void setListaRegulacionCorte(List<ExpActuacionesCorte> listaRegulacionCorte) {
        this.listaRegulacionCorte = listaRegulacionCorte;
    }

    public String getFiltroRadio() {
        return filtroRadio;
    }

    public void setFiltroRadio(String filtroRadio) {
        this.filtroRadio = filtroRadio;
    }

    public List<ExpActuacionesCorte> getListaActuacionesCorte() {
        return listaActuacionesCorte;
    }

    public void setListaActuacionesCorte(List<ExpActuacionesCorte> listaActuacionesCorte) {
        this.listaActuacionesCorte = listaActuacionesCorte;
    }

    public List<DocumentosJudicialesCorte> getExpedienteCorte() {
        return expedienteCorte;
    }

    public void setExpedienteCorte(List<DocumentosJudicialesCorte> expedienteCorte) {
        this.expedienteCorte = expedienteCorte;
    }

    public RespuestaConsultaCausas getSelectedCausa() {
        return selectedCausa;
    }

    public void setSelectedCausa(RespuestaConsultaCausas selectedCausa) {
        this.selectedCausa = selectedCausa;
    }

    public List<RespuestaConsultaCausas> getListaRespuestaConsultaCausas() {
        return listaRespuestaConsultaCausas;
    }

    public void setListaRespuestaConsultaCausas(List<RespuestaConsultaCausas> listaRespuestaConsultaCausas) {
        this.listaRespuestaConsultaCausas = listaRespuestaConsultaCausas;
    }

    public Integer getAsociarAno() {
        return asociarAno;
    }

    public void setAsociarAno(Integer asociarAno) {
        this.asociarAno = asociarAno;
    }

    public Integer getAsociarCausa() {
        return asociarCausa;
    }

    public void setAsociarCausa(Integer asociarCausa) {
        this.asociarCausa = asociarCausa;
    }

    public List<ExpMaterias> getListaMateriasCorte() {
        return listaMateriasCorte;
    }

    public void setListaMateriasCorte(List<ExpMaterias> listaMateriasCorte) {
        this.listaMateriasCorte = listaMateriasCorte;
    }

    public ExpMaterias getMateriaSelected() {
        return materiaSelected;
    }

    public void setMateriaSelected(ExpMaterias materiaSelected) {
        this.materiaSelected = materiaSelected;
    }

    public DepartamentosPersona getCircunscripcionSelected() {
        return circunscripcionSelected;
    }

    public void setCircunscripcionSelected(DepartamentosPersona circunscripcionSelected) {
        this.circunscripcionSelected = circunscripcionSelected;
    }

    public List<DepartamentosPersona> getListaCircunscripcionesCorte() {
        return listaCircunscripcionesCorte;
    }

    public void setListaCircunscripcionesCorte(List<DepartamentosPersona> listaCircunscripcionesCorte) {
        this.listaCircunscripcionesCorte = listaCircunscripcionesCorte;
    }

    public String getAsociarCaratula() {
        return asociarCaratula;
    }

    public void setAsociarCaratula(String asociarCaratula) {
        this.asociarCaratula = asociarCaratula;
    }

    private UploadedFile file;

    private String ckeditorConfig;

    public AntecedentesRoles getRolElegido() {
        return rolElegido;
    }

    public void setRolElegido(AntecedentesRoles rolElegido) {
        this.rolElegido = rolElegido;
    }

    public String getStyleHistorial() {
        return styleHistorial;
    }

    public void setStyleHistorial(String styleHistorial) {
        this.styleHistorial = styleHistorial;
    }

    public DefaultDiagramModel getModel() {
        return model;
    }

    public void setModel(DefaultDiagramModel model) {
        this.model = model;
    }

    public String getCkeditorConfig() {
        return ckeditorConfig;
    }

    public void setCkeditorConfig(String ckeditorConfig) {
        this.ckeditorConfig = ckeditorConfig;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<Personas> getListaPreopinantes() {
        return listaPreopinantes;
    }

    public void setListaPreopinantes(List<Personas> listaPreopinantes) {
        this.listaPreopinantes = listaPreopinantes;
    }

    public List<Personas> getListaSecretarios() {
        return listaSecretarios;
    }

    public void setListaSecretarios(List<Personas> listaSecretarios) {
        this.listaSecretarios = listaSecretarios;
    }

    public List<ExpEstadosActuacion> getListaEstadosActuacion() {
        return listaEstadosActuacion;
    }

    public void setListaEstadosActuacion(List<ExpEstadosActuacion> listaEstadosActuacion) {
        this.listaEstadosActuacion = listaEstadosActuacion;
    }

    public List<ExpFormatos> getListaFormatos() {
        return listaFormatos;
    }

    public void setListaFormatos(List<ExpFormatos> listaFormatos) {
        this.listaFormatos = listaFormatos;
    }

    public ExpPersonasFirmantesPorActuaciones getSelectedPersonaFirmantePorActuacion() {
        return selectedPersonaFirmantePorActuacion;
    }

    public void setSelectedPersonaFirmantePorActuacion(ExpPersonasFirmantesPorActuaciones selectedPersonaFirmantePorActuacion) {
        this.selectedPersonaFirmantePorActuacion = selectedPersonaFirmantePorActuacion;
    }

    public List<ExpPersonasFirmantesPorActuaciones> getListaEstadosFirmas() {
        return listaEstadosFirmas;
    }

    public void setListaEstadosFirmas(List<ExpPersonasFirmantesPorActuaciones> listaEstadosFirmas) {
        this.listaEstadosFirmas = listaEstadosFirmas;
    }

    public Personas getAbogadoSelected() {
        return abogadoSelected;
    }

    public void setAbogadoSelected(Personas abogadoSelected) {
        this.abogadoSelected = abogadoSelected;
    }

    public String getMensajeAviso() {
        return mensajeAviso;
    }

    public void setMensajeAviso(String mensajeAviso) {
        this.mensajeAviso = mensajeAviso;
    }

    public String getEstadoProcesal() {
        return estadoProcesal;
    }

    public void setEstadoProcesal(String estadoProcesal) {
        this.estadoProcesal = estadoProcesal;
    }

    public ExpEstadosActuacion getEstadoActuacion() {
        return estadoActuacion;
    }

    public void setEstadoActuacion(ExpEstadosActuacion estadoActuacion) {
        this.estadoActuacion = estadoActuacion;
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

    public List<ExpEstadosProcesoExpedienteElectronico> getEstadosProcesoFiltro() {
        return estadosProcesoFiltro;
    }

    public void setEstadosProcesoFiltro(List<ExpEstadosProcesoExpedienteElectronico> estadosProcesoFiltro) {
        this.estadosProcesoFiltro = estadosProcesoFiltro;
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

    public ExpPersonasAsociadas getPersonaFirmanteSelected() {
        return personaFirmanteSelected;
    }

    public void setPersonaFirmanteSelected(ExpPersonasAsociadas personaFirmanteSelected) {
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

    public String getNroCausaExp() {
        return nroCausaExp;
    }

    public void setNroCausaExp(String nroCausaExp) {
        this.nroCausaExp = nroCausaExp;
    }

    public List<Personas> getListaPersonasFirmantes() {
        return listaPersonasFirmantes;
    }

    public void setListaPersonasFirmantes(List<Personas> listaPersonasFirmantes) {
        this.listaPersonasFirmantes = listaPersonasFirmantes;
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

    public List<ExpPersonasAcusacion> getListaPersonasInformadasActivas() {
        List<ExpPersonasAcusacion> listaPersonasInformadasActivas = new ArrayList<>();
        if (listaPersonasInformadas != null) {
            for (ExpPersonasAcusacion per : listaPersonasInformadas) {
                if ("AC".equals(per.getEstado())) {
                    listaPersonasInformadasActivas.add(per);
                }
            }
        }

        return listaPersonasInformadasActivas;
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

    public List<Personas> getListaInhibidosActivos() {
        List<Personas> listaInhibidosActivos = new ArrayList<>();
        if (listaInhibidosAdmin != null) {
            for (Personas per : listaInhibidosAdmin) {
                if ("AC".equals(per.getEstado())) {
                    listaInhibidosActivos.add(per);
                }
            }
        }

        return listaInhibidosActivos;
    }

    public List<Personas> getListaRestringidosActivos() {
        List<Personas> listaRestringidosActivos = new ArrayList<>();
        if (listaRestringidosAdmin != null) {
            for (Personas per : listaRestringidosAdmin) {
                if ("AC".equals(per.getEstado())) {
                    listaRestringidosActivos.add(per);
                }
            }
        }

        return listaRestringidosActivos;
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

    public DocumentosJudicialesPorSecretariaController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudiciales.class);
    }

    /*
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
     */
    public void generar() {
        WordInsertHTMLaltChunkInDocument doc = new WordInsertHTMLaltChunkInDocument();

        try {
            doc.generar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private EndPoint createDotEndPoint(EndPointAnchor anchor) {
        // ImageEndPoint endPoint = new ImageEndPoint(anchor, "transferir.png");
        DotEndPoint endPoint = new DotEndPoint(anchor);
        endPoint.setScope("network");
        endPoint.setSource(false);
        endPoint.setTarget(false);
        endPoint.setStyle("{fillStyle:'#98AFC7'}");
        endPoint.setHoverStyle("{fillStyle:'#5C738B'}");

        return endPoint;
    }

    private EndPoint createRectangleEndPoint(EndPointAnchor anchor) {
        RectangleEndPoint endPoint = new RectangleEndPoint(anchor);
        endPoint.setScope("network");
        endPoint.setSource(false);
        endPoint.setTarget(false);
        endPoint.setStyle("{fillStyle:'#98AFC7'}");
        endPoint.setHoverStyle("{fillStyle:'#5C738B'}");

        return endPoint;
    }

    private Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        conn.setDetachable(false);

        if (label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }

        return conn;
    }

    public boolean renderedFiltroRadio(AntecedentesRoles rol) {
        return filtroURL.verifPermiso(Constantes.PERMISO_FILTRO_RADIO, rol.getId());
    }

    public boolean renderedVerHistorial() {
        return renderedVerHistorial(rolElegido);
    }

    public boolean renderedVerHistorial(AntecedentesRoles rol) {
        return filtroURL.verifPermiso(Constantes.PERMISO_VER_HISTORIAL, rol.getId());
    }

    public boolean renderedAsociarExpedienteCorte(AntecedentesRoles rol) {
        return filtroURL.verifPermiso(Constantes.PERMISO_ASOCIAR_EXPEDIENTE_CORTE, rol.getId());
    }

    public boolean renderedVerExpedienteCorte(AntecedentesRoles rol) {

        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_EXPEDIENTE_CORTE, rol.getId())) {
            return true;
        }

        return false;
    }

    public boolean renderedVerLibro(AntecedentesRoles rol) {

        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_LIBRO, rol.getId())) {
            return true;
        }

        return false;
    }

    public boolean renderedVerActuaciones() {
        return renderedVerActuaciones(rolElegido);
    }

    public boolean renderedVerActuaciones(AntecedentesRoles rol) {

        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_ACTUACIONES, rol.getId())) {
            return true;
        }

        return false;
    }

    public String getPdfCompleto() {
        /*
    }
        if (getSelected() != null) {
            if (listaActuaciones != null) {

                if (!listaActuaciones.isEmpty()) {
         */
        return url + "/tmp/" + nombre;
        /*
                }
            }
        }

        return "";
         */
    }

    public void preparePdfCompleto(DocumentosJudiciales doc, List<ExpActuaciones> listaAct) {
        if (doc != null) {

            if (listaAct != null) {

                if (!listaAct.isEmpty()) {

                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);
                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + ".pdf";
                    try {
                        PdfDocument pdf = new PdfDocument(new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre));
                        PdfMerger merger = new PdfMerger(pdf);

                        //for (ExpActuaciones act : listaActuaciones) {
                        ExpActuaciones act = null;
                        for (int i = listaAct.size() - 1; i >= 0; i--) {
                            act = listaAct.get(i);
                            if (act.isVisible()) {
                                if (act.getArchivo() != null) {
                                    PdfDocument firstSourcePdf = new PdfDocument(new PdfReader(par.getRutaArchivos() + "/" + act.getArchivo()));
                                    merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());
                                    firstSourcePdf.close();
                                }
                            }
                        }
                        pdf.close();

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }

    /*
    public void asociarExpedienteCorte(RespuestaConsultaCausas item) {
        if (getSelected() != null) {

            List<DocumentosJudicialesCorte> lista2 = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findByDocumentoJudicial", DocumentosJudicialesCorte.class).setParameter("documentoJudicial", getSelected()).getResultList();

            if (!lista2.isEmpty()) {

            }

            List<ExpActuacionesCorte> lista = ejbFacade.getEntityManager().createNamedQuery("ExpActuacionesCorte.findByDocumentoJudicial", ExpActuacionesCorte.class).setParameter("documentoJudicial", getSelected()).getResultList();

            for (ExpActuacionesCorte act : lista) {
                expActuacionesCorteController.setSelected(act);
                expActuacionesCorteController.delete(null);
            }

            List<ExpProcesosDocumentoJudicialCorte> lista5 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosDocumentoJudicialCorte.findByDocumentoJudicial", ExpProcesosDocumentoJudicialCorte.class).setParameter("documentoJudicial", getSelected()).getResultList();

            for (ExpProcesosDocumentoJudicialCorte act : lista5) {
                expProcesosDocumentoJudicialCorteController.setSelected(act);
                expProcesosDocumentoJudicialCorteController.delete(null);
            }

            getSelected().setExpedienteCorte(null);
            super.save(null);

            for (DocumentosJudicialesCorte act : lista2) {

                List<ExpObjetosPorDocumentosJudicialesCorte> lista6 = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpObjetosPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", act).getResultList();

                for (ExpObjetosPorDocumentosJudicialesCorte obj : lista6) {
                    objetosPorDocumentosJudicialesCorteController.setSelected(obj);
                    objetosPorDocumentosJudicialesCorteController.delete(null);
                }

                List<ExpIntervinientesPorDocumentosJudicialesCorte> lista8 = ejbFacade.getEntityManager().createNamedQuery("ExpIntervinientesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpIntervinientesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", act).getResultList();

                for (ExpIntervinientesPorDocumentosJudicialesCorte obj : lista8) {
                    intervinientesPorDocumentosJudicialesCorteController.setSelected(obj);
                    intervinientesPorDocumentosJudicialesCorteController.delete(null);
                }

                List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> lista9 = ejbFacade.getEntityManager().createNamedQuery("ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", act).getResultList();

                for (ExpDomiciliosPorPartesPorDocumentosJudicialesCorte obj : lista9) {
                    domiciliosPorPartesPorDocumentosJudicialesCorteController.setSelected(obj);
                    domiciliosPorPartesPorDocumentosJudicialesCorteController.delete(null);
                }

                List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> lista10 = ejbFacade.getEntityManager().createNamedQuery("ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", act).getResultList();

                for (ExpProfesionalesPorPartesPorDocumentosJudicialesCorte obj : lista10) {
                    profesionalesPorPartesPorDocumentosJudicialesCorteController.setSelected(obj);
                    profesionalesPorPartesPorDocumentosJudicialesCorteController.delete(null);
                }

                List<ExpPartesPorDocumentosJudicialesCorte> lista7 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", act).getResultList();

                for (ExpPartesPorDocumentosJudicialesCorte obj : lista7) {
                    partesPorDocumentosJudicialesCorteController.setSelected(obj);
                    partesPorDocumentosJudicialesCorteController.delete(null);
                }
                
                documentoJudicialCorteController.setSelected(act);
                documentoJudicialCorteController.delete(null);
            }

            Date fecha = ejbFacade.getSystemDate();

            DocumentosJudicialesCorte doc = new DocumentosJudicialesCorte(item.getCodCasoJudicial(), item.getNroExpedienteCircunscripcion(), item.getNroExpedienteMateria(), item.getNroExpedienteTipoDependencia(), item.getNroExpedienteNroDependencia(), item.getNroExpedienteAnio(), item.getNroExpedienteNumero(), item.getCaratula(), item.getCodDespachoJudicial(), item.getDescripcionDespachoJudicial(), item.getEstadoCasoDespacho(), item.getFechaPrimerActo(), item.getNombreJuez(), fecha, personaUsuario, getSelected(), circunscripcionSelected.getCodigoCorte(), circunscripcionSelected, item.getDescripcionMoneda(), item.getMonto(), item.getProceso(), item.getFase(), item.getNumeroLiquidacion(), item.getAuxiliarJusticiaPresentacion());

            documentoJudicialCorteController.setSelected(doc);
            documentoJudicialCorteController.saveNew(null);

            getSelected().setExpedienteCorte(documentoJudicialCorteController.getSelected());
            super.save(null);

        }

    }
     */
    public void desasociarExpedienteCorte(RespuestaConsultaCausas item) {
        if (item.getDocumentoJudicialCorte() != null) {
            List<ExpDocumentosJudicialesCortePorDocumentosJudiciales> docs = ejbFacade.getEntityManager().createNamedQuery("ExpDocumentosJudicialesCortePorDocumentosJudiciales.findByDocumentoJudicialCorteANDDocumentoJudicial", ExpDocumentosJudicialesCortePorDocumentosJudiciales.class).setParameter("documentoJudicialCorte", item.getDocumentoJudicialCorte().getId()).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            if (!docs.isEmpty()) {
                documentosJudicialesCortePorDocumentosJudicialesController.setSelected(docs.get(0));
                documentosJudicialesCortePorDocumentosJudicialesController.delete(null);

                obtenerExpedientesCorteAsociados();
                /*
                RespuestaConsultaCausas resp = new RespuestaConsultaCausas(docs.get(0).getDocumentoJudicialCorte().getCodCasoJudicial(),  docs.get(0).getDocumentoJudicialCorte().getNroExpedienteCircunscripcion(), docs.get(0).getDocumentoJudicialCorte().getNroExpedienteMateria(), docs.get(0).getDocumentoJudicialCorte().getNroExpedienteTipoDependencia(), docs.get(0).getDocumentoJudicialCorte().getNroExpedienteNroDependencia(), docs.get(0).getDocumentoJudicialCorte().getNroExpedienteAnio(), docs.get(0).getDocumentoJudicialCorte().getNroExpedienteNumero(), docs.get(0).getDocumentoJudicialCorte().getCaratula(), docs.get(0).getDocumentoJudicialCorte().getCodDespachoJudicial(), docs.get(0).getDocumentoJudicialCorte().getDescripcionDespachoJudicial(), docs.get(0).getDocumentoJudicialCorte().getEstadoCasoDespacho(), docs.get(0).getDocumentoJudicialCorte().getFechaPrimerActo(), docs.get(0).getDocumentoJudicialCorte().getNombreJuez(), docs.get(0).getDocumentoJudicialCorte().getDescripcionMoneda(), docs.get(0).getDocumentoJudicialCorte().getMonto(), docs.get(0).getDocumentoJudicialCorte().getProceso(), docs.get(0).getDocumentoJudicialCorte().getFase(), docs.get(0).getDocumentoJudicialCorte().getNumeroLiquidacion(), docs.get(0).getDocumentoJudicialCorte().getAuxiliarJusticiaPresentacion(), null);

                if(listaRespuestaConsultaCausas == null){
                    listaRespuestaConsultaCausas = new ArrayList<>();
                }
                
                listaRespuestaConsultaCausas.add(resp);
                 */

            } else if (docs.size() > 1) {
                JsfUtil.addErrorMessage("Se encontro mas de un expediente corte a desasociar");
            } else {
                JsfUtil.addErrorMessage("No se encontró expediente corte a desasociar");
            }
        }
    }

    public void asociarExpedienteCorte(RespuestaConsultaCausas item) {

        if (getSelected() != null) {
            DocumentosJudicialesCorte docCorte = null;

            List<DocumentosJudicialesCorte> lista11 = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findByCodCasoJudicial", DocumentosJudicialesCorte.class).setParameter("codCasoJudicial", item.getCodCasoJudicial()).getResultList();

            Date fecha = ejbFacade.getSystemDate();

            if (lista11.isEmpty()) {

                DocumentosJudicialesCorte doc = new DocumentosJudicialesCorte(item.getCodCasoJudicial(), item.getNroExpedienteCircunscripcion(), item.getNroExpedienteMateria(), item.getNroExpedienteTipoDependencia(), item.getNroExpedienteNroDependencia(), item.getNroExpedienteAnio(), item.getNroExpedienteNumero(), item.getCaratula(), item.getCodDespachoJudicial(), item.getDescripcionDespachoJudicial(), item.getEstadoCasoDespacho(), item.getFechaPrimerActo(), item.getNombreJuez(), fecha, personaUsuario, getSelected(), circunscripcionSelected.getCodigoCorte(), circunscripcionSelected, item.getDescripcionMoneda(), item.getMonto(), item.getProceso(), item.getFase(), item.getNumeroLiquidacion(), item.getAuxiliarJusticiaPresentacion());

                documentoJudicialCorteController.setSelected(doc);
                documentoJudicialCorteController.saveNew(null);

                docCorte = documentoJudicialCorteController.getSelected();
            } else if (lista11.size() > 1) {
                JsfUtil.addErrorMessage("El expediente corte está duplicado");
                return;
            } else {
                docCorte = lista11.get(0);
            }

            List<ExpDocumentosJudicialesCortePorDocumentosJudiciales> docs = ejbFacade.getEntityManager().createNamedQuery("ExpDocumentosJudicialesCortePorDocumentosJudiciales.findByDocumentoJudicialCorteANDDocumentoJudicial", ExpDocumentosJudicialesCortePorDocumentosJudiciales.class).setParameter("documentoJudicialCorte", docCorte.getId()).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            if (docs.isEmpty()) {

                ExpDocumentosJudicialesCortePorDocumentosJudicialesPK pk = new ExpDocumentosJudicialesCortePorDocumentosJudicialesPK(docCorte.getId(), getSelected().getId());

                ExpDocumentosJudicialesCortePorDocumentosJudiciales doc = new ExpDocumentosJudicialesCortePorDocumentosJudiciales(pk, fecha, fecha, personaUsuario, personaUsuario, getSelected(), docCorte);
                documentosJudicialesCortePorDocumentosJudicialesController.setSelected(doc);
                documentosJudicialesCortePorDocumentosJudicialesController.saveNew(null);

                // listaRespuestaConsultaCausas.remove(item);
            } else {
                JsfUtil.addErrorMessage("El expediente corte ya esta asociado a este expediente JEM");
                return;
            }

            obtenerExpedientesCorteAsociados();
        }

    }

    public boolean verifSiAsociado(Integer codCasoJudicial, DocumentosJudiciales doc) {
        List<ExpDocumentosJudicialesCortePorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ExpDocumentosJudicialesCortePorDocumentosJudiciales.findByDocumentoJudicial", ExpDocumentosJudicialesCortePorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).getResultList();

        for (ExpDocumentosJudicialesCortePorDocumentosJudiciales d : lista) {
            if (codCasoJudicial.equals(d.getDocumentoJudicialCorte().getCodCasoJudicial())) {
                return true;
            }
        }

        return false;
    }

    public void buscarAsociarExpedienteCorte() {
        try {

            if (circunscripcionSelected == null) {
                JsfUtil.addErrorMessage("Debe especificar la circunscripción");
                return;
            }

            List<ExpConexiones> ws = ejbFacade.getEntityManager().createNamedQuery("ExpConexiones.findById", ExpConexiones.class).setParameter("id", Constantes.CONEXION_CORTE_BUSCAR_EXPEDIENTES_ID).getResultList();

            if (ws.isEmpty()) {
                JsfUtil.addErrorMessage("No se encuentran parametros para conexion con la Corte");
                return;
            }

            listaRespuestaConsultaCausas = new ArrayList<>();

            CloseableHttpClient CLIENT = Utils.createAcceptSelfSignedCertificateClient();
            HttpPost request = new HttpPost(ws.get(0).getIpServidor() + ":" + ws.get(0).getPuertoServidor() + ws.get(0).getUri());

            JSONObject json = new JSONObject();
            json.put("caratula", asociarCaratula);
            json.put("causaJEM", getSelected().getId());
            json.put("ano", asociarAno);
            json.put("causa", asociarCausa);
            json.put("circunscripcion", circunscripcionSelected.getCodigoCorte());
            json.put("materia", materiaSelected.getCodigoCorte());
            json.put("usuario", ws.get(0).getUsuario());

            try {
                ByteArrayEntity params = new ByteArrayEntity(Utils.encryptMsg(json.toString(), Utils.generateKey()));
                request.addHeader("content-type", "application/octet-stream;charset=UTF-8");
                request.addHeader("charset", "UTF-8");
                request.setEntity(params);

                HttpResponse response = (HttpResponse) CLIENT.execute(request);
                if (response != null) {
                    HttpEntity entity = response.getEntity();

                    byte[] bytes = IOUtils.toByteArray(entity.getContent());

                    String respuesta = Utils.decryptMsg(bytes, Utils.generateKey());

                    if (respuesta != null) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(respuesta);

                            if (obj.getInt("codigo") == 0) {

                                JSONArray lista = obj.getJSONArray("lista");

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");

                                RespuestaConsultaCausas resp = null;
                                for (int j = 0; j < lista.length(); j++) {
                                    JSONObject rs = lista.getJSONObject(j);

                                    if (!verifSiAsociado(rs.getInt("codCasoJudicial"), getSelected())) {
                                        resp = new RespuestaConsultaCausas(rs.getInt("codCasoJudicial"), rs.getInt("nroExpedienteCircunscripcion"), rs.getInt("nroExpedienteMateria"), rs.getInt("nroExpedienteTipoDependencia"), rs.getInt("nroExpedienteNroDependencia"), rs.getInt("nroExpedienteAnio"), rs.getInt("nroExpedienteNumero"), rs.getString("caratula"), rs.getInt("codDespachoJudicial"), rs.getString("descripcionDespachoJudicial"), rs.getString("estadoCasoDespacho"), format.parse(rs.getString("fechaPrimerActo")), rs.getString("nombreJuez"), rs.getString("descripcionMoneda"), rs.getBigDecimal("monto"), rs.getString("proceso"), rs.getString("fase"), rs.getString("numeroLiquidacion"), rs.getString("auxiliarJusticiaPresentacion"));
                                        listaRespuestaConsultaCausas.add(resp);
                                    }
                                }

                                if (listaRespuestaConsultaCausas.isEmpty()) {
                                    JsfUtil.addErrorMessage("No hay resultados para la consulta");
                                }

                            } else if (obj.getInt("codigo") == 1) {
                                JsfUtil.addErrorMessage("Error :" + obj.getString("descripcion"));
                            } else {
                                JsfUtil.addErrorMessage("Error al buscar expedientes corte");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }

                }

            } catch (InvalidKeySpecException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            } catch (NoSuchPaddingException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            } catch (InvalidKeyException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            } catch (InvalidParameterSpecException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            } catch (IllegalBlockSizeException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            } catch (BadPaddingException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            } catch (InvalidAlgorithmParameterException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
                return;
            }

            //mensaje = decryptMsg(pedido, generateKey());
            /*
                HttpEntity entity = response.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                resp = mapper.readValue((EntityUtils.toString(entity)), RespuestaPersonas.class);
             */
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo buscar expedientes corte");
        }
    }

    public void verHistorial(DocumentosJudiciales doc) {

        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);
        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        //StateMachineConnector connector = new StateMachineConnector();
        FlowChartConnector connector = new FlowChartConnector();

        connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
        model.setDefaultConnector(connector);

        int x = 5;

        int y = 5;

        int contadorColor = 0;

        boolean primero = true;

        Element elAnterior = null;

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        List<String> firmantes = null;

        int contador;

        List<EstadosProcesalesDocumentosJudiciales> cambios = ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByDocumentoJudicial", EstadosProcesalesDocumentosJudiciales.class).setParameter("documentoJudicial", doc).getResultList();
        for (EstadosProcesalesDocumentosJudiciales cambio : cambios) {

            firmantes = new ArrayList<>();
            contador = 0;
            if (cambio.getActuacion() != null) {
                if (cambio.getActuacion().getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_RESOLUCION)) {
                    List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", cambio.getActuacion()).setParameter("estado", new Estados("AC")).getResultList();
                    boolean primer = true;
                    for (ExpPersonasFirmantesPorActuaciones firma : lista) {
                        if (firma.isFirmado()) {
                            if (primer) {
                                firmantes.add("FIRMADO POR:");
                            }
                            primer = false;
                            firmantes.add(firma.getPersonaFirmante().getNombresApellidos());
                            firmantes.add(format.format(firma.getFechaHoraFirma()));
                            contador++;
                        }
                    }
                }
            }

            Elemento el = new Elemento(cambio.getEstadoProcesal(), firmantes, format.format(cambio.getFechaHoraAlta()));
            Element elInicial = new Element(el, String.valueOf(x) + "em", String.valueOf(y) + "em");
            elInicial.setId(String.valueOf(cambio.getId()));

            elInicial.setDraggable(false);

            EndPoint endPoint = createDotEndPoint(EndPointAnchor.BOTTOM);
            elInicial.addEndPoint(endPoint);

            if (!primero) {
                EndPoint endPoint2 = createRectangleEndPoint(EndPointAnchor.TOP);
                elInicial.addEndPoint(endPoint2);
            }
            contadorColor++;

            if (contadorColor > 7) {
                contadorColor = 1;
            }

            if (contador == 0) {
                elInicial.setStyleClass("diagrama" + String.valueOf(contadorColor));
            } else {
                elInicial.setStyleClass("diagrama" + String.valueOf(contadorColor) + String.valueOf(contador));
            }
            model.addElement(elInicial);

            if (!primero) {
                model.connect(createConnection(elAnterior.getEndPoints().get(0), elInicial.getEndPoints().get(1), null));
            }

            y += 12 + (6 * contador);

            elAnterior = elInicial;

            primero = false;

        }

        styleHistorial = "width: 300px; height: " + ((y * 10) + 100) + "px";

        /*
        PrimeFaces.current().ajax().update("DocumentosJudicialesHistorialForm");

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('DocumentosJudicialesHistorialDialog').show();");
         */
    }

    public void generarArchivo() throws Exception {

        List<Metadatos> listaMetadatos = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        Date fechaActual = ejbFacade.getSystemDate();
        Date fecha = generarFechaPresentacion(fechaActual);
        /*
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2022);
        myCal.set(Calendar.MONTH, 4);
        myCal.set(Calendar.DAY_OF_MONTH, 12);
        myCal.set(Calendar.HOUR_OF_DAY, 15);
        myCal.set(Calendar.MINUTE, 38);
        myCal.set(Calendar.SECOND, 0);
        Date fechaInicio = myCal.getTime();
                    Date fecha = generarFechaPresentacion(fechaInicio);
         */

        Date fechaActuacion = fecha;

        if (resolucion != null) {
            if (resolucion.getFecha() != null) {
                fechaActuacion = resolucion.getFecha();
            }
        } else {
            if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(docImprimir.getTipoActuacion().getId())) {
                List<Resoluciones> lista = ejbFacade.getEntityManager().createNamedQuery("Resoluciones.findById", Resoluciones.class).setParameter("id", docImprimir.getResolucion().getId()).getResultList();
                if (!lista.isEmpty()) {
                    if (lista.get(0).getFecha() != null) {
                        fechaActuacion = lista.get(0).getFecha();
                    }
                }
            }
        }

        fechaFinal = fechaActuacion;

        // Metadatos meta = new Metadatos("fecha", format.format(fechaFinal));
        Metadatos meta = new Metadatos("fecha", format.format(fechaActual));
        listaMetadatos.add(meta);
        secuencia = null;
        if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(docImprimir.getTipoActuacion().getId())) {
            if (docImprimir.getResolucion() != null) {
                if (Constantes.TIPO_RESOLUCION_AI.equals(docImprimir.getResolucion().getTipoResolucion().getId())) {
                    ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();
                    secuencia = tipo.getSecuencia();
                } else if (Constantes.TIPO_RESOLUCION_SD.equals(docImprimir.getResolucion().getTipoResolucion().getId())) {
                    ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_SD).getSingleResult();
                    secuencia = tipo.getSecuencia();
                }
            } else if (resolucion != null) {
                if (Constantes.TIPO_RESOLUCION_AI.equals(resolucion.getTipoResolucion().getId())) {
                    ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();
                    secuencia = tipo.getSecuencia();
                } else if (Constantes.TIPO_RESOLUCION_SD.equals(resolucion.getTipoResolucion().getId())) {
                    ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_SD).getSingleResult();
                    secuencia = tipo.getSecuencia();
                }
            }

        } else {
            if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId())) {
                ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_OFICIO).getSingleResult();
                secuencia = tipo.getSecuencia();
            } else {
                secuencia = docImprimir.getTipoActuacion().getSecuencia();
            }
        }

        Metadatos meta2 = null;
        if (secuencia != null) {
            String[] array = secuencia.split("/");
            if (array.length > 1) {
                if (array[0].length() == 1) {
                    secuencia = "0" + secuencia;
                }
            }

            meta2 = new Metadatos("nro", secuencia);
        } else {
            meta2 = new Metadatos("nro", "");
        }

        textoFinal = (docImprimir.getTexto() == null) ? "" : docImprimir.getTexto();

        listaMetadatos.add(meta2);
        Metadatos meta3 = new Metadatos("texto", (docImprimir.getTexto() == null) ? "" : docImprimir.getTexto());
        listaMetadatos.add(meta3);
        if (getSelected() != null) {
            Metadatos meta4 = new Metadatos("causa", getSelected().getCausa().replace("-", "/"));
            listaMetadatos.add(meta4);
            Metadatos meta5 = new Metadatos("caratula", getSelected().getCaratula());
            listaMetadatos.add(meta5);
            Metadatos meta6 = new Metadatos("url", url);
            listaMetadatos.add(meta6);
        } else {
            Metadatos meta4 = new Metadatos("causa", docImprimir.getDocumentoJudicial().getCausa());
            listaMetadatos.add(meta4);
            Metadatos meta5 = new Metadatos("caratula", docImprimir.getDocumentoJudicial().getCaratula());
            listaMetadatos.add(meta5);
            /*
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            int pos = url.lastIndexOf(uri);
            url = url.substring(0, pos);
             */
            Metadatos meta6 = new Metadatos("url", url);
            listaMetadatos.add(meta6);
        }

        if (docImprimir.getDocumentoJudicialCorte() != null || docId != null) {
            List<DocumentosJudicialesCorte> docCorte = new ArrayList<>();
            if (docImprimir.getDocumentoJudicialCorte() != null) {
                docCorte.add(docImprimir.getDocumentoJudicialCorte());
            } else {
                docCorte = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findById", DocumentosJudicialesCorte.class).setParameter("id", docId).getResultList();
            }
            if (!docCorte.isEmpty()) {
                Metadatos meta7 = new Metadatos("causaCorte", String.valueOf(docCorte.get(0).getNroExpedienteNumero()));
                listaMetadatos.add(meta7);
                Metadatos meta8 = new Metadatos("caratulaCorte", String.valueOf(docCorte.get(0).getCaratula()));
                listaMetadatos.add(meta8);
            }
        }

        // System.err.println("URL: " + url);

        /*
                            if (Constantes.TIPO_ACTUACION_OFICIO.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(docImprimir.getTipoActuacion().getId())) {

                                XWPFDocument doc = new XWPFDocument(OPCPackage.open(par.getRutaPlantillas() + "/" + docImprimir.getFormato().getArchivo()));

                                WordReplacer wr = new WordReplacer(doc);

                                if (docImprimir.getTexto() != null) {
                                    for (Metadatos metadato : listaMetadatos) {
                                        docImprimir.setTexto(docImprimir.getTexto().replace(inicio + metadato.getClave().trim() + fin, metadato.getValor().trim()));
                                    }
                                }

                                for (Metadatos metadato : listaMetadatos) {
                                    wr.replaceWordsInText(inicio + metadato.getClave().trim() + fin, metadato.getValor().trim());
                                }

                                // ReemplazarWord rr = new ReemplazarWord();
                                // rr.iterateThroughParagraphs(doc, mapa);
                                // rr.iterateThroughFooters(doc, mapa);
                                // rr.iterateThroughTables(doc, mapa);
                                // for (XWPFParagraph p : doc.getParagraphs()) {
                                //     String todo = p.getText();
                                //     
                                //     for(Metadatos metadato : listaMetadatos){
                                //         todo = todo.replace(inicio + metadato.getClave().trim() + fin, metadato.getValor());
                                //         PositionInParagraph pos = 
                                //         p.searchText(sessionId, startPos)
                                //     }
                                //     
                                // }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);

                                nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + ".pdf";

                                PdfOptions options = PdfOptions.create();

                                OutputStream out = new FileOutputStream(new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre));
                                PdfConverter.getInstance().convert(doc, out, options);

                                super.save(null);
                            } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId())) {
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);
        Date fechaHora = ejbFacade.getSystemDate();
        nombre = session.getId() + "_" + simpleDateFormat.format(fechaHora) + ".pdf";

        //HtmlConverter.convertToPdf(docImprimir.getTexto(), new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre));
        //File inputHTML = new File(par.getRutaArchivos() + "/html.html");
        // String stringFinal = ((docImprimir.getTexto() == null) ? "" : docImprimir.getTexto().replace("&nbsp;", "&#160;"));
        String stringFinal = ((docImprimir.getTexto() == null) ? "" : docImprimir.getTexto());
        // @page { margin: 130px 50px 50px}

        // "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" /><title></title><style>@media print {html, body {width: 183.6mm;height: 280.5mm;margin-top: 15mm;margin-left: 10mm;margin-right: 70mm;margin-bottom: 18mm;}}</style></head>
        stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + stringFinal + "</body></html>";
        //org.jsoup.nodes.Document document = Jsoup.parse(sustituirTags(stringFinal, listaMetadatos));

        //String otro = document.html();
        // document.select("a").unwrap();
        // document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
        try ( OutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre + "_temp")) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();

            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);

            FontResolver resolver = renderer.getFontResolver();

            renderer.getFontResolver().addFont(par.getRutaArchivos() + "/garamond/GaramondRegular.ttf", true);

            renderer.setDocumentFromString(sustituirTags(stringFinal, listaMetadatos));
            renderer.layout();
            renderer.createPDF(outputStream);

        }

        /*
                    com.itextpdf.layout.element.Image img = new com.itextpdf.layout.element.Image(ImageDataFactory.createJpeg(Files.readAllBytes(Paths.get(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg")))).setFixedPosition(12, 300);
                    
                    // image.SetAbsolutePosition(12, 300);
                    
                    ImageEventHandler handler = new ImageEventHandler(img);
                    PdfDocument pdfDoc = new PdfDocument(new PdfReader(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre + "_temp"), new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre));
                    pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, handler);
                    
                    Document doc = new Document(pdfDoc);
                    Paragraph p = new Paragraph("List of reported UFO sightings in 20th century").setFontSize(14);

                    doc.add(p);
                    
                    doc.close();
                    
         */
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre + "_temp"), new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre));

        // PdfADocument pdfDoc = new PdfADocument(new PdfReader(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre + "_temp"), new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre));
        // Document doc = new Document(pdfDoc);
        pdfDoc.setTagged();
        // Document doc = new Document(pdfDoc, PageSize.A4);
        Document doc = new Document(pdfDoc);

        // doc.setMargins(200, 200, 200, 200);
        // java.awt.Image imagen = new java.awt.Image
        File pathToFile = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg");
        Image image = ImageIO.read(pathToFile);

        int diseno = docImprimir.getDiseno() == null ? 1 : docImprimir.getDiseno();

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            if ((i % 2) > 0) {

                if (diseno == 1) {

                    PdfPage pagina = pdfDoc.getPage(i);
                    Rectangle pageSize = pagina.getPageSize();
                    float x = (pageSize.getWidth() / 2) - 75;
                    float y = pageSize.getTop() - 80;
                    PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);
                    Rectangle rect = new Rectangle(x, y, 150, 60);
                    under.addImageFittedIntoRectangle(ImageDataFactory.create(image, Color.white), rect, false);
                    com.itextpdf.kernel.colors.Color magentaColor = new DeviceCmyk(1.f, 1.f, 1.f, 0.f);
                    under.setStrokeColor(magentaColor).moveTo(88, 856).lineTo(550, 856).closePathStroke();

                    under.saveState();
                    // under2.saveState();                            

                } else {
                    PdfPage pagina = pdfDoc.getPage(i);
                    Rectangle pageSize = pagina.getPageSize();

                    List<TabStop> tabStops = new ArrayList<>();
                    float width = pageSize.getWidth() - doc.getLeftMargin() - doc.getRightMargin();
                    tabStops.add(new TabStop(width / 2, TabAlignment.CENTER));
                    tabStops.add(new TabStop(width, TabAlignment.LEFT));

                    // Cabecera
                    float x = (pageSize.getWidth() / 2) - 75;
                    float y = pageSize.getTop() - 80;
                    PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);
                    Rectangle rect = new Rectangle(x, y, 150, 60);
                    under.addImageFittedIntoRectangle(ImageDataFactory.create(image, Color.white), rect, false);
                    com.itextpdf.kernel.colors.Color magentaColor = new DeviceCmyk(1.f, 1.f, 1.f, 0.f);
                    under.setStrokeColor(magentaColor).moveTo(88, 820).lineTo(550, 820).closePathStroke();

                    under.setStrokeColor(magentaColor).moveTo(88, 105).lineTo(550, 105).closePathStroke();

                    Rectangle rect2 = new Rectangle(88, pageSize.getTop() - 140, 480, 60);
                    under.rectangle(rect2);
                    // under.stroke();
                    Canvas canvas = new Canvas(under, pdfDoc, rect2);
                    PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                    PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC);
                    Text titleCab = new Text("Misión: Órgano constitucional que juzga el desempeño de los Magistrados Judiciales, Agentes Fiscales y Defensores Públicos por la supuesta comisión de delitos").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    Paragraph pCab = new Paragraph().add(titleCab).setMultipliedLeading(0.1f);
                    Text titleCab2 = new Text(" o mal desempeño en el ejercicio de sus funciones, velando por la correcta administración de justicia, en tutela de los derechos de los ciudadanos").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    Paragraph pCab2 = new Paragraph().addTabStops(tabStops).add(new Tab()).add(titleCab2).add(new Tab()).add(new Tab()).setMultipliedLeading(0.1f);

                    canvas.add(pCab);
                    canvas.add(pCab2);
                    canvas.close();

                    // Pie
                    Rectangle rectPie = new Rectangle(88, 10, 480, 90);
                    under.rectangle(rectPie);
                    // under.stroke();
                    Canvas canvasPie = new Canvas(under, pdfDoc, rectPie);
                    PdfFont fontPie = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                    PdfFont boldPie = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC);

                    Text titlePie = new Text("Visión: Ser una institución confiable y reconocida por la aplicación de procesos transparentes, objetivos e imparciales en el cumplimiento de su rol").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    Text titlePie2 = new Text("constitucional, para el fortalecimiento del estado de derecho, en beneficio de la sociedad").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    Text titlePie3 = new Text("4 de Mayo esq. Oliva - Ed. El Ciervo	                                                                                                                                    ,Tel: (595 21) 442662").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    Text titlePie4 = new Text("www.jem.gov.py                                                                                                                                                                         Asunción - Paraguay").setFont(bold).setFontSize(8).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    Paragraph pPie = new Paragraph().add(titlePie).setMultipliedLeading(0.1f);
                    Paragraph pPie2 = new Paragraph().addTabStops(tabStops).add(new Tab()).add(titlePie2).add(new Tab()).add(new Tab()).setMultipliedLeading(1.0f);
                    Paragraph pPie3 = new Paragraph().add(titlePie3).setMultipliedLeading(0.1f);
                    Paragraph pPie4 = new Paragraph().add(titlePie4).setMultipliedLeading(0.1f);

                    canvasPie.add(pPie);
                    canvasPie.add(pPie2);
                    canvasPie.add(pPie3);
                    canvasPie.add(pPie4);
                    canvasPie.close();

                    under.saveState();
                }
            }
        }

        doc.close();

        File archivo = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre + "_temp");
        archivo.delete();
        //}
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getArchivo() != null) {
                    try {
                        if (Constantes.TIPO_ACTUACION_ACTA_SESION.equals(docImprimir.getTipoActuacion().getId())) {
                            fileByte = Files.readAllBytes(new File(par.getRutaActas() + "/" + docImprimir.getArchivo()).toPath());
                        } else {
                            fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + docImprimir.getArchivo()).toPath());
                        }
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
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
                } else if (docImprimir.getFormato() != null) {
                    generarArchivo();
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;
    }

    private String sustituirTags(String texto, List<Metadatos> listaMetadatos) {
        if (texto != null) {
            for (Metadatos metadato : listaMetadatos) {
                texto = texto.replace(inicio + metadato.getClave().trim() + fin, metadato.getValor().trim());
            }
        }
        return texto;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /*
    public void actualizarPrevisualizacion(Documentos documento) {

        try {
            // if uploaded doc then use HWPF else if uploaded Docx
            // file use XWPFDocument

            setSelected(documento);

            Date fecha = ejbFacade.getSystemDate();
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FORMATO_FECHA_CORTO);

            listaMetadatos = new ArrayList<>();
            listaMetadatosNoEncontrados = new ArrayList<>();

            List<Metadatos> metadatos = ejbFacade.getEntityManager().createNamedQuery("Metadatos.findByGestion", Metadatos.class).setParameter("gestion", documento.getGestion()).getResultList();

            // Agregar metadatos generales
            agregarMetadatosGenerales(metadatos);

            // String inicio = "<&$";
            // String fin = "$&>";
            Document document = new Document();
            document.loadFromFile(getSelected().getPlantillaFormulario().getRuta());

            System.out.print("Texto: " + document.getText());

            //XWPFDocument doc = new XWPFDocument(OPCPackage.open(documento.getPlantillaFormulario().getRuta()));
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(documento.getPlantillaFormulario().getRuta()));
            for (XWPFParagraph p : doc.getParagraphs()) {
                String todo = p.getText();

                int idx = 0;
                int idx2 = 0;
                int cont = 0;
                while (idx != -1) {
                    idx = todo.indexOf(inicio, idx2);

                    if (idx != -1) {
                        idx += inicio.length();
                        idx2 = todo.indexOf(fin, idx);

                        if (idx2 != -1) {

                            String clave = todo.substring(idx, idx2).trim();
                            // System.out.println("Encontro: " + clave);

                            if (!Constantes.METADATO_MONTO_EN_LETRAS.equals(clave)) {

                                boolean yaAgregado = false;
                                for (Metadatos met : listaMetadatos) {
                                    if (met.getClave().equals(clave)) {
                                        yaAgregado = true;
                                    }
                                }

                                if (!yaAgregado) {
                                    boolean encontro = false;
                                    for (Metadatos metadato : metadatos) {
                                        if (metadato.getClave().equals(clave)) {

                                            listaMetadatos.add(metadato);
                                            encontro = true;
                                            break;
                                        }
                                    }

                                    if (!encontro) {
                                        Metadatos met = metadatosController.prepareCreate(null);
                                        met.setClave(clave);
                                        met.setValor("");

                                        cont++;
                                        met.setId(Integer.valueOf(sdf.format(fecha) + cont));

                                        listaMetadatos.add(met);
                                    }
                                }
                            }

                            idx2 = idx2 + fin.length();
                        } else {
                            idx2 = idx;
                        }
                    }
                }

                //        List<XWPFRun> runs = p.getRuns();
                //        if (runs != null) {
                //            boolean encontro = false;
                //            for (XWPFRun r : runs) {
                //                String text = r.getText(0);
                //                if(encontro){
                //                    System.out.println("Encontro: " + text);
                //                    encontro = false;
                //                }
                //                if (text != null && text.contains("<&$")) {
                //                    encontro = true;
                //                }
                //            }
                //        }
            }

            //        for (XWPFTable tbl : doc.getTables()) {
            //            for (XWPFTableRow row : tbl.getRows()) {
            //                for (XWPFTableCell cell : row.getTableCells()) {
            //                    for (XWPFParagraph p : cell.getParagraphs()) {
            //                        for (XWPFRun r : p.getRuns()) {
            //                            String text = r.getText(0);
            //                            if (text != null && text.contains("$$key$$")) {
            //                                text = text.replace("$$key$$", "abcd");
            //                                r.setText(text, 0);
            //                            }
            //                        }
            //                    }
            //                }
            //            }
            //        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(GestionesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     */
 /*
    public String getPrevisualizar() {

        try {

            if (actuacion != null) {
                if (actuacion.getFormato() != null) {
                    if (actuacion.getFormato().getArchivo() != null) {
                        if (!"".equals(actuacion.getFormato().getArchivo())) {

                            XWPFDocument doc = new XWPFDocument(OPCPackage.open(par.getRutaPlantillas() + "/" + actuacion.getFormato().getArchivo()));

                            WordReplacer wr = new WordReplacer(doc);
                            
                            List<Metadatos> listaMetadatos = new ArrayList<>();
                            SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
                            Date fecha = ejbFacade.getSystemDateOnly();
                            Metadatos meta = new Metadatos("fecha", format.format(fecha));
                            
                            listaMetadatos.add(meta);
                            
                            Metadatos meta2 = new Metadatos("nro", actuacion.getFormato().getTipoActuacion().getSecuencia());
                            
                            listaMetadatos.add(meta2);
                            
                            Metadatos meta3 = new Metadatos("texto", (actuacion.getTexto() == null)?"":actuacion.getTexto());
                            
                            listaMetadatos.add(meta3);

                            for (Metadatos metadato : listaMetadatos) {

                                wr.replaceWordsInText(inicio + metadato.getClave().trim() + fin, metadato.getValor().trim());
                                
                            }

                            // ReemplazarWord rr = new ReemplazarWord();
                            // rr.iterateThroughParagraphs(doc, mapa);
                            // rr.iterateThroughFooters(doc, mapa);
                            // rr.iterateThroughTables(doc, mapa);
                            // for (XWPFParagraph p : doc.getParagraphs()) {
                            //     String todo = p.getText();
                            //     
                            //     for(Metadatos metadato : listaMetadatos){
                            //         todo = todo.replace(inicio + metadato.getClave().trim() + fin, metadato.getValor());
                            //         PositionInParagraph pos = 
                            //         p.searchText(sessionId, startPos)
                            //     }
                            //     
                            // }
                           
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);
                            
                            nombrePrevisualizar = session.getId() + "_" + simpleDateFormat.format(fecha) + ".pdf";

                            PdfOptions options = PdfOptions.create();

                            OutputStream out = new FileOutputStream(new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombrePrevisualizar));
                            PdfConverter.getInstance().convert(doc, out, options);

                            super.save(null);

                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo generar el documento");
        }
        return url + "/tmp/" + nombrePrevisualizar;

    }
     */
    public void refrescar() {
        buscar();
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        estadosProcesoFiltro = new ArrayList<>();

        imagenSorteo = "images/sorteo_inicio2.jpg";
        sorteado = null;

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        // accion = params.get("tipo");

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();

        if (session.getAttribute("tipo") != null) {
            accion = (String) session.getAttribute("tipo");

            vieneDePestanas = session.getAttribute("pestanas") != null;

            paramPestana = (String) session.getAttribute("pestanas");
            session.removeAttribute("tipo");
        } else {
            accion = params.get("tipo");
            vieneDePestanas = params.get("pestanas") != null;
            paramPestana = (String) params.get("pestanas");
        }

        if (session.getAttribute("otro") != null) {
            otro = (String) session.getAttribute("otro");
            session.removeAttribute("otro");
        } else {
            otro = params.get("otro");
        }

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");
        usuario = (Usuarios) session.getAttribute("Usuarios");

        filtroOrden = "1";
        // ckeditorConfig = url + "/config.js";
        if (Constantes.ACCION_CONSULTA.equals(accion)) {
            titulo = "CONSULTA EXPEDIENTES";
            visible = true;
            categoriaActuacion = null;
        } else if (Constantes.ACCION_CONSULTA_ENJUICIAMIENTOS.equals(accion)) {
            titulo = "CONSULTA EXPEDIENTES";
            visible = true;
            categoriaActuacion = null;
        } else if (Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion)) {
            titulo = tituloActuacion = tituloVerActuacion = botonRegistrar = "REGISTRAR ACTUACIÓN";
            labelTipo = "Tipo Actuación";
            categoriaActuacion = new ExpCategoriasActuacion(1);
            visible = false;
        } else if (Constantes.ACCION_CREAR.equals(accion)) {
            titulo = tituloActuacion = "CREAR EXPEDIENTE";
            labelTipo = "Tipo documento";
            categoriaActuacion = new ExpCategoriasActuacion(Constantes.CATEGORIA_ACTUACION_PRESENTACION);
            visible = true;
        } else {
            //JsfUtil.addErrorMessage("Accion no permitida: " + accion);

            System.out.println("Accion no permitida: " + accion);
            return;
        }

        listaActuacionesDesglose = new ArrayList<>();

        listaObjetosActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findByCategoriaActuacion", ExpObjetosActuacion.class).setParameter("categoriaActuacion", categoriaActuacion).getResultList();

        departamento = departamentoController.prepareCreate(null);
        departamento.setId(40); // Secretaria
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE);
        tipoDoc = tiposDocumentosJudicialesController.prepareCreate(null);
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_JU);
        nroCausa = "";

        String act = (String) session.getAttribute("actuacionId");

        session.removeAttribute("actuacionId");

        Object esAppObj = session.getAttribute("esApp");

        esApp = (esAppObj == null ? false : (boolean) esAppObj);

        pantalla = Constantes.NO;

        // fechaDesde = ejbFacade.getSystemDateOnly(Constantes.FILTRO_CANT_DIAS_ATRAS);
        // fechaHasta = ejbFacade.getSystemDateOnly();
        fechaDesde = null;
        fechaHasta = null;

        listaTiposActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_OFICIO).getResultList();
        listaTiposActuacion.add(ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PROVIDENCIA).getSingleResult());
        try {
            ExpTiposActuacion ta = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE).getSingleResult();
            listaTiposActuacion.add(ta);
        } catch (Exception e) {

        }
        ExpTiposActuacion ai = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();

        String desc = ai.getDescripcion();
        ai.setDescripcion(desc + " A.I.");
        listaTiposActuacion.add(ai);
        ExpTiposActuacion sd = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_SD).getSingleResult();
        sd.setDescripcion(desc + " S.D.");
        listaTiposActuacion.add(sd);

        try {
            listaTiposActuacion.add(ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_OFICIO_CORTE).getSingleResult());
        } catch (Exception e) {

        }

        filtroRadio = "1";

        if (Constantes.ROL_RELATOR.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_PRESIDENTE.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_PRESIDENTE_RES_ACTAS.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_PRESIDENTE_OF_PROV_RES_ACTAS.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_EXMIEMBRO.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_MIEMBRO_CON_PERMISO.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_MIEMBRO_SUPLENTE.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_MIEMBRO_REEMPLAZANTE.equals(rolElegido.getId())) {

            List<ExpPersonasAsociadas> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();
            if (!lista.isEmpty()) {
                personaSelected = lista.get(0).getPersona();
            } else {
                personaSelected = personaUsuario;
            }
        } else {
            personaSelected = personaUsuario;
        }

        if (act != null) {
            verActuacion(act);

            if (Constantes.SI.equals(otro) && !(Constantes.ACCION_REGISTRAR_PROVIDENCIA.equals(accion) || Constantes.ACCION_REGISTRAR_RESOLUCION.equals(accion))) {
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('ExpAcusacionesOtroArchivoDialog').show();");
            }
            return;
        }

        if (filtroURL.verifPermiso(Constantes.GENERAR_ACUSACION, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            prepareCreateAcusacion();
        } else if (filtroURL.verifPermiso(Constantes.GENERAR_INVESTIGACION_PRELIMINAR, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            prepareCreateInvestigacionPreliminar();
        } else if (filtroURL.verifPermiso(Constantes.GENERAR_PEDIDO_DESAFUERO, rolElegido.getId()) && Constantes.ACCION_CREAR.equals(accion)) {
            prepareCreatePedidoDesafuero();
        } else {
            if (renderedEstadoProcesoEE()) {
                if (Constantes.ACCION_CONSULTA_ENJUICIAMIENTOS.equals(accion)) {
                    estadosProcesoFiltro = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosProcesoExpedienteElectronico.findByEnProceso", ExpEstadosProcesoExpedienteElectronico.class).setParameter("enProceso", true).getResultList();

                    filtroOrden = "2";
                    buscar();
                }
            }

        }

    }

    public boolean renderedMostrarExpedienteWeb() {
        return filtroURL.verifPermiso(Constantes.PERMISO_EXPONER_EXPEDIENTE_PDF, rolElegido.getId());
    }

    public void actualizarMostrarExpedienteWeb() {
        if (getSelected() != null) {
            super.save(null);
        }
    }

    public Personas getRandomElement(List<Personas> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    private void verActuacion(String actId) {
        selectedActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", Integer.valueOf(actId)).getSingleResult();
        nroCausa = selectedActuacion.getDocumentoJudicial().getCausa();
        buscar();

        setSelected(selectedActuacion.getDocumentoJudicial());

        if (filtroURL.verifPermiso(Constantes.PERMISO_FILTRO_RADIO, rolElegido.getId())) {
            filtroRadio = "2";
        } else {
            filtroRadio = "1";
        }

        prepareVerActuaciones();

        if (Constantes.TIPO_ACTUACION_NOTIFICACION.equals(selectedActuacion.getTipoActuacion().getId())) {
            if (!selectedActuacion.isLeido()) {
                selectedActuacion.setLeido(true);
                expActuacionesController.setSelected(selectedActuacion);
                expActuacionesController.save(null);
                expActuacionesController.setSelected(null);
            }
        }

        if (listaActuaciones != null) {
            itemsSize = listaActuaciones.size();
            newItemIx = listaActuaciones.indexOf(selectedActuacion);
        }

        PrimeFaces.current().executeScript("getNewItemPos()");

        // RequestContext.getCurrentInstance().execute("PF('widgetVar').getPaginator().setPage(pageno)");
    }

    public void actualizarRevisado(ExpPersonasFirmantesPorActuaciones act) {
        act.setRevisado(true);
        act.setFechaHoraRevisado(ejbFacade.getSystemDate());
        act.setPersonaRevisado(personaUsuario);
        personasFirmantesPorActuacionesController.setSelected(act);
        personasFirmantesPorActuacionesController.save(null);
        /*
        if (act.getActuacion() != null) {
            if (act.getActuacion().getTipoActuacion() != null) {
                if (Constantes.TIPO_ACTUACION_OFICIO.equals(act.getActuacion().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getActuacion().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getActuacion().getTipoActuacion().getId())) {
                    List<AntecedentesRolesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByPersona", AntecedentesRolesPorPersonas.class).setParameter("persona", act.getPersonaFirmante()).getResultList();
                    for (AntecedentesRolesPorPersonas rol : lista) {
                        if (Constantes.ROL_PRESIDENTE.equals(rol.getRoles().getId())) {
                            ExpEstadosActuacion est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE).getSingleResult();
                            actuacion.setEstado(est);
                        }
                    }
                }
            }
        }
         */

    }

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
        prepareEditFirmantes(act, true);
    }

    public void prepareEditFirmantes(ExpActuaciones act, boolean incluirSecretarios) {

        if (act != null) {

            selectedActuacion = act;

            if (incluirSecretarios) {
                listaPosiblesFirmantes = ejbFacade.getEntityManager().createNamedQuery("Personas.findByPermisoEstadoNoInhibidos", Personas.class).setParameter("funcion", Constantes.PERMISO_FIRMAR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).setParameter("documentoJudicial", act.getDocumentoJudicial()).getResultList();
            } else {
                List<Integer> roles = new ArrayList<>();

                roles.add(Constantes.ROL_SECRETARIO);
                roles.add(Constantes.ROL_EXSECRETARIO);

                listaPosiblesFirmantes = ejbFacade.getEntityManager().createNamedQuery("Personas.findByPermisoEstadoNoInhibidosButRoles", Personas.class).setParameter("funcion", Constantes.PERMISO_FIRMAR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).setParameter("documentoJudicial", act.getDocumentoJudicial()).setParameter("roles", roles).getResultList();
            }
            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("estado", new Estados("AC")).getResultList();

            listaPersonasFirmantes = new ArrayList<>();
            personaFirmante = null;

            for (ExpPersonasFirmantesPorActuaciones per : lista) {
                per.getPersonaFirmante().setEstado("AC");
                per.getPersonaFirmante().setFirmado(per.isFirmado());
                per.getPersonaFirmante().setRevisado(per.isRevisado());
                per.getPersonaFirmante().setRevisadoPor(per.getPersonaRevisado() == null ? "" : per.getPersonaRevisado().getNombresApellidos());
                per.getPersonaFirmante().setFechaHoraRevisado(per.getFechaHoraRevisado());
                per.getPersonaFirmante().setFechaHoraFirmado(per.getFechaHoraFirma());
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

    public void prepareVerFirmantes(ExpActuaciones act) {

        if (act != null) {

            selectedActuacion = act;

            listaEstadosFirmas = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("estado", new Estados("AC")).getResultList();

        }

    }

    public void prepareVerifVisible(ExpActuaciones act) {
        if (act != null) {

            selectedActuacion = act;

            mensajeAviso = act.getDescripcion();
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

    public void prepareEditInhibiciones() {

        if (getSelected() != null) {
            inhibido = null;
            listaInhibidosAlta = ejbFacade.getEntityManager().createNamedQuery("Personas.findByPermisoEstado", Personas.class).setParameter("funcion", Constantes.PERMISO_FIRMAR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            List<ExpPersonasInhibidasPorDocumentosJudiciales> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasInhibidasPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPersonasInhibidasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).setParameter("estado", new Estados("AC")).getResultList();

            listaInhibidosAdmin = new ArrayList<>();

            for (ExpPersonasInhibidasPorDocumentosJudiciales per2 : lista3) {
                per2.getPersona().setEstado("AC");
                listaInhibidosAdmin.add(per2.getPersona());
            }
        }
    }

    public void prepareEditRestringidos() {

        if (getSelected() != null) {
            restringido = null;
            listaRestringidosAlta = ejbFacade.getEntityManager().createNamedQuery("Personas.findByEstado", Personas.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            List<ExpPersonasHabilitadasPorDocumentosJudiciales> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasHabilitadasPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPersonasHabilitadasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).setParameter("estado", new Estados("AC")).getResultList();

            listaRestringidosAdmin = new ArrayList<>();

            for (ExpPersonasHabilitadasPorDocumentosJudiciales per2 : lista3) {
                per2.getPersona().setEstado("AC");
                listaRestringidosAdmin.add(per2.getPersona());
            }
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

    public void prepareEditActuacion(ExpActuaciones item) {
        actuacion = item;

        estadoActuacion = actuacion.getEstado();

        estadoActuacionOriginal = actuacion.getEstado();

        textoAnterior = actuacion.getTexto();

        if (getSelected() != null) {
            caratulaMostrar = getSelected().getCaratulaString();
        }

        List<ExpActuaciones> act = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", actuacion.getId()).getResultList();

        if (!act.isEmpty()) {
            if (act.get(0) != null) {
                if (act.get(0).getArchivo() != null) {
                    if (act.get(0).getEstado() != null) {
                        // Si la actuación ya está convertida a PDF (act.get(0).getArchivo() != null), solo se le permite a los relatores entrar a la pantalla de edición para marcar como revisado
                        if (!(Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES.equals(act.get(0).getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(act.get(0).getEstado().getCodigo()))) {
                            JsfUtil.addErrorMessage("Esta actuación no se puede editar");
                            return;
                        }
                    } else {
                        JsfUtil.addErrorMessage("Esta actuación no se puede editar");
                        return;
                    }
                }
            }

            actuacion = act.get(0);

            List<Integer> lista3 = new ArrayList<>();
            lista3.add(Constantes.ROL_SECRETARIO);
            lista3.add(Constantes.ROL_EXSECRETARIO);
            listaSecretarios = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", lista3).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            if (listaSecretarios.isEmpty() && actuacion.getSecretario() != null) {
                listaSecretarios.add(actuacion.getSecretario());
            }

            if (actuacion.getSecretario() != null) {
                boolean encontro = false;
                Personas per = null;
                int i = 0;
                for (; i < listaSecretarios.size(); i++) {

                    per = listaSecretarios.get(i);
                    if (per.equals(actuacion.getSecretario())) {
                        encontro = true;
                    }
                }

                if (!encontro && i != 0) {
                    listaSecretarios.add(actuacion.getSecretario());
                }
            }

            /*
            Integer iteracion = 1;
            if (Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES.equals(act.get(0).getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(act.get(0).getEstado().getCodigo())) {
                iteracion = 2;
            }
            listaEstadosActuacion = obtenerEstadosActuacion(actuacion.getEstado(), iteracion, par.getTipoCircuitoFirmaActuaciones());
             */
            listaEstadosActuacion = obtenerEstadosActuacion(actuacion.getEstado());

            /*
            listaEstadosActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByRol", ExpEstadosActuacion.class).setParameter("rol", rolElegido).setParameter("iteracion", iteracion).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();

            if (actuacion.getEstado() != null) {
                boolean encontro = false;
                for (ExpEstadosActuacion est : listaEstadosActuacion) {
                    if (est.equals(actuacion.getEstado())) {
                        encontro = true;
                        break;
                    }
                }

                if (!encontro) {
                    listaEstadosActuacion.add(actuacion.getEstado());
                }
            }
             */

 /*
            List<Personas> listaPreo = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", "AC").getResultList();
            List<Personas> listaMiembros = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_MIEMBRO).setParameter("estado", "AC").getResultList();
            listaPreo.addAll(listaMiembros);
             */
            List<Integer> listaRol = new ArrayList<>();
            listaRol.add(Constantes.ROL_PRESIDENTE);
            listaRol.add(Constantes.ROL_MIEMBRO);
            listaRol.add(Constantes.ROL_EXPRESIDENTE);
            listaRol.add(Constantes.ROL_EXMIEMBRO);
            listaRol.add(Constantes.ROL_MIEMBRO_CON_PERMISO);
            listaRol.add(Constantes.ROL_MIEMBRO_SUPLENTE);
            listaRol.add(Constantes.ROL_MIEMBRO_REEMPLAZANTE);

            List<Personas> listaPreo = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", listaRol).setParameter("estado", "AC").getResultList();

            List<ExpPersonasInhibidasPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasInhibidasPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPersonasInhibidasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).setParameter("estado", new Estados("AC")).getResultList();

            listaPreopinantes = new ArrayList<>();
            for (Personas per : listaPreo) {
                boolean encontro = false;
                for (ExpPersonasInhibidasPorDocumentosJudiciales inh : lista2) {
                    if (per.equals(inh.getPersona())) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    listaPreopinantes.add(per);
                }
            }

            if (item.getPreopinante() != null) {
                boolean encontro = false;
                for (Personas per : listaPreopinantes) {
                    if (per.equals(item.getPreopinante())) {
                        encontro = true;
                    }
                }

                if (!encontro) {
                    listaPreopinantes.add(item.getPreopinante());
                }
            }

            prepareEditFirmantes(item, false);

            if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId())) {
                List<Resoluciones> lista = ejbFacade.getEntityManager().createNamedQuery("Resoluciones.findById", Resoluciones.class).setParameter("id", actuacion.getResolucion().getId()).getResultList();
                if (!lista.isEmpty()) {
                    resolucion = lista.get(0);
                }
            }

            nombre = null;

            docImprimir = actuacion;

            buscarFormatos();

            listaDocumentosJudicialesCorte = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findByDocumentoJudicial", DocumentosJudicialesCorte.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            PrimeFaces.current().ajax().update("DocumentosEditActuacionForm");
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('DocumentosEditActuacionDialog').show();");
        }

    }

    private List<ExpEstadosActuacion> obtenerEstadosActuacion(ExpEstadosActuacion estado) {

        Integer iteracion = 1;
        if (estadoActuacion != null) {
            if (Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES.equals(estadoActuacion.getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(estadoActuacion.getCodigo())) {
                iteracion = 2;
            }
        }

        Integer circuito = 1;
        if (Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(actuacion.getTipoActuacion().getId())
                || actuacion.getSecretario() != null) {
            if (esExSecretario(actuacion.getSecretario())) {
                circuito = 4;
            } else if (esSecretario(actuacion.getSecretario())) {
                circuito = 3;
            }
        }

        return obtenerEstadosActuacion(estado, iteracion, circuito);
    }

    private List<ExpEstadosActuacion> obtenerEstadosActuacion(ExpEstadosActuacion estado, Integer iteracion, Integer circuito) {

        List<ExpEstadosActuacion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByRol", ExpEstadosActuacion.class).setParameter("rol", rolElegido).setParameter("iteracion", iteracion).setParameter("tipoCircuitoFirma", circuito).getResultList();

        if (estado != null) {
            boolean encontro = false;
            for (ExpEstadosActuacion est : lista) {
                if (est.equals(estado)) {
                    encontro = true;
                    break;
                }
            }

            if (!encontro) {
                lista.add(estado);
            }
        }

        return lista;
    }

    public ExpActuaciones prepareCreateActuacion(boolean parEsPrimerEscrito, boolean otro) {

        if (getSelected() != null) {
            // actuacion = expActuacionesController.prepareCreate(null);
            actuacion = new ExpActuaciones();

            docId = null;

            List<ExpPersonasInhibidasPorDocumentosJudiciales> listaInh = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasInhibidasPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPersonasInhibidasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).setParameter("estado", new Estados("AC")).getResultList();

            listaInhibidosAdmin = new ArrayList<>();

            for (ExpPersonasInhibidasPorDocumentosJudiciales per : listaInh) {
                per.getPersona().setEstado("AC");
                listaInhibidosAdmin.add(per.getPersona());
            }

            listaSecretarios = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
            if (!listaSecretarios.isEmpty()) {
                actuacion.setSecretario(listaSecretarios.get(0));
            }

            List<Integer> lista2 = new ArrayList<>();
            lista2.add(Constantes.ROL_SECRETARIO);
            lista2.add(Constantes.ROL_EXSECRETARIO);
            listaSecretarios = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", lista2).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            observacion = "";
            estadoProcesal = "";

            listaFormatos = null;

            listaPersonasFirmantes = new ArrayList<>();

            estadoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_EN_PROYECTO).getSingleResult();

            estadoActuacionOriginal = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_EN_PROYECTO).getSingleResult();

            // listaEstadosActuacion = obtenerEstadosActuacion(estadoActuacion, 1, par.getTipoCircuitoFirmaActuaciones());
            actuacion.setVisible(visible);

            /*
            if (Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion)) {
                esPrimerEscrito = true;
            } else if (Constantes.ACCION_REGISTRAR_RESURSO.equals(accion)) {
                esPrimerEscrito = false;
            } else if (Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion)) {
                esPrimerEscrito = false;
            }
             */
            esPrimerEscrito = parEsPrimerEscrito;

            /*
            if (parEsPrimerEscrito && Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion)) {
                actuacionPadre = null;
            } else {
                ExpObjetosActuacion obj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_PRESENTACION).getSingleResult();
                actuacion.setObjetoActuacion(obj);
            }

             */
            ExpObjetosActuacion obj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_PRESENTACION).getSingleResult();
            actuacion.setObjetoActuacion(obj);

            caratulaMostrar = getSelected().getCaratulaString();

            nombre = null;

            docImprimir = actuacion;

            /*
            List<Personas> listaPreo = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", "AC").getResultList();
            List<Personas> listaMiembros = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_MIEMBRO).setParameter("estado", "AC").getResultList();
            listaPreo.addAll(listaMiembros);
             */
            List<Integer> listaRol = new ArrayList<>();
            listaRol.add(Constantes.ROL_PRESIDENTE);
            listaRol.add(Constantes.ROL_MIEMBRO);
            listaRol.add(Constantes.ROL_EXPRESIDENTE);
            listaRol.add(Constantes.ROL_EXMIEMBRO);
            listaRol.add(Constantes.ROL_MIEMBRO_CON_PERMISO);
            listaRol.add(Constantes.ROL_MIEMBRO_SUPLENTE);
            listaRol.add(Constantes.ROL_MIEMBRO_REEMPLAZANTE);

            List<Personas> listaPreo = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", listaRol).setParameter("estado", "AC").getResultList();

            List<ExpPersonasInhibidasPorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasInhibidasPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPersonasInhibidasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).setParameter("estado", new Estados("AC")).getResultList();

            listaPreopinantes = new ArrayList<>();
            for (Personas per : listaPreo) {
                boolean encontro = false;
                for (ExpPersonasInhibidasPorDocumentosJudiciales inh : lista) {
                    if (per.equals(inh.getPersona())) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {
                    listaPreopinantes.add(per);
                }
            }

            listaActuacionesDesglose = obtenerActuacionesDesglose();

            observacion = "";
            estadoProcesal = "";
            resolucion = resolucionesController.prepareCreate(null);
            resolucion.setCaratula(getSelected().getCaratulaString());

            actualizarAcusados(getSelected());

            listaDocumentosJudicialesCorte = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findByDocumentoJudicial", DocumentosJudicialesCorte.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            if (!otro) {
                actuacion.setTipoActuacion(null);
                PrimeFaces.current().ajax().update("DocumentosCreateActuacionForm");
                // PrimeFaces.current().ajax().update("DocumentosCreateActuacionForm2");
            } else {
                actuacion.setTipoActuacion(ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PRESENTACION).getSingleResult());

                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('DocumentosCreateOtraActuacionDialog').show();");
            }

        }
        return actuacion;
    }

    private List<ExpActuaciones> obtenerActuacionesDesglose() {

        List<ExpActuaciones> lista = new ArrayList<>();
        for (ExpActuaciones act : listaActuaciones) {
            // if (!act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE)) {
            if (act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_RESOLUCION) ||
                act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_EXPEDIENTE_JEM) ||
                act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_OFICIO) ||
                act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_SD)) {
                lista.add(act);
            }
        }

        return lista;
    }

    // update=":ExpActuacionesViewForm" oncomplete="PF('ExpActuacionesViewDialog').show();"
    public void prepareVerActuaciones() {
        if (getSelected() != null) {
            Personas per = null;

            nroCausaExp = getSelected().getCausa();

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

            ExpTiposParte tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ACUSADO).getSingleResult();

            List<ExpPersonasAcusacionPorDocumentosJudiciales> listaPer = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAcusacionPorDocumentosJudiciales.findByDocumentoJudicialANDTipoParte", ExpPersonasAcusacionPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).setParameter("tipoParte", tipo).getResultList();

            ExpPersonasAcusacion per2 = null;
            listaPersonasInformadas = new ArrayList<>();
            for (int i = 0; i < listaPer.size(); i++) {
                per2 = listaPer.get(i).getPersona();
                per2.setEstado(listaPer.get(i).getEstado().getCodigo());
                listaPersonasInformadas.add(per2);
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

            // ExpTiposParte tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParte.findById", ExpTiposParte.class).setParameter("id", Constantes.TIPO_PARTE_ACUSADO).getSingleResult();
            for (PersonasPorDocumentosJudiciales parte : lista2) {
                listaPartes.add(new ExpPartesPorDocumentosJudiciales(new ExpPartesPorDocumentosJudicialesPK(parte.getPersona().getId(), getSelected().getId()), parte.getPersona(), getSelected(), tipo));
            }

            prepareEditInhibiciones();
            prepareEditRestringidos();

            buscarActuaciones();

            PrimeFaces.current().ajax().update("ExpActuacionesViewForm");

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('ExpActuacionesViewDialog').show();");
        }
    }

    public void prepareAsociarExpedienteCorte() {
        listaCircunscripcionesCorte = ejbFacade.getEntityManager().createNamedQuery("DepartamentosPersona.findAllButNull", DepartamentosPersona.class).getResultList();
        listaMateriasCorte = ejbFacade.getEntityManager().createNamedQuery("ExpMaterias.findAll", ExpMaterias.class).getResultList();
        listaRespuestaConsultaCausas = new ArrayList<>();
        selectedCausa = null;
        circunscripcionSelected = null;
        materiaSelected = null;
        asociarCaratula = null;
        asociarCausa = null;
        asociarAno = null;

        obtenerExpedientesCorteAsociados();

    }

    private void obtenerExpedientesCorteAsociados() {

        if (getSelected() != null) {
            List<ExpDocumentosJudicialesCortePorDocumentosJudiciales> docs = ejbFacade.getEntityManager().createNamedQuery("ExpDocumentosJudicialesCortePorDocumentosJudiciales.findByDocumentoJudicial", ExpDocumentosJudicialesCortePorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected().getId()).getResultList();

            listaRespuestaConsultaCausasAsociadas = new ArrayList<>();

            for (ExpDocumentosJudicialesCortePorDocumentosJudiciales doc : docs) {
                if (doc.getDocumentoJudicialCorte() != null) {
                    RespuestaConsultaCausas resp = new RespuestaConsultaCausas(doc.getDocumentoJudicialCorte().getCodCasoJudicial(), doc.getDocumentoJudicialCorte().getNroExpedienteCircunscripcion(), doc.getDocumentoJudicialCorte().getNroExpedienteMateria(), doc.getDocumentoJudicialCorte().getNroExpedienteTipoDependencia(), doc.getDocumentoJudicialCorte().getNroExpedienteNroDependencia(), doc.getDocumentoJudicialCorte().getNroExpedienteAnio(), doc.getDocumentoJudicialCorte().getNroExpedienteNumero(), doc.getDocumentoJudicialCorte().getCaratula(), doc.getDocumentoJudicialCorte().getCodDespachoJudicial(), doc.getDocumentoJudicialCorte().getDescripcionDespachoJudicial(), doc.getDocumentoJudicialCorte().getEstadoCasoDespacho(), doc.getDocumentoJudicialCorte().getFechaPrimerActo(), doc.getDocumentoJudicialCorte().getNombreJuez(), doc.getDocumentoJudicialCorte().getDescripcionMoneda(), doc.getDocumentoJudicialCorte().getMonto(), doc.getDocumentoJudicialCorte().getProceso(), doc.getDocumentoJudicialCorte().getFase(), doc.getDocumentoJudicialCorte().getNumeroLiquidacion(), doc.getDocumentoJudicialCorte().getAuxiliarJusticiaPresentacion(), doc.getDocumentoJudicialCorte());
                    listaRespuestaConsultaCausasAsociadas.add(resp);
                }
            }
        }
    }

    public void prepareVerPartesCorte() {
        PrimeFaces.current().ajax().update("ExpPartesCorteViewForm");

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('ExpPartesCorteViewDialog').show();");
    }

    public void prepareVerIntervinientesCorte() {
        PrimeFaces.current().ajax().update("ExpIntervinientesCorteViewForm");

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('ExpIntervinientesCorteViewDialog').show();");
    }

    public void buscarProceso(DocumentosJudiciales doc, int lista, ExpProcesosDocumentoJudicialCorte pro) {
        if (pro != null) {

            List<ExpActuacionesCorte> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpActuacionesCorte.findByDocumentoJudicialANDCodProcesoCaso", ExpActuacionesCorte.class).setParameter("documentoJudicial", doc).setParameter("codProcesoCaso", pro.getCodProcesoCaso()).getResultList();

            if (lista == 1) {
                listaRegulacionCorte = new ArrayList<>();
            } else if (lista == 2) {
                listaExcepcionesCorte = new ArrayList<>();
            } else if (lista == 3) {
                listaRecursosCorte = new ArrayList<>();
            }

            for (ExpActuacionesCorte act : lista2) {
                if (lista == 1) {
                    listaRegulacionCorte.add(act);
                } else if (lista == 2) {
                    listaExcepcionesCorte.add(act);
                } else if (lista == 3) {
                    listaRecursosCorte.add(act);
                }

                /*if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte()))) {
                    listaActuacionesCorte.add(act);
                } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                    listaRegulacionCorte.add(act);
                } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                    listaExcepcionesCorte.add(act);
                } else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                    listaRecursosCorte.add(act);
                }
                 */
            }
        }
    }

    public void prepareElegirExpedienteCorte(DocumentosJudiciales doc) {
        if (doc != null) {
            listaDocumentosJudicialesCorte = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findByDocumentoJudicial", DocumentosJudicialesCorte.class).setParameter("documentoJudicial", doc.getId()).getResultList();

            PrimeFaces.current().ajax().update("DocumentosJudicialesElegirForm");
            PrimeFaces.current().executeScript("PF('DocumentosJudicialesElegirDialog').show();");
        } else {
            listaDocumentosJudicialesCorte = null;
        }
    }

    public void prepareVerExpedienteCorte(DocumentosJudiciales doc, DocumentosJudicialesCorte docCorte, boolean reemplazar) {

        if (doc != null) {
            if (docCorte != null) {
                try {

                    List<ExpConexiones> ws = ejbFacade.getEntityManager().createNamedQuery("ExpConexiones.findById", ExpConexiones.class).setParameter("id", Constantes.CONEXION_CORTE_VER_ACTUACIONES_ID).getResultList();

                    if (ws.isEmpty()) {
                        JsfUtil.addErrorMessage("No se encuentran parametros para conexion con la Corte");
                        return;
                    }

                    expedienteCorte = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findById", DocumentosJudicialesCorte.class).setParameter("id", docCorte.getId()).getResultList();

                    if (expedienteCorte.isEmpty()) {
                        JsfUtil.addErrorMessage("No se encuentra el expediente corte asociado");
                        return;
                    }

                    List<ExpActuacionesCorte> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpActuacionesCorte.findByDocumentoJudicialCorte", ExpActuacionesCorte.class).setParameter("documentoJudicialCorte", docCorte).getResultList();

                    if (lista2.isEmpty() || reemplazar) {

                        listaRespuestaConsultaActuacionesCausa = new ArrayList<>();

                        CloseableHttpClient CLIENT = Utils.createAcceptSelfSignedCertificateClient();
                        HttpPost request = new HttpPost(ws.get(0).getIpServidor() + ":" + ws.get(0).getPuertoServidor() + ws.get(0).getUri());

                        JSONObject json = new JSONObject();
                        json.put("codCasoJudicial", expedienteCorte.get(0).getCodCasoJudicial());
                        json.put("usuario", ws.get(0).getUsuario());
                        json.put("codDespachoJudicial", expedienteCorte.get(0).getCodDespachoJudicial());
                        json.put("codCasoJEM", doc.getId());
                        json.put("codigoRol", -1);
                        json.put("codCircunscripcion", expedienteCorte.get(0).getCodCircunscripcion());

                        try {
                            ByteArrayEntity params = new ByteArrayEntity(Utils.encryptMsg(json.toString(), Utils.generateKey()));
                            request.addHeader("content-type", "application/octet-stream;charset=UTF-8");
                            request.addHeader("charset", "UTF-8");
                            request.setEntity(params);

                            HttpResponse response = (HttpResponse) CLIENT.execute(request);
                            if (response != null) {
                                HttpEntity entity = response.getEntity();

                                byte[] bytes = IOUtils.toByteArray(entity.getContent());

                                String respuesta = Utils.decryptMsg(bytes, Utils.generateKey());

                                if (respuesta != null) {
                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(respuesta);

                                        if (obj.getInt("codigo") == 0) {

                                            JSONArray lista = obj.getJSONArray("lista");

                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                            RespuestaConsultaActuacionesCausa resp = null;
                                            for (int j = 0; j < lista.length(); j++) {
                                                JSONObject rs = lista.getJSONObject(j);
                                                resp = new RespuestaConsultaActuacionesCausa(rs.getInt("codCasoJudicial"), rs.getInt("codActuacionCaso"), format.parse(rs.getString("fechaActuacion")),
                                                        format.parse(rs.getString("fechaHoraRegistro")), rs.getString("descripcionActuacion"), rs.getInt("codDespachoJudicial"),
                                                        rs.getString("descripcionDespachoJudicial"), rs.getString("descripcionTipoActuacion"), rs.getString("caratula"), rs.getInt("nroExpedienteNumero"),
                                                        rs.getInt("nroExpedienteAnio"), rs.getInt("codProcesoCaso"), rs.getInt("numeroActuacion"), rs.getInt("codTipoGrupoProceso"), rs.getString("descripcionTipoGrupoProceso"), rs.getInt("codCodProcesoJudicial"), rs.getString("descripcionProceso"), rs.getString("procesosDeRecursos"));
                                                listaRespuestaConsultaActuacionesCausa.add(resp);
                                            }

                                            JSONArray objetos = obj.getJSONArray("objetos");
                                            DatosObjetosCausa objeto = null;
                                            List<DatosObjetosCausa> objetosLista = new ArrayList<>();
                                            for (int k = 0; k < objetos.length(); k++) {
                                                JSONObject ob = objetos.getJSONObject(k);
                                                objeto = new DatosObjetosCausa(ob.getInt("codObjetoCausa"), ob.getInt("codCasoJudicial"), ob.getString("descripcionObjetoCausa"), ob.getString("gradoRealizacion"), ob.getString("principal"), ob.getString("ley"), ob.getString("articuloDelCodigo"));
                                                objetosLista.add(objeto);
                                            }

                                            JSONArray partes = obj.getJSONArray("partes");
                                            DatosPartes parte = null;
                                            List<DatosPartes> partesLista = new ArrayList<>();
                                            for (int k = 0; k < partes.length(); k++) {
                                                JSONObject part = partes.getJSONObject(k);
                                                parte = new DatosPartes(part.getInt("codParteCasoJudicial"), part.getInt("codCasoJudicial"), part.getString("nombresApellidos"), part.getString("nroDocumento"), part.getString("descripcionTipoDocumento"), part.getString("descripcionOcupacion"), part.getString("fechaNacimiento"), part.getString("sexo"), part.getString("descripcionTipoPersona"), part.getString("descripcionEstadoCivil"), part.getString("descripcionTipoParte"), part.getString("localidad"), part.getString("calle"), part.getString("telefono"), part.getString("email"), part.getString("tipoProfesional"), part.getString("nombreProfesional"), part.getString("representacion"), part.getString("profesionalHabilitado"), part.getString("descripcionTipoDomicilio"));
                                                partesLista.add(parte);
                                            }

                                            JSONArray intervinientes = obj.getJSONArray("intervinientes");
                                            DatosIntervinientes interviniente = null;
                                            List<DatosIntervinientes> intervinientesLista = new ArrayList<>();
                                            for (int k = 0; k < intervinientes.length(); k++) {
                                                JSONObject interv = intervinientes.getJSONObject(k);
                                                interviniente = new DatosIntervinientes(interv.getInt("codPersona"), interv.getInt("codCasoJudicial"), interv.getString("tipoInterviniente"), interv.getString("nombresApellidos"), interv.getString("descripcionTipoDocumento"), interv.getString("nroDocumento"), interv.getString("fechaNacimiento"), interv.getString("sexo"), interv.getString("descripcionTipoPersona"), interv.getString("descripcionOcupacion"), interv.getString("descripcionEstadoInterviniente"));
                                                intervinientesLista.add(interviniente);
                                            }

                                            for (ExpActuacionesCorte act : lista2) {
                                                expActuacionesCorteController.setSelected(act);
                                                expActuacionesCorteController.delete(null);
                                            }

                                            List<ExpProcesosDocumentoJudicialCorte> lista5 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosDocumentoJudicialCorte.findByDocumentoJudicialCorte", ExpProcesosDocumentoJudicialCorte.class).setParameter("documentoJudicialCorte", docCorte).getResultList();

                                            for (ExpProcesosDocumentoJudicialCorte act : lista5) {
                                                expProcesosDocumentoJudicialCorteController.setSelected(act);
                                                expProcesosDocumentoJudicialCorteController.delete(null);
                                            }

                                            List<ExpObjetosPorDocumentosJudicialesCorte> lista6 = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpObjetosPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", docCorte).getResultList();

                                            for (ExpObjetosPorDocumentosJudicialesCorte act : lista6) {
                                                objetosPorDocumentosJudicialesCorteController.setSelected(act);
                                                objetosPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> lista15 = ejbFacade.getEntityManager().createNamedQuery("ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", docCorte).getResultList();

                                            for (ExpDomiciliosPorPartesPorDocumentosJudicialesCorte act : lista15) {
                                                domiciliosPorPartesPorDocumentosJudicialesCorteController.setSelected(act);
                                                domiciliosPorPartesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> lista16 = ejbFacade.getEntityManager().createNamedQuery("ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", docCorte).getResultList();

                                            for (ExpProfesionalesPorPartesPorDocumentosJudicialesCorte act : lista16) {
                                                profesionalesPorPartesPorDocumentosJudicialesCorteController.setSelected(act);
                                                profesionalesPorPartesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpPartesPorDocumentosJudicialesCorte> lista7 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", docCorte).getResultList();

                                            for (ExpPartesPorDocumentosJudicialesCorte act : lista7) {
                                                partesPorDocumentosJudicialesCorteController.setSelected(act);
                                                partesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpIntervinientesPorDocumentosJudicialesCorte> lista8 = ejbFacade.getEntityManager().createNamedQuery("ExpIntervinientesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpIntervinientesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", docCorte).getResultList();

                                            for (ExpIntervinientesPorDocumentosJudicialesCorte act : lista8) {
                                                intervinientesPorDocumentosJudicialesCorteController.setSelected(act);
                                                intervinientesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            listaObjetosCorte = new ArrayList<>();

                                            ExpObjetosCorte objCorte = null;
                                            Date fecha = ejbFacade.getSystemDate();

                                            for (DatosObjetosCausa datos : objetosLista) {
                                                List<ExpObjetosCorte> lista9 = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosCorte.findByCodigoCorte", ExpObjetosCorte.class).setParameter("codigoCorte", String.valueOf(datos.getCodObjetoCausa())).getResultList();
                                                if (lista9.isEmpty()) {
                                                    objCorte = new ExpObjetosCorte(null, datos.getDescripcionObjetoCausa(), String.valueOf(datos.getCodObjetoCausa()), datos.getArticuloDelCodigo(), datos.getLey());
                                                    objetosCorteController.setSelected(objCorte);
                                                    objetosCorteController.saveNew(null);
                                                } else {
                                                    objCorte = lista9.get(0);
                                                }

                                                ExpObjetosPorDocumentosJudicialesCortePK pk = new ExpObjetosPorDocumentosJudicialesCortePK(objCorte.getId(), expedienteCorte.get(0).getId());

                                                ExpObjetosPorDocumentosJudicialesCorte objDoc = new ExpObjetosPorDocumentosJudicialesCorte(pk, expedienteCorte.get(0), doc, Constantes.SI.equals((datos.getPrincipal() == null ? Constantes.NO : datos.getPrincipal())), datos.getGradoRealizacion(), objCorte, personaUsuario, fecha, personaUsuario, fecha);

                                                objetosPorDocumentosJudicialesCorteController.setSelected(objDoc);
                                                objetosPorDocumentosJudicialesCorteController.saveNew(null);

                                                listaObjetosCorte.add(objDoc);
                                            }

                                            listaPartesCorte = new ArrayList<>();

                                            ExpTiposDocumentoCorte tipoDocCorte = null;
                                            ExpOcupacionesCorte ocCorte = null;
                                            ExpEstadosCivilCorte estCivilCorte = null;
                                            ExpSexosCorte sexoCorte = null;
                                            ExpTiposPersonaCorte tipoPerCorte = null;
                                            ExpTiposParteCorte tipoParteCorte = null;

                                            for (DatosPartes datos : partesLista) {

                                                List<ExpPartesPorDocumentosJudicialesCorte> partes2 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDNombresApellidos", ExpPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).setParameter("nombresApellidos", datos.getNombresApellidos()).getResultList();

                                                if (partes2.isEmpty()) {

                                                    List<ExpTiposDocumentoCorte> lista10 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposDocumentoCorte.findByDescripcion", ExpTiposDocumentoCorte.class).setParameter("descripcion", datos.getDescripcionTipoDocumento()).getResultList();
                                                    if (lista10.isEmpty()) {
                                                        tipoDocCorte = new ExpTiposDocumentoCorte(null, datos.getDescripcionTipoDocumento());
                                                        tiposDocumentoCorteController.setSelected(tipoDocCorte);
                                                        tiposDocumentoCorteController.saveNew(null);
                                                    } else {
                                                        tipoDocCorte = lista10.get(0);
                                                    }

                                                    List<ExpOcupacionesCorte> lista9 = ejbFacade.getEntityManager().createNamedQuery("ExpOcupacionesCorte.findByDescripcion", ExpOcupacionesCorte.class).setParameter("descripcion", datos.getDescripcionOcupacion()).getResultList();
                                                    if (lista9.isEmpty()) {
                                                        ocCorte = new ExpOcupacionesCorte(null, datos.getDescripcionOcupacion());
                                                        ocupacionesCorteController.setSelected(ocCorte);
                                                        ocupacionesCorteController.saveNew(null);
                                                    } else {
                                                        ocCorte = lista9.get(0);
                                                    }

                                                    List<ExpEstadosCivilCorte> lista11 = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosCivilCorte.findByDescripcion", ExpEstadosCivilCorte.class).setParameter("descripcion", datos.getDescripcionEstadoCivil()).getResultList();
                                                    if (lista11.isEmpty()) {
                                                        estCivilCorte = new ExpEstadosCivilCorte(null, datos.getDescripcionEstadoCivil());
                                                        estadosCivilCorteController.setSelected(estCivilCorte);
                                                        estadosCivilCorteController.saveNew(null);

                                                    } else {
                                                        estCivilCorte = lista11.get(0);
                                                    }

                                                    List<ExpSexosCorte> lista12 = ejbFacade.getEntityManager().createNamedQuery("ExpSexosCorte.findByDescripcion", ExpSexosCorte.class).setParameter("descripcion", datos.getSexo()).getResultList();
                                                    if (lista12.isEmpty()) {
                                                        sexoCorte = new ExpSexosCorte(null, datos.getSexo());
                                                        sexosCorteController.setSelected(sexoCorte);
                                                        sexosCorteController.saveNew(null);
                                                    } else {
                                                        sexoCorte = lista12.get(0);
                                                    }

                                                    List<ExpTiposPersonaCorte> lista13 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposPersonaCorte.findByDescripcion", ExpTiposPersonaCorte.class).setParameter("descripcion", datos.getDescripcionTipoPersona()).getResultList();
                                                    if (lista13.isEmpty()) {
                                                        tipoPerCorte = new ExpTiposPersonaCorte(null, datos.getDescripcionTipoPersona());
                                                        tiposPersonaCorteController.setSelected(tipoPerCorte);
                                                        tiposPersonaCorteController.saveNew(null);
                                                    } else {
                                                        tipoPerCorte = lista13.get(0);
                                                    }

                                                    List<ExpTiposParteCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParteCorte.findByDescripcion", ExpTiposParteCorte.class).setParameter("descripcion", datos.getDescripcionTipoParte()).getResultList();
                                                    if (lista14.isEmpty()) {
                                                        tipoParteCorte = new ExpTiposParteCorte(null, datos.getDescripcionTipoParte());
                                                        tiposParteCorteController.setSelected(tipoParteCorte);
                                                        tiposParteCorteController.saveNew(null);
                                                    } else {
                                                        tipoParteCorte = lista14.get(0);
                                                    }

                                                    Date fechaNac = null;
                                                    try {
                                                        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                                                        fechaNac = format2.parse(datos.getFechaNacimiento());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        Calendar myCal = Calendar.getInstance();
                                                        myCal.set(Calendar.YEAR, 1900);
                                                        myCal.set(Calendar.MONTH, 0);
                                                        myCal.set(Calendar.DAY_OF_MONTH, 1);
                                                        myCal.set(Calendar.HOUR, 0);
                                                        myCal.set(Calendar.MINUTE, 0);
                                                        myCal.set(Calendar.SECOND, 0);
                                                        fechaNac = myCal.getTime();
                                                    }

                                                    ExpPartesPorDocumentosJudicialesCorte parDoc = new ExpPartesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, tipoParteCorte, personaUsuario, fecha, ocCorte, estCivilCorte, datos.getNombresApellidos(), datos.getNroDocumento(), tipoDocCorte, fechaNac, sexoCorte, tipoPerCorte);

                                                    partesPorDocumentosJudicialesCorteController.setSelected(parDoc);
                                                    partesPorDocumentosJudicialesCorteController.saveNew(null);

                                                    listaPartesCorte.add(parDoc);

                                                    List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> dom = ejbFacade.getEntityManager().createNamedQuery("ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDDescripcionTipoDomicilioANDLocalidadANDCalle", ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).setParameter("localidad", datos.getLocalidad()).setParameter("calle", datos.getCalle()).setParameter("descripcionTipoDomicilio", datos.getDescripcionTipoDomicilio()).getResultList();

                                                    if (dom.isEmpty()) {
                                                        ExpDomiciliosPorPartesPorDocumentosJudicialesCorte domi = new ExpDomiciliosPorPartesPorDocumentosJudicialesCorte(null, parDoc, expedienteCorte.get(0), doc, personaUsuario, fecha, personaUsuario, fecha, datos.getLocalidad(), datos.getTelefono(), datos.getEmail(), datos.getCalle(), datos.getDescripcionTipoDomicilio());
                                                        domiciliosPorPartesPorDocumentosJudicialesCorteController.setSelected(domi);
                                                        domiciliosPorPartesPorDocumentosJudicialesCorteController.saveNew(null);
                                                    }

                                                    List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> prof = ejbFacade.getEntityManager().createNamedQuery("ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDTipoProfesionalANDNombreProfesional", ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).setParameter("tipoProfesional", datos.getLocalidad()).setParameter("tipoProfesional", datos.getTipoProfesional()).setParameter("nombreProfesional", datos.getNombresApellidos()).getResultList();

                                                    if (prof.isEmpty()) {
                                                        ExpProfesionalesPorPartesPorDocumentosJudicialesCorte profe = new ExpProfesionalesPorPartesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, personaUsuario, fecha, datos.getTipoProfesional(), datos.getNombreProfesional(), datos.getRepresentacion(), datos.getProfesionalHabilitado());
                                                        profesionalesPorPartesPorDocumentosJudicialesCorteController.setSelected(profe);
                                                        profesionalesPorPartesPorDocumentosJudicialesCorteController.saveNew(null);
                                                    }
                                                }
                                            }

                                            listaIntervinientesCorte = new ArrayList<>();

                                            tipoDocCorte = null;
                                            ocCorte = null;
                                            ExpEstadosCorte estCorte = null;
                                            sexoCorte = null;
                                            tipoPerCorte = null;
                                            ExpTiposIntervinienteCorte tipoIntervCorte = null;

                                            for (DatosIntervinientes datos : intervinientesLista) {

                                                List<ExpTiposDocumentoCorte> lista10 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposDocumentoCorte.findByDescripcion", ExpTiposDocumentoCorte.class).setParameter("descripcion", datos.getDescripcionTipoDocumento()).getResultList();
                                                if (lista10.isEmpty()) {
                                                    tipoDocCorte = new ExpTiposDocumentoCorte(null, datos.getDescripcionTipoDocumento());
                                                    tiposDocumentoCorteController.setSelected(tipoDocCorte);
                                                    tiposDocumentoCorteController.saveNew(null);
                                                } else {
                                                    tipoDocCorte = lista10.get(0);
                                                }

                                                List<ExpOcupacionesCorte> lista9 = ejbFacade.getEntityManager().createNamedQuery("ExpOcupacionesCorte.findByDescripcion", ExpOcupacionesCorte.class).setParameter("descripcion", datos.getDescripcionOcupacion()).getResultList();
                                                if (lista9.isEmpty()) {
                                                    ocCorte = new ExpOcupacionesCorte(null, datos.getDescripcionOcupacion());
                                                    ocupacionesCorteController.setSelected(ocCorte);
                                                    ocupacionesCorteController.saveNew(null);
                                                } else {
                                                    ocCorte = lista9.get(0);
                                                }

                                                List<ExpEstadosCorte> lista11 = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosCorte.findByDescripcion", ExpEstadosCorte.class).setParameter("descripcion", datos.getDescripcionEstadoInterviniente()).getResultList();
                                                if (lista11.isEmpty()) {
                                                    estCorte = new ExpEstadosCorte(null, datos.getDescripcionEstadoInterviniente());
                                                    estadosCorteController.setSelected(estCorte);
                                                    estadosCorteController.saveNew(null);

                                                } else {
                                                    estCorte = lista11.get(0);
                                                }

                                                List<ExpSexosCorte> lista12 = ejbFacade.getEntityManager().createNamedQuery("ExpSexosCorte.findByDescripcion", ExpSexosCorte.class).setParameter("descripcion", datos.getSexo()).getResultList();
                                                if (lista12.isEmpty()) {
                                                    sexoCorte = new ExpSexosCorte(null, datos.getSexo());
                                                    sexosCorteController.setSelected(sexoCorte);
                                                    sexosCorteController.saveNew(null);
                                                } else {
                                                    sexoCorte = lista12.get(0);
                                                }

                                                List<ExpTiposPersonaCorte> lista13 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposPersonaCorte.findByDescripcion", ExpTiposPersonaCorte.class).setParameter("descripcion", datos.getDescripcionTipoPersona()).getResultList();
                                                if (lista13.isEmpty()) {
                                                    tipoPerCorte = new ExpTiposPersonaCorte(null, datos.getDescripcionTipoPersona());
                                                    tiposPersonaCorteController.setSelected(tipoPerCorte);
                                                    tiposPersonaCorteController.saveNew(null);
                                                } else {
                                                    tipoPerCorte = lista13.get(0);
                                                }

                                                List<ExpTiposIntervinienteCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposIntervinienteCorte.findByDescripcion", ExpTiposIntervinienteCorte.class).setParameter("descripcion", datos.getTipoInterviniente()).getResultList();
                                                if (lista14.isEmpty()) {
                                                    tipoIntervCorte = new ExpTiposIntervinienteCorte(null, datos.getTipoInterviniente());
                                                    tiposIntervinienteCorteController.setSelected(tipoIntervCorte);
                                                    tiposIntervinienteCorteController.saveNew(null);
                                                } else {
                                                    tipoIntervCorte = lista14.get(0);
                                                }

                                                Date fechaNac = null;
                                                try {
                                                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                                                    fechaNac = format2.parse(datos.getFechaNacimiento());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Calendar myCal = Calendar.getInstance();
                                                    myCal.set(Calendar.YEAR, 1900);
                                                    myCal.set(Calendar.MONTH, 0);
                                                    myCal.set(Calendar.DAY_OF_MONTH, 1);
                                                    myCal.set(Calendar.HOUR, 0);
                                                    myCal.set(Calendar.MINUTE, 0);
                                                    myCal.set(Calendar.SECOND, 0);
                                                    fechaNac = myCal.getTime();
                                                }

                                                // ExpIntervinientesPorDocumentosJudicialesCorte parDoc = new ExpIntervinientesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, tipoParteCorte, personaUsuario, fecha, datos.getTipoProfesional(), datos.getNombreProfesional(), datos.getRepresentacion(), datos.getProfesionalHabilitado(), datos.getLocalidad(), datos.getTelefono(), datos.getEmail(), datos.getCalle(), ocCorte, estCivilCorte, datos.getNombresApellidos(), datos.getNroDocumento(), tipoDocCorte, fechaNac, sexoCorte, tipoPerCorte);
                                                ExpIntervinientesPorDocumentosJudicialesCorte parDoc = new ExpIntervinientesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, tipoIntervCorte, personaUsuario, fecha, estCorte, ocCorte, datos.getNombresApellidos(), datos.getNroDocumento(), tipoDocCorte, fechaNac, sexoCorte, tipoPerCorte);

                                                intervinientesPorDocumentosJudicialesCorteController.setSelected(parDoc);
                                                intervinientesPorDocumentosJudicialesCorteController.saveNew(null);

                                                listaIntervinientesCorte.add(parDoc);
                                            }

                                            ExpActuacionesCorte act = null;
                                            listaActuacionesCorte = new ArrayList<>();
                                            listaRecursosCorte = new ArrayList<>();
                                            listaRegulacionCorte = new ArrayList<>();
                                            listaExcepcionesCorte = new ArrayList<>();
                                            listaProcesosActuacionesCorte = new ArrayList<>();
                                            listaProcesosRecursosCorte = new ArrayList<>();
                                            listaProcesosRegulacionCorte = new ArrayList<>();
                                            listaProcesosExcepcionesCorte = new ArrayList<>();
                                            ExpGruposProcesoCorte procGrupo = null;
                                            ExpProcesosCorte proc = null;
                                            ExpProcesosDocumentoJudicialCorte procDoc = null;
                                            for (RespuestaConsultaActuacionesCausa item : listaRespuestaConsultaActuacionesCausa) {
                                                List<ExpGruposProcesoCorte> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpGruposProcesoCorte.findByCodigoCorte", ExpGruposProcesoCorte.class).setParameter("codigoCorte", String.valueOf(item.getCodTipoGrupoProceso())).getResultList();

                                                if (lista3.isEmpty()) {
                                                    procGrupo = new ExpGruposProcesoCorte();
                                                    procGrupo.setDescripcion(item.getDescripcionTipoGrupoProceso());
                                                    procGrupo.setCodigoCorte(String.valueOf(item.getCodTipoGrupoProceso()));
                                                    expGruposProcesoCorteController.setSelected(procGrupo);
                                                    expGruposProcesoCorteController.saveNew(null);
                                                    expGruposProcesoCorteController.setSelected(null);
                                                } else {
                                                    procGrupo = lista3.get(0);
                                                    procGrupo.setDescripcion(item.getDescripcionTipoGrupoProceso());
                                                    expGruposProcesoCorteController.setSelected(procGrupo);
                                                    expGruposProcesoCorteController.save(null);
                                                    expGruposProcesoCorteController.setSelected(null);
                                                }

                                                List<ExpProcesosCorte> lista4 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosCorte.findByCodigoCorte", ExpProcesosCorte.class).setParameter("codigoCorte", String.valueOf(item.getCodProcesoJudicial())).getResultList();

                                                if (lista4.isEmpty()) {
                                                    proc = new ExpProcesosCorte();
                                                    proc.setDescripcion(item.getDescripcionProceso());
                                                    proc.setCodigoCorte(String.valueOf(item.getCodProcesoJudicial()));
                                                    expProcesosCorteController.setSelected(proc);
                                                    expProcesosCorteController.saveNew(null);
                                                    expProcesosCorteController.setSelected(null);
                                                } else {
                                                    proc = lista4.get(0);
                                                    proc.setDescripcion(item.getDescripcionTipoGrupoProceso());
                                                    expProcesosCorteController.setSelected(proc);
                                                    expProcesosCorteController.save(null);
                                                    expProcesosCorteController.setSelected(null);
                                                }

                                                List<ExpProcesosDocumentoJudicialCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosDocumentoJudicialCorte.findByCodCasoJudicialANDCodProcesoCasoANDDocumentoJudicial", ExpProcesosDocumentoJudicialCorte.class).setParameter("codCasoJudicial", item.getCodCasoJudicial()).setParameter("codProcesoCaso", item.getCodProcesoCaso()).setParameter("documentoJudicial", doc).getResultList();

                                                boolean encontro = true;
                                                if (lista14.isEmpty()) {
                                                    encontro = false;
                                                    procDoc = new ExpProcesosDocumentoJudicialCorte();
                                                    // procDoc.setDescripcion(item.getDescripcionProceso() + " - " + item.getDescripcionDespachoJudicial());
                                                    procDoc.setDescripcion(item.getProcesosDeRecursos());
                                                    procDoc.setDocumentoJudicialCorte(docCorte);
                                                    procDoc.setDocumentoJudicial(doc);
                                                    procDoc.setProcesoCorte(proc);
                                                    procDoc.setGrupoProcesoCorte(procGrupo);

                                                    procDoc.setCodCasoJudicial(item.getCodCasoJudicial());
                                                    procDoc.setCodProcesoCaso(item.getCodProcesoCaso());
                                                    procDoc.setFechaHoraAlta(ejbFacade.getSystemDate());
                                                    procDoc.setFechaHoraUltimoEstado(ejbFacade.getSystemDate());
                                                    procDoc.setPersonaAlta(personaUsuario);
                                                    procDoc.setPersonaUltimoEstado(personaUsuario);
                                                    expProcesosDocumentoJudicialCorteController.setSelected(procDoc);
                                                    expProcesosDocumentoJudicialCorteController.saveNew(null);
                                                    expProcesosDocumentoJudicialCorteController.setSelected(null);
                                                } else {
                                                    procDoc = lista14.get(0);
                                                }

                                                if (!encontro) {
                                                    if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(procGrupo.getCodigoCorte()))) {
                                                        listaProcesosActuacionesCorte.add(procDoc);
                                                    } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                        listaProcesosRegulacionCorte.add(procDoc);
                                                    } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                        listaProcesosExcepcionesCorte.add(procDoc);
                                                    } else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                        listaProcesosRecursosCorte.add(procDoc);
                                                    }
                                                }

                                                act = new ExpActuacionesCorte(item.getCodCasoJudicial(), item.getCodActuacionCaso(), item.getFechaActuacion(), item.getFechaHoraRegistro(), item.getCodDespachoJudicial(), item.getDescripcionDespachoJudicial(), item.getDescripcionTipoActuacion(), item.getDescripcionActuacion(), item.getCaratula(), item.getNumeroActuacion(), item.getNroExpedienteNumero(), item.getNroExpedienteAnio(), fecha, personaUsuario, doc, docCorte, procDoc);
                                                expActuacionesCorteController.setSelected(act);
                                                expActuacionesCorteController.saveNew(null);
                                                if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(procGrupo.getCodigoCorte()))) {
                                                    listaActuacionesCorte.add(act);

                                                    // } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                    //     listaRegulacionCorte.add(act);
                                                    // } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                    //     listaExcepcionesCorte.add(act);
                                                    // } else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                    //     listaRecursosCorte.add(act);
                                                }

                                                //                                                listaRecursosCorte.add(act);
                                            }

                                            PrimeFaces.current().ajax().update("ExpActuacionesCorteViewForm");
                                            PrimeFaces.current().executeScript("PF('ExpActuacionesCorteViewDialog').show();");

                                            if (reemplazar) {
                                                JsfUtil.addSuccessMessage("Actuaciones obtenidas de la corte");
                                            }
                                        } else {
                                            JsfUtil.addErrorMessage(obj.getString("descripcion"));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }

                            }

                        } catch (InvalidKeySpecException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (NoSuchPaddingException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (InvalidKeyException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (InvalidParameterSpecException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (IllegalBlockSizeException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (BadPaddingException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (UnsupportedEncodingException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (InvalidAlgorithmParameterException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        }
                    } else {
                        listaActuacionesCorte = new ArrayList<>();
                        listaRecursosCorte = new ArrayList<>();
                        listaRegulacionCorte = new ArrayList<>();
                        listaExcepcionesCorte = new ArrayList<>();
                        listaObjetosCorte = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpObjetosPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).getResultList();
                        listaPartesCorte = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).getResultList();
                        listaIntervinientesCorte = ejbFacade.getEntityManager().createNamedQuery("ExpIntervinientesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpIntervinientesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).getResultList();

                        for (ExpActuacionesCorte act : lista2) {
                            if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte()))) {
                                listaActuacionesCorte.add(act);

                                //} else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                                //    listaRegulacionCorte.add(act);
                                //} else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                                //    listaExcepcionesCorte.add(act);
                                //} else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                                //    listaRecursosCorte.add(act);
                            }
                        }

                        List<ExpProcesosDocumentoJudicialCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosDocumentoJudicialCorte.findByDocumentoJudicial", ExpProcesosDocumentoJudicialCorte.class).setParameter("documentoJudicial", doc).getResultList();

                        listaProcesosActuacionesCorte = new ArrayList<>();
                        listaProcesosRegulacionCorte = new ArrayList<>();
                        listaProcesosExcepcionesCorte = new ArrayList<>();
                        listaProcesosRecursosCorte = new ArrayList<>();
                        for (ExpProcesosDocumentoJudicialCorte procDoc : lista14) {
                            if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte()))) {
                                listaProcesosActuacionesCorte.add(procDoc);
                            } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(procDoc.getProcesoCorte().getCodigoCorte()))) {
                                listaProcesosRegulacionCorte.add(procDoc);
                            } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(procDoc.getProcesoCorte().getCodigoCorte()))) {
                                listaProcesosExcepcionesCorte.add(procDoc);
                            } else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(procDoc.getProcesoCorte().getCodigoCorte()))) {
                                listaProcesosRecursosCorte.add(procDoc);
                            }
                        }

                        PrimeFaces.current().ajax().update("ExpActuacionesCorteViewForm");
                        PrimeFaces.current().executeScript("PF('ExpActuacionesCorteViewDialog').show();");
                    }

                } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    ex.printStackTrace();
                }
            } else {
                JsfUtil.addErrorMessage("Esta causa no tiene expediente corte asociado");
            }
        }

    }

    public void prepareVerExpedienteCorteBkp(DocumentosJudiciales doc, boolean reemplazar) {
        /*
        if (doc != null) {
            if (doc.getExpedienteCorte() != null) {
                try {

                    List<ExpConexiones> ws = ejbFacade.getEntityManager().createNamedQuery("ExpConexiones.findById", ExpConexiones.class).setParameter("id", Constantes.CONEXION_CORTE_VER_ACTUACIONES_ID).getResultList();

                    if (ws.isEmpty()) {
                        JsfUtil.addErrorMessage("No se encuentran parametros para conexion con la Corte");
                        return;
                    }

                    expedienteCorte = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findById", DocumentosJudicialesCorte.class).setParameter("id", doc.getExpedienteCorte().getId()).getResultList();

                    if (expedienteCorte.isEmpty()) {
                        JsfUtil.addErrorMessage("No se encuentra el expediente corte asociado");
                        return;
                    }

                    List<ExpActuacionesCorte> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpActuacionesCorte.findByDocumentoJudicial", ExpActuacionesCorte.class).setParameter("documentoJudicial", doc).getResultList();

                    if (lista2.isEmpty() || reemplazar) {

                        listaRespuestaConsultaActuacionesCausa = new ArrayList<>();

                        CloseableHttpClient CLIENT = Utils.createAcceptSelfSignedCertificateClient();
                        HttpPost request = new HttpPost(ws.get(0).getIpServidor() + ":" + ws.get(0).getPuertoServidor() + ws.get(0).getUri());

                        JSONObject json = new JSONObject();
                        json.put("codCasoJudicial", expedienteCorte.get(0).getCodCasoJudicial());
                        json.put("usuario", ws.get(0).getUsuario());
                        json.put("codDespachoJudicial", expedienteCorte.get(0).getCodDespachoJudicial());
                        json.put("codigoRol", -1);
                        json.put("codCircunscripcion", expedienteCorte.get(0).getCodCircunscripcion());

                        try {
                            ByteArrayEntity params = new ByteArrayEntity(Utils.encryptMsg(json.toString(), Utils.generateKey()));
                            request.addHeader("content-type", "application/octet-stream;charset=UTF-8");
                            request.addHeader("charset", "UTF-8");
                            request.setEntity(params);

                            HttpResponse response = (HttpResponse) CLIENT.execute(request);
                            if (response != null) {
                                HttpEntity entity = response.getEntity();

                                byte[] bytes = IOUtils.toByteArray(entity.getContent());

                                String respuesta = Utils.decryptMsg(bytes, Utils.generateKey());

                                if (respuesta != null) {
                                    JSONObject obj = null;
                                    try {
                                        obj = new JSONObject(respuesta);

                                        if (obj.getInt("codigo") == 0) {

                                            JSONArray lista = obj.getJSONArray("lista");

                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                            RespuestaConsultaActuacionesCausa resp = null;
                                            for (int j = 0; j < lista.length(); j++) {
                                                JSONObject rs = lista.getJSONObject(j);
                                                resp = new RespuestaConsultaActuacionesCausa(rs.getInt("codCasoJudicial"), rs.getInt("codActuacionCaso"), format.parse(rs.getString("fechaActuacion")),
                                                        format.parse(rs.getString("fechaHoraRegistro")), rs.getString("descripcionActuacion"), rs.getInt("codDespachoJudicial"),
                                                        rs.getString("descripcionDespachoJudicial"), rs.getString("descripcionTipoActuacion"), rs.getString("caratula"), rs.getInt("nroExpedienteNumero"),
                                                        rs.getInt("nroExpedienteAnio"), rs.getInt("codProcesoCaso"), rs.getInt("numeroActuacion"), rs.getInt("codTipoGrupoProceso"), rs.getString("descripcionTipoGrupoProceso"), rs.getInt("codCodProcesoJudicial"), rs.getString("descripcionProceso"), rs.getString("procesosDeRecursos"));
                                                listaRespuestaConsultaActuacionesCausa.add(resp);
                                            }

                                            JSONArray objetos = obj.getJSONArray("objetos");
                                            DatosObjetosCausa objeto = null;
                                            List<DatosObjetosCausa> objetosLista = new ArrayList<>();
                                            for (int k = 0; k < objetos.length(); k++) {
                                                JSONObject ob = objetos.getJSONObject(k);
                                                objeto = new DatosObjetosCausa(ob.getInt("codObjetoCausa"), ob.getInt("codCasoJudicial"), ob.getString("descripcionObjetoCausa"), ob.getString("gradoRealizacion"), ob.getString("principal"), ob.getString("ley"), ob.getString("articuloDelCodigo"));
                                                objetosLista.add(objeto);
                                            }

                                            JSONArray partes = obj.getJSONArray("partes");
                                            DatosPartes parte = null;
                                            List<DatosPartes> partesLista = new ArrayList<>();
                                            for (int k = 0; k < partes.length(); k++) {
                                                JSONObject part = partes.getJSONObject(k);
                                                parte = new DatosPartes(part.getInt("codParteCasoJudicial"), part.getInt("codCasoJudicial"), part.getString("nombresApellidos"), part.getString("nroDocumento"), part.getString("descripcionTipoDocumento"), part.getString("descripcionOcupacion"), part.getString("fechaNacimiento"), part.getString("sexo"), part.getString("descripcionTipoPersona"), part.getString("descripcionEstadoCivil"), part.getString("descripcionTipoParte"), part.getString("localidad"), part.getString("calle"), part.getString("telefono"), part.getString("email"), part.getString("tipoProfesional"), part.getString("nombreProfesional"), part.getString("representacion"), part.getString("profesionalHabilitado"), part.getString("descripcionTipoDomicilio"));
                                                partesLista.add(parte);
                                            }

                                            JSONArray intervinientes = obj.getJSONArray("intervinientes");
                                            DatosIntervinientes interviniente = null;
                                            List<DatosIntervinientes> intervinientesLista = new ArrayList<>();
                                            for (int k = 0; k < intervinientes.length(); k++) {
                                                JSONObject interv = intervinientes.getJSONObject(k);
                                                interviniente = new DatosIntervinientes(interv.getInt("codPersona"), interv.getInt("codCasoJudicial"), interv.getString("tipoInterviniente"), interv.getString("nombresApellidos"), interv.getString("descripcionTipoDocumento"), interv.getString("nroDocumento"), interv.getString("fechaNacimiento"), interv.getString("sexo"), interv.getString("descripcionTipoPersona"), interv.getString("descripcionOcupacion"), interv.getString("descripcionEstadoInterviniente"));
                                                intervinientesLista.add(interviniente);
                                            }

                                            for (ExpActuacionesCorte act : lista2) {
                                                expActuacionesCorteController.setSelected(act);
                                                expActuacionesCorteController.delete(null);
                                            }

                                            List<ExpProcesosDocumentoJudicialCorte> lista5 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosDocumentoJudicialCorte.findByDocumentoJudicial", ExpProcesosDocumentoJudicialCorte.class).setParameter("documentoJudicial", doc).getResultList();

                                            for (ExpProcesosDocumentoJudicialCorte act : lista5) {
                                                expProcesosDocumentoJudicialCorteController.setSelected(act);
                                                expProcesosDocumentoJudicialCorteController.delete(null);
                                            }

                                            List<ExpObjetosPorDocumentosJudicialesCorte> lista6 = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosPorDocumentosJudicialesCorte.findByDocumentoJudicial", ExpObjetosPorDocumentosJudicialesCorte.class).setParameter("documentoJudicial", doc).getResultList();

                                            for (ExpObjetosPorDocumentosJudicialesCorte act : lista6) {
                                                objetosPorDocumentosJudicialesCorteController.setSelected(act);
                                                objetosPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> lista15 = ejbFacade.getEntityManager().createNamedQuery("ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicial", ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicial", doc).getResultList();

                                            for (ExpDomiciliosPorPartesPorDocumentosJudicialesCorte act : lista15) {
                                                domiciliosPorPartesPorDocumentosJudicialesCorteController.setSelected(act);
                                                domiciliosPorPartesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> lista16 = ejbFacade.getEntityManager().createNamedQuery("ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicial", ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicial", doc).getResultList();

                                            for (ExpProfesionalesPorPartesPorDocumentosJudicialesCorte act : lista16) {
                                                profesionalesPorPartesPorDocumentosJudicialesCorteController.setSelected(act);
                                                profesionalesPorPartesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpPartesPorDocumentosJudicialesCorte> lista7 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicial", ExpPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicial", doc).getResultList();

                                            for (ExpPartesPorDocumentosJudicialesCorte act : lista7) {
                                                partesPorDocumentosJudicialesCorteController.setSelected(act);
                                                partesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            List<ExpIntervinientesPorDocumentosJudicialesCorte> lista8 = ejbFacade.getEntityManager().createNamedQuery("ExpIntervinientesPorDocumentosJudicialesCorte.findByDocumentoJudicial", ExpIntervinientesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicial", doc).getResultList();

                                            for (ExpIntervinientesPorDocumentosJudicialesCorte act : lista8) {
                                                intervinientesPorDocumentosJudicialesCorteController.setSelected(act);
                                                intervinientesPorDocumentosJudicialesCorteController.delete(null);
                                            }

                                            listaObjetosCorte = new ArrayList<>();

                                            ExpObjetosCorte objCorte = null;
                                            Date fecha = ejbFacade.getSystemDate();

                                            for (DatosObjetosCausa datos : objetosLista) {
                                                List<ExpObjetosCorte> lista9 = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosCorte.findByCodigoCorte", ExpObjetosCorte.class).setParameter("codigoCorte", String.valueOf(datos.getCodObjetoCausa())).getResultList();
                                                if (lista9.isEmpty()) {
                                                    objCorte = new ExpObjetosCorte(null, datos.getDescripcionObjetoCausa(), String.valueOf(datos.getCodObjetoCausa()), datos.getArticuloDelCodigo(), datos.getLey());
                                                    objetosCorteController.setSelected(objCorte);
                                                    objetosCorteController.saveNew(null);
                                                } else {
                                                    objCorte = lista9.get(0);
                                                }

                                                ExpObjetosPorDocumentosJudicialesCortePK pk = new ExpObjetosPorDocumentosJudicialesCortePK(objCorte.getId(), expedienteCorte.get(0).getId());

                                                ExpObjetosPorDocumentosJudicialesCorte objDoc = new ExpObjetosPorDocumentosJudicialesCorte(pk, expedienteCorte.get(0), doc, Constantes.SI.equals((datos.getPrincipal() == null ? Constantes.NO : datos.getPrincipal())), datos.getGradoRealizacion(), objCorte, personaUsuario, fecha, personaUsuario, fecha);

                                                objetosPorDocumentosJudicialesCorteController.setSelected(objDoc);
                                                objetosPorDocumentosJudicialesCorteController.saveNew(null);

                                                listaObjetosCorte.add(objDoc);
                                            }

                                            listaPartesCorte = new ArrayList<>();

                                            ExpTiposDocumentoCorte tipoDocCorte = null;
                                            ExpOcupacionesCorte ocCorte = null;
                                            ExpEstadosCivilCorte estCivilCorte = null;
                                            ExpSexosCorte sexoCorte = null;
                                            ExpTiposPersonaCorte tipoPerCorte = null;
                                            ExpTiposParteCorte tipoParteCorte = null;

                                            for (DatosPartes datos : partesLista) {

                                                List<ExpPartesPorDocumentosJudicialesCorte> partes2 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDNombresApellidos", ExpPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).setParameter("nombresApellidos", datos.getNombresApellidos()).getResultList();

                                                if (partes2.isEmpty()) {

                                                    List<ExpTiposDocumentoCorte> lista10 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposDocumentoCorte.findByDescripcion", ExpTiposDocumentoCorte.class).setParameter("descripcion", datos.getDescripcionTipoDocumento()).getResultList();
                                                    if (lista10.isEmpty()) {
                                                        tipoDocCorte = new ExpTiposDocumentoCorte(null, datos.getDescripcionTipoDocumento());
                                                        tiposDocumentoCorteController.setSelected(tipoDocCorte);
                                                        tiposDocumentoCorteController.saveNew(null);
                                                    } else {
                                                        tipoDocCorte = lista10.get(0);
                                                    }

                                                    List<ExpOcupacionesCorte> lista9 = ejbFacade.getEntityManager().createNamedQuery("ExpOcupacionesCorte.findByDescripcion", ExpOcupacionesCorte.class).setParameter("descripcion", datos.getDescripcionOcupacion()).getResultList();
                                                    if (lista9.isEmpty()) {
                                                        ocCorte = new ExpOcupacionesCorte(null, datos.getDescripcionOcupacion());
                                                        ocupacionesCorteController.setSelected(ocCorte);
                                                        ocupacionesCorteController.saveNew(null);
                                                    } else {
                                                        ocCorte = lista9.get(0);
                                                    }

                                                    List<ExpEstadosCivilCorte> lista11 = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosCivilCorte.findByDescripcion", ExpEstadosCivilCorte.class).setParameter("descripcion", datos.getDescripcionEstadoCivil()).getResultList();
                                                    if (lista11.isEmpty()) {
                                                        estCivilCorte = new ExpEstadosCivilCorte(null, datos.getDescripcionEstadoCivil());
                                                        estadosCivilCorteController.setSelected(estCivilCorte);
                                                        estadosCivilCorteController.saveNew(null);

                                                    } else {
                                                        estCivilCorte = lista11.get(0);
                                                    }

                                                    List<ExpSexosCorte> lista12 = ejbFacade.getEntityManager().createNamedQuery("ExpSexosCorte.findByDescripcion", ExpSexosCorte.class).setParameter("descripcion", datos.getSexo()).getResultList();
                                                    if (lista12.isEmpty()) {
                                                        sexoCorte = new ExpSexosCorte(null, datos.getSexo());
                                                        sexosCorteController.setSelected(sexoCorte);
                                                        sexosCorteController.saveNew(null);
                                                    } else {
                                                        sexoCorte = lista12.get(0);
                                                    }

                                                    List<ExpTiposPersonaCorte> lista13 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposPersonaCorte.findByDescripcion", ExpTiposPersonaCorte.class).setParameter("descripcion", datos.getDescripcionTipoPersona()).getResultList();
                                                    if (lista13.isEmpty()) {
                                                        tipoPerCorte = new ExpTiposPersonaCorte(null, datos.getDescripcionTipoPersona());
                                                        tiposPersonaCorteController.setSelected(tipoPerCorte);
                                                        tiposPersonaCorteController.saveNew(null);
                                                    } else {
                                                        tipoPerCorte = lista13.get(0);
                                                    }

                                                    List<ExpTiposParteCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposParteCorte.findByDescripcion", ExpTiposParteCorte.class).setParameter("descripcion", datos.getDescripcionTipoParte()).getResultList();
                                                    if (lista14.isEmpty()) {
                                                        tipoParteCorte = new ExpTiposParteCorte(null, datos.getDescripcionTipoParte());
                                                        tiposParteCorteController.setSelected(tipoParteCorte);
                                                        tiposParteCorteController.saveNew(null);
                                                    } else {
                                                        tipoParteCorte = lista14.get(0);
                                                    }

                                                    Date fechaNac = null;
                                                    try {
                                                        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                                                        fechaNac = format2.parse(datos.getFechaNacimiento());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        Calendar myCal = Calendar.getInstance();
                                                        myCal.set(Calendar.YEAR, 1900);
                                                        myCal.set(Calendar.MONTH, 0);
                                                        myCal.set(Calendar.DAY_OF_MONTH, 1);
                                                        myCal.set(Calendar.HOUR, 0);
                                                        myCal.set(Calendar.MINUTE, 0);
                                                        myCal.set(Calendar.SECOND, 0);
                                                        fechaNac = myCal.getTime();
                                                    }

                                                    ExpPartesPorDocumentosJudicialesCorte parDoc = new ExpPartesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, tipoParteCorte, personaUsuario, fecha, ocCorte, estCivilCorte, datos.getNombresApellidos(), datos.getNroDocumento(), tipoDocCorte, fechaNac, sexoCorte, tipoPerCorte);

                                                    partesPorDocumentosJudicialesCorteController.setSelected(parDoc);
                                                    partesPorDocumentosJudicialesCorteController.saveNew(null);

                                                    listaPartesCorte.add(parDoc);

                                                    List<ExpDomiciliosPorPartesPorDocumentosJudicialesCorte> dom = ejbFacade.getEntityManager().createNamedQuery("ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDDescripcionTipoDomicilioANDLocalidadANDCalle", ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).setParameter("localidad", datos.getLocalidad()).setParameter("calle", datos.getCalle()).setParameter("descripcionTipoDomicilio", datos.getDescripcionTipoDomicilio()).getResultList();

                                                    if (dom.isEmpty()) {
                                                        ExpDomiciliosPorPartesPorDocumentosJudicialesCorte domi = new ExpDomiciliosPorPartesPorDocumentosJudicialesCorte(null, parDoc, expedienteCorte.get(0), doc, personaUsuario, fecha, personaUsuario, fecha, datos.getLocalidad(), datos.getTelefono(), datos.getEmail(), datos.getCalle(), datos.getDescripcionTipoDomicilio());
                                                        domiciliosPorPartesPorDocumentosJudicialesCorteController.setSelected(domi);
                                                        domiciliosPorPartesPorDocumentosJudicialesCorteController.saveNew(null);
                                                    }

                                                    List<ExpProfesionalesPorPartesPorDocumentosJudicialesCorte> prof = ejbFacade.getEntityManager().createNamedQuery("ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDTipoProfesionalANDNombreProfesional", ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).setParameter("tipoProfesional", datos.getLocalidad()).setParameter("tipoProfesional", datos.getTipoProfesional()).setParameter("nombreProfesional", datos.getNombresApellidos()).getResultList();

                                                    if (prof.isEmpty()) {
                                                        ExpProfesionalesPorPartesPorDocumentosJudicialesCorte profe = new ExpProfesionalesPorPartesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, personaUsuario, fecha, datos.getTipoProfesional(), datos.getNombreProfesional(), datos.getRepresentacion(), datos.getProfesionalHabilitado());
                                                        profesionalesPorPartesPorDocumentosJudicialesCorteController.setSelected(profe);
                                                        profesionalesPorPartesPorDocumentosJudicialesCorteController.saveNew(null);
                                                    }
                                                }
                                            }

                                            listaIntervinientesCorte = new ArrayList<>();

                                            tipoDocCorte = null;
                                            ocCorte = null;
                                            ExpEstadosCorte estCorte = null;
                                            sexoCorte = null;
                                            tipoPerCorte = null;
                                            ExpTiposIntervinienteCorte tipoIntervCorte = null;

                                            for (DatosIntervinientes datos : intervinientesLista) {

                                                List<ExpTiposDocumentoCorte> lista10 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposDocumentoCorte.findByDescripcion", ExpTiposDocumentoCorte.class).setParameter("descripcion", datos.getDescripcionTipoDocumento()).getResultList();
                                                if (lista10.isEmpty()) {
                                                    tipoDocCorte = new ExpTiposDocumentoCorte(null, datos.getDescripcionTipoDocumento());
                                                    tiposDocumentoCorteController.setSelected(tipoDocCorte);
                                                    tiposDocumentoCorteController.saveNew(null);
                                                } else {
                                                    tipoDocCorte = lista10.get(0);
                                                }

                                                List<ExpOcupacionesCorte> lista9 = ejbFacade.getEntityManager().createNamedQuery("ExpOcupacionesCorte.findByDescripcion", ExpOcupacionesCorte.class).setParameter("descripcion", datos.getDescripcionOcupacion()).getResultList();
                                                if (lista9.isEmpty()) {
                                                    ocCorte = new ExpOcupacionesCorte(null, datos.getDescripcionOcupacion());
                                                    ocupacionesCorteController.setSelected(ocCorte);
                                                    ocupacionesCorteController.saveNew(null);
                                                } else {
                                                    ocCorte = lista9.get(0);
                                                }

                                                List<ExpEstadosCorte> lista11 = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosCorte.findByDescripcion", ExpEstadosCorte.class).setParameter("descripcion", datos.getDescripcionEstadoInterviniente()).getResultList();
                                                if (lista11.isEmpty()) {
                                                    estCorte = new ExpEstadosCorte(null, datos.getDescripcionEstadoInterviniente());
                                                    estadosCorteController.setSelected(estCorte);
                                                    estadosCorteController.saveNew(null);

                                                } else {
                                                    estCorte = lista11.get(0);
                                                }

                                                List<ExpSexosCorte> lista12 = ejbFacade.getEntityManager().createNamedQuery("ExpSexosCorte.findByDescripcion", ExpSexosCorte.class).setParameter("descripcion", datos.getSexo()).getResultList();
                                                if (lista12.isEmpty()) {
                                                    sexoCorte = new ExpSexosCorte(null, datos.getSexo());
                                                    sexosCorteController.setSelected(sexoCorte);
                                                    sexosCorteController.saveNew(null);
                                                } else {
                                                    sexoCorte = lista12.get(0);
                                                }

                                                List<ExpTiposPersonaCorte> lista13 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposPersonaCorte.findByDescripcion", ExpTiposPersonaCorte.class).setParameter("descripcion", datos.getDescripcionTipoPersona()).getResultList();
                                                if (lista13.isEmpty()) {
                                                    tipoPerCorte = new ExpTiposPersonaCorte(null, datos.getDescripcionTipoPersona());
                                                    tiposPersonaCorteController.setSelected(tipoPerCorte);
                                                    tiposPersonaCorteController.saveNew(null);
                                                } else {
                                                    tipoPerCorte = lista13.get(0);
                                                }

                                                List<ExpTiposIntervinienteCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpTiposIntervinienteCorte.findByDescripcion", ExpTiposIntervinienteCorte.class).setParameter("descripcion", datos.getTipoInterviniente()).getResultList();
                                                if (lista14.isEmpty()) {
                                                    tipoIntervCorte = new ExpTiposIntervinienteCorte(null, datos.getTipoInterviniente());
                                                    tiposIntervinienteCorteController.setSelected(tipoIntervCorte);
                                                    tiposIntervinienteCorteController.saveNew(null);
                                                } else {
                                                    tipoIntervCorte = lista14.get(0);
                                                }

                                                Date fechaNac = null;
                                                try {
                                                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                                                    fechaNac = format2.parse(datos.getFechaNacimiento());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Calendar myCal = Calendar.getInstance();
                                                    myCal.set(Calendar.YEAR, 1900);
                                                    myCal.set(Calendar.MONTH, 0);
                                                    myCal.set(Calendar.DAY_OF_MONTH, 1);
                                                    myCal.set(Calendar.HOUR, 0);
                                                    myCal.set(Calendar.MINUTE, 0);
                                                    myCal.set(Calendar.SECOND, 0);
                                                    fechaNac = myCal.getTime();
                                                }

                                                // ExpIntervinientesPorDocumentosJudicialesCorte parDoc = new ExpIntervinientesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, tipoParteCorte, personaUsuario, fecha, datos.getTipoProfesional(), datos.getNombreProfesional(), datos.getRepresentacion(), datos.getProfesionalHabilitado(), datos.getLocalidad(), datos.getTelefono(), datos.getEmail(), datos.getCalle(), ocCorte, estCivilCorte, datos.getNombresApellidos(), datos.getNroDocumento(), tipoDocCorte, fechaNac, sexoCorte, tipoPerCorte);
                                                ExpIntervinientesPorDocumentosJudicialesCorte parDoc = new ExpIntervinientesPorDocumentosJudicialesCorte(null, expedienteCorte.get(0), doc, personaUsuario, fecha, tipoIntervCorte, personaUsuario, fecha, estCorte, ocCorte, datos.getNombresApellidos(), datos.getNroDocumento(), tipoDocCorte, fechaNac, sexoCorte, tipoPerCorte);

                                                intervinientesPorDocumentosJudicialesCorteController.setSelected(parDoc);
                                                intervinientesPorDocumentosJudicialesCorteController.saveNew(null);

                                                listaIntervinientesCorte.add(parDoc);
                                            }

                                            ExpActuacionesCorte act = null;
                                            listaActuacionesCorte = new ArrayList<>();
                                            listaRecursosCorte = new ArrayList<>();
                                            listaRegulacionCorte = new ArrayList<>();
                                            listaExcepcionesCorte = new ArrayList<>();
                                            listaProcesosActuacionesCorte = new ArrayList<>();
                                            listaProcesosRecursosCorte = new ArrayList<>();
                                            listaProcesosRegulacionCorte = new ArrayList<>();
                                            listaProcesosExcepcionesCorte = new ArrayList<>();
                                            ExpGruposProcesoCorte procGrupo = null;
                                            ExpProcesosCorte proc = null;
                                            ExpProcesosDocumentoJudicialCorte procDoc = null;
                                            for (RespuestaConsultaActuacionesCausa item : listaRespuestaConsultaActuacionesCausa) {
                                                List<ExpGruposProcesoCorte> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpGruposProcesoCorte.findByCodigoCorte", ExpGruposProcesoCorte.class).setParameter("codigoCorte", String.valueOf(item.getCodTipoGrupoProceso())).getResultList();

                                                if (lista3.isEmpty()) {
                                                    procGrupo = new ExpGruposProcesoCorte();
                                                    procGrupo.setDescripcion(item.getDescripcionTipoGrupoProceso());
                                                    procGrupo.setCodigoCorte(String.valueOf(item.getCodTipoGrupoProceso()));
                                                    expGruposProcesoCorteController.setSelected(procGrupo);
                                                    expGruposProcesoCorteController.saveNew(null);
                                                    expGruposProcesoCorteController.setSelected(null);
                                                } else {
                                                    procGrupo = lista3.get(0);
                                                    procGrupo.setDescripcion(item.getDescripcionTipoGrupoProceso());
                                                    expGruposProcesoCorteController.setSelected(procGrupo);
                                                    expGruposProcesoCorteController.save(null);
                                                    expGruposProcesoCorteController.setSelected(null);
                                                }

                                                List<ExpProcesosCorte> lista4 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosCorte.findByCodigoCorte", ExpProcesosCorte.class).setParameter("codigoCorte", String.valueOf(item.getCodProcesoJudicial())).getResultList();

                                                if (lista4.isEmpty()) {
                                                    proc = new ExpProcesosCorte();
                                                    proc.setDescripcion(item.getDescripcionProceso());
                                                    proc.setCodigoCorte(String.valueOf(item.getCodProcesoJudicial()));
                                                    expProcesosCorteController.setSelected(proc);
                                                    expProcesosCorteController.saveNew(null);
                                                    expProcesosCorteController.setSelected(null);
                                                } else {
                                                    proc = lista4.get(0);
                                                    proc.setDescripcion(item.getDescripcionTipoGrupoProceso());
                                                    expProcesosCorteController.setSelected(proc);
                                                    expProcesosCorteController.save(null);
                                                    expProcesosCorteController.setSelected(null);
                                                }

                                                List<ExpProcesosDocumentoJudicialCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosDocumentoJudicialCorte.findByCodCasoJudicialANDCodProcesoCasoANDDocumentoJudicial", ExpProcesosDocumentoJudicialCorte.class).setParameter("codCasoJudicial", item.getCodCasoJudicial()).setParameter("codProcesoCaso", item.getCodProcesoCaso()).setParameter("documentoJudicial", doc).getResultList();

                                                boolean encontro = true;
                                                if (lista14.isEmpty()) {
                                                    encontro = false;
                                                    procDoc = new ExpProcesosDocumentoJudicialCorte();
                                                    // procDoc.setDescripcion(item.getDescripcionProceso() + " - " + item.getDescripcionDespachoJudicial());
                                                    procDoc.setDescripcion(item.getProcesosDeRecursos());
                                                    procDoc.setDocumentoJudicialCorte(doc.getExpedienteCorte());
                                                    procDoc.setDocumentoJudicial(doc);
                                                    procDoc.setProcesoCorte(proc);
                                                    procDoc.setGrupoProcesoCorte(procGrupo);

                                                    procDoc.setCodCasoJudicial(item.getCodCasoJudicial());
                                                    procDoc.setCodProcesoCaso(item.getCodProcesoCaso());
                                                    procDoc.setFechaHoraAlta(ejbFacade.getSystemDate());
                                                    procDoc.setFechaHoraUltimoEstado(ejbFacade.getSystemDate());
                                                    procDoc.setPersonaAlta(personaUsuario);
                                                    procDoc.setPersonaUltimoEstado(personaUsuario);
                                                    expProcesosDocumentoJudicialCorteController.setSelected(procDoc);
                                                    expProcesosDocumentoJudicialCorteController.saveNew(null);
                                                    expProcesosDocumentoJudicialCorteController.setSelected(null);
                                                } else {
                                                    procDoc = lista14.get(0);
                                                }

                                                if (!encontro) {
                                                    if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(procGrupo.getCodigoCorte()))) {
                                                        listaProcesosActuacionesCorte.add(procDoc);
                                                    } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                        listaProcesosRegulacionCorte.add(procDoc);
                                                    } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                        listaProcesosExcepcionesCorte.add(procDoc);
                                                    } else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                                        listaProcesosRecursosCorte.add(procDoc);
                                                    }
                                                }

                                                act = new ExpActuacionesCorte(item.getCodCasoJudicial(), item.getCodActuacionCaso(), item.getFechaActuacion(), item.getFechaHoraRegistro(), item.getCodDespachoJudicial(), item.getDescripcionDespachoJudicial(), item.getDescripcionTipoActuacion(), item.getDescripcionActuacion(), item.getCaratula(), item.getNumeroActuacion(), item.getNroExpedienteNumero(), item.getNroExpedienteAnio(), fecha, personaUsuario, doc, doc.getExpedienteCorte(), procDoc);
                                                expActuacionesCorteController.setSelected(act);
                                                expActuacionesCorteController.saveNew(null);
                                                if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(procGrupo.getCodigoCorte()))) {
                                                    listaActuacionesCorte.add(act);
                                                    
                                               // } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                               //     listaRegulacionCorte.add(act);
                                               // } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                               //     listaExcepcionesCorte.add(act);
                                               // } else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(procGrupo.getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(proc.getCodigoCorte()))) {
                                               //     listaRecursosCorte.add(act);
                                                     
                                                }

                                                //                                                listaRecursosCorte.add(act);
                                            }

                                            PrimeFaces.current().ajax().update("ExpActuacionesCorteViewForm");
                                            PrimeFaces.current().executeScript("PF('ExpActuacionesCorteViewDialog').show();");
                                        } else {
                                            JsfUtil.addErrorMessage("Error al obtener actuaciones");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }

                            }

                        } catch (InvalidKeySpecException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (NoSuchPaddingException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (InvalidKeyException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (InvalidParameterSpecException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (IllegalBlockSizeException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (BadPaddingException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (UnsupportedEncodingException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        } catch (InvalidAlgorithmParameterException ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                            return;
                        }
                    } else {
                        listaActuacionesCorte = new ArrayList<>();
                        listaRecursosCorte = new ArrayList<>();
                        listaRegulacionCorte = new ArrayList<>();
                        listaExcepcionesCorte = new ArrayList<>();
                        listaObjetosCorte = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpObjetosPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).getResultList();
                        listaPartesCorte = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpPartesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).getResultList();
                        listaIntervinientesCorte = ejbFacade.getEntityManager().createNamedQuery("ExpIntervinientesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", ExpIntervinientesPorDocumentosJudicialesCorte.class).setParameter("documentoJudicialCorte", expedienteCorte.get(0)).getResultList();

                        for (ExpActuacionesCorte act : lista2) {
                            if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte()))) {
                                listaActuacionesCorte.add(act);
                                
                            //} else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                            //    listaRegulacionCorte.add(act);
                            //} else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                            //    listaExcepcionesCorte.add(act);
                            //} else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(act.getProcesoDocumentoJudicialCorte().getProcesoCorte().getCodigoCorte()))) {
                            //    listaRecursosCorte.add(act);
                                 
                            }
                        }

                        List<ExpProcesosDocumentoJudicialCorte> lista14 = ejbFacade.getEntityManager().createNamedQuery("ExpProcesosDocumentoJudicialCorte.findByDocumentoJudicial", ExpProcesosDocumentoJudicialCorte.class).setParameter("documentoJudicial", doc).getResultList();

                        listaProcesosActuacionesCorte = new ArrayList<>();
                        listaProcesosRegulacionCorte = new ArrayList<>();
                        listaProcesosExcepcionesCorte = new ArrayList<>();
                        listaProcesosRecursosCorte = new ArrayList<>();
                        for (ExpProcesosDocumentoJudicialCorte procDoc : lista14) {
                            if (Constantes.GRUPO_PROCESO_CORTE_ORDINARIO.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte()))) {
                                listaProcesosActuacionesCorte.add(procDoc);
                            } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte())) && Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(procDoc.getProcesoCorte().getCodigoCorte()))) {
                                listaProcesosRegulacionCorte.add(procDoc);
                            } else if (Constantes.GRUPO_PROCESO_CORTE_INCIDENTE.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(procDoc.getProcesoCorte().getCodigoCorte()))) {
                                listaProcesosExcepcionesCorte.add(procDoc);
                            } else if (Constantes.GRUPO_PROCESO_CORTE_RECURSO.equals(Integer.valueOf(procDoc.getGrupoProcesoCorte().getCodigoCorte())) && !Constantes.PROCESO_CORTE_REGULACION_HONORARIOS.equals(Integer.valueOf(procDoc.getProcesoCorte().getCodigoCorte()))) {
                                listaProcesosRecursosCorte.add(procDoc);
                            }
                        }

                        PrimeFaces.current().ajax().update("ExpActuacionesCorteViewForm");
                        PrimeFaces.current().executeScript("PF('ExpActuacionesCorteViewDialog').show();");
                    }

                } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    ex.printStackTrace();
                }
            } else {
                JsfUtil.addErrorMessage("Esta causa no tiene expediente corte asociado");
            }
        }
         */
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

    public void prepareCorregirFirmantes(ExpActuaciones act) {
        if (act != null) {

            if (act.getPreopinante() != null) {
                List<Personas> lista = new ArrayList<>();

                lista.add(act.getPreopinante());

                listaCorregirFirmantes = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstadoNotPersonaFirmante", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("estado", new Estados("AC")).setParameter("personaFirmante", lista).getResultList();
            } else {
                listaCorregirFirmantes = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("estado", new Estados("AC")).getResultList();
            }

            /*
            if(listaCorregirFirmantes != null && act.getPreopinante() != null){
                Personas preopinante = null;
                for(ExpPersonasFirmantesPorActuaciones per : listaCorregirFirmantes){
                    if(per.getPersonaFirmante().equals(per.getActuacion().getPreopinante())){
                        preopinante = per.getPersonaFirmante();
                    }
                }
                
                if(preopinante != null){
                    listaCorregirFirmantes.remove(preopinante);
                }
            }
             */
 /*
            ExpPersonasFirmantesPorActuaciones preopinante = null;
            for(ExpPersonasFirmantesPorActuaciones per : listaCorregirFirmantes){
                if(act.getPreopinante() != null){
                    if(per.getPersonaFirmante().equals(act.getPreopinante())){
                        preopinante = 
                    }
                }
            }
             */
        }
    }

    public void corregirPendiente(ExpPersonasFirmantesPorActuaciones per) {
        per.setFirmado(false);
        per.setFechaHoraFirma(null);

        personasFirmantesPorActuacionesController.setSelected(per);
        personasFirmantesPorActuacionesController.save(null);

        List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS).getResultList();

        if (!est.isEmpty()) {
            if (per.getActuacion().getEstado() != null) {
                if (Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO.equals(per.getActuacion().getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO.equals(per.getActuacion().getEstado().getCodigo())) {
                    per.getActuacion().setEstado(est.get(0));
                    expActuacionesController.setSelected(per.getActuacion());
                    expActuacionesController.save(null);
                }
            }
        }

        buscarActuaciones();
    }

    public void corregirFirmado(ExpPersonasFirmantesPorActuaciones per) {
        List<Firmas> lista = ejbFacade.getEntityManager().createNamedQuery("Firmas.findByActuacionANDPersona", Firmas.class).setParameter("actuacion", per.getActuacion()).setParameter("persona", per.getPersonaFirmante()).getResultList();
        if (!lista.isEmpty()) {
            per.setFirmado(true);
            per.setFechaHoraFirma(lista.get(0).getFechaHora());

            personasFirmantesPorActuacionesController.setSelected(per);
            personasFirmantesPorActuacionesController.save(null);

            if (Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(per.getActuacion().getEstado().getCodigo())) {

                List<ExpPersonasFirmantesPorActuaciones> listaPer = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstadoFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", per.getActuacion()).setParameter("firmado", false).setParameter("estado", new Estados("AC")).getResultList();

                if (listaPer.isEmpty()) {
                    List<AntecedentesRolesPorPersonas> perExSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_EXSECRETARIO).getResultList();

                    boolean esExSecretario = false;

                    if (per.getActuacion().getSecretario() != null) {
                        for (AntecedentesRolesPorPersonas rol : perExSecretario) {
                            if (per.getActuacion().getSecretario().equals(rol.getPersonas())) {
                                esExSecretario = true;
                                break;
                            }
                        }
                    }
                    List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", esExSecretario ? Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO : Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO).getResultList();
                    if (!est.isEmpty()) {
                        per.getActuacion().setEstado(est.get(0));
                        expActuacionesController.setSelected(per.getActuacion());
                        expActuacionesController.save(null);
                    }
                }
                buscarActuaciones();
            } else {
                JsfUtil.addErrorMessage("No se encuentran intentos de firma");
            }
        }
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
                    if (doc.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_ACTA_SESION)) {
                        fileByte = Files.readAllBytes(new File(par.getRutaActas() + "/" + doc.getArchivo()).toPath());
                    } else {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + doc.getArchivo()).toPath());
                    }
                } catch (IOException ex) {
                    JsfUtil.addErrorMessage("No tiene documento adjunto");
                }
            } else if (doc.getFormato() != null) {
                docImprimir = doc;
                getContent();

                try {
                    fileByte = Files.readAllBytes(new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre).toPath());
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
        } finally {
            if (nombre != null) {
                if (!"".equals(nombre)) {
                    File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
                    f.delete();

                    docImprimir = null;
                }
            }
        }
    }

    public void verDocCompleto(DocumentosJudiciales doc, List<ExpActuaciones> listaAct) {
        if (doc != null) {

            if (listaAct != null) {

                if (!listaAct.isEmpty()) {

                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);
                    String nombreArchivo = session.getId() + "_" + simpleDateFormat.format(fecha) + ".pdf";
                    try {
                        try {
                            PdfDocument pdf = new PdfDocument(new PdfWriter(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombreArchivo));
                            PdfMerger merger = new PdfMerger(pdf);

                            //for (ExpActuaciones act : listaActuaciones) {
                            ExpActuaciones act = null;
                            for (int i = listaAct.size() - 1; i >= 0; i--) {
                                act = listaAct.get(i);
                                if (act.isVisible()) {
                                    if (act.getArchivo() != null) {
                                        PdfDocument firstSourcePdf = new PdfDocument(new PdfReader(par.getRutaArchivos() + "/" + act.getArchivo()));
                                        merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());
                                        firstSourcePdf.close();
                                    }
                                }
                            }
                            pdf.close();

                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                            return;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            return;
                        }

                        HttpServletResponse httpServletResponse = null;
                        try {
                            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                            httpServletResponse.setContentType("application/pdf");
                            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                            byte[] fileByte = null;

                            try {
                                fileByte = Files.readAllBytes(new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombreArchivo).toPath());
                            } catch (IOException ex) {
                                JsfUtil.addErrorMessage("No tiene documento adjunto");
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
                    } finally {
                        if (nombreArchivo != null) {
                            if (!"".equals(nombreArchivo)) {
                                File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombreArchivo);
                                f.delete();
                            }
                        }
                    }
                }
            }
        }
    }

    public byte[] decompress(byte[] bytesToDecompress) {
        byte[] returnValues = null;

        Inflater inflater = new Inflater();

        int numberOfBytesToDecompress = bytesToDecompress.length;

        inflater.setInput(bytesToDecompress, 0, numberOfBytesToDecompress);

        int bufferSizeInBytes = numberOfBytesToDecompress;

        int numberOfBytesDecompressedSoFar = 0;
        List<Byte> bytesDecompressedSoFar = new ArrayList<Byte>();

        try {
            while (inflater.needsInput() == false) {
                byte[] bytesDecompressedBuffer = new byte[bufferSizeInBytes];

                int numberOfBytesDecompressedThisTime = inflater.inflate(bytesDecompressedBuffer);

                if (numberOfBytesDecompressedThisTime == 0) {
                    break;
                }

                numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime;

                for (int b = 0; b < numberOfBytesDecompressedThisTime; b++) {
                    bytesDecompressedSoFar.add(bytesDecompressedBuffer[b]);
                }
            }

            returnValues = new byte[bytesDecompressedSoFar.size()];
            for (int b = 0; b < returnValues.length; b++) {
                returnValues[b] = (byte) (bytesDecompressedSoFar.get(b));
            }

        } catch (DataFormatException dfe) {
            dfe.printStackTrace();
        }

        inflater.end();

        return returnValues;
    }

    /*
    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);

        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
     */
    public void verDocCorte(ExpActuacionesCorte doc) {

        if (doc != null) {
            byte[] fileByte = null;
            String datos = "";

            boolean esPDF = true;

            String extension = "";
            try {

                List<ExpConexiones> ws = ejbFacade.getEntityManager().createNamedQuery("ExpConexiones.findById", ExpConexiones.class).setParameter("id", Constantes.CONEXION_CORTE_VER_PDF_ID).getResultList();

                if (ws.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encuentran parametros para conexion con la Corte");
                    return;
                }

                listaRespuestaConsultaActuacionesCausa = new ArrayList<>();

                CloseableHttpClient CLIENT = Utils.createAcceptSelfSignedCertificateClient();
                HttpPost request = new HttpPost(ws.get(0).getIpServidor() + ":" + ws.get(0).getPuertoServidor() + ws.get(0).getUri());

                JSONObject json = new JSONObject();
                json.put("codActuacionCaso", doc.getCodActuacionCaso());
                json.put("codCasoJEM", doc.getDocumentoJudicial().getId());
                json.put("codCircunscripcion", doc.getDocumentoJudicialCorte().getCodCircunscripcion());
                // json.put("codActuacionCaso", 6230582);

                json.put("codigoDespachoJudicialFiltro", doc.getDocumentoJudicialCorte().getCodDespachoJudicial());
                json.put("usuario", ws.get(0).getUsuario());

                try {
                    ByteArrayEntity params = new ByteArrayEntity(Utils.encryptMsg(json.toString(), Utils.generateKey()));
                    request.addHeader("content-type", "application/octet-stream;charset=UTF-8");
                    request.addHeader("charset", "UTF-8");
                    request.setEntity(params);

                    HttpResponse response = (HttpResponse) CLIENT.execute(request);
                    if (response != null) {
                        HttpEntity entity = response.getEntity();

                        byte[] bytes = IOUtils.toByteArray(entity.getContent());

                        String respuesta = Utils.decryptMsg(bytes, Utils.generateKey());

                        if (respuesta != null) {
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(respuesta);

                                if (obj.getInt("codigo") == 0) {

                                    //JSONObject rs = obj.getJSONObject("lista");
                                    // SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
                                    // RespuestaConsultaPdf resp = new RespuestaConsultaPdf(rs.getInt("CodDocumento"), rs.getInt("CodTipoDocumentoCaso"), rs.getString("DescripcionTipoDocumentoCaso"), rs.getInt("CodActuacionCaso"), rs.getString("NombreArchivo"), format.format(rs.getString("FechaImportacion")), rs.getString("Documento").toString().getBytes(), rs.getInt("CantidadHojas"), rs.getString("Comentario"), rs.getString("EstadoActuacion"), rs.getString("DocumentoFinal").toString().getBytes(), rs.getString("DocumentoTexto"), rs.getString("ExtensionArchivo"), rs.getInt("CodFormato"), rs.getString("DescripcionFormato"));
                                    // fileByte = rs.getString("documentoFinal").getBytes();
                                    JSONObject rs = obj.getJSONObject("resp");

                                    // JSONObject rs2 = rs.getJSONObject("codFormato");
                                    //esPDF = "1".equals(rs2.getString("codFormato"));
                                    esPDF = (rs.getInt("codFormato") == 9 || rs.getInt("codFormato") == 6);

                                    extension = rs.getString("extensionArchivo");

                                    System.out.println("esPDF: " + (esPDF ? "true" : "false"));
                                    System.out.println("extension: " + extension);
                                    //if (esPDF) {
                                    // if (!extension.equals("html")) {
                                    JSONArray lista = rs.getJSONArray("documentoFinal");
                                    fileByte = new byte[lista.length()];
                                    int i = 0;
                                    for (int j = 0; j < lista.length(); j++) {
                                        if (!extension.equals("html") || (extension.equals("html") && lista.getInt(j) != 0x00)) {
                                            fileByte[i++] = (byte) lista.getInt(j);
                                        }
                                    }
                                    /*
                                    } else {
                                        datos = rs.getString("documentoTexto");
                                    }
                                     */
 /*
                                    } else {

                                        datos = rs.getString("documentoTexto");

                                        System.out.println("datos1 : " + datos);
                                        if (!extension.equals("html")) {
                                            int pos = datos.lastIndexOf("}");
                                            System.out.println("pos : " + pos);
                                            if (pos != -1) {
                                                if (pos + 1 <= datos.length()) {
                                                    datos = datos.substring(pos + 1);
                                                }
                                            }
                                            System.out.println("datos2 : " + datos);
                                        }
                                        fileByte = datos.getBytes();
                                    }
                                     */

                                    // File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
                                    /*
                                    try ( FileOutputStream fos = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/prueba1")) {
                                        fos.write(fileByte);
                                        fos.close();
                                    }
                                     */
                                } else {
                                    JsfUtil.addErrorMessage("No se pudo obtener la actuacion");
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                } catch (InvalidKeySpecException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (NoSuchPaddingException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (InvalidKeyException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (InvalidParameterSpecException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (IllegalBlockSizeException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (BadPaddingException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                } catch (InvalidAlgorithmParameterException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                    return;
                }

                //mensaje = decryptMsg(pedido, generateKey());
                /*
                HttpEntity entity = response.getEntity();
                ObjectMapper mapper = new ObjectMapper();
                resp = mapper.readValue((EntityUtils.toString(entity)), RespuestaPersonas.class);
                 */
            } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
                JsfUtil.addErrorMessage("No se pudo obtener actuaciones");
                ex.printStackTrace();
            }

            HttpServletResponse httpServletResponse = null;
            try {
                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                if (esPDF) {
                    httpServletResponse.setContentType("application/pdf");
                    httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");
                } else {
                    if (extension.equals("html")) {
                        httpServletResponse.setContentType("text/html; charset=iso-8859-1");
                        httpServletResponse.addHeader("Content-disposition", "filename=documento.html");
                    } else {

                        // unzip(Constantes.RUTA_ARCHIVOS_TEMP + "/prueba.zip", Constantes.RUTA_ARCHIVOS_TEMP);
                        fileByte = decompress(fileByte);

                        String hola = new String(fileByte);
                        /*
                        try ( FileOutputStream fos = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/prueba2")) {
                            fos.write(fileByte);
                            fos.close();
                        }
                         */
                        httpServletResponse.setContentType("application/msword; charset=utf-8");
                        httpServletResponse.addHeader("Content-disposition", "filename=documento.doc");
                    }
                }

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                // if (!extension.equals("html")) {
                // if (esPDF) {
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                servletOutputStream.write(fileByte);
                /*} else {
                    // datos = new String(fileByte);
                    httpServletResponse.getWriter().print(datos);
                }
                 */
 /*
                } else {
                    httpServletResponse.getWriter().print(datos);
                }
                 */

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
    }

    public void prepareDialogoVerDoc(ExpActuaciones doc) {
        docImprimir = doc;

        PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public void actualizarEstadoActuacion(Personas per) {
        if (Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE.equals(estadoActuacion.getCodigo())) {
            // List<AntecedentesRolesPorPersonas> personas = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).getResultList();
            //if (!personas.isEmpty()) {
            if (actuacion.getPreopinante().equals(per)) {
                if (per.isRevisado()) {
                    List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE).getResultList();

                    if (!est.isEmpty()) {
                        estadoActuacion = est.get(0);
                        listaEstadosActuacion = new ArrayList<>();
                        listaEstadosActuacion.add(est.get(0));
                    }
                } else {
                    List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE).getResultList();

                    if (!est.isEmpty()) {

                        Integer iteracion = 1;
                        if (Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES.equals(actuacion.getEstado().getCodigo())) {
                            iteracion = 2;
                        }

                        estadoActuacion = est.get(0);
                        // listaEstadosActuacion = obtenerEstadosActuacion(estadoActuacion, iteracion, par.getTipoCircuitoFirmaActuaciones());
                        listaEstadosActuacion = obtenerEstadosActuacion(estadoActuacion);

                        /*
                        listaEstadosActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByRol", ExpEstadosActuacion.class).setParameter("rol", rolElegido).setParameter("iteracion", iteracion).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();

                        boolean encontro = false;
                        for (ExpEstadosActuacion est2 : listaEstadosActuacion) {
                            if (est2.equals(estadoActuacion)) {
                                encontro = true;
                                break;
                            }
                        }

                        if (!encontro) {
                            listaEstadosActuacion.add(estadoActuacion);
                        }
                        (
                         */
                    }
                }
            }

            //}
        }
    }

    public void actualizarPreop() {
        //System.err.println("");
    }

    public void actualizarFirmantes() {
        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                if (estadoActuacion != null) {
                    List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE).getResultList();
                    // List<ExpEstadosActuacion> est2 = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE).getResultList();

                    Personas perActual = null;
                    if (!est.isEmpty()) {

                        Personas preopinante = null;
                        if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(actuacion.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_OFICIO.equals(actuacion.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(actuacion.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(actuacion.getTipoActuacion().getId())) {
                            /*
                            List<AntecedentesRolesPorPersonas> personas = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).getResultList();
                            if (personas.isEmpty()) {
                                JsfUtil.addErrorMessage("No se pudo encontrar una persona con el rol de presidente");
                                estadoActuacion = estadoActuacionOriginal;
                                return;
                            }
                            preopinante = personas.get(0).getPersonas();
                             */
                            preopinante = actuacion.getPreopinante();
                        } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId())) {

                            preopinante = actuacion.getPreopinante();
                        }

                        boolean encontro = false;

                        if (listaPersonasFirmantes == null) {
                            listaPersonasFirmantes = new ArrayList<>();
                        }

                        for (Personas per : listaPersonasFirmantes) {
                            if (per.equals(preopinante)) {
                                perActual = per;
                                encontro = true;
                                break;
                            }
                        }
                        if (est.get(0).getOrden().compareTo(estadoActuacion.getOrden()) <= 0) {
                            if (!encontro) {
                                if (preopinante == null) {
                                    JsfUtil.addErrorMessage("Primero debe escojer un preopinante");
                                    estadoActuacion = estadoActuacionOriginal;
                                    return;
                                }
                                listaPersonasFirmantes.add(preopinante);
                                perActual = preopinante;
                                actuacion.setPreopinante(preopinante);
                            }

                            /*
                                if(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE.equals(estadoActuacion.getCodigo())){
                                    if(perActual != null){
                                        perActual.setRevisado(true);
                                        Date fecha = ejbFacade.getSystemDate();
                                        perActual.setFechaHoraRevisado(fecha);
                                        perActual.setPersonaRevisado(personaUsuario);
                                    }
                                }else if(est2.get(0).getOrden().compareTo(estadoActuacion.getOrden()) <= 0){
                                    if(perActual != null){
                                        perActual.setRevisado(false);
                                        perActual.setFechaHoraRevisado(null);
                                        perActual.setPersonaRevisado(null);
                                    }
                                }
                             */
                        } else {
                            if (encontro) {
                                listaPersonasFirmantes.remove(perActual);
                                /*
                                if (Constantes.TIPO_ACTUACION_OFICIO.equals(actuacion.getTipoActuacion().getId())
                                        || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(actuacion.getTipoActuacion().getId())) {
                                    actuacion.setPreopinante(null);
                                }
                                 */
                            }
                        }

                        estadoActuacionOriginal = estadoActuacion;
                    }
                }
            }
        }
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

    public String obtenerPersonaAcusadoraInformada(DocumentosJudiciales doc) {
        ExpPersonasAcusacion per = obtenerPersonaInformada(doc, Constantes.TIPO_PARTE_ACUSADOR);
        doc.setAcusadorInformado(per);
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

    public ExpPersonasAcusacion obtenerPersonaInformada(DocumentosJudiciales doc, Integer tipoParte) {
        ExpPersonasAcusacion per = null;

        String respuesta = "";
        ExpPersonasAcusacionPorDocumentosJudiciales acusador = null;

        if (doc != null) {
            try {
                acusador = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAcusacionPorDocumentosJudiciales.findByDocumentoJudicialEstadoTipoParte", ExpPersonasAcusacionPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).setParameter("tipoParte", tipoParte).getSingleResult();
            } catch (Exception ex) {
                ex.printStackTrace();
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

    /*
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

            if (personaActual != null) {
                if (personaActual.isFirmado() || personaActual.isRevisado()) {
                    JsfUtil.addErrorMessage("La actuación ya fue firmada o ya fue revisada por la persona, ya no puede ser borrada");
                    return;
                }

                listaPersonasFirmantes.remove(personaActual);

                personaActual.setEstado("IN");

                listaPersonasFirmantes.add(personaActual);
            }

        }
    }
     */
    public void agregarPersonaFirmante() {

        if (personaFirmante != null) {

            if (listaPersonasFirmantes == null) {
                listaPersonasFirmantes = new ArrayList<>();
            }

            if (actuacion != null) {
                if (actuacion.getTipoActuacion() != null) {
                    if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(actuacion.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_OFICIO.equals(actuacion.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(actuacion.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(actuacion.getTipoActuacion().getId())) {

                        JsfUtil.addErrorMessage("No se puede agregar firmantes a una providencia u oficio");
                        return;

                        /*
                        List<AntecedentesRolesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();

                        if (lista.isEmpty()) {
                            JsfUtil.addErrorMessage("No se pude encontro persona con rol de secretario");
                            return;
                        } else if (lista.get(0).getPersonas().equals(personaFirmante)) {
                            JsfUtil.addErrorMessage("No se pude agregar al secretario");
                            return;
                        }

                        List<AntecedentesRolesPorPersonas> lista2 = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).getResultList();

                        if (lista2.isEmpty()) {
                            JsfUtil.addErrorMessage("No se pude encontro persona con rol de presidente");
                            return;
                        } else if (lista2.get(0).getPersonas().equals(personaFirmante)) {
                            JsfUtil.addErrorMessage("No se pude agregar al presidente");
                            return;
                        }
                         */
                    }
                }
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
            }
        }
    }

    public void borrarPersonaFirmante(Personas personaActual) {

        if (listaPersonasFirmantes != null) {

            if (personaActual != null) {
                if (personaActual.isFirmado() || personaActual.isRevisado()) {
                    JsfUtil.addErrorMessage("La actuación ya fue firmada o ya fue revisada por la persona, ya no puede ser borrada");
                    return;
                }

                if (actuacion != null) {
                    if (actuacion.getPreopinante() != null) {
                        if (actuacion.getPreopinante().equals(personaActual)) {
                            JsfUtil.addErrorMessage("No se pude borrar al presidente/preopinante");
                            return;
                        }
                    }
                }
                /*
                List<AntecedentesRolesPorPersonas> per = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();

                if (per.isEmpty()) {
                    JsfUtil.addErrorMessage("No se pude encontro persona con rol de secretario");
                    return;
                } else if (per.get(0).getPersonas().equals(personaActual)) {
                    JsfUtil.addErrorMessage("No se pude borrar al secretario");
                    return;
                }
                 */

                if (actuacion.getSecretario() == null) {
                    JsfUtil.addErrorMessage("No se pude encontro el secretario correspondiente a esta actuación");
                    return;
                } else if (actuacion.getSecretario().equals(personaActual)) {
                    JsfUtil.addErrorMessage("No se pude borrar al secretario correspondiente a esta actuación");
                    return;
                }

                listaPersonasFirmantes.remove(personaActual);
            }

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

    public boolean desabilitarBotonBorrarFirmante(Personas per) {
        // return per.isFirmado() || deshabilitarCamposEdicionActuacion();
        return per.isFirmado();
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

    public void agregarInhibido() {

        if (inhibido != null) {

            if (listaInhibidosAdmin == null) {
                listaInhibidosAdmin = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaInhibidosAdmin) {
                if (per.equals(inhibido)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaInhibidosAdmin.add(inhibido);
            } else if (perActual != null) {
                if ("IN".equals(perActual.getEstado())) {
                    perActual.setEstado("AC");
                }
            }
        }
    }

    public void agregarRestringido() {

        if (restringido != null) {

            if (listaRestringidosAdmin == null) {
                listaRestringidosAdmin = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaRestringidosAdmin) {
                if (per.equals(restringido)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaRestringidosAdmin.add(restringido);
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

    public void borrarInhibido(Personas personaActual) {

        if (listaInhibidosAdmin != null) {

            listaInhibidosAdmin.remove(personaActual);

            personaActual.setEstado("IN");

            listaInhibidosAdmin.add(personaActual);

        }
    }

    public void borrarRestringido(Personas personaActual) {

        if (listaRestringidosAdmin != null) {

            listaRestringidosAdmin.remove(personaActual);

            personaActual.setEstado("IN");

            listaRestringidosAdmin.add(personaActual);

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

    private boolean personaHabilitada(Personas per, DocumentosJudiciales doc) {
        boolean resp = false;

        if (doc != null) {
            List<ExpPersonasHabilitadasPorDocumentosJudiciales> listaHabilitadasActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasHabilitadasPorDocumentosJudiciales.findByDocumentoJudicial", ExpPersonasHabilitadasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();

            for (ExpPersonasHabilitadasPorDocumentosJudiciales pers : listaHabilitadasActual) {
                if ("AC".equals(pers.getEstado().getCodigo()) && pers.getPersona().equals(per)) {
                    resp = true;
                    break;
                }
            }
        }

        return resp;
    }

    public void buscarActuaciones() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_SOLO_PRIMER_ESCRITO, rolElegido.getId()) || (getSelected().isRestringido() && !personaHabilitada(personaUsuario, getSelected()))) {
                listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicialVisibleTipoActuacion", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).setParameter("visible", true).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).getResultList();
                /* CAMBIOS PLAZO ENJUICIAMIENTO
                Date fecha = ejbFacade.getSystemDate();
                listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicialVisibleTipoActuacionFechaPresentacion", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).setParameter("visible", true).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).setParameter("fechaPresentacion", fecha).getResultList();
                 */
            } else if (filtroURL.verifPermiso(Constantes.PERMISO_VER_ACTUACIONES_NO_VISIBLES, rolElegido.getId()) || filtroURL.verifPermiso(Constantes.PERMISO_VER_ACTUACIONES_NO_VISIBLES_SI_FIRMANTE, rolElegido.getId()) || filtroURL.verifPermiso(Constantes.PERMISO_VER_ACTUACIONES_NO_VISIBLES_SI_EXSECRETARIO, rolElegido.getId())) {
                if ("1".equals(filtroRadio)) {
                    if (filtroURL.verifPermiso(Constantes.PERMISO_VER_ACTUACIONES_NO_VISIBLES_SI_FIRMANTE, rolElegido.getId()) || filtroURL.verifPermiso(Constantes.PERMISO_VER_ACTUACIONES_NO_VISIBLES_SI_EXSECRETARIO, rolElegido.getId())) {
                        List<String> estados = new ArrayList<>();
                        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_ACTUACIONES_NO_VISIBLES_SI_FIRMANTE, rolElegido.getId())) {
                            estados.add("FM");
                            estados.add("RP");
                            estados.add("FP");
                        } else {
                            estados.add("FE");
                        }

                        listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicialANDEstadosActuacion", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).setParameter("personaFirmante", personaSelected).setParameter("estados", estados).setParameter("limite", 0).getResultList();
                    } else {
                        listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicial", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).getResultList();
                    }
                } else {
                    String comando = "SELECT t.* FROM exp_actuaciones as t WHERE t.documento_judicial = ?1 AND (SELECT count(*) > 0 FROM exp_personas_firmantes_por_actuaciones as e WHERE e.actuacion = t.id AND e.persona_firmante = ?2 AND e.firmado = false AND e.estado = 'AC') ORDER BY t.fecha_presentacion, t.fecha_hora_alta";
                    listaActuaciones = ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).setParameter(1, getSelected().getId()).setParameter(2, personaUsuario.getId()).getResultList();

                    if (listaActuaciones.isEmpty()) {
                        // List<ExpPersonasAsociadas> per = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();
                        if (personaSelected != null) {
                            listaActuaciones = ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).setParameter(1, getSelected().getId()).setParameter(2, personaSelected.getId()).getResultList();
                        }
                    }
                }
            } else {
                listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicialVisible", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).setParameter("visible", true).getResultList();
                /* CAMBIOS PLAZO ENJUICIAMIENTO
                Date fecha = ejbFacade.getSystemDate();
                List<Integer> lista = new ArrayList<>();
                lista.add(Constantes.TIPO_ACTUACION_DOCUMENTAL);
                lista.add(Constantes.TIPO_ACTUACION_ESCRITO_PRESENTACION);
                lista.add(Constantes.TIPO_ACTUACION_INCIDENTE);
                lista.add(Constantes.TIPO_ACTUACION_PRESENTACION);
                lista.add(Constantes.TIPO_ACTUACION_PRIMER_ESCRITO);
                lista.add(Constantes.TIPO_ACTUACION_RECURSO);
                lista.add(Constantes.TIPO_ACTUACION_RECUSACION);
                listaActuaciones = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicialVisibleFechaPresentacion", ExpActuaciones.class).setParameter("documentoJudicial", getSelected()).setParameter("visible", true).setParameter("fechaPresentacion", fecha).setParameter("tiposActuacion1", lista).setParameter("tiposActuacion2", lista).getResultList();
                 */
            }
        } else {
            listaActuaciones = null;
        }

        if (listaActuaciones != null) {
            for (ExpActuaciones act : listaActuaciones) {
                if (act.getTipoActuacion() != null) {
                    if (act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_OFICIO)
                            || act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_OFICIO_CORTE)) {
                        act.setNroFinalString(act.getNroFinal());
                    } else if (act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_SD)
                            || act.getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_RESOLUCION)) {
                        act.setNroFinalString(act.getResolucion() == null ? null : act.getResolucion().getNroResolucion());
                    } else {
                        act.setNroFinalString(null);
                    }
                }
            }
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

    public void prepareCerrarDialogoVerTodoDoc() {
        File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
        f.delete();
    }

    public void prepareCerrarDialogoPrevisualizar() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
            f.delete();
        }
    }

    /*
    public void prepareCerrarCreateActuacion() {
        if (actuacion != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
            f.delete();
        }
    }
     */

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

        if (!filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_PRESENTACION, rolElegido.getId())) {
            JsfUtil.addErrorMessage("No tiene permiso para borrar actuaciones");
            return;
        }

        boolean firmada = false;
        boolean revisada = false;
        List<ExpPersonasFirmantesPorActuaciones> listaVerif = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacion", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).getResultList();

        for (ExpPersonasFirmantesPorActuaciones est : listaVerif) {

            if (est.isFirmado()) {
                firmada = true;
                break;
            }

            if (est.isRevisado()) {
                revisada = true;
                break;
            }
        }

        if (!filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_FIRMADAS, rolElegido.getId()) && firmada) {
            JsfUtil.addErrorMessage("No tiene permiso para borrar actuaciones firmadas");
            return;
        }

        if (!filtroURL.verifPermiso(Constantes.PERMISO_BORRAR_FIRMADAS, rolElegido.getId()) && revisada) {
            JsfUtil.addErrorMessage("No tiene permiso para borrar actuaciones revisadas");
            return;
        }

        if (act != null) {

            if (act.isVisible()) {
                JsfUtil.addErrorMessage("No se pueden borrar actuaciones visibles");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            /*
            ExpHistActuaciones hact = new ExpHistActuaciones();


            hact.setActuacion(act.getId());
            if (act.getActuacionPadre() != null) {
                hact.setActuacionPadre(act.getActuacionPadre().getId());
            } else {
                hact.setActuacionPadre(null);
            }

            hact.setArchivo(act.getArchivo());
            hact.setDescripcion(act.getDescripcion());
            hact.setDocumentoJudicial(act.getDocumentoJudicial());
            hact.setEmpresa(act.getEmpresa());
            hact.setFechaHoraUltimoEstadoOriginal(act.getFechaHoraUltimoEstado());
            hact.setFechaHoraAltaOriginal(act.getFechaHoraAlta());
            hact.setFechaHoraVisible(act.getFechaHoraVisible());
            hact.setFechaPresentacion(act.getFechaPresentacion());
            hact.setFojas(act.getFojas());
            hact.setHash(act.getHash());
            hact.setObjetoActuacion(act.getObjetoActuacion());
            hact.setPersona(act.getPersonaAlta());
            hact.setPersonaUltimoEstado(act.getPersonaUltimoEstado());
            hact.setPersonaVisible(act.getPersonaVisible());
            hact.setResolucion(act.getResolucion());
            hact.setTipoActuacion(act.getTipoActuacion());
            hact.setBorrado(true);
            hact.setPersonaBorrado(personaUsuario);
            hact.setFechaHoraAlta(act.getFechaHoraAlta());
            hact.setFechaHoraBorrado(fecha);

            expHistActuacionesController.setSelected(hact);
            expHistActuacionesController.saveNew(null);
             */
            List<ObservacionesDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByActuacion", ObservacionesDocumentosJudiciales.class).setParameter("actuacion", act).getResultList();

            boolean modificar = false;
            if (getSelected() != null) {
                if (!lista.isEmpty()) {
                    if (lista.get(0).equals(getSelected().getObservacionDocumentoJudicial())) {
                        getSelected().setObservacionDocumentoJudicial(null);
                        getSelected().setUltimaObservacion("");
                        super.save(null);
                        modificar = true;
                    }
                }
            }

            for (ObservacionesDocumentosJudiciales obs : lista) {
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setPersonaUltimoEstado(personaUsuario);
                obsController.setSelected(obs);
                obsController.save(null);
                obsController.delete(null);

            }

            if (modificar) {
                List<ObservacionesDocumentosJudiciales> listaObs = ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();
                if (!listaObs.isEmpty()) {
                    getSelected().setObservacionDocumentoJudicial(listaObs.get(0));
                    getSelected().setUltimaObservacion(listaObs.get(0).getObservacion());
                    super.save(null);

                }
            }

            List<EstadosProcesalesDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByActuacion", EstadosProcesalesDocumentosJudiciales.class).setParameter("actuacion", act).getResultList();

            modificar = false;
            if (getSelected() != null) {
                if (!lista2.isEmpty()) {
                    if (lista2.get(0).equals(getSelected().getEstadoProcesalDocumentoJudicial())) {
                        getSelected().setEstadoProcesalDocumentoJudicial(null);
                        getSelected().setEstadoProcesal("");
                        super.save(null);
                        modificar = true;
                    }
                }
            }

            for (EstadosProcesalesDocumentosJudiciales est : lista2) {
                est.setFechaHoraUltimoEstado(fecha);
                est.setPersonaUltimoEstado(personaUsuario);
                estadosProcesalesDocumentosJudicialesController.setSelected(est);
                estadosProcesalesDocumentosJudicialesController.save(null);
                estadosProcesalesDocumentosJudicialesController.delete(null);

            }

            if (modificar) {
                List<EstadosProcesalesDocumentosJudiciales> listaEst = ejbFacade.getEntityManager().createNamedQuery("EstadosProcesalesDocumentosJudiciales.findByDocumentoJudicial", EstadosProcesalesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();
                if (!listaEst.isEmpty()) {
                    getSelected().setEstadoProcesalDocumentoJudicial(listaEst.get(0));
                    getSelected().setEstadoProcesal(listaEst.get(0).getEstadoProcesal());
                    super.save(null);

                }
            }
            // exp_personas_firmantes_por_actuaciones
            List<ExpNotificaciones> lista6 = ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByActuacion", ExpNotificaciones.class).setParameter("actuacion", act).getResultList();

            for (ExpNotificaciones est : lista6) {

                est.setFechaHoraUltimoEstado(fecha);
                est.setPersonaUltimoEstado(personaUsuario);
                expNotificacionController.setSelected(est);
                expNotificacionController.save(null);

                expNotificacionController.delete(null);

            }

            if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())) {

                // exp_personas_firmantes_por_actuaciones
                List<ResuelvePorResolucionesPorPersonas> lista7 = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByResolucion", ResuelvePorResolucionesPorPersonas.class).setParameter("resolucion", act.getResolucion()).getResultList();

                for (ResuelvePorResolucionesPorPersonas est : lista7) {

                    est.setFechaHoraUltimoEstado(fecha);
                    est.setPersonaUltimoEstado(personaUsuario);
                    resuelvePorResolucionesPorPersonasController.setSelected(est);
                    resuelvePorResolucionesPorPersonasController.save(null);

                    resuelvePorResolucionesPorPersonasController.delete(null);
                }

                if (act.getResolucion() != null) {
                    List<Resoluciones> res = ejbFacade.getEntityManager().createNamedQuery("Resoluciones.findById", Resoluciones.class).setParameter("id", act.getResolucion().getId()).getResultList();

                    if (!res.isEmpty()) {
                        res.get(0).setFechaHoraUltimoEstado(fecha);
                        res.get(0).setPersonaUltimoEstado(personaUsuario);
                        resolucionesController.setSelected(res.get(0));
                        resolucionesController.save(null);
                        resolucionesController.delete(null);

                    }
                }

            }

            // exp_personas_firmantes_por_actuaciones
            List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacion", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).getResultList();

            for (ExpPersonasFirmantesPorActuaciones est : lista3) {
                /*
                ExpHistPersonasFirmantesPorActuaciones hist = new ExpHistPersonasFirmantesPorActuaciones();
                hist.setDocumentoJudicial(est.getDocumentoJudicial());
                hist.setEstado(est.getEstado());
                hist.setFechaHoraAlta(est.getFechaHoraAlta());
                hist.setFechaHoraBorrado(fecha);
                hist.setFechaHoraFirma(est.getFechaHoraFirma());
                hist.setFechaHoraRevisado(est.getFechaHoraRevisado());
                hist.setFirmado(est.isFirmado());
                hist.setHistActuacion(expHistActuacionesController.getSelected());
                hist.setIdRol(est.getIdRol());
                hist.setPersonaAlta(est.getPersonaAlta());
                hist.setPersonaBorrado(personaUsuario);
                hist.setPersonaFirma(est.getPersonaFirma());
                hist.setPersonaFirmante(est.getPersonaFirmante());
                hist.setPersonaRevisado(est.getPersonaRevisado());
                hist.setRevisado(est.isRevisado());

                expHistPersonasFirmantesPorActuacionesController.setSelected(hist);
                expHistPersonasFirmantesPorActuacionesController.saveNew(null);
                 */

                est.setFechaHoraUltimoEstado(fecha);
                est.setPersonaUltimoEstado(personaUsuario);
                personasFirmantesPorActuacionesController.setSelected(est);
                personasFirmantesPorActuacionesController.save(null);

                personasFirmantesPorActuacionesController.delete(null);

            }

            // exp_personas_firmantes_por_actuaciones
            List<Firmas> lista5 = ejbFacade.getEntityManager().createNamedQuery("Firmas.findByActuacion", Firmas.class).setParameter("actuacion", act).getResultList();

            for (Firmas est : lista5) {
                /*
                HistFirmas hist = new HistFirmas();

                hist.setDocumentoJudicial(est.getDocumentoJudicial());
                hist.setEmpresa(est.getEmpresa());
                hist.setEstado(est.getEstado());
                hist.setFechaHora(est.getFechaHora());
                hist.setFechaHoraBorrado(fecha);
                hist.setHistActuacion(expHistActuacionesController.getSelected());
                hist.setHistActuacionOriginal(est.getHistActuacion());
                hist.setPersona(est.getPersona());
                hist.setPersonaBorrado(personaUsuario);
                hist.setSesion(est.getSesion());

                histFirmasController.setSelected(hist);
                histFirmasController.saveNew(null);
                 */

                est.setFechaHoraUltimoEstado(fecha);
                est.setPersonaUltimoEstado(personaUsuario);
                firmasController.setSelected(est);
                firmasController.save(null);
                firmasController.delete(null);

            }

            List<ExpRolesFirmantesPorActuaciones> lista4 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacion", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", act).getResultList();

            for (ExpRolesFirmantesPorActuaciones est : lista4) {
                est.setFechaHoraUltimoEstado(fecha);
                est.setPersonaUltimoEstado(personaUsuario);
                rolesFirmantesPorActuacionesController.setSelected(est);
                rolesFirmantesPorActuacionesController.save(null);
                rolesFirmantesPorActuacionesController.delete(null);
            }

            List<ExpCambiosEstadoActuacion> listaEstados = ejbFacade.getEntityManager().createNamedQuery("ExpCambiosEstadoActuacion.findByActuacion", ExpCambiosEstadoActuacion.class).setParameter("actuacion", act).getResultList();

            for (ExpCambiosEstadoActuacion obs : listaEstados) {
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setPersonaUltimoEstado(personaUsuario);
                expCambiosEstadoActuacionController.setSelected(obs);
                expCambiosEstadoActuacionController.save(null);
                expCambiosEstadoActuacionController.delete(null);
            }

            List<ExpCambiosTextoActuacion> listaTextos = ejbFacade.getEntityManager().createNamedQuery("ExpCambiosTextoActuacion.findByActuacion", ExpCambiosTextoActuacion.class).setParameter("actuacion", act).getResultList();

            for (ExpCambiosTextoActuacion obs : listaTextos) {
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setPersonaUltimoEstado(personaUsuario);
                expCambiosTextoActuacionController.setSelected(obs);
                expCambiosTextoActuacionController.save(null);
                expCambiosTextoActuacionController.delete(null);
            }

            act.setActuacionPadre(null);
            act.setFechaHoraUltimoEstado(fecha);
            act.setPersonaUltimoEstado(personaUsuario);
            act.setPersonaBorrado(personaUsuario);
            act.setFechaHoraBorrado(fecha);
            expActuacionesController.setSelected(act);
            expActuacionesController.save(null);
            expActuacionesController.delete(null);
            buscarActuaciones();
        }
    }

    public boolean renderedVisible() {
        return filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_VISIBLE_EXPEDIENTE, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_VISIBLE_EXPEDIENTE, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_VER_VISIBLE_EXPEDIENTE, rolElegido.getId());
    }

    public boolean renderedRestringido() {
        return filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_EXPEDIENTE_RESTRINGIDO, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_EXPEDIENTE_RESTRINGIDO, rolElegido.getId());
    }

    public boolean renderedEstadoActuacion() {
        return filtroURL.verifPermiso(Constantes.PERMISO_EDITAR_ACTUACION, rolElegido.getId());
    }

    public boolean renderedRevisado() {
        return filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_VER_REVISADO_EXPEDIENTE, rolElegido.getId());
    }

    public boolean renderedEditarActuacion() {
        return filtroURL.verifPermiso(Constantes.PERMISO_EDITAR_ACTUACION, rolElegido.getId());
    }

    public boolean renderedNotificarManualmente() {
        return filtroURL.verifPermiso(Constantes.PERMISO_NOTIFICAR_MANUALMENTE, rolElegido.getId());
    }

    public boolean renderedRegenerarQR() {
        return filtroURL.verifPermiso(Constantes.PERMISO_REGENERAR_QR, rolElegido.getId());
    }

    public boolean renderedEnviarCorte() {
        return filtroURL.verifPermiso(Constantes.PERMISO_ENVIAR_CORTE, rolElegido.getId());
    }

    public boolean renderedEnviarManualmenteCorte() {
        return filtroURL.verifPermiso(Constantes.PERMISO_ENVIAR_MANUALMENTE_CORTE, rolElegido.getId());
    }

    public boolean renderedRetrocederActuaciones() {
        return filtroURL.verifPermiso(Constantes.PERMISO_RETROCEDER_ACTUACIONES, rolElegido.getId());
    }

    public boolean renderedFinalizarActuaciones() {
        return filtroURL.verifPermiso(Constantes.PERMISO_FINALIZAR_ACTUACIONES, rolElegido.getId());
    }

    public boolean renderedCorregirFirmantes() {
        return filtroURL.verifPermiso(Constantes.PERMISO_CORREGIR_FIRMANTES, rolElegido.getId());
    }

    public boolean deshabilitarRetrocederActuaciones(ExpActuaciones act) {
        if (act != null) {
            if (act.getEstado() != null) {
                return !(filtroURL.verifPermiso(Constantes.PERMISO_RETROCEDER_ACTUACIONES, rolElegido.getId()) && !(Constantes.ESTADO_ACTUACION_FINALIZADA.equals(act.getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_EN_PROYECTO.equals(act.getEstado().getCodigo())));
            }
        }
        return true;
    }

    public boolean deshabilitarCorregirFirmantes(ExpActuaciones act) {
        if (act != null) {
            if (act.getEstado() != null) {
                return !((Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())) && filtroURL.verifPermiso(Constantes.PERMISO_CORREGIR_FIRMANTES, rolElegido.getId()) && (Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(act.getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO.equals(act.getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO.equals(act.getEstado().getCodigo())));
            }
        }
        return true;
    }

    public boolean deshabilitarFinalizarActuaciones(ExpActuaciones act) {
        if (act != null) {
            if (act.getEstado() != null) {
                return !(filtroURL.verifPermiso(Constantes.PERMISO_FINALIZAR_ACTUACIONES, rolElegido.getId()) && (Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO.equals(act.getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO.equals(act.getEstado().getCodigo())));
            }
        }
        return true;
    }

    public boolean deshabilitarExpedienteCorte() {
        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                return !Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(actuacion.getTipoActuacion().getId());
            }
        }
        return true;
    }

    public boolean deshabilitarEnviarManualmenteCorte(ExpActuaciones item) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_ENVIAR_MANUALMENTE_CORTE, rolElegido.getId())) {
            return (item != null ? (item.getDocumentoJudicialCorte() != null ? item.getCodActuacionCaso() != null : true) : true);
        }
        return true;
    }

    public boolean deshabilitarRestringido() {
        return !filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_EXPEDIENTE_RESTRINGIDO, rolElegido.getId());
    }

    public boolean deshabilitarEditarActuacion(ExpActuaciones act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_EDITAR_ACTUACION, rolElegido.getId())) {
            if (act != null) {
                if (act.getEstado() != null) {
                    return !(act.getEstado() != null && (act.getArchivo() == null || Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(act.getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES.equals(act.getEstado().getCodigo())));
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    public boolean deshabilitarNotificarManualmente(ExpActuaciones act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_NOTIFICAR_MANUALMENTE, rolElegido.getId())) {
            if (act != null) {
                if (Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId())) {
                    if (act.getFormato() != null) {
                        if (act.getEstado() != null) {
                            if (Constantes.ESTADO_ACTUACION_FINALIZADA.equals(act.getEstado().getCodigo())) {
                                return act.isNotificado();
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarRegenerarQR(ExpActuaciones act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_REGENERAR_QR, rolElegido.getId())) {
            if (act != null) {
                if (act.getTipoActuacion() != null) {

                    /*if (Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(act.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())
                            || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId())) {
                     */
                    if (act.getEstado() != null) {
                        // if (act.getFormato() != null) {
                        if (Constantes.ESTADO_ACTUACION_FINALIZADA.equals(act.getEstado().getCodigo())) {
                            return !(act.getHash() != null && act.getArchivo() != null);
                            // return act.isRegeneradoqr();
                        }
                        // }
                    } else {
                        return !(act.getHash() != null && act.getArchivo() != null);
                    }
                    // }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarEstadoActuacionCreate() {
        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                return ((Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId())) && actuacion.getPreopinante() == null);
            }
        }
        return false;
    }

    public boolean deshabilitarCamposEdicionActuacion() {
        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                if (actuacion.getEstado() != null) {

                    if (Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE.equals(actuacion.getEstado().getCodigo())) {
                        if (actuacion.getPreopinante() != null) {
                            return !(esPersonaAsociada(actuacion.getPreopinante(), personaUsuario) && !actuacion.getPreopinante().equals(personaUsuario));
                        }
                    } else if (Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE.equals(actuacion.getEstado().getCodigo())) {
                        boolean resp = (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante().equals(personaUsuario)) : true;;
                        return resp;
                        //return (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante() == personaUsuario) : true;

                    } else if (Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(actuacion.getEstado().getCodigo())) {
                        if (actuacion.getPreopinante() != null) {
                            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class
                            ).setParameter("actuacion", actuacion).setParameter("personaFirmante", actuacion.getPreopinante()).setParameter("estado", new Estados("AC")).getResultList();
                            if (lista.isEmpty()) {
                                //JsfUtil.addErrorMessage("No se encuentra persona firmante para el preopinante");
                                return true;
                            }

                            if (lista.get(0).isRevisado()) {
                                return !(actuacion.getPreopinante().equals(personaUsuario));
                            } else {
                                return !esPersonaAsociada(actuacion.getPreopinante(), personaOrigen);
                            }
                        }
                        return (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante().equals(personaUsuario)) : true;

                    }

                    List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacionIteracion", ExpEstadosActuacionPorRoles.class
                    ).setParameter("rol", rolElegido).setParameter("estadoActuacion", actuacion.getEstado()).setParameter("iteracion", 1).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();
                    if (!lista.isEmpty()) {
                        return lista.get(0).isDeshabilitar();
                    }
                }
            }
        }

        return true;
    }

    public boolean deshabilitarEstadoActuacion() {

        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                if (actuacion.getEstado() != null) {

                    if (!actuacion.getEstado().equals(estadoActuacion)) {
                        return true;
                    }

                    if (Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE.equals(actuacion.getEstado().getCodigo())) {

                        boolean deshabilitar = false;
                        List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacionIteracion", ExpEstadosActuacionPorRoles.class
                        ).setParameter("rol", rolElegido).setParameter("estadoActuacion", actuacion.getEstado()).setParameter("iteracion", 1).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();
                        if (!lista.isEmpty()) {
                            deshabilitar = lista.get(0).isDeshabilitar();
                        }

                        if (actuacion.getPreopinante() != null) {
                            return !((esPersonaAsociada(actuacion.getPreopinante(), personaUsuario) && !actuacion.getPreopinante().equals(personaUsuario)) || !deshabilitar);
                        }
                    } else if (Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE.equals(actuacion.getEstado().getCodigo())) {
                        // boolean resp = (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante().equals(personaUsuario)) : true;
                        boolean resp = !esPersonaAsociada(actuacion.getPreopinante(), personaUsuario);
                        return resp;
                        //return (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante() == personaUsuario) : true;

                    } else if (Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(actuacion.getEstado().getCodigo())) {
                        List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class
                        ).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                        if (!lista2.isEmpty()) {
                            return !lista2.get(0).equals(personaUsuario);

                        }

                        if (actuacion.getPreopinante() != null) {
                            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class
                            ).setParameter("actuacion", actuacion).setParameter("personaFirmante", actuacion.getPreopinante()).setParameter("estado", new Estados("AC")).getResultList();
                            if (lista.isEmpty()) {
                                //JsfUtil.addErrorMessage("No se encuentra persona firmante para el preopinante");
                                return true;
                            }

                            if (lista.get(0).isRevisado()) {
                                return !(actuacion.getPreopinante().equals(personaUsuario));
                            } else {
                                return !esPersonaAsociada(actuacion.getPreopinante(), personaOrigen);
                            }
                        }
                        return (actuacion.getPreopinante() != null) ? !(actuacion.getPreopinante().equals(personaUsuario)) : true;
                    } else if (Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES.equals(actuacion.getEstado().getCodigo())) {

                        return !filtroURL.verifPermiso("adminFirmantes");
                        /*
                        List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                        if (!lista.isEmpty()) {
                            return !lista.get(0).equals(personaUsuario);
                        }
                         */

                    }

                    List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacionIteracion", ExpEstadosActuacionPorRoles.class
                    ).setParameter("rol", rolElegido).setParameter("estadoActuacion", actuacion.getEstado()).setParameter("iteracion", 1).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();
                    if (!lista.isEmpty()) {
                        return lista.get(0).isDeshabilitar();
                    }
                }
            }
        }
        /*
        if(!listaFormatos.isEmpty()){
            if(actuacion != null){
                if(actuacion.getEstado() != null){
                    if(Constantes.ESTADO_ACTUACION_EN_PROYECTO.equals(actuacion.getEstado().getCodigo()) ||
                      Constantes.ESTADO_ACTUACION_REVISION_SECRETARIO.equals(actuacion.getEstado().getCodigo()) ||
                      Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE.equals(actuacion.getEstado().getCodigo())){
                        return actuacion.getEstado().equals(listaEstadosActuacion.get(listaEstadosActuacion.size() - 1));
                    }else if(Constantes.ESTADO_ACTUACION_FINALIZADA.equals(actuacion.getEstado().getCodigo())){
                        return actuacion.getEstado().equals(listaEstadosActuacion.get(listaEstadosActuacion.size() - 1)) && !Constantes.ROL_RELATOR.equals(rolElegido.getId());
                    }
                }
            }
        }
         */
        return true;
    }

    public boolean deshabilitarDatosResolucion() {

        if (filtroURL.verifPermiso(Constantes.PERMISO_EDITAR_DATOS_RESOLUCIONES, rolElegido.getId())) {
            if (actuacion != null) {
                if (actuacion.getTipoActuacion() != null) {
                    return !(Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId()));
                }
            }
        }

        return true;
    }

    public boolean deshabilitarDatosResolucionEdit() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_EDITAR_DATOS_RESOLUCIONES, rolElegido.getId())) {
            if (actuacion != null) {
                if (actuacion.getTipoActuacion() != null) {
                    if (estadoActuacion != null) {
                        if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId())) {
                            if (actuacion.getEstado() != null) {
                                List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacionIteracion", ExpEstadosActuacionPorRoles.class
                                ).setParameter("rol", rolElegido).setParameter("estadoActuacion", estadoActuacion).setParameter("iteracion", 1).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();
                                if (!lista.isEmpty()) {
                                    return lista.get(0).isDeshabilitar();
                                }
                            } else {
                                /*
                                List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacion", ExpEstadosActuacionPorRoles.class).setParameter("rol", rolElegido).setParameter("estadoActuacion", actuacion.getEstado()).getResultList();
                                if (!lista.isEmpty()) {
                                    return lista.get(0).isDeshabilitar();
                                }
                                 */
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean deshabilitarPreopinanteEdit() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_PREOPINANTES, rolElegido.getId())) {
            if (actuacion != null) {
                if (actuacion.getTipoActuacion() != null) {
                    if (estadoActuacion != null) {
                        if (actuacion.getEstado() != null) {

                            List<ExpEstadosActuacion> estRev = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class
                            ).setParameter("codigo", Constantes.ESTADO_ACTUACION_REVISION_SECRETARIO).getResultList();

                            if (estRev.isEmpty()) {
                                return true;

                            }

                            List<ExpEstadosActuacion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByMenorOrden", ExpEstadosActuacion.class
                            ).setParameter("orden", estRev.get(0).getOrden()).getResultList();

                            boolean encontro = false;
                            for (ExpEstadosActuacion est : lista) {
                                if (est.equals(estadoActuacion)) {
                                    encontro = true;
                                }
                            }

                            return !encontro;
                            /*
                            List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacionIteracion", ExpEstadosActuacionPorRoles.class).setParameter("rol", rolElegido).setParameter("estadoActuacion", estadoActuacion).setParameter("iteracion", 1).setParameter("tipoCircuitoFirma", par.getTipoCircuitoFirmaActuaciones()).getResultList();
                            if (!lista.isEmpty()) {
                                return lista.get(0).isDeshabilitar();
                            }
                             */
                        } else {
                            /*
                            List<ExpEstadosActuacionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacionPorRoles.findByRolEstadoActuacion", ExpEstadosActuacionPorRoles.class).setParameter("rol", rolElegido).setParameter("estadoActuacion", actuacion.getEstado()).getResultList();
                            if (!lista.isEmpty()) {
                                return lista.get(0).isDeshabilitar();
                            }
                             */
                            return true;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean deshabilitarAgregarPersonasFirmantes() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_EDITAR_DATOS_RESOLUCIONES, rolElegido.getId())) {
            if (actuacion != null) {
                if (actuacion.getTipoActuacion() != null) {
                    if (estadoActuacion != null) {
                        if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId())) {
                            if (actuacion.getEstado() != null) {
                                return !Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES.equals(actuacion.getEstado().getCodigo());
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean deshabilitarEditor() {
        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                return !(Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId()));
            }
        }

        return true;
    }

    public boolean deshabilitarPrevisualizar() {
        if (actuacion != null) {
            if (actuacion.getFormato() != null) {
                if (actuacion.getTipoActuacion() != null) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean deshabilitarFormatos() {
        if (actuacion != null) {
            // return (actuacion.getTipoActuacion() == null) || (actuacion.getEstado() == null) ? false : deshabilitarCamposEdicionActuacion();
            return (actuacion.getTipoActuacion() != null) ? false : deshabilitarCamposEdicionActuacion();
        }
        return true;
    }

    public boolean deshabilitarModificarCaratula() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_MODIFICAR_DATOS_EXPEDIENTE, rolElegido.getId())) {
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarModificarMotivoProceso() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_MODIFICAR_DATOS_EXPEDIENTE, rolElegido.getId())) {
                // return !getSelected().isExpedienteViejo();
                return false;
            }
        }

        return true;
    }

    public boolean deshabilitarModificarAno() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_MODIFICAR_DATOS_EXPEDIENTE, rolElegido.getId())) {
                return !getSelected().isExpedienteViejo();
            }
        }

        return true;
    }

    public boolean deshabilitarModificarCausa() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_MODIFICAR_DATOS_EXPEDIENTE, rolElegido.getId())) {
                return !getSelected().isExpedienteViejo();
            }
        }

        return true;
    }

    public boolean deshabilitarModificarTipoExpediente() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_MODIFICAR_DATOS_EXPEDIENTE, rolElegido.getId())) {
                return !getSelected().isExpedienteViejo();
            }
        }

        return true;
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
            return act.getProvidenciaDesglose() == null ? act.isVisible() : false;
        }
        return false;
    }

    public boolean deshabilitarVisible(ExpActuaciones act) {

        if (act != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_VISIBLE_EXPEDIENTE, rolElegido.getId()) && !act.isVisible()) {
                return act.getFormato() != null && (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(act.getTipoActuacion().getId()));
            } else if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_VISIBLE_EXPEDIENTE, rolElegido.getId()) && !act.isVisible()) {
                return !(act.getPersonaAlta() == personaUsuario && act.getFormato() == null && !(Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(act.getTipoActuacion().getId())));
            }
            /*
        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_VISIBLE_EXPEDIENTE, rolElegido.getId()) ){
                return false;
            }else if(filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_VISIBLE_EXPEDIENTE, rolElegido.getId())) {
                return !(act.getPersonaAlta() == personaUsuario);
            }
             */
        }
        return true;
    }

    public boolean deshabilitarBotonRevisado(ExpActuaciones act) {

        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            return (act == null) ? false : !(act.getFormato() == null);
        } else if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            return (act == null) ? false : !(act.getFormato() == null);
        } else if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            return (act == null) ? false : !(act.getFormato() == null);
        }
        return true;
    }

    /*
    public boolean deshabilitarBotonRevisado(ExpActuaciones act) {

        
        // if (act != null) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            return false;
        } else if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            return false;
        } else if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            return false;
        }
        
        //} else {
        //    if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId()) ){
        //        return false;
        //    }else if(filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())) {
        //        return !esPersonaAsociada();
        //    }
        
        // }
        return true;
    }
     */
    //public boolean deshabilitarBotonRevisado(ExpActuaciones act) {
    public boolean deshabilitarBotonRevisado() {
        return deshabilitarBotonRevisado(null);
    }

    public boolean deshabilitarRevisadoPersona(Personas per) {

        if (per != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())) {
                return per.isRevisado();
            } else if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())) {
                return per.isRevisado() || !esPersonaAsociada(per, personaUsuario) || !(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE.equals(estadoActuacion.getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(estadoActuacion.getCodigo()));
            }
            /*
        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId()) ){
                return false;
            }else if(filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())) {
                return !esPersonaAsociada();
            }
             */
        }
        return true;
    }

    public boolean deshabilitarRevisado(ExpPersonasFirmantesPorActuaciones act) {

        if (act != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())) {
                return act.isRevisado() || !filtroURL.verifPermiso(Constantes.PERMISO_BOTON_REVISADO, rolElegido.getId());
            } else if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())) {
                return act.isRevisado() || !esPersonaAsociada(act.getPersonaFirmante(), personaUsuario) || !filtroURL.verifPermiso(Constantes.PERMISO_BOTON_REVISADO, rolElegido.getId());
            }
            /*
        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId()) ){
                return false;
            }else if(filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())) {
                return !esPersonaAsociada();
            }
             */
        }
        return true;
    }

    public void actualizarRestringido() {
        if (getSelected() != null) {
            super.save(null);
        }
    }

    public void retrocederActuaciones(ExpActuaciones act) {
        if (act != null) {
            if (act.getEstado() != null) {
                if (!(Constantes.ESTADO_ACTUACION_FINALIZADA.equals(act.getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_EN_PROYECTO.equals(act.getEstado().getCodigo()))) {

                    try {

                        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                        Date fecha = ejbFacade.getSystemDate();

                        ut.begin();

                        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery("UPDATE exp_personas_firmantes_por_actuaciones SET fecha_hora_borrado = ?1, persona_borrado = ?2 WHERE actuacion = ?3");
                        query.setParameter(1, fecha);
                        query.setParameter(2, personaUsuario.getId());
                        query.setParameter(3, act.getId());
                        query.executeUpdate();

                        ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.deleteByActuacion", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).executeUpdate();

                        query = ejbFacade.getEntityManager().createNativeQuery("UPDATE exp_actuaciones SET estado = ?1, archivo = null, nro_final = null, texto_final = null WHERE id = ?2");
                        query.setParameter(1, Constantes.ESTADO_ACTUACION_EN_PROYECTO);
                        query.setParameter(2, act.getId());
                        query.executeUpdate();

                        ut.commit();

                        buscarActuaciones();
                    } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    public void finalizarActuaciones(ExpActuaciones act) {
        if (act != null) {
            if (act.getEstado() != null) {
                if (Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO.equals(act.getEstado().getCodigo()) || Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO.equals(act.getEstado().getCodigo())) {

                    List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getResultList();

                    if (!est.isEmpty()) {
                        if (act.getSecretario() != null) {
                            try {
                                List<Firmas> lista = ejbFacade.getEntityManager().createNamedQuery("Firmas.findByActuacionANDPersona", Firmas.class).setParameter("actuacion", act).setParameter("persona", act.getSecretario()).getResultList();

                                if (!lista.isEmpty()) {

                                    List<ExpPersonasFirmantesPorActuaciones> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", act.getSecretario()).setParameter("estado", new Estados("AC")).getResultList();

                                    if (!lista2.isEmpty()) {

                                        ut.begin();

                                        Date fecha = ejbFacade.getSystemDate();

                                        lista2.get(0).setFechaHoraFirma(lista.get(0).getFechaHora());
                                        lista2.get(0).setFirmado(true);
                                        lista2.get(0).setFechaHoraUltimoEstado(fecha);
                                        lista2.get(0).setPersonaUltimoEstado(personaUsuario);

                                        personasFirmantesPorActuacionesController.setSelected(lista2.get(0));
                                        personasFirmantesPorActuacionesController.save(null);

                                        act.setEstado(est.get(0));
                                        act.setVisible(true);
                                        act.setPersonaVisible(personaUsuario);
                                        act.setFechaHoraVisible(lista.get(0).getFechaHora());
                                        act.setFechaPresentacion(generarFechaPresentacion(lista.get(0).getFechaHora()));

                                        act.setFechaHoraUltimoEstado(fecha);
                                        act.setPersonaUltimoEstado(personaUsuario);

                                        expActuacionesController.setSelected(act);
                                        expActuacionesController.save(null);

                                        ut.commit();

                                        buscarActuaciones();
                                    } else {
                                        JsfUtil.addErrorMessage("No se encontro firma pendiente");
                                    }
                                } else {
                                    JsfUtil.addErrorMessage("No se encontraron intentos de firmas para esta actuacion");
                                }
                            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                                ex.printStackTrace();
                                return;
                            }
                        } else {
                            JsfUtil.addErrorMessage("Esta actuacion no tiene secretario asignado");
                        }
                    }
                }
            }
        }
    }

    private ExpEnviosCorte guardarEnvioCorte(ExpActuaciones act, String pedido, String urlCorte, Date fecha) {
        ExpEnviosCorte env = new ExpEnviosCorte();
        env.setActuacion(act);
        env.setDocumentoJudicial(act.getDocumentoJudicial());
        env.setFechaHoraEnvio(fecha);
        env.setPedido(pedido);
        env.setCodRespuesta(null);
        env.setPersonaEnvio(personaUsuario);
        env.setRespuesta(null);
        env.setUrl(urlCorte);

        enviosCorteController.setSelected(env);
        enviosCorteController.saveNew(null);
        return enviosCorteController.getSelected();
    }

    private void actualizarEnvioCorte(ExpEnviosCorte env, Integer codRespuesta, String respuesta, Integer codActuacionCaso) {
        env.setCodRespuesta(String.valueOf(codRespuesta));
        env.setRespuesta(respuesta);
        env.setCodActuacionCaso(codActuacionCaso);
        enviosCorteController.setSelected(env);
        enviosCorteController.save(null);
    }

    private ExpActuaciones guardarAcuseEnvioCorte(Date fecha, ExpActuaciones parAct, ExpFormatos formato, Personas destinatario) {

        ExpActuaciones act = new ExpActuaciones();

        act.setFechaPresentacion(generarFechaPresentacion(fecha));

        ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_ACUSE_RECIBO_OFICIO_CORTE).getSingleResult();
        act.setTipoActuacion(tipoActuacion);

        act.setDocumentoJudicial(parAct.getDocumentoJudicial());

        ExpEstadosActuacion estAct = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getSingleResult();
        act.setEstado(estAct);
        act.setActuacionPadre(null);

        act.setArchivo(null);
        act.setAutenticado(false);
        act.setDescripcion("Acuse de Recibo " + parAct.getTipoActuacion().getDescripcion().toLowerCase());
        act.setFechaHoraAutenticado(null);
        act.setFechaHoraVisible(fecha);
        act.setPersonaVisible(personaUsuario);
        act.setVisible(true);
        act.setFojas(1);
        act.setFormato(formato);
        act.setDestinatario(destinatario);

        List<ExpObjetosActuacion> listaObj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_OFICIO_CORTE).getResultList();
        ExpObjetosActuacion obj = null;
        if (!listaObj.isEmpty()) {
            obj = listaObj.get(0);
        }

        act.setObjetoActuacion(obj);

        act.setLeido(false);
        String hash = "";
        try {
            hash = Utils.generarHash(fecha, destinatario.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        act.setHash(hash);
        act.setNotificar(false);

        act.setFechaHoraAlta(fecha);
        act.setFechaHoraUltimoEstado(fecha);
        act.setPersonaAlta(personaUsuario);
        act.setPersonaUltimoEstado(personaUsuario);
        act.setEmpresa(personaUsuario.getEmpresa());

        expActuacionesController.setSelected(act);
        expActuacionesController.saveNew(null);

        return expActuacionesController.getSelected();

    }

    private void generarAcusaReciboEnvioCorte(ExpActuaciones act, Integer codActuacionCaso) throws FileNotFoundException, IOException, DocumentException {
        List<Metadatos> listaMetadatos = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        SimpleDateFormat format2 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'a las' HH:mm:ss", new Locale("es", "ES"));
        Date fecha = ejbFacade.getSystemDate();

        Metadatos meta = new Metadatos("fecha", format.format(act.getFechaFinal()));
        listaMetadatos.add(meta);

        /*
        String sec = act.getNroFinal();

        Metadatos meta2 = null;
        if (sec != null) {
            meta2 = new Metadatos("nro", sec);
        } else {
            meta2 = new Metadatos("nro", "");
        }
        listaMetadatos.add(meta2);
        Metadatos meta4 = new Metadatos("causa", act.getDocumentoJudicial().getCausa().replace("-", "/"));
        listaMetadatos.add(meta4);
        Metadatos meta5 = new Metadatos("caratula", act.getDocumentoJudicial().getCaratula());
        listaMetadatos.add(meta5);
        Metadatos meta6 = new Metadatos("url", url);
        listaMetadatos.add(meta6);
         */
        Metadatos meta7 = new Metadatos("nrorecepcion", String.valueOf(codActuacionCaso));
        listaMetadatos.add(meta7);
        /*
        Metadatos meta8 = new Metadatos("fechaActual", format.format(fecha));
        listaMetadatos.add(meta8);
        Metadatos meta9 = new Metadatos("tipoActuacion", act.getTipoActuacion().getDescripcion().toLowerCase());
        listaMetadatos.add(meta9);
        Metadatos meta10 = new Metadatos("fechaHoraActual", format2.format(fecha));
        listaMetadatos.add(meta10);
        Metadatos meta3 = new Metadatos("cuerpo", sustituirTags((act.getTexto() == null) ? "" : act.getTexto(), listaMetadatos));
        listaMetadatos.add(meta3);
        Metadatos meta11 = null;
        listaMetadatos.add(meta11);
         */

        List<ExpFormatos> lista = ejbFacade.getEntityManager().createNamedQuery("ExpFormatos.findByIdTipoActuacion", ExpFormatos.class).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_ACUSE_RECIBO_OFICIO_CORTE).getResultList();

        if (lista.isEmpty()) {
            return;
        }

        ExpActuaciones actNoti = guardarAcuseEnvioCorte(fecha, act, lista.get(0), destinatario);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String nombreArch = simpleDateFormat.format(fecha);
        nombreArch += "_" + actNoti.getDocumentoJudicial().getId() + "_" + actNoti.getId() + ".pdf";

        String stringFinal = ((lista.get(0).getTexto() == null) ? "" : lista.get(0).getTexto());

        stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + stringFinal + "</body></html>";

        // try ( OutputStream outputStream = new FileOutputStream(par.getRutaArchivos() + "/" + nombreArch + "_temp")) {
        try ( OutputStream outputStream = new FileOutputStream(par.getRutaArchivos() + "/" + nombreArch)) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();

            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);

            // FontResolver resolver = renderer.getFontResolver();
            renderer.getFontResolver().addFont(par.getRutaArchivos() + "/garamond/GaramondRegular.ttf", true);

            renderer.setDocumentFromString(sustituirTags(stringFinal, listaMetadatos));
            renderer.layout();
            renderer.createPDF(outputStream);

        }
        /*
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(par.getRutaArchivos() + "/" + nombreArch + "_temp"), new PdfWriter(par.getRutaArchivos() + "/" + nombreArch));
        // Document doc = new Document(pdfDoc);

        pdfDoc.setTagged();
        // Document doc = new Document(pdfDoc, PageSize.A4);
        Document doc = new Document(pdfDoc);

        // doc.setMargins(200, 200, 200, 200);
        // java.awt.Image imagen = new java.awt.Image
        File pathToFile = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg");
        Image image = ImageIO.read(pathToFile);

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            if ((i % 2) > 0) {
                PdfPage pagina = pdfDoc.getPage(i);
                Rectangle pageSize = pagina.getPageSize();
                float x = (pageSize.getWidth() / 2) - 75;
                float y = pageSize.getTop() - 80;
                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);
                Rectangle rect = new Rectangle(x, y, 150, 60);
                under.addImageFittedIntoRectangle(ImageDataFactory.create(image, Color.white), rect, false);
                com.itextpdf.kernel.colors.Color magentaColor = new DeviceCmyk(1.f, 1.f, 1.f, 0.f);
                under.setStrokeColor(magentaColor).moveTo(88, 856).lineTo(550, 856).closePathStroke();

                under.saveState();
                // under2.saveState();
            }
        }

        doc.close();

        File archivo = new File(par.getRutaArchivos() + "/" + nombreArch + "_temp");
        archivo.delete();
         */
        actNoti.setArchivo(nombreArch);
        expActuacionesController.setSelected(actNoti);
        expActuacionesController.save(null);

    }

    /*
    public void enviarCorte(ExpActuaciones item) {
        try {

            if (item.getArchivo() != null) {
                List<ExpConexiones> ws = ejbFacade.getEntityManager().createNamedQuery("ExpConexiones.findById", ExpConexiones.class).setParameter("id", Constantes.CONEXION_CORTE_ENVIAR_CORTE_ID).getResultList();

                if (ws.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encuentran parametros para conexion con la Corte");
                    return;
                }

                Date fecha = ejbFacade.getSystemDate();

                listaRespuestaConsultaCausas = new ArrayList<>();

                CloseableHttpClient CLIENT = Utils.createAcceptSelfSignedCertificateClient();
                String urlCorte = ws.get(0).getIpServidor() + ":" + ws.get(0).getPuertoServidor() + ws.get(0).getUri();
                HttpPost request = new HttpPost(urlCorte);

                byte[] fileByte = null;

                if (item.getNroFinal() != null) {
                    String[] array = item.getNroFinal().split("/");

                    int numero = Integer.valueOf(array[0]);

                    JSONObject json = new JSONObject();
                    json.put("codDespachoJudicial", item.getDocumentoJudicial().getExpedienteCorte().getCodDespachoJudicial());
                    json.put("numero", numero);
                    json.put("cantidadHojas", item.getFojas());
                    json.put("codCasoJudicial", item.getDocumentoJudicial().getExpedienteCorte().getCodCasoJudicial());
                    json.put("usuario", ws.get(0).getUsuario());
                    json.put("documento", fileByte);
                    json.put("usuarioResponsable", personaUsuario.getNombresApellidos().substring(0, personaUsuario.getNombresApellidos().length() > 80 ? 80 : personaUsuario.getNombresApellidos().length()));

                    fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + item.getArchivo()).toPath());

                    ExpEnviosCorte env = guardarEnvioCorte(item, json.toString(), urlCorte, fecha);
                    json.put("documento", fileByte);

                    try {
                        ByteArrayEntity params = new ByteArrayEntity(Utils.encryptMsg(json.toString(), Utils.generateKey()));
                        request.addHeader("content-type", "application/octet-stream;charset=UTF-8");
                        request.addHeader("charset", "UTF-8");
                        request.setEntity(params);

                        HttpResponse response = (HttpResponse) CLIENT.execute(request);
                        if (response != null) {
                            HttpEntity entity = response.getEntity();

                            byte[] bytes = IOUtils.toByteArray(entity.getContent());

                            String respuesta = Utils.decryptMsg(bytes, Utils.generateKey());

                            if (respuesta != null) {
                                JSONObject obj = null;
                                obj = new JSONObject(respuesta);

                                if (obj.getInt("codigo") == 0) {

                                    JSONObject rs = obj.getJSONObject("resp");

                                    if (rs != null) {
                                        RespuestaEnviarCorte resp = new RespuestaEnviarCorte(rs.getInt("codActuacionCaso"));

                                        item.setCodActuacionCaso(rs.getInt("codActuacionCaso"));
                                        item.setPersonaEnvioCorte(personaUsuario);
                                        item.setFechaHoraEnvioCorte(fecha);
                                        expActuacionesController.setSelected(item);
                                        expActuacionesController.save(null);
                                        expActuacionesController.setSelected(null);

                                        actualizarEnvioCorte(env, obj.getInt("codigo"), rs.toString(), rs.getInt("codActuacionCaso"));

                                        generarAcusaReciboEnvioCorte(item, rs.getInt("codActuacionCaso"));

                                        buscarActuaciones();
                                    }

                                } else {
                                    JsfUtil.addErrorMessage("Problemas al obtener respuesta");
                                    return;
                                }
                            } else {
                                JsfUtil.addErrorMessage("No se pudo obtener respuesta");
                                return;
                            }

                        } else {
                            JsfUtil.addErrorMessage("No se pudo obtener respuesta.");
                            return;
                        }

                    } catch (InvalidKeySpecException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (NoSuchPaddingException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (InvalidKeyException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (InvalidParameterSpecException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (IllegalBlockSizeException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (BadPaddingException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (InvalidAlgorithmParameterException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (DocumentException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    }
                } else {
                    JsfUtil.addErrorMessage("La actuacion no tiene numero");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("La actuacion no tiene un archivo pdf asociado");
                return;
            }
            //mensaje = decryptMsg(pedido, generateKey());
            
            //    HttpEntity entity = response.getEntity();
            //    ObjectMapper mapper = new ObjectMapper();
            //    resp = mapper.readValue((EntityUtils.toString(entity)), RespuestaPersonas.class);
             
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
            ex.printStackTrace();
        }
    }
     */
    public void enviarCorte(ExpActuaciones item) {

        try {

            if (item.getArchivo() != null) {
                List<ExpConexiones> ws = ejbFacade.getEntityManager().createNamedQuery("ExpConexiones.findById", ExpConexiones.class
                ).setParameter("id", Constantes.CONEXION_CORTE_ENVIAR_CORTE_ID).getResultList();

                if (ws.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encuentran parametros para conexion con la Corte");
                    return;
                }

                Date fecha = ejbFacade.getSystemDate();

                listaRespuestaConsultaCausas = new ArrayList<>();

                CloseableHttpClient CLIENT = Utils.createAcceptSelfSignedCertificateClient();
                String urlCorte = ws.get(0).getIpServidor() + ":" + ws.get(0).getPuertoServidor() + ws.get(0).getUri();
                HttpPost request = new HttpPost(urlCorte);

                byte[] fileByte = null;

                if (item.getNroFinal() != null) {
                    String[] array = item.getNroFinal().split("/");

                    int numero = Integer.valueOf(array[0]);

                    JSONObject json = new JSONObject();
                    json.put("codDespachoJudicial", item.getDocumentoJudicialCorte().getCodDespachoJudicial());
                    json.put("circunscripcion", item.getDocumentoJudicialCorte().getCodCircunscripcion());
                    json.put("numero", numero);
                    json.put("cantidadHojas", item.getFojas());
                    json.put("codCasoJudicial", item.getDocumentoJudicialCorte().getCodCasoJudicial());
                    json.put("codCasoJEM", item.getDocumentoJudicial().getId());
                    json.put("caratulaJEM", item.getDocumentoJudicial().getCaratula());
                    json.put("usuario", ws.get(0).getUsuario());
                    json.put("documento", fileByte);
                    json.put("usuarioResponsable", personaUsuario.getNombresApellidos().substring(0, personaUsuario.getNombresApellidos().length() > 80 ? 80 : personaUsuario.getNombresApellidos().length()));

                    fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + item.getArchivo()).toPath());

                    ExpEnviosCorte env = guardarEnvioCorte(item, json.toString(), urlCorte, fecha);
                    json.put("documento", fileByte);

                    try {
                        ByteArrayEntity params = new ByteArrayEntity(Utils.encryptMsg(json.toString(), Utils.generateKey()));
                        request.addHeader("content-type", "application/octet-stream;charset=UTF-8");
                        request.addHeader("charset", "UTF-8");
                        request.setEntity(params);

                        HttpResponse response = (HttpResponse) CLIENT.execute(request);
                        if (response != null) {
                            HttpEntity entity = response.getEntity();

                            byte[] bytes = IOUtils.toByteArray(entity.getContent());

                            String respuesta = Utils.decryptMsg(bytes, Utils.generateKey());

                            if (respuesta != null) {
                                JSONObject obj = null;
                                obj = new JSONObject(respuesta);

                                if (obj.getInt("codigo") == 0) {

                                    JSONObject rs = obj.getJSONObject("resp");

                                    if (rs != null) {
                                        RespuestaEnviarCorte resp = new RespuestaEnviarCorte(rs.getInt("codActuacionCaso"));

                                        item.setCodActuacionCaso(rs.getInt("codActuacionCaso"));
                                        item.setPersonaEnvioCorte(personaUsuario);
                                        item.setFechaHoraEnvioCorte(fecha);
                                        item.setEnvioCorte(true);
                                        item.setProveidoCorte(false);
                                        expActuacionesController.setSelected(item);
                                        expActuacionesController.save(null);
                                        expActuacionesController.setSelected(null);

                                        actualizarEnvioCorte(env, obj.getInt("codigo"), rs.toString(), rs.getInt("codActuacionCaso"));

                                        generarAcusaReciboEnvioCorte(item, rs.getInt("codActuacionCaso"));

                                        buscarActuaciones();
                                    }

                                } else {
                                    JsfUtil.addErrorMessage("Problemas al obtener respuesta");
                                    return;
                                }
                            } else {
                                JsfUtil.addErrorMessage("No se pudo obtener respuesta");
                                return;
                            }

                        } else {
                            JsfUtil.addErrorMessage("No se pudo obtener respuesta.");
                            return;
                        }

                    } catch (InvalidKeySpecException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (NoSuchPaddingException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (InvalidKeyException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (InvalidParameterSpecException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (IllegalBlockSizeException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (BadPaddingException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (InvalidAlgorithmParameterException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    } catch (DocumentException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo enviar oficio");
                        return;
                    }
                } else {
                    JsfUtil.addErrorMessage("La actuacion no tiene numero");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("La actuacion no tiene un archivo pdf asociado");
                return;
            }
            //mensaje = decryptMsg(pedido, generateKey());

            //    HttpEntity entity = response.getEntity();
            //    ObjectMapper mapper = new ObjectMapper();
            //    resp = mapper.readValue((EntityUtils.toString(entity)), RespuestaPersonas.class);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
            ex.printStackTrace();
        }

    }

    public void actualizarTexto() {
        if (actuacion != null) {
            if (actuacion.getFormato() != null) {
                actuacion.setTexto(actuacion.getFormato().getTexto());
            }
        }
    }

    public void actualizarDatosSecretario() {
        listaEstadosActuacion = obtenerEstadosActuacion(estadoActuacion);
    }

    public void buscarFormatosCreate() {
        docId = null;
        listaFormatos = null;
        if (actuacion != null) {
            actuacion.setTexto("");
            if (actuacion.getTipoActuacion() != null) {
                if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(actuacion.getTipoActuacion().getId())) {
                    if (listaDocumentosJudicialesCorte != null) {
                        if (listaDocumentosJudicialesCorte.isEmpty()) {
                            actuacion.setTipoActuacion(null);
                            JsfUtil.addErrorMessage("No tiene expedientes corte asociados");
                            return;
                        }
                    }

                } else if (Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(actuacion.getTipoActuacion().getId())) {
                    listaActuacionesDesglose = obtenerActuacionesDesglose();
                }

                listaEstadosActuacion = obtenerEstadosActuacion(estadoActuacion);

                buscarFormatos();
            }
        }
        docId = null;
    }

    public void buscarFormatos() {
        prepareCerrarDialogoPrevisualizar();

        if (actuacion != null) {
            if (actuacion.getTipoActuacion() != null) {
                listaFormatos = ejbFacade.getEntityManager().createNamedQuery("ExpFormatos.findByTipoActuacion", ExpFormatos.class
                ).setParameter("tipoActuacion", actuacion.getTipoActuacion()).getResultList();

                if (Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId())) {
                    if (resolucion != null) {
                        TiposResolucion tipo = ejbFacade.getEntityManager().createNamedQuery("TiposResolucion.findById", TiposResolucion.class
                        ).setParameter("id", Constantes.TIPO_RESOLUCION_SD).getSingleResult();
                        resolucion.setTipoResolucion(tipo);
                        actualizarResuelve();
                        //actuacion.setPreopinante(null);

                    }
                } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId())) {
                    if (resolucion != null) {
                        TiposResolucion tipo = ejbFacade.getEntityManager().createNamedQuery("TiposResolucion.findById", TiposResolucion.class
                        ).setParameter("id", Constantes.TIPO_RESOLUCION_AI).getSingleResult();
                        resolucion.setTipoResolucion(tipo);
                        actualizarResuelve();
                        //actuacion.setPreopinante(null);
                    }
                } else if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(actuacion.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_OFICIO.equals(actuacion.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(actuacion.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(actuacion.getTipoActuacion().getId())) {
                    actualizarPreopinante();
                    if (resolucion != null) {
                        resolucion.setTipoResolucion(null);
                        resolucion.setSubtipoResolucion(null);
                        resolucion.setFecha(null);
                    }
                }
                return;
            }
        }

        listaFormatos = null;
    }

    public void actualizarPreopinante() {
        if (actuacion != null) {
            if (actuacion.getPreopinante() == null) {
                List<Personas> lista3 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class
                ).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

                if (!lista3.isEmpty()) {

                    List<ExpPersonasInhibidasPorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasInhibidasPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPersonasInhibidasPorDocumentosJudiciales.class
                    ).setParameter("documentoJudicial", getSelected()).setParameter("estado", new Estados("AC")).getResultList();

                    boolean encontro = false;
                    for (ExpPersonasInhibidasPorDocumentosJudiciales inh : lista) {
                        if (lista3.get(0).equals(inh)) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        actuacion.setPreopinante(lista3.get(0));
                    }
                }
            }
        }
    }

    private boolean esPersonaAsociada(Personas persona, Personas personaAsociada) {

        boolean encontro = false;
        if (persona != null && personaAsociada != null) {

            if (persona.equals(personaAsociada)) {
                return true;

            }

            List<ExpPersonasAsociadas> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersona", ExpPersonasAsociadas.class
            ).setParameter("persona", persona.getId()).getResultList();
            for (ExpPersonasAsociadas per : lista) {
                if (per.getPersonaAsociada().equals(personaAsociada)) {
                    encontro = true;
                    break;
                }
            }
        }
        return encontro;
    }

    public boolean deshabilitarBotonVerEnVentana() {
        return deshabilitarBotonVerEnVentana(docImprimir);
    }

    public boolean deshabilitarBotonVerEnVentana(ExpActuaciones act) {
        // return false;
        return !(renderedFirma(act) || !deshabilitarAutenticar() || esApp);
    }

    public boolean renderedFirmantes() {
        return renderedFirmantes(null);
    }

    public boolean renderedFirmantes(ExpActuaciones act) {
        if (act == null) {
            return filtroURL.verifPermiso(Constantes.PERMISO_FIRMANTES, rolElegido.getId());
        } else {
            return filtroURL.verifPermiso(Constantes.PERMISO_FIRMANTES, rolElegido.getId()) && act.getFormato() == null;
        }
    }

    /*
    public boolean renderedFirmantes(){
        
    }
     */
    public boolean deshabilitarFirmantes(ExpActuaciones act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_FIRMANTES, rolElegido.getId())) {
            if (act == null) {
                return false;
                /*  } else if (act.getTipoActuacion() != null) {
                return Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId());
                 */
            } else {
                return !(act.getFormato() == null && !(act.getEstado() == null ? true : act.getEstado().getCodigo().equals(Constantes.ESTADO_ACTUACION_FINALIZADA)));
            }
        }
        return true;
    }

    public boolean deshabilitarFirmantes() {
        return deshabilitarFirmantes(null);
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

    public boolean deshabilitarInhibiciones() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_INHIBICIONES, rolElegido.getId())) {
                return false;
            }
        }
        return true;
    }

    public boolean deshabilitarRestringidos() {
        if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_INHIBICIONES, rolElegido.getId())) {
                return false;
            }
        }
        return true;
    }

    public boolean deshabilitarListaPartesAlta() {
        return tipoParte == null;
    }

    public boolean deshabilitarListaInhibidosAlta() {
        return false;
    }

    public boolean deshabilitarListaRestringidosAlta() {
        return false;
    }

    public boolean deshabilitarDestinatario() {
        if (categoriaActuacion != null) {
            if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
                return deshabilitarCamposEdicionActuacion();
            }
        }
        return true;
    }

    public boolean deshabilitarObjetoActuacion() {
        return !esPrimerEscrito || !(Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion) || Constantes.ACCION_REGISTRAR_RECURSO.equals(accion) || Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion));
    }

    public boolean deshabilitarTipoActuacion() {
        // return esPrimerEscrito && Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion);
        // return Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion) || Constantes.ACCION_CREAR.equals(accion);
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
        return !filtroURL.verifPermiso(Constantes.PERMISO_OBSERVACION, rolElegido.getId());
    }

    public boolean renderedTipoActuacion() {
        // return esPrimerEscrito && Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion);
        // return !Constantes.ACCION_REGISTRAR_PROVIDENCIA.equals(accion);
        return false;
    }

    public boolean renderedInhibiciones() {
        return !deshabilitarInhibiciones();
    }

    public boolean renderedEstadoProcesoEE() {
        return filtroURL.verifPermiso(Constantes.PERMISO_VER_ESTADO_PROCESO, rolElegido.getId()) || !deshabilitarEstadoProcesoEE();
    }

    public boolean deshabilitarEstadoProcesoEE() {
        return !filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_ESTADO_PROCESO, rolElegido.getId());
    }

    private boolean verificarFirmantes(ExpActuaciones act, Personas firmante, AntecedentesRoles rol) {

        ExpPersonasFirmantesPorActuaciones firm = null;
        ExpRolesFirmantesPorActuaciones firm2 = null;

        try {
            firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class
            ).setParameter("actuacion", act).setParameter("personaFirmante", firmante).setParameter("estado", new Estados("AC")).getSingleResult();
            return firm.isFirmado();

        } catch (Exception e) {
            try {
                firm2 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacionRolFirmanteEstado", ExpRolesFirmantesPorActuaciones.class
                ).setParameter("actuacion", act).setParameter("rolFirmante", rol).setParameter("estado", new Estados("AC")).getSingleResult();
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

                List<Integer> roles = new ArrayList<>();
                roles.add(Constantes.ROL_SECRETARIO);
                roles.add(Constantes.ROL_EXSECRETARIO);
                List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", roles).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

                if (!lista.isEmpty()) {
                    firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getSingleResult();
                    boolean esSecretario = false;
                    for (Personas per : lista) {
                        if (per.equals(personaUsuario)) {
                            esSecretario = true;
                            break;
                        }
                    }

                    return (act.getFormato() == null) ? true : (firm.isRevisado() || esSecretario);

                }
            } catch (Exception e) {
                // e.printStackTrace();;
                try {
                    firm2 = ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByActuacionRolFirmanteEstado", ExpRolesFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("rolFirmante", rolElegido).setParameter("estado", new Estados("AC")).getSingleResult();
                    return true;
                } catch (Exception ex) {
                    // ex.printStackTrace();
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

    public Long cantDiasEnjuiciamiento(DocumentosJudiciales doc) {
        Date now = ejbFacade.getSystemDateOnly(0);
        return Utils.cantDiasEnjuiciamiento(doc, now, ejbFacade.getEntityManager());
    }

    public String rowClassEstadoProceso(DocumentosJudiciales doc) {
        /*
        if (doc != null) {
            if (doc.getFechaInicioEnjuiciamiento() != null) {
                if (doc.getEstadoProcesoExpedienteElectronico().isEnProceso()) {
                    Date now = ejbFacade.getSystemDateOnly(0);
                    Long dias = Utils.cantDiasEnjuiciamiento(doc, now, ejbFacade.getEntityManager());
                    return dias > 120 ? "red" : dias > 60 ? "yellow" : "green";
                }
            }
        }
         */
        return "";
    }

    public String rowClass(ExpActuaciones act) {

        List<AntecedentesRolesPorPersonas> perSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();

        boolean encontro = false;
        if (!perSecretario.isEmpty()) {
            if (perSecretario.get(0).getPersonas().equals(personaUsuario)) {
                encontro = true;

            }
        }

        if (!encontro) {
            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();

            if (!lista.isEmpty()) {
                return lista.get(0).isFirmado() ? "" : lista.get(0).isRevisado() ? "green" : "red";

            }

            List<ExpPersonasAsociadas> miembro = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();

            for (ExpPersonasAsociadas per : miembro) {
                List<ExpPersonasFirmantesPorActuaciones> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", per.getPersona()).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();

                if (!lista2.isEmpty()) {
                    return lista2.get(0).isFirmado() ? "" : lista2.get(0).isRevisado() ? "green" : "red";
                }
            }
        }

        return "";

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
        if (rolElegido == null) {
            session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");
        }

        if (docImprimir != null) {
            if (filtroURL.verifPermiso(Constantes.PERMISO_AUTENTICAR, rolElegido.getId())) {
                return docImprimir.isVisible() && (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_OFICIO.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(docImprimir.getTipoActuacion().getId()));
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
        if (filtroURL.verifPermiso(Constantes.PERMISO_BOTON_REGISTRAR_ACTUACION_POR_SECRETARIA, rolElegido.getId())
                && (Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(accion)
                || Constantes.ACCION_REGISTRAR_RECURSO.equals(accion)
                || Constantes.ACCION_REGISTRAR_INCIDENTE.equals(accion)
                || Constantes.ACCION_REGISTRAR_NOTIFICACION.equals(accion)
                || Constantes.ACCION_REGISTRAR_OFICIO.equals(accion)
                || Constantes.ACCION_REGISTRAR_PROVIDENCIA.equals(accion))) {
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

    public void prepareVerifRevisado(ExpPersonasFirmantesPorActuaciones per) {
        selectedPersonaFirmantePorActuacion = per;
        mensajeAviso = per.getActuacion().getDescripcion();
    }

    public void actualizarActuacion(ExpActuaciones act) {
        if (act != null && getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            Date fechaHora = generarFechaPresentacion(fecha);

            act.setFechaPresentacion(fechaHora);
            act.setFechaHoraVisible(fecha);
            act.setPersonaVisible(personaUsuario);
            act.setVisible(true);

            expActuacionesController.setSelected(act);

            expActuacionesController.save(null);

            //EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();
            EstadosProcesalesDocumentosJudiciales estadoProc = new EstadosProcesalesDocumentosJudiciales();

            estadoProc.setPersonaAlta(personaUsuario);
            estadoProc.setPersonaUltimoEstado(personaUsuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())) {
                estadoProc.setEstadoProcesal("Resolución " + (act.getResolucion().getNroResolucion() != null ? ("Nro " + act.getResolucion().getNroResolucion()) : "") + " publicada");
            } else {
                estadoProc.setEstadoProcesal(act.getDescripcion());
            }
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

                enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto, getSelected(), act, fechaHora, personaUsuario);

                if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getCategoriaActuacion().getId()) /*
                        || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId())*/) {

                    try {
                        ExpNotificaciones not = ejbFacade.getEntityManager().createNamedQuery("ExpNotificaciones.findByActuacion", ExpNotificaciones.class).setParameter("actuacion", act).getSingleResult();

                        not.setVisible(true);
                        not.setPersonaVisible(personaUsuario);
                        not.setFechaHoraVisible(fecha);
                        expNotificacionController.setSelected(not);
                        expNotificacionController.save(null);

                        String texto2 = "<p>Hola " + not.getDestinatario().getNombresApellidos() + "<br> "
                                + "     Se ha presentado una presentación del tipo " + act.getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                                + getSelected().getCaratulaString()
                                + "<br><br>"
                                + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                                + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                        enviarEmailAviso(not.getDestinatario().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto2, getSelected(), act, fechaHora, personaUsuario);
                    } catch (Exception e) {

                    }

                }

            }
        }
    }

    public void versionImpresion(ExpActuaciones act) {
        HttpServletResponse httpServletResponse = null;
        String nombreArch = null;
        try {

            if (act.getArchivo() != null) {
                if (act.getHash() != null) {
                    byte[] fileByte = null;
                    try {

                        nombreArch = regenerarQR(act);

                        fileByte = Files.readAllBytes(new File(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArch).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        return;
                    }

                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.setContentType("application/pdf");
                    // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                    httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                    ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                    servletOutputStream.write(fileByte);
                    FacesContext.getCurrentInstance().responseComplete();
                } else {
                    JsfUtil.addErrorMessage("Actuación no tiene hash");
                    return;
                }
            }

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
        } finally {
            if (nombreArch != null) {
                if (!"".equals(nombreArch)) {
                    File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArch);
                    f.delete();

                    docImprimir = null;
                }
            }
        }
    }

    public String regenerarQR(ExpActuaciones act) {

        Date fecha = ejbFacade.getSystemDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);

        String nombreArchivo = session.getId() + "_" + simpleDateFormat.format(fecha) + ".pdf";

        /*
        try (
          InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivos() + File.separator + act.getArchivo()));
          OutputStream out = new BufferedOutputStream(new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArchivo))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
         */
        agregarQR(par.getRutaArchivos() + File.separator + act.getArchivo(), Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArchivo, act.getHash(), false);
        /*
            act.setArchivo(nombreArchivo);
            act.setRegeneradoqr(true);
            act.setFechaHoraRegeneradoqr(fecha);
            act.setPersonaRegeneradoqr(personaSelected);

            expActuacionesController.setSelected(act);
            expActuacionesController.save(null);
         */
 /*
        }catch(Exception e){
            e.printStackTrace();
        }
         */

        return nombreArchivo;
    }

    /*
    public void regenerarQR(ExpActuaciones act){
        
        Date fecha = ejbFacade.getSystemDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String nombreArchivo = simpleDateFormat.format(fecha);
        nombreArchivo += "_" + act.getDocumentoJudicial().getId() + "_" + act.getId() + ".pdf";
        
        String hash = "";
        try {
            hash = Utils.generarHash(fecha, personaUsuario.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
            return;
        }
        
        try (
          InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivos() + File.separator + act.getArchivo()));
          OutputStream out = new BufferedOutputStream(new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp"))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
                
        agregarQR(par.getRutaArchivos() + File.separator + nombreArchivo, hash, false);
        
        act.setArchivo(nombreArchivo);
        act.setRegeneradoqr(true);
        act.setFechaHoraRegeneradoqr(fecha);
        act.setPersonaRegeneradoqr(personaSelected);
        
        expActuacionesController.setSelected(act);
        expActuacionesController.save(null);
    }
    
     */
    public void autenticar() {

        if (docImprimir != null) {
            boolean encontro = false;
            if (docImprimir.getFormato() != null) {
                JsfUtil.addErrorMessage("Este documento no puede ser autenticado");
                return;
            }
        }

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

            agregarQR(par.getRutaArchivos() + File.separator + act.getArchivo() + "_temp", par.getRutaArchivos() + File.separator + act.getArchivo(), hash, primeraPagina);
            File archivo = new File(par.getRutaArchivos() + File.separator + act.getArchivo() + "_temp");
            archivo.delete();

            act.setHash(hash);

            act.setAutenticado(true);
            act.setFechaHoraAutenticado(fecha);
            act.setPersonaAutenticado(personaUsuario);

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
                act.setPersonaFirmante(personaUsuario);

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

            Date fecha = ejbFacade.getSystemDate();

            String texto = "<p>Hola " + ((act == null) ? act2.getPersonaAlta().getNombresApellidos() : act.getPersonaAlta().getNombresApellidos()) + "<br> "
                    + "     Una presentacion ha sido firmada por " + ((act == null) ? act2.getPersonaFirmante().getNombresApellidos() : act.getPersonaFirmante().getNombresApellidos()) + " en la causa nro " + docImprimir.getDocumentoJudicial().getCausa() + " caratulada: <br><br>"
                    + docImprimir.getDocumentoJudicial().getCaratulaString()
                    + "<br><br>"
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + docImprimir.getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(((act == null) ? act2.getPersonaAlta().getEmail() : act.getPersonaAlta().getEmail()), "Se ha firmado una presentacion en el Sistema de Expediente Electrónico JEM", texto, firma.getActuacion().getDocumentoJudicial(), firma.getActuacion(), fecha, personaUsuario);

            List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
            for (Personas per : lista) {
                String texto2 = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Una presentacion ha sido firmada por " + ((act == null) ? act2.getPersonaFirmante().getNombresApellidos() : act.getPersonaFirmante().getNombresApellidos()) + " en la causa nro " + docImprimir.getDocumentoJudicial().getCausa() + " caratulada: <br><br>"
                        + docImprimir.getDocumentoJudicial().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + docImprimir.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Se ha firmado una presentacion en el Sistema de Expediente Electrónico JEM", texto, firma.getActuacion().getDocumentoJudicial(), firma.getActuacion(), fecha, personaUsuario);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void volverAtrasFirma(ExpActuaciones act) {
        if (act.getFormato() != null) {
            act.setArchivo(null);
            expActuacionesController.setSelected(act);
            expActuacionesController.save(null);
        }
    }

    public void incrementarNroSecuenciaActuacion(ExpTiposActuacion tipo) {
        List<ExpTiposActuacion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class
        ).setParameter("id", tipo.getId()).getResultList();
        if (!lista.isEmpty()) {

            String nroSecuencia = lista.get(0).getSecuencia();

            String[] array = nroSecuencia.split("/");

            if (array.length > 0) {
                DateFormat format = new SimpleDateFormat("yyyy");

                Date fecha = ejbFacade.getSystemDate();

                String anoActual = format.format(fecha);

                if (anoActual.equals(array[1])) {
                    Integer nro = Integer.valueOf(array[0]);
                    nro++;
                    nroSecuencia = nro + "/" + array[1];
                } else {
                    nroSecuencia = "1/" + anoActual;
                }

                lista.get(0).setSecuencia(nroSecuencia);
                tipoActuacionController.setSelected(lista.get(0));
                tipoActuacionController.save(null);
            }

        }
    }

    /*
    private void enviarEmails(ExpActuaciones act) {

        String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                + "     Se ha agregado una nueva presentación del tipo " + act.getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                + getSelected().getCaratulaString()
                + "<br><br>"
                + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

        enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto, act.getDocumentoJudicial(), act);

        if (!getSelected().getPersonaAlta().equals(personaUsuario)) {
            texto = "<p>Hola " + personaUsuario.getNombresApellidos() + "<br> "
                    + "     Ud ha agregado una nueva presentación del tipo " + act.getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                    + getSelected().getCaratulaString()
                    + "<br><br>"
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(personaUsuario.getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto, act.getDocumentoJudicial(), act);

        }

        List<ExpPartesPorDocumentosJudiciales> partes = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicial", ExpPartesPorDocumentosJudiciales.class
        ).setParameter("documentoJudicial", getSelected()).getResultList();
        for (ExpPartesPorDocumentosJudiciales per : partes) {
            if (!per.getPersona().equals(getSelected().getPersonaAlta()) && per.getPersona().equals(personaUsuario)) {
                texto = "<p>Hola " + per.getPersona().getNombresApellidos() + "<br> "
                        + "     Ud ha agregado una nueva presentación del tipo " + act.getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getPersona().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto, act.getDocumentoJudicial(), act);

            }
        }

        List<PersonasPorDocumentosJudiciales> partes2 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();
        for (PersonasPorDocumentosJudiciales per : partes2) {

            boolean encontro = false;
            //for()

            if (!per.getPersona().equals(getSelected().getPersonaAlta()) && !per.getPersona().equals(personaUsuario)) {
                texto = "<p>Hola " + per.getPersona().getNombresApellidos() + "<br> "
                        + "     Se ha agregado una nueva presentación del tipo " + act.getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getPersona().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto, act.getDocumentoJudicial(), act);

            }
        }
    }
     */

 /*
    private void guardarNotificacion(ExpActuaciones actuacion, Personas remitente, Personas destinatario, boolean visible) {
        ExpNotificaciones noti = expNotificacionController.prepareCreate(null);
        noti.setActuacion(actuacion);
        noti.setRemitente(remitente);
        noti.setDestinatario(destinatario);

        ExpEstadosNotificacion estado = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosNotificacion.findByCodigo", ExpEstadosNotificacion.class).setParameter("codigo", Constantes.ESTADO_NOTIFICACION_NO_LEIDO).getSingleResult();

        noti.setEstado(estado);

        noti.setFechaHoraAlta(actuacion.getFechaHoraAlta());
        noti.setDocumentoJudicial(actuacion.getDocumentoJudicial());
        noti.setFechaHoraLectura(null);
        noti.setVisible(visible);
        noti.setTexto(actuacion.getPersonaAlta().getNombresApellidos() + "\n\n\nUd ha recibido una notificacion de " + remitente.getNombresApellidos() + ". Favor verificar en el sistema");

        expNotificacionController.setSelected(noti);
        expNotificacionController.saveNew(null);

    }
     */
    private ExpActuaciones guardarNotificacion(Date fechaPresentacion, ExpActuaciones parAct, ExpFormatos formato, Personas destinatario) {
        Date fecha = ejbFacade.getSystemDate();

        ExpActuaciones act = new ExpActuaciones();

        act.setFechaPresentacion(fechaPresentacion);

        ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_NOTIFICACION).getSingleResult();
        act.setTipoActuacion(tipoActuacion);

        act.setDocumentoJudicial(parAct.getDocumentoJudicial());

        ExpEstadosActuacion estAct = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getSingleResult();
        act.setEstado(estAct);
        act.setActuacionPadre(null);

        act.setArchivo(null);
        act.setAutenticado(false);
        act.setDescripcion("Notificación de " + parAct.getTipoActuacion().getDescripcion().toLowerCase());
        act.setFechaHoraAutenticado(null);
        act.setFechaHoraVisible(fecha);
        act.setPersonaVisible(personaUsuario);
        act.setVisible(true);
        act.setFojas(1);
        act.setFormato(formato);
        act.setDestinatario(destinatario);

        List<ExpObjetosActuacion> listaObj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_NOTIFICACION).getResultList();
        ExpObjetosActuacion obj = null;
        if (!listaObj.isEmpty()) {
            obj = listaObj.get(0);
        }

        act.setObjetoActuacion(obj);

        act.setLeido(false);
        String hash = "";
        try {
            hash = Utils.generarHash(fecha, destinatario.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        act.setHash(hash);
        act.setNotificar(false);

        act.setFechaHoraAlta(fecha);
        act.setFechaHoraUltimoEstado(fecha);
        act.setPersonaAlta(personaUsuario);
        act.setPersonaUltimoEstado(personaUsuario);
        act.setEmpresa(personaUsuario.getEmpresa());

        expActuacionesController.setSelected(act);
        expActuacionesController.saveNew(null);

        return expActuacionesController.getSelected();

    }

    private void prepararNotificacion(ExpActuaciones act, Personas destinatario, Date fechaNofif, Date fechaPresentacion) throws FileNotFoundException, IOException, DocumentException {
        List<Metadatos> listaMetadatos = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        SimpleDateFormat format2 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'a las' HH:mm:ss", new Locale("es", "ES"));
        Date fecha = ejbFacade.getSystemDate();

        /*
        Date fechaActuacion = act.getFechaPresentacion();
        if (act.getResolucion() != null) {
            if (act.getResolucion().getFecha() != null) {
                fechaActuacion = act.getResolucion().getFecha();
            }
        } else {
            if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())) {
                List<Resoluciones> lista = ejbFacade.getEntityManager().createNamedQuery("Resoluciones.findById", Resoluciones.class).setParameter("id", act.getResolucion().getId()).getResultList();
                if (!lista.isEmpty()) {
                    if (lista.get(0).getFecha() != null) {
                        fechaActuacion = lista.get(0).getFecha();
                    }
                }
            }
        }
         */
        Metadatos meta = new Metadatos("fecha", format.format(act.getFechaFinal()));
        listaMetadatos.add(meta);
        String sec = act.getNroFinalString();

        Metadatos meta2 = null;
        if (sec != null) {
            meta2 = new Metadatos("nro", sec);
        } else {
            meta2 = new Metadatos("nro", "");
        }
        listaMetadatos.add(meta2);
        Metadatos meta4 = new Metadatos("causa", act.getDocumentoJudicial().getCausa().replace("-", "/"));
        listaMetadatos.add(meta4);
        Metadatos meta5 = new Metadatos("caratula", act.getDocumentoJudicial().getCaratula());
        listaMetadatos.add(meta5);
        Metadatos meta6 = new Metadatos("url", url);
        listaMetadatos.add(meta6);
        Metadatos meta7 = new Metadatos("parte", destinatario.getNombresApellidos());
        listaMetadatos.add(meta7);
        Metadatos meta8 = new Metadatos("fechaActual", format.format(fechaNofif));
        listaMetadatos.add(meta8);
        Metadatos meta9 = new Metadatos("tipoActuacion", act.getTipoActuacion().getDescripcion().toLowerCase());
        listaMetadatos.add(meta9);
        Metadatos meta10 = new Metadatos("fechaHoraActual", format2.format(fechaNofif));
        listaMetadatos.add(meta10);
        Metadatos meta3 = new Metadatos("cuerpo", sustituirTags((act.getTexto() == null) ? "" : act.getTexto(), listaMetadatos));
        listaMetadatos.add(meta3);
        Metadatos meta11 = null;
        if (!Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                && !Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(act.getTipoActuacion().getId())) {
            meta11 = new Metadatos("literalNro", "nro " + sec);
        } else {
            meta11 = new Metadatos("literalNro", "");
        }
        listaMetadatos.add(meta11);

        List<ExpFormatos> lista = ejbFacade.getEntityManager().createNamedQuery("ExpFormatos.findByIdTipoActuacion", ExpFormatos.class).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_NOTIFICACION).getResultList();

        if (lista.isEmpty()) {
            return;
        }

        ExpActuaciones actNoti = guardarNotificacion(fechaPresentacion, act, lista.get(0), destinatario);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String nombreArch = simpleDateFormat.format(fecha);
        nombreArch += "_" + actNoti.getDocumentoJudicial().getId() + "_" + actNoti.getId() + ".pdf";

        String stringFinal = ((lista.get(0).getTexto() == null) ? "" : lista.get(0).getTexto());

        stringFinal = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + (par.getFormatoActuaciones() == null ? "" : par.getFormatoActuaciones()) + "<body>" + stringFinal + "</body></html>";

        try ( OutputStream outputStream = new FileOutputStream(par.getRutaArchivos() + "/" + nombreArch + "_temp")) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();

            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);

            // FontResolver resolver = renderer.getFontResolver();
            renderer.getFontResolver().addFont(par.getRutaArchivos() + "/garamond/GaramondRegular.ttf", true);

            renderer.setDocumentFromString(sustituirTags(stringFinal, listaMetadatos));
            renderer.layout();
            renderer.createPDF(outputStream);

        }

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(par.getRutaArchivos() + "/" + nombreArch + "_temp"), new PdfWriter(par.getRutaArchivos() + "/" + nombreArch));
        // Document doc = new Document(pdfDoc);

        pdfDoc.setTagged();
        // Document doc = new Document(pdfDoc, PageSize.A4);
        Document doc = new Document(pdfDoc);

        // doc.setMargins(200, 200, 200, 200);
        // java.awt.Image imagen = new java.awt.Image
        File pathToFile = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg");
        Image image = ImageIO.read(pathToFile);

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            if ((i % 2) > 0) {
                PdfPage pagina = pdfDoc.getPage(i);
                Rectangle pageSize = pagina.getPageSize();
                float x = (pageSize.getWidth() / 2) - 75;
                float y = pageSize.getTop() - 80;
                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);
                Rectangle rect = new Rectangle(x, y, 150, 60);
                under.addImageFittedIntoRectangle(ImageDataFactory.create(image, Color.white), rect, false);
                com.itextpdf.kernel.colors.Color magentaColor = new DeviceCmyk(1.f, 1.f, 1.f, 0.f);
                under.setStrokeColor(magentaColor).moveTo(88, 856).lineTo(550, 856).closePathStroke();

                under.saveState();
                // under2.saveState();
            }
        }

        doc.close();

        File archivo = new File(par.getRutaArchivos() + "/" + nombreArch + "_temp");
        archivo.delete();

        actNoti.setArchivo(nombreArch);
        expActuacionesController.setSelected(actNoti);
        expActuacionesController.save(null);

        String texto = "<p>Hola " + destinatario.getNombresApellidos() + "<br> "
                + "     Una notificación dirigida a Ud a sigo agregada a la causa nro " + act.getDocumentoJudicial().getCausa() + " caratulada: <br><br>"
                + act.getDocumentoJudicial().getCaratulaString()
                + "<br><br>"
                + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

        enviarEmailAviso(destinatario.getEmail(), "Ud. ha recibido una notificación en el Sistema de Expediente Electrónico JEM", texto, actNoti.getDocumentoJudicial(), actNoti, fechaNofif, personaUsuario);

    }

    private void generarNotificaciones(ExpActuaciones act, Date fechaNotif, Date fechaPresentacion) {

        try {
            if (act.getFormato() != null) {

                List<ExpPartesPorDocumentosJudiciales> listaPar = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicial", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", act.getDocumentoJudicial().getId()).getResultList();

                for (ExpPartesPorDocumentosJudiciales part : listaPar) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(part.getEstado().getCodigo())) {
                        prepararNotificacion(act, part.getPersona(), fechaNotif, fechaPresentacion);

                    }
                }

                List<PersonasPorDocumentosJudiciales> listaPer = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", act.getDocumentoJudicial().getId()).getResultList();

                for (PersonasPorDocumentosJudiciales part : listaPer) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(part.getEstado().getCodigo())) {
                        prepararNotificacion(act, part.getPersona(), fechaNotif, fechaPresentacion);
                    }
                }
            } else {
                JsfUtil.addErrorMessage("Actuacion no tiene formato, no se puede notificar");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

    }

    private boolean verifActuacionAnterior(ExpActuaciones act) {
        if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())) {
            List<ExpTiposActuacion> array = new ArrayList<>();
            array.add(new ExpTiposActuacion(Constantes.TIPO_ACTUACION_OFICIO_CORTE));
            array.add(new ExpTiposActuacion(Constantes.TIPO_ACTUACION_OFICIO));
            array.add(new ExpTiposActuacion(Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE));
            array.add(new ExpTiposActuacion(Constantes.TIPO_ACTUACION_PROVIDENCIA));
            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByDocumentoJudicialPersonaFirmanteFirmadoTiposActuacion", ExpPersonasFirmantesPorActuaciones.class).setParameter("documentoJudicial", act.getDocumentoJudicial()).setParameter("personaFirmante", personaUsuario).setParameter("firmado", false).setParameter("tiposActuacion", array).getResultList();
            if (!lista.isEmpty()) {
                return lista.get(0).getActuacion().equals(act);
            }
            return false;
        } else {
            return true;
        }
    }

    public void notificarManualmente(ExpActuaciones act) {

        if (!deshabilitarNotificarManualmente(act)) {

            Date fecha = ejbFacade.getSystemDate();

            act.setNotificado(true);
            act.setFechaHoraNotificado(fecha);
            act.setPersonaNotificado(personaUsuario);

            expActuacionesController.setSelected(act);
            expActuacionesController.save(null);

            // generarNotificaciones(act, generarFechaPresentacion(fecha));
            generarNotificaciones(act, fecha, generarFechaPresentacion(fecha));

            buscarActuaciones();
        } else {
            JsfUtil.addErrorMessage("Actuacion no puede ser notificada");
        }
    }

    public String firmarReturn() {
        if (firmar()) {
            if (vieneDePestanas && filtroURL.verifPermiso("redireccionarPestanas")) {
                return "/pages/expActuaciones/index.xhtml?tipo=" + paramPestana + "faces-redirect=true";
            }
        }
        return "";
    }

    /*
    public boolean firmar() {

        Date fecha = ejbFacade.getSystemDate();
        Firmas firma = null;

        ExpActuaciones docImprimirAnterior = new ExpActuaciones();

        boolean resp = false;

        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        List<ExpActuaciones> listaAct = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", docImprimir.getId()).getResultList();
        if (listaAct.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro actuación a firmar");
            return resp;
        }

        docImprimir = listaAct.get(0);
        List<ExpPersonasFirmantesPorActuaciones> listaPerAnt = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getResultList();
        if (listaPerAnt.isEmpty()) {
            JsfUtil.addErrorMessage("Ud ya no es firmante de esta actuación");
            return resp;
        }

        if (listaPerAnt.get(0).isFirmado()) {
            JsfUtil.addErrorMessage("Ud ya firmó esta actuación");
            return resp;
        }

        if (!verifActuacionAnterior(docImprimir)) {
            JsfUtil.addErrorMessage("Ud tiene una actuación anterior que firmar en esta causa");
            return resp;

        }

        List<AntecedentesRolesPorPersonas> perSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();
        if (perSecretario.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro persona con rol SECRETARIO");
            return resp;
        }

        Personas secretario = null;

        boolean esExSecretario = false;
        if (docImprimir.getSecretario() == null) {
            secretario = perSecretario.get(0).getPersonas();
        } else {
            secretario = docImprimir.getSecretario();

            if (!secretario.equals(perSecretario.get(0).getPersonas())) {

                List<AntecedentesRolesPorPersonas> perExSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_EXSECRETARIO).getResultList();
                if (perSecretario.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encontro persona con rol EXSECRETARIO");
                    return resp;
                } else {
                    for (AntecedentesRolesPorPersonas per : perExSecretario) {
                        if (secretario.equals(per.getPersonas())) {
                            esExSecretario = true;
                            break;
                        }
                    }

                    if (!esExSecretario) {
                        JsfUtil.addErrorMessage("La persona " + secretario.getNombresApellidos() + " no tiene rol de ex secretario");
                        return resp;

                    }
                }
            }
        }

        List<ExpTiposActuacion> tipoOficio = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_OFICIO).getResultList();
        if (tipoOficio.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro tipo actuación OFICIO");
            return resp;

        }

        if (docImprimir != null) {
            if (docImprimir.getPreopinante() != null) {
                if (!docImprimir.getPreopinante().equals(personaUsuario)) {
                    List<ExpPersonasFirmantesPorActuaciones> listaPer = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("personaFirmante", docImprimir.getPreopinante()).setParameter("estado", new Estados("AC")).getResultList();
                    if (!listaPer.isEmpty()) {
                        if (!listaPer.get(0).isFirmado()) {
                            JsfUtil.addErrorMessage("El preopinante debe firmar primero");
                            return resp;
                        }
                    } else {
                        JsfUtil.addErrorMessage("El preopinante debe firmar primero");
                        return resp;
                    }
                }
            }
        }

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

        boolean esProcesoNuevo = false;

        if (docImprimir != null) {
            if (docImprimir.getArchivo() == null) {
                esProcesoNuevo = true;
            } else if ("".equals(docImprimir.getArchivo())) {
                esProcesoNuevo = true;
            }

            if (esProcesoNuevo) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String nombreArchivo = simpleDateFormat.format(fecha);
                nombreArchivo += "_" + getSelected().getId() + "_" + docImprimir.getId() + ".pdf";
                
                File copied = new File(par.getRutaArchivos() + "/" + nombreArchivo);
                
                File original = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);

                try {
                    FileUtils.copyFile(original, copied);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error en proceso.");
                    return resp;
                }

                docImprimir.setArchivo(nombreArchivo);

                expActuacionesController.setSelected(docImprimir);
                expActuacionesController.save(null);

                if (!docImprimir.isAutenticado()) {
                    autenticar(docImprimir, false);
                }
                // }
            }
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

            // System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);
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

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    docImprimir = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", docImprimir.getId()).getSingleResult();

                    // autenticar(docImprimir);
                    actualizarEstadoFirma(firma2, rolElegido);

                    resp = true;

                    if (docImprimir.getPreopinante() != null) {
                        if (docImprimir.getPreopinante().equals(personaUsuario)) {
                            if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_OFICIO.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(docImprimir.getTipoActuacion().getId())) {

                                if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId())) {
                                    incrementarNroSecuenciaActuacion(tipoOficio.get(0));
                                } else {
                                    incrementarNroSecuenciaActuacion(docImprimir.getTipoActuacion());
                                }

                                docImprimir.setNroFinal(secuencia);
                                docImprimir.setFechaFinal(fechaFinal);
                                docImprimir.setTextoFinal(textoFinal);

                                // if (!perSecretario.isEmpty()) {
                                if (secretario != null) {
                                    List<ExpEstadosActuacion> est = null;

                                    if (esExSecretario) {
                                        est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO).getResultList();
                                    } else {
                                        est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO).getResultList();
                                    }

                                    if (!est.isEmpty()) {

                                        docImprimir.setEstado(est.get(0));

                                        expActuacionesController.setSelected(docImprimir);
                                        expActuacionesController.save(null);

                                    }

                                    prepareEditFirmantes(docImprimir);

                                    if (listaPersonasFirmantes == null) {
                                        listaPersonasFirmantes = new ArrayList<>();
                                    }

                                    // listaPersonasFirmantes.add(perSecretario.get(0).getPersonas());
                                    listaPersonasFirmantes.add(secretario);
                                    selectedActuacion = docImprimir;
                                    saveFirmantes();

                                }
                            } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(docImprimir.getTipoActuacion().getId())) {

                                if (docImprimir.getResolucion() != null) {
                                    if (Constantes.TIPO_RESOLUCION_AI.equals(docImprimir.getResolucion().getTipoResolucion().getId())) {
                                        ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();
                                        incrementarNroSecuenciaActuacion(tipo);
                                        docImprimir.setNroFinal(secuencia);
                                        docImprimir.setFechaFinal(fechaFinal);
                                        docImprimir.setTextoFinal(textoFinal);

                                    } else if (Constantes.TIPO_RESOLUCION_SD.equals(docImprimir.getResolucion().getTipoResolucion().getId())) {
                                        ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_SD).getSingleResult();
                                        incrementarNroSecuenciaActuacion(tipo);
                                        docImprimir.setNroFinal(secuencia);
                                        docImprimir.setFechaFinal(fechaFinal);
                                        docImprimir.setTextoFinal(textoFinal);

                                    }
                                }

                                List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES).getResultList();

                                if (!est.isEmpty()) {

                                    docImprimir.setEstado(est.get(0));

                                    expActuacionesController.setSelected(docImprimir);
                                    expActuacionesController.save(null);

                                }
                            }
                        } else {
                            if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_OFICIO.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(docImprimir.getTipoActuacion().getId())) {
                                List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getResultList();

                                if (!est.isEmpty()) {
                                    docImprimir.setEstado(est.get(0));
                                    docImprimir.setVisible(true);
                                    docImprimir.setPersonaVisible(personaUsuario);
                                    fecha = ejbFacade.getSystemDate();
                                    docImprimir.setFechaHoraVisible(fecha);
                                    docImprimir.setFechaPresentacion(generarFechaPresentacion(fecha));

                                    expActuacionesController.setSelected(docImprimir);

                                    expActuacionesController.save(null);

                                    if (docImprimir.isNotificar()) {
                                        generarNotificaciones(docImprimir);

                                        docImprimir.setNotificado(true);
                                        docImprimir.setFechaHoraNotificado(fecha);
                                        docImprimir.setPersonaNotificado(personaUsuario);

                                        expActuacionesController.setSelected(docImprimir);
                                        expActuacionesController.save(null);
                                    }

                                    if (docImprimir.getFormato().getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_OFICIO_CORTE)) {
                                        enviarCorte(docImprimir);
                                    }

                                }

                            } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(docImprimir.getTipoActuacion().getId())) {
                                boolean encontro = false;

                                List<ExpPersonasFirmantesPorActuaciones> listaPer = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("estado", new Estados("AC")).getResultList();

                                int i = 0;
                                ExpPersonasFirmantesPorActuaciones perso = null;
                                for (; i < listaPer.size();) {
                                    perso = listaPer.get(i);

                                    List<Integer> listaRol = new ArrayList<>();
                                    listaRol.add(Constantes.ROL_PRESIDENTE);
                                    listaRol.add(Constantes.ROL_MIEMBRO);
                                    listaRol.add(Constantes.ROL_EXPRESIDENTE);
                                    listaRol.add(Constantes.ROL_EXMIEMBRO);
                                    listaRol.add(Constantes.ROL_MIEMBRO_CON_PERMISO);
                                    listaRol.add(Constantes.ROL_MIEMBRO_SUPLENTE);
                                    listaRol.add(Constantes.ROL_MIEMBRO_REEMPLAZANTE);

                                    List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", listaRol).setParameter("estado", "AC").getResultList();

                                    for (Personas p : lista2) {
                                        if (p.equals(perso.getPersonaFirmante())) {
                                            if (!perso.isFirmado()) {
                                                encontro = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (encontro) {
                                        break;
                                    }

                                    i++;
                                }

                                // List<AntecedentesRolesPorPersonas> per = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();
                                // if (perSecretario.isEmpty()) {
                                if (secretario == null) {
                                    JsfUtil.addErrorMessage("No se pude encontro el secretario correspondiente a esta actuación");
                                    return false;

                                } else {
                                    // if (personaUsuario.equals(perSecretario.get(0).getPersonas()) && i > 0) {
                                    if (personaUsuario.equals(secretario) && i > 0) {
                                        List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getResultList();

                                        if (!est.isEmpty()) {
                                            docImprimir.setEstado(est.get(0));
                                            docImprimir.setVisible(true);
                                            docImprimir.setPersonaVisible(personaUsuario);
                                            fecha = ejbFacade.getSystemDate();
                                            docImprimir.setFechaHoraVisible(fecha);
                                            docImprimir.setFechaPresentacion(generarFechaPresentacion(fecha));

                                            expActuacionesController.setSelected(docImprimir);
                                            expActuacionesController.save(null);

                                            if (docImprimir.isNotificar()) {
                                                generarNotificaciones(docImprimir);

                                                docImprimir.setNotificado(true);
                                                docImprimir.setFechaHoraNotificado(fecha);
                                                docImprimir.setPersonaNotificado(personaUsuario);

                                                expActuacionesController.setSelected(docImprimir);
                                                expActuacionesController.save(null);
                                            }
                                        }

                                    } else if (!encontro && i > 0) {

                                        List<ExpEstadosActuacion> est = null;

                                        if (esExSecretario) {
                                            est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO).getResultList();

                                        } else {
                                            est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO).getResultList();
                                        }

                                        if (!est.isEmpty()) {

                                            docImprimir.setEstado(est.get(0));

                                            expActuacionesController.setSelected(docImprimir);
                                            expActuacionesController.save(null);

                                        }

                                        prepareEditFirmantes(docImprimir);

                                        if (listaPersonasFirmantes == null) {
                                            listaPersonasFirmantes = new ArrayList<>();
                                        }

                                        // listaPersonasFirmantes.add(perSecretario.get(0).getPersonas());
                                        listaPersonasFirmantes.add(secretario);
                                        selectedActuacion = docImprimir;
                                        saveFirmantes();
                                    }
                                }

                            }

                        }
                    }

                } else {
                    if (esProcesoNuevo) {
                        volverAtrasFirma(docImprimir);
                    }
                    JsfUtil.addErrorMessage("Error en proceso");
                }
            } else {
                if (esProcesoNuevo) {
                    volverAtrasFirma(docImprimir);
                }
                JsfUtil.addErrorMessage("Error en proceso.");
            }
        } else {
            if (esProcesoNuevo) {
                volverAtrasFirma(docImprimir);
            }
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasController.setSelected(firma2);
                firmasController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }
        buscarActuaciones();
        return resp;

    }
     */
    private boolean esExSecretario(Personas persona) {
        List<AntecedentesRolesPorPersonas> perExSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_EXSECRETARIO).getResultList();
        if (perExSecretario.isEmpty()) {
            return false;
        } else {
            for (AntecedentesRolesPorPersonas per : perExSecretario) {
                if (persona.equals(per.getPersonas())) {
                    return true;
                }
            }

            return false;
        }
    }

    private boolean esSecretario(Personas persona) {
        List<AntecedentesRolesPorPersonas> perExSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();
        if (perExSecretario.isEmpty()) {
            return false;
        } else {
            for (AntecedentesRolesPorPersonas per : perExSecretario) {
                if (persona.equals(per.getPersonas())) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean firmar() {

        Date fecha = ejbFacade.getSystemDate();
        Firmas firma = null;

        ExpActuaciones docImprimirAnterior = new ExpActuaciones();

        boolean resp = false;

        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        List<ExpActuaciones> listaAct = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", docImprimir.getId()).getResultList();
        if (listaAct.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro actuación a firmar");
            return resp;
        }

        docImprimir = listaAct.get(0);
        List<ExpPersonasFirmantesPorActuaciones> listaPerAnt = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getResultList();
        if (listaPerAnt.isEmpty()) {
            JsfUtil.addErrorMessage("Ud ya no es firmante de esta actuación");
            return resp;
        }

        if (listaPerAnt.get(0).isFirmado()) {
            JsfUtil.addErrorMessage("Ud ya firmó esta actuación");
            return resp;
        }

        if (!verifActuacionAnterior(docImprimir)) {
            JsfUtil.addErrorMessage("Ud tiene una actuación anterior que firmar en esta causa");
            return resp;

        }

        List<AntecedentesRolesPorPersonas> perSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();
        if (perSecretario.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro persona con rol SECRETARIO");
            return resp;
        }

        Personas secretario = null;

        boolean esExSecretario = false;
        if (docImprimir.getSecretario() == null) {
            secretario = perSecretario.get(0).getPersonas();
        } else {
            secretario = docImprimir.getSecretario();

            if (!secretario.equals(perSecretario.get(0).getPersonas())) {

                List<AntecedentesRolesPorPersonas> perExSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_EXSECRETARIO).getResultList();
                if (perExSecretario.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encontro persona con rol EXSECRETARIO");
                    return resp;
                } else {
                    for (AntecedentesRolesPorPersonas per : perExSecretario) {
                        if (secretario.equals(per.getPersonas())) {
                            esExSecretario = true;
                            break;
                        }
                    }

                    if (!esExSecretario) {
                        JsfUtil.addErrorMessage("La persona " + secretario.getNombresApellidos() + " no tiene rol de ex secretario");
                        return resp;

                    }
                }
            }
        }

        List<ExpTiposActuacion> tipoOficio = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_OFICIO).getResultList();
        if (tipoOficio.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro tipo actuación OFICIO");
            return resp;

        }

        if (docImprimir != null) {
            if (docImprimir.getPreopinante() != null) {
                if (!docImprimir.getPreopinante().equals(personaUsuario)) {
                    List<ExpPersonasFirmantesPorActuaciones> listaPer = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("personaFirmante", docImprimir.getPreopinante()).setParameter("estado", new Estados("AC")).getResultList();
                    if (!listaPer.isEmpty()) {
                        if (!listaPer.get(0).isFirmado()) {
                            JsfUtil.addErrorMessage("El preopinante debe firmar primero");
                            return resp;
                        }
                    } else {
                        JsfUtil.addErrorMessage("El preopinante debe firmar primero");
                        return resp;
                    }
                }
            }
        }

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

        boolean esProcesoNuevo = false;

        if (docImprimir != null) {
            if (docImprimir.getArchivo() == null) {
                esProcesoNuevo = true;
            } else if ("".equals(docImprimir.getArchivo())) {
                esProcesoNuevo = true;
            }

            if (esProcesoNuevo) {
                try {
                    generarArchivo();
                } catch (Exception e) {
                    e.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo completar la firma");
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String nombreArchivo = simpleDateFormat.format(fecha);
                nombreArchivo += "_" + getSelected().getId() + "_" + docImprimir.getId() + ".pdf";

                File copied = new File(par.getRutaArchivos() + "/" + nombreArchivo);

                File original = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);

                try {
                    FileUtils.copyFile(original, copied);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error en proceso.");
                    return resp;
                }

                docImprimir.setArchivo(nombreArchivo);

                expActuacionesController.setSelected(docImprimir);
                expActuacionesController.save(null);

                if (!docImprimir.isAutenticado()) {
                    autenticar(docImprimir, false);
                }
                // }
            }
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

            // System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);
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

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    docImprimir = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", docImprimir.getId()).getSingleResult();

                    // autenticar(docImprimir);
                    actualizarEstadoFirma(firma2, rolElegido);

                    resp = true;

                    if (docImprimir.getPreopinante() != null) {
                        if (docImprimir.getPreopinante().equals(personaUsuario) || !Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(docImprimir.getTipoActuacion().getId())) {
                            if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_OFICIO.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(docImprimir.getTipoActuacion().getId())) {

                                if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId())) {
                                    incrementarNroSecuenciaActuacion(tipoOficio.get(0));
                                } else {
                                    incrementarNroSecuenciaActuacion(docImprimir.getTipoActuacion());
                                }

                                docImprimir.setNroFinal(secuencia);
                                docImprimir.setFechaFinal(fechaFinal);
                                docImprimir.setTextoFinal(textoFinal);

                                // if (!perSecretario.isEmpty()) {
                                if (secretario != null) {
                                    List<ExpEstadosActuacion> est = null;

                                    if (esExSecretario) {
                                        est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO).getResultList();
                                    } else {
                                        est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO).getResultList();
                                    }

                                    if (!est.isEmpty()) {

                                        docImprimir.setEstado(est.get(0));

                                        expActuacionesController.setSelected(docImprimir);
                                        expActuacionesController.save(null);

                                    }

                                    prepareEditFirmantes(docImprimir);

                                    if (listaPersonasFirmantes == null) {
                                        listaPersonasFirmantes = new ArrayList<>();
                                    }

                                    // listaPersonasFirmantes.add(perSecretario.get(0).getPersonas());
                                    listaPersonasFirmantes.add(secretario);
                                    selectedActuacion = docImprimir;
                                    saveFirmantes();

                                }
                            } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(docImprimir.getTipoActuacion().getId())) {

                                if (docImprimir.getResolucion() != null) {
                                    if (Constantes.TIPO_RESOLUCION_AI.equals(docImprimir.getResolucion().getTipoResolucion().getId())) {
                                        ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();
                                        incrementarNroSecuenciaActuacion(tipo);
                                        docImprimir.setNroFinal(secuencia);
                                        docImprimir.setFechaFinal(fechaFinal);
                                        docImprimir.setTextoFinal(textoFinal);
                                    } else if (Constantes.TIPO_RESOLUCION_SD.equals(docImprimir.getResolucion().getTipoResolucion().getId())) {
                                        ExpTiposActuacion tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_SD).getSingleResult();
                                        incrementarNroSecuenciaActuacion(tipo);
                                        docImprimir.setNroFinal(secuencia);
                                        docImprimir.setFechaFinal(fechaFinal);
                                        docImprimir.setTextoFinal(textoFinal);
                                    }
                                }

                                List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES).getResultList();

                                if (!est.isEmpty()) {

                                    docImprimir.setEstado(est.get(0));

                                    expActuacionesController.setSelected(docImprimir);
                                    expActuacionesController.save(null);

                                }
                            }
                        } else {
                            if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_OFICIO.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(docImprimir.getTipoActuacion().getId())
                                    || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(docImprimir.getTipoActuacion().getId())) {

                                List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getResultList();

                                if (!est.isEmpty()) {
                                    docImprimir.setEstado(est.get(0));
                                    docImprimir.setVisible(true);
                                    docImprimir.setPersonaVisible(personaUsuario);
                                    fecha = ejbFacade.getSystemDate();
                                    Date fechaPresentacion = generarFechaPresentacion(fecha);
                                    docImprimir.setFechaHoraVisible(fecha);
                                    docImprimir.setFechaPresentacion(fechaPresentacion);

                                    expActuacionesController.setSelected(docImprimir);

                                    expActuacionesController.save(null);

                                    if (docImprimir.isNotificar()) {
                                        generarNotificaciones(docImprimir, fecha, fechaPresentacion);
                                        // generarNotificaciones(docImprimir, fechaPresentacion);

                                        docImprimir.setNotificado(true);
                                        docImprimir.setFechaHoraNotificado(fecha);
                                        docImprimir.setPersonaNotificado(personaUsuario);

                                        expActuacionesController.setSelected(docImprimir);
                                        expActuacionesController.save(null);
                                    }

                                    if (docImprimir.getFormato().getTipoActuacion().getId().equals(Constantes.TIPO_ACTUACION_OFICIO_CORTE)) {
                                        enviarCorte(docImprimir);
                                    }

                                }

                            } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(docImprimir.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(docImprimir.getTipoActuacion().getId())) {
                                boolean encontro = false;

                                List<ExpPersonasFirmantesPorActuaciones> listaPer = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", docImprimir).setParameter("estado", new Estados("AC")).getResultList();

                                int i = 0;
                                ExpPersonasFirmantesPorActuaciones perso = null;
                                for (; i < listaPer.size();) {
                                    perso = listaPer.get(i);

                                    List<Integer> listaRol = new ArrayList<>();
                                    listaRol.add(Constantes.ROL_PRESIDENTE);
                                    listaRol.add(Constantes.ROL_MIEMBRO);
                                    listaRol.add(Constantes.ROL_EXPRESIDENTE);
                                    listaRol.add(Constantes.ROL_EXMIEMBRO);
                                    listaRol.add(Constantes.ROL_MIEMBRO_CON_PERMISO);
                                    listaRol.add(Constantes.ROL_MIEMBRO_SUPLENTE);
                                    listaRol.add(Constantes.ROL_MIEMBRO_REEMPLAZANTE);

                                    List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", listaRol).setParameter("estado", "AC").getResultList();

                                    for (Personas p : lista2) {
                                        if (p.equals(perso.getPersonaFirmante())) {
                                            if (!perso.isFirmado()) {
                                                encontro = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (encontro) {
                                        break;
                                    }

                                    i++;
                                }

                                // List<AntecedentesRolesPorPersonas> per = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();
                                // if (perSecretario.isEmpty()) {
                                if (secretario == null) {
                                    JsfUtil.addErrorMessage("No se pude encontro el secretario correspondiente a esta actuación");
                                    return false;

                                } else {
                                    // if (personaUsuario.equals(perSecretario.get(0).getPersonas()) && i > 0) {
                                    if (personaUsuario.equals(secretario) && i > 0) {
                                        List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getResultList();

                                        if (!est.isEmpty()) {
                                            docImprimir.setEstado(est.get(0));
                                            docImprimir.setVisible(true);
                                            docImprimir.setPersonaVisible(personaUsuario);
                                            fecha = ejbFacade.getSystemDate();
                                            docImprimir.setFechaHoraVisible(fecha);
                                            docImprimir.setFechaPresentacion(generarFechaPresentacion(fecha));
                                            // docImprimir.setFechaPresentacion(fechaFinal);

                                            expActuacionesController.setSelected(docImprimir);
                                            expActuacionesController.save(null);

                                            if (docImprimir.isNotificar()) {
                                                generarNotificaciones(docImprimir, fecha, fechaFinal);
                                                // generarNotificaciones(docImprimir, fechaFinal);

                                                docImprimir.setNotificado(true);
                                                docImprimir.setFechaHoraNotificado(fecha);
                                                docImprimir.setPersonaNotificado(personaUsuario);

                                                expActuacionesController.setSelected(docImprimir);
                                                expActuacionesController.save(null);
                                            }
                                        }

                                    } else if (!encontro && i > 0) {

                                        List<ExpEstadosActuacion> est = null;

                                        if (esExSecretario) {
                                            est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO).getResultList();

                                        } else {
                                            est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO).getResultList();
                                        }

                                        if (!est.isEmpty()) {

                                            docImprimir.setEstado(est.get(0));

                                            expActuacionesController.setSelected(docImprimir);
                                            expActuacionesController.save(null);

                                        }

                                        prepareEditFirmantes(docImprimir);

                                        if (listaPersonasFirmantes == null) {
                                            listaPersonasFirmantes = new ArrayList<>();
                                        }

                                        // listaPersonasFirmantes.add(perSecretario.get(0).getPersonas());
                                        listaPersonasFirmantes.add(secretario);
                                        selectedActuacion = docImprimir;
                                        saveFirmantes();
                                    }
                                }

                            }

                        }
                    }

                } else {
                    if (esProcesoNuevo) {
                        volverAtrasFirma(docImprimir);
                    }
                    JsfUtil.addErrorMessage("Error en proceso");
                }
            } else {
                if (esProcesoNuevo) {
                    volverAtrasFirma(docImprimir);
                }
                JsfUtil.addErrorMessage("Error en proceso.");
            }
        } else {
            if (esProcesoNuevo) {
                volverAtrasFirma(docImprimir);
            }
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasController.setSelected(firma2);
                firmasController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }
        buscarActuaciones();
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

            Date fecha = ejbFacade.getSystemDate();
            enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Se ha revisado una presentacion en el Sistema de Expediente Electrónico JEM", texto, docImprimir.getDocumentoJudicial(), docImprimir, fecha, personaUsuario);

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

        if ("".equals(caratulaBusqueda) && "".equals(nroCausa) && "".equals(ano) && fechaDesde == null && fechaHasta == null && estadosProcesoFiltro.isEmpty()) {
            JsfUtil.addErrorMessage("Debe ingresar algun criterio de busqueda");
            return;
        }

        if (!estadosProcesoFiltro.isEmpty()) {
            buscarPorEstadoProceso();
        } else if (!"".equals(nroCausa)) {
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
            if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_EXPEDIENTES_FIRMANTE, rolElegido.getId())) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausaFirmante", DocumentosJudiciales.class).setParameter("causa", "%" + nroCausa + "%").setParameter("canalEntradaDocumentoJudicial", canal).setParameter("limite", 0).setParameter("personaFirmante", personaSelected).getResultList());

            } else if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("causa", "%" + nroCausa + "%").setParameter("canalEntradaDocumentoJudicial", canal).getResultList());

            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausaPersona", DocumentosJudiciales.class).setParameter("causa", "%" + nroCausa + "%").setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
            }
        }
    }

    private void buscarPorEstadoProceso() {
        if (estadosProcesoFiltro == null) {
            JsfUtil.addErrorMessage("Debe ingresar estados proceso ");
            return;
        } else if (estadosProcesoFiltro.isEmpty()) {
            JsfUtil.addErrorMessage("Debe ingresar estados proceso.");
            return;

        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_EXPEDIENTES_FIRMANTE, rolElegido.getId())) {
                if ("1".equals(filtroOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEstadosProcesoExpedienteElectronicoFirmante", DocumentosJudiciales.class).setParameter("estadosProcesoExpedienteElectronico", estadosProcesoFiltro).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("limite", 0).setParameter("personaFirmante", personaSelected).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEstadosProcesoExpedienteElectronicoFirmanteOrderFechaInicioEnjuiciamiento", DocumentosJudiciales.class).setParameter("estadosProcesoExpedienteElectronico", estadosProcesoFiltro).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("limite", 0).setParameter("personaFirmante", personaSelected).getResultList());
                }
            } else if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                if ("1".equals(filtroOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEstadosProcesoExpedienteElectronico", DocumentosJudiciales.class).setParameter("estadosProcesoExpedienteElectronico", estadosProcesoFiltro).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEstadosProcesoExpedienteElectronicoOrderFechaInicioEnjuiciamiento", DocumentosJudiciales.class).setParameter("estadosProcesoExpedienteElectronico", estadosProcesoFiltro).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                }
            } else {
                if ("1".equals(filtroOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEstadosProcesoPersonaExpedienteElectronico", DocumentosJudiciales.class).setParameter("estadosProcesoExpedienteElectronico", estadosProcesoFiltro).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEstadosProcesoPersonaExpedienteElectronicoOrderFechaInicioEnjuiciamiento", DocumentosJudiciales.class).setParameter("estadosProcesoExpedienteElectronico", estadosProcesoFiltro).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());

                }
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

                if ("1".equals(filtroOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCaratula", DocumentosJudiciales.class).setParameter("caratula", "%" + caratulaBusqueda + "%").setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCaratulaOrderFechaInicioEnjuiciamiento", DocumentosJudiciales.class).setParameter("caratula", "%" + caratulaBusqueda + "%").setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                }
            } else {
                if ("1".equals(filtroOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCaratulaPersona", DocumentosJudiciales.class).setParameter("caratula", "%" + caratulaBusqueda + "%").setParameter("persona1", personaUsuario).setParameter("persona2", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCaratulaPersonaOrderFechaInicioEnjuiciamiento", DocumentosJudiciales.class).setParameter("caratula", "%" + caratulaBusqueda + "%").setParameter("persona1", personaUsuario).setParameter("persona2", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                }
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
                if ("1".equals(filtroOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByFechaAlta", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByFechaAltaOrderFechaInicioEnjuiciamiento", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                }
            } else {
                if ("1".equals(filtroOrden)) {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByPersonaFechaAlta", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                } else {
                    setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByPersonaFechaAltaOrderFechaInicioEnjuiciamiento", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
                }
            }
        }
    }

    /*
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
     */
    public void buscarPorAno() {
        if (ano == null) {
            JsfUtil.addErrorMessage("Debe ingresar Año");
        } else if ("".equals(ano)) {
            JsfUtil.addErrorMessage("Debe ingresar Año");
        } else if (ano.length() < 4) {
            JsfUtil.addErrorMessage("Debe ingresar año de 4 dígitos");

        } else {
            if (filtroURL.verifPermiso(Constantes.PERMISO_ACCESO_TOTAL_EXPEDIENTES, rolElegido.getId())) {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByAno", DocumentosJudiciales.class
                ).setParameter("ano", Integer.valueOf(ano)).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());

            } else {
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByPersonaAno", DocumentosJudiciales.class
                ).setParameter("ano", Integer.valueOf(ano)).setParameter("persona", personaUsuario).setParameter("canalEntradaDocumentoJudicial", canal).getResultList());
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

                    List<PersonasPorDocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicial", PersonasPorDocumentosJudiciales.class
                    ).setParameter("documentoJudicial", getSelected().getId()).getResultList();

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

            if (actuacion.getPreopinante() == null) {
                JsfUtil.addErrorMessage("Debe elegir preopinante");
                return false;
            }

            return true;
        }

        return false;
    }

    public void enviarEmailAviso(String email, String asunto, String msg, DocumentosJudiciales doc, ExpActuaciones act, Date fechaHoraEnvioProgramado, Personas per) {

        Date fecha = ejbFacade.getSystemDate();

        ExpEnviosEmail env = new ExpEnviosEmail(fecha, null, fechaHoraEnvioProgramado, act, doc, per, asunto, msg, email, false);

        enviosEmailController.setSelected(env);

        enviosEmailController.saveNew(null);

        /*
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
         */
    }

    private void guardarRolFirmantePorActuacion(ExpActuaciones act, Integer rol) {
        ExpRolesFirmantesPorActuaciones firm = rolesFirmantesPorActuacionesController.prepareCreate(null);
        firm.setActuacion(act);
        firm.setDocumentoJudicial(act.getDocumentoJudicial());

        Estados est = ejbFacade.getEntityManager().createNamedQuery("Estados.findByCodigo", Estados.class
        ).setParameter("codigo", Constantes.ESTADO_USUARIO_AC).getSingleResult();

        firm.setEstado(est);
        firm.setFechaHoraAlta(ejbFacade.getSystemDate());
        firm.setFirmado(false);
        firm.setPersonaAlta(personaUsuario);

        AntecedentesRoles antRol = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRoles.findById", AntecedentesRoles.class
        ).setParameter("id", rol).getSingleResult();

        firm.setRolFirmante(antRol);

        rolesFirmantesPorActuacionesController.setSelected(firm);
        rolesFirmantesPorActuacionesController.save(null);
    }

    /*
    private boolean guardarActuacion(UploadedFile file, Date fecha, String hash, DocumentosJudiciales doc, ExpActuaciones act, boolean validar, Personas per) {

        String nombreAbogado = per.getNombresApellidos();
        String ci = per.getCi() == null ? "" : per.getCi();
        if (file != null) {
            if (file.getContents().length > 0) {

                if (par == null) {
                    JsfUtil.addErrorMessage("Error al obtener parametros");
                    return false;
                }

                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file.getInputstream());
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
                    
//                    fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
//                    fos.write(bytes);
//                    fos.flush();
//                    fos.close();
                    

                    if (validar) {
                        if (Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())) {
                            fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                            fos.write(bytes);
                            fos.flush();
                            fos.close();
                            autenticar(act, false);

                            if (!Constantes.TIPO_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getId())) {
                                List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                                List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

                                lista.addAll(lista2);

                                savePersonasFirmantes(act, lista, new ArrayList<>());
                            }
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
                        boolean visibleNoti = true;
                        if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
                            visible = false;
                        }
                        guardarNotificacion(actuacion, personaUsuario, destinatario, visibleNoti);
                    }

                    
//                    if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
//                        guardarRolFirmantePorActuacion(actuacion, Constantes.ROL_UJIER);
//                    }
                     
                    return true;

                } else {
                    expActuacionesController.delete(null);
                    return false;
                }

            }
        }
        return false;
    }
     */
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
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void agregarQR(String nombreOri, String nombre, String hash, boolean primeraPagina) {
        // nombre + "_temp"
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

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(nombreOri), new PdfWriter(nombre));

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
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean guardarActuacion(Date fecha, DocumentosJudiciales doc, Resoluciones res, Personas per, String hash, ExpTiposActuacion tipoActuacion, Integer parFojas, boolean parVisible, String descripcion, String texto, ExpFormatos formato, Personas preopinante, ExpEstadosActuacion est, Personas secretario, boolean urgente) {
        /*
        if (file != null) {
            if (file.getContents().length > 0) {
         */
        if (par == null) {
            JsfUtil.addErrorMessage("Error al obtener parametros");
            return false;

        }
        /*
                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file.getInputstream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error al leer archivo");
                    return false;
                }
         */
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
        act.setPreopinante(preopinante);
        act.setSecretario(secretario);
        act.setEstado(est);
        act.setUrgente(urgente);

        if (descripcion == null) {
            act.setDescripcion(tipoActuacion.getDescripcion());
        } else {
            act.setDescripcion(descripcion);
        }

        act.setTexto(texto);
        act.setHash(hash);
        act.setResolucion(res);
        act.setVisible(parVisible);
        act.setArchivo(null);
        act.setFormato(formato);

        expActuacionesController.setSelected(act);

        expActuacionesController.saveNew(null);
        /*
        if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getCategoriaActuacion().getId())
                || Constantes.TIPO_ACTUACION_RECUSACION.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())) {
            boolean visibleNoti = false;
            if (Constantes.TIPO_ACTUACION_RECUSACION.equals(act.getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())) {
                visibleNoti = true;
            }
            guardarNotificacion(act, personaUsuario, null, visibleNoti);
        }

        if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
            guardarRolFirmantePorActuacion(actuacion, Constantes.ROL_UJIER);
        }
         */

        return true;

        // return actualizarNombreArchivoActuacion(validar, nombreArchivo, bytes, nombresAbogado, ci, doc, act, fecha, hash);
/*
            }
        }
                
        return false;
         */
    }

    private boolean actualizarNombreArchivoActuacion(boolean validar, String nombreArchivo, byte bytes[], String nombresAbogado, String ci, DocumentosJudiciales doc, ExpActuaciones act, Date fecha, String hash) {

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
    public Date generarFechaPresentacion(Date fecha) {
        // Aqui analizar dias habiles y feriados

        Date fechaFinal = fecha;

        boolean encontro = false;
        LocalDateTime localDateTime = null;
        while (true) {
            localDateTime = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            if (localDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
                localDateTime = localDateTime.plusDays(2);
                Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Calendar cal = Calendar.getInstance();
                cal.setTime(curr);
                cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                cal.set(Calendar.MINUTE, par.getMinutoInicio());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                fechaFinal = cal.getTime();
                encontro = true;
            } else if (localDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
                localDateTime = localDateTime.plusDays(1);
                Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                Calendar cal = Calendar.getInstance();
                cal.setTime(curr);
                cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                cal.set(Calendar.MINUTE, par.getMinutoInicio());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                fechaFinal = cal.getTime();
                encontro = true;
            }
            localDateTime = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            List<ExpFeriados> lista = ejbFacade.getEntityManager().createNamedQuery("ExpFeriados.findByFecha", ExpFeriados.class).setParameter("fecha", fechaFinal).getResultList();
            if (lista.isEmpty()) {
                if (encontro) {
                    break;
                } else {
                    int hora = localDateTime.getHour();
                    int minuto = localDateTime.getMinute();
                    int segundo = localDateTime.getSecond();

                    if (hora < par.getHoraInicio() || (hora == par.getHoraInicio() && minuto < par.getMinutoInicio())) {
                        Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(curr);
                        cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                        cal.set(Calendar.MINUTE, par.getMinutoInicio());
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        fechaFinal = cal.getTime();
                    }

                    boolean continuar = false;

                    if (hora > par.getHoraFin() || (hora == par.getHoraFin() && minuto >= par.getMinutoFin())) {
                        encontro = true;
                        localDateTime = localDateTime.plusDays(1);
                        Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(curr);
                        cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
                        cal.set(Calendar.MINUTE, par.getMinutoInicio());
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        fechaFinal = cal.getTime();
                        continuar = true;
                    }

                    if (continuar) {
                        continue;
                    } else {
                        break;
                    }
                }
            }

            encontro = true;

            localDateTime = fechaFinal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            localDateTime = localDateTime.plusDays(1);
            Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();
            cal.setTime(curr);
            cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
            cal.set(Calendar.MINUTE, par.getMinutoInicio());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            fechaFinal = cal.getTime();
        }

        return fechaFinal;
    }

    private Date generarFechaPresentacion3(Date fecha) {
        // Aqui analizar dias habiles y feriados

        LocalDateTime localDateTime = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (localDateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
            localDateTime = localDateTime.plusDays(2);
            Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();
            cal.setTime(curr);
            cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
            cal.set(Calendar.MINUTE, par.getMinutoInicio());
            cal.set(Calendar.SECOND, 0);
            return cal.getTime();
        } else if (localDateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            localDateTime = localDateTime.plusDays(1);
            Date curr = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Calendar cal = Calendar.getInstance();
            cal.setTime(curr);
            cal.set(Calendar.HOUR_OF_DAY, par.getHoraInicio());
            cal.set(Calendar.MINUTE, par.getMinutoInicio());
            cal.set(Calendar.SECOND, 0);
            return cal.getTime();
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

        try {
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
                    ExpPersonasFirmantesPorActuaciones perDoc = new ExpPersonasFirmantesPorActuaciones();
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
                    /*
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
                     */

                }
                /*
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
                 */

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

                    personasFirmantesPorActuacionesController.setSelected(listaFirmantesActual.get(i));
                    personasFirmantesPorActuacionesController.delete(null);
                    /*
                    listaFirmantesActual.get(i).setEstado(new Estados("IN"));
                    listaFirmantesActual.get(i).setFechaHoraAlta(fecha);
                    listaFirmantesActual.get(i).setPersonaAlta(personaUsuario);
                    listaFirmantesActual.get(i).setFirmado(per2.isFirmado());
                    personasFirmantesPorActuacionesController.setSelected(listaFirmantesActual.get(i));
                    personasFirmantesPorActuacionesController.save(null);
                     */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

                    enviarEmailAviso(pers.getEmail(), "Firma pendiente en Expediente Electrónico JEM", texto, selectedActuacion.getDocumentoJudicial(), selectedActuacion, fecha, personaUsuario);
                }

                if (enviarTextoBorrarParte) {
                    String texto = "<p>Hola " + pers.getNombresApellidos() + "<br> "
                            + "     Ud dejo de ser firmante en una presentacion en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + selectedActuacion.getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(pers.getEmail(), "Dejo de ser firmante en Expediente Electrónico JEM", texto, selectedActuacion.getDocumentoJudicial(), selectedActuacion, fecha, personaUsuario);
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

                enviarEmailAviso(per.getEmail(), "Revision pendiente en Expediente Electrónico JEM", texto, selectedActuacion.getDocumentoJudicial(), selectedActuacion, fecha, personaUsuario);
            }

            if (enviarTextoBorrarRevision) {
                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Ud ya no debe mas revisar una presentacion en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + selectedActuacion.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Ya no debe revisar presentacion en Expediente Electrónico JEM", texto, selectedActuacion.getDocumentoJudicial(), selectedActuacion, fecha, personaUsuario);
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

                        perDoc2 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialPersona", PersonasPorDocumentosJudiciales.class
                        ).setParameter("documentoJudicial", getSelected().getId()).setParameter("persona", per.getId()).getSingleResult();

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

                    enviarEmailAviso(per.getEmail(), "Agregado como Parte en Expediente Electrónico JEM", texto, getSelected(), null, fecha, personaUsuario);
                }

                if (enviarTextoBorrarParte) {
                    String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                            + "     Ud ya no es parte en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + " Ha sido borrado de la causa<br>";

                    enviarEmailAviso(per.getEmail(), "Borrado como Parte en Expediente Electrónico JEM", texto, getSelected(), null, fecha, personaUsuario);
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
                        listaPartesActual.get(i).setFechaHoraUltimoEstado(fecha);
                        listaPartesActual.get(i).setPersonaUltimoEstado(personaUsuario);
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
                        //perDoc2.setPersonaAlta(personaUsuario);
                        //perDoc2.setUsuarioAlta(usuario);
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

 /*
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
             */
        }
    }

    public void saveInhibidos() {

        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            Personas per2 = null;
            ExpPersonasInhibidasPorDocumentosJudiciales perDocActual = null;
            ExpPersonasInhibidasPorDocumentosJudiciales perDoc = null;
            boolean encontro = false;
            List<ExpPersonasInhibidasPorDocumentosJudiciales> listaInhibidosActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasInhibidasPorDocumentosJudiciales.findByDocumentoJudicial", ExpPersonasInhibidasPorDocumentosJudiciales.class
            ).setParameter("documentoJudicial", getSelected()).getResultList();

            for (Personas per : listaInhibidosAdmin) {
                encontro = false;
                perDocActual = null;
                for (int i = 0; i < listaInhibidosActual.size(); i++) {
                    per2 = listaInhibidosActual.get(i).getPersona();
                    if (per2.equals(per)) {
                        encontro = true;
                        perDocActual = listaInhibidosActual.get(i);
                        break;
                    }
                }

                if (!encontro) {
                    perDoc = new ExpPersonasInhibidasPorDocumentosJudiciales();
                    perDoc.setPersona(per);
                    perDoc.setDocumentoJudicial(getSelected());
                    perDoc.setPersonaAlta(personaUsuario);
                    perDoc.setFechaHoraAlta(fecha);
                    perDoc.setPersonaUltimoEstado(personaUsuario);
                    perDoc.setFechaHoraUltimoEstado(fecha);
                    perDoc.setEstado(new Estados("AC"));
                    personasInhibidasPorDocumentosJudicialesController.setSelected(perDoc);
                    personasInhibidasPorDocumentosJudicialesController.saveNew(null);
                } else {
                    if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                        perDocActual.setEstado(new Estados(per.getEstado()));
                        perDocActual.setFechaHoraUltimoEstado(fecha);
                        perDocActual.setPersonaUltimoEstado(personaUsuario);
                        personasInhibidasPorDocumentosJudicialesController.setSelected(perDocActual);
                        personasInhibidasPorDocumentosJudicialesController.save(null);
                    }
                }

            }

            for (int i = 0; i < listaInhibidosActual.size(); i++) {
                per2 = listaInhibidosActual.get(i).getPersona();
                encontro = false;
                for (Personas per : listaInhibidosAdmin) {
                    if (per2.equals(per)) {
                        encontro = true;
                        break;
                    }
                }

                if (!encontro) {
                    listaInhibidosActual.get(i).setEstado(new Estados("IN"));
                    listaInhibidosActual.get(i).setFechaHoraAlta(fecha);
                    listaInhibidosActual.get(i).setPersonaAlta(personaUsuario);
                    personasInhibidasPorDocumentosJudicialesController.setSelected(listaInhibidosActual.get(i));
                    personasInhibidasPorDocumentosJudicialesController.save(null);
                }
            }
        }
    }

    public void saveRestringidos() {

        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            Personas per2 = null;
            ExpPersonasHabilitadasPorDocumentosJudiciales perDocActual = null;
            ExpPersonasHabilitadasPorDocumentosJudiciales perDoc = null;
            boolean encontro = false;
            List<ExpPersonasHabilitadasPorDocumentosJudiciales> listaHabilitadasActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasHabilitadasPorDocumentosJudiciales.findByDocumentoJudicial", ExpPersonasHabilitadasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();

            for (Personas per : listaRestringidosAdmin) {
                encontro = false;
                perDocActual = null;
                for (int i = 0; i < listaHabilitadasActual.size(); i++) {
                    per2 = listaHabilitadasActual.get(i).getPersona();
                    if (per2.equals(per)) {
                        encontro = true;
                        perDocActual = listaHabilitadasActual.get(i);
                        break;
                    }
                }

                if (!encontro) {
                    perDoc = new ExpPersonasHabilitadasPorDocumentosJudiciales();
                    perDoc.setPersona(per);
                    perDoc.setDocumentoJudicial(getSelected());
                    perDoc.setPersonaAlta(personaUsuario);
                    perDoc.setFechaHoraAlta(fecha);
                    perDoc.setPersonaUltimoEstado(personaUsuario);
                    perDoc.setFechaHoraUltimoEstado(fecha);
                    perDoc.setEstado(new Estados("AC"));
                    personasHabilitadasPorDocumentosJudicialesController.setSelected(perDoc);
                    personasHabilitadasPorDocumentosJudicialesController.saveNew(null);
                } else {
                    if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                        perDocActual.setEstado(new Estados(per.getEstado()));
                        perDocActual.setFechaHoraUltimoEstado(fecha);
                        perDocActual.setPersonaUltimoEstado(personaUsuario);
                        personasHabilitadasPorDocumentosJudicialesController.setSelected(perDocActual);
                        personasHabilitadasPorDocumentosJudicialesController.save(null);
                    }
                }

            }

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
            listaHabilitadasActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasHabilitadasPorDocumentosJudiciales.findByDocumentoJudicial", ExpPersonasHabilitadasPorDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList();

            Personas per3 = null;
            for (int i = 0; i < listaHabilitadasActual.size(); i++) {
                per2 = listaHabilitadasActual.get(i).getPersona();
                encontro = false;
                for (Personas per : listaRestringidosAdmin) {
                    if (per2.equals(per)) {
                        encontro = true;
                        per3 = per;
                        break;
                    }
                }

                if (!encontro) {
                    listaHabilitadasActual.get(i).setEstado(new Estados("IN"));
                    listaHabilitadasActual.get(i).setFechaHoraUltimoEstado(fecha);
                    listaHabilitadasActual.get(i).setPersonaUltimoEstado(personaUsuario);
                    personasHabilitadasPorDocumentosJudicialesController.setSelected(listaHabilitadasActual.get(i));
                    personasHabilitadasPorDocumentosJudicialesController.save(null);
                } else {
                    listaHabilitadasActual.get(i).setEstado(new Estados(per3.getEstado()));
                    listaHabilitadasActual.get(i).setFechaHoraUltimoEstado(fecha);
                    listaHabilitadasActual.get(i).setPersonaUltimoEstado(personaUsuario);
                    personasHabilitadasPorDocumentosJudicialesController.setSelected(listaHabilitadasActual.get(i));
                    personasHabilitadasPorDocumentosJudicialesController.save(null);
                }
            }
        }
    }

    public boolean validarSaveNewOtraActuacion() {
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

    /*
    public String saveNewOtraActuacion() {
        if (actuacion != null && getSelected() != null) {

            if (!validarSaveNewOtraActuacion()) {
                return "";
            }

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un documento");
                return "";
            } else if (file.getContents().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return "";
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
                if (file.getContents().length > 0) {
                    String hash = "";
                    try {
                        hash = Utils.generarHash(fecha, personaUsuario.getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                        return "index.xhtml?tipo=" + accion + "&faces-redirect=true";
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

                    // EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();
                    EstadosProcesalesDocumentosJudiciales estadoProc = new EstadosProcesalesDocumentosJudiciales();

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
                        || Constantes.TIPO_ACTUACION_OFICIO.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_RESOLUCION.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_SD.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_NOTIFICACION.equals(expActuacionesController.getSelected().getTipoActuacion().getId()))) {
                    String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                            + "     Se a presentado una presentación del tipo " + expActuacionesController.getSelected().getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                            + getSelected().getCaratulaString()
                            + "<br><br>"
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);
                    
                    //if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
//
                    //    String texto2 = "<p>Hola " + destinatario.getNombresApellidos() + "<br> "
                    //            + "     Se ha presentado una presentación del tipo " + expActuacionesController.getSelected().getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                    //            + getSelected().getCaratulaString()
                    //            + "<br><br>"
                    //            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    //            + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";
//
                    //    enviarEmailAviso(destinatario.getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto2);
                    //}
                     
                }
            }

            personasController.badgePresentacionesPendientes();

            // Providencia no necesita de un segundo documento
            if (!Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(expActuacionesController.getSelected().getTipoActuacion().getId())) {
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('ExpAcusacionesOtroArchivoDialog').show();");
            }
        }

        // return "index.xhtml?tipo=" + accion + "&otro=SI&faces-redirect=true";
        String retornar = "index.xhtml?tipo=" + accion + "&act=" + expActuacionesController.getSelected().getId() + "&otro=SI&faces-redirect=true";
        return retornar;
    }
     */
 /*
    private boolean guardarActuacion(UploadedFile file, Date fecha, String hash, DocumentosJudiciales doc, ExpActuaciones act, boolean validar, Personas per) {

        String nombreAbogado = per.getNombresApellidos();
        String ci = per.getCi() == null ? "" : per.getCi();
        if (file != null) {
            if (file.getContents().length > 0) {

                if (par == null) {
                    JsfUtil.addErrorMessage("Error al obtener parametros");
                    return false;
                }

                byte[] bytes = null;
                try {
                    bytes = IOUtils.toByteArray(file.getInputstream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error al leer archivo");
                    return false;
                }

                List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getResultList();

                if (est.isEmpty()) {
                    return false;
                }

                act.setEstado(est.get(0));

                expActuacionesController.setSelected(act);

                expActuacionesController.saveNew(null);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String nombreArchivo = simpleDateFormat.format(actuacion.getFechaHoraAlta());
                nombreArchivo += "_" + doc.getId() + "_" + act.getId() + ".pdf";

                act.setArchivo(nombreArchivo);

                FileOutputStream fos = null;
                try {
                    
                    //fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                    //fos.write(bytes);
                    //fos.flush();
                    //fos.close();
                     

                    if (validar) {
                        if (Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getId())
                                || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())) {
                            fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                            fos.write(bytes);
                            fos.flush();
                            fos.close();
                            autenticar(act, false);

                            if (!Constantes.TIPO_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getId())) {
                                List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                                List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

                                lista.addAll(lista2);

                                savePersonasFirmantes(act, lista, new ArrayList<>());
                            }
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
                        boolean visibleNoti = true;
                        if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
                            visible = false;
                        }
                        guardarNotificacion(actuacion, personaUsuario, destinatario, visibleNoti);
                    }

                    
                    //if (Constantes.CATEGORIA_ACTUACION_NOTIFICACION.equals(categoriaActuacion.getId())) {
                    //    guardarRolFirmantePorActuacion(actuacion, Constantes.ROL_UJIER);
                   // }
                     
                    return true;

                } else {
                    expActuacionesController.delete(null);
                    return false;
                }

            }
        }
        return false;
    }
     */
    public String saveEditActuacion() {
        if (actuacion != null && getSelected() != null) {

            if (estadoActuacion == null) {
                JsfUtil.addErrorMessage("Debe elegir un estado para la actuación");
                return "";
            }
            /*
            List<AntecedentesRolesPorPersonas> personas = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).getResultList();
            if (personas.isEmpty()) {
                JsfUtil.addErrorMessage("No se pudo encontrar una persona con el rol de presidente");
                return "";
            }
             */
            if (!validarSaveNewActuacion()) {
                return "";

            }

            List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE).getResultList();

            Date fecha = ejbFacade.getSystemDate();

            if (est.get(0).equals(estadoActuacion)) {
                /*
                if (Constantes.TIPO_ACTUACION_OFICIO.equals(actuacion.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(actuacion.getTipoActuacion().getId())) {
                    List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", actuacion).setParameter("personaFirmante", personas.get(0).getPersonas()).setParameter("estado", new Estados("AC")).getResultList();
                    if (lista.isEmpty()) {
                        JsfUtil.addErrorMessage("No se encuentra persona firmante para el presidente");
                        return "";
                    }


                    lista.get(0).setRevisado(true);
                    lista.get(0).setFechaHoraRevisado(fecha);
                    lista.get(0).setPersonaRevisado(personaUsuario);

                    personasFirmantesPorActuacionesController.setSelected(lista.get(0));
                    personasFirmantesPorActuacionesController.save(null);
                } else if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId())) {
                 */
                List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", actuacion).setParameter("personaFirmante", actuacion.getPreopinante()).setParameter("estado", new Estados("AC")).getResultList();
                if (lista.isEmpty()) {
                    JsfUtil.addErrorMessage("No se encuentra persona firmante para el preopinante");
                    return "";
                }

                lista.get(0).setRevisado(true);
                lista.get(0).setFechaHoraRevisado(fecha);
                lista.get(0).setPersonaRevisado(personaUsuario);

                personasFirmantesPorActuacionesController.setSelected(lista.get(0));
                personasFirmantesPorActuacionesController.save(null);

                // }
            } else if (est.get(0).getOrden().compareTo(estadoActuacion.getOrden()) > 0) {
                List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", actuacion).setParameter("personaFirmante", actuacion.getPreopinante()).setParameter("estado", new Estados("AC")).getResultList();
                if (!lista.isEmpty()) {

                    lista.get(0).setRevisado(false);
                    lista.get(0).setFechaHoraRevisado(null);
                    lista.get(0).setPersonaRevisado(null);

                    personasFirmantesPorActuacionesController.setSelected(lista.get(0));
                    personasFirmantesPorActuacionesController.save(null);

                }
            } else if (Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS.equals(estadoActuacion.getCodigo())) {

                List<ExpPersonasAsociadas> miembro = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();

                if (!miembro.isEmpty()) {

                    List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", actuacion).setParameter("personaFirmante", miembro.get(0).getPersona()).setParameter("estado", new Estados("AC")).getResultList();
                    if (lista.isEmpty()) {
                        JsfUtil.addErrorMessage("No se encuentra persona firmante para el miembro");
                        return "";
                    }

                    lista.get(0).setRevisado(true);
                    lista.get(0).setFechaHoraRevisado(fecha);
                    lista.get(0).setPersonaRevisado(personaUsuario);

                    personasFirmantesPorActuacionesController.setSelected(lista.get(0));
                    personasFirmantesPorActuacionesController.save(null);
                }
            }

            saveFirmantes();

            if (!actuacion.getEstado().equals(estadoActuacion)) {
                /**
                 * * Guardar Cambio estado **
                 */
                ExpCambiosEstadoActuacion cambio = new ExpCambiosEstadoActuacion(null, fecha, actuacion, personaUsuario, personaUsuario, fecha, actuacion.getEstado(), estadoActuacion);
                expCambiosEstadoActuacionController.setSelected(cambio);
                expCambiosEstadoActuacionController.saveNew(null);
            }

            if (!actuacion.getTexto().equals(textoAnterior)) {
                ExpCambiosTextoActuacion cambioTexto = new ExpCambiosTextoActuacion(null, fecha, actuacion, personaUsuario, personaUsuario, fecha, textoAnterior, (actuacion != null) ? actuacion.getTexto() : null);
                expCambiosTextoActuacionController.setSelected(cambioTexto);
                expCambiosTextoActuacionController.saveNew(null);
            }

            actuacion.setEstado(estadoActuacion);

            actuacion.setPersonaUltimoEstado(personaUsuario);
            actuacion.setFechaHoraUltimoEstado(fecha);

            expActuacionesController.setSelected(actuacion);
            expActuacionesController.save(null);
        }

        String retornar;

        if (vieneDePestanas && filtroURL.verifPermiso("redireccionarPestanas")) {
            retornar = "/pages/expActuaciones/index.xhtml?tipo=" + paramPestana + "faces-redirect=true";
        } else {
            // retornar = "index.xhtml?tipo=" + accion + "&act=" + expActuacionesController.getSelected().getId() + "&otro=NO&faces-redirect=true";
            retornar = "index.xhtml?tipo=registrar_actuacion_secretaria&act=" + expActuacionesController.getSelected().getId() + "&otro=NO&faces-redirect=true";
        }

        return retornar;
    }

    public String saveNew() {
        if (actuacion != null) {

            if (actuacion.getTipoActuacion() == null) {
                JsfUtil.addErrorMessage("Debe elegir un tipo de actuacion");
                return "";
            } else if (actuacion.getFormato() == null) {
                JsfUtil.addErrorMessage("Debe elegir un formato");
                return "";
            } else if (actuacion.getSecretario() == null) {
                JsfUtil.addErrorMessage("Debe elegir un secretario para esta actuación");
                return "";
            }

            if (actuacion.getTipoActuacion() != null) {
                if (Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(actuacion.getTipoActuacion().getId())) {
                    if (actuacionDesglose != null) {
                        if (actuacionDesglose.isEmpty()) {
                            JsfUtil.addErrorMessage("Es una providencia de desglose, debe elegir las actuaciones a desglosar");
                            return "";
                        }
                    } else {
                        JsfUtil.addErrorMessage("Es una providencia de desglose, debe elegir las actuaciones a desglosar.");
                        return "";
                    }
                }

                if (Constantes.TIPO_ACTUACION_RESOLUCION.equals(actuacion.getTipoActuacion().getId()) || Constantes.TIPO_ACTUACION_SD.equals(actuacion.getTipoActuacion().getId())) {
                    return saveNewResolucion();
                } else {
                    return saveNewActuacion();
                }
            }
        }
        return "";
    }
    
    private void saveActuacionesDesglose(ExpActuaciones desglose, List<ExpActuaciones> todas, List<ExpActuaciones> acts){
        if(acts != null && todas != null){
            boolean encontro;
            for(ExpActuaciones act : todas){
                encontro = false;
                for(ExpActuaciones a : acts){
                    if(act.equals(a)){
                        encontro = true;
                        break;
                    }
                }
                
                act.setProvidenciaDesglose(encontro?desglose:null);
                expActuacionesController.setSelected(act);
                expActuacionesController.save(null);
            }
        }
    }

    public String saveNewActuacion() {
        if (actuacion != null && getSelected() != null) {

            if (!validarSaveNewActuacion()) {
                return "";

            }

            if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(actuacion.getTipoActuacion().getId())) {
                if (docId != null) {
                    List<DocumentosJudicialesCorte> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudicialesCorte.findById", DocumentosJudicialesCorte.class
                    ).setParameter("id", docId).getResultList();
                    if (!lista.isEmpty()) {
                        actuacion.setDocumentoJudicialCorte(lista.get(0));
                    }
                } else {
                    JsfUtil.addErrorMessage("Debe elegir un expediente corte");
                    return "";
                }
            }

            Date fecha = ejbFacade.getSystemDate();
            actuacion.setFechaHoraAlta(fecha);
            actuacion.setFechaHoraUltimoEstado(fecha);
            /*
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2022);
        myCal.set(Calendar.MONTH, 3);
        myCal.set(Calendar.DAY_OF_MONTH, 11);
        myCal.set(Calendar.HOUR_OF_DAY, 17);
        myCal.set(Calendar.MINUTE, 10);
        myCal.set(Calendar.SECOND, 10);
        Date fechaInicio = myCal.getTime();
        
            actuacion.setFechaPresentacion(generarFechaPresentacion(fechaInicio));
             */
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

            actuacion.setEstado(estadoActuacion);
            /*
            if (actuacionPadre != null) {
                actuacion.setActuacionPadre(actuacionPadre);
            }
             */

            expActuacionesController.setSelected(actuacion);
            expActuacionesController.saveNew(null);

            selectedActuacion = actuacion;

            if (Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE.equals(actuacion.getTipoActuacion().getId())) {
                saveActuacionesDesglose(actuacion, listaActuacionesDesglose, actuacionDesglose);
            }

            saveFirmantes();

            /**
             * * Guardar Cambio estado **
             */
            List<ExpEstadosActuacion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByMenorOrden", ExpEstadosActuacion.class).setParameter("orden", estadoActuacion.getOrden()).getResultList();
            for (ExpEstadosActuacion act : lista) {
                ExpCambiosEstadoActuacion cambio = new ExpCambiosEstadoActuacion(null, fecha, actuacion, personaUsuario, personaUsuario, fecha, null, act);
                expCambiosEstadoActuacionController.setSelected(cambio);
                expCambiosEstadoActuacionController.saveNew(null);
            }

            ExpCambiosTextoActuacion cambioTexto = new ExpCambiosTextoActuacion(null, fecha, actuacion, personaUsuario, personaUsuario, fecha, null, (actuacion != null) ? actuacion.getTexto() : null);
            expCambiosTextoActuacionController.setSelected(cambioTexto);
            expCambiosTextoActuacionController.saveNew(null);

            if (esPrimerEscrito) {
                // actuacionPadre = expActuacionesController.getSelected();
                actuacionPadre = actuacion;
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

                // EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();
                EstadosProcesalesDocumentosJudiciales estadoProc = new EstadosProcesalesDocumentosJudiciales();

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
            /*
            if (!(Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_OFICIO.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_RESOLUCION.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_SD.equals(expActuacionesController.getSelected().getTipoActuacion().getId())
                    || Constantes.TIPO_ACTUACION_NOTIFICACION.equals(expActuacionesController.getSelected().getTipoActuacion().getId()))) {
                String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                        + "     Se a presentado una presentación del tipo " + expActuacionesController.getSelected().getTipoActuacion().getDescripcion() + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);

            }
             */
            personasController.badgePresentacionesPendientes();

            // Providencia no necesita de un segundo documento
            /*
            if (!Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(expActuacionesController.getSelected().getTipoActuacion().getId())) {
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('ExpAcusacionesOtroArchivoDialog').show();");
            }
             */
        }

        // return "index.xhtml?tipo=" + accion + "&otro=SI&faces-redirect=true";
        // String retornar = "index.xhtml?tipo=" + accion + "&act=" + expActuacionesController.getSelected().getId() + "&otro=NO&faces-redirect=true";

        String retornar = "index.xhtml?tipo=" + accion + "&act=" + actuacion.getId() + "&otro=NO&faces-redirect=true";
        return retornar;
    }

    public void saveEditExpediente() {
        if (getSelected() != null) {
            if (getSelected().getAno() == null) {
                JsfUtil.addErrorMessage("Debe ingresar el año");
                buscarActuaciones();
                return;
            } else if ("".equals(getSelected().getAno())) {
                JsfUtil.addErrorMessage("Debe ingresar el año");
                buscarActuaciones();
                return;
            } else if (getSelected().getAno() < 1000) {
                JsfUtil.addErrorMessage("Debe ingresar año de 4 digitos");
                buscarActuaciones();
                return;

            }

            /*
            if (getSelected().getCausa() != null) {
                if (!"".equals(getSelected().getCausa())) {
                    List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", nroCausa).getResultList();
                    if (!listaDocs.isEmpty()) {
                        JsfUtil.addErrorMessage("Ya existe una causa con nro " + getSelected().getCausa());
                        buscarActuaciones();
                        return;
                    }
                }
            }
             */
            if (nroCausaExp != null) {
                if (getSelected().getCausa() != null) {
                    if (!"".equals(nroCausaExp)) {
                        if (!"".equals(getSelected().getCausa())) {
                            if (!nroCausaExp.equals(getSelected().getCausa())) {
                                List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class
                                ).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", nroCausaExp).getResultList();
                                if (!listaDocs.isEmpty()) {
                                    JsfUtil.addErrorMessage("Ya existe una causa con nro " + nroCausaExp);
                                    buscarActuaciones();
                                    return;
                                }
                                getSelected().setCausa(nroCausaExp);
                            }
                        } else {
                            JsfUtil.addErrorMessage("Debe ingresar nro causa");
                            buscarActuaciones();
                            return;
                        }
                    } else {
                        JsfUtil.addErrorMessage("Debe ingresar nro causa");
                        buscarActuaciones();
                        return;
                    }
                } else {
                    JsfUtil.addErrorMessage("Debe ingresar nro causa");
                    buscarActuaciones();
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Debe ingresar nro causa");
                buscarActuaciones();
                return;
            }

            if (getSelected().getCausa() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_nroCausa"));
                buscarActuaciones();
                return;
            } else if ("".equals(getSelected().getCausa())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_nroCausa"));
                buscarActuaciones();
                return;
                /*
            } else {
                List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCausa", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("causa", nroCausaExp).getResultList();
                if (!listaDocs.isEmpty()) {
                    JsfUtil.addErrorMessage("Ya existe una causa con nro " + getSelected().getCausa());
                    buscarActuaciones();
                    return;
                }
                 */
            }

            if (getSelected().getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_caratula"));
                buscarActuaciones();
                return;
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateDocumentosJudicialesHelpText_caratula"));
                buscarActuaciones();
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setPersonaUltimoEstado(personaUsuario);
            getSelected().setFechaHoraUltimoEstado(fecha);

            super.save(null);

            saveRestringidos();

        }
    }

    /*
    public String saveNewExpediente() {

        if (getSelected() != null) {
            try {
                getSelected().setCausa(generarNroCausa());
            } catch (Exception ex) {
                JsfUtil.addErrorMessage("No se pudo generar nro causa");
                return "";
            }

            if (!validarSaveNew()) {
                buscarActuaciones();
                return "";
            }

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return "";
            } else if (file.getContents().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return "";
            }

            Date fecha = ejbFacade.getSystemDate();

            String hash = "";
            try {
                hash = Utils.generarHash(fecha, personaUsuario.getId());;
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                return "";
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy");

            getSelected().setExpedienteViejo(false);
            getSelected().setPersonaAlta(personaUsuario);
            getSelected().setFolios("");
            getSelected().setFechaPresentacion(generarFechaPresentacion(fecha));
            getSelected().setAno(Integer.valueOf(format.format(getSelected().getFechaPresentacion())));
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
                if (file.getContents().length > 0) {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String nombreArchivo = simpleDateFormat.format(fecha);
                    nombreArchivo += "_" + getSelected().getId() + "_" + getSelected().getId() + ".pdf";

                    ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).getSingleResult();

                    guardado = guardarActuacion(file, nombreArchivo, fecha, getSelected(), null, personaUsuario, hash, tipoActuacion, fojas, true, true, personaUsuario.getNombresApellidos(), personaUsuario.getCi() == null ? "" : personaUsuario.getCi(), null, null);
                }
            }

            if (esPrimerEscrito && guardado) {
                actuacionPadre = expActuacionesController.getSelected();
            }
            
            if (!this.isErrorPersistencia()) {

                for (Personas per : listaPersonas) {
                    if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                        
//                        Personas perso = null;
//                        try{
//                            perso = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", per.getId()).getSingleResult();
//                        }catch(Exception e){
//                            
//                        }
//                        
//                        if(perso == null){           
//                            personasController.setSelected(per);
//                            personasController.saveNew(null);
//                        }
                         

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

            }

            buscar();
            //buscarPorFechaAlta();

            //buscarActuaciones();
            //PrimeFaces current = PrimeFaces.current();
            //current.executeScript("PF('ExpAcusacionesOtroArchivoDialog').show();");
            String retornar = "index.xhtml?tipo=" + accion + "&act=" + expActuacionesController.getSelected().getId() + "&otro=SI&faces-redirect=true";
            return retornar;

        }

        return "";
    }
     */
    public String saveNewResolucion() {
        if (resolucion != null) {
            /*
            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un documento");
                return "";
            } else if (file.getContents().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return "";
            }
             */

            if (actuacion == null) {
                JsfUtil.addErrorMessage("Actuacion no puede ser nula");
                return "";
            }

            if (actuacion.getPreopinante() == null) {
                JsfUtil.addErrorMessage("Debe elegir preopinante");
                return "";
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
                return "";
            } else if ("".equals(resolucion.getNroResolucion())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_nroResolucion"));
                return "";
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
                return "";
            }

            if (resolucion.getFecha() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_fecha"));
                return "";
            }

            if (resolucion.getCaratula() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_caratula"));
                return "";
            } else if ("".equals(getSelected().getCaratula())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_caratula"));
                return "";
            }

            if (listaPersonas != null) {
                for (ResuelvePorResolucionesPorPersonas res : listaPersonasResuelve) {
                    if (res.getResuelve() == null) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreateResolucionesHelpText_resuelve"));
                        return "";
                    }
                }
            }

            resolucion.setFojas(actuacion.getFojas());

            resolucionesController.setSelected(resolucion);
            resolucionesController.saveNew(null);

            boolean guardado = false;
            String nombreArchivo = "";

            ExpTiposActuacion tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult();
            /*
            if (file != null) {
                if (file.getContents().length > 0) {

                    String hash = "";
                    try {
                        hash = Utils.generarHash(fechaActual, personaUsuario.getId());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                        return "";
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    nombreArchivo = simpleDateFormat.format(fechaActual);
                    nombreArchivo += "_" + getSelected().getId() + "_" + getSelected().getId() + ".pdf";
             */

            ExpEstadosActuacion est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_EN_PROYECTO).getSingleResult();

            guardado = guardarActuacion(fechaActual, getSelected(), resolucion, personaUsuario, null, tipoActuacion, resolucion.getFojas(), visible, (actuacion != null) ? actuacion.getDescripcion() : null, (actuacion != null) ? actuacion.getTexto() : null, (actuacion != null) ? actuacion.getFormato() : null, (actuacion != null) ? actuacion.getPreopinante() : null, estadoActuacion, (actuacion != null) ? actuacion.getSecretario() : null, actuacion.isUrgente());

            /*
                }
            }
             */
            if (guardado) {
                resolucion.setArchivo(nombreArchivo);
                resolucionesController.setSelected(resolucion);
                resolucionesController.save(null);

                List<ExpEstadosActuacion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByMenorOrden", ExpEstadosActuacion.class
                ).setParameter("orden", estadoActuacion.getOrden()).getResultList();
                for (ExpEstadosActuacion act : lista) {
                    ExpCambiosEstadoActuacion cambio = new ExpCambiosEstadoActuacion(null, fechaActual, expActuacionesController.getSelected(), personaUsuario, personaUsuario, fechaActual, null, act);
                    expCambiosEstadoActuacionController.setSelected(cambio);
                    expCambiosEstadoActuacionController.saveNew(null);
                }

                ExpCambiosTextoActuacion cambioTexto = new ExpCambiosTextoActuacion(null, fechaActual, expActuacionesController.getSelected(), personaUsuario, personaUsuario, fechaActual, null, (expActuacionesController.getSelected() != null) ? expActuacionesController.getSelected().getTexto() : null);
                expCambiosTextoActuacionController.setSelected(cambioTexto);
                expCambiosTextoActuacionController.saveNew(null);
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
                } else {
                    estadoProcesalFinal = "Resolución " + (resolucion.getNroResolucion() != null ? ("Nro " + resolucion.getNroResolucion()) : "") + " en proceso de suscripción";
                }
            } else {
                estadoProcesalFinal = "Resolución " + (resolucion.getNroResolucion() != null ? ("Nro " + resolucion.getNroResolucion()) : "") + " en proceso de suscripción";
            }

            if (expActuacionesController.getSelected().isVisible() && "".equals(estadoProcesalFinal)) {
                estadoProcesalFinal = tipoActuacion.getDescripcion();
            }

            if (!"".equals(estadoProcesalFinal)) {
                //EstadosProcesalesDocumentosJudiciales estadoProc = getSelected().getEstadoProcesalDocumentoJudicial();
                EstadosProcesalesDocumentosJudiciales estadoProc = new EstadosProcesalesDocumentosJudiciales();

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
            /*
            String texto = "<p>Hola " + getSelected().getPersonaAlta().getNombresApellidos() + "<br> "
                    + "     Ha recibido una presentación del tipo " + tipoActuacion + " en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                    + getSelected().getCaratulaString()
                    + "<br><br>"
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + expActuacionesController.getSelected().getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(getSelected().getPersonaAlta().getEmail(), "Nueva Presentación Sistema de Expediente Electrónico JEM", texto);
             */
        }
        return "index.xhtml?tipo=" + accion + "&act=" + expActuacionesController.getSelected().getId() + "&faces-redirect=true";
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
