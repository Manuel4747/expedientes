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
@Table(name = "exp_personas_acusacion_por_documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPersonasAcusacionPorDocumentosJudiciales.findAll", query = "SELECT r FROM ExpPersonasAcusacionPorDocumentosJudiciales r")
    , @NamedQuery(name = "ExpPersonasAcusacionPorDocumentosJudiciales.findByPersona", query = "SELECT r FROM ExpPersonasAcusacionPorDocumentosJudiciales r WHERE r.personasAcusacionPorDocumentosJudicialesPK.persona = :persona")
    , @NamedQuery(name = "ExpPersonasAcusacionPorDocumentosJudiciales.findByDocumentoJudicial", query = "SELECT r FROM ExpPersonasAcusacionPorDocumentosJudiciales r WHERE r.personasAcusacionPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial")
    , @NamedQuery(name = "ExpPersonasAcusacionPorDocumentosJudiciales.findByDocumentoJudicialEstadoTipoParte", query = "SELECT r FROM ExpPersonasAcusacionPorDocumentosJudiciales r WHERE r.personasAcusacionPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.estado = :estado AND r.tipoParte.id = :tipoParte ")
    , @NamedQuery(name = "ExpPersonasAcusacionPorDocumentosJudiciales.findByDocumentoJudicialANDTipoParte", query = "SELECT r FROM ExpPersonasAcusacionPorDocumentosJudiciales r WHERE r.personasAcusacionPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial and r.tipoParte = :tipoParte")})
public class ExpPersonasAcusacionPorDocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpPersonasAcusacionPorDocumentosJudicialesPK personasAcusacionPorDocumentosJudicialesPK;
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
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaBorrado;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "persona", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ExpPersonasAcusacion persona;
    @JoinColumn(name = "tipo_parte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposParte tipoParte;

    public ExpPersonasAcusacionPorDocumentosJudiciales() {
    }

    public ExpPersonasAcusacionPorDocumentosJudiciales(ExpPersonasAcusacionPorDocumentosJudicialesPK personasAcusacionPorDocumentosJudicialesPK) {
        this.personasAcusacionPorDocumentosJudicialesPK = personasAcusacionPorDocumentosJudicialesPK;
    }

    public ExpPersonasAcusacionPorDocumentosJudiciales(ExpPersonasAcusacionPorDocumentosJudicialesPK personasAcusacionPorDocumentosJudicialesPK, ExpPersonasAcusacion persona, DocumentosJudiciales documentoJudicial) {
        this.personasAcusacionPorDocumentosJudicialesPK = personasAcusacionPorDocumentosJudicialesPK;
        this.persona = persona;
        this.documentoJudicial = documentoJudicial;
    }

    public ExpPersonasAcusacionPorDocumentosJudiciales(ExpPersonasAcusacionPorDocumentosJudicialesPK personasAcusacionPorDocumentosJudicialesPK, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.personasAcusacionPorDocumentosJudicialesPK = personasAcusacionPorDocumentosJudicialesPK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public ExpPersonasAcusacionPorDocumentosJudiciales(int persona, int documentoJudicial) {
        this.personasAcusacionPorDocumentosJudicialesPK = new ExpPersonasAcusacionPorDocumentosJudicialesPK(persona, documentoJudicial);
    }
/*
    public ExpPersonasAcusacionPorDocumentosJudiciales(Personas persona, DocumentosJudiciales documentoJudicial, ExpTiposParte tipoParte) {
        this.personasPorDocumentosJudicialesPK = new ExpPersonasAcusacionPorDocumentosJudicialesPK(persona.getId(), documentoJudicial.getId());
        this.persona = persona;
        this.documentosJudiciales = documentoJudicial;
        this.tipoParte = tipoParte;
    }
*/

    public ExpPersonasAcusacionPorDocumentosJudicialesPK getPartesPorDocumentosJudicialesPK() {
        return personasAcusacionPorDocumentosJudicialesPK;
    }

    public void setPartesPorDocumentosJudicialesPK(ExpPersonasAcusacionPorDocumentosJudicialesPK personasAcusacionPorDocumentosJudicialesPK) {
        this.personasAcusacionPorDocumentosJudicialesPK = personasAcusacionPorDocumentosJudicialesPK;
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

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentosJudiciales) {
        this.documentoJudicial = documentosJudiciales;
    }

    public ExpPersonasAcusacionPorDocumentosJudicialesPK getPersonasAcusacionPorDocumentosJudicialesPK() {
        return personasAcusacionPorDocumentosJudicialesPK;
    }

    public void setPersonasAcusacionPorDocumentosJudicialesPK(ExpPersonasAcusacionPorDocumentosJudicialesPK personasAcusacionPorDocumentosJudicialesPK) {
        this.personasAcusacionPorDocumentosJudicialesPK = personasAcusacionPorDocumentosJudicialesPK;
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

    public ExpPersonasAcusacion getPersona() {
        return persona;
    }

    public void setPersona(ExpPersonasAcusacion persona) {
        this.persona = persona;
    }

    public ExpTiposParte getTipoParte() {
        return tipoParte;
    }

    public void setTipoParte(ExpTiposParte tipoParte) {
        this.tipoParte = tipoParte;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personasAcusacionPorDocumentosJudicialesPK != null ? personasAcusacionPorDocumentosJudicialesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpPersonasAcusacionPorDocumentosJudiciales)) {
            return false;
        }
        ExpPersonasAcusacionPorDocumentosJudiciales other = (ExpPersonasAcusacionPorDocumentosJudiciales) object;
        if ((this.personasAcusacionPorDocumentosJudicialesPK == null && other.personasAcusacionPorDocumentosJudicialesPK != null) || (this.personasAcusacionPorDocumentosJudicialesPK != null && !this.personasAcusacionPorDocumentosJudicialesPK.equals(other.personasAcusacionPorDocumentosJudicialesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpPersonasAcusacionPorDocumentosJudiciales[ personasAcusacionPorDocumentosJudicialesPK=" + personasAcusacionPorDocumentosJudicialesPK + " ]";
    }
    
}
