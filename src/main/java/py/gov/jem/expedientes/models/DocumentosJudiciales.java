/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosJudiciales.findAll", query = "SELECT d FROM DocumentosJudiciales d ORDER BY d.fechaHoraAlta desc")
    , @NamedQuery(name = "DocumentosJudiciales.findByCanalEntradaDocumentoJudicial", query = "SELECT d FROM DocumentosJudiciales d WHERE d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaPresentacion DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByAtencion", query = "SELECT d FROM DocumentosJudiciales d WHERE d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.responsable = :responsable AND d.estado.codigo NOT IN ('2','4','6','8','10','12','14','16','18') ORDER BY d.fechaPresentacion DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedDpto", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND (d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial OR d.departamento = :departamento) ORDER BY d.fechaPresentacion DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedDptoTipoDoc", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND (d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial OR d.departamento = :departamento) ORDER BY d.fechaHoraAlta DESC, d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedFechaAltaDpto", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findbyTipoExpedienteEstadoProceso", query = "SELECT d FROM DocumentosJudiciales d WHERE d.tipoExpediente.id = :tipoExpediente AND (d.estadoProceso.codigo = :estadoProceso1 OR d.estadoProceso.codigo = :estadoProceso2) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByCausaPersona", query = "SELECT d FROM DocumentosJudiciales d WHERE d.causa like :causa AND (((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona GROUP BY f.documentoJudicial, f.persona) = 1) OR ((select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona GROUP BY p.documentoJudicial, p.persona) = 1)) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByEstadosProcesoPersonaExpedienteElectronico", query = "SELECT d FROM DocumentosJudiciales d WHERE d.estadoProcesoExpedienteElectronico in :estadosProcesoExpedienteElectronico AND (((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona GROUP BY f.documentoJudicial, f.persona) = 1) OR ((select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona GROUP BY p.documentoJudicial, p.persona) = 1)) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByEstadosProcesoPersonaExpedienteElectronicoOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.estadoProcesoExpedienteElectronico in :estadosProcesoExpedienteElectronico AND (((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona GROUP BY f.documentoJudicial, f.persona) = 1) OR ((select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona GROUP BY p.documentoJudicial, p.persona) = 1)) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY  CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByCausa", query = "SELECT d FROM DocumentosJudiciales d WHERE d.causa like :causa AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByEstadosProcesoExpedienteElectronico", query = "SELECT d FROM DocumentosJudiciales d WHERE d.estadoProcesoExpedienteElectronico in :estadosProcesoExpedienteElectronico AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByEstadosProcesoExpedienteElectronicoOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.estadoProcesoExpedienteElectronico in :estadosProcesoExpedienteElectronico AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByCausaFirmante", query = "SELECT d FROM DocumentosJudiciales d WHERE d.causa like :causa AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND ((select count(a.id) from ExpPersonasFirmantesPorActuaciones a WHERE a.documentoJudicial.id = d.id and a.estado.codigo = 'AC' AND a.personaFirmante = :personaFirmante AND a.actuacion.estado.codigo in ('FM', 'RP', 'FP')) > :limite) ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByCausaFirmanteOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.causa like :causa AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND ((select count(a.id) from ExpPersonasFirmantesPorActuaciones a WHERE a.documentoJudicial.id = d.id and a.estado.codigo = 'AC' AND a.personaFirmante = :personaFirmante AND a.actuacion.estado.codigo in ('FM', 'RP', 'FP')) > :limite) ORDER BY  CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByEstadosProcesoExpedienteElectronicoFirmante", query = "SELECT d FROM DocumentosJudiciales d WHERE d.estadoProcesoExpedienteElectronico in :estadosProcesoExpedienteElectronico AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND ((select count(a.id) from ExpPersonasFirmantesPorActuaciones a WHERE a.documentoJudicial.id = d.id and a.estado.codigo = 'AC' AND a.personaFirmante = :personaFirmante AND a.actuacion.estado.codigo in ('FM', 'RP', 'FP')) > :limite) ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByEstadosProcesoExpedienteElectronicoFirmanteOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.estadoProcesoExpedienteElectronico in :estadosProcesoExpedienteElectronico AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND ((select count(a.id) from ExpPersonasFirmantesPorActuaciones a WHERE a.documentoJudicial.id = d.id and a.estado.codigo = 'AC' AND a.personaFirmante = :personaFirmante AND a.actuacion.estado.codigo in ('FM', 'RP', 'FP')) > :limite) ORDER BY  CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByCaratulaPersona", query = "SELECT d FROM DocumentosJudiciales d WHERE d.caratula like :caratula AND (((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona1) = 1) OR ((select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona2 GROUP BY p.documentoJudicial, p.persona) = 1)) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByCaratulaPersonaOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.caratula like :caratula AND (((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona1) = 1) OR ((select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona2 GROUP BY p.documentoJudicial, p.persona) = 1)) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY  CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByCaratula", query = "SELECT d FROM DocumentosJudiciales d WHERE d.caratula like :caratula AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByCaratulaOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.caratula like :caratula AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY  CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedAsignado", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedAsignadoAll", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedFechaAlta", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByPersonaFechaAlta", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND (((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona GROUP BY f.documentoJudicial, f.persona) = 1) OR ((select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona GROUP BY p.documentoJudicial, p.persona) = 1)) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByPersonaFechaAltaOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND (((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona GROUP BY f.documentoJudicial, f.persona) = 1) OR ((select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona GROUP BY p.documentoJudicial, p.persona) = 1)) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY  CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByFechaAlta", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByFechaAltaOrderFechaInicioEnjuiciamiento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY  CASE WHEN d.fechaInicioEnjuiciamiento IS NULL THEN CURRENT_DATE ELSE d.fechaInicioEnjuiciamiento END, d.fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByPersonaFechaPresentacion", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND ((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.persona = :persona GROUP BY f.documentoJudicial, f.persona) = 1 OR (select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.persona = :persona GROUP BY p.documentoJudicial, p.persona) = 1) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC, d.fechaPresentacion")
    , @NamedQuery(name = "DocumentosJudiciales.findByPersonaAno", query = "SELECT d FROM DocumentosJudiciales d WHERE d.ano = :ano AND ((select 1 from ExpPartesPorDocumentosJudiciales f where f.documentoJudicial = d AND f.estado.codigo = 'AC' AND f.persona = :persona GROUP BY f.documentoJudicial, f.persona) = 1 OR (select 1 from PersonasPorDocumentosJudiciales p where p.documentoJudicial = d AND p.estado.codigo = 'AC' AND p.persona = :persona GROUP BY p.documentoJudicial, p.persona) = 1) AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC, d.fechaPresentacion")
    , @NamedQuery(name = "DocumentosJudiciales.findByFechaPresentacion", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaPresentacion >= :fechaDesde AND d.fechaPresentacion <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC, d.fechaPresentacion")
    , @NamedQuery(name = "DocumentosJudiciales.findByAno", query = "SELECT d FROM DocumentosJudiciales d WHERE d.ano = :ano AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC, d.fechaPresentacion")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedFechaAltaAll", query = "SELECT d FROM DocumentosJudiciales d WHERE d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial ORDER BY d.fechaHoraAlta DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedFechaAltaAsignado", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial AND d.departamento = :departamento ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedFechaAltaAsignadoAll", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial = :tipoDocumentoJudicial ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findById", query = "SELECT d FROM DocumentosJudiciales d WHERE d.id = :id")
    , @NamedQuery(name = "DocumentosJudiciales.findByEntradaDocumento", query = "SELECT d FROM DocumentosJudiciales d WHERE d.entradaDocumento = :entradaDocumento")
    , @NamedQuery(name = "DocumentosJudiciales.findByEntradaDocumentoMaxDoc", query = "SELECT c FROM DocumentosJudiciales c WHERE c.id in (SELECT MAX(d.id) FROM DocumentosJudiciales d WHERE d.entradaDocumento = :entradaDocumento)")
    , @NamedQuery(name = "DocumentosJudiciales.findByDescripcionMesaEntrada", query = "SELECT d FROM DocumentosJudiciales d WHERE d.descripcionMesaEntrada = :descripcionMesaEntrada")
    , @NamedQuery(name = "DocumentosJudiciales.findByNroMesaEntradaJudicial", query = "SELECT d FROM DocumentosJudiciales d WHERE d.tipoDocumentoJudicial.codigo = 'JU' AND d.entradaDocumento.nroMesaEntrada = :nroMesaEntrada")
    , @NamedQuery(name = "DocumentosJudiciales.findByNroMesaEntradaAdministrativa", query = "SELECT d FROM DocumentosJudiciales d WHERE d.tipoDocumentoJudicial.codigo = 'AD' AND d.entradaDocumento.nroMesaEntrada = :nroMesaEntrada")
    , @NamedQuery(name = "DocumentosJudiciales.findByNroExpediente", query = "SELECT d FROM DocumentosJudiciales d WHERE d.nroExpediente = :nroExpediente")
    , @NamedQuery(name = "DocumentosJudiciales.findByFolios", query = "SELECT d FROM DocumentosJudiciales d WHERE d.folios = :folios")
    , @NamedQuery(name = "DocumentosJudiciales.findByMotivoProceso", query = "SELECT d FROM DocumentosJudiciales d WHERE d.motivoProceso = :motivoProceso")
    , @NamedQuery(name = "DocumentosJudiciales.findByFechaHoraAlta", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByPersonaAlta", query = "SELECT d FROM DocumentosJudiciales d WHERE d.personaAlta = :personaAlta")
    , @NamedQuery(name = "DocumentosJudiciales.findByPersonaUltimaObservacion", query = "SELECT d FROM DocumentosJudiciales d WHERE d.personaUltimaObservacion = :personaUltimaObservacion")
    , @NamedQuery(name = "DocumentosJudiciales.findByPersonaUltimoEstadoProcesal", query = "SELECT d FROM DocumentosJudiciales d WHERE d.personaUltimoEstadoProcesal = :personaUltimoEstadoProcesal")
    , @NamedQuery(name = "DocumentosJudiciales.findOrderedFechaAltaAsignadoCount2", query = "SELECT count(d) FROM DocumentosJudiciales d WHERE d.fechaHoraAlta >= :fechaDesde AND d.fechaHoraAlta <= :fechaHasta AND d.canalEntradaDocumentoJudicial = :canalEntradaDocumentoJudicial AND d.tipoDocumentoJudicial IN :tiposDocumentoJudicial AND d.departamento = :departamento and d.estado.tipo <> :tipo ORDER BY d.fechaHoraUltimoEstado DESC")
    , @NamedQuery(name = "DocumentosJudiciales.findByFechaHoraUltimoEstado", query = "SELECT d FROM DocumentosJudiciales d WHERE d.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class DocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "entrada_documento", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private EntradasDocumentosJudiciales entradaDocumento;
    @JoinColumn(name = "estado_procesal_documento_judicial", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private EstadosProcesalesDocumentosJudiciales estadoProcesalDocumentoJudicial;
    @JoinColumn(name = "observacion_documento_judicial", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesDocumentosJudiciales observacionDocumentoJudicial;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion_mesa_entrada")
    private String descripcionMesaEntrada;
    @Basic(optional = true)
    @Size(max = 40)
    @Column(name = "nro_expediente")
    private String nroExpediente;
    @Lob
    @Size(max = 65535)
    @Column(name = "causa")
    private String causa;
    @Lob
    @Size(max = 65535)
    @Column(name = "caratula")
    private String caratula;
    @Lob
    @Size(max = 65535)
    @Column(name = "caratula_anterior")
    private String caratulaAnterior;
    @Lob
    @Size(max = 65535)
    @Column(name = "ultima_observacion")
    private String ultimaObservacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "estado_procesal")
    private String estadoProcesal;
    @Transient
    private Personas acusador;
    @Transient
    private ExpPersonasAcusacion acusadorInformado;
    @Transient
    private Personas abogadoAcusador;
    @Transient
    private String ultimaObservacionAux;
    @Transient
    private String ultimaObservacionAntecedenteAux;
    @Basic(optional = true)
    @Column(name = "fecha_ultima_observacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaObservacion;
    @Transient
    private String estadoProcesalAux;
    @Basic(optional = true)
    @Column(name = "fecha_hora_estado_procesal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEstadoProcesal;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "folios")
    private String folios;
    @Lob
    @Size(max = 65535)
    @Column(name = "motivo_proceso")
    private String motivoProceso;
    @Size(min = 2, max = 2)
    @Column(name = "mostrar_web")
    private String mostrarWeb;
    @Size(min = 2, max = 2)
    @Column(name = "mostrar_expediente_web")
    private String mostrarExpedienteWeb;
    @Basic(optional = true)
    @Column(name = "documento_escaneado")
    private Integer documentoEscaneado;
    @Basic(optional = true)
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPresentacion;
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
    @JoinColumn(name = "tipo_documento_judicial", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    @JoinColumn(name = "tipo_expediente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposExpediente tipoExpediente;
    @JoinColumn(name = "tipo_expediente_anterior", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TiposExpediente tipoExpedienteAnterior;
    @JoinColumn(name = "canal_entrada_documento_judicial", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private CanalesEntradaDocumentoJudicial canalEntradaDocumentoJudicial;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "usuario_ultima_observacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioUltimaObservacion;
    @JoinColumn(name = "usuario_estado_procesal", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioEstadoProcesal;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsable;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosDocumento estado;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @JoinColumn(name = "estado_proceso", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosProceso estadoProceso;
    @JoinColumn(name = "estado_proceso_anterior", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosProceso estadoProcesoAnterior;
    @JoinColumn(name = "observacion_documento_judicial_antecedente", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    private ObservacionesDocumentosJudicialesAntecedentes observacionDocumentoJudicialAntecedente;
    @Lob
    @Size(max = 65535)
    @Column(name = "ultima_observacion_antecedente")
    private String ultimaObservacionAntecedente;
    @JoinColumn(name = "usuario_ultima_observacion_antecedente", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioUltimaObservacionAntecedente;
    @Basic(optional = true)
    @Column(name = "fecha_ultima_observacion_antecedente")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimaObservacionAntecedente;
    @Lob
    @Size(max = 65535)
    @Column(name = "juzgado")
    private String juzgado;
    @Column(name = "presento_solvencia")
    private boolean presentoSolvencia;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaAlta;
    @JoinColumn(name = "persona_ultima_observacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimaObservacion;
    @JoinColumn(name = "persona_ultimo_estado_procesal", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstadoProcesal;
    @Basic(optional = true)
    @Column(name = "ano")
    private Integer ano;
    @Column(name = "expediente_viejo")
    private boolean expedienteViejo;
    @Column(name = "restringido")
    private boolean restringido;
    @JoinColumn(name = "persona_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaUltimoEstado;
    @JoinColumn(name = "persona_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_inicio_enjuiciamiento")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioEnjuiciamiento;
    @JoinColumn(name = "estado_proceso_expediente_electronico", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private ExpEstadosProcesoExpedienteElectronico estadoProcesoExpedienteElectronico;
    

    public DocumentosJudiciales() {
    }

    public DocumentosJudiciales(Integer id) {
        this.id = id;
    }

    public DocumentosJudiciales(Integer id, EntradasDocumentosJudiciales entradaDocumento, String descripcionMesaEntrada, String nroExpediente, String folios, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.entradaDocumento = entradaDocumento;
        this.descripcionMesaEntrada = descripcionMesaEntrada;
        this.nroExpediente = nroExpediente;
        this.folios = folios;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstadoProcesal() {
        return estadoProcesal;
    }

    @XmlTransient
    public String getEstadoProcesalString() {
        if( estadoProcesal != null){
            return estadoProcesal.replace("\n", "<br />");
        }else{
            return "";
        }
    }
    
    public void setEstadoProcesal(String estadoProcesal) {
        this.estadoProcesal = estadoProcesal;
    }

    public String getMostrarWeb() {
        return mostrarWeb;
    }

    public void setMostrarWeb(String mostrarWeb) {
        this.mostrarWeb = mostrarWeb;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public CanalesEntradaDocumentoJudicial getCanalEntradaDocumentoJudicial() {
        return canalEntradaDocumentoJudicial;
    }

    public void setCanalEntradaDocumentoJudicial(CanalesEntradaDocumentoJudicial canalEntradaDocumentoJudicial) {
        this.canalEntradaDocumentoJudicial = canalEntradaDocumentoJudicial;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public String getUltimaObservacionAux() {
        return ultimaObservacion;
    }

    public void setUltimaObservacionAux(String ultimaObservacionAux) {
        this.ultimaObservacionAux = ultimaObservacionAux;
    }

    public String getUltimaObservacionAntecedenteAux() {
        return ultimaObservacion;
    }

    public void setUltimaObservacionAntecedenteAux(String ultimaObservacionAntecedenteAux) {
        this.ultimaObservacionAntecedenteAux = ultimaObservacionAntecedenteAux;
    }


    public String getEstadoProcesalAux() {
        return estadoProcesal;
    }

    public void setEstadoProcesalAux(String estadoProcesalAux) {
        this.estadoProcesalAux = estadoProcesalAux;
    }
    
    public EstadosProcesalesDocumentosJudiciales getEstadoProcesalDocumentoJudicial() {
        return estadoProcesalDocumentoJudicial;
    }

    public void setEstadoProcesalDocumentoJudicial(EstadosProcesalesDocumentosJudiciales estadoProcesalDocumentoJudicial) {
        this.estadoProcesalDocumentoJudicial = estadoProcesalDocumentoJudicial;
    }

    public Date getFechaHoraEstadoProcesal() {
        return fechaHoraEstadoProcesal;
    }

    public void setFechaHoraEstadoProcesal(Date fechaHoraEstadoProcesal) {
        this.fechaHoraEstadoProcesal = fechaHoraEstadoProcesal;
    }

    public String getJuzgado() {
        return juzgado;
    }

    public void setJuzgado(String juzgado) {
        this.juzgado = juzgado;
    }

    public boolean isPresentoSolvencia() {
        return presentoSolvencia;
    }

    public void setPresentoSolvencia(boolean presentoSolvencia) {
        this.presentoSolvencia = presentoSolvencia;
    }

    public Personas getAcusador() {
        return acusador;
    }

    public void setAcusador(Personas acusador) {
        this.acusador = acusador;
    }

    public ExpPersonasAcusacion getAcusadorInformado() {
        return acusadorInformado;
    }

    public void setAcusadorInformado(ExpPersonasAcusacion acusadorInformado) {
        this.acusadorInformado = acusadorInformado;
    }

    public Personas getAbogadoAcusador() {
        return abogadoAcusador;
    }

    public void setAbogadoAcusador(Personas abogadoAcusador) {
        this.abogadoAcusador = abogadoAcusador;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public Personas getPersonaUltimaObservacion() {
        return personaUltimaObservacion;
    }

    public void setPersonaUltimaObservacion(Personas personaUltimaObservacion) {
        this.personaUltimaObservacion = personaUltimaObservacion;
    }

    public Personas getPersonaUltimoEstadoProcesal() {
        return personaUltimoEstadoProcesal;
    }

    public void setPersonaUltimoEstadoProcesal(Personas personaUltimoEstadoProcesal) {
        this.personaUltimoEstadoProcesal = personaUltimoEstadoProcesal;
    }

    public boolean isExpedienteViejo() {
        return expedienteViejo;
    }

    public void setExpedienteViejo(boolean expedienteViejo) {
        this.expedienteViejo = expedienteViejo;
    }
    
    @XmlTransient
    public boolean verifObs(){
        if((ultimaObservacion == null &&  ultimaObservacionAux != null) || (ultimaObservacion != null &&  ultimaObservacionAux == null)){
            return true;
        }else if(ultimaObservacion == null &&  ultimaObservacionAux == null){
            return false;
        }
        
        return !ultimaObservacion.equals(ultimaObservacionAux);
    }
    
    @XmlTransient
    public void transferirObs(){
        ultimaObservacion = ultimaObservacionAux;
    }
    
    @XmlTransient
    public boolean verifObsAntecedente(){
        if((ultimaObservacionAntecedente == null &&  ultimaObservacionAntecedenteAux != null) || (ultimaObservacionAntecedente != null &&  ultimaObservacionAntecedenteAux == null)){
            return true;
        }else if(ultimaObservacionAntecedente == null &&  ultimaObservacionAntecedenteAux == null){
            return false;
        }
        
        return !ultimaObservacionAntecedente.equals(ultimaObservacionAntecedenteAux);
    }
    
    @XmlTransient
    public void transferirObsAntecedente(){
        ultimaObservacionAntecedente = ultimaObservacionAntecedenteAux;
    }

    @XmlTransient
    public boolean verifEstadoProcesal(){
        if((estadoProcesal == null &&  estadoProcesalAux != null) || (estadoProcesal != null &&  estadoProcesalAux == null)){
            return true;
        }else if(estadoProcesal == null &&  estadoProcesalAux == null){
            return false;
        }
        
        return !estadoProcesal.equals(estadoProcesalAux);
    }
    
    @XmlTransient
    public void transferirEstadoProcesal(){
        estadoProcesal = estadoProcesalAux;
    }

    public Usuarios getUsuarioEstadoProcesal() {
        return usuarioEstadoProcesal;
    }

    public void setUsuarioEstadoProcesal(Usuarios usuarioEstadoProcesal) {
        this.usuarioEstadoProcesal = usuarioEstadoProcesal;
    }
    
    public Usuarios getUsuarioUltimaObservacion() {
        return usuarioUltimaObservacion;
    }

    public void setUsuarioUltimaObservacion(Usuarios usuarioUltimaObservacion) {
        this.usuarioUltimaObservacion = usuarioUltimaObservacion;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    @XmlTransient
    public String getUltimaObservacionString() {
        if( ultimaObservacion != null){
            return ultimaObservacion.replace("\n", "<br />");
        }else{
            return "";
        }
    }
    
    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public Date getFechaUltimaObservacion() {
        return fechaUltimaObservacion;
    }

    public void setFechaUltimaObservacion(Date fechaUltimaObservacion) {
        this.fechaUltimaObservacion = fechaUltimaObservacion;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public EntradasDocumentosJudiciales getEntradaDocumento() {
        return entradaDocumento;
    }

    public void setEntradaDocumento(EntradasDocumentosJudiciales entradaDocumento) {
        this.entradaDocumento = entradaDocumento;
    }

    public ObservacionesDocumentosJudiciales getObservacionDocumentoJudicial() {
        return observacionDocumentoJudicial;
    }

    public void setObservacionDocumentoJudicial(ObservacionesDocumentosJudiciales observacionDocumentoJudicial) {
        this.observacionDocumentoJudicial = observacionDocumentoJudicial;
    }

    public String getDescripcionMesaEntrada() {
        return descripcionMesaEntrada;
    }

    public void setDescripcionMesaEntrada(String descripcionMesaEntrada) {
        this.descripcionMesaEntrada = descripcionMesaEntrada;
    }

    @XmlTransient
    public String setDescripcionMesaEntradaString() {
        if(descripcionMesaEntrada != null){
            return descripcionMesaEntrada.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public String getCaratula() {
        return caratula;
    }

    @XmlTransient
    public String getCaratulaString() {
        if( caratula != null ){
            return caratula.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    @XmlTransient
    public String getCaratulaAnteriorString() {
        if( caratulaAnterior != null ){
            return caratulaAnterior.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getFolios() {
        return folios;
    }

    public void setFolios(String folios) {
        this.folios = folios;
    }

    public String getMotivoProceso() {
        return motivoProceso;
    }

    @XmlTransient
    public String getMotivoProcesoString() {
        if(motivoProceso != null){
            return motivoProceso.replace("\n", "<br />");
        }else{
            return "";
        }
    }

    public void setMotivoProceso(String motivoProceso) {
        this.motivoProceso = motivoProceso;
    }

    public Integer getDocumentoEscaneado() {
        return documentoEscaneado;
    }

    public void setDocumento(Integer documentoEscaneado) {
        this.documentoEscaneado = documentoEscaneado;
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

    public EstadosDocumento getEstado() {
        return estado;
    }

    public void setEstado(EstadosDocumento estado) {
        this.estado = estado;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public EstadosProceso getEstadoProceso() {
        return estadoProceso;
    }

    public void setEstadoProceso(EstadosProceso estadoProceso) {
        this.estadoProceso = estadoProceso;
    }

    public TiposExpediente getTipoExpediente() {
        return tipoExpediente;
    }

    public void setTipoExpediente(TiposExpediente tipoExpediente) {
        this.tipoExpediente = tipoExpediente;
    }

    @XmlTransient
    public String getTieneDocumento() {
        if (documentoEscaneado == null) {
            return "NO";
        } else {
            return "SI";
        }
    }

    public ObservacionesDocumentosJudicialesAntecedentes getObservacionDocumentoJudicialAntecedente() {
        return observacionDocumentoJudicialAntecedente;
    }

    public void setObservacionDocumentoJudicialAntecedente(ObservacionesDocumentosJudicialesAntecedentes observacionDocumentoJudicialAntecedente) {
        this.observacionDocumentoJudicialAntecedente = observacionDocumentoJudicialAntecedente;
    }

    public String getUltimaObservacionAntecedente() {
        return ultimaObservacionAntecedente;
    }

    public void setUltimaObservacionAntecedente(String ultimaObservacionAntecedente) {
        this.ultimaObservacionAntecedente = ultimaObservacionAntecedente;
    }

    public Usuarios getUsuarioUltimaObservacionAntecedente() {
        return usuarioUltimaObservacionAntecedente;
    }

    public void setUsuarioUltimaObservacionAntecedente(Usuarios usuarioUltimaObservacionAntecedente) {
        this.usuarioUltimaObservacionAntecedente = usuarioUltimaObservacionAntecedente;
    }

    public Date getFechaUltimaObservacionAntecedente() {
        return fechaUltimaObservacionAntecedente;
    }

    public void setFechaUltimaObservacionAntecedente(Date fechaUltimaObservacionAntecedente) {
        this.fechaUltimaObservacionAntecedente = fechaUltimaObservacionAntecedente;
    }    

    public String getCaratulaAnterior() {
        return caratulaAnterior;
    }

    public void setCaratulaAnterior(String caratulaAnterior) {
        this.caratulaAnterior = caratulaAnterior;
    }

    public TiposExpediente getTipoExpedienteAnterior() {
        return tipoExpedienteAnterior;
    }

    public void setTipoExpedienteAnterior(TiposExpediente tipoExpedienteAnterior) {
        this.tipoExpedienteAnterior = tipoExpedienteAnterior;
    }

    public EstadosProceso getEstadoProcesoAnterior() {
        return estadoProcesoAnterior;
    }

    public void setEstadoProcesoAnterior(EstadosProceso estadoProcesoAnterior) {
        this.estadoProcesoAnterior = estadoProcesoAnterior;
    }

    public String getMostrarExpedienteWeb() {
        return mostrarExpedienteWeb;
    }

    public void setMostrarExpedienteWeb(String mostrarExpedienteWeb) {
        this.mostrarExpedienteWeb = mostrarExpedienteWeb;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public boolean isRestringido() {
        return restringido;
    }

    public void setRestringido(boolean restringido) {
        this.restringido = restringido;
    }

    public Personas getPersonaUltimoEstado() {
        return personaUltimoEstado;
    }

    public void setPersonaUltimoEstado(Personas personaUltimoEstado) {
        this.personaUltimoEstado = personaUltimoEstado;
    }

    public Personas getPersonaBorrado() {
        return personaBorrado;
    }

    public void setPersonaBorrado(Personas personaBorrado) {
        this.personaBorrado = personaBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Date getFechaInicioEnjuiciamiento() {
        return fechaInicioEnjuiciamiento;
    }

    public void setFechaInicioEnjuiciamiento(Date fechaInicioEnjuiciamiento) {
        this.fechaInicioEnjuiciamiento = fechaInicioEnjuiciamiento;
    }

    public ExpEstadosProcesoExpedienteElectronico getEstadoProcesoExpedienteElectronico() {
        return estadoProcesoExpedienteElectronico;
    }

    public void setEstadoProcesoExpedienteElectronico(ExpEstadosProcesoExpedienteElectronico estadoProcesoExpedienteElectronico) {
        this.estadoProcesoExpedienteElectronico = estadoProcesoExpedienteElectronico;
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
        if (!(object instanceof DocumentosJudiciales)) {
            return false;
        }
        DocumentosJudiciales other = (DocumentosJudiciales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.gestionstartic.models.DocumentosJudiciales[ id=" + id + " ]";
    }
    
}
