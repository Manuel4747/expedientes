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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "antecedentes_roles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesRoles.findAll", query = "SELECT r FROM AntecedentesRoles r ORDER BY r.descripcion")
    , @NamedQuery(name = "AntecedentesRoles.findById", query = "SELECT r FROM AntecedentesRoles r WHERE r.id = :id")
    , @NamedQuery(name = "AntecedentesRoles.findButId", query = "SELECT r FROM AntecedentesRoles r WHERE r.id <> :id ORDER BY r.descripcion")
    , @NamedQuery(name = "AntecedentesRoles.findByDescripcion", query = "SELECT r FROM AntecedentesRoles r WHERE r.descripcion = :descripcion")})
public class AntecedentesRoles implements Serializable {

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
    @Transient
    private String estado;
    @Transient
    private boolean firmado;

    public AntecedentesRoles() {
    }

    public AntecedentesRoles(Integer id) {
        this.id = id;
    }

    public AntecedentesRoles(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }
    
    @XmlTransient
    public String getFirmadoString() {
        return firmado?"SI":"NO";
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
        if (!(object instanceof AntecedentesRoles)) {
            return false;
        }
        AntecedentesRoles other = (AntecedentesRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.AntecedentesRoles[ id=" + id + " ]";
    }
    
}
