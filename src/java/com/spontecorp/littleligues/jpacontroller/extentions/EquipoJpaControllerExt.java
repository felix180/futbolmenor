package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.EquipoJpaController;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.liga.Equipo_;
import com.spontecorp.littleligues.model.liga.Liga;
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
public class EquipoJpaControllerExt extends EquipoJpaController{

    public EquipoJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Equipo> findEquiposOnCategoria(Categoria categoria, Liga liga){
        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Equipo> cq = cb.createQuery(Equipo.class);
        Root<Equipo> equipo = cq.from(Equipo.class);
        cq.select(equipo);
        cq.where(cb.and(cb.equal(equipo.get(Equipo_.categoriaId), categoria)),
                cb.equal(equipo.get(Equipo_.ligaId), liga));
        TypedQuery<Equipo> query = em.createQuery(cq);
        return query.getResultList();
    }
}
