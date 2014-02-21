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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "tipo_arbitro")
@NamedQueries({
    @NamedQuery(name = "TipoArbitro.findAll", query = "SELECT t FROM TipoArbitro t"),
    @NamedQuery(name = "TipoArbitro.findById", query = "SELECT t FROM TipoArbitro t WHERE t.id = :id"),
    @NamedQuery(name = "TipoArbitro.findByNombre", query = "SELECT t FROM TipoArbitro t WHERE t.nombre = :nombre")})
public class TipoArbitro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoArbitroId")
    private List<ArbitroPartido> arbitroPartidoList;

    public TipoArbitro() {
    }

    public TipoArbitro(Integer id) {
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
        if (!(object instanceof TipoArbitro)) {
            return false;
        }
        TipoArbitro other = (TipoArbitro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.TipoArbitro[ id=" + id + " ]";
    }
    
}
