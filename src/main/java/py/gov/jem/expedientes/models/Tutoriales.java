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
@Table(name = "tutoriales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tutoriales.findAll", query = "SELECT r FROM Tutoriales r ORDER BY r.descripcion")
    , @NamedQuery(name = "Tutoriales.findById", query = "SELECT r FROM Tutoriales r WHERE r.id = :id")
    , @NamedQuery(name = "Tutoriales.findButId", query = "SELECT r FROM Tutoriales r WHERE r.id <> :id ORDER BY r.descripcion")
    , @NamedQuery(name = "Tutoriales.findByDescripcion", query = "SELECT r FROM Tutoriales r WHERE r.descripcion = :descripcion")})
public class Tutoriales implements Serializable {

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
    @Column(name = "url")
    private String url;
    
    @Size(min = 1, max = 200)
    @Column(name = "url_biblioteca")
    private String urlBiblioteca;

    public Tutoriales() {
    }

    public Tutoriales(Integer id) {
        this.id = id;
    }

    public Tutoriales(Integer id, String descripcion) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlBiblioteca() {
        return urlBiblioteca;
    }

    public void setUrlBiblioteca(String urlBiblioteca) {
        this.urlBiblioteca = urlBiblioteca;
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
        if (!(object instanceof Tutoriales)) {
            return false;
        }
        Tutoriales other = (Tutoriales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.Tutoriales[ id=" + id + " ]";
    }
    
}
