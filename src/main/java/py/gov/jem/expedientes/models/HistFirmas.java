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
@Table(name = "hist_firmas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistFirmas.findAll", query = "SELECT r FROM HistFirmas r")
    , @NamedQuery(name = "HistFirmas.findById", query = "SELECT r FROM HistFirmas r WHERE r.id = :id")
    , @NamedQuery(name = "HistFirmas.findBySesion", query = "SELECT r FROM HistFirmas r WHERE r.sesion = :sesion and r.estado = :estado and r.fechaHora > :fechaHora")})
public class HistFirmas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "hist_actuacion_original", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpHistActuaciones histActuacionOriginal;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaBorrado;

    public HistFirmas() {
    }

    public HistFirmas(Integer id) {
        this.id = id;
    }

    public HistFirmas(Integer id, DocumentosJudiciales documentoJudicial) {
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

    public ExpHistActuaciones getHistActuacionOriginal() {
        return histActuacionOriginal;
    }

    public void setHistActuacionOriginal(ExpHistActuaciones histActuacionOriginal) {
        this.histActuacionOriginal = histActuacionOriginal;
    }

    public ExpHistActuaciones getHistActuacion() {
        return histActuacion;
    }

    public void setHistActuacion(ExpHistActuaciones histActuacion) {
        this.histActuacion = histActuacion;
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
        if (!(object instanceof HistFirmas)) {
            return false;
        }
        HistFirmas other = (HistFirmas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.HistFirmas[ id=" + id + " ]";
    }
    
}
