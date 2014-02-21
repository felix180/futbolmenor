/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.ArbitroPartido;
import com.spontecorp.littleligues.model.torneo.TipoArbitro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class TipoArbitroJpaController implements Serializable {

    public TipoArbitroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoArbitro tipoArbitro) {
        if (tipoArbitro.getArbitroPartidoList() == null) {
            tipoArbitro.setArbitroPartidoList(new ArrayList<ArbitroPartido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ArbitroPartido> attachedArbitroPartidoList = new ArrayList<ArbitroPartido>();
            for (ArbitroPartido arbitroPartidoListArbitroPartidoToAttach : tipoArbitro.getArbitroPartidoList()) {
                arbitroPartidoListArbitroPartidoToAttach = em.getReference(arbitroPartidoListArbitroPartidoToAttach.getClass(), arbitroPartidoListArbitroPartidoToAttach.getId());
                attachedArbitroPartidoList.add(arbitroPartidoListArbitroPartidoToAttach);
            }
            tipoArbitro.setArbitroPartidoList(attachedArbitroPartidoList);
            em.persist(tipoArbitro);
            for (ArbitroPartido arbitroPartidoListArbitroPartido : tipoArbitro.getArbitroPartidoList()) {
                TipoArbitro oldTipoArbitroIdOfArbitroPartidoListArbitroPartido = arbitroPartidoListArbitroPartido.getTipoArbitroId();
                arbitroPartidoListArbitroPartido.setTipoArbitroId(tipoArbitro);
                arbitroPartidoListArbitroPartido = em.merge(arbitroPartidoListArbitroPartido);
                if (oldTipoArbitroIdOfArbitroPartidoListArbitroPartido != null) {
                    oldTipoArbitroIdOfArbitroPartidoListArbitroPartido.getArbitroPartidoList().remove(arbitroPartidoListArbitroPartido);
                    oldTipoArbitroIdOfArbitroPartidoListArbitroPartido = em.merge(oldTipoArbitroIdOfArbitroPartidoListArbitroPartido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoArbitro tipoArbitro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoArbitro persistentTipoArbitro = em.find(TipoArbitro.class, tipoArbitro.getId());
            List<ArbitroPartido> arbitroPartidoListOld = persistentTipoArbitro.getArbitroPartidoList();
            List<ArbitroPartido> arbitroPartidoListNew = tipoArbitro.getArbitroPartidoList();
            List<String> illegalOrphanMessages = null;
            for (ArbitroPartido arbitroPartidoListOldArbitroPartido : arbitroPartidoListOld) {
                if (!arbitroPartidoListNew.contains(arbitroPartidoListOldArbitroPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArbitroPartido " + arbitroPartidoListOldArbitroPartido + " since its tipoArbitroId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ArbitroPartido> attachedArbitroPartidoListNew = new ArrayList<ArbitroPartido>();
            for (ArbitroPartido arbitroPartidoListNewArbitroPartidoToAttach : arbitroPartidoListNew) {
                arbitroPartidoListNewArbitroPartidoToAttach = em.getReference(arbitroPartidoListNewArbitroPartidoToAttach.getClass(), arbitroPartidoListNewArbitroPartidoToAttach.getId());
                attachedArbitroPartidoListNew.add(arbitroPartidoListNewArbitroPartidoToAttach);
            }
            arbitroPartidoListNew = attachedArbitroPartidoListNew;
            tipoArbitro.setArbitroPartidoList(arbitroPartidoListNew);
            tipoArbitro = em.merge(tipoArbitro);
            for (ArbitroPartido arbitroPartidoListNewArbitroPartido : arbitroPartidoListNew) {
                if (!arbitroPartidoListOld.contains(arbitroPartidoListNewArbitroPartido)) {
                    TipoArbitro oldTipoArbitroIdOfArbitroPartidoListNewArbitroPartido = arbitroPartidoListNewArbitroPartido.getTipoArbitroId();
                    arbitroPartidoListNewArbitroPartido.setTipoArbitroId(tipoArbitro);
                    arbitroPartidoListNewArbitroPartido = em.merge(arbitroPartidoListNewArbitroPartido);
                    if (oldTipoArbitroIdOfArbitroPartidoListNewArbitroPartido != null && !oldTipoArbitroIdOfArbitroPartidoListNewArbitroPartido.equals(tipoArbitro)) {
                        oldTipoArbitroIdOfArbitroPartidoListNewArbitroPartido.getArbitroPartidoList().remove(arbitroPartidoListNewArbitroPartido);
                        oldTipoArbitroIdOfArbitroPartidoListNewArbitroPartido = em.merge(oldTipoArbitroIdOfArbitroPartidoListNewArbitroPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoArbitro.getId();
                if (findTipoArbitro(id) == null) {
                    throw new NonexistentEntityException("The tipoArbitro with id " + id + " no longer exists.");
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
            TipoArbitro tipoArbitro;
            try {
                tipoArbitro = em.getReference(TipoArbitro.class, id);
                tipoArbitro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoArbitro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ArbitroPartido> arbitroPartidoListOrphanCheck = tipoArbitro.getArbitroPartidoList();
            for (ArbitroPartido arbitroPartidoListOrphanCheckArbitroPartido : arbitroPartidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoArbitro (" + tipoArbitro + ") cannot be destroyed since the ArbitroPartido " + arbitroPartidoListOrphanCheckArbitroPartido + " in its arbitroPartidoList field has a non-nullable tipoArbitroId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoArbitro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoArbitro> findTipoArbitroEntities() {
        return findTipoArbitroEntities(true, -1, -1);
    }

    public List<TipoArbitro> findTipoArbitroEntities(int maxResults, int firstResult) {
        return findTipoArbitroEntities(false, maxResults, firstResult);
    }

    private List<TipoArbitro> findTipoArbitroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoArbitro.class));
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

    public TipoArbitro findTipoArbitro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoArbitro.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoArbitroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoArbitro> rt = cq.from(TipoArbitro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
