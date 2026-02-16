package py.gov.jem.expedientes.controllers;

import java.util.Collection;
import py.gov.jem.expedientes.models.ExpPersonasHabilitadasPorDocumentosJudiciales;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "expPersonasHabilitadasPorDocumentosJudicialesController")
@ViewScoped
public class ExpPersonasHabilitadasPorDocumentosJudicialesController extends AbstractController<ExpPersonasHabilitadasPorDocumentosJudiciales> {

    public ExpPersonasHabilitadasPorDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(ExpPersonasHabilitadasPorDocumentosJudiciales.class);
    }

    @Override
    public Collection<ExpPersonasHabilitadasPorDocumentosJudiciales> getItems() {
        return super.getItems2();
    }

}
