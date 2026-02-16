package py.gov.jem.expedientes.converters;

import py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudiciales;
import py.gov.jem.expedientes.facades.ExpPartesPorDocumentosJudicialesFacade;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.faces.convert.FacesConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@FacesConverter(value = "expPartesPorDocumentosJudicialesConverter")
public class ExpPartesPorDocumentosJudicialesConverter implements Converter {

    private ExpPartesPorDocumentosJudicialesFacade ejbFacade;
    
    public ExpPartesPorDocumentosJudicialesConverter() {
        this.ejbFacade = CDI.current().select(ExpPartesPorDocumentosJudicialesFacade.class).get();
    }

    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesPK getKey(String value) {
        py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesPK();
        key.setPersona(Integer.parseInt(values[0]));
        key.setDocumentoJudicial(Integer.parseInt(values[1]));
        return key;
    }

    String getStringKey(py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getPersona());
        sb.append(SEPARATOR);
        sb.append(value.getDocumentoJudicial());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof ExpPartesPorDocumentosJudiciales) {
            ExpPartesPorDocumentosJudiciales o = (ExpPartesPorDocumentosJudiciales) object;
            return getStringKey(o.getPartesPorDocumentosJudicialesPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ExpPartesPorDocumentosJudiciales.class.getName()});
            return null;
        }
    }

}
