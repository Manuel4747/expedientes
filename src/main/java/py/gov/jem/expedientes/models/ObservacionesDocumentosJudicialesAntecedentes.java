/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "observaciones_documentos_judiciales_antecedentes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObservacionesDocumentosJudicialesAntecedentes.findAll", query = "SELECT o FROM ObservacionesDocumentosJudicialesAntecedentes o")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesAntecedentes.findOrdered", query = "SELECT o FROM ObservacionesDocumentosJudicialesAntecedentes o ORDER BY o.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesAntecedentes.findById", query = "SELECT o FROM ObservacionesDocumentosJudicialesAntecedentes o WHERE o.id = :id")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesAntecedentes.findByDocumentoJudicial", query = "SELECT o FROM ObservacionesDocumentosJudicialesAntecedentes o WHERE o.documentoJudicial = :documentoJudicial ORDER BY o.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesAntecedentes.findByObservacion", query = "SELECT o FROM ObservacionesDocumentosJudicialesAntecedentes o WHERE o.observacion = :observacion")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesAntecedentes.findByFechaHoraAlta", query = "SELECT o FROM ObservacionesDocumentosJudicialesAntecedentes o WHERE o.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ObservacionesDocumentosJudicialesAntecedentes.findByFechaHoraUltimoEstado", query = "SELECT o FROM ObservacionesDocumentosJudicialesAntecedentes o WHERE o.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ObservacionesDocumentosJudicialesAntecedentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "observacion")
    private String observacion;
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
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "observacionDocumentoJudicialAntecedente")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection;

    public ObservacionesDocumentosJudicialesAntecedentes() {
    }

    public ObservacionesDocumentosJudicialesAntecedentes(Integer id) {
        this.id = id;
    }

    public ObservacionesDocumentosJudicialesAntecedentes(Integer id, String observacion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.observacion = observacion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    @XmlTransient
    public String getObservacionString() {
        return observacion.replace("\n", "<br />");
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection() {
        return documentosJudicialesCollection;
    }

    public void setDocumentosJudicialesCollection(Collection<DocumentosJudiciales> documentosJudicialesCollection) {
        this.documentosJudicialesCollection = documentosJudicialesCollection;
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
        if (!(object instanceof ObservacionesDocumentosJudicialesAntecedentes)) {
            return false;
        }
        ObservacionesDocumentosJudicialesAntecedentes other = (ObservacionesDocumentosJudicialesAntecedentes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ObservacionesDocumentosJudicialesAntecedentes[ id=" + id + " ]";
    }
    
}
