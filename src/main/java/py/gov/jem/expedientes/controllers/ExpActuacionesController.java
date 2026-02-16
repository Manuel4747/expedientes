package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.datasource.CantidadItem;
import py.gov.jem.expedientes.datasource.EstadoCantidad;
import py.gov.jem.expedientes.datasource.IdCantidad;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.ExpEstadosActuacion;
import py.gov.jem.expedientes.models.ExpFeriados;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.PersonasPorDocumentosJudiciales;

@Named(value = "expActuacionesController")
@ViewScoped
public class ExpActuacionesController extends AbstractController<ExpActuaciones> {

    @EJB
    private DeclarativeScheduler declarativeScheduler;

    @Inject
    private EmpresasController empresaController;
    private HttpSession session;
    private Personas personaUsuario = null;
    private AntecedentesRoles rolElegido;
    private final FiltroURL filtroURL = new FiltroURL();
    private List<ExpActuaciones> listaPendientes;
    private List<ExpActuaciones> listaPendientesAlt;
    private ExpActuaciones selectedAlt;
    private List<Personas> listaPersonas;
    private Personas personaSelected;
    private String accion;
    private String titulo;
    private String tituloAlt;
    private String endpoint;
    private boolean filtrarResoluciones;
    private boolean filtrarProvidencias;
    private boolean filtrarOficios;
    private boolean esRelator;

    public List<ExpActuaciones> getListaPendientesAlt() {
        return listaPendientesAlt;
    }

    public void setListaPendientesAlt(List<ExpActuaciones> listaPendientesAlt) {
        this.listaPendientesAlt = listaPendientesAlt;
    }

    public ExpActuaciones getSelectedAlt() {
        return selectedAlt;
    }

    public void setSelectedAlt(ExpActuaciones selectedAlt) {
        this.selectedAlt = selectedAlt;
    }

    public String getTituloAlt() {
        return tituloAlt;
    }

    public void setTituloAlt(String tituloAlt) {
        this.tituloAlt = tituloAlt;
    }

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

    public List<ExpActuaciones> getListaPendientes() {
        return listaPendientes;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public Personas getPersonaSelected() {
        return personaSelected;
    }

    public void setPersonaSelected(Personas personaSelected) {
        this.personaSelected = personaSelected;
    }

    public void setListaPendientes(List<ExpActuaciones> listaPendientes) {
        this.listaPendientes = listaPendientes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ExpActuacionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpActuaciones.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        accion = params.get("tipo");
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

        if (Constantes.ACCION_PENDIENTES_ABOGADO.equals(accion)) {
            titulo = "PENDIENTES DE PRESENTACION";
        } else if (Constantes.ACCION_PENDIENTES.equals(accion)) {
            titulo = "PENDIENTES DE PRESENTACION";
        } else if (Constantes.ACCION_NOTIFICACION.equals(accion)) {
            titulo = "NOTIFICACIONES";
        } else if (Constantes.ACCION_PARA_LA_FIRMA.equals(accion)) {
            titulo = "PRESENTACIONES PARA LA FIRMA";
        } else if (Constantes.ACCION_ULTIMAS.equals(accion)) {
            titulo = "ULTIMAS 10 PRESENTACIONES";
        } else if (Constantes.ACCION_ACTUACIONES_EN_PROYECTO.equals(accion)) {
            titulo = "ACTUACIONES EN ELABORACION";
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_SECRETARIO.equals(accion)) {
            titulo = "ACTUACIONES P/ REVISION POR EL SECRETARIO";
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_DIRECTOR.equals(accion)) {
            titulo = "ACTUACIONES P/ REVISION POR EL DIRECTOR";
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE.equals(accion)) {
            titulo = "ACTUACIONES P/ REVISION POR EL PREOPINANTE";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE.equals(accion)) {
            titulo = "ACTUACIONES P/ LA FIRMA DEL PREOPINANTE";
        } else if (Constantes.ACCION_ACTUACIONES_AGREGAR_FIRMANTES.equals(accion)) {
            titulo = "ACTUACIONES P/ AGREGAR FIRMANTES";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)) {
            titulo = "ACTUACIONES P/ LA FIRMA DE LOS MIEMBROS";
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE_OF_PROV.equals(accion)) {
            titulo = "OFICIOS Y PROVIDENCIAS P/ REVISION";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV.equals(accion)) {
            titulo = "OFICIOS Y PROVIDENCIAS P/ LA FIRMA";
        } else if (Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV.equals(accion)) {
            titulo = "OFICIOS Y PROVIDENCIAS PENDIENTES";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_RESOLUCIONES.equals(accion)) {
            titulo = "RESOLUCIONES COMO PREOPINANTE P/ LA FIRMA";
            tituloAlt = "RESOLUCIONES COMO MIEMBRO P/ LA FIRMA";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion)) {
            titulo = "ACTUACIONES P/ LA FIRMA DEL SECRETARIO";
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_EXSECRETARIO.equals(accion)) {
            titulo = "ACTUACIONES P/ LA FIRMA DEL EXSECRETARIO";
        } else if (Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion)) {
            titulo = "ACTUACIONES FINALIZADAS";
        } else if (Constantes.ACCION_ACTUACIONES_OFICIO_ELECT.equals(accion)) {
            titulo = "OFICIOS ELECTRÓNICO";
        } else {
            // JsfUtil.addErrorMessage("Accion no permitida:: " + accion);
            return;
        }

        List<Integer> listaMiembros = new ArrayList<>();

        listaMiembros.add(Constantes.ROL_MIEMBRO);
        listaMiembros.add(Constantes.ROL_EXMIEMBRO);
        listaMiembros.add(Constantes.ROL_MIEMBRO_CON_PERMISO);
        listaMiembros.add(Constantes.ROL_MIEMBRO_SUPLENTE);
        listaMiembros.add(Constantes.ROL_MIEMBRO_REEMPLAZANTE);

        listaPersonas = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolesEstado", Personas.class).setParameter("roles", listaMiembros).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        List<Personas> listaPre = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_PRESIDENTE).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        listaPersonas.addAll(listaPre);

        esRelator = false;
        if (Constantes.ROL_RELATOR.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_PRESIDENTE.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_PRESIDENTE_RES_ACTAS.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_PRESIDENTE_OF_PROV_RES_ACTAS.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_EXMIEMBRO.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_MIEMBRO_CON_PERMISO.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_MIEMBRO_SUPLENTE.equals(rolElegido.getId())
                || Constantes.ROL_RELATOR_MIEMBRO_REEMPLAZANTE.equals(rolElegido.getId())) {

            List<ExpPersonasAsociadas> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();
            if (!lista.isEmpty()) {
                personaSelected = lista.get(0).getPersona();
                esRelator = true;
            } else {
                personaSelected = personaUsuario;
            }
        } else if (Constantes.ROL_MIEMBRO.equals(rolElegido.getId())
                || Constantes.ROL_MIEMBRO_REEMPLAZANTE.equals(rolElegido.getId())
                || Constantes.ROL_MIEMBRO_SUPLENTE.equals(rolElegido.getId())
                || Constantes.ROL_MIEMBRO_CON_PERMISO.equals(rolElegido.getId())
                || Constantes.ROL_EXMIEMBRO.equals(rolElegido.getId())
                || Constantes.ROL_PRESIDENTE.equals(rolElegido.getId())
                || Constantes.ROL_EXSECRETARIO.equals(rolElegido.getId())) {
            personaSelected = personaUsuario;
        } else {
            personaSelected = null;
        }

        buscar(personaSelected, rolElegido, filtrarResoluciones, filtrarProvidencias, filtrarOficios);

    }

    public String rowClassEstadoProceso(ExpActuaciones doc) {
        if (doc != null) {
            if(doc.isUrgente()){
                return "redOscuro";
            }else{
                if (doc.getDocumentoJudicial().getFechaInicioEnjuiciamiento() != null) {
                    if (doc.getDocumentoJudicial().getEstadoProcesoExpedienteElectronico().isEnProceso()) {
                        Date now = ejbFacade.getSystemDateOnly(0);
                        Long dias = Utils.cantDiasEnjuiciamiento(doc.getDocumentoJudicial(), now, ejbFacade.getEntityManager());
                        return dias > 120 ? "red" : dias > 60 ? "yellow" : "green";
                    }
                }
            }
        }

        return "";
    }
    
    public Long cantDiasEnjuiciamiento(ExpActuaciones act) {
        Date now = ejbFacade.getSystemDateOnly(0);
        return Utils.cantDiasEnjuiciamiento(act.getDocumentoJudicial(), now, ejbFacade.getEntityManager());
    }

    public String rowClass(ExpActuaciones act) {
        // #{item.tipoActuacion.id==9?(item.leido==null?'while':(!item.leido?'red':'white')):'white'}
        if(act.isUrgente()){
            return "redOscuro";  
        }else if (Constantes.TIPO_ACTUACION_NOTIFICACION.equals(act.getTipoActuacion().getId())) {
            return !act.isLeido() ? "red" : "white";
        } else if (((Constantes.TIPO_ACTUACION_PROVIDENCIA.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_OFICIO.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()))
                && Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion))
                || ((Constantes.TIPO_ACTUACION_SD.equals(act.getTipoActuacion().getId())
                || Constantes.TIPO_ACTUACION_RESOLUCION.equals(act.getTipoActuacion().getId()))
                && Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_RESOLUCIONES.equals(accion))) {
            List<ExpPersonasFirmantesPorActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getResultList();
            if (lista.isEmpty()) {
                List<ExpPersonasAsociadas> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();
                if (lista2.isEmpty()) {
                    return "white";
                } else {
                    List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", lista2.get(0).getPersona()).setParameter("estado", new Estados("AC")).getResultList();
                    if (lista3.isEmpty()) {
                        return "white";
                    } else {
                        return lista3.get(0).isFirmado() ? "white" : (lista3.get(0).isRevisado() ? "green" : "red");
                    }
                }
            } else {
                return lista.get(0).isFirmado() ? "white" : (lista.get(0).isRevisado() ? "green" : "red");
            }
        } else if (Constantes.TIPO_ACTUACION_OFICIO_CORTE.equals(act.getTipoActuacion().getId())) {
            return !act.isProveidoCorte() ? "red" : "white";
        } else {
            return "white";
        }
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

    @Override
    public Collection<ExpActuaciones> getItems() {
        return super.getItems2();
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
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

    public void navigateActuacion(ExpActuaciones not) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", String.valueOf(not.getId()));

            //if (Constantes.ACCION_PENDIENTES.equals(accion)) {
            //    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudicialesPorSecretaria/index.xhtml?tipo=" + Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA);
            //} else {
            /*
            not.setLeido(true);
            setSelected(not);
            super.save(null);
            setSelected(null);
             */
            if (filtroURL.verifPermiso(Constantes.PERMISO_REGISTRAR_ACTUACION_POR_SECRETARIA)) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudicialesPorSecretaria/index.xhtml?tipo=" + Constantes.ACCION_REGISTRAR_ACTUACION_SECRETARIA + "&pestanas=" + accion);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudiciales/index.xhtml?tipo=" + Constantes.ACCION_CONSULTA);
            }
            //}
        } catch (IOException ex) {
        }
    }

    public boolean renderedVerifOficios() {
        if (Constantes.ACCION_ACTUACIONES_OFICIO_ELECT.equals(accion) && filtroURL.verifPermiso("/pages/expActuaciones/index.xhtml", "OFICIO_ELECTRONICO")) {
            return true;
        }
        return false;
    }

    public boolean renderedFiltros() {
        if (Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)
                || Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE_OF_PROV.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV.equals(accion)
                || Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV.equals(accion)
                || Constantes.ACCION_ACTUACIONES_AGREGAR_FIRMANTES.equals(accion)
                || Constantes.ACCION_ACTUACIONES_REVISION_SECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_EXSECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion)
                || Constantes.ACCION_ACTUACIONES_EN_PROYECTO.equals(accion)) {
            return true;
        }
        return false;
    }

    public boolean renderedFiltrosResoluciones() {
        if (Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)
                || Constantes.ACCION_ACTUACIONES_AGREGAR_FIRMANTES.equals(accion)
                || Constantes.ACCION_ACTUACIONES_REVISION_SECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_EXSECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion)
                || Constantes.ACCION_ACTUACIONES_EN_PROYECTO.equals(accion)) {
            return true;
        }
        return false;
    }

    public boolean renderedPendientesAlt() {
        if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_RESOLUCIONES.equals(accion)) {
            return true;
        }
        return false;
    }

    public boolean renderedFiltroPersonas() {
        if (Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)
                || Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE_OF_PROV.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV.equals(accion)
                || Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV.equals(accion)
                || Constantes.ACCION_ACTUACIONES_AGREGAR_FIRMANTES.equals(accion)
                || Constantes.ACCION_ACTUACIONES_REVISION_SECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FIRMA_EXSECRETARIO.equals(accion)
                || Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion)
                || Constantes.ACCION_ACTUACIONES_EN_PROYECTO.equals(accion)) {
            return true;
        }
        return false;
    }
    
    public String revisado(ExpActuaciones act){
        return act==null?"":(act.getPersonaRevisado()==null?"images/no.png":"images/si.png");
    }

    public List<ExpActuaciones> obtenerParaLaFirma() {
        return ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findParaLaFirma", ExpActuaciones.class).setParameter("estado", new Estados("AC")).getResultList();
    }

    public List<ExpActuaciones> obtenerNotificaciones(boolean soloNoLeidos) {
        Date fecha = ejbFacade.getSystemDate();
        if (soloNoLeidos) {
            // List<ExpActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDestinatarioTipoActuacionLeido", ExpActuaciones.class).setParameter("destinatario", personaUsuario).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_NOTIFICACION).setParameter("leido", false).getResultList();
            List<ExpActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDestinatarioTipoActuacionLeidoFechaPresentacion", ExpActuaciones.class).setParameter("destinatario", personaUsuario).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_NOTIFICACION).setParameter("leido", false).setParameter("fechaPresentacion", fecha).getResultList();
            return lista;
        } else {
            // List<ExpActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDestinatarioTipoActuacion", ExpActuaciones.class).setParameter("destinatario", personaUsuario).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_NOTIFICACION).getResultList();
            List<ExpActuaciones> lista = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDestinatarioTipoActuacionFechaPresentacion", ExpActuaciones.class).setParameter("destinatario", personaUsuario).setParameter("tipoActuacion", Constantes.TIPO_ACTUACION_NOTIFICACION).setParameter("fechaPresentacion", fecha).getResultList();
            return lista;
        }
    }

    public List<ExpActuaciones> obtenerCantidadActuacionesEstadoPresidente(String estado, Personas persona, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios) {
        return obtenerCantidadActuacionesEstadoPresidente(estado, persona, incluirResoluciones, incluirProvidencias, incluirOficios, accion, false, false);
    }

    public List<ExpActuaciones> obtenerCantidadActuacionesEstadoPresidente(String estado, Personas persona, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios, String accion, boolean pendientes, boolean esRelator) {
        String preopinante = "";
        String estadoString = "  and a.estado = '" + estado + "' ";
        String tipoActuacion = "";
        if (persona != null && (estado.equals(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE) || estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE))) {
            tipoActuacion = " and a.tipo_actuacion not in (" + Constantes.TIPO_ACTUACION_RESOLUCION + "," + Constantes.TIPO_ACTUACION_SD + ") ";
            preopinante = " and (a.preopinante = " + persona.getId() + " or a.preopinante in (select persona from exp_personas_asociadas where persona_asociada = " + persona.getId() + ")) ";
        }

        if (persona != null && (estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS))) {
            tipoActuacion = " and a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_RESOLUCION + "," + Constantes.TIPO_ACTUACION_SD + ") ";
            preopinante = " and (select count(*) > 0 from exp_personas_firmantes_por_actuaciones as p where p.actuacion = a.id and p.persona_firmante = " + persona.getId() + " and not p.firmado) ";
            estadoString = "  and a.estado in ('" + Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS + "','" + Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE + "','" + Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE + "') ";
        }

        if (persona != null && accion != null && !esRelator) {
            if (Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV.equals(accion)) {
                if (pendientes) {
                    tipoActuacion += " and ((a.preopinante = " + persona.getId() + " and a.pendiente ) or (a.preopinante <> " + persona.getId() + " and false))";
                } else {
                    tipoActuacion += " and ((a.preopinante = " + persona.getId() + " and not a.pendiente ) or (a.preopinante <> " + persona.getId() + " and false))";
                }
            }
            if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV.equals(accion)) {
                if (pendientes) {
                    tipoActuacion += " and ((a.preopinante = " + persona.getId() + " and a.pendiente ) or (a.preopinante <> " + persona.getId() + " and true))";
                } else {
                    tipoActuacion += " and ((a.preopinante = " + persona.getId() + " and not a.pendiente ) or (a.preopinante <> " + persona.getId() + " and true))";
                }
            }
        }

        String mas = "";
        if (incluirOficios) {
            mas = " and a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_OFICIO + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE;
        }
        if (incluirProvidencias) {
            if ("".equals(mas)) {
                mas = " and a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_PROVIDENCIA;
            } else {
                mas += " ," + Constantes.TIPO_ACTUACION_PROVIDENCIA;
            }
        }
        if (incluirResoluciones) {
            if ("".equals(mas)) {
                mas = " and a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_RESOLUCION;
            } else {
                mas += " ," + Constantes.TIPO_ACTUACION_RESOLUCION;
            }
        }

        if (!"".equals(mas)) {
            mas += ") ";
        }

        // String comando = "select a.* from exp_actuaciones as a where a.formato is not null" + estadoString + tipoActuacion + preopinante + mas;

        String comando = "select a.*, ifnull(((DATEDIFF(CURRENT_DATE() , d.fecha_inicio_enjuiciamiento)) - ((WEEK(CURRENT_DATE()) - WEEK(d.fecha_inicio_enjuiciamiento)) * 2) - (case when weekday(CURRENT_DATE()) = 6 then 1 else 0 end) - (case when weekday(d.fecha_inicio_enjuiciamiento) = 5 then 1 else 0 end) - (SELECT COUNT(*) FROM exp_feriados WHERE fecha = d.fecha_inicio_enjuiciamiento and fecha<=CURRENT_DATE()) - 1), 0) as plazo from exp_actuaciones as a, documentos_judiciales as d where a.documento_judicial = d.id and a.formato is not null" + estadoString + tipoActuacion + preopinante + mas + " order by a.urgente desc, plazo desc";

        // String comando = "select a.*, p.nombres_apellidos as personarevisado, f.fecha_hora_revisado as fechahorarevisado from exp_actuaciones as a left join exp_personas_firmantes_por_actuaciones as f on (f.actuacion = a.id and f.persona_firmante = " + (persona==null?0:persona.getId()) + ") left join personas as p on (f.persona_revisado = p.id) where a.formato is not null" + estadoString + tipoActuacion + preopinante + mas;
        // return ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).getResultList();

        List<ExpActuaciones> lista = ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).getResultList();

        for(ExpActuaciones act : lista){
            List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", persona).setParameter("estado", new Estados("AC")).getResultList();
            if(!lista3.isEmpty()){
                act.setPersonaRevisado(lista3.get(0).getPersonaRevisado()==null?null:lista3.get(0).getPersonaRevisado().getNombresApellidos());
                act.setFechaHoraRevisado(lista3.get(0).getFechaHoraRevisado());
            }

        }

        return lista;

        //    if(personaSelected != null){
        //    return ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByEstado", ExpActuaciones.class).setParameter("estado", new ExpEstadosActuacion(estado)).getResultList();
        // }else{
        //    return ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByPreopinanteEstado", ExpActuaciones.class).setParameter("preopinante", personaSelected).setParameter("estado", new ExpEstadosActuacion(estado)).getResultList();
        //}
    }

    public List<ExpActuaciones> obtenerOficiosElectronicos(boolean soloProveidos) {
        // String comando = "select a.* from exp_actuaciones as a where tipo_actuacion = " + Constantes.TIPO_ACTUACION_OFICIO_CORTE + " AND cod_actuacion_caso is not null " + (soloProveidos ? " AND proveido_corte = true " : "") + " ORDER BY a.proveido_corte, a.fecha_hora_alta DESC";

        String comando = "select a.* from exp_actuaciones as a where tipo_actuacion = " + Constantes.TIPO_ACTUACION_OFICIO_CORTE + " AND cod_actuacion_caso is not null " + (soloProveidos ? " AND proveido_corte = true " : "") + " ORDER BY a.proveido_corte, a.fecha_hora_alta DESC";
        return ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).getResultList();
    }

    public List<ExpActuaciones> obtenerCantidadActuacionesEstado(String estado, Personas persona, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios) {
        String preopinante = "";
        if (persona != null && (estado.equals(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE) || estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE))) {
            preopinante = " and (a.preopinante = " + persona.getId() + " or a.preopinante in (select persona from exp_personas_asociadas where persona_asociada = " + persona.getId() + ")) ";
        } else if (persona != null && estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO)) {
            preopinante = " and a.secretario = " + persona.getId() + " ";
        } else if (persona != null && estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS)) {
            preopinante = " and (select count(*) > 0 from exp_personas_firmantes_por_actuaciones as p where p.actuacion = a.id and p.persona_firmante = " + persona.getId() + " and not p.firmado) ";
        }

        String mas = "";
        if (incluirOficios) {
            mas = " and a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_OFICIO + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE;
        }
        if (incluirProvidencias) {
            if ("".equals(mas)) {
                mas = " and a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_PROVIDENCIA;
            } else {
                mas += " ," + Constantes.TIPO_ACTUACION_PROVIDENCIA;
            }
        }
        if (incluirResoluciones) {
            if ("".equals(mas)) {
                mas = " and a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_RESOLUCION;
            } else {
                mas += " ," + Constantes.TIPO_ACTUACION_RESOLUCION;
            }
        }

        if (!"".equals(mas)) {
            mas += ") ";
        }

        // String comando = "select a.*, p.nombres_apellidos as personarevisado, f.fecha_hora_revisado as fechahorarevisado from exp_actuaciones as a left join exp_personas_firmantes_por_actuaciones as f on (f.actuacion = a.id and f.persona_firmante = " + (persona==null?0:persona.getId()) + ") left join personas as p on (f.persona_revisado = p.id) where a.formato is not null and a.estado = ?1" + preopinante + mas;

        // String comando = "select a.* from exp_actuaciones as a where a.formato is not null and a.estado = ?1" + preopinante + mas + " order by a.urgente desc";

        String comando = "select a.*, ifnull(((DATEDIFF(CURRENT_DATE() , d.fecha_inicio_enjuiciamiento)) - ((WEEK(CURRENT_DATE()) - WEEK(d.fecha_inicio_enjuiciamiento)) * 2) - (case when weekday(CURRENT_DATE()) = 6 then 1 else 0 end) - (case when weekday(d.fecha_inicio_enjuiciamiento) = 5 then 1 else 0 end) - (SELECT COUNT(*) FROM exp_feriados WHERE fecha = d.fecha_inicio_enjuiciamiento and fecha<=CURRENT_DATE()) - 1), 0) as plazo from exp_actuaciones as a, documentos_judiciales as d where a.documento_judicial = d.id and a.formato is not null and a.estado = ?1" + preopinante + mas + " order by a.urgente desc, plazo desc, a.fecha_presentacion, a.fecha_hora_alta";
        return ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).setParameter(1, estado).getResultList();

        /* CAMBIOS PLAZO ENJUICIAMIENTO
        List<ExpActuaciones> lista = ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).setParameter(1, estado).getResultList();

        for(ExpActuaciones act : lista){
            List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", persona).setParameter("estado", new Estados("AC")).getResultList();
            if(!lista3.isEmpty()){
                act.setPersonaRevisado(lista3.get(0).getPersonaRevisado()==null?null:lista3.get(0).getPersonaRevisado().getNombresApellidos());
                act.setFechaHoraRevisado(lista3.get(0).getFechaHoraRevisado());
            }

        }

        return lista;
        
        */
    }

    public List<ExpActuaciones> obtenerCantidadActuacionesEstadoPreopinante(String estado, Personas persona) {
        String preopinante = "";
        if (persona != null && (estado.equals(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE) || estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE))) {
            preopinante = " and (a.preopinante = " + persona.getId() + " or a.preopinante in (select persona from exp_personas_asociadas where persona_asociada = " + persona.getId() + ")) ";
        } else if (persona != null && estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO)) {
            preopinante = " and a.secretario = " + persona.getId() + " ";
        } else if (persona != null && estado.equals(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS)) {
            preopinante = " and (select count(*) > 0 from exp_personas_firmantes_por_actuaciones as p where p.actuacion = a.id and p.persona_firmante = " + persona.getId() + " and not p.firmado) ";
        }

        //String comando = "select a.*, p.nombres_apellidos as personarevisado, f.fecha_hora_revisado as fechahorarevisado from exp_actuaciones as a left join exp_personas_firmantes_por_actuaciones as f on (f.actuacion = a.id and f.persona_firmante = " + (persona==null?0:persona.getId()) + ") left join personas as p on (f.persona_revisado = p.id) where a.formato is not null and a.estado = ?1" + preopinante;

        // String comando = "select a.* from exp_actuaciones as a where a.formato is not null and a.estado = ?1" + preopinante + " order by a.urgente desc";

        String comando = "select a.*, ifnull(((DATEDIFF(CURRENT_DATE() , d.fecha_inicio_enjuiciamiento)) - ((WEEK(CURRENT_DATE()) - WEEK(d.fecha_inicio_enjuiciamiento)) * 2) - (case when weekday(CURRENT_DATE()) = 6 then 1 else 0 end) - (case when weekday(d.fecha_inicio_enjuiciamiento) = 5 then 1 else 0 end) - (SELECT COUNT(*) FROM exp_feriados WHERE fecha = d.fecha_inicio_enjuiciamiento and fecha<=CURRENT_DATE()) - 1), 0) as plazo from exp_actuaciones as a, documentos_judiciales as d where a.documento_judicial = d.id and a.formato is not null and a.estado = ?1" + preopinante + " order by a.urgente desc, plazo desc, a.fecha_presentacion, a.fecha_hora_alta";
        return ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).setParameter(1, estado).getResultList();
/* CAMBIOS PLAZO ENJUICIAMIENTO
        List<ExpActuaciones> lista = ejbFacade.getEntityManager().createNativeQuery(comando, ExpActuaciones.class).setParameter(1, estado).getResultList();

        for(ExpActuaciones act : lista){
            List<ExpPersonasFirmantesPorActuaciones> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", ExpPersonasFirmantesPorActuaciones.class).setParameter("actuacion", act).setParameter("personaFirmante", persona).setParameter("estado", new Estados("AC")).getResultList();
            if(!lista3.isEmpty()){
                act.setPersonaRevisado(lista3.get(0).getPersonaRevisado()==null?null:lista3.get(0).getPersonaRevisado().getNombresApellidos());
                act.setFechaHoraRevisado(lista3.get(0).getFechaHoraRevisado());
            }

        }
        return lista;
*/
    }

    public List<EstadoCantidad> obtenerCantidadActuacionesEstadoTodos(List<String> listaEstados) {

        String mas = "";

        if (listaEstados != null) {
            for (String estado : listaEstados) {
                if (!"".equals(mas)) {
                    mas += ",";
                }
                mas += "'" + estado + "'";
            }

            if (!"".equals(mas)) {
                mas = " and a.estado in (" + mas + ")";
            }
        }

        if (!"".equals(mas)) {
            mas += " and ";
        }

        mas += " a.tipo_actuacion in (" + Constantes.TIPO_ACTUACION_OFICIO + ", " + Constantes.TIPO_ACTUACION_OFICIO_CORTE + ", " + Constantes.TIPO_ACTUACION_PROVIDENCIA + ", " + Constantes.TIPO_ACTUACION_RESOLUCION + ") ";

        String comando = "select a.estado, count(*) as cantidad from exp_actuaciones as a where a.formato is not null" + mas + " group by a.estado";
        return ejbFacade.getEntityManager().createNativeQuery(comando, EstadoCantidad.class).getResultList();
    }

    public List<List<ExpActuaciones>> obtenerPresentacionesPendientes2() {
        ExpActuaciones act = null;
        List<List<ExpActuaciones>> array = new ArrayList<>(2);

        array.add(new ArrayList<>());
        array.add(new ArrayList<>());

        // List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.id in (select max(b.id) from exp_actuaciones as b where b.documento_judicial = ?1 and (select ifnull(e.destinatario,0) from exp_notificaciones as e where e.actuacion = b.id) <> ?2) and a.visible = true", ExpActuaciones.class).setParameter(1, doc.getId()).setParameter(2, personaUsuario.getId()).getResultList();
        List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a \n"
                + "where a.documento_judicial in (select b.documento_judicial from exp_partes_por_documentos_judiciales as b where b.persona = ?1 union select c.documento_judicial from personas_por_documentos_judiciales as c where c.persona = ?2) \n"
                + "and a.persona in (select b.persona from exp_partes_por_documentos_judiciales as b where b.documento_judicial = a.documento_judicial union select c.persona from personas_por_documentos_judiciales as c where c.documento_judicial = a.documento_judicial)\n"
                + "and a.visible = true order by a.fecha_hora_alta desc limit 10", ExpActuaciones.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).getResultList();
        if (!listAct.isEmpty()) {
            array.get(0).addAll(listAct);
        }

        // List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.id in (select max(b.id) from exp_actuaciones as b where b.documento_judicial = ?1 and (select ifnull(e.destinatario,0) from exp_notificaciones as e where e.actuacion = b.id) <> ?2) and a.visible = true", ExpActuaciones.class).setParameter(1, doc.getId()).setParameter(2, personaUsuario.getId()).getResultList();
        List<ExpActuaciones> listAct2 = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a \n"
                + "where a.documento_judicial in (select b.documento_judicial from exp_partes_por_documentos_judiciales as b where b.persona = ?1 union select c.documento_judicial from personas_por_documentos_judiciales as c where c.persona = ?2) \n"
                + "and a.persona not in (select b.persona from exp_partes_por_documentos_judiciales as b where b.documento_judicial = a.documento_judicial union select c.persona from personas_por_documentos_judiciales as c where c.documento_judicial = a.documento_judicial)\n"
                + "and a.visible = true order by a.fecha_hora_alta desc limit 10", ExpActuaciones.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).getResultList();
        if (!listAct.isEmpty()) {
            array.get(1).addAll(listAct2);
        }

        return null;

    }
// 
    
    public boolean deshabilitarDatosRevision(){
        return !(Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_RESOLUCIONES.equals(accion));
        /*
        return !(Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE_OF_PROV.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_RESOLUCIONES.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FIRMA_EXSECRETARIO.equals(accion) ||
            Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion));
        */
    }

    public boolean deshabilitarDatosSecretaria() {
        return filtroURL.verifPermiso(Constantes.PERMISO_VER_DATOS_SECRETARIA);
    }

    public boolean renderedBandejaPendientes() {
        return accion == null ? false : (Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV.equals(accion) || Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV.equals(accion) ? filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_PENDINETES_PRESIDENTE_OF_PROV, rolElegido.getId()) : false);
    }

    public List<List<ExpActuaciones>> obtenerPresentacionesPendientes() {

        Calendar fecha1 = Calendar.getInstance();
        List<DocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", new CanalesEntradaDocumentoJudicial(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE)).getResultList();

        Utils.timeStamp("findByCanalEntradaDocumentoJudicial: ", fecha1, Calendar.getInstance());
        List<List<ExpActuaciones>> array = new ArrayList<>(2);

        array.add(new ArrayList<>());
        array.add(new ArrayList<>());

        //List<ExpActuaciones> listaFinal = new ArrayList<>();
        //List<ExpActuaciones> listaFinal2 = new ArrayList<>();
        for (DocumentosJudiciales doc : lista) {
            ExpActuaciones act = null;
            fecha1 = Calendar.getInstance();
            List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.documento_judicial = ?1 order by a.fecha_presentacion desc, a.fecha_hora_alta desc", ExpActuaciones.class).setParameter(1, doc.getId()).getResultList();

            Utils.timeStamp("actuaciones: ", fecha1, Calendar.getInstance());
            if (!listAct.isEmpty()) {
                act = listAct.get(0);
            }

            if (act != null) {
                fecha1 = Calendar.getInstance();
                List<ExpPartesPorDocumentosJudiciales> lista4 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                Utils.timeStamp("findByDocumentoJudicialEstado1: ", fecha1, Calendar.getInstance());
                boolean pertenece = false;
                for (ExpPartesPorDocumentosJudiciales par : lista4) {
                    if (par.getPersona().equals(personaUsuario)) {
                        pertenece = true;
                        break;
                    }
                }

                fecha1 = Calendar.getInstance();
                List<PersonasPorDocumentosJudiciales> lista5 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                Utils.timeStamp("findByDocumentoJudicialEstado2: ", fecha1, Calendar.getInstance());
                for (PersonasPorDocumentosJudiciales par : lista5) {
                    if (par.getPersona().equals(personaUsuario)) {
                        pertenece = true;
                        break;
                    }
                }

                fecha1 = Calendar.getInstance();
                List<ExpPartesPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                Utils.timeStamp("findByDocumentoJudicialEstado3: ", fecha1, Calendar.getInstance());
                boolean encontro = false;
                for (ExpPartesPorDocumentosJudiciales par : lista2) {
                    if (par.getPersona().equals(act.getPersonaAlta())) {
                        encontro = true;
                        break;
                    }
                }
                /*
                List<PersonasPorDocumentosJudiciales> lista3 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                for (PersonasPorDocumentosJudiciales par : lista3) {
                    if (par.getPersona().equals(act.getPersonaAlta())) {
                        encontro = true;
                        break;
                    }
                }
                 */
                if (Constantes.TIPO_ACTUACION_DOCUMENTAL.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_INCIDENTE.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_ESCRITO_PRESENTACION.equals(act.getTipoActuacion().getId())) {
                    encontro = true;
                }

                if (encontro) {
                    array.get(0).add(act);
                } else if (pertenece) {
                    if (act.isVisible()) {
                        array.get(1).add(act);
                    }
                }
            }

        }

        return array;
    }

    public List<List<ExpActuaciones>> obtenerPresentacionesPendientesNew_() {

        Calendar fecha1 = Calendar.getInstance();
        // List<DocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", new CanalesEntradaDocumentoJudicial(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE)).getResultList();

        List<CantidadItem> lista = ejbFacade.getEntityManager().createNativeQuery("select d.id as cantidad from documentos_judiciales as d where d.canal_entrada_documento_judicial = 'EE'", CantidadItem.class).getResultList();

        Utils.timeStamp("findByCanalEntradaDocumentoJudicial: ", fecha1, Calendar.getInstance());
        List<List<ExpActuaciones>> array = new ArrayList<>(2);

        array.add(new ArrayList<>());
        array.add(new ArrayList<>());

        //List<ExpActuaciones> listaFinal = new ArrayList<>();
        //List<ExpActuaciones> listaFinal2 = new ArrayList<>();
        for (CantidadItem doc : lista) {
            ExpActuaciones act = null;

            fecha1 = Calendar.getInstance();
            List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.documento_judicial = ?1 and fecha_presentacion in (select max(fecha_presentacion) from exp_actuaciones as a where a.documento_judicial = ?2) order by a.fecha_hora_alta desc", ExpActuaciones.class).setParameter(1, doc.getCantidad()).setParameter(2, doc.getCantidad()).getResultList();

            Utils.timeStamp("actuaciones: ", fecha1, Calendar.getInstance());
            if (!listAct.isEmpty()) {
                act = listAct.get(0);
            }

            if (act != null) {
                fecha1 = Calendar.getInstance();
                List<ExpPartesPorDocumentosJudiciales> lista4 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstadoPersona", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getCantidad()).setParameter("estado", new Estados("AC")).setParameter("persona", personaUsuario.getId()).getResultList();
                Utils.timeStamp("findByDocumentoJudicialEstadoPersona1: ", fecha1, Calendar.getInstance());

                /*
                boolean pertenece = false;
                for (ExpPartesPorDocumentosJudiciales par : lista4) {
                    if (par.getPersona().equals(personaUsuario)) {
                        pertenece = true;
                        break;
                    }
                }
                 */
                fecha1 = Calendar.getInstance();
                List<PersonasPorDocumentosJudiciales> lista5 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstadoPersona", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getCantidad()).setParameter("estado", new Estados("AC")).setParameter("persona", personaUsuario.getId()).getResultList();
                Utils.timeStamp("findByDocumentoJudicialEstadoPersona2: ", fecha1, Calendar.getInstance());

                boolean pertenece = !lista4.isEmpty() || !lista5.isEmpty();

                /*
                for (PersonasPorDocumentosJudiciales par : lista5) {
                    if (par.getPersona().equals(personaUsuario)) {
                        pertenece = true;
                        break;
                    }
                }
                 */
                fecha1 = Calendar.getInstance();
                List<ExpPartesPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstadoPersona", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getCantidad()).setParameter("estado", new Estados("AC")).setParameter("persona", act.getPersonaAlta().getId()).getResultList();
                Utils.timeStamp("findByDocumentoJudicialEstadoPersona3: ", fecha1, Calendar.getInstance());

                boolean encontro = !lista2.isEmpty();
                /*
                boolean encontro = false;
                for (ExpPartesPorDocumentosJudiciales par : lista2) {
                    if (par.getPersona().equals(act.getPersonaAlta())) {
                        encontro = true;
                        break;
                    }
                }
                 */
 /*
                List<PersonasPorDocumentosJudiciales> lista3 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                for (PersonasPorDocumentosJudiciales par : lista3) {
                    if (par.getPersona().equals(act.getPersonaAlta())) {
                        encontro = true;
                        break;
                    }
                }
                 */
                if (Constantes.TIPO_ACTUACION_DOCUMENTAL.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_INCIDENTE.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_ESCRITO_PRESENTACION.equals(act.getTipoActuacion().getId())) {
                    encontro = true;
                }

                if (encontro) {
                    array.get(0).add(act);
                } else if (pertenece) {
                    if (act.isVisible()) {
                        array.get(1).add(act);
                    }
                }
            }

        }

        return array;
    }

    public int[] obtenerPresentacionesPendientesNew() {
        int array[] = {0, 0};
        Date fecha = ejbFacade.getSystemDate();
        LocalDateTime hoy = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Calendar fecha1 = Calendar.getInstance();
        List<IdCantidad> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select b.id, b.tipo_actuacion, b.visible, b.fecha_presentacion, "
                + "(SELECT count(*) FROM exp_partes_por_documentos_judiciales r \n"
                + "WHERE r.documento_judicial =  b.documento_judicial AND r.estado = 'AC' AND r.persona = ?1) > 0 as parte,\n"
                + "(SELECT count(*) FROM personas_por_documentos_judiciales r \n"
                + "WHERE r.documento_judicial =  b.documento_judicial AND r.estado = 'AC' AND r.persona = ?2) > 0 as persona,\n"
                + "(SELECT count(*) FROM personas_por_documentos_judiciales r \n"
                + "WHERE r.documento_judicial =  b.documento_judicial AND r.estado = 'AC' AND r.persona = b.persona) > 0 as funcionario "
                + "from exp_actuaciones as b"
                + " where (b.documento_judicial, b.fecha_presentacion) in (select a.documento_judicial, max(fecha_presentacion) from exp_actuaciones as a group by a.documento_judicial) "
                + "and b.id in (select max(c.id) from exp_actuaciones as c where c.documento_judicial = b.documento_judicial and c.fecha_presentacion = b.fecha_presentacion)", IdCantidad.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).getResultList();
        Utils.timeStamp("Actuaciones: ", fecha1, Calendar.getInstance());

        for (IdCantidad act : listAct) {

            if (act != null) {
                boolean pertenece = act.isParte() || act.isPersona();

                boolean encontro = act.isFuncionario();
                if (Constantes.TIPO_ACTUACION_DOCUMENTAL.equals(act.getTipoActuacion())
                        || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion())
                        || Constantes.TIPO_ACTUACION_INCIDENTE.equals(act.getTipoActuacion())
                        || Constantes.TIPO_ACTUACION_ESCRITO_PRESENTACION.equals(act.getTipoActuacion())) {
                    encontro = true;
                }

                if (encontro) {
                    array[0]++;
                } else if (pertenece) {
                    if (act.isVisible()) {
                        LocalDateTime fechaPresentacion = act.getFechaPresentacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        if (fechaPresentacion.isBefore(hoy)) {
                            array[1]++;
                        }
                    }
                }
            }

        }

        return array;
    }

    public List<List<ExpActuaciones>> obtenerPresentacionesPendientesTodo() {
        List<List<ExpActuaciones>> array = new ArrayList<>(2);

        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        
        array.add(new ArrayList<>());
        array.add(new ArrayList<>());

        Date fecha = ejbFacade.getSystemDate();
        LocalDateTime hoy = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Calendar fecha1 = Calendar.getInstance();
        List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select b.*, "
                + "(SELECT count(*) FROM exp_partes_por_documentos_judiciales r \n"
                + "WHERE r.documento_judicial =  b.documento_judicial AND r.estado = 'AC' AND r.persona = ?1) as parte,\n"
                + "(SELECT count(*) FROM personas_por_documentos_judiciales r \n"
                + "WHERE r.documento_judicial =  b.documento_judicial AND r.estado = 'AC' AND r.persona = ?2) as acusado,\n"
                + "(SELECT count(*) FROM personas_por_documentos_judiciales r \n"
                + "WHERE r.documento_judicial =  b.documento_judicial AND r.estado = 'AC' AND r.persona = b.persona) as funcionario "
                + "from exp_actuaciones as b"
                + " where (b.documento_judicial, b.fecha_presentacion) in (select a.documento_judicial, max(fecha_presentacion) from exp_actuaciones as a group by a.documento_judicial) "
                + "and b.id in (select max(c.id) from exp_actuaciones as c where c.documento_judicial = b.documento_judicial and c.fecha_presentacion = b.fecha_presentacion)", ExpActuaciones.class).setParameter(1, personaUsuario.getId()).setParameter(2, personaUsuario.getId()).getResultList();
        Utils.timeStamp("Actuaciones: ", fecha1, Calendar.getInstance());

        for (ExpActuaciones act : listAct) {

            if (act != null) {
                boolean pertenece = act.getParte() > 0 || act.getAcusado() > 0;

                boolean encontro = act.getFuncionario() > 0;
                if (Constantes.TIPO_ACTUACION_DOCUMENTAL.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_PRIMER_ESCRITO.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_INCIDENTE.equals(act.getTipoActuacion().getId())
                        || Constantes.TIPO_ACTUACION_ESCRITO_PRESENTACION.equals(act.getTipoActuacion().getId())) {
                    encontro = true;
                }

                if (encontro) {
                    array.get(0).add(act);
                } else if (pertenece) {
                    if (act.isVisible()) {
                        LocalDateTime fechaPresentacion = act.getFechaPresentacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        if (fechaPresentacion.isBefore(hoy)) {
                            array.get(1).add(act);
                        }
                    }
                }
            }

        }

        return array;
    }

    public void buscar() {
        buscar(personaSelected, rolElegido, filtrarResoluciones, filtrarProvidencias, filtrarOficios);
    }

    private void buscar(Personas persona, AntecedentesRoles rol, boolean incluirResoluciones, boolean incluirProvidencias, boolean incluirOficios) {

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarResoluciones", incluirResoluciones);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarProvidencias", incluirProvidencias);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("filtrarOficios", incluirOficios);

        if (Constantes.ACCION_PENDIENTES_ABOGADO.equals(accion)
                || Constantes.ACCION_PENDIENTES.equals(accion)) {
            List<List<ExpActuaciones>> array = obtenerPresentacionesPendientes();
            // List<List<ExpActuaciones>> array = obtenerPresentacionesPendientesTodo();

            if (Constantes.ACCION_PENDIENTES.equals(accion)) {
                listaPendientes = array.get(0);
            } else if (Constantes.ACCION_PENDIENTES_ABOGADO.equals(accion)) {
                listaPendientes = array.get(1);
            }
        } else if (Constantes.ACCION_NOTIFICACION.equals(accion)) {
            listaPendientes = obtenerNotificaciones(false);
        } else if (Constantes.ACCION_PARA_LA_FIRMA.equals(accion)) {
            listaPendientes = obtenerParaLaFirma();
        } else if (Constantes.ACCION_ACTUACIONES_EN_PROYECTO.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_EN_PROYECTO, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_SECRETARIO.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_REVISION_SECRETARIO, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_DIRECTOR.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_REVISION_DIRECTOR, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_AGREGAR_FIRMANTES.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_AGREGAR_FIRMANTES, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_MIEMBROS.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_REVISION_PRESIDENTE_OF_PROV.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_REVISION_PRESIDENTE, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_OF_PROV.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE, persona, incluirResoluciones, incluirProvidencias, incluirOficios, accion, false, esRelator);
        } else if (Constantes.ACCION_ACTUACIONES_PENDIENTES_PRESIDENTE_OF_PROV.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_FIRMA_PRESIDENTE, persona, incluirResoluciones, incluirProvidencias, incluirOficios, accion, true, esRelator);
        } else if (Constantes.ACCION_ACTUACIONES_OFICIO_ELECT.equals(accion)) {
            listaPendientes = obtenerOficiosElectronicos(false);
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_PRESIDENTE_RESOLUCIONES.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstadoPresidente(Constantes.ESTADO_ACTUACION_FIRMA_MIEMBROS, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
            listaPendientesAlt = new ArrayList<>();
            for (ExpActuaciones act : listaPendientes) {
                if (personaSelected != null) {
                    if (!personaSelected.equals(act.getPreopinante())) {
                        listaPendientesAlt.add(act);
                    }
                }
            }
            for (ExpActuaciones act : listaPendientesAlt) {
                listaPendientes.remove(act);
            }

        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_SECRETARIO.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_SECRETARIO, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_FIRMA_EXSECRETARIO.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FIRMA_EXSECRETARIO, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else if (Constantes.ACCION_ACTUACIONES_FINALIZADAS.equals(accion)) {
            listaPendientes = obtenerCantidadActuacionesEstado(Constantes.ESTADO_ACTUACION_FINALIZADA, persona, incluirResoluciones, incluirProvidencias, incluirOficios);
        } else {
            listaPendientes = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.persona = ?1 ORDER BY a.fecha_hora_alta DESC LIMIT 10", ExpActuaciones.class).setParameter(1, personaUsuario.getId()).getResultList();
        }
        /*
        if(filtroURL.verifPermiso("/pages/expActuaciones/index.xhtml", "PENDIENTES", rolElegido.getId())){
            List<DocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", new CanalesEntradaDocumentoJudicial(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE)).getResultList();
            
            listaPendientes = new ArrayList<>();
            
            
            for(DocumentosJudiciales doc : lista){
                ExpActuaciones act = null;
                List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.id in (select max(b.id) from exp_actuaciones as b where b.documento_judicial = ?1) ORDER BY a.fecha_hora_alta DESC", ExpActuaciones.class).setParameter(1, doc.getId()).getResultList();
                if(!listAct.isEmpty()){
                    act = listAct.get(0);
                }
                
                if(act != null){
                    List<ExpPartesPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                    boolean encontro = false;
                    for(ExpPartesPorDocumentosJudiciales par : lista2){
                        if(par.getPersona().equals(act.getPersonaAlta())){
                            encontro = true;
                            break;
                        }
                    }

                    List<PersonasPorDocumentosJudiciales> lista3 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                    for(PersonasPorDocumentosJudiciales par : lista3){
                        if(par.getPersona().equals(act.getPersonaAlta())){
                            encontro = true;
                            break;
                        }
                    }


                    if(encontro){
                        listaPendientes.add(act);
                    }
                }
                
            }
            
        }else if(filtroURL.verifPermiso("/pages/expActuaciones/index.xhtml", "PENDIENTES_ABOGADO", rolElegido.getId())){
            List<DocumentosJudiciales> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", new CanalesEntradaDocumentoJudicial(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE)).getResultList();
            
            listaPendientes = new ArrayList<>();
            
            
            for(DocumentosJudiciales doc : lista){
                ExpActuaciones act = null;
                List<ExpActuaciones> listAct = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.id in (select max(b.id) from exp_actuaciones as b where b.documento_judicial = ?1) ORDER BY a.fecha_hora_alta DESC", ExpActuaciones.class).setParameter(1, doc.getId()).getResultList();
                if(!listAct.isEmpty()){
                    act = listAct.get(0);
                }
                
                if(act != null){
                    List<ExpPartesPorDocumentosJudiciales> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstado", ExpPartesPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                    boolean encontro = false;
                    for(ExpPartesPorDocumentosJudiciales par : lista2){
                        if(par.getPersona().equals(act.getPersonaAlta())){
                            encontro = true;
                            break;
                        }
                    }

                    List<PersonasPorDocumentosJudiciales> lista3 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", PersonasPorDocumentosJudiciales.class).setParameter("documentoJudicial", doc.getId()).setParameter("estado", new Estados("AC")).getResultList();
                    for(PersonasPorDocumentosJudiciales par : lista3){
                        if(par.getPersona().equals(act.getPersonaAlta())){
                            encontro = true;
                            break;
                        }
                    }


                    if(!encontro){
                        listaPendientes.add(act);
                    }
                }
                
            }
            
        }else{
        
            listaPendientes = this.ejbFacade.getEntityManager().createNativeQuery("select a.* from exp_actuaciones as a where a.persona = ?1 ORDER BY a.fecha_hora_alta DESC LIMIT 10", ExpActuaciones.class).setParameter(1, personaUsuario.getId()).getResultList();
        }

         */
    }

    public void actualizarPendiente(ExpActuaciones act) {
        if (act != null) {
            ExpActuaciones actAntes = getSelected();
            setSelected(act);
            super.save(null);

            setSelected(actAntes);

            buscar(personaSelected, rolElegido, filtrarResoluciones, filtrarProvidencias, filtrarOficios);

        }
    }

    public void actualizarOficiosElectronicos() {
        declarativeScheduler.consultarOficios();
        listaPendientes = obtenerOficiosElectronicos(false);
    }

}
