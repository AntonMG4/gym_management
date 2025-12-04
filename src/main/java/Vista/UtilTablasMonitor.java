/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Monitor;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Antón
 */
public class UtilTablasMonitor {
    DefaultTableModel modeloTablaMonitores = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    
    public void inicializarTablaMonitores(vMonitor vMonitor) {
        vMonitor.jTableMonitores.setModel(modeloTablaMonitores);
    }
    
    public void dibujarTablaMonitores(vMonitor vMonitor) {
        String[] columnasTabla = {"Código", "Nombre", "DNI",
            "Teléfono", "Correo", "Fecha Incorporación", "Nick"};
        modeloTablaMonitores.setColumnIdentifiers(columnasTabla);
        
        vMonitor.jTableMonitores.getTableHeader().setResizingAllowed(false);
        vMonitor.jTableMonitores.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        
        vMonitor.jTableMonitores.getColumnModel().getColumn(0).setPreferredWidth(40);
        vMonitor.jTableMonitores.getColumnModel().getColumn(1).setPreferredWidth(240);
        vMonitor.jTableMonitores.getColumnModel().getColumn(2).setPreferredWidth(70);
        vMonitor.jTableMonitores.getColumnModel().getColumn(3).setPreferredWidth(70);
        vMonitor.jTableMonitores.getColumnModel().getColumn(4).setPreferredWidth(200);
        vMonitor.jTableMonitores.getColumnModel().getColumn(5).setPreferredWidth(150);
        vMonitor.jTableMonitores.getColumnModel().getColumn(6).setPreferredWidth(60);     
    }
    
    
    public void rellenarTablaMonitores(ArrayList<Monitor> monitores) {
        Object[] fila = new Object[7];
        for(Monitor monitor : monitores) {
            fila[0] = monitor.getCodMonitor();
            fila[1] = monitor.getNombre();
            fila[2] = monitor.getDni();
            fila[3] = monitor.getTelefono();
            fila[4] = monitor.getCorreo();
            fila[5] = monitor.getFechaEntrada();
            fila[6] = monitor.getNick();
            modeloTablaMonitores.addRow(fila);
        }
    }
    
    public void vaciarTablaMonitores() {
        while(modeloTablaMonitores.getRowCount() > 0) {
            modeloTablaMonitores.removeRow(0);
        }
    }
}
