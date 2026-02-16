package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import py.gov.jem.expedientes.models.ExpPersonasInhibidasPorDocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "expPersonasInhibidasPorDocumentosJudicialesController")
@ViewScoped
public class ExpPersonasInhibidasPorDocumentosJudicialesController extends AbstractController<ExpPersonasInhibidasPorDocumentosJudiciales> {

    public ExpPersonasInhibidasPorDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpPersonasInhibidasPorDocumentosJudiciales.class);
    }

    @Override
    public Collection<ExpPersonasInhibidasPorDocumentosJudiciales> getItems() {
        return super.getItems2();
    }

}
