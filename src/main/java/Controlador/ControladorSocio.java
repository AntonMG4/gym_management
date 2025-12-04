/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.UtilTablasSocio;
import Vista.VistaCRUDSocio;
import Vista.VistaMensajes;
import Vista.vSocio;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class ControladorSocio implements ActionListener {
    private final SessionFactory sessionFactory;
    private Session sesion;
    private final vSocio vSocio;
    private final VistaMensajes vm = new VistaMensajes();
    private Transaction tr;
    private final VistaCRUDSocio vCRUDSocio;
    private final SocioDAO sDAO = new SocioDAO();
    public final Date fActual;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final UtilTablasSocio uTablasS;
    
    public ControladorSocio(SessionFactory sessionFactory, vSocio vSocio, UtilTablasSocio uTablasS) {
        this.sessionFactory = sessionFactory;
        sesion = this.sessionFactory.openSession();
        this.vSocio = vSocio;
        vCRUDSocio = new VistaCRUDSocio();
        fActual = new Date();
        this.uTablasS = uTablasS;
        vaciarCampos();
        addListeners();
    }
    
    private void addListeners() {
        vSocio.nuevoSocio.addActionListener(this);
        vSocio.bajaSocio.addActionListener(this);
        vSocio.actualizacionSocio.addActionListener(this);
        vSocio.buscador.addActionListener(this);
        vCRUDSocio.aceptar.addActionListener(this);
        vCRUDSocio.cancelar.addActionListener(this);
    }
    
    private void vaciarCampos(){
        vCRUDSocio.codigo.setText(null);
        vCRUDSocio.nombre.setText(null);
        vCRUDSocio.dni.setText(null);
        vCRUDSocio.telefono.setText(null);
        vCRUDSocio.correo.setText(null);
        vCRUDSocio.fechaEntrada.setDate(fActual);
        vCRUDSocio.fechaNacimiento.setDate(fActual);
        vCRUDSocio.categoria.setSelectedIndex(0);
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
    
    private ArrayList<Socio> pideSocios() throws Exception {
        //ArrayList<Socio> lSocios = sDAO.listaSociosHQL(sesion);
        ArrayList<Socio> lSocios = sDAO.listaSociosNombrada(sesion);
        return lSocios;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "NuevoSocio":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    vaciarCampos();
                    vCRUDSocio.codigo.setText(nuevoCodigo());
                    vCRUDSocio.setLocationRelativeTo(null);
                    vCRUDSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vCRUDSocio.setResizable(false);
                    tr.commit();
                    vCRUDSocio.setVisible(true);
                    
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la inserción del socio\n"+ex.getMessage(), vSocio);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "BajaSocio":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    if(vSocio.jTableSocios.getSelectedRow() != -1) {
                    int opc = vm.dialogo(vSocio, "¿Seguro desea eliminar el socio seleccionado? ");
                    if(JOptionPane.YES_OPTION == opc) {
                    sDAO.eliminaSocio(sesion, (String) vSocio.jTableSocios.getValueAt(vSocio.jTableSocios.getSelectedRow(), 0));
                    ArrayList<Socio> lSocios = pideSocios();
                    uTablasS.vaciarTablaSocios();
                    uTablasS.rellenarTablaSocios(lSocios);
                    vm.Mensaje("Socio eliminado correctamente", vSocio);
                    }
                    tr.commit();
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vSocio);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la eliminación del socio\n"+ex.getMessage(), vSocio);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "ActualizacionSocio":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    int fila = vSocio.jTableSocios.getSelectedRow();
                    if(fila != -1) {
                    vaciarCampos();
                    String fechaNacString = (String) vSocio.jTableSocios.getValueAt(fila, 3);
                    Date fechaNac = formatoFecha.parse(fechaNacString);
                    vCRUDSocio.fechaNacimiento.setDate(fechaNac);
                    
                    
                    String fechaEntString = (String) vSocio.jTableSocios.getValueAt(fila, 6);
                    Date fechaEnt = formatoFecha.parse(fechaEntString);
                    vCRUDSocio.fechaEntrada.setDate(fechaEnt);
                    
                    vCRUDSocio.codigo.setText((String) vSocio.jTableSocios.getValueAt(fila, 0));
                    vCRUDSocio.nombre.setText((String) vSocio.jTableSocios.getValueAt(fila, 1));
                    vCRUDSocio.dni.setText((String) vSocio.jTableSocios.getValueAt(fila, 2));
                    vCRUDSocio.telefono.setText((String) vSocio.jTableSocios.getValueAt(fila, 4));
                    vCRUDSocio.correo.setText((String) vSocio.jTableSocios.getValueAt(fila, 5));
                    vCRUDSocio.categoria.setSelectedItem(vSocio.jTableSocios.getValueAt(fila, 7).toString());
                    vCRUDSocio.setLocationRelativeTo(null);
                    vCRUDSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vCRUDSocio.setResizable(false);
                    tr.commit();
                    vCRUDSocio.setVisible(true);
                    }
                    else {
                        vm.MensajeError("Error: ninguna fila seleccionada", vSocio);
                    }
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la actualización del socio\n"+ex.getMessage(), vSocio);
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
                    
                    // Patrones para el control de excepciones
                    String patron = "\\d{8}[A-Z]";
                    String patron2 = "\\d{9}";
                    String patron3 = ".{3,}@.{3,}";
                    Pattern pattern = Pattern.compile(patron);
                    Pattern pattern2 = Pattern.compile(patron2);
                    Pattern pattern3 = Pattern.compile(patron3);
                    Matcher matcher1 = pattern.matcher(vCRUDSocio.dni.getText());
                    Matcher matcher2 = pattern2.matcher(vCRUDSocio.telefono.getText());
                    Matcher matcher3 = pattern3.matcher(vCRUDSocio.correo.getText());
                    
                    // Para el cálculo de la fecha a partir de la cual se es mayor de 18 años
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fActual);
                    calendar.add(Calendar.YEAR, -18);
                    Date fecha18 = calendar.getTime();
                    
                    if(vCRUDSocio.nombre.getText().length() > 0) {
                        if(vCRUDSocio.dni.getText().length() > 0 && matcher1.matches()) {
                            if(vCRUDSocio.telefono.getText().length() == 0 || matcher2.matches()) {
                                if(vCRUDSocio.correo.getText().length() == 0 || matcher3.matches()) {
                                    if(fActual.after(vCRUDSocio.fechaEntrada.getDate())) {
                                        if (fecha18.after(vCRUDSocio.fechaNacimiento.getDate())) {
                                                Date fe = vCRUDSocio.fechaEntrada.getDate();
                                                String fechaEntrada = formatoFecha.format(fe);
                                                fe = vCRUDSocio.fechaNacimiento.getDate();
                                                String fechaNacimiento = formatoFecha.format(fe);
                                                Socio s = new Socio(vCRUDSocio.codigo.getText(), vCRUDSocio.nombre.getText(), vCRUDSocio.dni.getText(), fechaNacimiento,
                                                        vCRUDSocio.telefono.getText(), vCRUDSocio.correo.getText(), fechaEntrada, vCRUDSocio.categoria.getSelectedItem().toString().charAt(0));
                                                sDAO.insertaActualizaSocio(sesion, s);
                                                ArrayList<Socio> lSocios = pideSocios();
                                                uTablasS.vaciarTablaSocios();
                                                uTablasS.rellenarTablaSocios(lSocios);
                                                vm.Mensaje("Socio insertado/actualizado correctamente", vCRUDSocio);
                                                vCRUDSocio.dispose();
                                            
                                        } else
                                            vm.MensajeError("Error: atributo fecha de nacimiento no es válido, debe ser +18 años", vCRUDSocio);
                                    } else
                                        vm.MensajeError("Error: atributo fecha de entrada no es válido", vCRUDSocio);
                                } else 
                                    vm.MensajeError("Error: atributo correo debe contener al menos xxx@xxx", vCRUDSocio);
                            } else
                                vm.MensajeError("Error: atributo teléfono debe contener 9 dígitos", vCRUDSocio);
                        } else
                            vm.MensajeError("Error: atributo dni no válido", vCRUDSocio);
                    } else 
                        vm.MensajeError("Error: atributo nombre vacío", vCRUDSocio);
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la inserción del socio\n"+ex.getMessage(), vSocio);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            case "Cancelar":
                vCRUDSocio.dispose();
                break;
            
            case "Buscador":
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    if(vSocio.filtro.getText().length() == 0) {
                        ArrayList<Socio> lSocios = pideSocios();
                        uTablasS.vaciarTablaSocios();
                        uTablasS.rellenarTablaSocios(lSocios);
                    } else {
                        switch (vSocio.campo.getSelectedIndex()) {
                            case 0:
                                {
                                    ArrayList<Socio> lSocios = sDAO.listaSociosNombre(sesion, vSocio.filtro.getText());
                                    if (lSocios.isEmpty()) {
                                        vm.MensajeError("Fallo en la búsqueda por Nombre:\n"
                                                + "Ningún socio coincide con el criterio de búsqueda", vCRUDSocio);
                                    } else {
                                        uTablasS.vaciarTablaSocios();
                                        uTablasS.rellenarTablaSocios(lSocios);
                                    }
                                    break;
                                }
                            case 1:
                                {
                                    ArrayList<Socio> lSocios = sDAO.listaSociosDNI(sesion, vSocio.filtro.getText());
                                    if (lSocios.isEmpty()) {
                                        vm.MensajeError("Fallo en la búsqueda por DNI:\n"
                                                + "Ningún socio coincide con el criterio de búsqueda", vCRUDSocio);
                                    } else {
                                        uTablasS.vaciarTablaSocios();
                                        uTablasS.rellenarTablaSocios(lSocios);
                                    }
                                    break;
                                }
                            case 2:
                                {
                                    ArrayList<Socio> lSocios = sDAO.listaSociosCategoria(sesion, vSocio.filtro.getText());
                                    if (lSocios.isEmpty()) {
                                        vm.MensajeError("Fallo en la búsqueda por Categoría:\n"
                                                + "Ningún socio coincide con el criterio de búsqueda", vCRUDSocio);
                                    } else {
                                        uTablasS.vaciarTablaSocios();
                                        uTablasS.rellenarTablaSocios(lSocios);
                                    }
                                    break;
                                }
                        }
                    }
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la búsqueda de filtrado\n"+ex.getMessage(), vSocio);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
        }
    }
}
