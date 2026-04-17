/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.gov.jem.expedientes.datasource;

import java.util.Date;

/**
 *
 * @author Grecia
 */
public class RepRecusaciones {
     private String causa;
         private String caratula;
         private String motivoProceso;
         private Date fechaHoraAlta;
          private String remitente;

    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getMotivoProceso() {
        return motivoProceso;
    }

    public void setMotivoProceso(String motivoProceso) {
        this.motivoProceso = motivoProceso;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }


    
    
    
}
