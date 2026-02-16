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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "chart_expedientes_por_dia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChartExpedientesPorDia.findByRangoFechaAlta", query = "SELECT r FROM ChartExpedientesPorDia r WHERE r.fechaAlta >= :fechaDesde AND r.fechaAlta <= :fechaHasta ORDER BY r.fechaAlta")})
public class ChartExpedientesPorDia implements Serializable {
    @Id
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_alta")
    private Date fechaAlta;
    @Column(name = "cantidad")
    private Integer cantidad;
    
    public ChartExpedientesPorDia(){
        
    }
    
    public ChartExpedientesPorDia(Date fechaAlta, Integer cantidad){
        this.fechaAlta = fechaAlta;
        this.cantidad = cantidad;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
