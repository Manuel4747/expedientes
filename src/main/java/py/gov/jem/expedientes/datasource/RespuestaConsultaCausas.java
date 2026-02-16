package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import py.gov.jem.expedientes.models.DocumentosJudicialesCorte;

@Entity
public class RespuestaConsultaCausas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodCasoJudicial")
    private Integer codCasoJudicial;
    @Basic(optional = true)
    @Column(name = "NroExpedienteCircunscripcion")
    private Integer nroExpedienteCircunscripcion;
    @Basic(optional = true)
    @Column(name = "NroExpedienteMateria")
    private Integer nroExpedienteMateria;
    @Basic(optional = true)
    @Column(name = "NroExpedienteTipoDependencia")
    private Integer nroExpedienteTipoDependencia;
    @Basic(optional = true)
    @Column(name = "NroExpedienteNroDependencia")
    private Integer nroExpedienteNroDependencia;
    @Basic(optional = true)
    @Column(name = "NroExpedienteAnio")
    private Integer nroExpedienteAnio;
    @Basic(optional = true)
    @Column(name = "NroExpedienteNumero")
    private Integer nroExpedienteNumero;
    @Basic(optional = true)
    @Column(name = "Caratula")
    private String caratula;
    @Basic(optional = true)
    @Column(name = "CodDespachoJudicial")
    private Integer codDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "DescripcionDespachoJudicial")
    private String descripcionDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "EstadoCasoDespacho")
    private String estadoCasoDespacho;
    @Basic(optional = true)
    @Column(name = "FechaPrimerActo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPrimerActo;
    @Basic(optional = true)
    @Column(name = "NombreJuez")
    private String nombreJuez;
    @Basic(optional = true)
    @Column(name = "DescripcionMoneda")
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
    @Column(name = "NumeroLiquidacion")
    private String numeroLiquidacion;
    @Basic(optional = true)
    @Column(name = "AuxiliarJusticiaPresentacion")
    private String auxiliarJusticiaPresentacion;
    @Transient
    private DocumentosJudicialesCorte documentoJudicialCorte;
    
    public RespuestaConsultaCausas() {
    }

    public RespuestaConsultaCausas(int codCasoJudicial, int nroExpedienteCircunscripcion, int nroExpedienteMateria, int nroExpedienteTipoDependencia, int nroExpedienteNroDependencia, int nroExpedienteAnio, int nroExpedienteNumero, String caratula, int codDespachoJudicial, String descripcionDespachoJudicial, String estadoCasoDespacho, Date fechaPrimerActo, String nombreJuez, String descripcionMoneda, BigDecimal monto, String proceso, String fase, String numeroLiquidacion, String auxiliarJusticiaPresentacion) {
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
        this.descripcionMoneda = descripcionMoneda;
        this.monto = monto;
        this.proceso = proceso;
        this.fase = fase;
        this.numeroLiquidacion = numeroLiquidacion;
        this.auxiliarJusticiaPresentacion = auxiliarJusticiaPresentacion;
    }
    
    public RespuestaConsultaCausas(int codCasoJudicial, int nroExpedienteCircunscripcion, int nroExpedienteMateria, int nroExpedienteTipoDependencia, int nroExpedienteNroDependencia, int nroExpedienteAnio, int nroExpedienteNumero, String caratula, int codDespachoJudicial, String descripcionDespachoJudicial, String estadoCasoDespacho, Date fechaPrimerActo, String nombreJuez, String descripcionMoneda, BigDecimal monto, String proceso, String fase, String numeroLiquidacion, String auxiliarJusticiaPresentacion, DocumentosJudicialesCorte documentoJudicialCorte) {
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
        this.descripcionMoneda = descripcionMoneda;
        this.monto = monto;
        this.proceso = proceso;
        this.fase = fase;
        this.numeroLiquidacion = numeroLiquidacion;
        this.auxiliarJusticiaPresentacion = auxiliarJusticiaPresentacion;
        this.documentoJudicialCorte = documentoJudicialCorte;
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

    public DocumentosJudicialesCorte getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setDocumentoJudicialCorte(DocumentosJudicialesCorte documentoJudicialCorte) {
        this.documentoJudicialCorte = documentoJudicialCorte;
    }
    
}
