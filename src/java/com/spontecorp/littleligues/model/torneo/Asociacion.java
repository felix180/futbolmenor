/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

import com.spontecorp.littleligues.model.liga.Contacto;
import com.spontecorp.littleligues.model.liga.Direccion;
import com.spontecorp.littleligues.model.liga.Liga;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "asociacion")
@NamedQueries({
    @NamedQuery(name = "Asociacion.findAll", query = "SELECT a FROM Asociacion a"),
    @NamedQuery(name = "Asociacion.findById", query = "SELECT a FROM Asociacion a WHERE a.id = :id"),
    @NamedQuery(name = "Asociacion.findByNombre", query = "SELECT a FROM Asociacion a WHERE a.nombre = :nombre")})
public class Asociacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @OneToMany(mappedBy = "asociacionId")
    private List<Contacto> contactoList;
    @OneToMany(mappedBy = "asociacionId")
    private List<Liga> ligaList;
    @OneToMany(mappedBy = "asociacionId")
    private List<Arbitro> arbitroList;
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    @ManyToOne
    private Direccion direccionId;

    public Asociacion() {
    }

    public Asociacion(Integer id) {
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

    public void setNombre(String nombre) throws UnsupportedEncodingException{
        this.nombre = new String(nombre.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public List<Liga> getLigaList() {
        return ligaList;
    }

    public void setLigaList(List<Liga> ligaList) {
        this.ligaList = ligaList;
    }

    public List<Arbitro> getArbitroList() {
        return arbitroList;
    }

    public void setArbitroList(List<Arbitro> arbitroList) {
        this.arbitroList = arbitroList;
    }

    public Direccion getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Direccion direccionId) {
        this.direccionId = direccionId;
    }

    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
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
        if (!(object instanceof Asociacion)) {
            return false;
        }
        Asociacion other = (Asociacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.Asociacion[ id=" + id + " ]";
    }
    
}
