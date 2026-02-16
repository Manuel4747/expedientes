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
@Table(name = "exp_personas_acusacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPersonasAcusacion.findAll", query = "SELECT p FROM ExpPersonasAcusacion p ORDER BY p.nombresApellidos")
  , @NamedQuery(name = "ExpPersonasAcusacion.findByCiEstado", query = "SELECT p FROM ExpPersonasAcusacion p WHERE p.ci = :ci and p.estado = :estado")})
public class ExpPersonasAcusacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne
    private Personas personaAlta;
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne
    private Personas personaUltimoEstado;
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaBorrado;

    public ExpPersonasAcusacion() {
    }

    public ExpPersonasAcusacion(Integer id) {
        this.id = id;
    }
    
    public ExpPersonasAcusacion(ExpPersonasAcusacion per) {
        this.id = per.id;
        this.nombresApellidos = per.nombresApellidos;
        this.ci = per.ci;
        this.estado = per.estado;
        this.fechaHoraAlta = per.fechaHoraAlta;
        this.personaAlta = per.personaAlta;
        this.fechaHoraUltimoEstado = per.fechaHoraUltimoEstado;
        this.personaUltimoEstado = per.personaUltimoEstado;
        this.fechaHoraBorrado = per.fechaHoraBorrado;
        this.personaBorrado = per.personaBorrado;
    }

    public ExpPersonasAcusacion(Integer id, String nombresApellidos, String ci, String estado, Date fechaHoraAlta) {
        this.id = id;
        this.nombresApellidos = nombresApellidos;
        this.ci = ci;
        this.estado = estado;
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Personas getPersonaBorrado() {
        return personaBorrado;
    }

    public void setPersonaBorrado(Personas personaBorrado) {
        this.personaBorrado = personaBorrado;
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
        if (!(object instanceof ExpPersonasAcusacion)) {
            return false;
        }
        ExpPersonasAcusacion other = (ExpPersonasAcusacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombresApellidos==null?"":nombresApellidos;
    }
    
}
