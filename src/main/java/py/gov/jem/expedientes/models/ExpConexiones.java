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
@Table(name = "exp_conexiones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpConexiones.findAll", query = "SELECT t FROM ExpConexiones t")
    , @NamedQuery(name = "ExpConexiones.findById", query = "SELECT t FROM ExpConexiones t WHERE t.id = :id")
    , @NamedQuery(name = "ExpConexiones.findByDescripcion", query = "SELECT t FROM ExpConexiones t WHERE t.descripcion = :descripcion")})
public class ExpConexiones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ip_servidor")
    private String ipServidor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "puerto_servidor")
    private String puertoServidor;
    @Basic(optional = true)
    @Size(max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "uri")
    private String uri;

    public ExpConexiones() {
    }

    public ExpConexiones(Integer id) {
        this.id = id;
    }

    public ExpConexiones(Integer id, String descripcion) {
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

    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public String getPuertoServidor() {
        return puertoServidor;
    }

    public void setPuertoServidor(String puertoServidor) {
        this.puertoServidor = puertoServidor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
        if (!(object instanceof ExpConexiones)) {
            return false;
        }
        ExpConexiones other = (ExpConexiones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (descripcion!=null)?descripcion:"";
    }
    
}
