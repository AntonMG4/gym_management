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
public class ActividadDAO {
       
    static public ArrayList<Object[]> listaActividades(Session sesion, String cod){ //DEVUELVE UNA LISTA CON LAS ACTIVIDADES A LAS QUE ESTA APUNTADO EL SOCIO PASADO POR PARÁMETROS
        Query consulta = sesion.createNativeQuery("SELECT a.idActividad, a.nombre, a.descripcion, a.precioBaseMes, a.monitorResponsable FROM ACTIVIDAD a INNER JOIN REALIZA r ON (r.idActividad = a.idActividad) INNER JOIN SOCIO s ON (s.numeroSocio = r.numeroSocio) WHERE s.numeroSocio = :id").setParameter("id", cod);
        ArrayList<Object[]> actividades = (ArrayList<Object[]>) consulta.getResultList();
        return actividades;
    }
    
    static public ArrayList<Actividad> listaActividadesHQL(Session sesion) throws Exception{//DEVUELVE UNA LISTA CON TODAS LAS ACTIVIDADES
        Query consulta = sesion.createQuery("SELECT a FROM Actividad a", Actividad.class);
        ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.getResultList();
        return actividades;
    }
    
    static public Actividad buscarActividad(Session sesion, String codAct){//DEVUELVE LA ACTIVIDAD CORRESPONDIENTE AL CODIGO PASADO POR PARAMETROS
        Actividad a = sesion.get(Actividad.class,codAct);
        return a;
    }
}
