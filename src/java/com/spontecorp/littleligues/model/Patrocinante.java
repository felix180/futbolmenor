/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model;

import com.spontecorp.littleligues.utils.LittleLiguesUtils;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "patrocinante")
@NamedQueries({
    @NamedQuery(name = "Patrocinante.findAll", query = "SELECT p FROM Patrocinante p"),
    @NamedQuery(name = "Patrocinante.findById", query = "SELECT p FROM Patrocinante p WHERE p.id = :id"),
    @NamedQuery(name = "Patrocinante.findByNombre", query = "SELECT p FROM Patrocinante p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Patrocinante.findByStatus", query = "SELECT p FROM Patrocinante p WHERE p.status = :status")})
public class Patrocinante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "logo")
    private byte[] logo;
    @Lob
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;

    public Patrocinante() {
    }

    public Patrocinante(Integer id) {
        this.id = id;
    }

    public Patrocinante(Integer id, String nombre, int status) {
        this.id = id;
        this.nombre = nombre;
        this.status = status;
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
        this.nombre = new String(nombre.getBytes(LittleLiguesUtils.ISO8859),LittleLiguesUtils.UTF8);
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getComentario(){
        return comentario;
    }

    public void setComentario(String comentario) throws UnsupportedEncodingException{
        this.comentario = new String(comentario.getBytes(LittleLiguesUtils.ISO8859),LittleLiguesUtils.UTF8);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        if (!(object instanceof Patrocinante)) {
            return false;
        }
        Patrocinante other = (Patrocinante) object;
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
