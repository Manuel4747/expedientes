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
@Table(name = "despachos_persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DespachosPersona.findAll", query = "SELECT p FROM DespachosPersona p")
    , @NamedQuery(name = "DespachosPersona.findById", query = "SELECT p FROM DespachosPersona p WHERE p.id = :id")
    , @NamedQuery(name = "DespachosPersona.findByDescripcion", query = "SELECT p FROM DespachosPersona p WHERE p.descripcion = :descripcion")})
public class DespachosPersona implements Serializable {

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
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "localidad_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private LocalidadesPersona localidadPersona;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DepartamentosPersona departamentoPersona;

    public DespachosPersona() {
    }

    public DespachosPersona(Integer id) {
        this.id = id;
    }

    public DespachosPersona(Integer id, String descripcion) {
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public LocalidadesPersona getLocalidadPersona() {
        return localidadPersona;
    }

    public void setLocalidadPersona(LocalidadesPersona localidadPersona) {
        this.localidadPersona = localidadPersona;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
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
        if (!(object instanceof DespachosPersona)) {
            return false;
        }
        DespachosPersona other = (DespachosPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.DespachosPersonas[ id=" + id + " ]";
    }
    
}
