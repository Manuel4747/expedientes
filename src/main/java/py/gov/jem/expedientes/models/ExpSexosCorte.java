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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_sexos_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpSexosCorte.findAll", query = "SELECT r FROM ExpSexosCorte r")
    , @NamedQuery(name = "ExpSexosCorte.findById", query = "SELECT r FROM ExpSexosCorte r WHERE r.id = :id")
    , @NamedQuery(name = "ExpSexosCorte.findByDescripcion", query = "SELECT r FROM ExpSexosCorte r WHERE r.descripcion = :descripcion")})
public class ExpSexosCorte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;

    public ExpSexosCorte() {
    }

    public ExpSexosCorte(Integer id) {
        this.id = id;
    }

    public ExpSexosCorte(Integer id, String descripcion) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpSexosCorte)) {
            return false;
        }
        ExpSexosCorte other = (ExpSexosCorte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpSexosCorte[ id=" + id + " ]";
    }
    
}
