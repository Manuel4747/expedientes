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
public class CantidadLongItem implements Serializable {
    private Long cantidad;

    @Id
    @Column(name = "cantidad")
    public Long getCantidad() {
        return cantidad;
    }
    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }
}
