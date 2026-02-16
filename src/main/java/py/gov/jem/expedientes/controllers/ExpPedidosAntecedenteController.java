package py.gov.jem.expedientes.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
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
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.Antecedentes;
import py.gov.jem.expedientes.models.DepartamentosPersona;
import py.gov.jem.expedientes.models.ExpPedidosAntecedente;
import py.gov.jem.expedientes.models.HistPersonasAntecedente;
import py.gov.jem.expedientes.models.LocalidadesPersona;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.Tutoriales;

@Named(value = "pedidosAntecedenteController")
@ViewScoped
public class ExpPedidosAntecedenteController extends AbstractController<ExpPedidosAntecedente> {

    @Inject
    private AntecedentesController antecedentesController;
    @Inject
    private PersonasController personasController;
    @Inject
    private HistPersonasAntecedenteController histPersonasAntecedenteController;

    private HttpSession session;
    private Personas personaUsuario;
    private Personas personaSelected;
    private String endpoint;
    private List<ExpPedidosAntecedente> listaPend;
    private List<ExpPedidosAntecedente> listaAut;
    private ExpPedidosAntecedente selectedPedido;
    private ExpPedidosAntecedente selectedAut;
    private String observacion;
    private Date fechaInicio;
    private ExpPedidosAntecedente pedidoRechazar;
    private ParametrosSistema par;
    private UploadedFile file;
    private List<Personas> listaPersonas;
    private Personas personaElegida;
    private List<ExpPedidosAntecedente> listaPedidos;
    private String linkTutorial;
    private String linkBiblioteca;
    private final FiltroURL filtroURL = new FiltroURL();
    private String url;
    private String nombre;
    private Personas persona;
    private List<LocalidadesPersona> listaLocalidadesPersona;
    private DepartamentosPersona departamentoPersona;
    private LocalidadesPersona localidadPersona;

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public LocalidadesPersona getLocalidadPersona() {
        return localidadPersona;
    }

    public void setLocalidadPersona(LocalidadesPersona localidadPersona) {
        this.localidadPersona = localidadPersona;
    }

    public List<LocalidadesPersona> getListaLocalidadesPersona() {
        return listaLocalidadesPersona;
    }

    public void setListaLocalidadesPersona(List<LocalidadesPersona> listaLocalidadesPersona) {
        this.listaLocalidadesPersona = listaLocalidadesPersona;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLinkTutorial() {
        return linkTutorial;
    }

    public void setLinkTutorial(String linkTutorial) {
        this.linkTutorial = linkTutorial;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public Personas getPersonaElegida() {
        return personaElegida;
    }

    public void setPersonaElegida(Personas personaElegida) {
        this.personaElegida = personaElegida;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<ExpPedidosAntecedente> getListaPend() {
        return listaPend;
    }

    public void setListaPend(List<ExpPedidosAntecedente> listaPend) {
        this.listaPend = listaPend;
    }

    public List<ExpPedidosAntecedente> getListaAut() {
        return listaAut;
    }

    public void setListaAut(List<ExpPedidosAntecedente> listaAut) {
        this.listaAut = listaAut;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public ExpPedidosAntecedente getSelectedPedido() {
        return selectedPedido;
    }

    public void setSelectedPedido(ExpPedidosAntecedente selectedPedido) {
        this.selectedPedido = selectedPedido;
    }

    public ExpPedidosAntecedente getSelectedAut() {
        return selectedAut;
    }

    public void setSelectedAut(ExpPedidosAntecedente selectedAut) {
        this.selectedAut = selectedAut;
    }

    public ExpPedidosAntecedente getPedidoRechazar() {
        return pedidoRechazar;
    }

    public void setPedidoRechazar(ExpPedidosAntecedente pedidoRechazar) {
        this.pedidoRechazar = pedidoRechazar;
    }

    public String getLinkBiblioteca() {
        return linkBiblioteca;
    }

    public void setLinkBiblioteca(String linkBiblioteca) {
        this.linkBiblioteca = linkBiblioteca;
    }
    

    public ExpPedidosAntecedenteController() {
        // Inform the Abstract parent controller of the concrete ExpPedidosAntecedente Entity
        super(ExpPedidosAntecedente.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        List<Tutoriales> tut = ejbFacade.getEntityManager().createNamedQuery("Tutoriales.findById", Tutoriales.class).setParameter("id", Constantes.TUTORIAL_ID_ANTECEDENTES).getResultList();

        if (!tut.isEmpty()) {
            linkTutorial = tut.get(0).getUrl();
        }else{
        

        }if (!tut.isEmpty()) {
            linkBiblioteca = tut.get(0).getUrlBiblioteca();
              
        }

        if (session != null) {
            personaUsuario = (Personas) session.getAttribute("Persona");
        }
        prepareCreate(null);

        observacion = "     Su pedido de usuario fue rechazado por el sgte motivo: ";

        listaPersonas = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_ANTECEDENTES).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();
        buscarPend();
        buscarAut();
        buscarPedidosAntecedente();
    }

    public Integer buscarPend() {
        listaPend = ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByEstado", ExpPedidosAntecedente.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
        return listaPend.size();
    }

    public void buscarAut() {
        listaAut = ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByNotEstado", ExpPedidosAntecedente.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
    }

    public void prepareRechazarPedido(ExpPedidosAntecedente item) {
        pedidoRechazar = item;
    }

    public void prepareAlzarArchivo(ExpPedidosAntecedente ped) {
        selectedPedido = ped;
    }

    public Integer cantidadPedidosAntecedente() {
        return buscarPend();
    }

    public boolean deshabilitarAdminAntecedentes() {
        return !filtroURL.verifPermiso("adminAntecedentes");
    }

    public void borrarAntecedente(ExpPedidosAntecedente ant) {
        if (Constantes.SI.equals(ant.getEstado())) {
            if (antecedentesController.borrarAntecedente(ant.getAntecedente())) {

                ant.setEstado(Constantes.NO);
                ant.setObservacion("Borrado");
                ant.setPersonaBorradoAntecedente(personaUsuario);
                ant.setFechaHoraBorradoAntecedente(ejbFacade.getSystemDate());
                setSelected(ant);

                super.save(null);

                setSelected(null);

            }
        } else {
            JsfUtil.addErrorMessage("El pedido fue rechazado, no hay antecedente a borrar");
        }
    }
    
    public void mensaje(){
        PrimeFaces.current().ajax().update("PedidosAntecedenteAvisoForm");

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('PedidosAntecedenteAvisoDialog').show();");
    }

    public void prepareEdit() {
        if(validarPedidoAntecedente()){
            localidadPersona = personaUsuario.getLocalidadPersona();
            departamentoPersona = personaUsuario.getDepartamentoPersona();
            persona = new Personas(personaUsuario);
            actualizarListaLocalidades();
            PrimeFaces.current().ajax().update("PedidosAntecedenteEditForm");

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('PedidosAntecedenteEditDialog').show();");
        }else{
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('PedidosAntecedenteEditDialog').hide();");
        }
    }

    public void actualizarListaLocalidades() {

        if (departamentoPersona != null) {
            listaLocalidadesPersona = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", personaUsuario.getDepartamentoPersona()).getResultList();
        } else {
            listaLocalidadesPersona = new ArrayList<>();
        }
    }

    public void rechazar(ExpPedidosAntecedente ped, boolean enviarEmail) {

        if (ped != null) {
            Date fecha = ejbFacade.getSystemDate();
            try {
                ped.setFechaHoraRespuesta(fecha);
                ped.setPersonaRespuesta(personaUsuario);
                ped.setEstado("NO");

                setSelected(ped);

                super.save(null);
                setSelected(null);
                buscarPend();
                buscarAut();
                if (enviarEmail) {
                    String texto = "<p>Hola " + ped.getPersona().getNombresApellidos() + "<br> <br>"
                            + "Su pedido de antecedentes fue rechazado por el siguiente motivo:<br>" + ped.getObservacion();
                    enviarEmailAviso(ped.getPersona().getEmail(), "RECHAZADO: Pedido de antecedente JEM", texto);
                }
            } finally {
                ped = null;
            }

        }

    }

    /*
    public void enviarEmailAntecedente(Antecedentes ant) {
        String texto = "<p>Hola " + ant.getPersona().getNombresApellidos() + "<br> <br> " + "Hemos generado su antecedente solicitado. El archivo se adjunta en este email.";
        enviarEmailAviso(ant.getPersona().getEmail(), "APROBADO: Pedido de antecedente JEM", texto, Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentes(), ant.getPathArchivo());
    }
     */
    public void enviarEmailAntecedente(Antecedentes ant) {
        String texto = "<p>Hola " + ant.getPersona().getNombresApellidos() + "<br> <br> " + "Hemos generado su antecedente solicitado. Para descargalo, favor entrar al Sistema de Antecedentes y darle click en el botón \"Último Antecedente Generado\"";
        enviarEmailAviso(ant.getPersona().getEmail(), "APROBADO: Pedido de antecedente JEM", texto);
    }

    private void buscarPedidosAntecedente() {
        List<ExpPedidosAntecedente> lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByPersona", ExpPedidosAntecedente.class).setParameter("persona", personaUsuario).getResultList();

        listaPedidos = new ArrayList<>();
        if (lista.size() > 5) {
            ExpPedidosAntecedente ant = null;
            for (int i = 0; i < 5; i++) {
                listaPedidos.add(lista.get(i));
            }
        } else {
            listaPedidos = lista;
        }
    }

    public void obtenerUltimoAntecedente() {
        List<ExpPedidosAntecedente> lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByPersona", ExpPedidosAntecedente.class).setParameter("persona", personaUsuario).getResultList();
        if (!lista.isEmpty()) {
            ExpPedidosAntecedente ped = lista.get(0);

            if ("SI".equals(ped.getEstado())) {
                if (ped.getAntecedente() != null) {
                    if (ped.getAntecedente().getPathArchivo() != null) {
                        verDoc(ped);
                    } else {
                        JsfUtil.addErrorMessage("Su pedido todavia no fue procesado-");
                    }
                } else {
                    JsfUtil.addErrorMessage("Su pedido todavia no fue procesado.");
                }
            } else if ("AC".equals(ped.getEstado())) {
                JsfUtil.addErrorMessage("Su pedido todavia no fue procesado");
            } else {
                JsfUtil.addErrorMessage("Su pedido ha sido rechazado por el sgte motivo: " + (ped.getObservacion() == null ? "" : ped.getObservacion()));
            }

        }

    }

    public void prepareCerrarDialogoVerDoc() {
        File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
        f.delete();
    }

    public void marcarParaConfirmado(ExpPedidosAntecedente ped) {

        if (ped != null) {

            if (ped.getPathArchivoPendiente() != null) {
                File f = new File(Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentesPendientes() + File.separator + ped.getPathArchivoPendiente());
                f.delete();
            }

            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un escrito");
                return;
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return;
            }

            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }

            if (bytes == null) {
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            } else if (bytes.length == 0) {
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }

            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al generar MD5");
                return;
            }

            DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

            md.update((format2.format(ejbFacade.getSystemDate())).getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

            String nombreArchivo = myHash + ".pdf";

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentesPendientes() + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            Date fecha = ejbFacade.getSystemDate();

            ped.setPathArchivoPendiente(nombreArchivo);
            ped.setPersonaRespuesta(personaUsuario);
            ped.setFechaHoraRespuesta(fecha);
            setSelected(ped);
            super.save(null);
            setSelected(null);
        }
    }

    public boolean deshabilitarConfirmarAntecedente(ExpPedidosAntecedente ped) {
        if (ped != null) {
            return ped.getPathArchivoPendiente() == null;
        }

        return true;
    }

    public boolean deshabilitarAlzarArchivo(ExpPedidosAntecedente ped) {
        return false;
    }

    public String getContent() {
        if (selectedPedido != null) {
            try {
                byte[] fileByte = Files.readAllBytes(Paths.get(Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentesPendientes() + File.separator + selectedPedido.getPathArchivoPendiente()));

                Antecedentes ant = antecedentesController.generarAntecedente(fileByte, selectedPedido.getPersona(), false, false, Constantes.RUTA_ARCHIVOS_TEMP + File.separator);

                if (ant != null) {
                    return url + "/tmp/" + ant.getPathArchivo();
                    //return url + "/tmp/" + "A90E5D0FDBEE7474CCA640C49B196CC9.pdf";
                } else {
                    return "";
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se puede encontrar archivo original");
                return "";
            }
        }
        return "";
    }

    public void procesarArchivo() {
        if (selectedPedido != null) {

            byte[] fileByte;
            try {
                fileByte = Files.readAllBytes(Paths.get(Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentesPendientes() + File.separator + selectedPedido.getPathArchivoPendiente()));

                Antecedentes ant = antecedentesController.generarAntecedente(fileByte, selectedPedido.getPersona(), false);

                if (ant != null) {
                    File f = new File(Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentesPendientes() + File.separator + selectedPedido.getPathArchivoPendiente());
                    f.delete();

                    Date fecha = ejbFacade.getSystemDate();

                    selectedPedido.setEstado("SI");
                    selectedPedido.setPersonaConfirmacion(personaUsuario);
                    selectedPedido.setFechaHoraConfirmacion(fecha);
                    selectedPedido.setAntecedente(ant);

                    setSelected(selectedPedido);
                    super.save(null);

                    setSelected(null);

                    enviarEmailAntecedente(ant);

                    buscarPend();
                    buscarAut();
                }
            } catch (IOException ex) {
                Logger.getLogger(ExpPedidosAntecedenteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void verDoc(ExpPedidosAntecedente doc) {

        try {
            byte[] fileByte = Files.readAllBytes(Paths.get(Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentes() + File.separator + doc.getAntecedente().getPathArchivo()));

            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

            if (!doc.getAntecedente().isVisto()) {
                doc.getAntecedente().setVisto(true);
                doc.getAntecedente().setFechaHoraVisto(ejbFacade.getSystemDate());

                antecedentesController.setSelected(doc.getAntecedente());
                antecedentesController.save(null);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public void enviarEmailAviso(String email, String asunto, String msg) {
        enviarEmailAviso(email, asunto, msg, null, null);
    }

    public void enviarEmailAviso(String email, String asunto, String msg, String path, String filename) {

        BodyPart texto = new MimeBodyPart();
        try {
            texto.setContent(msg, "text/html; charset=utf-8");

            Utils.sendEmailAsync(par.getIpServidorEmail(),
                    par.getPuertoServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    par.getContrasenaServidorEmail(),
                    par.getUsuarioServidorEmail(),
                    email,
                    asunto,
                    texto,
                    path,
                    filename);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return;
        }

    }

    public void actualizar(ExpPedidosAntecedente ped) {

    }

    @Override
    public ExpPedidosAntecedente prepareCreate(ActionEvent event) {
        ExpPedidosAntecedente doc = super.prepareCreate(event);
        return doc;
    }

    @Override
    public Collection<ExpPedidosAntecedente> getItems() {
        return super.getItems2();
    }

    private boolean verifCambioDatosPersona(Personas personaUsuario, Personas persona) {

        if (!(personaUsuario == null || persona == null)) {
            if (persona.getCi() != null) {
                if (!persona.getCi().equals(personaUsuario.getCi())) {
                    personaUsuario.setCi(persona.getCi());
                    return true;
                }
            }

            if (persona.getNombresApellidos() != null) {
                if (!persona.getNombresApellidos().equals(personaUsuario.getNombresApellidos())) {
                    personaUsuario.setNombresApellidos(persona.getNombresApellidos());
                    return true;
                }
            }

            if (persona.getEmail() != null) {
                if (!persona.getEmail().equals(personaUsuario.getEmail())) {
                    personaUsuario.setEmail(persona.getEmail());
                    return true;
                }
            }

            if (persona.getTelefono1() != null) {
                if (!persona.getTelefono1().equals(personaUsuario.getTelefono1())) {
                    personaUsuario.setTelefono1(persona.getTelefono1());
                    return true;
                }
            }

            if (persona.getTelefono2() != null) {
                if (!persona.getTelefono2().equals(personaUsuario.getTelefono2())) {
                    personaUsuario.setTelefono2(persona.getTelefono2());
                    return true;
                }
            }

            if (persona.getDespachoPersona() != null) {
                if (!persona.getDespachoPersona().equals(personaUsuario.getDespachoPersona())) {
                    personaUsuario.setDespachoPersona(persona.getDespachoPersona());
                    return true;
                }
            }

            if (departamentoPersona != null) {
                if (!departamentoPersona.equals(personaUsuario.getDepartamentoPersona())) {
                    personaUsuario.setDepartamentoPersona(departamentoPersona);
                    return true;
                }
            }

            if (localidadPersona != null) {
                if (!localidadPersona.equals(personaUsuario.getLocalidadPersona())) {
                    personaUsuario.setLocalidadPersona(localidadPersona);
                    return true;
                }
            }
        }

        return false;
    }
    
    public boolean validarPedidoAntecedente(){
        List<ExpPedidosAntecedente> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByPersonaAndEstado", ExpPedidosAntecedente.class).setParameter("persona", personaUsuario).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        if (lista.isEmpty()) {

            List<ExpPedidosAntecedente> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByPersonaAndEstado", ExpPedidosAntecedente.class).setParameter("persona", personaUsuario).setParameter("estado", Constantes.SI).getResultList();

            if (!lista2.isEmpty()) {
                if (lista2.get(0).getAntecedente() != null) {
                    if (!lista2.get(0).getAntecedente().isVisto()) {
                        JsfUtil.addErrorMessage("Antes de solicitar un nuevo antecedente debe hacer clic en \"Ultimo antecedente generado\" para ver el ultimo generado");
                        return false;
                    }
                }
            }

            return true;
        } else {
            JsfUtil.addErrorMessage("Ud ya tiene un pedido pendiente");
            return false;
        }
    }

    public void solicitarAntecedentes() {
        
        if(validarPedidoAntecedente()){ 
            /*
            Personas personaBkp = new Personas(persona);

            HistPersonasAntecedente hist = null;
            if (verifCambioDatosPersona(personaUsuario, persona)) {
                personasController.setSelected(personaUsuario);
                personasController.save(null);

                hist = new HistPersonasAntecedente();

                hist.setDespachoPersonaAntes(personaBkp.getDespachoPersona());
                hist.setDespachoPersonaDespues(persona.getDespachoPersona());
                hist.setDepartamentoPersonaAntes(personaBkp.getDepartamentoPersona());
                hist.setDepartamentoPersonaDespues(departamentoPersona);
                hist.setLocalidadPersonaAntes(personaBkp.getLocalidadPersona());
                hist.setLocalidadPersonaDespues(localidadPersona);
                hist.setEmailAntes(personaBkp.getEmail());
                hist.setEmailDespues(persona.getEmail());
                hist.setTelefono1Antes(personaBkp.getTelefono1());
                hist.setTelefono1Despues(persona.getTelefono1());
                hist.setTelefono2Antes(personaBkp.getTelefono2());
                hist.setTelefono2Despues(persona.getTelefono2());

                hist.setFechaHoraAlta(ejbFacade.getSystemDate());
                hist.setPersonaAlta(personaUsuario);

                histPersonasAntecedenteController.setSelected(hist);
                histPersonasAntecedenteController.saveNew(null);
            }
*/
            List<ExpPedidosAntecedente> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByPersonaAndEstado", ExpPedidosAntecedente.class).setParameter("persona", personaUsuario).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            if (lista.isEmpty()) {

                List<ExpPedidosAntecedente> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPedidosAntecedente.findByPersonaAndEstado", ExpPedidosAntecedente.class).setParameter("persona", personaUsuario).setParameter("estado", Constantes.SI).getResultList();

                if (!lista2.isEmpty()) {
                    if (lista2.get(0).getAntecedente() != null) {
                        if (!lista2.get(0).getAntecedente().isVisto()) {
                            JsfUtil.addErrorMessage("Antes de solicitar un nuevo antecedente debe hacer clic en \"Ultimo antecedente generado\" para ver el ultimo generado");
                            return;
                        }
                    }
                }

                ExpPedidosAntecedente per = new ExpPedidosAntecedente();
                per.setEstado("AC");
                per.setFechaHoraAlta(ejbFacade.getSystemDate());
                per.setFechaHoraRespuesta(null);
                per.setPersona(personaUsuario);
                per.setPersonaRespuesta(null);

                setSelected(per);
                super.saveNew(null);
/*
                if (hist != null) {
                    hist.setPedidoAntecedente(per);
                    histPersonasAntecedenteController.setSelected(hist);
                    histPersonasAntecedenteController.save(null);
                }
                */

                JsfUtil.addSuccessMessage("Su pedido de antecedentes ha sido enviado");
            } else {
                JsfUtil.addErrorMessage("Ud ya tiene un pedido pendiente");
            }
        }
        /*
        PrimeFaces.current().ajax().update("PedidosAntecedenteGraciasForm");

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('PedidosAntecedenteGraciasDialog').show();");
         */
    }
    

    public void saveNew() {
        if (getSelected() != null) {
            /*
            Date fecha = ejbFacade.getSystemDate();
            getSelected().setFechaHoraPedido(fecha);
            getSelected().setEmpresa(new Empresas(1));
            getSelected().setEstado("AC");
            getSelected().setPersona(personaSelected);

            getSelected().setContrasena(contrasena1);
            getSelected().setEmailValidado(false);
            getSelected().setRealizado(false);

            MessageDigest md = null;
            String myHash = "";
            try {
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                md = MessageDigest.getInstance("MD5");

                md.update((format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }

            getSelected().setHash(myHash);
            getSelected().setFechaHoraCaducidad(ejbFacade.getSystemDate(1));
            getSelected().setTipoPedidoPersona(Constantes.TIPO_PEDIDO_PERSONA_EXPEDIENTE);

            super.saveNew(null);

            alzarArchivo(file1, "Cédula", getSelected());
            alzarArchivo(file2, "Matrícula", getSelected());
            alzarArchivo(file3, "Foto propia", getSelected());

            enviarValidacionEmail(getSelected().getId(), personaSelected, getSelected(), getSelected().getEmail(), myHash);

            if (!this.isErrorPersistencia()) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/pedidosPersonaExpediente/GraciasPersonaExpediente.xhtml?email=" + getSelected().getEmail());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
             */
        }

    }

    public void save2() {
        super.save(null);
    }

    public void save() {
        if (getSelected() != null) {

            super.save(null);
        }
    }
}
