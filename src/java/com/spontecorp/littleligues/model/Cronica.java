package com.spontecorp.littleligues.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "cronica")
@NamedQueries({
    @NamedQuery(name = "Cronica.findAll", query = "SELECT c FROM Cronica c"),
    @NamedQuery(name = "Cronica.findById", query = "SELECT c FROM Cronica c WHERE c.id = :id"),
    @NamedQuery(name = "Cronica.findByTitulo", query = "SELECT c FROM Cronica c WHERE c.titulo = :titulo"),
    @NamedQuery(name = "Cronica.findByFecha", query = "SELECT c FROM Cronica c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Cronica.findActive", query = "SELECT c FROM Cronica c WHERE c.activa = :activa")})

public class Cronica implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Lob
    @Column(name = "cuerpo")
    private String cuerpo;
    @JoinColumn(name = "cronista_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cronista cronistaId;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @Basic(optional = false)
    @Column(name = "activa")
    private Integer activa;

    public Cronica() {
    }

    public Cronica(Integer id) {
        this.id = id;
    }

    public Cronica(Integer id, String titulo, Date fecha, String cuerpo) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.cuerpo = cuerpo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws UnsupportedEncodingException{
        this.titulo = new String(titulo.getBytes("ISO-8859-1"),"UTF-8");
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) throws UnsupportedEncodingException{
        this.cuerpo = new String(cuerpo.getBytes("ISO-8859-1"),"UTF-8");
    }

    public Cronista getCronistaId() {
        return cronistaId;
    }

    public void setCronistaId(Cronista cronistaId) {
        this.cronistaId = cronistaId;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Integer getActiva() {
        return activa;
    }

    public void setActiva(Integer activa) {
        this.activa = activa;
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
        if (!(object instanceof Cronica)) {
            return false;
        }
        Cronica other = (Cronica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.Cronica[ id=" + id + " ]";
    }

    
    
}
