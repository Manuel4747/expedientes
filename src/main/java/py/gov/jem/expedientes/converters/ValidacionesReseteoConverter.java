package py.gov.jem.expedientes.converters;

import py.gov.jem.expedientes.models.ValidacionesReseteo;
import py.gov.jem.expedientes.facades.ValidacionesReseteoFacade;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.faces.convert.FacesConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@FacesConverter(value = "validacionesReseteoConverter")
public class ValidacionesReseteoConverter implements Converter {

    private ValidacionesReseteoFacade ejbFacade;
    
    public ValidacionesReseteoConverter() {
        this.ejbFacade = CDI.current().select(ValidacionesReseteoFacade.class).get();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
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
        if (object instanceof ValidacionesReseteo) {
            ValidacionesReseteo o = (ValidacionesReseteo) object;
            return getStringKey(o.getHash());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ValidacionesReseteo.class.getName()});
            return null;
        }
    }

}
