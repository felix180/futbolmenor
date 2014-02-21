/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.littleligues.model;

import com.spontecorp.littleligues.model.liga.Liga;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "video")
@NamedQueries({
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v"),
    @NamedQuery(name = "Video.findById", query = "SELECT v FROM Video v WHERE v.id = :id"),
    @NamedQuery(name = "Video.findByDescripcion", query = "SELECT v FROM Video v WHERE v.descripcion = :descripcion"),
    @NamedQuery(name = "Video.findByFecha", query = "SELECT v FROM Video v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "Video.findByUrl", query = "SELECT v FROM Video v WHERE v.url = :url")})
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    
    @JoinColumn(name = "liga_id", referencedColumnName = "id")
    @ManyToOne
    private Liga ligaId;

    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    
    private static final Logger logger = LoggerFactory.getLogger(Video.class);
    

    public Video() {
    }

    public Video(Integer id) {
        this.id = id;
    }

    public Video(Integer id, String url, String titulo) {
        this.id = id;
        this.url = url;
        this.titulo = titulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Liga getLigaId() {
        return ligaId;
    }

    public void setLigaId(Liga ligaId) {
        this.ligaId = ligaId;
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
        if (!(object instanceof Video)) {
            return false;
        }
        Video other = (Video) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.Video[ id=" + id + " ]";
    }
}
