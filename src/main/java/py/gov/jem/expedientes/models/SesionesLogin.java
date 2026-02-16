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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "sesiones_login")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SesionesLogin.findAll", query = "SELECT t FROM SesionesLogin t")
    , @NamedQuery(name = "SesionesLogin.findByHashFechaHoraCaducidadEstado", query = "SELECT p FROM SesionesLogin p WHERE p.hash = :hash AND p.fechaHoraCaducidad > :fechaHoraCaducidad AND p.estado.codigo = :estado")
    , @NamedQuery(name = "SesionesLogin.findById", query = "SELECT t FROM SesionesLogin t WHERE t.id = :id")})
public class SesionesLogin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuario;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas persona;
    @JoinColumn(name = "parametro_sistema", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ParametrosSistema parametroSistema;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "hash")
    private String hash;
    @Basic(optional = false)
    @Column(name = "fecha_hora_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraCaducidad;

    public SesionesLogin() {
    }

    public SesionesLogin(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public ParametrosSistema getParametroSistema() {
        return parametroSistema;
    }

    public void setParametrosSistema(ParametrosSistema parametroSistema) {
        this.parametroSistema = parametroSistema;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getFechaHoraCaducidad() {
        return fechaHoraCaducidad;
    }

    public void setFechaHoraCaducidad(Date fechaHoraCaducidad) {
        this.fechaHoraCaducidad = fechaHoraCaducidad;
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
        if (!(object instanceof SesionesLogin)) {
            return false;
        }
        SesionesLogin other = (SesionesLogin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.SesionesLogin[ id=" + id + " ]";
    }
    
}
