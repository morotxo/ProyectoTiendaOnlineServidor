/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import com.mycompany.proyectotiendaonlinejsf.Cliente;
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
public class ClienteFacade extends AbstractFacade<Cliente> {
    @PersistenceContext(unitName = "com.mycompany_ProyectoTiendaOnlineJSF_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }
    public List<Cliente> findbyCiudad (String ciudad) {
        Query query = this.em.createNamedQuery(Cliente.findbyciudad);
        query.setParameter("ciudad", ciudad);
        return query.getResultList();
    }
    public List<Cliente> findbyApellido (String apellido) {
        Query query = this.em.createNamedQuery(Cliente.findbyapellido);
        query.setParameter("apellido", apellido);
        return query.getResultList();
    }
    public List<Cliente> findbyEmail (String email) {
        Query query = this.em.createNamedQuery(Cliente.findbyemail);
        query.setParameter("email", email);
        return query.getResultList();
    }
    public List<Cliente> findbyEstado (Character estado) {
        Query query = this.em.createNamedQuery(Cliente.findbyestado);
        query.setParameter("estado", estado);
        return query.getResultList();
    }
    public List<Cliente> findbyIdCliente (Integer idCliente) {
        Query query = this.em.createNamedQuery(Cliente.findbyidcliente);
        query.setParameter("idCliente", idCliente);
        return query.getResultList();
    }
    public List<Cliente> findbyNombre (String nombre) {
        Query query = this.em.createNamedQuery(Cliente.findbynombre);
        query.setParameter("nombre", nombre);
        return query.getResultList();
    }
    public List<Cliente> findbyPais (String pais) {
        Query query = this.em.createNamedQuery(Cliente.findbypais);
        query.setParameter("pais", pais);
        return query.getResultList();
    }
    public List<Cliente> findbyTelefono (String telefono) {
        Query query = this.em.createNamedQuery(Cliente.findbytelefono);
        query.setParameter("telefono", telefono);
        return query.getResultList();
    }
    
}
