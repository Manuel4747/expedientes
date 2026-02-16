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
@Table(name = "exp_partes_por_documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPartesPorDocumentosJudiciales.findAll", query = "SELECT r FROM ExpPartesPorDocumentosJudiciales r")
    , @NamedQuery(name = "ExpPartesPorDocumentosJudiciales.findByPersona", query = "SELECT r FROM ExpPartesPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.persona = :persona")
    , @NamedQuery(name = "ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstadoTipoParte", query = "SELECT r FROM ExpPartesPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.estado = :estado AND r.tipoParte.id = :tipoParte ")
    , @NamedQuery(name = "ExpPartesPorDocumentosJudiciales.findByDocumentoJudicial", query = "SELECT r FROM ExpPartesPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial")
    , @NamedQuery(name = "ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstado", query = "SELECT r FROM ExpPartesPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.estado = :estado ORDER BY r.persona.nombresApellidos")
    , @NamedQuery(name = "ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialEstadoPersona", query = "SELECT r FROM ExpPartesPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.estado = :estado AND r.personasPorDocumentosJudicialesPK.persona = :persona")
    , @NamedQuery(name = "ExpPartesPorDocumentosJudiciales.findByDocumentoJudicialTipoParteEstado", query = "SELECT r FROM ExpPartesPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.tipoParte = :tipoParte AND r.estado = :estado ORDER BY r.persona.nombresApellidos")})
public class ExpPartesPorDocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExpPartesPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK;
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
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "persona", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "tipo_parte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposParte tipoParte;

    public ExpPartesPorDocumentosJudiciales() {
    }

    public ExpPartesPorDocumentosJudiciales(ExpPartesPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
    }

    public ExpPartesPorDocumentosJudiciales(ExpPartesPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK, Personas persona, DocumentosJudiciales documentoJudicial, ExpTiposParte tipoParte) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
        this.persona = persona;
        this.documentoJudicial = documentoJudicial;
        this.tipoParte = tipoParte;
    }

    public ExpPartesPorDocumentosJudiciales(ExpPartesPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public ExpPartesPorDocumentosJudiciales(int persona, int documentoJudicial) {
        this.personasPorDocumentosJudicialesPK = new ExpPartesPorDocumentosJudicialesPK(persona, documentoJudicial);
    }
/*
    public ExpPartesPorDocumentosJudiciales(Personas persona, DocumentosJudiciales documentoJudicial, ExpTiposParte tipoParte) {
        this.personasPorDocumentosJudicialesPK = new ExpPartesPorDocumentosJudicialesPK(persona.getId(), documentoJudicial.getId());
        this.persona = persona;
        this.documentosJudiciales = documentoJudicial;
        this.tipoParte = tipoParte;
    }
*/

    public ExpPartesPorDocumentosJudicialesPK getPartesPorDocumentosJudicialesPK() {
        return personasPorDocumentosJudicialesPK;
    }

    public void setPartesPorDocumentosJudicialesPK(ExpPartesPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK) {
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

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
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
        hash += (personasPorDocumentosJudicialesPK != null ? personasPorDocumentosJudicialesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpPartesPorDocumentosJudiciales)) {
            return false;
        }
        ExpPartesPorDocumentosJudiciales other = (ExpPartesPorDocumentosJudiciales) object;
        if ((this.personasPorDocumentosJudicialesPK == null && other.personasPorDocumentosJudicialesPK != null) || (this.personasPorDocumentosJudicialesPK != null && !this.personasPorDocumentosJudicialesPK.equals(other.personasPorDocumentosJudicialesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpPartesPorDocumentosJudiciales[ personasPorDocumentosJudicialesPK=" + personasPorDocumentosJudicialesPK + " ]";
    }
    
}
