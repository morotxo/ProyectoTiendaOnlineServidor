/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.mycompany.proyectotiendaonlinejsf.Producto;
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
public class ProductoFacade extends AbstractFacade<Producto> {

    @PersistenceContext(unitName = "com.mycompany_ProyectoTiendaOnlineJSF_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductoFacade() {
        super(Producto.class);
    }

    public List<Producto> findbyMarca(String marca) {
        Query query = this.em.createNamedQuery(Producto.findbymarca);
        query.setParameter("marca", marca);
        return query.getResultList();
    }

    public List<Producto> findbyId(Integer id) {
        Query query = this.em.createNamedQuery(Producto.findbyidproducto);
        query.setParameter("idProducto", id);
        return query.getResultList();
    }

    public List<Producto> findbyPrecio(Double precio) {
        Query query = this.em.createNamedQuery(Producto.findbyprecio);
        query.setParameter("precio", precio);
        return query.getResultList();
    }

    public List<Producto> findbyEstado(Character estado) {
        Query query = this.em.createNamedQuery(Producto.findbyestado);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

}
