package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import py.gov.jem.expedientes.models.Antecedentes;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.Empresas;

@Named(value = "validacionController")
@ViewScoped
public class ValidacionController extends AbstractController<Antecedentes> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    private String nombresApellidos;
    private String codigo;
    private Antecedentes ant;
    private String endpoint;

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public ValidacionController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(Antecedentes.class);
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
                ant = ejbFacade.getEntityManager().createNamedQuery("Antecedentes.findByHash", Antecedentes.class).setParameter("hash", hash).getSingleResult();
                
                
                Date fecha = ejbFacade.getSystemDate();
                
                long diffInMillies = Math.abs(fecha.getTime() - ant.getFechaHoraAlta().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                if(diff > 30){
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/Caducado.xhtml");
                    return;
                }
                nombresApellidos = ant.getPersona().getNombresApellidos();
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

        codigo = "";

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
    public Collection<Antecedentes> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("Antecedentes.findAll", Antecedentes.class).getResultList());
        return super.getItems2();
    }

    public void validar() {
        if(codigo == null){
            JsfUtil.addErrorMessage("Debe ingresar Codigo Verificacion");
            return;
        }else if ("".contains(codigo)){
            JsfUtil.addErrorMessage("Debe ingresar Codigo Verificacion");
            return;
        }
        
        if(ant != null){
            if(codigo.equals(ant.getCodigoArchivo())){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/ant/" + ant.getPathArchivo() );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            }else{
                JsfUtil.addErrorMessage("Codigo no verifica");
                return;
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
