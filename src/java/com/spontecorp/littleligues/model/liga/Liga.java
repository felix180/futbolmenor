/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.liga;

import com.spontecorp.littleligues.model.torneo.Asociacion;
import com.spontecorp.littleligues.model.torneo.Temporada;
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
import javax.persistence.Lob;
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
@Table(name = "liga")
@NamedQueries({
    @NamedQuery(name = "Liga.findAll", query = "SELECT l FROM Liga l"),
    @NamedQuery(name = "Liga.findById", query = "SELECT l FROM Liga l WHERE l.id = :id"),
    @NamedQuery(name = "Liga.findByNombre", query = "SELECT l FROM Liga l WHERE l.nombre = :nombre")})
public class Liga implements Serializable {
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @OneToMany(mappedBy = "ligaId")
    private List<Equipo> equipoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ligaId")
    private List<Temporada> temporadaList;
    @OneToMany(mappedBy = "ligaId")
    private List<Contacto> contactoList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    @ManyToOne
    private Direccion direccionId;
    @JoinColumn(name = "asociacion_id", referencedColumnName = "id")
    @ManyToOne
    private Asociacion asociacionId;

    public Liga() {
    }

    public Liga(Integer id) {
        this.id = id;
    }

    public Liga(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public Direccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Direccion direccionId) {
        this.direccionId = direccionId;
    }

    public Asociacion getAsociacionId() {
        return asociacionId;
    }

    public void setAsociacionId(Asociacion asociacionId) {
        this.asociacionId = asociacionId;
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
        if (!(object instanceof Liga)) {
            return false;
        }
        Liga other = (Liga) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    public List<Temporada> getTemporadaList() {
        return temporadaList;
    }

    public void setTemporadaList(List<Temporada> temporadaList) {
        this.temporadaList = temporadaList;
    }
    
}
