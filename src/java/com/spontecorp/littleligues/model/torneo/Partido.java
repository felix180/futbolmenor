/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

import com.spontecorp.littleligues.model.liga.Cancha;
import com.spontecorp.littleligues.model.liga.Categoria;
import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.utils.StatusPartidoEnum;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "partido")
@NamedQueries({
    @NamedQuery(name = "Partido.findAll", query = "SELECT p FROM Partido p"),
    @NamedQuery(name = "Partido.findById", query = "SELECT p FROM Partido p WHERE p.id = :id"),
    @NamedQuery(name = "Partido.findByFecha", query = "SELECT p FROM Partido p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Partido.findByGolEq1", query = "SELECT p FROM Partido p WHERE p.golEq1 = :golEq1"),
    @NamedQuery(name = "Partido.findByGolEq2", query = "SELECT p FROM Partido p WHERE p.golEq2 = :golEq2"),
    @NamedQuery(name = "Partido.findByStatus", query = "SELECT p FROM Partido p WHERE p.status = :status")})
public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "gol_eq1")
    private Integer golEq1;
    @Column(name = "gol_eq2")
    private Integer golEq2;
    @Column(name = "status")
    private Integer status;
    @Column(name = "fake_eq_1")
    private String fakeEq1;
    @Column(name = "fake_eq_2")
    private String fakeEq2;
    @JoinColumn(name = "llave_id", referencedColumnName = "id")
    @ManyToOne
    private Llave llaveId;
    @JoinColumn(name = "jornada_id", referencedColumnName = "id")
    @ManyToOne
    private Jornada jornadaId;
    @JoinColumn(name = "equipo2_id", referencedColumnName = "id")
    @ManyToOne
    private Equipo equipo2Id;
    @JoinColumn(name = "equipo1_id", referencedColumnName = "id")
    @ManyToOne
    private Equipo equipo1Id;
    @JoinColumn(name = "cancha_id", referencedColumnName = "id")
    @ManyToOne
    private Cancha canchaId;
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Categoria categoriaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidoId")
    private List<Evento> eventoList;
    @OneToMany(mappedBy = "partidoId")
    private List<ConvocatoriaEq> convocatoriaEqList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidoId")
    private List<ArbitroPartido> arbitroPartidoList;
    
    private transient String statusString;

    public Partido() {
    }

    public Partido(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cancha getCanchaId() {
        return canchaId;
    }

    public void setCanchaId(Cancha canchaId) {
        this.canchaId = canchaId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getGolEq1() {
        return golEq1;
    }

    public void setGolEq1(Integer golEq1) {
        this.golEq1 = golEq1;
    }

    public Integer getGolEq2() {
        return golEq2;
    }

    public void setGolEq2(Integer golEq2) {
        this.golEq2 = golEq2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFakeEq1() {
        return fakeEq1;
    }

    public void setFakeEq1(String fakeEq1) {
        this.fakeEq1 = fakeEq1;
    }

    public String getFakeEq2() {
        return fakeEq2;
    }

    public void setFakeEq2(String fakeEq2) {
        this.fakeEq2 = fakeEq2;
    }

    public Llave getLlaveId() {
        return llaveId;
    }

    public void setLlaveId(Llave llaveId) {
        this.llaveId = llaveId;
    }

    public Jornada getJornadaId() {
        return jornadaId;
    }

    public void setJornadaId(Jornada jornadaId) {
        this.jornadaId = jornadaId;
    }

    public Equipo getEquipo2Id() {
        return equipo2Id;
    }

    public void setEquipo2Id(Equipo equipo2Id) {
        this.equipo2Id = equipo2Id;
    }

    public Equipo getEquipo1Id() {
        return equipo1Id;
    }

    public void setEquipo1Id(Equipo equipo1Id) {
        this.equipo1Id = equipo1Id;
    }

    public Categoria getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Categoria categoriaId) {
        this.categoriaId = categoriaId;
    }

    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    public List<ConvocatoriaEq> getConvocatoriaEqList() {
        return convocatoriaEqList;
    }

    public void setConvocatoriaEqList(List<ConvocatoriaEq> convocatoriaEqList) {
        this.convocatoriaEqList = convocatoriaEqList;
    }

    public List<ArbitroPartido> getArbitroPartidoList() {
        return arbitroPartidoList;
    }

    public void setArbitroPartidoList(List<ArbitroPartido> arbitroPartidoList) {
        this.arbitroPartidoList = arbitroPartidoList;
    }

    public String getStatusString() {
        for(StatusPartidoEnum st : StatusPartidoEnum.values()){
            if(status == st.valor()){
                statusString = st.etiqueta();
                break;
            }
        }
        return statusString;
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
        if (!(object instanceof Partido)) {
            return false;
        }
        Partido other = (Partido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.Partido[ id=" + id + " ]";
    }
    
}
