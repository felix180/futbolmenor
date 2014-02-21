/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Localidad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class LocalidadJpaController implements Serializable {

    public LocalidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localidad localidad) {
        if (localidad.getDireccionList() == null) {
            localidad.setDireccionList(new ArrayList<Direccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Direccion> attachedDireccionList = new ArrayList<Direccion>();
            for (Direccion direccionListDireccionToAttach : localidad.getDireccionList()) {
                direccionListDireccionToAttach = em.getReference(direccionListDireccionToAttach.getClass(), direccionListDireccionToAttach.getId());
                attachedDireccionList.add(direccionListDireccionToAttach);
            }
            localidad.setDireccionList(attachedDireccionList);
            em.persist(localidad);
            for (Direccion direccionListDireccion : localidad.getDireccionList()) {
                Localidad oldLocalidadIdOfDireccionListDireccion = direccionListDireccion.getLocalidadId();
                direccionListDireccion.setLocalidadId(localidad);
                direccionListDireccion = em.merge(direccionListDireccion);
                if (oldLocalidadIdOfDireccionListDireccion != null) {
                    oldLocalidadIdOfDireccionListDireccion.getDireccionList().remove(direccionListDireccion);
                    oldLocalidadIdOfDireccionListDireccion = em.merge(oldLocalidadIdOfDireccionListDireccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localidad localidad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad persistentLocalidad = em.find(Localidad.class, localidad.getId());
            List<Direccion> direccionListOld = persistentLocalidad.getDireccionList();
            List<Direccion> direccionListNew = localidad.getDireccionList();
            List<String> illegalOrphanMessages = null;
            for (Direccion direccionListOldDireccion : direccionListOld) {
                if (!direccionListNew.contains(direccionListOldDireccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Direccion " + direccionListOldDireccion + " since its localidadId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Direccion> attachedDireccionListNew = new ArrayList<Direccion>();
            for (Direccion direccionListNewDireccionToAttach : direccionListNew) {
                direccionListNewDireccionToAttach = em.getReference(direccionListNewDireccionToAttach.getClass(), direccionListNewDireccionToAttach.getId());
                attachedDireccionListNew.add(direccionListNewDireccionToAttach);
            }
            direccionListNew = attachedDireccionListNew;
            localidad.setDireccionList(direccionListNew);
            localidad = em.merge(localidad);
            for (Direccion direccionListNewDireccion : direccionListNew) {
                if (!direccionListOld.contains(direccionListNewDireccion)) {
                    Localidad oldLocalidadIdOfDireccionListNewDireccion = direccionListNewDireccion.getLocalidadId();
                    direccionListNewDireccion.setLocalidadId(localidad);
                    direccionListNewDireccion = em.merge(direccionListNewDireccion);
                    if (oldLocalidadIdOfDireccionListNewDireccion != null && !oldLocalidadIdOfDireccionListNewDireccion.equals(localidad)) {
                        oldLocalidadIdOfDireccionListNewDireccion.getDireccionList().remove(direccionListNewDireccion);
                        oldLocalidadIdOfDireccionListNewDireccion = em.merge(oldLocalidadIdOfDireccionListNewDireccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = localidad.getId();
                if (findLocalidad(id) == null) {
                    throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad localidad;
            try {
                localidad = em.getReference(Localidad.class, id);
                localidad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Direccion> direccionListOrphanCheck = localidad.getDireccionList();
            for (Direccion direccionListOrphanCheckDireccion : direccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Localidad (" + localidad + ") cannot be destroyed since the Direccion " + direccionListOrphanCheckDireccion + " in its direccionList field has a non-nullable localidadId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(localidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Localidad> findLocalidadEntities() {
        return findLocalidadEntities(true, -1, -1);
    }

    public List<Localidad> findLocalidadEntities(int maxResults, int firstResult) {
        return findLocalidadEntities(false, maxResults, firstResult);
    }

    private List<Localidad> findLocalidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localidad.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Localidad findLocalidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localidad> rt = cq.from(Localidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
