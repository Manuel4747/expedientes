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
public class AntecedentesRolesPorPersonasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "persona")
    private int persona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rol")
    private int rol;

    public AntecedentesRolesPorPersonasPK() {
    }

    public AntecedentesRolesPorPersonasPK(int persona, int rol) {
        this.persona = persona;
        this.rol = rol;
    }

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
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
        hash += (int) persona;
        hash += (int) rol;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecedentesRolesPorPersonasPK)) {
            return false;
        }
        AntecedentesRolesPorPersonasPK other = (AntecedentesRolesPorPersonasPK) object;
        if (this.persona != other.persona) {
            return false;
        }
        if (this.rol != other.rol) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.AntecedentesRolesPorPersonaPK[ persona=" + persona + ", rol=" + rol + " ]";
    }
    
}
