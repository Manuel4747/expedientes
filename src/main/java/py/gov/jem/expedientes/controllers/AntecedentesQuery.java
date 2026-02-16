package py.gov.jem.expedientes.controllers;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import py.gov.jem.expedientes.models.AntecedentesRoles;
import py.gov.jem.expedientes.models.Personas;
import py.gov.jem.expedientes.models.Usuarios;
import py.gov.jem.expedientes.models.VAntecedentesPermisosPersonas;

public class AntecedentesQuery {

    private EntityManagerFactory emf;
    private EntityManager em;
    private AntecedentesRoles rolElegido;

    public AntecedentesQuery() {
        emf = Persistence.createEntityManagerFactory("gestionstarticPU");
        em = emf.createEntityManager();
        //em.getTransaction().begin();
    }

}
