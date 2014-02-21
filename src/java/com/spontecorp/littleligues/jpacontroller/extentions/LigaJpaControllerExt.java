package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.LigaJpaController;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.liga.Liga_;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jgcastillo
 */
public class LigaJpaControllerExt extends LigaJpaController{

    public LigaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Liga findLiga(String nombre){
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Liga> cq = cb.createQuery(Liga.class);
            Root<Liga> liga = cq.from(Liga.class);
            cq.select(liga);
            cq.where(cb.equal(liga.get(Liga_.nombre), nombre));
            TypedQuery<Liga> q = em.createQuery(cq);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }

}
