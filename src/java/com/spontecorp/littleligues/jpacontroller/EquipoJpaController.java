/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.jpacontroller;

import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.IllegalOrphanException;
import com.spontecorp.littleligues.jpacontroller.torneo.exceptions.NonexistentEntityException;
import com.spontecorp.littleligues.model.liga.*;
import com.spontecorp.littleligues.model.torneo.Clasificacion;
import com.spontecorp.littleligues.model.torneo.ConvocatoriaEq;
import com.spontecorp.littleligues.model.torneo.Jugador;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.model.torneo.Staff;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author jgcastillo
 */
public class EquipoJpaController implements Serializable {

    public EquipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Equipo equipo) {
        if (equipo.getJugadorList() == null) {
            equipo.setJugadorList(new ArrayList<Jugador>());
        }
        if (equipo.getStaffList() == null) {
            equipo.setStaffList(new ArrayList<Staff>());
        }
        if (equipo.getClasificacionList() == null) {
            equipo.setClasificacionList(new ArrayList<Clasificacion>());
        }
        if (equipo.getConvocatoriaEqList() == null) {
            equipo.setConvocatoriaEqList(new ArrayList<ConvocatoriaEq>());
        }
        if (equipo.getPartidoList() == null) {
            equipo.setPartidoList(new ArrayList<Partido>());
        }
        if (equipo.getPartidoList1() == null) {
            equipo.setPartidoList1(new ArrayList<Partido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Liga ligaId = equipo.getLigaId();
            if (ligaId != null) {
                ligaId = em.getReference(ligaId.getClass(), ligaId.getId());
                equipo.setLigaId(ligaId);
            }
            Club clubId = equipo.getClubId();
            if (clubId != null) {
                clubId = em.getReference(clubId.getClass(), clubId.getId());
                equipo.setClubId(clubId);
            }
            Categoria categoriaId = equipo.getCategoriaId();
            if (categoriaId != null) {
                categoriaId = em.getReference(categoriaId.getClass(), categoriaId.getId());
                equipo.setCategoriaId(categoriaId);
            }
            List<Jugador> attachedJugadorList = new ArrayList<Jugador>();
            for (Jugador jugadorListJugadorToAttach : equipo.getJugadorList()) {
                jugadorListJugadorToAttach = em.getReference(jugadorListJugadorToAttach.getClass(), jugadorListJugadorToAttach.getId());
                attachedJugadorList.add(jugadorListJugadorToAttach);
            }
            equipo.setJugadorList(attachedJugadorList);
            List<Staff> attachedStaffList = new ArrayList<Staff>();
            for (Staff staffListStaffToAttach : equipo.getStaffList()) {
                staffListStaffToAttach = em.getReference(staffListStaffToAttach.getClass(), staffListStaffToAttach.getId());
                attachedStaffList.add(staffListStaffToAttach);
            }
            equipo.setStaffList(attachedStaffList);
            List<Clasificacion> attachedClasificacionList = new ArrayList<Clasificacion>();
            for (Clasificacion clasificacionListClasificacionToAttach : equipo.getClasificacionList()) {
                clasificacionListClasificacionToAttach = em.getReference(clasificacionListClasificacionToAttach.getClass(), clasificacionListClasificacionToAttach.getId());
                attachedClasificacionList.add(clasificacionListClasificacionToAttach);
            }
            equipo.setClasificacionList(attachedClasificacionList);
            List<ConvocatoriaEq> attachedConvocatoriaEqList = new ArrayList<ConvocatoriaEq>();
            for (ConvocatoriaEq convocatoriaEqListConvocatoriaEqToAttach : equipo.getConvocatoriaEqList()) {
                convocatoriaEqListConvocatoriaEqToAttach = em.getReference(convocatoriaEqListConvocatoriaEqToAttach.getClass(), convocatoriaEqListConvocatoriaEqToAttach.getId());
                attachedConvocatoriaEqList.add(convocatoriaEqListConvocatoriaEqToAttach);
            }
            equipo.setConvocatoriaEqList(attachedConvocatoriaEqList);
            List<Partido> attachedPartidoList = new ArrayList<Partido>();
            for (Partido partidoListPartidoToAttach : equipo.getPartidoList()) {
                partidoListPartidoToAttach = em.getReference(partidoListPartidoToAttach.getClass(), partidoListPartidoToAttach.getId());
                attachedPartidoList.add(partidoListPartidoToAttach);
            }
            equipo.setPartidoList(attachedPartidoList);
            List<Partido> attachedPartidoList1 = new ArrayList<Partido>();
            for (Partido partidoList1PartidoToAttach : equipo.getPartidoList1()) {
                partidoList1PartidoToAttach = em.getReference(partidoList1PartidoToAttach.getClass(), partidoList1PartidoToAttach.getId());
                attachedPartidoList1.add(partidoList1PartidoToAttach);
            }
            equipo.setPartidoList1(attachedPartidoList1);
            em.persist(equipo);
            if (ligaId != null) {
                ligaId.getEquipoList().add(equipo);
                ligaId = em.merge(ligaId);
            }
            if (clubId != null) {
                clubId.getEquipoList().add(equipo);
                clubId = em.merge(clubId);
            }
            if (categoriaId != null) {
                categoriaId.getEquipoList().add(equipo);
                categoriaId = em.merge(categoriaId);
            }
            for (Jugador jugadorListJugador : equipo.getJugadorList()) {
                Equipo oldEquipoIdOfJugadorListJugador = jugadorListJugador.getEquipoId();
                jugadorListJugador.setEquipoId(equipo);
                jugadorListJugador = em.merge(jugadorListJugador);
                if (oldEquipoIdOfJugadorListJugador != null) {
                    oldEquipoIdOfJugadorListJugador.getJugadorList().remove(jugadorListJugador);
                    oldEquipoIdOfJugadorListJugador = em.merge(oldEquipoIdOfJugadorListJugador);
                }
            }
            for (Staff staffListStaff : equipo.getStaffList()) {
                Equipo oldEquipoIdOfStaffListStaff = staffListStaff.getEquipoId();
                staffListStaff.setEquipoId(equipo);
                staffListStaff = em.merge(staffListStaff);
                if (oldEquipoIdOfStaffListStaff != null) {
                    oldEquipoIdOfStaffListStaff.getStaffList().remove(staffListStaff);
                    oldEquipoIdOfStaffListStaff = em.merge(oldEquipoIdOfStaffListStaff);
                }
            }
            for (Clasificacion clasificacionListClasificacion : equipo.getClasificacionList()) {
                Equipo oldEquipoIdOfClasificacionListClasificacion = clasificacionListClasificacion.getEquipoId();
                clasificacionListClasificacion.setEquipoId(equipo);
                clasificacionListClasificacion = em.merge(clasificacionListClasificacion);
                if (oldEquipoIdOfClasificacionListClasificacion != null) {
                    oldEquipoIdOfClasificacionListClasificacion.getClasificacionList().remove(clasificacionListClasificacion);
                    oldEquipoIdOfClasificacionListClasificacion = em.merge(oldEquipoIdOfClasificacionListClasificacion);
                }
            }
            for (ConvocatoriaEq convocatoriaEqListConvocatoriaEq : equipo.getConvocatoriaEqList()) {
                Equipo oldEquipoIdOfConvocatoriaEqListConvocatoriaEq = convocatoriaEqListConvocatoriaEq.getEquipoId();
                convocatoriaEqListConvocatoriaEq.setEquipoId(equipo);
                convocatoriaEqListConvocatoriaEq = em.merge(convocatoriaEqListConvocatoriaEq);
                if (oldEquipoIdOfConvocatoriaEqListConvocatoriaEq != null) {
                    oldEquipoIdOfConvocatoriaEqListConvocatoriaEq.getConvocatoriaEqList().remove(convocatoriaEqListConvocatoriaEq);
                    oldEquipoIdOfConvocatoriaEqListConvocatoriaEq = em.merge(oldEquipoIdOfConvocatoriaEqListConvocatoriaEq);
                }
            }
            for (Partido partidoListPartido : equipo.getPartidoList()) {
                Equipo oldEquipo2IdOfPartidoListPartido = partidoListPartido.getEquipo2Id();
                partidoListPartido.setEquipo2Id(equipo);
                partidoListPartido = em.merge(partidoListPartido);
                if (oldEquipo2IdOfPartidoListPartido != null) {
                    oldEquipo2IdOfPartidoListPartido.getPartidoList().remove(partidoListPartido);
                    oldEquipo2IdOfPartidoListPartido = em.merge(oldEquipo2IdOfPartidoListPartido);
                }
            }
            for (Partido partidoList1Partido : equipo.getPartidoList1()) {
                Equipo oldEquipo1IdOfPartidoList1Partido = partidoList1Partido.getEquipo1Id();
                partidoList1Partido.setEquipo1Id(equipo);
                partidoList1Partido = em.merge(partidoList1Partido);
                if (oldEquipo1IdOfPartidoList1Partido != null) {
                    oldEquipo1IdOfPartidoList1Partido.getPartidoList1().remove(partidoList1Partido);
                    oldEquipo1IdOfPartidoList1Partido = em.merge(oldEquipo1IdOfPartidoList1Partido);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Equipo equipo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Equipo persistentEquipo = em.find(Equipo.class, equipo.getId());
            Liga ligaIdOld = persistentEquipo.getLigaId();
            Liga ligaIdNew = equipo.getLigaId();
            Club clubIdOld = persistentEquipo.getClubId();
            Club clubIdNew = equipo.getClubId();
            Categoria categoriaIdOld = persistentEquipo.getCategoriaId();
            Categoria categoriaIdNew = equipo.getCategoriaId();
            List<Partido> partidoListOld = persistentEquipo.getPartidoList();
            List<Partido> partidoListNew = equipo.getPartidoList();
            List<Partido> partidoList1Old = persistentEquipo.getPartidoList1();
            List<Partido> partidoList1New = equipo.getPartidoList1();
            List<Staff> staffListOld = persistentEquipo.getStaffList();
            List<Staff> staffListNew = equipo.getStaffList();
            List<Clasificacion> clasificacionListOld = persistentEquipo.getClasificacionList();
            List<Clasificacion> clasificacionListNew = equipo.getClasificacionList();
            List<Jugador> jugadorListOld = persistentEquipo.getJugadorList();
            List<Jugador> jugadorListNew = equipo.getJugadorList();
            List<ConvocatoriaEq> convocatoriaEqListOld = persistentEquipo.getConvocatoriaEqList();
            List<ConvocatoriaEq> convocatoriaEqListNew = equipo.getConvocatoriaEqList();
            List<String> illegalOrphanMessages = null;
            for (Staff staffListOldStaff : staffListOld) {
                if (!staffListNew.contains(staffListOldStaff)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Staff " + staffListOldStaff + " since its equipoId field is not nullable.");
                }
            }
            for (Clasificacion clasificacionListOldClasificacion : clasificacionListOld) {
                if (!clasificacionListNew.contains(clasificacionListOldClasificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clasificacion " + clasificacionListOldClasificacion + " since its equipoId field is not nullable.");
                }
            }
            for (Jugador jugadorListOldJugador : jugadorListOld) {
                if (!jugadorListNew.contains(jugadorListOldJugador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Jugador " + jugadorListOldJugador + " since its equipoId field is not nullable.");
                }
            }
            for (ConvocatoriaEq convocatoriaEqListOldConvocatoriaEq : convocatoriaEqListOld) {
                if (!convocatoriaEqListNew.contains(convocatoriaEqListOldConvocatoriaEq)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConvocatoriaEq " + convocatoriaEqListOldConvocatoriaEq + " since its equipoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new com.spontecorp.littleligues.jpacontroller.exceptions.IllegalOrphanException(illegalOrphanMessages);
            }
            if (ligaIdNew != null) {
                ligaIdNew = em.getReference(ligaIdNew.getClass(), ligaIdNew.getId());
                equipo.setLigaId(ligaIdNew);
            }
            if (clubIdNew != null) {
                clubIdNew = em.getReference(clubIdNew.getClass(), clubIdNew.getId());
                equipo.setClubId(clubIdNew);
            }
            if (categoriaIdNew != null) {
                categoriaIdNew = em.getReference(categoriaIdNew.getClass(), categoriaIdNew.getId());
                equipo.setCategoriaId(categoriaIdNew);
            }
            List<Partido> attachedPartidoListNew = new ArrayList<Partido>();
            for (Partido partidoListNewPartidoToAttach : partidoListNew) {
                partidoListNewPartidoToAttach = em.getReference(partidoListNewPartidoToAttach.getClass(), partidoListNewPartidoToAttach.getId());
                attachedPartidoListNew.add(partidoListNewPartidoToAttach);
            }
            partidoListNew = attachedPartidoListNew;
            equipo.setPartidoList(partidoListNew);
            List<Partido> attachedPartidoList1New = new ArrayList<Partido>();
            for (Partido partidoList1NewPartidoToAttach : partidoList1New) {
                partidoList1NewPartidoToAttach = em.getReference(partidoList1NewPartidoToAttach.getClass(), partidoList1NewPartidoToAttach.getId());
                attachedPartidoList1New.add(partidoList1NewPartidoToAttach);
            }
            partidoList1New = attachedPartidoList1New;
            equipo.setPartidoList1(partidoList1New);
            List<Staff> attachedStaffListNew = new ArrayList<Staff>();
            for (Staff staffListNewStaffToAttach : staffListNew) {
                staffListNewStaffToAttach = em.getReference(staffListNewStaffToAttach.getClass(), staffListNewStaffToAttach.getId());
                attachedStaffListNew.add(staffListNewStaffToAttach);
            }
            staffListNew = attachedStaffListNew;
            equipo.setStaffList(staffListNew);
            List<Clasificacion> attachedClasificacionListNew = new ArrayList<Clasificacion>();
            for (Clasificacion clasificacionListNewClasificacionToAttach : clasificacionListNew) {
                clasificacionListNewClasificacionToAttach = em.getReference(clasificacionListNewClasificacionToAttach.getClass(), clasificacionListNewClasificacionToAttach.getId());
                attachedClasificacionListNew.add(clasificacionListNewClasificacionToAttach);
            }
            clasificacionListNew = attachedClasificacionListNew;
            equipo.setClasificacionList(clasificacionListNew);
            List<Jugador> attachedJugadorListNew = new ArrayList<Jugador>();
            for (Jugador jugadorListNewJugadorToAttach : jugadorListNew) {
                jugadorListNewJugadorToAttach = em.getReference(jugadorListNewJugadorToAttach.getClass(), jugadorListNewJugadorToAttach.getId());
                attachedJugadorListNew.add(jugadorListNewJugadorToAttach);
            }
            jugadorListNew = attachedJugadorListNew;
            equipo.setJugadorList(jugadorListNew);
            List<ConvocatoriaEq> attachedConvocatoriaEqListNew = new ArrayList<ConvocatoriaEq>();
            for (ConvocatoriaEq convocatoriaEqListNewConvocatoriaEqToAttach : convocatoriaEqListNew) {
                convocatoriaEqListNewConvocatoriaEqToAttach = em.getReference(convocatoriaEqListNewConvocatoriaEqToAttach.getClass(), convocatoriaEqListNewConvocatoriaEqToAttach.getId());
                attachedConvocatoriaEqListNew.add(convocatoriaEqListNewConvocatoriaEqToAttach);
            }
            convocatoriaEqListNew = attachedConvocatoriaEqListNew;
            equipo.setConvocatoriaEqList(convocatoriaEqListNew);
            equipo = em.merge(equipo);
            if (ligaIdOld != null && !ligaIdOld.equals(ligaIdNew)) {
                ligaIdOld.getEquipoList().remove(equipo);
                ligaIdOld = em.merge(ligaIdOld);
            }
            if (ligaIdNew != null && !ligaIdNew.equals(ligaIdOld)) {
                ligaIdNew.getEquipoList().add(equipo);
                ligaIdNew = em.merge(ligaIdNew);
            }
            if (clubIdOld != null && !clubIdOld.equals(clubIdNew)) {
                clubIdOld.getEquipoList().remove(equipo);
                clubIdOld = em.merge(clubIdOld);
            }
            if (clubIdNew != null && !clubIdNew.equals(clubIdOld)) {
                clubIdNew.getEquipoList().add(equipo);
                clubIdNew = em.merge(clubIdNew);
            }
            if (categoriaIdOld != null && !categoriaIdOld.equals(categoriaIdNew)) {
                categoriaIdOld.getEquipoList().remove(equipo);
                categoriaIdOld = em.merge(categoriaIdOld);
            }
            if (categoriaIdNew != null && !categoriaIdNew.equals(categoriaIdOld)) {
                categoriaIdNew.getEquipoList().add(equipo);
                categoriaIdNew = em.merge(categoriaIdNew);
            }
            for (Partido partidoListOldPartido : partidoListOld) {
                if (!partidoListNew.contains(partidoListOldPartido)) {
                    partidoListOldPartido.setEquipo2Id(null);
                    partidoListOldPartido = em.merge(partidoListOldPartido);
                }
            }
            for (Partido partidoListNewPartido : partidoListNew) {
                if (!partidoListOld.contains(partidoListNewPartido)) {
                    Equipo oldEquipo2IdOfPartidoListNewPartido = partidoListNewPartido.getEquipo2Id();
                    partidoListNewPartido.setEquipo2Id(equipo);
                    partidoListNewPartido = em.merge(partidoListNewPartido);
                    if (oldEquipo2IdOfPartidoListNewPartido != null && !oldEquipo2IdOfPartidoListNewPartido.equals(equipo)) {
                        oldEquipo2IdOfPartidoListNewPartido.getPartidoList().remove(partidoListNewPartido);
                        oldEquipo2IdOfPartidoListNewPartido = em.merge(oldEquipo2IdOfPartidoListNewPartido);
                    }
                }
            }
            for (Partido partidoList1OldPartido : partidoList1Old) {
                if (!partidoList1New.contains(partidoList1OldPartido)) {
                    partidoList1OldPartido.setEquipo1Id(null);
                    partidoList1OldPartido = em.merge(partidoList1OldPartido);
                }
            }
            for (Partido partidoList1NewPartido : partidoList1New) {
                if (!partidoList1Old.contains(partidoList1NewPartido)) {
                    Equipo oldEquipo1IdOfPartidoList1NewPartido = partidoList1NewPartido.getEquipo1Id();
                    partidoList1NewPartido.setEquipo1Id(equipo);
                    partidoList1NewPartido = em.merge(partidoList1NewPartido);
                    if (oldEquipo1IdOfPartidoList1NewPartido != null && !oldEquipo1IdOfPartidoList1NewPartido.equals(equipo)) {
                        oldEquipo1IdOfPartidoList1NewPartido.getPartidoList1().remove(partidoList1NewPartido);
                        oldEquipo1IdOfPartidoList1NewPartido = em.merge(oldEquipo1IdOfPartidoList1NewPartido);
                    }
                }
            }
            for (Staff staffListNewStaff : staffListNew) {
                if (!staffListOld.contains(staffListNewStaff)) {
                    Equipo oldEquipoIdOfStaffListNewStaff = staffListNewStaff.getEquipoId();
                    staffListNewStaff.setEquipoId(equipo);
                    staffListNewStaff = em.merge(staffListNewStaff);
                    if (oldEquipoIdOfStaffListNewStaff != null && !oldEquipoIdOfStaffListNewStaff.equals(equipo)) {
                        oldEquipoIdOfStaffListNewStaff.getStaffList().remove(staffListNewStaff);
                        oldEquipoIdOfStaffListNewStaff = em.merge(oldEquipoIdOfStaffListNewStaff);
                    }
                }
            }
            for (Clasificacion clasificacionListNewClasificacion : clasificacionListNew) {
                if (!clasificacionListOld.contains(clasificacionListNewClasificacion)) {
                    Equipo oldEquipoIdOfClasificacionListNewClasificacion = clasificacionListNewClasificacion.getEquipoId();
                    clasificacionListNewClasificacion.setEquipoId(equipo);
                    clasificacionListNewClasificacion = em.merge(clasificacionListNewClasificacion);
                    if (oldEquipoIdOfClasificacionListNewClasificacion != null && !oldEquipoIdOfClasificacionListNewClasificacion.equals(equipo)) {
                        oldEquipoIdOfClasificacionListNewClasificacion.getClasificacionList().remove(clasificacionListNewClasificacion);
                        oldEquipoIdOfClasificacionListNewClasificacion = em.merge(oldEquipoIdOfClasificacionListNewClasificacion);
                    }
                }
            }
            for (Jugador jugadorListNewJugador : jugadorListNew) {
                if (!jugadorListOld.contains(jugadorListNewJugador)) {
                    Equipo oldEquipoIdOfJugadorListNewJugador = jugadorListNewJugador.getEquipoId();
                    jugadorListNewJugador.setEquipoId(equipo);
                    jugadorListNewJugador = em.merge(jugadorListNewJugador);
                    if (oldEquipoIdOfJugadorListNewJugador != null && !oldEquipoIdOfJugadorListNewJugador.equals(equipo)) {
                        oldEquipoIdOfJugadorListNewJugador.getJugadorList().remove(jugadorListNewJugador);
                        oldEquipoIdOfJugadorListNewJugador = em.merge(oldEquipoIdOfJugadorListNewJugador);
                    }
                }
            }
            for (ConvocatoriaEq convocatoriaEqListNewConvocatoriaEq : convocatoriaEqListNew) {
                if (!convocatoriaEqListOld.contains(convocatoriaEqListNewConvocatoriaEq)) {
                    Equipo oldEquipoIdOfConvocatoriaEqListNewConvocatoriaEq = convocatoriaEqListNewConvocatoriaEq.getEquipoId();
                    convocatoriaEqListNewConvocatoriaEq.setEquipoId(equipo);
                    convocatoriaEqListNewConvocatoriaEq = em.merge(convocatoriaEqListNewConvocatoriaEq);
                    if (oldEquipoIdOfConvocatoriaEqListNewConvocatoriaEq != null && !oldEquipoIdOfConvocatoriaEqListNewConvocatoriaEq.equals(equipo)) {
                        oldEquipoIdOfConvocatoriaEqListNewConvocatoriaEq.getConvocatoriaEqList().remove(convocatoriaEqListNewConvocatoriaEq);
                        oldEquipoIdOfConvocatoriaEqListNewConvocatoriaEq = em.merge(oldEquipoIdOfConvocatoriaEqListNewConvocatoriaEq);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = equipo.getId();
                if (findEquipo(id) == null) {
                    throw new com.spontecorp.littleligues.jpacontroller.exceptions.NonexistentEntityException("The equipo with id " + id + " no longer exists.");
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
            Equipo equipo;
            try {
                equipo = em.getReference(Equipo.class, id);
                equipo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Staff> staffListOrphanCheck = equipo.getStaffList();
            for (Staff staffListOrphanCheckStaff : staffListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Staff " + staffListOrphanCheckStaff + " in its staffList field has a non-nullable equipoId field.");
            }
            List<Clasificacion> clasificacionListOrphanCheck = equipo.getClasificacionList();
            for (Clasificacion clasificacionListOrphanCheckClasificacion : clasificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Clasificacion " + clasificacionListOrphanCheckClasificacion + " in its clasificacionList field has a non-nullable equipoId field.");
            }
            List<Jugador> jugadorListOrphanCheck = equipo.getJugadorList();
            for (Jugador jugadorListOrphanCheckJugador : jugadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the Jugador " + jugadorListOrphanCheckJugador + " in its jugadorList field has a non-nullable equipoId field.");
            }
            List<ConvocatoriaEq> convocatoriaEqListOrphanCheck = equipo.getConvocatoriaEqList();
            for (ConvocatoriaEq convocatoriaEqListOrphanCheckConvocatoriaEq : convocatoriaEqListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Equipo (" + equipo + ") cannot be destroyed since the ConvocatoriaEq " + convocatoriaEqListOrphanCheckConvocatoriaEq + " in its convocatoriaEqList field has a non-nullable equipoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Liga ligaId = equipo.getLigaId();
            if (ligaId != null) {
                ligaId.getEquipoList().remove(equipo);
                ligaId = em.merge(ligaId);
            }
            Club clubId = equipo.getClubId();
            if (clubId != null) {
                clubId.getEquipoList().remove(equipo);
                clubId = em.merge(clubId);
            }
            Categoria categoriaId = equipo.getCategoriaId();
            if (categoriaId != null) {
                categoriaId.getEquipoList().remove(equipo);
                categoriaId = em.merge(categoriaId);
            }
            List<Partido> partidoList = equipo.getPartidoList();
            for (Partido partidoListPartido : partidoList) {
                partidoListPartido.setEquipo2Id(null);
                partidoListPartido = em.merge(partidoListPartido);
            }
            List<Partido> partidoList1 = equipo.getPartidoList1();
            for (Partido partidoList1Partido : partidoList1) {
                partidoList1Partido.setEquipo1Id(null);
                partidoList1Partido = em.merge(partidoList1Partido);
            }
            em.remove(equipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Equipo> findEquipoEntities() {
        return findEquipoEntities(true, -1, -1);
    }

    public List<Equipo> findEquipoEntities(int maxResults, int firstResult) {
        return findEquipoEntities(false, maxResults, firstResult);
    }

    private List<Equipo> findEquipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Equipo.class));
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
    
    /**
     * Lista de Equipos según la Liga y la Categoría a la que pertenecen
     * Ordenados por nombre en forma ASC
     * @param categoria
     * @param liga
     * @return 
     */
    public List<Equipo> findEquiposOnCategoriaLiga(Categoria categoria, Liga liga){
        List<Equipo> equipos = new ArrayList<>();
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT e from Equipo e "
                    + "WHERE e.ligaId = :ligaId AND e.categoriaId = :categoriaId ORDER BY e.nombre ASC";
            Query q = em.createQuery(query);
            q.setParameter("ligaId", liga);
            q.setParameter("categoriaId", categoria);
            equipos = q.getResultList();
        } finally {
            em.close();
        }

        return equipos;
    }

    public Equipo findEquipo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Equipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Equipo> rt = cq.from(Equipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
