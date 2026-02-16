/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_documentos_judiciales_corte_por_documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpDocumentosJudicialesCortePorDocumentosJudiciales.findAll", query = "SELECT r FROM ExpDocumentosJudicialesCortePorDocumentosJudiciales r")
    , @NamedQuery(name = "ExpDocumentosJudicialesCortePorDocumentosJudiciales.findByDocumentoJudicial", query = "SELECT r FROM ExpDocumentosJudicialesCortePorDocumentosJudiciales r WHERE r.documentosJudicialesCortePorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial")
    , @NamedQuery(name = "ExpDocumentosJudicialesCortePorDocumentosJudiciales.findByDocumentoJudicialCorteANDDocumentoJudicial", query = "SELECT r FROM ExpDocumentosJudicialesCortePorDocumentosJudiciales r WHERE r.documentosJudicialesCortePorDocumentosJudicialesPK.documentoJudicialCorte = :documentoJudicialCorte AND r.documentosJudicialesCortePorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial")})
public class ExpDocumentosJudicialesCortePorDocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpDocumentosJudicialesCortePorDocumentosJudicialesPK documentosJudicialesCortePorDocumentosJudicialesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "documento_judicial_corte", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentosJudicialesCorte documentoJudicialCorte;

    public ExpDocumentosJudicialesCortePorDocumentosJudiciales() {
    }

    public ExpDocumentosJudicialesCortePorDocumentosJudiciales(ExpDocumentosJudicialesCortePorDocumentosJudicialesPK documentosJudicialesCortePorDocumentosJudicialesPK) {
        this.documentosJudicialesCortePorDocumentosJudicialesPK = documentosJudicialesCortePorDocumentosJudicialesPK;
    }

    public ExpDocumentosJudicialesCortePorDocumentosJudiciales(ExpDocumentosJudicialesCortePorDocumentosJudicialesPK documentosJudicialesCortePorDocumentosJudicialesPK, DocumentosJudicialesCorte documentoJudicialCorte, DocumentosJudiciales documentoJudicial) {
        this.documentosJudicialesCortePorDocumentosJudicialesPK = documentosJudicialesCortePorDocumentosJudicialesPK;
        this.documentoJudicialCorte = documentoJudicialCorte;
        this.documentoJudicial = documentoJudicial;
    }

    public ExpDocumentosJudicialesCortePorDocumentosJudiciales(int documentoJudicialCorte, int documentoJudicial) {
        this.documentosJudicialesCortePorDocumentosJudicialesPK = new ExpDocumentosJudicialesCortePorDocumentosJudicialesPK(documentoJudicialCorte, documentoJudicial);
    }

    public ExpDocumentosJudicialesCortePorDocumentosJudiciales(ExpDocumentosJudicialesCortePorDocumentosJudicialesPK documentosJudicialesCortePorDocumentosJudicialesPK, Date fechaHoraAlta, Date fechaHoraUltimoEstado, Personas personaAlta, Personas personaUltimoEstado, DocumentosJudiciales documentoJudicial, DocumentosJudicialesCorte documentoJudicialCorte) {
        this.documentosJudicialesCortePorDocumentosJudicialesPK = documentosJudicialesCortePorDocumentosJudicialesPK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.personaAlta = personaAlta;
        this.personaUltimoEstado = personaUltimoEstado;
        this.documentoJudicial = documentoJudicial;
        this.documentoJudicialCorte = documentoJudicialCorte;
    }
    
/*
    public ExpDocumentosJudicialesCortePorDocumentosJudiciales(Personas persona, DocumentosJudiciales documentoJudicial, ExpTiposParte tipoParte) {
        this.personasPorDocumentosJudicialesPK = new ExpDocumentosJudicialesCortePorDocumentosJudicialesPK(persona.getId(), documentoJudicial.getId());
        this.persona = persona;
        this.documentosJudiciales = documentoJudicial;
        this.tipoParte = tipoParte;
    }
*/

    public ExpDocumentosJudicialesCortePorDocumentosJudicialesPK getDocumentosJudicialesCortePorDocumentosJudicialesPK() {
        return documentosJudicialesCortePorDocumentosJudicialesPK;
    }

    public DocumentosJudicialesCorte getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setExpDocumentosJudicialesCortePorDocumentosJudicialesPK(ExpDocumentosJudicialesCortePorDocumentosJudicialesPK documentosJudicialesCortePorDocumentosJudicialesPK) {
        this.documentosJudicialesCortePorDocumentosJudicialesPK = documentosJudicialesCortePorDocumentosJudicialesPK;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentosJudiciales) {
        this.documentoJudicial = documentosJudiciales;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentosJudicialesCortePorDocumentosJudicialesPK != null ? documentosJudicialesCortePorDocumentosJudicialesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpDocumentosJudicialesCortePorDocumentosJudiciales)) {
            return false;
        }
        ExpDocumentosJudicialesCortePorDocumentosJudiciales other = (ExpDocumentosJudicialesCortePorDocumentosJudiciales) object;
        if ((this.documentosJudicialesCortePorDocumentosJudicialesPK == null && other.documentosJudicialesCortePorDocumentosJudicialesPK != null) || (this.documentosJudicialesCortePorDocumentosJudicialesPK != null && !this.documentosJudicialesCortePorDocumentosJudicialesPK.equals(other.documentosJudicialesCortePorDocumentosJudicialesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpDocumentosJudicialesCortePorDocumentosJudiciales[ DocumentosJudicialesCortePorDocumentosJudicialesPK=" + documentosJudicialesCortePorDocumentosJudicialesPK + " ]";
    }
    
}
