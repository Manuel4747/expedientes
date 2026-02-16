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
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonasPK;
import py.gov.jem.expedientes.models.DespachosPersona;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.PedidosPersona;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.ValidacionesEmail;

@Named(value = "graciasPersonaController")
@ViewScoped
public class GraciasPersonaController extends AbstractController<PedidosPersona> {

    @Inject
    private PersonasController personasController;
    @Inject
    private AntecedentesRolesPorPersonasController antecedentesRolesPorPersonasController;
    @Inject
    private DespachosPersonaController despachosPersonaController;
    private PedidosPersona val;
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public GraciasPersonaController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(PedidosPersona.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        // Obtenemos el nro de telefono enviado por parametro
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String email = params.get("email");

        mensaje = "HEMOS ENVIADO UN CORREO ELECTRÓNICO CON UN LINK DE VALIDACION";
        if (email != null) {
            if (!"".equals(email)) {
                mensaje = "HEMOS ENVIADO UN CORREO ELECTRÓNICO A " + email + " CON UN LINK DE VALIDACION";
            }
        }

    }

}
