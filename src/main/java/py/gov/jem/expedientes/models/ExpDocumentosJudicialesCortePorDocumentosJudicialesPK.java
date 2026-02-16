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
public class ExpDocumentosJudicialesCortePorDocumentosJudicialesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "documento_judicial_corte")
    private int documentoJudicialCorte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "documento_judicial")
    private int documentoJudicial;

    public ExpDocumentosJudicialesCortePorDocumentosJudicialesPK() {
    }

    public ExpDocumentosJudicialesCortePorDocumentosJudicialesPK(int documentoJudicialCorte, int documentoJudicial) {
        this.documentoJudicialCorte = documentoJudicialCorte;
        this.documentoJudicial = documentoJudicial;
    }

    public int getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setDocumentoJudicialCorte(int documentoJudicialCorte) {
        this.documentoJudicialCorte = documentoJudicialCorte;
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
        hash += (int) documentoJudicialCorte;
        hash += (int) documentoJudicial;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpDocumentosJudicialesCortePorDocumentosJudicialesPK)) {
            return false;
        }
        ExpDocumentosJudicialesCortePorDocumentosJudicialesPK other = (ExpDocumentosJudicialesCortePorDocumentosJudicialesPK) object;
        if (this.documentoJudicialCorte != other.documentoJudicialCorte) {
            return false;
        }
        if (this.documentoJudicial != other.documentoJudicial) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpDocumentosJudicialesPorDocumentosJudicialesPK[ documentoJudicialCorte=" + documentoJudicialCorte + ", documentoJudicial=" + documentoJudicial + " ]";
    }
    
}
