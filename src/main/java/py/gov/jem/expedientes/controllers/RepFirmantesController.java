package py.gov.jem.expedientes.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.RepFirmantes;
import py.gov.jem.expedientes.datasource.RepFirmantesDetalle;
import py.gov.jem.expedientes.datasource.RepPersonasUsuario;
import py.gov.jem.expedientes.datasource.RepPersonasUsuarioAsociados;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "repFirmantesController")
@ViewScoped
public class RepFirmantesController extends AbstractController<ExpPersonasFirmantesPorActuaciones> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    RepFirmantesController personasFirmantesPorActuacionesController;
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
    private boolean filtrarAI;
    private boolean filtrarSD;
    private boolean filtrarProvidencias;
    private boolean filtrarOficios;
    private List<Personas> listaPersonasSeleccionadas;
    private boolean filtrarResoluciones2;
    private boolean filtrarAI2;
    private boolean filtrarSD2;
    private boolean filtrarProvidencias2;
    private boolean filtrarOficios2;
    private List<Personas> listaPersonasSeleccionadas2;
    private List<Personas> listaPersonas;
    private List<AntecedentesRoles> listaRoles;
    private List<AntecedentesRoles> listaRolesSeleccionadas;
    private String miembros;
    private String miembros2;
    private String roles;
    private Date fechaDesde;
    private Date fechaHasta;

    public boolean isFiltrarResoluciones2() {
        return filtrarResoluciones2;
    }

    public void setFiltrarResoluciones2(boolean filtrarResoluciones2) {
        this.filtrarResoluciones2 = filtrarResoluciones2;
    }

    public boolean isFiltrarAI2() {
        return filtrarAI2;
    }

    public void setFiltrarAI2(boolean filtrarAI2) {
        this.filtrarAI2 = filtrarAI2;
    }

    public boolean isFiltrarSD2() {
        return filtrarSD2;
    }

    public void setFiltrarSD2(boolean filtrarSD2) {
        this.filtrarSD2 = filtrarSD2;
    }

    public boolean isFiltrarProvidencias2() {
        return filtrarProvidencias2;
    }

    public void setFiltrarProvidencias2(boolean filtrarProvidencias2) {
        this.filtrarProvidencias2 = filtrarProvidencias2;
    }

    public boolean isFiltrarOficios2() {
        return filtrarOficios2;
    }

    public void setFiltrarOficios2(boolean filtrarOficios2) {
        this.filtrarOficios2 = filtrarOficios2;
    }

    public List<Personas> getListaPersonasSeleccionadas2() {
        return listaPersonasSeleccionadas2;
    }

    public void setListaPersonasSeleccionadas2(List<Personas> listaPersonasSeleccionadas2) {
        this.listaPersonasSeleccionadas2 = listaPersonasSeleccionadas2;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public boolean isFiltrarAI() {
        return filtrarAI;
    }

    public void setFiltrarAI(boolean filtrarAI) {
        this.filtrarAI = filtrarAI;
    }

    public boolean isFiltrarSD() {
        return filtrarSD;
    }

    public void setFiltrarSD(boolean filtrarSD) {
        this.filtrarSD = filtrarSD;
    }

    public List<AntecedentesRoles> getListaRolesSeleccionadas() {
        return listaRolesSeleccionadas;
    }

    public void setListaRolesSeleccionadas(List<AntecedentesRoles> listaRolesSeleccionadas) {
        this.listaRolesSeleccionadas = listaRolesSeleccionadas;
    }

    public List<AntecedentesRoles> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<AntecedentesRoles> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public List<Personas> getListaPersonasSeleccionadas() {
        return listaPersonasSeleccionadas;
    }

    public void setListaPersonasSeleccionadas(List<Personas> listaPersonasSeleccionadas) {
        this.listaPersonasSeleccionadas = listaPersonasSeleccionadas;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
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

    public RepFirmantesController() {
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

        filtrarResoluciones = true;
        filtrarAI = true;
        filtrarSD = true;
        filtrarProvidencias = true;
        filtrarOficios = true;

        listaPersonas = obtenerPersonas();
        listaRoles = obtenerRoles();

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2022);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);
        fechaDesde = myCal.getTime();

        fechaHasta = ejbFacade.getSystemDateOnly();

        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaHasta);
        cal.add(Calendar.DATE, 1);
        fechaHasta = cal.getTime();

    }

    private List<Personas> obtenerPersonas() {
        String comando = "select p.* from personas as p where p.id in (select distinct a.persona_firmante from exp_personas_firmantes_por_actuaciones as a where a.estado = 'AC') order by p.nombres_apellidos";

        listaPersonas = ejbFacade.getEntityManager().createNativeQuery(comando, Personas.class).getResultList();

        listaPersonasSeleccionadas = new ArrayList<>();

        for (Personas per : listaPersonas) {
            List<AntecedentesRolesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByPersonaRol", AntecedentesRolesPorPersonas.class).setParameter("persona", per.getId()).setParameter("rol", Constantes.ROL_MIEMBRO).getResultList();
            if (!lista.isEmpty()) {
                listaPersonasSeleccionadas.add(per);
                continue;
            }
            List<AntecedentesRolesPorPersonas> lista2 = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByPersonaRol", AntecedentesRolesPorPersonas.class).setParameter("persona", per.getId()).setParameter("rol", Constantes.ROL_PRESIDENTE).getResultList();
            if (!lista2.isEmpty()) {
                listaPersonasSeleccionadas.add(per);
            }
        }

        return listaPersonas;
    }

    private List<AntecedentesRoles> obtenerRoles() {
        List<AntecedentesRoles> roles = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRoles.findAll", AntecedentesRoles.class).getResultList();
        listaRolesSeleccionadas = new ArrayList<>();

        for (AntecedentesRoles per : roles) {
            if (per.getId().equals(Constantes.ROL_MAGISTRADO) || per.getId().equals(Constantes.ROL_ABOGADO)) {
                listaRolesSeleccionadas.add(per);
            }
        }

        return roles;
    }

    private List<RepFirmantes> obtenerReporte() {

        String mas3 = "";

        miembros = "";

        if (listaPersonasSeleccionadas != null) {
            if (!listaPersonasSeleccionadas.isEmpty()) {
                for (Personas per : listaPersonasSeleccionadas) {
                    if (!"".equals(mas3)) {
                        mas3 += ", ";
                    }
                    mas3 += per.getId();

                    if (!"".equals(miembros)) {
                        miembros += ", ";
                    }

                    miembros += per.getNombresApellidos();
                }
            }
        }

        String mas = "";
        if (filtrarProvidencias) {
            mas = String.valueOf(Constantes.TIPO_ACTUACION_PROVIDENCIA);
        }

        if (filtrarResoluciones) {
            if (!"".equals(mas)) {
                mas += ", ";
            }
            mas += String.valueOf(Constantes.TIPO_ACTUACION_RESOLUCION) + ", " + String.valueOf(Constantes.TIPO_ACTUACION_SD);
        }

        if (filtrarOficios) {
            if (!"".equals(mas)) {
                mas += ", ";
            }
            mas += String.valueOf(Constantes.TIPO_ACTUACION_OFICIO_CORTE) + ", " + String.valueOf(Constantes.TIPO_ACTUACION_OFICIO);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String comando = "select concat(DATE_FORMAT(now(),'%H%i%S'),  p.id) as id, p.nombres_apellidos as nombre, count(*) as total, sum(case when a.firmado then 1 else 0 end) as firmados, (count(*) - sum(case when a.firmado then 1 else 0 end)) diferencia from personas as p, exp_personas_firmantes_por_actuaciones as a, exp_actuaciones as b where a.actuacion = b.id and a.persona_firmante = p.id and a.estado = 'AC' and b.tipo_actuacion in (" + mas + ") " + (!"".equals(mas3) ? " and a.persona_firmante in (" + mas3 + ")" : "") + " and a.fecha_hora_alta >= '" + format.format(fechaDesde) + "' and a.fecha_hora_alta <= '" + format.format(fechaHasta) + "' group by p.id, p.nombres_apellidos order by sum(case when a.firmado then 1 else 0 end) desc, count(*) desc";

        return ejbFacade.getEntityManager().createNativeQuery(comando, RepFirmantes.class).getResultList();

    }

    private List<RepFirmantesDetalle> obtenerReporteDetalle() {

        String mas3 = "";

        String mas2 = "";

        miembros2 = "";

        if (listaPersonasSeleccionadas2 != null) {
            if (!listaPersonasSeleccionadas.isEmpty()) {
                for (Personas per : listaPersonasSeleccionadas2) {
                    if (!"".equals(mas3)) {
                        mas3 += ", ";
                    }
                    mas3 += per.getId();

                    if (!"".equals(miembros2)) {
                        miembros2 += ", ";
                    }

                    miembros2 += per.getNombresApellidos();
                }
            }
        }

        String mas = "";
        if (filtrarProvidencias2) {
            mas = String.valueOf(Constantes.TIPO_ACTUACION_PROVIDENCIA);
        }

        if (filtrarResoluciones2) {
            if (!"".equals(mas)) {
                mas += ", ";
            }
            mas += String.valueOf(Constantes.TIPO_ACTUACION_RESOLUCION) + ", " + String.valueOf(Constantes.TIPO_ACTUACION_SD);
        }

        if (filtrarAI2) {
            if (!"".equals(mas)) {
                mas += ", ";
            }
            mas += String.valueOf(Constantes.TIPO_ACTUACION_RESOLUCION) + ", " + String.valueOf(Constantes.TIPO_ACTUACION_SD);

            if (!"".equals(mas2)) {
                mas2 += ", ";
            }
            mas2 += String.valueOf(Constantes.TIPO_RESOLUCION_AI);
        }

        if (filtrarSD2) {
            if (!"".equals(mas)) {
                mas += ", ";
            }
            mas += String.valueOf(Constantes.TIPO_ACTUACION_RESOLUCION) + ", " + String.valueOf(Constantes.TIPO_ACTUACION_SD);

            if (!"".equals(mas2)) {
                mas2 += ", ";
            }
            mas2 += String.valueOf(Constantes.TIPO_RESOLUCION_SD);
        }

        if (filtrarOficios2) {
            if (!"".equals(mas)) {
                mas += ", ";
            }
            mas += String.valueOf(Constantes.TIPO_ACTUACION_OFICIO_CORTE) + ", " + String.valueOf(Constantes.TIPO_ACTUACION_OFICIO);
        }

        String comando = "select a.id, p.nombres_apellidos as nombre, case when r.id is null then t.descripcion else r.desc_resolucion end as tipo, d.causa as causa, case when b.preopinante = a.persona_firmante then 'preopinante' else 'miembro' end as preopinante, case when b.tipo_actuacion in (4,21) then b.nro_final when b.tipo_actuacion in (13,18) then r.nro_resolucion else '' end as nro from personas as p, exp_personas_firmantes_por_actuaciones as a, documentos_judiciales as d, exp_tipos_actuacion as t, exp_actuaciones as b left join (select u.*, i.descripcion as desc_resolucion from resoluciones as u, tipos_resolucion as i where u.tipo_resolucion = i.id) as r on (b.resolucion = r.id) where b.tipo_actuacion = t.id and d.id = b.documento_judicial and a.actuacion = b.id and a.persona_firmante = p.id and a.estado = 'AC' and a.firmado = false and (b.estado in ('FM','FP') or b.estado is null) and b.tipo_actuacion in (" + mas + ") " + (!"".equals(mas3) ? " and a.persona_firmante in (" + mas3 + ")" : "") + " " + (!"".equals(mas2) ? " and (r.tipo_resolucion is null or r.tipo_resolucion in (" + mas2 + "))" : "") + " order by p.nombres_apellidos, case when b.preopinante = a.persona_firmante then 'preopinante' else 'miembro' end, d.fecha_hora_alta, b.fecha_hora_alta";

        return ejbFacade.getEntityManager().createNativeQuery(comando, RepFirmantesDetalle.class).getResultList();

    }

    public void imprimir() {

        if (listaPersonasSeleccionadas != null) {
            if (listaPersonasSeleccionadas.isEmpty()) {
                JsfUtil.addErrorMessage("Debe seleccionar firmantes");
                return;
            }

        } else {
            JsfUtil.addErrorMessage("Debe seleccionar firmantes");
            return;
        }

        if (!filtrarOficios && !filtrarProvidencias && !filtrarResoluciones) {
            JsfUtil.addErrorMessage("Debe seleccionar al menos un tipo de actuación");
            return;
        }

        HttpServletResponse httpServletResponse = null;
        try {
            List<RepFirmantes> lista = obtenerReporte();

            // List<Integer> cantFirma = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC')", Integer.class).getResultList();
            // List<Integer> cantFirmados = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC' and firmado)", Integer.class).getResultList();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM");

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("fechaDesde", format.format(fechaDesde));
            map.put("fechaHasta", format.format(fechaHasta));
            map.put("hora", format2.format(fecha));
            map.put("usuario", personaUsuario.getNombresApellidos());
            map.put("miembros", miembros);
            map.put("resoluciones", filtrarResoluciones ? "SI" : "NO");
            map.put("providencias", filtrarProvidencias ? "SI" : "NO");
            map.put("oficios", filtrarOficios ? "SI" : "NO");
            // map.put("cantFirma", cantFirma.isEmpty()?0:cantFirma.get(0));
            // map.put("cantFirmados", cantFirmados.isEmpty()?0:cantFirma.get(0));

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repFirmantes.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

    }

    public void imprimirDetalle() {

        if (listaPersonasSeleccionadas2 != null) {
            if (listaPersonasSeleccionadas2.isEmpty()) {
                JsfUtil.addErrorMessage("Debe seleccionar firmantes");
                return;
            }

        } else {
            JsfUtil.addErrorMessage("Debe seleccionar firmantes");
            return;
        }

        if (!filtrarOficios2 && !filtrarProvidencias2 && !filtrarResoluciones2 && !filtrarSD2 && !filtrarAI2) {
            JsfUtil.addErrorMessage("Debe seleccionar al menos un tipo de actuación");
            return;
        }

        HttpServletResponse httpServletResponse = null;
        try {
            List<RepFirmantesDetalle> lista = obtenerReporteDetalle();

            // List<Integer> cantFirma = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC')", Integer.class).getResultList();
            // List<Integer> cantFirmados = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC' and firmado)", Integer.class).getResultList();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM");

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("hora", format2.format(fecha));
            map.put("usuario", personaUsuario.getNombresApellidos());
            map.put("miembros", miembros);
            map.put("resoluciones", filtrarResoluciones2 ? "SI" : "NO");
            map.put("providencias", filtrarProvidencias2 ? "SI" : "NO");
            map.put("oficios", filtrarOficios2 ? "SI" : "NO");
            // map.put("cantFirma", cantFirma.isEmpty()?0:cantFirma.get(0));
            // map.put("cantFirmados", cantFirmados.isEmpty()?0:cantFirma.get(0));

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repFirmantesDetalle.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

    }

    private List<RepPersonasUsuario> obtenerRepPersonasUsuario(boolean incluirTodasPersonas) {
        String mas3 = "";

        roles = "";

        if (listaRolesSeleccionadas != null) {
            if (listaRolesSeleccionadas.size() > 0) {
                for (AntecedentesRoles per : listaRolesSeleccionadas) {
                    if (!"".equals(mas3)) {
                        mas3 += ", ";
                    }
                    mas3 += per.getId();

                    if (!"".equals(roles)) {
                        roles += ", ";
                    }

                    roles += per.getDescripcion();
                }
            }
        }

        String comando = "";

        if (incluirTodasPersonas) {
            comando = "select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id) where p.estado = 'AC' order by p.nombres_apellidos";

        } else {
            comando = "select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id) where p.usuario is not null and p.estado = 'AC' " + (!"".equals(mas3) ? " and p.id in (select r.persona from antecedentes_roles_por_personas as r where r.rol in (" + mas3 + ")) " : " and p.id = -1 ") + " order by p.nombres_apellidos";
        }

        return ejbFacade.getEntityManager().createNativeQuery(comando, RepPersonasUsuario.class).getResultList();

    }

    private List<RepPersonasUsuarioAsociados> obtenerRepPersonasUsuarioAsociados() {
        /*
        String comando = "select concat(ifnull(p.id,0), a.id) as id, ifnull(p.id,0) as persona_id, p.ci, p.nombre, p.cargo, p.telefono, p.email, a.exp_administrativo, a.res_exp_administrativo, a.exp_electronico, a.res_exp_electronico from (select d.id, e.persona, '' as exp_administrativo, '' as res_exp_administrativo, d.causa as exp_electronico, '' as res_exp_electronico from exp_partes_por_documentos_judiciales as e, documentos_judiciales as d where e.estado = 'AC' and e.documento_judicial = d.id) as a left join\n" +
"(select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id) where p.estado = 'AC') as p\n" +
"on (a.persona = p.id)\n" +
"union\n" +
"select concat(ifnull(p.id,0), a.id) as id, ifnull(p.id,0) as persona_id, p.ci, p.nombre, p.cargo, p.telefono, p.email, a.exp_administrativo, a.res_exp_administrativo, a.exp_electronico, a.res_exp_electronico from (select d.id, e.persona, case when d.canal_entrada_documento_judicial = 'SE' then d.causa else '' end as exp_administrativo, '' as res_exp_administrativo, case when d.canal_entrada_documento_judicial = 'EE' then d.causa else '' end as exp_electronico, '' as res_exp_electronico from personas_por_documentos_judiciales as e, documentos_judiciales as d where e.estado = 'AC' and d.canal_entrada_documento_judicial IN ('SE','EE') and e.documento_judicial = d.id) as a left join\n" +
"(select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id) where p.estado = 'AC') as p\n" +
"on (a.persona = p.id)\n" +
"union\n" +
"select concat(ifnull(p.id,0), a.id) as id, ifnull(p.id,0) as persona_id, p.ci, p.nombre, p.cargo, p.telefono, p.email, a.exp_administrativo, a.res_exp_administrativo, a.exp_electronico, a.res_exp_electronico from (select r.id, e.persona, '' as exp_administrativo, case when d.canal_entrada_documento_judicial = 'SE' then r.nro_resolucion else '' end as res_exp_administrativo, '' as exp_electronico, case when d.canal_entrada_documento_judicial = 'EE' then r.nro_resolucion else '' end as res_exp_electronico from resuelve_por_resoluciones_por_personas as e, resoluciones as r, documentos_judiciales as d where e.resolucion = r.id and r.documento_judicial = d.id) as a left join\n" +
"(select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id)) as p\n" +
"on (a.persona = p.id)\n" +
"order by nombre;";
         */
        String comando = "select concat(ifnull(p.id,0), a.id) as id, ifnull(p.id,0) as persona_id, p.ci, p.nombre, p.cargo, p.telefono, p.email, a.exp_administrativo, a.res_exp_administrativo, a.exp_electronico, a.res_exp_electronico from (select d.id, e.persona, '' as exp_administrativo, '' as res_exp_administrativo, d.causa as exp_electronico, '' as res_exp_electronico from exp_partes_por_documentos_judiciales as e, documentos_judiciales as d where e.estado = 'AC' and e.documento_judicial = d.id) as a left join\n"
                + "(select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id) where p.estado = 'AC') as p\n"
                + "on (a.persona = p.id)\n"
                + "union\n"
                + "select concat(ifnull(p.id,0), a.id) as id, ifnull(p.id,0) as persona_id, p.ci, p.nombre, p.cargo, p.telefono, p.email, a.exp_administrativo, a.res_exp_administrativo, a.exp_electronico, a.res_exp_electronico from (select d.id, e.persona, case when d.canal_entrada_documento_judicial = 'SE' then d.causa else '' end as exp_administrativo, '' as res_exp_administrativo, case when d.canal_entrada_documento_judicial = 'EE' then d.causa else '' end as exp_electronico, '' as res_exp_electronico from personas_por_documentos_judiciales as e, documentos_judiciales as d where e.estado = 'AC' and d.canal_entrada_documento_judicial IN ('SE','EE') and e.documento_judicial = d.id) as a left join\n"
                + "(select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id) where p.estado = 'AC') as p\n"
                + "on (a.persona = p.id)\n"
                + "union\n"
                + "select concat(ifnull(p.id,0), a.id) as id, ifnull(p.id,0) as persona_id, p.ci, p.nombre, p.cargo, p.telefono, p.email, a.exp_administrativo, a.res_exp_administrativo, a.exp_electronico, a.res_exp_electronico from (select r.id, e.persona, '' as exp_administrativo, case when d.canal_entrada_documento_judicial = 'SE' then r.nro_resolucion else '' end as res_exp_administrativo, '' as exp_electronico, case when d.canal_entrada_documento_judicial = 'EE' then r.nro_resolucion else '' end as res_exp_electronico from resuelve_por_resoluciones_por_personas as e, resoluciones as r, documentos_judiciales as d where e.resolucion = r.id and r.documento_judicial = d.id) as a left join\n"
                + "(select p.id, ifnull(p.ci,'') as ci, p.nombres_apellidos as nombre, ifnull(t.descripcion,'') as cargo, ifnull(p.telefono1,'') as telefono, ifnull(p.email,'') as email from personas as p left join despachos_persona as t on (p.despacho_persona = t.id)) as p\n"
                + "on (a.persona = p.id)\n"
                + "order by nombre;";

        List<RepPersonasUsuarioAsociados> lista = ejbFacade.getEntityManager().createNativeQuery(comando, RepPersonasUsuarioAsociados.class).getResultList();

        List<RepPersonasUsuarioAsociados> resp = new ArrayList<>();
        RepPersonasUsuarioAsociados item = null;
        String idAnt = "";
        boolean primero = true;
        for (int i = 0; i < lista.size(); i++) {
            if (!idAnt.equals(lista.get(i).getPersonaId())) {

                if (!primero) {
                    resp.add(item);
                }

                item = new RepPersonasUsuarioAsociados(lista.get(i));

                idAnt = item.getPersonaId();
            } else {
                if (item != null) {
                    if (lista.get(i).getExpAdministrativo() != null) {
                        if (!"".equals(lista.get(i).getExpAdministrativo())) {
                            if (item.getExpAdministrativo() != null) {
                                if (!"".equals(item.getExpAdministrativo())) {
                                    item.setExpAdministrativo(item.getExpAdministrativo() + ", ");
                                }
                            }

                            item.setExpAdministrativo((item.getExpAdministrativo() == null) ? "" : item.getExpAdministrativo() + lista.get(i).getExpAdministrativo());
                        }
                    }

                    if (lista.get(i).getExpElectronico() != null) {
                        if (!"".equals(lista.get(i).getExpElectronico())) {
                            if (item.getExpElectronico() != null) {
                                if (!"".equals(item.getExpElectronico())) {
                                    item.setExpElectronico(item.getExpElectronico() + ", ");
                                }
                            }

                            item.setExpElectronico((item.getExpElectronico() == null) ? "" : item.getExpElectronico() + lista.get(i).getExpElectronico());
                        }
                    }

                    if (lista.get(i).getResExpAdministrativo() != null) {
                        if (!"".equals(lista.get(i).getResExpAdministrativo())) {
                            if (item.getResExpAdministrativo() != null) {
                                if (!"".equals(item.getResExpAdministrativo())) {
                                    item.setResExpAdministrativo(item.getResExpAdministrativo() + ", ");
                                }
                            }

                            item.setResExpAdministrativo((item.getResExpAdministrativo() == null) ? "" : item.getResExpAdministrativo() + lista.get(i).getResExpAdministrativo());
                        }
                    }

                    if (lista.get(i).getResExpElectronico() != null) {
                        if (!"".equals(lista.get(i).getResExpElectronico())) {
                            if (item.getResExpElectronico() != null) {
                                if (!"".equals(item.getResExpElectronico())) {
                                    item.setResExpElectronico(item.getResExpElectronico() + ", ");
                                }
                            }

                            item.setResExpElectronico((item.getResExpElectronico() == null) ? "" : item.getResExpElectronico() + lista.get(i).getResExpElectronico());
                        }
                    }
                } else {
                    item = new RepPersonasUsuarioAsociados(lista.get(i));
                }
            }

            primero = false;

        }

        if (!primero) {
            resp.add(item);
        }

        return resp;
    }

    public void repPersonasUsuario(boolean incluirTodasPersonas) {

        HttpServletResponse httpServletResponse = null;
        try {
            List<RepPersonasUsuario> lista = obtenerRepPersonasUsuario(incluirTodasPersonas);

            // List<Integer> cantFirma = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC')", Integer.class).getResultList();
            // List<Integer> cantFirmados = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC' and firmado)", Integer.class).getResultList();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM");

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("hora", format2.format(fecha));
            map.put("usuario", personaUsuario.getNombresApellidos());
            map.put("roles", roles);

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repPersonasUsuario.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

    }

    public void repPersonasUsuarioAsociados() {

        HttpServletResponse httpServletResponse = null;
        try {
            List<RepPersonasUsuarioAsociados> lista = obtenerRepPersonasUsuarioAsociados();

            // List<Integer> cantFirma = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC')", Integer.class).getResultList();
            // List<Integer> cantFirmados = ejbFacade.getEntityManager().createNativeQuery("select count(*) from (select distinct actuaciones from exp_personas_firmantes_por_actuaciones where estado = 'AC' and firmado)", Integer.class).getResultList();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM");

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(lista);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("hora", format2.format(fecha));
            map.put("usuario", personaUsuario.getNombresApellidos());
            map.put("roles", roles);

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reportePersonasUsuarioExcel.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=reporte.xlsx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            exporter.exportReport();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

    }

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
}
