package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class RespuestaConsultaPdf implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CodDocumento")
    private Integer codDocumento;
    @Basic(optional = true)
    @Column(name = "CodTipoDocumentoCaso")
    private Integer codTipoDocumentoCaso;
    @Basic(optional = true)
    @Column(name = "DescripcionTipoDocumentoCaso")
    private String descripcionTipoDocumentoCaso;
    @Basic(optional = true)
    @Column(name = "CodActuacionCaso")
    private Integer codActuacionCaso;
    @Basic(optional = true)
    @Column(name = "NombreArchivo")
    private String nombreArchivo;
    @Basic(optional = true)
    @Column(name = "FechaImportacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaImportacion;
    @Basic(optional = true)
    @Lob
    @Column(name = "Documento")
    private byte[] documento;
    @Column(name = "CantidadHojas")
    private Integer cantidadHojas;
    @Basic(optional = true)
    @Column(name = "Comentario")
    private String comentario;
    @Basic(optional = true)
    @Column(name = "EstadoActuacion")
    private String estadoActuacion;
    @Basic(optional = true)
    @Lob
    @Column(name = "DocumentoFinal")
    private byte[] documentoFinal;
    @Basic(optional = true)
    @Column(name = "DocumentoTexto")
    private String documentoTexto;
    @Basic(optional = true)
    @Column(name = "ExtensionArchivo")
    private String extensionArchivo;
    @Column(name = "CodFormato")
    private Integer codFormato;
    @Basic(optional = true)
    @Column(name = "DescripcionFormato")
    private String descripcionFormato;
    @Basic(optional = true)
    @Column(name = "CodProcesoCaso")
    private Integer codProcesoCaso;
    @Basic(optional = true)
    @Column(name = "CodCodigoProceso")
    private Integer codCodigoProceso;
    @Basic(optional = true)
    @Column(name = "DescripcionProceso")
    private String descripcionProceso;
    
    
    public RespuestaConsultaPdf() {
    }

    public RespuestaConsultaPdf(Integer codDocumento, Integer codTipoDocumentoCaso, String descripcionTipoDocumentoCaso, Integer codActuacionCaso, String nombreArchivo, Date fechaImportacion, byte[] documento, Integer cantidadHojas, String comentario, String estadoActuacion, byte[] documentoFinal, String documentoTexto, String extensionArchivo, Integer codFormato, String descripcionFormato, Integer codProcesoCaso, Integer codCodigoProceso, String descripcionProceso) {
        this.codDocumento = codDocumento;
        this.codTipoDocumentoCaso = codTipoDocumentoCaso;
        this.descripcionTipoDocumentoCaso = descripcionTipoDocumentoCaso;
        this.codActuacionCaso = codActuacionCaso;
        this.nombreArchivo = nombreArchivo;
        this.fechaImportacion = fechaImportacion;
        this.documento = documento;
        this.cantidadHojas = cantidadHojas;
        this.comentario = comentario;
        this.estadoActuacion = estadoActuacion;
        this.documentoFinal = documentoFinal;
        this.documentoTexto = documentoTexto;
        this.extensionArchivo = extensionArchivo;
        this.codFormato = codFormato;
        this.descripcionFormato = descripcionFormato;
        this.codProcesoCaso = codProcesoCaso;
        this.codCodigoProceso = codCodigoProceso;
        this.descripcionProceso = descripcionProceso;
    }

    public Integer getCodDocumento() {
        return codDocumento;
    }

    public void setCodDocumento(Integer codDocumento) {
        this.codDocumento = codDocumento;
    }

    public Integer getCodTipoDocumentoCaso() {
        return codTipoDocumentoCaso;
    }

    public void setCodTipoDocumentoCaso(Integer codTipoDocumentoCaso) {
        this.codTipoDocumentoCaso = codTipoDocumentoCaso;
    }

    public String getDescripcionTipoDocumentoCaso() {
        return descripcionTipoDocumentoCaso;
    }

    public void setDescripcionTipoDocumentoCaso(String descripcionTipoDocumentoCaso) {
        this.descripcionTipoDocumentoCaso = descripcionTipoDocumentoCaso;
    }

    public Integer getCodActuacionCaso() {
        return codActuacionCaso;
    }

    public void setCodActuacionCaso(Integer codActuacionCaso) {
        this.codActuacionCaso = codActuacionCaso;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Date getFechaImportacion() {
        return fechaImportacion;
    }

    public void setFechaImportacion(Date fechaImportacion) {
        this.fechaImportacion = fechaImportacion;
    }

    public byte[] getDocumento() {
        return documento;
    }

    public void setDocumento(byte[] documento) {
        this.documento = documento;
    }

    public Integer getCantidadHojas() {
        return cantidadHojas;
    }

    public void setCantidadHojas(Integer cantidadHojas) {
        this.cantidadHojas = cantidadHojas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstadoActuacion() {
        return estadoActuacion;
    }

    public void setEstadoActuacion(String estadoActuacion) {
        this.estadoActuacion = estadoActuacion;
    }

    public byte[] getDocumentoFinal() {
        return documentoFinal;
    }

    public void setDocumentoFinal(byte[] documentoFinal) {
        this.documentoFinal = documentoFinal;
    }

    public String getDocumentoTexto() {
        return documentoTexto;
    }

    public void setDocumentoTexto(String documentoTexto) {
        this.documentoTexto = documentoTexto;
    }

    public String getExtensionArchivo() {
        return extensionArchivo;
    }

    public void setExtensionArchivo(String extensionArchivo) {
        this.extensionArchivo = extensionArchivo;
    }

    public Integer getCodFormato() {
        return codFormato;
    }

    public void setCodFormato(Integer codFormato) {
        this.codFormato = codFormato;
    }

    public String getDescripcionFormato() {
        return descripcionFormato;
    }

    public void setDescripcionFormato(String descripcionFormato) {
        this.descripcionFormato = descripcionFormato;
    }

    public Integer getCodProcesoCaso() {
        return codProcesoCaso;
    }

    public void setCodProcesoCaso(Integer codProcesoCaso) {
        this.codProcesoCaso = codProcesoCaso;
    }

    public Integer getCodCodigoProceso() {
        return codCodigoProceso;
    }

    public void setCodCodigoProceso(Integer codCodigoProceso) {
        this.codCodigoProceso = codCodigoProceso;
    }

    public String getDescripcionProceso() {
        return descripcionProceso;
    }

    public void setDescripcionProceso(String descripcionProceso) {
        this.descripcionProceso = descripcionProceso;
    }

}
