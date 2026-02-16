/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.datasource;

import java.util.List;

/**
 *
 * @author eduardo
 */
public class Elemento {
    private String descripcion;
    private List<String> firmantes;
    private String detalle;
    
    public Elemento(String descripcion, String detalle){
        this.descripcion = descripcion;
        this.detalle = detalle;
    }
    
    public Elemento(String descripcion, List<String> firmantes, String detalle){
        this.descripcion = descripcion;
        this.firmantes = firmantes;
        this.detalle = detalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public List<String> getFirmantes() {
        return firmantes;
    }

    public void setFirmantes(List<String> firmantes) {
        this.firmantes = firmantes;
    }

    public String firmantesItem(int item) {
        if(firmantes != null){
            if(firmantes.size() > item){
                return firmantes.get(item);
            }
        }
        return "";
    }
    
}
