/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findById", query = "SELECT u FROM Usuarios u WHERE u.id = :id")
    , @NamedQuery(name = "Usuarios.control", query = "SELECT u FROM Usuarios u WHERE u.usuario = :usuario AND u.contrasena = :contrasena AND u.estado.codigo = 'AC'")
    , @NamedQuery(name = "Usuarios.findByUsuario", query = "SELECT u FROM Usuarios u WHERE u.usuario = :usuario")
    , @NamedQuery(name = "Usuarios.findByContrasena", query = "SELECT u FROM Usuarios u WHERE u.contrasena = :contrasena")
    , @NamedQuery(name = "Usuarios.findByNombresApellidos", query = "SELECT u FROM Usuarios u WHERE u.nombresApellidos = :nombresApellidos")
    , @NamedQuery(name = "Usuarios.findByDireccion", query = "SELECT u FROM Usuarios u WHERE u.direccion = :direccion")
    , @NamedQuery(name = "Usuarios.findByTelefono", query = "SELECT u FROM Usuarios u WHERE u.telefono = :telefono")
    , @NamedQuery(name = "Usuarios.findByCi", query = "SELECT u FROM Usuarios u WHERE u.ci = :ci")
    , @NamedQuery(name = "Usuarios.findByFechaHoraAlta", query = "SELECT u FROM Usuarios u WHERE u.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Usuarios.findByFechaHoraUltimoEstado", query = "SELECT u FROM Usuarios u WHERE u.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")
    , @NamedQuery(name = "Usuarios.findByCelular", query = "SELECT u FROM Usuarios u WHERE u.celular = :celular")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "contrasena")
    private String contrasena;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Size(max = 500)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ci")
    private String ci;
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
    @Size(max = 200)
    @Column(name = "celular")
    private String celular;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<Departamentos> departamentosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<Departamentos> departamentosCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<Roles> rolesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<Roles> rolesCollection1;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(mappedBy = "usuarioAlta")
    private Collection<Usuarios> usuariosCollection;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioAlta;
    @OneToMany(mappedBy = "usuarioUltimoEstado")
    private Collection<Usuarios> usuariosCollection1;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioUltimoEstado;
    @Basic(optional = true)
    @Column(name = "fecha_desde_salario")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeSalario;
    @Basic(optional = true)
    @Column(name = "salario")
    private BigDecimal salario;
    @Transient
    private BigDecimal salarioAux;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosUsuario estado;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimaObservacion")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection2;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "responsable")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection3;

    public Usuarios() {
    }

    public Usuarios(Integer id) {
        this.id = id;
    }
    public Usuarios(Integer id, Empresas empresa) {
        this.id = id;
        this.empresa = empresa;
    }

    public Usuarios(Integer id, String usuario, String contrasena, String nombresApellidos, String ci, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombresApellidos = nombresApellidos;
        this.ci = ci;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFechaDesdeSalario() {
        return fechaDesdeSalario;
    }

    public void setFechaDesdeSalario(Date fechaDesdeSalario) {
        this.fechaDesdeSalario = fechaDesdeSalario;
    }

    public BigDecimal getSalarioAux() {
        return salario;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }
    
    @XmlTransient
    public BigDecimal getSalarioAuxReal() {
        return salarioAux;
    }

    public void setSalarioAux(BigDecimal salarioAux) {
        this.salarioAux = salarioAux;
    }

    public EstadosUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadosUsuario estado) {
        this.estado = estado;
    }
    
    @XmlTransient
    public boolean verifSalario(){
        if((salario == null &&  salarioAux != null) || (salario != null &&  salarioAux == null)){
            return true;
        }else if(salario == null &&  salarioAux == null){
            return false;
        }
        
        return !salario.equals(salarioAux);
    }
    
    @XmlTransient
    public void transferirSalario(){
        salario = salarioAux;
    }
    
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
    
    @XmlTransient
    public Collection<Departamentos> getDepartamentosCollection() {
        return departamentosCollection;
    }

    public void setDepartamentosCollection(Collection<Departamentos> departamentosCollection) {
        this.departamentosCollection = departamentosCollection;
    }

    @XmlTransient
    public Collection<Departamentos> getDepartamentosCollection1() {
        return departamentosCollection1;
    }

    public void setDepartamentosCollection1(Collection<Departamentos> departamentosCollection1) {
        this.departamentosCollection1 = departamentosCollection1;
    }

    @XmlTransient
    public Collection<Roles> getRolesCollection() {
        return rolesCollection;
    }

    public void setRolesCollection(Collection<Roles> rolesCollection) {
        this.rolesCollection = rolesCollection;
    }

    @XmlTransient
    public Collection<Roles> getRolesCollection1() {
        return rolesCollection1;
    }

    public void setRolesCollection1(Collection<Roles> rolesCollection1) {
        this.rolesCollection1 = rolesCollection1;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection1() {
        return usuariosCollection1;
    }

    public void setUsuariosCollection1(Collection<Usuarios> usuariosCollection1) {
        this.usuariosCollection1 = usuariosCollection1;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection() {
        return documentosJudicialesCollection;
    }

    public void setDocumentosJudicialesCollection(Collection<DocumentosJudiciales> documentosJudicialesCollection) {
        this.documentosJudicialesCollection = documentosJudicialesCollection;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection1() {
        return documentosJudicialesCollection1;
    }

    public void setDocumentosJudicialesCollection1(Collection<DocumentosJudiciales> documentosJudicialesCollection1) {
        this.documentosJudicialesCollection1 = documentosJudicialesCollection1;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection2() {
        return documentosJudicialesCollection2;
    }

    public void setDocumentosJudicialesCollection2(Collection<DocumentosJudiciales> documentosJudicialesCollection2) {
        this.documentosJudicialesCollection2 = documentosJudicialesCollection2;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection3() {
        return documentosJudicialesCollection3;
    }

    public void setDocumentosJudicialesCollection3(Collection<DocumentosJudiciales> documentosJudicialesCollection3) {
        this.documentosJudicialesCollection3 = documentosJudicialesCollection3;
    }

    @XmlTransient
    public Collection<ObservacionesDocumentosJudiciales> getObservacionesDocumentosJudicialesCollection() {
        return observacionesDocumentosJudicialesCollection;
    }

    public void setObservacionesDocumentosJudicialesCollection(Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection) {
        this.observacionesDocumentosJudicialesCollection = observacionesDocumentosJudicialesCollection;
    }

    @XmlTransient
    public Collection<ObservacionesDocumentosJudiciales> getObservacionesDocumentosJudicialesCollection1() {
        return observacionesDocumentosJudicialesCollection1;
    }

    public void setObservacionesDocumentosJudicialesCollection1(Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection1) {
        this.observacionesDocumentosJudicialesCollection1 = observacionesDocumentosJudicialesCollection1;
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
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombresApellidos + " - " + departamento.getNombre();
    }
    
}
