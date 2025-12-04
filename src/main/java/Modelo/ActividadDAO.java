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
public class ActividadDAO {
    
    // Funciones CRUD
    
    // Devuelve todas las actividades de la base de datos
    static ArrayList<Actividad> listaActividadesHQL(Session sesion) {
        Query consulta = sesion.createQuery("SELECT a FROM Actividad a", Actividad.class);
        ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.list();
        return actividades;
    }
    
    // Permite insertar una actividad nueva o actualizar alguna ya existente
    public void insertaActualizaActividad(Session sesion, Actividad actividad) throws Exception {
        sesion.saveOrUpdate(actividad);
    }
    
    // Devuelve true si la actividad con ID pasado por parámetro se encuentra en la base de datos,
    // si no es asi, devuelve false
    public boolean existeActividad(Session sesion, String actividad) {
        Actividad act = sesion.get(Actividad.class, actividad);
        if (act == null) 
            return false;
        else 
            return true;
    }
    
    // Devuelve la actividad cuyo ID se pasa por parámetro
    public Actividad devuelveActividad(Session sesion, String actividad) {
        Actividad act = sesion.get(Actividad.class, actividad);
        return act;
    }
    
    // Devuelve la lista de actividades (su ID, nombre, descrpción, precio y monitor responsable)
    // a las que está apuntado el socio cuyo ID se pasa por parámetro
    static public ArrayList<Object[]> listaActividades(Session sesion, String id){
        Query consulta = sesion.createNativeQuery("SELECT a.idActividad, a.nombre, a.descripcion, a.precioBaseMes, a.monitorResponsable FROM ACTIVIDAD a INNER JOIN REALIZA r ON (r.idActividad = a.idActividad) INNER JOIN SOCIO s ON (s.numeroSocio = r.numeroSocio) WHERE s.numeroSocio = :id").setParameter("id", id);
        ArrayList<Object[]> actividades = (ArrayList<Object[]>) consulta.getResultList();
        return actividades;
    }
    
    // Devuelve la lista de actividades (su ID, nombre, descrpción, precio y monitor responsable)
    // a las que no está apuntado el socio (Actividades totales - Actividades a las que está apuntado)
    static public ArrayList<Actividad> listaActividadesNoApuntadas(Session sesion, ArrayList<Object[]> lAct) {
        ArrayList<Actividad> lista = listaActividadesHQL(sesion);
        for(Object[] actividad : lAct) {
            lista.remove(sesion.get(Actividad.class, (String)actividad[0]));
        }
        return lista;
    }
    
    // Da de alta el socio con ID pasado por parámetro en la actividad
    // con ID también pasado por parámetro
    public void darDeAlta(Session sesion, String act, String idS) {
        Actividad actividad = sesion.get(Actividad.class, act);
        actividad.altaSocio(sesion.get(Socio.class, idS));
        sesion.saveOrUpdate(actividad);
    }
    
    // Da de baja el socio con ID pasado por parámetro en la actividad
    // con ID también pasado por parámetro
    public void darDeBaja(Session sesion, String act, String idS) {
        Actividad actividad = sesion.get(Actividad.class, act);
        actividad.bajaSocio(sesion.get(Socio.class, idS));
        sesion.saveOrUpdate(actividad);
    }
}
