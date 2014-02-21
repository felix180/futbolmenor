/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.torneo.Arbitro;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.Asociacion;
import com.spontecorp.littleligues.model.torneo.ArbitroPartido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class ArbitroJpaController implements Serializable {

    public ArbitroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Arbitro arbitro) {
        if (arbitro.getArbitroPartidoList() == null) {
            arbitro.setArbitroPartidoList(new ArrayList<ArbitroPartido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asociacion asociacionId = arbitro.getAsociacionId();
            if (asociacionId != null) {
                asociacionId = em.getReference(asociacionId.getClass(), asociacionId.getId());
                arbitro.setAsociacionId(asociacionId);
            }
            List<ArbitroPartido> attachedArbitroPartidoList = new ArrayList<ArbitroPartido>();
            for (ArbitroPartido arbitroPartidoListArbitroPartidoToAttach : arbitro.getArbitroPartidoList()) {
                arbitroPartidoListArbitroPartidoToAttach = em.getReference(arbitroPartidoListArbitroPartidoToAttach.getClass(), arbitroPartidoListArbitroPartidoToAttach.getId());
                attachedArbitroPartidoList.add(arbitroPartidoListArbitroPartidoToAttach);
            }
            arbitro.setArbitroPartidoList(attachedArbitroPartidoList);
            em.persist(arbitro);
            if (asociacionId != null) {
                asociacionId.getArbitroList().add(arbitro);
                asociacionId = em.merge(asociacionId);
            }
            for (ArbitroPartido arbitroPartidoListArbitroPartido : arbitro.getArbitroPartidoList()) {
                Arbitro oldArbitroIdOfArbitroPartidoListArbitroPartido = arbitroPartidoListArbitroPartido.getArbitroId();
                arbitroPartidoListArbitroPartido.setArbitroId(arbitro);
                arbitroPartidoListArbitroPartido = em.merge(arbitroPartidoListArbitroPartido);
                if (oldArbitroIdOfArbitroPartidoListArbitroPartido != null) {
                    oldArbitroIdOfArbitroPartidoListArbitroPartido.getArbitroPartidoList().remove(arbitroPartidoListArbitroPartido);
                    oldArbitroIdOfArbitroPartidoListArbitroPartido = em.merge(oldArbitroIdOfArbitroPartidoListArbitroPartido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Arbitro arbitro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Arbitro persistentArbitro = em.find(Arbitro.class, arbitro.getId());
            Asociacion asociacionIdOld = persistentArbitro.getAsociacionId();
            Asociacion asociacionIdNew = arbitro.getAsociacionId();
            List<ArbitroPartido> arbitroPartidoListOld = persistentArbitro.getArbitroPartidoList();
            List<ArbitroPartido> arbitroPartidoListNew = arbitro.getArbitroPartidoList();
            List<String> illegalOrphanMessages = null;
            for (ArbitroPartido arbitroPartidoListOldArbitroPartido : arbitroPartidoListOld) {
                if (!arbitroPartidoListNew.contains(arbitroPartidoListOldArbitroPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArbitroPartido " + arbitroPartidoListOldArbitroPartido + " since its arbitroId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (asociacionIdNew != null) {
                asociacionIdNew = em.getReference(asociacionIdNew.getClass(), asociacionIdNew.getId());
                arbitro.setAsociacionId(asociacionIdNew);
            }
            List<ArbitroPartido> attachedArbitroPartidoListNew = new ArrayList<ArbitroPartido>();
            for (ArbitroPartido arbitroPartidoListNewArbitroPartidoToAttach : arbitroPartidoListNew) {
                arbitroPartidoListNewArbitroPartidoToAttach = em.getReference(arbitroPartidoListNewArbitroPartidoToAttach.getClass(), arbitroPartidoListNewArbitroPartidoToAttach.getId());
                attachedArbitroPartidoListNew.add(arbitroPartidoListNewArbitroPartidoToAttach);
            }
            arbitroPartidoListNew = attachedArbitroPartidoListNew;
            arbitro.setArbitroPartidoList(arbitroPartidoListNew);
            arbitro = em.merge(arbitro);
            if (asociacionIdOld != null && !asociacionIdOld.equals(asociacionIdNew)) {
                asociacionIdOld.getArbitroList().remove(arbitro);
                asociacionIdOld = em.merge(asociacionIdOld);
            }
            if (asociacionIdNew != null && !asociacionIdNew.equals(asociacionIdOld)) {
                asociacionIdNew.getArbitroList().add(arbitro);
                asociacionIdNew = em.merge(asociacionIdNew);
            }
            for (ArbitroPartido arbitroPartidoListNewArbitroPartido : arbitroPartidoListNew) {
                if (!arbitroPartidoListOld.contains(arbitroPartidoListNewArbitroPartido)) {
                    Arbitro oldArbitroIdOfArbitroPartidoListNewArbitroPartido = arbitroPartidoListNewArbitroPartido.getArbitroId();
                    arbitroPartidoListNewArbitroPartido.setArbitroId(arbitro);
                    arbitroPartidoListNewArbitroPartido = em.merge(arbitroPartidoListNewArbitroPartido);
                    if (oldArbitroIdOfArbitroPartidoListNewArbitroPartido != null && !oldArbitroIdOfArbitroPartidoListNewArbitroPartido.equals(arbitro)) {
                        oldArbitroIdOfArbitroPartidoListNewArbitroPartido.getArbitroPartidoList().remove(arbitroPartidoListNewArbitroPartido);
                        oldArbitroIdOfArbitroPartidoListNewArbitroPartido = em.merge(oldArbitroIdOfArbitroPartidoListNewArbitroPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = arbitro.getId();
                if (findArbitro(id) == null) {
                    throw new NonexistentEntityException("The arbitro with id " + id + " no longer exists.");
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
            Arbitro arbitro;
            try {
                arbitro = em.getReference(Arbitro.class, id);
                arbitro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The arbitro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ArbitroPartido> arbitroPartidoListOrphanCheck = arbitro.getArbitroPartidoList();
            for (ArbitroPartido arbitroPartidoListOrphanCheckArbitroPartido : arbitroPartidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Arbitro (" + arbitro + ") cannot be destroyed since the ArbitroPartido " + arbitroPartidoListOrphanCheckArbitroPartido + " in its arbitroPartidoList field has a non-nullable arbitroId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Asociacion asociacionId = arbitro.getAsociacionId();
            if (asociacionId != null) {
                asociacionId.getArbitroList().remove(arbitro);
                asociacionId = em.merge(asociacionId);
            }
            em.remove(arbitro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Arbitro> findArbitroEntities() {
        return findArbitroEntities(true, -1, -1);
    }

    public List<Arbitro> findArbitroEntities(int maxResults, int firstResult) {
        return findArbitroEntities(false, maxResults, firstResult);
    }

    private List<Arbitro> findArbitroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Arbitro.class));
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

    public Arbitro findArbitro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Arbitro.class, id);
        } finally {
            em.close();
        }
    }

    public int getArbitroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Arbitro> rt = cq.from(Arbitro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
