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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_notificaciones_ujieres")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpNotificacionesUjieres.findAll", query = "SELECT r FROM ExpNotificacionesUjieres r")
    , @NamedQuery(name = "ExpNotificacionesUjieres.findById", query = "SELECT r FROM ExpNotificacionesUjieres r WHERE r.id = :id")
    , @NamedQuery(name = "ExpNotificacionesUjieres.findByNotificado", query = "SELECT r FROM ExpNotificacionesUjieres r WHERE r.notificado = :notificado ORDER BY r.fechaHoraAlta")
    , @NamedQuery(name = "ExpNotificacionesUjieres.findByActuacion", query = "SELECT r FROM ExpNotificacionesUjieres r WHERE r.actuacion = :actuacion")})
public class ExpNotificacionesUjieres implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "actuacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ExpActuaciones actuacion;
    @JoinColumn(name = "destinatario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas destinatario;
    @JoinColumn(name = "actuacion_notificacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActuaciones actuacionNotificacion;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas responsable;
    @Basic(optional = true)
    @Column(name = "fecha_hora_notificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraNotificacion;
    @JoinColumn(name = "notificador", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas notificador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaBorrado;
    @Basic(optional = false)
    @Column(name = "notificado")
    private boolean notificado;

    public ExpNotificacionesUjieres() {
    }

    public ExpNotificacionesUjieres(Integer id) {
        this.id = id;
    }

    public ExpNotificacionesUjieres(Integer id, DocumentosJudiciales documentoJudicial) {
        this.id = id;
        this.documentoJudicial = documentoJudicial;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
    }

    public Personas getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Personas destinatario) {
        this.destinatario = destinatario;
    }

    public ExpActuaciones getActuacionNotificacion() {
        return actuacionNotificacion;
    }

    public void setActuacionNotificacion(ExpActuaciones actuacionNotificacion) {
        this.actuacionNotificacion = actuacionNotificacion;
    }

    public Personas getResponsable() {
        return responsable;
    }

    public void setResponsable(Personas responsable) {
        this.responsable = responsable;
    }

    public Date getFechaHoraNotificacion() {
        return fechaHoraNotificacion;
    }

    public void setFechaHoraNotificacion(Date fechaHoraNotificacion) {
        this.fechaHoraNotificacion = fechaHoraNotificacion;
    }

    public Personas getNotificador() {
        return notificador;
    }

    public void setNotificador(Personas notificador) {
        this.notificador = notificador;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
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

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
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
        if (!(object instanceof ExpNotificacionesUjieres)) {
            return false;
        }
        ExpNotificacionesUjieres other = (ExpNotificacionesUjieres) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpNotificacionesUjieres[ id=" + id + " ]";
    }
    
}
