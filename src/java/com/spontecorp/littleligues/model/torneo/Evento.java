/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

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
@Table(name = "evento")
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e"),
    @NamedQuery(name = "Evento.findById", query = "SELECT e FROM Evento e WHERE e.id = :id"),
    @NamedQuery(name = "Evento.findByTiempo", query = "SELECT e FROM Evento e WHERE e.tiempo = :tiempo"),
    @NamedQuery(name = "Evento.findByMinuto", query = "SELECT e FROM Evento e WHERE e.minuto = :minuto")})
public class Evento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tiempo")
    private Integer tiempo;
    @Column(name = "minuto")
    private Integer minuto;
    @JoinColumn(name = "tipo_evento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoEvento tipoEventoId;
    @JoinColumn(name = "partido_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Partido partidoId;
    @JoinColumn(name = "convocado_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Convocado convocadoId;

    public Evento() {
    }

    public Evento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTiempo() {
        return tiempo;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }

    public TipoEvento getTipoEventoId() {
        return tipoEventoId;
    }

    public void setTipoEventoId(TipoEvento tipoEventoId) {
        this.tipoEventoId = tipoEventoId;
    }

    public Partido getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Partido partidoId) {
        this.partidoId = partidoId;
    }

    public Convocado getConvocadoId() {
        return convocadoId;
    }

    public void setConvocadoId(Convocado convocadoId) {
        this.convocadoId = convocadoId;
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
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.Evento[ id=" + id + " ]";
    }
    
}
