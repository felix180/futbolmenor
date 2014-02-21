/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller.torneo;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.model.torneo.ArbitroPartido;
import com.spontecorp.littleligues.model.torneo.ConvocatoriaEq;
import com.spontecorp.littleligues.model.torneo.Evento;
import com.spontecorp.littleligues.model.torneo.Jornada;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Partido;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jgcastillo
 */
public class PartidoJpaController implements Serializable {

    public PartidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Partido partido) {
        if (partido.getEventoList() == null) {
            partido.setEventoList(new ArrayList<Evento>());
        }
        if (partido.getConvocatoriaEqList() == null) {
            partido.setConvocatoriaEqList(new ArrayList<ConvocatoriaEq>());
        }
        if (partido.getArbitroPartidoList() == null) {
            partido.setArbitroPartidoList(new ArrayList<ArbitroPartido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llave llaveId = partido.getLlaveId();
            if (llaveId != null) {
                llaveId = em.getReference(llaveId.getClass(), llaveId.getId());
                partido.setLlaveId(llaveId);
            }
            Jornada jornadaId = partido.getJornadaId();
            if (jornadaId != null) {
                jornadaId = em.getReference(jornadaId.getClass(), jornadaId.getId());
                partido.setJornadaId(jornadaId);
            }
            Equipo equipo2Id = partido.getEquipo2Id();
            if (equipo2Id != null) {
                equipo2Id = em.getReference(equipo2Id.getClass(), equipo2Id.getId());
                partido.setEquipo2Id(equipo2Id);
            }
            Equipo equipo1Id = partido.getEquipo1Id();
            if (equipo1Id != null) {
                equipo1Id = em.getReference(equipo1Id.getClass(), equipo1Id.getId());
                partido.setEquipo1Id(equipo1Id);
            }
            Categoria categoriaId = partido.getCategoriaId();
            if (categoriaId != null) {
                categoriaId = em.getReference(categoriaId.getClass(), categoriaId.getId());
                partido.setCategoriaId(categoriaId);
            }
            Cancha canchaId = partido.getCanchaId();
            if (canchaId != null) {
                canchaId = em.getReference(canchaId.getClass(), canchaId.getId());
                partido.setCanchaId(canchaId);
            }
            List<Evento> attachedEventoList = new ArrayList<Evento>();
            for (Evento eventoListEventoToAttach : partido.getEventoList()) {
                eventoListEventoToAttach = em.getReference(eventoListEventoToAttach.getClass(), eventoListEventoToAttach.getId());
                attachedEventoList.add(eventoListEventoToAttach);
            }
            partido.setEventoList(attachedEventoList);
            List<ConvocatoriaEq> attachedConvocatoriaEqList = new ArrayList<ConvocatoriaEq>();
            for (ConvocatoriaEq convocatoriaEqListConvocatoriaEqToAttach : partido.getConvocatoriaEqList()) {
                convocatoriaEqListConvocatoriaEqToAttach = em.getReference(convocatoriaEqListConvocatoriaEqToAttach.getClass(), convocatoriaEqListConvocatoriaEqToAttach.getId());
                attachedConvocatoriaEqList.add(convocatoriaEqListConvocatoriaEqToAttach);
            }
            partido.setConvocatoriaEqList(attachedConvocatoriaEqList);
            List<ArbitroPartido> attachedArbitroPartidoList = new ArrayList<ArbitroPartido>();
            for (ArbitroPartido arbitroPartidoListArbitroPartidoToAttach : partido.getArbitroPartidoList()) {
                arbitroPartidoListArbitroPartidoToAttach = em.getReference(arbitroPartidoListArbitroPartidoToAttach.getClass(), arbitroPartidoListArbitroPartidoToAttach.getId());
                attachedArbitroPartidoList.add(arbitroPartidoListArbitroPartidoToAttach);
            }
            partido.setArbitroPartidoList(attachedArbitroPartidoList);
            em.persist(partido);
            if (llaveId != null) {
                llaveId.getPartidoList().add(partido);
                llaveId = em.merge(llaveId);
            }
            if (jornadaId != null) {
                jornadaId.getPartidoList().add(partido);
                jornadaId = em.merge(jornadaId);
            }
            if (equipo2Id != null) {
                equipo2Id.getPartidoList().add(partido);
                equipo2Id = em.merge(equipo2Id);
            }
            if (equipo1Id != null) {
                equipo1Id.getPartidoList().add(partido);
                equipo1Id = em.merge(equipo1Id);
            }
            if (categoriaId != null) {
                categoriaId.getPartidoList().add(partido);
                categoriaId = em.merge(categoriaId);
            }
            if (canchaId != null) {
                canchaId.getPartidoList().add(partido);
                canchaId = em.merge(canchaId);
            }
            for (Evento eventoListEvento : partido.getEventoList()) {
                Partido oldPartidoIdOfEventoListEvento = eventoListEvento.getPartidoId();
                eventoListEvento.setPartidoId(partido);
                eventoListEvento = em.merge(eventoListEvento);
                if (oldPartidoIdOfEventoListEvento != null) {
                    oldPartidoIdOfEventoListEvento.getEventoList().remove(eventoListEvento);
                    oldPartidoIdOfEventoListEvento = em.merge(oldPartidoIdOfEventoListEvento);
                }
            }
            for (ConvocatoriaEq convocatoriaEqListConvocatoriaEq : partido.getConvocatoriaEqList()) {
                Partido oldPartidoIdOfConvocatoriaEqListConvocatoriaEq = convocatoriaEqListConvocatoriaEq.getPartidoId();
                convocatoriaEqListConvocatoriaEq.setPartidoId(partido);
                convocatoriaEqListConvocatoriaEq = em.merge(convocatoriaEqListConvocatoriaEq);
                if (oldPartidoIdOfConvocatoriaEqListConvocatoriaEq != null) {
                    oldPartidoIdOfConvocatoriaEqListConvocatoriaEq.getConvocatoriaEqList().remove(convocatoriaEqListConvocatoriaEq);
                    oldPartidoIdOfConvocatoriaEqListConvocatoriaEq = em.merge(oldPartidoIdOfConvocatoriaEqListConvocatoriaEq);
                }
            }
            for (ArbitroPartido arbitroPartidoListArbitroPartido : partido.getArbitroPartidoList()) {
                Partido oldPartidoIdOfArbitroPartidoListArbitroPartido = arbitroPartidoListArbitroPartido.getPartidoId();
                arbitroPartidoListArbitroPartido.setPartidoId(partido);
                arbitroPartidoListArbitroPartido = em.merge(arbitroPartidoListArbitroPartido);
                if (oldPartidoIdOfArbitroPartidoListArbitroPartido != null) {
                    oldPartidoIdOfArbitroPartidoListArbitroPartido.getArbitroPartidoList().remove(arbitroPartidoListArbitroPartido);
                    oldPartidoIdOfArbitroPartidoListArbitroPartido = em.merge(oldPartidoIdOfArbitroPartidoListArbitroPartido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Partido partido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partido persistentPartido = em.find(Partido.class, partido.getId());
            Llave llaveIdOld = persistentPartido.getLlaveId();
            Llave llaveIdNew = partido.getLlaveId();
            Jornada jornadaIdOld = persistentPartido.getJornadaId();
            Jornada jornadaIdNew = partido.getJornadaId();
            Equipo equipo2IdOld = persistentPartido.getEquipo2Id();
            Equipo equipo2IdNew = partido.getEquipo2Id();
            Equipo equipo1IdOld = persistentPartido.getEquipo1Id();
            Equipo equipo1IdNew = partido.getEquipo1Id();
            Categoria categoriaIdOld = persistentPartido.getCategoriaId();
            Categoria categoriaIdNew = partido.getCategoriaId();
            Cancha canchaIdOld = persistentPartido.getCanchaId();
            Cancha canchaIdNew = partido.getCanchaId();
            List<Evento> eventoListOld = persistentPartido.getEventoList();
            List<Evento> eventoListNew = partido.getEventoList();
            List<ConvocatoriaEq> convocatoriaEqListOld = persistentPartido.getConvocatoriaEqList();
            List<ConvocatoriaEq> convocatoriaEqListNew = partido.getConvocatoriaEqList();
            List<ArbitroPartido> arbitroPartidoListOld = persistentPartido.getArbitroPartidoList();
            List<ArbitroPartido> arbitroPartidoListNew = partido.getArbitroPartidoList();
            List<String> illegalOrphanMessages = null;
            for (Evento eventoListOldEvento : eventoListOld) {
                if (!eventoListNew.contains(eventoListOldEvento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Evento " + eventoListOldEvento + " since its partidoId field is not nullable.");
                }
            }
            for (ArbitroPartido arbitroPartidoListOldArbitroPartido : arbitroPartidoListOld) {
                if (!arbitroPartidoListNew.contains(arbitroPartidoListOldArbitroPartido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArbitroPartido " + arbitroPartidoListOldArbitroPartido + " since its partidoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (llaveIdNew != null) {
                llaveIdNew = em.getReference(llaveIdNew.getClass(), llaveIdNew.getId());
                partido.setLlaveId(llaveIdNew);
            }
            if (jornadaIdNew != null) {
                jornadaIdNew = em.getReference(jornadaIdNew.getClass(), jornadaIdNew.getId());
                partido.setJornadaId(jornadaIdNew);
            }
            if (equipo2IdNew != null) {
                equipo2IdNew = em.getReference(equipo2IdNew.getClass(), equipo2IdNew.getId());
                partido.setEquipo2Id(equipo2IdNew);
            }
            if (equipo1IdNew != null) {
                equipo1IdNew = em.getReference(equipo1IdNew.getClass(), equipo1IdNew.getId());
                partido.setEquipo1Id(equipo1IdNew);
            }
            if (categoriaIdNew != null) {
                categoriaIdNew = em.getReference(categoriaIdNew.getClass(), categoriaIdNew.getId());
                partido.setCategoriaId(categoriaIdNew);
            }
            if (canchaIdNew != null) {
                canchaIdNew = em.getReference(canchaIdNew.getClass(), canchaIdNew.getId());
                partido.setCanchaId(canchaIdNew);
            }
            List<Evento> attachedEventoListNew = new ArrayList<Evento>();
            for (Evento eventoListNewEventoToAttach : eventoListNew) {
                eventoListNewEventoToAttach = em.getReference(eventoListNewEventoToAttach.getClass(), eventoListNewEventoToAttach.getId());
                attachedEventoListNew.add(eventoListNewEventoToAttach);
            }
            eventoListNew = attachedEventoListNew;
            partido.setEventoList(eventoListNew);
            List<ConvocatoriaEq> attachedConvocatoriaEqListNew = new ArrayList<ConvocatoriaEq>();
            for (ConvocatoriaEq convocatoriaEqListNewConvocatoriaEqToAttach : convocatoriaEqListNew) {
                convocatoriaEqListNewConvocatoriaEqToAttach = em.getReference(convocatoriaEqListNewConvocatoriaEqToAttach.getClass(), convocatoriaEqListNewConvocatoriaEqToAttach.getId());
                attachedConvocatoriaEqListNew.add(convocatoriaEqListNewConvocatoriaEqToAttach);
            }
            convocatoriaEqListNew = attachedConvocatoriaEqListNew;
            partido.setConvocatoriaEqList(convocatoriaEqListNew);
            List<ArbitroPartido> attachedArbitroPartidoListNew = new ArrayList<ArbitroPartido>();
            for (ArbitroPartido arbitroPartidoListNewArbitroPartidoToAttach : arbitroPartidoListNew) {
                arbitroPartidoListNewArbitroPartidoToAttach = em.getReference(arbitroPartidoListNewArbitroPartidoToAttach.getClass(), arbitroPartidoListNewArbitroPartidoToAttach.getId());
                attachedArbitroPartidoListNew.add(arbitroPartidoListNewArbitroPartidoToAttach);
            }
            arbitroPartidoListNew = attachedArbitroPartidoListNew;
            partido.setArbitroPartidoList(arbitroPartidoListNew);
            partido = em.merge(partido);
            if (llaveIdOld != null && !llaveIdOld.equals(llaveIdNew)) {
                llaveIdOld.getPartidoList().remove(partido);
                llaveIdOld = em.merge(llaveIdOld);
            }
            if (llaveIdNew != null && !llaveIdNew.equals(llaveIdOld)) {
                llaveIdNew.getPartidoList().add(partido);
                llaveIdNew = em.merge(llaveIdNew);
            }
            if (jornadaIdOld != null && !jornadaIdOld.equals(jornadaIdNew)) {
                jornadaIdOld.getPartidoList().remove(partido);
                jornadaIdOld = em.merge(jornadaIdOld);
            }
            if (jornadaIdNew != null && !jornadaIdNew.equals(jornadaIdOld)) {
                jornadaIdNew.getPartidoList().add(partido);
                jornadaIdNew = em.merge(jornadaIdNew);
            }
            if (equipo2IdOld != null && !equipo2IdOld.equals(equipo2IdNew)) {
                equipo2IdOld.getPartidoList().remove(partido);
                equipo2IdOld = em.merge(equipo2IdOld);
            }
            if (equipo2IdNew != null && !equipo2IdNew.equals(equipo2IdOld)) {
                equipo2IdNew.getPartidoList().add(partido);
                equipo2IdNew = em.merge(equipo2IdNew);
            }
            if (equipo1IdOld != null && !equipo1IdOld.equals(equipo1IdNew)) {
                equipo1IdOld.getPartidoList().remove(partido);
                equipo1IdOld = em.merge(equipo1IdOld);
            }
            if (equipo1IdNew != null && !equipo1IdNew.equals(equipo1IdOld)) {
                equipo1IdNew.getPartidoList().add(partido);
                equipo1IdNew = em.merge(equipo1IdNew);
            }
            if (categoriaIdOld != null && !categoriaIdOld.equals(categoriaIdNew)) {
                categoriaIdOld.getPartidoList().remove(partido);
                categoriaIdOld = em.merge(categoriaIdOld);
            }
            if (categoriaIdNew != null && !categoriaIdNew.equals(categoriaIdOld)) {
                categoriaIdNew.getPartidoList().add(partido);
                categoriaIdNew = em.merge(categoriaIdNew);
            }
            if (canchaIdOld != null && !canchaIdOld.equals(canchaIdNew)) {
                canchaIdOld.getPartidoList().remove(partido);
                canchaIdOld = em.merge(canchaIdOld);
            }
            if (canchaIdNew != null && !canchaIdNew.equals(canchaIdOld)) {
                canchaIdNew.getPartidoList().add(partido);
                canchaIdNew = em.merge(canchaIdNew);
            }
            for (Evento eventoListNewEvento : eventoListNew) {
                if (!eventoListOld.contains(eventoListNewEvento)) {
                    Partido oldPartidoIdOfEventoListNewEvento = eventoListNewEvento.getPartidoId();
                    eventoListNewEvento.setPartidoId(partido);
                    eventoListNewEvento = em.merge(eventoListNewEvento);
                    if (oldPartidoIdOfEventoListNewEvento != null && !oldPartidoIdOfEventoListNewEvento.equals(partido)) {
                        oldPartidoIdOfEventoListNewEvento.getEventoList().remove(eventoListNewEvento);
                        oldPartidoIdOfEventoListNewEvento = em.merge(oldPartidoIdOfEventoListNewEvento);
                    }
                }
            }
            for (ConvocatoriaEq convocatoriaEqListOldConvocatoriaEq : convocatoriaEqListOld) {
                if (!convocatoriaEqListNew.contains(convocatoriaEqListOldConvocatoriaEq)) {
                    convocatoriaEqListOldConvocatoriaEq.setPartidoId(null);
                    convocatoriaEqListOldConvocatoriaEq = em.merge(convocatoriaEqListOldConvocatoriaEq);
                }
            }
            for (ConvocatoriaEq convocatoriaEqListNewConvocatoriaEq : convocatoriaEqListNew) {
                if (!convocatoriaEqListOld.contains(convocatoriaEqListNewConvocatoriaEq)) {
                    Partido oldPartidoIdOfConvocatoriaEqListNewConvocatoriaEq = convocatoriaEqListNewConvocatoriaEq.getPartidoId();
                    convocatoriaEqListNewConvocatoriaEq.setPartidoId(partido);
                    convocatoriaEqListNewConvocatoriaEq = em.merge(convocatoriaEqListNewConvocatoriaEq);
                    if (oldPartidoIdOfConvocatoriaEqListNewConvocatoriaEq != null && !oldPartidoIdOfConvocatoriaEqListNewConvocatoriaEq.equals(partido)) {
                        oldPartidoIdOfConvocatoriaEqListNewConvocatoriaEq.getConvocatoriaEqList().remove(convocatoriaEqListNewConvocatoriaEq);
                        oldPartidoIdOfConvocatoriaEqListNewConvocatoriaEq = em.merge(oldPartidoIdOfConvocatoriaEqListNewConvocatoriaEq);
                    }
                }
            }
            for (ArbitroPartido arbitroPartidoListNewArbitroPartido : arbitroPartidoListNew) {
                if (!arbitroPartidoListOld.contains(arbitroPartidoListNewArbitroPartido)) {
                    Partido oldPartidoIdOfArbitroPartidoListNewArbitroPartido = arbitroPartidoListNewArbitroPartido.getPartidoId();
                    arbitroPartidoListNewArbitroPartido.setPartidoId(partido);
                    arbitroPartidoListNewArbitroPartido = em.merge(arbitroPartidoListNewArbitroPartido);
                    if (oldPartidoIdOfArbitroPartidoListNewArbitroPartido != null && !oldPartidoIdOfArbitroPartidoListNewArbitroPartido.equals(partido)) {
                        oldPartidoIdOfArbitroPartidoListNewArbitroPartido.getArbitroPartidoList().remove(arbitroPartidoListNewArbitroPartido);
                        oldPartidoIdOfArbitroPartidoListNewArbitroPartido = em.merge(oldPartidoIdOfArbitroPartidoListNewArbitroPartido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = partido.getId();
                if (findPartido(id) == null) {
                    throw new NonexistentEntityException("The partido with id " + id + " no longer exists.");
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
            Partido partido;
            try {
                partido = em.getReference(Partido.class, id);
                partido.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The partido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Evento> eventoListOrphanCheck = partido.getEventoList();
            for (Evento eventoListOrphanCheckEvento : eventoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the Evento " + eventoListOrphanCheckEvento + " in its eventoList field has a non-nullable partidoId field.");
            }
            List<ArbitroPartido> arbitroPartidoListOrphanCheck = partido.getArbitroPartidoList();
            for (ArbitroPartido arbitroPartidoListOrphanCheckArbitroPartido : arbitroPartidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Partido (" + partido + ") cannot be destroyed since the ArbitroPartido " + arbitroPartidoListOrphanCheckArbitroPartido + " in its arbitroPartidoList field has a non-nullable partidoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Llave llaveId = partido.getLlaveId();
            if (llaveId != null) {
                llaveId.getPartidoList().remove(partido);
                llaveId = em.merge(llaveId);
            }
            Jornada jornadaId = partido.getJornadaId();
            if (jornadaId != null) {
                jornadaId.getPartidoList().remove(partido);
                jornadaId = em.merge(jornadaId);
            }
            Equipo equipo2Id = partido.getEquipo2Id();
            if (equipo2Id != null) {
                equipo2Id.getPartidoList().remove(partido);
                equipo2Id = em.merge(equipo2Id);
            }
            Equipo equipo1Id = partido.getEquipo1Id();
            if (equipo1Id != null) {
                equipo1Id.getPartidoList().remove(partido);
                equipo1Id = em.merge(equipo1Id);
            }
            Categoria categoriaId = partido.getCategoriaId();
            if (categoriaId != null) {
                categoriaId.getPartidoList().remove(partido);
                categoriaId = em.merge(categoriaId);
            }
            Cancha canchaId = partido.getCanchaId();
            if (canchaId != null) {
                canchaId.getPartidoList().remove(partido);
                canchaId = em.merge(canchaId);
            }
            List<ConvocatoriaEq> convocatoriaEqList = partido.getConvocatoriaEqList();
            for (ConvocatoriaEq convocatoriaEqListConvocatoriaEq : convocatoriaEqList) {
                convocatoriaEqListConvocatoriaEq.setPartidoId(null);
                convocatoriaEqListConvocatoriaEq = em.merge(convocatoriaEqListConvocatoriaEq);
            }
            em.remove(partido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Partido> findPartidoEntities() {
        return findPartidoEntities(true, -1, -1);
    }

    public List<Partido> findPartidoEntities(int maxResults, int firstResult) {
        return findPartidoEntities(false, maxResults, firstResult);
    }

    private List<Partido> findPartidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Partido.class));
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

    public Partido findPartido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Partido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPartidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Partido> rt = cq.from(Partido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
