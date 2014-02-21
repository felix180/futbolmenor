/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.liga;

import com.spontecorp.littleligues.model.torneo.Clasificacion;
import com.spontecorp.littleligues.model.torneo.ConvocatoriaEq;
import com.spontecorp.littleligues.model.torneo.Jugador;
import com.spontecorp.littleligues.model.torneo.Partido;
import com.spontecorp.littleligues.model.torneo.Staff;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "equipo")
@NamedQueries({
    @NamedQuery(name = "Equipo.findAll", query = "SELECT e FROM Equipo e"),
    @NamedQuery(name = "Equipo.findById", query = "SELECT e FROM Equipo e WHERE e.id = :id"),
    @NamedQuery(name = "Equipo.findByNombre", query = "SELECT e FROM Equipo e WHERE e.nombre = :nombre")})
public class Equipo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "equipo2Id")
    private List<Partido> partidoList;
    @OneToMany(mappedBy = "equipo1Id")
    private List<Partido> partidoList1;
    @JoinColumn(name = "liga_id", referencedColumnName = "id")
    @ManyToOne
    private Liga ligaId;
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Club clubId;
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Categoria categoriaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipoId")
    private List<Jugador> jugadorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipoId")
    private List<Staff> staffList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipoId")
    private List<Clasificacion> clasificacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "equipoId")
    private List<ConvocatoriaEq> convocatoriaEqList;

    public Equipo() {
    }

    public Equipo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Liga getLigaId() {
        return ligaId;
    }

    public void setLigaId(Liga ligaId) {
        this.ligaId = ligaId;
    }

    public Club getClubId() {
        return clubId;
    }

    public void setClubId(Club clubId) {
        this.clubId = clubId;
    }

    public Categoria getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Categoria categoriaId) {
        this.categoriaId = categoriaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipo)) {
            return false;
        }
        Equipo other = (Equipo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNombre();
    }

    public List<Jugador> getJugadorList() {
        return jugadorList;
    }

    public void setJugadorList(List<Jugador> jugadorList) {
        this.jugadorList = jugadorList;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }

    public List<Clasificacion> getClasificacionList() {
        return clasificacionList;
    }

    public void setClasificacionList(List<Clasificacion> clasificacionList) {
        this.clasificacionList = clasificacionList;
    }

    public List<ConvocatoriaEq> getConvocatoriaEqList() {
        return convocatoriaEqList;
    }

    public void setConvocatoriaEqList(List<ConvocatoriaEq> convocatoriaEqList) {
        this.convocatoriaEqList = convocatoriaEqList;
    }

    public List<Partido> getPartidoList() {
        return partidoList;
    }

    public void setPartidoList(List<Partido> partidoList) {
        this.partidoList = partidoList;
    }

    public List<Partido> getPartidoList1() {
        return partidoList1;
    }

    public void setPartidoList1(List<Partido> partidoList1) {
        this.partidoList1 = partidoList1;
    }
    
}
