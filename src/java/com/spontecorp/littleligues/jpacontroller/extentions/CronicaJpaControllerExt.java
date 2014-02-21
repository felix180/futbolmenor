package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.CronicaJpaController;
import com.spontecorp.littleligues.model.Cronica;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author jgcastillo
 */
public class CronicaJpaControllerExt extends CronicaJpaController{

    public CronicaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public List<Cronica> findCronicaEntitiesActives(){
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Cronica.findActive", Cronica.class);
            query.setParameter("activa", LittleLiguesUtils.ARTICULO_ACTIVO);
            return query.getResultList();
        } finally {
            em.close();
        }
        
    }
}
