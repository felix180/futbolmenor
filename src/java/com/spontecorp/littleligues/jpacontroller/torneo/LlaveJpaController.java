/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Partido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class LlaveJpaController implements Serializable {

    public LlaveJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Llave llave) {
        if (llave.getPartidoList() == null) {
            llave.setPartidoList(new ArrayList<Partido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fase faseId = llave.getFaseId();
            if (faseId != null) {
                faseId = em.getReference(faseId.getClass(), faseId.getId());
                llave.setFaseId(faseId);
            }
            List<Partido> attachedPartidoList = new ArrayList<Partido>();
            for (Partido partidoListPartidoToAttach : llave.getPartidoList()) {
                partidoListPartidoToAttach = em.getReference(partidoListPartidoToAttach.getClass(), partidoListPartidoToAttach.getId());
                attachedPartidoList.add(partidoListPartidoToAttach);
            }
            llave.setPartidoList(attachedPartidoList);
            em.persist(llave);
            if (faseId != null) {
                faseId.getLlaveList().add(llave);
                faseId = em.merge(faseId);
            }
            for (Partido partidoListPartido : llave.getPartidoList()) {
                Llave oldLlaveIdOfPartidoListPartido = partidoListPartido.getLlaveId();
                partidoListPartido.setLlaveId(llave);
                partidoListPartido = em.merge(partidoListPartido);
                if (oldLlaveIdOfPartidoListPartido != null) {
                    oldLlaveIdOfPartidoListPartido.getPartidoList().remove(partidoListPartido);
                    oldLlaveIdOfPartidoListPartido = em.merge(oldLlaveIdOfPartidoListPartido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Llave llave) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llave persistentLlave = em.find(Llave.class, llave.getId());
            Fase faseIdOld = persistentLlave.getFaseId();
            Fase faseIdNew = llave.getFaseId();
            List<Partido> partidoListOld = persistentLlave.getPartidoList();
            List<Partido> partidoListNew = llave.getPartidoList();
            if (faseIdNew != null) {
                faseIdNew = em.getReference(faseIdNew.getClass(), faseIdNew.getId());
                llave.setFaseId(faseIdNew);
            }
            List<Partido> attachedPartidoListNew = new ArrayList<Partido>();
            for (Partido partidoListNewPartidoToAttach : partidoListNew) {
                partidoListNewPartidoToAttach = em.getReference(partidoListNewPartidoToAttach.getClass(), partidoListNewPartidoToAttach.getId());
                attachedPartidoListNew.add(partidoListNewPartidoToAttach);
            }
            partidoListNew = attachedPartidoListNew;
            llave.setPartidoList(partidoListNew);
            llave = em.merge(llave);
            if (faseIdOld != null && !faseIdOld.equals(faseIdNew)) {
                faseIdOld.getLlaveList().remove(llave);
                faseIdOld = em.merge(faseIdOld);
            }
            if (faseIdNew != null && !faseIdNew.equals(faseIdOld)) {
                faseIdNew.getLlaveList().add(llave);
                faseIdNew = em.merge(faseIdNew);
            }
            for (Partido partidoListOldPartido : partidoListOld) {
                if (!partidoListNew.contains(partidoListOldPartido)) {
                    partidoListOldPartido.setLlaveId(null);
                    partidoListOldPartido = em.merge(partidoListOldPartido);
                }
            }
            for (Partido partidoListNewPartido : partidoListNew) {
                if (!partidoListOld.contains(partidoListNewPartido)) {
                    Llave oldLlaveIdOfPartidoListNewPartido = partidoListNewPartido.getLlaveId();
                    partidoListNewPartido.setLlaveId(llave);
                    partidoListNewPartido = em.merge(partidoListNewPartido);
                    if (oldLlaveIdOfPartidoListNewPartido != null && !oldLlaveIdOfPartidoListNewPartido.equals(llave)) {
                        oldLlaveIdOfPartidoListNewPartido.getPartidoList().remove(partidoListNewPartido);
                        oldLlaveIdOfPartidoListNewPartido = em.merge(oldLlaveIdOfPartidoListNewPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = llave.getId();
                if (findLlave(id) == null) {
                    throw new NonexistentEntityException("The llave with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llave llave;
            try {
                llave = em.getReference(Llave.class, id);
                llave.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The llave with id " + id + " no longer exists.", enfe);
            }
            Fase faseId = llave.getFaseId();
            if (faseId != null) {
                faseId.getLlaveList().remove(llave);
                faseId = em.merge(faseId);
            }
            List<Partido> partidoList = llave.getPartidoList();
            for (Partido partidoListPartido : partidoList) {
                partidoListPartido.setLlaveId(null);
                partidoListPartido = em.merge(partidoListPartido);
            }
            em.remove(llave);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Llave> findLlaveEntities() {
        return findLlaveEntities(true, -1, -1);
    }

    public List<Llave> findLlaveEntities(int maxResults, int firstResult) {
        return findLlaveEntities(false, maxResults, firstResult);
    }

    private List<Llave> findLlaveEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Llave.class));
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

    public Llave findLlave(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Llave.class, id);
        } finally {
            em.close();
        }
    }

    public int getLlaveCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Llave> rt = cq.from(Llave.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
