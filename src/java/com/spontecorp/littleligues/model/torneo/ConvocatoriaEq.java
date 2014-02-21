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
@Table(name = "convocatoria_eq")
@NamedQueries({
    @NamedQuery(name = "ConvocatoriaEq.findAll", query = "SELECT c FROM ConvocatoriaEq c"),
    @NamedQuery(name = "ConvocatoriaEq.findById", query = "SELECT c FROM ConvocatoriaEq c WHERE c.id = :id")})
public class ConvocatoriaEq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "partido_id", referencedColumnName = "id")
    @ManyToOne
    private Partido partidoId;
    @JoinColumn(name = "equipo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equipo equipoId;
    @JoinColumn(name = "convocados_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Convocado convocadosId;

    public ConvocatoriaEq() {
    }

    public ConvocatoriaEq(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Partido getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(Partido partidoId) {
        this.partidoId = partidoId;
    }

    public Equipo getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Equipo equipoId) {
        this.equipoId = equipoId;
    }

    public Convocado getConvocadosId() {
        return convocadosId;
    }

    public void setConvocadosId(Convocado convocadosId) {
        this.convocadosId = convocadosId;
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
        if (!(object instanceof ConvocatoriaEq)) {
            return false;
        }
        ConvocatoriaEq other = (ConvocatoriaEq) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.ConvocatoriaEq[ id=" + id + " ]";
    }
    
}
