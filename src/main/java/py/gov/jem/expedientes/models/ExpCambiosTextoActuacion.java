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
@Table(name = "exp_cambios_texto_actuacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpCambiosTextoActuacion.findAll", query = "SELECT m FROM ExpCambiosTextoActuacion m")
    , @NamedQuery(name = "ExpCambiosTextoActuacion.findByActuacion", query = "SELECT m FROM ExpCambiosTextoActuacion m WHERE m.actuacion = :actuacion ORDER BY m.fechaHoraAlta DESC, m.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "ExpCambiosTextoActuacion.findById", query = "SELECT m FROM ExpCambiosTextoActuacion m WHERE m.id = :id")
    , @NamedQuery(name = "ExpCambiosTextoActuacion.findByFechaHoraAlta", query = "SELECT m FROM ExpCambiosTextoActuacion m WHERE m.fechaHoraAlta = :fechaHoraAlta")})
public class ExpCambiosTextoActuacion implements Serializable {

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
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "texto_original")
    private String textoOriginal;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "texto_final")
    private String textoFinal;

    public ExpCambiosTextoActuacion() {
    }

    public ExpCambiosTextoActuacion(Integer id) {
        this.id = id;
    }

    public ExpCambiosTextoActuacion(Integer id, Date fechaHoraAlta) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
    }
    
    public ExpCambiosTextoActuacion(Integer id, Date fechaHoraAlta, ExpActuaciones actuacion, Personas personaAlta, Personas personaUltimoEstado, Date fechaHoraUltimoEstado, String textoOriginal, String textoFinal){
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.actuacion = actuacion;
        this.personaAlta = personaAlta;
        this.personaUltimoEstado = personaUltimoEstado;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
        this.textoOriginal = textoOriginal;
        this.textoFinal = textoFinal;
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

    public String getTextoOriginal() {
        return textoOriginal;
    }

    public void setTextoOriginal(String textoOriginal) {
        this.textoOriginal = textoOriginal;
    }

    public String getTextoFinal() {
        return textoFinal;
    }

    public void setTextoFinal(String textoFinal) {
        this.textoFinal = textoFinal;
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
        if (!(object instanceof ExpCambiosTextoActuacion)) {
            return false;
        }
        ExpCambiosTextoActuacion other = (ExpCambiosTextoActuacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpCambiosTextoActuacion[ id=" + id + " ]";
    }
    
}
