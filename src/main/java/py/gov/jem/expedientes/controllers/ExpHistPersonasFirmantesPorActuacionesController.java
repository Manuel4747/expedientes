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
import py.gov.jem.expedientes.models.ExpHistPersonasFirmantesPorActuaciones;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.ExpNotificaciones;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import py.gov.jem.expedientes.models.ExpRolesFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "expHistPersonasFirmantesPorActuacionesController")
@ViewScoped
public class ExpHistPersonasFirmantesPorActuacionesController extends AbstractController<ExpHistPersonasFirmantesPorActuaciones> {

    public ExpHistPersonasFirmantesPorActuacionesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpHistPersonasFirmantesPorActuaciones.class);
    }


}
