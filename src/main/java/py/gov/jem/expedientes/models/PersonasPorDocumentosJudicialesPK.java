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
public class PersonasPorDocumentosJudicialesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "persona")
    private int persona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "documento_judicial")
    private int documentoJudicial;

    public PersonasPorDocumentosJudicialesPK() {
    }

    public PersonasPorDocumentosJudicialesPK(int persona, int documentoJudicial) {
        this.persona = persona;
        this.documentoJudicial = documentoJudicial;
    }

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
    }

    public int getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(int documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) persona;
        hash += (int) documentoJudicial;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonasPorDocumentosJudicialesPK)) {
            return false;
        }
        PersonasPorDocumentosJudicialesPK other = (PersonasPorDocumentosJudicialesPK) object;
        if (this.persona != other.persona) {
            return false;
        }
        if (this.documentoJudicial != other.documentoJudicial) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.PersonasPorDocumentosJudicialesPK[ persona=" + persona + ", documentoJudicial=" + documentoJudicial + " ]";
    }
    
}
