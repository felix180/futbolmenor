/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

import com.spontecorp.littleligues.jsfcontroller.LigaBean;
import com.spontecorp.littleligues.jsfcontroller.LigaBean.Clasifica;
import java.io.Serializable;
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
import javax.persistence.Transient;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "jornada")
@NamedQueries({
    @NamedQuery(name = "Jornada.findAll", query = "SELECT j FROM Jornada j"),
    @NamedQuery(name = "Jornada.findById", query = "SELECT j FROM Jornada j WHERE j.id = :id"),
    @NamedQuery(name = "Jornada.findByNumero", query = "SELECT j FROM Jornada j WHERE j.numero = :numero")})
public class Jornada implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "numero")
    private Integer numero;
    @OneToMany(mappedBy = "jornadaId")
    private List<Partido> partidoList;
    @JoinColumn(name = "grupo_id", referencedColumnName = "id")
    @ManyToOne
    private Grupo grupoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jornadaId")
    private List<Clasificacion> clasificacionList;
    @Transient
    private List<LigaBean.Clasifica> clasifica;
    @Transient
    private List<Partido> partidoJornada;
    @Column(name = "isCurrent")
    private Integer isCurrent;
    

    public Jornada() {
    }

    public Jornada(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public List<Partido> getPartidoList() {
        return partidoList;
    }

    public void setPartidoList(List<Partido> partidoList) {
        this.partidoList = partidoList;
    }

    public Grupo getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Grupo grupoId) {
        this.grupoId = grupoId;
    }

    public List<Clasificacion> getClasificacionList() {
        return clasificacionList;
    }

    public void setClasificacionList(List<Clasificacion> clasificacionList) {
        this.clasificacionList = clasificacionList;
    }

    public List<Clasifica> getClasifica() {
        return clasifica;
    }

    public void setClasifica(List<Clasifica> clasifica) {
        this.clasifica = clasifica;
    }

    public List<Partido> getPartidoJornada() {
        return partidoJornada;
    }

    public void setPartidoJornada(List<Partido> partidoJornada) {
        this.partidoJornada = partidoJornada;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
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
        if (!(object instanceof Jornada)) {
            return false;
        }
        Jornada other = (Jornada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(numero);
    }
    
}
