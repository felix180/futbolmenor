/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.torneo.PartidoJpaController;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.model.torneo.Partido_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Andrés
 */
public class PartidoJpaControllerExt extends PartidoJpaController{

    public PartidoJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Partido> findPartidoEntitiesByCategoria(Categoria categoria,int maxResults, int firstResult) {
        return findPartidoEntitiesByCategoria(categoria, false, maxResults, firstResult);
    }
    
    private List<Partido> findPartidoEntitiesByCategoria(Categoria categoria, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Partido> partido = cq.from(Partido.class);
            cq.select(partido);
            cq.where(cb.equal(partido.get(Partido_.categoriaId), categoria));
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
    
    public List<Partido> findPartidoEntitiesOnCategoriaWithinLlave(Categoria categoria, Llave llave) {
        List<Partido> partidos = new ArrayList<>();
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT p from Partido p "
                    + "WHERE p.llaveId = :llaveId AND p.categoriaId = :categoria";
            Query q = em.createQuery(query);
            q.setParameter("llaveId", llave);
            q.setParameter("categoria", categoria);
            partidos = q.getResultList();
        } finally {
            em.close();
        }

        return partidos;
    }

    public List<Partido> findPartidoEntitiesOnCategoriaWithinJornada(Categoria categoria, Jornada jornada) {
        List<Partido> partidos = new ArrayList<>();
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT p from Partido p "
                    + "WHERE p.jornadaId = :jornadaId AND p.categoriaId = :categoria";
            Query q = em.createQuery(query);
            q.setParameter("jornadaId", jornada);
            q.setParameter("categoria", categoria);
            partidos = q.getResultList();
        } finally {
            em.close();
        }

        return partidos;
    }
    
}
