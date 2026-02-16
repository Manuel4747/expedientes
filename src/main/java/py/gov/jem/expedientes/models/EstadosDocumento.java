/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "estados_documento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadosDocumento.findAll", query = "SELECT e FROM EstadosDocumento e")
    , @NamedQuery(name = "EstadosDocumento.findByCodigo", query = "SELECT e FROM EstadosDocumento e WHERE e.codigo = :codigo")
    , @NamedQuery(name = "EstadosDocumento.findByDescripcion", query = "SELECT e FROM EstadosDocumento e WHERE e.descripcion = :descripcion")})
public class EstadosDocumento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "estado")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection;

    public EstadosDocumento() {
    }

    public EstadosDocumento(String codigo) {
        this.codigo = codigo;
    }

    public EstadosDocumento(String codigo, String descripcion) {
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

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection() {
        return documentosJudicialesCollection;
    }

    public void setDocumentosJudicialesCollection(Collection<DocumentosJudiciales> documentosJudicialesCollection) {
        this.documentosJudicialesCollection = documentosJudicialesCollection;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof EstadosDocumento)) {
            return false;
        }
        EstadosDocumento other = (EstadosDocumento) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.gestionstartic.models.EstadosDocumento[ codigo=" + codigo + " ]";
    }
    
}
