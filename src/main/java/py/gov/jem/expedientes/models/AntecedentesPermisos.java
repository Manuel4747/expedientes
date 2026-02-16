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
@Table(name = "antecedentes_permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesPermisos.findAll", query = "SELECT p FROM AntecedentesPermisos p")
    , @NamedQuery(name = "AntecedentesPermisos.findById", query = "SELECT p FROM AntecedentesPermisos p WHERE p.id = :id")
    , @NamedQuery(name = "AntecedentesPermisos.findByDescripcion", query = "SELECT p FROM AntecedentesPermisos p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "AntecedentesPermisos.findByFuncion", query = "SELECT p FROM AntecedentesPermisos p WHERE p.funcion = :funcion")
    , @NamedQuery(name = "AntecedentesPermisos.findByPermiso", query = "SELECT p FROM AntecedentesPermisos p WHERE p.permiso = :permiso")})
public class AntecedentesPermisos implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "funcion")
    private String funcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "permiso")
    private String permiso;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "menu")
    private String menu;

    public AntecedentesPermisos() {
    }

    public AntecedentesPermisos(Integer id) {
        this.id = id;
    }

    public AntecedentesPermisos(Integer id, String descripcion, String funcion, String permiso) {
        this.id = id;
        this.descripcion = descripcion;
        this.funcion = funcion;
        this.permiso = permiso;
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

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
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
        if (!(object instanceof AntecedentesPermisos)) {
            return false;
        }
        AntecedentesPermisos other = (AntecedentesPermisos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.AntecedentesPermisos[ id=" + id + " ]";
    }
    
}
