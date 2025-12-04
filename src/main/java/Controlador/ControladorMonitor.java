/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Monitor;
import Modelo.MonitorDAO;
import Vista.UtilTablasMonitor;
import Vista.VistaCRUDMonitor;
import Vista.VistaMensajes;
import Vista.vMonitor;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Antón
 */
public class ControladorMonitor implements ActionListener {
    private final SessionFactory sessionFactory;
    private Session sesion;
    private final vMonitor vMonitor;
    private final VistaMensajes vm = new VistaMensajes();
    private Transaction tr;
    private final VistaCRUDMonitor vCRUDMonitor;
    private final MonitorDAO mDAO = new MonitorDAO();
    public final Date fActual;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final UtilTablasMonitor uTablasM;

    
    public ControladorMonitor(SessionFactory sessionFactory, vMonitor vMonitor, UtilTablasMonitor uTablasM) {
        this.sessionFactory = sessionFactory;
        sesion = this.sessionFactory.openSession();
        this.vMonitor = vMonitor;
        vCRUDMonitor = new VistaCRUDMonitor();
        fActual = new Date();
        this.uTablasM = uTablasM;
        vaciarCampos();
        addListeners();
    }
    
    private void addListeners() {
        vMonitor.nuevoMonitor.addActionListener(this);
        vMonitor.bajaMonitor.addActionListener(this);
        vMonitor.actualizacionMonitor.addActionListener(this);
        vCRUDMonitor.aceptar.addActionListener(this);
        vCRUDMonitor.cancelar.addActionListener(this);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "NuevoMonitor":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    vaciarCampos();
                    vCRUDMonitor.codigo.setText(nuevoCodigo());
                    vCRUDMonitor.setLocationRelativeTo(null);
                    vCRUDMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vCRUDMonitor.setResizable(false);
                    tr.commit();
                    vCRUDMonitor.setVisible(true);
                    
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la inserción del monitor\n"+ex.getMessage(), vMonitor);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "BajaMonitor":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    if(vMonitor.jTableMonitores.getSelectedRow() != -1) {
                    int opc = vm.dialogo(vMonitor, "¿Seguro desea eliminar el monitor seleccionado? ");
                    if(JOptionPane.YES_OPTION == opc) {
                    mDAO.eliminaMonitor(sesion, (String) vMonitor.jTableMonitores.getValueAt(vMonitor.jTableMonitores.getSelectedRow(), 0));
                    ArrayList<Monitor> lMonitores = pideMonitores();
                    uTablasM.vaciarTablaMonitores();
                    uTablasM.rellenarTablaMonitores(lMonitores);
                    vm.Mensaje("Monitor eliminado correctamente", vMonitor);
                    }
                    tr.commit();
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vMonitor);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la eliminación del monitor\n"+ex.getMessage(), vMonitor);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                
                break;
            case "ActualizacionMonitor":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    int fila = vMonitor.jTableMonitores.getSelectedRow();
                    if(fila != -1) {
                    vaciarCampos();
                    
                    String fechaEntString = (String) vMonitor.jTableMonitores.getValueAt(fila, 5);
                    Date fechaEnt = formatoFecha.parse(fechaEntString);
                    vCRUDMonitor.fechaEntrada.setDate(fechaEnt);
                    
                    vCRUDMonitor.codigo.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 0));
                    vCRUDMonitor.nombre.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 1));
                    vCRUDMonitor.dni.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 2));
                    vCRUDMonitor.telefono.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 3));
                    vCRUDMonitor.nick.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 6));
                    vCRUDMonitor.correo.setText((String) vMonitor.jTableMonitores.getValueAt(fila, 4));
                    
                    vCRUDMonitor.setLocationRelativeTo(null);
                    vCRUDMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vCRUDMonitor.setResizable(false);
                    tr.commit();
                    vCRUDMonitor.setVisible(true);
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vMonitor);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la actualización del monitor\n"+ex.getMessage(), vMonitor);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "Aceptar":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    String patron = "\\d{8}[A-Z]";
                    Pattern pattern = Pattern.compile(patron);
                    Matcher matcher1 = pattern.matcher(vCRUDMonitor.dni.getText());
                    
                    String patron2 = ".{3,}@.{3,}";
                    Pattern pattern2 = Pattern.compile(patron2);
                    Matcher matcher2 = pattern2.matcher(vCRUDMonitor.correo.getText());
                    
                    String patron3 = "\\d{9}";
                    Pattern pattern3 = Pattern.compile(patron3);
                    Matcher matcher3 = pattern3.matcher(vCRUDMonitor.telefono.getText());
                    
                    if(vCRUDMonitor.nombre.getText().length() > 0) {
                        if(vCRUDMonitor.dni.getText().length() > 0 && matcher1.matches()) {
                            if(vCRUDMonitor.telefono.getText().length() == 0 || matcher3.matches()) {
                                if(vCRUDMonitor.correo.getText().length() == 0 || matcher2.matches()) {
                                    if(fActual.after(vCRUDMonitor.fechaEntrada.getDate())) {
                                        if(vCRUDMonitor.nick.getText().length() == 0 || vCRUDMonitor.nick.getText().length() < 7) {
                                            Date fe = vCRUDMonitor.fechaEntrada.getDate();
                                            String fechaString = formatoFecha.format(fe);
                                            Monitor m = new Monitor(vCRUDMonitor.codigo.getText(), vCRUDMonitor.nombre.getText(), vCRUDMonitor.dni.getText(),
                                                                        vCRUDMonitor.telefono.getText(), vCRUDMonitor.correo.getText(), fechaString, vCRUDMonitor.nick.getText());
                                            mDAO.insertaActualizaMonitor(sesion, m);
                                            ArrayList<Monitor> lMonitores = pideMonitores();
                                            uTablasM.vaciarTablaMonitores();
                                            uTablasM.rellenarTablaMonitores(lMonitores);
                                            vm.Mensaje("Monitor insertado/actualizado correctamente", vCRUDMonitor);
                                            vCRUDMonitor.dispose();
                                        } else
                                            vm.MensajeError("Error: atributo nick demasiado largo", vCRUDMonitor);
                                    } else
                                        vm.MensajeError("Error: la fecha de entrada debe ser anterior a la actual", vCRUDMonitor);
                                } else
                                    vm.MensajeError("Error: atributo correo debe contener al menos xxx@xxx", vCRUDMonitor);
                            } else
                                vm.MensajeError("Error: atributo telefono debe contener 9 dígitos", vCRUDMonitor);
                        } else
                            vm.MensajeError("Error: atributo dni no válido", vCRUDMonitor);
                    } else 
                        vm.MensajeError("Error: atributo nombre vacío", vCRUDMonitor);
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la inserción del monitor\n"+ex.getMessage(), vMonitor);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "Cancelar":
                vCRUDMonitor.dispose();
                break;
        }
    }
    
    private void vaciarCampos(){
        vCRUDMonitor.codigo.setText(null);
        vCRUDMonitor.nombre.setText(null);
        vCRUDMonitor.dni.setText(null);
        vCRUDMonitor.telefono.setText(null);
        vCRUDMonitor.correo.setText(null);
        vCRUDMonitor.fechaEntrada.setDate(fActual);
        vCRUDMonitor.nick.setText(null);
    }
    
    private ArrayList<Monitor> pideMonitores() throws Exception {
        ArrayList<Monitor> lMonitores = mDAO.listaMonitores(sesion);
        return lMonitores;
    }
}
