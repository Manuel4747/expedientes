/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_personas_asociadas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPersonasAsociadas.findAll", query = "SELECT p FROM ExpPersonasAsociadas p")
    , @NamedQuery(name = "ExpPersonasAsociadas.findByPersona", query = "SELECT p FROM ExpPersonasAsociadas p WHERE p.expPersonasAsociadasPK.persona = :persona")
    , @NamedQuery(name = "ExpPersonasAsociadas.findByPersonaAsociada", query = "SELECT p FROM ExpPersonasAsociadas p WHERE p.expPersonasAsociadasPK.personaAsociada = :personaAsociada")})
public class ExpPersonasAsociadas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpPersonasAsociadasPK expPersonasAsociadasPK;
    @JoinColumn(name = "persona", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "persona_asociada", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personas personaAsociada;

    public ExpPersonasAsociadas() {
    }

    public ExpPersonasAsociadas(ExpPersonasAsociadasPK expPersonasAsociadasPK) {
        this.expPersonasAsociadasPK = expPersonasAsociadasPK;
    }

    public ExpPersonasAsociadas(int persona, int personaAsociada) {
        this.expPersonasAsociadasPK = new ExpPersonasAsociadasPK(persona, personaAsociada);
    }

    public ExpPersonasAsociadasPK getExpPersonasAsociadasPK() {
        return expPersonasAsociadasPK;
    }

    public void setExpPersonasAsociadasPK(ExpPersonasAsociadasPK expPersonasAsociadasPK) {
        this.expPersonasAsociadasPK = expPersonasAsociadasPK;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Personas getPersonaAsociada() {
        return personaAsociada;
    }

    public void setPersonaAsociada(Personas personaAsociada) {
        this.personaAsociada = personaAsociada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (expPersonasAsociadasPK != null ? expPersonasAsociadasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpPersonasAsociadas)) {
            return false;
        }
        ExpPersonasAsociadas other = (ExpPersonasAsociadas) object;
        if ((this.expPersonasAsociadasPK == null && other.expPersonasAsociadasPK != null) || (this.expPersonasAsociadasPK != null && !this.expPersonasAsociadasPK.equals(other.expPersonasAsociadasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpPersonasAsociadas[ expPersonasAsociadasPK=" + expPersonasAsociadasPK + " ]";
    }
    
}
