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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "subtipos_resolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubtiposResolucion.findAll", query = "SELECT t FROM SubtiposResolucion t ORDER BY t.descripcion")
    , @NamedQuery(name = "SubtiposResolucion.findById", query = "SELECT t FROM SubtiposResolucion t WHERE t.id = :id")
    , @NamedQuery(name = "SubtiposResolucion.findByTipoResolucion", query = "SELECT t FROM SubtiposResolucion t WHERE t.tipoResolucion = :tipoResolucion ORDER BY t.descripcion")
    , @NamedQuery(name = "SubtiposResolucion.findByDescripcion", query = "SELECT t FROM SubtiposResolucion t WHERE t.descripcion = :descripcion")
    , @NamedQuery(name = "SubtiposResolucion.findByFechaHoraAlta", query = "SELECT t FROM SubtiposResolucion t WHERE t.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "SubtiposResolucion.findByFechaHoraUltimoEstado", query = "SELECT t FROM SubtiposResolucion t WHERE t.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class SubtiposResolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "tipo_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposResolucion tipoResolucion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
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
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "subtipoResolucion")
    private Collection<Resoluciones> resolucionesCollection;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_corta")
    private String descripcionCorta;

    public SubtiposResolucion() {
    }

    public SubtiposResolucion(Integer id) {
        this.id = id;
    }

    public SubtiposResolucion(Integer id, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
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

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public TiposResolucion getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(TiposResolucion tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @XmlTransient
    public Collection<Resoluciones> getResolucionesCollection() {
        return resolucionesCollection;
    }

    public void setResolucionesCollection(Collection<Resoluciones> resolucionesCollection) {
        this.resolucionesCollection = resolucionesCollection;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubtiposResolucion)) {
            return false;
        }
        SubtiposResolucion other = (SubtiposResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (descripcion != null)?descripcion:"";
    }
    
}
