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
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class GrupoJpaController implements Serializable {

    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) {
        if (grupo.getJornadaList() == null) {
            grupo.setJornadaList(new ArrayList<Jornada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fase faseId = grupo.getFaseId();
            if (faseId != null) {
                faseId = em.getReference(faseId.getClass(), faseId.getId());
                grupo.setFaseId(faseId);
            }
            List<Jornada> attachedJornadaList = new ArrayList<Jornada>();
            for (Jornada jornadaListJornadaToAttach : grupo.getJornadaList()) {
                jornadaListJornadaToAttach = em.getReference(jornadaListJornadaToAttach.getClass(), jornadaListJornadaToAttach.getId());
                attachedJornadaList.add(jornadaListJornadaToAttach);
            }
            grupo.setJornadaList(attachedJornadaList);
            em.persist(grupo);
            if (faseId != null) {
                faseId.getGrupoList().add(grupo);
                faseId = em.merge(faseId);
            }
            for (Jornada jornadaListJornada : grupo.getJornadaList()) {
                Grupo oldGrupoIdOfJornadaListJornada = jornadaListJornada.getGrupoId();
                jornadaListJornada.setGrupoId(grupo);
                jornadaListJornada = em.merge(jornadaListJornada);
                if (oldGrupoIdOfJornadaListJornada != null) {
                    oldGrupoIdOfJornadaListJornada.getJornadaList().remove(jornadaListJornada);
                    oldGrupoIdOfJornadaListJornada = em.merge(oldGrupoIdOfJornadaListJornada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getId());
            Fase faseIdOld = persistentGrupo.getFaseId();
            Fase faseIdNew = grupo.getFaseId();
            List<Jornada> jornadaListOld = persistentGrupo.getJornadaList();
            List<Jornada> jornadaListNew = grupo.getJornadaList();
            if (faseIdNew != null) {
                faseIdNew = em.getReference(faseIdNew.getClass(), faseIdNew.getId());
                grupo.setFaseId(faseIdNew);
            }
            List<Jornada> attachedJornadaListNew = new ArrayList<Jornada>();
            for (Jornada jornadaListNewJornadaToAttach : jornadaListNew) {
                jornadaListNewJornadaToAttach = em.getReference(jornadaListNewJornadaToAttach.getClass(), jornadaListNewJornadaToAttach.getId());
                attachedJornadaListNew.add(jornadaListNewJornadaToAttach);
            }
            jornadaListNew = attachedJornadaListNew;
            grupo.setJornadaList(jornadaListNew);
            grupo = em.merge(grupo);
            if (faseIdOld != null && !faseIdOld.equals(faseIdNew)) {
                faseIdOld.getGrupoList().remove(grupo);
                faseIdOld = em.merge(faseIdOld);
            }
            if (faseIdNew != null && !faseIdNew.equals(faseIdOld)) {
                faseIdNew.getGrupoList().add(grupo);
                faseIdNew = em.merge(faseIdNew);
            }
            for (Jornada jornadaListOldJornada : jornadaListOld) {
                if (!jornadaListNew.contains(jornadaListOldJornada)) {
                    jornadaListOldJornada.setGrupoId(null);
                    jornadaListOldJornada = em.merge(jornadaListOldJornada);
                }
            }
            for (Jornada jornadaListNewJornada : jornadaListNew) {
                if (!jornadaListOld.contains(jornadaListNewJornada)) {
                    Grupo oldGrupoIdOfJornadaListNewJornada = jornadaListNewJornada.getGrupoId();
                    jornadaListNewJornada.setGrupoId(grupo);
                    jornadaListNewJornada = em.merge(jornadaListNewJornada);
                    if (oldGrupoIdOfJornadaListNewJornada != null && !oldGrupoIdOfJornadaListNewJornada.equals(grupo)) {
                        oldGrupoIdOfJornadaListNewJornada.getJornadaList().remove(jornadaListNewJornada);
                        oldGrupoIdOfJornadaListNewJornada = em.merge(oldGrupoIdOfJornadaListNewJornada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grupo.getId();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            Fase faseId = grupo.getFaseId();
            if (faseId != null) {
                faseId.getGrupoList().remove(grupo);
                faseId = em.merge(faseId);
            }
            List<Jornada> jornadaList = grupo.getJornadaList();
            for (Jornada jornadaListJornada : jornadaList) {
                jornadaListJornada.setGrupoId(null);
                jornadaListJornada = em.merge(jornadaListJornada);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
