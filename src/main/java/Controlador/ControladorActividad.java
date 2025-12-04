/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.UtilTablasActividad;
import Vista.VistaCRUDActividadAlta;
import Vista.VistaCRUDActividadBaja;
import Vista.VistaMensajes;
import Vista.vActividad;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Antón
 */
public class ControladorActividad implements ActionListener {
    private final SessionFactory sessionFactory;
    private Session sesion;
    private final vActividad vActividad;
    private final VistaMensajes vm = new VistaMensajes();
    private Transaction tr;
    private final VistaCRUDActividadAlta vAlta;
    private final VistaCRUDActividadBaja vBaja;
    private final SocioDAO sDAO = new SocioDAO();
    private final ActividadDAO aDAO = new ActividadDAO();
    private final UtilTablasActividad uTablasA;
    private String id = null;
    
    public ControladorActividad(SessionFactory sessionFactory, vActividad vActividad, UtilTablasActividad uTablasA) {
        this.sessionFactory = sessionFactory;
        sesion = this.sessionFactory.openSession();
        this.vActividad = vActividad;
        vAlta = new VistaCRUDActividadAlta();
        vBaja = new VistaCRUDActividadBaja();
        this.uTablasA = uTablasA;
        addListeners();
    }
    
    private void addListeners() {
        vActividad.altaActividad.addActionListener(this);
        vActividad.bajaActividad.addActionListener(this);
        vAlta.aceptarAlta.addActionListener(this);
        vAlta.cancelarAlta.addActionListener(this);
        vBaja.aceptarBaja.addActionListener(this);
        vBaja.cancelarBaja.addActionListener(this);
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "BajaActividad":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    if(vActividad.jTableSocios.getSelectedRow() != -1) {
                    uTablasA.inicializarTablaActividades(vBaja);
                    vBaja.setLocationRelativeTo(null);
                    vBaja.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vBaja.setResizable(false);
                    uTablasA.dibujarTablaActividades(vBaja);
                    uTablasA.vaciarTablaActividades();
                    int fila = vActividad.jTableSocios.getSelectedRow();
                    id = (String) vActividad.jTableSocios.getValueAt(fila, 0);
                    uTablasA.rellenarTablaActividadesBaja(ActividadDAO.listaActividades(sesion, id));
                    tr.commit();
                    vBaja.setVisible(true);
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vActividad);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la creación de la tabla de actividades\n"+ex.getMessage(), vActividad);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            
            case "AltaActividad":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    if(vActividad.jTableSocios.getSelectedRow() != -1) {
                    uTablasA.inicializarTablaActividades(vAlta);
                    vAlta.setLocationRelativeTo(null);
                    vAlta.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vAlta.setResizable(false);
                    uTablasA.dibujarTablaActividades(vAlta);
                    uTablasA.vaciarTablaActividades();
                    int fila = vActividad.jTableSocios.getSelectedRow();
                    id = (String) vActividad.jTableSocios.getValueAt(fila, 0);
                    uTablasA.rellenarTablaActividadesAlta(ActividadDAO.listaActividadesNoApuntadas(sesion, ActividadDAO.listaActividades(sesion, id)));
                    tr.commit();
                    vAlta.setVisible(true);
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vActividad);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la creación de la tabla de actividades\n"+ex.getMessage(), vActividad);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
                
            case "aceptarAlta":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vAlta.jTableAlta.getSelectedRow();
                try {
                    if(fila != -1) {
                    String act = (String)vAlta.jTableAlta.getValueAt(fila, 0);
                    aDAO.darDeAlta(sesion, act, id);
                    tr.commit();
                    vm.Mensaje("Socio dado de alta en la actividad correctamente", vAlta);
                    vAlta.dispose();
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vActividad);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error dando de alta en la actividad\n"+ex.getMessage(), vActividad);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
                
            case "cancelarAlta":
                vAlta.dispose();
                break;
                
            case "aceptarBaja":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila2 = vBaja.jTableBaja.getSelectedRow();
                try {
                    if(fila2 != -1) {
                    String act = (String)vBaja.jTableBaja.getValueAt(fila2, 0);
                    aDAO.darDeBaja(sesion, act, id);
                    tr.commit();
                    vm.Mensaje("Socio dado de baja en la actividad correctamente", vBaja);
                    vBaja.dispose();
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vActividad);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error dando de baja en la actividad\n"+ex.getMessage(), vActividad);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
                
            case "cancelarBaja":
                vBaja.dispose();
                break;
        }
    }
    
    
}
