/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.datasource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author eduardo
 */
@Entity
public class RepFirmantes {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "total")
    private Integer total;
    @Column(name = "firmados")
    private Integer firmados;
    @Column(name = "diferencia")
    private Integer diferencia;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFirmados() {
        return firmados;
    }

    public void setFirmados(Integer firmados) {
        this.firmados = firmados;
    }

    public Integer getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Integer diferencia) {
        this.diferencia = diferencia;
    }
    
    
}
