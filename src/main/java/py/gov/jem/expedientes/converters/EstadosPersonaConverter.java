package py.gov.jem.expedientes.converters;

import py.gov.jem.expedientes.models.EstadosPersona;
import py.gov.jem.expedientes.facades.EstadosPersonaFacade;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.convert.FacesConverter;
import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@FacesConverter(value = "estadosPersonaConverter")
public class EstadosPersonaConverter implements Converter {

    private EstadosPersonaFacade ejbFacade;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.getEjbFacade().find(getKey(value));
    }

    java.lang.String getKey(String value) {
        java.lang.String key;
        key = value;
        return key;
    }

    String getStringKey(java.lang.String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof EstadosPersona) {
            EstadosPersona o = (EstadosPersona) object;
            return getStringKey(o.getCodigo());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), EstadosPersona.class.getName()});
            return null;
        }
    }

    private EstadosPersonaFacade getEjbFacade() {
        this.ejbFacade = CDI.current().select(EstadosPersonaFacade.class).get();
        return this.ejbFacade;
    }
}
