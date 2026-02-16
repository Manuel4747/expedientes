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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_personas_firmantes_por_actas_sesion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPersonasFirmantesPorActasSesion.findAll", query = "SELECT d FROM ExpPersonasFirmantesPorActasSesion d")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActasSesion.findById", query = "SELECT d FROM ExpPersonasFirmantesPorActasSesion d WHERE d.id = :id")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActasSesion.findByPersonaFirmanteFirmado", query = "SELECT o FROM ExpPersonasFirmantesPorActasSesion o WHERE o.personaFirmante = :personaFirmante AND o.firmado = :firmado ORDER BY o.actaSesion.fechaSesion,  o.actaSesion.fechaHoraAlta")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActasSesion.findByActaSesion", query = "SELECT o FROM ExpPersonasFirmantesPorActasSesion o WHERE o.actaSesion = :actaSesion ORDER BY o.personaFirmante.nombresApellidos, o.fechaHoraAlta")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActasSesion.findByActaSesionPersonaFirmanteEstado", query = "SELECT o FROM ExpPersonasFirmantesPorActasSesion o WHERE o.actaSesion = :actaSesion AND o.personaFirmante = :personaFirmante AND o.estado = :estado")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActasSesion.findByActaSesionEstado", query = "SELECT o FROM ExpPersonasFirmantesPorActasSesion o WHERE o.actaSesion = :actaSesion AND o.estado = :estado ORDER BY o.personaFirmante.nombresApellidos, o.fechaHoraAlta")})
public class ExpPersonasFirmantesPorActasSesion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "acta_sesion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActasSesion actaSesion;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_firmante", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaFirmante;
    @Basic(optional = false)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @Column(name = "fecha_hora_firma")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraFirma;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @Column(name = "firmado")
    private boolean firmado;
    @JoinColumn(name = "persona_firma", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaFirma;
    @Column(name = "revisado")
    private boolean revisado;
    @JoinColumn(name = "persona_revisado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaRevisado;
    @Column(name = "fecha_hora_revisado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRevisado;

    public ExpPersonasFirmantesPorActasSesion() {
    }
    
    public ExpPersonasFirmantesPorActasSesion(Integer id, ExpRolesFirmantesPorActuaciones rol, Personas personaFirmante) {
        this.estado = rol.getEstado();
        this.fechaHoraAlta = rol.getFechaHoraAlta();
        this.fechaHoraFirma = rol.getFechaHoraFirma();
        this.firmado = rol.isFirmado();
        this.personaAlta = rol.getPersonaAlta();
        this.personaFirmante = personaFirmante;
        this.id = id;
    }

    public ExpPersonasFirmantesPorActasSesion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ExpActasSesion getActaSesion() {
        return actaSesion;
    }

    public void setActaSesion(ExpActasSesion actaSesion) {
        this.actaSesion = actaSesion;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Personas getPersonaFirmante() {
        return personaFirmante;
    }

    public void setPersonaFirmante(Personas personaFirmante) {
        this.personaFirmante = personaFirmante;
    }

    public Date getFechaHoraFirma() {
        return fechaHoraFirma;
    }

    public void setFechaHoraFirma(Date fechaHoraFirma) {
        this.fechaHoraFirma = fechaHoraFirma;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public Personas getPersonaFirma() {
        return personaFirma;
    }

    public void setPersonaFirma(Personas personaFirma) {
        this.personaFirma = personaFirma;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
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
    
    @XmlTransient
    public String getFirmadoString() {
        return firmado?"SI":"NO";
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
        if (!(object instanceof ExpPersonasFirmantesPorActasSesion)) {
            return false;
        }
        ExpPersonasFirmantesPorActasSesion other = (ExpPersonasFirmantesPorActasSesion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActasSesion[ id=" + id + " ]";
    }
    
}
