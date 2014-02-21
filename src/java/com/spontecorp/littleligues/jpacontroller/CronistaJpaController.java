/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.Cronica;
import com.spontecorp.littleligues.model.Cronista;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Casper
 */
public class CronistaJpaController implements Serializable {

    public CronistaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cronista cronista) {
        if (cronista.getCronicaList() == null) {
            cronista.setCronicaList(new ArrayList<Cronica>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cronica> attachedCronicaList = new ArrayList<Cronica>();
            for (Cronica cronicaListCronicaToAttach : cronista.getCronicaList()) {
                cronicaListCronicaToAttach = em.getReference(cronicaListCronicaToAttach.getClass(), cronicaListCronicaToAttach.getId());
                attachedCronicaList.add(cronicaListCronicaToAttach);
            }
            cronista.setCronicaList(attachedCronicaList);
            em.persist(cronista);
            for (Cronica cronicaListCronica : cronista.getCronicaList()) {
                Cronista oldCronistaIdOfCronicaListCronica = cronicaListCronica.getCronistaId();
                cronicaListCronica.setCronistaId(cronista);
                cronicaListCronica = em.merge(cronicaListCronica);
                if (oldCronistaIdOfCronicaListCronica != null) {
                    oldCronistaIdOfCronicaListCronica.getCronicaList().remove(cronicaListCronica);
                    oldCronistaIdOfCronicaListCronica = em.merge(oldCronistaIdOfCronicaListCronica);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cronista cronista) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cronista persistentCronista = em.find(Cronista.class, cronista.getId());
            List<Cronica> cronicaListOld = persistentCronista.getCronicaList();
            List<Cronica> cronicaListNew = cronista.getCronicaList();
            List<String> illegalOrphanMessages = null;
            for (Cronica cronicaListOldCronica : cronicaListOld) {
                if (!cronicaListNew.contains(cronicaListOldCronica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cronica " + cronicaListOldCronica + " since its cronistaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cronica> attachedCronicaListNew = new ArrayList<Cronica>();
            for (Cronica cronicaListNewCronicaToAttach : cronicaListNew) {
                cronicaListNewCronicaToAttach = em.getReference(cronicaListNewCronicaToAttach.getClass(), cronicaListNewCronicaToAttach.getId());
                attachedCronicaListNew.add(cronicaListNewCronicaToAttach);
            }
            cronicaListNew = attachedCronicaListNew;
            cronista.setCronicaList(cronicaListNew);
            cronista = em.merge(cronista);
            for (Cronica cronicaListNewCronica : cronicaListNew) {
                if (!cronicaListOld.contains(cronicaListNewCronica)) {
                    Cronista oldCronistaIdOfCronicaListNewCronica = cronicaListNewCronica.getCronistaId();
                    cronicaListNewCronica.setCronistaId(cronista);
                    cronicaListNewCronica = em.merge(cronicaListNewCronica);
                    if (oldCronistaIdOfCronicaListNewCronica != null && !oldCronistaIdOfCronicaListNewCronica.equals(cronista)) {
                        oldCronistaIdOfCronicaListNewCronica.getCronicaList().remove(cronicaListNewCronica);
                        oldCronistaIdOfCronicaListNewCronica = em.merge(oldCronistaIdOfCronicaListNewCronica);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cronista.getId();
                if (findCronista(id) == null) {
                    throw new NonexistentEntityException("The cronista with id " + id + " no longer exists.");
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
            Cronista cronista;
            try {
                cronista = em.getReference(Cronista.class, id);
                cronista.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cronista with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cronica> cronicaListOrphanCheck = cronista.getCronicaList();
            for (Cronica cronicaListOrphanCheckCronica : cronicaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cronista (" + cronista + ") cannot be destroyed since the Cronica " + cronicaListOrphanCheckCronica + " in its cronicaList field has a non-nullable cronistaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cronista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cronista> findCronistaEntities() {
        return findCronistaEntities(true, -1, -1);
    }

    public List<Cronista> findCronistaEntities(int maxResults, int firstResult) {
        return findCronistaEntities(false, maxResults, firstResult);
    }

    private List<Cronista> findCronistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cronista.class));
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

    public Cronista findCronista(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cronista.class, id);
        } finally {
            em.close();
        }
    }

    public int getCronistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cronista> rt = cq.from(Cronista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
