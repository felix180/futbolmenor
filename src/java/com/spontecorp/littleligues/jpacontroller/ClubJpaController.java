/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Equipo;
import java.util.ArrayList;
import java.util.List;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Club;
import com.spontecorp.littleligues.model.liga.Contacto;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class ClubJpaController implements Serializable {

    public ClubJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Club club) {
        if (club.getCanchaList() == null) {
            club.setCanchaList(new ArrayList<Cancha>());
        }
        if (club.getContactoList() == null) {
            club.setContactoList(new ArrayList<Contacto>());
        }
        if (club.getEquipoList() == null) {
            club.setEquipoList(new ArrayList<Equipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccionId = club.getDireccionId();
            if (direccionId != null) {
                direccionId = em.getReference(direccionId.getClass(), direccionId.getId());
                club.setDireccionId(direccionId);
            }
            List<Cancha> attachedCanchaList = new ArrayList<Cancha>();
            for (Cancha canchaListCanchaToAttach : club.getCanchaList()) {
                canchaListCanchaToAttach = em.getReference(canchaListCanchaToAttach.getClass(), canchaListCanchaToAttach.getId());
                attachedCanchaList.add(canchaListCanchaToAttach);
            }
            club.setCanchaList(attachedCanchaList);
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : club.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getId());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            club.setContactoList(attachedContactoList);
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : club.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getId());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            club.setEquipoList(attachedEquipoList);
            em.persist(club);
            if (direccionId != null) {
                direccionId.getClubList().add(club);
                direccionId = em.merge(direccionId);
            }
            for (Cancha canchaListCancha : club.getCanchaList()) {
                canchaListCancha.getClubList().add(club);
                canchaListCancha = em.merge(canchaListCancha);
            }
            for (Contacto contactoListContacto : club.getContactoList()) {
                Club oldClubIdOfContactoListContacto = contactoListContacto.getClubId();
                contactoListContacto.setClubId(club);
                contactoListContacto = em.merge(contactoListContacto);
                if (oldClubIdOfContactoListContacto != null) {
                    oldClubIdOfContactoListContacto.getContactoList().remove(contactoListContacto);
                    oldClubIdOfContactoListContacto = em.merge(oldClubIdOfContactoListContacto);
                }
            }
            for (Equipo equipoListEquipo : club.getEquipoList()) {
                Club oldClubIdOfEquipoListEquipo = equipoListEquipo.getClubId();
                equipoListEquipo.setClubId(club);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldClubIdOfEquipoListEquipo != null) {
                    oldClubIdOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldClubIdOfEquipoListEquipo = em.merge(oldClubIdOfEquipoListEquipo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Club club) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Club persistentClub = em.find(Club.class, club.getId());
            Direccion direccionIdOld = persistentClub.getDireccionId();
            Direccion direccionIdNew = club.getDireccionId();
            List<Cancha> canchaListOld = persistentClub.getCanchaList();
            List<Cancha> canchaListNew = club.getCanchaList();
            List<Contacto> contactoListOld = persistentClub.getContactoList();
            List<Contacto> contactoListNew = club.getContactoList();
            List<Equipo> equipoListOld = persistentClub.getEquipoList();
            List<Equipo> equipoListNew = club.getEquipoList();
            List<String> illegalOrphanMessages = null;
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Equipo " + equipoListOldEquipo + " since its clubId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (direccionIdNew != null) {
                direccionIdNew = em.getReference(direccionIdNew.getClass(), direccionIdNew.getId());
                club.setDireccionId(direccionIdNew);
            }
            List<Cancha> attachedCanchaListNew = new ArrayList<Cancha>();
            for (Cancha canchaListNewCanchaToAttach : canchaListNew) {
                canchaListNewCanchaToAttach = em.getReference(canchaListNewCanchaToAttach.getClass(), canchaListNewCanchaToAttach.getId());
                attachedCanchaListNew.add(canchaListNewCanchaToAttach);
            }
            canchaListNew = attachedCanchaListNew;
            club.setCanchaList(canchaListNew);
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getId());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            club.setContactoList(contactoListNew);
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getId());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            club.setEquipoList(equipoListNew);
            club = em.merge(club);
            if (direccionIdOld != null && !direccionIdOld.equals(direccionIdNew)) {
                direccionIdOld.getClubList().remove(club);
                direccionIdOld = em.merge(direccionIdOld);
            }
            if (direccionIdNew != null && !direccionIdNew.equals(direccionIdOld)) {
                direccionIdNew.getClubList().add(club);
                direccionIdNew = em.merge(direccionIdNew);
            }
            for (Cancha canchaListOldCancha : canchaListOld) {
                if (!canchaListNew.contains(canchaListOldCancha)) {
                    canchaListOldCancha.getClubList().remove(club);
                    canchaListOldCancha = em.merge(canchaListOldCancha);
                }
            }
            for (Cancha canchaListNewCancha : canchaListNew) {
                if (!canchaListOld.contains(canchaListNewCancha)) {
                    canchaListNewCancha.getClubList().add(club);
                    canchaListNewCancha = em.merge(canchaListNewCancha);
                }
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.setClubId(null);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    Club oldClubIdOfContactoListNewContacto = contactoListNewContacto.getClubId();
                    contactoListNewContacto.setClubId(club);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                    if (oldClubIdOfContactoListNewContacto != null && !oldClubIdOfContactoListNewContacto.equals(club)) {
                        oldClubIdOfContactoListNewContacto.getContactoList().remove(contactoListNewContacto);
                        oldClubIdOfContactoListNewContacto = em.merge(oldClubIdOfContactoListNewContacto);
                    }
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    Club oldClubIdOfEquipoListNewEquipo = equipoListNewEquipo.getClubId();
                    equipoListNewEquipo.setClubId(club);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldClubIdOfEquipoListNewEquipo != null && !oldClubIdOfEquipoListNewEquipo.equals(club)) {
                        oldClubIdOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldClubIdOfEquipoListNewEquipo = em.merge(oldClubIdOfEquipoListNewEquipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = club.getId();
                if (findClub(id) == null) {
                    throw new NonexistentEntityException("The club with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Club club;
            try {
                club = em.getReference(Club.class, id);
                club.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The club with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Equipo> equipoListOrphanCheck = club.getEquipoList();
            for (Equipo equipoListOrphanCheckEquipo : equipoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Club (" + club + ") cannot be destroyed since the Equipo " + equipoListOrphanCheckEquipo + " in its equipoList field has a non-nullable clubId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Direccion direccionId = club.getDireccionId();
            if (direccionId != null) {
                direccionId.getClubList().remove(club);
                direccionId = em.merge(direccionId);
            }
            List<Cancha> canchaList = club.getCanchaList();
            for (Cancha canchaListCancha : canchaList) {
                canchaListCancha.getClubList().remove(club);
                canchaListCancha = em.merge(canchaListCancha);
            }
            List<Contacto> contactoList = club.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.setClubId(null);
                contactoListContacto = em.merge(contactoListContacto);
            }
            em.remove(club);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Club> findClubEntities() {
        return findClubEntities(true, -1, -1);
    }

    public List<Club> findClubEntities(int maxResults, int firstResult) {
        return findClubEntities(false, maxResults, firstResult);
    }

    private List<Club> findClubEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Club.class));
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

    public Club findClub(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Club.class, id);
        } finally {
            em.close();
        }
    }

    public int getClubCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Club> rt = cq.from(Club.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
