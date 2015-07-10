/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.mycompany.proyectotiendaonlinejsf.Detalle;
import com.mycompany.proyectotiendaonlinejsf.Pedido;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author david13mo
 */
@Stateless
public class DetalleFacade extends AbstractFacade<Detalle> {
    @PersistenceContext(unitName = "com.mycompany_ProyectoTiendaOnlineJSF_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DetalleFacade() {
        super(Detalle.class);
    }
    public List<Detalle> findbyId(Pedido pedido) {
        Query query = this.em.createNamedQuery(Detalle.findbypedido);
        query.setParameter("pedido",pedido);
        return query.getResultList();
    }

    
}
