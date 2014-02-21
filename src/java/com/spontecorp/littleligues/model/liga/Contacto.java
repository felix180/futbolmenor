/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model.liga;

import com.spontecorp.littleligues.model.torneo.Asociacion;
import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import javax.persistence.Table;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "contacto")
@NamedQueries({
    @NamedQuery(name = "Contacto.findAll", query = "SELECT c FROM Contacto c"),
    @NamedQuery(name = "Contacto.findById", query = "SELECT c FROM Contacto c WHERE c.id = :id"),
    @NamedQuery(name = "Contacto.findByApellido", query = "SELECT c FROM Contacto c WHERE c.apellido = :apellido"),
    @NamedQuery(name = "Contacto.findByNombre", query = "SELECT c FROM Contacto c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Contacto.findByApodo", query = "SELECT c FROM Contacto c WHERE c.apodo = :apodo"),
    @NamedQuery(name = "Contacto.findByCargo", query = "SELECT c FROM Contacto c WHERE c.cargo = :cargo"),
    @NamedQuery(name = "Contacto.findByTelefono1", query = "SELECT c FROM Contacto c WHERE c.telefono1 = :telefono1"),
    @NamedQuery(name = "Contacto.findByTelefono2", query = "SELECT c FROM Contacto c WHERE c.telefono2 = :telefono2"),
    @NamedQuery(name = "Contacto.findByEmail", query = "SELECT c FROM Contacto c WHERE c.email = :email"),
    @NamedQuery(name = "Contacto.findByOtro", query = "SELECT c FROM Contacto c WHERE c.otro = :otro")})
public class Contacto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "apellido")
    private String apellido;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apodo")
    private String apodo;
    @Column(name = "cargo")
    private String cargo;
    @Column(name = "telefono1")
    private String telefono1;
    @Column(name = "telefono2")
    private String telefono2;
    @Column(name = "email")
    private String email;
    @Column(name = "otro")
    private String otro;
    @JoinColumn(name = "liga_id", referencedColumnName = "id")
    @ManyToOne
    private Liga ligaId;
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    @ManyToOne
    private Club clubId;
    @JoinColumn(name = "asociacion_id", referencedColumnName = "id")
    @ManyToOne
    private Asociacion asociacionId;

    public Contacto() {
    }

    public Contacto(Integer id) {
        this.id = id;
    }

    public Contacto(Integer id, String apellido, String nombre) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) throws UnsupportedEncodingException {
        this.apellido = new String(apellido.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws UnsupportedEncodingException{
        this.nombre = new String(nombre.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) throws UnsupportedEncodingException{
        this.apodo = new String(apodo.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) throws UnsupportedEncodingException{
        this.cargo = new String(cargo.getBytes(LittleLiguesUtils.ISO8859), LittleLiguesUtils.UTF8);
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

    public String getOtro() {
        return otro;
    }

    public void setOtro(String otro) {
        this.otro = otro;
    }

    public Liga getLigaId() {
        return ligaId;
    }

    public void setLigaId(Liga ligaId) {
        this.ligaId = ligaId;
    }

    public Club getClubId() {
        return clubId;
    }

    public void setClubId(Club clubId) {
        this.clubId = clubId;
    }

    public Asociacion getAsociacionId() {
        return asociacionId;
    }

    public void setAsociacionId(Asociacion asociacionId) {
        this.asociacionId = asociacionId;
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
        if (!(object instanceof Contacto)) {
            return false;
        }
        Contacto other = (Contacto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.liga.Contacto[ id=" + id + " ]";
    }
    
}
