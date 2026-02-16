package py.gov.jem.expedientes.converters;

import py.gov.jem.expedientes.models.EstadosProceso;
import py.gov.jem.expedientes.facades.EstadosProcesoFacade;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.convert.FacesConverter;
import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@FacesConverter(value = "estadosProcesoConverter")
public class EstadosProcesoConverter implements Converter {

    private EstadosProcesoFacade ejbFacade;

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
        if (object instanceof EstadosProceso) {
            EstadosProceso o = (EstadosProceso) object;
            return getStringKey(o.getCodigo());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), EstadosProceso.class.getName()});
            return null;
        }
    }

    private EstadosProcesoFacade getEjbFacade() {
        this.ejbFacade = CDI.current().select(EstadosProcesoFacade.class).get();
        return this.ejbFacade;
    }
}
