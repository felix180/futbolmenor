package com.spontecorp.littleligues.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "cronista")
@NamedQueries({
    @NamedQuery(name = "Cronista.findAll", query = "SELECT c FROM Cronista c"),
    @NamedQuery(name = "Cronista.findById", query = "SELECT c FROM Cronista c WHERE c.id = :id"),
    @NamedQuery(name = "Cronista.findByApellido", query = "SELECT c FROM Cronista c WHERE c.apellido = :apellido"),
    @NamedQuery(name = "Cronista.findByNombre", query = "SELECT c FROM Cronista c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cronista.findByAlias", query = "SELECT c FROM Cronista c WHERE c.alias = :alias"),
    @NamedQuery(name = "Cronista.findByColumna", query = "SELECT c FROM Cronista c WHERE c.columna = :columna"),
    @NamedQuery(name = "Cronista.findByCelular", query = "SELECT c FROM Cronista c WHERE c.celular = :celular"),
    @NamedQuery(name = "Cronista.findByEmail", query = "SELECT c FROM Cronista c WHERE c.email = :email"),
    @NamedQuery(name = "Cronista.findByTwitter", query = "SELECT c FROM Cronista c WHERE c.twitter = :twitter"),
    @NamedQuery(name = "Cronista.findByTelefono", query = "SELECT c FROM Cronista c WHERE c.telefono = :telefono")})
public class Cronista implements Serializable {
    
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
    @Column(name = "alias")
    private String alias;
    @Column(name = "columna")
    private String columna;
    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "celular")
    private String celular;
    @Column(name = "email")
    private String email;
    @Column(name = "twitter")
    private String twitter;
    @Column(name = "telefono")
    private String telefono;
    @Lob
    @Column(name = "foto")
    private byte[] foto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cronistaId")
    private List<Cronica> cronicaList;

    public Cronista() {
    }

    public Cronista(Integer id) {
        this.id = id;
    }

    public Cronista(Integer id, String apellido, String nombre) {
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
        this.apellido = new String(apellido.getBytes("ISO-8859-1" ),"UTF-8");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws UnsupportedEncodingException{
        this.nombre = new String(nombre.getBytes("ISO-8859-1" ),"UTF-8");
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) throws UnsupportedEncodingException{
        this.alias = new String(alias.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) throws UnsupportedEncodingException{
        this.columna = new String(columna.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) throws UnsupportedEncodingException{
        this.descripcion = new String(descripcion.getBytes("ISO-8859-1"), "UTF-8");
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Cronica> getCronicaList() {
        return cronicaList;
    }

    public void setCronicaList(List<Cronica> cronicaList) {
        this.cronicaList = cronicaList;
    }
    
    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
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
        if (!(object instanceof Cronista)) {
            return false;
        }
        Cronista other = (Cronista) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }

    
    
}
