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
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.torneo.Cargo;
import com.spontecorp.littleligues.model.torneo.Convocado;
import com.spontecorp.littleligues.model.torneo.Staff;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class StaffJpaController implements Serializable {

    public StaffJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Staff staff) {
        if (staff.getConvocadoList() == null) {
            staff.setConvocadoList(new ArrayList<Convocado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo equipoId = staff.getEquipoId();
            if (equipoId != null) {
                equipoId = em.getReference(equipoId.getClass(), equipoId.getId());
                staff.setEquipoId(equipoId);
            }
            Cargo cargoId = staff.getCargoId();
            if (cargoId != null) {
                cargoId = em.getReference(cargoId.getClass(), cargoId.getId());
                staff.setCargoId(cargoId);
            }
            List<Convocado> attachedConvocadoList = new ArrayList<Convocado>();
            for (Convocado convocadoListConvocadoToAttach : staff.getConvocadoList()) {
                convocadoListConvocadoToAttach = em.getReference(convocadoListConvocadoToAttach.getClass(), convocadoListConvocadoToAttach.getId());
                attachedConvocadoList.add(convocadoListConvocadoToAttach);
            }
            staff.setConvocadoList(attachedConvocadoList);
            em.persist(staff);
            if (equipoId != null) {
                equipoId.getStaffList().add(staff);
                equipoId = em.merge(equipoId);
            }
            if (cargoId != null) {
                cargoId.getStaffList().add(staff);
                cargoId = em.merge(cargoId);
            }
            for (Convocado convocadoListConvocado : staff.getConvocadoList()) {
                Staff oldStaffIdOfConvocadoListConvocado = convocadoListConvocado.getStaffId();
                convocadoListConvocado.setStaffId(staff);
                convocadoListConvocado = em.merge(convocadoListConvocado);
                if (oldStaffIdOfConvocadoListConvocado != null) {
                    oldStaffIdOfConvocadoListConvocado.getConvocadoList().remove(convocadoListConvocado);
                    oldStaffIdOfConvocadoListConvocado = em.merge(oldStaffIdOfConvocadoListConvocado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Staff staff) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Staff persistentStaff = em.find(Staff.class, staff.getId());
            Equipo equipoIdOld = persistentStaff.getEquipoId();
            Equipo equipoIdNew = staff.getEquipoId();
            Cargo cargoIdOld = persistentStaff.getCargoId();
            Cargo cargoIdNew = staff.getCargoId();
            List<Convocado> convocadoListOld = persistentStaff.getConvocadoList();
            List<Convocado> convocadoListNew = staff.getConvocadoList();
            if (equipoIdNew != null) {
                equipoIdNew = em.getReference(equipoIdNew.getClass(), equipoIdNew.getId());
                staff.setEquipoId(equipoIdNew);
            }
            if (cargoIdNew != null) {
                cargoIdNew = em.getReference(cargoIdNew.getClass(), cargoIdNew.getId());
                staff.setCargoId(cargoIdNew);
            }
            List<Convocado> attachedConvocadoListNew = new ArrayList<Convocado>();
            for (Convocado convocadoListNewConvocadoToAttach : convocadoListNew) {
                convocadoListNewConvocadoToAttach = em.getReference(convocadoListNewConvocadoToAttach.getClass(), convocadoListNewConvocadoToAttach.getId());
                attachedConvocadoListNew.add(convocadoListNewConvocadoToAttach);
            }
            convocadoListNew = attachedConvocadoListNew;
            staff.setConvocadoList(convocadoListNew);
            staff = em.merge(staff);
            if (equipoIdOld != null && !equipoIdOld.equals(equipoIdNew)) {
                equipoIdOld.getStaffList().remove(staff);
                equipoIdOld = em.merge(equipoIdOld);
            }
            if (equipoIdNew != null && !equipoIdNew.equals(equipoIdOld)) {
                equipoIdNew.getStaffList().add(staff);
                equipoIdNew = em.merge(equipoIdNew);
            }
            if (cargoIdOld != null && !cargoIdOld.equals(cargoIdNew)) {
                cargoIdOld.getStaffList().remove(staff);
                cargoIdOld = em.merge(cargoIdOld);
            }
            if (cargoIdNew != null && !cargoIdNew.equals(cargoIdOld)) {
                cargoIdNew.getStaffList().add(staff);
                cargoIdNew = em.merge(cargoIdNew);
            }
            for (Convocado convocadoListOldConvocado : convocadoListOld) {
                if (!convocadoListNew.contains(convocadoListOldConvocado)) {
                    convocadoListOldConvocado.setStaffId(null);
                    convocadoListOldConvocado = em.merge(convocadoListOldConvocado);
                }
            }
            for (Convocado convocadoListNewConvocado : convocadoListNew) {
                if (!convocadoListOld.contains(convocadoListNewConvocado)) {
                    Staff oldStaffIdOfConvocadoListNewConvocado = convocadoListNewConvocado.getStaffId();
                    convocadoListNewConvocado.setStaffId(staff);
                    convocadoListNewConvocado = em.merge(convocadoListNewConvocado);
                    if (oldStaffIdOfConvocadoListNewConvocado != null && !oldStaffIdOfConvocadoListNewConvocado.equals(staff)) {
                        oldStaffIdOfConvocadoListNewConvocado.getConvocadoList().remove(convocadoListNewConvocado);
                        oldStaffIdOfConvocadoListNewConvocado = em.merge(oldStaffIdOfConvocadoListNewConvocado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = staff.getId();
                if (findStaff(id) == null) {
                    throw new NonexistentEntityException("The staff with id " + id + " no longer exists.");
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
            Staff staff;
            try {
                staff = em.getReference(Staff.class, id);
                staff.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The staff with id " + id + " no longer exists.", enfe);
            }
            Equipo equipoId = staff.getEquipoId();
            if (equipoId != null) {
                equipoId.getStaffList().remove(staff);
                equipoId = em.merge(equipoId);
            }
            Cargo cargoId = staff.getCargoId();
            if (cargoId != null) {
                cargoId.getStaffList().remove(staff);
                cargoId = em.merge(cargoId);
            }
            List<Convocado> convocadoList = staff.getConvocadoList();
            for (Convocado convocadoListConvocado : convocadoList) {
                convocadoListConvocado.setStaffId(null);
                convocadoListConvocado = em.merge(convocadoListConvocado);
            }
            em.remove(staff);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Staff> findStaffEntities() {
        return findStaffEntities(true, -1, -1);
    }

    public List<Staff> findStaffEntities(int maxResults, int firstResult) {
        return findStaffEntities(false, maxResults, firstResult);
    }

    private List<Staff> findStaffEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Staff.class));
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

    public Staff findStaff(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Staff.class, id);
        } finally {
            em.close();
        }
    }

    public int getStaffCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Staff> rt = cq.from(Staff.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
