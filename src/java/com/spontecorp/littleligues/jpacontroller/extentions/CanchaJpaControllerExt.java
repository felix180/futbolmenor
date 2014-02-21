/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.CanchaJpaController;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Cancha_;
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
public class CanchaJpaControllerExt extends CanchaJpaController{

    public CanchaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Cancha findCancha(String nombre) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Cancha> cq = cb.createQuery(Cancha.class);
            Root<Cancha> cancha = cq.from(Cancha.class);
            cq.select(cancha);
            cq.where(cb.equal(cancha.get(Cancha_.nombre), nombre));
            TypedQuery<Cancha> q = em.createQuery(cq);
            return q.getSingleResult();
        } finally {
            em.close();
        }
    }
}
