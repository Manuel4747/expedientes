package py.gov.jem.expedientes.controllers;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpSorteos;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.CantidadItem;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "sorteosController")
@ViewScoped
public class ExpSorteosController extends AbstractController<ExpSorteos> {
    @Inject
    private ParametrosSistemaController parametrosSistemaController;

    private List<Personas> listaSorteo;
    private List<Personas> listaSorteoActual;
    private Personas sorteado;
    private String imagenSorteo;
    private ParametrosSistema par;
    private HttpSession session;
    private Personas personaUsuario;

    public List<Personas> getListaSorteoActual() {
        return listaSorteoActual;
    }

    public void setListaSorteoActual(List<Personas> listaSorteoActual) {
        this.listaSorteoActual = listaSorteoActual;
    }

    public List<Personas> getListaSorteo() {
        return listaSorteo;
    }

    public void setListaSorteo(List<Personas> listaSorteo) {
        this.listaSorteo = listaSorteo;
    }

    public Personas getSorteado() {
        return sorteado;
    }

    public void setSorteado(Personas sorteado) {
        this.sorteado = sorteado;
    }

    public String getImagenSorteo() {
        return imagenSorteo;
    }

    public void setImagenSorteo(String imagenSorteo) {
        this.imagenSorteo = imagenSorteo;
    }
    
    public ExpSorteosController() {
        // Inform the Abstract parent controller of the concrete ExpSorteos Entity
        super(ExpSorteos.class);
    }
    
    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        imagenSorteo = "images/sorteo_inicio2.jpg";
        sorteado = null;
        
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        personaUsuario = (Personas) session.getAttribute("Persona");
        
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public Personas getRandomElement(List<Personas> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public void prepareSorteo() {
        sorteado = null;
        imagenSorteo = "images/sorteo_inicio2.jpg";
        /*
        Date fecha = ejbFacade.getSystemDate();
        
        if(par != null){
            par.setFechaHoraReseteoSorteo(fecha);
            parametrosSistemaController.setSelected(par);
            parametrosSistemaController.save(null);
            
        }
*/
        
        listaSorteo = obtenerTodosFiscales();
    }
    
    private int obtenerCantSorteo(Personas per){
        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_sorteos where persona_sorteada = ?1 and fecha_hora_alta >= ?2", CantidadItem.class).setParameter(1, per.getId()).setParameter(2, par.getFechaHoraReseteoSorteo());
        CantidadItem cantidadItem = (CantidadItem) query.getSingleResult();
        return cantidadItem.getCantidad();
    }
    
    private Personas obtenerUltimoSorteado(){
        javax.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery("select p.* from personas as p where p.id in (select e.persona_sorteada from exp_sorteos as e where e.fecha_hora_alta in (select max(s.fecha_hora_alta) from exp_sorteos as s))", Personas.class);
        List<Personas> lista = query.getResultList();
        if(lista.isEmpty()){
            return null;
        }else{
            return lista.get(0);
        }
    }

    private List<Personas> obtenerTodosFiscales(){
        List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_FISCAL_ACUSADOR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
        
        for(Personas per : lista){
            per.setCantSorteos(obtenerCantSorteo(per));
        }
        
        return lista;
    }
    
    public boolean modoDebug(){
        return par.isSorteoModoDebug();
    }
    
    private List<Personas> obtenerFiscales(){
        List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_FISCAL_ACUSADOR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
        
        Personas ultima = obtenerUltimoSorteado();
        
        int cantidadMin = 1000000;
        
        for(Personas per : lista){
            int cant = obtenerCantSorteo(per);
            
            if(cant < cantidadMin && per.isSorteo() && !per.equals(ultima)){
                cantidadMin = cant;
            }
        }
        
        if(cantidadMin == 1000000){
            return lista;
        }else{
            List<Personas> listaFinal = new ArrayList<>();
            for(Personas per : lista){
                int cant = obtenerCantSorteo(per);

                if(cant == cantidadMin && per.isSorteo() && !per.equals(ultima)){
                    per.setCantSorteos(cant);
                    listaFinal.add(per);
                }
            }
            
            return listaFinal;
        }
    }
    

    public void resetearSorteo() {
        imagenSorteo = "images/sorteo_final2.jpg";
    }

    public void sortear() {
        if (listaSorteo != null) {
            imagenSorteo = "images/sorteo2.gif";
            
            listaSorteoActual = obtenerTodosFiscales();
            
            if(listaSorteoActual.isEmpty()){
                JsfUtil.addErrorMessage("Faltan fiscales");
                return;
            }
            
            sorteado = getRandomElement(listaSorteoActual);
            
            ExpSorteos sort = new ExpSorteos();
            
            
            Date fecha = ejbFacade.getSystemDate();
            
            sort.setFechaHoraAlta(fecha);
            sort.setFechaHoraUltimoEstado(fecha);
            sort.setPersonaAlta(personaUsuario);
            sort.setPersonaUltimoEstado(personaUsuario);
            sort.setPersonaSorteada(sorteado);
            
            setSelected(sort);
            super.saveNew(null);
            
            
            listaSorteo = obtenerTodosFiscales();
            
            PrimeFaces.current().executeScript("startTimer();");
        }
    }
}