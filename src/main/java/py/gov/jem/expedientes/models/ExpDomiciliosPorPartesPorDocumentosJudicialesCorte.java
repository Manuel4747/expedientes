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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_domicilios_por_partes_por_documentos_judiciales_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findAll", query = "SELECT r FROM ExpDomiciliosPorPartesPorDocumentosJudicialesCorte r")
  , @NamedQuery(name = "ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", query = "SELECT t FROM ExpDomiciliosPorPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte")
  , @NamedQuery(name = "ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDDescripcionTipoDomicilioANDLocalidadANDCalle", query = "SELECT t FROM ExpDomiciliosPorPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte AND t.descripcionTipoDomicilio = :descripcionTipoDomicilio AND t.localidad = :localidad AND t.calle = :calle")
  , @NamedQuery(name = "ExpDomiciliosPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicial", query = "SELECT t FROM ExpDomiciliosPorPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicial = :documentoJudicial")})
public class ExpDomiciliosPorPartesPorDocumentosJudicialesCorte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_judicial_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudicialesCorte documentoJudicialCorte;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
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
    @JoinColumn(name = "parte_por_documento_judicial_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpPartesPorDocumentosJudicialesCorte partePorDocumentoJudicialCorte;
    @Size(max = 200)
    @Column(name = "descripcionTipoDomicilio")
    private String descripcionTipoDomicilio;
    @Size(max = 200)
    @Column(name = "localidad")
    private String localidad;
    @Size(max = 200)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 200)
    @Column(name = "email")
    private String email;
    @Size(max = 200)
    @Column(name = "calle")
    private String calle;

    public ExpDomiciliosPorPartesPorDocumentosJudicialesCorte() {
    }

    public ExpDomiciliosPorPartesPorDocumentosJudicialesCorte(Integer id) {
        this.id = id;
    }

    public ExpDomiciliosPorPartesPorDocumentosJudicialesCorte(Integer id, ExpPartesPorDocumentosJudicialesCorte partePorDocumentoJudicialCorte, DocumentosJudicialesCorte documentoJudicialCorte, DocumentosJudiciales documentoJudicial, Personas personaAlta, Date fechaHoraAlta, Personas personaUltimoEstado, Date fechaHoraUltimoEstado, String localidad, String telefono, String email, String calle, String descripcionTipoDomicilio) {
        this.id = id;
        this.documentoJudicialCorte = documentoJudicialCorte;
        this.documentoJudicial = documentoJudicial;
        this.personaAlta = personaAlta;
        this.fechaHoraAlta = fechaHoraAlta;
        this.personaUltimoEstado = personaUltimoEstado;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.localidad = localidad;
        this.telefono = telefono;
        this.email = email;
        this.calle = calle;
        this.descripcionTipoDomicilio = descripcionTipoDomicilio;
        this.partePorDocumentoJudicialCorte = partePorDocumentoJudicialCorte;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getDescripcionTipoDomicilio() {
        return descripcionTipoDomicilio;
    }

    public void setDescripcionTipoDomicilio(String descripcionTipoDomicilio) {
        this.descripcionTipoDomicilio = descripcionTipoDomicilio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpDomiciliosPorPartesPorDocumentosJudicialesCorte)) {
            return false;
        }
        ExpDomiciliosPorPartesPorDocumentosJudicialesCorte other = (ExpDomiciliosPorPartesPorDocumentosJudicialesCorte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpDomiciliosPorPartesPorDocumentosJudicialesCorte[ id=" + id + " ]";
    }
    
}
