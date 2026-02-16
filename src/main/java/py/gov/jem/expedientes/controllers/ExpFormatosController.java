package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.ExpFormatos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.ExpTiposActuacion;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "expFormatosController")
@ViewScoped
public class ExpFormatosController extends AbstractController<ExpFormatos> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    private UploadedFile file;
    private List<ExpTiposActuacion> listaTiposActuacion;
    private HttpSession session;
    private Personas personaUsuario = null;
    private AntecedentesRoles rolElegido;
    private ParametrosSistema par;
    private String url;
    private String endpoint;
    private String ckeditorConfig;

    public String getCkeditorConfig() {
        return ckeditorConfig;
    }

    public void setCkeditorConfig(String ckeditorConfig) {
        this.ckeditorConfig = ckeditorConfig;
    }

    public List<ExpTiposActuacion> getListaTiposActuacion() {
        return listaTiposActuacion;
    }

    public void setListaTiposActuacion(List<ExpTiposActuacion> listaTiposActuacion) {
        this.listaTiposActuacion = listaTiposActuacion;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public ExpFormatosController() {
        // Inform the Abstract parent controller of the concrete ExpFormatos Entity
        super(ExpFormatos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];
        
        ckeditorConfig = url + "/config.js";

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");

        listaTiposActuacion = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_OFICIO).getResultList();
        listaTiposActuacion.add(ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PROVIDENCIA).getSingleResult());
        try{
            ExpTiposActuacion ta = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_PROVIDENCIA_DESGLOSE).getSingleResult();
            listaTiposActuacion.add(ta);
        }catch(Exception e){
            
        }
        listaTiposActuacion.add(ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_RESOLUCION).getSingleResult());

        buscar();
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
    }

    private void buscar() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("ExpFormatos.findAll", ExpFormatos.class).getResultList());
    }

    @Override
    public Collection<ExpFormatos> getItems() {
        return super.getItems2();
    }

    public void borrar(ExpFormatos item) {
        setSelected(item);
        super.delete(null);
        buscar();
    }

    public void guardar() {
        if (getSelected() != null) {
            //if (file != null) {
                //if (file.getContents().length > 0) {
                /*
                    if (par == null) {
                        JsfUtil.addErrorMessage("Error al obtener parametros");
                        return;
                    }
                */

                    Date fecha = ejbFacade.getSystemDate();
                    
                    getSelected().setFechaHoraAlta(fecha);
                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setPersonaAlta(personaUsuario);
                    getSelected().setPersonaUltimoEstado(personaUsuario);
                    
                    super.saveNew(null);
                    /*
                    byte[] bytes = null;
                    try {
                        bytes = IOUtils.toByteArray(file.getInputstream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer archivo");
                        return;
                    }
                    
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

                    String partes[] = file.getFileName().split("[.]");
                    String ext = "docx";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    String nombre = "pla" + simpleDateFormat.format(fecha) + "_" + getSelected().getId() + "." + ext;
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(par.getRutaPlantillas() + File.separator + nombre);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al crear archivo");
                        return;
                    }

                    try {
                        output.write(bytes);
                        output.flush();
                        output.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al guardar archivo");
                        return;
                    }
                    
                    getSelected().setArchivo(nombre);
                    
                    super.save(null);
*/
                    buscar();
                //}
            //}
        }
    }
    
    public void guardarEdit() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(personaUsuario);

            super.save(null);
            buscar();
        }
    }
}
