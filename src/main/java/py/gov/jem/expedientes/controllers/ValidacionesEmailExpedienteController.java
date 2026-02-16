package py.gov.jem.expedientes.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonasPK;
import py.gov.jem.expedientes.models.DespachosPersona;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.PedidosPersona;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.ValidacionesEmail;

@Named(value = "validacionesEmailExpedienteController")
@ViewScoped
public class ValidacionesEmailExpedienteController extends AbstractController<PedidosPersona> {

    @Inject
    private PersonasController personasController;
    @Inject
    private AntecedentesRolesPorPersonasController antecedentesRolesPorPersonasController;
    @Inject
    private DespachosPersonaController despachosPersonaController;
    private PedidosPersona val;
    private String mensaje;
    private String endpoint;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public ValidacionesEmailExpedienteController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(PedidosPersona.class);
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

        if (hash != null) {
            try {
                Date fecha = ejbFacade.getSystemDate();
                val = ejbFacade.getEntityManager().createNamedQuery("PedidosPersona.findByHashFechaHoraCaducidad", PedidosPersona.class).setParameter("hash", hash).setParameter("fechaHoraCaducidad", fecha).getSingleResult();
            } catch (Exception e) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesEmailExpediente/Caducado.xhtml");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesEmailExpediente/HashNoValido.xhtml");
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (val != null) {
            if (val.getFechaHoraValidacion() != null) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacionesEmailExpediente/Caducado.xhtml");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (val != null) {
            Date fecha = ejbFacade.getSystemDate();
            val.setFechaHoraValidacion(fecha);

            setSelected(val);

            if (val.getPersona() != null) {
                /*
                Personas per = ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", val.getPersona().getId()).getSingleResult();
                
                per.setEmailValidado(true);
                per.setUsuario(val.getUsuario());
                per.setContrasena(val.getContrasena());

                if (val.getDespachoPersona() != null) {
                    per.setDespachoPersona(val.getDespachoPersona());
                } else {
                    DespachosPersona des = despachosPersonaController.prepareCreate(null);
                    des.setEmpresa(new Empresas(1));
                    if(val.getDescripcionDespachoPersona() == null){
                        val.setDescripcionDespachoPersona("");
                    }
                    des.setDescripcion(val.getDescripcionDespachoPersona());
                    despachosPersonaController.setSelected(des);
                    despachosPersonaController.saveNew(null);
                    
                    per.setDespachoPersona(des);
                }

                if (val.getDepartamentoPersona() != null) {
                    per.setDepartamentoPersona(val.getDepartamentoPersona());
                }

                if (val.getLocalidadPersona() != null) {
                    per.setLocalidadPersona(val.getLocalidadPersona());
                }

                if (val.getEmail() != null) {
                    if (!"".equals(val.getEmail())) {
                        per.setEmail(val.getEmail());
                    }
                }

                if (val.getTelefono1() != null) {
                    if (!"".equals(val.getTelefono1())) {
                        per.setTelefono1(val.getTelefono1());
                    }
                }

                if (val.getTelefono2() != null) {
                    if (!"".equals(val.getTelefono2())) {
                        per.setTelefono2(val.getTelefono2());
                    }
                }
*/
                val.setEmailValidado(true);
                val.setRealizado(true);

                super.save(null);
/*
                per.setEmailValidado(true);
                

                personasController.setSelected(per);
                personasController.save2();

                List<AntecedentesRolesPorPersonas> rol = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByPersona", AntecedentesRolesPorPersonas.class).setParameter("persona", per.getId()).getResultList();
                if (rol.isEmpty()) {
                    AntecedentesRolesPorPersonas rolPer = antecedentesRolesPorPersonasController.prepareCreate(null);
                    AntecedentesRolesPorPersonasPK pk = new AntecedentesRolesPorPersonasPK(per.getId(), 1);
                    rolPer.setAntecedentesRolesPorPersonasPK(pk);
                    rolPer.setPersonas(per);
                    rolPer.setRoles(new AntecedentesRoles(1));
                    antecedentesRolesPorPersonasController.setSelected(rolPer);
                    antecedentesRolesPorPersonasController.saveNew(null);
                }
*/

                mensaje = "SU USUARIO HA SIDO VALIDADO CORRECTAMENTE";
            } else {
                if (val != null) {
                    val.setEmailValidado(true);

                    super.save(null);

                    mensaje = "SU EMAIL HA SIDO VALIDADO. CUANDO SU PEDIDO DE USUARIO SEA ANALIZADO RECIBIRA UN EMAIL CON LA ACTIVACION DEL MISMO";
                }

            }

        }

    }
    
    public String volver(){
        return "/" + endpoint + "/faces/login.xhtml";
    }

}
