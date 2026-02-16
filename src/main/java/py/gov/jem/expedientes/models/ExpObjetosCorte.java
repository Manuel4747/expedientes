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
@Table(name = "exp_objetos_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpObjetosCorte.findAll", query = "SELECT r FROM ExpObjetosCorte r")
    , @NamedQuery(name = "ExpObjetosCorte.findById", query = "SELECT r FROM ExpObjetosCorte r WHERE r.id = :id")
    , @NamedQuery(name = "ExpObjetosCorte.findByDescripcion", query = "SELECT r FROM ExpObjetosCorte r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "ExpObjetosCorte.findByCodigoCorte", query = "SELECT r FROM ExpObjetosCorte r WHERE r.codigoCorte = :codigoCorte")})
public class ExpObjetosCorte implements Serializable {

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
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "codigo_corte")
    private String codigoCorte;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "articulo")
    private String articulo;
    @Size(max = 200)
    @Column(name = "ley_procesal")
    private String leyProcesal;

    public ExpObjetosCorte() {
    }

    public ExpObjetosCorte(Integer id) {
        this.id = id;
    }

    public ExpObjetosCorte(Integer id, String descripcion, String codigoCorte, String articulo, String leyProcesal) {
        this.id = id;
        this.descripcion = descripcion;
        this.codigoCorte = codigoCorte;
        this.articulo = articulo;
        this.leyProcesal = leyProcesal;
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

    public String getCodigoCorte() {
        return codigoCorte;
    }

    public void setCodigoCorte(String codigoCorte) {
        this.codigoCorte = codigoCorte;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getLeyProcesal() {
        return leyProcesal;
    }

    public void setLeyProcesal(String leyProcesal) {
        this.leyProcesal = leyProcesal;
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
        if (!(object instanceof ExpObjetosCorte)) {
            return false;
        }
        ExpObjetosCorte other = (ExpObjetosCorte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpObjetosCorte[ id=" + id + " ]";
    }
    
}
