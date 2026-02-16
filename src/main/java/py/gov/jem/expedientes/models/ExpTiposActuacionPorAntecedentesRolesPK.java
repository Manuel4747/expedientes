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
public class ExpTiposActuacionPorAntecedentesRolesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private int rol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_actuacion")
    private int tipoActuacion;

    public ExpTiposActuacionPorAntecedentesRolesPK() {
    }

    public ExpTiposActuacionPorAntecedentesRolesPK(int rol, int tipoActuacion) {
        this.rol = rol;
        this.tipoActuacion = tipoActuacion;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getTipoActuacion() {
        return tipoActuacion;
    }

    public void setTipoActuacion(int tipoActuacion) {
        this.tipoActuacion = tipoActuacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) rol;
        hash += (int) tipoActuacion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpTiposActuacionPorAntecedentesRolesPK)) {
            return false;
        }
        ExpTiposActuacionPorAntecedentesRolesPK other = (ExpTiposActuacionPorAntecedentesRolesPK) object;
        if (this.rol != other.rol) {
            return false;
        }
        if (this.tipoActuacion != other.tipoActuacion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpTiposActuacionPorAntecedenteRolesPK[ rol=" + rol + ", tipoActuacion=" + tipoActuacion + " ]";
    }
    
}
