/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.liga.Localidad;
import com.spontecorp.littleligues.model.liga.Liga;
import java.util.ArrayList;
import java.util.List;
import com.spontecorp.littleligues.model.liga.Club;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.torneo.Asociacion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class DireccionJpaController implements Serializable {

    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Direccion direccion) {
        if (direccion.getLigaList() == null) {
            direccion.setLigaList(new ArrayList<Liga>());
        }
        if (direccion.getClubList() == null) {
            direccion.setClubList(new ArrayList<Club>());
        }
        if (direccion.getCanchaList() == null) {
            direccion.setCanchaList(new ArrayList<Cancha>());
        }
        if (direccion.getAsociacionList() == null) {
            direccion.setAsociacionList(new ArrayList<Asociacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad localidadId = direccion.getLocalidadId();
            if (localidadId != null) {
                localidadId = em.getReference(localidadId.getClass(), localidadId.getId());
                direccion.setLocalidadId(localidadId);
            }
            List<Liga> attachedLigaList = new ArrayList<Liga>();
            for (Liga ligaListLigaToAttach : direccion.getLigaList()) {
                ligaListLigaToAttach = em.getReference(ligaListLigaToAttach.getClass(), ligaListLigaToAttach.getId());
                attachedLigaList.add(ligaListLigaToAttach);
            }
            direccion.setLigaList(attachedLigaList);
            List<Club> attachedClubList = new ArrayList<Club>();
            for (Club clubListClubToAttach : direccion.getClubList()) {
                clubListClubToAttach = em.getReference(clubListClubToAttach.getClass(), clubListClubToAttach.getId());
                attachedClubList.add(clubListClubToAttach);
            }
            direccion.setClubList(attachedClubList);
            List<Cancha> attachedCanchaList = new ArrayList<Cancha>();
            for (Cancha canchaListCanchaToAttach : direccion.getCanchaList()) {
                canchaListCanchaToAttach = em.getReference(canchaListCanchaToAttach.getClass(), canchaListCanchaToAttach.getId());
                attachedCanchaList.add(canchaListCanchaToAttach);
            }
            direccion.setCanchaList(attachedCanchaList);
            List<Asociacion> attachedAsociacionList = new ArrayList<Asociacion>();
            for (Asociacion asociacionListAsociacionToAttach : direccion.getAsociacionList()) {
                asociacionListAsociacionToAttach = em.getReference(asociacionListAsociacionToAttach.getClass(), asociacionListAsociacionToAttach.getId());
                attachedAsociacionList.add(asociacionListAsociacionToAttach);
            }
            direccion.setAsociacionList(attachedAsociacionList);
            em.persist(direccion);
            if (localidadId != null) {
                localidadId.getDireccionList().add(direccion);
                localidadId = em.merge(localidadId);
            }
            for (Liga ligaListLiga : direccion.getLigaList()) {
                Direccion oldDireccionIdOfLigaListLiga = ligaListLiga.getDireccionId();
                ligaListLiga.setDireccionId(direccion);
                ligaListLiga = em.merge(ligaListLiga);
                if (oldDireccionIdOfLigaListLiga != null) {
                    oldDireccionIdOfLigaListLiga.getLigaList().remove(ligaListLiga);
                    oldDireccionIdOfLigaListLiga = em.merge(oldDireccionIdOfLigaListLiga);
                }
            }
            for (Club clubListClub : direccion.getClubList()) {
                Direccion oldDireccionIdOfClubListClub = clubListClub.getDireccionId();
                clubListClub.setDireccionId(direccion);
                clubListClub = em.merge(clubListClub);
                if (oldDireccionIdOfClubListClub != null) {
                    oldDireccionIdOfClubListClub.getClubList().remove(clubListClub);
                    oldDireccionIdOfClubListClub = em.merge(oldDireccionIdOfClubListClub);
                }
            }
            for (Cancha canchaListCancha : direccion.getCanchaList()) {
                Direccion oldDireccionIdOfCanchaListCancha = canchaListCancha.getDireccionId();
                canchaListCancha.setDireccionId(direccion);
                canchaListCancha = em.merge(canchaListCancha);
                if (oldDireccionIdOfCanchaListCancha != null) {
                    oldDireccionIdOfCanchaListCancha.getCanchaList().remove(canchaListCancha);
                    oldDireccionIdOfCanchaListCancha = em.merge(oldDireccionIdOfCanchaListCancha);
                }
            }
            for (Asociacion asociacionListAsociacion : direccion.getAsociacionList()) {
                Direccion oldDireccionIdOfAsociacionListAsociacion = asociacionListAsociacion.getDireccionId();
                asociacionListAsociacion.setDireccionId(direccion);
                asociacionListAsociacion = em.merge(asociacionListAsociacion);
                if (oldDireccionIdOfAsociacionListAsociacion != null) {
                    oldDireccionIdOfAsociacionListAsociacion.getAsociacionList().remove(asociacionListAsociacion);
                    oldDireccionIdOfAsociacionListAsociacion = em.merge(oldDireccionIdOfAsociacionListAsociacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Direccion direccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion persistentDireccion = em.find(Direccion.class, direccion.getId());
            Localidad localidadIdOld = persistentDireccion.getLocalidadId();
            Localidad localidadIdNew = direccion.getLocalidadId();
            List<Liga> ligaListOld = persistentDireccion.getLigaList();
            List<Liga> ligaListNew = direccion.getLigaList();
            List<Club> clubListOld = persistentDireccion.getClubList();
            List<Club> clubListNew = direccion.getClubList();
            List<Cancha> canchaListOld = persistentDireccion.getCanchaList();
            List<Cancha> canchaListNew = direccion.getCanchaList();
            List<Asociacion> asociacionListOld = persistentDireccion.getAsociacionList();
            List<Asociacion> asociacionListNew = direccion.getAsociacionList();
            if (localidadIdNew != null) {
                localidadIdNew = em.getReference(localidadIdNew.getClass(), localidadIdNew.getId());
                direccion.setLocalidadId(localidadIdNew);
            }
            List<Liga> attachedLigaListNew = new ArrayList<Liga>();
            for (Liga ligaListNewLigaToAttach : ligaListNew) {
                ligaListNewLigaToAttach = em.getReference(ligaListNewLigaToAttach.getClass(), ligaListNewLigaToAttach.getId());
                attachedLigaListNew.add(ligaListNewLigaToAttach);
            }
            ligaListNew = attachedLigaListNew;
            direccion.setLigaList(ligaListNew);
            List<Club> attachedClubListNew = new ArrayList<Club>();
            for (Club clubListNewClubToAttach : clubListNew) {
                clubListNewClubToAttach = em.getReference(clubListNewClubToAttach.getClass(), clubListNewClubToAttach.getId());
                attachedClubListNew.add(clubListNewClubToAttach);
            }
            clubListNew = attachedClubListNew;
            direccion.setClubList(clubListNew);
            List<Cancha> attachedCanchaListNew = new ArrayList<Cancha>();
            for (Cancha canchaListNewCanchaToAttach : canchaListNew) {
                canchaListNewCanchaToAttach = em.getReference(canchaListNewCanchaToAttach.getClass(), canchaListNewCanchaToAttach.getId());
                attachedCanchaListNew.add(canchaListNewCanchaToAttach);
            }
            canchaListNew = attachedCanchaListNew;
            direccion.setCanchaList(canchaListNew);
            List<Asociacion> attachedAsociacionListNew = new ArrayList<Asociacion>();
            for (Asociacion asociacionListNewAsociacionToAttach : asociacionListNew) {
                asociacionListNewAsociacionToAttach = em.getReference(asociacionListNewAsociacionToAttach.getClass(), asociacionListNewAsociacionToAttach.getId());
                attachedAsociacionListNew.add(asociacionListNewAsociacionToAttach);
            }
            asociacionListNew = attachedAsociacionListNew;
            direccion.setAsociacionList(asociacionListNew);
            direccion = em.merge(direccion);
            if (localidadIdOld != null && !localidadIdOld.equals(localidadIdNew)) {
                localidadIdOld.getDireccionList().remove(direccion);
                localidadIdOld = em.merge(localidadIdOld);
            }
            if (localidadIdNew != null && !localidadIdNew.equals(localidadIdOld)) {
                localidadIdNew.getDireccionList().add(direccion);
                localidadIdNew = em.merge(localidadIdNew);
            }
            for (Liga ligaListOldLiga : ligaListOld) {
                if (!ligaListNew.contains(ligaListOldLiga)) {
                    ligaListOldLiga.setDireccionId(null);
                    ligaListOldLiga = em.merge(ligaListOldLiga);
                }
            }
            for (Liga ligaListNewLiga : ligaListNew) {
                if (!ligaListOld.contains(ligaListNewLiga)) {
                    Direccion oldDireccionIdOfLigaListNewLiga = ligaListNewLiga.getDireccionId();
                    ligaListNewLiga.setDireccionId(direccion);
                    ligaListNewLiga = em.merge(ligaListNewLiga);
                    if (oldDireccionIdOfLigaListNewLiga != null && !oldDireccionIdOfLigaListNewLiga.equals(direccion)) {
                        oldDireccionIdOfLigaListNewLiga.getLigaList().remove(ligaListNewLiga);
                        oldDireccionIdOfLigaListNewLiga = em.merge(oldDireccionIdOfLigaListNewLiga);
                    }
                }
            }
            for (Club clubListOldClub : clubListOld) {
                if (!clubListNew.contains(clubListOldClub)) {
                    clubListOldClub.setDireccionId(null);
                    clubListOldClub = em.merge(clubListOldClub);
                }
            }
            for (Club clubListNewClub : clubListNew) {
                if (!clubListOld.contains(clubListNewClub)) {
                    Direccion oldDireccionIdOfClubListNewClub = clubListNewClub.getDireccionId();
                    clubListNewClub.setDireccionId(direccion);
                    clubListNewClub = em.merge(clubListNewClub);
                    if (oldDireccionIdOfClubListNewClub != null && !oldDireccionIdOfClubListNewClub.equals(direccion)) {
                        oldDireccionIdOfClubListNewClub.getClubList().remove(clubListNewClub);
                        oldDireccionIdOfClubListNewClub = em.merge(oldDireccionIdOfClubListNewClub);
                    }
                }
            }
            for (Cancha canchaListOldCancha : canchaListOld) {
                if (!canchaListNew.contains(canchaListOldCancha)) {
                    canchaListOldCancha.setDireccionId(null);
                    canchaListOldCancha = em.merge(canchaListOldCancha);
                }
            }
            for (Cancha canchaListNewCancha : canchaListNew) {
                if (!canchaListOld.contains(canchaListNewCancha)) {
                    Direccion oldDireccionIdOfCanchaListNewCancha = canchaListNewCancha.getDireccionId();
                    canchaListNewCancha.setDireccionId(direccion);
                    canchaListNewCancha = em.merge(canchaListNewCancha);
                    if (oldDireccionIdOfCanchaListNewCancha != null && !oldDireccionIdOfCanchaListNewCancha.equals(direccion)) {
                        oldDireccionIdOfCanchaListNewCancha.getCanchaList().remove(canchaListNewCancha);
                        oldDireccionIdOfCanchaListNewCancha = em.merge(oldDireccionIdOfCanchaListNewCancha);
                    }
                }
            }
            for (Asociacion asociacionListOldAsociacion : asociacionListOld) {
                if (!asociacionListNew.contains(asociacionListOldAsociacion)) {
                    asociacionListOldAsociacion.setDireccionId(null);
                    asociacionListOldAsociacion = em.merge(asociacionListOldAsociacion);
                }
            }
            for (Asociacion asociacionListNewAsociacion : asociacionListNew) {
                if (!asociacionListOld.contains(asociacionListNewAsociacion)) {
                    Direccion oldDireccionIdOfAsociacionListNewAsociacion = asociacionListNewAsociacion.getDireccionId();
                    asociacionListNewAsociacion.setDireccionId(direccion);
                    asociacionListNewAsociacion = em.merge(asociacionListNewAsociacion);
                    if (oldDireccionIdOfAsociacionListNewAsociacion != null && !oldDireccionIdOfAsociacionListNewAsociacion.equals(direccion)) {
                        oldDireccionIdOfAsociacionListNewAsociacion.getAsociacionList().remove(asociacionListNewAsociacion);
                        oldDireccionIdOfAsociacionListNewAsociacion = em.merge(oldDireccionIdOfAsociacionListNewAsociacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = direccion.getId();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.");
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
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.", enfe);
            }
            Localidad localidadId = direccion.getLocalidadId();
            if (localidadId != null) {
                localidadId.getDireccionList().remove(direccion);
                localidadId = em.merge(localidadId);
            }
            List<Liga> ligaList = direccion.getLigaList();
            for (Liga ligaListLiga : ligaList) {
                ligaListLiga.setDireccionId(null);
                ligaListLiga = em.merge(ligaListLiga);
            }
            List<Club> clubList = direccion.getClubList();
            for (Club clubListClub : clubList) {
                clubListClub.setDireccionId(null);
                clubListClub = em.merge(clubListClub);
            }
            List<Cancha> canchaList = direccion.getCanchaList();
            for (Cancha canchaListCancha : canchaList) {
                canchaListCancha.setDireccionId(null);
                canchaListCancha = em.merge(canchaListCancha);
            }
            List<Asociacion> asociacionList = direccion.getAsociacionList();
            for (Asociacion asociacionListAsociacion : asociacionList) {
                asociacionListAsociacion.setDireccionId(null);
                asociacionListAsociacion = em.merge(asociacionListAsociacion);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
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

    public Direccion findDireccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
