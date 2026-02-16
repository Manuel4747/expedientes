package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonasPK;
import py.gov.jem.expedientes.models.DespachosPersona;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.ExpPedidosCambioContrasena;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.ValidacionesEmail;

@Named(value = "cambioContrasenaController")
@ViewScoped
public class CambioContrasenaController extends AbstractController<ExpPedidosCambioContrasena> {

    @Inject
    private PersonasController personasController;
    @Inject
    private AntecedentesRolesPorPersonasController antecedentesRolesPorPersonasController;
    @Inject
    private DespachosPersonaController despachosPersonaController;
    private ExpPedidosCambioContrasena val;
    private String mensaje;
    private String endpoint;
    private String contrasena1;
    private String contrasena2;
    private String politicas;

    public String getPoliticas() {
        return politicas;
    }

    public void setPoliticas(String politicas) {
        this.politicas = politicas;
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public CambioContrasenaController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(ExpPedidosCambioContrasena.class);
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
        
        politicas = Utils.politicasContrasena;

        if (hash != null) {
            try {
                Date fecha = ejbFacade.getSystemDate();
                val = ejbFacade.getEntityManager().createNamedQuery("ExpPedidosCambioContrasena.findByHashFechaHoraCaducidad", ExpPedidosCambioContrasena.class).setParameter("hash", hash).setParameter("fechaHoraCaducidad", fecha).getSingleResult();
            } catch (Exception e) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/cambioContrasena/Caducado.xhtml");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/cambioContrasena/HashNoValido.xhtml");
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (val != null) {
            if (val.getFechaHoraCambioContrasena() != null) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/cambioContrasena/Caducado.xhtml");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (val.getPersona() == null) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/cambioContrasena/Caducado.xhtml");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void cambiar() {
        if (val != null) {
            if (contrasena1.equals(contrasena2)) {

                String resp = Utils.politicasContrasena(contrasena1);

                if ("".equals(resp)) {

                    if (val.getPersona() != null) {

                        String password = Utils.passwordToHash(contrasena1);
                        val.getPersona().setContrasena(password);
                        personasController.setSelected(val.getPersona());
                        personasController.save2();

                        Date fecha = ejbFacade.getSystemDate();
                        val.setFechaHoraCambioContrasena(fecha);

                        setSelected(val);

                        val.setRealizado(true);

                        super.save(null);
                        // mensaje = "SU CONTRASEÑA HA SIDO CAMBIADA CORRECTAMENTE";

                        try {
                            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/cambioContrasena/GraciasCambioContrasena.xhtml");
                            return;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JsfUtil.addErrorMessage(resp);
                }

            } else {
                JsfUtil.addErrorMessage("Contrasenas no coinciden");
            }

        }
    }

    public String volver() {
        return "/" + endpoint + "/faces/login.xhtml";
    }

}
