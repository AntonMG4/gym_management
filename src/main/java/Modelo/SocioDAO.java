/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.Set;
import javax.persistence.Query;
import org.hibernate.Session;

/**
 *
 * @author alber
 */
public class SocioDAO {
    
    static public boolean existeSocio(Session sesion, String numSocio) throws Exception{//DEVUELVE TRUE SI EL SOCIO PASADO POR PARÁMETROS EXISTE
        Socio s = sesion.get(Socio.class, numSocio);
        return (s!=null);
    }
    
    static public ArrayList<Socio> listaSociosHQL(Session sesion) throws Exception{//DEVUELVE UNA LISTA CON TODOS LOS SOCIOS
        Query consulta = sesion.createQuery("SELECT s FROM Socio s", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.getResultList();
        return socios;
    } 
    
    static public ArrayList<Socio> listaSociosSQLNativo(Session sesion) throws Exception{//DEVUELVE UNA LISTA CON TODOS LOS SOCIOS
        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO S", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.getResultList();
        return socios;
    }
    
    static public ArrayList<Socio> listaSociosNombrada(Session sesion) throws Exception{//DEVUELVE UNA LISTA CON TODOS LOS SOCIOS
        Query consulta = sesion.createNamedQuery("Socio.findAll", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.getResultList();
        return socios;
    }
    
    static public ArrayList<Object[]> listaNom_y_TLF(Session sesion) throws Exception{//DEVUELVE EL NOMBRE Y TELÉFONO DE TODOS LOS SOCIOS
        Query consulta = sesion.createQuery("SELECT s.nombre, s.telefono FROM Socio s");
        ArrayList<Object[]> socios = (ArrayList<Object[]>) consulta.getResultList();
        return socios;
    }
    
    static public ArrayList<Object[]> listaNom_y_Categ(Session sesion, Character cat) throws Exception{//DEVUELVE EL NOMBRE Y CATEGORÍA DE TODOS LOS SOCIOS
        Query consulta = sesion.createQuery("SELECT s.nombre, s.categoria FROM Socio s WHERE s.categoria= :cat").setParameter("cat", cat);
        ArrayList<Object[]> socios = (ArrayList<Object[]>) consulta.getResultList();
        return socios;
    }
    
    static public ArrayList<String> listaActividadesSocio(Session sesion, String idActividad) throws Exception {//DEVUELVE LOS NOMBRE DE LOS SOCIOS APUNTADOS A LA ACTIVIDAD PASADA POR PARÁMETROS
        Query consulta = sesion.createNativeQuery("SELECT s.nombre FROM SOCIO s INNER JOIN REALIZA r ON s.numeroSocio = r.numeroSocio INNER JOIN ACTIVIDAD a ON r.idActividad = a.idActividad WHERE a.idActividad = :act").setParameter("act", idActividad);
        ArrayList<String> socios = (ArrayList<String>) consulta.getResultList();
        return socios;
    }
    
    static public void alta_actu_Socio(Session sesion, Socio socio) throws Exception{//ACTUALIZA O AÑADE EL SOCIO PASADO POR PARÁMETROS
            sesion.saveOrUpdate(socio);
    }
    
    static public void bajaSocio(Session sesion, String numSocio) throws Exception{//ELIMINA EL MONITOR PASADO POR PARÁMETROS
        sesion.delete(sesion.get(Socio.class, numSocio));
    }
    
    static public Set<Actividad> listaSocios(Socio socio){//DEVUELVE UNA LISTA CON TODAS LAS ACTIVIDADES A LAS QUE ESTA APUNTADO EL SOCIO PASADO POR PARÁMETROS
        Set<Actividad> actividades = socio.getActividades();
        return actividades;
    } 
    
    static public ArrayList<String> ultimoSocio(Session sesion){//DEVUELVE EL SOCIO CON EL NUMEROSOCIO MÁS ALTO
        Query consulta = sesion.createNativeQuery("SELECT MAX(numeroSocio) AS max_valor FROM SOCIO");
        ArrayList<String> numeroSocio = (ArrayList<String>) consulta.getResultList();
        return numeroSocio;
    }
    
    static public ArrayList<Socio> busquedaSocio(Session sesion, int i, String busqueda){//DEVUELVE EL GRUPO DE SOCIOS QUE CUMPLEN CON EL VALOR DE UN CAMPO DETERMINADO
        ArrayList<Socio> socios = null; 
        switch(i){
            case 1:{
                Query consulta = sesion.createQuery("SELECT s FROM Socio s WHERE s.dni= :dni").setParameter("dni", busqueda);
                socios = (ArrayList<Socio>) consulta.getResultList();
                break;
            }
            case 2:{
                Query consulta = sesion.createQuery("SELECT s FROM Socio s WHERE s.nombre LIKE :nombre").setParameter("nombre", busqueda + "%");
                socios = (ArrayList<Socio>) consulta.getResultList();
                break;
            }
            case 3:{
                Query consulta = sesion.createQuery("SELECT s FROM Socio s WHERE s.categoria= :categoria").setParameter("categoria", busqueda.charAt(0));
                socios = (ArrayList<Socio>) consulta.getResultList();
                break;
            }
        }
        return socios;
    }
}
