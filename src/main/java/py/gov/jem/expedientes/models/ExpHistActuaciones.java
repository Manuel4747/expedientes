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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_hist_actuaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpHistActuaciones.findAll", query = "SELECT d FROM ExpHistActuaciones d")
    , @NamedQuery(name = "ExpHistActuaciones.findById", query = "SELECT d FROM ExpHistActuaciones d WHERE d.id = :id")
    , @NamedQuery(name = "ExpHistActuaciones.findByDocumentoJudicial", query = "SELECT o FROM ExpHistActuaciones o WHERE o.documentoJudicial = :documentoJudicial ORDER BY o.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpHistActuaciones.findByActuacion", query = "SELECT o FROM ExpHistActuaciones o WHERE o.actuacion = :actuacion ORDER BY o.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpHistActuaciones.findByArchivo", query = "SELECT d FROM ExpHistActuaciones d WHERE d.archivo = :archivo")})
public class ExpHistActuaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "archivo")
    private String archivo;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @Basic(optional = false)
    @Column(name = "actuacion")
    private Integer actuacion;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @Basic(optional = false)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    
    
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPresentacion;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "resolucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Resoluciones resolucion;
    @JoinColumn(name = "tipo_actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposActuacion tipoActuacion;
    @JoinColumn(name = "objeto_actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpObjetosActuacion objetoActuacion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = false)
    @Column(name = "fojas")
    private Integer fojas;
    @Basic(optional = false)
    @Column(name = "actuacion_padre")
    private Integer actuacionPadre;
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
    @Column(name = "borrado")
    private boolean borrado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "hash")
    private String hash;
    @Basic(optional = true)
    @Column(name = "fecha_hora_alta_original")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAltaOriginal;
    @Basic(optional = true)
    @Column(name = "fecha_hora_ultimo_estado_original")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstadoOriginal;

    public ExpHistActuaciones() {
    }

    public ExpHistActuaciones(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Integer getActuacion() {
        return actuacion;
    }

    public void setActuacion(Integer actuacion) {
        this.actuacion = actuacion;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public Resoluciones getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resoluciones resolucion) {
        this.resolucion = resolucion;
    }

    public ExpTiposActuacion getTipoActuacion() {
        return tipoActuacion;
    }

    public void setTipoActuacion(ExpTiposActuacion tipoActuacion) {
        this.tipoActuacion = tipoActuacion;
    }

    public ExpObjetosActuacion getObjetoActuacion() {
        return objetoActuacion;
    }

    public void setObjetoActuacion(ExpObjetosActuacion objetoActuacion) {
        this.objetoActuacion = objetoActuacion;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Integer getFojas() {
        return fojas;
    }

    public void setFojas(Integer fojas) {
        this.fojas = fojas;
    }

    public Integer getActuacionPadre() {
        return actuacionPadre;
    }

    public void setActuacionPadre(Integer actuacionPadre) {
        this.actuacionPadre = actuacionPadre;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public Personas getPersonaBorrado() {
        return personaBorrado;
    }

    public void setPersonaBorrado(Personas personaBorrado) {
        this.personaBorrado = personaBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Date getFechaHoraAltaOriginal() {
        return fechaHoraAltaOriginal;
    }

    public void setFechaHoraAltaOriginal(Date fechaHoraAltaOriginal) {
        this.fechaHoraAltaOriginal = fechaHoraAltaOriginal;
    }

    public Date getFechaHoraUltimoEstadoOriginal() {
        return fechaHoraUltimoEstadoOriginal;
    }

    public void setFechaHoraUltimoEstadoOriginal(Date fechaHoraUltimoEstadoOriginal) {
        this.fechaHoraUltimoEstadoOriginal = fechaHoraUltimoEstadoOriginal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof ExpHistActuaciones)) {
            return false;
        }
        ExpHistActuaciones other = (ExpHistActuaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpHistActuaciones[ id=" + id + " ]";
    }
    
}
