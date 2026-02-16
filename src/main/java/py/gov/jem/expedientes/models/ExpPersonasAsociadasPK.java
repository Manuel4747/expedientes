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
public class ExpPersonasAsociadasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "persona")
    private int persona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "persona_asociada")
    private int personaAsociada;

    public ExpPersonasAsociadasPK() {
    }

    public ExpPersonasAsociadasPK(int persona, int personaAsociada) {
        this.persona = persona;
        this.personaAsociada = personaAsociada;
    }

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
    }

    public int getPersonaAsociada() {
        return personaAsociada;
    }

    public void setPersonaAsociada(int personaAsociada) {
        this.personaAsociada = personaAsociada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) persona;
        hash += (int) personaAsociada;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpPersonasAsociadasPK)) {
            return false;
        }
        ExpPersonasAsociadasPK other = (ExpPersonasAsociadasPK) object;
        if (this.persona != other.persona) {
            return false;
        }
        if (this.personaAsociada != other.personaAsociada) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpPersonasAsociadasPK[ persona=" + persona + ", personaAsociada=" + personaAsociada + " ]";
    }
    
}
