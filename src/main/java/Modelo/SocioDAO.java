/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;


import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Antón
 */
public class SocioDAO {
    
    // FUNCIONES CRUD
    
    // Devuelve la lista completa de socios registrados en la base de datos
    // mediante comandos HQL
    public ArrayList<Socio> listaSociosHQL(Session sesion) {
        Query consulta = sesion.createQuery("SELECT s FROM Socio s", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        return socios;
    }
    
    // Devuelve la lista completa de socios registrados en la base de datos
    // mediante comandos SQL
    public ArrayList<Socio> listaSociosSQL(Session sesion) {
        Query consulta = sesion.createNativeQuery ("SELECT * FROM SOCIO s", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        return socios;
    }
    
    // Devuelve la lista completa de socios registrados en la base de datos
    // mediante consulta nombrada
    public ArrayList<Socio> listaSociosNombrada(Session sesion) {
        Query consulta = sesion.createNamedQuery ("Socio.findAll", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        return socios;
    }
    
    // Devuelve una lista con todos los socios cuyo nombre coincida con el que es
    // pasado por parámetro
    public ArrayList<Socio> listaSociosNombre(Session sesion, String nombre) {
        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO s WHERE s.nombre = :nom", Socio.class);
        consulta.setParameter("nom", nombre);
        
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        return socios;
    }
    
    // Devuelve una lista con todos los socios cuyo DNI coincida con el que es
    // pasado por parámetro
    public ArrayList<Socio> listaSociosDNI(Session sesion, String dni) {
        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO s WHERE s.dni = :dnii", Socio.class);
        consulta.setParameter("dnii", dni);
        
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        return socios;
    }
    
    // Devuelve una lista con todos los socios cuya categoría coincida con la que es
    // pasada por parámetro
    public ArrayList<Socio> listaSociosCategoria(Session sesion, String categoria) {
        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO s WHERE s.categoria = :cat", Socio.class);
        consulta.setParameter("cat", categoria);
        
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();
        return socios;
    }
    
    // Registra un nuevo socio en la base de datos o actualiza uno ya existente
    public void insertaActualizaSocio(Session sesion, Socio socio) throws Exception {
            sesion.saveOrUpdate(socio);     
    }
    
    // Elimina un socio de la base de datos cuyo ID coincide con el que está pasado por parámetro
    // si lo encuentra y lo elimina devuelve true, si no devuelve false
    public boolean eliminaSocio(Session sesion, String idSocio) throws Exception {
        Socio soc = sesion.get(Socio.class, idSocio);
        if (soc == null) 
            return false;
        else {
            sesion.delete(soc);
            return true;
        }
                
    }
    
    // Devuelve true si existe un socio en la base de datos cuyo ID
    // sea el mismo que el que está pasado por parámetro, si no devuelve false
    public boolean existeSocio(Session sesion, String socio) {
        Socio soc = sesion.get(Socio.class, socio);
        if (soc == null) 
            return false;
        else 
            return true;
    }
    
    // Devuelve el socio de la base de datos cuyo ID coincide con el que
    // está pasado por parámetro
    public Socio devuelveSocio(Session sesion, String idSocio) {
        Socio soc = sesion.get(Socio.class, idSocio);
        return soc;
    }
    
    // Devuelve el código del último socio apuntado en la base de datos 
    static public ArrayList<String> ultimoSocio(Session sesion){
        Query consulta = sesion.createNativeQuery("SELECT MAX(numeroSocio) AS max_valor FROM SOCIO");
        ArrayList<String> numeroSocio = (ArrayList<String>) consulta.getResultList();
        return numeroSocio;
    }
    
}
