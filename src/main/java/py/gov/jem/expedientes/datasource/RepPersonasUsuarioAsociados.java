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
public class RepPersonasUsuarioAsociados implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "ci")
    private String ci;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "email")
    private String email;
    @Column(name = "exp_administrativo")
    private String expAdministrativo;
    @Column(name = "res_exp_administrativo")
    private String resExpAdministrativo;
    @Column(name = "exp_electronico")
    private String expElectronico;
    @Column(name = "res_exp_electronico")
    private String resExpElectronico;
    @Column(name = "persona_id")
    private String personaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpAdministrativo() {
        return expAdministrativo;
    }

    public void setExpAdministrativo(String expAdministrativo) {
        this.expAdministrativo = expAdministrativo;
    }

    public String getResExpAdministrativo() {
        return resExpAdministrativo;
    }

    public void setResExpAdministrativo(String resExpAdministrativo) {
        this.resExpAdministrativo = resExpAdministrativo;
    }

    public String getExpElectronico() {
        return expElectronico;
    }

    public void setExpElectronico(String expElectronico) {
        this.expElectronico = expElectronico;
    }

    public String getResExpElectronico() {
        return resExpElectronico;
    }

    public void setResExpElectronico(String resExpElectronico) {
        this.resExpElectronico = resExpElectronico;
    }

    public String getPersonaId() {
        return personaId;
    }

    public void setPersonaId(String personaId) {
        this.personaId = personaId;
    }
    
    public RepPersonasUsuarioAsociados() {
        
    }

    public RepPersonasUsuarioAsociados(RepPersonasUsuarioAsociados rep) {

        this.id = rep.id;
        this.ci = rep.ci;
        this.nombre = rep.nombre;
        this.cargo = rep.cargo;
        this.telefono = rep.telefono;
        this.email = rep.email;
        this.expAdministrativo = rep.expAdministrativo;
        this.resExpAdministrativo = rep.resExpAdministrativo;
        this.expElectronico = rep.expElectronico;
        this.personaId = rep.personaId;
    }

}
