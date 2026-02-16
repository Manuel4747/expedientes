package py.gov.jem.expedientes.converters;

import py.gov.jem.expedientes.models.AntecedentesPermisosPorRoles;
import py.gov.jem.expedientes.facades.AntecedentesPermisosPorRolesFacade;
import py.gov.jem.expedientes.controllers.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;
import javax.faces.convert.FacesConverter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@FacesConverter(value = "antecedentesPermisosPorRolesConverter")
public class AntecedentesPermisosPorRolesConverter implements Converter {

    private AntecedentesPermisosPorRolesFacade ejbFacade;
    
    public AntecedentesPermisosPorRolesConverter() {
        this.ejbFacade = CDI.current().select(AntecedentesPermisosPorRolesFacade.class).get();
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

    py.gov.jem.expedientes.models.AntecedentesPermisosPorRolesPK getKey(String value) {
        py.gov.jem.expedientes.models.AntecedentesPermisosPorRolesPK key;
        String values[] = value.split(SEPARATOR_ESCAPED);
        key = new py.gov.jem.expedientes.models.AntecedentesPermisosPorRolesPK();
        key.setPermiso(Integer.parseInt(values[0]));
        key.setRol(Integer.parseInt(values[1]));
        return key;
    }

    String getStringKey(py.gov.jem.expedientes.models.AntecedentesPermisosPorRolesPK value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value.getPermiso());
        sb.append(SEPARATOR);
        sb.append(value.getRol());
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null
                || (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof AntecedentesPermisosPorRoles) {
            AntecedentesPermisosPorRoles o = (AntecedentesPermisosPorRoles) object;
            return getStringKey(o.getAntecedentesPermisosPorRolesPK());
        } else {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), AntecedentesPermisosPorRoles.class.getName()});
            return null;
        }
    }

}
