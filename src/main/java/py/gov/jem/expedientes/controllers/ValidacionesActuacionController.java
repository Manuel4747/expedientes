package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

import py.gov.jem.expedientes.models.ExpActuaciones;
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
import py.gov.jem.expedientes.models.ParametrosSistema;

@Named(value = "validacionesActuacionController")
@ViewScoped
public class ValidacionesActuacionController extends AbstractController<ExpActuaciones> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    private String caratula;
    private String ano;
    private String causa;
    private String descripcion;
    private String fechaHora;
    private ExpActuaciones actuacion;
    private ParametrosSistema par;
    private String endpoint;

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

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
    }

    public ValidacionesActuacionController() {
        // Inform the Abstract parent controller of the concrete ExpActuaciones Entity
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
        String[] array2 = uri.split("/");
        endpoint = array2[1];

        if(hash != null){
            try {
                actuacion = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByHash", ExpActuaciones.class).setParameter("hash", hash).getSingleResult();
                
                par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

                caratula = actuacion.getDocumentoJudicial().getCaratulaString();
                causa = actuacion.getDocumentoJudicial().getCausa();
                String[] array = causa.split("-");
                ano = array[1];
                descripcion = actuacion.getDescripcion();
                
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                fechaHora = format.format(actuacion.getFechaPresentacion());
            } catch (Exception e) {
                try {
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

    @Override
    public Collection<ExpActuaciones> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findAll", ExpActuaciones.class).getResultList());
        return super.getItems2();
    }

    public void validar() {
        if(actuacion != null){
            File archivo = new File(par.getRutaArchivos() + File.separator + actuacion.getArchivo());
            
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "inline; filename=actuacion.pdf");

            try {
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                
                // FileInputStream fis = new FileInputStream(archivo); 
                
                servletOutputStream.write(Files.readAllBytes(archivo.toPath()));
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException ex) {
                Logger.getLogger(ValidacionesActuacionController.class.getName()).log(Level.SEVERE, null, ex);
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
            getSelected().setEmpresa(new Empresas(1));

            super.saveNew(event);

        }

    }
}
