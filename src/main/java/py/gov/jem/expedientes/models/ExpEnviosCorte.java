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
@Table(name = "exp_envios_corte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpEnviosCorte.findAll", query = "SELECT t FROM ExpEnviosCorte t")
    , @NamedQuery(name = "ExpEnviosCorte.findById", query = "SELECT t FROM ExpEnviosCorte t WHERE t.id = :id")})
public class ExpEnviosCorte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_envio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraEnvio;
    @JoinColumn(name = "actuacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ExpActuaciones actuacion;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentoJudicial;
    @JoinColumn(name = "persona_envio", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas personaEnvio;
    @Basic(optional = false)
    @Size(max = 200)
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Lob
    @Size(max = 65535)
    @Column(name = "pedido")
    private String pedido;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "respuesta")
    private String respuesta;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "cod_respuesta")
    private String codRespuesta;
    @Basic(optional = true)
    @Column(name = "cod_actuacion_caso")
    private Integer codActuacionCaso;
    

    public ExpEnviosCorte() {
    }

    public ExpEnviosCorte(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }

    public void setFechaHoraEnvio(Date fechaHoraEnvio) {
        this.fechaHoraEnvio = fechaHoraEnvio;
    }

    public ExpActuaciones getActuacion() {
        return actuacion;
    }

    public void setActuacion(ExpActuaciones actuacion) {
        this.actuacion = actuacion;
    }

    public DocumentosJudiciales getDocumentoJudicial() {
        return documentoJudicial;
    }

    public void setDocumentoJudicial(DocumentosJudiciales documentoJudicial) {
        this.documentoJudicial = documentoJudicial;
    }

    public Personas getPersonaEnvio() {
        return personaEnvio;
    }

    public void setPersonaEnvio(Personas personaEnvio) {
        this.personaEnvio = personaEnvio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getCodRespuesta() {
        return codRespuesta;
    }

    public void setCodRespuesta(String codRespuesta) {
        this.codRespuesta = codRespuesta;
    }

    public Integer getCodActuacionCaso() {
        return codActuacionCaso;
    }

    public void setCodActuacionCaso(Integer codActuacionCaso) {
        this.codActuacionCaso = codActuacionCaso;
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
        if (!(object instanceof ExpEnviosCorte)) {
            return false;
        }
        ExpEnviosCorte other = (ExpEnviosCorte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpEnviosCorte[ id=" + id + " ]";
    }
    
}
