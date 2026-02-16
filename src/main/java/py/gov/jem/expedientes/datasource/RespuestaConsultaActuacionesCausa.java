package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class RespuestaConsultaActuacionesCausa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodActuacionCaso")
    private Integer codActuacionCaso;
    @Basic(optional = true)
    @Column(name = "CodCasoJudicial")
    private Integer codCasoJudicial;
    @Basic(optional = true)
    @Column(name = "FechaActuacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActuacion;
    @Column(name = "FechaHoraRegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRegistro;
    @Basic(optional = true)
    @Column(name = "DescripcionActuacion")
    private String descripcionActuacion;
    @Basic(optional = true)
    @Column(name = "CodDespachoJudicial")
    private Integer codDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "DescripcionDespachoJudicial")
    private String descripcionDespachoJudicial;
    @Basic(optional = true)
    @Column(name = "DescripcionTipoActuacion")
    private String descripcionTipoActuacion;
    @Basic(optional = true)
    @Column(name = "Caratula")
    private String caratula;
    @Basic(optional = true)
    @Column(name = "NroExpedienteNumero")
    private Integer nroExpedienteNumero;
    @Basic(optional = true)
    @Column(name = "NroExpedienteAnio")
    private Integer nroExpedienteAnio;
    @Basic(optional = true)
    @Column(name = "CodProcesoCaso")
    private Integer codProcesoCaso;
    @Basic(optional = true)
    @Column(name = "NumeroActuacion")
    private Integer numeroActuacion;
    @Basic(optional = true)
    @Column(name = "CodTipoGrupoProceso")
    private Integer codTipoGrupoProceso;
    @Basic(optional = true)
    @Column(name = "DescripcionTipoGrupoProceso")
    private String descripcionTipoGrupoProceso;
    @Basic(optional = true)
    @Column(name = "CodProcesoJudicial")
    private Integer codProcesoJudicial;
    @Basic(optional = true)
    @Column(name = "DescripcionProceso")
    private String descripcionProceso;
    @Basic(optional = true)
    @Column(name = "ProcesosDeRecursos")
    private String procesosDeRecursos;

    public RespuestaConsultaActuacionesCausa() {
    }
    
    public RespuestaConsultaActuacionesCausa(int codCasoJudicial, Integer codActuacionCaso, Date fechaActuacion,
            Date fechaHoraRegistro, String descripcionActuacion, Integer codDespachoJudicial,
            String descripcionDespachoJudicial, String descripcionTipoActuacion, String caratula, Integer nroExpedienteNumero,
            Integer nroExpedienteAnio, Integer codProcesoCaso, Integer numeroActuacion, Integer codTipoGrupoProceso, String descripcionTipoGrupoProceso, Integer codProcesoJudicial, String descripcionProceso, String procesosDeRecursos) {
        this.codCasoJudicial = codCasoJudicial;
        this.codActuacionCaso = codActuacionCaso;
        this.fechaActuacion = fechaActuacion;
        this.fechaHoraRegistro = fechaHoraRegistro;
        this.descripcionActuacion = descripcionActuacion;
        this.codDespachoJudicial = codDespachoJudicial;
        this.descripcionDespachoJudicial = descripcionDespachoJudicial;
        this.descripcionTipoActuacion = descripcionTipoActuacion;
        this.caratula = caratula;
        this.nroExpedienteNumero = nroExpedienteNumero;
        this.nroExpedienteAnio = nroExpedienteAnio;
        this.codProcesoCaso = codProcesoCaso;
        this.numeroActuacion = numeroActuacion;
        this.codTipoGrupoProceso = codTipoGrupoProceso;
        this.descripcionTipoGrupoProceso = descripcionTipoGrupoProceso;
        this.codProcesoJudicial = codProcesoJudicial;
        this.descripcionProceso = descripcionProceso;
        this.procesosDeRecursos = procesosDeRecursos;
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

    public String getDescripcionActuacion() {
        return descripcionActuacion;
    }

    public void setDescripcionActuacion(String descripcionActuacion) {
        this.descripcionActuacion = descripcionActuacion;
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

    public Integer getCodProcesoCaso() {
        return codProcesoCaso;
    }

    public void setCodProcesoCaso(Integer codProcesoCaso) {
        this.codProcesoCaso = codProcesoCaso;
    }

    public Integer getNumeroActuacion() {
        return numeroActuacion;
    }

    public void setNumeroActuacion(Integer numeroActuacion) {
        this.numeroActuacion = numeroActuacion;
    }

    public Integer getCodTipoGrupoProceso() {
        return codTipoGrupoProceso;
    }

    public void setCodTipoGrupoProceso(Integer codTipoGrupoProceso) {
        this.codTipoGrupoProceso = codTipoGrupoProceso;
    }

    public String getDescripcionTipoGrupoProceso() {
        return descripcionTipoGrupoProceso;
    }

    public void setDescripcionTipoGrupoProceso(String descripcionTipoGrupoProceso) {
        this.descripcionTipoGrupoProceso = descripcionTipoGrupoProceso;
    }

    public Integer getCodProcesoJudicial() {
        return codProcesoJudicial;
    }

    public void setCodProcesoJudicial(Integer codProcesoJudicial) {
        this.codProcesoJudicial = codProcesoJudicial;
    }

    public String getDescripcionProceso() {
        return descripcionProceso;
    }

    public void setDescripcionProceso(String descripcionProceso) {
        this.descripcionProceso = descripcionProceso;
    }

    public String getProcesosDeRecursos() {
        return procesosDeRecursos;
    }

    public void setProcesosDeRecursos(String procesosDeRecursos) {
        this.procesosDeRecursos = procesosDeRecursos;
    }
    
}
