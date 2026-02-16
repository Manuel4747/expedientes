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
@Table(name = "exp_pedidos_antecedente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpPedidosAntecedente.findAll", query = "SELECT p FROM ExpPedidosAntecedente p order by p.fechaHoraAlta")
    , @NamedQuery(name = "ExpPedidosAntecedente.findById", query = "SELECT p FROM ExpPedidosAntecedente p WHERE p.id = :id")
    , @NamedQuery(name = "ExpPedidosAntecedente.findByEstado", query = "SELECT p FROM ExpPedidosAntecedente p WHERE p.estado = :estado ORDER BY p.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpPedidosAntecedente.findByPersona", query = "SELECT p FROM ExpPedidosAntecedente p WHERE p.persona = :persona ORDER BY p.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpPedidosAntecedente.findByPersonaAndEstado", query = "SELECT p FROM ExpPedidosAntecedente p WHERE p.persona = :persona and p.estado = :estado ORDER BY p.fechaHoraAlta DESC")
    , @NamedQuery(name = "ExpPedidosAntecedente.findByNotEstado", query = "SELECT p FROM ExpPedidosAntecedente p WHERE p.estado <> :estado ORDER BY p.fechaHoraRespuesta DESC")
    , @NamedQuery(name = "ExpPedidosAntecedente.findByFechaHoraAlta", query = "SELECT p FROM ExpPedidosAntecedente p WHERE p.fechaHoraAlta = :fechaHoraAlta")})
public class ExpPedidosAntecedente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id; 
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = true)
    @Column(name = "fecha_hora_respuesta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraRespuesta;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "persona_respuesta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaRespuesta;
    @JoinColumn(name = "antecedente", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Antecedentes antecedente;
    @Lob
    @Size(max = 65535)
    @Column(name = "observacion")
    private String observacion;
    @Transient
    private String estadoString;
    @JoinColumn(name = "persona_borrado_antecedente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaBorradoAntecedente;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado_antecedente")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorradoAntecedente;
    @JoinColumn(name = "persona_confirmacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaConfirmacion;
    @Basic(optional = true)
    @Column(name = "fecha_hora_confirmacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraConfirmacion;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "path_archivo_pendiente")
    private String pathArchivoPendiente;

    public ExpPedidosAntecedente() {
    }

    public ExpPedidosAntecedente(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getFechaHoraRespuesta() {
        return fechaHoraRespuesta;
    }

    public void setFechaHoraRespuesta(Date fechaHoraRespuesta) {
        this.fechaHoraRespuesta = fechaHoraRespuesta;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Personas getPersonaRespuesta() {
        return personaRespuesta;
    }

    public void setPersonaRespuesta(Personas personaRespuesta) {
        this.personaRespuesta = personaRespuesta;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Antecedentes getAntecedente() {
        return antecedente;
    }

    public void setAntecedente(Antecedentes antecedente) {
        this.antecedente = antecedente;
    }

    public Personas getPersonaBorradoAntecedente() {
        return personaBorradoAntecedente;
    }

    public void setPersonaBorradoAntecedente(Personas personaBorradoAntecedente) {
        this.personaBorradoAntecedente = personaBorradoAntecedente;
    }

    public Date getFechaHoraBorradoAntecedente() {
        return fechaHoraBorradoAntecedente;
    }

    public void setFechaHoraBorradoAntecedente(Date fechaHoraBorradoAntecedente) {
        this.fechaHoraBorradoAntecedente = fechaHoraBorradoAntecedente;
    }

    public Personas getPersonaConfirmacion() {
        return personaConfirmacion;
    }

    public void setPersonaConfirmacion(Personas personaConfirmacion) {
        this.personaConfirmacion = personaConfirmacion;
    }

    public Date getFechaHoraConfirmacion() {
        return fechaHoraConfirmacion;
    }

    public void setFechaHoraConfirmacion(Date fechaHoraConfirmacion) {
        this.fechaHoraConfirmacion = fechaHoraConfirmacion;
    }

    public String getPathArchivoPendiente() {
        return pathArchivoPendiente;
    }

    public void setPathArchivoPendiente(String pathArchivoPendiente) {
        this.pathArchivoPendiente = pathArchivoPendiente;
    }

    public String getEstadoString() {
        if(estado != null){
            if("SI".equals(estado)){
                return "GENERADO";
            }else if("NO".equals(estado)){
                return "RECHAZADO";
            }else if("AC".equals(estado)){
                return "PENDIENTE";
            }
        }
        return estadoString;
    }

    public void setEstadoString(String estadoString) {
        this.estadoString = estadoString;
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
        if (!(object instanceof ExpPedidosAntecedente)) {
            return false;
        }
        ExpPedidosAntecedente other = (ExpPedidosAntecedente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.ExpPedidosAntecedente[ id=" + id + " ]";
    }
    
}
