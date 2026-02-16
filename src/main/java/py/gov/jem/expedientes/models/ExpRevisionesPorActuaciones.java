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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_revisiones_por_actuaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpRevisionesPorActuaciones.findAll", query = "SELECT d FROM ExpRevisionesPorActuaciones d")
    , @NamedQuery(name = "ExpRevisionesPorActuaciones.findById", query = "SELECT d FROM ExpRevisionesPorActuaciones d WHERE d.id = :id")
    , @NamedQuery(name = "ExpRevisionesPorActuaciones.findByDocumentoJudicial", query = "SELECT o FROM ExpRevisionesPorActuaciones o WHERE o.documentoJudicial = :documentoJudicial ORDER BY o.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpRevisionesPorActuaciones.findByActuacion", query = "SELECT o FROM ExpRevisionesPorActuaciones o WHERE o.actuacion = :actuacion ORDER BY o.revisionPor.nombresApellidos, o.fechaHoraAlta")
    , @NamedQuery(name = "ExpRevisionesPorActuaciones.findByActuacionRevisionPorEstado", query = "SELECT o FROM ExpRevisionesPorActuaciones o WHERE o.actuacion = :actuacion AND o.revisionPor = :revisionPor AND o.estado = :estado")
    , @NamedQuery(name = "ExpRevisionesPorActuaciones.findByActuacionEstado", query = "SELECT o FROM ExpRevisionesPorActuaciones o WHERE o.actuacion = :actuacion AND o.estado = :estado ORDER BY o.revisionPor.nombresApellidos, o.fechaHoraAlta")})
public class ExpRevisionesPorActuaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActuaciones actuacion;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "revision_por", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas revisionPor;
    @Basic(optional = false)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Column(name = "fecha_hora_revision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRevision;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @Column(name = "revisado")
    private boolean revisado;


    public ExpRevisionesPorActuaciones() {
    }

    public ExpRevisionesPorActuaciones(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
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

    public Date getFechaHoraRevision() {
        return fechaHoraRevision;
    }

    public void setFechaHoraRevision(Date fechaHoraRevision) {
        this.fechaHoraRevision = fechaHoraRevision;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Personas getRevisionPor() {
        return revisionPor;
    }

    public void setRevisionPor(Personas revisionPor) {
        this.revisionPor = revisionPor;
    }

    public boolean isRevisado() {
        return revisado;
    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
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
        if (!(object instanceof ExpRevisionesPorActuaciones)) {
            return false;
        }
        ExpRevisionesPorActuaciones other = (ExpRevisionesPorActuaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpRevisionesPorActuaciones[ id=" + id + " ]";
    }
    
}
