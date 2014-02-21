/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.torneo.ClasificacionJpaController;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Clasificacion;
import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Jornada;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Andrés
 */
public class ClasificacionJpaControllerExt extends ClasificacionJpaController{

    public ClasificacionJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    /**
     * 
     * @param equipo
     * @param jornada
     * @return 
     */
    public Clasificacion findClasificacionEntityByEquipoAndJornada(Equipo equipo, Jornada jornada){
        EntityManager em = getEntityManager();
        Clasificacion clasificacion = null;
        try {
            String query = "SELECT c FROM Clasificacion c WHERE c.equipoId = :equipo AND c.jornadaId = :jornada";
            Query q = em.createQuery(query);
            q.setParameter("equipo", equipo);
            q.setParameter("jornada", jornada);
            clasificacion = (Clasificacion) q.getSingleResult();
        } finally {
            em.close();
            return clasificacion;
        }
    }
    
    /**
     * 
     * @param categoria
     * @param jornada
     * @return 
     */
    public List<Object[]> findClasificacionByCategoria(Categoria categoria, Jornada jornada) {
        String query = "SELECT eq.nombre as EQUIPO, SUM(cla.jugados) AS JJ,"
                + "SUM(cla.ganados) AS G, "
                + "SUM(cla.empatados) as E, SUM(cla.perdidos) AS P, "
                + "SUM(cla.goles_favor) AS FAVOR, SUM(cla.goles_contra) AS CONTRA, "
                + "SUM(cla.diferencia) AS DIF, SUM(cla.puntos) AS PTS "
                + "FROM clasificacion cla JOIN equipo eq JOIN jornada jor "
                + "WHERE cla.equipo_id = eq.id AND eq.categoria_id = ? "
                + "AND cla.jornada_id = jor.id AND jor.numero <= ? "
                + "group by cla.equipo_id order by sum(cla.puntos) desc";
        EntityManager em = getEntityManager();
        List<Object[]> result = null;
        try {
            Query q = em.createNativeQuery(query)
                    .setParameter(1, categoria.getId())
                    .setParameter(2, jornada.getNumero());
            result = q.getResultList();
        } finally {
            em.close();
        }
        return result;
    }
    
    /**
     * Clasificación Total (Como Local y Visitante)
     * @param categoria
     * @param jornada
     * @param grupo
     * @param liga
     * @return 
     */
    public List<Object[]> findClasificacionByCategoria(Categoria categoria, Jornada jornada, Grupo grupo, Liga liga) {
        String query = "SELECT eq.id as idEquipo, eq.nombre as EQUIPO, SUM(cla.jugados) AS JJ,"
                + "SUM(cla.ganados) AS G, "
                + "SUM(cla.empatados) as E, SUM(cla.perdidos) AS P, "
                + "SUM(cla.goles_favor) AS FAVOR, SUM(cla.goles_contra) AS CONTRA, "
                + "SUM(cla.diferencia) AS DIF, SUM(cla.puntos) AS PTS "
                + "FROM clasificacion cla JOIN equipo eq JOIN jornada jor JOIN grupo grp JOIN liga li "
                + "WHERE cla.equipo_id = eq.id AND eq.categoria_id = ? "
                + "AND cla.jornada_id = jor.id AND jor.numero <= ? "
                + "AND jor.grupo_id = grp.id AND grp.id = ? "
                + "AND eq.liga_id = li.id and li.id = ? "
                + "GROUP BY cla.equipo_id ORDER BY PTS DESC, DIF DESC, FAVOR DESC, CONTRA ASC";
        EntityManager em = getEntityManager();
        List<Object[]> result = null;
        try {
            Query q = em.createNativeQuery(query)
                    .setParameter(1, categoria.getId())
                    .setParameter(2, jornada.getNumero())
                    .setParameter(3, grupo.getId())
                    .setParameter(4, liga.getId());
            result = q.getResultList();
        } finally {
            em.close();
        }
        return result;
    }
    
    /**
     * Clasificación Local o Visitante dependieno de status
     * status = 1 --> LOCAL
     * status = 0 --> VISITANTE
     * @param categoria
     * @param jornada
     * @param grupo
     * @param liga
     * @param status
     * @return 
     */
    public Object[] findClasificacionLocalVisitanteByCategoria(int idEquipo, Categoria categoria, Jornada jornada, Grupo grupo, Liga liga, int status) {
        String query = "SELECT SUM(cla.jugados) AS JJ,"
                + "SUM(cla.ganados) AS G, "
                + "SUM(cla.empatados) as E, SUM(cla.perdidos) AS P, "
                + "SUM(cla.goles_favor) AS FAVOR, SUM(cla.goles_contra) AS CONTRA, "
                + "SUM(cla.diferencia) AS DIF, SUM(cla.puntos) AS PTS "
                + "FROM clasificacion cla JOIN equipo eq JOIN jornada jor JOIN grupo grp JOIN liga li "
                + "WHERE cla.equipo_id = ? AND eq.id = ? AND eq.categoria_id = ? "
                + "AND cla.jornada_id = jor.id AND jor.numero <= ? "
                + "AND jor.grupo_id = grp.id AND grp.id = ? "
                + "AND eq.liga_id = li.id and li.id = ? "
                + "AND cla.eqlocal = ? ";
        
        EntityManager em = getEntityManager();
        List<Object[]> result = null;
        try {
            Query q = em.createNativeQuery(query)
                    .setParameter(1, idEquipo)
                    .setParameter(2, idEquipo)
                    .setParameter(3, categoria.getId())
                    .setParameter(4, jornada.getNumero())
                    .setParameter(5, grupo.getId())
                    .setParameter(6, liga.getId())
                    .setParameter(7, status);
            result = q.getResultList();
        } finally {
            em.close();
        }
        
        if(result.size() > 0){
            return result.get(0);
        }else{
            return null;
        }
        
    }
    
}
