/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Collection;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_estados_acta_sesion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpEstadosActaSesion.findAll", query = "SELECT e FROM ExpEstadosActaSesion e ORDER BY e.orden")
    , @NamedQuery(name = "ExpEstadosActaSesion.findByCodigo", query = "SELECT e FROM ExpEstadosActaSesion e WHERE e.codigo = :codigo ORDER BY e.orden")
    , @NamedQuery(name = "ExpEstadosActaSesion.findByRol", query = "SELECT e.estadoActaSesion FROM ExpEstadosActaSesionPorRoles e WHERE e.rol = :rol AND e.iteracion = :iteracion ORDER BY e.estadoActaSesion.orden")
    , @NamedQuery(name = "ExpEstadosActaSesion.findByOrden", query = "SELECT e FROM ExpEstadosActaSesion e WHERE e.orden = :orden")
    , @NamedQuery(name = "ExpEstadosActaSesion.findByMenorOrden", query = "SELECT e FROM ExpEstadosActaSesion e WHERE e.orden <= :orden ORDER BY e.orden")
    , @NamedQuery(name = "ExpEstadosActaSesion.findByDescripcion", query = "SELECT e FROM ExpEstadosActaSesion e WHERE e.descripcion = :descripcion")})
public class ExpEstadosActaSesion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = true)
    @Column(name = "orden")
    private Integer orden;

    public ExpEstadosActaSesion() {
    }

    public ExpEstadosActaSesion(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpEstadosActaSesion)) {
            return false;
        }
        ExpEstadosActaSesion other = (ExpEstadosActaSesion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpEstadosActaSesion[ codigo=" + codigo + " ]";
    }
    
}
