package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.ValidacionesReseteo;

@Named(value = "validacionesReseteoController")
@ViewScoped
public class ValidacionesReseteoController extends AbstractController<ValidacionesReseteo> {

    @Inject
    private PersonasController personasController;
    private ValidacionesReseteo val;
    private String contrasena1;
    private String contrasena2;
    private String endpoint;

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

    public ValidacionesReseteoController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(ValidacionesReseteo.class);
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
                Date fecha = ejbFacade.getSystemDate();
                val = ejbFacade.getEntityManager().createNamedQuery("ValidacionesReseteo.findByHashFechaHoraCaducidad", ValidacionesReseteo.class).setParameter("hash", hash).setParameter("fechaHoraCaducidad", fecha).getSingleResult();
            } catch (Exception e) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesReseteo/Caducado.xhtml");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }else{
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesReseteo/HashNoValido.xhtml");
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        
        if(val != null){
            if(val.getFechaHoraReseteo() != null){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesReseteo/Caducado.xhtml");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        contrasena1 = "";
        contrasena2 = "";

    }
    
    public void validar() {
        if(contrasena1 == null){
            JsfUtil.addErrorMessage("Debe Ingresar Nueva Contraseña");
            return;
        }else if ("".contains(contrasena1)){
            JsfUtil.addErrorMessage("Debe Ingresar Nueva Contraseña");
            return;
        }
        
        if(contrasena2 == null){
            JsfUtil.addErrorMessage("Debe Repetir la Nueva Contraseña");
            return;
        }else if ("".contains(contrasena2)){
            JsfUtil.addErrorMessage("Debe Repetir la Nueva Contraseña");
            return;
        }
        
        if(!contrasena1.equals(contrasena2)){
            JsfUtil.addErrorMessage("Contraseñas no coinciden");
            return;
        }
        
        if(val != null){
            Date fecha = ejbFacade.getSystemDate();
            val.setFechaHoraReseteo(fecha);
            
            setSelected(val);
            
            super.save(null);
            
            if(val.getPersona() != null){
                val.getPersona().setContrasena(contrasena1);
                
                personasController.setSelected(val.getPersona());
                personasController.save2();
            }
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesReseteo/Gracias.xhtml");
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
      }
        
    }
}
