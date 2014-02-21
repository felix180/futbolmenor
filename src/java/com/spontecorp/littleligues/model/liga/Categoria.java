package com.spontecorp.littleligues.model.liga;

import com.spontecorp.littleligues.model.torneo.Grupo;
import com.spontecorp.littleligues.model.torneo.Llave;
import com.spontecorp.littleligues.model.torneo.Partido;
import java.io.Serializable;
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
import javax.persistence.Transient;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "categoria")
@NamedQueries({
    @NamedQuery(name = "Categoria.findAll", query = "SELECT c FROM Categoria c"),
    @NamedQuery(name = "Categoria.findAllOrderAge", query = "SELECT c FROM Categoria c ORDER BY c.edadMin"),
    @NamedQuery(name = "Categoria.findById", query = "SELECT c FROM Categoria c WHERE c.id = :id"),
    @NamedQuery(name = "Categoria.findByNombre", query = "SELECT c FROM Categoria c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Categoria.findByEdadMin", query = "SELECT c FROM Categoria c WHERE c.edadMin = :edadMin"),
    @NamedQuery(name = "Categoria.findByEdadMax", query = "SELECT c FROM Categoria c WHERE c.edadMax = :edadMax")})
public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "edad_min")
    private Integer edadMin;
    @Column(name = "edad_max")
    private Integer edadMax;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaId")
    private List<Partido> partidoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriaId")
    private List<Equipo> equipoList;
    @Transient
    private List<Grupo> allGrupos;
    @Transient
    private List<Llave> allLlaves;

    public Categoria() {
    }

    public Categoria(Integer id) {
        this.id = id;
    }

    public Categoria(Integer id, String nombre) {
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

    public Integer getEdadMin() {
        return edadMin;
    }

    public void setEdadMin(Integer edadMin) {
        this.edadMin = edadMin;
    }

    public Integer getEdadMax() {
        return edadMax;
    }

    public void setEdadMax(Integer edadMax) {
        this.edadMax = edadMax;
    }

    public List<Partido> getPartidoList() {
        return partidoList;
    }

    public void setPartidoList(List<Partido> partidoList) {
        this.partidoList = partidoList;
    }

    public List<Equipo> getEquipoList() {
        return equipoList;
    }

    public void setEquipoList(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    public List<Grupo> getAllGrupos() {
        return allGrupos;
    }

    public void setAllGrupos(List<Grupo> allGrupos) {
        this.allGrupos = allGrupos;
    }

    public List<Llave> getAllLlaves() {
        return allLlaves;
    }

    public void setAllLlaves(List<Llave> allLlaves) {
        this.allLlaves = allLlaves;
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
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
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
