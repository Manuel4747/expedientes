/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.datasource;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CantidadItem implements Serializable {
    private Integer cantidad;

    @Id
    @Column(name = "cantidad")
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
