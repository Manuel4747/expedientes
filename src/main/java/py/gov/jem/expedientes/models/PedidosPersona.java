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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "pedidos_persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidosPersona.findAll", query = "SELECT p FROM PedidosPersona p order by p.fechaHoraAlta")
    , @NamedQuery(name = "PedidosPersona.control", query = "SELECT u FROM PedidosPersona u WHERE u.usuario = :usuario AND u.contrasena = :contrasena")
    , @NamedQuery(name = "PedidosPersona.findById", query = "SELECT p FROM PedidosPersona p WHERE p.id = :id")
    , @NamedQuery(name = "PedidosPersona.findByEstado", query = "SELECT p FROM PedidosPersona p WHERE p.fechaHoraAlta >= :fechaInicio AND p.estado = :estado ORDER BY p.fechaHoraAlta DESC")
    , @NamedQuery(name = "PedidosPersona.findByNotEstado", query = "SELECT p FROM PedidosPersona p WHERE p.fechaHoraAlta >= :fechaInicio AND p.estado <> :estado ORDER BY p.fechaHoraRespuesta DESC")
    , @NamedQuery(name = "PedidosPersona.findByNombresApellidos", query = "SELECT p FROM PedidosPersona p WHERE p.nombresApellidos = :nombresApellidos")
    , @NamedQuery(name = "PedidosPersona.findByCi", query = "SELECT p FROM PedidosPersona p WHERE p.ci = :ci")
    , @NamedQuery(name = "PedidosPersona.findByHashFechaHoraCaducidad", query = "SELECT p FROM PedidosPersona p WHERE p.hash = :hash and p.fechaHoraCaducidad > :fechaHoraCaducidad")
    , @NamedQuery(name = "PedidosPersona.findByFechaHoraAlta", query = "SELECT p FROM PedidosPersona p WHERE p.fechaHoraAlta = :fechaHoraAlta")})
public class PedidosPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = true)
    @Size(max = 2)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "contrasena")
    private String contrasena;
    @Basic(optional = true)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = true)
    @Column(name = "fecha_hora_pedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraPedido;
    @JoinColumn(name = "cargo_persona", referencedColumnName = "id")
    @ManyToOne
    private CargosPersona cargoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_cargo_persona")
    private String descripcionCargoPersona;
    @JoinColumn(name = "despacho_persona", referencedColumnName = "id")
    @ManyToOne
    private DespachosPersona despachoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_despacho_persona")
    private String descripcionDespachoPersona;
    @JoinColumn(name = "tipo_persona", referencedColumnName = "id")
    @ManyToOne
    private TiposPersona tipoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_tipo_persona")
    private String descripcionTipoPersona;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "localidad_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private LocalidadesPersona localidadPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_localidad_persona")
    private String descripcionLocalidadPersona;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DepartamentosPersona departamentoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_departamento_persona")
    private String descripcionDepartamentoPersona;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "telefono1")
    private String telefono1;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "telefono2")
    private String telefono2;
    @Basic(optional = false)
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "email_validado")
    private boolean emailValidado;
    @Basic(optional = false)
    @Column(name = "realizado")
    private boolean realizado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_caducidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraCaducidad;
    @Basic(optional = true)
    @Column(name = "fecha_hora_validacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraValidacion;
    @Basic(optional = true)
    @Column(name = "fecha_hora_respuesta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRespuesta;
    @Basic(optional = true)
    @Size(max = 50)
    @Column(name = "hash")
    private String hash;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas persona;
    @JoinColumn(name = "persona_respuesta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaRespuesta;
    @Size(max = 20)
    @Column(name = "tipo_pedido_persona")
    private String tipoPedidoPersona;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "caracter_solicitante")
    private String caracterSolicitante;
    @Basic(optional = true)
    @Column(name = "caracter")
    private Integer caracter;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "expediente")
    private String expediente;

    public PedidosPersona() {
    }

    public PedidosPersona(Integer id) {
        this.id = id;
    }

    public PedidosPersona(Integer id, String nombresApellidos, String ci, String estado, Date fechaHoraAlta) {
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

    public String getDescripcionCargoPersona() {
        return descripcionCargoPersona;
    }

    public void setDescripcionCargoPersona(String descripcionCargoPersona) {
        this.descripcionCargoPersona = descripcionCargoPersona;
    }

    public String getDescripcionDespachoPersona() {
        return descripcionDespachoPersona;
    }

    public void setDescripcionDespachoPersona(String descripcionDespachoPersona) {
        this.descripcionDespachoPersona = descripcionDespachoPersona;
    }

    public String getDescripcionTipoPersona() {
        return descripcionTipoPersona;
    }

    public void setDescripcionTipoPersona(String descripcionTipoPersona) {
        this.descripcionTipoPersona = descripcionTipoPersona;
    }

    public String getDescripcionLocalidadPersona() {
        return descripcionLocalidadPersona;
    }

    public void setDescripcionLocalidadPersona(String descripcionLocalidadPersona) {
        this.descripcionLocalidadPersona = descripcionLocalidadPersona;
    }

    public String getDescripcionDepartamentoPersona() {
        return descripcionDepartamentoPersona;
    }

    public void setDescripcionDepartamentoPersona(String descripcionDepartamentoPersona) {
        this.descripcionDepartamentoPersona = descripcionDepartamentoPersona;
    }

    public Date getFechaHoraPedido() {
        return fechaHoraPedido;
    }

    public void setFechaHoraPedido(Date fechaHoraPedido) {
        this.fechaHoraPedido = fechaHoraPedido;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
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

    public boolean isEmailValidado() {
        return emailValidado;
    }

    public void setEmailValidado(boolean emailValidado) {
        this.emailValidado = emailValidado;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }

    public Date getFechaHoraCaducidad() {
        return fechaHoraCaducidad;
    }

    public void setFechaHoraCaducidad(Date fechaHoraCaducidad) {
        this.fechaHoraCaducidad = fechaHoraCaducidad;
    }

    public Date getFechaHoraValidacion() {
        return fechaHoraValidacion;
    }

    public void setFechaHoraValidacion(Date fechaHoraValidacion) {
        this.fechaHoraValidacion = fechaHoraValidacion;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public String getTipoPedidoPersona() {
        return tipoPedidoPersona;
    }

    public void setTipoPedidoPersona(String tipoPedidoPersona) {
        this.tipoPedidoPersona = tipoPedidoPersona;
    }

    public Date getFechaHoraRespuesta() {
        return fechaHoraRespuesta;
    }

    public void setFechaHoraRespuesta(Date fechaHoraRespuesta) {
        this.fechaHoraRespuesta = fechaHoraRespuesta;
    }

    public Personas getPersonaRespuesta() {
        return personaRespuesta;
    }

    public void setPersonaRespuesta(Personas personaRespuesta) {
        this.personaRespuesta = personaRespuesta;
    }

    public String getCaracterSolicitante() {
        return caracterSolicitante;
    }

    public void setCaracterSolicitante(String caracterSolicitante) {
        this.caracterSolicitante = caracterSolicitante;
    }

    public Integer getCaracter() {
        return caracter;
    }

    public void setCaracter(Integer caracter) {
        this.caracter = caracter;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
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
        if (!(object instanceof PedidosPersona)) {
            return false;
        }
        PedidosPersona other = (PedidosPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.PedidosPersona[ id=" + id + " ]";
    }
    
}
