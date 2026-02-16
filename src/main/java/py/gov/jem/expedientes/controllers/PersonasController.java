package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.Personas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.PrimeFaces;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.CantidadItem;
import py.gov.jem.expedientes.datasource.CantidadLongItem;
import py.gov.jem.expedientes.datasource.EstadoCantidad;
import py.gov.jem.expedientes.datasource.RepAntecedentesDocumentosJudiciales;
import py.gov.jem.expedientes.datasource.RepAntecedentesDocumentosJudicialesRes;
import py.gov.jem.expedientes.models.Antecedentes;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DepartamentosPersona;
import py.gov.jem.expedientes.models.DespachosPersona;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.ExpActasSesion;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpEstadosNotificacion;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.ExpRolesFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.ExpTiposActuacion;
import py.gov.jem.expedientes.models.LocalidadesPersona;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.SesionesLogin;
import py.gov.jem.expedientes.models.TiposDocumentosJudiciales;
import py.gov.jem.expedientes.models.Usuarios;
import py.gov.jem.expedientes.models.VAntecedentesPermisosPersonas;

@Named(value = "personasController")
@ViewScoped
public class PersonasController extends AbstractController<Personas> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private AntecedentesController antecedentesController;
    @Inject
    private DespachosPersonaController despachoController;
    @Inject
    private ExpActuacionesController actuacionesController;
    @Inject
    private ExpActasSesionController actasSesionController;
    @Inject
    private ExpPedidosAntecedenteController pedidosAntecedenteController;
    @Inject
    private PedidosPersonaAutController pedidosPersonaAutController;
    @Inject
    private ExpAvisoOrdenesDiaController expAvisoOrdenesDiaController;
    @Inject
    private ExpPersonasFirmantesPorActuacionesController personasFirmantesPorActuacionesController;
    private LocalidadesPersona localidadPersona;
    private DepartamentosPersona departamentoPersona;
    private HttpSession session;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    private SesionesLoginController sesionesLoginController;
    @Inject
    private ExpNotificacionesController notificacionesController;
    private CanalesEntradaDocumentoJudicial canal;
    private CanalesEntradaDocumentoJudicial canal2;
    private final AntecedentesQuery query = new AntecedentesQuery();

    private String usuario;
    private String contrasena;
    private String contrasena2;
    private String nombreUsu;
    private String cambioContrasena1;
    private String cambioContrasena2;
    private String home;
    private String sesionLogin;

    private String imagenInicio;
    // private String imagenFondo;
    private String imagenLogo;
    private ParametrosSistema par;
    private ParametrosSistema parAdmin;
    private final String borrador = null;
    private final String titulo = "INFORME DE ANTECEDENTES";
    private final boolean imprimirQR = true;
    private final boolean valido = true;
    private Personas per;
    private Usuarios usu;
    private String endpoint;
    private final FiltroURL filtroURL = new FiltroURL();
    private AntecedentesRoles rolElegido;
    private AntecedentesRoles rolEle;
    private List<AntecedentesRoles> listaRolesElegir;
    private Usuarios usuarioLogin = null;
    private Personas personaLogin = null;
    private Integer cantidadPendientesFirma;
    private Integer cantidadPrimer;
    private Integer cantidadPresentacionesPendientes;
    private Integer cantidadParaLaFirma;
    private Integer cantidadPresentacionesPendientesAbogados;
    private Integer cantidadUltimasPresentaciones;
    private Integer cantidadNotificaciones;
    private Integer cantidadRecusaciones;
    private Integer cantidadOrdenesDelDia;
    private Integer cantidadActuacionesEnProyecto;
    private Integer cantidadActuacionesRevisionSecretario;
    private Integer cantidadActuacionesRevisionDirector;
    private Integer cantidadActuacionesRevisionPresidente;
    private Integer cantidadActuacionesFirmaPresidente;
    private Integer cantidadActuacionesAgregarFirmantes;
    private Integer cantidadActuacionesFirmaMiembros;
    private Integer cantidadActuacionesRevisionPresidenteOfProv;
    private Integer cantidadActuacionesFirmaPresidenteOfProv;
    private Integer cantidadActuacionesPendientesPresidenteOfProv;
    private Integer cantidadActuacionesFirmaPresidenteResoluciones;
    private Integer cantidadActuacionesFirmaSecretario;
    private Integer cantidadActuacionesFirmaExSecretario;
    private Integer cantidadActuacionesFinalizadas;
    private Integer cantidadOficiosElectronicos;
    private Integer cantidadCausasUrgentes;
    private Integer cantidadPedidosAntecedente;
    private Integer cantidadPedidosPersona;
    private Long cantidadMesaDeEntradaBandejaEntrada;

    private Integer cantidadActasSesionEnProyecto;
    private Integer cantidadActasSesionRevisionMiembros;
    private Integer cantidadActasSesionFirmaMiembros;
    private Integer cantidadActasSesionFirmaSecretario;
    private Integer cantidadActasSesionFinalizadas;
    private List<TiposDocumentosJudiciales> tiposDoc;

    private String url;
    private String host;
    private boolean expLog;
    private String passOri;

    private Integer menu;
    private String previousPage = null;

    public Integer getCantidadCausasUrgentes() {
        return cantidadCausasUrgentes;
    }

    public void setCantidadCausasUrgentes(Integer cantidadCausasUrgentes) {
        this.cantidadCausasUrgentes = cantidadCausasUrgentes;
    }

    public Long getCantidadMesaDeEntradaBandejaEntrada() {
        return cantidadMesaDeEntradaBandejaEntrada;
    }

    public void setCantidadMesaDeEntradaBandejaEntrada(Long cantidadMesaDeEntradaBandejaEntrada) {
        this.cantidadMesaDeEntradaBandejaEntrada = cantidadMesaDeEntradaBandejaEntrada;
    }

    public Integer getCantidadActuacionesRevisionPresidenteOfProv() {
        return cantidadActuacionesRevisionPresidenteOfProv;
    }

    public void setCantidadActuacionesRevisionPresidenteOfProv(Integer cantidadActuacionesRevisionPresidenteOfProv) {
        this.cantidadActuacionesRevisionPresidenteOfProv = cantidadActuacionesRevisionPresidenteOfProv;
    }

    public Integer getCantidadActuacionesPendientesPresidenteOfProv() {
        return cantidadActuacionesPendientesPresidenteOfProv;
    }

    public void setCantidadActuacionesPendientesPresidenteOfProv(Integer cantidadActuacionesPendientesPresidenteOfProv) {
        this.cantidadActuacionesPendientesPresidenteOfProv = cantidadActuacionesPendientesPresidenteOfProv;
    }

    public Integer getCantidadActuacionesFirmaPresidenteOfProv() {
        return cantidadActuacionesFirmaPresidenteOfProv;
    }

    public void setCantidadActuacionesFirmaPresidenteOfProv(Integer cantidadActuacionesFirmaPresidenteOfProv) {
        this.cantidadActuacionesFirmaPresidenteOfProv = cantidadActuacionesFirmaPresidenteOfProv;
    }

    public Integer getCantidadActuacionesFirmaPresidenteResoluciones() {
        return cantidadActuacionesFirmaPresidenteResoluciones;
    }

    public void setCantidadActuacionesFirmaPresidenteResoluciones(Integer cantidadActuacionesFirmaPresidenteResoluciones) {
        this.cantidadActuacionesFirmaPresidenteResoluciones = cantidadActuacionesFirmaPresidenteResoluciones;
    }

    public Integer getCantidadActasSesionEnProyecto() {
        return cantidadActasSesionEnProyecto;
    }

    public void setCantidadActasSesionEnProyecto(Integer cantidadActasSesionEnProyecto) {
        this.cantidadActasSesionEnProyecto = cantidadActasSesionEnProyecto;
    }

    public Integer getCantidadActasSesionRevisionMiembros() {
        return cantidadActasSesionRevisionMiembros;
    }

    public void setCantidadActasSesionRevisionMiembros(Integer cantidadActasSesionRevisionMiembros) {
        this.cantidadActasSesionRevisionMiembros = cantidadActasSesionRevisionMiembros;
    }

    public Integer getCantidadActasSesionFirmaMiembros() {
        return cantidadActasSesionFirmaMiembros;
    }

    public void setCantidadActasSesionFirmaMiembros(Integer cantidadActasSesionFirmaMiembros) {
        this.cantidadActasSesionFirmaMiembros = cantidadActasSesionFirmaMiembros;
    }

    public Integer getCantidadActasSesionFirmaSecretario() {
        return cantidadActasSesionFirmaSecretario;
    }

    public void setCantidadActasSesionFirmaSecretario(Integer cantidadActasSesionFirmaSecretario) {
        this.cantidadActasSesionFirmaSecretario = cantidadActasSesionFirmaSecretario;
    }

    public Integer getCantidadActasSesionFinalizadas() {
        return cantidadActasSesionFinalizadas;
    }

    public void setCantidadActasSesionFinalizadas(Integer cantidadActasSesionFinalizadas) {
        this.cantidadActasSesionFinalizadas = cantidadActasSesionFinalizadas;
    }

    public String getImagenInicio() throws IOException {
        return imagenInicio;
    }

    public String getImagenLogo() {

        return imagenLogo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public PersonasController() {
        // Inform the Abstract parent controller of the concrete Personas Entity
        super(Personas.class);
    }

    public LocalidadesPersona getLocalidadPersona() {
        return localidadPersona;
    }

    public void setLocalidadPersona(LocalidadesPersona localidadPersona) {
        this.localidadPersona = localidadPersona;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getCambioContrasena1() {
        return cambioContrasena1;
    }

    public void setCambioContrasena1(String cambioContrasena1) {
        this.cambioContrasena1 = cambioContrasena1;
    }

    public String getCambioContrasena2() {
        return cambioContrasena2;
    }

    public void setCambioContrasena2(String cambioContrasena2) {
        this.cambioContrasena2 = cambioContrasena2;
    }

    public AntecedentesRoles getRolElegido() {
        return rolElegido;
    }

    public void setRolElegido(AntecedentesRoles rolElegido) {
        this.rolElegido = rolElegido;
    }

    public List<AntecedentesRoles> getListaRolesElegir() {
        return listaRolesElegir;
    }

    public void setListaRolesElegir(List<AntecedentesRoles> listaRolesElegir) {
        this.listaRolesElegir = listaRolesElegir;
    }

    public Integer getCantidadPendientesFirma() {
        return cantidadPendientesFirma;
    }

    public void setCantidadPendientesFirma(Integer cantidadPendientesFirma) {
        this.cantidadPendientesFirma = cantidadPendientesFirma;
    }

    public Integer getCantidadPrimer() {
        return cantidadPrimer;
    }

    public void setCantidadPrimer(Integer cantidadPrimer) {
        this.cantidadPrimer = cantidadPrimer;
    }

    public Integer getCantidadPresentacionesPendientes() {
        return cantidadPresentacionesPendientes;
    }

    public void setCantidadPresentacionesPendientes(Integer cantidadPresentacionesPendientes) {
        this.cantidadPresentacionesPendientes = cantidadPresentacionesPendientes;
    }

    public Integer getCantidadPresentacionesPendientesAbogados() {
        return cantidadPresentacionesPendientesAbogados;
    }

    public void setCantidadPresentacionesPendientesAbogados(Integer cantidadPresentacionesPendientesAbogados) {
        this.cantidadPresentacionesPendientesAbogados = cantidadPresentacionesPendientesAbogados;
    }

    public Integer getCantidadUltimasPresentaciones() {
        return cantidadUltimasPresentaciones;
    }

    public void setCantidadUltimasPresentaciones(Integer cantidadUltimasPresentaciones) {
        this.cantidadUltimasPresentaciones = cantidadUltimasPresentaciones;
    }

    public Integer getCantidadActuacionesRevisionDirector() {
        return cantidadActuacionesRevisionDirector;
    }

    public void setCantidadActuacionesRevisionDirector(Integer cantidadActuacionesRevisionDirector) {
        this.cantidadActuacionesRevisionDirector = cantidadActuacionesRevisionDirector;
    }

    public Integer getCantidadActuacionesAgregarFirmantes() {
        return cantidadActuacionesAgregarFirmantes;
    }

    public void setCantidadActuacionesAgregarFirmantes(Integer cantidadActuacionesAgregarFirmantes) {
        this.cantidadActuacionesAgregarFirmantes = cantidadActuacionesAgregarFirmantes;
    }

    public Integer getCantidadNotificaciones() {
        return cantidadNotificaciones;
    }

    public void setCantidadNotificaciones(Integer cantidadNotificaciones) {
        this.cantidadNotificaciones = cantidadNotificaciones;
    }

    public Integer getCantidadRecusaciones() {
        return cantidadRecusaciones;
    }

    public void setCantidadRecusaciones(Integer cantidadRecusaciones) {
        this.cantidadRecusaciones = cantidadRecusaciones;
    }

    public Integer getCantidadParaLaFirma() {
        return cantidadParaLaFirma;
    }

    public void setCantidadParaLaFirma(Integer cantidadParaLaFirma) {
        this.cantidadParaLaFirma = cantidadParaLaFirma;
    }

    public Integer getCantidadOrdenesDelDia() {
        return cantidadOrdenesDelDia;
    }

    public void setCantidadOrdenesDelDia(Integer cantidadOrdenesDelDia) {
        this.cantidadOrdenesDelDia = cantidadOrdenesDelDia;
    }

    public Integer getCantidadActuacionesEnProyecto() {
        return cantidadActuacionesEnProyecto;
    }

    public void setCantidadActuacionesEnProyecto(Integer cantidadActuacionesEnProyecto) {
        this.cantidadActuacionesEnProyecto = cantidadActuacionesEnProyecto;
    }

    public Integer getCantidadActuacionesRevisionSecretario() {
        return cantidadActuacionesRevisionSecretario;
    }

    public void setCantidadActuacionesRevisionSecretario(Integer cantidadActuacionesRevisionSecretario) {
        this.cantidadActuacionesRevisionSecretario = cantidadActuacionesRevisionSecretario;
    }

    public Integer getCantidadActuacionesRevisionPresidente() {
        return cantidadActuacionesRevisionPresidente;
    }

    public void setCantidadActuacionesRevisionPresidente(Integer cantidadActuacionesRevisionPresidente) {
        this.cantidadActuacionesRevisionPresidente = cantidadActuacionesRevisionPresidente;
    }

    public Integer getCantidadActuacionesFirmaPresidente() {
        return cantidadActuacionesFirmaPresidente;
    }

    public void setCantidadActuacionesFirmaPresidente(Integer cantidadActuacionesFirmaPresidente) {
        this.cantidadActuacionesFirmaPresidente = cantidadActuacionesFirmaPresidente;
    }

    public Integer getCantidadActuacionesFirmaMiembros() {
        return cantidadActuacionesFirmaMiembros;
    }

    public void setCantidadActuacionesFirmaMiembros(Integer cantidadActuacionesFirmaMiembros) {
        this.cantidadActuacionesFirmaMiembros = cantidadActuacionesFirmaMiembros;
    }

    public Integer getCantidadActuacionesFirmaSecretario() {
        return cantidadActuacionesFirmaSecretario;
    }

    public void setCantidadActuacionesFirmaSecretario(Integer cantidadActuacionesFirmaSecretario) {
        this.cantidadActuacionesFirmaSecretario = cantidadActuacionesFirmaSecretario;
    }

    public Integer getCantidadActuacionesFirmaExSecretario() {
        return cantidadActuacionesFirmaExSecretario;
    }

    public void setCantidadActuacionesFirmaExSecretario(Integer cantidadActuacionesFirmaExSecretario) {
        this.cantidadActuacionesFirmaExSecretario = cantidadActuacionesFirmaExSecretario;
    }

    public Integer getCantidadActuacionesFinalizadas() {
        return cantidadActuacionesFinalizadas;
    }

    public void setCantidadActuacionesFinalizadas(Integer cantidadActuacionesFinalizadas) {
        this.cantidadActuacionesFinalizadas = cantidadActuacionesFinalizadas;
    }

    public Integer getCantidadOficiosElectronicos() {
        return cantidadOficiosElectronicos;
    }

    public void setCantidadOficiosElectronicos(Integer cantidadOficiosElectronicos) {
        this.cantidadOficiosElectronicos = cantidadOficiosElectronicos;
    }

    public Integer getCantidadPedidosAntecedente() {
        return cantidadPedidosAntecedente;
    }

    public void setCantidadPedidosAntecedente(Integer cantidadPedidosAntecedente) {
        this.cantidadPedidosAntecedente = cantidadPedidosAntecedente;
    }

    public Integer getCantidadPedidosPersona() {
        return cantidadPedidosPersona;
    }

    public void setCantidadPedidosPersona(Integer cantidadPedidosPersona) {
        this.cantidadPedidosPersona = cantidadPedidosPersona;
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String act = params.get("act");
        String accion = params.get("tipo");
        String otro = params.get("otro");
        String solicitar = params.get("solicitar");
        sesionLogin = params.get("id");

        TiposDocumentosJudiciales tipoDoc = new TiposDocumentosJudiciales();
        tipoDoc.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_AD);
        TiposDocumentosJudiciales tipoDoc2 = new TiposDocumentosJudiciales();
        tipoDoc2.setCodigo(Constantes.TIPO_DOCUMENTO_JUDICIAL_JU);

        tiposDoc = new ArrayList<>();
        tiposDoc.add(tipoDoc);
        tiposDoc.add(tipoDoc2);

        usuario = params.get("usuario");
        contrasena = params.get("contrasena");
        contrasena2 = params.get("contrasena");

        // System.out.println("Usuario: " + (usuario==null?"":usuario) + ", contrasena: " + (contrasena==null?"":contrasena));
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String[] array1 = url.split(":");
        if (array1.length > 2) {
            host = array1[0] + array1[1];
        } else {
            host = "";
        }
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        if (solicitar != null) {
            if (Constantes.SI.equals(solicitar)) {
                try {
                    session.invalidate();
                    session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/pedidosPersonaExpediente/altaPersona.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(PersonasController.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            }
        }

        Object appObj = session.getAttribute("esApp");

        boolean esApp = (appObj == null) ? false : (boolean) appObj;

        if (uri.lastIndexOf("login.xhtml") > -1 && esApp) {

            try {
                session.invalidate();
                session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/login.xhtml?usuario=" + usuario + "&contrasena=" + contrasena);

                return;
            } catch (IOException ex) {
                Logger.getLogger(PersonasController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }

        }

        passOri = (String) session.getAttribute("passOri");
        per = (Personas) session.getAttribute("Persona");
        usu = (Usuarios) session.getAttribute("Usuarios");
        rolEle = (AntecedentesRoles) session.getAttribute("RolElegido");

        expLog = session.getAttribute("expLog") == null ? false : (boolean) session.getAttribute("expLog");

        if (act != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", act);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipo", accion);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("otro", otro);
        }

        if (per != null) {
            String actua = (String) session.getAttribute("actuacionId");
            String acc = (String) session.getAttribute("tipo");
            if (actua != null) {
                if (Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA.equals(acc)) {
                    navigateExpedientes(act, 2);

                } else if (Constantes.ACCION_CONSULTA.equals(acc)) {
                    navigateExpedientes(act, 1);
                }

            }

        }

        setSelected(per);
        // canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);

        canal = new CanalesEntradaDocumentoJudicial();
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_SE);

        canal2 = new CanalesEntradaDocumentoJudicial();
        canal2.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_ME);

        try {
            par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

            parAdmin = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_GESTION).getSingleResult();

            File file = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaRecursos());
            boolean bool = file.mkdir();
            imagenInicio = url + "/" + par.getRutaRecursos() + "/" + "imagen_inicio.jpg";
            imagenLogo = url + "/" + par.getRutaRecursos() + "/" + "imagen_logo.jpg";
        } catch (Exception ex) {
            ex.printStackTrace();
            imagenInicio = "";
            // imagenFondo = "";
            imagenLogo = "";
        }

        if (per != null) {
            String usuDescripcion = " - (" + ((per.getUsuario() == null) ? (usu != null ? usu.getUsuario() : "") : per.getUsuario()) + ")";
            nombreUsu = per.getNombresApellidos() + usuDescripcion;
        }

        Calendar fecha1;

        fecha1 = Calendar.getInstance();

        setItems(this.ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList());

        Utils.timeStamp("Personas: ", fecha1, Calendar.getInstance());

        if (rolEle != null && per != null) {
            ExpEstadosNotificacion estadoNL = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosNotificacion.findByCodigo", ExpEstadosNotificacion.class).setParameter("codigo", Constantes.ESTADO_NOTIFICACION_NO_LEIDO).getSingleResult();

            ExpTiposActuacion tipoActuacion = null;
            if (!deshabilitarMenues("nuevasCausas")) {
                fecha1 = Calendar.getInstance();
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PRIMER_ESCRITO).getSingleResult();

                Utils.timeStamp("Tipos Actuacion: ", fecha1, Calendar.getInstance());
                fecha1 = Calendar.getInstance();
                cantidadPrimer = notificacionesController.obtenerNotificaciones(tipoActuacion, estadoNL, per, rolEle).size();

                Utils.timeStamp("obtenerNotificaciones: ", fecha1, Calendar.getInstance());
                if (cantidadPrimer == 0) {
                    cantidadPrimer = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "NOTIFICACIONES")) {
                //tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_NOTIFICACION).getSingleResult();
                //cantidadNotificaciones = notificacionesController.obtenerNotificaciones(tipoActuacion, estadoNL, per, rolEle).size();

                fecha1 = Calendar.getInstance();

                cantidadNotificaciones = actuacionesController.obtenerNotificaciones(true).size();
                Utils.timeStamp("obtenerNotificaciones2: ", fecha1, Calendar.getInstance());
                if (cantidadNotificaciones == 0) {
                    cantidadNotificaciones = null;
                }
            }

            if (!deshabilitarMenues("recusaciones")) {
                tipoActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RECUSACION).getSingleResult();

                fecha1 = Calendar.getInstance();
                cantidadRecusaciones = notificacionesController.obtenerNotificaciones(tipoActuacion, estadoNL, per, rolEle).size();

                Utils.timeStamp("obtenerNotificaciones3: ", fecha1, Calendar.getInstance());
                if (cantidadRecusaciones == 0) {
                    cantidadRecusaciones = null;
                }
            }

            if (!deshabilitarPendientesFirma()) {

                fecha1 = Calendar.getInstance();
                cantidadPendientesFirma = badgePendientesFirma(per, rolEle);
                Utils.timeStamp("badgePendientesFirma: ", fecha1, Calendar.getInstance());
                if (cantidadPendientesFirma == 0) {
                    cantidadPendientesFirma = null;
                }
            }

            if (!deshabilitarMenues("/pages/expEntradaDocumentosJudicialesPorSecretaria/index.xhtml", "CONSULTA")) {

                fecha1 = Calendar.getInstance();
                cantidadCausasUrgentes = obtenerCantidadEnjuiciamientos();

                Utils.timeStamp("obtenerCantidadCausasUrgentes: ", fecha1, Calendar.getInstance());
                if (cantidadCausasUrgentes == 0) {
                    cantidadCausasUrgentes = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PENDIENTES_FIRMA")) {

                fecha1 = Calendar.getInstance();
                cantidadParaLaFirma = actuacionesController.obtenerParaLaFirma().size();
                Utils.timeStamp("obtenerParaLaFirma: ", fecha1, Calendar.getInstance());
                if (cantidadParaLaFirma == 0) {
                    cantidadParaLaFirma = null;
                }
            }

            if (!deshabilitarMenues("/pages/expVerOrdenesDia/index.xhtml")) {
                Date fecha = ejbFacade.getSystemDateOnly();

                fecha1 = Calendar.getInstance();
                cantidadOrdenesDelDia = expAvisoOrdenesDiaController.obtenerOrdenesDelDiaPend(fecha).size();
                Utils.timeStamp("obtenerOrdenesDelDiaPend: ", fecha1, Calendar.getInstance());
                if (cantidadOrdenesDelDia == 0) {
                    cantidadOrdenesDelDia = null;
                }
            }

            /*
            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PENDIENTES_ABOGADO") || !deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PENDIENTES")) {

                fecha1 = Calendar.getInstance();
                badgePresentacionesPendientes();
                Utils.timeStamp("badgePresentacionesPendientes: ", fecha1, Calendar.getInstance());
            }
            */
            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PENDIENTES_ABOGADO") || !deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PENDIENTES")) {

                fecha1 = Calendar.getInstance();
                badgePresentacionesPendientesNew();
                Utils.timeStamp("badgePresentacionesPendientesNew: ", fecha1, Calendar.getInstance());
            }
             
            
            Personas personaSelected = null;
            Personas personaSelected2 = null;

            boolean esRelator = false;

            if (Constantes.ROL_RELATOR.equals(rolEle.getId())
                    || Constantes.ROL_RELATOR_PRESIDENTE.equals(rolEle.getId())
                    || Constantes.ROL_RELATOR_PRESIDENTE_RES_ACTAS.equals(rolEle.getId())
                    || Constantes.ROL_RELATOR_PRESIDENTE_OF_PROV_RES_ACTAS.equals(rolEle.getId())
                    || Constantes.ROL_RELATOR_EXMIEMBRO.equals(rolEle.getId())
                    || Constantes.ROL_RELATOR_MIEMBRO_CON_PERMISO.equals(rolEle.getId())
                    || Constantes.ROL_RELATOR_MIEMBRO_SUPLENTE.equals(rolEle.getId())
                    || Constantes.ROL_RELATOR_MIEMBRO_REEMPLAZANTE.equals(rolEle.getId())) {

                fecha1 = Calendar.getInstance();
                List<ExpPersonasAsociadas> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", per.getId()).getResultList();

                Utils.timeStamp("Personas asociadas: ", fecha1, Calendar.getInstance());
                if (!lista.isEmpty()) {
                    personaSelected = lista.get(0).getPersona();
                    personaSelected2 = lista.get(0).getPersona();
                    esRelator = true;
                } else {
                    personaSelected = per;
                    personaSelected2 = per;
                }
            } else if (Constantes.ROL_MIEMBRO.equals(rolEle.getId()) || Constantes.ROL_EXMIEMBRO.equals(rolEle.getId()) || Constantes.ROL_MIEMBRO_CON_PERMISO.equals(rolEle.getId()) || Constantes.ROL_MIEMBRO_SUPLENTE.equals(rolEle.getId()) || Constantes.ROL_MIEMBRO_REEMPLAZANTE.equals(rolEle.getId()) || Constantes.ROL_PRESIDENTE.equals(rolEle.getId()) || Constantes.ROL_EXSECRETARIO.equals(rolEle.getId())) {
                personaSelected = per;
                personaSelected2 = per;
            } else {
                personaSelected = null;
                personaSelected2 = per;
            }

            List<String> listaEstados = new ArrayList<>();

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PROYECTO")) {
                listaEstados.add(Constantes.ESTADO_ACTUACION_EN_PROYECTO);
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "REVISION_SECRETARIO")) {
                listaEstados.add(Constantes.ESTADO_ACTUACION_REVISION_SECRETARIO);
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "REVISION_DIRECTOR")) {
                listaEstados.add(Constantes.ESTADO_ACTUACION_REVISION_DIRECTOR);
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "AGREGAR_FIRMANTES")) {
                listaEstados.add(Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES);
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_SECRETARIO")) {
                listaEstados.add(Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO);
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FINALIZADAS")) {
                listaEstados.add(Constantes.ESTADO_ACTUACION_FINALIZADA);
            }

            if (!listaEstados.isEmpty()) {
                fecha1 = Calendar.getInstance();

                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                List<EstadoCantidad> todas = actuacionesController.obtenerCantidadActuacionesEstadoTodos(listaEstados);

                Utils.timeStamp("obtenerCantidadActuacionesEstadoTodos: ", fecha1, Calendar.getInstance());
                cantidadActuacionesEnProyecto = 0;
                cantidadActuacionesRevisionSecretario = 0;
                cantidadActuacionesRevisionDirector = 0;
                cantidadActuacionesAgregarFirmantes = 0;
                cantidadActuacionesFirmaSecretario = 0;
                cantidadActuacionesFinalizadas = 0;

                if (todas != null) {
                    for (EstadoCantidad actu : todas) {
                        if (null != actu.getEstado()) {
                            switch (actu.getEstado()) {
                                case Constantes.ESTADO_ACTUACION_EN_PROYECTO:
                                    cantidadActuacionesEnProyecto += actu.getCantidad();
                                    break;
                                case Constantes.ESTADO_ACTUACION_REVISION_SECRETARIO:
                                    cantidadActuacionesRevisionSecretario += actu.getCantidad();
                                    break;
                                case Constantes.ESTADO_ACTUACION_REVISION_DIRECTOR:
                                    cantidadActuacionesRevisionDirector += actu.getCantidad();
                                    break;
                                case Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES:
                                    cantidadActuacionesAgregarFirmantes += actu.getCantidad();
                                    break;
                                case Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO:
                                    cantidadActuacionesFirmaSecretario += actu.getCantidad();
                                    break;
                                case Constantes.ESTADO_ACTUACION_FINALIZADA:
                                    cantidadActuacionesFinalizadas += actu.getCantidad();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                
                if (cantidadActuacionesEnProyecto == 0) {
                    cantidadActuacionesEnProyecto = null;
                }

                if (cantidadActuacionesRevisionSecretario == 0) {
                    cantidadActuacionesRevisionSecretario = null;
                }

                if (cantidadActuacionesRevisionDirector == 0) {
                    cantidadActuacionesRevisionDirector = null;
                }

                if (cantidadActuacionesAgregarFirmantes == 0) {
                    cantidadActuacionesAgregarFirmantes = null;
                }

                if (cantidadActuacionesFirmaSecretario == 0) {
                    cantidadActuacionesFirmaSecretario = null;
                }

                if (cantidadActuacionesFinalizadas == 0) {
                    cantidadActuacionesFinalizadas = null;
                }

            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "REVISION_PRESIDENTE")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesRevisionPresidente = actuacionesController.obtenerCantidadActuacionesEstadoPreopinante(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE, personaSelected).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPreopinante1: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesRevisionPresidente == 0) {
                    cantidadActuacionesRevisionPresidente = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_PRESIDENTE")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesFirmaPresidente = actuacionesController.obtenerCantidadActuacionesEstadoPreopinante(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE, personaSelected).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPreopinante2: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesFirmaPresidente == 0) {
                    cantidadActuacionesFirmaPresidente = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_MIEMBROS")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesFirmaMiembros = actuacionesController.obtenerCantidadActuacionesEstadoPreopinante(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS, personaSelected).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPreopinante3: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesFirmaMiembros == 0) {
                    cantidadActuacionesFirmaMiembros = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_EXSECRETARIO")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesFirmaExSecretario = actuacionesController.obtenerCantidadActuacionesEstadoPreopinante(Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO, personaSelected).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPreopinante4: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesFirmaExSecretario == 0) {
                    cantidadActuacionesFirmaExSecretario = null;
                }
            }

            /*
            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PROYECTO")){
                cantidadActuacionesEnProyecto = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_EN_PROYECTO, personaSelected, true, true, true).size();
                if (cantidadActuacionesEnProyecto == 0) {
                    cantidadActuacionesEnProyecto = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "REVISION_SECRETARIO")){
                cantidadActuacionesRevisionSecretario = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_REVISION_SECRETARIO, personaSelected, true, true, true).size();
                if (cantidadActuacionesRevisionSecretario == 0) {
                    cantidadActuacionesRevisionSecretario = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "REVISION_DIRECTOR")){
                cantidadActuacionesRevisionDirector = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_REVISION_DIRECTOR, personaSelected, true, true, true).size();
                if (cantidadActuacionesRevisionDirector == 0) {
                    cantidadActuacionesRevisionDirector = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "REVISION_PRESIDENTE")){
                cantidadActuacionesRevisionPresidente = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE, personaSelected, true, true, true).size();
                if (cantidadActuacionesRevisionPresidente == 0) {
                    cantidadActuacionesRevisionPresidente = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_PRESIDENTE")){
                cantidadActuacionesFirmaPresidente = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE, personaSelected, true, true, true).size();
                if (cantidadActuacionesFirmaPresidente == 0) {
                    cantidadActuacionesFirmaPresidente = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "AGREGAR_FIRMANTES")){
                cantidadActuacionesAgregarFirmantes = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES, personaSelected, true, true, true).size();
                if (cantidadActuacionesAgregarFirmantes == 0) {
                    cantidadActuacionesAgregarFirmantes = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_MIEMBROS")){
                cantidadActuacionesFirmaMiembros = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS, personaSelected, true, true, true).size();
                if (cantidadActuacionesFirmaMiembros == 0) {
                    cantidadActuacionesFirmaMiembros = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_SECRETARIO")){
                cantidadActuacionesFirmaSecretario = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO, personaSelected, true, true, true).size();
                if (cantidadActuacionesFirmaSecretario == 0) {
                    cantidadActuacionesFirmaSecretario = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_EXSECRETARIO")){
                cantidadActuacionesFirmaExSecretario = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO, personaSelected, true, true, true).size();
                if (cantidadActuacionesFirmaExSecretario == 0) {
                    cantidadActuacionesFirmaExSecretario = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FINALIZADAS")){
                cantidadActuacionesFinalizadas = actuacionesController.obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FINALIZADA, personaSelected, true, true, true).size();
                if (cantidadActuacionesFinalizadas == 0) {
                    cantidadActuacionesFinalizadas = null;
                }
            }
             */
            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "REVISION_PRESIDENTE_OF_PROV")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesRevisionPresidenteOfProv = actuacionesController.obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE, personaSelected, true, true, true).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPresidente1: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesRevisionPresidenteOfProv == 0) {
                    cantidadActuacionesRevisionPresidenteOfProv = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_PRESIDENTE_OF_PROV")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesFirmaPresidenteOfProv = actuacionesController.obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE, personaSelected, true, true, true, Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV, false, esRelator).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPresidente2: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesFirmaPresidenteOfProv == 0) {
                    cantidadActuacionesFirmaPresidenteOfProv = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "PENDIENTES_PRESIDENTE_OF_PROV")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesPendientesPresidenteOfProv = actuacionesController.obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE, personaSelected, true, true, true, Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV, true, esRelator).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPresidente3: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesPendientesPresidenteOfProv == 0) {
                    cantidadActuacionesPendientesPresidenteOfProv = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "FIRMA_PRESIDENTE_RESOLUCIONES")) {

                fecha1 = Calendar.getInstance();
                cantidadActuacionesFirmaPresidenteResoluciones = actuacionesController.obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS, personaSelected, true, true, true).size();

                Utils.timeStamp("obtenerCantidadActuacionesEstadoPresidente4: ", fecha1, Calendar.getInstance());
                if (cantidadActuacionesFirmaPresidenteResoluciones == 0) {
                    cantidadActuacionesFirmaPresidenteResoluciones = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActuaciones/index.xhtml", "OFICIO_ELECTRONICO")) {

                fecha1 = Calendar.getInstance();
                cantidadOficiosElectronicos = actuacionesController.obtenerOficiosElectronicos(true).size();
                Utils.timeStamp("obtenerOficiosElectronicos: ", fecha1, Calendar.getInstance());
                if (cantidadOficiosElectronicos == 0) {
                    cantidadOficiosElectronicos = null;
                }
            }

            listaEstados = new ArrayList<>();
            if (!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "PROYECTO")) {
                listaEstados.add(Constantes.ESTADO_ACTA_SESION_EN_PROYECTO);
            }

            if (!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "FIRMA_SECRETARIO")) {
                listaEstados.add(Constantes.ESTADO_ACTA_SESION_FIRMA_SECRETARIO);
            }

            if (!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "FINALIZADAS")) {
                listaEstados.add(Constantes.ESTADO_ACTA_SESION_FINALIZADA);
            }

            if (!listaEstados.isEmpty()) {

                fecha1 = Calendar.getInstance();
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                List<EstadoCantidad> todas = actasSesionController.obtenerCantidadActasSesionEstadoTodos(listaEstados);

                Utils.timeStamp("obtenerCantidadActasSesionEstadoTodos: ", fecha1, Calendar.getInstance());
                cantidadActasSesionEnProyecto = 0;
                cantidadActasSesionFirmaSecretario = 0;
                cantidadActasSesionFinalizadas = 0;

                if (todas != null) {
                    for (EstadoCantidad actu : todas) {
                        if (null != actu.getEstado()) {
                            switch (actu.getEstado()) {
                                case Constantes.ESTADO_ACTA_SESION_EN_PROYECTO:
                                    cantidadActasSesionEnProyecto += actu.getCantidad();
                                    break;
                                case Constantes.ESTADO_ACTA_SESION_FIRMA_SECRETARIO:
                                    cantidadActasSesionFirmaSecretario += actu.getCantidad();
                                    break;
                                case Constantes.ESTADO_ACTA_SESION_FINALIZADA:
                                    cantidadActasSesionFinalizadas += actu.getCantidad();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

                if (cantidadActasSesionEnProyecto == 0) {
                    cantidadActasSesionEnProyecto = null;
                }

                if (cantidadActasSesionFirmaSecretario == 0) {
                    cantidadActasSesionFirmaSecretario = null;
                }

                if (cantidadActasSesionFinalizadas == 0) {
                    cantidadActasSesionFinalizadas = null;
                }
            }

            /*
            if(!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "PROYECTO")){
                cantidadActasSesionEnProyecto = actasSesionController.obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_EN_PROYECTO, personaSelected2).size();
                if (cantidadActasSesionEnProyecto == 0) {
                    cantidadActasSesionEnProyecto = null;
                }
            }
            
            if(!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "FIRMA_SECRETARIO")){
                cantidadActasSesionFirmaSecretario = actasSesionController.obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_FIRMA_SECRETARIO, personaSelected2).size();
                if (cantidadActasSesionFirmaSecretario == 0) {
                    cantidadActasSesionFirmaSecretario = null;
                }
            }

            if(!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "FINALIZADAS")){
                cantidadActasSesionFinalizadas = actasSesionController.obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_FINALIZADA, personaSelected2).size();
                if (cantidadActasSesionFinalizadas == 0) {
                    cantidadActasSesionFinalizadas = null;
                }
            }
             */
            if (!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "REVISION_MIEMBROS")) {

                fecha1 = Calendar.getInstance();
                cantidadActasSesionRevisionMiembros = actasSesionController.obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_REVISION_MIEMBROS, personaSelected2).size();

                Utils.timeStamp("obtenerCantidadActasSesionEstado1: ", fecha1, Calendar.getInstance());
                if (cantidadActasSesionRevisionMiembros == 0) {
                    cantidadActasSesionRevisionMiembros = null;
                }
            }

            if (!deshabilitarMenues("/pages/expActasSesion/index.xhtml", "FIRMA_MIEMBROS")) {

                fecha1 = Calendar.getInstance();
                cantidadActasSesionFirmaMiembros = actasSesionController.obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_FIRMA_MIEMBROS, personaSelected2).size();

                Utils.timeStamp("obtenerCantidadActasSesionEstado2: ", fecha1, Calendar.getInstance());
                if (cantidadActasSesionFirmaMiembros == 0) {
                    cantidadActasSesionFirmaMiembros = null;
                }
            }

            if (!deshabilitarMenues("/pages/pedidosAntecedente/index.xhtml")) {

                fecha1 = Calendar.getInstance();
                cantidadPedidosAntecedente = pedidosAntecedenteController.cantidadPedidosAntecedente();

                Utils.timeStamp("cantidadPedidosAntecedente: ", fecha1, Calendar.getInstance());
                if (cantidadPedidosAntecedente == 0) {
                    cantidadPedidosAntecedente = null;
                }
            }

            if (!deshabilitarMenues("/pages/autPedidosPersona/index.xhtml")) {

                fecha1 = Calendar.getInstance();
                cantidadPedidosPersona = pedidosPersonaAutController.cantidadPedidosPersona();
                Utils.timeStamp("cantidadPedidosPersona: ", fecha1, Calendar.getInstance());
                if (cantidadPedidosPersona == 0) {
                    cantidadPedidosPersona = null;
                }
            }

            if (filtroURL.verifPermiso("sistemaAdministrativo")) {

                fecha1 = Calendar.getInstance();
                cantidadMesaDeEntradaBandejaEntrada = buscarPorFechaAltaCount();
                Utils.timeStamp("buscarPorFechaAltaCount: ", fecha1, Calendar.getInstance());
                if (cantidadMesaDeEntradaBandejaEntrada == 0) {
                    cantidadMesaDeEntradaBandejaEntrada = null;
                }
            }

            menu = 1;

        }

    }
    
    private int obtenerCantidadEnjuiciamientos(){
        // CantidadItem cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from documentos_judiciales where canal_entrada_documento_judicial = 'EE' and (case when fecha_inicio_enjuiciamiento is null then 0 else DATEDIFF(current_date,fecha_inicio_enjuiciamiento) end ) > 120", CantidadItem.class).getSingleResult();
        CantidadItem cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from documentos_judiciales where canal_entrada_documento_judicial = 'EE' and estado_proceso_expediente_electronico not in ('ET', 'AR')", CantidadItem.class).getSingleResult();
        return cant.getCantidad();
    }

    public boolean deshabilitarPendientesFirma() {

        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_BANDEJA_PENDIENTES_FIRMA, rolEle.getId())) {
            // || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())
            // || filtroURL.verifPermiso(Constantes.PERMISO_FIRMAR, rolElegido.getId())){
            // || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            // return (getItems2() == null)?true:(getItems2().isEmpty());

            return false;
        }

        return true;
    }

    public boolean deshabilitarMultiFirma() {

        if ((filtroURL.verifPermiso(Constantes.PERMISO_FIRMAR, rolEle.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolEle.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolEle.getId()))
                && filtroURL.verifPermiso(Constantes.PERMISO_MULTIFIRMA, rolEle.getId())) {
            // return (getItems2() == null)?true:(getItems2().isEmpty());
            return false;
        }

        return true;
    }

    public Long buscarPorFechaAltaCount() {
        if (usu != null) {
            Calendar myCal = Calendar.getInstance();
            myCal.set(Calendar.YEAR, 2022);
            myCal.set(Calendar.MONTH, 0);
            myCal.set(Calendar.DAY_OF_MONTH, 1);

            Date fechaAltaDesde = myCal.getTime();

            Date nuevaFechaHasta = ejbFacade.getSystemDateOnly();
            Calendar cal = Calendar.getInstance();
            cal.setTime(nuevaFechaHasta);
            cal.add(Calendar.DATE, 1);
            nuevaFechaHasta = cal.getTime();

            Long lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedFechaAltaAsignadoCount2", Long.class).setParameter("fechaDesde", fechaAltaDesde).setParameter("fechaHasta", nuevaFechaHasta).setParameter("canalEntradaDocumentoJudicial", canal2).setParameter("tiposDocumentoJudicial", tiposDoc).setParameter("departamento", usu.getDepartamento()).setParameter("tipo", Constantes.TIPO_ESTADO_DOCUMENTO_AR).getSingleResult();

            return lista;
        } else {
            return 0L;
        }
    }

    public String volver() {
        return "/" + endpoint + "/faces/login.xhtml";
    }

    public List<ExpPersonasFirmantesPorActuaciones> obtenerPersonasFirmantes(Personas per, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios, boolean incluirOtros, boolean incluirSoloRevisados) {
        // return ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", per).setParameter("firmado", false).getResultList();
        /*
        List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", per).setParameter("firmado", false).getResultList();
        List<ExpPersonasAsociadas> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", per.getId()).getResultList();
        for(ExpPersonasAsociadas perAso : lista2){
            List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", perAso.getPersona()).setParameter("firmado", false).getResultList();
            lista.addAll(lista3);
        }
        
        return lista;
         */

        String incluir = "";
        String incluirMas = "";
        String soloRevisados = "";

        if (incluirSoloRevisados) {
            soloRevisados += " and p.revisado = true ";
        }

        if (incluirResoluciones) {
            incluir += Constantes.TIPO_ACTUACION_RESOLUCION;
        }

        if (incluirProvidencias) {
            if (!"".equals(incluir)) {
                incluir += ", ";
            }
            incluir += Constantes.TIPO_ACTUACION_PROVIDENCIA;
        }

        if (incluirOficios) {
            if (!"".equals(incluir)) {
                incluir += ", ";
            }
            incluir += Constantes.TIPO_ACTUACION_OFICIO + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE;
        }

        if (!"".equals(incluir)) {
            incluir = " a.tipo_actuacion in (" + incluir + ")";
        }

        if (incluirOtros) {
            incluirMas = " a.tipo_actuacion not in (" + Constantes.TIPO_ACTUACION_RESOLUCION + ", " + Constantes.TIPO_ACTUACION_PROVIDENCIA + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE + ", " + Constantes.TIPO_ACTUACION_OFICIO + ")";
        }

        // String comando = "select p.* from exp_personas_firmantes_por_actuaciones as p, exp_actuaciones as a where p.actuacion = a.id " + (("".equals(incluir) && ("".equals(incluirMas)) ? " and 1 = 0 " : ((!"".equals(incluir) || (!"".equals(incluirMas))) ? " and (" : "") + incluir + ((!"".equals(incluir) && (!"".equals(incluirMas))) ? " or " : "") + incluirMas + ((!"".equals(incluir) || (!"".equals(incluirMas))) ? ")" : ""))) + " and p.firmado = ?1 and p.estado = 'AC' and (p.persona_firmante = ?2 or p.persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) order by p.fecha_hora_alta asc";
        String comando = "select p.* from exp_personas_firmantes_por_actuaciones as p, exp_actuaciones as a where a.formato is null " + soloRevisados + " and p.actuacion = a.id " + (("".equals(incluir) && ("".equals(incluirMas)) ? " and 1 = 0 " : ((!"".equals(incluir) || (!"".equals(incluirMas))) ? " and (" : "") + incluir + ((!"".equals(incluir) && (!"".equals(incluirMas))) ? " or " : "") + incluirMas + ((!"".equals(incluir) || (!"".equals(incluirMas))) ? ")" : ""))) + " and p.firmado = ?1 and p.estado = 'AC' and (p.persona_firmante = ?2 or p.persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) and (ifnull(a.preopinante,?4) = ?5 or (select ifnull(f.firmado,true) from exp_personas_firmantes_por_actuaciones as f where f.actuacion = p.actuacion and f.persona_firmante = a.preopinante and f.estado = 'AC'))  order by p.fecha_hora_alta asc";
        return ejbFacade.getEntityManager().createNativeQuery(comando, ExpPersonasFirmantesPorActuaciones.class).setParameter(1, false).setParameter(2, per.getId()).setParameter(3, per.getId()).setParameter(4, per.getId()).setParameter(5, per.getId()).getResultList();
        // return ejbFacade.getEntityManager().createNativeQuery("select p.* from exp_personas_firmantes_por_actuaciones as p where firmado = ?1 And estado = 'AC' and (persona_firmante = ?2 or persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) order by p.fecha_hora_alta desc", ExpPersonasFirmantesPorActuaciones.class).setParameter(1, false).setParameter(2, per.getId()).setParameter(3, per.getId()).getResultList();
    }

    private Integer badgePendientesFirma(Personas per, AntecedentesRoles rol) {

        List<ExpPersonasFirmantesPorActuaciones> lista3 = obtenerPersonasFirmantes(per, true, true, true, true, false);

        // List<ExpRolesFirmantesPorActuaciones> lista2 = personasFirmantesPorActuacionesController.obtenerRolesFirmantes(rol, true, true, true, true);
        // return lista3.size() + lista2.size();
        return lista3.size();

    }

    public void badgeOrdenesDelDia() {
        Date fecha = ejbFacade.getSystemDate();
        cantidadOrdenesDelDia = expAvisoOrdenesDiaController.obtenerOrdenesDelDiaPend(fecha).size();
    }

    public void badgePresentacionesPendientes() {

        List<List<ExpActuaciones>> array = actuacionesController.obtenerPresentacionesPendientes();
        cantidadPresentacionesPendientes = array.get(0).size();
        if (cantidadPresentacionesPendientes == 0) {
            cantidadPresentacionesPendientes = null;
        }

        cantidadPresentacionesPendientesAbogados = array.get(1).size();
        if (cantidadPresentacionesPendientesAbogados == 0) {
            cantidadPresentacionesPendientesAbogados = null;
        }
        /*
        List<DocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", new CanalesEntradaDocumentoJudicial(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE)).getResultList();

        List<ExpActuaciones> listaFinal = new ArrayList<>();
        List<ExpActuaciones> listaFinal2 = new ArrayList<>();

        for (DocumentosJudiciales doc : lista) {
            ExpActuaciones act = null;
            List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.id in (select max(b.id) from exp_actuaciones as b where b.documento_judicial = ?1)", ExpActuaciones.class).setParameter(1, doc.getId()).getResultList();
            if (!listAct.isEmpty()) {
                act = listAct.get(0);
            }

            if (act != null) {
                List<ExpPartesPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                boolean encontro = false;
                for (ExpPartesPorDocumentosJudiciales par : lista2) {
                    if (par.getPersona().equals(act.getPersonaAlta())) {
                        encontro = true;
                        break;
                    }
                }

                List<PersonasPorDocumentosJudiciales> lista3 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                for (PersonasPorDocumentosJudiciales par : lista3) {
                    if (par.getPersona().equals(act.getPersonaAlta())) {
                        encontro = true;
                        break;
                    }
                }

                if (encontro) {
                    listaFinal.add(act);
                }else{
                    listaFinal2.add(act);
                }
            }

        }
         */

    }

    public void badgePresentacionesPendientesNew_() {

        List<List<ExpActuaciones>> array = actuacionesController.obtenerPresentacionesPendientesNew_();
        cantidadPresentacionesPendientes = array.get(0).size();
        if (cantidadPresentacionesPendientes == 0) {
            cantidadPresentacionesPendientes = null;
        }

        cantidadPresentacionesPendientesAbogados = array.get(1).size();
        if (cantidadPresentacionesPendientesAbogados == 0) {
            cantidadPresentacionesPendientesAbogados = null;
        }
    }

    public void badgePresentacionesPendientesNew() {

        int array[] = actuacionesController.obtenerPresentacionesPendientesNew();
        cantidadPresentacionesPendientes = array[0];
        if (cantidadPresentacionesPendientes == 0) {
            cantidadPresentacionesPendientes = null;
        }

        cantidadPresentacionesPendientesAbogados = array[1];
        if (cantidadPresentacionesPendientesAbogados == 0) {
            cantidadPresentacionesPendientesAbogados = null;
        }
    }

    public void navigateExpedientes(String act, int camino) {

        if (camino == 1) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudiciales/index.xhtml?tipo=consulta");
            } catch (IOException ex) {
                Logger.getLogger(PersonasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (camino == 2) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudicialesPorSecretaria/index.xhtml?tipo=registrar_actuacion_secretaria");
            } catch (IOException ex) {
                Logger.getLogger(PersonasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        cargoPersonaController.setSelected(null);
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the CargosPersona controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCargoPersona(ActionEvent event) {
        Personas selected = this.getSelected();
        if (selected != null && cargoPersonaController.getSelected() == null) {
            cargoPersonaController.setSelected(selected.getCargoPersona());
        }
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        Personas selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

    public void prepareEdit() {
        departamentoPersona = null;
        localidadPersona = null;
        actualizarDptoYLocalidad();
    }

    @Override
    public Personas prepareCreate(ActionEvent event) {
        Personas doc = super.prepareCreate(event);
        departamentoPersona = null;
        localidadPersona = null;
        return doc;
    }

    public boolean deshabilitarMenues(String url) {
        if (rolEle != null) {
            return !(filtroURL.verifPermiso(url, rolEle.getId()));
        } else {
            return true;
        }
    }

    public boolean deshabilitarMenues(String url, String permiso) {
        if (rolEle != null) {
            return !(filtroURL.verifPermiso(url, permiso, rolEle.getId()));
        } else {
            return true;
        }
    }

    public void menu(Integer menu) {
        this.menu = menu;
    }

    public boolean deshabilitarMenues(String url, String permiso, Integer menu) {
        if (rolEle != null) {
            return !(filtroURL.verifPermiso(url, permiso, rolEle.getId()) && this.menu.equals(menu));
        } else {
            return true;
        }
    }

    public boolean deshabilitarMenues(Integer menu) {

        return !(this.menu.equals(menu)) || !deshabilitarAntecedentes();
    }
    
   

    public boolean deshabilitarMenues(String url, String permiso, String menu) {
        if (rolEle != null) {
            return !(filtroURL.verifPermiso(url, permiso, menu, rolEle.getId()));
        } else {
            return true;
        }
    }

    public boolean deshabilitarMenues(String url, Integer menu) {
        if (rolEle != null) {
            return !(filtroURL.verifPermiso(url, rolEle.getId()) && this.menu.equals(menu));
        } else {
            return true;
        }
    }

    public boolean deshabilitarAntecedentes() {
        if (per != null) {
            if (rolEle != null) {
                // boolean res = !(per.isHabilitarAntecedentes() && filtroURL.verifPermiso("/pages/antecedentes/index.xhtml", rolEle.getId()));
                boolean res = !(filtroURL.verifPermiso("/pages/antecedentes/index.xhtml", rolEle.getId()));
                return res;
            } else {
                return true;
            }
        }

        return true;
    }
     public boolean deshabilitarlink() {
        if (per != null) {
            if (rolEle != null) {
                // boolean res = !(per.isHabilitarAntecedentes() && filtroURL.verifPermiso("/pages/antecedentes/index.xhtml", rolEle.getId()));
                boolean res = !(filtroURL.verifPermiso("/pages/expEntradaDocumentosJudiciales/index.xhtml", rolEle.getId()));
                return res;
            } else {
                return true;
            }
        }

        return true;
    }
    
  

    public void actualizarDptoYLocalidad() {
        if (getSelected() != null) {
            if (getSelected().getDespachoPersona() != null) {
                departamentoPersona = getSelected().getDespachoPersona().getDepartamentoPersona();
                localidadPersona = getSelected().getDespachoPersona().getLocalidadPersona();
            }
        }
    }

    public String redirigir() {

        List<VAntecedentesPermisosPersonas> permisos = null;
        if (rolElegido != null) {
            permisos = this.ejbFacade.getEntityManager().createNamedQuery("VAntecedentesPermisosPersonas.findByUsuaIdRol", VAntecedentesPermisosPersonas.class).setParameter("usuaId", personaLogin.getId()).setParameter("rolId", rolElegido.getId()).getResultList();
        } else {
            permisos = this.ejbFacade.getEntityManager().createNamedQuery("VAntecedentesPermisosPersonas.findByUsuaId", VAntecedentesPermisosPersonas.class).setParameter("usuaId", usuarioLogin.getId()).getResultList();
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("VAntecedentesPermisosPersonas", permisos);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Persona", personaLogin);

        Usuarios usu = null;
        if (usuarioLogin == null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expLog", true);
            usu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", Constantes.USUARIO_POR_OMISION).getSingleResult();
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("expLog", false);
            usu = usuarioLogin;
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuarios", usu);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("RolElegido", rolElegido);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("passOri", passOri);
        home = "index.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Home", home);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/indexPrincipal.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return home;
    }

    public String enviarSistemaAdministrativo() {
        return enviarSistemaAdministrativo(false);
    }

    public String enviarSistemaAdministrativo(boolean redireccionar) {

        String link = "";
        String datos;
        if (expLog) {
            datos = per.getUsuario() + "," + passOri + ",docsAdm";
        } else {
            datos = usu.getUsuario() + "," + passOri + ",docsAdm";
        }

        String encoded;
        try {
            SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhh");
            Date fecha = ejbFacade.getSystemDate();
            encoded = Base64.getEncoder().encodeToString(Utils.encryptMsg(String.valueOf(datos), Utils.generateKey(format.format(fecha) + "jemjem")));

            encoded = URLEncoder.encode(encoded, StandardCharsets.UTF_8.toString());

            if (host.equals(parAdmin.getProtocolo() + "://" + par.getIpServidor())) {
                link = parAdmin.getProtocolo() + "://" + parAdmin.getIpServidor() + ":" + parAdmin.getPuertoServidor() + "/" + Constantes.URL_SISTEMA_ADMINISTRATIVO + "?hash=" + encoded + "";
            } else {
                link = parAdmin.getProtocoloInterno() + "://" + parAdmin.getIpServidorInterno() + ":" + parAdmin.getPuertoServidorInterno() + "/" + Constantes.URL_SISTEMA_ADMINISTRATIVO + "?hash=" + encoded;
            }

            link = link == null ? "#" : link;

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (InvalidKeySpecException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (InvalidParameterSpecException ex) {
            ex.printStackTrace();
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        if (redireccionar) {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            try {
                externalContext.redirect(link);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return "";
        } else {
            return link;
        }
    }

    public String enviarCarpetaDigitalizacion() {
        // return "file://172.16.0.13/Digitalizacion%20Juridica";
        return "explorer://172.16.0.13/Digitalizacion%20Juridica";
    }

    public Personas verifUsuario(String username, String pass, String estado) {
        try {
            this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            passOri = pass;

            String password = Utils.passwordToHash(pass);

            try {
                personaLogin = this.ejbFacade.getEntityManager().createNamedQuery("Personas.control", Personas.class).setParameter("usuario", username).setParameter("contrasena", password).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (personaLogin == null) {
                usuarioLogin = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.control", Usuarios.class).setParameter("usuario", username).setParameter("contrasena", password).getSingleResult();
                if (usuarioLogin != null) {
                    if (usuarioLogin.getPersona() != null) {
                        personaLogin = this.ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", usuarioLogin.getPersona().getId()).getSingleResult();
                    }
                }
            }

            return personaLogin;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loginControl() {
        loginControl(usuario, contrasena);
    }

    public void loginControl(String usuario, String contrasena) {
        loginControl(usuario, contrasena, true);
    }

    public void loginControl(String usuario, String contrasena, boolean procesarError) {

        Personas per = verifUsuario(usuario, contrasena, "AC");
        if (per == null) {

            if (procesarError) {
                PrimeFaces.current().ajax().update("growl");
                FacesContext context = FacesContext.getCurrentInstance();

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error de acceso"));
            }

        } else {
            List<VAntecedentesPermisosPersonas> permisos = null;
            permisos = this.ejbFacade.getEntityManager().createNamedQuery("VAntecedentesPermisosPersonas.findByUsuaId", VAntecedentesPermisosPersonas.class).setParameter("usuaId", personaLogin.getId()).getResultList();

            listaRolesElegir = new ArrayList<>();

            HashSet<AntecedentesRoles> mapa = new HashSet<>();
            for (int i = 0; i < permisos.size(); i++) {
                VAntecedentesPermisosPersonas perm = permisos.get(i);

                AntecedentesRoles rol = this.ejbFacade.getEntityManager().createNamedQuery("AntecedentesRoles.findById", AntecedentesRoles.class).setParameter("id", perm.getRoleId()).getSingleResult();
                mapa.add(rol);
            }

            listaRolesElegir.addAll(mapa);

            this.usuario = "";
            this.contrasena2 = "";

            PrimeFaces.current().ajax().update("DocumentosListForm");
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('DocumentosCreateDialog').show();");

        }
    }

    public void onload() {

        if (usuario != null && contrasena2 != null) {

            String usuarioActual = usuario;
            String contrasenaActual = contrasena2;

            if (!"".equals(usuarioActual) && !"".equals(contrasenaActual)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("esApp", true);
                loginControl(usuarioActual, contrasenaActual);
            }
        }

        if (sesionLogin != null) {
            Date fecha = ejbFacade.getSystemDate();
            List<SesionesLogin> ses = ejbFacade.getEntityManager().createNamedQuery("SesionesLogin.findByHashFechaHoraCaducidadEstado", SesionesLogin.class).setParameter("hash", sesionLogin).setParameter("fechaHoraCaducidad", fecha).setParameter("estado", "AC").getResultList();

            if (!ses.isEmpty()) {

                ses.get(0).setEstado(new Estados("IN"));

                sesionesLoginController.setSelected(ses.get(0));
                sesionesLoginController.save(null);

                // List<Personas> us = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", ses.get(0).getPersona().getId()).getResultList();
                //if(!us.isEmpty()){                
                // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("esApp", true);
                if (ses.get(0).getPersona() != null) {
                    loginControl(ses.get(0).getPersona().getUsuario(), ses.get(0).getPersona().getContrasena(), false);
                }

                if (ses.get(0).getUsuario() != null) {
                    loginControl(ses.get(0).getUsuario().getUsuario(), ses.get(0).getUsuario().getContrasena(), true);
                } else {
                    JsfUtil.addErrorMessage("Sesion no tiene ni usuario ni persona");
                }
                /*
                }else{
                    JsfUtil.addErrorMessage("No se encontro persona");
                }
                 */
            } else {
                JsfUtil.addErrorMessage("No se encontro sesión o sesión caducada");
            }
        }
    }

    public String obtenerHome() {
        if (session != null) {

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/indexPrincipal.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return (String) session.getAttribute("Home");
        } else {
            return "";
        }
    }

    public String cerrarSession() {
        this.usuario = null;
        this.contrasena = null;
        this.contrasena2 = null;
        HttpSession httpSession;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.invalidate();//para borrar la session
        return "/login?faces-redirect=true";
    }

    @Override
    public Collection<Personas> getItems() {
        return super.getItems2();
    }

    public void save2() {
        super.save(null);
    }

    public void save() {
        if (getSelected() != null) {

            if (departamentoPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_departamentoPersona"));
                return;
            }

            if (localidadPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_localidadPersona"));
                return;
            }

            if (getSelected().getTipoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_tipoPersona"));
                return;
            }

            if (getSelected().getCargoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_cargoPersona"));
                return;
            }

            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_ci"));
                return;
            }

            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_nombresApellidos"));
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setDepartamentoPersona(departamentoPersona);
            getSelected().setLocalidadPersona(localidadPersona);

            super.save(null);

            if (!departamentoPersona.equals(getSelected().getDespachoPersona().getDepartamentoPersona())
                    || !localidadPersona.equals(getSelected().getDespachoPersona().getLocalidadPersona())) {

                DespachosPersona despacho = ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findById", DespachosPersona.class).setParameter("id", getSelected().getDespachoPersona().getId()).getSingleResult();

                despacho.setDepartamentoPersona(departamentoPersona);
                despacho.setLocalidadPersona(localidadPersona);

                despachoController.setSelected(despacho);
                despachoController.save(null);

            }

            setItems(this.ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList());

        }
    }

    public String generarCodigoArchivo() {
        Random aleatorio;
        int valor = 0;
        String pin = "";
        aleatorio = new Random(System.currentTimeMillis());
        valor = aleatorio.nextInt(999999);
        pin = String.valueOf(valor);
        while (pin.trim().length() < 6) {
            pin = '0' + pin;
        }
        return pin;
    }

    /*
    public void reimprimirRepAntecedentes(Antecedentes ant) {

        HttpServletResponse httpServletResponse = null;
        if (ant != null) {
            if (ant.getPathArchivo() != null) {
                try {

                    byte[] fileByte = null;
                    try {
                        fileByte = Files.readAllBytes(new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + ant.getPathArchivo()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No existe documento");
                        return;
                    }

                    if (fileByte != null) {

                        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                        httpServletResponse.setContentType("application/pdf");
                        // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                        httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                        servletOutputStream.write(fileByte);
                        FacesContext.getCurrentInstance().responseComplete();
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
                    JsfUtil.addErrorMessage("No se pudo generar el documento.");

                }
            } else {
                JsfUtil.addErrorMessage("No existe documento (2)");
            }
        } else {
            JsfUtil.addErrorMessage("No existe documento (3)");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }
     */
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            List<RepAntecedentesDocumentosJudiciales> listaInformacionSumaria = null;
            List<RepAntecedentesDocumentosJudiciales> listaPedidoDesafuero = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;

            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");
            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaDenuncias == null) {
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAcusaciones == null) {
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPreliminar == null) {
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAntecedentesCSJ == null) {
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaInformacionSumaria == null) {
                    listaInformacionSumaria = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Informacion Sumaria:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaInformacionSumaria.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPedidoDesafuero == null) {
                    listaPedidoDesafuero = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPedidoDesafuero.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaEnjuiciamientos == null) {
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n"
                        + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n"
                        + "	where r.resolucion = s.id \n"
                        + "       and s.tipo_resolucion = t.id\n"
                        + "       and e.tipo_resolucion = p.id\n"
                        + "	and r.resuelve = e.codigo\n"
                        + "	and s.documento_judicial = ?1 \n"
                        + "	and r.persona = ?2\n"
                        + "       and e.tipo_resuelve = ?3\n"
                        + "	and r.estado = ?4 \n"
                        + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                        .getSingleResult();

                if (listaFiniquitados == null) {
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion() != null) ? res.getTipoResolucion() : res.getTipoResolucionAlt());

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (valido) {
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }

            String myHash = "";

            String codigoArchivo = "";
            if (imprimirQR) {

                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }

            String reportPath;

            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null || listaInformacionSumaria != null || listaPedidoDesafuero != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                } else {
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                } else {
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                } else {
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                } else {
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                } else {
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                } else {
                    map.put("datasourceAntecedentesCSJ", null);
                }
                if (listaInformacionSumaria != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceInformacionSumaria = new JRBeanCollectionDataSource(listaInformacionSumaria);
                    map.put("datasourceInformacionSumaria", beanCollectionDataSourceInformacionSumaria);
                } else {
                    map.put("datasourceInformacionSumaria", null);
                }
                if (listaPedidoDesafuero != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePedidoDesafuero = new JRBeanCollectionDataSource(listaPedidoDesafuero);
                    map.put("datasourcePedidoDesafuero", beanCollectionDataSourcePedidoDesafuero);
                } else {
                    map.put("datasourcePedidoDesafuero", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            if (imprimirQR) {

                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;

            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");
            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaDenuncias == null) {
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAcusaciones == null) {
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPreliminar == null) {
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAntecedentesCSJ == null) {
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaEnjuiciamientos == null) {
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n"
                        + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n"
                        + "	where r.resolucion = s.id \n"
                        + "       and s.tipo_resolucion = t.id\n"
                        + "       and e.tipo_resolucion = p.id\n"
                        + "	and r.resuelve = e.codigo\n"
                        + "	and s.documento_judicial = ?1 \n"
                        + "	and r.persona = ?2\n"
                        + "       and e.tipo_resuelve = ?3\n"
                        + "	and r.estado = ?4 \n"
                        + "	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                        .getSingleResult();

                if (listaFiniquitados == null) {
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion() != null) ? res.getTipoResolucion() : res.getTipoResolucionAlt());

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (valido) {
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }

            String myHash = "";

            String codigoArchivo = "";
            if (imprimirQR) {

                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }

            String reportPath;

            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                } else {
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                } else {
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                } else {
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                } else {
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                } else {
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                } else {
                    map.put("datasourceAntecedentesCSJ", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            if (imprimirQR) {

                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     */
 /*
    
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;
            
            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");

            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaDenuncias == null){
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                String caratula = doc.getCaratula();
                if(doc.getTipoExpedienteAnterior() != null){
                    if(doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA){
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    getResultList();
            
            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAcusaciones == null){
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                String caratula = doc.getCaratula();
                if(doc.getTipoExpedienteAnterior() != null){
                    if(doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION){
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaPreliminar == null){
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                String caratula = doc.getCaratula();
                if(doc.getTipoExpedienteAnterior() != null){
                    if(doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR){
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAntecedentesCSJ == null){
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                String caratula = doc.getCaratula();
                if(doc.getTipoExpedienteAnterior() != null){
                    if(doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ){
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?7\n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaEnjuiciamientos == null){
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?7\n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n" +
"	where r.resolucion = s.id \n" +
"       and s.tipo_resolucion = t.id\n" + 
"       and e.tipo_resolucion = p.id\n" + 
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = ?1 \n" +
"	and r.persona = ?2\n" +
"       and e.tipo_resuelve = ?3\n" +
"	and r.estado = ?4 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                                .getSingleResult();

                if(listaFiniquitados == null){
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion()!=null)?res.getTipoResolucion():res.getTipoResolucionAlt());
                
                
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if(valido){
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }
            
            String myHash = "";
            
            String codigoArchivo = "";
            if(imprimirQR){
                
                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }
            
            String reportPath;
            
            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                }else{
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                }else{
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                }else{
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                }else{
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                }else{
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                }else{
                    map.put("datasourceAntecedentesCSJ", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());
            
            if(imprimirQR){
            
                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }
            
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
 /*
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;
            
            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");

            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaDenuncias == null){
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();
            
            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAcusaciones == null){
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaPreliminar == null){
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?8\n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAntecedentesCSJ == null){
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?7\n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaEnjuiciamientos == null){
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"       and e.tipo_resuelve <> ?7\n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n" +
"	where r.resolucion = s.id \n" +
"       and s.tipo_resolucion = t.id\n" + 
"       and e.tipo_resolucion = p.id\n" + 
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = ?1 \n" +
"	and r.persona = ?2\n" +
"       and e.tipo_resuelve = ?3\n" +
"	and r.estado = ?4 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                                .getSingleResult();

                if(listaFiniquitados == null){
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion()!=null)?res.getTipoResolucion():res.getTipoResolucionAlt());
                
                
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if(valido){
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }
            
            String myHash = "";
            
            String codigoArchivo = "";
            if(imprimirQR){
                
                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }
            
            String reportPath;
            
            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                }else{
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                }else{
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                }else{
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                }else{
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                }else{
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                }else{
                    map.put("datasourceAntecedentesCSJ", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());
            
            if(imprimirQR){
            
                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }
            
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

 /*
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;
            
            Date fecha = ejbFacade.getSystemDate();

            DateFormat format = new SimpleDateFormat("yyyy");

            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaDenuncias == null){
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();
            
            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAcusaciones == null){
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:");
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaPreliminar == null){
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAntecedentesCSJ == null){
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaEnjuiciamientos == null){
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select s.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, e.descripcion as resuelve\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t\n" +
"	where r.resolucion = s.id \n" +
"       and s.tipo_resolucion = t.id\n" + 
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = ?1 \n" +
"	and r.persona = ?2\n" +
"       and e.tipo_resuelve = ?3\n" +
"	and r.estado = ?4 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").getSingleResult();

                if(listaFiniquitados == null){
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " RESOLUCION " + res.getTipoResolucion() + " N° " + res.getNroResolucion() + " SENTIDO: " + res.getResuelve() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            // System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }
            
            String myHash = "";
            
            String codigoArchivo = "";
            if(imprimirQR){
                
                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }
            
            String reportPath;
            
            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                }else{
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                }else{
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                }else{
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                }else{
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                }else{
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                }else{
                    map.put("datasourceAntecedentesCSJ", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());
            
            if(imprimirQR){
            
                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setCodigoArchivo(codigoArchivo);
                ant.setHash(myHash);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }
            
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     */
 /*
    
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = new ArrayList<>();
            RepAntecedentesDocumentosJudiciales denuncia = null;
            
            Date fecha = ejbFacade.getSystemDate();

            DateFormat format = new SimpleDateFormat("yyyy");

            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve in(?7,?8))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(8, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Denuncia:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(denuncia);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?7,?8))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(8, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Acusacion:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(denuncia);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?7,?8))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(8, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Investigacion Preliminar:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(denuncia);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?7,?8))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(8, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Antecedentes C.S.J.:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(denuncia);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?7,?8) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(8, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(denuncia);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?7,?8) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(8, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                ResuelvePorResolucionesPorPersonas res = (ResuelvePorResolucionesPorPersonas) ejbFacade.getEntityManager().createNativeQuery("select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = ?1 and r.persona = ?2 and r.estado = 'AC' and r.resuelve NOT IN (?3,?4) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' )", ResuelvePorResolucionesPorPersonas.class).setParameter(1, doc.getId()).setParameter(2, persona.getId()).setParameter(3, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(4, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getSingleResult();

                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaFiniquitados.add(denuncia);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }
            
            String myHash = "";
            
            if(imprimirQR){
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                map.put("qr", pathAntecedentes + myHash + ".pdf");
            }
            
            String reportPath;
            if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {

                if (listaDenuncias.isEmpty() && listaAcusaciones.isEmpty() && listaPreliminar.isEmpty() && listaAntecedentesCSJ.isEmpty()) {
                    listaDenuncias.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                
                if (listaEnjuiciamientos.isEmpty()) {
                    listaEnjuiciamientos.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                if (listaFiniquitados.isEmpty()) {
                    listaFiniquitados.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }

                JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);

                JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);

                JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);

                JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);

                JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);

                JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());
            
            if(imprimirQR){
            
                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }
            
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
 /*

    public void imprimirRepAntecedentes() {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = new ArrayList<>();
            RepAntecedentesDocumentosJudiciales denuncia = null;
            
            Date fecha = ejbFacade.getSystemDate();

            DateFormat format = new SimpleDateFormat("yyyy");

            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Denuncia:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(denuncia);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Acusacion:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(denuncia);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Investigacion Preliminar:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(denuncia);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("Antecedentes C.S.J.:");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(denuncia);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN' and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(denuncia);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve <> 'EN' and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                ResuelvePorResolucionesPorPersonas res = (ResuelvePorResolucionesPorPersonas) ejbFacade.getEntityManager().createNativeQuery("select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = ?1 and r.persona = ?2 and r.estado = 'AC' and r.resuelve <> 'EN' and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' )", ResuelvePorResolucionesPorPersonas.class).setParameter(1, doc.getId()).setParameter(2, persona.getId()).getSingleResult();

                denuncia = new RepAntecedentesDocumentosJudiciales();
                denuncia.setNroCausa(doc.getCausa());
                denuncia.setCaratula(doc.getCaratulaString());
                denuncia.setMotivoProceso(doc.getMotivoProcesoString());
                denuncia.setAno(format.format(doc.getFechaPresentacion()));
                denuncia.setLabel("");
                denuncia.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaFiniquitados.add(denuncia);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }
            
            DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");
            
            String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            
            map.put("qr", pathAntecedentes + myHash + ".pdf");
            
            String reportPath;
            if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {

                if (listaDenuncias.isEmpty() && listaAcusaciones.isEmpty() && listaPreliminar.isEmpty() && listaAntecedentesCSJ.isEmpty()) {
                    listaDenuncias.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                
                if (listaEnjuiciamientos.isEmpty()) {
                    listaEnjuiciamientos.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                if (listaFiniquitados.isEmpty()) {
                    listaFiniquitados.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }

                JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);

                JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);

                JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);

                JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);

                JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);

                JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }
            
            
            Antecedentes ant = new Antecedentes();
            
            ant.setEmpresa(new Empresas(1));
            ant.setFechaHoraAlta(fecha);
            ant.setFechaHoraUltimoEstado(fecha);
            ant.setPersona(persona);
            ant.setPathArchivo(myHash + ".pdf");
            
            antecedentesController.setSelected(ant);
            antecedentesController.saveNew(null);

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());
            
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
}
