package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpPedidosCambioContrasena;
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
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "pedidosCambioContrasenaController")
@ViewScoped
public class ExpPedidoCambioContrasenaController extends AbstractController<ExpPedidosCambioContrasena> {

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
    @Inject
    private DetallesPedidoPersonaController detallePedidoPersonaController;
    private HttpSession session;
    private Personas persona;
    private Personas personaSelected;
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
    private boolean actualizar;


    public ExpPedidoCambioContrasenaController() {
        // Inform the Abstract parent controller of the concrete ExpPedidosCambioContrasena Entity
        super(ExpPedidosCambioContrasena.class);
    }

    public CargosPersonaController getCargoPersonaController() {
        return cargoPersonaController;
    }

    public void setCargoPersonaController(CargosPersonaController cargoPersonaController) {
        this.cargoPersonaController = cargoPersonaController;
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
        leyenda = "Enviar Pedido";
        actualizar = false;

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        cargoPersonaController.setSelected(null);
        empresaController.setSelected(null);
    }

    @Override
    public ExpPedidosCambioContrasena prepareCreate(ActionEvent event) {
        ExpPedidosCambioContrasena doc = super.prepareCreate(event);
        return doc;
    }

    @Override
    public Collection<ExpPedidosCambioContrasena> getItems() {
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

    public void enviarValidacionEmail(Integer id, Personas per, ExpPedidosCambioContrasena ped, String email, Date fechaCaducidad, String myHash) {

        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("hh:mm");
        
        BodyPart texto = new MimeBodyPart();
        try {
            texto.setContent("<p>Hola " + per.getNombresApellidos() + "<br><br> "
                    + "     Ud recibió este email porque solicitó resetear su contraseña. Si no lo ha solicitado, por favor, ignore este email.<br><br>"
                    + "Para resetear su contraseña debe ingresar al siguiente link hasta el " + format.format(fechaCaducidad) + " a las " + format2.format(fechaCaducidad) + " para que no caduque su pedido<br><br>"
                    + "<a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_CAMBIO_CONTRASENA + "?hash=" + myHash + "'>CLICK AQUÍ</a>.<br><br>",
                    "text/html; charset=utf-8");

            Utils.sendEmailAsync(par.getIpServidorEmail(),
                    par.getPuertoServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    par.getContrasenaServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    email,
                    "Reseteo de contraseña para Sistema de Expedientes Electrónicos JEM",
                    texto);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return;
        }

    }

    public void saveNew() {
        if (getSelected() != null) {

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

            List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByEmail", Personas.class).setParameter("email", getSelected().getEmail()).getResultList();

            if (lista.isEmpty()) {
                JsfUtil.addErrorMessage("El email no existe en nuestra base de datos");
                return;
            } else if (lista.size() > 1) {
                JsfUtil.addErrorMessage("El email existe mas de una vez en nuestra base de datos. Contacte con el JEM");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setPersona(lista.get(0));

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
            getSelected().setFechaHoraCaducidad(ejbFacade.getSystemDateMinutes(60));

            super.saveNew(null);

            enviarValidacionEmail(getSelected().getId(), getSelected().getPersona(), getSelected(), getSelected().getEmail(), getSelected().getFechaHoraCaducidad(), myHash);

            if (!this.isErrorPersistencia()) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/pedidosCambioContrasena/GraciasPedidosCambioContrasena.xhtml?email=" + getSelected().getEmail());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }

        }

    }

    public void save2() {
        super.save(null);
    }
}
