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
@Table(name = "resuelve")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resuelve.findAll", query = "SELECT s FROM Resuelve s")
    , @NamedQuery(name = "Resuelve.findByCodigo", query = "SELECT s FROM Resuelve s WHERE s.codigo = :codigo")
    , @NamedQuery(name = "Resuelve.findByTipoResolucion", query = "SELECT s FROM Resuelve s WHERE s.tipoResolucion = :tipoResolucion ORDER BY s.descripcion")
    , @NamedQuery(name = "Resuelve.findByTipoResolucionOCancelacion", query = "SELECT s FROM Resuelve s WHERE s.tipoResolucion = :tipoResolucion OR s.codigo = 'CA' ORDER BY s.descripcion")
    , @NamedQuery(name = "Resuelve.findByDescripcion", query = "SELECT s FROM Resuelve s WHERE s.descripcion = :descripcion")})
public class Resuelve implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "tipo_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposResolucion tipoResolucion;

    public Resuelve() {
    }

    public Resuelve(String codigo) {
        this.codigo = codigo;
    }

    public Resuelve(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public TiposResolucion getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(TiposResolucion tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
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
        if (!(object instanceof Resuelve)) {
            return false;
        }
        Resuelve other = (Resuelve) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (descripcion != null)?descripcion:"";
    }
    
}
