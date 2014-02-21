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
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Partido;
import java.util.ArrayList;
import java.util.List;
import com.spontecorp.littleligues.model.torneo.Clasificacion;
import com.spontecorp.littleligues.model.torneo.Jornada;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class JornadaJpaController implements Serializable {

    public JornadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jornada jornada) {
        if (jornada.getPartidoList() == null) {
            jornada.setPartidoList(new ArrayList<Partido>());
        }
        if (jornada.getClasificacionList() == null) {
            jornada.setClasificacionList(new ArrayList<Clasificacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo grupoId = jornada.getGrupoId();
            if (grupoId != null) {
                grupoId = em.getReference(grupoId.getClass(), grupoId.getId());
                jornada.setGrupoId(grupoId);
            }
            List<Partido> attachedPartidoList = new ArrayList<Partido>();
            for (Partido partidoListPartidoToAttach : jornada.getPartidoList()) {
                partidoListPartidoToAttach = em.getReference(partidoListPartidoToAttach.getClass(), partidoListPartidoToAttach.getId());
                attachedPartidoList.add(partidoListPartidoToAttach);
            }
            jornada.setPartidoList(attachedPartidoList);
            List<Clasificacion> attachedClasificacionList = new ArrayList<Clasificacion>();
            for (Clasificacion clasificacionListClasificacionToAttach : jornada.getClasificacionList()) {
                clasificacionListClasificacionToAttach = em.getReference(clasificacionListClasificacionToAttach.getClass(), clasificacionListClasificacionToAttach.getId());
                attachedClasificacionList.add(clasificacionListClasificacionToAttach);
            }
            jornada.setClasificacionList(attachedClasificacionList);
            em.persist(jornada);
            if (grupoId != null) {
                grupoId.getJornadaList().add(jornada);
                grupoId = em.merge(grupoId);
            }
            for (Partido partidoListPartido : jornada.getPartidoList()) {
                Jornada oldJornadaIdOfPartidoListPartido = partidoListPartido.getJornadaId();
                partidoListPartido.setJornadaId(jornada);
                partidoListPartido = em.merge(partidoListPartido);
                if (oldJornadaIdOfPartidoListPartido != null) {
                    oldJornadaIdOfPartidoListPartido.getPartidoList().remove(partidoListPartido);
                    oldJornadaIdOfPartidoListPartido = em.merge(oldJornadaIdOfPartidoListPartido);
                }
            }
            for (Clasificacion clasificacionListClasificacion : jornada.getClasificacionList()) {
                Jornada oldJornadaIdOfClasificacionListClasificacion = clasificacionListClasificacion.getJornadaId();
                clasificacionListClasificacion.setJornadaId(jornada);
                clasificacionListClasificacion = em.merge(clasificacionListClasificacion);
                if (oldJornadaIdOfClasificacionListClasificacion != null) {
                    oldJornadaIdOfClasificacionListClasificacion.getClasificacionList().remove(clasificacionListClasificacion);
                    oldJornadaIdOfClasificacionListClasificacion = em.merge(oldJornadaIdOfClasificacionListClasificacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jornada jornada) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jornada persistentJornada = em.find(Jornada.class, jornada.getId());
            Grupo grupoIdOld = persistentJornada.getGrupoId();
            Grupo grupoIdNew = jornada.getGrupoId();
            List<Partido> partidoListOld = persistentJornada.getPartidoList();
            List<Partido> partidoListNew = jornada.getPartidoList();
            List<Clasificacion> clasificacionListOld = persistentJornada.getClasificacionList();
            List<Clasificacion> clasificacionListNew = jornada.getClasificacionList();
            List<String> illegalOrphanMessages = null;
            for (Clasificacion clasificacionListOldClasificacion : clasificacionListOld) {
                if (!clasificacionListNew.contains(clasificacionListOldClasificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clasificacion " + clasificacionListOldClasificacion + " since its jornadaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (grupoIdNew != null) {
                grupoIdNew = em.getReference(grupoIdNew.getClass(), grupoIdNew.getId());
                jornada.setGrupoId(grupoIdNew);
            }
            List<Partido> attachedPartidoListNew = new ArrayList<Partido>();
            for (Partido partidoListNewPartidoToAttach : partidoListNew) {
                partidoListNewPartidoToAttach = em.getReference(partidoListNewPartidoToAttach.getClass(), partidoListNewPartidoToAttach.getId());
                attachedPartidoListNew.add(partidoListNewPartidoToAttach);
            }
            partidoListNew = attachedPartidoListNew;
            jornada.setPartidoList(partidoListNew);
            List<Clasificacion> attachedClasificacionListNew = new ArrayList<Clasificacion>();
            for (Clasificacion clasificacionListNewClasificacionToAttach : clasificacionListNew) {
                clasificacionListNewClasificacionToAttach = em.getReference(clasificacionListNewClasificacionToAttach.getClass(), clasificacionListNewClasificacionToAttach.getId());
                attachedClasificacionListNew.add(clasificacionListNewClasificacionToAttach);
            }
            clasificacionListNew = attachedClasificacionListNew;
            jornada.setClasificacionList(clasificacionListNew);
            jornada = em.merge(jornada);
            if (grupoIdOld != null && !grupoIdOld.equals(grupoIdNew)) {
                grupoIdOld.getJornadaList().remove(jornada);
                grupoIdOld = em.merge(grupoIdOld);
            }
            if (grupoIdNew != null && !grupoIdNew.equals(grupoIdOld)) {
                grupoIdNew.getJornadaList().add(jornada);
                grupoIdNew = em.merge(grupoIdNew);
            }
            for (Partido partidoListOldPartido : partidoListOld) {
                if (!partidoListNew.contains(partidoListOldPartido)) {
                    partidoListOldPartido.setJornadaId(null);
                    partidoListOldPartido = em.merge(partidoListOldPartido);
                }
            }
            for (Partido partidoListNewPartido : partidoListNew) {
                if (!partidoListOld.contains(partidoListNewPartido)) {
                    Jornada oldJornadaIdOfPartidoListNewPartido = partidoListNewPartido.getJornadaId();
                    partidoListNewPartido.setJornadaId(jornada);
                    partidoListNewPartido = em.merge(partidoListNewPartido);
                    if (oldJornadaIdOfPartidoListNewPartido != null && !oldJornadaIdOfPartidoListNewPartido.equals(jornada)) {
                        oldJornadaIdOfPartidoListNewPartido.getPartidoList().remove(partidoListNewPartido);
                        oldJornadaIdOfPartidoListNewPartido = em.merge(oldJornadaIdOfPartidoListNewPartido);
                    }
                }
            }
            for (Clasificacion clasificacionListNewClasificacion : clasificacionListNew) {
                if (!clasificacionListOld.contains(clasificacionListNewClasificacion)) {
                    Jornada oldJornadaIdOfClasificacionListNewClasificacion = clasificacionListNewClasificacion.getJornadaId();
                    clasificacionListNewClasificacion.setJornadaId(jornada);
                    clasificacionListNewClasificacion = em.merge(clasificacionListNewClasificacion);
                    if (oldJornadaIdOfClasificacionListNewClasificacion != null && !oldJornadaIdOfClasificacionListNewClasificacion.equals(jornada)) {
                        oldJornadaIdOfClasificacionListNewClasificacion.getClasificacionList().remove(clasificacionListNewClasificacion);
                        oldJornadaIdOfClasificacionListNewClasificacion = em.merge(oldJornadaIdOfClasificacionListNewClasificacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jornada.getId();
                if (findJornada(id) == null) {
                    throw new NonexistentEntityException("The jornada with id " + id + " no longer exists.");
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
            Jornada jornada;
            try {
                jornada = em.getReference(Jornada.class, id);
                jornada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jornada with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Clasificacion> clasificacionListOrphanCheck = jornada.getClasificacionList();
            for (Clasificacion clasificacionListOrphanCheckClasificacion : clasificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jornada (" + jornada + ") cannot be destroyed since the Clasificacion " + clasificacionListOrphanCheckClasificacion + " in its clasificacionList field has a non-nullable jornadaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grupo grupoId = jornada.getGrupoId();
            if (grupoId != null) {
                grupoId.getJornadaList().remove(jornada);
                grupoId = em.merge(grupoId);
            }
            List<Partido> partidoList = jornada.getPartidoList();
            for (Partido partidoListPartido : partidoList) {
                partidoListPartido.setJornadaId(null);
                partidoListPartido = em.merge(partidoListPartido);
            }
            em.remove(jornada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jornada> findJornadaEntities() {
        return findJornadaEntities(true, -1, -1);
    }

    public List<Jornada> findJornadaEntities(int maxResults, int firstResult) {
        return findJornadaEntities(false, maxResults, firstResult);
    }

    private List<Jornada> findJornadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jornada.class));
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

    public Jornada findJornada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jornada.class, id);
        } finally {
            em.close();
        }
    }

    public int getJornadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jornada> rt = cq.from(Jornada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
