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
@Table(name = "exp_envios_email")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpEnviosEmail.findAll", query = "SELECT m FROM ExpEnviosEmail m")
    , @NamedQuery(name = "ExpEnviosEmail.findByActuacion", query = "SELECT m FROM ExpEnviosEmail m WHERE m.actuacion = :actuacion ORDER BY m.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpEnviosEmail.findByFechaHoraEnvioProgramadoEnviado", query = "SELECT m FROM ExpEnviosEmail m WHERE m.fechaHoraEnvioProgramado <= :fechaHoraEnvioProgramado and m.enviado = :enviado ORDER BY m.fechaHoraEnvioProgramado, m.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpEnviosEmail.findById", query = "SELECT m FROM ExpEnviosEmail m WHERE m.id = :id")
    , @NamedQuery(name = "ExpEnviosEmail.findByFechaHoraAlta", query = "SELECT m FROM ExpEnviosEmail m WHERE m.fechaHoraAlta = :fechaHoraAlta")})
public class ExpEnviosEmail implements Serializable {

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
    @Basic(optional = true)
    @Column(name = "fecha_hora_envio_programado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEnvioProgramado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEnvio;
    @JoinColumn(name = "actuacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ExpActuaciones actuacion;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "persona_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaAlta;
    @Basic(optional = true)
    @Column(name = "asunto")
    private String asunto;
    @Basic(optional = true)
    @Column(name = "mensaje")
    private String mensaje;
    @Basic(optional = true)
    @Column(name = "email")
    private String email;
    @Column(name = "enviado")
    private boolean enviado;

    public ExpEnviosEmail() {
    }

    public ExpEnviosEmail(Integer id) {
        this.id = id;
    }

    public ExpEnviosEmail(Date fechaHoraAlta, Date fechaHoraEnvio, Date fechaHoraEnvioProgramado, ExpActuaciones actuacion, DocumentosJudiciales documentoJudicial, Personas personaAlta, String asunto, String mensaje, String email, boolean enviado) {
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraEnvio = fechaHoraEnvio;
        this.fechaHoraEnvioProgramado = fechaHoraEnvioProgramado;
        this.actuacion = actuacion;
        this.documentoJudicial = documentoJudicial;
        this.personaAlta = personaAlta;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.email = email;
        this.enviado = enviado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
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

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public Date getFechaHoraEnvioProgramado() {
        return fechaHoraEnvioProgramado;
    }

    public void setFechaHoraEnvioProgramado(Date fechaHoraEnvioProgramado) {
        this.fechaHoraEnvioProgramado = fechaHoraEnvioProgramado;
    }

    public Date getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }

    public void setFechaHoraEnvio(Date fechaHoraEnvio) {
        this.fechaHoraEnvio = fechaHoraEnvio;
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
        if (!(object instanceof ExpEnviosEmail)) {
            return false;
        }
        ExpEnviosEmail other = (ExpEnviosEmail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpEnviosEmail[ id=" + id + " ]";
    }
    
}
