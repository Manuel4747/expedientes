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
import javax.persistence.Lob;
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
@Table(name = "exp_auditorias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpAuditorias.findAll", query = "SELECT r FROM ExpAuditorias r")
    , @NamedQuery(name = "ExpAuditorias.findByAuditTimestamp", query = "SELECT d FROM ExpAuditorias d WHERE d.auditTimestamp >= :fechaDesde ORDER BY d.auditTimestamp DESC, d.auditTimestamp DESC")})
public class ExpAuditorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "auditId")
    private Integer auditId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "auditTimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditTimestamp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "accion")
    private String accion;
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "causa")
    private String causa;
    @Basic(optional = true)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    
    public ExpAuditorias() {
    }

    public ExpAuditorias(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public Date getAuditTimestamp() {
        return auditTimestamp;
    }

    public void setAuditTimestamp(Date auditTimestamp) {
        this.auditTimestamp = auditTimestamp;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
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
        if (!(object instanceof ExpAuditorias)) {
            return false;
        }
        ExpAuditorias other = (ExpAuditorias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpOrdenesDia[ id=" + id + " ]";
    }
    
}
