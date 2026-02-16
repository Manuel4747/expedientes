package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import py.gov.jem.expedientes.models.ExpActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.Empresas;

@Named(value = "validacionesEscritoController")
@ViewScoped
public class ValidacionesEscritoController extends AbstractController<ExpActuaciones> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    private String mensaje;
    private String mensaje2;
    private ExpActuaciones actuacion;
    private String endpoint;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje2() {
        return mensaje2;
    }

    public void setMensaje2(String mensaje2) {
        this.mensaje2 = mensaje2;
    }

    public ValidacionesEscritoController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(ExpActuaciones.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        // Obtenemos el nro de telefono enviado por parametro
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String hash = params.get("hash");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        if(hash != null){
            try {
                actuacion = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByHash", ExpActuaciones.class).setParameter("hash", hash).getSingleResult();
                
                
                String fechaString = ejbFacade.getSystemDateString(actuacion.getFechaHoraAlta(), 0);
                
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String horaString = format.format(actuacion.getFechaHoraAlta());
                
                mensaje = "Código de Verificacion es Válido";
                mensaje2 = "Documento registrado electrónicamente en fecha " + fechaString + " a las " + horaString;
            } catch (Exception e) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesEscrito/HashNoValido.xhtml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }else{
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesEscrito/HashNoValido.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
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

}
