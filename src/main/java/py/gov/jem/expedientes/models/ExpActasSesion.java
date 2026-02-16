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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_actas_sesion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpActasSesion.findAll", query = "SELECT r FROM ExpActasSesion r")
    , @NamedQuery(name = "ExpActasSesion.findById", query = "SELECT r FROM ExpActasSesion r WHERE r.id = :id")
    , @NamedQuery(name = "ExpActasSesion.findByHashEstado", query = "SELECT t FROM ExpActasSesion t WHERE t.hash = :hash and t.estado.codigo = :estado")
    , @NamedQuery(name = "ExpActasSesion.findByFechaSesion", query = "SELECT d FROM ExpActasSesion d WHERE d.fechaSesion >= :fechaDesde ORDER BY d.fechaSesion DESC, d.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActasSesion.findByFechaHoraAlta", query = "SELECT r FROM ExpActasSesion r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ExpActasSesion.findByFechaHoraUltimoEstado", query = "SELECT r FROM ExpActasSesion r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ExpActasSesion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_sesion")
    @Temporal(TemporalType.DATE)
    private Date fechaSesion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "persona_visible", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaVisible;
    @Basic(optional = true)
    @Column(name = "fecha_hora_visible")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisible;
    @JoinColumn(name = "tipo_sesion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposSesion tipoSesion;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private ExpEstadosActaSesion estado;
    @Basic(optional = false)
    @Column(name = "visible")
    private boolean visible;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "archivo")
    private String archivo;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "hash")
    private String hash;
    @Transient
    private boolean revisado;
    @Transient
    private Personas personaRevisado;
    @Transient
    private Date fechaHoraRevisado;

    public ExpActasSesion() {
    }

    public ExpActasSesion(Integer id) {
        this.id = id;
    }

    public ExpActasSesion(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
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

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public Date getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(Date fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public ExpTiposSesion getTipoSesion() {
        return tipoSesion;
    }

    public void setTipoSesion(ExpTiposSesion tipoSesion) {
        this.tipoSesion = tipoSesion;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Personas getPersonaVisible() {
        return personaVisible;
    }

    public void setPersonaVisible(Personas personaVisible) {
        this.personaVisible = personaVisible;
    }

    public Date getFechaHoraVisible() {
        return fechaHoraVisible;
    }

    public void setFechaHoraVisible(Date fechaHoraVisible) {
        this.fechaHoraVisible = fechaHoraVisible;
    }

    public ExpEstadosActaSesion getEstado() {
        return estado;
    }

    public void setEstado(ExpEstadosActaSesion estado) {
        this.estado = estado;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public boolean isRevisado() {
        return revisado;
    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
    }

    public Personas getPersonaRevisado() {
        return personaRevisado;
    }

    public void setPersonaRevisado(Personas personaRevisado) {
        this.personaRevisado = personaRevisado;
    }

    public Date getFechaHoraRevisado() {
        return fechaHoraRevisado;
    }

    public void setFechaHoraRevisado(Date fechaHoraRevisado) {
        this.fechaHoraRevisado = fechaHoraRevisado;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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
        if (!(object instanceof ExpActasSesion)) {
            return false;
        }
        ExpActasSesion other = (ExpActasSesion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpActasSesion[ id=" + id + " ]";
    }
    
}
