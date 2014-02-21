/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model;

import com.spontecorp.littleligues.model.liga.Liga;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author zuleidyb
 */
@Entity
@Table(name = "carrusel_home")
@NamedQueries({
    @NamedQuery(name = "CarruselHome.findAll", query = "SELECT c FROM CarruselHome c")})
public class CarruselHome implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
//    @Column(name = "liga_id")
//    private String ligaId;
//    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Basic(optional = false)
    @Column(name = "fecha_config")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConfig;
    @JoinColumn(name = "liga_id", referencedColumnName = "id")
    @ManyToOne
    private Liga ligaId;

    public CarruselHome() {
    }

    public CarruselHome(Integer id) {
        this.id = id;
    }

    public CarruselHome(Integer id, Date fechaInicio, Date fechaFin, Date fechaConfig) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaConfig = fechaConfig;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaConfig() {
        return fechaConfig;
    }

    public void setFechaConfig(Date fechaConfig) {
        this.fechaConfig = fechaConfig;
    }

    public Liga getLigaId() {
        return ligaId;
    }

    public void setLigaId(Liga ligaId) {
        this.ligaId = ligaId;
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
        if (!(object instanceof CarruselHome)) {
            return false;
        }
        CarruselHome other = (CarruselHome) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.CarruselHome[ id=" + id + " ]";
    }
    
}
