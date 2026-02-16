package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import py.gov.jem.expedientes.models.Usuarios;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.Departamentos;
import py.gov.jem.expedientes.models.ParametrosSistema;

@Named(value = "usuariosController")
@ViewScoped
public class UsuariosController extends AbstractController<Usuarios> {

    public UsuariosController() {
        // Inform the Abstract parent controller of the concrete Usuarios Entity
        super(Usuarios.class);
    }

}
