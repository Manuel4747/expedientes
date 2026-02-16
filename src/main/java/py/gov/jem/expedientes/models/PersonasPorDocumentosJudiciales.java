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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "personas_por_documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonasPorDocumentosJudiciales.findAll", query = "SELECT r FROM PersonasPorDocumentosJudiciales r")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByPersona", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.persona = :persona")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByPersonaEstado", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.persona = :persona AND r.estado = :estado")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByPersonaAltaEstado", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personaAlta = :personaAlta AND r.estado = :estado")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByDocumentoJudicialPersona", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.personasPorDocumentosJudicialesPK.persona = :persona")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.estado = :estado ORDER BY r.persona.nombresApellidos")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstadoPersona", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.estado = :estado AND r.personasPorDocumentosJudicialesPK.persona = :persona")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByDocumentoJudicial", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial ")})
public class PersonasPorDocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK;
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
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "persona", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaAlta;
    @Column(name = "tipo_expediente_anterior")
    private boolean tipoExpedienteAnterior;

    public PersonasPorDocumentosJudiciales() {
    }

    public PersonasPorDocumentosJudiciales(PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
    }

    public PersonasPorDocumentosJudiciales(PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public PersonasPorDocumentosJudiciales(int persona, int documentoJudicial) {
        this.personasPorDocumentosJudicialesPK = new PersonasPorDocumentosJudicialesPK(persona, documentoJudicial);
    }

    public PersonasPorDocumentosJudicialesPK getPersonasPorDocumentosJudicialesPK() {
        return personasPorDocumentosJudicialesPK;
    }

    public void setPersonasPorDocumentosJudicialesPK(PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
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

    public boolean isTipoExpedienteAnterior() {
        return tipoExpedienteAnterior;
    }

    public void setTipoExpedienteAnterior(boolean tipoExpedienteAnterior) {
        this.tipoExpedienteAnterior = tipoExpedienteAnterior;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personasPorDocumentosJudicialesPK != null ? personasPorDocumentosJudicialesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonasPorDocumentosJudiciales)) {
            return false;
        }
        PersonasPorDocumentosJudiciales other = (PersonasPorDocumentosJudiciales) object;
        if ((this.personasPorDocumentosJudicialesPK == null && other.personasPorDocumentosJudicialesPK != null) || (this.personasPorDocumentosJudicialesPK != null && !this.personasPorDocumentosJudicialesPK.equals(other.personasPorDocumentosJudicialesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.PersonasPorDocumentosJudiciales[ personasPorDocumentosJudicialesPK=" + personasPorDocumentosJudicialesPK + " ]";
    }
    
}
