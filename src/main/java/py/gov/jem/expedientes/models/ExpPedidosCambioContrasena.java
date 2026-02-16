/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_pedidos_cambio_contrasena")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPedidosCambioContrasena.findAll", query = "SELECT p FROM ExpPedidosCambioContrasena p order by p.fechaHoraAlta")
    , @NamedQuery(name = "ExpPedidosCambioContrasena.findByHashFechaHoraCaducidad", query = "SELECT p FROM ExpPedidosCambioContrasena p WHERE p.hash = :hash and p.fechaHoraCaducidad > :fechaHoraCaducidad")
    , @NamedQuery(name = "ExpPedidosCambioContrasena.findByFechaHoraAlta", query = "SELECT p FROM ExpPedidosCambioContrasena p WHERE p.fechaHoraAlta = :fechaHoraAlta")})
public class ExpPedidosCambioContrasena implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Column(name = "realizado")
    private boolean realizado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraCaducidad;
    @Basic(optional = true)
    @Column(name = "fecha_hora_cambio_contrasena")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraCambioContrasena;
    @Basic(optional = true)
    @Size(max = 50)
    @Column(name = "hash")
    private String hash;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas persona;

    public ExpPedidosCambioContrasena() {
    }

    public ExpPedidosCambioContrasena(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public Date getFechaHoraCaducidad() {
        return fechaHoraCaducidad;
    }

    public void setFechaHoraCaducidad(Date fechaHoraCaducidad) {
        this.fechaHoraCaducidad = fechaHoraCaducidad;
    }

    public Date getFechaHoraCambioContrasena() {
        return fechaHoraCambioContrasena;
    }

    public void setFechaHoraCambioContrasena(Date fechaHoraCambioContrasena) {
        this.fechaHoraCambioContrasena = fechaHoraCambioContrasena;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
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
        if (!(object instanceof ExpPedidosCambioContrasena)) {
            return false;
        }
        ExpPedidosCambioContrasena other = (ExpPedidosCambioContrasena) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpPedidosCambioContrasena[ id=" + id + " ]";
    }
    
}
