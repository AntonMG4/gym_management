/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Monitor;
import Modelo.MonitorDAO;
import Vista.UtilTablasMonitor;
import Vista.VistaActuMonitor;
import Vista.VistaMensajes;
import Vista.VistaMonitores;
import Vista.VistaNuevoMonitor;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JDialog;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author alber
 */
public class ControladorMonitor implements ActionListener{
    private Session sesion;
    private final SessionFactory sessionFactory;
    private Transaction tr;
    private final VistaNuevoMonitor vNuevoMonitor;
    private final VistaMonitores vMonitor;
    private final VistaMensajes vMensaje;
    private final SimpleDateFormat formatoFecha;
    private final UtilTablasMonitor uTablasM;
    private final Date fechaActual;
    private final VistaActuMonitor vActuMonitor;
    
    public ControladorMonitor(SessionFactory sessionFactory, VistaMonitores vMonitor, UtilTablasMonitor uTablasM){
        this.sessionFactory = sessionFactory;
        this.vMonitor = vMonitor;
        this.uTablasM = uTablasM;
        vNuevoMonitor = new VistaNuevoMonitor();
        vActuMonitor = new VistaActuMonitor();
        vMensaje = new VistaMensajes();
        fechaActual = new Date();
        
        formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        addListeners();
    }
    
    private void inicializar(JDialog objeto){
        objeto.setLocationRelativeTo(null);
        objeto.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        objeto.setResizable(false);
        objeto.setVisible(true);
    }
    
    private void addListeners(){
        vMonitor.BotonNuevoMonitor.addActionListener(this);
        vMonitor.BotonBajaMonitor.addActionListener(this);
        vMonitor.BotonActuMonitor.addActionListener(this);
        vNuevoMonitor.BotonInsertar.addActionListener(this);
        vNuevoMonitor.BotonCancelarInsertar.addActionListener(this);
        vActuMonitor.BotonActualizar.addActionListener(this);
        vActuMonitor.BotonCancelarActualizar.addActionListener(this);
    }
    
    private String nuevoCodigo(){
        ArrayList<String> valor = MonitorDAO.ultimoMonitor(sesion);
        String codigo = valor.get(0);
        
        String prefijo = codigo.substring(0, codigo.length() - 3);
        int numero = Integer.parseInt(codigo.substring(codigo.length() - 3));
        numero++;

        codigo = prefijo + String.format("%03d", numero);
        
        return codigo;
    }
    
    public boolean validarDNI(String dni) {
        
        String patronDNI = "\\d{8}[A-HJ-NP-TV-Z]";

        Pattern pattern = Pattern.compile(patronDNI);
        Matcher matcher = pattern.matcher(dni);

        return matcher.matches();
    }
    
    public boolean validarCorreo(String correo) {
        
        if(correo.equals(""))
            return true;

        if (correo.length() < 7) {
            return false;
        }
        if (!correo.contains("@")) {
            return false;
        }

        String[] partes = correo.split("@");
        if (partes[0].length() < 3 || partes[1].length() < 3) {
            return false;
        }

        return true;
    }
    
    public boolean validarTelefono(String telefono) {
        if(telefono.equals(""))
            return true;
        if (telefono.length() != 9) {
            return false;
        }
        for (char c : telefono.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    
    private ArrayList<Monitor> pideMonitores() throws Exception{
        ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitores(sesion);
        return lMonitores;
    }
    
    private void updateTable() throws Exception{
         ArrayList<Monitor> lMonitores = pideMonitores();
         uTablasM.vaciarTablaMonitores();
         uTablasM.rellenarTablaMonitores(lMonitores);
    }
    
    private void vaciarCampos(){
        vNuevoMonitor.CampoCodMonitor.setText("");
        vNuevoMonitor.CampoNombre.setText("");
        vNuevoMonitor.CampoDNI.setText("");
        vNuevoMonitor.CampoTelefono.setText("");
        vNuevoMonitor.CampoCorreo.setText("");
        vNuevoMonitor.CampoFechaEntrada.setDate(fechaActual);
        vNuevoMonitor.CampoNick.setText("");
    }
    
    private void llenarCampos(String cod, String nombre, String dni, String telefono, String correo, String fecha, String nick){
        vActuMonitor.CampoCodMonitor.setText(cod);
        vActuMonitor.CampoNombre.setText(nombre);
        vActuMonitor.CampoDNI.setText(dni);
        vActuMonitor.CampoTelefono.setText(telefono);
        vActuMonitor.CampoCorreo.setText(correo);
        Date f = new Date();
        try {
            f = formatoFecha.parse(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(ControladorMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        vActuMonitor.CampoFechaEntrada.setDate(f);
        vActuMonitor.CampoNick.setText(nick);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "NuevoMonitor":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try{
                    vaciarCampos();
                    vNuevoMonitor.CampoCodMonitor.setText(nuevoCodigo());
                    inicializar(vNuevoMonitor);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: " + ex.getMessage(), vMonitor);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
            case "Insertar":{
                try{
                    sesion = sessionFactory.openSession();
                    tr = sesion.beginTransaction();
                    String dni = vNuevoMonitor.CampoDNI.getText();
                    String nombre = vNuevoMonitor.CampoNombre.getText();
                    String correo = vNuevoMonitor.CampoCorreo.getText();
                    String telefono = vNuevoMonitor.CampoTelefono.getText();
                    Date fechaChooser = vNuevoMonitor.CampoFechaEntrada.getDate();
                    if( dni.equals("") )
                        vMensaje.MensajeError("El campo 'DNI' no puede estar vacío.", vNuevoMonitor);
                        else if(!validarDNI(dni))
                            vMensaje.MensajeError("El DNI escrito no es válido.", vNuevoMonitor);
                            else if(nombre.equals(""))
                                vMensaje.MensajeError("El campo 'Nombre' no puede estar vacío.", vNuevoMonitor);
                                else if(!validarTelefono(telefono))
                                    vMensaje.MensajeError("El teléfono no cumple el patrón mínimo (9 dígitos)", vNuevoMonitor);
                                    else if(!validarCorreo(correo))
                                        vMensaje.MensajeError("El correo no cumple el patrón mínimo (xxx@xxx)", vNuevoMonitor);
                                        else if(fechaChooser.after(fechaActual))
                                            vMensaje.MensajeError("La fecha de entrada no puede ser más tardía que la fecha actual.", vNuevoMonitor);
                                            else{
                                                String cod = vNuevoMonitor.CampoCodMonitor.getText();
                                                String nick = vNuevoMonitor.CampoNick.getText();
                                                String fecha = formatoFecha.format(fechaChooser);

                                                Monitor monitor = new Monitor(cod, nombre, dni, telefono, correo, fecha, nick);
                                                MonitorDAO.alta_actu_Monitor(sesion, monitor);
                                                updateTable();
                                                vMensaje.Mensaje("¡Monitor añadido con éxito!", vNuevoMonitor);
                                                vNuevoMonitor.dispose();
                                            }
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo insertar la nueva tupla.\n" + ex.getMessage(), vMonitor);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
            case "CancelarInsertar":{
                vNuevoMonitor.dispose();
                break;
            }
            case "BajaMonitor":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vMonitor.jTableMonitores.getSelectedRow();
                try{
                    if(fila!=-1){
                        String cod = (String)vMonitor.jTableMonitores.getValueAt(fila, 0);
                        String nombre = (String)vMonitor.jTableMonitores.getValueAt(fila, 1);
                        int resp = vMensaje.Dialogo(vMonitor, "¿Seguro que quiere dar de baja a " + nombre +"?");
                        if(resp == JOptionPane.YES_OPTION){
                            MonitorDAO.bajaMonitor(sesion, cod);
                            updateTable();
                        }
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vMonitor);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo borrar la tupla.\n" + ex.getMessage(), vMonitor);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vMonitor.jTableMonitores.clearSelection();
                    break;
                }
            }
            case "ActualizacionMonitor":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vMonitor.jTableMonitores.getSelectedRow();
                try{
                    if(fila!=-1){
                        String cod = (String)vMonitor.jTableMonitores.getValueAt(fila, 0);
                        String nombre = (String)vMonitor.jTableMonitores.getValueAt(fila, 1);
                        String dni = (String)vMonitor.jTableMonitores.getValueAt(fila, 2);
                        String telefono = (String)vMonitor.jTableMonitores.getValueAt(fila, 3);
                        String correo = (String)vMonitor.jTableMonitores.getValueAt(fila, 4);
                        String fecha = (String)vMonitor.jTableMonitores.getValueAt(fila, 5);
                        String nick = (String)vMonitor.jTableMonitores.getValueAt(fila, 6);
                        llenarCampos(cod, nombre, dni, telefono, correo, fecha, nick);
                        inicializar(vActuMonitor);
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vMonitor);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo actualizar la tupla.\n" + ex.getMessage(), vMonitor);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
            case "Actualizar":{
                try{
                    sesion = sessionFactory.openSession();
                    tr = sesion.beginTransaction();
                    String dni = vActuMonitor.CampoDNI.getText();
                    String nombre = vActuMonitor.CampoNombre.getText();
                    String telefono = vActuMonitor.CampoTelefono.getText();
                    String correo = vActuMonitor.CampoCorreo.getText();
                    Date fechaChooser = vActuMonitor.CampoFechaEntrada.getDate();
                    if( dni.equals("") )
                        vMensaje.MensajeError("El campo 'DNI' no puede estar vacío.", vActuMonitor);
                        else if(!validarDNI(dni))
                            vMensaje.MensajeError("El DNI escrito no es válido.", vActuMonitor);
                            else if(nombre.equals(""))
                                vMensaje.MensajeError("El campo 'Nombre' no puede estar vacío.", vActuMonitor);
                                else if(!validarTelefono(telefono))
                                   vMensaje.MensajeError("El teléfono no cumple el patrón mínimo (9 dígitos)", vActuMonitor); 
                                   else if(!validarCorreo(correo))
                                        vMensaje.MensajeError("El correo no cumple el patrón mínimo (xxx@xxx)", vActuMonitor);
                                        else if(fechaChooser.after(fechaActual))
                                            vMensaje.MensajeError("La fecha de entrada no puede ser más tardía que la fecha actual.", vActuMonitor);
                                            else{
                                                String cod = vActuMonitor.CampoCodMonitor.getText();


                                                String nick = vActuMonitor.CampoNick.getText();
                                                String fecha = formatoFecha.format(fechaChooser);

                                                Monitor monitor = new Monitor(cod, nombre, dni, telefono, correo, fecha, nick);
                                                MonitorDAO.alta_actu_Monitor(sesion, monitor);
                                                updateTable();
                                                vMensaje.Mensaje("¡Monitor actualizado con éxito!", vActuMonitor);

                                                vActuMonitor.dispose();
                                            }
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo insertar la nueva tupla.\n" + ex.getMessage(), vMonitor);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vMonitor.jTableMonitores.clearSelection();
                    break;
                }
            }
            case "CancelarActualizar":{
                vMonitor.jTableMonitores.clearSelection();
                vActuMonitor.dispose();
                break;
            }
        }
    }    
    
}
