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
@Table(name = "resuelve_por_resoluciones_por_personas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResuelvePorResolucionesPorPersonas.findAll", query = "SELECT r FROM ResuelvePorResolucionesPorPersonas r")
    , @NamedQuery(name = "ResuelvePorResolucionesPorPersonas.findByResuelve", query = "SELECT r FROM ResuelvePorResolucionesPorPersonas r WHERE r.resuelve = :resuelve")
    , @NamedQuery(name = "ResuelvePorResolucionesPorPersonas.findByResolucion", query = "SELECT r FROM ResuelvePorResolucionesPorPersonas r WHERE r.resolucion = :resolucion")
    , @NamedQuery(name = "ResuelvePorResolucionesPorPersonas.findByResolucionEstado", query = "SELECT r FROM ResuelvePorResolucionesPorPersonas r WHERE r.resolucion = :resolucion AND r.estado = :estado")
    , @NamedQuery(name = "ResuelvePorResolucionesPorPersonas.findByPersonaEstado", query = "SELECT r FROM ResuelvePorResolucionesPorPersonas r WHERE r.persona = :persona AND r.estado = :estado")
    , @NamedQuery(name = "ResuelvePorResolucionesPorPersonas.findByPersona", query = "SELECT r FROM ResuelvePorResolucionesPorPersonas r WHERE r.resolucion = :resolucion AND r.persona = :persona")
    , @NamedQuery(name = "ResuelvePorResolucionesPorPersonas.findByDocumentoJudicialPersonaEstado", query = "SELECT r FROM ResuelvePorResolucionesPorPersonas r WHERE r.resolucion.documentoJudicial = :documentoJudicial AND r.persona = :persona AND r.estado = :estado")})
public class ResuelvePorResolucionesPorPersonas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "resolucion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Resoluciones resolucion;
    @JoinColumn(name = "resuelve", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private Resuelve resuelve;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas persona;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstado;

    public ResuelvePorResolucionesPorPersonas() {
    }

    public ResuelvePorResolucionesPorPersonas(Integer id) {
        this.id = id;
    }

    public ResuelvePorResolucionesPorPersonas(int id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Resoluciones getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resoluciones resolucion) {
        this.resolucion = resolucion;
    }

    public Resuelve getResuelve() {
        return resuelve;
    }

    public void setResuelve(Resuelve resuelve) {
        this.resuelve = resuelve;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
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
        if (!(object instanceof ResuelvePorResolucionesPorPersonas)) {
            return false;
        }
        ResuelvePorResolucionesPorPersonas other = (ResuelvePorResolucionesPorPersonas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ResuelvePorResolucionesPorPersonas[ id=" + id + " ]";
    }
    
}
