package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.torneo.JornadaJpaController;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Jornada_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Casper
 */
public class JornadaJpaControllerExt extends JornadaJpaController{

    public JornadaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Jornada> findJornadasOnGrupo(Grupo grupo){
        List<Jornada> jornadas = new ArrayList<>();
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Jornada> cq = cb.createQuery(Jornada.class);
            Root<Jornada> jornada = cq.from(Jornada.class);
            cq.select(jornada);
            cq.where(cb.equal(jornada.get(Jornada_.grupoId), grupo));
            TypedQuery<Jornada> query = em.createQuery(cq);
            jornadas = query.getResultList();
        } finally {
            em.close();
            return jornadas;
        }
    }
    
    public List<Jornada> findJornadaEntitiesOnGrupo(Grupo grupo, int maxResults, int firstResult) {
        return findJornadaEntitiesOnGrupo(grupo,false, maxResults, firstResult);
    }
    
    private List<Jornada> findJornadaEntitiesOnGrupo(Grupo grupo, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Jornada> jornadas = new ArrayList<>();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Jornada> jornada = cq.from(Jornada.class);
            cq.select(jornada);
            cq.where(cb.equal(jornada.get(Jornada_.grupoId), grupo));
            TypedQuery<Jornada> q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            jornadas = q.getResultList();
        } finally {
            em.close();
            return jornadas;
        }
    }
    
    /**
     * Listado de Jornadas para un Grupo Seleccionado
     * @param grupo
     * @return 
     */
    public List<Jornada> findJornadaEntitiesOnGrupo(Grupo grupo) {
        EntityManager em = getEntityManager();
        List<Jornada> jornadas = new ArrayList<>();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Jornada> jornada = cq.from(Jornada.class);
            cq.select(jornada);
            cq.where(cb.equal(jornada.get(Jornada_.grupoId), grupo));
            //cq.orderBy(cb.desc(jornada.get(Jornada_.numero)));
            TypedQuery<Jornada> q = em.createQuery(cq);
            jornadas = q.getResultList();
        } finally {
            em.close();
            return jornadas;
        }
    }
    
    public Jornada findCurrentJornada(Grupo grupoId) {
        List<Jornada> jornadaList = new ArrayList();
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT j from Jornada j "
                    + "WHERE j.grupoId = :grupoId AND j.isCurrent = 1";
            Query q = em.createQuery(query);
            q.setParameter("grupoId", grupoId);
            jornadaList = q.getResultList();
        } finally {
            em.close();
        }
        if(jornadaList.size() > 0){
            return jornadaList.get(0);
        }else{
            return null;
        }
    }
    
}
