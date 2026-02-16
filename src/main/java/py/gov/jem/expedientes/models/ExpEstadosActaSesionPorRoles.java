/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_estados_acta_sesion_por_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpEstadosActaSesionPorRoles.findAll", query = "SELECT r FROM ExpEstadosActaSesionPorRoles r")
    , @NamedQuery(name = "ExpEstadosActaSesionPorRoles.findById", query = "SELECT r FROM ExpEstadosActaSesionPorRoles r WHERE r.id = :id")
    , @NamedQuery(name = "ExpEstadosActaSesionPorRoles.findByRol", query = "SELECT r FROM ExpEstadosActaSesionPorRoles r WHERE r.rol = :rol")
    , @NamedQuery(name = "ExpEstadosActaSesionPorRoles.findByRolEstadoActaSesionIteracion", query = "SELECT r FROM ExpEstadosActaSesionPorRoles r WHERE r.rol = :rol and r.estadoActaSesion = :estadoActaSesion and r.iteracion = :iteracion")
    , @NamedQuery(name = "ExpEstadosActaSesionPorRoles.findByEstadoActaSesion", query = "SELECT r FROM ExpEstadosActaSesionPorRoles r WHERE r.estadoActaSesion = :estadoActaSesion")})
public class ExpEstadosActaSesionPorRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "estado_acta_sesion", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private ExpEstadosActaSesion estadoActaSesion;
    @JoinColumn(name = "rol", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private AntecedentesRoles rol;
    @Column(name = "deshabilitar")
    private boolean deshabilitar;
    @Column(name = "iteracion")
    private Integer iteracion;

    public ExpEstadosActaSesionPorRoles() {
    }

    public ExpEstadosActaSesionPorRoles(Integer id) {
        this.id = id;
    }

    public ExpEstadosActaSesionPorRoles(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ExpEstadosActaSesion getEstadoActaSesion() {
        return estadoActaSesion;
    }

    public void setEstadoActaSesion(ExpEstadosActaSesion estadoActaSesion) {
        this.estadoActaSesion = estadoActaSesion;
    }

    public AntecedentesRoles getRol() {
        return rol;
    }

    public void setRol(AntecedentesRoles rol) {
        this.rol = rol;
    }

    public boolean isDeshabilitar() {
        return deshabilitar;
    }

    public void setDeshabilitar(boolean deshabilitar) {
        this.deshabilitar = deshabilitar;
    }

    public Integer getIteracion() {
        return iteracion;
    }

    public void setIteracion(Integer iteracion) {
        this.iteracion = iteracion;
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
        if (!(object instanceof ExpEstadosActaSesionPorRoles)) {
            return false;
        }
        ExpEstadosActaSesionPorRoles other = (ExpEstadosActaSesionPorRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpEstadosActaSesionPorRoles[ id=" + id + " ]";
    }
    
}
