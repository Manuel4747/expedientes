package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.primefaces.PrimeFaces;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.DepartamentosPersona;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.LocalidadesPersona;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.ValidacionesEmail;
import py.gov.jem.expedientes.models.ValidacionesReseteo;

@Named(value = "pedidosPersonaController")
@ViewScoped
public class PedidosPersonaController extends AbstractController<PedidosPersona> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private ValidacionesEmailController validacionesEmailController;
    @Inject
    private ValidacionesReseteoController validacionesReseteoController;
    @Inject
    private PersonasController personasController;
    @Inject
    private EmpresasController empresaController;
    private HttpSession session;
    private Personas persona;
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

    public PedidosPersonaController() {
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

        if (session != null) {
            persona = (Personas) session.getAttribute("Persona");
        }
        prepareCreate(null);
        cargoPersonaRadio = "1";
        despachoPersonaRadio = "1";
        departamentoPersonaRadio = "1";
        localidadPersonaRadio = "1";
        deshabilitarEdicionDespachoPersona = true;
        deshabilitarBoton = true;
        leyenda = "Enviar Pedido";

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

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
        /*
        if (getSelected() != null) {
            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    return false;
                }
            }
        }
        */
        return true;
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
                        leyenda = "Crear Usuario";

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
                        leyenda = "Enviar Pedido";
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
                        e.printStackTrace();
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
                        + "     Gracias por registrarse en “Antecedentes del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Su cuenta ha sido creada y debe ser validada antes de su uso<br><br>"
                        + "Para validar la cuenta <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_EMAIL + "?hash=" + myHash + "'>CLICK AQUÍ</a>.<br><br>"
                        + "Tras la validación, ya podrá acceder a <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "'>" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "</a> usando el siguiente usuario y contraseña:<br><br>"
                        + "Usuario: " + ped.getUsuario() + "<br><br>"
                        + "     Obs: El usuario y contraseña suministrado para la operación de los sistemas de la institución, son personales e intransferibles<br>"
                        + "y constituye su firma electrónica; en tal sentido asume las características referidas a la firma electrónica en el marco legal correspondiente.<br><br>"
                        , "text/html; charset=utf-8");
/*
                Hola,………………
Gracias por registrarse en “Antecedentes del Jurado de Enjuiciamiento de Magistrados”. Su cuenta ha sido creada y debe ser validada antes de su uso. Para validar la cuenta CLICK AQUÍ.
Tras la validación, ya podrá acceder a ____________ (enlace de antecedentes) usando el siguiente usuario y contraseña:

Usuario: que le aparezca su número de cédula.
	Obs: El usuario y contraseña suministrado para la operación de los sistemas de la institución, son personales e intransferibles y constituye su firma electrónica; en tal sentido asume las características referidas a la firma electrónica en el marco legal correspondiente.
                
                */
                Utils.sendEmail(par.getIpServidorEmail(),
                        par.getPuertoServidorEmail(),
                        par.getUsuarioServidorEmail(),
                        par.getContrasenaServidorEmail(),
                        par.getUsuarioServidorEmail(),
                        email,
                        "Usuario para Sistema de Antecedentes JEM",
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
            val.setFechaHoraCaducidad(ejbFacade.getSystemDate(3));

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

    public void saveNew() {
        if (getSelected() != null) {
/*
            boolean personaExiste = false;

            if (personaSelected != null) {
                if (personaSelected.getUsuario() != null) {
                    if (!"".equals(personaSelected.getUsuario())) {
                        if (personaSelected.getEmail() != null) {
                            if (!"".equals(personaSelected.getEmail())) {

                                String email = "";
                                try {
                                    email = obtenerEmailOcultado(personaSelected.getEmail());
                                } catch (Exception e) {

                                }

                                if (!"".equals(email)) {
                                    avisoReseteo = "El usuario " + personaSelected.getNombresApellidos() + " ya tiene un usuario creado.";
                                    avisoReseteo2 = "Si se olvido su usuario y/o contraseña haga click en Enviar Reseteo";
                                    avisoReseteo3 = "y le enviaremos un link de reseteo de contraseña a su email registrado (" + email + ").";
                                    PrimeFaces.current().ajax().update("EnviarEmailForm");
                                    PrimeFaces current = PrimeFaces.current();
                                    current.executeScript("PF('EnviarEmailDialog').show();");
                                    return;
                                }
                            } else {
                                // Email esta en blanco
                            }
                        } else {
                            // Email es nulo
                        }
                    } else {
                        personaExiste = true;
                    }
                } else {
                    personaExiste = true;
                }
            }
*/

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

            if (getSelected().getUsuario() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Usuario");
                return;
            } else if ("".equals(getSelected().getUsuario())) {
                JsfUtil.addErrorMessage("Debe ingresar Usuario");
                return;
            }else if(getSelected().getUsuario().length() < 4){
                JsfUtil.addErrorMessage("El usuario debe tener al menos 4 caracteres");
                return;
            }

            if (getSelected().getEmail() == null) {
                JsfUtil.addErrorMessage("Debe ingresar Email");
                return;
            } else if ("".equals(getSelected().getEmail())) {
                JsfUtil.addErrorMessage("Debe ingresar Email");
                return;
            }else if(getSelected().getEmail().length() < 6){
                JsfUtil.addErrorMessage("El email debe tener al menos 6 caracteres");
                return;
            }

            if (contrasena1 == null) {
                JsfUtil.addErrorMessage("Debe Ingresar Nueva Contraseña");
                return;
            } else if ("".contains(contrasena1)) {
                JsfUtil.addErrorMessage("Debe Ingresar Nueva Contraseña");
                return;
            }else if(contrasena1.length() < 6){
                JsfUtil.addErrorMessage("El contraseña debe tener al menos 6 caracteres");
                return;
            }

            if (contrasena2 == null) {
                JsfUtil.addErrorMessage("Debe Repetir la Nueva Contraseña");
                return;
            } else if ("".contains(contrasena2)) {
                JsfUtil.addErrorMessage("Debe Repetir la Nueva Contraseña");
                return;
            }else if(contrasena2.length() < 6){
                JsfUtil.addErrorMessage("El contraseña debe tener al menos 6 caracteres");
                return;
            }

            if (!contrasena1.equals(contrasena2)) {
                JsfUtil.addErrorMessage("Contraseñas no coinciden");
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
            
            if(getSelected().getDescripcionDespachoPersona() != null){
                if(!"".equals(getSelected().getDescripcionDespachoPersona())){
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
            getSelected().setFechaHoraCaducidad(ejbFacade.getSystemDate(3));
            
            super.saveNew(null);

            enviarValidacionEmail(getSelected().getId(), personaSelected, getSelected(), getSelected().getEmail(), myHash);
            
            

            if (!this.isErrorPersistencia()) {
                try {
                    mensaje1 = "VERIFIQUE SU CORRE ELECTRÓNICO PARA ACCEDER A SU CUENTA.";
                    mensaje2 = "HEMOS ENVIADO UN CORREO ELECTRÓNICO A " + getSelected().getEmail() + " CON UN LINK DE VALIDACION";
                    mensaje3 = "¿HA RECIBIDO EL CORREO ELECTRÓNICO?";
                    mensaje4 = "SI NO LO HA RECIBIDO, CONSULTE A SU CARPETA SPAM, O COMUNÍQUESE A";
                    mensaje5 = "secretaria@jem.gov.py O AL 021442662, interno 224";
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/pedidosPersona/GraciasPersona.xhtml?email=" + getSelected().getEmail());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return ;
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
