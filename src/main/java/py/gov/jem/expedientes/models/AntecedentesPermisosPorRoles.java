/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "antecedentes_permisos_por_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesPermisosPorRoles.findAll", query = "SELECT p FROM AntecedentesPermisosPorRoles p")
    , @NamedQuery(name = "AntecedentesPermisosPorRoles.findByPermiso", query = "SELECT p FROM AntecedentesPermisosPorRoles p WHERE p.antecedentesPermisosPorRolesPK.permiso = :permiso")
    , @NamedQuery(name = "AntecedentesPermisosPorRoles.findByRol", query = "SELECT p FROM AntecedentesPermisosPorRoles p WHERE p.antecedentesPermisosPorRolesPK.rol = :rol")})
public class AntecedentesPermisosPorRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AntecedentesPermisosPorRolesPK antecedentesPermisosPorRolesPK;
    @JoinColumn(name = "permiso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AntecedentesPermisos permisos;
    @JoinColumn(name = "rol", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Roles roles;

    public AntecedentesPermisosPorRoles() {
    }

    public AntecedentesPermisosPorRoles(AntecedentesPermisosPorRolesPK antecedentesPermisosPorRolesPK) {
        this.antecedentesPermisosPorRolesPK = antecedentesPermisosPorRolesPK;
    }

    public AntecedentesPermisosPorRoles(int permiso, int rol) {
        this.antecedentesPermisosPorRolesPK = new AntecedentesPermisosPorRolesPK(permiso, rol);
    }

    public AntecedentesPermisosPorRolesPK getAntecedentesPermisosPorRolesPK() {
        return antecedentesPermisosPorRolesPK;
    }

    public void setAntecedentesPermisosPorRolesPK(AntecedentesPermisosPorRolesPK antecedentesPermisosPorRolesPK) {
        this.antecedentesPermisosPorRolesPK = antecedentesPermisosPorRolesPK;
    }

    public AntecedentesPermisos getFormPermisos() {
        return permisos;
    }

    public void setFormPermisos(AntecedentesPermisos permisos) {
        this.permisos = permisos;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (antecedentesPermisosPorRolesPK != null ? antecedentesPermisosPorRolesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecedentesPermisosPorRoles)) {
            return false;
        }
        AntecedentesPermisosPorRoles other = (AntecedentesPermisosPorRoles) object;
        if ((this.antecedentesPermisosPorRolesPK == null && other.antecedentesPermisosPorRolesPK != null) || (this.antecedentesPermisosPorRolesPK != null && !this.antecedentesPermisosPorRolesPK.equals(other.antecedentesPermisosPorRolesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.AntecedentesPermisosPorRoles[ antecedentesPermisosPorRolesPK=" + antecedentesPermisosPorRolesPK + " ]";
    }
    
}
