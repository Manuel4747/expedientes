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
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import py.gov.jem.expedientes.models.DocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.file.UploadedFile;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import py.gov.jem.expedientes.models.ParametrosSistema;

@Named(value = "qrController")
@ViewScoped
public class QRController extends AbstractController<DocumentosJudiciales> {

    private UploadedFile file;
    private ParametrosSistema par;
    private String endpoint;
    private String url;
    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public QRController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudiciales.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID_ANTECEDENTE).getSingleResult();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];
    }
    
    public void guardar(){
        if (file != null) {
            if (file.getContent().length > 0) {

                if (par == null) {
                    JsfUtil.addErrorMessage("Error al obtener parametros");
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
                
                String nombreArchivo = "qr.pdf";
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo + "_temp");
                    fos.write(bytes);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                agregarQR(par.getRutaArchivos() + File.separator + nombreArchivo, hash, false);
            }
        }
    }
    
    private void agregarQR(String nombre, String hash, boolean primeraPagina) {
        PdfWriter writer;
        try {

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(nombre + "_temp"), new PdfWriter(nombre));

            Document document = new Document(pdfDoc);

            String path = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + endpoint + "/" + Constantes.URL_VALIDACION_ACTUACION;
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
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
