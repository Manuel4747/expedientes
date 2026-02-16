package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.ExpNotificaciones;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import py.gov.jem.expedientes.models.ExpRolesFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.Firmas;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "expPersonasFirmantesPorActuacionesController")
@ViewScoped
public class ExpPersonasFirmantesPorActuacionesController extends AbstractController<ExpPersonasFirmantesPorActuaciones> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    ExpPersonasFirmantesPorActuacionesController personasFirmantesPorActuacionesController;
    @Inject
    FirmasController firmasController;

    private List<ExpPersonasFirmantesPorActuaciones> listaFirmantes;
    private List<ExpPersonasFirmantesPorActuaciones> listaFirmados;
    private List<ExpPersonasFirmantesPorActuaciones> listaMultiFirma;
    private HttpSession session;
    private Personas personaUsuario = null;
    private String sessionId;
    private String sessionIdFirma;
    private AntecedentesRoles rolElegido;
    private final FiltroURL filtroURL = new FiltroURL();
    private String endpoint;
    private boolean filtrarResoluciones;
    private boolean filtrarProvidencias;
    private boolean filtrarOficios;
    private boolean filtrarOtros;
    private boolean filtrarSoloRevisados;

    public boolean isFiltrarResoluciones() {
        return filtrarResoluciones;
    }

    public void setFiltrarResoluciones(boolean filtrarResoluciones) {
        this.filtrarResoluciones = filtrarResoluciones;
    }

    public boolean isFiltrarProvidencias() {
        return filtrarProvidencias;
    }

    public void setFiltrarProvidencias(boolean filtrarProvidencias) {
        this.filtrarProvidencias = filtrarProvidencias;
    }

    public boolean isFiltrarOficios() {
        return filtrarOficios;
    }

    public void setFiltrarOficios(boolean filtrarOficios) {
        this.filtrarOficios = filtrarOficios;
    }

    public boolean isFiltrarOtros() {
        return filtrarOtros;
    }

    public void setFiltrarOtros(boolean filtrarOtros) {
        this.filtrarOtros = filtrarOtros;
    }

    public boolean isFiltrarSoloRevisados() {
        return filtrarSoloRevisados;
    }

    public void setFiltrarSoloRevisados(boolean filtrarSoloRevisados) {
        this.filtrarSoloRevisados = filtrarSoloRevisados;
    }

    public List<ExpPersonasFirmantesPorActuaciones> getListaFirmantes() {
        return listaFirmantes;
    }

    public void setListaFirmantes(List<ExpPersonasFirmantesPorActuaciones> listaFirmantes) {
        this.listaFirmantes = listaFirmantes;
    }

    public List<ExpPersonasFirmantesPorActuaciones> getListaFirmados() {
        return listaFirmados;
    }

    public void setListaFirmados(List<ExpPersonasFirmantesPorActuaciones> listaFirmados) {
        this.listaFirmados = listaFirmados;
    }

    public List<ExpPersonasFirmantesPorActuaciones> getListaMultiFirma() {
        return listaMultiFirma;
    }

    public void setListaMultiFirma(List<ExpPersonasFirmantesPorActuaciones> listaMultiFirma) {
        this.listaMultiFirma = listaMultiFirma;
    }

    public String getSessionIdFirma() {
        return sessionIdFirma;
    }

    public void setSessionIdFirma(String sessionIdFirma) {
        this.sessionIdFirma = sessionIdFirma;
    }

    public ExpPersonasFirmantesPorActuacionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpPersonasFirmantesPorActuaciones.class);
    }

    @Override
    public Collection<ExpPersonasFirmantesPorActuaciones> getItems() {
        return super.getItems2();
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();
        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        filtrarResoluciones = (boolean) ((session.getAttribute("filtrarResoluciones") == null) ? true : session.getAttribute("filtrarResoluciones"));
        filtrarProvidencias = (boolean) ((session.getAttribute("filtrarProvidencias") == null) ? true : session.getAttribute("filtrarProvidencias"));
        filtrarOficios = (boolean) ((session.getAttribute("filtrarOficios") == null) ? true : session.getAttribute("filtrarOficios"));
        filtrarOtros = (boolean) ((session.getAttribute("filtrarOtros") == null) ? true : session.getAttribute("filtrarOtros"));
        filtrarSoloRevisados = (boolean) ((session.getAttribute("filtrarSoloRevisados") == null) ? false : session.getAttribute("filtrarSoloRevisados"));

        buscar(personaUsuario, rolElegido, filtrarResoluciones, filtrarProvidencias, filtrarOficios, filtrarOtros, filtrarSoloRevisados);

    }

    private void buscar(Personas persona, AntecedentesRoles rol, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios, boolean incluirOtros, boolean incluirSoloRevisados) {
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarResoluciones", incluirResoluciones);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarProvidencias", incluirProvidencias);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarOficios", incluirOficios);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarOtros", incluirOtros);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarSoloRevisados", incluirSoloRevisados);

        setItems(obtenerPersonasFirmantes(persona, incluirResoluciones, incluirProvidencias, incluirOficios, incluirOtros, incluirSoloRevisados));

        listaFirmados = obtenerDocFirmados(persona);
/*
        List<ExpRolesFirmantesPorActuaciones> lista2 = obtenerRolesFirmantes(rol, true, true, true, true);

        List<ExpPersonasFirmantesPorActuaciones> listaRolesFirmantes = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);
        Date fecha = ejbFacade.getSystemDate();
        int contador = 0;

        ExpPersonasFirmantesPorActuaciones per2 = null;
        for (ExpRolesFirmantesPorActuaciones per : lista2) {
            Integer id = Integer.valueOf(simpleDateFormat.format(fecha)) + contador;
            per2 = new ExpPersonasFirmantesPorActuaciones(id, per, persona);
            listaRolesFirmantes.add(per2);
            contador++;
        }

        getItems2().addAll(listaRolesFirmantes);
*/
    }

    public void buscar() {
        buscar(personaUsuario, rolElegido, filtrarResoluciones, filtrarProvidencias, filtrarOficios, filtrarOtros, filtrarSoloRevisados);
    }

    public List<ExpPersonasFirmantesPorActuaciones> obtenerPersonasFirmantes(Personas per, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios, boolean incluirOtros, boolean incluirSoloRevisados) {
        // return ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", per).setParameter("firmado", false).getResultList();
        /*
        List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", per).setParameter("firmado", false).getResultList();
        List<ExpPersonasAsociadas> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", per.getId()).getResultList();
        for(ExpPersonasAsociadas perAso : lista2){
            List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", perAso.getPersona()).setParameter("firmado", false).getResultList();
            lista.addAll(lista3);
        }
        
        return lista;
         */

        String incluir = "";
        String incluirMas = "";
        String soloRevisados = "";

        if (incluirSoloRevisados) {
            soloRevisados += " and p.revisado = true ";
        }

        if (incluirResoluciones) {
            incluir += Constantes.TIPO_ACTUACION_RESOLUCION;
        }

        if (incluirProvidencias) {
            if (!"".equals(incluir)) {
                incluir += ", ";
            }
            incluir += Constantes.TIPO_ACTUACION_PROVIDENCIA;
        }

        if (incluirOficios) {
            if (!"".equals(incluir)) {
                incluir += ", ";
            }
            incluir += Constantes.TIPO_ACTUACION_OFICIO + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE;
        }

        if (!"".equals(incluir)) {
            incluir = " a.tipo_actuacion in (" + incluir + ")";
        }

        if (incluirOtros) {
            incluirMas = " a.tipo_actuacion not in (" + Constantes.TIPO_ACTUACION_RESOLUCION + ", " + Constantes.TIPO_ACTUACION_PROVIDENCIA + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE + ", " + Constantes.TIPO_ACTUACION_OFICIO + ")";
        }

        // String comando = "select p.* from exp_personas_firmantes_por_actuaciones as p, exp_actuaciones as a where p.actuacion = a.id " + (("".equals(incluir) && ("".equals(incluirMas)) ? " and 1 = 0 " : ((!"".equals(incluir) || (!"".equals(incluirMas))) ? " and (" : "") + incluir + ((!"".equals(incluir) && (!"".equals(incluirMas))) ? " or " : "") + incluirMas + ((!"".equals(incluir) || (!"".equals(incluirMas))) ? ")" : ""))) + " and p.firmado = ?1 and p.estado = 'AC' and (p.persona_firmante = ?2 or p.persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) order by p.fecha_hora_alta asc";
           String comando = "select p.* from exp_personas_firmantes_por_actuaciones as p, exp_actuaciones as a where a.formato is null " + soloRevisados + " and p.actuacion = a.id " + (("".equals(incluir) && ("".equals(incluirMas)) ? " and 1 = 0 " : ((!"".equals(incluir) || (!"".equals(incluirMas))) ? " and (" : "") + incluir + ((!"".equals(incluir) && (!"".equals(incluirMas))) ? " or " : "") + incluirMas + ((!"".equals(incluir) || (!"".equals(incluirMas))) ? ")" : ""))) + " and p.firmado = ?1 and p.estado = 'AC' and (p.persona_firmante = ?2 or p.persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) and (ifnull(a.preopinante,?4) = ?5 or (select ifnull(f.firmado,true) from exp_personas_firmantes_por_actuaciones as f where f.actuacion = p.actuacion and f.persona_firmante = a.preopinante and f.estado = 'AC'))  order by p.fecha_hora_alta asc";
        return ejbFacade.getEntityManager().createNativeQuery(comando, ExpPersonasFirmantesPorActuaciones.class).setParameter(1, false).setParameter(2, per.getId()).setParameter(3, per.getId()).setParameter(4, per.getId()).setParameter(5, per.getId()).getResultList();
        // return ejbFacade.getEntityManager().createNativeQuery("select p.* from exp_personas_firmantes_por_actuaciones as p where firmado = ?1 And estado = 'AC' and (persona_firmante = ?2 or persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) order by p.fecha_hora_alta desc", ExpPersonasFirmantesPorActuaciones.class).setParameter(1, false).setParameter(2, per.getId()).setParameter(3, per.getId()).getResultList();
    }

    public List<ExpPersonasFirmantesPorActuaciones> obtenerDocFirmados(Personas per) {
        // return ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", per).setParameter("firmado", false).getResultList();
        /*
        List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", per).setParameter("firmado", true).getResultList();
        List<ExpPersonasAsociadas> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", per.getId()).getResultList();
        for(ExpPersonasAsociadas perAso : lista2){
            List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", perAso.getPersona()).setParameter("firmado", true).getResultList();
            lista.addAll(lista3);
        }
        return lista;
         */

        return ejbFacade.getEntityManager().createNativeQuery("select p.* from exp_personas_firmantes_por_actuaciones as p where firmado = ?1 and (persona_firmante = ?2 or persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) order by p.fecha_hora_firma desc", ExpPersonasFirmantesPorActuaciones.class).setParameter(1, true).setParameter(2, per.getId()).setParameter(3, per.getId()).getResultList();

    }
/*
    public List<ExpRolesFirmantesPorActuaciones> obtenerRolesFirmantes(AntecedentesRoles rol, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios, boolean incluirOtros) {

        String incluir = "";
        String incluirMas = "";

        if (incluirResoluciones) {
            incluir += Constantes.TIPO_ACTUACION_RESOLUCION;
        }

        if (incluirProvidencias) {
            if (!"".equals(incluir)) {
                incluir += ", ";
            }
            incluir += Constantes.TIPO_ACTUACION_PROVIDENCIA;
        }

        if (incluirOficios) {
            if (!"".equals(incluir)) {
                incluir += ", ";
            }
            incluir += Constantes.TIPO_ACTUACION_OFICIO_CORTE + ", " + Constantes.TIPO_ACTUACION_OFICIO;
        }

        if (!"".equals(incluir)) {
            incluir = " p.tipo_actuacion in (" + incluir + ")";
        }

        if (incluirOtros) {
            incluirMas = " a.tipo_actuacion not in (" + Constantes.TIPO_ACTUACION_RESOLUCION + ", " + Constantes.TIPO_ACTUACION_PROVIDENCIA + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE + ", " + Constantes.TIPO_ACTUACION_OFICIO + ")";
        }

        // return ejbFacade.getEntityManager().createNativeQuery("select p.* from exp_personas_firmantes_por_actuaciones as p, exp_actuaciones as a where p.actuacion = a.id and (" + incluir + ((!"".equals(incluir))?" or ":"") + incluirMas + ") and p.firmado = ?1 and p.estado = 'AC' and (p.persona_firmante = ?2 or p.persona_firmante in (select persona from exp_personas_asociadas where persona_asociada = ?3)) order by p.fecha_hora_alta asc", ExpPersonasFirmantesPorActuaciones.class).setParameter(1, false).setParameter(2, per.getId()).setParameter(3, per.getId()).getResultList();
        return ejbFacade.getEntityManager().createNamedQuery("ExpRolesFirmantesPorActuaciones.findByRolFirmanteFirmado", ExpRolesFirmantesPorActuaciones.class).setParameter("rolFirmante", rol).setParameter("firmado", false).getResultList();
    }
*/
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    public String datePattern() {
        return "yyyy/MM/dd";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public String datePattern2() {
        return "yyyy";
    }

    public String customFormatDate2(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern2());
            return format.format(date);
        }
        return "";
    }

    public String datePattern3() {
        return "yyyy/MM/dd HH:mm:ss";
    }

    public String datePattern4() {
        return "yyyy/MM/dd HH:mm";
    }

    public String customFormatDate4(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern4());
            return format.format(date);
        }
        return "";
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    public void navigateActuacion(ExpPersonasFirmantesPorActuaciones not) {
        try {

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarResoluciones", filtrarResoluciones);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarProvidencias", filtrarProvidencias);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarOficios", filtrarOficios);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarOtros", filtrarOtros);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarSoloRevisados", filtrarSoloRevisados);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", String.valueOf(not.getActuacion().getId()));
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudicialesPorSecretaria/index.xhtml?tipo=consulta");
        } catch (IOException ex) {
        }
    }

    public boolean deshabilitarPendientesFirma() {

        if (filtroURL.verifPermiso(Constantes.PERMISO_VER_BANDEJA_PENDIENTES_FIRMA, rolElegido.getId())){
                // || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())
                // || filtroURL.verifPermiso(Constantes.PERMISO_FIRMAR, rolElegido.getId())){
                // || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())) {
            // return (getItems2() == null)?true:(getItems2().isEmpty());
            
            return false;
        }

        return true;
    }
    
    public boolean deshabilitarMultiFirma() {

        if ((filtroURL.verifPermiso(Constantes.PERMISO_FIRMAR, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_RESTRINGIDO_REVISADO_EXPEDIENTE, rolElegido.getId())
                || filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_REVISADO_EXPEDIENTE, rolElegido.getId())) &&
                filtroURL.verifPermiso(Constantes.PERMISO_MULTIFIRMA, rolElegido.getId())) {
            // return (getItems2() == null)?true:(getItems2().isEmpty());
            return false;
        }

        return true;
    }
    
    public void actualizarFirmarMultiple(ExpPersonasFirmantesPorActuaciones item){
        setSelected(item);
        super.save(null);
        setSelected(null);
    }
    
    public void firmarMultiple(){

        Date fecha = ejbFacade.getSystemDate();
        Firmas firma = null;

        boolean resp = false;
        if (firma == null) {
            
            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmarMultiple", ExpPersonasFirmantesPorActuaciones.class).setParameter("personaFirmante", personaUsuario).setParameter("firmarMultiple", true).getResultList();
            
            sessionIdFirma = "";
            for(ExpPersonasFirmantesPorActuaciones per : lista){
                firma = new Firmas();

                firma.setActuacion(per.getActuacion());
                firma.setEmpresa(new Empresas(1));
                firma.setFechaHora(fecha);
                firma.setEstado("PE");
                firma.setDocumentoJudicial(per.getDocumentoJudicial());
                firma.setSesion(sessionId);
                firma.setPersona(personaUsuario);

                firmasController.setSelected(firma);
                firmasController.saveNew(null);
                
                firma.setSesion(sessionId + "_" + firma.getId());
                if(!"".equals(sessionIdFirma)){
                    sessionIdFirma += ",";
                }
                sessionIdFirma += sessionId + "_" + firma.getId();
                
                firmasController.save(null);
                
                
            }

        }

/*    
        int cont = 60;
        Firmas firma2 = null;

        while (cont >= 0) {

            try {
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                firma2 = ejbFacade.getEntityManager().createNamedQuery("Firmas.findById", Firmas.class).setParameter("id", firma.getId()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                cont = 0;
                break;
            }

            System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);

            if (!firma2.getEstado().equals("PE")) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            cont--;
        }

        if (cont > 0) {

            if (firma2 != null) {
                if (firma2.getEstado().equals("AC")) {
                    docImprimir = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findById", ExpActuaciones.class).setParameter("id", docImprimir.getId()).getSingleResult();

                    // autenticar(docImprimir);
                    actualizarEstadoFirma(firma2, rolElegido);

                    resp = true;
                } else {
                    JsfUtil.addErrorMessage("Error en proceso");
                }
            } else {
                JsfUtil.addErrorMessage("Error en proceso.");
            }
        } else {
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasController.setSelected(firma2);
                firmasController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }
        buscarActuaciones();
        return resp;
*/

    }
}
