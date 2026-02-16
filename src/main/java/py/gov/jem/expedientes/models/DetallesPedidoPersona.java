/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "detalles_pedido_persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetallesPedidoPersona.findAll", query = "SELECT t FROM DetallesPedidoPersona t")
    , @NamedQuery(name = "DetallesPedidoPersona.findById", query = "SELECT t FROM DetallesPedidoPersona t WHERE t.id = :id")
    , @NamedQuery(name = "DetallesPedidoPersona.findByPedidoPersona", query = "SELECT t FROM DetallesPedidoPersona t WHERE t.pedidoPersona = :pedidoPersona ORDER BY t.fechaHoraAlta DESC")
    , @NamedQuery(name = "DetallesPedidoPersona.findByPedidoPersonaOrdered", query = "SELECT t FROM DetallesPedidoPersona t WHERE t.pedidoPersona = :pedidoPersona ORDER BY t.fechaHoraAlta")
    , @NamedQuery(name = "DetallesPedidoPersona.findByFechaHoraAlta", query = "SELECT t FROM DetallesPedidoPersona t WHERE t.fechaHoraAlta = :fechaHoraAlta")})
public class DetallesPedidoPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "pedido_persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PedidosPersona pedidoPersona;

    public DetallesPedidoPersona() {
    }

    public DetallesPedidoPersona(Integer id) {
        this.id = id;
    }

    public DetallesPedidoPersona(Integer id, Date fechaHoraAlta) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public PedidosPersona getPedidoPersona() {
        return pedidoPersona;
    }

    public void setPedidoPersona(PedidosPersona pedidoPersona) {
        this.pedidoPersona = pedidoPersona;
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
        if (!(object instanceof DetallesPedidoPersona)) {
            return false;
        }
        DetallesPedidoPersona other = (DetallesPedidoPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (ruta!=null)?ruta:"";
    }
    
}
