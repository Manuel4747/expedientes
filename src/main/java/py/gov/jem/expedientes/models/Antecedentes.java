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
@Table(name = "antecedentes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Antecedentes.findAll", query = "SELECT r FROM Antecedentes r ORDER BY r.fechaHoraAlta DESC")
    , @NamedQuery(name = "Antecedentes.findById", query = "SELECT r FROM Antecedentes r WHERE r.id = :id")
    , @NamedQuery(name = "Antecedentes.findByHash", query = "SELECT r FROM Antecedentes r WHERE r.hash = :hash")
    , @NamedQuery(name = "Antecedentes.findByPersona", query = "SELECT r FROM Antecedentes r WHERE r.persona = :persona ORDER BY r.fechaHoraAlta DESC")
    , @NamedQuery(name = "Antecedentes.findByFechaHoraAlta", query = "SELECT r FROM Antecedentes r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Antecedentes.findByFechaHoraUltimoEstado", query = "SELECT r FROM Antecedentes r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class Antecedentes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "path_archivo")
    private String pathArchivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "codigo_archivo")
    private String codigoArchivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "hash")
    private String hash;
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
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "persona_generacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaGeneracion;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Column(name = "visto")
    private boolean visto;
    @Column(name = "fecha_hora_visto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisto;

    public Antecedentes() {
    }

    public Antecedentes(Integer id) {
        this.id = id;
    }

    public Antecedentes(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
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

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    public String getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(String codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Personas getPersonaGeneracion() {
        return personaGeneracion;
    }

    public void setPersonaGeneracion(Personas personaGeneracion) {
        this.personaGeneracion = personaGeneracion;
    }

    public Personas getPersonaBorrado() {
        return personaBorrado;
    }

    public void setPersonaBorrado(Personas personaBorrado) {
        this.personaBorrado = personaBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public Date getFechaHoraVisto() {
        return fechaHoraVisto;
    }

    public void setFechaHoraVisto(Date fechaHoraVisto) {
        this.fechaHoraVisto = fechaHoraVisto;
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
        if (!(object instanceof Antecedentes)) {
            return false;
        }
        Antecedentes other = (Antecedentes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.Antecedentes[ id=" + id + " ]";
    }
    
}
