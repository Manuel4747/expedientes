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
@Table(name = "exp_partes_por_documentos_judiciales_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPartesPorDocumentosJudicialesCorte.findAll", query = "SELECT r FROM ExpPartesPorDocumentosJudicialesCorte r")
  , @NamedQuery(name = "ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorte", query = "SELECT t FROM ExpPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte")
  , @NamedQuery(name = "ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicialCorteANDNombresApellidos", query = "SELECT t FROM ExpPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte AND t.nombresApellidos = :nombresApellidos")
  , @NamedQuery(name = "ExpPartesPorDocumentosJudicialesCorte.findByDocumentoJudicial", query = "SELECT t FROM ExpPartesPorDocumentosJudicialesCorte t WHERE t.documentoJudicial = :documentoJudicial")})
public class ExpPartesPorDocumentosJudicialesCorte implements Serializable {

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
    @JoinColumn(name = "tipo_parte_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposParteCorte tipoParteCorte;  
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "ocupacion_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpOcupacionesCorte ocupacionCorte;
    @JoinColumn(name = "estado_civil_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpEstadosCivilCorte estadoCivilCorte;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nro_documento")
    private String nroDocumento;
    @JoinColumn(name = "tipo_documento_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposDocumentoCorte tipoDocumentoCorte;
    @Basic(optional = true)
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @JoinColumn(name = "sexo_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpSexosCorte sexoCorte;
    @JoinColumn(name = "tipo_persona_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposPersonaCorte tipoPersonaCorte;

    public ExpPartesPorDocumentosJudicialesCorte() {
    }

    public ExpPartesPorDocumentosJudicialesCorte(Integer id) {
        this.id = id;
    }

    public ExpPartesPorDocumentosJudicialesCorte(Integer id, DocumentosJudicialesCorte documentoJudicialCorte, DocumentosJudiciales documentoJudicial, Personas personaAlta, Date fechaHoraAlta, ExpTiposParteCorte tipoParteCorte, Personas personaUltimoEstado, Date fechaHoraUltimoEstado, ExpOcupacionesCorte ocupacionCorte, ExpEstadosCivilCorte estadoCivilCorte, String nombresApellidos, String nroDocumento, ExpTiposDocumentoCorte tipoDocumentoCorte, Date fechaNacimiento, ExpSexosCorte sexoCorte, ExpTiposPersonaCorte tipoPersonaCorte) {
        this.id = id;
        this.documentoJudicialCorte = documentoJudicialCorte;
        this.documentoJudicial = documentoJudicial;
        this.personaAlta = personaAlta;
        this.fechaHoraAlta = fechaHoraAlta;
        this.tipoParteCorte = tipoParteCorte;
        this.personaUltimoEstado = personaUltimoEstado;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.ocupacionCorte = ocupacionCorte;
        this.estadoCivilCorte = estadoCivilCorte;
        this.nombresApellidos = nombresApellidos;
        this.nroDocumento = nroDocumento;
        this.tipoDocumentoCorte = tipoDocumentoCorte;
        this.fechaNacimiento = fechaNacimiento;
        this.sexoCorte = sexoCorte;
        this.tipoPersonaCorte = tipoPersonaCorte;
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

    public ExpTiposParteCorte getTipoParteCorte() {
        return tipoParteCorte;
    }

    public void setTipoParteCorte(ExpTiposParteCorte tipoParteCorte) {
        this.tipoParteCorte = tipoParteCorte;
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
    
    public ExpOcupacionesCorte getOcupacionCorte() {
        return ocupacionCorte;
    }

    public void setOcupacionCorte(ExpOcupacionesCorte ocupacionCorte) {
        this.ocupacionCorte = ocupacionCorte;
    }

    public ExpEstadosCivilCorte getEstadoCivilCorte() {
        return estadoCivilCorte;
    }

    public void setEstadoCivilCorte(ExpEstadosCivilCorte estadoCivilCorte) {
        this.estadoCivilCorte = estadoCivilCorte;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public ExpTiposDocumentoCorte getTipoDocumentoCorte() {
        return tipoDocumentoCorte;
    }

    public void setTipoDocumentoCorte(ExpTiposDocumentoCorte tipoDocumentoCorte) {
        this.tipoDocumentoCorte = tipoDocumentoCorte;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public ExpSexosCorte getSexoCorte() {
        return sexoCorte;
    }

    public void setSexoCorte(ExpSexosCorte sexoCorte) {
        this.sexoCorte = sexoCorte;
    }

    public ExpTiposPersonaCorte getTipoPersonaCorte() {
        return tipoPersonaCorte;
    }

    public void setTipoPersonaCorte(ExpTiposPersonaCorte tipoPersonaCorte) {
        this.tipoPersonaCorte = tipoPersonaCorte;
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
        if (!(object instanceof ExpPartesPorDocumentosJudicialesCorte)) {
            return false;
        }
        ExpPartesPorDocumentosJudicialesCorte other = (ExpPartesPorDocumentosJudicialesCorte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpPartesPorDocumentosJudicialesCorte[ id=" + id + " ]";
    }
    
}
