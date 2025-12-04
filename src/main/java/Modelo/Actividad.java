/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Antón
 */
@Entity
@Table(name = "ACTIVIDAD")
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findByIdActividad", query = "SELECT a FROM Actividad a WHERE a.idActividad = :idActividad"),
    @NamedQuery(name = "Actividad.findByNombre", query = "SELECT a FROM Actividad a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Actividad.findByDescripcion", query = "SELECT a FROM Actividad a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "Actividad.findByPrecioBaseMes", query = "SELECT a FROM Actividad a WHERE a.precioBaseMes = :precioBaseMes")})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idActividad")
    private String idActividad;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "precioBaseMes")
    private int precioBaseMes;
    @JoinTable(name = "REALIZA", joinColumns = {
        @JoinColumn(name = "idActividad", referencedColumnName = "idActividad")}, inverseJoinColumns = {
        @JoinColumn(name = "numeroSocio", referencedColumnName = "numeroSocio")})
    @ManyToMany
    private Set<Socio> socios = new HashSet<Socio>();
    @JoinColumn(name = "monitorResponsable", referencedColumnName = "codMonitor")
    @ManyToOne
    private Monitor monitorResponsable;

    public Actividad() {
    }

    public Actividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public Actividad(String idActividad, String nombre, int precioBaseMes) {
        this.idActividad = idActividad;
        this.nombre = nombre;
        this.precioBaseMes = precioBaseMes;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecioBaseMes() {
        return precioBaseMes;
    }

    public void setPrecioBaseMes(int precioBaseMes) {
        this.precioBaseMes = precioBaseMes;
    }

    public Set<Socio> getSocios() {
        return socios;
    }

    public void setSocios(Set<Socio> socios) {
        this.socios = socios;
    }

    public Monitor getMonitorResponsable() {
        return monitorResponsable;
    }

    public void setMonitorResponsable(Monitor monitorResponsable) {
        this.monitorResponsable = monitorResponsable;
    }
    
    public void altaSocio(Socio socio) {
        this.socios.add(socio);
        socio.getActividades().add(this);
    }
    
    public void bajaSocio(Socio socio) {
        this.socios.remove(socio);
        socio.getActividades().remove(this);
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idActividad != null ? idActividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.idActividad == null && other.idActividad != null) || (this.idActividad != null && !this.idActividad.equals(other.idActividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Actividad[ idActividad=" + idActividad + " ]";
    }
    
}
