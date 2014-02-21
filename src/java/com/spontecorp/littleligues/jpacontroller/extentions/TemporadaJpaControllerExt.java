package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.torneo.TemporadaJpaController;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.liga.Liga_;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.model.torneo.Temporada_;
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
public class TemporadaJpaControllerExt extends TemporadaJpaController{

    public TemporadaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Temporada> findTemporadasOnLiga(Liga liga){
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Temporada> cq = cb.createQuery(Temporada.class);
        Root<Temporada> temporada = cq.from(Temporada.class);
        cq.select(temporada);
        cq.where(cb.equal(temporada.get(Temporada_.ligaId), liga));
        TypedQuery<Temporada> query = em.createQuery(cq);
        return query.getResultList();
    }
    
    public List<Temporada> findTemporadaEntitiesOnLiga(Liga liga, int maxResults, int firstResult) {
        return findFaseEntitiesOnTemporada(liga, false, maxResults, firstResult);
    }

    private List<Temporada> findFaseEntitiesOnTemporada(Liga liga, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Temporada> temporadas = new ArrayList<>();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Temporada> season = cq.from(Temporada.class);
            cq.select(season);
            cq.where(cb.equal(season.get(Temporada_.ligaId), liga));
            TypedQuery<Temporada> q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            temporadas = q.getResultList();
        } finally {
            em.close();
            return temporadas;
        }
    }
}
