/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class IdCantidad implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipo_actuacion")
    private Integer tipoActuacion;
    @Column(name = "visible")
    private boolean visible;
    @Basic(optional = true)
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPresentacion;
    @Column(name = "parte")
    private boolean parte;
    @Column(name = "persona")
    private boolean persona;
    @Column(name = "funcionario")
    private boolean funcionario;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTipoActuacion() {
        return tipoActuacion;
    }

    public void setTipoActuacion(Integer tipoActuacion) {
        this.tipoActuacion = tipoActuacion;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isParte() {
        return parte;
    }

    public void setParte(boolean parte) {
        this.parte = parte;
    }

    public boolean isPersona() {
        return persona;
    }

    public void setPersona(boolean persona) {
        this.persona = persona;
    }

    public boolean isFuncionario() {
        return funcionario;
    }

    public void setFuncionario(boolean funcionario) {
        this.funcionario = funcionario;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }
    
}
