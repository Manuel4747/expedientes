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
@Table(name = "firmas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Firmas.findAll", query = "SELECT r FROM Firmas r")
    , @NamedQuery(name = "Firmas.findById", query = "SELECT r FROM Firmas r WHERE r.id = :id")
    , @NamedQuery(name = "Firmas.findByActuacion", query = "SELECT r FROM Firmas r WHERE r.actuacion = :actuacion")
    , @NamedQuery(name = "Firmas.findByActuacionANDPersona", query = "SELECT r FROM Firmas r WHERE r.actuacion = :actuacion AND r.persona = :persona ORDER BY r.fechaHora DESC")
    , @NamedQuery(name = "Firmas.findBySesion", query = "SELECT r FROM Firmas r WHERE r.sesion = :sesion and r.estado = :estado and r.fechaHora > :fechaHora")})
public class Firmas implements Serializable {

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
    @JoinColumn(name = "hist_actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpHistActuaciones histActuacion;
    @Column(name = "sesion")
    private String sesion;
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @Basic(optional = true)
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "acta_sesion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ExpActasSesion actaSesion;

    public Firmas() {
    }

    public Firmas(Integer id) {
        this.id = id;
    }

    public Firmas(Integer id, DocumentosJudiciales documentoJudicial) {
        this.id = id;
        this.documentoJudicial = documentoJudicial;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
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

    public ExpHistActuaciones getHistActuacion() {
        return histActuacion;
    }

    public void setHistActuacion(ExpHistActuaciones histActuacion) {
        this.histActuacion = histActuacion;
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

    public ExpActasSesion getActaSesion() {
        return actaSesion;
    }

    public void setActaSesion(ExpActasSesion actaSesion) {
        this.actaSesion = actaSesion;
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
        if (!(object instanceof Firmas)) {
            return false;
        }
        Firmas other = (Firmas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.Firmas[ id=" + id + " ]";
    }
    
}
