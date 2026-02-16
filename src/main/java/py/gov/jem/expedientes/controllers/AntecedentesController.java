package py.gov.jem.expedientes.controllers;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import java.awt.Color;
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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.Antecedentes;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.apache.poi.util.IOUtils;
// import org.primefaces.model.UploadedFile;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.Empresas;
import py.gov.jem.expedientes.models.ExpPedidosAntecedente;
import py.gov.jem.expedientes.models.ParametrosSistema;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.Roles;

@Named(value = "antecedentesController")
@ViewScoped
public class AntecedentesController extends AbstractController<Antecedentes> {

    private HttpSession session;
    private Personas personaUsuario;
    private ParametrosSistema par;
    private String endpoint;

    public AntecedentesController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(Antecedentes.class);
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);
        String[] array = uri.split("/");
        endpoint = array[1];

        personaUsuario = (Personas) session.getAttribute("Persona");
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<Antecedentes> getItems() {
        List<Antecedentes> lista = this.ejbFacade.getEntityManager().createNamedQuery("Antecedentes.findByPersona", Antecedentes.class).setParameter("persona", personaUsuario).getResultList();

        List<Antecedentes> listaFinal = new ArrayList<>();
        if (lista.size() > 5) {
            Antecedentes ant = null;
            for (int i = 0; i < 5; i++) {
                listaFinal.add(lista.get(i));
            }
        } else {
            listaFinal = lista;
        }

        setItems(listaFinal);
        return super.getItems2();
    }

    public boolean borrarAntecedente(Antecedentes ant) {
        if (!ant.isVisto()) {
            ant.setPersonaBorrado(personaUsuario);
            ant.setFechaHoraBorrado(ejbFacade.getSystemDate());

            setSelected(ant);
            super.save(null);
            super.delete(null);
            setSelected(null);

            return true;
        } else {
            JsfUtil.addErrorMessage("No se puede borrar un antecedente que ya fue visto");
            return false;
        }
    }

    public String generarCodigoArchivo() {
        Random aleatorio;
        int valor = 0;
        String pin = "";
        aleatorio = new Random(System.currentTimeMillis());
        valor = aleatorio.nextInt(999999);
        pin = String.valueOf(valor);
        while (pin.trim().length() < 6) {
            pin = '0' + pin;
        }
        return pin;
    }

    public Antecedentes generarAntecedente(byte[] file, Personas per, boolean abrir) {

        return generarAntecedente(file, per, abrir, true, Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentes() + File.separator);
    }
    
    public Antecedentes generarAntecedente(UploadedFile file, Personas per, boolean abrir) {
        if (file == null) {
            JsfUtil.addErrorMessage("Debe adjuntar un escrito");
            return null;
        // } else if (file.getContents().length == 0) {
        } else if (file.getContent().length == 0) {
            JsfUtil.addErrorMessage("El documento esta vacio");
            return null;
        }

        byte[] bytes = null;
        try {
            // bytes = IOUtils.toByteArray(file.getInputstream());
            bytes = IOUtils.toByteArray(file.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error al leer archivo");
            return null;
        }

        if (bytes == null) {
            JsfUtil.addErrorMessage("Error al leer archivo");
            return null;
        } else if (bytes.length == 0) {
            JsfUtil.addErrorMessage("Error al leer archivo");
            return null;
        }

        return generarAntecedente(bytes, per, abrir, true, Constantes.RUTA_RAIZ_ARCHIVOS + File.separator + par.getRutaAntecedentes() + File.separator);
    }

    public Antecedentes generarAntecedente(byte[] file, Personas per, boolean abrir, boolean guardar, String ruta) {

        Date fecha = ejbFacade.getSystemDate();
        String codigoArchivo = generarCodigoArchivo();
        DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error al generar MD5");
            return null;
        }

        md.update((per.getId() + "_" + format2.format(fecha)).getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

        String nombreArchivo = myHash + ".pdf";

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(ruta + nombreArchivo + "_temp");
            fos.write(file);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            JsfUtil.addErrorMessage("No se pudo guardar archivo");
            fos = null;
        } catch (IOException ex) {
            JsfUtil.addErrorMessage("No se pudo guardar archivo");
            fos = null;

        }

        agregarQR(nombreArchivo, fecha, codigoArchivo, myHash, ruta);

        File archivo = new File(ruta + nombreArchivo + "_temp");
        archivo.delete();

        Antecedentes ant = new Antecedentes();
        ant.setPathArchivo(nombreArchivo);

        if (guardar) {

            ant.setEmpresa(new Empresas(1));
            ant.setFechaHoraAlta(fecha);
            ant.setFechaHoraUltimoEstado(fecha);
            ant.setPersona(per);
            ant.setHash(myHash);
            ant.setCodigoArchivo(codigoArchivo);
            ant.setPersonaGeneracion(personaUsuario);

            setSelected(ant);
            saveNew(null);
        }

        if (abrir) {
            try {
                byte[] fileByte = Files.readAllBytes(Paths.get(ruta + nombreArchivo));

                HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                servletOutputStream.write(fileByte);
                FacesContext.getCurrentInstance().responseComplete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return ant;
    }

    public void reimprimirRepAntecedentes(Antecedentes ant) {

        HttpServletResponse httpServletResponse = null;
        if (ant != null) {
            if (ant.getPathArchivo() != null) {
                try {

                    byte[] fileByte = null;
                    try {
                        fileByte = Files.readAllBytes(new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/" + par.getRutaAntecedentes() + "/" + ant.getPathArchivo()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No existe documento");
                        return;
                    }

                    if (fileByte != null) {

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
                    JsfUtil.addErrorMessage("No se pudo generar el documento.");

                }
            } else {
                JsfUtil.addErrorMessage("No existe documento (2)");
            }
        } else {
            JsfUtil.addErrorMessage("No existe documento (3)");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void agregarQR(String nombre, Date fecha, String codigoArchivo, String hash, String ruta) {
        PdfWriter writer;
        try {

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(ruta + nombre + "_temp"), new PdfWriter(ruta + nombre));

            PdfPage pagina = pdfDoc.getPage(pdfDoc.getNumberOfPages());

            DateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Paragraph paragraph = new Paragraph("Fecha de emision: " + format2.format(fecha)).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(10);
            Paragraph paragraph2 = new Paragraph("Código de Verificación: " + codigoArchivo).setMargin(0).setMultipliedLeading(1).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(10);

            Canvas canvas = new Canvas(new PdfCanvas(pagina), pdfDoc, pagina.getMediaBox());

            paragraph.setWidth(500);
            canvas.showTextAligned(paragraph, 80, 160, TextAlignment.LEFT);
            canvas.showTextAligned(paragraph2, 80, 150, TextAlignment.LEFT);
            canvas.close();

            String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION;
            BarcodeQRCode qrCode = new BarcodeQRCode(path + "?hash=" + hash);

            java.awt.Image imagen = qrCode.createAwtImage(Color.black, new Color(0, 0, 0, 0));

            PdfCanvas under = new PdfCanvas(pagina.newContentStreamAfter(), pagina.getResources(), pdfDoc);

            Rectangle rect = new Rectangle(80, 70, 80, 80);
            under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);

            under.saveState();

            pdfDoc.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DocumentosJudicialesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DocumentosJudicialesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setEmpresa(new Empresas(1));

            super.saveNew(event);

        }

    }
}
