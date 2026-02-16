/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "v_antecedentes_permisos_personas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VAntecedentesPermisosPersonas.findAll", query = "SELECT v FROM VAntecedentesPermisosPersonas v")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findById", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.id = :id")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByUsuaId", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.usuaId = :usuaId")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByUsuaIdRol", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.usuaId = :usuaId AND v.roleId = :rolId")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByUsuario", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.usuario = :usuario")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByRoleId", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.roleId = :roleId")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByDescRol", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.descRol = :descRol")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByPermId", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.permId = :permId")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByDescPermiso", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.descPermiso = :descPermiso")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByFuncion", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.funcion = :funcion")
    , @NamedQuery(name = "VAntecedentesPermisosPersonas.findByPermiso", query = "SELECT v FROM VAntecedentesPermisosPersonas v WHERE v.permiso = :permiso")})
public class VAntecedentesPermisosPersonas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 15)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usua_id")
    private int usuaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "role_id")
    private int roleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "desc_rol")
    private String descRol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "perm_id")
    private int permId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "desc_permiso")
    private String descPermiso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "funcion")
    private String funcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "permiso")
    private String permiso;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "menu")
    private String menu;

    public VAntecedentesPermisosPersonas() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUsuaId() {
        return usuaId;
    }

    public void setUsuaId(int usuaId) {
        this.usuaId = usuaId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getDescRol() {
        return descRol;
    }

    public void setDescRol(String descRol) {
        this.descRol = descRol;
    }

    public int getPermId() {
        return permId;
    }

    public void setPermId(int permId) {
        this.permId = permId;
    }

    public String getDescPermiso() {
        return descPermiso;
    }

    public void setDescPermiso(String descPermiso) {
        this.descPermiso = descPermiso;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
    
}
