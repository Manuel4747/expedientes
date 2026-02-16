/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "resoluciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resoluciones.findAll", query = "SELECT r FROM Resoluciones r")
    , @NamedQuery(name = "Resoluciones.findOrdered", query = "SELECT r FROM Resoluciones r WHERE r.fecha >= :fechaDesde AND r.fecha <= :fechaHasta ORDER BY r.fecha DESC, r.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "Resoluciones.findOrderedFechaAlta", query = "SELECT r FROM Resoluciones r WHERE r.fechaHoraAlta >= :fechaDesde AND r.fechaHoraAlta <= :fechaHasta ORDER BY r.fechaHoraAlta DESC")
    , @NamedQuery(name = "Resoluciones.findById", query = "SELECT r FROM Resoluciones r WHERE r.id = :id")
    , @NamedQuery(name = "Resoluciones.findByFecha", query = "SELECT r FROM Resoluciones r WHERE r.fecha = :fecha")
    , @NamedQuery(name = "Resoluciones.findByNroResolucion", query = "SELECT r FROM Resoluciones r WHERE r.nroResolucion = :nroResolucion")
    , @NamedQuery(name = "Resoluciones.findByFechaHoraAlta", query = "SELECT r FROM Resoluciones r WHERE r.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Resoluciones.findByFechaHoraUltimoEstado", query = "SELECT r FROM Resoluciones r WHERE r.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class Resoluciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = true)
    @Column(name = "fecha_sesion")
    @Temporal(TemporalType.DATE)
    private Date fechaSesion;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nro_resolucion")
    private String nroResolucion;
    @JoinColumn(name = "subtipo_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SubtiposResolucion subtipoResolucion;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "caratula")
    private String caratula;
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
    @Column(name = "resolucion_escaneada")
    private Integer resolucionEscaneada;
    @JoinColumn(name = "tipo_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposResolucion tipoResolucion;
    @JoinColumn(name = "canal_entrada_documento_judicial", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private CanalesEntradaDocumentoJudicial canalEntradaDocumentoJudicial;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentoJudicial;
    @Column(name = "firmado")
    private boolean firmado;
    @Size(min = 2, max = 2)
    @Column(name = "mostrar_web")
    private String mostrarWeb;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "archivo")
    private String archivo;
    @Basic(optional = false)
    @Column(name = "fojas")
    private Integer fojas;

    public Resoluciones() {
    }

    public Resoluciones(Integer id) {
        this.id = id;
    }

    public Resoluciones(Integer id, Date fecha, String nroResolucion, String caratula, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fecha = fecha;
        this.nroResolucion = nroResolucion;
        this.caratula = caratula;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }
    
    public Resoluciones(Resoluciones res) {
        this.id = res.id;
        this.fecha = res.fecha;
        this.nroResolucion = res.nroResolucion;
        this.caratula = res.caratula;
        this.tipoResolucion = res.tipoResolucion;
        this.resolucionEscaneada = res.resolucionEscaneada;
        this.documentoJudicial = res.documentoJudicial;
        this.firmado = res.firmado;
        this.empresa = res.empresa;
        this.usuarioAlta = res.usuarioAlta;
        this.fechaHoraAlta = res.fechaHoraAlta;
        this.usuarioUltimoEstado = res.usuarioUltimoEstado;
        this.fechaHoraUltimoEstado = res.fechaHoraUltimoEstado;
    }
    
    public Resoluciones(Integer id, 
            Date fecha, 
            String nroResolucion, 
            String caratula, 
            TiposResolucion tipoResolucion,
            Integer resolucionEscaneada,
            DocumentosJudiciales documentoJudicial,
            boolean firmado,
            Empresas empresa,
            Usuarios usuarioAlta,
            Date fechaHoraAlta, 
            Usuarios usuarioUltimoEstado,
            Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fecha = fecha;
        this.nroResolucion = nroResolucion;
        this.caratula = caratula;
        this.tipoResolucion = tipoResolucion;
        this.resolucionEscaneada = resolucionEscaneada;
        this.documentoJudicial = documentoJudicial;
        this.firmado = firmado;
        this.empresa = empresa;
        this.usuarioAlta = usuarioAlta;
        this.fechaHoraAlta = fechaHoraAlta;
        this.usuarioUltimoEstado = usuarioUltimoEstado;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TiposResolucion getTipoResolucion() {
        return tipoResolucion;
    }

    public void setTipoResolucion(TiposResolucion tipoResolucion) {
        this.tipoResolucion = tipoResolucion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaSesion() {
        return fechaSesion;
    }

    public void setFechaSesion(Date fechaSesion) {
        this.fechaSesion = fechaSesion;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }

    public String getCaratula() {
        return caratula;
    }

    @XmlTransient
    public String getCaratulaString() {
        return caratula.replace("\n", "<br />");
    }
    
    public void setCaratula(String caratula) {
        this.caratula = caratula;
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

    public Integer getResolucionEscaneada() {
        return resolucionEscaneada;
    }

    public void setResolucionEscaneada(Integer resolucionEscaneada) {
        this.resolucionEscaneada = resolucionEscaneada;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public String getMostrarWeb() {
        return mostrarWeb;
    }

    public void setMostrarWeb(String mostrarWeb) {
        this.mostrarWeb = mostrarWeb;
    }

    public SubtiposResolucion getSubtipoResolucion() {
        return subtipoResolucion;
    }

    public void setSubtipoResolucion(SubtiposResolucion subtipoResolucion) {
        this.subtipoResolucion = subtipoResolucion;
    }

    public CanalesEntradaDocumentoJudicial getCanalEntradaDocumentoJudicial() {
        return canalEntradaDocumentoJudicial;
    }

    public void setCanalEntradaDocumentoJudicial(CanalesEntradaDocumentoJudicial canalEntradaDocumentoJudicial) {
        this.canalEntradaDocumentoJudicial = canalEntradaDocumentoJudicial;
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

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public Integer getFojas() {
        return fojas;
    }

    public void setFojas(Integer fojas) {
        this.fojas = fojas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @XmlTransient
    public String getTieneDocumento() {
        if (resolucionEscaneada == null) {
            return "NO";
        } else {
            return "SI";
        }
    }

    @XmlTransient
    public String getEstaFirmado() {
        if (firmado) {
            return "SI";
        } else {
            return "NO";
        }
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resoluciones)) {
            return false;
        }
        Resoluciones other = (Resoluciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.Resoluciones[ id=" + id + " ]";
    }
    
}
