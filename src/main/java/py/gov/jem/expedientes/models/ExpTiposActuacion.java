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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_tipos_actuacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpTiposActuacion.findAll", query = "SELECT t FROM ExpTiposActuacion t")
    , @NamedQuery(name = "ExpTiposActuacion.findById", query = "SELECT t FROM ExpTiposActuacion t WHERE t.id = :id")
    , @NamedQuery(name = "ExpTiposActuacion.findByCategoriaActuacion", query = "SELECT t FROM ExpTiposActuacion t WHERE t.categoriaActuacion = :categoriaActuacion ORDER BY t.descripcion")
    , @NamedQuery(name = "ExpTiposActuacion.findByRolCategoriaActuacion", query = "SELECT t FROM ExpTiposActuacion t, ExpTiposActuacionPorAntecedentesRoles r WHERE t.id = r.tiposActuacionPorAntecedentesRolesPK.tipoActuacion AND t.categoriaActuacion = :categoriaActuacion AND r.tiposActuacionPorAntecedentesRolesPK.rol = :rol ORDER BY t.descripcion")
    , @NamedQuery(name = "ExpTiposActuacion.findByDescripcion", query = "SELECT t FROM ExpTiposActuacion t WHERE t.descripcion = :descripcion")})
public class ExpTiposActuacion implements Serializable {

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
    @JoinColumn(name = "categoria_actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpCategoriasActuacion categoriaActuacion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "secuencia")
    private String secuencia;

    public ExpTiposActuacion() {
    }

    public ExpTiposActuacion(Integer id) {
        this.id = id;
    }

    public ExpTiposActuacion(Integer id, String descripcion) {
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

    public ExpCategoriasActuacion getCategoriaActuacion() {
        return categoriaActuacion;
    }

    public void setCategoriaActuacion(ExpCategoriasActuacion categoriaActuacion) {
        this.categoriaActuacion = categoriaActuacion;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
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
        if (!(object instanceof ExpTiposActuacion)) {
            return false;
        }
        ExpTiposActuacion other = (ExpTiposActuacion) object;
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
