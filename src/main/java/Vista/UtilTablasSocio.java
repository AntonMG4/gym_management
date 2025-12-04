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
 * @author Antón
 */
public class UtilTablasSocio {
    DefaultTableModel modeloTablaSocios = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    
    public void inicializarTablaSocios(vSocio vSocio) {
        vSocio.jTableSocios.setModel(modeloTablaSocios);
    }
    
    public void inicializarTablaSocios(vActividad vActividad) {
        vActividad.jTableSocios.setModel(modeloTablaSocios);
    }
    
    public void dibujarTablaSocios(vSocio vSocio) {
        String[] columnasTabla = {"Número", "Nombre", "DNI", "Fecha Nacimiento",
            "Teléfono", "Correo", "Fecha Incorporación", "Categoria"};
        modeloTablaSocios.setColumnIdentifiers(columnasTabla);
        
        vSocio.jTableSocios.getTableHeader().setResizingAllowed(false);
        vSocio.jTableSocios.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        
        vSocio.jTableSocios.getColumnModel().getColumn(0).setPreferredWidth(40);
        vSocio.jTableSocios.getColumnModel().getColumn(1).setPreferredWidth(240);
        vSocio.jTableSocios.getColumnModel().getColumn(2).setPreferredWidth(70);
        vSocio.jTableSocios.getColumnModel().getColumn(3).setPreferredWidth(150);
        vSocio.jTableSocios.getColumnModel().getColumn(4).setPreferredWidth(70);
        vSocio.jTableSocios.getColumnModel().getColumn(5).setPreferredWidth(200);
        vSocio.jTableSocios.getColumnModel().getColumn(6).setPreferredWidth(150); 
        vSocio.jTableSocios.getColumnModel().getColumn(7).setPreferredWidth(40);
    }
    
    public void dibujarTablaSocios(vActividad vActividad) {
        String[] columnasTabla = {"Número", "Nombre", "DNI", "Fecha Nacimiento",
            "Teléfono", "Correo", "Fecha Incorporación", "Categoria"};
        modeloTablaSocios.setColumnIdentifiers(columnasTabla);
        
        vActividad.jTableSocios.getTableHeader().setResizingAllowed(false);
        vActividad.jTableSocios.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        
        vActividad.jTableSocios.getColumnModel().getColumn(0).setPreferredWidth(40);
        vActividad.jTableSocios.getColumnModel().getColumn(1).setPreferredWidth(240);
        vActividad.jTableSocios.getColumnModel().getColumn(2).setPreferredWidth(70);
        vActividad.jTableSocios.getColumnModel().getColumn(3).setPreferredWidth(150);
        vActividad.jTableSocios.getColumnModel().getColumn(4).setPreferredWidth(70);
        vActividad.jTableSocios.getColumnModel().getColumn(5).setPreferredWidth(200);
        vActividad.jTableSocios.getColumnModel().getColumn(6).setPreferredWidth(150); 
        vActividad.jTableSocios.getColumnModel().getColumn(7).setPreferredWidth(40);
    }
    
    
    public void rellenarTablaSocios(ArrayList<Socio> socios) {
        Object[] fila = new Object[8];
        for(Socio socio : socios) {
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
    
    public void vaciarTablaSocios() {
        while(modeloTablaSocios.getRowCount() > 0) {
            modeloTablaSocios.removeRow(0);
        }
    }
}
