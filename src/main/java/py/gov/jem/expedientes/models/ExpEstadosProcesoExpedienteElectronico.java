/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "exp_estados_proceso_expediente_electronico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExpEstadosProcesoExpedienteElectronico.findAll", query = "SELECT e FROM ExpEstadosProcesoExpedienteElectronico e")
    , @NamedQuery(name = "ExpEstadosProcesoExpedienteElectronico.findByCodigo", query = "SELECT e FROM ExpEstadosProcesoExpedienteElectronico e WHERE e.codigo = :codigo")
    , @NamedQuery(name = "ExpEstadosProcesoExpedienteElectronico.findByEnProceso", query = "SELECT e FROM ExpEstadosProcesoExpedienteElectronico e WHERE e.enProceso = :enProceso")
    , @NamedQuery(name = "ExpEstadosProcesoExpedienteElectronico.findByDescripcion", query = "SELECT e FROM ExpEstadosProcesoExpedienteElectronico e WHERE e.descripcion = :descripcion")})
public class ExpEstadosProcesoExpedienteElectronico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "en_proceso")
    private boolean enProceso;

    public ExpEstadosProcesoExpedienteElectronico() {
    }

    public ExpEstadosProcesoExpedienteElectronico(String codigo) {
        this.codigo = codigo;
    }

    public ExpEstadosProcesoExpedienteElectronico(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEnProceso() {
        return enProceso;
    }

    public void setEnProceso(boolean enProceso) {
        this.enProceso = enProceso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExpEstadosProcesoExpedienteElectronico)) {
            return false;
        }
        ExpEstadosProcesoExpedienteElectronico other = (ExpEstadosProcesoExpedienteElectronico) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.models.ExpEstadosProcesoExpedienteElectronico[ codigo=" + codigo + " ]";
    }
    
}
