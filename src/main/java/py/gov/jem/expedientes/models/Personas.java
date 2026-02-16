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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 * findByPermisoEstadoNoInhibidos
 */
@Entity
@Table(name = "personas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personas.findAll", query = "SELECT p FROM Personas p WHERE p.estado = 'AC' ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.findByRolEstado", query = "SELECT p FROM Personas p WHERE p.id in (select r.antecedentesRolesPorPersonasPK.persona FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol = :rol) AND p.estado = :estado ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.findByRolesEstado", query = "SELECT p FROM Personas p WHERE p.id in (select r.antecedentesRolesPorPersonasPK.persona FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol in :roles) AND p.estado = :estado ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.findByPermisoEstado", query = "SELECT p FROM Personas p WHERE p.id in (select r.antecedentesRolesPorPersonasPK.persona FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol in (select t.antecedentesPermisosPorRolesPK.rol FROM AntecedentesPermisosPorRoles t WHERE t.antecedentesPermisosPorRolesPK.permiso in (SELECT i.id FROM AntecedentesPermisos i WHERE i.funcion = :funcion))) AND p.estado = :estado ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.findByPermisoEstadoNoInhibidos", query = "SELECT p FROM Personas p WHERE p.id in (select r.antecedentesRolesPorPersonasPK.persona FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol in (select t.antecedentesPermisosPorRolesPK.rol FROM AntecedentesPermisosPorRoles t WHERE t.antecedentesPermisosPorRolesPK.permiso in (SELECT i.id FROM AntecedentesPermisos i WHERE i.funcion = :funcion))) AND p.estado = :estado and p.id NOT IN (select e.persona.id FROM ExpPersonasInhibidasPorDocumentosJudiciales e WHERE e.documentoJudicial = :documentoJudicial and e.estado.codigo = 'AC') ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.findByPermisoEstadoNoInhibidosButRoles", query = "SELECT p FROM Personas p WHERE p.id in (select r.antecedentesRolesPorPersonasPK.persona FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol in (select t.antecedentesPermisosPorRolesPK.rol FROM AntecedentesPermisosPorRoles t WHERE t.antecedentesPermisosPorRolesPK.permiso in (SELECT i.id FROM AntecedentesPermisos i WHERE i.funcion = :funcion))) AND p.estado = :estado and p.id NOT IN (select e.persona.id FROM ExpPersonasInhibidasPorDocumentosJudiciales e WHERE e.documentoJudicial = :documentoJudicial and e.estado.codigo = 'AC') AND p.id NOT IN (select r.antecedentesRolesPorPersonasPK.persona FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol in :roles) ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.control", query = "SELECT u FROM Personas u WHERE u.usuario = :usuario AND u.contrasena = :contrasena AND u.estado = 'AC' AND u.estadoPersona.codigo = 'AC'")
    , @NamedQuery(name = "Personas.findById", query = "SELECT p FROM Personas p WHERE p.id = :id")
    , @NamedQuery(name = "Personas.findByEmail", query = "SELECT p FROM Personas p WHERE p.email = :email AND p.estado = 'AC'")
    , @NamedQuery(name = "Personas.findByEstado", query = "SELECT p FROM Personas p WHERE p.estado = :estado ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.findByNombresApellidos", query = "SELECT p FROM Personas p WHERE p.nombresApellidos = :nombresApellidos")
    , @NamedQuery(name = "Personas.findByNombresApellidosEstado", query = "SELECT p FROM Personas p WHERE p.nombresApellidos = :nombresApellidos and p.estado = :estado")
    , @NamedQuery(name = "Personas.findByCi", query = "SELECT p FROM Personas p WHERE p.ci = :ci")
    , @NamedQuery(name = "Personas.findByCiEstado", query = "SELECT p FROM Personas p WHERE p.ci = :ci and p.estado = :estado")
    , @NamedQuery(name = "Personas.findByEstado", query = "SELECT p FROM Personas p WHERE p.estado = :estado ORDER BY p.nombresApellidos")
    , @NamedQuery(name = "Personas.findByFechaHoraAlta", query = "SELECT p FROM Personas p WHERE p.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Personas.findByUsuarioAlta", query = "SELECT p FROM Personas p WHERE p.usuarioAlta = :usuarioAlta")
    , @NamedQuery(name = "Personas.findByFechaHoraUltimoEstado", query = "SELECT p FROM Personas p WHERE p.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")
    , @NamedQuery(name = "Personas.findByUsuarioUltimoEstado", query = "SELECT p FROM Personas p WHERE p.usuarioUltimoEstado = :usuarioUltimoEstado")})
public class Personas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "contrasena")
    private String contrasena;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_desbloqueo")
    private String codigoDesbloqueo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioAlta;
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "cargo_persona", referencedColumnName = "id")
    @ManyToOne
    private CargosPersona cargoPersona;
    @JoinColumn(name = "despacho_persona", referencedColumnName = "id")
    @ManyToOne
    private DespachosPersona despachoPersona;
    @JoinColumn(name = "tipo_persona", referencedColumnName = "id")
    @ManyToOne
    private TiposPersona tipoPersona;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "localidad_persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LocalidadesPersona localidadPersona;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DepartamentosPersona departamentoPersona;
    @Transient
    private boolean tipoExpedienteAnterior;
    @Transient
    private int cantSorteos;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "telefono1")
    private String telefono1;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "telefono2")
    private String telefono2;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = true)
    @Column(name = "habilitar_antecedentes")
    private boolean habilitarAntecedentes;
    @Basic(optional = true)
    @Column(name = "sorteo")
    private boolean sorteo;
    @Basic(optional = true)
    @Column(name = "email_validado")
    private boolean emailValidado;
    @Transient
    private boolean firmado;
    @Transient
    private boolean revisado;
    @Transient
    private String revisadoPor;
    @Transient
    private Date fechaHoraRevisado;
    @Transient
    private Date fechaHoraFirmado;
    @Transient
    private ExpTiposParte tipoParte;
    @JoinColumn(name = "estado_persona", referencedColumnName = "codigo")
    @ManyToOne
    private EstadosPersona estadoPersona;

    public Personas() {
    }

    public Personas(Integer id) {
        this.id = id;
    }
    
    public Personas(Personas per) {
        this.id = per.id;
        this.nombresApellidos = per.nombresApellidos;
        this.ci = per.ci;
        this.estado = per.estado;
        this.fechaHoraAlta = per.fechaHoraAlta;
        this.usuarioAlta = per.usuarioAlta;
        this.fechaHoraUltimoEstado = per.fechaHoraUltimoEstado;
        this.usuarioUltimoEstado = per.usuarioUltimoEstado;
        this.cargoPersona = per.cargoPersona;
        this.despachoPersona = per.despachoPersona;
        this.tipoPersona = per.tipoPersona;
        this.empresa = per.empresa;
        this.localidadPersona = per.localidadPersona;
        this.departamentoPersona = per.departamentoPersona;
        this.tipoExpedienteAnterior = per.tipoExpedienteAnterior;
        this.usuario = per.usuario;
        this.contrasena = per.contrasena;
        this.telefono1 = per.telefono1;
        this.telefono2 = per.telefono2;
        this.email = per.email;
        this.habilitarAntecedentes = per.habilitarAntecedentes;
        this.emailValidado = per.emailValidado;
    }

    public Personas(Integer id, String nombresApellidos, String ci, String estado, Date fechaHoraAlta) {
        this.id = id;
        this.nombresApellidos = nombresApellidos;
        this.ci = ci;
        this.estado = estado;
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSorteo() {
        return sorteo;
    }

    public void setSorteo(boolean sorteo) {
        this.sorteo = sorteo;
    }

    public int getCantSorteos() {
        return cantSorteos;
    }

    public void setCantSorteos(int cantSorteos) {
        this.cantSorteos = cantSorteos;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public CargosPersona getCargoPersona() {
        return cargoPersona;
    }

    public void setCargoPersona(CargosPersona cargoPersona) {
        this.cargoPersona = cargoPersona;
    }

    public TiposPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TiposPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public boolean isFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public String getCodigoDesbloqueo() {
        return codigoDesbloqueo;
    }

    public void setCodigoDesbloqueo(String codigoDesbloqueo) {
        this.codigoDesbloqueo = codigoDesbloqueo;
    }

    public String getRevisadoPor() {
        return revisadoPor;
    }

    public void setRevisadoPor(String revisadoPor) {
        this.revisadoPor = revisadoPor;
    }

    public Date getFechaHoraRevisado() {
        return fechaHoraRevisado;
    }

    public void setFechaHoraRevisado(Date fechaHoraRevisado) {
        this.fechaHoraRevisado = fechaHoraRevisado;
    }

    public Date getFechaHoraFirmado() {
        return fechaHoraFirmado;
    }

    public void setFechaHoraFirmado(Date fechaHoraFirmado) {
        this.fechaHoraFirmado = fechaHoraFirmado;
    }
    
    @XmlTransient
    public String getFirmadoString() {
        return firmado?"SI":"NO";
    }

    public boolean isRevisado() {
        return revisado;
    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
    }
    
    @XmlTransient
    public String getRevisadoString() {
        return revisado?"SI":"NO";
    }

    public DespachosPersona getDespachoPersona() {
        return despachoPersona;
    }

    public void setDespachoPersona(DespachosPersona despachoPersona) {
        this.despachoPersona = despachoPersona;
    }

    public LocalidadesPersona getLocalidadPersona() {
        return localidadPersona;
    }

    public void setLocalidadPersona(LocalidadesPersona localidadPersona) {
        this.localidadPersona = localidadPersona;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHabilitarAntecedentes() {
        return habilitarAntecedentes;
    }

    public void setHabilitarAntecedentes(boolean habilitarAntecedentes) {
        this.habilitarAntecedentes = habilitarAntecedentes;
    }

    public boolean isEmailValidado() {
        return emailValidado;
    }

    public void setEmailValidado(boolean emailValidado) {
        this.emailValidado = emailValidado;
    }

    public boolean isTipoExpedienteAnterior() {
        return tipoExpedienteAnterior;
    }

    public void setTipoExpedienteAnterior(boolean tipoExpedienteAnterior) {
        this.tipoExpedienteAnterior = tipoExpedienteAnterior;
    }

    public ExpTiposParte getTipoParte() {
        return tipoParte;
    }

    public void setTipoParte(ExpTiposParte tipoParte) {
        this.tipoParte = tipoParte;
    }

    public EstadosPersona getEstadoPersona() {
        return estadoPersona;
    }

    public void setEstadoPersona(EstadosPersona estadoPersona) {
        this.estadoPersona = estadoPersona;
    }

    @XmlTransient
    public String getEstaHabilitarAntecedentes() {
        if (habilitarAntecedentes) {
            return "SI";
        } else {
            return "NO";
        }
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
        if (!(object instanceof Personas)) {
            return false;
        }
        Personas other = (Personas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombresApellidos==null?"":nombresApellidos;
    }
    
}
