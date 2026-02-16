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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "entradas_documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntradasDocumentosJudiciales.findAll", query = "SELECT e FROM EntradasDocumentosJudiciales e")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findById", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.id = :id")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByFechaHoraAlta", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByRecibidoPor", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.recibidoPor = :recibidoPor")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByNroMesaEntrada", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.nroMesaEntrada = :nroMesaEntrada")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByEntregadoPor", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.entregadoPor = :entregadoPor")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByTelefono", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.telefono = :telefono")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByNroCedulaRuc", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.nroCedulaRuc = :nroCedulaRuc")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByEmpresa", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.empresa = :empresa")
    , @NamedQuery(name = "EntradasDocumentosJudiciales.findByFechaHoraUltimoEstado", query = "SELECT e FROM EntradasDocumentosJudiciales e WHERE e.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class EntradasDocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "recibido_por", referencedColumnName = "id")
    @ManyToOne
    private Usuarios recibidoPor;
    @Basic(optional = false)
    @NotNull
    @Size(max = 200)
    @Column(name = "nro_mesa_entrada")
    private String nroMesaEntrada;
    @Size(max = 200)
    @Column(name = "entregado_por")
    private String entregadoPor;
    @Size(max = 200)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 30)
    @Column(name = "nro_cedula_ruc")
    private String nroCedulaRuc;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;

    public EntradasDocumentosJudiciales() {
    }

    public EntradasDocumentosJudiciales(Integer id) {
        this.id = id;
    }

    public EntradasDocumentosJudiciales(Integer id, Date fechaHoraAlta, Usuarios recibidoPor, Empresas empresa, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.recibidoPor = recibidoPor;
        this.empresa = empresa;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNroMesaEntrada() {
        return nroMesaEntrada;
    }

    public void setNroMesaEntrada(String nroMesaEntrada) {
        this.nroMesaEntrada = nroMesaEntrada;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Usuarios getRecibidoPor() {
        return recibidoPor;
    }

    public void setRecibidoPor(Usuarios recibidoPor) {
        this.recibidoPor = recibidoPor;
    }

    public String getEntregadoPor() {
        return entregadoPor;
    }

    public void setEntregadoPor(String entregadoPor) {
        this.entregadoPor = entregadoPor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNroCedulaRuc() {
        return nroCedulaRuc;
    }

    public void setNroCedulaRuc(String nroCedulaRuc) {
        this.nroCedulaRuc = nroCedulaRuc;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntradasDocumentosJudiciales)) {
            return false;
        }
        EntradasDocumentosJudiciales other = (EntradasDocumentosJudiciales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nroMesaEntrada;
    }
    
}
