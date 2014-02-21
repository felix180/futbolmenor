/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

import java.io.Serializable;
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
@Table(name = "convocado")
@NamedQueries({
    @NamedQuery(name = "Convocado.findAll", query = "SELECT c FROM Convocado c"),
    @NamedQuery(name = "Convocado.findById", query = "SELECT c FROM Convocado c WHERE c.id = :id"),
    @NamedQuery(name = "Convocado.findByTitular", query = "SELECT c FROM Convocado c WHERE c.titular = :titular"),
    @NamedQuery(name = "Convocado.findByPosicion", query = "SELECT c FROM Convocado c WHERE c.posicion = :posicion")})
public class Convocado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "titular")
    private Integer titular;
    @Column(name = "posicion")
    private Integer posicion;
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @ManyToOne
    private Staff staffId;
    @JoinColumn(name = "jugador_id", referencedColumnName = "id")
    @ManyToOne
    private Jugador jugadorId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "convocadoId")
    private List<Evento> eventoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "convocadosId")
    private List<ConvocatoriaEq> convocatoriaEqList;

    public Convocado() {
    }

    public Convocado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTitular() {
        return titular;
    }

    public void setTitular(Integer titular) {
        this.titular = titular;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public Staff getStaffId() {
        return staffId;
    }

    public void setStaffId(Staff staffId) {
        this.staffId = staffId;
    }

    public Jugador getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(Jugador jugadorId) {
        this.jugadorId = jugadorId;
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public List<ConvocatoriaEq> getConvocatoriaEqList() {
        return convocatoriaEqList;
    }

    public void setConvocatoriaEqList(List<ConvocatoriaEq> convocatoriaEqList) {
        this.convocatoriaEqList = convocatoriaEqList;
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
        if (!(object instanceof Convocado)) {
            return false;
        }
        Convocado other = (Convocado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.Convocado[ id=" + id + " ]";
    }
    
}
