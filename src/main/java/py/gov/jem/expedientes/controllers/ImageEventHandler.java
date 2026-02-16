/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.controllers;

import com.itextpdf.commons.actions.IEvent;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageEventHandler implements IEventHandler {

    protected Image img;

    public ImageEventHandler(Image img) {
        this.img = img;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        /*
        PdfCanvas aboveCanvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
        Rectangle area = page.getPageSize();
        new Canvas(aboveCanvas, pdfDoc, area).add(img);
*/

        try {
            File pathToFile = new File(Constantes.RUTA_RAIZ_ARCHIVOS + "/jem/imagen_logo_chico.jpg");
            java.awt.Image imagen = ImageIO.read(pathToFile);
            Rectangle pageSize = page.getPageSize();
            float x = (pageSize.getWidth() / 2) - 100;
            float y = pageSize.getTop() - 100;
            PdfCanvas under = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
            Rectangle rect = new Rectangle(x, y, 200, 80);
            under.addImageFittedIntoRectangle(ImageDataFactory.create(imagen, Color.white), rect, false);
            under.saveState();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
