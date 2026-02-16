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
@Table(name = "exp_formatos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpFormatos.findAll", query = "SELECT t FROM ExpFormatos t order by t.fechaHoraAlta desc")
    , @NamedQuery(name = "ExpFormatos.findById", query = "SELECT t FROM ExpFormatos t WHERE t.id = :id")
    , @NamedQuery(name = "ExpFormatos.findByDescripcion", query = "SELECT t FROM ExpFormatos t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "ExpFormatos.findByTipoActuacion", query = "SELECT t FROM ExpFormatos t WHERE t.tipoActuacion = :tipoActuacion ORDER BY t.descripcion")
    , @NamedQuery(name = "ExpFormatos.findByIdTipoActuacion", query = "SELECT t FROM ExpFormatos t WHERE t.tipoActuacion.id = :tipoActuacion ORDER BY t.descripcion")
    , @NamedQuery(name = "ExpFormatos.findByFechaHoraAlta", query = "SELECT t FROM ExpFormatos t WHERE t.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ExpFormatos.findByFechaHoraUltimoEstado", query = "SELECT t FROM ExpFormatos t WHERE t.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ExpFormatos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tipo_actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposActuacion tipoActuacion;
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
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "archivo")
    private String archivo;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "texto")
    private String texto;

    public ExpFormatos() {
    }

    public ExpFormatos(Integer id) {
        this.id = id;
    }

    public ExpFormatos(Integer id, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public ExpTiposActuacion getTipoActuacion() {
        return tipoActuacion;
    }

    public void setTipoActuacion(ExpTiposActuacion tipoActuacion) {
        this.tipoActuacion = tipoActuacion;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
        if (!(object instanceof ExpFormatos)) {
            return false;
        }
        ExpFormatos other = (ExpFormatos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (descripcion!=null)?descripcion:"";
    }
    
}
