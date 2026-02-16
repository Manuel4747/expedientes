package py.gov.jem.expedientes.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.Antecedentes;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.ExpPedidosAntecedente;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "generarAntecedenteController")
@ViewScoped
public class GenerarAntecedenteController extends AbstractController<Antecedentes> {

    private HttpSession session;
    private Personas per;
    private Personas personaSelected;
    private List<Antecedentes> listaAntecedentes;
    private ParametrosSistema par;
    private String endpoint;
    private List<Personas> listaPersonas;
    private UploadedFile file;
    private final FiltroURL filtroURL = new FiltroURL();
    @Inject
    private AntecedentesController antecedentesController;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public List<Antecedentes> getListaAntecedentes() {
        return listaAntecedentes;
    }

    public void setListaAntecedentes(List<Antecedentes> listaAntecedentes) {
        this.listaAntecedentes = listaAntecedentes;
    }

    public Personas getPersonaSelected() {
        return personaSelected;
    }

    public void setPersonaSelected(Personas personaSelected) {
        this.personaSelected = personaSelected;
    }

    public GenerarAntecedenteController() {
        super(Antecedentes.class);

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
        
        personaSelected = null;

        per = (Personas) session.getAttribute("Persona");
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

        listaPersonas = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_ANTECEDENTES).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public void prepareAlzarArchivo(ExpPedidosAntecedente ped) {
        // selectedPedido = ped;
    }
    
    public boolean deshabilitarAdminAntecedentes(){
        return !filtroURL.verifPermiso("adminAntecedentes");
    }
    
    public void borrarAntecedente(Antecedentes item){
        if(antecedentesController.borrarAntecedente(item)){
            buscarAntecedentes();
        }
    }
    
    public void buscarAntecedentes() {
        List<Antecedentes> lista = this.ejbFacade.getEntityManager().createNamedQuery("Antecedentes.findByPersona", Antecedentes.class).setParameter("persona", personaSelected).getResultList();
        
        listaAntecedentes = new ArrayList<>();
        if(lista.size() > 5){
            Antecedentes ant = null;
            for(int i = 0; i < 5; i++){
                listaAntecedentes.add(lista.get(i));
            }
        }else{
            listaAntecedentes = lista;
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
