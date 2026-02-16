package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

import py.gov.jem.expedientes.models.ExpActasSesion;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ParametrosSistema;

@Named(value = "validacionesActaSesionController")
@ViewScoped
public class ValidacionesActaSesionController extends AbstractController<ExpActasSesion> {

    @Inject
    private ExpActuacionesController actuacionesController;
    private String caratula;
    private String ano;
    private String causa;
    private String descripcion;
    private String fechaHora;
    private ExpActasSesion actuacion;
    private ParametrosSistema par;
    private String endpoint;
    private SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public ExpActasSesion getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActasSesion actuacion) {
        this.actuacion = actuacion;
    }

    public ValidacionesActaSesionController() {
        // Inform the Abstract parent controller of the concrete ExpActuaciones Entity
        super(ExpActasSesion.class);
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
        String[] array2 = uri.split("/");
        endpoint = array2[1];

        if(hash != null){
            try {
                actuacion = ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findByHashEstado", ExpActasSesion.class).setParameter("hash", hash).setParameter("estado", Constantes.ESTADO_ACTA_SESION_FINALIZADA).getSingleResult();
                
                par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

                caratula = "";
                causa = "";
                //String[] array = causa.split("-");
                //ano = array[1];
                ano = "";
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                descripcion = "Acta de sesión " + actuacion.getTipoSesion().getDescripcion() + " de fecha " + format.format(actuacion.getFechaSesion());;
                
                List<ExpActuaciones> act = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByActaSesion", ExpActuaciones.class).setParameter("actaSesion", actuacion).getResultList();
                
                if(!act.isEmpty()){
                    fechaHora = format.format(act.get(0).getFechaPresentacion());
                }
                
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/HashNoValido.xhtml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }else{
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/HashNoValido.xhtml");
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
    }

    @Override
    public Collection<ExpActasSesion> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findAll", ExpActasSesion.class).getResultList());
        return super.getItems2();
    }

    public void validar() {
        if(actuacion != null){
            
            
            File archivo = new File(par.getRutaActas() + File.separator + actuacion.getArchivo());
            
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=actuacion.pdf");

            try {
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                
                // FileInputStream fis = new FileInputStream(archivo); 
                
                servletOutputStream.write(Files.readAllBytes(archivo.toPath()));
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException ex) {
                Logger.getLogger(ValidacionesActaSesionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setFechaHoraAlta(fecha);

            super.saveNew(event);

        }

    }
}
