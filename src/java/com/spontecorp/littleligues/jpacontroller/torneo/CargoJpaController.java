/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.torneo.Cargo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.torneo.Staff;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class CargoJpaController implements Serializable {

    public CargoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cargo cargo) {
        if (cargo.getStaffList() == null) {
            cargo.setStaffList(new ArrayList<Staff>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Staff> attachedStaffList = new ArrayList<Staff>();
            for (Staff staffListStaffToAttach : cargo.getStaffList()) {
                staffListStaffToAttach = em.getReference(staffListStaffToAttach.getClass(), staffListStaffToAttach.getId());
                attachedStaffList.add(staffListStaffToAttach);
            }
            cargo.setStaffList(attachedStaffList);
            em.persist(cargo);
            for (Staff staffListStaff : cargo.getStaffList()) {
                Cargo oldCargoIdOfStaffListStaff = staffListStaff.getCargoId();
                staffListStaff.setCargoId(cargo);
                staffListStaff = em.merge(staffListStaff);
                if (oldCargoIdOfStaffListStaff != null) {
                    oldCargoIdOfStaffListStaff.getStaffList().remove(staffListStaff);
                    oldCargoIdOfStaffListStaff = em.merge(oldCargoIdOfStaffListStaff);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cargo cargo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cargo persistentCargo = em.find(Cargo.class, cargo.getId());
            List<Staff> staffListOld = persistentCargo.getStaffList();
            List<Staff> staffListNew = cargo.getStaffList();
            List<String> illegalOrphanMessages = null;
            for (Staff staffListOldStaff : staffListOld) {
                if (!staffListNew.contains(staffListOldStaff)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Staff " + staffListOldStaff + " since its cargoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Staff> attachedStaffListNew = new ArrayList<Staff>();
            for (Staff staffListNewStaffToAttach : staffListNew) {
                staffListNewStaffToAttach = em.getReference(staffListNewStaffToAttach.getClass(), staffListNewStaffToAttach.getId());
                attachedStaffListNew.add(staffListNewStaffToAttach);
            }
            staffListNew = attachedStaffListNew;
            cargo.setStaffList(staffListNew);
            cargo = em.merge(cargo);
            for (Staff staffListNewStaff : staffListNew) {
                if (!staffListOld.contains(staffListNewStaff)) {
                    Cargo oldCargoIdOfStaffListNewStaff = staffListNewStaff.getCargoId();
                    staffListNewStaff.setCargoId(cargo);
                    staffListNewStaff = em.merge(staffListNewStaff);
                    if (oldCargoIdOfStaffListNewStaff != null && !oldCargoIdOfStaffListNewStaff.equals(cargo)) {
                        oldCargoIdOfStaffListNewStaff.getStaffList().remove(staffListNewStaff);
                        oldCargoIdOfStaffListNewStaff = em.merge(oldCargoIdOfStaffListNewStaff);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cargo.getId();
                if (findCargo(id) == null) {
                    throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.");
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
            Cargo cargo;
            try {
                cargo = em.getReference(Cargo.class, id);
                cargo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Staff> staffListOrphanCheck = cargo.getStaffList();
            for (Staff staffListOrphanCheckStaff : staffListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cargo (" + cargo + ") cannot be destroyed since the Staff " + staffListOrphanCheckStaff + " in its staffList field has a non-nullable cargoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cargo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cargo> findCargoEntities() {
        return findCargoEntities(true, -1, -1);
    }

    public List<Cargo> findCargoEntities(int maxResults, int firstResult) {
        return findCargoEntities(false, maxResults, firstResult);
    }

    private List<Cargo> findCargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cargo.class));
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

    public Cargo findCargo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cargo> rt = cq.from(Cargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
