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
@Table(name = "arbitro_partido")
@NamedQueries({
    @NamedQuery(name = "ArbitroPartido.findAll", query = "SELECT a FROM ArbitroPartido a"),
    @NamedQuery(name = "ArbitroPartido.findById", query = "SELECT a FROM ArbitroPartido a WHERE a.id = :id")})
public class ArbitroPartido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "tipo_arbitro_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoArbitro tipoArbitroId;
    @JoinColumn(name = "partido_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Partido partidoId;
    @JoinColumn(name = "arbitro_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Arbitro arbitroId;

    public ArbitroPartido() {
    }

    public ArbitroPartido(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoArbitro getTipoArbitroId() {
        return tipoArbitroId;
    }

    public void setTipoArbitroId(TipoArbitro tipoArbitroId) {
        this.tipoArbitroId = tipoArbitroId;
    }

    public Partido getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Partido partidoId) {
        this.partidoId = partidoId;
    }

    public Arbitro getArbitroId() {
        return arbitroId;
    }

    public void setArbitroId(Arbitro arbitroId) {
        this.arbitroId = arbitroId;
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
        if (!(object instanceof ArbitroPartido)) {
            return false;
        }
        ArbitroPartido other = (ArbitroPartido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.ArbitroPartido[ id=" + id + " ]";
    }
    
}
