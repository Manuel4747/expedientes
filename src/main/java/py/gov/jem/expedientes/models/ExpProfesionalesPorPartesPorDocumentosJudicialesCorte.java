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
@Table(name = "exp_profesionales_por_partes_por_documentos_judiciales_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findAll", query = "SELECT r FROM ExpProfesionalesPorPartesPorDocumentosJudicialesCorte r")
  , @NamedQuery(name = "ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", query = "SELECT t FROM ExpProfesionalesPorPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte")
  , @NamedQuery(name = "ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDTipoProfesionalANDNombreProfesional", query = "SELECT t FROM ExpProfesionalesPorPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte AND t.tipoProfesional = :tipoProfesional AND t.nombreProfesional = :nombreProfesional")
  , @NamedQuery(name = "ExpProfesionalesPorPartesPorDocumentosJudicialesCorte.findByDocumentoJudicial", query = "SELECT t FROM ExpProfesionalesPorPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicial = :documentoJudicial")})
public class ExpProfesionalesPorPartesPorDocumentosJudicialesCorte implements Serializable {

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
    @Size(max = 200)
    @Column(name = "tipo_profesional")
    private String tipoProfesional;
    @Size(max = 200)
    @Column(name = "nombre_profesional")
    private String nombreProfesional;
    @Size(max = 200)
    @Column(name = "representacion")
    private String representacion;
    @Size(max = 200)
    @Column(name = "profesional_habilitado")
    private String profesionalHabilitado;

    public ExpProfesionalesPorPartesPorDocumentosJudicialesCorte() {
    }

    public ExpProfesionalesPorPartesPorDocumentosJudicialesCorte(Integer id) {
        this.id = id;
    }

    public ExpProfesionalesPorPartesPorDocumentosJudicialesCorte(Integer id, DocumentosJudicialesCorte documentoJudicialCorte, DocumentosJudiciales documentoJudicial, Personas personaAlta, Date fechaHoraAlta, Personas personaUltimoEstado, Date fechaHoraUltimoEstado, String tipoProfesional, String nombreProfesional, String representacion, String profesionalHabilitado) {
        this.id = id;
        this.documentoJudicialCorte = documentoJudicialCorte;
        this.documentoJudicial = documentoJudicial;
        this.personaAlta = personaAlta;
        this.fechaHoraAlta = fechaHoraAlta;
        this.personaUltimoEstado = personaUltimoEstado;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.tipoProfesional = tipoProfesional;
        this.nombreProfesional = nombreProfesional;
        this.representacion = representacion;
        this.profesionalHabilitado = profesionalHabilitado;
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

    public String getTipoProfesional() {
        return tipoProfesional;
    }

    public void setTipoProfesional(String tipoProfesional) {
        this.tipoProfesional = tipoProfesional;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public void setNombreProfesional(String nombreProfesional) {
        this.nombreProfesional = nombreProfesional;
    }

    public String getRepresentacion() {
        return representacion;
    }

    public void setRepresentacion(String representacion) {
        this.representacion = representacion;
    }

    public String getProfesionalHabilitado() {
        return profesionalHabilitado;
    }

    public void setProfesionalHabilitado(String profesionalHabilitado) {
        this.profesionalHabilitado = profesionalHabilitado;
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
        if (!(object instanceof ExpProfesionalesPorPartesPorDocumentosJudicialesCorte)) {
            return false;
        }
        ExpProfesionalesPorPartesPorDocumentosJudicialesCorte other = (ExpProfesionalesPorPartesPorDocumentosJudicialesCorte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpProfesionalesPorPartesPorDocumentosJudicialesCorte[ id=" + id + " ]";
    }
    
}
