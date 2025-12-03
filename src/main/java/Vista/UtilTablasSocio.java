/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Socio;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alberto
 */
public class UtilTablasSocio {
    
    DefaultTableModel modeloTablaSocios = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    
    public UtilTablasSocio(VistaSocios vSocio){
        vSocio.jTableSocios.setModel(modeloTablaSocios);
    }
    
    public UtilTablasSocio(VistaActividades vActividad){
        vActividad.jTableSocios.setModel(modeloTablaSocios);
    }
    
    public void dibujarTablaSocio(VistaSocios vSocio){
        String[] columnasTablas = {"Socio", "Nombre", "DNI", "Fecha de Nacimiento", "Teléfono", "Correo", "Fecha de Alta", "Cat."};
        modeloTablaSocios.setColumnIdentifiers(columnasTablas);
        
        vSocio.jTableSocios.getTableHeader().setResizingAllowed(false);
        vSocio.jTableSocios.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        vSocio.jTableSocios.getColumnModel().getColumn(0).setPreferredWidth(50);
        vSocio.jTableSocios.getColumnModel().getColumn(1).setPreferredWidth(210);
        vSocio.jTableSocios.getColumnModel().getColumn(2).setPreferredWidth(75);
        vSocio.jTableSocios.getColumnModel().getColumn(3).setPreferredWidth(130);
        vSocio.jTableSocios.getColumnModel().getColumn(4).setPreferredWidth(75);
        vSocio.jTableSocios.getColumnModel().getColumn(5).setPreferredWidth(210);
        vSocio.jTableSocios.getColumnModel().getColumn(6).setPreferredWidth(100);
        vSocio.jTableSocios.getColumnModel().getColumn(7).setPreferredWidth(30);
    }
    
    public void dibujarTablaSocio(VistaActividades vActividad){
        String[] columnasTablas = {"Socio", "Nombre", "DNI", "Fecha de Nacimiento", "Teléfono", "Correo", "Fecha de Alta", "Cat."};
        modeloTablaSocios.setColumnIdentifiers(columnasTablas);
        
        vActividad.jTableSocios.getTableHeader().setResizingAllowed(false);
        vActividad.jTableSocios.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        vActividad.jTableSocios.getColumnModel().getColumn(0).setPreferredWidth(50);
        vActividad.jTableSocios.getColumnModel().getColumn(1).setPreferredWidth(210);
        vActividad.jTableSocios.getColumnModel().getColumn(2).setPreferredWidth(75);
        vActividad.jTableSocios.getColumnModel().getColumn(3).setPreferredWidth(130);
        vActividad.jTableSocios.getColumnModel().getColumn(4).setPreferredWidth(75);
        vActividad.jTableSocios.getColumnModel().getColumn(5).setPreferredWidth(210);
        vActividad.jTableSocios.getColumnModel().getColumn(6).setPreferredWidth(100);
        vActividad.jTableSocios.getColumnModel().getColumn(7).setPreferredWidth(30);
    }
    
    public void vaciarTablaSocios(){
        while(modeloTablaSocios.getRowCount() > 0){
            modeloTablaSocios.removeRow(0);
        }
    }
    
    public void rellenarTablaSocios(ArrayList<Socio> socios){
        Object[] fila = new Object[8];
        for(Socio socio : socios){
            fila[0] = socio.getNumeroSocio();
            fila[1] = socio.getNombre();
            fila[2] = socio.getDni();
            fila[3] = socio.getFechaNacimiento();
            fila[4] = socio.getTelefono();
            fila[5] = socio.getCorreo();
            fila[6] = socio.getFechaEntrada();
            fila[7] = socio.getCategoria();
            modeloTablaSocios.addRow(fila);
        }
    }
}
