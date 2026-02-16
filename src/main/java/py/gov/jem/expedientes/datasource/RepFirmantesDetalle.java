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

/**
 *
 * @author eduardo
 */
@Entity
public class RepFirmantesDetalle implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "preopinante")
    private String preopinante;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "causa")
    private String causa;
    @Column(name = "nro")
    private String nro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public String getPreopinante() {
        return preopinante;
    }

    public void setPreopinante(String preopinante) {
        this.preopinante = preopinante;
    }
    
}
