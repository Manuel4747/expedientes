package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.PedidosPersona;
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
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonasPK;
import py.gov.jem.expedientes.models.DepartamentosPersona;
import py.gov.jem.expedientes.models.DetallesPedidoPersona;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.EstadosPersona;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.LocalidadesPersona;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.TiposPersona;
import py.gov.jem.expedientes.models.Usuarios;
import py.gov.jem.expedientes.models.ValidacionesEmail;
import py.gov.jem.expedientes.models.ValidacionesReseteo;

@Named(value = "pedidosPersonaAutController")
@ViewScoped
public class PedidosPersonaAutController extends AbstractController<PedidosPersona> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private ValidacionesEmailController validacionesEmailController;
    @Inject
    private ValidacionesReseteoController validacionesReseteoController;
    @Inject
    private PersonasController personasController;
    @Inject
    private AntecedentesRolesPorPersonasController antecedentesRolesPorPersonasController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private DetallesPedidoPersonaController detallePedidoPersonaController;
    private HttpSession session;
    private Personas personaUsuario;
    private Personas personaSelected;
    private List<LocalidadesPersona> listaLocalidades;
    private DepartamentosPersona departamentoPersona;
    private String cargoPersonaRadio;
    private String despachoPersonaRadio;
    private String departamentoPersonaRadio;
    private String localidadPersonaRadio;
    private boolean deshabilitarEdicionDespachoPersona;
    private boolean deshabilitarBoton;
    private String leyenda;
    private String avisoReseteo;
    private String avisoReseteo2;
    private String avisoReseteo3;
    private ParametrosSistema par;
    private String contrasena1;
    private String contrasena2;
    private String mensaje1;
    private String mensaje2;
    private String mensaje3;
    private String mensaje4;
    private String mensaje5;
    private String endpoint;
    private List<PedidosPersona> listaPend;
    private List<PedidosPersona> listaAut;
    private PedidosPersona selectedPedido;
    private List<DetallesPedidoPersona> listaDet;
    private PedidosPersona selectedAut;
    private List<DetallesPedidoPersona> listaDetAut;
    private String observacion;
    private Date fechaInicio;
    private PedidosPersona pedidoRechazar;

    private UploadedFile file1;

    private UploadedFile file2;

    private UploadedFile file3;

    public UploadedFile getFile1() {
        return file1;
    }

    public void setFile1(UploadedFile file1) {
        this.file1 = file1;
    }

    public UploadedFile getFile2() {
        return file2;
    }

    public void setFile2(UploadedFile file2) {
        this.file2 = file2;
    }

    public UploadedFile getFile3() {
        return file3;
    }

    public void setFile3(UploadedFile file3) {
        this.file3 = file3;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public List<LocalidadesPersona> getListaLocalidades() {
        return listaLocalidades;
    }

    public void setListaLocalidades(List<LocalidadesPersona> listaLocalidades) {
        this.listaLocalidades = listaLocalidades;
    }

    public String getCargoPersonaRadio() {
        return cargoPersonaRadio;
    }

    public void setCargoPersonaRadio(String cargoPersonaRadio) {
        this.cargoPersonaRadio = cargoPersonaRadio;
    }

    public CargosPersonaController getCargoPersonaController() {
        return cargoPersonaController;
    }

    public void setCargoPersonaController(CargosPersonaController cargoPersonaController) {
        this.cargoPersonaController = cargoPersonaController;
    }

    public String getDespachoPersonaRadio() {
        return despachoPersonaRadio;
    }

    public void setDespachoPersonaRadio(String despachoPersonaRadio) {
        this.despachoPersonaRadio = despachoPersonaRadio;
    }

    public String getDepartamentoPersonaRadio() {
        return departamentoPersonaRadio;
    }

    public void setDepartamentoPersonaRadio(String departamentoPersonaRadio) {
        this.departamentoPersonaRadio = departamentoPersonaRadio;
    }

    public String getLocalidadPersonaRadio() {
        return localidadPersonaRadio;
    }

    public void setLocalidadPersonaRadio(String localidadPersonaRadio) {
        this.localidadPersonaRadio = localidadPersonaRadio;
    }

    public boolean isDeshabilitarEdicionDespachoPersona() {
        return deshabilitarEdicionDespachoPersona;
    }

    public void setDeshabilitarEdicionDespachoPersona(boolean deshabilitarEdicionDespachoPersona) {
        this.deshabilitarEdicionDespachoPersona = deshabilitarEdicionDespachoPersona;
    }

    public boolean isDeshabilitarBoton() {
        return deshabilitarBoton;
    }

    public void setDeshabilitarBoton(boolean deshabilitarBoton) {
        this.deshabilitarBoton = deshabilitarBoton;
    }

    public String getLeyenda() {
        return leyenda;
    }

    public void setLeyenda(String leyenda) {
        this.leyenda = leyenda;
    }

    public String getAvisoReseteo() {
        return avisoReseteo;
    }

    public void setAvisoReseteo(String avisoReseteo) {
        this.avisoReseteo = avisoReseteo;
    }

    public String getAvisoReseteo2() {
        return avisoReseteo2;
    }

    public void setAvisoReseteo2(String avisoReseteo2) {
        this.avisoReseteo2 = avisoReseteo2;
    }

    public String getAvisoReseteo3() {
        return avisoReseteo3;
    }

    public void setAvisoReseteo3(String avisoReseteo3) {
        this.avisoReseteo3 = avisoReseteo3;
    }

    public String getContrasena1() {
        return contrasena1;
    }

    public void setContrasena1(String contrasena1) {
        this.contrasena1 = contrasena1;
    }

    public String getContrasena2() {
        return contrasena2;
    }

    public void setContrasena2(String contrasena2) {
        this.contrasena2 = contrasena2;
    }

    public String getMensaje1() {
        return mensaje1;
    }

    public void setMensaje1(String mensaje1) {
        this.mensaje1 = mensaje1;
    }

    public String getMensaje2() {
        return mensaje2;
    }

    public void setMensaje2(String mensaje2) {
        this.mensaje2 = mensaje2;
    }

    public String getMensaje3() {
        return mensaje3;
    }

    public void setMensaje3(String mensaje3) {
        this.mensaje3 = mensaje3;
    }

    public String getMensaje4() {
        return mensaje4;
    }

    public void setMensaje4(String mensaje4) {
        this.mensaje4 = mensaje4;
    }

    public String getMensaje5() {
        return mensaje5;
    }

    public void setMensaje5(String mensaje5) {
        this.mensaje5 = mensaje5;
    }

    public List<PedidosPersona> getListaPend() {
        return listaPend;
    }

    public void setListaPend(List<PedidosPersona> listaPend) {
        this.listaPend = listaPend;
    }

    public List<PedidosPersona> getListaAut() {
        return listaAut;
    }

    public void setListaAut(List<PedidosPersona> listaAut) {
        this.listaAut = listaAut;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public PedidosPersona getSelectedPedido() {
        return selectedPedido;
    }

    public void setSelectedPedido(PedidosPersona selectedPedido) {
        this.selectedPedido = selectedPedido;
    }

    public List<DetallesPedidoPersona> getListaDet() {
        return listaDet;
    }

    public void setListaDet(List<DetallesPedidoPersona> listaDet) {
        this.listaDet = listaDet;
    }

    public PedidosPersona getSelectedAut() {
        return selectedAut;
    }

    public void setSelectedAut(PedidosPersona selectedAut) {
        this.selectedAut = selectedAut;
    }

    public List<DetallesPedidoPersona> getListaDetAut() {
        return listaDetAut;
    }

    public void setListaDetAut(List<DetallesPedidoPersona> listaDetAut) {
        this.listaDetAut = listaDetAut;
    }

    public PedidosPersona getPedidoRechazar() {
        return pedidoRechazar;
    }

    public void setPedidoRechazar(PedidosPersona pedidoRechazar) {
        this.pedidoRechazar = pedidoRechazar;
    }

    public PedidosPersonaAutController() {
        // Inform the Abstract parent controller of the concrete PedidosPersona Entity
        super(PedidosPersona.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2022);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        fechaInicio = myCal.getTime();

        if (session != null) {
            personaUsuario = (Personas) session.getAttribute("Persona");
        }
        prepareCreate(null);
        cargoPersonaRadio = "1";
        despachoPersonaRadio = "1";
        departamentoPersonaRadio = "1";
        localidadPersonaRadio = "1";
        deshabilitarEdicionDespachoPersona = true;
        deshabilitarBoton = true;
        leyenda = "Enviar Pedido";

        observacion = "     Su pedido de usuario fue rechazado por el sgte motivo: ";

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();
        buscarPend();
        buscarAut();
    }

    public Integer buscarPend() {
        listaPend = ejbFacade.getEntityManager().createNamedQuery("PedidosPersona.findByEstado", PedidosPersona.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).setParameter("fechaInicio", fechaInicio).getResultList();
        return listaPend.size();
    }

    public void buscarAut() {
        listaAut = ejbFacade.getEntityManager().createNamedQuery("PedidosPersona.findByNotEstado", PedidosPersona.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).setParameter("fechaInicio", fechaInicio).getResultList();
    }

    public void buscarDet() {
        listaDet = ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoPersona.findByPedidoPersona", DetallesPedidoPersona.class).setParameter("pedidoPersona", selectedPedido).getResultList();
    }

    public void buscarDetAut() {
        listaDetAut = ejbFacade.getEntityManager().createNamedQuery("DetallesPedidoPersona.findByPedidoPersona", DetallesPedidoPersona.class).setParameter("pedidoPersona", selectedAut).getResultList();
    }

    public void prepareRechazarPedido(PedidosPersona item) {
        pedidoRechazar = item;
    }
    
    public Integer cantidadPedidosPersona(){
        return buscarPend();
    }

    public void verDoc(DetallesPedidoPersona doc) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            //httpServletResponse.setContentType("application/pdf");
            httpServletResponse.setContentType("image");
            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
            httpServletResponse.addHeader("Content-disposition", "filename=documento");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            if (doc.getRuta() != null) {
                try {
                    fileByte = Files.readAllBytes(new File(par.getRutaArchivosPedidosPersona() + "/" + doc.getRuta()).toPath());
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
        PedidosPersona selected = this.getSelected();
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
        PedidosPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

    public void editarDespachoPersona() {
        deshabilitarEdicionDespachoPersona = !deshabilitarEdicionDespachoPersona;
    }

    public boolean deshabilitarNombresApellidos() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean deshabilitarAutorizar(PedidosPersona per) {
        return !per.isEmailValidado();
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

    public void actualizar(PedidosPersona ped, String resp, boolean enviarEmail) {

        if (ped != null) {
            Date fecha = ejbFacade.getSystemDate();
            if (Constantes.SI.equals(resp)) {
                String hash = "";
                try {
                    hash = Utils.generarHash(fecha, ped.getId());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                    return;
                }

                Personas per = null;
                if (ped.getPersona() == null) {
                    per = new Personas();
                    per.setCi(ped.getCi());
                    per.setNombresApellidos(ped.getNombresApellidos());
                    per.setEmail(ped.getEmail());
                    per.setTelefono1(ped.getTelefono1());
                    per.setTelefono2(ped.getTelefono2());
                    per.setUsuario(ped.getCi());
                    
                    String pass = Utils.passwordToHash(hash.substring(1, 7));
                    
                    List<EstadosPersona> estPer = ejbFacade.getEntityManager().createNamedQuery("EstadosPersona.findByCodigo", EstadosPersona.class).setParameter("codigo", Constantes.ESTADO_USUARIO_AC).getResultList();
                    
                    if(!estPer.isEmpty()){
                        per.setEstadoPersona(estPer.get(0));
                    }
                    
                    per.setContrasena(pass);
                    per.setEstado(Constantes.ESTADO_USUARIO_AC);
                    per.setDepartamentoPersona(ped.getDepartamentoPersona());
                    per.setLocalidadPersona(ped.getLocalidadPersona());
                    per.setCargoPersona(ped.getCargoPersona());
                    per.setDespachoPersona(ped.getDespachoPersona());
                    per.setEmpresa(new Empresas(1));
                    per.setFechaHoraAlta(fecha);
                    per.setFechaHoraUltimoEstado(fecha);
                    per.setHabilitarAntecedentes(true);
                    per.setEmailValidado(ped.isEmailValidado());
                    per.setTipoPersona(new TiposPersona(1));
                    per.setUsuarioAlta(new Usuarios(Constantes.USUARIO_POR_OMISION));
                    
                        
                    String pass2 = Utils.passwordToHash(hash.substring(24, 31));
                    per.setCodigoDesbloqueo(pass2);

                    personasController.setSelected(per);
                    personasController.saveNew(null);

                    AntecedentesRolesPorPersonas rol = null;
                    if (ped.getCaracter().equals(Constantes.CARACTER_MAGISTRADO)) {
                        AntecedentesRolesPorPersonasPK pk = new AntecedentesRolesPorPersonasPK(per.getId(), Constantes.ROL_MAGISTRADO);
                        rol = new AntecedentesRolesPorPersonas(pk);
                        rol.setRoles(new AntecedentesRoles(Constantes.ROL_MAGISTRADO));
                    } else {
                        AntecedentesRolesPorPersonasPK pk = new AntecedentesRolesPorPersonasPK(per.getId(), Constantes.ROL_ABOGADO);
                        rol = new AntecedentesRolesPorPersonas(pk);
                        rol.setRoles(new AntecedentesRoles(Constantes.ROL_ABOGADO));
                    }

                    rol.setPersonas(per);
                    antecedentesRolesPorPersonasController.setSelected(rol);
                    antecedentesRolesPorPersonasController.saveNew2(null);

                    if (ped.getCaracter().equals(Constantes.CARACTER_MAGISTRADO)) {
                        AntecedentesRolesPorPersonasPK pk2 = new AntecedentesRolesPorPersonasPK(per.getId(), Constantes.ROL_ANTECEDENTES);
                        AntecedentesRolesPorPersonas rol2 = new AntecedentesRolesPorPersonas(pk2);
                        rol2.setRoles(new AntecedentesRoles(Constantes.ROL_ANTECEDENTES));
                        rol2.setPersonas(per);
                        antecedentesRolesPorPersonasController.setSelected(rol2);
                        antecedentesRolesPorPersonasController.saveNew2(null);
                    }

                } else {
                    per = null;
                    try {
                        per = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", ped.getPersona().getId()).getSingleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se encontro la persona asociada al pedido: " + ped.getPersona().getId() + ", " + ped.getNombresApellidos());
                        return;
                    }

                    boolean tieneUsuario = false;
                    if (per.getUsuario() != null) {
                        if (!"".equals(per.getUsuario())) {
                            tieneUsuario = true;
                            AntecedentesRolesPorPersonasPK pk = new AntecedentesRolesPorPersonasPK(per.getId(), Constantes.ROL_ABOGADO);
                            AntecedentesRolesPorPersonas rol = new AntecedentesRolesPorPersonas(pk);
                            rol.setRoles(new AntecedentesRoles(Constantes.ROL_ABOGADO));
                            rol.setPersonas(per);
                            antecedentesRolesPorPersonasController.setSelected(rol);
                            antecedentesRolesPorPersonasController.saveNew2(null);
                        }
                    }

                    if (!tieneUsuario) {
                        per.setUsuario(ped.getCi());
                    
                        String pass = Utils.passwordToHash(hash.substring(1, 7));
                        
                        per.setContrasena(pass);
                        
                        String pass2 = Utils.passwordToHash(hash.substring(24, 31));
                        
                        per.setCodigoDesbloqueo(pass2);
                        per.setEmailValidado(ped.isEmailValidado());
                        per.setHabilitarAntecedentes(true);
                        personasController.setSelected(per);
                        personasController.save(null);
                    }
                }

                ped.setFechaHoraRespuesta(fecha);
                ped.setPersonaRespuesta(personaUsuario);
                ped.setEstado("SI");

                setSelected(ped);

                super.save(null);

                selectedPedido = null;
                selectedAut = null;
                buscarPend();
                buscarAut();
                buscarDet();
                buscarDetAut();

                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Se le ha generado un usuario para el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br><br>"
                        + "Usuario: " + per.getUsuario()
                        + "<br>"
                        + "Contraseña: " + hash.substring(1, 7)
                        + "<br><br>"
                        + "     Ademas se le ha generado un código de desbloqueo para el caso en que su usuario se bloquee por reiterados intentos fallidos de ingreso de contraseña”<br><br>"
                        + "<br>"
                        + "Código: " + hash.substring(24, 31)
                        + "<br><br>"
                        + "Para entrar al sistema, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Usuario para en el Sistema de Expediente Electrónico JEM", texto);
            } else {
                try {
                    ped.setFechaHoraRespuesta(fecha);
                    ped.setPersonaRespuesta(personaUsuario);
                    ped.setEstado("NO");

                    setSelected(ped);

                    super.save(null);
                    buscarPend();
                    buscarAut();
                    buscarDet();
                    buscarDetAut();
                    if (enviarEmail) {
                        String texto = "<p>Hola " + getSelected().getNombresApellidos() + "<br> "
                                + observacion;
                        enviarEmailAviso(ped.getEmail(), "RECHAZADO: Usuario para en el Sistema de Expediente Electrónico JEM", texto);
                    }
                } finally {
                    ped = null;
                }
            }

        }

    }

    public boolean deshabilitarCargoPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if ("1".equals(cargoPersonaRadio)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarDescripcionCargoPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if ("2".equals(cargoPersonaRadio)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarTipoPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean deshabilitarDespachoPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if ("1".equals(despachoPersonaRadio)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarDescripcionDespachoPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if ("2".equals(despachoPersonaRadio)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarDepartamentoPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if ("1".equals(departamentoPersonaRadio)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarDescripcionDepartamentoPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if ("2".equals(departamentoPersonaRadio)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarLocalidadPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if (getSelected().getDepartamentoPersona() != null) {
                        if ("1".equals(localidadPersonaRadio)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean deshabilitarDescripcionLocalidadPersona() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    if ("2".equals(localidadPersonaRadio)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void actualizarDatos() {
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    try {
                        personaSelected = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCiEstado", Personas.class).setParameter("ci", getSelected().getCi()).setParameter("estado", "AC").getSingleResult();

                        deshabilitarBoton = false;
                        // leyenda = "Enviar Pedido";

                        getSelected().setNombresApellidos(personaSelected.getNombresApellidos());
                        getSelected().setCargoPersona(personaSelected.getCargoPersona());
                        if (personaSelected.getCargoPersona() == null) {
                            getSelected().setDescripcionCargoPersona("");
                        }
                        getSelected().setDespachoPersona(personaSelected.getDespachoPersona());
                        if (personaSelected.getDespachoPersona() == null) {
                            getSelected().setDescripcionDespachoPersona("");
                        }
                        getSelected().setTipoPersona(personaSelected.getTipoPersona());
                        if (personaSelected.getTipoPersona() == null) {
                            getSelected().setDescripcionTipoPersona("");
                        }
                        getSelected().setDepartamentoPersona(personaSelected.getDepartamentoPersona());
                        if (personaSelected.getDepartamentoPersona() == null) {
                            getSelected().setDescripcionDepartamentoPersona("");
                        }
                        getSelected().setLocalidadPersona(personaSelected.getLocalidadPersona());
                        if (personaSelected.getLocalidadPersona() == null) {
                            getSelected().setDescripcionLocalidadPersona("");
                        }

                        actualizarListaLocalidad();
                    } catch (Exception e) {
                        // leyenda = "Enviar Pedido";
                        personaSelected = null;
                        getSelected().setNombresApellidos("");
                        getSelected().setCargoPersona(null);
                        getSelected().setDescripcionCargoPersona("");
                        getSelected().setDespachoPersona(null);
                        getSelected().setDescripcionDespachoPersona("");
                        getSelected().setTipoPersona(null);
                        getSelected().setDescripcionTipoPersona("");
                        getSelected().setDepartamentoPersona(null);
                        getSelected().setDescripcionDepartamentoPersona("");
                        getSelected().setLocalidadPersona(null);
                        getSelected().setDescripcionLocalidadPersona("");
                        deshabilitarBoton = false;
                        // e.printStackTrace();
                    }

                } else {
                    deshabilitarBoton = true;
                }
            } else {
                deshabilitarBoton = true;
            }
        }
    }

    public void actualizarListaLocalidad() {
        if (getSelected() != null) {
            if (getSelected().getDepartamentoPersona() != null) {
                listaLocalidades = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", getSelected().getDepartamentoPersona()).getResultList();
            }
        }
    }

    public void prepareEdit() {
    }

    @Override
    public PedidosPersona prepareCreate(ActionEvent event) {
        PedidosPersona doc = super.prepareCreate(event);
        return doc;
    }

    @Override
    public Collection<PedidosPersona> getItems() {
        return super.getItems2();
    }

    public String volver() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/login.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "/" + endpoint + "/faces/login";
    }

    public void enviarValidacionEmail(Integer id, Personas per, PedidosPersona ped, String email, String myHash) {

        // ValidacionesEmail val = validacionesEmailController.prepareCreate(null);
        //ValidacionesEmail val = new ValidacionesEmail();

        /*
            
            val.setHash(myHash);
            val.setPersona(per);
            val.setPedidoPersona(getSelected());
            val.setEmpresa(new Empresas(1));
            val.setFechaHoraAlta(fecha);
            val.setEmail(email);
            val.setFechaHoraCaducidad(ejbFacade.getSystemDate(1));

            validacionesEmailController.setSelected(val);

            validacionesEmailController.saveNew(null);

         */
        BodyPart texto = new MimeBodyPart();
        try {
            texto.setContent("<p>Hola " + ped.getNombresApellidos() + "<br> "
                    + "     Gracias por registrarse en “Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Antes que se le genere su usuario debe de validar su dirección de correo<br><br>"
                    + "Para validar su correo haga <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_EMAIL_EXPEDIENTE + "?hash=" + myHash + "'>CLICK AQUÍ</a>.<br><br>",
                    "text/html; charset=utf-8");
            /*
                Hola,………………
Gracias por registrarse en “Antecedentes del Jurado de Enjuiciamiento de Magistrados”. Su cuenta ha sido creada y debe ser validada antes de su uso. Para validar la cuenta CLICK AQUÍ.
Tras la validación, ya podrá acceder a ____________ (enlace de antecedentes) usando el siguiente usuario y contraseña:

Usuario: que le aparezca su número de cédula.
	Obs: El usuario y contraseña suministrado para la operación de los sistemas de la institución, son personales e intransferibles y constituye su firma electrónica; en tal sentido asume las características referidas a la firma electrónica en el marco legal correspondiente.
                
             */
            Utils.sendEmailAsync(par.getIpServidorEmail(),
                    par.getPuertoServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    par.getContrasenaServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    email,
                    "Validación de Email para Sistema de Expedientes Electrónicos JEM",
                    texto);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return;
        }

    }

    public String enviarReseteo() {

        // ValidacionesReseteo val = validacionesReseteoController.prepareCreate(null);
        ValidacionesReseteo val = new ValidacionesReseteo();

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

            DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");
            Date fecha = ejbFacade.getSystemDate();
            md.update((personaSelected.getId() + "_" + format2.format(fecha)).getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

            val.setHash(myHash);
            val.setPersona(personaSelected);
            val.setEmpresa(new Empresas(1));
            val.setFechaHoraAlta(fecha);
            val.setEmail(personaSelected.getEmail());
            val.setFechaHoraCaducidad(ejbFacade.getSystemDate(1));

            validacionesReseteoController.setSelected(val);

            validacionesReseteoController.saveNew(null);

            BodyPart texto = new MimeBodyPart();
            try {
                texto.setContent("<p>" + personaSelected.getNombresApellidos() + "<br> "
                        + "     Ud esta recibiendo este email porque ha pedido un reseteo la contraseña del usuario " + personaSelected.getUsuario() + " para el Sistema de Antecedentes del Jurado de Enjuiciamiento de Magistrados <br>"
                        + "Si Ud no ha hecho dicho pedido, por favor, ignone este email<br><br>"
                        + "Para restear su contraseña haga <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_EMAIL + "?hash=" + myHash + "'>Click Aqui</a>.<br><br>"
                        + "Obs: Tiene hasta 24 horas para hacerlo antes que este pedido caduque<br>"
                        + "Cordiales saludos", "text/html; charset=utf-8");

                Utils.sendEmail(par.getIpServidorEmail(),
                        par.getPuertoServidorEmail(),
                        par.getUsuarioServidorEmail(),
                        par.getContrasenaServidorEmail(),
                        par.getUsuarioServidorEmail(),
                        personaSelected.getEmail(),
                        "Reseteo contraseña Sistema de Antecedentes JEM",
                        texto);

            } catch (MessagingException ex) {
                ex.printStackTrace();
                return "";
            }

        } catch (NoSuchAlgorithmException ex) {
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/pedidosPersona/GraciasReseteo.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "/pages/pedidosPersona/GraciasReseteo";
    }

    private String obtenerEmailOcultado(String email) {
        String[] array = email.split("@");

        String emailFinal = "";

        if (array.length < 2) {
            return emailFinal;
        }

        if (array[0].length() > 5) {
            emailFinal = array[0].substring(0, 3);

            int i = array[0].length() - 3;
            for (; i > 0; i--) {
                emailFinal += "*";
            }

            emailFinal += "@" + array[1];
        }

        return emailFinal;

    }

    public void alzarArchivo(UploadedFile file, String descripcionArchivo, PedidosPersona pedido) {

        if (getSelected() != null) {

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);

            String array[] = file.getFileName().split(".");

            nombreArchivo += "_" + getSelected().getId() + "." + array[array.length - 1];

            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(par.getRutaArchivosPedidosPersona() + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            DetallesPedidoPersona archivo = new DetallesPedidoPersona();

            archivo.setPedidoPersona(pedido);
            archivo.setDescripcion(descripcionArchivo);
            archivo.setRuta(nombreArchivo);
            archivo.setFechaHoraAlta(fecha);

            detallePedidoPersonaController.setSelected(archivo);
            detallePedidoPersonaController.saveNew(null);
        }

    }

    public void saveNew() {
        if (getSelected() != null) {

            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage("Debe ingresar CI");
                return;
            } else if ("".equals(getSelected().getCi())) {
                JsfUtil.addErrorMessage("Debe ingresar CI");
                return;
            }

            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Nombres y Apellidos");
                return;
            } else if ("".equals(getSelected().getNombresApellidos())) {
                JsfUtil.addErrorMessage("Debe ingresar Nombres y Apellidos");
                return;
            }

            if (getSelected().getEmail() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Email");
                return;
            } else if ("".equals(getSelected().getEmail())) {
                JsfUtil.addErrorMessage("Debe ingresar Email");
                return;
            } else if (getSelected().getEmail().length() < 6) {
                JsfUtil.addErrorMessage("El email debe tener al menos 6 caracteres");
                return;
            }

            if (file2 == null) {
                JsfUtil.addErrorMessage("Debe ingresar una foto de la matrícula");
                return;
            }

            if (file3 == null) {
                JsfUtil.addErrorMessage("Debe ingresar una foto suya sosteniendo su cédula o su de la matrícula");
                return;
            }

            //if (!personaExiste) {
            if (getSelected().getCargoPersona() != null) {
                getSelected().setDescripcionCargoPersona(getSelected().getCargoPersona().getDescripcion());
            } else {
                getSelected().setDescripcionCargoPersona("");
            }

            /*
            if (getSelected().getDespachoPersona() != null) {
                getSelected().setDescripcionDespachoPersona(getSelected().getDespachoPersona().getDescripcion());
            } else {
                getSelected().setDescripcionDespachoPersona("");
            }
             */
            if (getSelected().getDescripcionDespachoPersona() != null) {
                if (!"".equals(getSelected().getDescripcionDespachoPersona())) {
                    getSelected().setDespachoPersona(null);
                }
            }

            if (getSelected().getTipoPersona() != null) {
                getSelected().setDescripcionTipoPersona(getSelected().getTipoPersona().getDescripcion());
            } else {
                getSelected().setDescripcionTipoPersona("");
            }

            if (getSelected().getDepartamentoPersona() != null) {
                getSelected().setDescripcionDepartamentoPersona(getSelected().getDepartamentoPersona().getDescripcion());
            } else {
                getSelected().setDescripcionDepartamentoPersona("");
            }

            if (getSelected().getLocalidadPersona() != null) {
                getSelected().setDescripcionLocalidadPersona(getSelected().getLocalidadPersona().getDescripcion());
            } else {
                getSelected().setDescripcionLocalidadPersona("");
            }

            Date fecha = ejbFacade.getSystemDate();
            getSelected().setFechaHoraPedido(fecha);
            getSelected().setEmpresa(new Empresas(1));
            getSelected().setEstado("AC");
            getSelected().setPersona(personaSelected);

            getSelected().setContrasena(contrasena1);
            getSelected().setEmailValidado(false);
            getSelected().setRealizado(false);

            MessageDigest md = null;
            String myHash = "";
            try {
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                md = MessageDigest.getInstance("MD5");

                md.update((format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }

            getSelected().setHash(myHash);
            getSelected().setFechaHoraCaducidad(ejbFacade.getSystemDate(1));
            getSelected().setTipoPedidoPersona(Constantes.TIPO_PEDIDO_PERSONA_EXPEDIENTE);

            super.saveNew(null);

            alzarArchivo(file1, "Cédula", getSelected());
            alzarArchivo(file2, "Matrícula", getSelected());
            alzarArchivo(file3, "Foto propia", getSelected());

            enviarValidacionEmail(getSelected().getId(), personaSelected, getSelected(), getSelected().getEmail(), myHash);

            if (!this.isErrorPersistencia()) {
                try {
                    /*
                    mensaje1 = "VERIFIQUE SU CORRE ELECTRÓNICO PARA VALIDAR SU PEDIDO DE USUARIO.";
                    mensaje2 = "HEMOS ENVIADO UN CORREO ELECTRÓNICO A " + getSelected().getEmail() + " CON UN LINK DE VALIDACIÓN";
                    mensaje3 = "¿HA RECIBIDO EL CORREO ELECTRÓNICO?";
                    mensaje4 = "SI NO LO HA RECIBIDO, CONSULTE A SU CARPETA SPAM, O COMUNÍQUESE A";
                    mensaje5 = "secretaria@jem.gov.py O AL 021442662, interno 224";
                     */
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/pedidosPersonaExpediente/GraciasPersonaExpediente.xhtml?email=" + getSelected().getEmail());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }

            /*
            } else {
                if (getSelected().getCargoPersona() != null) {
                    personaSelected.setCargoPersona(getSelected().getCargoPersona());
                }
                if (getSelected().getDespachoPersona() != null) {
                    personaSelected.setDespachoPersona(getSelected().getDespachoPersona());
                }
                if (getSelected().getTipoPersona() != null) {
                    personaSelected.setTipoPersona(getSelected().getTipoPersona());
                }
                if (getSelected().getDepartamentoPersona() != null) {
                    personaSelected.setDepartamentoPersona(getSelected().getDepartamentoPersona());
                }
                if (getSelected().getLocalidadPersona() != null) {
                    personaSelected.setLocalidadPersona(getSelected().getLocalidadPersona());
                }

                personaSelected.setEmailValidado(false);
                personaSelected.setHabilitarAntecedentes(false);
                personaSelected.setUsuario(usuario);
                personaSelected.setContrasena(contrasena1);

                personasController.setSelected(personaSelected);
                personasController.save2();
                if (!this.isErrorPersistencia()) {
                    try {
                        FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/pedidosPersona/GraciasPersona.xhtml");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return;
                }
            }
             */
        }

    }

    public void save2() {
        super.save(null);
    }

    public void save() {
        if (getSelected() != null) {

            if (getSelected().getTipoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_tipoPersona"));
                return;
            }

            if (getSelected().getCargoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_cargoPersona"));
                return;
            }

            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_ci"));
                return;
            }

            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_nombresApellidos"));
                return;
            }

            super.save(null);
        }
    }
}
