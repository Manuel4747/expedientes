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
@Table(name = "exp_estados_actuacion_por_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpEstadosActuacionPorRoles.findAll", query = "SELECT r FROM ExpEstadosActuacionPorRoles r")
    , @NamedQuery(name = "ExpEstadosActuacionPorRoles.findById", query = "SELECT r FROM ExpEstadosActuacionPorRoles r WHERE r.id = :id")
    , @NamedQuery(name = "ExpEstadosActuacionPorRoles.findByRol", query = "SELECT r FROM ExpEstadosActuacionPorRoles r WHERE r.rol = :rol")
    , @NamedQuery(name = "ExpEstadosActuacionPorRoles.findByRolEstadoActuacionIteracion", query = "SELECT r FROM ExpEstadosActuacionPorRoles r WHERE r.rol = :rol and r.estadoActuacion = :estadoActuacion and r.iteracion = :iteracion AND r.tipoCircuitoFirma = :tipoCircuitoFirma")
    , @NamedQuery(name = "ExpEstadosActuacionPorRoles.findByEstadoActuacion", query = "SELECT r FROM ExpEstadosActuacionPorRoles r WHERE r.estadoActuacion = :estadoActuacion")})
public class ExpEstadosActuacionPorRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "estado_actuacion", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private ExpEstadosActuacion estadoActuacion;
    @JoinColumn(name = "rol", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private AntecedentesRoles rol;
    @Column(name = "deshabilitar")
    private boolean deshabilitar;
    @Column(name = "iteracion")
    private Integer iteracion;
    @Basic(optional = true)
    @Column(name = "tipo_circuito_firma")
    private Integer tipoCircuitoFirma;


    public ExpEstadosActuacionPorRoles() {
    }

    public ExpEstadosActuacionPorRoles(Integer id) {
        this.id = id;
    }

    public ExpEstadosActuacionPorRoles(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ExpEstadosActuacion getEstadoActuacion() {
        return estadoActuacion;
    }

    public void setEstadoActuacion(ExpEstadosActuacion estadoActuacion) {
        this.estadoActuacion = estadoActuacion;
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

    public Integer getTipoCircuitoFirma() {
        return tipoCircuitoFirma;
    }

    public void setTipoCircuitoFirma(Integer tipoCircuitoFirma) {
        this.tipoCircuitoFirma = tipoCircuitoFirma;
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
        if (!(object instanceof ExpEstadosActuacionPorRoles)) {
            return false;
        }
        ExpEstadosActuacionPorRoles other = (ExpEstadosActuacionPorRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpEstadosActuacionPorRoles[ id=" + id + " ]";
    }
    
}
