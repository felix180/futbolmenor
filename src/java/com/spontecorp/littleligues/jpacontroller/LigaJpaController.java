/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.Noticia;
import com.spontecorp.littleligues.model.Noticia_;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Equipo;
import java.util.ArrayList;
import java.util.List;
import com.spontecorp.littleligues.model.liga.Contacto;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.model.torneo.Temporada;
import com.spontecorp.littleligues.model.torneo.Temporada_;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author jgcastillo
 */
public class LigaJpaController implements Serializable {

    public LigaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Liga liga) {
        if (liga.getEquipoList() == null) {
            liga.setEquipoList(new ArrayList<Equipo>());
        }
        if (liga.getContactoList() == null) {
            liga.setContactoList(new ArrayList<Contacto>());
        }
        if (liga.getTemporadaList() == null) {
            liga.setTemporadaList(new ArrayList<Temporada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccionId = liga.getDireccionId();
            if (direccionId != null) {
                direccionId = em.getReference(direccionId.getClass(), direccionId.getId());
                liga.setDireccionId(direccionId);
            }
            List<Equipo> attachedEquipoList = new ArrayList<Equipo>();
            for (Equipo equipoListEquipoToAttach : liga.getEquipoList()) {
                equipoListEquipoToAttach = em.getReference(equipoListEquipoToAttach.getClass(), equipoListEquipoToAttach.getId());
                attachedEquipoList.add(equipoListEquipoToAttach);
            }
            liga.setEquipoList(attachedEquipoList);
            List<Contacto> attachedContactoList = new ArrayList<Contacto>();
            for (Contacto contactoListContactoToAttach : liga.getContactoList()) {
                contactoListContactoToAttach = em.getReference(contactoListContactoToAttach.getClass(), contactoListContactoToAttach.getId());
                attachedContactoList.add(contactoListContactoToAttach);
            }
            liga.setContactoList(attachedContactoList);
            List<Temporada> attachedTemporadaList = new ArrayList<Temporada>();
            for (Temporada temporadaListTemporadaToAttach : liga.getTemporadaList()) {
                temporadaListTemporadaToAttach = em.getReference(temporadaListTemporadaToAttach.getClass(), temporadaListTemporadaToAttach.getId());
                attachedTemporadaList.add(temporadaListTemporadaToAttach);
            }
            liga.setTemporadaList(attachedTemporadaList);
            em.persist(liga);
            if (direccionId != null) {
                direccionId.getLigaList().add(liga);
                direccionId = em.merge(direccionId);
            }
            for (Equipo equipoListEquipo : liga.getEquipoList()) {
                Liga oldLigaIdOfEquipoListEquipo = equipoListEquipo.getLigaId();
                equipoListEquipo.setLigaId(liga);
                equipoListEquipo = em.merge(equipoListEquipo);
                if (oldLigaIdOfEquipoListEquipo != null) {
                    oldLigaIdOfEquipoListEquipo.getEquipoList().remove(equipoListEquipo);
                    oldLigaIdOfEquipoListEquipo = em.merge(oldLigaIdOfEquipoListEquipo);
                }
            }
            for (Contacto contactoListContacto : liga.getContactoList()) {
                Liga oldLigaIdOfContactoListContacto = contactoListContacto.getLigaId();
                contactoListContacto.setLigaId(liga);
                contactoListContacto = em.merge(contactoListContacto);
                if (oldLigaIdOfContactoListContacto != null) {
                    oldLigaIdOfContactoListContacto.getContactoList().remove(contactoListContacto);
                    oldLigaIdOfContactoListContacto = em.merge(oldLigaIdOfContactoListContacto);
                }
            }
            for (Temporada temporadaListTemporada : liga.getTemporadaList()) {
                Liga oldLigaIdOfTemporadaListTemporada = temporadaListTemporada.getLigaId();
                temporadaListTemporada.setLigaId(liga);
                temporadaListTemporada = em.merge(temporadaListTemporada);
                if (oldLigaIdOfTemporadaListTemporada != null) {
                    oldLigaIdOfTemporadaListTemporada.getTemporadaList().remove(temporadaListTemporada);
                    oldLigaIdOfTemporadaListTemporada = em.merge(oldLigaIdOfTemporadaListTemporada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Liga liga) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Liga persistentLiga = em.find(Liga.class, liga.getId());
            Direccion direccionIdOld = persistentLiga.getDireccionId();
            Direccion direccionIdNew = liga.getDireccionId();
            List<Equipo> equipoListOld = persistentLiga.getEquipoList();
            List<Equipo> equipoListNew = liga.getEquipoList();
            List<Contacto> contactoListOld = persistentLiga.getContactoList();
            List<Contacto> contactoListNew = liga.getContactoList();
            List<Temporada> temporadaListOld = persistentLiga.getTemporadaList();
            List<Temporada> temporadaListNew = liga.getTemporadaList();
            List<String> illegalOrphanMessages = null;
            for (Temporada temporadaListOldTemporada : temporadaListOld) {
                if (!temporadaListNew.contains(temporadaListOldTemporada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Temporada " + temporadaListOldTemporada + " since its ligaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (direccionIdNew != null) {
                direccionIdNew = em.getReference(direccionIdNew.getClass(), direccionIdNew.getId());
                liga.setDireccionId(direccionIdNew);
            }
            List<Equipo> attachedEquipoListNew = new ArrayList<Equipo>();
            for (Equipo equipoListNewEquipoToAttach : equipoListNew) {
                equipoListNewEquipoToAttach = em.getReference(equipoListNewEquipoToAttach.getClass(), equipoListNewEquipoToAttach.getId());
                attachedEquipoListNew.add(equipoListNewEquipoToAttach);
            }
            equipoListNew = attachedEquipoListNew;
            liga.setEquipoList(equipoListNew);
            List<Contacto> attachedContactoListNew = new ArrayList<Contacto>();
            for (Contacto contactoListNewContactoToAttach : contactoListNew) {
                contactoListNewContactoToAttach = em.getReference(contactoListNewContactoToAttach.getClass(), contactoListNewContactoToAttach.getId());
                attachedContactoListNew.add(contactoListNewContactoToAttach);
            }
            contactoListNew = attachedContactoListNew;
            liga.setContactoList(contactoListNew);
            List<Temporada> attachedTemporadaListNew = new ArrayList<Temporada>();
            for (Temporada temporadaListNewTemporadaToAttach : temporadaListNew) {
                temporadaListNewTemporadaToAttach = em.getReference(temporadaListNewTemporadaToAttach.getClass(), temporadaListNewTemporadaToAttach.getId());
                attachedTemporadaListNew.add(temporadaListNewTemporadaToAttach);
            }
            temporadaListNew = attachedTemporadaListNew;
            liga.setTemporadaList(temporadaListNew);
            liga = em.merge(liga);
            if (direccionIdOld != null && !direccionIdOld.equals(direccionIdNew)) {
                direccionIdOld.getLigaList().remove(liga);
                direccionIdOld = em.merge(direccionIdOld);
            }
            if (direccionIdNew != null && !direccionIdNew.equals(direccionIdOld)) {
                direccionIdNew.getLigaList().add(liga);
                direccionIdNew = em.merge(direccionIdNew);
            }
            for (Equipo equipoListOldEquipo : equipoListOld) {
                if (!equipoListNew.contains(equipoListOldEquipo)) {
                    equipoListOldEquipo.setLigaId(null);
                    equipoListOldEquipo = em.merge(equipoListOldEquipo);
                }
            }
            for (Equipo equipoListNewEquipo : equipoListNew) {
                if (!equipoListOld.contains(equipoListNewEquipo)) {
                    Liga oldLigaIdOfEquipoListNewEquipo = equipoListNewEquipo.getLigaId();
                    equipoListNewEquipo.setLigaId(liga);
                    equipoListNewEquipo = em.merge(equipoListNewEquipo);
                    if (oldLigaIdOfEquipoListNewEquipo != null && !oldLigaIdOfEquipoListNewEquipo.equals(liga)) {
                        oldLigaIdOfEquipoListNewEquipo.getEquipoList().remove(equipoListNewEquipo);
                        oldLigaIdOfEquipoListNewEquipo = em.merge(oldLigaIdOfEquipoListNewEquipo);
                    }
                }
            }
            for (Contacto contactoListOldContacto : contactoListOld) {
                if (!contactoListNew.contains(contactoListOldContacto)) {
                    contactoListOldContacto.setLigaId(null);
                    contactoListOldContacto = em.merge(contactoListOldContacto);
                }
            }
            for (Contacto contactoListNewContacto : contactoListNew) {
                if (!contactoListOld.contains(contactoListNewContacto)) {
                    Liga oldLigaIdOfContactoListNewContacto = contactoListNewContacto.getLigaId();
                    contactoListNewContacto.setLigaId(liga);
                    contactoListNewContacto = em.merge(contactoListNewContacto);
                    if (oldLigaIdOfContactoListNewContacto != null && !oldLigaIdOfContactoListNewContacto.equals(liga)) {
                        oldLigaIdOfContactoListNewContacto.getContactoList().remove(contactoListNewContacto);
                        oldLigaIdOfContactoListNewContacto = em.merge(oldLigaIdOfContactoListNewContacto);
                    }
                }
            }
            for (Temporada temporadaListNewTemporada : temporadaListNew) {
                if (!temporadaListOld.contains(temporadaListNewTemporada)) {
                    Liga oldLigaIdOfTemporadaListNewTemporada = temporadaListNewTemporada.getLigaId();
                    temporadaListNewTemporada.setLigaId(liga);
                    temporadaListNewTemporada = em.merge(temporadaListNewTemporada);
                    if (oldLigaIdOfTemporadaListNewTemporada != null && !oldLigaIdOfTemporadaListNewTemporada.equals(liga)) {
                        oldLigaIdOfTemporadaListNewTemporada.getTemporadaList().remove(temporadaListNewTemporada);
                        oldLigaIdOfTemporadaListNewTemporada = em.merge(oldLigaIdOfTemporadaListNewTemporada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = liga.getId();
                if (findLiga(id) == null) {
                    throw new NonexistentEntityException("The liga with id " + id + " no longer exists.");
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
            Liga liga;
            try {
                liga = em.getReference(Liga.class, id);
                liga.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The liga with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Temporada> temporadaListOrphanCheck = liga.getTemporadaList();
            for (Temporada temporadaListOrphanCheckTemporada : temporadaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Liga (" + liga + ") cannot be destroyed since the Temporada " + temporadaListOrphanCheckTemporada + " in its temporadaList field has a non-nullable ligaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Direccion direccionId = liga.getDireccionId();
            if (direccionId != null) {
                direccionId.getLigaList().remove(liga);
                direccionId = em.merge(direccionId);
            }
            List<Equipo> equipoList = liga.getEquipoList();
            for (Equipo equipoListEquipo : equipoList) {
                equipoListEquipo.setLigaId(null);
                equipoListEquipo = em.merge(equipoListEquipo);
            }
            List<Contacto> contactoList = liga.getContactoList();
            for (Contacto contactoListContacto : contactoList) {
                contactoListContacto.setLigaId(null);
                contactoListContacto = em.merge(contactoListContacto);
            }
            em.remove(liga);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Liga> findLigaEntities() {
        return findLigaEntities(true, -1, -1);
    }

    public List<Liga> findLigaEntities(int maxResults, int firstResult) {
        return findLigaEntities(false, maxResults, firstResult);
    }

    private List<Liga> findLigaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Liga.class));
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

    public Liga findLiga(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Liga.class, id);
        } finally {
            em.close();
        }
    }

    public int getLigaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Liga> rt = cq.from(Liga.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Lista de las 8 primeras Noticias asociadas a la Liga Seleccionada
     *
     * @param liga
     * @return
     */
    public List<Noticia> findNoticiasLigaEntities(Liga liga) {
        return findNoticiasLiga(liga, false, 8, 0);
    }

    /**
     * Lista de Noticias asociadas a la Liga Seleccionada Ordenadas por fecha
     * Orden DESC
     *
     * @param liga
     * @param all
     * @param maxResults
     * @param firstResult
     * @return
     */
    private List<Noticia> findNoticiasLiga(Liga liga, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Noticia> noticias = null;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root noticia = cq.from(Noticia.class);
            cq.select(noticia);
            cq.where(cb.equal(noticia.get(Noticia_.ligaId), liga));
            cq.orderBy(cb.desc(noticia.get(Noticia_.fecha)));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            noticias = q.getResultList();
            return noticias;
        } finally {
            em.close();
        }
    }

    public Noticia findNoticia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Noticia.class, id);
        } finally {
            em.close();
        }
    }
    
    /**
     * Metodo para obtener la última temporada ordenada por fecha fin Orden DESC
     * @param liga
     * @return 
     */
    public Temporada findLastTemporadaLiga(Liga liga) {
        return findLastTemporadaLiga(liga, false, 1, 0);
    }

    /**
     * Metodo para obtener la última temporada ordenada por fecha fin Orden DESC
     * @param liga
     * @param all
     * @param maxResults
     * @param firstResult
     * @return 
     */
    private Temporada findLastTemporadaLiga(Liga liga, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        Temporada lastTemporada = null;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root temp = cq.from(Temporada.class);
            cq.select(temp);
            cq.where(cb.equal(temp.get(Temporada_.ligaId), liga));
            cq.orderBy(cb.desc(temp.get(Temporada_.fechaFin)));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            List<Temporada> temporada = q.getResultList();
            if(temporada.size() > 0){
                lastTemporada = temporada.get(0);
            }else{
                lastTemporada = null;
            }
            return lastTemporada;
        } finally {
            em.close();
        }
    }

}
