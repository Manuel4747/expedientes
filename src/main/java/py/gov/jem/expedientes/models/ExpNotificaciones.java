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
import javax.persistence.Lob;
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
@Table(name = "exp_notificaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpNotificaciones.findAll", query = "SELECT r FROM ExpNotificaciones r ORDER BY r.estado DESC, r.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpNotificaciones.findAllTipoActuacionEstado", query = "SELECT r FROM ExpNotificaciones r WHERE r.actuacion.tipoActuacion = :tipoActuacion AND r.estado = :estado ORDER BY r.estado DESC, r.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpNotificaciones.findByActuacion", query = "SELECT r FROM ExpNotificaciones r WHERE r.actuacion = :actuacion")
    , @NamedQuery(name = "ExpNotificaciones.findByDestinatarioTipoActuacionEstadoVisible", query = "SELECT r FROM ExpNotificaciones r WHERE r.destinatario = :destinatario AND r.actuacion.tipoActuacion = :tipoActuacion AND r.estado = :estado AND r.visible = :visible ORDER BY r.estado DESC, r.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpNotificaciones.findByEstado", query = "SELECT r FROM ExpNotificaciones r WHERE r.estado = :estado ORDER BY r.fechaHoraAlta")
    , @NamedQuery(name = "ExpNotificaciones.findById", query = "SELECT r FROM ExpNotificaciones r WHERE r.id = :id")})
public class ExpNotificaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "texto")
    private String texto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = true)
    @Column(name = "fecha_hora_lectura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraLectura;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private ExpEstadosNotificacion estado;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActuaciones actuacion;
    @JoinColumn(name = "remitente", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas remitente;
    @JoinColumn(name = "destinatario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas destinatario;
    @Basic(optional = true)
    @Column(name = "visible")
    private boolean visible;
    @JoinColumn(name = "persona_visible", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaVisible;
    @Basic(optional = true)
    @Column(name = "fecha_hora_visible")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisible;
    @Basic(optional = true)
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstado;

    public ExpNotificaciones() {
    }

    public ExpNotificaciones(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraLectura() {
        return fechaHoraLectura;
    }

    public void setFechaHoraLectura(Date fechaHoraLectura) {
        this.fechaHoraLectura = fechaHoraLectura;
    }

    public ExpEstadosNotificacion getEstado() {
        return estado;
    }

    public void setEstado(ExpEstadosNotificacion estado) {
        this.estado = estado;
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

    public Personas getRemitente() {
        return remitente;
    }

    public void setRemitente(Personas remitente) {
        this.remitente = remitente;
    }

    public Personas getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Personas destinatario) {
        this.destinatario = destinatario;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpNotificaciones)) {
            return false;
        }
        ExpNotificaciones other = (ExpNotificaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpNotificaciones[ id=" + id + " ]";
    }
    
}
