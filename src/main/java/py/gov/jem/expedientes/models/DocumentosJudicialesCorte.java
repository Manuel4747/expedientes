package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "documentos_judiciales_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosJudicialesCorte.findAll", query = "SELECT d FROM DocumentosJudicialesCorte d ORDER BY d.fechaHoraAlta desc")
    , @NamedQuery(name = "DocumentosJudicialesCorte.findById", query = "SELECT d FROM DocumentosJudicialesCorte d WHERE d.id = :id")
    , @NamedQuery(name = "DocumentosJudicialesCorte.findByCodCasoJudicial", query = "SELECT d FROM DocumentosJudicialesCorte d WHERE d.codCasoJudicial = :codCasoJudicial")
    , @NamedQuery(name = "DocumentosJudicialesCorte.findByEnvioCorteANDProveido", query = "SELECT d FROM DocumentosJudicialesCorte d WHERE d.id in (SELECT t.documentoJudicialCorte.id FROM ExpActuaciones t WHERE t.envioCorte = :envioCorte AND t.proveidoCorte = :proveidoCorte) ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudicialesCorte.findByDocumentoJudicial", query = "SELECT d FROM DocumentosJudicialesCorte d WHERE d.id in (SELECT r.documentosJudicialesCortePorDocumentosJudicialesPK.documentoJudicialCorte FROM ExpDocumentosJudicialesCortePorDocumentosJudiciales r WHERE r.documentosJudicialesCortePorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial)")})

public class DocumentosJudicialesCorte implements Serializable {

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
    @Column(name = "nro_expediente_circunscripcion")
    private Integer nroExpedienteCircunscripcion;
    @Basic(optional = true)
    @Column(name = "nro_expediente_materia")
    private Integer nroExpedienteMateria;
    @Basic(optional = true)
    @Column(name = "nro_expediente_tipo_dependencia")
    private Integer nroExpedienteTipoDependencia;
    @Basic(optional = true)
    @Column(name = "nro_expediente_nro_dependencia")
    private Integer nroExpedienteNroDependencia;
    @Basic(optional = true)
    @Column(name = "nro_expediente_anio")
    private Integer nroExpedienteAnio;
    @Basic(optional = true)
    @Column(name = "nro_expediente_numero")
    private Integer nroExpedienteNumero;
    @Basic(optional = true)
    @Column(name = "caratula")
    private String caratula;
    @Basic(optional = true)
    @Column(name = "cod_despacho_judicial")
    private Integer codDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "descripcion_despacho_judicial")
    private String descripcionDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "estado_caso_despacho")
    private String estadoCasoDespacho;
    @Basic(optional = true)
    @Column(name = "fecha_primer_acto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrimerActo;
    @Basic(optional = true)
    @Column(name = "nombre_juez")
    private String nombreJuez;
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
    @Basic(optional = true)
    @Column(name = "cod_circunscripcion")
    private String codCircunscripcion;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DepartamentosPersona departamentoPersona;
    @Basic(optional = true)
    @Column(name = "descripcion_moneda")
    private String descripcionMoneda;
    @Basic(optional = true)
    @Column(name = "monto")
    private BigDecimal monto;
    @Basic(optional = true)
    @Column(name = "proceso")
    private String proceso;
    @Basic(optional = true)
    @Column(name = "fase")
    private String fase;
    @Basic(optional = true)
    @Column(name = "numero_liquidacion")
    private String numeroLiquidacion;
    @Basic(optional = true)
    @Column(name = "auxiliar_justicia_presentacion")
    private String auxiliarJusticiaPresentacion;

    public DocumentosJudicialesCorte() {
    }

    public DocumentosJudicialesCorte(int codCasoJudicial, int nroExpedienteCircunscripcion, int nroExpedienteMateria, int nroExpedienteTipoDependencia, int nroExpedienteNroDependencia, int nroExpedienteAnio, int nroExpedienteNumero, String caratula, int codDespachoJudicial, String descripcionDespachoJudicial, String estadoCasoDespacho, Date fechaPrimerActo, String nombreJuez, Date fechaHoraAlta, Personas personaAlta, DocumentosJudiciales documentoJudicial, String codCircunscripcion, DepartamentosPersona departamentoPersona, String descripcionMoneda, BigDecimal monto, String proceso, String fase, String numeroLiquidacion, String auxiliarJusticiaPresentacion) {
        this.codCasoJudicial = codCasoJudicial;
        this.nroExpedienteCircunscripcion = nroExpedienteCircunscripcion;
        this.nroExpedienteMateria = nroExpedienteMateria;
        this.nroExpedienteTipoDependencia = nroExpedienteTipoDependencia;
        this.nroExpedienteNroDependencia = nroExpedienteNroDependencia;
        this.nroExpedienteAnio = nroExpedienteAnio;
        this.nroExpedienteNumero = nroExpedienteNumero;
        this.caratula = caratula;
        this.codDespachoJudicial = codDespachoJudicial;
        this.descripcionDespachoJudicial = descripcionDespachoJudicial;
        this.estadoCasoDespacho = estadoCasoDespacho;
        this.fechaPrimerActo = fechaPrimerActo;
        this.nombreJuez = nombreJuez;
        this.fechaHoraAlta = fechaHoraAlta;
        this.personaAlta = personaAlta;
        this.documentoJudicial = documentoJudicial;
        this.codCircunscripcion = codCircunscripcion;
        this.departamentoPersona = departamentoPersona;
        this.descripcionMoneda = descripcionMoneda;
        this.monto = monto;
        this.proceso = proceso;
        this.fase = fase;
        this.numeroLiquidacion = numeroLiquidacion;
        this.auxiliarJusticiaPresentacion = auxiliarJusticiaPresentacion;
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

    public Integer getNroExpedienteCircunscripcion() {
        return nroExpedienteCircunscripcion;
    }

    public void setNroExpedienteCircunscripcion(Integer nroExpedienteCircunscripcion) {
        this.nroExpedienteCircunscripcion = nroExpedienteCircunscripcion;
    }

    public Integer getNroExpedienteMateria() {
        return nroExpedienteMateria;
    }

    public void setNroExpedienteMateria(Integer nroExpedienteMateria) {
        this.nroExpedienteMateria = nroExpedienteMateria;
    }

    public Integer getNroExpedienteTipoDependencia() {
        return nroExpedienteTipoDependencia;
    }

    public void setNroExpedienteTipoDependencia(Integer nroExpedienteTipoDependencia) {
        this.nroExpedienteTipoDependencia = nroExpedienteTipoDependencia;
    }

    public Integer getNroExpedienteNroDependencia() {
        return nroExpedienteNroDependencia;
    }

    public void setNroExpedienteNroDependencia(Integer nroExpedienteNroDependencia) {
        this.nroExpedienteNroDependencia = nroExpedienteNroDependencia;
    }

    public Integer getNroExpedienteAnio() {
        return nroExpedienteAnio;
    }

    public void setNroExpedienteAnio(Integer nroExpedienteAnio) {
        this.nroExpedienteAnio = nroExpedienteAnio;
    }

    public Integer getNroExpedienteNumero() {
        return nroExpedienteNumero;
    }

    public void setNroExpedienteNumero(Integer nroExpedienteNumero) {
        this.nroExpedienteNumero = nroExpedienteNumero;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
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

    public String getEstadoCasoDespacho() {
        return estadoCasoDespacho;
    }

    public void setEstadoCasoDespacho(String estadoCasoDespacho) {
        this.estadoCasoDespacho = estadoCasoDespacho;
    }

    public Date getFechaPrimerActo() {
        return fechaPrimerActo;
    }

    public void setFechaPrimerActo(Date fechaPrimerActo) {
        this.fechaPrimerActo = fechaPrimerActo;
    }

    public String getNombreJuez() {
        return nombreJuez;
    }

    public void setNombreJuez(String nombreJuez) {
        this.nombreJuez = nombreJuez;
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

    public String getCodCircunscripcion() {
        return codCircunscripcion;
    }

    public void setCodCircunscripcion(String codCircunscripcion) {
        this.codCircunscripcion = codCircunscripcion;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public String getDescripcionMoneda() {
        return descripcionMoneda;
    }

    public void setDescripcionMoneda(String descripcionMoneda) {
        this.descripcionMoneda = descripcionMoneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getNumeroLiquidacion() {
        return numeroLiquidacion;
    }

    public void setNumeroLiquidacion(String numeroLiquidacion) {
        this.numeroLiquidacion = numeroLiquidacion;
    }

    public String getAuxiliarJusticiaPresentacion() {
        return auxiliarJusticiaPresentacion;
    }

    public void setAuxiliarJusticiaPresentacion(String auxiliarJusticiaPresentacion) {
        this.auxiliarJusticiaPresentacion = auxiliarJusticiaPresentacion;
    }

}
