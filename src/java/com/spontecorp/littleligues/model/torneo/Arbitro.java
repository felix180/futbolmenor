/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

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
@Table(name = "arbitro")
@NamedQueries({
    @NamedQuery(name = "Arbitro.findAll", query = "SELECT a FROM Arbitro a"),
    @NamedQuery(name = "Arbitro.findById", query = "SELECT a FROM Arbitro a WHERE a.id = :id"),
    @NamedQuery(name = "Arbitro.findByNombre", query = "SELECT a FROM Arbitro a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Arbitro.findByApellido", query = "SELECT a FROM Arbitro a WHERE a.apellido = :apellido"),
    @NamedQuery(name = "Arbitro.findByNacionalidad", query = "SELECT a FROM Arbitro a WHERE a.nacionalidad = :nacionalidad")})
public class Arbitro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "nacionalidad")
    private String nacionalidad;
    @JoinColumn(name = "asociacion_id", referencedColumnName = "id")
    @ManyToOne
    private Asociacion asociacionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arbitroId")
    private List<ArbitroPartido> arbitroPartidoList;

    public Arbitro() {
    }

    public Arbitro(Integer id) {
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

    public String getApellido(){
        return apellido;
    }

    public void setApellido(String apellido) throws UnsupportedEncodingException{
        this.apellido = new String(apellido.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) throws UnsupportedEncodingException{
        this.nacionalidad = new String(nacionalidad.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public Asociacion getAsociacionId() {
        return asociacionId;
    }

    public void setAsociacionId(Asociacion asociacionId) {
        this.asociacionId = asociacionId;
    }

    public List<ArbitroPartido> getArbitroPartidoList() {
        return arbitroPartidoList;
    }

    public void setArbitroPartidoList(List<ArbitroPartido> arbitroPartidoList) {
        this.arbitroPartidoList = arbitroPartidoList;
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
        if (!(object instanceof Arbitro)) {
            return false;
        }
        Arbitro other = (Arbitro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.Arbitro[ id=" + id + " ]";
    }
    
}
