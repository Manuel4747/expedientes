package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import py.gov.jem.expedientes.models.Personas;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
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
import org.primefaces.PrimeFaces;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.RepAntecedentesDocumentosJudiciales;
import py.gov.jem.expedientes.datasource.RepAntecedentesDocumentosJudicialesRes;
import py.gov.jem.expedientes.models.Antecedentes;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DepartamentosPersona;
import py.gov.jem.expedientes.models.DespachosPersona;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpEstadosNotificacion;
import py.gov.jem.expedientes.models.ExpNotificaciones;
import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.ExpRolesFirmantesPorActuaciones;
import py.gov.jem.expedientes.models.ExpTiposActuacion;
import py.gov.jem.expedientes.models.LocalidadesPersona;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.PersonasPorDocumentosJudiciales;
import py.gov.jem.expedientes.models.Roles;
import py.gov.jem.expedientes.models.Usuarios;
import py.gov.jem.expedientes.models.VAntecedentesPermisosPersonas;

@Named(value = "personasContrasenaController")
@ViewScoped
public class PersonasContrasenaController extends AbstractController<Personas> {

    @Inject
    private UsuariosController usuariosController;

    private String contrasena;
    private String contrasena2;
    private String nombreUsu;
    private String cambioContrasena1;
    private String cambioContrasena2;
    private Personas per;
    private String endpoint;
    private String mensaje;
    private boolean visible;
    private Usuarios usu;
    private String politicas;

    public PersonasContrasenaController() {
        // Inform the Abstract parent controller of the concrete Personas Entity
        super(Personas.class);
    }

    public String getPoliticas() {
        return politicas;
    }

    public void setPoliticas(String politicas) {
        this.politicas = politicas;
    }

    public String getCambioContrasena1() {
        return cambioContrasena1;
    }

    public void setCambioContrasena1(String cambioContrasena1) {
        this.cambioContrasena1 = cambioContrasena1;
    }

    public String getCambioContrasena2() {
        return cambioContrasena2;
    }

    public void setCambioContrasena2(String cambioContrasena2) {
        this.cambioContrasena2 = cambioContrasena2;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        mensaje = "Cambio";

        politicas = Utils.politicasContrasena;

        visible = true;

        per = (Personas) session.getAttribute("Persona");
        usu = (Usuarios) session.getAttribute("Usuarios");

        setSelected(per);

        //PrimeFaces current = PrimeFaces.current();
        //current.executeScript("PF('PersonasContrasenaDialog').show();");
    }

    public void saveContrasena() {

        if (getSelected() != null) {

            if (cambioContrasena1.equals(cambioContrasena2)) {

                String resp = Utils.politicasContrasena(cambioContrasena1);

                if (!"".equals(resp)) {
                    JsfUtil.addErrorMessage(resp);
                    return;
                }

                boolean cambiar = true;
                if (usu != null) {
                    if (usu.getId().equals(Constantes.USUARIO_POR_OMISION)) {
                        cambiar = false;
                    }
                } else {
                    cambiar = false;
                }

                if (cambiar) {
                    String password = Utils.passwordToHash(cambioContrasena1);
                    usu.setContrasena(password);
                    usuariosController.setSelected(usu);
                    usuariosController.save(null);
                } else {

                    String password = Utils.passwordToHash(cambioContrasena1);
                    getSelected().setContrasena(password);
                    cambioContrasena1 = "";
                    cambioContrasena2 = "";
                    this.save(null);
                }

            } else {
                JsfUtil.addErrorMessage("Contrasenas no coinciden");
            }
        }
    }
}
