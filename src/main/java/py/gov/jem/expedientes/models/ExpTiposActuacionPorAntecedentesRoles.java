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
@Table(name = "exp_tipos_actuacion_por_antecedentes_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpTiposActuacionPorAntecedentesRoles.findAll", query = "SELECT r FROM ExpTiposActuacionPorAntecedentesRoles r")
    , @NamedQuery(name = "ExpTiposActuacionPorAntecedentesRoles.findByRol", query = "SELECT r FROM ExpTiposActuacionPorAntecedentesRoles r WHERE r.tiposActuacionPorAntecedentesRolesPK.rol = :rol AND r.estado = :estado")
    , @NamedQuery(name = "ExpTiposActuacionPorAntecedentesRoles.findByTipoActuacionEstado", query = "SELECT r FROM ExpTiposActuacionPorAntecedentesRoles r WHERE r.tiposActuacionPorAntecedentesRolesPK.tipoActuacion = :tipoActuacion AND r.estado = :estado")})
public class ExpTiposActuacionPorAntecedentesRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpTiposActuacionPorAntecedentesRolesPK tiposActuacionPorAntecedentesRolesPK;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @JoinColumn(name = "tipoActuacion", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExpTiposActuacion tipoActuacion;
    @JoinColumn(name = "rol", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AntecedentesRoles rol;

    public ExpTiposActuacionPorAntecedentesRoles() {
    }

    public ExpTiposActuacionPorAntecedentesRoles(ExpTiposActuacionPorAntecedentesRolesPK tiposActuacionPorAntecedentesRolesPK) {
        this.tiposActuacionPorAntecedentesRolesPK = tiposActuacionPorAntecedentesRolesPK;
    }

    public ExpTiposActuacionPorAntecedentesRoles(int rol, int tipoActuacion) {
        this.tiposActuacionPorAntecedentesRolesPK = new ExpTiposActuacionPorAntecedentesRolesPK(rol, tipoActuacion);
    }

    public ExpTiposActuacionPorAntecedentesRolesPK getExpTiposActuacionPorAntecedentesRolesPK() {
        return tiposActuacionPorAntecedentesRolesPK;
    }

    public void setExpTiposActuacionPorAntecedentesRolesPK(ExpTiposActuacionPorAntecedentesRolesPK tiposActuacionPorAntecedentesRolesPK) {
        this.tiposActuacionPorAntecedentesRolesPK = tiposActuacionPorAntecedentesRolesPK;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public ExpTiposActuacionPorAntecedentesRolesPK getTiposActuacionPorAntecedentesRolesPK() {
        return tiposActuacionPorAntecedentesRolesPK;
    }

    public void setTiposActuacionPorAntecedentesRolesPK(ExpTiposActuacionPorAntecedentesRolesPK tiposActuacionPorAntecedentesRolesPK) {
        this.tiposActuacionPorAntecedentesRolesPK = tiposActuacionPorAntecedentesRolesPK;
    }

    public ExpTiposActuacion getTipoActuacion() {
        return tipoActuacion;
    }

    public void setTipoActuacion(ExpTiposActuacion tipoActuacion) {
        this.tipoActuacion = tipoActuacion;
    }

    public AntecedentesRoles getRol() {
        return rol;
    }

    public void setRol(AntecedentesRoles rol) {
        this.rol = rol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tiposActuacionPorAntecedentesRolesPK != null ? tiposActuacionPorAntecedentesRolesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpTiposActuacionPorAntecedentesRoles)) {
            return false;
        }
        ExpTiposActuacionPorAntecedentesRoles other = (ExpTiposActuacionPorAntecedentesRoles) object;
        if ((this.tiposActuacionPorAntecedentesRolesPK == null && other.tiposActuacionPorAntecedentesRolesPK != null) || (this.tiposActuacionPorAntecedentesRolesPK != null && !this.tiposActuacionPorAntecedentesRolesPK.equals(other.tiposActuacionPorAntecedentesRolesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpTiposActuacionPorAntecedentesRoles[ tiposActuacionPorAntecedentesRolesPK=" + tiposActuacionPorAntecedentesRolesPK + " ]";
    }
    
}
