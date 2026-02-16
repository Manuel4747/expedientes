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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_actuaciones")
@XmlRootElement
@NamedQueries({ // ((select count(a.id) from ExpPersonasFirmantesPorActuaciones a WHERE a.documentoJudicial.id = d.id and a.estado.codigo = 'AC' AND a.personaFirmante = :personaFirmante AND a.actuacion.estado.codigo in ('FM', 'RP', 'FP')) > :limite)
    @NamedQuery(name = "ExpActuaciones.findAll", query = "SELECT t FROM ExpActuaciones t")
    , @NamedQuery(name = "ExpActuaciones.findById", query = "SELECT t FROM ExpActuaciones t WHERE t.id = :id")    , @NamedQuery(name = "ExpActuaciones.findByPersonaAlta", query = "SELECT t FROM ExpActuaciones t WHERE t.personaAlta = :personaAlta")
    , @NamedQuery(name = "ExpActuaciones.findByActaSesion", query = "SELECT t FROM ExpActuaciones t WHERE t.actaSesion = :actaSesion ORDER BY t.fechaPresentacion")
    , @NamedQuery(name = "ExpActuaciones.findParaLaFirma", query = "SELECT t.actuacion FROM ExpPersonasFirmantesPorActuaciones t, ExpActuaciones a WHERE t.actuacion.id = a.id and t.estado = :estado AND t.firmado = false and a.formato is null ORDER BY t.actuacion.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByHash", query = "SELECT t FROM ExpActuaciones t WHERE t.hash = :hash")
    , @NamedQuery(name = "ExpActuaciones.findByDestinatarioTipoActuacion", query = "SELECT t FROM ExpActuaciones t WHERE t.destinatario = :destinatario and t.tipoActuacion.id = :tipoActuacion AND t.visible = true ORDER BY t.leido, t.fechaPresentacion DESC, t.fechaHoraAlta DESC, t.id DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDestinatarioTipoActuacionFechaPresentacion", query = "SELECT t FROM ExpActuaciones t WHERE t.destinatario = :destinatario and t.tipoActuacion.id = :tipoActuacion AND t.visible = true AND t.fechaPresentacion <= :fechaPresentacion ORDER BY t.leido, t.fechaPresentacion DESC, t.fechaHoraAlta DESC, t.id DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDestinatarioTipoActuacionLeido", query = "SELECT t FROM ExpActuaciones t WHERE t.destinatario = :destinatario and t.tipoActuacion.id = :tipoActuacion AND t.visible = true AND t.leido = :leido ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC, t.id DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDestinatarioTipoActuacionLeidoFechaPresentacion", query = "SELECT t FROM ExpActuaciones t WHERE t.destinatario = :destinatario and t.tipoActuacion.id = :tipoActuacion AND t.visible = true AND t.leido = :leido AND t.fechaPresentacion <= :fechaPresentacion ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC, t.id DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicial", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicialANDEstadosActuacion", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial AND ((select count(a.id) from ExpPersonasFirmantesPorActuaciones a WHERE a.actuacion.id = t.id and a.estado.codigo = 'AC' AND a.personaFirmante = :personaFirmante AND a.actuacion.estado.codigo in :estados) > :limite OR t.visible = true) ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByEstado", query = "SELECT t FROM ExpActuaciones t WHERE t.estado = :estado ORDER BY t.fechaHoraAlta")
    , @NamedQuery(name = "ExpActuaciones.findByPreopinanteEstado", query = "SELECT t FROM ExpActuaciones t WHERE t.preopinante = :preopinante AND t.estado = :estado ORDER BY t.fechaHoraAlta")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicialVisibleTipoActuacion", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial AND t.visible = :visible AND t.tipoActuacion.id = :tipoActuacion ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicialTiposActuacion", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicialVisibleTipoActuacion", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial AND t.visible = :visible AND t.tipoActuacion.id = :tipoActuacion ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicialVisibleTipoActuacionFechaPresentacion", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial AND t.visible = :visible AND t.tipoActuacion.id = :tipoActuacion AND t.fechaPresentacion <= :fechaPresentacion ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicialVisible", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial AND t.visible = :visible ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByDocumentoJudicialVisibleFechaPresentacion", query = "SELECT t FROM ExpActuaciones t WHERE t.documentoJudicial = :documentoJudicial AND t.visible = :visible AND ((t.tipoActuacion in :tiposActuacion1) or (t.tipoActuacion not in :tiposActuacion2 AND t.fechaPresentacion <= :fechaPresentacion)) ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpActuaciones.findByCodActuacionCaso", query = "SELECT t FROM ExpActuaciones t WHERE t.codActuacionCaso = :codActuacionCaso")
    , @NamedQuery(name = "ExpActuaciones.findByEnvioCorteANDProveido", query = "SELECT t FROM ExpActuaciones t WHERE t.envioCorte = :envioCorte AND t.proveidoCorte = :proveidoCorte ORDER BY t.fechaPresentacion DESC, t.fechaHoraAlta DESC")})
public class ExpActuaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "archivo")
    private String archivo;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPresentacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "resolucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Resoluciones resolucion;
    @JoinColumn(name = "tipo_actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpTiposActuacion tipoActuacion;
    @JoinColumn(name = "objeto_actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpObjetosActuacion objetoActuacion;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = false)
    @Column(name = "fojas")
    private Integer fojas;
    @JoinColumn(name = "actuacion_padre", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ExpActuaciones actuacionPadre;
    @Basic(optional = true)
    @Column(name = "visible")
    private boolean visible;
    @JoinColumn(name = "persona_visible", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaVisible;
    @JoinColumn(name = "secretario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas secretario;
    @Basic(optional = true)
    @Column(name = "fecha_hora_visible")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraVisible;
    @Basic(optional = true)
    @Column(name = "notificado")
    private boolean notificado;
    @JoinColumn(name = "persona_notificado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaNotificado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_notificado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraNotificado;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "hash")
    private String hash;
    @Transient
    private boolean visibleNuevo;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "texto")
    private String texto;
    @JoinColumn(name = "formato", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ExpFormatos formato;
    @JoinColumn(name = "preopinante", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas preopinante;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private ExpEstadosActuacion estado;
    @Basic(optional = true)
    @Column(name = "autenticado")
    private boolean autenticado;
    @JoinColumn(name = "persona_autenticado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAutenticado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_autenticado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAutenticado;
    @Basic(optional = true)
    @Column(name = "regeneradoqr")
    private boolean regeneradoqr;
    @JoinColumn(name = "persona_regeneradoqr", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaRegeneradoqr;
    @Basic(optional = true)
    @Column(name = "fecha_hora_regeneradoqr")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRegeneradoqr;
    @Column(name = "notificar")
    private boolean notificar;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nro_final")
    private String nroFinal;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @Lob
    @Size(max = 65535)
    @Column(name = "texto_final")
    private String textoFinal;
    @JoinColumn(name = "destinatario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas destinatario;
    @Basic(optional = true)
    @Column(name = "leido")
    private boolean leido;
    @Basic(optional = true)
    @Column(name = "cod_actuacion_caso")
    private Integer codActuacionCaso;
    @Basic(optional = true)
    @Column(name = "cod_actuacion_relacionada")
    private Integer codActuacionRelacionada;
    @Column(name = "fecha_hora_envio_corte")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEnvioCorte;
    @JoinColumn(name = "persona_envio_corte", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaEnvioCorte;
    @JoinColumn(name = "documento_judicial_corte", referencedColumnName = "id")
    @Basic(optional = true)
    @Column(name = "envio_corte")
    private boolean envioCorte;
    @ManyToOne(optional = false)
    @JoinColumn(name = "documento_judicial_corte", referencedColumnName = "id")
    private DocumentosJudicialesCorte documentoJudicialCorte;
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaBorrado;
    @Transient
    private String nroFinalString;
    @Basic(optional = true)
    @Column(name = "proveido_corte")
    private boolean proveidoCorte;
    @Column(name = "fecha_hora_proveido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraProveido;
    @Basic(optional = false)
    @Column(name = "diseno")
    private Integer diseno;
    @Basic(optional = true)
    @Column(name = "pendiente")
    private boolean pendiente;
    @Transient
    private int parte;
    @Transient
    private int acusado;
    @Transient
    private int funcionario;
    @Transient
    private String personaRevisado;
    @Transient
    private Date fechaHoraRevisado;
    @Basic(optional = true)
    @Column(name = "urgente")
    private boolean urgente;
    @JoinColumn(name = "acta_sesion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActasSesion actaSesion;
    @JoinColumn(name = "providencia_desglose", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ExpActuaciones providenciaDesglose;
    @Basic(optional = true)
    @Column(name = "providencia_desglose_firmada")
    private boolean providenciaDesgloseFirmada;

    public ExpActuaciones() {
        
    }

    public ExpActuaciones(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getParte() {
        return parte;
    }

    public void setParte(int parte) {
        this.parte = parte;
    }

    public int getAcusado() {
        return acusado;
    }

    public void setAcusado(int acusado) {
        this.acusado = acusado;
    }

    public int getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(int funcionario) {
        this.funcionario = funcionario;
    }


    public Integer getDiseno() {
        return diseno;
    }

    public void setDiseno(Integer diseno) {
        this.diseno = diseno;
    }

    public Personas getSecretario() {
        return secretario;
    }

    public void setSecretario(Personas secretario) {
        this.secretario = secretario;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public boolean isVisibleNuevo() {
        return visible;
    }

    public void setVisibleNuevo(boolean visibleNuevo) {
        this.visibleNuevo = visibleNuevo;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
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

    public ExpTiposActuacion getTipoActuacion() {
        return tipoActuacion;
    }

    public void setTipoActuacion(ExpTiposActuacion tipoActuacion) {
        this.tipoActuacion = tipoActuacion;
    }

    public ExpObjetosActuacion getObjetoActuacion() {
        return objetoActuacion;
    }

    public void setObjetoActuacion(ExpObjetosActuacion objetoActuacion) {
        this.objetoActuacion = objetoActuacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getCodActuacionRelacionada() {
        return codActuacionRelacionada;
    }

    public void setCodActuacionRelacionada(Integer codActuacionRelacionada) {
        this.codActuacionRelacionada = codActuacionRelacionada;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public Integer getFojas() {
        return fojas;
    }

    public void setFojas(Integer fojas) {
        this.fojas = fojas;
    }

    public ExpActuaciones getActuacionPadre() {
        return actuacionPadre;
    }

    public void setActuacionPadre(ExpActuaciones actuacionPadre) {
        this.actuacionPadre = actuacionPadre;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Resoluciones getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resoluciones resolucion) {
        this.resolucion = resolucion;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Date getFechaHoraVisible() {
        return fechaHoraVisible;
    }

    public void setFechaHoraVisible(Date fechaHoraVisible) {
        this.fechaHoraVisible = fechaHoraVisible;
    }

    public Personas getPersonaVisible() {
        return personaVisible;
    }

    public void setPersonaVisible(Personas personaVisible) {
        this.personaVisible = personaVisible;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public ExpFormatos getFormato() {
        return formato;
    }

    public void setFormato(ExpFormatos formato) {
        this.formato = formato;
    }

    public Personas getPreopinante() {
        return preopinante;
    }

    public void setPreopinante(Personas preopinante) {
        this.preopinante = preopinante;
    }

    public ExpEstadosActuacion getEstado() {
        return estado;
    }

    public void setEstado(ExpEstadosActuacion estado) {
        this.estado = estado;
    }

    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    public Personas getPersonaAutenticado() {
        return personaAutenticado;
    }

    public void setPersonaAutenticado(Personas personaAutenticado) {
        this.personaAutenticado = personaAutenticado;
    }

    public Date getFechaHoraAutenticado() {
        return fechaHoraAutenticado;
    }

    public void setFechaHoraAutenticado(Date fechaHoraAutenticado) {
        this.fechaHoraAutenticado = fechaHoraAutenticado;
    }

    public boolean isNotificar() {
        return notificar;
    }

    public void setNotificar(boolean notificar) {
        this.notificar = notificar;
    }

    public String getNroFinal() {
        return nroFinal;
    }

    public void setNroFinal(String nroFinal) {
        this.nroFinal = nroFinal;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getTextoFinal() {
        return textoFinal;
    }

    public void setTextoFinal(String textoFinal) {
        this.textoFinal = textoFinal;
    }

    public Personas getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Personas destinatario) {
        this.destinatario = destinatario;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public Integer getCodActuacionCaso() {
        return codActuacionCaso;
    }

    public void setCodActuacionCaso(Integer codActuacionCaso) {
        this.codActuacionCaso = codActuacionCaso;
    }

    public Date getFechaHoraEnvioCorte() {
        return fechaHoraEnvioCorte;
    }

    public void setFechaHoraEnvioCorte(Date fechaHoraEnvioCorte) {
        this.fechaHoraEnvioCorte = fechaHoraEnvioCorte;
    }

    public Personas getPersonaEnvioCorte() {
        return personaEnvioCorte;
    }

    public void setPersonaEnvioCorte(Personas personaEnvioCorte) {
        this.personaEnvioCorte = personaEnvioCorte;
    }

    public DocumentosJudicialesCorte getDocumentoJudicialCorte() {
        return documentoJudicialCorte;
    }

    public void setDocumentoJudicialCorte(DocumentosJudicialesCorte documentoJudicialCorte) {
        this.documentoJudicialCorte = documentoJudicialCorte;
    }

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }

    public Personas getPersonaNotificado() {
        return personaNotificado;
    }

    public void setPersonaNotificado(Personas personaNotificado) {
        this.personaNotificado = personaNotificado;
    }

    public Date getFechaHoraNotificado() {
        return fechaHoraNotificado;
    }

    public void setFechaHoraNotificado(Date fechaHoraNotificado) {
        this.fechaHoraNotificado = fechaHoraNotificado;
    }

    public boolean isRegeneradoqr() {
        return regeneradoqr;
    }

    public void setRegeneradoqr(boolean regeneradoqr) {
        this.regeneradoqr = regeneradoqr;
    }

    public Personas getPersonaRegeneradoqr() {
        return personaRegeneradoqr;
    }

    public void setPersonaRegeneradoqr(Personas personaRegeneradoqr) {
        this.personaRegeneradoqr = personaRegeneradoqr;
    }

    public Date getFechaHoraRegeneradoqr() {
        return fechaHoraRegeneradoqr;
    }

    public void setFechaHoraRegeneradoqr(Date fechaHoraRegeneradoqr) {
        this.fechaHoraRegeneradoqr = fechaHoraRegeneradoqr;
    }

    public String getNroFinalString() {
        return nroFinalString;
    }

    public void setNroFinalString(String nroFinalString) {
        this.nroFinalString = nroFinalString;
    }
    
    /*
    @XmlTransient
    public String getNroFinalString() {
    if(tipoActuacion != null){
    if(tipoActuacion.getId().equals(Constantes.TIPO_ACTUACION_OFICIO) ||
    tipoActuacion.getId().equals(Constantes.TIPO_ACTUACION_OFICIO_CORTE) ||
    tipoActuacion.getId().equals(Constantes.TIPO_ACTUACION_SD) ||
    tipoActuacion.getId().equals(Constantes.TIPO_ACTUACION_RESOLUCION))
    return nroFinal;
    }
    return "";
    }
     */

    public boolean isEnvioCorte() {
        return envioCorte;
    }

    public void setEnvioCorte(boolean envioCorte) {
        this.envioCorte = envioCorte;
    }

    public boolean isProveidoCorte() {
        return proveidoCorte;
    }

    public void setProveidoCorte(boolean proveidoCorte) {
        this.proveidoCorte = proveidoCorte;
    }

    public Date getFechaHoraProveido() {
        return fechaHoraProveido;
    }

    public void setFechaHoraProveido(Date fechaHoraProveido) {
        this.fechaHoraProveido = fechaHoraProveido;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
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

    public String getPersonaRevisado() {
        return personaRevisado;
    }

    public void setPersonaRevisado(String personaRevisado) {
        this.personaRevisado = personaRevisado;
    }

    public Date getFechaHoraRevisado() {
        return fechaHoraRevisado;
    }

    public void setFechaHoraRevisado(Date fechaHoraRevisado) {
        this.fechaHoraRevisado = fechaHoraRevisado;
    }

    public boolean isUrgente() {
        return urgente;
    }

    public void setUrgente(boolean urgente) {
        this.urgente = urgente;
    }

    public ExpActasSesion getActaSesion() {
        return actaSesion;
    }

    public void setActaSesion(ExpActasSesion actaSesion) {
        this.actaSesion = actaSesion;
    }

    public ExpActuaciones getProvidenciaDesglose() {
        return providenciaDesglose;
    }

    public void setProvidenciaDesglose(ExpActuaciones providenciaDesglose) {
        this.providenciaDesglose = providenciaDesglose;
    }

    public boolean isProvidenciaDesgloseFirmada() {
        return providenciaDesgloseFirmada;
    }

    public void setProvidenciaDesgloseFirmada(boolean providenciaDesgloseFirmada) {
        this.providenciaDesgloseFirmada = providenciaDesgloseFirmada;
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
        if (!(object instanceof ExpActuaciones)) {
            return false;
        }
        ExpActuaciones other = (ExpActuaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpActuaciones[ id=" + id + " ]";
    }
    
}
