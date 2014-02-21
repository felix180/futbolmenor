package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.torneo.LlaveJpaController;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Llave_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Casper
 */
public class LlaveJpaControllerExt extends LlaveJpaController{

    public LlaveJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Llave> findLlavesOnFase(Fase fase){
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Llave> cq = cb.createQuery(Llave.class);
        Root<Llave> llave = cq.from(Llave.class);
        cq.select(llave);
        cq.where(cb.equal(llave.get(Llave_.faseId), fase));
        TypedQuery<Llave> query = em.createQuery(cq);
        return query.getResultList();
    }
    
    public List<Llave> findLlaveEntitiesOnFase(Fase fase, int maxResults, int firstResult) {
        return findLlaveEntitiesOnFase(fase, false, maxResults, firstResult);
    }

    private List<Llave> findLlaveEntitiesOnFase(Fase fase, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Llave> llaves = new ArrayList<>();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Llave> llave = cq.from(Llave.class);
            cq.select(llave);
            cq.where(cb.equal(llave.get(Llave_.faseId), fase));
            TypedQuery<Llave> q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            llaves = q.getResultList();
        } finally {
            em.close();
            return llaves;
        }
    }

    public Object findLlave(String value) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Llave> cq = cb.createQuery(Llave.class);
            Root<Llave> llave = cq.from(Llave.class);
            cq.select(llave);
            cq.where(cb.equal(llave.get(Llave_.nombre), value));
            TypedQuery<Llave> q = em.createQuery(cq);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }
 
}
