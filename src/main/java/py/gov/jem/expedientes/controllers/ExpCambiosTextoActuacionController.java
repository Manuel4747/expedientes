package py.gov.jem.expedientes.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.ExpCambiosTextoActuacion;
import py.gov.jem.expedientes.models.Personas;

@Named(value = "expCambiosTextoActuacionController")
@ViewScoped
public class ExpCambiosTextoActuacionController extends AbstractController<ExpCambiosTextoActuacion> {
    private HttpSession session;
    private Personas usu;

    public ExpCambiosTextoActuacionController() {
        // Inform the Abstract parent controller of the concrete CambiosEstadoActuacion Entity
        super(ExpCambiosTextoActuacion.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usu = (Personas) session.getAttribute("Persona");

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

}
