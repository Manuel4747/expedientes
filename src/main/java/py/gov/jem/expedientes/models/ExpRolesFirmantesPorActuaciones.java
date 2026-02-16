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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_roles_firmantes_por_actuaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpRolesFirmantesPorActuaciones.findAll", query = "SELECT d FROM ExpRolesFirmantesPorActuaciones d")
    , @NamedQuery(name = "ExpRolesFirmantesPorActuaciones.findById", query = "SELECT d FROM ExpRolesFirmantesPorActuaciones d WHERE d.id = :id")
    , @NamedQuery(name = "ExpRolesFirmantesPorActuaciones.findByDocumentoJudicial", query = "SELECT o FROM ExpRolesFirmantesPorActuaciones o WHERE o.documentoJudicial = :documentoJudicial ORDER BY o.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpRolesFirmantesPorActuaciones.findByRolFirmanteFirmado", query = "SELECT o FROM ExpRolesFirmantesPorActuaciones o WHERE o.rolFirmante = :rolFirmante AND o.firmado = :firmado ORDER BY o.fechaHoraAlta ASC")
    , @NamedQuery(name = "ExpRolesFirmantesPorActuaciones.findByActuacion", query = "SELECT o FROM ExpRolesFirmantesPorActuaciones o WHERE o.actuacion = :actuacion ORDER BY o.rolFirmante.descripcion, o.fechaHoraAlta")
    , @NamedQuery(name = "ExpRolesFirmantesPorActuaciones.findByActuacionRolFirmanteEstado", query = "SELECT o FROM ExpRolesFirmantesPorActuaciones o WHERE o.actuacion = :actuacion AND o.rolFirmante = :rolFirmante AND o.estado = :estado")
    , @NamedQuery(name = "ExpRolesFirmantesPorActuaciones.findByActuacionEstado", query = "SELECT o FROM ExpRolesFirmantesPorActuaciones o WHERE o.actuacion = :actuacion AND o.estado = :estado ORDER BY o.rolFirmante.descripcion, o.fechaHoraAlta")})
public class ExpRolesFirmantesPorActuaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActuaciones actuacion;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_firmante", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaFirmante;
    @JoinColumn(name = "rol_firmante", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AntecedentesRoles rolFirmante;
    @Basic(optional = false)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Column(name = "fecha_hora_firma")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraFirma;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @Column(name = "firmado")
    private boolean firmado;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;


    public ExpRolesFirmantesPorActuaciones() {
    }

    public ExpRolesFirmantesPorActuaciones(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
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

    public AntecedentesRoles getRolFirmante() {
        return rolFirmante;
    }

    public void setRolFirmante(AntecedentesRoles rolFirmante) {
        this.rolFirmante = rolFirmante;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpRolesFirmantesPorActuaciones)) {
            return false;
        }
        ExpRolesFirmantesPorActuaciones other = (ExpRolesFirmantesPorActuaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpRolesFirmantesPorActuaciones[ id=" + id + " ]";
    }
    
}
