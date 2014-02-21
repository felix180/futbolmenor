/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Club;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.torneo.Partido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Casper
 */
public class CanchaJpaController implements Serializable {

    public CanchaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cancha cancha) {
        if (cancha.getClubList() == null) {
            cancha.setClubList(new ArrayList<Club>());
        }
        if (cancha.getPartidoList() == null) {
            cancha.setPartidoList(new ArrayList<Partido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccionId = cancha.getDireccionId();
            if (direccionId != null) {
                direccionId = em.getReference(direccionId.getClass(), direccionId.getId());
                cancha.setDireccionId(direccionId);
            }
            List<Club> attachedClubList = new ArrayList<Club>();
            for (Club clubListClubToAttach : cancha.getClubList()) {
                clubListClubToAttach = em.getReference(clubListClubToAttach.getClass(), clubListClubToAttach.getId());
                attachedClubList.add(clubListClubToAttach);
            }
            cancha.setClubList(attachedClubList);
            List<Partido> attachedPartidoList = new ArrayList<Partido>();
            for (Partido partidoListPartidoToAttach : cancha.getPartidoList()) {
                partidoListPartidoToAttach = em.getReference(partidoListPartidoToAttach.getClass(), partidoListPartidoToAttach.getId());
                attachedPartidoList.add(partidoListPartidoToAttach);
            }
            cancha.setPartidoList(attachedPartidoList);
            em.persist(cancha);
            if (direccionId != null) {
                direccionId.getCanchaList().add(cancha);
                direccionId = em.merge(direccionId);
            }
            for (Club clubListClub : cancha.getClubList()) {
                clubListClub.getCanchaList().add(cancha);
                clubListClub = em.merge(clubListClub);
            }
            for (Partido partidoListPartido : cancha.getPartidoList()) {
                Cancha oldCanchaIdOfPartidoListPartido = partidoListPartido.getCanchaId();
                partidoListPartido.setCanchaId(cancha);
                partidoListPartido = em.merge(partidoListPartido);
                if (oldCanchaIdOfPartidoListPartido != null) {
                    oldCanchaIdOfPartidoListPartido.getPartidoList().remove(partidoListPartido);
                    oldCanchaIdOfPartidoListPartido = em.merge(oldCanchaIdOfPartidoListPartido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cancha cancha) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cancha persistentCancha = em.find(Cancha.class, cancha.getId());
            Direccion direccionIdOld = persistentCancha.getDireccionId();
            Direccion direccionIdNew = cancha.getDireccionId();
            List<Club> clubListOld = persistentCancha.getClubList();
            List<Club> clubListNew = cancha.getClubList();
            List<Partido> partidoListOld = persistentCancha.getPartidoList();
            List<Partido> partidoListNew = cancha.getPartidoList();
            if (direccionIdNew != null) {
                direccionIdNew = em.getReference(direccionIdNew.getClass(), direccionIdNew.getId());
                cancha.setDireccionId(direccionIdNew);
            }
            List<Club> attachedClubListNew = new ArrayList<Club>();
            for (Club clubListNewClubToAttach : clubListNew) {
                clubListNewClubToAttach = em.getReference(clubListNewClubToAttach.getClass(), clubListNewClubToAttach.getId());
                attachedClubListNew.add(clubListNewClubToAttach);
            }
            clubListNew = attachedClubListNew;
            cancha.setClubList(clubListNew);
            List<Partido> attachedPartidoListNew = new ArrayList<Partido>();
            for (Partido partidoListNewPartidoToAttach : partidoListNew) {
                partidoListNewPartidoToAttach = em.getReference(partidoListNewPartidoToAttach.getClass(), partidoListNewPartidoToAttach.getId());
                attachedPartidoListNew.add(partidoListNewPartidoToAttach);
            }
            partidoListNew = attachedPartidoListNew;
            cancha.setPartidoList(partidoListNew);
            cancha = em.merge(cancha);
            if (direccionIdOld != null && !direccionIdOld.equals(direccionIdNew)) {
                direccionIdOld.getCanchaList().remove(cancha);
                direccionIdOld = em.merge(direccionIdOld);
            }
            if (direccionIdNew != null && !direccionIdNew.equals(direccionIdOld)) {
                direccionIdNew.getCanchaList().add(cancha);
                direccionIdNew = em.merge(direccionIdNew);
            }
            for (Club clubListOldClub : clubListOld) {
                if (!clubListNew.contains(clubListOldClub)) {
                    clubListOldClub.getCanchaList().remove(cancha);
                    clubListOldClub = em.merge(clubListOldClub);
                }
            }
            for (Club clubListNewClub : clubListNew) {
                if (!clubListOld.contains(clubListNewClub)) {
                    clubListNewClub.getCanchaList().add(cancha);
                    clubListNewClub = em.merge(clubListNewClub);
                }
            }
            for (Partido partidoListOldPartido : partidoListOld) {
                if (!partidoListNew.contains(partidoListOldPartido)) {
                    partidoListOldPartido.setCanchaId(null);
                    partidoListOldPartido = em.merge(partidoListOldPartido);
                }
            }
            for (Partido partidoListNewPartido : partidoListNew) {
                if (!partidoListOld.contains(partidoListNewPartido)) {
                    Cancha oldCanchaIdOfPartidoListNewPartido = partidoListNewPartido.getCanchaId();
                    partidoListNewPartido.setCanchaId(cancha);
                    partidoListNewPartido = em.merge(partidoListNewPartido);
                    if (oldCanchaIdOfPartidoListNewPartido != null && !oldCanchaIdOfPartidoListNewPartido.equals(cancha)) {
                        oldCanchaIdOfPartidoListNewPartido.getPartidoList().remove(partidoListNewPartido);
                        oldCanchaIdOfPartidoListNewPartido = em.merge(oldCanchaIdOfPartidoListNewPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cancha.getId();
                if (findCancha(id) == null) {
                    throw new NonexistentEntityException("The cancha with id " + id + " no longer exists.");
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
            Cancha cancha;
            try {
                cancha = em.getReference(Cancha.class, id);
                cancha.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cancha with id " + id + " no longer exists.", enfe);
            }
            Direccion direccionId = cancha.getDireccionId();
            if (direccionId != null) {
                direccionId.getCanchaList().remove(cancha);
                direccionId = em.merge(direccionId);
            }
            List<Club> clubList = cancha.getClubList();
            for (Club clubListClub : clubList) {
                clubListClub.getCanchaList().remove(cancha);
                clubListClub = em.merge(clubListClub);
            }
            List<Partido> partidoList = cancha.getPartidoList();
            for (Partido partidoListPartido : partidoList) {
                partidoListPartido.setCanchaId(null);
                partidoListPartido = em.merge(partidoListPartido);
            }
            em.remove(cancha);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cancha> findCanchaEntities() {
        return findCanchaEntities(true, -1, -1);
    }

    public List<Cancha> findCanchaEntities(int maxResults, int firstResult) {
        return findCanchaEntities(false, maxResults, firstResult);
    }

    private List<Cancha> findCanchaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cancha.class));
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

    public Cancha findCancha(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cancha.class, id);
        } finally {
            em.close();
        }
    }

    public int getCanchaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cancha> rt = cq.from(Cancha.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
