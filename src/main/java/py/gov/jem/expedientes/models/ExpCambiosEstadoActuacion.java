/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_cambios_estado_actuacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpCambiosEstadoActuacion.findAll", query = "SELECT m FROM ExpCambiosEstadoActuacion m")
    , @NamedQuery(name = "ExpCambiosEstadoActuacion.findByActuacion", query = "SELECT m FROM ExpCambiosEstadoActuacion m WHERE m.actuacion = :actuacion ORDER BY m.fechaHoraAlta DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ExpCambiosEstadoActuacion.findById", query = "SELECT m FROM ExpCambiosEstadoActuacion m WHERE m.id = :id")
    , @NamedQuery(name = "ExpCambiosEstadoActuacion.findByFechaHoraAlta", query = "SELECT m FROM ExpCambiosEstadoActuacion m WHERE m.fechaHoraAlta = :fechaHoraAlta")})
public class ExpCambiosEstadoActuacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "actuacion", referencedColumnName = "id")
    @ManyToOne
    private ExpActuaciones actuacion;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "estado_original", referencedColumnName = "codigo")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = true)
    private ExpEstadosActuacion estadoOriginal;
    @JoinColumn(name = "estado_final", referencedColumnName = "codigo")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ExpEstadosActuacion estadoFinal;

    public ExpCambiosEstadoActuacion() {
    }

    public ExpCambiosEstadoActuacion(Integer id) {
        this.id = id;
    }

    public ExpCambiosEstadoActuacion(Integer id, Date fechaHoraAlta, ExpActuaciones actuacion, Personas personaAlta, Personas personaUltimoEstado, Date fechaHoraUltimoEstado, ExpEstadosActuacion estadoOriginal, ExpEstadosActuacion estadoFinal) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.actuacion = actuacion;
        this.personaAlta = personaAlta;
        this.personaUltimoEstado = personaUltimoEstado;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.estadoOriginal = estadoOriginal;
        this.estadoFinal = estadoFinal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public ExpEstadosActuacion getEstadoOriginal() {
        return estadoOriginal;
    }

    public void setEstadoOriginal(ExpEstadosActuacion estadoOriginal) {
        this.estadoOriginal = estadoOriginal;
    }

    public ExpEstadosActuacion getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(ExpEstadosActuacion estadoFinal) {
        this.estadoFinal = estadoFinal;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpCambiosEstadoActuacion)) {
            return false;
        }
        ExpCambiosEstadoActuacion other = (ExpCambiosEstadoActuacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpCambiosEstadoActuacion[ id=" + id + " ]";
    }
    
}
