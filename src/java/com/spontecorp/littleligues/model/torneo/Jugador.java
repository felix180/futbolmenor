/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.torneo;

import com.spontecorp.littleligues.model.liga.Equipo;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "jugador")
@NamedQueries({
    @NamedQuery(name = "Jugador.findAll", query = "SELECT j FROM Jugador j"),
    @NamedQuery(name = "Jugador.findById", query = "SELECT j FROM Jugador j WHERE j.id = :id"),
    @NamedQuery(name = "Jugador.findByNombre", query = "SELECT j FROM Jugador j WHERE j.nombre = :nombre"),
    @NamedQuery(name = "Jugador.findByApellido", query = "SELECT j FROM Jugador j WHERE j.apellido = :apellido"),
    @NamedQuery(name = "Jugador.findByCi", query = "SELECT j FROM Jugador j WHERE j.ci = :ci"),
    @NamedQuery(name = "Jugador.findByApodo", query = "SELECT j FROM Jugador j WHERE j.apodo = :apodo"),
    @NamedQuery(name = "Jugador.findByFechaNacimiento", query = "SELECT j FROM Jugador j WHERE j.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Jugador.findByNacionalidad", query = "SELECT j FROM Jugador j WHERE j.nacionalidad = :nacionalidad"),
    @NamedQuery(name = "Jugador.findByNumeroCamiseta", query = "SELECT j FROM Jugador j WHERE j.numeroCamiseta = :numeroCamiseta"),
    @NamedQuery(name = "Jugador.findByAltura", query = "SELECT j FROM Jugador j WHERE j.altura = :altura"),
    @NamedQuery(name = "Jugador.findByPeso", query = "SELECT j FROM Jugador j WHERE j.peso = :peso")})
public class Jugador implements Serializable {
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
    @Column(name = "ci")
    private Integer ci;
    @Column(name = "apodo")
    private String apodo;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "nacionalidad")
    private String nacionalidad;
    @Column(name = "numero_camiseta")
    private Integer numeroCamiseta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "altura")
    private Double altura;
    @Column(name = "peso")
    private Double peso;
    @JoinColumn(name = "equipo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equipo equipoId;
    @OneToMany(mappedBy = "jugadorId")
    private List<Convocado> convocadoList;

    public Jugador() {
    }

    public Jugador(Integer id) {
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) throws UnsupportedEncodingException{
        this.apellido = new String(apellido.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public Integer getCi() {
        return ci;
    }

    public void setCi(Integer ci) {
        this.ci = ci;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) throws UnsupportedEncodingException{
        this.apodo = new String(apodo.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) throws UnsupportedEncodingException{
        this.nacionalidad = new String(nacionalidad.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public Integer getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(Integer numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Equipo getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Equipo equipoId) {
        this.equipoId = equipoId;
    }

    public List<Convocado> getConvocadoList() {
        return convocadoList;
    }

    public void setConvocadoList(List<Convocado> convocadoList) {
        this.convocadoList = convocadoList;
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
        if (!(object instanceof Jugador)) {
            return false;
        }
        Jugador other = (Jugador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.torneo.Jugador[ id=" + id + " ]";
    }
    
}
