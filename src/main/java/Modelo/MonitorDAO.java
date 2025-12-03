/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import javax.persistence.Query;
import org.hibernate.Session;

/**
 *
 * @author alber
 */
public class MonitorDAO {
    
    static public ArrayList<Object[]> ResponsableAct(Session sesion, String idActividad){ //DEVUELVE EL MONITOR RESPONSABLE DE UNA ACTIVIDAD
        Query consulta = sesion.createNativeQuery("SELECT m.* FROM MONITOR m INNER JOIN ACTIVIDAD a ON (a.monitorResponsable = m.codMonitor) WHERE a.idActividad = :idActividad").setParameter("idActividad", idActividad);
        ArrayList<Object[]> monitor = (ArrayList<Object[]>) consulta.getResultList();
        return monitor;
    }
    
    static public ArrayList<Monitor> listaMonitores(Session sesion){ //DEVUELVE UNA LISTA CON TODOS LOS MONITORES
        Query consulta = sesion.createQuery("SELECT m FROM Monitor m", Monitor.class);
        ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.getResultList();
        return monitores;
    }
    
    static public ArrayList<String> ultimoMonitor(Session sesion){ //DEVUELVE EL MONITOR CON EL CODMONITOR MÁS ALTO
        Query consulta = sesion.createNativeQuery("SELECT MAX(codMonitor) AS max_valor FROM MONITOR");
        ArrayList<String> codMonitor = (ArrayList<String>) consulta.getResultList();
        return codMonitor;
    }
    
    static public void alta_actu_Monitor(Session sesion, Monitor monitor) throws Exception{//ACTUALIZA O AÑADE EL MONITOR PASADO POR PARÁMETROS
            sesion.saveOrUpdate(monitor);
    }
    
    static public void bajaMonitor(Session sesion, String numMonitor) throws Exception{//ELIMINA UN MONITOR DE LA BD
            sesion.delete(sesion.get(Monitor.class, numMonitor));
    }
    
    
    
}
