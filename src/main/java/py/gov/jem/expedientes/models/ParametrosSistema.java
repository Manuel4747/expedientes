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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "parametros_sistema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametrosSistema.findAll", query = "SELECT p FROM ParametrosSistema p")
    , @NamedQuery(name = "ParametrosSistema.findById", query = "SELECT p FROM ParametrosSistema p WHERE p.id = :id")})
public class ParametrosSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "ip_servidor")
    private String ipServidor;
    @Size(max = 5)
    @Column(name = "puerto_servidor")
    private String puertoServidor;
    @Size(max = 100)
    @Column(name = "ip_servidor_interno")
    private String ipServidorInterno;
    @Size(max = 5)
    @Column(name = "puerto_servidor_interno")
    private String puertoServidorInterno;
    @Column(name = "ip_servidor_email")
    private String ipServidorEmail;
    @Size(max = 5)
    @Column(name = "puerto_servidor_email")
    private String puertoServidorEmail;
    @Size(max = 100)
    @Column(name = "usuario_servidor_email")
    private String usuarioServidorEmail;
    @Size(max = 100)
    @Column(name = "contrasena_servidor_email")
    private String contrasenaServidorEmail;
    @Size(max = 200)
    @Column(name = "ruta_recursos")
    private String rutaRecursos;
    @Size(max = 20)
    @Column(name = "layout_menu_por_omision")
    private String layoutMenuPorOmision;
    @Size(max = 20)
    @Column(name = "tema_por_omision")
    private String temaPorOmision;
    @Size(max = 200)
    @Column(name = "ruta_antecedentes")
    private String rutaAntecedentes;
    @Size(max = 200)
    @Column(name = "ruta_archivos")
    private String rutaArchivos;
    @Size(max = 200)
    @Column(name = "ruta_archivos_pedidos_persona")
    private String rutaArchivosPedidosPersona;
    @Size(max = 200)
    @Column(name = "ruta_plantillas")
    private String rutaPlantillas;
    @Size(max = 20)
    @Column(name = "protocolo")
    private String protocolo;
    @Size(max = 20)
    @Column(name = "protocolo_interno")
    private String protocoloInterno;
    @Size(max = 20)
    @Column(name = "ultimo_nro_causa")
    private String ultimoNroCausa;
    @Column(name = "hora_inicio")
    private Integer horaInicio;
    @Column(name = "hora_fin")
    private Integer horaFin;
    @Column(name = "minuto_inicio")
    private Integer minutoInicio;
    @Column(name = "minuto_fin")
    private Integer minutoFin;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "formato_actuaciones")
    private String formatoActuaciones;
    @Size(max = 200)
    @Column(name = "ruta_actas")
    private String rutaActas;
    @Size(max = 200)
    @Column(name = "ruta_archivos_corte")
    private String rutaArchivosCorte;
    @Basic(optional = true)
    @Column(name = "tipo_circuito_firma_actuaciones")
    private Integer tipoCircuitoFirmaActuaciones;
    @Basic(optional = true)
    @Column(name = "fecha_hora_reseteo_sorteo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraReseteoSorteo;
    @Basic(optional = true)
    @Column(name = "sorteo_modo_debug")
    private boolean sorteoModoDebug;
    @Size(max = 200)
    @Column(name = "ruta_antecedentes_pendientes")
    private String rutaAntecedentesPendientes;
    @Size(max = 200)
    @Column(name = "ruta_avisos")
    private String rutaAvisos;


    public ParametrosSistema() {
    }

    public ParametrosSistema(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSorteoModoDebug() {
        return sorteoModoDebug;
    }

    public void setSorteoModoDebug(boolean sorteoModoDebug) {
        this.sorteoModoDebug = sorteoModoDebug;
    }

    public Date getFechaHoraReseteoSorteo() {
        return fechaHoraReseteoSorteo;
    }

    public void setFechaHoraReseteoSorteo(Date fechaHoraReseteoSorteo) {
        this.fechaHoraReseteoSorteo = fechaHoraReseteoSorteo;
    }
    
    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public String getPuertoServidor() {
        return puertoServidor;
    }

    public void setPuertoServidor(String puertoServidor) {
        this.puertoServidor = puertoServidor;
    }

    public String getRutaRecursos() {
        return rutaRecursos;
    }

    public void setRutaRecursos(String rutaRecursos) {
        this.rutaRecursos = rutaRecursos;
    }

    public String getLayoutMenuPorOmision() {
        return layoutMenuPorOmision;
    }

    public void setLayoutMenuPorOmision(String layoutMenuPorOmision) {
        this.layoutMenuPorOmision = layoutMenuPorOmision;
    }

    public String getTemaPorOmision() {
        return temaPorOmision;
    }

    public void setTemaPorOmision(String temaPorOmision) {
        this.temaPorOmision = temaPorOmision;
    }

    public String getIpServidorEmail() {
        return ipServidorEmail;
    }

    public void setIpServidorEmail(String ipServidorEmail) {
        this.ipServidorEmail = ipServidorEmail;
    }

    public String getPuertoServidorEmail() {
        return puertoServidorEmail;
    }

    public void setPuertoServidorEmail(String puertoServidorEmail) {
        this.puertoServidorEmail = puertoServidorEmail;
    }

    public String getUsuarioServidorEmail() {
        return usuarioServidorEmail;
    }

    public void setUsuarioServidorEmail(String usuarioServidorEmail) {
        this.usuarioServidorEmail = usuarioServidorEmail;
    }

    public String getContrasenaServidorEmail() {
        return contrasenaServidorEmail;
    }

    public void setContrasenaServidorEmail(String contrasenaServidorEmail) {
        this.contrasenaServidorEmail = contrasenaServidorEmail;
    }

    public String getRutaAntecedentes() {
        return rutaAntecedentes;
    }

    public void setRutaAntecedentes(String rutaAntecedentes) {
        this.rutaAntecedentes = rutaAntecedentes;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getUltimoNroCausa() {
        return ultimoNroCausa;
    }

    public void setUltimoNroCausa(String ultimoNroCausa) {
        this.ultimoNroCausa = ultimoNroCausa;
    }

    public String getRutaArchivos() {
        return rutaArchivos;
    }

    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Integer horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getMinutoInicio() {
        return minutoInicio;
    }

    public void setMinutoInicio(Integer minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    public Integer getMinutoFin() {
        return minutoFin;
    }

    public void setMinutoFin(Integer minutoFin) {
        this.minutoFin = minutoFin;
    }

    public String getRutaArchivosPedidosPersona() {
        return rutaArchivosPedidosPersona;
    }

    public void setRutaArchivosPedidosPersona(String rutaArchivosPedidosPersona) {
        this.rutaArchivosPedidosPersona = rutaArchivosPedidosPersona;
    }

    public String getRutaPlantillas() {
        return rutaPlantillas;
    }

    public void setRutaPlantillas(String rutaPlantillas) {
        this.rutaPlantillas = rutaPlantillas;
    }

    public String getFormatoActuaciones() {
        return formatoActuaciones;
    }

    public void setFormatoActuaciones(String formatoActuaciones) {
        this.formatoActuaciones = formatoActuaciones;
    }

    public String getRutaActas() {
        return rutaActas;
    }

    public void setRutaActas(String rutaActas) {
        this.rutaActas = rutaActas;
    }

    public Integer getTipoCircuitoFirmaActuaciones() {
        return tipoCircuitoFirmaActuaciones;
    }

    public void setTipoCircuitoFirmaActuaciones(Integer tipoCircuitoFirmaActuaciones) {
        this.tipoCircuitoFirmaActuaciones = tipoCircuitoFirmaActuaciones;
    }

    public String getRutaArchivosCorte() {
        return rutaArchivosCorte;
    }

    public void setRutaArchivosCorte(String rutaArchivosCorte) {
        this.rutaArchivosCorte = rutaArchivosCorte;
    }

    public String getRutaAntecedentesPendientes() {
        return rutaAntecedentesPendientes;
    }

    public void setRutaAntecedentesPendientes(String rutaAntecedentesPendientes) {
        this.rutaAntecedentesPendientes = rutaAntecedentesPendientes;
    }

    public String getIpServidorInterno() {
        return ipServidorInterno;
    }

    public void setIpServidorInterno(String ipServidorInterno) {
        this.ipServidorInterno = ipServidorInterno;
    }

    public String getPuertoServidorInterno() {
        return puertoServidorInterno;
    }

    public void setPuertoServidorInterno(String puertoServidorInterno) {
        this.puertoServidorInterno = puertoServidorInterno;
    }

    public String getProtocoloInterno() {
        return protocoloInterno;
    }

    public void setProtocoloInterno(String protocoloInterno) {
        this.protocoloInterno = protocoloInterno;
    }

    public String getRutaAvisos() {
        return rutaAvisos;
    }

    public void setRutaAvisos(String rutaAvisos) {
        this.rutaAvisos = rutaAvisos;
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
        if (!(object instanceof ParametrosSistema)) {
            return false;
        }
        ParametrosSistema other = (ParametrosSistema) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ParametrosSistema[ id=" + id + " ]";
    }
    
}
