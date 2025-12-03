/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.UtilTablasSocio;
import Vista.VistaActuSocio;
import Vista.VistaMensajes;
import Vista.VistaNuevoSocio;
import Vista.VistaSocios;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author alber
 */
public class ControladorSocio implements ActionListener{
    private Session sesion;
    private final SessionFactory sessionFactory;
    private Transaction tr;
    private final VistaNuevoSocio vNuevoSocio;
    private final VistaSocios vSocio;
    private final VistaMensajes vMensaje;
    private final SimpleDateFormat formatoFecha;
    private final UtilTablasSocio uTablasS;
    private final Date fechaActual;
    private final VistaActuSocio vActuSocio;
    
    public ControladorSocio(SessionFactory sessionFactory, VistaSocios vSocio, UtilTablasSocio uTablasS){
        this.sessionFactory = sessionFactory;
        this.vSocio = vSocio;
        this.uTablasS = uTablasS;
        vActuSocio = new VistaActuSocio();
        vMensaje = new VistaMensajes();
        fechaActual = new Date();
        vNuevoSocio = new VistaNuevoSocio();
        
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
        vSocio.BotonNuevoSocio.addActionListener(this);
        vSocio.BotonBajaSocio.addActionListener(this);
        vSocio.BotonActuSocio.addActionListener(this);
        vNuevoSocio.BotonInsertar.addActionListener(this);
        vNuevoSocio.BotonCancelarInsertar.addActionListener(this);
        vActuSocio.BotonActualizar.addActionListener(this);
        vActuSocio.BotonCancelarActualizar.addActionListener(this);
        vSocio.BotonBuscar.addActionListener(this);
    }
    
    private String nuevoCodigo(){
        ArrayList<String> valor = SocioDAO.ultimoSocio(sesion);
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
    
    public boolean esMayorDe18(Date fechaNacimiento) {

        Calendar calNacimiento = Calendar.getInstance();
        calNacimiento.setTime(fechaNacimiento);

        Calendar calActual = Calendar.getInstance();
        calActual.setTime(this.fechaActual);

        int aniosDiferencia = calActual.get(Calendar.YEAR) - calNacimiento.get(Calendar.YEAR);
        int mesesDiferencia = calActual.get(Calendar.MONTH) - calNacimiento.get(Calendar.MONTH);
        int diasDiferencia = calActual.get(Calendar.DAY_OF_MONTH) - calNacimiento.get(Calendar.DAY_OF_MONTH);

        if (mesesDiferencia < 0 || (mesesDiferencia == 0 && diasDiferencia < 0)) {
            aniosDiferencia--;
        }

        return aniosDiferencia >= 18;
 
    }
    
    private ArrayList<Socio> pideSocios() throws Exception{
        ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL(sesion);
        return lSocios;
    }
    
    private void updateTable() throws Exception{
         ArrayList<Socio> lSocios = pideSocios();
         uTablasS.vaciarTablaSocios();
         uTablasS.rellenarTablaSocios(lSocios);
    }
    
    private void vaciarCampos(){
        vNuevoSocio.CampoCodSocio.setText("");
        vNuevoSocio.CampoNombre.setText("");
        vNuevoSocio.CampoDNI.setText("");
        vNuevoSocio.CampoTelefono.setText("");
        vNuevoSocio.CampoCorreo.setText("");
        vNuevoSocio.CampoFechaEntrada.setDate(fechaActual);
        vNuevoSocio.CampoFechaNac.setDate(fechaActual);
    }
    
    private void llenarCampos(String cod, String nombre, String dni, String telefono, String correo, String fechaEntrada, String fechaNac){
        vActuSocio.CampoCodSocio.setText(cod);
        vActuSocio.CampoNombre.setText(nombre);
        vActuSocio.CampoDNI.setText(dni);
        vActuSocio.CampoTelefono.setText(telefono);
        vActuSocio.CampoCorreo.setText(correo);
        Date f = new Date();
        try {
            f = formatoFecha.parse(fechaEntrada);
        } catch (ParseException ex) {
            Logger.getLogger(ControladorSocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        vActuSocio.CampoFechaEntrada.setDate(f);
        try {
            f = formatoFecha.parse(fechaNac);
        } catch (ParseException ex) {
            Logger.getLogger(ControladorSocio.class.getName()).log(Level.SEVERE, null, ex);
        }
        vActuSocio.CampoFechaNac.setDate(f);
    }
        
    @Override
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "BuscarSocio":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try{
                    String busqueda = vSocio.CampoBuscar.getText();
                    if(busqueda != null  && !busqueda.equals("")){
                        ArrayList<Socio> lSocios = null;
                        switch((String)vSocio.ComboBuscar.getSelectedItem()){
                            case "DNI":{
                                lSocios = SocioDAO.busquedaSocio(sesion, 1, busqueda);
                                break;
                            }
                            case "Nombre":{
                                lSocios = SocioDAO.busquedaSocio(sesion, 2, busqueda);
                                break;
                            }
                            case "Categoria":{
                                lSocios = SocioDAO.busquedaSocio(sesion, 3, busqueda);
                                break;
                            }
                        }
                        uTablasS.vaciarTablaSocios();
                        uTablasS.rellenarTablaSocios(lSocios);
                    }
                    else
                        updateTable();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: " + ex.getMessage(), vSocio);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
            case "NuevoSocio":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try{
                    vaciarCampos();
                    vNuevoSocio.CampoCodSocio.setText(nuevoCodigo());
                    vNuevoSocio.CampoCategoria.setSelectedIndex(0);
                    inicializar(vNuevoSocio);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: " + ex.getMessage(), vSocio);
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
                    String dni = vNuevoSocio.CampoDNI.getText();
                    String nombre = vNuevoSocio.CampoNombre.getText();
                    String telefono = vNuevoSocio.CampoTelefono.getText();
                    String correo = vNuevoSocio.CampoCorreo.getText();
                    Date fechaChooser = vNuevoSocio.CampoFechaEntrada.getDate();
                    Date fechaChooser2 = vNuevoSocio.CampoFechaNac.getDate();
                    if( dni.equals("") )
                        vMensaje.MensajeError("El campo 'DNI' no puede estar vacío.", vNuevoSocio);
                        else if(!validarDNI(dni))
                            vMensaje.MensajeError("El DNI escrito no es válido.", vNuevoSocio);
                            else if(nombre.equals(""))
                                vMensaje.MensajeError("El campo 'Nombre' no puede estar vacío.", vNuevoSocio);
                                else if(!validarTelefono(telefono))
                                    vMensaje.MensajeError("El teléfono no cumple el patrón mínimo (9 dígitos)", vNuevoSocio);
                                    else if(!validarCorreo(correo))
                                        vMensaje.MensajeError("El correo no cumple el patrón mínimo (xxx@xxx)", vNuevoSocio);
                                        else if(fechaChooser.after(fechaActual))
                                            vMensaje.MensajeError("La fecha de entrada no puede ser más tardía que la fecha actual.", vNuevoSocio);
                                            else if(!esMayorDe18(fechaChooser2))
                                                vMensaje.MensajeError("El socio no puede ser menor de edad.", vNuevoSocio);
                                                else if(fechaChooser2.after(fechaChooser))
                                                    vMensaje.MensajeError("La fecha de nacimiento no puede ser más tardía que la fecha de entrada.", vNuevoSocio);
                                                    else {
                                                        String cod = vNuevoSocio.CampoCodSocio.getText();

                                                        String catg = (String) vNuevoSocio.CampoCategoria.getSelectedItem();
                                                        Character cat = catg.charAt(0);
                                                        String fechaEntr = formatoFecha.format(fechaChooser);
                                                        String fechaNac = formatoFecha.format(fechaChooser2);

                                                        Socio socio = new Socio(cod, nombre, dni, fechaNac, telefono, correo, fechaEntr, cat);
                                                        SocioDAO.alta_actu_Socio(sesion, socio);
                                                        updateTable();
                                                        vMensaje.Mensaje("¡Socio añadido con éxito!", vNuevoSocio);
                                                        vNuevoSocio.dispose();
                                                    }
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo insertar la nueva tupla.\n" + ex.getMessage(), vSocio);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
            case "CancelarInsertar":{
                vNuevoSocio.dispose();
                break;
            }
            case "BajaSocio":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vSocio.jTableSocios.getSelectedRow();
                try{
                    if(fila!=-1){
                        String cod = (String)vSocio.jTableSocios.getValueAt(fila, 0);
                        String nombre = (String)vSocio.jTableSocios.getValueAt(fila, 1);
                        int resp = vMensaje.Dialogo(vSocio, "¿Seguro que quiere dar de baja a " + nombre +"?");
                        if(resp == JOptionPane.YES_OPTION){
                            SocioDAO.bajaSocio(sesion, cod);
                            updateTable();
                        }
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vSocio);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo borrar la tupla.\n" + ex.getMessage(), vSocio);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vSocio.jTableSocios.clearSelection();
                    break;
                }
            }
            case "ActualizacionSocio":{
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                int fila = vSocio.jTableSocios.getSelectedRow();
                try{
                    if(fila!=-1){
                        String cod = (String)vSocio.jTableSocios.getValueAt(fila, 0);
                        String nombre = (String)vSocio.jTableSocios.getValueAt(fila, 1);
                        String dni = (String)vSocio.jTableSocios.getValueAt(fila, 2);
                        String telefono = (String)vSocio.jTableSocios.getValueAt(fila, 4);
                        String correo = (String)vSocio.jTableSocios.getValueAt(fila, 5);
                        String fechaEntrada = (String)vSocio.jTableSocios.getValueAt(fila, 6);
                        String fechaNac = (String)vSocio.jTableSocios.getValueAt(fila, 3);
                        Character cat = (Character)vSocio.jTableSocios.getValueAt(fila, 7);
                        llenarCampos(cod, nombre, dni, telefono, correo, fechaEntrada, fechaNac);
                        vActuSocio.CampoCategoria.setSelectedItem(cat.toString());
                        inicializar(vActuSocio);
                    }
                    else
                        vMensaje.MensajeError("No ha seleccionado ninguna fila.", vSocio);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo actualizar la tupla.\n" + ex.getMessage(), vSocio);
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
                    String dni = vActuSocio.CampoDNI.getText();
                    String nombre = vActuSocio.CampoNombre.getText();
                    String telefono = vActuSocio.CampoTelefono.getText();
                    String correo = vActuSocio.CampoCorreo.getText();
                    Date fechaChooser = vActuSocio.CampoFechaEntrada.getDate();
                    Date fechaChooser2 = vActuSocio.CampoFechaNac.getDate();
                    if( dni.equals("") )
                        vMensaje.MensajeError("El campo 'DNI' no puede estar vacío.", vActuSocio);
                        else if(!validarDNI(dni))
                            vMensaje.MensajeError("El DNI escrito no es válido.", vActuSocio);
                            else if(nombre.equals(""))
                                vMensaje.MensajeError("El campo 'Nombre' no puede estar vacío.", vActuSocio);
                                else if(!validarTelefono(telefono))
                                    vMensaje.MensajeError("El teléfono no cumple el patrón mínimo (9 dígitos)", vActuSocio);
                                        else if(!validarCorreo(correo))
                                            vMensaje.MensajeError("El correo no cumple el patrón mínimo (xxx@xxx)", vActuSocio);
                                            else if(fechaChooser.after(fechaActual))
                                                vMensaje.MensajeError("La fecha de entrada no puede ser más tardía que la fecha actual.", vActuSocio);
                                                else if(!esMayorDe18(fechaChooser2))
                                                    vMensaje.MensajeError("El socio no puede ser menor de edad.", vActuSocio);
                                                    else if(fechaChooser2.after(fechaActual))
                                                        vMensaje.MensajeError("La fecha de nacimiento no puede ser más tardía que la fecha actual.", vActuSocio);
                                                        else{
                                                            String cod = vActuSocio.CampoCodSocio.getText();

                                                            String catg = (String) vActuSocio.CampoCategoria.getSelectedItem();
                                                            Character cat = catg.charAt(0);
                                                            String fechaEntr = formatoFecha.format(fechaChooser);
                                                            String fechaNac = formatoFecha.format(fechaChooser2);

                                                            Socio socio = new Socio(cod, nombre, dni, fechaNac, telefono, correo, fechaEntr, cat);
                                                            SocioDAO.alta_actu_Socio(sesion, socio);
                                                            updateTable();
                                                            vMensaje.Mensaje("¡Socio actualizado con éxito!", vActuSocio);

                                                            vActuSocio.dispose();
                                                        }
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la BD: no se pudo insertar la nueva tupla.\n" + ex.getMessage(), vSocio);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    vSocio.jTableSocios.clearSelection();
                    break;
                }
            }
            case "CancelarActualizar":{
                vSocio.jTableSocios.clearSelection();
                vActuSocio.dispose();
                break;
            }
        }
    }
}
