package com.spontecorp.littleligues.jpacontroller.extentions;

import com.spontecorp.littleligues.jpacontroller.NoticiaJpaController;
import com.spontecorp.littleligues.model.Noticia;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author jgcastillo
 */
public class NoticiaJpaControllerExt extends NoticiaJpaController{

    public NoticiaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    /*
    public Noticia findNoticiaByPrincipal(Integer ppal) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Noticia.findByPrincipal", Noticia.class);
            query.setParameter("principal", ppal);
            Noticia noticia = (Noticia)query.getSingleResult();
            return noticia;
        } finally {
            em.close();
        }
    }
    */
    public Noticia findLastNoticia(){
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Noticia.findLastNoticia", Noticia.class);
            Noticia noticia = (Noticia)query.getSingleResult();
            return noticia;
        } finally {
            em.close();
        }
    }
}
