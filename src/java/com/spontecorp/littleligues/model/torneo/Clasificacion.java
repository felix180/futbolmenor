/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

import com.spontecorp.littleligues.model.liga.Equipo;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "clasificacion")
@NamedQueries({
    @NamedQuery(name = "Clasificacion.findAll", query = "SELECT c FROM Clasificacion c"),
    @NamedQuery(name = "Clasificacion.findById", query = "SELECT c FROM Clasificacion c WHERE c.id = :id"),
    @NamedQuery(name = "Clasificacion.findByJugados", query = "SELECT c FROM Clasificacion c WHERE c.jugados = :jugados"),
    @NamedQuery(name = "Clasificacion.findByGanados", query = "SELECT c FROM Clasificacion c WHERE c.ganados = :ganados"),
    @NamedQuery(name = "Clasificacion.findByEmpatados", query = "SELECT c FROM Clasificacion c WHERE c.empatados = :empatados"),
    @NamedQuery(name = "Clasificacion.findByPerdidos", query = "SELECT c FROM Clasificacion c WHERE c.perdidos = :perdidos"),
    @NamedQuery(name = "Clasificacion.findByGolesFavor", query = "SELECT c FROM Clasificacion c WHERE c.golesFavor = :golesFavor"),
    @NamedQuery(name = "Clasificacion.findByGolesContra", query = "SELECT c FROM Clasificacion c WHERE c.golesContra = :golesContra"),
    @NamedQuery(name = "Clasificacion.findByDiferencia", query = "SELECT c FROM Clasificacion c WHERE c.diferencia = :diferencia"),
    @NamedQuery(name = "Clasificacion.findByPuntos", query = "SELECT c FROM Clasificacion c WHERE c.puntos = :puntos"),
    @NamedQuery(name = "Clasificacion.findByEqlocal", query = "SELECT c FROM Clasificacion c WHERE c.eqlocal = :eqlocal")})
public class Clasificacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "jugados")
    private Integer jugados;
    @Column(name = "ganados")
    private Integer ganados;
    @Column(name = "empatados")
    private Integer empatados;
    @Column(name = "perdidos")
    private Integer perdidos;
    @Column(name = "goles_favor")
    private Integer golesFavor;
    @Column(name = "goles_contra")
    private Integer golesContra;
    @Column(name = "diferencia")
    private Integer diferencia;
    @Column(name = "puntos")
    private Integer puntos;
    @Column(name = "eqlocal")
    private Integer eqlocal;
    @JoinColumn(name = "jornada_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Jornada jornadaId;
    @JoinColumn(name = "equipo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equipo equipoId;

    public Clasificacion() {
    }

    public Clasificacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJugados() {
        return jugados;
    }

    public void setJugados(Integer jugados) {
        this.jugados = jugados;
    }

    public Integer getGanados() {
        return ganados;
    }

    public void setGanados(Integer ganados) {
        this.ganados = ganados;
    }

    public Integer getEmpatados() {
        return empatados;
    }

    public void setEmpatados(Integer empatados) {
        this.empatados = empatados;
    }

    public Integer getPerdidos() {
        return perdidos;
    }

    public void setPerdidos(Integer perdidos) {
        this.perdidos = perdidos;
    }

    public Integer getGolesFavor() {
        return golesFavor;
    }

    public void setGolesFavor(Integer golesFavor) {
        this.golesFavor = golesFavor;
    }

    public Integer getGolesContra() {
        return golesContra;
    }

    public void setGolesContra(Integer golesContra) {
        this.golesContra = golesContra;
    }

    public Integer getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Integer diferencia) {
        this.diferencia = diferencia;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public Integer getEqlocal() {
        return eqlocal;
    }

    public void setEqlocal(Integer eqlocal) {
        this.eqlocal = eqlocal;
    }

    public Jornada getJornadaId() {
        return jornadaId;
    }

    public void setJornadaId(Jornada jornadaId) {
        this.jornadaId = jornadaId;
    }

    public Equipo getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Equipo equipoId) {
        this.equipoId = equipoId;
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
        if (!(object instanceof Clasificacion)) {
            return false;
        }
        Clasificacion other = (Clasificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.Clasificacion[ id=" + id + " ]";
    }
    
}
