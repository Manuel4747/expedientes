package py.gov.jem.expedientes.controllers;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.Personas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.CantidadItem;
import py.gov.jem.expedientes.datasource.RepAntecedentesDocumentosJudiciales;
import py.gov.jem.expedientes.datasource.RepAntecedentesDocumentosJudicialesRes;
import py.gov.jem.expedientes.datasource.RepPersonasUsuario;
import py.gov.jem.expedientes.models.Antecedentes;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DepartamentosPersona;
import py.gov.jem.expedientes.models.DespachosPersona;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.EstadosPersona;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import py.gov.jem.expedientes.models.ExpPersonasAsociadasPK;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.LocalidadesPersona;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.PersonasPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ResuelvePorResolucionesPorPersonas;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "personasAdminController")
@ViewScoped
public class PersonasAdminController extends AbstractController<Personas> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private DespachosPersonaController despachoController;
    @Inject
    private AntecedentesController antecedentesController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    private ResuelvePorResolucionesPorPersonasController resuelvePorResolucionesPorPersonasController;
    @Inject
    private AntecedentesRolesPorPersonasController antecedentesRolesPorPersonasController;
    @Inject
    ExpPersonasAsociadasController personasAsociadasController;

    private LocalidadesPersona localidadPersona;
    private DepartamentosPersona departamentoPersona;
    private HttpSession session;
    private Usuarios usuario;
    private CanalesEntradaDocumentoJudicial canal;
    private ParametrosSistema par;
    private DespachosPersona despachoPersona;
    private List<LocalidadesPersona> listaLocalidadesPersona;
    private final String borrador = "SI";
    private final String titulo = "BORRADOR";
    private final boolean imprimirQR = false;
    private final boolean valido = false;
    private final FiltroURL filtroURL = new FiltroURL();
    private Personas personaOriginal;
    private TreeNode root1Roles;
    private TreeNode selectedNode1Roles;
    private TreeNode root2Roles;
    private TreeNode selectedNode2Roles;
    private List<AntecedentesRolesPorPersonas> listaBorrarRoles;
    private List<AntecedentesRolesPorPersonas> listaAgregarRoles;
    private List<AntecedentesRolesPorPersonas> listaOriginalRoles;
    private String endpoint;
    private Personas personaAsociarSelected;
    private Personas personaAsociar;
    private Personas personaUsuario;
    private Personas secretario;
    private Personas presidente;
    private List<Personas> listaPersonasAsociar;
    private List<Personas> listaPersonasAAsociar;

    public PersonasAdminController() {
        // Inform the Abstract parent controller of the concrete Personas Entity
        super(Personas.class);
    }

    public List<Personas> getListaPersonasAAsociar() {
        return listaPersonasAAsociar;
    }

    public void setListaPersonasAAsociar(List<Personas> listaPersonasAAsociar) {
        this.listaPersonasAAsociar = listaPersonasAAsociar;
    }

    public Personas getPersonaAsociarSelected() {
        return personaAsociarSelected;
    }

    public void setPersonaAsociarSelected(Personas personaAsociarSelected) {
        this.personaAsociarSelected = personaAsociarSelected;
    }

    public Personas getPersonaAsociar() {
        return personaAsociar;
    }

    public void setPersonaAsociar(Personas personaAsociar) {
        this.personaAsociar = personaAsociar;
    }

    public List<Personas> getListaPersonasAsociar() {
        return listaPersonasAsociar;
    }

    public void setListaPersonasAsociar(List<Personas> listaPersonasAsociar) {
        this.listaPersonasAsociar = listaPersonasAsociar;
    }

    public LocalidadesPersona getLocalidadPersona() {
        return localidadPersona;
    }

    public void setLocalidadPersona(LocalidadesPersona localidadPersona) {
        this.localidadPersona = localidadPersona;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public DespachosPersona getDespachoPersona() {
        if (session.getAttribute("despachoPersonaSelected") != null) {
            despachoPersona = (DespachosPersona) session.getAttribute("despachoPersonaSelected");
            session.removeAttribute("despachoPersonaSelected");
        }
        return despachoPersona;
    }

    public void setDespachoPersona(DespachosPersona despachoPersona) {
        this.despachoPersona = despachoPersona;
    }

    public List<LocalidadesPersona> getListaLocalidadesPersona() {
        return listaLocalidadesPersona;
    }

    public void setListaLocalidadesPersona(List<LocalidadesPersona> listaLocalidadesPersona) {
        this.listaLocalidadesPersona = listaLocalidadesPersona;
    }

    public TreeNode getRoot1Roles() {
        return root1Roles;
    }

    public void setRoot1Roles(TreeNode root1Roles) {
        this.root1Roles = root1Roles;
    }

    public TreeNode getSelectedNode1Roles() {
        return selectedNode1Roles;
    }

    public void setSelectedNode1Roles(TreeNode selectedNode1Roles) {
        this.selectedNode1Roles = selectedNode1Roles;
    }

    public TreeNode getRoot2Roles() {
        return root2Roles;
    }

    public void setRoot2Roles(TreeNode root2Roles) {
        this.root2Roles = root2Roles;
    }

    public TreeNode getSelectedNode2Roles() {
        return selectedNode2Roles;
    }

    public void setSelectedNode2Roles(TreeNode selectedNode2Roles) {
        this.selectedNode2Roles = selectedNode2Roles;
    }

    public List<AntecedentesRolesPorPersonas> getListaBorrarRoles() {
        return listaBorrarRoles;
    }

    public void setListaBorrarRoles(List<AntecedentesRolesPorPersonas> listaBorrarRoles) {
        this.listaBorrarRoles = listaBorrarRoles;
    }

    public List<AntecedentesRolesPorPersonas> getListaAgregarRoles() {
        return listaAgregarRoles;
    }

    public void setListaAgregarRoles(List<AntecedentesRolesPorPersonas> listaAgregarRoles) {
        this.listaAgregarRoles = listaAgregarRoles;
    }

    public List<AntecedentesRolesPorPersonas> getListaOriginalRoles() {
        return listaOriginalRoles;
    }

    public void setListaOriginalRoles(List<AntecedentesRolesPorPersonas> listaOriginalRoles) {
        this.listaOriginalRoles = listaOriginalRoles;
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        personaUsuario = (Personas) session.getAttribute("Persona");
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_SE);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];
        
        try {
            par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

        refrescar();
    }
    
    private boolean verificarSecretario(){
        List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        if(!lista.isEmpty()){
            if(lista.size() == 1){
                secretario = lista.get(0);
            }else{
                String nombres = "";
                for(Personas per : lista){
                   if(!"".equals(nombres)){
                       nombres += ",";
                   }
                   nombres += per.getCi() + "-" + per.getNombresApellidos();
                }
                
                JsfUtil.addErrorMessage("Existe mas de un secretario configurado. En el sistema debe haber solo uno: " + nombres);
                return false;
            }
        }
        
        return true;
    }
    
    private boolean verificarPresidente(){
        
        List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        if(!lista2.isEmpty()){
            if(lista2.size() == 1){
                presidente = lista2.get(0);
            }else{
                String nombres = "";
                for(Personas per : lista2){
                   if(!"".equals(nombres)){
                       nombres += ",";
                   }
                   nombres += per.getCi() + "-" + per.getNombresApellidos();
                }
                JsfUtil.addErrorMessage("Existe mas de un presidente configurado. En el sistema debe haber solo uno: " + nombres);
                return false;
            }
        }
        
        return true;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        cargoPersonaController.setSelected(null);
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the CargosPersona controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCargoPersona(ActionEvent event) {
        Personas selected = this.getSelected();
        if (selected != null && cargoPersonaController.getSelected() == null) {
            cargoPersonaController.setSelected(selected.getCargoPersona());
        }
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        Personas selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

    public void prepareEditRoles() {
        if (getSelected() != null) {

            listaBorrarRoles = new ArrayList<>();
            listaAgregarRoles = new ArrayList<>();

            cargarTreeRoles();
        }
    }

    public void prepareEdit() {
        departamentoPersona = null;
        localidadPersona = null;
        // actualizarDptoYLocalidadEnCreate();
        departamentoPersona = getSelected().getDepartamentoPersona();
        localidadPersona = getSelected().getLocalidadPersona();
        actualizarListaLocalidades();

        if (getSelected() != null) {
            personaOriginal = new Personas(getSelected());
            despachoPersona = getSelected().getDespachoPersona();
        }
    }

    public void prepareEditPersonaAsociar() {
        if (getSelected() != null) {
            
            personaAsociarSelected = null;

            listaPersonasAAsociar = ejbFacade.getEntityManager().createNamedQuery("Personas.findByEstado", Personas.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            List<ExpPersonasAsociadas> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersona", ExpPersonasAsociadas.class).setParameter("persona", getSelected().getId()).getResultList();
            listaPersonasAsociar = new ArrayList<>();
            for (ExpPersonasAsociadas per : lista) {
                listaPersonasAsociar.add(per.getPersonaAsociada());
            }
        }

    }

    @Override
    public Personas prepareCreate(ActionEvent event) {
        Personas doc = super.prepareCreate(event);
        departamentoPersona = null;
        localidadPersona = null;
        despachoPersona = null;
        listaLocalidadesPersona = new ArrayList<>();
        doc.setHabilitarAntecedentes(true);
        return doc;
    }

    public void actualizarDptoYLocalidadEnEdit() {
        actualizarDptoYLocalidadEnCreate(despachoPersona);
    }

    public void actualizarDptoYLocalidadEnEdit(DespachosPersona despachoPersona) {
        if (getSelected() != null) {
            if (despachoPersona != null) {
                departamentoPersona = despachoPersona.getDepartamentoPersona();
                localidadPersona = despachoPersona.getLocalidadPersona();
            } else {
                departamentoPersona = getSelected().getDepartamentoPersona();
                localidadPersona = getSelected().getLocalidadPersona();
            }

            actualizarListaLocalidades();
        }
    }

    public void actualizarListaLocalidades() {
        if (departamentoPersona != null) {
            listaLocalidadesPersona = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", departamentoPersona).getResultList();
        } else {
            listaLocalidadesPersona = new ArrayList<>();
        }
    }

    public void actualizarDptoYLocalidadEnCreate() {
        actualizarDptoYLocalidadEnCreate(despachoPersona);
    }

    public void actualizarDptoYLocalidadEnCreate(DespachosPersona despachoPersona) {
        if (getSelected() != null) {
            if (despachoPersona != null) {
                departamentoPersona = despachoPersona.getDepartamentoPersona();
                localidadPersona = despachoPersona.getLocalidadPersona();
            } else {
                departamentoPersona = getSelected().getDepartamentoPersona();
                localidadPersona = getSelected().getLocalidadPersona();
            }

            actualizarListaLocalidades();
        }
    }

    public void refrescar() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList());
    }

    @Override
    public Collection<Personas> getItems() {
        return super.getItems2();
    }

    public boolean deshabilitarHabilitarAntecedentes() {
        //if (getSelected() != null) {
        if (filtroURL.verifPermiso(Constantes.PERMITIR_HABILITAR_ANTECEDENTES)) {
            return false;
        }
        //}

        return true;
    }

    public boolean deshabilitarRepAntecedentes() {
        /*
        if(getSelected() != null){
            if(filtroURL.verifPermiso(Constantes.IMPRIMIR_REP_ANTECEDENTES)){
                return false;
            }
        }
        
        return true;
         */
        return getSelected() == null;
    }

    @Override
    public void delete(ActionEvent event) {

        if (getSelected() != null) {
            
            List<PersonasPorDocumentosJudiciales> lista1 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByPersonaEstado", PersonasPorDocumentosJudiciales.class).setParameter("persona", getSelected().getId()).setParameter("estado", new Estados("AC")).getResultList();
            
            /*
            boolean encontro = false;
            
            for (PersonasPorDocumentosJudiciales res : lista1) {
                encontro = true;
                break;
            }
            */

            if (!lista1.isEmpty()) {
                JsfUtil.addErrorMessage("La persona tiene expedientes asociados (1).");
                return;
            }
            
            lista1 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByPersonaAltaEstado", PersonasPorDocumentosJudiciales.class).setParameter("personaAlta", getSelected()).setParameter("estado", new Estados("AC")).getResultList();
            
            /*
            boolean encontro = false;
            
            for (PersonasPorDocumentosJudiciales res : lista1) {
                encontro = true;
                break;
            }
            */

            if (!lista1.isEmpty()) {
                JsfUtil.addErrorMessage("La persona tiene expedientes asociados (2).");
                return;
            }

            // id not in (select ifnull(persona_ultimo_estado,-1) from resuelve_por_resoluciones_por_personas ppdj)  and   
            List<ResuelvePorResolucionesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByPersonaEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("persona", getSelected()).setParameter("estado", new Estados("AC")).getResultList();
            /*
            encontro = false;
            for (ResuelvePorResolucionesPorPersonas res : lista) {
                encontro = true;
                break;
            }
            */

            if (!lista.isEmpty()) {
                JsfUtil.addErrorMessage("La persona tiene resoluciones asociadas.");
                return;
            }
            
            CantidadItem cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from antecedentes where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                JsfUtil.addErrorMessage("La persona tiene antecedentes asociados.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actuaciones where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene actuaciones asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from antecedentes_roles_por_personas where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene roles asociados.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from documentos_judiciales where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene documentos asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from documentos_judiciales where persona_ultima_observacion = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene documentos asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from documentos_judiciales where persona_ultimo_estado_procesal = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene asociado estados documentos administrativos.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from estados_procesales_documentos_administrativos where persona_visible = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene asociado estados documentos administrativos.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from estados_procesales_documentos_judiciales where persona_visible = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene asociado estados documentos judiciales.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actas_sesion where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene asociadas actas de sesión (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actas_sesion where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene asociadas actas de sesión (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actas_sesion where persona_visible = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene asociadas actas de sesión (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actuaciones where persona_notificado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene actuaciones asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actuaciones where persona_visible = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene actuaciones asociadas (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actuaciones where destinatario = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene actuaciones asociadas (4).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_actuaciones where persona_autenticado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene actuaciones asociadas (5).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_cambios_estado_actuacion where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene cambios de estado actuaciones asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_cambios_estado_actuacion where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene cambios de estado actuaciones asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_cambios_texto_actuacion where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene cambios de texto actuaciones asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_cambios_texto_actuacion where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene cambios de texto actuaciones asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_detalles_acta_sesion where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene detalles de acta sesión asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_detalles_acta_sesion where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene detalles de acta sesión asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_detalles_orden_dia where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene detalles orden día asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_detalles_orden_dia where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene detalles orden día asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_documentos_judiciales_corte_por_documentos_judiciales where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene documentos judiciales corte asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_documentos_judiciales_corte_por_documentos_judiciales where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene documentos judiciales corte asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_envios_corte where persona_envio = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene envios corte asociados.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_fiscales_jurado where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene fiscales JEM asociados.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_formatos where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene formatos asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_formatos where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene formatos asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_hist_actuaciones where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico actuaciones asociados.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_hist_personas_firmantes_por_actuaciones where persona_firmante = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico personas firmantes por actuaciones asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_hist_personas_firmantes_por_actuaciones where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico personas firmantes por actuaciones asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_hist_personas_firmantes_por_actuaciones where persona_borrado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico personas firmantes por actuaciones asociados (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_hist_personas_firmantes_por_actuaciones where persona_firma = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico personas firmantes por actuaciones asociados (4).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_hist_personas_firmantes_por_actuaciones where persona_revisado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico personas firmantes por actuaciones asociados (5).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_intervinientes_por_documentos_judiciales_corte where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene intervinientes por documentos judiciales corte asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_intervinientes_por_documentos_judiciales_corte where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene intervinientes por documentos judiciales corte asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_intervinientes_por_documentos_judiciales_corte where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene intervinientes por documentos judiciales corte asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_notificaciones where remitente = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene notificaciones asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_notificaciones where destinatario = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene notificaciones asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_notificaciones where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene notificaciones asociadas (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_ordenes_dia where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene ordenes día asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_ordenes_dia where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene ordenes día asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_partes_por_documentos_judiciales where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene partes por documentos judiciales asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_partes_por_documentos_judiciales where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene partes por documentos judiciales asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_partes_por_documentos_judiciales where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene partes por documentos judiciales asociadas (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_partes_por_documentos_judiciales_corte where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene partes por documentos judiciales corte asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_partes_por_documentos_judiciales_corte where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene partes por documentos judiciales corte asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_pedidos_antecedente where persona_confirmacion = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene pedidos antecedentes asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_pedidos_antecedente where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene pedidos antecedentes asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_pedidos_antecedente where persona_respuesta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene pedidos antecedentes asociados (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_pedidos_antecedente where persona_borrado_antecedente = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene pedidos antecedentes asociados (4).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_acusacion_por_documentos_judiciales where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas acusación por documentos judiciales asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_acusacion_por_documentos_judiciales where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas acusación por documentos judiciales asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_asociadas where persona_asociada = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas asociadas a miembros (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_asociadas where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas asociadas a miembros (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actas_sesion where persona_firmante = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actas sesión asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actas_sesion where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actas sesión asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actas_sesion where persona_firma = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actas sesión asociadas (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actas_sesion where persona_revisado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actas sesión asociadas (4).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actuaciones where persona_firma = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actuaciones asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actuaciones where persona_revisado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actuaciones asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actuaciones where persona_firmante = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actuaciones asociadas (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actuaciones where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actuaciones asociadas (4).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_firmantes_por_actuaciones where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas firmantes por actuaciones asociadas (5).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_habilitadas_por_documentos_judiciales where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas habilitadas por documentos judiciales asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_habilitadas_por_documentos_judiciales where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas habilitadas por documentos judiciales asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_habilitadas_por_documentos_judiciales where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas habilitadas por documentos judiciales asociadas (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_habilitadas_por_documentos_judiciales where persona_borrado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas habilitadas por documentos judiciales asociadas (4).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_inhibidas_por_documentos_judiciales where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas inhibidas por documentos judiciales asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_inhibidas_por_documentos_judiciales where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas inhibidas por documentos judiciales asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_inhibidas_por_documentos_judiciales where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas inhibidas por documentos judiciales asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_personas_inhibidas_por_documentos_judiciales where persona_borrado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene personas inhibidas por documentos judiciales asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_procesos_documento_judicial_corte where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene procesos por documentos judiciales corte asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_procesos_documento_judicial_corte where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene procesos por documentos judiciales corte asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_profesionales_por_partes_por_documentos_judiciales_corte where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene profesionales por documentos judiciales corte asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_profesionales_por_partes_por_documentos_judiciales_corte where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene profesionales por documentos judiciales corte asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_roles_firmantes_por_actuaciones where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene roles firmantes por actuaciones asociados (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_roles_firmantes_por_actuaciones where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene roles firmantes por actuaciones asociados (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from exp_roles_firmantes_por_actuaciones where persona_firmante = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene roles firmantes por actuaciones asociados (3).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from firmas where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene firmas asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from firmas where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene firmas asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from hist_firmas where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico de firmas asociado (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from hist_firmas where persona_borrado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene historico de firmas asociado (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from pedidos_persona where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene pedidos de usuario asociados.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from pedidos_persona where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene pedidos de usuario asociados.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from resoluciones where persona_alta = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene resoluciones asociadas (1).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from resoluciones where persona_ultimo_estado = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene resoluciones asociadas (2).");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from sesiones_login where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene sesiones login asociadas.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from validaciones_email where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene validaciones email asociadas.");
                    return;
                }
            }
            
            cant = (CantidadItem) ejbFacade.getEntityManager().createNativeQuery("select count(*) as cantidad from validaciones_reseteo where persona = ?1", CantidadItem.class).setParameter(1, getSelected().getId()).getSingleResult();
                
            if(cant != null){
                if(cant.getCantidad() > 0){
                    JsfUtil.addErrorMessage("La persona tiene validaciones reseteo asociadas.");
                    return;
                }
            }
            
            
            Date fecha = ejbFacade.getSystemDate();

            getSelected().setEstado("IN");
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);

            refrescar();
        }
    }

    public boolean deshabilitarGenerarUsuario() {
        if (getSelected() != null) {
            return getSelected().getUsuario() == null ? false : ("".equals(getSelected().getUsuario()) ? false : true);
        }

        return true;
    }

    public boolean deshabilitarReenvioUsuario() {
        if (getSelected() != null) {
            return false;
        }

        return true;
    }

    public void enviarEmailAviso(String email, String asunto, String msg) {

        BodyPart texto = new MimeBodyPart();
        try {
            texto.setContent(msg, "text/html; charset=utf-8");

            Utils.sendEmail(par.getIpServidorEmail(),
                    par.getPuertoServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    par.getContrasenaServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    email,
                    asunto,
                    texto);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return;
        }

    }

    public void generarUsuario() {

        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            String hash = "";
            try {
                hash = Utils.generarHash(fecha, getSelected().getId());
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                return;
            }
            getSelected().setUsuario(getSelected().getCi());

            String pass = Utils.passwordToHash(hash.substring(1, 7));
            
            getSelected().setContrasena(pass);

            super.save(null);

            String texto = "<p>Hola " + getSelected().getNombresApellidos() + "<br> "
                    + "     Se le ha generado un usuario para el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br><br>"
                    + "Usuario: " + getSelected().getCi()
                    + "<br>"
                    + "Contraseña: " + hash.substring(1, 7)
                    + "<br><br>"
                    + "Para entrar al sistema, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/'>CLICK AQUÍ</a>";

            enviarEmailAviso(getSelected().getEmail(), "Usuario para en el Sistema de Expediente Electrónico JEM", texto);

        }

    }

    public void reenviarUsuario() {

        if (getSelected() != null) {
            /*
            Date fecha = ejbFacade.getSystemDate();
            String hash = "";
            try {
                hash = Utils.generarHash(fecha, getSelected().getId());
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo generar codigo de autenticacion");
                return;
            }
            getSelected().setUsuario(getSelected().getCi());

            getSelected().setContrasena(hash.substring(1, 6));
            
            super.save(null);
             */

            boolean enviar = true;
            if (getSelected().getContrasena() != null) {
                if ("".equals(getSelected().getContrasena())) {
                    enviar = false;
                }
            } else {
                enviar = false;
            }

            if (enviar) {
                String texto = "<p>Hola " + getSelected().getNombresApellidos() + "<br> "
                        + "     Se le ha generado un usuario para el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br><br>"
                        + "Usuario: " + getSelected().getUsuario()
                        + "<br>"
                        + "Contraseña: " + getSelected().getContrasena()
                        + "<br><br>"
                        + "Para entrar al sistema, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/'>CLICK AQUÍ</a>";

                enviarEmailAviso(getSelected().getEmail(), "Usuario para en el Sistema de Expediente Electrónico JEM", texto);
            } else {
                JsfUtil.addErrorMessage("Esta persona no tiene usuario. Debe generarlo primero");
                return;
            }

        }

    }

    public void saveNew() {
        if (getSelected() != null) {

            if (departamentoPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_departamentoPersona"));
                return;
            }

            if (localidadPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_localidadPersona"));
                return;
            }
            /*
            if (getSelected().getTipoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_tipoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCargoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_cargoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_ci"));
                return;
            }
             */
            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_nombresApellidos"));
                return;
            } else if ("".equals(getSelected().getNombresApellidos())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_nombresApellidos"));
                return;
            }

            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCiEstado", Personas.class).setParameter("ci", getSelected().getCi()).setParameter("estado", "AC").getResultList();
                    if (listaPer != null) {
                        if (!listaPer.isEmpty()) {
                            JsfUtil.addErrorMessage("Ya existe una persona con cedula " + getSelected().getCi());
                            return;
                        }
                    }
                }
            }

            List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByNombresApellidosEstado", Personas.class).setParameter("nombresApellidos", getSelected().getNombresApellidos().trim()).setParameter("estado", "AC").getResultList();
            if (listaPer != null) {
                if (!listaPer.isEmpty()) {
                    JsfUtil.addErrorMessage("Ya existe una persona con el nombre " + getSelected().getNombresApellidos().trim());
                    return;
                }
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setDepartamentoPersona(departamentoPersona);
            getSelected().setLocalidadPersona(localidadPersona);
            getSelected().setDespachoPersona(despachoPersona);
            getSelected().setEstado("AC");
            
            List<EstadosPersona> lista = ejbFacade.getEntityManager().createNamedQuery("EstadosPersona.findByCodigo", EstadosPersona.class).setParameter("codigo", Constantes.ESTADO_USUARIO_AC).getResultList();
            
            if(!lista.isEmpty()){
                getSelected().setEstadoPersona(lista.get(0));
            }

            super.saveNew(null);
            /*
            if (getSelected().getDespachoPersona() != null) {
                if (!departamentoPersona.equals(getSelected().getDespachoPersona().getDepartamentoPersona())
                        || !localidadPersona.equals(getSelected().getDespachoPersona().getLocalidadPersona())) {

                    DespachosPersona despacho = ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findById", DespachosPersona.class).setParameter("id", getSelected().getDespachoPersona().getId()).getSingleResult();

                    despacho.setDepartamentoPersona(departamentoPersona);
                    despacho.setLocalidadPersona(localidadPersona);

                    despachoController.setSelected(despacho);
                    despachoController.save(null);

                }
            }
             */
            refrescar();

        }
    }

    public void save() {
        if (getSelected() != null) {

            if (departamentoPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_departamentoPersona"));
                return;
            }

            if (localidadPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_localidadPersona"));
                return;
            }
            /*
            if (getSelected().getTipoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_tipoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCargoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_cargoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_ci"));
                return;
            }
             */
            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_nombresApellidos"));
                return;
            }

            boolean ciOriginalNulo = false;

            if (personaOriginal != null) {
                if (personaOriginal.getCi() != null) {
                    if ("".equals(personaOriginal.getCi()) || "NULL".equals(personaOriginal.getCi())) {
                        ciOriginalNulo = true;
                    }
                } else {
                    ciOriginalNulo = true;
                }
            }

            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi()) && !"NULL".equals(getSelected().getCi())) {
                    //if (!ciOriginalNulo) {
                    if (!getSelected().getCi().equals(personaOriginal.getCi())) {
                        List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCiEstado", Personas.class).setParameter("ci", getSelected().getCi()).setParameter("estado", "AC").getResultList();
                        if (listaPer != null) {
                            if (!listaPer.isEmpty()) {
                                JsfUtil.addErrorMessage("Ya existe una persona con cedula " + getSelected().getCi());
                                getSelected().setCi(personaOriginal.getCi());
                                return;
                            }
                        }
                    }
                    //}
                } else if (!ciOriginalNulo) {
                    JsfUtil.addErrorMessage("Debe cargar la cedula");
                    return;
                }
            } else if (!ciOriginalNulo) {
                JsfUtil.addErrorMessage("Debe cargar la cedula");
                return;
            }

            if (!"".equals(getSelected().getNombresApellidos())) {
                if (!personaOriginal.getNombresApellidos().equals(getSelected().getNombresApellidos())) {
                    List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByNombresApellidosEstado", Personas.class).setParameter("nombresApellidos", getSelected().getNombresApellidos().trim()).setParameter("estado", "AC").getResultList();
                    if (listaPer != null) {
                        if (!listaPer.isEmpty()) {
                            JsfUtil.addErrorMessage("Ya existe una persona con el nombre " + getSelected().getNombresApellidos().trim());
                            getSelected().setNombresApellidos(personaOriginal.getNombresApellidos());
                            return;
                        }
                    }
                }
            } else {
                JsfUtil.addErrorMessage("Debe Ingresar Nombres y Apellidos");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setDepartamentoPersona(departamentoPersona);
            getSelected().setLocalidadPersona(localidadPersona);
            getSelected().setDespachoPersona(despachoPersona);

            super.save(null);
            /*
            if (getSelected().getDespachoPersona() != null) {
                if (!departamentoPersona.equals(getSelected().getDespachoPersona().getDepartamentoPersona())
                        || !localidadPersona.equals(getSelected().getDespachoPersona().getLocalidadPersona())) {

                    DespachosPersona despacho = ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findById", DespachosPersona.class).setParameter("id", getSelected().getDespachoPersona().getId()).getSingleResult();

                    despacho.setDepartamentoPersona(departamentoPersona);
                    despacho.setLocalidadPersona(localidadPersona);

                    despachoController.setSelected(despacho);
                    despachoController.save(null);

                }
            }
             */

            refrescar();

        }
    }

    public String generarCodigoArchivo() {
        Random aleatorio;
        int valor = 0;
        String pin = "";
        aleatorio = new Random(System.currentTimeMillis());
        valor = aleatorio.nextInt(999999);
        pin = String.valueOf(valor);
        while (pin.trim().length() < 6) {
            pin = '0' + pin;
        }
        return pin;
    }

    public void onDragDropRoles(TreeDragDropEvent event) {
        TreeNode dragNode = event.getDragNode();
        TreeNode dropNode = event.getDropNode();
        int dropIndex = event.getDropIndex();
    }

    private void cargarTreeRoles() {

        selectedNode1Roles = null;
        selectedNode2Roles = null;

        AntecedentesRolesPorPersonas descNodo = new AntecedentesRolesPorPersonas(0, 0);
        root1Roles = new DefaultTreeNode(descNodo, null);

        List<AntecedentesRolesPorPersonas> listaRoles2 = new ArrayList<>();
        listaOriginalRoles = new ArrayList<>();

        if (getSelected() != null) {
            root2Roles = new DefaultTreeNode(descNodo, null);
            listaRoles2 = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByPersona", AntecedentesRolesPorPersonas.class).setParameter("persona", getSelected().getId()).getResultList();

            listaOriginalRoles = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByPersona", AntecedentesRolesPorPersonas.class).setParameter("persona", getSelected().getId()).getResultList();

            for (AntecedentesRolesPorPersonas rolUsuario : listaRoles2) {

                // grupo = grupoEstado.getGrupos();
                // grupo.setDescripcion(grupo.getDescripcion() + " - " + grupoUsuario.getNiveles().getDescripcion());
                new DefaultTreeNode(rolUsuario, root2Roles);
            }
        } else {
            root2Roles = new DefaultTreeNode(descNodo, null);
        }

        List<AntecedentesRoles> lista = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRoles.findAll", AntecedentesRoles.class).getResultList();

        // List<GruposPorUsuarios> listaGrupos = new ArrayList<>();
        AntecedentesRolesPorPersonas rolUsuario;

        boolean existe;

        for (AntecedentesRoles rol : lista) {

            rolUsuario = antecedentesRolesPorPersonasController.prepareCreate(null);

            rolUsuario.setRoles(rol);
            rolUsuario.setPersonas(getSelected());

            existe = false;
            for (AntecedentesRolesPorPersonas re : listaRoles2) {
                if (re.getRoles().equals(rol)) {
                    existe = true;
                }
            }

            if (!existe) {
                new DefaultTreeNode(rolUsuario, root1Roles);
            }
        }
    }

    public void transferir1Roles() {
        
        if (selectedNode1Roles != null) {
            
            AntecedentesRolesPorPersonas gu = (AntecedentesRolesPorPersonas) selectedNode1Roles.getData(); 
            
            secretario = null;
            if(Constantes.ROL_SECRETARIO.equals(gu.getRoles().getId())){
                if(verificarSecretario()){
                    if(secretario != null){
                        if(!secretario.equals(gu.getPersonas())){
                            JsfUtil.addErrorMessage("Ya existe un secretario configurado. Debe sacarle el rol de secretario a " + secretario.getCi() + " " + secretario.getNombresApellidos() + " antes");
                            return;
                        }
                    }
                }else{
                    return;
                }
            }
            
            presidente = null;
            if(Constantes.ROL_PRESIDENTE.equals(gu.getRoles().getId())){
                if(verificarPresidente()){
                    if(presidente != null){
                        if(!presidente.equals(gu.getPersonas())){
                            JsfUtil.addErrorMessage("Ya existe un presidente configurado. Debe sacarle el rol de presidente a " + presidente.getCi() + " " + presidente.getNombresApellidos() + " antes");
                            return;
                        }
                    }
                }else{
                    return;
                }
            }
            
            TreeNode parent = selectedNode1Roles.getParent();
            parent.getChildren().remove(selectedNode1Roles);
            
            new DefaultTreeNode(selectedNode1Roles.getData(), root2Roles);

            if (!listaOriginalRoles.contains(gu)) {
                listaAgregarRoles.add(gu);
            }

            listaBorrarRoles.remove(gu);

            selectedNode1Roles = null;
        }
    }

    public void transferir2Roles() {
        if (selectedNode2Roles != null) {
            TreeNode parent = selectedNode2Roles.getParent();
            parent.getChildren().remove(selectedNode2Roles);

            AntecedentesRolesPorPersonas gu = (AntecedentesRolesPorPersonas) selectedNode2Roles.getData();

            new DefaultTreeNode(selectedNode2Roles.getData(), root1Roles);

            listaAgregarRoles.remove(gu);

            if (listaOriginalRoles.contains(gu)) {
                listaBorrarRoles.add(gu);
            }

            selectedNode2Roles = null;
        }
    }

    public boolean dehabilitarTransfer1RolesButton() {
        return selectedNode1Roles == null;
    }

    public boolean dehabilitarTransfer2RolesButton() {
        return selectedNode2Roles == null;
    }

    public void guardarRoles() {

        if (getSelected() != null) {
            // Guardar cambios en roles
            for (AntecedentesRolesPorPersonas gu : listaAgregarRoles) {
                gu.setPersonas(getSelected());
                antecedentesRolesPorPersonasController.setSelected(gu);

                if (!listaOriginalRoles.contains(gu)) {
                    antecedentesRolesPorPersonasController.saveNew(null);
                }
            }

            for (AntecedentesRolesPorPersonas gu : listaBorrarRoles) {
                gu.setPersonas(getSelected());
                if (listaOriginalRoles.contains(gu)) {
                    antecedentesRolesPorPersonasController.setSelected(gu);
                    antecedentesRolesPorPersonasController.delete(null);
                }
            }
        }
    }

    public List<Personas> getListaPersonasAsociarActivas() {
        List<Personas> listaPersonasAsociarActivas = new ArrayList<>();
        if (listaPersonasAsociar != null) {
            for (Personas per : listaPersonasAsociar) {
                if ("AC".equals(per.getEstado())) {
                    listaPersonasAsociarActivas.add(per);
                }
            }
        }

        return listaPersonasAsociarActivas;
    }

    public void agregarPersonaAsociar() {

        if (personaAsociar != null) {

            if (listaPersonasAsociar == null) {
                listaPersonasAsociar = new ArrayList<>();
            }

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaPersonasAsociar) {
                if (per.equals(personaAsociar)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaPersonasAsociar.add(personaAsociar);
            }
        }
    }

    public void borrarPersonaAsociar(Personas personaActual) {

        if (listaPersonasAsociar != null) {

            if (personaActual != null) {
                if (personaActual.isFirmado() || personaActual.isRevisado()) {
                    JsfUtil.addErrorMessage("La actuación ya fue firmada o ya fue revisada por la persona, ya no puede ser borrada");
                    return;
                }

                listaPersonasAsociar.remove(personaActual);
/*
                personaActual.setEstado("IN");

                listaPersonasAsociar.add(personaActual);
                */
            }

        }
    }

    public void savePersonasAsociar() {
        if (getSelected() != null) {
            List<ExpPersonasAsociadas> listaAsociarActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersona", ExpPersonasAsociadas.class).setParameter("persona", getSelected().getId()).getResultList();
            savePersonasAsociar(getSelected(), listaPersonasAsociar, listaAsociarActual);
        }

        // saveRolesFirmantes();
    }

    public void savePersonasAsociar(Personas persona, List<Personas> listaPersonasAsociar, List<ExpPersonasAsociadas> listaAsociarActual) {

        Date fecha = ejbFacade.getSystemDate();
        Personas per2 = null;
        ExpPersonasAsociadas perDocActual = null;
        boolean encontro = false;

        boolean enviarTextoNuevaParte;
        boolean enviarTextoBorrarParte;

        for (Personas per : listaPersonasAsociar) {
            enviarTextoNuevaParte = false;
            enviarTextoBorrarParte = false;
            encontro = false;
            perDocActual = null;
            for (int i = 0; i < listaAsociarActual.size(); i++) {
                per2 = listaAsociarActual.get(i).getPersonaAsociada();
                if (per2.equals(per)) {
                    encontro = true;
                    perDocActual = listaAsociarActual.get(i);
                    break;
                }
            }
            if (!encontro) {
                enviarTextoNuevaParte = true;
                ExpPersonasAsociadasPK pk = new ExpPersonasAsociadasPK(persona.getId(), per.getId());
                ExpPersonasAsociadas perDoc = new ExpPersonasAsociadas(pk);
                perDoc.setPersona(persona);
                perDoc.setPersonaAsociada(per);

                personasAsociadasController.setSelected(perDoc);
                personasAsociadasController.saveNew(null);
            }
            /*
            if (enviarTextoNuevaParte) {
                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Tiene una nueva presentacion que firmar en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Firma pendiente en Expediente Electrónico JEM", texto);
            }

            if (enviarTextoBorrarParte) {
                String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                        + "     Ud dejo de ser firmante en una presentacion en la causa nro " + getSelected().getCausa() + " caratulada: <br><br>"
                        + getSelected().getCaratulaString()
                        + "<br><br>"
                        + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                        + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + act.getId() + "'>CLICK AQUÍ</a>";

                enviarEmailAviso(per.getEmail(), "Dejo de ser firmante en Expediente Electrónico JEM", texto);
            }
             */
        }

        for (int i = 0; i < listaAsociarActual.size(); i++) {
            per2 = listaAsociarActual.get(i).getPersonaAsociada();
            encontro = false;
            for (Personas per : listaPersonasAsociar) {
                if (per2.equals(per)) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                personasAsociadasController.setSelected(listaAsociarActual.get(i));
                personasAsociadasController.delete(null);
            }
        }
    }


    public void imprimirRepAntecedentesViejo(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            List<RepAntecedentesDocumentosJudiciales> listaInformacionSumaria = null;
            List<RepAntecedentesDocumentosJudiciales> listaPedidoDesafuero = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;

            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");
            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaDenuncias == null) {
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAcusaciones == null) {
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPreliminar == null) {
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAntecedentesCSJ == null) {
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaInformacionSumaria == null) {
                    listaInformacionSumaria = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Informacion Sumaria:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaInformacionSumaria.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPedidoDesafuero == null) {
                    listaPedidoDesafuero = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPedidoDesafuero.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaEnjuiciamientos == null) {
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n"
                        + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n"
                        + "	where r.resolucion = s.id \n"
                        + "       and s.tipo_resolucion = t.id\n"
                        + "       and e.tipo_resolucion = p.id\n"
                        + "	and r.resuelve = e.codigo\n"
                        + "	and s.documento_judicial = ?1 \n"
                        + "	and r.persona = ?2\n"
                        + "       and e.tipo_resuelve = ?3\n"
                        + "	and r.estado = ?4 \n"
                        + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                        .getSingleResult();

                if (listaFiniquitados == null) {
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion() != null) ? res.getTipoResolucion() : res.getTipoResolucionAlt());

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (valido) {
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }

            String myHash = "";

            String codigoArchivo = "";
            if (imprimirQR) {

                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }

            String reportPath;

            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null || listaInformacionSumaria != null || listaPedidoDesafuero != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                } else {
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                } else {
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                } else {
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                } else {
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                } else {
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                } else {
                    map.put("datasourceAntecedentesCSJ", null);
                }
                if (listaInformacionSumaria != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceInformacionSumaria = new JRBeanCollectionDataSource(listaInformacionSumaria);
                    map.put("datasourceInformacionSumaria", beanCollectionDataSourceInformacionSumaria);
                } else {
                    map.put("datasourceInformacionSumaria", null);
                }
                if (listaPedidoDesafuero != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePedidoDesafuero = new JRBeanCollectionDataSource(listaPedidoDesafuero);
                    map.put("datasourcePedidoDesafuero", beanCollectionDataSourcePedidoDesafuero);
                } else {
                    map.put("datasourcePedidoDesafuero", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            if (imprimirQR) {

                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            List<RepAntecedentesDocumentosJudiciales> listaInformacionSumaria = null;
            List<RepAntecedentesDocumentosJudiciales> listaPedidoDesafuero = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;

            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");
            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )), d.estado_proceso <> ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaDenuncias == null) {
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )), d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAcusaciones == null) {
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )), d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPreliminar == null) {
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAntecedentesCSJ == null) {
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaInformacionSumaria == null) {
                    listaInformacionSumaria = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Informacion Sumaria:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaInformacionSumaria.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPedidoDesafuero == null) {
                    listaPedidoDesafuero = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPedidoDesafuero.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaEnjuiciamientos == null) {
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n"
                        + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n"
                        + "	where r.resolucion = s.id \n"
                        + "       and s.tipo_resolucion = t.id\n"
                        + "       and e.tipo_resolucion = p.id\n"
                        + "	and r.resuelve = e.codigo\n"
                        + "	and s.documento_judicial = ?1 \n"
                        + "	and r.persona = ?2\n"
                        + "       and e.tipo_resuelve = ?3\n"
                        + "	and r.estado = ?4 \n"
                        + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                        .getSingleResult();

                if (listaFiniquitados == null) {
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion() != null) ? res.getTipoResolucion() : res.getTipoResolucionAlt());

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (valido) {
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }

            String myHash = "";

            String codigoArchivo = "";
            if (imprimirQR) {

                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }

            String reportPath;

            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null || listaInformacionSumaria != null || listaPedidoDesafuero != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                } else {
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                } else {
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                } else {
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                } else {
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                } else {
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                } else {
                    map.put("datasourceAntecedentesCSJ", null);
                }
                if (listaInformacionSumaria != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceInformacionSumaria = new JRBeanCollectionDataSource(listaInformacionSumaria);
                    map.put("datasourceInformacionSumaria", beanCollectionDataSourceInformacionSumaria);
                } else {
                    map.put("datasourceInformacionSumaria", null);
                }
                if (listaPedidoDesafuero != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePedidoDesafuero = new JRBeanCollectionDataSource(listaPedidoDesafuero);
                    map.put("datasourcePedidoDesafuero", beanCollectionDataSourcePedidoDesafuero);
                } else {
                    map.put("datasourcePedidoDesafuero", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            if (imprimirQR) {

                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
