package py.gov.jem.expedientes.converters;

import py.gov.jem.expedientes.models.AntecedentesPermisos;
import py.gov.jem.expedientes.facades.AntecedentesPermisosFacade;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.faces.convert.FacesConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@FacesConverter(value = "antecedentesPermisosConverter")
public class AntecedentesPermisosConverter implements Converter {

    private AntecedentesPermisosFacade ejbFacade;
    
    public AntecedentesPermisosConverter() {
        this.ejbFacade = CDI.current().select(AntecedentesPermisosFacade.class).get();
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
        return this.ejbFacade.find(getKey(value));
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
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
        if (object instanceof AntecedentesPermisos) {
            AntecedentesPermisos o = (AntecedentesPermisos) object;
            return getStringKey(o.getId());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AntecedentesPermisos.class.getName()});
            return null;
        }
    }

}
