/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Liga;
import java.util.ArrayList;
import java.util.List;
import com.spontecorp.littleligues.model.torneo.Arbitro;
import com.spontecorp.littleligues.model.liga.Contacto;
import com.spontecorp.littleligues.model.torneo.Asociacion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class AsociacionJpaController implements Serializable {

    public AsociacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asociacion asociacion) {
        if (asociacion.getLigaList() == null) {
            asociacion.setLigaList(new ArrayList<Liga>());
        }
        if (asociacion.getArbitroList() == null) {
            asociacion.setArbitroList(new ArrayList<Arbitro>());
        }
        if (asociacion.getContactoList() == null) {
            asociacion.setContactoList(new ArrayList<Contacto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccionId = asociacion.getDireccionId();
            if (direccionId != null) {
                direccionId = em.getReference(direccionId.getClass(), direccionId.getId());
                asociacion.setDireccionId(direccionId);
            }
            List<Liga> attachedLigaList = new ArrayList<Liga>();
            for (Liga ligaListLigaToAttach : asociacion.getLigaList()) {
                ligaListLigaToAttach = em.getReference(ligaListLigaToAttach.getClass(), ligaListLigaToAttach.getId());
                attachedLigaList.add(ligaListLigaToAttach);
            }
            asociacion.setLigaList(attachedLigaList);
            List<Arbitro> attachedArbitroList = new ArrayList<Arbitro>();
            for (Arbitro arbitroListArbitroToAttach : asociacion.getArbitroList()) {
                arbitroListArbitroToAttach = em.getReference(arbitroListArbitroToAttach.getClass(), arbitroListArbitroToAttach.getId());
                attachedArbitroList.add(arbitroListArbitroToAttach);
            }
            asociacion.setArbitroList(attachedArbitroList);
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : asociacion.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getId());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            asociacion.setContactoList(attachedContactoList);
            em.persist(asociacion);
            if (direccionId != null) {
                direccionId.getAsociacionList().add(asociacion);
                direccionId = em.merge(direccionId);
            }
            for (Liga ligaListLiga : asociacion.getLigaList()) {
                Asociacion oldAsociacionIdOfLigaListLiga = ligaListLiga.getAsociacionId();
                ligaListLiga.setAsociacionId(asociacion);
                ligaListLiga = em.merge(ligaListLiga);
                if (oldAsociacionIdOfLigaListLiga != null) {
                    oldAsociacionIdOfLigaListLiga.getLigaList().remove(ligaListLiga);
                    oldAsociacionIdOfLigaListLiga = em.merge(oldAsociacionIdOfLigaListLiga);
                }
            }
            for (Arbitro arbitroListArbitro : asociacion.getArbitroList()) {
                Asociacion oldAsociacionIdOfArbitroListArbitro = arbitroListArbitro.getAsociacionId();
                arbitroListArbitro.setAsociacionId(asociacion);
                arbitroListArbitro = em.merge(arbitroListArbitro);
                if (oldAsociacionIdOfArbitroListArbitro != null) {
                    oldAsociacionIdOfArbitroListArbitro.getArbitroList().remove(arbitroListArbitro);
                    oldAsociacionIdOfArbitroListArbitro = em.merge(oldAsociacionIdOfArbitroListArbitro);
                }
            }
            for (Contacto contactoListContacto : asociacion.getContactoList()) {
                Asociacion oldAsociacionIdOfContactoListContacto = contactoListContacto.getAsociacionId();
                contactoListContacto.setAsociacionId(asociacion);
                contactoListContacto = em.merge(contactoListContacto);
                if (oldAsociacionIdOfContactoListContacto != null) {
                    oldAsociacionIdOfContactoListContacto.getContactoList().remove(contactoListContacto);
                    oldAsociacionIdOfContactoListContacto = em.merge(oldAsociacionIdOfContactoListContacto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asociacion asociacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asociacion persistentAsociacion = em.find(Asociacion.class, asociacion.getId());
            Direccion direccionIdOld = persistentAsociacion.getDireccionId();
            Direccion direccionIdNew = asociacion.getDireccionId();
            List<Liga> ligaListOld = persistentAsociacion.getLigaList();
            List<Liga> ligaListNew = asociacion.getLigaList();
            List<Arbitro> arbitroListOld = persistentAsociacion.getArbitroList();
            List<Arbitro> arbitroListNew = asociacion.getArbitroList();
            List<Contacto> contactoListOld = persistentAsociacion.getContactoList();
            List<Contacto> contactoListNew = asociacion.getContactoList();
            if (direccionIdNew != null) {
                direccionIdNew = em.getReference(direccionIdNew.getClass(), direccionIdNew.getId());
                asociacion.setDireccionId(direccionIdNew);
            }
            List<Liga> attachedLigaListNew = new ArrayList<Liga>();
            for (Liga ligaListNewLigaToAttach : ligaListNew) {
                ligaListNewLigaToAttach = em.getReference(ligaListNewLigaToAttach.getClass(), ligaListNewLigaToAttach.getId());
                attachedLigaListNew.add(ligaListNewLigaToAttach);
            }
            ligaListNew = attachedLigaListNew;
            asociacion.setLigaList(ligaListNew);
            List<Arbitro> attachedArbitroListNew = new ArrayList<Arbitro>();
            for (Arbitro arbitroListNewArbitroToAttach : arbitroListNew) {
                arbitroListNewArbitroToAttach = em.getReference(arbitroListNewArbitroToAttach.getClass(), arbitroListNewArbitroToAttach.getId());
                attachedArbitroListNew.add(arbitroListNewArbitroToAttach);
            }
            arbitroListNew = attachedArbitroListNew;
            asociacion.setArbitroList(arbitroListNew);
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getId());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            asociacion.setContactoList(contactoListNew);
            asociacion = em.merge(asociacion);
            if (direccionIdOld != null && !direccionIdOld.equals(direccionIdNew)) {
                direccionIdOld.getAsociacionList().remove(asociacion);
                direccionIdOld = em.merge(direccionIdOld);
            }
            if (direccionIdNew != null && !direccionIdNew.equals(direccionIdOld)) {
                direccionIdNew.getAsociacionList().add(asociacion);
                direccionIdNew = em.merge(direccionIdNew);
            }
            for (Liga ligaListOldLiga : ligaListOld) {
                if (!ligaListNew.contains(ligaListOldLiga)) {
                    ligaListOldLiga.setAsociacionId(null);
                    ligaListOldLiga = em.merge(ligaListOldLiga);
                }
            }
            for (Liga ligaListNewLiga : ligaListNew) {
                if (!ligaListOld.contains(ligaListNewLiga)) {
                    Asociacion oldAsociacionIdOfLigaListNewLiga = ligaListNewLiga.getAsociacionId();
                    ligaListNewLiga.setAsociacionId(asociacion);
                    ligaListNewLiga = em.merge(ligaListNewLiga);
                    if (oldAsociacionIdOfLigaListNewLiga != null && !oldAsociacionIdOfLigaListNewLiga.equals(asociacion)) {
                        oldAsociacionIdOfLigaListNewLiga.getLigaList().remove(ligaListNewLiga);
                        oldAsociacionIdOfLigaListNewLiga = em.merge(oldAsociacionIdOfLigaListNewLiga);
                    }
                }
            }
            for (Arbitro arbitroListOldArbitro : arbitroListOld) {
                if (!arbitroListNew.contains(arbitroListOldArbitro)) {
                    arbitroListOldArbitro.setAsociacionId(null);
                    arbitroListOldArbitro = em.merge(arbitroListOldArbitro);
                }
            }
            for (Arbitro arbitroListNewArbitro : arbitroListNew) {
                if (!arbitroListOld.contains(arbitroListNewArbitro)) {
                    Asociacion oldAsociacionIdOfArbitroListNewArbitro = arbitroListNewArbitro.getAsociacionId();
                    arbitroListNewArbitro.setAsociacionId(asociacion);
                    arbitroListNewArbitro = em.merge(arbitroListNewArbitro);
                    if (oldAsociacionIdOfArbitroListNewArbitro != null && !oldAsociacionIdOfArbitroListNewArbitro.equals(asociacion)) {
                        oldAsociacionIdOfArbitroListNewArbitro.getArbitroList().remove(arbitroListNewArbitro);
                        oldAsociacionIdOfArbitroListNewArbitro = em.merge(oldAsociacionIdOfArbitroListNewArbitro);
                    }
                }
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.setAsociacionId(null);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    Asociacion oldAsociacionIdOfContactoListNewContacto = contactoListNewContacto.getAsociacionId();
                    contactoListNewContacto.setAsociacionId(asociacion);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                    if (oldAsociacionIdOfContactoListNewContacto != null && !oldAsociacionIdOfContactoListNewContacto.equals(asociacion)) {
                        oldAsociacionIdOfContactoListNewContacto.getContactoList().remove(contactoListNewContacto);
                        oldAsociacionIdOfContactoListNewContacto = em.merge(oldAsociacionIdOfContactoListNewContacto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = asociacion.getId();
                if (findAsociacion(id) == null) {
                    throw new NonexistentEntityException("The asociacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Asociacion asociacion;
            try {
                asociacion = em.getReference(Asociacion.class, id);
                asociacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asociacion with id " + id + " no longer exists.", enfe);
            }
            Direccion direccionId = asociacion.getDireccionId();
            if (direccionId != null) {
                direccionId.getAsociacionList().remove(asociacion);
                direccionId = em.merge(direccionId);
            }
            List<Liga> ligaList = asociacion.getLigaList();
            for (Liga ligaListLiga : ligaList) {
                ligaListLiga.setAsociacionId(null);
                ligaListLiga = em.merge(ligaListLiga);
            }
            List<Arbitro> arbitroList = asociacion.getArbitroList();
            for (Arbitro arbitroListArbitro : arbitroList) {
                arbitroListArbitro.setAsociacionId(null);
                arbitroListArbitro = em.merge(arbitroListArbitro);
            }
            List<Contacto> contactoList = asociacion.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.setAsociacionId(null);
                contactoListContacto = em.merge(contactoListContacto);
            }
            em.remove(asociacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Asociacion> findAsociacionEntities() {
        return findAsociacionEntities(true, -1, -1);
    }

    public List<Asociacion> findAsociacionEntities(int maxResults, int firstResult) {
        return findAsociacionEntities(false, maxResults, firstResult);
    }

    private List<Asociacion> findAsociacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asociacion.class));
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

    public Asociacion findAsociacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asociacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsociacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asociacion> rt = cq.from(Asociacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
