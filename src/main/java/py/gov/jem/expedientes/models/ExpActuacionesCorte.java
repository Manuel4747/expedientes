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

@Entity
@Table(name = "exp_actuaciones_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpActuacionesCorte.findAll", query = "SELECT t FROM ExpActuacionesCorte t")
    , @NamedQuery(name = "ExpActuacionesCorte.findByCodCasoJudicial", query = "SELECT t FROM ExpActuacionesCorte t WHERE t.codCasoJudicial = :codCasoJudicial")
    , @NamedQuery(name = "ExpActuacionesCorte.findByCodActuacionCaso", query = "SELECT t FROM ExpActuacionesCorte t WHERE t.codActuacionCaso = :codActuacionCaso")
    , @NamedQuery(name = "ExpActuacionesCorte.findByDocumentoJudicial", query = "SELECT t FROM ExpActuacionesCorte t WHERE t.documentoJudicial = :documentoJudicial")
    , @NamedQuery(name = "ExpActuacionesCorte.findByDocumentoJudicialANDCodProcesoCaso", query = "SELECT t FROM ExpActuacionesCorte t, ExpProcesosDocumentoJudicialCorte p WHERE t.procesoDocumentoJudicialCorte.id = p.id AND t.documentoJudicial = :documentoJudicial AND p.codProcesoCaso = :codProcesoCaso")
    , @NamedQuery(name = "ExpActuacionesCorte.findByDocumentoJudicialCorte", query = "SELECT t FROM ExpActuacionesCorte t WHERE t.documentoJudicialCorte = :documentoJudicialCorte")
    , @NamedQuery(name = "ExpActuacionesCorte.findById", query = "SELECT t FROM ExpActuacionesCorte t WHERE t.id = :id")})

public class ExpActuacionesCorte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "cod_caso_judicial")
    private Integer codCasoJudicial;
    @Basic(optional = true)
    @Column(name = "cod_actuacion_caso")
    private Integer codActuacionCaso;
    @Basic(optional = true)
    @Column(name = "fecha_actuacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActuacion;
    @Basic(optional = true)
    @Column(name = "fecha_hora_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRegistro;
    @Basic(optional = true)
    @Column(name = "cod_despacho_judicial")
    private Integer codDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "descripcion_despacho_judicial")
    private String descripcionDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "descripcion_tipo_actuacion")
    private String descripcionTipoActuacion;
    @Basic(optional = true)
    @Column(name = "descripcion_actuacion")
    private String descripcionActuacion;
    @Basic(optional = true)
    @Column(name = "caratula")
    private String caratula;
    @Basic(optional = true)
    @Column(name = "numero_actuacion")
    private Integer numeroActuacion;
    @Basic(optional = true)
    @Column(name = "nro_expediente_numero")
    private Integer nroExpedienteNumero;
    @Basic(optional = true)
    @Column(name = "nro_expediente_anio")
    private Integer nroExpedienteAnio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "documento_judicial_corte", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudicialesCorte documentoJudicialCorte;
    @JoinColumn(name = "proceso_documento_judicial_corte", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ExpProcesosDocumentoJudicialCorte procesoDocumentoJudicialCorte;

    public ExpActuacionesCorte() {
    }

    public ExpActuacionesCorte(Integer codCasoJudicial, Integer codActuacionCaso, Date fechaActuacion, Date fechaHoraRegistro, Integer codDespachoJudicial, String descripcionDespachoJudicial, String descripcionTipoActuacion, String descripcionActuacion, String caratula, Integer numeroActuacion, Integer nroExpedienteNumero, Integer nroExpedienteAnio, Date fechaHoraAlta, Personas personaAlta, DocumentosJudiciales documentoJudicial, DocumentosJudicialesCorte documentoJudicialCorte, ExpProcesosDocumentoJudicialCorte procesoDocumentoJudicialCorte) {
        this.codCasoJudicial = codCasoJudicial;
        this.codActuacionCaso = codActuacionCaso;
        this.fechaActuacion = fechaActuacion;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.codDespachoJudicial = codDespachoJudicial;
        this.descripcionDespachoJudicial = descripcionDespachoJudicial;
        this.descripcionTipoActuacion = descripcionTipoActuacion;
        this.descripcionActuacion = descripcionActuacion;
        this.caratula = caratula;
        this.nroExpedienteNumero = nroExpedienteNumero;
        this.nroExpedienteAnio = nroExpedienteAnio;
        this.fechaHoraAlta = fechaHoraAlta;
        this.personaAlta = personaAlta;
        this.documentoJudicial = documentoJudicial;
        this.documentoJudicialCorte = documentoJudicialCorte;
        this.procesoDocumentoJudicialCorte = procesoDocumentoJudicialCorte;
    }

    public String getDescripcionActuacion() {
        return descripcionActuacion;
    }

    public void setDescripcionActuacion(String descripcionActuacion) {
        this.descripcionActuacion = descripcionActuacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodCasoJudicial() {
        return codCasoJudicial;
    }

    public void setCodCasoJudicial(Integer codCasoJudicial) {
        this.codCasoJudicial = codCasoJudicial;
    }

    public Integer getCodActuacionCaso() {
        return codActuacionCaso;
    }

    public void setCodActuacionCaso(Integer codActuacionCaso) {
        this.codActuacionCaso = codActuacionCaso;
    }

    public Date getFechaActuacion() {
        return fechaActuacion;
    }

    public void setFechaActuacion(Date fechaActuacion) {
        this.fechaActuacion = fechaActuacion;
    }

    public Date getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Date fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Integer getCodDespachoJudicial() {
        return codDespachoJudicial;
    }

    public void setCodDespachoJudicial(Integer codDespachoJudicial) {
        this.codDespachoJudicial = codDespachoJudicial;
    }

    public String getDescripcionDespachoJudicial() {
        return descripcionDespachoJudicial;
    }

    public void setDescripcionDespachoJudicial(String descripcionDespachoJudicial) {
        this.descripcionDespachoJudicial = descripcionDespachoJudicial;
    }

    public String getDescripcionTipoActuacion() {
        return descripcionTipoActuacion;
    }

    public void setDescripcionTipoActuacion(String descripcionTipoActuacion) {
        this.descripcionTipoActuacion = descripcionTipoActuacion;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public Integer getNroExpedienteNumero() {
        return nroExpedienteNumero;
    }

    public void setNroExpedienteNumero(Integer nroExpedienteNumero) {
        this.nroExpedienteNumero = nroExpedienteNumero;
    }

    public Integer getNroExpedienteAnio() {
        return nroExpedienteAnio;
    }

    public void setNroExpedienteAnio(Integer nroExpedienteAnio) {
        this.nroExpedienteAnio = nroExpedienteAnio;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public DocumentosJudicialesCorte getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setDocumentoJudicialCorte(DocumentosJudicialesCorte documentoJudicialCorte) {
        this.documentoJudicialCorte = documentoJudicialCorte;
    }

    public ExpProcesosDocumentoJudicialCorte getProcesoDocumentoJudicialCorte() {
        return procesoDocumentoJudicialCorte;
    }

    public void setProcesoDocumentoJudicialCorte(ExpProcesosDocumentoJudicialCorte procesoDocumentoJudicialCorte) {
        this.procesoDocumentoJudicialCorte = procesoDocumentoJudicialCorte;
    }

    public Integer getNumeroActuacion() {
        return numeroActuacion;
    }

    public void setNumeroActuacion(Integer numeroActuacion) {
        this.numeroActuacion = numeroActuacion;
    }

}
