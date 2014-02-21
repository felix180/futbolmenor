/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.torneo.Fase;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.model.torneo.Grupo;
import java.util.ArrayList;
import java.util.List;
import com.spontecorp.littleligues.model.torneo.Llave;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class FaseJpaController implements Serializable {

    public FaseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fase fase) {
        if (fase.getGrupoList() == null) {
            fase.setGrupoList(new ArrayList<Grupo>());
        }
        if (fase.getLlaveList() == null) {
            fase.setLlaveList(new ArrayList<Llave>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Temporada temporadaId = fase.getTemporadaId();
            if (temporadaId != null) {
                temporadaId = em.getReference(temporadaId.getClass(), temporadaId.getId());
                fase.setTemporadaId(temporadaId);
            }
            List<Grupo> attachedGrupoList = new ArrayList<Grupo>();
            for (Grupo grupoListGrupoToAttach : fase.getGrupoList()) {
                grupoListGrupoToAttach = em.getReference(grupoListGrupoToAttach.getClass(), grupoListGrupoToAttach.getId());
                attachedGrupoList.add(grupoListGrupoToAttach);
            }
            fase.setGrupoList(attachedGrupoList);
            List<Llave> attachedLlaveList = new ArrayList<Llave>();
            for (Llave llaveListLlaveToAttach : fase.getLlaveList()) {
                llaveListLlaveToAttach = em.getReference(llaveListLlaveToAttach.getClass(), llaveListLlaveToAttach.getId());
                attachedLlaveList.add(llaveListLlaveToAttach);
            }
            fase.setLlaveList(attachedLlaveList);
            em.persist(fase);
            if (temporadaId != null) {
                temporadaId.getFaseList().add(fase);
                temporadaId = em.merge(temporadaId);
            }
            for (Grupo grupoListGrupo : fase.getGrupoList()) {
                Fase oldFaseIdOfGrupoListGrupo = grupoListGrupo.getFaseId();
                grupoListGrupo.setFaseId(fase);
                grupoListGrupo = em.merge(grupoListGrupo);
                if (oldFaseIdOfGrupoListGrupo != null) {
                    oldFaseIdOfGrupoListGrupo.getGrupoList().remove(grupoListGrupo);
                    oldFaseIdOfGrupoListGrupo = em.merge(oldFaseIdOfGrupoListGrupo);
                }
            }
            for (Llave llaveListLlave : fase.getLlaveList()) {
                Fase oldFaseIdOfLlaveListLlave = llaveListLlave.getFaseId();
                llaveListLlave.setFaseId(fase);
                llaveListLlave = em.merge(llaveListLlave);
                if (oldFaseIdOfLlaveListLlave != null) {
                    oldFaseIdOfLlaveListLlave.getLlaveList().remove(llaveListLlave);
                    oldFaseIdOfLlaveListLlave = em.merge(oldFaseIdOfLlaveListLlave);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fase fase) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fase persistentFase = em.find(Fase.class, fase.getId());
            Temporada temporadaIdOld = persistentFase.getTemporadaId();
            Temporada temporadaIdNew = fase.getTemporadaId();
            List<Grupo> grupoListOld = persistentFase.getGrupoList();
            List<Grupo> grupoListNew = fase.getGrupoList();
            List<Llave> llaveListOld = persistentFase.getLlaveList();
            List<Llave> llaveListNew = fase.getLlaveList();
            if (temporadaIdNew != null) {
                temporadaIdNew = em.getReference(temporadaIdNew.getClass(), temporadaIdNew.getId());
                fase.setTemporadaId(temporadaIdNew);
            }
            List<Grupo> attachedGrupoListNew = new ArrayList<Grupo>();
            for (Grupo grupoListNewGrupoToAttach : grupoListNew) {
                grupoListNewGrupoToAttach = em.getReference(grupoListNewGrupoToAttach.getClass(), grupoListNewGrupoToAttach.getId());
                attachedGrupoListNew.add(grupoListNewGrupoToAttach);
            }
            grupoListNew = attachedGrupoListNew;
            fase.setGrupoList(grupoListNew);
            List<Llave> attachedLlaveListNew = new ArrayList<Llave>();
            for (Llave llaveListNewLlaveToAttach : llaveListNew) {
                llaveListNewLlaveToAttach = em.getReference(llaveListNewLlaveToAttach.getClass(), llaveListNewLlaveToAttach.getId());
                attachedLlaveListNew.add(llaveListNewLlaveToAttach);
            }
            llaveListNew = attachedLlaveListNew;
            fase.setLlaveList(llaveListNew);
            fase = em.merge(fase);
            if (temporadaIdOld != null && !temporadaIdOld.equals(temporadaIdNew)) {
                temporadaIdOld.getFaseList().remove(fase);
                temporadaIdOld = em.merge(temporadaIdOld);
            }
            if (temporadaIdNew != null && !temporadaIdNew.equals(temporadaIdOld)) {
                temporadaIdNew.getFaseList().add(fase);
                temporadaIdNew = em.merge(temporadaIdNew);
            }
            for (Grupo grupoListOldGrupo : grupoListOld) {
                if (!grupoListNew.contains(grupoListOldGrupo)) {
                    grupoListOldGrupo.setFaseId(null);
                    grupoListOldGrupo = em.merge(grupoListOldGrupo);
                }
            }
            for (Grupo grupoListNewGrupo : grupoListNew) {
                if (!grupoListOld.contains(grupoListNewGrupo)) {
                    Fase oldFaseIdOfGrupoListNewGrupo = grupoListNewGrupo.getFaseId();
                    grupoListNewGrupo.setFaseId(fase);
                    grupoListNewGrupo = em.merge(grupoListNewGrupo);
                    if (oldFaseIdOfGrupoListNewGrupo != null && !oldFaseIdOfGrupoListNewGrupo.equals(fase)) {
                        oldFaseIdOfGrupoListNewGrupo.getGrupoList().remove(grupoListNewGrupo);
                        oldFaseIdOfGrupoListNewGrupo = em.merge(oldFaseIdOfGrupoListNewGrupo);
                    }
                }
            }
            for (Llave llaveListOldLlave : llaveListOld) {
                if (!llaveListNew.contains(llaveListOldLlave)) {
                    llaveListOldLlave.setFaseId(null);
                    llaveListOldLlave = em.merge(llaveListOldLlave);
                }
            }
            for (Llave llaveListNewLlave : llaveListNew) {
                if (!llaveListOld.contains(llaveListNewLlave)) {
                    Fase oldFaseIdOfLlaveListNewLlave = llaveListNewLlave.getFaseId();
                    llaveListNewLlave.setFaseId(fase);
                    llaveListNewLlave = em.merge(llaveListNewLlave);
                    if (oldFaseIdOfLlaveListNewLlave != null && !oldFaseIdOfLlaveListNewLlave.equals(fase)) {
                        oldFaseIdOfLlaveListNewLlave.getLlaveList().remove(llaveListNewLlave);
                        oldFaseIdOfLlaveListNewLlave = em.merge(oldFaseIdOfLlaveListNewLlave);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = fase.getId();
                if (findFase(id) == null) {
                    throw new NonexistentEntityException("The fase with id " + id + " no longer exists.");
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
            Fase fase;
            try {
                fase = em.getReference(Fase.class, id);
                fase.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fase with id " + id + " no longer exists.", enfe);
            }
            Temporada temporadaId = fase.getTemporadaId();
            if (temporadaId != null) {
                temporadaId.getFaseList().remove(fase);
                temporadaId = em.merge(temporadaId);
            }
            List<Grupo> grupoList = fase.getGrupoList();
            for (Grupo grupoListGrupo : grupoList) {
                grupoListGrupo.setFaseId(null);
                grupoListGrupo = em.merge(grupoListGrupo);
            }
            List<Llave> llaveList = fase.getLlaveList();
            for (Llave llaveListLlave : llaveList) {
                llaveListLlave.setFaseId(null);
                llaveListLlave = em.merge(llaveListLlave);
            }
            em.remove(fase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fase> findFaseEntities() {
        return findFaseEntities(true, -1, -1);
    }

    public List<Fase> findFaseEntities(int maxResults, int firstResult) {
        return findFaseEntities(false, maxResults, firstResult);
    }

    private List<Fase> findFaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fase.class));
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

    public Fase findFase(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fase.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fase> rt = cq.from(Fase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
