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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_procesos_documento_judicial_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findAll", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r")
    , @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findById", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r WHERE r.id = :id")
    , @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findByDescripcion", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findByDocumentoJudicialCorte", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r WHERE r.documentoJudicialCorte = :documentoJudicialCorte")
    , @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findByDocumentoJudicial", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r WHERE r.documentoJudicial = :documentoJudicial")
    , @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findByCodCasoJudicialANDCodProcesoCasoANDDocumentoJudicial", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r WHERE r.codCasoJudicial = :codCasoJudicial AND r.codProcesoCaso = :codProcesoCaso AND r.documentoJudicial = :documentoJudicial")
    , @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findByFechaHoraAlta", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ExpProcesosDocumentoJudicialCorte.findByFechaHoraUltimoEstado", query = "SELECT r FROM ExpProcesosDocumentoJudicialCorte r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ExpProcesosDocumentoJudicialCorte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "documento_judicial_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudicialesCorte documentoJudicialCorte;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "proceso_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpProcesosCorte procesoCorte;
    @JoinColumn(name = "grupo_proceso_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpGruposProcesoCorte grupoProcesoCorte;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
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
    @Column(name = "cod_caso_judicial")
    private Integer codCasoJudicial;
    @Basic(optional = true)
    @Column(name = "cod_proceso_caso")
    private Integer codProcesoCaso;

    public ExpProcesosDocumentoJudicialCorte() {
    }

    public ExpProcesosDocumentoJudicialCorte(Integer id) {
        this.id = id;
    }

    public ExpProcesosDocumentoJudicialCorte(Integer id, String descripcion, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public DocumentosJudicialesCorte getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setDocumentoJudicialCorte(DocumentosJudicialesCorte documentoJudicialCorte) {
        this.documentoJudicialCorte = documentoJudicialCorte;
    }

    public ExpProcesosCorte getProcesoCorte() {
        return procesoCorte;
    }

    public void setProcesoCorte(ExpProcesosCorte procesoCorte) {
        this.procesoCorte = procesoCorte;
    }

    public ExpGruposProcesoCorte getGrupoProcesoCorte() {
        return grupoProcesoCorte;
    }

    public void setGrupoProcesoCorte(ExpGruposProcesoCorte grupoProcesoCorte) {
        this.grupoProcesoCorte = grupoProcesoCorte;
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

    public Integer getCodCasoJudicial() {
        return codCasoJudicial;
    }

    public void setCodCasoJudicial(Integer codCasoJudicial) {
        this.codCasoJudicial = codCasoJudicial;
    }

    public Integer getCodProcesoCaso() {
        return codProcesoCaso;
    }

    public void setCodProcesoCaso(Integer codProcesoCaso) {
        this.codProcesoCaso = codProcesoCaso;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
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
        if (!(object instanceof ExpProcesosDocumentoJudicialCorte)) {
            return false;
        }
        ExpProcesosDocumentoJudicialCorte other = (ExpProcesosDocumentoJudicialCorte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.ExpProcesosDocumentoJudicialCorte[ id=" + id + " ]";
    }
    
}
