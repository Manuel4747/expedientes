/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.gov.jem.expedientes.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import py.gov.jem.expedientes.models.ExpCategoriasActuacion;

/**
 *
 * @author eduardo
 */
@Stateless
public class ExpCategoriasActuacionFacade extends AbstractFacade<ExpCategoriasActuacion> {

    @PersistenceContext(unitName = "gestionstarticPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public ExpCategoriasActuacionFacade() {
        super(ExpCategoriasActuacion.class);
    }
    
}
