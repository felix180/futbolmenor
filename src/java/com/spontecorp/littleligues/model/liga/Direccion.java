/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.liga;

import com.spontecorp.littleligues.model.torneo.Asociacion;
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
@Table(name = "direccion")
@NamedQueries({
    @NamedQuery(name = "Direccion.findAll", query = "SELECT d FROM Direccion d"),
    @NamedQuery(name = "Direccion.findById", query = "SELECT d FROM Direccion d WHERE d.id = :id"),
    @NamedQuery(name = "Direccion.findByTelefono1", query = "SELECT d FROM Direccion d WHERE d.telefono1 = :telefono1"),
    @NamedQuery(name = "Direccion.findByTelefono2", query = "SELECT d FROM Direccion d WHERE d.telefono2 = :telefono2"),
    @NamedQuery(name = "Direccion.findByEmail", query = "SELECT d FROM Direccion d WHERE d.email = :email"),
    @NamedQuery(name = "Direccion.findByPaginaWeb", query = "SELECT d FROM Direccion d WHERE d.paginaWeb = :paginaWeb")})
public class Direccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "telefono1")
    private String telefono1;
    @Column(name = "telefono2")
    private String telefono2;
    @Column(name = "email")
    private String email;
    @Column(name = "pagina_web")
    private String paginaWeb;
    @JoinColumn(name = "localidad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Localidad localidadId;
    @OneToMany(mappedBy = "direccionId")
    private List<Liga> ligaList;
    @OneToMany(mappedBy = "direccionId")
    private List<Asociacion> asociacionList;
    @OneToMany(mappedBy = "direccionId")
    private List<Club> clubList;
    @OneToMany(mappedBy = "direccionId")
    private List<Cancha> canchaList;

    public Direccion() {
    }

    public Direccion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) throws UnsupportedEncodingException{
        this.direccion = new String(direccion.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public Localidad getLocalidadId() {
        return localidadId;
    }

    public void setLocalidadId(Localidad localidadId) {
        this.localidadId = localidadId;
    }

    public List<Liga> getLigaList() {
        return ligaList;
    }

    public void setLigaList(List<Liga> ligaList) {
        this.ligaList = ligaList;
    }

    public List<Club> getClubList() {
        return clubList;
    }

    public void setClubList(List<Club> clubList) {
        this.clubList = clubList;
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
        if (!(object instanceof Direccion)) {
            return false;
        }
        Direccion other = (Direccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.liga.Direccion[ id=" + id + " ]";
    }

    public List<Cancha> getCanchaList() {
        return canchaList;
    }

    public void setCanchaList(List<Cancha> canchaList) {
        this.canchaList = canchaList;
    }

    public List<Asociacion> getAsociacionList() {
        return asociacionList;
    }

    public void setAsociacionList(List<Asociacion> asociacionList) {
        this.asociacionList = asociacionList;
    }
    
}
