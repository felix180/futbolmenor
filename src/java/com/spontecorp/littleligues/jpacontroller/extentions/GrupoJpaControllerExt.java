package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.torneo.GrupoJpaController;
import com.spontecorp.littleligues.model.torneo.Fase;
import com.spontecorp.littleligues.model.torneo.Fase_;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Grupo_;
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
public class GrupoJpaControllerExt extends GrupoJpaController{

    public GrupoJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Grupo> findGruposOnFase(Fase fase){
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Grupo> cq = cb.createQuery(Grupo.class);
        Root<Grupo> grupo = cq.from(Grupo.class);
        cq.select(grupo);
        cq.where(cb.equal(grupo.get(Grupo_.faseId), fase));
        TypedQuery<Grupo> query = em.createQuery(cq);
        return query.getResultList();
    }
    
    public List<Grupo> findGrupoEntitiesOnFase(Fase fase, int maxResults, int firstResult) {
        return findGrupoEntitiesOnFase(fase, false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntitiesOnFase(Fase fase, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Grupo> grupos = new ArrayList<>();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Grupo> grupo = cq.from(Grupo.class);
            cq.select(grupo);
            cq.where(cb.equal(grupo.get(Grupo_.faseId), fase));
            TypedQuery<Grupo> q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            grupos = q.getResultList();
        } finally {
            em.close();
            return grupos;
        }
    }
}
