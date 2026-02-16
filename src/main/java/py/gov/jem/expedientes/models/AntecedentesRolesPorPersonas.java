/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.models;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "antecedentes_roles_por_personas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AntecedentesRolesPorPersonas.findAll", query = "SELECT r FROM AntecedentesRolesPorPersonas r")
    , @NamedQuery(name = "AntecedentesRolesPorPersonas.findByPersona", query = "SELECT r FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.persona = :persona")
    , @NamedQuery(name = "AntecedentesRolesPorPersonas.findByRol", query = "SELECT r FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol = :rol")
    , @NamedQuery(name = "AntecedentesRolesPorPersonas.findByRoles", query = "SELECT r FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol in :roles")
    , @NamedQuery(name = "AntecedentesRolesPorPersonas.findByPersonaRol", query = "SELECT r FROM AntecedentesRolesPorPersonas r WHERE r.antecedentesRolesPorPersonasPK.rol = :rol AND r.antecedentesRolesPorPersonasPK.persona = :persona")})
public class AntecedentesRolesPorPersonas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AntecedentesRolesPorPersonasPK antecedentesRolesPorPersonasPK;
    @JoinColumn(name = "rol", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AntecedentesRoles roles;
    @JoinColumn(name = "persona", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personas personas;

    public AntecedentesRolesPorPersonas() {
    }

    public AntecedentesRolesPorPersonas(AntecedentesRolesPorPersonasPK antecedentesRolesPorPersonasPK) {
        this.antecedentesRolesPorPersonasPK = antecedentesRolesPorPersonasPK;
    }

    public AntecedentesRolesPorPersonas(int usuario, int rol) {
        this.antecedentesRolesPorPersonasPK = new AntecedentesRolesPorPersonasPK(usuario, rol);
    }

    public AntecedentesRolesPorPersonasPK getAntecedentesRolesPorPersonasPK() {
        return antecedentesRolesPorPersonasPK;
    }

    public void setAntecedentesRolesPorPersonasPK(AntecedentesRolesPorPersonasPK antecedentesRolesPorPersonasPK) {
        this.antecedentesRolesPorPersonasPK = antecedentesRolesPorPersonasPK;
    }

    public AntecedentesRoles getRoles() {
        return roles;
    }

    public void setRoles(AntecedentesRoles roles) {
        this.roles = roles;
    }

    public Personas getPersonas() {
        return personas;
    }

    public void setPersonas(Personas personas) {
        this.personas = personas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (antecedentesRolesPorPersonasPK != null ? antecedentesRolesPorPersonasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AntecedentesRolesPorPersonas)) {
            return false;
        }
        AntecedentesRolesPorPersonas other = (AntecedentesRolesPorPersonas) object;
        if ((this.antecedentesRolesPorPersonasPK == null && other.antecedentesRolesPorPersonasPK != null) || (this.antecedentesRolesPorPersonasPK != null && !this.antecedentesRolesPorPersonasPK.equals(other.antecedentesRolesPorPersonasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return roles==null?"":roles.getDescripcion();
    }
    
}
