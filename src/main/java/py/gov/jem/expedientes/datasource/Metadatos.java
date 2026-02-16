/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.datasource;

/**
 *
 * @author eduardo
 */
public class Metadatos {
    private String clave;
    private String valor;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public Metadatos(){
        
    }
    
    public Metadatos(String clave, String valor){
        this.clave = clave;
        this.valor = valor;
    }
    
}
