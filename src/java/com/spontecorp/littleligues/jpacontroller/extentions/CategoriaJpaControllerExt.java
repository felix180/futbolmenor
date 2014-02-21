/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.CategoriaJpaController;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Categoria_;
import com.spontecorp.littleligues.model.torneo.Fase;
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
 * @author Casper
 */
public class CategoriaJpaControllerExt extends CategoriaJpaController {

    public CategoriaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    @Override
    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    @Override
    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }
    
    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Categoria> categoria = cq.from(Categoria.class);
            cq.select(categoria);
            cq.orderBy(cb.asc(categoria.get(Categoria_.edadMin)));
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
    
    public List<Categoria> findListCategoria() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Categoria> categoria = cq.from(Categoria.class);
            cq.select(categoria);
            cq.orderBy(cb.asc(categoria.get(Categoria_.edadMin)));
            Query q = em.createQuery(cq);

            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Listado de Categorias de una Fase seleccionada
     * donde hay Partidos asociados a dicha Categoria
     * @param fase
     * @return 
     */
    public List<Categoria> findListCategoria(Fase fase) {
        List<Categoria> categorias = new ArrayList<>();
        EntityManager em = getEntityManager();
        try {
            String query = "select Categoria from Categoria where Categoria.id in "
                    + "(select Partido.categoriaId from Partido where (Partido.llaveId in "
                    + "(select Partido.llaveId from Llave where Llave.faseId = 7)) or "
                    + "(Partido.jornadaId in (select Jornada.id from Jornada where "
                    + "Jornada.grupoId in (select Grupo.id from Grupo where Grupo.faseId= 7))))";
            Query q = em.createQuery(query);
            q.setParameter("faseId", fase);
            categorias = q.getResultList();
        } finally {
            em.close();
        }
        return categorias;
    }
    
}
