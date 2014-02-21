/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.liga;

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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
@Table(name = "club")
@NamedQueries({
    @NamedQuery(name = "Club.findAll", query = "SELECT c FROM Club c"),
    @NamedQuery(name = "Club.findById", query = "SELECT c FROM Club c WHERE c.id = :id"),
    @NamedQuery(name = "Club.findByNombre", query = "SELECT c FROM Club c WHERE c.nombre = :nombre")})
public class Club implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "logo")
    private String logo;
    @OneToMany(mappedBy = "clubId")
    private List<Contacto> contactoList;
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    @ManyToOne
    private Direccion direccionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clubId")
    private List<Equipo> equipoList;
    @ManyToMany(mappedBy = "clubList")
    private List<Cancha> canchaList;

    public Club() {
    }

    public Club(Integer id) {
        this.id = id;
    }

    public Club(Integer id, String nombre) {
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

    public void setNombre(String nombre) throws UnsupportedEncodingException{
        this.nombre = new String(nombre.getBytes("ISO-8859-1"), "UTF-8");
    }

    public Direccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Direccion direccionId) {
        this.direccionId = direccionId;
    }
    
    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    public List<Cancha> getCanchaList() {
        return canchaList;
    }

    public void setCanchaList(List<Cancha> canchaList) {
        this.canchaList = canchaList;
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
        if (!(object instanceof Club)) {
            return false;
        }
        Club other = (Club) object;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    
    
}
