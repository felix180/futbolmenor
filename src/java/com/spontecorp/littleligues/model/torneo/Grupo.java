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
import javax.persistence.Transient;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "grupo")
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findById", query = "SELECT g FROM Grupo g WHERE g.id = :id"),
    @NamedQuery(name = "Grupo.findByNombre", query = "SELECT g FROM Grupo g WHERE g.nombre = :nombre")})
public class Grupo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "fase_id", referencedColumnName = "id")
    @ManyToOne
    private Fase faseId;
    @OneToMany(mappedBy = "grupoId")
    private List<Jornada> jornadaList;
    @Transient 
    private List<Jornada> allJornadas;
    @Transient 
    private int totalJornadas;

    public Grupo() {
    }

    public Grupo(Integer id) {
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

    public Fase getFaseId() {
        return faseId;
    }

    public void setFaseId(Fase faseId) {
        this.faseId = faseId;
    }

    public List<Jornada> getJornadaList() {
        return jornadaList;
    }

    public void setJornadaList(List<Jornada> jornadaList) {
        this.jornadaList = jornadaList;
    }

    public List<Jornada> getAllJornadas() {
        return allJornadas;
    }

    public void setAllJornadas(List<Jornada> allJornadas) {
        this.allJornadas = allJornadas;
    }

    public int getTotalJornadas() {
        return totalJornadas;
    }

    public void setTotalJornadas(int totalJornadas) {
        this.totalJornadas = totalJornadas;
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
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
