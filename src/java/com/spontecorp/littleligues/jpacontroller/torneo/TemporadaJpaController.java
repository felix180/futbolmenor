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
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Temporada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class TemporadaJpaController implements Serializable {

    public TemporadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Temporada temporada) {
        if (temporada.getFaseList() == null) {
            temporada.setFaseList(new ArrayList<Fase>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Liga ligaId = temporada.getLigaId();
            if (ligaId != null) {
                ligaId = em.getReference(ligaId.getClass(), ligaId.getId());
                temporada.setLigaId(ligaId);
            }
            List<Fase> attachedFaseList = new ArrayList<Fase>();
            for (Fase faseListFaseToAttach : temporada.getFaseList()) {
                faseListFaseToAttach = em.getReference(faseListFaseToAttach.getClass(), faseListFaseToAttach.getId());
                attachedFaseList.add(faseListFaseToAttach);
            }
            temporada.setFaseList(attachedFaseList);
            em.persist(temporada);
            if (ligaId != null) {
                ligaId.getTemporadaList().add(temporada);
                ligaId = em.merge(ligaId);
            }
            for (Fase faseListFase : temporada.getFaseList()) {
                Temporada oldTemporadaIdOfFaseListFase = faseListFase.getTemporadaId();
                faseListFase.setTemporadaId(temporada);
                faseListFase = em.merge(faseListFase);
                if (oldTemporadaIdOfFaseListFase != null) {
                    oldTemporadaIdOfFaseListFase.getFaseList().remove(faseListFase);
                    oldTemporadaIdOfFaseListFase = em.merge(oldTemporadaIdOfFaseListFase);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Temporada temporada) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Temporada persistentTemporada = em.find(Temporada.class, temporada.getId());
            Liga ligaIdOld = persistentTemporada.getLigaId();
            Liga ligaIdNew = temporada.getLigaId();
            List<Fase> faseListOld = persistentTemporada.getFaseList();
            List<Fase> faseListNew = temporada.getFaseList();
            List<String> illegalOrphanMessages = null;
            for (Fase faseListOldFase : faseListOld) {
                if (!faseListNew.contains(faseListOldFase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Fase " + faseListOldFase + " since its temporadaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ligaIdNew != null) {
                ligaIdNew = em.getReference(ligaIdNew.getClass(), ligaIdNew.getId());
                temporada.setLigaId(ligaIdNew);
            }
            List<Fase> attachedFaseListNew = new ArrayList<Fase>();
            for (Fase faseListNewFaseToAttach : faseListNew) {
                faseListNewFaseToAttach = em.getReference(faseListNewFaseToAttach.getClass(), faseListNewFaseToAttach.getId());
                attachedFaseListNew.add(faseListNewFaseToAttach);
            }
            faseListNew = attachedFaseListNew;
            temporada.setFaseList(faseListNew);
            temporada = em.merge(temporada);
            if (ligaIdOld != null && !ligaIdOld.equals(ligaIdNew)) {
                ligaIdOld.getTemporadaList().remove(temporada);
                ligaIdOld = em.merge(ligaIdOld);
            }
            if (ligaIdNew != null && !ligaIdNew.equals(ligaIdOld)) {
                ligaIdNew.getTemporadaList().add(temporada);
                ligaIdNew = em.merge(ligaIdNew);
            }
            for (Fase faseListNewFase : faseListNew) {
                if (!faseListOld.contains(faseListNewFase)) {
                    Temporada oldTemporadaIdOfFaseListNewFase = faseListNewFase.getTemporadaId();
                    faseListNewFase.setTemporadaId(temporada);
                    faseListNewFase = em.merge(faseListNewFase);
                    if (oldTemporadaIdOfFaseListNewFase != null && !oldTemporadaIdOfFaseListNewFase.equals(temporada)) {
                        oldTemporadaIdOfFaseListNewFase.getFaseList().remove(faseListNewFase);
                        oldTemporadaIdOfFaseListNewFase = em.merge(oldTemporadaIdOfFaseListNewFase);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = temporada.getId();
                if (findTemporada(id) == null) {
                    throw new NonexistentEntityException("The temporada with id " + id + " no longer exists.");
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
            Temporada temporada;
            try {
                temporada = em.getReference(Temporada.class, id);
                temporada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The temporada with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Fase> faseListOrphanCheck = temporada.getFaseList();
            for (Fase faseListOrphanCheckFase : faseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Temporada (" + temporada + ") cannot be destroyed since the Fase " + faseListOrphanCheckFase + " in its faseList field has a non-nullable temporadaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Liga ligaId = temporada.getLigaId();
            if (ligaId != null) {
                ligaId.getTemporadaList().remove(temporada);
                ligaId = em.merge(ligaId);
            }
            em.remove(temporada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Temporada> findTemporadaEntities() {
        return findTemporadaEntities(true, -1, -1);
    }

    public List<Temporada> findTemporadaEntities(int maxResults, int firstResult) {
        return findTemporadaEntities(false, maxResults, firstResult);
    }

    private List<Temporada> findTemporadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Temporada.class));
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

    public Temporada findTemporada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Temporada.class, id);
        } finally {
            em.close();
        }
    }

    public int getTemporadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Temporada> rt = cq.from(Temporada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
