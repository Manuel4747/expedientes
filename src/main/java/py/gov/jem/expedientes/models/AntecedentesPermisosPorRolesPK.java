/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author eduardo
 */
@Embeddable
public class AntecedentesPermisosPorRolesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "permiso")
    private int permiso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private int rol;

    public AntecedentesPermisosPorRolesPK() {
    }

    public AntecedentesPermisosPorRolesPK(int permiso, int rol) {
        this.permiso = permiso;
        this.rol = rol;
    }

    public int getPermiso() {
        return permiso;
    }

    public void setPermiso(int permiso) {
        this.permiso = permiso;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) permiso;
        hash += (int) rol;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecedentesPermisosPorRolesPK)) {
            return false;
        }
        AntecedentesPermisosPorRolesPK other = (AntecedentesPermisosPorRolesPK) object;
        if (this.permiso != other.permiso) {
            return false;
        }
        if (this.rol != other.rol) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.AntecedenesPermisosPorRolesPK[ permiso=" + permiso + ", rol=" + rol + " ]";
    }
    
}
