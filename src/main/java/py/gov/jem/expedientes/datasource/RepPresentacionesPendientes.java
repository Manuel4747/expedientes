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
public class RepPresentacionesPendientes {

    private String caratula;
    private String nroExpediente;
    private Date fechaPresentacion; 
    private Date fechaResolucion; 
    private String motivoProceso;
    private String descripcion;
    private String tipoActuacion;
    private String remitente;
    private String preopinante;
    private String secretario;

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getNroExpediente() {
        return nroExpediente;
    }

    public void setNroExpediente(String nroExpediente) {
        this.nroExpediente = nroExpediente;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public String getMotivoProceso() {
        return motivoProceso;
    }

    public void setMotivoProceso(String motivoProceso) {
        this.motivoProceso = motivoProceso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoActuacion() {
        return tipoActuacion;
    }

    public void setTipoActuacion(String tipoActuacion) {
        this.tipoActuacion = tipoActuacion;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public Date getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(Date fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getPreopinante() {
        return preopinante;
    }

    public void setPreopinante(String preopinante) {
        this.preopinante = preopinante;
    }

    public String getSecretario() {
        return secretario;
    }

    public void setSecretario(String secretario) {
        this.secretario = secretario;
    }
    
    
    
    
}
