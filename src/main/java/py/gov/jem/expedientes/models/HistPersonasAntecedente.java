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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "hist_personas_antecedente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistPersonasAntecedente.findAll", query = "SELECT r FROM HistPersonasAntecedente r")
    , @NamedQuery(name = "HistPersonasAntecedente.findById", query = "SELECT r FROM HistPersonasAntecedente r WHERE r.id = :id")})
public class HistPersonasAntecedente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "despacho_persona_antes", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DespachosPersona despachoPersonaAntes;
    @JoinColumn(name = "despacho_persona_despues", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DespachosPersona despachoPersonaDespues;
    @JoinColumn(name = "departamento_persona_antes", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DepartamentosPersona departamentoPersonaAntes;
    @JoinColumn(name = "departamento_persona_despues", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DepartamentosPersona departamentoPersonaDespues;
    @JoinColumn(name = "localidad_persona_antes", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LocalidadesPersona localidadPersonaAntes;
    @JoinColumn(name = "localidad_persona_despues", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LocalidadesPersona localidadPersonaDespues;
    @Column(name = "email_antes")
    private String emailAntes;
    @Column(name = "email_despues")
    private String emailDespues;
    @Column(name = "telefono1_antes")
    private String telefono1Antes;
    @Column(name = "telefono1_despues")
    private String telefono1Despues;
    @Column(name = "telefono2_antes")
    private String telefono2Antes;
    @Column(name = "telefono2_despues")
    private String telefono2Despues;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @JoinColumn(name = "pedido_antecedente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpPedidosAntecedente pedidoAntecedente;

    public HistPersonasAntecedente() {
    }

    public HistPersonasAntecedente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DespachosPersona getDespachoPersonaAntes() {
        return despachoPersonaAntes;
    }

    public void setDespachoPersonaAntes(DespachosPersona despachoPersonaAntes) {
        this.despachoPersonaAntes = despachoPersonaAntes;
    }

    public DespachosPersona getDespachoPersonaDespues() {
        return despachoPersonaDespues;
    }

    public void setDespachoPersonaDespues(DespachosPersona despachoPersonaDespues) {
        this.despachoPersonaDespues = despachoPersonaDespues;
    }

    public DepartamentosPersona getDepartamentoPersonaAntes() {
        return departamentoPersonaAntes;
    }

    public void setDepartamentoPersonaAntes(DepartamentosPersona departamentoPersonaAntes) {
        this.departamentoPersonaAntes = departamentoPersonaAntes;
    }

    public DepartamentosPersona getDepartamentoPersonaDespues() {
        return departamentoPersonaDespues;
    }

    public void setDepartamentoPersonaDespues(DepartamentosPersona departamentoPersonaDespues) {
        this.departamentoPersonaDespues = departamentoPersonaDespues;
    }

    public LocalidadesPersona getLocalidadPersonaAntes() {
        return localidadPersonaAntes;
    }

    public void setLocalidadPersonaAntes(LocalidadesPersona localidadPersonaAntes) {
        this.localidadPersonaAntes = localidadPersonaAntes;
    }

    public LocalidadesPersona getLocalidadPersonaDespues() {
        return localidadPersonaDespues;
    }

    public void setLocalidadPersonaDespues(LocalidadesPersona localidadPersonaDespues) {
        this.localidadPersonaDespues = localidadPersonaDespues;
    }

    public String getEmailAntes() {
        return emailAntes;
    }

    public void setEmailAntes(String emailAntes) {
        this.emailAntes = emailAntes;
    }

    public String getEmailDespues() {
        return emailDespues;
    }

    public void setEmailDespues(String emailDespues) {
        this.emailDespues = emailDespues;
    }

    public String getTelefono1Antes() {
        return telefono1Antes;
    }

    public void setTelefono1Antes(String telefono1Antes) {
        this.telefono1Antes = telefono1Antes;
    }

    public String getTelefono1Despues() {
        return telefono1Despues;
    }

    public void setTelefono1Despues(String telefono1Despues) {
        this.telefono1Despues = telefono1Despues;
    }

    public String getTelefono2Antes() {
        return telefono2Antes;
    }

    public void setTelefono2Antes(String telefono2Antes) {
        this.telefono2Antes = telefono2Antes;
    }

    public String getTelefono2Despues() {
        return telefono2Despues;
    }

    public void setTelefono2Despues(String telefono2Despues) {
        this.telefono2Despues = telefono2Despues;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Personas getPersonaAlta() {
        return personaAlta;
    }

    public void setPersonaAlta(Personas personaAlta) {
        this.personaAlta = personaAlta;
    }

    public ExpPedidosAntecedente getPedidoAntecedente() {
        return pedidoAntecedente;
    }

    public void setPedidoAntecedente(ExpPedidosAntecedente pedidoAntecedente) {
        this.pedidoAntecedente = pedidoAntecedente;
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
        if (!(object instanceof HistPersonasAntecedente)) {
            return false;
        }
        HistPersonasAntecedente other = (HistPersonasAntecedente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.HistPersonasAntecedente[ id=" + id + " ]";
    }
    
}
