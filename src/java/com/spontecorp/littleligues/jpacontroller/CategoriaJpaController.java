/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.liga.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.torneo.Partido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) {
        if (categoria.getPartidoList() == null) {
            categoria.setPartidoList(new ArrayList<Partido>());
        }
        if (categoria.getEquipoList() == null) {
            categoria.setEquipoList(new ArrayList<Equipo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Partido> attachedPartidoList = new ArrayList<Partido>();
            for (Partido partidoListPartidoToAttach : categoria.getPartidoList()) {
                partidoListPartidoToAttach = em.getReference(partidoListPartidoToAttach.getClass(), partidoListPartidoToAttach.getId());
                attachedPartidoList.add(partidoListPartidoToAttach);
            }
            categoria.setPartidoList(attachedPartidoList);
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : categoria.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getId());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            categoria.setEquipoList(attachedEquipoList);
            em.persist(categoria);
            for (Partido partidoListPartido : categoria.getPartidoList()) {
                Categoria oldCategoriaIdOfPartidoListPartido = partidoListPartido.getCategoriaId();
                partidoListPartido.setCategoriaId(categoria);
                partidoListPartido = em.merge(partidoListPartido);
                if (oldCategoriaIdOfPartidoListPartido != null) {
                    oldCategoriaIdOfPartidoListPartido.getPartidoList().remove(partidoListPartido);
                    oldCategoriaIdOfPartidoListPartido = em.merge(oldCategoriaIdOfPartidoListPartido);
                }
            }
            for (Equipo equipoListEquipo : categoria.getEquipoList()) {
                Categoria oldCategoriaIdOfEquipoListEquipo = equipoListEquipo.getCategoriaId();
                equipoListEquipo.setCategoriaId(categoria);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldCategoriaIdOfEquipoListEquipo != null) {
                    oldCategoriaIdOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldCategoriaIdOfEquipoListEquipo = em.merge(oldCategoriaIdOfEquipoListEquipo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getId());
            List<Partido> partidoListOld = persistentCategoria.getPartidoList();
            List<Partido> partidoListNew = categoria.getPartidoList();
            List<Equipo> equipoListOld = persistentCategoria.getEquipoList();
            List<Equipo> equipoListNew = categoria.getEquipoList();
            List<String> illegalOrphanMessages = null;
            for (Partido partidoListOldPartido : partidoListOld) {
                if (!partidoListNew.contains(partidoListOldPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Partido " + partidoListOldPartido + " since its categoriaId field is not nullable.");
                }
            }
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Equipo " + equipoListOldEquipo + " since its categoriaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Partido> attachedPartidoListNew = new ArrayList<Partido>();
            for (Partido partidoListNewPartidoToAttach : partidoListNew) {
                partidoListNewPartidoToAttach = em.getReference(partidoListNewPartidoToAttach.getClass(), partidoListNewPartidoToAttach.getId());
                attachedPartidoListNew.add(partidoListNewPartidoToAttach);
            }
            partidoListNew = attachedPartidoListNew;
            categoria.setPartidoList(partidoListNew);
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getId());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            categoria.setEquipoList(equipoListNew);
            categoria = em.merge(categoria);
            for (Partido partidoListNewPartido : partidoListNew) {
                if (!partidoListOld.contains(partidoListNewPartido)) {
                    Categoria oldCategoriaIdOfPartidoListNewPartido = partidoListNewPartido.getCategoriaId();
                    partidoListNewPartido.setCategoriaId(categoria);
                    partidoListNewPartido = em.merge(partidoListNewPartido);
                    if (oldCategoriaIdOfPartidoListNewPartido != null && !oldCategoriaIdOfPartidoListNewPartido.equals(categoria)) {
                        oldCategoriaIdOfPartidoListNewPartido.getPartidoList().remove(partidoListNewPartido);
                        oldCategoriaIdOfPartidoListNewPartido = em.merge(oldCategoriaIdOfPartidoListNewPartido);
                    }
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    Categoria oldCategoriaIdOfEquipoListNewEquipo = equipoListNewEquipo.getCategoriaId();
                    equipoListNewEquipo.setCategoriaId(categoria);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldCategoriaIdOfEquipoListNewEquipo != null && !oldCategoriaIdOfEquipoListNewEquipo.equals(categoria)) {
                        oldCategoriaIdOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldCategoriaIdOfEquipoListNewEquipo = em.merge(oldCategoriaIdOfEquipoListNewEquipo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getId();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Partido> partidoListOrphanCheck = categoria.getPartidoList();
            for (Partido partidoListOrphanCheckPartido : partidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Partido " + partidoListOrphanCheckPartido + " in its partidoList field has a non-nullable categoriaId field.");
            }
            List<Equipo> equipoListOrphanCheck = categoria.getEquipoList();
            for (Equipo equipoListOrphanCheckEquipo : equipoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Equipo " + equipoListOrphanCheckEquipo + " in its equipoList field has a non-nullable categoriaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
