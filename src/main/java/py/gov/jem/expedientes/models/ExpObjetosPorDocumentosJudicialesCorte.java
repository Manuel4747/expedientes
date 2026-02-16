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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_objetos_por_documentos_judiciales_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpObjetosPorDocumentosJudicialesCorte.findAll", query = "SELECT r FROM ExpObjetosPorDocumentosJudicialesCorte r")
  , @NamedQuery(name = "ExpObjetosPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", query = "SELECT t FROM ExpObjetosPorDocumentosJudicialesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte")
  , @NamedQuery(name = "ExpObjetosPorDocumentosJudicialesCorte.findByDocumentoJudicial", query = "SELECT t FROM ExpObjetosPorDocumentosJudicialesCorte t WHERE t.documentoJudicial = :documentoJudicial")
})
public class ExpObjetosPorDocumentosJudicialesCorte implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpObjetosPorDocumentosJudicialesCortePK personasPorDocumentosJudicialesCortePK;
    @JoinColumn(name = "documento_judicial_corte", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentosJudicialesCorte documentoJudicialCorte;
    @JoinColumn(name = "objeto_corte", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExpObjetosCorte objetoCorte;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @Basic(optional = false)
    @Column(name = "principal")
    private boolean principal;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "grado_realizacion")
    private String gradoRealizacion;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;

    public ExpObjetosPorDocumentosJudicialesCorte() {
    }
    
    public ExpObjetosPorDocumentosJudicialesCorte(ExpObjetosPorDocumentosJudicialesCortePK personasPorDocumentosJudicialesCortePK, DocumentosJudicialesCorte documentoJudicialCorte, DocumentosJudiciales documentoJudicial, boolean principal, String gradoRealizacion, ExpObjetosCorte objetoCorte, Personas personaAlta, Date fechaHoraAlta, Personas personaUltimoEstado, Date fechaHoraUltimoEstado) {
        this.personasPorDocumentosJudicialesCortePK = personasPorDocumentosJudicialesCortePK;
        this.documentoJudicialCorte = documentoJudicialCorte;
        this.documentoJudicial = documentoJudicial;
        this.principal = principal;
        this.gradoRealizacion = gradoRealizacion;
        this.objetoCorte = objetoCorte;
        this.personaAlta = personaAlta;
        this.fechaHoraAlta = fechaHoraAlta;
        this.personaUltimoEstado = personaUltimoEstado;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public ExpObjetosPorDocumentosJudicialesCorte(ExpObjetosPorDocumentosJudicialesCortePK personasPorDocumentosJudicialesCortePK) {
        this.personasPorDocumentosJudicialesCortePK = personasPorDocumentosJudicialesCortePK;
    }

    public ExpObjetosPorDocumentosJudicialesCorte(ExpObjetosPorDocumentosJudicialesCortePK personasPorDocumentosJudicialesCortePK, ExpObjetosCorte objetoCorte, DocumentosJudicialesCorte documentoJudicialCorte) {
        this.personasPorDocumentosJudicialesCortePK = personasPorDocumentosJudicialesCortePK;
        this.objetoCorte = objetoCorte;
        this.documentoJudicialCorte = documentoJudicialCorte;
    }

    public ExpObjetosPorDocumentosJudicialesCorte(ExpObjetosPorDocumentosJudicialesCortePK personasPorDocumentosJudicialesCortePK, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.personasPorDocumentosJudicialesCortePK = personasPorDocumentosJudicialesCortePK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public ExpObjetosPorDocumentosJudicialesCorte(int personaCorte, int documentoJudicialCorte) {
        this.personasPorDocumentosJudicialesCortePK = new ExpObjetosPorDocumentosJudicialesCortePK(personaCorte, documentoJudicialCorte);
    }

    public ExpObjetosPorDocumentosJudicialesCortePK getPartesPorDocumentosJudicialesCortePK() {
        return personasPorDocumentosJudicialesCortePK;
    }

    public void setPartesPorDocumentosJudicialesPK(ExpObjetosPorDocumentosJudicialesCortePK personasPorDocumentosJudicialesCortePK) {
        this.personasPorDocumentosJudicialesCortePK = personasPorDocumentosJudicialesCortePK;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
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

    public ExpObjetosPorDocumentosJudicialesCortePK getPersonasPorDocumentosJudicialesCortePK() {
        return personasPorDocumentosJudicialesCortePK;
    }

    public void setPersonasPorDocumentosJudicialesCortePK(ExpObjetosPorDocumentosJudicialesCortePK personasPorDocumentosJudicialesCortePK) {
        this.personasPorDocumentosJudicialesCortePK = personasPorDocumentosJudicialesCortePK;
    }

    public DocumentosJudicialesCorte getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setDocumentoJudicialCorte(DocumentosJudicialesCorte documentoJudicialCorte) {
        this.documentoJudicialCorte = documentoJudicialCorte;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public ExpObjetosCorte getObjetoCorte() {
        return objetoCorte;
    }

    public void setObjetoCorte(ExpObjetosCorte objetoCorte) {
        this.objetoCorte = objetoCorte;
    }

    public String getGradoRealizacion() {
        return gradoRealizacion;
    }

    public void setGradoRealizacion(String gradoRealizacion) {
        this.gradoRealizacion = gradoRealizacion;
    }

    @XmlTransient
    public String getPrincipalString() {
        return principal?"SI":"NO";
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personasPorDocumentosJudicialesCortePK != null ? personasPorDocumentosJudicialesCortePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpObjetosPorDocumentosJudicialesCorte)) {
            return false;
        }
        ExpObjetosPorDocumentosJudicialesCorte other = (ExpObjetosPorDocumentosJudicialesCorte) object;
        if ((this.personasPorDocumentosJudicialesCortePK == null && other.personasPorDocumentosJudicialesCortePK != null) || (this.personasPorDocumentosJudicialesCortePK != null && !this.personasPorDocumentosJudicialesCortePK.equals(other.personasPorDocumentosJudicialesCortePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpObjetosPorDocumentosJudicialesCorte[ personasPorDocumentosJudicialesCortePK=" + personasPorDocumentosJudicialesCortePK + " ]";
    }
    
}
