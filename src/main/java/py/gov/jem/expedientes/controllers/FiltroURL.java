/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.controllers;

import java.util.Iterator;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import py.gov.jem.expedientes.models.VAntecedentesPermisosPersonas;

/**
 *
 * @author eduardo
 */
@Named(value = "filtroURL")
@ViewScoped
public class FiltroURL implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent pe) {
        FacesContext facesContext = pe.getFacesContext();

        String currentPage = facesContext.getViewRoot().getViewId();

        boolean isPageLogin = currentPage.lastIndexOf("login.xhtml") > -1;
        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("altaPersona.xhtml") > -1;
        }
        
        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("cambioContrasena.xhtml") > -1;
        }
        
        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("pedidosCambioContrasena.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacion.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacionesEscrito.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacionesActuacion.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacionesActaSesion.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacionesReseteo.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacionesReseteo_1.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacionesEmail.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("validacionesEmailExpediente.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasReseteo.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPedido.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPersona.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPersona2.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPedidoExpediente.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPersonaExpediente.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPersonaExpediente2.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPedidosCambioContrasena.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasPedidosCambioContrasena2.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("Caducado.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("Caducado2.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasValidacionEmail.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasCambioContrasena.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("GraciasValidacionEmailExpediente.xhtml") > -1;
        }

        if (!isPageLogin) {
            isPageLogin = currentPage.lastIndexOf("elegirRol.xhtml") > -1;
        }

        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        
        Object es = session.getAttribute("esApp");
        
        boolean esApp = ((es==null)?false:(boolean) es);
        

        Object usuario = session.getAttribute("Persona");

        if (!isPageLogin && usuario == null && !(boolean) esApp) {
            NavigationHandler handler = facesContext.getApplication().getNavigationHandler();
            handler.handleNavigation(facesContext, null, "login.xhtml");
        } else if (!isPageLogin) {

            if (!verifPermiso(currentPage, "")) {
                NavigationHandler handler = facesContext.getApplication().getNavigationHandler();
                handler.handleNavigation(facesContext, null, "/denegado.xhtml");
            }

        }
    }

    public boolean verifPermiso(String url, int rol) {
        return verifPermiso(url, "", rol);
    }

    public boolean verifPermiso(String url) {
        return verifPermiso(url, "");
    }

    public boolean verifPermiso(String url, String permiso) {
        return verifPermiso(url, permiso, null, 0);
    }

    public boolean verifPermiso(String url, String permiso, int rol) {
        return verifPermiso(url, permiso, null, rol);
    }

    public boolean verifPermiso(String url, String permiso, String menu, int rol) {

        if (permiso == null) {
            permiso = "";
        }

        if (url == null) {
            url = "";
        }

        boolean found = false;
        if (url.contains("pedidosPersona")) {
            found = true;
        } else {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            List<VAntecedentesPermisosPersonas> p = (List<VAntecedentesPermisosPersonas>) session.getAttribute("VAntecedentesPermisosPersonas");

            Iterator<VAntecedentesPermisosPersonas> it = p.iterator();
            VAntecedentesPermisosPersonas permUsua = null;

            while (it.hasNext()) {

                permUsua = it.next();

                if (menu == null) {
                    if (permUsua.getFuncion().equals(url) && (rol == 0 || permUsua.getRoleId() == rol)) {
                        if (!"".equals(permiso)) {
                            if (permUsua.getPermiso().equals(permiso) || permUsua.getPermiso().equals("TOTAL")) {
                                found = true;
                                break;
                            }
                        } else {
                            found = true;
                            break;
                        }
                    }
                } else {
                    if(permUsua.getMenu() != null){
                        if (permUsua.getMenu().equals(menu) && (rol == 0 || permUsua.getRoleId() == rol)) {
                            found = true;
                            break;
                        }
                    }

                }

            }
/*
            if (!found) {

                System.out.println("No tiene acceso a " + url + ", permiso " + ((permiso == null) ? "null" : permiso));
            }
*/
        }
        return found;
    }

    @Override
    public void beforePhase(PhaseEvent pe) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
