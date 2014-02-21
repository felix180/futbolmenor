/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.CarruselHome;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Grupo;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

/**
 *
 * @author zuleidyb
 */
public class CarruselHomeJpaController implements Serializable {

    public CarruselHomeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CarruselHome carruselHome) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(carruselHome);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CarruselHome carruselHome) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            carruselHome = em.merge(carruselHome);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carruselHome.getId();
                if (findCarruselHome(id) == null) {
                    throw new NonexistentEntityException("The carruselHome with id " + id + " no longer exists.");
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
            CarruselHome carruselHome;
            try {
                carruselHome = em.getReference(CarruselHome.class, id);
                carruselHome.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carruselHome with id " + id + " no longer exists.", enfe);
            }
            em.remove(carruselHome);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CarruselHome> findCarruselHomeEntities() {
        return findCarruselHomeEntities(true, -1, -1);
    }

    public List<CarruselHome> findCarruselHomeEntities(int maxResults, int firstResult) {
        return findCarruselHomeEntities(false, maxResults, firstResult);
    }

    private List<CarruselHome> findCarruselHomeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CarruselHome.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            //System.out.println("q.getResultList(): "+q.getResultList());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CarruselHome findCarruselHome(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CarruselHome.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarruselHomeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CarruselHome> rt = cq.from(CarruselHome.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    //Verifico si el Grupo pertenece a la fase y Liga correspondiente
    public boolean findGrupo(int idGrupo, Liga liga) {
        List<Grupo> grupos = new ArrayList<>();
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT g FROM Grupo g WHERE g.id = :idGrupo AND g.faseId IN "
                    + "(SELECT f FROM Fase f WHERE f.temporadaId IN "
                    + "(SELECT t FROM Temporada t WHERE t.ligaId = :liga))";
            Query q = em.createQuery(query);
            q.setParameter("idGrupo", idGrupo);
            q.setParameter("liga", liga);
            grupos = q.getResultList();
        } finally {
            em.close();
        }
        System.out.println("grupos: "+grupos);
        if(grupos.size() > 0){
            return true;
        }else {
            return false;
        }
    }
    
}
