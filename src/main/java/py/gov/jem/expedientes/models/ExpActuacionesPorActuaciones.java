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
@Table(name = "exp_actuaciones_por_actuaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpActuacionesPorActuaciones.findAll", query = "SELECT d FROM ExpActuacionesPorActuaciones d")
    , @NamedQuery(name = "ExpActuacionesPorActuaciones.findById", query = "SELECT d FROM ExpActuacionesPorActuaciones d WHERE d.id = :id")
    , @NamedQuery(name = "ExpActuacionesPorActuaciones.findByDocumentoJudicial", query = "SELECT o FROM ExpActuacionesPorActuaciones o WHERE o.documentoJudicialPadre = :documentoJudicialPadre")})
public class ExpActuacionesPorActuaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_judicial_padre", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicialPadre;
    @JoinColumn(name = "actuacion_padre", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActuaciones actuacionPadre;
    @JoinColumn(name = "documento_judicial_hijo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicialHijo;
    @JoinColumn(name = "actuacion_hijo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActuaciones actuacionHijo;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
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
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaBorrado;

    public ExpActuacionesPorActuaciones() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DocumentosJudiciales getDocumentoJudicialPadre() {
        return documentoJudicialPadre;
    }

    public void setDocumentoJudicialPadre(DocumentosJudiciales documentoJudicialPadre) {
        this.documentoJudicialPadre = documentoJudicialPadre;
    }

    public ExpActuaciones getActuacionPadre() {
        return actuacionPadre;
    }

    public void setActuacionPadre(ExpActuaciones actuacionPadre) {
        this.actuacionPadre = actuacionPadre;
    }

    public DocumentosJudiciales getDocumentoJudicialHijo() {
        return documentoJudicialHijo;
    }

    public void setDocumentoJudicialHijo(DocumentosJudiciales documentoJudicialHijo) {
        this.documentoJudicialHijo = documentoJudicialHijo;
    }

    public ExpActuaciones getActuacionHijo() {
        return actuacionHijo;
    }

    public void setActuacionHijo(ExpActuaciones actuacionHijo) {
        this.actuacionHijo = actuacionHijo;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpActuacionesPorActuaciones)) {
            return false;
        }
        ExpActuacionesPorActuaciones other = (ExpActuacionesPorActuaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpActuacionesPorActuaciones[ id=" + id + " ]";
    }
    
}
