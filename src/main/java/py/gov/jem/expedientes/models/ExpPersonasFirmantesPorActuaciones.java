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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_personas_firmantes_por_actuaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findAll", query = "SELECT d FROM ExpPersonasFirmantesPorActuaciones d")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findById", query = "SELECT d FROM ExpPersonasFirmantesPorActuaciones d WHERE d.id = :id")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByDocumentoJudicial", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.documentoJudicial = :documentoJudicial ORDER BY o.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByDocumentoJudicialPersonaFirmanteFirmadoTiposActuacion", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.documentoJudicial = :documentoJudicial AND o.personaFirmante = :personaFirmante AND o.firmado = :firmado AND o.actuacion.tipoActuacion IN :tiposActuacion AND o.estado.codigo = 'AC' ORDER BY o.actuacion.fechaPresentacion,  o.actuacion.fechaHoraAlta")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmado", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.personaFirmante = :personaFirmante AND o.firmado = :firmado ORDER BY o.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByActuacionEstadoFirmado", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.actuacion = :actuacion AND o.estado = :estado AND o.firmado = :firmado")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByPersonaFirmanteFirmarMultiple", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.personaFirmante = :personaFirmante AND o.firmarMultiple = :firmarMultiple ORDER BY o.fechaHoraAlta")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByActuacion", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.actuacion = :actuacion ORDER BY o.personaFirmante.nombresApellidos, o.fechaHoraAlta")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.deleteByActuacion", query = "DELETE FROM ExpPersonasFirmantesPorActuaciones o WHERE o.actuacion = :actuacion")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByActuacionPersonaFirmanteEstado", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.actuacion = :actuacion AND o.personaFirmante = :personaFirmante AND o.estado = :estado")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByActuacionEstado", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.actuacion = :actuacion AND o.estado = :estado ORDER BY o.personaFirmante.nombresApellidos, o.fechaHoraAlta")
    , @NamedQuery(name = "ExpPersonasFirmantesPorActuaciones.findByActuacionEstadoNotPersonaFirmante", query = "SELECT o FROM ExpPersonasFirmantesPorActuaciones o WHERE o.actuacion = :actuacion AND o.estado = :estado AND o.personaFirmante not in :personaFirmante ORDER BY o.personaFirmante.nombresApellidos, o.fechaHoraAlta")})
public class ExpPersonasFirmantesPorActuaciones implements Serializable {

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
    @Transient
    private Integer idRol;
    @Basic(optional = true)
    @Column(name = "revisado")
    private boolean revisado;
    @JoinColumn(name = "persona_revisado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaRevisado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_revisado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRevisado;
    @Transient
    private boolean revisadoNuevo;
    @Column(name = "firmar_multiple")
    private boolean firmarMultiple;
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaBorrado;

    public ExpPersonasFirmantesPorActuaciones() {
    }
    
    public ExpPersonasFirmantesPorActuaciones(Integer id, ExpRolesFirmantesPorActuaciones rol, Personas personaFirmante) {
        this.actuacion = rol.getActuacion();
        this.documentoJudicial = rol.getDocumentoJudicial();
        this.estado = rol.getEstado();
        this.fechaHoraAlta = rol.getFechaHoraAlta();
        this.fechaHoraFirma = rol.getFechaHoraFirma();
        this.firmado = rol.isFirmado();
        this.personaAlta = rol.getPersonaAlta();
        this.personaFirmante = personaFirmante;
        this.id = id;
        this.idRol = rol.getId();
    }

    public ExpPersonasFirmantesPorActuaciones(Integer id) {
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

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
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

    public Personas getPersonaFirma() {
        return personaFirma;
    }

    public void setPersonaFirma(Personas personaFirma) {
        this.personaFirma = personaFirma;
    }

    public boolean isRevisadoNuevo() {
        return revisado;
    }

    public void setRevisadoNuevo(boolean revisadoNuevo) {
        this.revisadoNuevo = revisadoNuevo;
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

    public boolean isFirmarMultiple() {
        return firmarMultiple;
    }

    public void setFirmarMultiple(boolean firmarMultiple) {
        this.firmarMultiple = firmarMultiple;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Personas getPersonaBorrado() {
        return personaBorrado;
    }

    public void setPersonaBorrado(Personas personaBorrado) {
        this.personaBorrado = personaBorrado;
    }
    
    @XmlTransient
    public String getFirmadoString() {
        return firmado?"SI":"NO";
    }
    
    @XmlTransient
    public String getRevisadoString() {
        return revisado?"SI":"NO";
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
        if (!(object instanceof ExpPersonasFirmantesPorActuaciones)) {
            return false;
        }
        ExpPersonasFirmantesPorActuaciones other = (ExpPersonasFirmantesPorActuaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpPersonasFirmantesPorActuaciones[ id=" + id + " ]";
    }
    
}
