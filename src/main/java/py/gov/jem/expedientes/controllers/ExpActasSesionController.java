package py.gov.jem.expedientes.controllers;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.RootRenderer;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.persistence.TemporalType;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.datasource.EstadoCantidad;
import py.gov.jem.expedientes.datasource.RepActasSesion;
import py.gov.jem.expedientes.datasource.RepExpedientesAsociados;
import py.gov.jem.expedientes.datasource.RepPersonasFirmantes;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.AntecedentesRolesPorPersonas;
import py.gov.jem.expedientes.models.CanalesEntradaDocumentoJudicial;
import py.gov.jem.expedientes.models.DocumentosJudiciales;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.Estados;
import py.gov.jem.expedientes.models.EstadosDocumento;
import py.gov.jem.expedientes.models.ExpActuaciones;
import py.gov.jem.expedientes.models.ExpDetallesActaSesion;
import py.gov.jem.expedientes.models.ExpActasSesion;
import py.gov.jem.expedientes.models.ExpEstadosActaSesion;
import py.gov.jem.expedientes.models.ExpEstadosActaSesionPorRoles;
import py.gov.jem.expedientes.models.ExpEstadosActuacion;
import py.gov.jem.expedientes.models.ExpObjetosActuacion;
import py.gov.jem.expedientes.models.ExpPersonasAsociadas;
import py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActasSesion;
import py.gov.jem.expedientes.models.ExpTiposActuacion;
import py.gov.jem.expedientes.models.Firmas;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.Usuarios;

@Named(value = "actasSesionController")
@ViewScoped
public class ExpActasSesionController extends AbstractController<ExpActasSesion> {

    @Inject
    private PersonasController personasController;
    @Inject
    private ExpDetallesActaSesionController detalleActaSesionController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    ExpPersonasFirmantesPorActasSesionController personasFirmantesPorActasSesionController;
    @Inject
    private FirmasController firmasController;
    @Inject
    private ExpActuacionesController actuacionesController;
    @Inject
    private DocumentosJudicialesPorSecretariaController documentoJudicialPorSecretariaController;

    private Date fechaDesde;
    private Date fechaHasta;
    private Personas personaUsuario;
    private Personas personaSelected;
    private List<ExpActasSesion> listaActasSesion;
    private List<ExpDetallesActaSesion> listaDetallesActaSesion;
    private List<ExpPersonasFirmantesPorActasSesion> listaDetallesFirmantes;
    private ExpDetallesActaSesion detalleActaSesionSelected;
    private ExpPersonasFirmantesPorActasSesion detallesFirmantesSelected;
    private List<DocumentosJudiciales> listaExpedientes;
    private CanalesEntradaDocumentoJudicial canal;
    private final FiltroURL filtroURL = new FiltroURL();
    private AntecedentesRoles rolElegido;
    private String endpoint;
    private UploadedFile file;
    private ParametrosSistema par;
    private Personas personaFirmante;
    private List<Personas> listaPosiblesFirmantes;
    private List<Personas> listaPersonasFirmantes;
    private DocumentosJudiciales expediente;
    private List<DocumentosJudiciales> listaPosiblesExpedientes;
    private boolean esApp;
    private ExpActasSesion docImprimir;
    private String nombre;
    private String content;
    private HttpSession session;
    private String url;
    private String sessionId;
    private Usuarios usuario;
    private List<ExpEstadosActaSesion> listaEstadosActaSesion;
    private ExpEstadosActaSesion estadoActaSesion;
    private Integer itemsSize;
    private Integer newItemIx;
    private String titulo;
    private String tituloReporte;
    private String accion;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTituloReporte() {
        return tituloReporte;
    }

    public void setTituloReporte(String tituloReporte) {
        this.tituloReporte = tituloReporte;
    }
    

    public Integer getItemsSize() {
        return itemsSize;
    }

    public void setItemsSize(Integer itemsSize) {
        this.itemsSize = itemsSize;
    }

    public Integer getNewItemIx() {
        return newItemIx;
    }

    public void setNewItemIx(Integer newItemIx) {
        this.newItemIx = newItemIx;
    }

    public ExpEstadosActaSesion getEstadoActaSesion() {
        return estadoActaSesion;
    }

    public void setEstadoActaSesion(ExpEstadosActaSesion estadoActaSesion) {
        this.estadoActaSesion = estadoActaSesion;
    }

    public List<ExpEstadosActaSesion> getListaEstadosActaSesion() {
        return listaEstadosActaSesion;
    }

    public void setListaEstadosActaSesion(List<ExpEstadosActaSesion> listaEstadosActaSesion) {
        this.listaEstadosActaSesion = listaEstadosActaSesion;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ExpPersonasFirmantesPorActasSesion> getListaDetallesFirmantes() {
        return listaDetallesFirmantes;
    }

    public void setListaDetallesFirmantes(List<ExpPersonasFirmantesPorActasSesion> listaDetallesFirmantes) {
        this.listaDetallesFirmantes = listaDetallesFirmantes;
    }

    public ExpPersonasFirmantesPorActasSesion getDetallesFirmantesSelected() {
        return detallesFirmantesSelected;
    }

    public void setDetallesFirmantesSelected(ExpPersonasFirmantesPorActasSesion detallesFirmantesSelected) {
        this.detallesFirmantesSelected = detallesFirmantesSelected;
    }

    public DocumentosJudiciales getExpediente() {
        return expediente;
    }

    public void setExpediente(DocumentosJudiciales expediente) {
        this.expediente = expediente;
    }

    public List<DocumentosJudiciales> getListaPosiblesExpedientes() {
        return listaPosiblesExpedientes;
    }

    public void setListaPosiblesExpedientes(List<DocumentosJudiciales> listaPosiblesExpedientes) {
        this.listaPosiblesExpedientes = listaPosiblesExpedientes;
    }

    public List<Personas> getListaPersonasFirmantes() {
        return listaPersonasFirmantes;
    }

    public void setListaPersonasFirmantes(List<Personas> listaPersonasFirmantes) {
        this.listaPersonasFirmantes = listaPersonasFirmantes;
    }

    public List<Personas> getListaPosiblesFirmantes() {
        return listaPosiblesFirmantes;
    }

    public void setListaPosiblesFirmantes(List<Personas> listaPosiblesFirmantes) {
        this.listaPosiblesFirmantes = listaPosiblesFirmantes;
    }

    public Personas getPersonaFirmante() {
        return personaFirmante;
    }

    public void setPersonaFirmante(Personas personaFirmante) {
        this.personaFirmante = personaFirmante;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
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

    public PersonasController getPersonasController() {
        return personasController;
    }

    public void setPersonasController(PersonasController personasController) {
        this.personasController = personasController;
    }

    public List<ExpActasSesion> getListaActasSesion() {
        return listaActasSesion;
    }

    public void setListaActasSesion(List<ExpActasSesion> listaActasSesion) {
        this.listaActasSesion = listaActasSesion;
    }

    public List<ExpDetallesActaSesion> getListaDetallesActaSesion() {
        return listaDetallesActaSesion;
    }

    public void setListaDetallesActaSesion(List<ExpDetallesActaSesion> listaDetallesActaSesion) {
        this.listaDetallesActaSesion = listaDetallesActaSesion;
    }

    public ExpDetallesActaSesion getDetalleActaSesionSelected() {
        return detalleActaSesionSelected;
    }

    public void setDetalleActaSesionSelected(ExpDetallesActaSesion detalleActaSesionSelected) {
        this.detalleActaSesionSelected = detalleActaSesionSelected;
    }

    public List<DocumentosJudiciales> getListaExpedientes() {
        return listaExpedientes;
    }

    public void setListaExpedientes(List<DocumentosJudiciales> listaExpedientes) {
        this.listaExpedientes = listaExpedientes;
    }

    public ExpActasSesionController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(ExpActasSesion.class);
    }

    public boolean deshabilitarSesion() {
        return getSelected() == null;
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionId = session.getId();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        accion = params.get("tipo");

        Object esAppObj = session.getAttribute("esApp");
        esApp = (esAppObj == null ? false : (boolean) esAppObj);

        canal = new CanalesEntradaDocumentoJudicial();
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_EE);

        if (Constantes.ACCION_ACTA_SESION_EN_PROYECTO.equals(accion)) {
            titulo = "ACTAS DE SESIÓN EN ELABORACION";
            tituloReporte ="Actas de Sesión en Elaboración";
        } else if (Constantes.ACCION_ACTA_SESION_REVISION_MIEMBROS.equals(accion)) {
            titulo = "ACTAS DE SESIÓN P/ REVISIÓN POR EL PRESIDENTE";
            tituloReporte= "Actas de Sesión P/ Revisión Por el Presidente";
        } else if (Constantes.ACCION_ACTA_SESION_FIRMA_MIEMBROS.equals(accion)) {
            titulo = "ACTAS DE SESIÓN P/ LA FIRMA DEL PRESIDENTE";
            tituloReporte = "Actas de Sesión P/ la Firma del Presidente";
        } else if (Constantes.ACCION_ACTA_SESION_FIRMA_SECRETARIO.equals(accion)) {
            titulo = "ACTAS DE SESIÓN P/ LA FIRMA DEL SECRETARIO";
            tituloReporte = "Actas de Sesión P/ la Firma del Secretario";
        } else if (Constantes.ACCION_ACTA_SESION_FINALIZADAS.equals(accion)) {
            titulo = "ACTAS DE SESIÓN FINALIZADAS";
            tituloReporte = "Actas de Sesión Finalizadas";
        } else {
            titulo = "ACTAS DE SESIÓN";
            // Si viene del menu Registrar Acta Sesion entonces abrir dialog para crear 
            PrimeFaces.current().ajax().update("ExpActasSesionCreateForm");
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('ExpActasSesionCreateDialog').show();");
        }

        personaUsuario = (Personas) session.getAttribute("Persona");
        rolElegido = (AntecedentesRoles) session.getAttribute("RolElegido");
        usuario = (Usuarios) session.getAttribute("Usuarios");
        String act = (String) session.getAttribute("actaSesionId");
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, 2022);
        myCal.set(Calendar.MONTH, 0);
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.set(Calendar.HOUR, 0);
        myCal.set(Calendar.MINUTE, 0);
        myCal.set(Calendar.SECOND, 0);
        fechaDesde = myCal.getTime();
        fechaHasta = ejbFacade.getSystemDateOnly();
        //if (Constantes.ROL_RELATOR.equals(rolElegido.getId())) {

        List<ExpPersonasAsociadas> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();
        if (!lista.isEmpty()) {
            personaSelected = lista.get(0).getPersona();
        } else {
            personaSelected = personaUsuario;
        }
        /*}else{
            personaSelected = personaUsuario;
        }
         */

        if (act != null) {
            verActaSesion(act);
            return;
        } else {
            buscarPorFecha();
        }
    }

    private void verActaSesion(String actId) {
        setSelected(ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findById", ExpActasSesion.class).setParameter("id", Integer.valueOf(actId)).getSingleResult());

        buscarPorFecha();

        if (listaActasSesion != null) {
            itemsSize = listaActasSesion.size();
            newItemIx = listaActasSesion.indexOf(getSelected());
        }

        PrimeFaces.current().executeScript("getNewItemPosActaSesion()");
    }

    public boolean deshabilitarBotonVerEnVentana() {
        return deshabilitarBotonVerEnVentana(docImprimir);
    }

    public boolean deshabilitarBotonVerEnVentana(ExpActasSesion act) {
        return !(renderedFirma(act) || esApp);
    }

    public boolean renderedRegenerarQR() {
        return filtroURL.verifPermiso(Constantes.PERMISO_REGENERAR_QR, rolElegido.getId());
    }

    public boolean renderedFirma() {
        return renderedFirma(docImprimir);
    }

    public boolean renderedFirma(ExpActasSesion act) {

        if (act != null) {
            List<ExpPersonasFirmantesPorActasSesion> firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", act).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getResultList();
            if (firm.isEmpty()) {
                return false;
            } else {
                List<Personas> lista = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_SECRETARIO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                if (!lista.isEmpty()) {
                    //return (act.getFormato() == null) ? true : (firm.isRevisado() || lista.get(0).equals(personaUsuario));

                    return !firm.get(0).isFirmado() && (lista.get(0).equals(personaUsuario) ? true : firm.get(0).isRevisado());
                }
            }
        }
        return false;
    }

    public boolean deshabilitarRegenerarQR(ExpActasSesion act) {
        if (filtroURL.verifPermiso(Constantes.PERMISO_REGENERAR_QR, rolElegido.getId())) {
            if (act != null) {
                if (act.getEstado() != null) {
                    // if (act.getFormato() != null) {
                    if (Constantes.ESTADO_ACTUACION_FINALIZADA.equals(act.getEstado().getCodigo())) {
                        return !(act.getArchivo() != null);
                        // return act.isRegeneradoqr();
                    }
                    // }
                } else {
                    return !(act.getArchivo() != null);
                }
            }
        }
        return true;
    }

    public void versionImpresion(ExpActasSesion act) {
        HttpServletResponse httpServletResponse = null;
        String nombreArch = null;
        try {

            if (act.getArchivo() != null) {
                byte[] fileByte = null;
                try {

                    nombreArch = regenerarQR(act);

                    fileByte = Files.readAllBytes(new File(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArch).toPath());
                } catch (IOException ex) {
                    JsfUtil.addErrorMessage("No tiene documento adjunto");
                    return;
                }

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.setContentType("application/pdf");
                // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                servletOutputStream.write(fileByte);
                FacesContext.getCurrentInstance().responseComplete();
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");
        } finally {
            if (nombreArch != null) {
                if (!"".equals(nombreArch)) {
                    File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArch);
                    f.delete();

                    docImprimir = null;
                }
            }
        }
    }

    public String regenerarQR(ExpActasSesion act) {

        Date fecha = ejbFacade.getSystemDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);

        String nombreArchivo = session.getId() + "_" + simpleDateFormat.format(fecha) + ".pdf";

        /*
        try (
          InputStream in = new BufferedInputStream(new FileInputStream(par.getRutaArchivos() + File.separator + act.getArchivo()));
          OutputStream out = new BufferedOutputStream(new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArchivo))) {

            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
         */
        if (act.getHash() == null) {

            String hash = "";
            try {
                hash = Utils.generarHash(fecha, act.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            act.setHash(hash);
            ExpActasSesion actAnt = getSelected();

            setSelected(act);

            super.save(null);

            setSelected(actAnt);
        }

        agregarQR(par.getRutaActas() + File.separator + act.getArchivo(), Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArchivo, act.getHash(), false);
        /*
            act.setArchivo(nombreArchivo);
            act.setRegeneradoqr(true);
            act.setFechaHoraRegeneradoqr(fecha);
            act.setPersonaRegeneradoqr(personaSelected);

            expActuacionesController.setSelected(act);
            expActuacionesController.save(null);
         */
 /*
        }catch(Exception e){
            e.printStackTrace();
        }
         */

        return nombreArchivo;
    }

    private void agregarQR(String nombreOri, String nombre, String hash, boolean primeraPagina) {
        // nombre + "_temp"
        PdfWriter writer;
        try {
            /*
            writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            BarcodeQRCode qrCode = new BarcodeQRCode(hash);
            PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDoc);
            Image barcodeImage = new Image(barcodeObject).setWidth(100f).setHeight(100f);
            //document.add(new Paragraph().add(barcodeImage));
            document.add(barcodeImage);
            
            document.close();
             */

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(nombreOri), new PdfWriter(nombre));

            Document document = new Document(pdfDoc);

            // String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_ACTUACION;
            String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/expedientes/" + Constantes.URL_VALIDACION_ACTA_SESION;
            BarcodeQRCode qrCode = new BarcodeQRCode(path + "?hash=" + hash);
            // PdfFormXObject barcodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDoc);
            // Image barcodeImage = new Image(barcodeObject).setWidth(50f).setHeight(50f);

            java.awt.Image imagen = qrCode.createAwtImage(Color.black, new Color(0, 0, 0, 0));

            if (primeraPagina) {
                // qrCode.createAwtImage(Color.darkGray, Color.darkGray);
                PageSize ps = new PageSize(pdfDoc.getFirstPage().getPageSize());
                pdfDoc.addNewPage(1, ps);

                PdfPage page = pdfDoc.getPage(1);

                float fontSize = 50.0f;
                float allowedWidth = 500;

                Paragraph paragraph = new Paragraph("Es copia del instrumento que tuve a la vista").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD)).setFontSize(fontSize);

                Canvas canvas = new Canvas(new PdfCanvas(page), pdfDoc, page.getMediaBox());
                RootRenderer canvasRenderer = canvas.getRenderer();
                // paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2))));
                while (paragraph.createRendererSubTree().setParent(canvasRenderer).layout(new LayoutContext(new LayoutArea(1, new Rectangle(allowedWidth, fontSize * 2)))).getStatus() != LayoutResult.FULL) {
                    paragraph.setFontSize(--fontSize);
                }

                float xCoord = page.getPageSize().getRight() / 2;
                float yCoord = 120;

                paragraph.setWidth(allowedWidth);
                canvas.showTextAligned(paragraph, xCoord, yCoord, TextAlignment.CENTER);
                canvas.close();
            }

            //PdfCanvas under = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(), new PdfResources(), pdfDoc);
            for (int i = 0; i < pdfDoc.getNumberOfPages(); i++) {

                PdfPage pagina = pdfDoc.getPage(i + 1);

                PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);

                // PdfXObject xObject = PdfXObject();
                Rectangle rect = new Rectangle(pagina.getPageSize().getRight() - 100, 1, 80, 80);
                under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);
                //under.addImage(ImageDataFactory.create(imagen, Color.white), 50, 0f, 0f, 50, 0, 0, false);

                Paragraph paragraph2 = new Paragraph("Para conocer la validez del documento verifique aqui").setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE)).setFontSize(8);
                Canvas canvas2 = new Canvas(new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc), pdfDoc, pagina.getMediaBox());

                RootRenderer canvasRenderer2 = canvas2.getRenderer();
                paragraph2.createRendererSubTree().setParent(canvasRenderer2).layout(new LayoutContext(new LayoutArea(0, new Rectangle(pagina.getPageSize().getRight() - 100, 6 * 2))));
                paragraph2.setWidth(60);
                canvas2.showTextAligned(paragraph2, pagina.getPageSize().getRight() - 100, 10, TextAlignment.RIGHT);
                canvas2.close();

                under.saveState();
            }

            pdfDoc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesPorSecretariaController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean deshabilitarFirma() {

        if (docImprimir != null) {
            ExpPersonasFirmantesPorActasSesion firm = null;
            try {
                firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", docImprimir).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getSingleResult();
                return firm.isFirmado();
            } catch (Exception e) {
            }
        }
        return true;
    }

    public void prepareDialogoVerDoc(ExpActasSesion doc) {
        docImprimir = doc;
        PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    public boolean deshabilitarAdminActaSesion() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_ACTAS_SESION, rolElegido.getId())) {
            return false;
        }
        return true;
    }

    public boolean renderedDatatable() {
        return accion != null;
    }

    public boolean deshabilitarRevisarActaSesion(ExpActasSesion item) {
        if (item != null) {
            List<ExpPersonasFirmantesPorActasSesion> per = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", item).setParameter("personaFirmante", personaSelected).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();
            if (!per.isEmpty()) {
                return item.isRevisado();
            }
        }
        return true;
    }

    public boolean deshabilitarEditActaSesion() {
        if (getSelected() != null) {
            if (getSelected().getEstado() != null) {
                return Constantes.ESTADO_ACTA_SESION_FINALIZADA.equals(getSelected().getEstado().getCodigo());
            }
        }

        return true;
    }

    public boolean renderedRevisarActaSesion() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_REVISAR_ACTAS_SESION, rolElegido.getId())) {
            return true;
        }
        return false;
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getArchivo() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaActas() + "/" + docImprimir.getArchivo()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }
                }

                if (fileByte != null) {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA_HORA);

                    String partes[] = docImprimir.getArchivo().split("[.]");
                    String ext = "pdf";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
                    outputStream.write(fileByte);

                    outputStream.close();

                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        return url + "/tmp/" + nombre;
    }

    private void actualizarEstadoFirma(Firmas firma, AntecedentesRoles rol, Personas per) {
        try {
            ExpPersonasFirmantesPorActasSesion act = null;
            boolean esPersona = false;
            try {
                act = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", firma.getActaSesion()).setParameter("personaFirmante", firma.getPersona()).setParameter("estado", new Estados("AC")).getSingleResult();
                esPersona = true;
            } catch (Exception e) {
            }

            if (act != null) {
                act.setFechaHoraFirma(ejbFacade.getSystemDate());
                act.setFirmado(true);
                act.setPersonaFirmante(personaUsuario);

                personasFirmantesPorActasSesionController.setSelected(act);
                personasFirmantesPorActasSesionController.save(null);
            }
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            String texto = "<p>Hola " + act.getPersonaAlta().getNombresApellidos() + "<br> "
                    + "     Ud ha sido firmada el acta se la sesión " + docImprimir.getTipoSesion().getDescripcion() + " de fecha " + format.format(docImprimir.getFechaSesion())
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver el acta, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?acta=" + docImprimir.getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(act.getPersonaAlta().getEmail(), "Se ha firmada un acta en el Sistema de Expediente Electrónico JEM", texto);

            String texto2 = "<p>Hola " + per.getNombresApellidos() + "<br> "
                    + "     El acta de la sesión " + docImprimir.getTipoSesion().getDescripcion() + " de fecha " + format.format(docImprimir.getFechaSesion())
                    + " ha sido firmada por " + act.getPersonaFirmante().getNombresApellidos()
                    + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                    + "Para ver la presentacion, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?act=" + docImprimir.getId() + "'>CLICK AQUÍ</a>";

            enviarEmailAviso(per.getEmail(), "Se ha firmada una presentacion en el Sistema de Expediente Electrónico JEM", texto);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void generarActuacionesActa(ExpActasSesion doc, Date fecha) {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        List<ExpEstadosActuacion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActuacion.findByCodigo", ExpEstadosActuacion.class).setParameter("codigo", Constantes.ESTADO_ACTUACION_FINALIZADA).getResultList();
        if (est.isEmpty()) {
            return;
        }
        List<ExpObjetosActuacion> obj = ejbFacade.getEntityManager().createNamedQuery("ExpObjetosActuacion.findById", ExpObjetosActuacion.class).setParameter("id", Constantes.OBJETO_ACTUACION_ACTA_SESION).getResultList();
        if (obj.isEmpty()) {
            return;
        }
        List<ExpTiposActuacion> tipo = ejbFacade.getEntityManager().createNamedQuery("ExpTiposActuacion.findById", ExpTiposActuacion.class).setParameter("id", Constantes.TIPO_ACTUACION_ACTA_SESION).getResultList();
        if (tipo.isEmpty()) {
            return;
        }
        List<ExpDetallesActaSesion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpDetallesActaSesion.findByActaSesion", ExpDetallesActaSesion.class).setParameter("actaSesion", doc).getResultList();
        for (ExpDetallesActaSesion det : lista) {
            ExpActuaciones act = new ExpActuaciones();

            act.setActuacionPadre(null);
            act.setArchivo(doc.getArchivo());
            act.setAutenticado(false);
            act.setDescripcion("Acta de sesión " + doc.getTipoSesion().getDescripcion() + " de fecha " + format.format(doc.getFechaSesion()));
            act.setDestinatario(null);
            act.setDocumentoJudicial(det.getDocumentoJudicial());
            act.setEmpresa(new Empresas(1));
            act.setEstado(est.get(0));
            act.setFechaFinal(null);
            act.setFechaHoraAlta(doc.getFechaHoraAlta());
            act.setFechaHoraAutenticado(null);
            act.setFechaHoraUltimoEstado(fecha);
            act.setFechaHoraVisible(fecha);
            act.setFechaPresentacion(documentoJudicialPorSecretariaController.generarFechaPresentacion(fecha));
            act.setFojas(1);
            act.setFormato(null);
            act.setActaSesion(doc);
            String hash = "";
            try {
                hash = Utils.generarHash(fecha, personaUsuario.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
                hash = null;
            }
            act.setHash(hash);

            act.setLeido(false);
            act.setNotificar(false);
            act.setNroFinal("0");
            act.setObjetoActuacion(obj.get(0));
            act.setTipoActuacion(tipo.get(0));
            act.setPersonaAlta(personaUsuario);
            act.setPersonaAutenticado(null);
            act.setPersonaUltimoEstado(personaUsuario);
            act.setPersonaVisible(personaUsuario);
            act.setPreopinante(null);
            act.setResolucion(null);
            act.setTexto(null);
            act.setTextoFinal(null);
            act.setVisible(true);

            actuacionesController.setSelected(act);
            actuacionesController.saveNew(null);
        }

    }

    private boolean verifActuacionAnterior(ExpActasSesion act) {
        List<ExpPersonasFirmantesPorActasSesion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByPersonaFirmanteFirmado", ExpPersonasFirmantesPorActasSesion.class).setParameter("personaFirmante", personaUsuario).setParameter("firmado", false).getResultList();
        if (!lista.isEmpty()) {
            return lista.get(0).getActaSesion().equals(act);
        }
        return false;
    }

    public boolean firmar() {

        Date fecha = ejbFacade.getSystemDate();
        Firmas firma = null;

        boolean resp = false;

        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        List<AntecedentesRolesPorPersonas> perSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();

        if (perSecretario.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro una persona que tenga rol de secretario");
            return resp;
        }

        List<ExpActasSesion> listaAct = ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findById", ExpActasSesion.class).setParameter("id", docImprimir.getId()).getResultList();
        if (listaAct.isEmpty()) {
            JsfUtil.addErrorMessage("No se encontro acta a firmar");
            return resp;
        }

        docImprimir = listaAct.get(0);
        List<ExpPersonasFirmantesPorActasSesion> listaPerAnt = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", docImprimir).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getResultList();
        if (listaPerAnt.isEmpty()) {
            JsfUtil.addErrorMessage("Ud ya no es firmante de esta acta");
            return resp;
        }

        if (listaPerAnt.get(0).isFirmado()) {
            JsfUtil.addErrorMessage("Ud ya firmó esta acta");
            return resp;
        }

        if (!perSecretario.get(0).getPersonas().equals(personaUsuario)) {
            if (!listaPerAnt.get(0).isRevisado()) {
                JsfUtil.addErrorMessage("El acta debe ser revisada antes de poder ser firmada");
                return resp;
            }
        }
        /*
        if (!verifActuacionAnterior(docImprimir)) {
            JsfUtil.addErrorMessage("Ud tiene actas anteriores que firmar");
            return resp;
        }
         */
        if (firma == null) {
            firma = new Firmas();

            firma.setActaSesion(docImprimir);
            firma.setEmpresa(usuario.getEmpresa());
            firma.setFechaHora(fecha);
            firma.setEstado("PE");
            firma.setSesion(sessionId);
            firma.setPersona(personaUsuario);

            firmasController.setSelected(firma);
            firmasController.saveNew(null);

        }

        int cont = 60;
        Firmas firma2 = null;

        while (cont >= 0) {

            try {
                ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
                firma2 = ejbFacade.getEntityManager().createNamedQuery("Firmas.findById", Firmas.class).setParameter("id", firma.getId()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                cont = 0;
                break;
            }

            // System.out.println("Esperando firma " + firma.getId() + ", estado: " + firma2.getEstado() + ", contador:" + cont);
            if (!firma2.getEstado().equals("PE")) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }

            cont--;
        }

        if (cont > 0) {

            if (firma2 != null) {
                if (firma2.getEstado().equals("AC")) {

                    ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

                    docImprimir = ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findById", ExpActasSesion.class).setParameter("id", docImprimir.getId()).getSingleResult();

                    actualizarEstadoFirma(firma2, rolElegido, perSecretario.get(0).getPersonas());

                    resp = true;

                    boolean encontro = false;
                    List<ExpPersonasFirmantesPorActasSesion> listaPer = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", docImprimir).setParameter("estado", new Estados("AC")).getResultList();

                    int i = 0;
                    ExpPersonasFirmantesPorActasSesion perso = null;
                    for (; i < listaPer.size();) {
                        perso = listaPer.get(i);
                        List<Personas> lista2 = ejbFacade.getEntityManager().createNamedQuery("Personas.findByRolEstado", Personas.class).setParameter("rol", Constantes.ROL_MIEMBRO).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
                        for (Personas p : lista2) {
                            if (p.equals(perso.getPersonaFirmante())) {
                                if (!perso.isFirmado()) {
                                    encontro = true;
                                    break;
                                }
                            }
                        }

                        if (encontro) {
                            break;
                        }

                        i++;
                    }

                    // List<AntecedentesRolesPorPersonas> per = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();
                    if (perSecretario.isEmpty()) {
                        JsfUtil.addErrorMessage("No se pude encontro persona con rol de secretario");
                        return false;
                    } else {
                        if (personaUsuario.equals(perSecretario.get(0).getPersonas()) && i > 0) {
                            List<ExpEstadosActaSesion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findByCodigo", ExpEstadosActaSesion.class).setParameter("codigo", Constantes.ESTADO_ACTA_SESION_FINALIZADA).getResultList();

                            if (!est.isEmpty()) {
                                docImprimir.setEstado(est.get(0));
                                docImprimir.setVisible(true);
                                docImprimir.setPersonaVisible(personaUsuario);
                                fecha = ejbFacade.getSystemDate();
                                docImprimir.setFechaHoraVisible(fecha);

                                setSelected(docImprimir);
                                super.save(null);

                                generarActuacionesActa(docImprimir, fecha);

                            }

                        } else if (!encontro && i > 0) {

                            List<ExpEstadosActaSesion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findByCodigo", ExpEstadosActaSesion.class).setParameter("codigo", Constantes.ESTADO_ACTA_SESION_FIRMA_SECRETARIO).getResultList();

                            if (!est.isEmpty()) {

                                docImprimir.setEstado(est.get(0));

                                super.setSelected(docImprimir);
                                super.save(null);

                            }

                            prepareEditFirmantes(docImprimir);

                            if (listaPersonasFirmantes == null) {
                                listaPersonasFirmantes = new ArrayList<>();
                            }

                            listaPersonasFirmantes.add(perSecretario.get(0).getPersonas());
                            // selectedActuacion = docImprimir;
                            saveFirmantes();
                        }
                    }
                }
            }
        } else {
            if (firma2 != null) {
                firma2.setEstado("TO");
                firmasController.setSelected(firma2);
                firmasController.save(null);
            }

            JsfUtil.addErrorMessage("Tiempo de espera terminado");
        }
        setSelected(null);
        buscarPorFecha();
        buscarDetalles();
        return resp;

    }

    public void agregarPersonaFirmante() {

        if (personaFirmante != null) {

            if (listaPersonasFirmantes == null) {
                listaPersonasFirmantes = new ArrayList<>();
            }
            /*
            if(listaPersonasFirmantes.size() > 0){
                JsfUtil.addErrorMessage("No se puede agregar mas de 1 (un) firmante");
                return;
            }
             */

            boolean encontro = false;
            Personas perActual = null;
            for (Personas per : listaPersonasFirmantes) {
                if (per.equals(personaFirmante)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaPersonasFirmantes.add(personaFirmante);
            }
        }
    }

    public void borrarPersonaFirmante(Personas personaActual) {

        List<AntecedentesRolesPorPersonas> perSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();

        if (perSecretario.isEmpty()) {
            JsfUtil.addErrorMessage("No se pudo determinar al secretario");
            return;
        }

        if (personaActual.equals(perSecretario.get(0).getPersonas())) {
            JsfUtil.addErrorMessage("No se puede borrar al secretario");
            return;
        }

        if (listaPersonasFirmantes != null) {

            if (personaActual != null) {
                if (personaActual.isFirmado() || personaActual.isRevisado()) {
                    JsfUtil.addErrorMessage("La actuación ya fue firmada o ya fue revisada por la persona, ya no puede ser borrada");
                    return;
                }

                listaPersonasFirmantes.remove(personaActual);
            }

        }
    }

    public void agregarExpediente() {

        if (expediente != null) {

            if (listaExpedientes == null) {
                listaExpedientes = new ArrayList<>();
            }

            boolean encontro = false;
            DocumentosJudiciales perActual = null;
            for (DocumentosJudiciales per : listaExpedientes) {
                if (per.equals(expediente)) {
                    perActual = per;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaExpedientes.add(expediente);
            }
        }
    }

    public void borrarExpediente(DocumentosJudiciales docActual) {

        if (listaExpedientes != null) {
            listaExpedientes.remove(docActual);
        }
    }

    private List<ExpActasSesion> obtenerActasSesion(Date fecha, Personas persona) {

        List<ExpActasSesion> lista = null;

        // lista = this.ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findByFechaSesion", ExpActasSesion.class).setParameter("fechaDesde", fechaDesde).getResultList();
        if (Constantes.ACCION_ACTA_SESION_EN_PROYECTO.equals(accion)) {
            lista = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_EN_PROYECTO, persona);
        } else if (Constantes.ACCION_ACTA_SESION_REVISION_MIEMBROS.equals(accion)) {
            lista = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_REVISION_MIEMBROS, persona);
        } else if (Constantes.ACCION_ACTA_SESION_FIRMA_MIEMBROS.equals(accion)) {
            lista = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_FIRMA_MIEMBROS, persona);
        } else if (Constantes.ACCION_ACTA_SESION_FIRMA_SECRETARIO.equals(accion)) {
            lista = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_FIRMA_SECRETARIO, persona);
        } else if (Constantes.ACCION_ACTA_SESION_FINALIZADAS.equals(accion)) {
            lista = obtenerCantidadActasSesionEstado(Constantes.ESTADO_ACTA_SESION_FINALIZADA, persona);
        } else {
            lista = obtenerCantidadActasSesionEstado(null, persona);
        }

        return lista;

    }

    public String rowClass(ExpActasSesion act) {
        if (Constantes.ACCION_ACTA_SESION_FIRMA_MIEMBROS.equals(accion)) {
            List<ExpPersonasFirmantesPorActasSesion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", act).setParameter("personaFirmante", personaUsuario).setParameter("estado", new Estados("AC")).getResultList();
            if (lista.isEmpty()) {
                List<ExpPersonasAsociadas> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasAsociadas.findByPersonaAsociada", ExpPersonasAsociadas.class).setParameter("personaAsociada", personaUsuario.getId()).getResultList();
                if (lista2.isEmpty()) {
                    return "white";
                } else {
                    List<ExpPersonasFirmantesPorActasSesion> lista3 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", act).setParameter("personaFirmante", lista2.get(0).getPersona()).setParameter("estado", new Estados("AC")).getResultList();
                    if (lista3.isEmpty()) {
                        return "white";
                    } else {
                        return lista3.get(0).isFirmado() ? "white" : (lista3.get(0).isRevisado() ? "green" : "red");
                    }
                }
            } else {
                return lista.get(0).isFirmado() ? "white" : (lista.get(0).isRevisado() ? "green" : "red");
            }
        } else {
            return "white";
        }

    }

    public void buscarPorFecha() {
        listaActasSesion = obtenerActasSesion(fechaDesde, personaSelected);
        listaDetallesActaSesion = null;
        detalleActaSesionSelected = null;

    }

    public void actualizarRevisarActaSesion(ExpActasSesion item) {
        List<ExpPersonasFirmantesPorActasSesion> per = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", item).setParameter("personaFirmante", personaSelected).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();
        if (!per.isEmpty()) {
            if (item.isRevisado()) {
                Date fecha = ejbFacade.getSystemDate();
                per.get(0).setRevisado(true);
                per.get(0).setPersonaRevisado(personaUsuario);
                per.get(0).setFechaHoraRevisado(fecha);
                personasFirmantesPorActasSesionController.setSelected(per.get(0));
                personasFirmantesPorActasSesionController.save(null);
                item.setPersonaRevisado(personaUsuario);
                item.setFechaHoraRevisado(fecha);
            }
        }
    }

    public List<ExpActasSesion> obtenerCantidadActasSesionEstado(String estado, Personas persona) {
        if (persona != null) {
            String complemento = "";

            if (estado != null) {
                if (Constantes.ESTADO_ACTA_SESION_REVISION_MIEMBROS.equals(estado)) {
                    complemento = " and a.estado = '" + Constantes.ESTADO_ACTA_SESION_FIRMA_MIEMBROS + "' ";
                } else {
                    complemento = " and a.estado = '" + estado + "' ";
                }
            }

            if (filtroURL.verifPermiso("adminActasSesion")) {
                if (Constantes.ESTADO_ACTA_SESION_FIRMA_MIEMBROS.equals(estado)) {
                    complemento += " and (select count(*) > 0 from exp_personas_firmantes_por_actas_sesion as p where p.acta_sesion = a.id and not p.firmado and p.revisado) ";
                } else if (Constantes.ESTADO_ACTA_SESION_REVISION_MIEMBROS.equals(estado)) {
                    complemento += " and (select count(*) > 0 from exp_personas_firmantes_por_actas_sesion as p where p.acta_sesion = a.id and not p.revisado) ";
                }
            } else {
                if (Constantes.ESTADO_ACTA_SESION_FIRMA_MIEMBROS.equals(estado)) {
                    complemento += " and (select count(*) > 0 from exp_personas_firmantes_por_actas_sesion as p where p.acta_sesion = a.id and p.persona_firmante = " + persona.getId() + " and not p.firmado and p.revisado) ";
                } else if (Constantes.ESTADO_ACTA_SESION_REVISION_MIEMBROS.equals(estado)) {
                    complemento += " and (select count(*) > 0 from exp_personas_firmantes_por_actas_sesion as p where p.acta_sesion = a.id and p.persona_firmante = " + persona.getId() + " and not p.revisado) ";
                }
            }

            // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String comando = "select a.*, f.revisado, f.persona_revisado, f.fecha_hora_revisado from exp_actas_sesion as a left join exp_personas_firmantes_por_actas_sesion as f on (a.id = f.acta_sesion and f.persona_firmante = ?3 and f.estado = 'AC') where a.fecha_sesion >= ?1 and a.fecha_sesion <= ?2 " + complemento + " order by a.fecha_sesion, a.fecha_hora_alta";
            List<ExpActasSesion> lista = ejbFacade.getEntityManager().createNativeQuery(comando, ExpActasSesion.class).setParameter(1, fechaDesde).setParameter(2, fechaHasta).setParameter(3, persona.getId()).getResultList();

            for (ExpActasSesion act : lista) {
                List<ExpPersonasFirmantesPorActasSesion> firm = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", act).setParameter("personaFirmante", personaSelected).setParameter("estado", new Estados("AC")).getResultList();
                if (!firm.isEmpty()) {
                    act.setRevisado(firm.get(0).isRevisado());
                    act.setPersonaRevisado(firm.get(0).getPersonaRevisado());
                    act.setFechaHoraRevisado(firm.get(0).getFechaHoraRevisado());
                }
            }

            return lista;
        } else {
            return new ArrayList<>();
        }
    }

    public List<EstadoCantidad> obtenerCantidadActasSesionEstadoTodos(List<String> listaEstados) {
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

        String comando = "select a.estado, count(*) as cantidad from exp_actas_sesion as a where a.fecha_sesion >= ?1 and a.fecha_sesion <= ?2 " + mas + " group by a.estado";
        List<EstadoCantidad> lista = ejbFacade.getEntityManager().createNativeQuery(comando, EstadoCantidad.class).setParameter(1, fechaDesde).setParameter(2, fechaHasta).getResultList();

        return lista;
    }

    /*
    public void buscarPorFecha() {
        if (fechaDesde == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaHasta);
            cal.add(Calendar.DATE, 1);
            Date nuevaFechaHasta = cal.getTime();
             
            if (deshabilitarAdminActaSesion()) {
                listaActasSesion = obtenerOrdenesDelDia(fechaDesde);
            } else {
                listaActasSesion = this.ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findByFechaSesion", ExpActasSesion.class).setParameter("fechaDesde", fechaDesde).getResultList();
            }

            setSelected(null);
            listaDetallesActaSesion = null;
            detalleActaSesionSelected = null;
        }
    }
     */
    public void buscarDetalles() {
        if (getSelected() != null) {
            listaDetallesActaSesion = this.ejbFacade.getEntityManager().createNamedQuery("ExpDetallesActaSesion.findByActaSesion", ExpDetallesActaSesion.class).setParameter("actaSesion", getSelected()).getResultList();
            listaDetallesFirmantes = this.ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesion", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", getSelected()).getResultList();
        } else {
            listaDetallesActaSesion = null;
            listaDetallesFirmantes = null;
        }
    }

    public void verDoc(ExpActasSesion doc) {

        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
            httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

            byte[] fileByte = null;

            if (doc.getArchivo() != null) {
                try {
                    fileByte = Files.readAllBytes(new File(par.getRutaActas() + "/" + doc.getArchivo()).toPath());
                } catch (IOException ex) {
                    JsfUtil.addErrorMessage("No tiene documento adjunto");
                }
            }

            servletOutputStream.write(fileByte);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
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

    @Override
    public Collection<ExpActasSesion> getItems() {
        return super.getItems2();
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

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public ExpActasSesion prepareCreate() {
        ExpActasSesion doc = super.prepareCreate(null);
        doc.setFechaSesion(ejbFacade.getSystemDate());
        List<ExpEstadosActaSesion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findByCodigo", ExpEstadosActaSesion.class).setParameter("codigo", Constantes.ESTADO_ACTA_SESION_EN_PROYECTO).getResultList();
        if (lista.isEmpty()) {
            JsfUtil.addErrorMessage("No se puede crear nueva acta de sesión");
            return null;
        }
        estadoActaSesion = lista.get(0);

        listaEstadosActaSesion = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findByRol", ExpEstadosActaSesion.class).setParameter("rol", rolElegido).setParameter("iteracion", 1).getResultList();

        prepareEditFirmantes(getSelected());
        prepareEditExpedientes();
        return doc;
    }

    public boolean deshabilitarEstadoActaSesion() {
        if (getSelected() != null) {
            if (getSelected().getEstado() != null) {
                List<ExpEstadosActaSesionPorRoles> lista = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesionPorRoles.findByRolEstadoActaSesionIteracion", ExpEstadosActaSesionPorRoles.class).setParameter("rol", rolElegido).setParameter("estadoActaSesion", getSelected().getEstado()).setParameter("iteracion", 1).getResultList();
                if (!lista.isEmpty()) {
                    return lista.get(0).isDeshabilitar();
                }
            }
        }

        return true;
    }

    public void prepareEdit() {
        if (getSelected() != null) {
            estadoActaSesion = getSelected().getEstado();

            listaEstadosActaSesion = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findByRol", ExpEstadosActaSesion.class).setParameter("rol", rolElegido).setParameter("iteracion", 1).getResultList();

            if (getSelected().getEstado() != null) {
                boolean encontro = false;
                for (ExpEstadosActaSesion est : listaEstadosActaSesion) {
                    if (est.equals(getSelected().getEstado())) {
                        encontro = true;
                        break;
                    }
                }

                if (!encontro) {
                    listaEstadosActaSesion.add(getSelected().getEstado());
                }
            }

            prepareEditFirmantes(getSelected());
            prepareEditExpedientes();
        }
    }

    public ExpDetallesActaSesion prepareCreateDetalle() {
        detalleActaSesionSelected = detalleActaSesionController.prepareCreate(null);
        listaPosiblesExpedientes = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
        return detalleActaSesionSelected;
    }

    public void prepareEditDetalle() {
    }

    public void prepareEditExpedientes() {

        listaPosiblesExpedientes = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", DocumentosJudiciales.class).setParameter("canalEntradaDocumentoJudicial", canal).getResultList();

        if (getSelected() != null) {
            List<ExpDetallesActaSesion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpDetallesActaSesion.findByActaSesion", ExpDetallesActaSesion.class).setParameter("actaSesion", getSelected()).getResultList();

            listaExpedientes = new ArrayList<>();
            expediente = null;

            for (ExpDetallesActaSesion per : lista) {
                per.getDocumentoJudicial().setEstado(new EstadosDocumento("AC"));
                listaExpedientes.add(per.getDocumentoJudicial());
            }
        }

    }

    public void navigateActuacion(ExpDetallesActaSesion item) {
        List<ExpActuaciones> act = ejbFacade.getEntityManager().createNamedQuery("ExpActuaciones.findByDocumentoJudicial", ExpActuaciones.class).setParameter("documentoJudicial", item.getDocumentoJudicial()).getResultList();
        if (!act.isEmpty()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("actuacionId", String.valueOf(act.get(0).getId()));
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/expEntradaDocumentosJudiciales/index.xhtml?tipo=consulta");
            } catch (IOException ex) {
            }

        }
    }

    public void enviarEmailAviso(String email, String asunto, String msg) {
        /*
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
                    texto);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return;
        }
         */
    }

    public void actualizarActaSesion(ExpActasSesion item) {
        /*
        setSelected(item);
        super.save(null);
         */
    }

    public void borrarActaSesion(ExpActasSesion item) {
        List<ExpDetallesActaSesion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpDetallesActaSesion.findByActaSesion", ExpDetallesActaSesion.class).setParameter("actaSesion", item).getResultList();
        for (ExpDetallesActaSesion uno : lista) {
            detalleActaSesionController.setSelected(uno);
            detalleActaSesionController.delete(null);
        }

        List<ExpPersonasFirmantesPorActasSesion> lista2 = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesion", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", item).getResultList();
        for (ExpPersonasFirmantesPorActasSesion uno : lista2) {
            personasFirmantesPorActasSesionController.setSelected(uno);
            personasFirmantesPorActasSesionController.delete(null);
        }

        setSelected(item);
        super.delete(null);
        buscarPorFecha();
    }

    public void borrarDetalle(ExpDetallesActaSesion item) {
        detalleActaSesionController.setSelected(item);
        detalleActaSesionController.delete(null);
        buscarDetalles();
    }

    public void prepareEditFirmantes(ExpActasSesion doc) {

        listaPosiblesFirmantes = ejbFacade.getEntityManager().createNamedQuery("Personas.findByPermisoEstado", Personas.class).setParameter("funcion", Constantes.PERMISO_FIRMAR).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        if (getSelected() != null) {
            List<ExpPersonasFirmantesPorActasSesion> lista = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesionEstado", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", doc).setParameter("estado", new Estados("AC")).getResultList();

            listaPersonasFirmantes = new ArrayList<>();
            personaFirmante = null;

            List<AntecedentesRolesPorPersonas> perSecretario = ejbFacade.getEntityManager().createNamedQuery("AntecedentesRolesPorPersonas.findByRol", AntecedentesRolesPorPersonas.class).setParameter("rol", Constantes.ROL_SECRETARIO).getResultList();

            for (ExpPersonasFirmantesPorActasSesion per : lista) {
                if (!per.getPersonaFirmante().equals(perSecretario.get(0).getPersonas())) {
                    per.getPersonaFirmante().setEstado("AC");
                    per.getPersonaFirmante().setFirmado(per.isFirmado());
                    listaPersonasFirmantes.add(per.getPersonaFirmante());
                }
            }
        }

    }

    public void saveFirmantes() {
        List<ExpPersonasFirmantesPorActasSesion> listaFirmantesActual = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesion", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", getSelected()).getResultList();
        savePersonasFirmantes(getSelected(), listaPersonasFirmantes, listaFirmantesActual);
        // saveRolesFirmantes();
    }

    public void savePersonasFirmantes(ExpActasSesion act, List<Personas> listaPersonasFirm, List<ExpPersonasFirmantesPorActasSesion> listaFirmantesActual) {

        try {
            Date fecha = ejbFacade.getSystemDate();
            Personas per2 = null;
            ExpPersonasFirmantesPorActasSesion perDocActual = null;
            boolean encontro = false;

            boolean enviarTextoNuevaParte;
            boolean enviarTextoBorrarParte;

            for (Personas per : listaPersonasFirm) {
                enviarTextoNuevaParte = false;
                enviarTextoBorrarParte = false;
                encontro = false;
                perDocActual = null;
                for (int i = 0; i < listaFirmantesActual.size(); i++) {
                    per2 = listaFirmantesActual.get(i).getPersonaFirmante();
                    if (per2.equals(per)) {
                        encontro = true;
                        perDocActual = listaFirmantesActual.get(i);
                        break;
                    }
                }
                if (!encontro) {
                    enviarTextoNuevaParte = true;
                    ExpPersonasFirmantesPorActasSesion perDoc = personasFirmantesPorActasSesionController.prepareCreate(null);
                    perDoc.setPersonaFirmante(per);
                    perDoc.setActaSesion(act);
                    perDoc.setPersonaAlta(personaUsuario);
                    perDoc.setFechaHoraAlta(fecha);
                    perDoc.setFirmado(false);
                    perDoc.setFechaHoraFirma(null);
                    perDoc.setEstado(new Estados("AC"));
                    personasFirmantesPorActasSesionController.setSelected(perDoc);
                    personasFirmantesPorActasSesionController.saveNew(null);
                    /*
                } else {
                    if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                        if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                            enviarTextoNuevaParte = true;
                        } else {
                            enviarTextoBorrarParte = true;
                        }

                        perDocActual.setEstado(new Estados(per.getEstado()));
                        perDocActual.setFechaHoraAlta(fecha);
                        perDocActual.setPersonaAlta(personaUsuario);
                        perDocActual.setFirmado(per.isFirmado());
                        personasFirmantesPorActasSesionController.setSelected(perDocActual);
                        personasFirmantesPorActasSesionController.save(null);
                    }
                     */

                }

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");

                if (enviarTextoNuevaParte) {
                    String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                            + "     Se le a agregado como firmante al acta de la sesion " + act.getTipoSesion().getDescripcion() + " de fecha " + format.format(act.getFechaSesion())
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver el acta, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?acta=" + act.getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(per.getEmail(), "Firma pendiente en Expediente Electrónico JEM", texto);
                }

                if (enviarTextoBorrarParte) {
                    String texto = "<p>Hola " + per.getNombresApellidos() + "<br> "
                            + "     Se le a sacado como firmante al acta de la sesion " + act.getTipoSesion().getDescripcion() + " de fecha " + format.format(act.getFechaSesion())
                            + " en el “Sistema de Expedientes Electrónicos del Jurado de Enjuiciamiento de Magistrados”<br>"
                            + "Para ver el acta, por favor, haga click el sgte link: <a href='" + par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/?acta=" + act.getId() + "'>CLICK AQUÍ</a>";

                    enviarEmailAviso(per.getEmail(), "Dejo de ser firmante en Expediente Electrónico JEM", texto);
                }
            }

            for (int i = 0; i < listaFirmantesActual.size(); i++) {
                per2 = listaFirmantesActual.get(i).getPersonaFirmante();
                encontro = false;
                for (Personas per : listaPersonasFirm) {
                    if (per2.equals(per)) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {

                    personasFirmantesPorActasSesionController.setSelected(listaFirmantesActual.get(i));
                    personasFirmantesPorActasSesionController.delete(null);
                    /*
                    listaFirmantesActual.get(i).setEstado(new Estados("IN"));
                    listaFirmantesActual.get(i).setFechaHoraAlta(fecha);
                    listaFirmantesActual.get(i).setPersonaAlta(personaUsuario);
                    listaFirmantesActual.get(i).setFirmado(per2.isFirmado());
                    personasFirmantesPorActasSesionController.setSelected(listaFirmantesActual.get(i));
                    personasFirmantesPorActasSesionController.save(null);
                     */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveExpedientes() {
        List<ExpDetallesActaSesion> listaExpedientesActual = ejbFacade.getEntityManager().createNamedQuery("ExpDetallesActaSesion.findByActaSesion", ExpDetallesActaSesion.class).setParameter("actaSesion", getSelected()).getResultList();
        saveExpedientes(getSelected(), listaExpedientes, listaExpedientesActual);
        // saveRolesFirmantes();
    }

    public void saveExpedientes(ExpActasSesion act, List<DocumentosJudiciales> listaPersonasFirm, List<ExpDetallesActaSesion> listaFirmantesActual) {

        try {
            Date fecha = ejbFacade.getSystemDate();
            DocumentosJudiciales per2 = null;
            ExpDetallesActaSesion perDocActual = null;
            boolean encontro = false;

            boolean enviarTextoNuevaParte;
            boolean enviarTextoBorrarParte;

            for (DocumentosJudiciales per : listaPersonasFirm) {
                enviarTextoNuevaParte = false;
                enviarTextoBorrarParte = false;
                encontro = false;
                perDocActual = null;
                for (int i = 0; i < listaFirmantesActual.size(); i++) {
                    per2 = listaFirmantesActual.get(i).getDocumentoJudicial();
                    if (per2.equals(per)) {
                        encontro = true;
                        perDocActual = listaFirmantesActual.get(i);
                        break;
                    }
                }
                if (!encontro) {
                    enviarTextoNuevaParte = true;
                    ExpDetallesActaSesion perDoc = new ExpDetallesActaSesion();
                    perDoc.setDocumentoJudicial(per);
                    perDoc.setActaSesion(act);
                    perDoc.setPersonaAlta(personaUsuario);
                    perDoc.setFechaHoraAlta(fecha);
                    perDoc.setPersonaUltimoEstado(personaUsuario);
                    perDoc.setFechaHoraUltimoEstado(fecha);
                    detalleActaSesionController.setSelected(perDoc);
                    detalleActaSesionController.saveNew(null);
                    /*
                } else {
                    if (!perDocActual.getEstado().getCodigo().equals(per.getEstado())) {
                        if (Constantes.ESTADO_USUARIO_AC.equals(per.getEstado())) {
                            enviarTextoNuevaParte = true;
                        } else {
                            enviarTextoBorrarParte = true;
                        }

                        perDocActual.setEstado(new Estados(per.getEstado()));
                        perDocActual.setFechaHoraAlta(fecha);
                        perDocActual.setPersonaAlta(personaUsuario);
                        perDocActual.setFirmado(per.isFirmado());
                        personasFirmantesPorActasSesionController.setSelected(perDocActual);
                        personasFirmantesPorActasSesionController.save(null);
                    }
                     */

                }

            }

            for (int i = 0; i < listaFirmantesActual.size(); i++) {
                per2 = listaFirmantesActual.get(i).getDocumentoJudicial();
                encontro = false;
                for (DocumentosJudiciales per : listaPersonasFirm) {
                    if (per2.equals(per)) {
                        encontro = true;
                        break;
                    }
                }
                if (!encontro) {

                    detalleActaSesionController.setSelected(listaFirmantesActual.get(i));
                    detalleActaSesionController.delete(null);
                    /*
                    listaFirmantesActual.get(i).setEstado(new Estados("IN"));
                    listaFirmantesActual.get(i).setFechaHoraAlta(fecha);
                    listaFirmantesActual.get(i).setPersonaAlta(personaUsuario);
                    listaFirmantesActual.get(i).setFirmado(per2.isFirmado());
                    personasFirmantesPorActasSesionController.setSelected(listaFirmantesActual.get(i));
                    personasFirmantesPorActasSesionController.save(null);
                     */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String save() {

        if (getSelected() != null) {

            if (getSelected().getTipoSesion() == null) {
                JsfUtil.addErrorMessage("Debe elegir un tipo de sesión");
                return "";
            }

            if (getSelected().getFechaSesion() == null) {
                JsfUtil.addErrorMessage("Debe elegir una fecha de sesión");
                return "";
            }

            if (estadoActaSesion == null) {
                JsfUtil.addErrorMessage("Debe elegir un estado");
                return "";
            }

            List<ExpEstadosActaSesion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findByCodigo", ExpEstadosActaSesion.class).setParameter("codigo", Constantes.ESTADO_ACTA_SESION_EN_PROYECTO).getResultList();

            if (est.isEmpty()) {
                JsfUtil.addErrorMessage("Error al obtener estado");
                return "";
            }

            if (est.get(0).getOrden().compareTo(estadoActaSesion.getOrden()) < 0) {
                if (listaPersonasFirmantes == null) {
                    JsfUtil.addErrorMessage("Debe escojer firmantes");
                    return "";
                } else if (listaPersonasFirmantes.isEmpty()) {
                    JsfUtil.addErrorMessage("Debe escojer firmantes");
                    return "";
                }
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(personaUsuario);
            getSelected().setEstado(estadoActaSesion);

            saveFirmantes();
            saveExpedientes();

            super.save(null);
            setSelected(null);

            buscarPorFecha();
            buscarDetalles();

            String retornar = "";
            if (accion == null) {
                retornar = "index.xhtml?faces-redirect=true";
            } else {
                retornar = "index.xhtml?tipo=" + accion + "&faces-redirect=true";
            }
            return retornar;
        }
        return "";
    }

    public String saveNew() {
        if (getSelected() != null) {

            if (getSelected().getTipoSesion() == null) {
                JsfUtil.addErrorMessage("Debe elegir un tipo de sesión");
                return "";
            }

            if (getSelected().getFechaSesion() == null) {
                JsfUtil.addErrorMessage("Debe elegir una fecha de sesión");
                return "";
            }

            if (estadoActaSesion == null) {
                JsfUtil.addErrorMessage("Debe elegir un estado");
                return "";
            }

            Date fecha = ejbFacade.getSystemDate();
            if (file == null) {
                JsfUtil.addErrorMessage("Debe adjuntar un documento");
                return "";
            } else if (file.getContent().length == 0) {
                JsfUtil.addErrorMessage("El documento esta vacio");
                return "";
            }

            if (par == null) {
                JsfUtil.addErrorMessage("Error al obtener parametros");
                return "";
            }

            List<ExpEstadosActaSesion> est = ejbFacade.getEntityManager().createNamedQuery("ExpEstadosActaSesion.findByCodigo", ExpEstadosActaSesion.class).setParameter("codigo", Constantes.ESTADO_ACTA_SESION_EN_PROYECTO).getResultList();

            if (est.isEmpty()) {
                JsfUtil.addErrorMessage("Error al obtener estado");
                return "";
            }

            if (est.get(0).getOrden().compareTo(estadoActaSesion.getOrden()) < 0) {
                if (listaPersonasFirmantes == null) {
                    JsfUtil.addErrorMessage("Debe escojer firmantes");
                    return "";
                } else if (listaPersonasFirmantes.isEmpty()) {
                    JsfUtil.addErrorMessage("Debe escojer firmantes");
                    return "";
                }
            }

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setPersonaUltimoEstado(personaUsuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setPersonaAlta(personaUsuario);
            getSelected().setVisible(false);
            getSelected().setEstado(estadoActaSesion);

            super.saveNew(null);

            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return "";
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + ".pdf";

            getSelected().setArchivo(nombreArchivo);

            String hash = "";
            try {
                hash = Utils.generarHash(fecha, getSelected().getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            getSelected().setHash(hash);

            super.save(null);

            FileOutputStream fos = null;
            try {
                // fos = new FileOutputStream(par.getRutaActas() + File.separator + nombreArchivo);
                fos = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            agregarQR(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArchivo, par.getRutaActas() + File.separator + getSelected().getArchivo(), getSelected().getHash(), false);

            File archivo = new File(Constantes.RUTA_ARCHIVOS_TEMP + File.separator + nombreArchivo);
            archivo.delete();

            saveFirmantes();
            saveExpedientes();

            buscarPorFecha();
            buscarDetalles();

            String retornar = "";
            if (accion == null) {
                retornar = "index.xhtml?faces-redirect=true";
            } else {
                retornar = "index.xhtml?tipo=" + accion + "&faces-redirect=true";
            }
            return retornar;
        }
        return "";
    }

    public void imprimirSesionEnElaboracion() {
        HttpServletResponse httpServletResponse = null;
        try {
            Collection<ExpActasSesion> fcCabecera = ejbFacade.getEntityManager().createNamedQuery("ExpActasSesion.findById", ExpActasSesion.class).setParameter("id", getSelected().getId()).getResultList();
            Iterator<ExpActasSesion> ifcCabecera = fcCabecera.iterator();
            Collection<RepActasSesion> detalles = new ArrayList<>();
            Collection<RepPersonasFirmantes> detallesFirmantes = new ArrayList<>();
            Collection<RepExpedientesAsociados> detallesExpedientes = new ArrayList<>();

            Collection<ExpPersonasFirmantesPorActasSesion> pfDetalle = new ArrayList<>();
            Collection<ExpDetallesActaSesion> fcDetalle = new ArrayList<>();
            while (ifcCabecera.hasNext()) {
                ExpActasSesion acta = ifcCabecera.next();
                fcDetalle = ejbFacade.getEntityManager().createNamedQuery("ExpDetallesActaSesion.findByActaSesion", ExpDetallesActaSesion.class).setParameter("actaSesion", acta).getResultList();
                //Iterator<ExpDetallesActaSesion> ifcDetalle = fcDetalle.iterator();
                pfDetalle = ejbFacade.getEntityManager().createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesion", ExpPersonasFirmantesPorActasSesion.class).setParameter("actaSesion", acta).getResultList();

                RepActasSesion actaSesion = new RepActasSesion();
                actaSesion.setFechaSesion(acta.getFechaSesion());
                actaSesion.setTipoSesion(acta.getTipoSesion().getDescripcion());
                actaSesion.setEstado(acta.getEstado().getDescripcion());
                
                detalles.add(actaSesion);
                //}
            }
            ifcCabecera = fcCabecera.iterator();
            while (ifcCabecera.hasNext()) {
                ExpActasSesion acta = ifcCabecera.next();

                pfDetalle = ejbFacade.getEntityManager()
                        .createNamedQuery("ExpPersonasFirmantesPorActasSesion.findByActaSesion", ExpPersonasFirmantesPorActasSesion.class)
                        .setParameter("actaSesion", acta)
                        .getResultList();

                // Recorrer la lista de firmantes
                for (ExpPersonasFirmantesPorActasSesion epf : pfDetalle) {

                    RepPersonasFirmantes personasFirmantes = new RepPersonasFirmantes();

                    personasFirmantes.setCi(epf.getPersonaFirmante().getCi());
                    personasFirmantes.setNombreApellidos(epf.getPersonaFirmante().getNombresApellidos());

                    detallesFirmantes.add(personasFirmantes);
                }
            }
            ifcCabecera = fcCabecera.iterator();
            while (ifcCabecera.hasNext()) {
                ExpActasSesion acta = ifcCabecera.next();

                fcDetalle = ejbFacade.getEntityManager()
                        .createNamedQuery("ExpDetallesActaSesion.findByActaSesion", ExpDetallesActaSesion.class)
                        .setParameter("actaSesion", acta)
                        .getResultList();

                // Recorrer la lista de expedientes asociados
                for (ExpDetallesActaSesion fcd : fcDetalle) {

                    RepExpedientesAsociados expedientesAsociados = new RepExpedientesAsociados();

                   expedientesAsociados.setCausas(fcd.getDocumentoJudicial().getCausa());
                   expedientesAsociados.setCaratula(fcd.getDocumentoJudicial().getCaratula());

                    detallesExpedientes.add(expedientesAsociados);
                }
            }
            HashMap map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date fecha = ejbFacade.getSystemDate();

            map.put("fecha", format.format(fecha));
            map.put("tituloReporte", tituloReporte);
            map.put("listas_firmantes", new JRBeanCollectionDataSource(detallesFirmantes));
            map.put("listas_expedientes", new JRBeanCollectionDataSource(detallesExpedientes));
            map.put("listas_sesion_actas", new JRBeanCollectionDataSource(detalles));

            //Paso el detalle del reporte
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(detalles);

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/acta_sesion_en_elaboracion.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=actas_sesion.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
