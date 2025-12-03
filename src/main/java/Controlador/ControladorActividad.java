/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Socio;
import Vista.AltaActividad;
import Vista.BajaActividad;
import Vista.UtilTablasActividad;
import Vista.VistaActividades;
import Vista.VistaMensajes;
import java.awt.Dialog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JDialog;

/**
 *
 * @author alberto
 */
public class ControladorActividad implements ActionListener{
    private Session sesion;
    private final SessionFactory sessionFactory;
    private Transaction tr;
    private final VistaActividades vActividad;
    private final VistaMensajes vMensaje;
    private final AltaActividad vAlta;
    private final BajaActividad vBaja;
    private UtilTablasActividad uTablaA;
    private String cod="", nombre="";
    
    public ControladorActividad(SessionFactory sessionFactory, VistaActividades vActividad){
        this.sessionFactory = sessionFactory;
        this.vActividad = vActividad;
        vMensaje = new VistaMensajes();
        vAlta = new AltaActividad();
        vBaja = new BajaActividad();
     
        addListeners();
    }
    
    private void inicializar(JDialog objeto){
        objeto.setLocationRelativeTo(null);
        objeto.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        objeto.setResizable(false);
        objeto.setVisible(true);
    }
    
    private void addListeners(){
        vActividad.BotonAltaActividad.addActionListener(this);
        vActividad.BotonBajaActividad.addActionListener(this);
        vAlta.BotonDarDeAlta.addActionListener(this);
        vAlta.BotonCancelarAlta.addActionListener(this);
        vBaja.BotonDarDeBaja.addActionListener(this);
        vBaja.BotonCancelarBaja.addActionListener(this);
    }
    
    private ArrayList<Actividad> ActNoApuntadas(Session sesion, ArrayList<Object[]> lActividades) throws Exception{
        ArrayList<Actividad> lista = ActividadDAO.listaActividadesHQL(sesion);
        for(Object[] actividad : lActividades){
            lista.remove(ActividadDAO.buscarActividad(sesion, (String)actividad[0]));
        }
        return lista;
    }
    
    private void rellenarAlta(UtilTablasActividad tabla, String cod) throws Exception{
        tabla = new UtilTablasActividad(vAlta);
        tabla.dibujarTablaActividades(vAlta);
        ArrayList<Object[]> lActividades = ActividadDAO.listaActividades(sesion, cod);
        tabla.vaciarTablaActividades();
        tabla.rellenarTablaActividadesAlta(ActNoApuntadas(sesion, lActividades));
    }
    
    private void rellenarBaja(UtilTablasActividad tabla, String cod){
        tabla = new UtilTablasActividad(vBaja);
        tabla.dibujarTablaActividades(vBaja);
        ArrayList<Object[]> lActividades = ActividadDAO.listaActividades(sesion, cod);
        tabla.vaciarTablaActividades();
        tabla.rellenarTablaActividadesBaja(lActividades);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "AltaActividad":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vActividad.jTableSocios.getSelectedRow();
                try{
                    if(fila!=-1){
                        cod = (String)vActividad.jTableSocios.getValueAt(fila, 0);
                        nombre = (String)vActividad.jTableSocios.getValueAt(fila, 1);
                        rellenarAlta(uTablaA, cod);
                        inicializar(vAlta);
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vActividad);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo actualizar la actividad.\n" + ex.getMessage(), vActividad);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vActividad.jTableSocios.clearSelection();
                    break;
                }
            }
            case "DarDeAlta":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vAlta.jTableActividades.getSelectedRow();
                try{
                    if(fila!=-1){
                        String act = (String)vAlta.jTableActividades.getValueAt(fila, 0);
                        String nom = (String)vAlta.jTableActividades.getValueAt(fila, 1);
                        Actividad actividad = sesion.get(Actividad.class, act);
                        actividad.altaSocio(sesion.get(Socio.class, cod));
                        sesion.saveOrUpdate(actividad);
                        
                        vMensaje.Mensaje("¡" + nombre + " fue dado de alta en " + nom + " con éxito!", vAlta);
                        vAlta.dispose();
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vAlta);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo actualizar la actividad.\n" + ex.getMessage(), vActividad);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vActividad.jTableSocios.clearSelection();
                    break;
                }
            }
            case "CancelarDarDeAlta":{
                vActividad.jTableSocios.clearSelection();
                vAlta.dispose();
                break;
            }
            case "BajaActividad":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vActividad.jTableSocios.getSelectedRow();
                try{
                    if(fila!=-1){
                        cod = (String)vActividad.jTableSocios.getValueAt(fila, 0);
                        nombre = (String)vActividad.jTableSocios.getValueAt(fila, 1);
                        rellenarBaja(uTablaA, cod);
                        inicializar(vBaja);
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vActividad);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo actualizar la actividad.\n" + ex.getMessage(), vActividad);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vActividad.jTableSocios.clearSelection();
                    break;
                }
            }
            case "DarDeBaja":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vBaja.jTableActividades.getSelectedRow();
                try{
                    if(fila!=-1){
                        String act = (String)vBaja.jTableActividades.getValueAt(fila, 0);
                        String nom = (String)vBaja.jTableActividades.getValueAt(fila, 1);
                        Actividad actividad = sesion.get(Actividad.class, act);
                        actividad.bajaSocio(sesion.get(Socio.class, cod));
                        sesion.saveOrUpdate(actividad);
                        
                        vMensaje.Mensaje("¡" + nombre + " fue dado de baja en " + nom + " con éxito!", vBaja);
                        vBaja.dispose();
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vBaja);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo actualizar la actividad.\n" + ex.getMessage(), vActividad);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vActividad.jTableSocios.clearSelection();
                    break;
                }
            }
            case "CancelarDarDeBaja":{
                vActividad.jTableSocios.clearSelection();
                vBaja.dispose();
                break;
            }
        }
    }
}
