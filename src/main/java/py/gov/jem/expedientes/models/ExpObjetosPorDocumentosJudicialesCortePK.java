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
public class ExpObjetosPorDocumentosJudicialesCortePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "objeto_corte")
    private int objetoCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "documento_judicial_corte")
    private int documentoJudicialCorte;

    public ExpObjetosPorDocumentosJudicialesCortePK() {
    }

    public ExpObjetosPorDocumentosJudicialesCortePK(int objetoCorte, int documentoJudicialCorte) {
        this.objetoCorte = objetoCorte;
        this.documentoJudicialCorte = documentoJudicialCorte;
    }

    public int getPersonaCorte() {
        return objetoCorte;
    }

    public void setPersonaCorte(int objetoCorte) {
        this.objetoCorte = objetoCorte;
    }

    public int getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setDocumentoJudicialCorte(int documentoJudicialCorte) {
        this.documentoJudicialCorte = documentoJudicialCorte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) objetoCorte;
        hash += (int) documentoJudicialCorte;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpObjetosPorDocumentosJudicialesCortePK)) {
            return false;
        }
        ExpObjetosPorDocumentosJudicialesCortePK other = (ExpObjetosPorDocumentosJudicialesCortePK) object;
        if (this.objetoCorte != other.objetoCorte) {
            return false;
        }
        if (this.documentoJudicialCorte != other.documentoJudicialCorte) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpObjetosPorDocumentosJudicialesCortePK[ personaCorte=" + objetoCorte + ", documentoJudicialCorte=" + documentoJudicialCorte + " ]";
    }
    
}
