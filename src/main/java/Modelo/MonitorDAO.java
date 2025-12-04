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
public class MonitorDAO {
    
    // FUNCIONES CRUD
    
    // Devuelve el monitor responsable de la actividad cuyo ID se pasa por parámetro
     public Monitor monitorResponsable(Session sesion, String idAct) {
        Query consulta = sesion.createQuery("SELECT m FROM Actividad a INNER JOIN Monitor m ON a.monitorResponsable = m.codMonitor WHERE a.idActividad = :idActiv");
        consulta.setParameter("idActiv", idAct);
        
        Monitor monitor = (Monitor)consulta.getSingleResult();
        return monitor;
    }
    
    // Devuelve la lista completa de monitores registrados en la base de datos
    // mediante HQL
    public ArrayList<Monitor> listaMonitores(Session sesion) {
        Query consulta = sesion.createQuery("SELECT m FROM Monitor m", Monitor.class);
        ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.list();
        return monitores;
    }
    
    // Devuelve el código del último monitor apuntado en la base de datos
    static public ArrayList<String> ultimoMonitor(Session sesion){
        Query consulta = sesion.createNativeQuery("SELECT MAX(codMonitor) AS max_valor FROM MONITOR");
        ArrayList<String> codMonitor = (ArrayList<String>) consulta.getResultList();
        return codMonitor;
    }
    
    // Elimina el monitor de la base de datos cuyo ID coincide con el que se pasa por parámetro,
    // devuelve true si se ha conseguido eliminar y false si no (no se ha encontrado en la base de datos)
    public boolean eliminaMonitor(Session sesion, String idMonitor) throws Exception {
        Monitor mon = sesion.get(Monitor.class, idMonitor);
        if (mon == null) 
            return false;
        else {
            sesion.delete(mon);
            return true;
        }
                
    }
    
    // Inserta un nuevo monitor en la base de datos o actualiza uno ya existente
    public void insertaActualizaMonitor(Session sesion, Monitor monitor) throws Exception {
            sesion.saveOrUpdate(monitor);
    }
}
