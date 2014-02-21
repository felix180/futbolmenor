package com.spontecorp.littleligues.model;

import com.spontecorp.littleligues.model.liga.Liga;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "noticia")
@NamedQueries({
    @NamedQuery(name = "Noticia.findAll", query = "SELECT n FROM Noticia n"),
    @NamedQuery(name = "Noticia.findById", query = "SELECT n FROM Noticia n WHERE n.id = :id"),
    @NamedQuery(name = "Noticia.findByLugar", query = "SELECT n FROM Noticia n WHERE n.lugar = :lugar"),
    @NamedQuery(name = "Noticia.findByFecha", query = "SELECT n FROM Noticia n WHERE n.fecha = :fecha"),
    @NamedQuery(name = "Noticia.findByAutor", query = "SELECT n FROM Noticia n WHERE n.autor = :autor"),
    @NamedQuery(name = "Noticia.findByPretitulo", query = "SELECT n FROM Noticia n WHERE n.pretitulo = :pretitulo"),
    @NamedQuery(name = "Noticia.findByTitulo", query = "SELECT n FROM Noticia n WHERE n.titulo = :titulo"),
    @NamedQuery(name = "Noticia.findByComentario", query = "SELECT n FROM Noticia n WHERE n.comentario = :comentario"),
    @NamedQuery(name = "Noticia.findByPrincipal", query = "SELECT n FROM Noticia n WHERE n.principal = :principal"),
    @NamedQuery(name = "Noticia.findLastNoticia", query = "SELECT n FROM Noticia n WHERE n.fecha IN ("
                                                        + "SELECT MAX(m.fecha) from Noticia m)")})
    
public class Noticia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "autor")
    private String autor;
    @Column(name = "pretitulo")
    private String pretitulo;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "comentario")
    private String comentario;
    @Basic(optional = false)
    @Lob
    @Column(name = "cuerpo")
    private String cuerpo;
    @Lob
    @Column(name = "foto")
    private String foto;
    @Column(name = "principal")
    private Integer principal;
    @JoinColumn(name = "liga_id", referencedColumnName = "id")
    @ManyToOne
    private Liga ligaId;

    public Noticia() {
    }

    public Noticia(Integer id) {
        this.id = id;
    }

    public Noticia(Integer id, String lugar, Date fecha, String autor, String titulo, String cuerpo) {
        this.id = id;
        this.lugar = lugar;
        this.fecha = fecha;
        this.autor = autor;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) throws UnsupportedEncodingException{
        this.lugar = new String(lugar.getBytes("ISO-8859-1"), "UTF-8");
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) throws UnsupportedEncodingException{
        this.autor = new String(autor.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getPretitulo() {
        return pretitulo;
    }

    public void setPretitulo(String pretitulo) throws UnsupportedEncodingException{
        this.pretitulo = new String(pretitulo.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws UnsupportedEncodingException{
        this.titulo = new String(titulo.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) throws UnsupportedEncodingException {
        this.comentario = new String(comentario.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) throws UnsupportedEncodingException {
        cuerpo = new String(cuerpo.getBytes("ISO-8859-1"), "UTF-8");
        this.cuerpo = cuerpo;
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
        if (!(object instanceof Noticia)) {
            return false;
        }
        Noticia other = (Noticia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.littleligues.model.Noticia[ id=" + id + " ]";
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getPrincipal() {
        return principal;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    public Liga getLigaId() {
        return ligaId;
    }

    public void setLigaId(Liga ligaId) {
        this.ligaId = ligaId;
    }
    
}
