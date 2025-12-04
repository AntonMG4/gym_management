/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Actividad;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Antón
 */
public class UtilTablasActividad {
    DefaultTableModel modeloTablaActividades = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    
    public void inicializarTablaActividades(VistaCRUDActividadAlta vAlta) {
        vAlta.jTableAlta.setModel(modeloTablaActividades);
    }
    
    public void inicializarTablaActividades(VistaCRUDActividadBaja vBaja) {
        vBaja.jTableBaja.setModel(modeloTablaActividades);
    }
    
    public void dibujarTablaActividades(VistaCRUDActividadAlta vAlta) {
        String[] columnasTabla = {"ID", "Nombre", "Descripción", "Precio al mes",
            "Monitor"};
        modeloTablaActividades.setColumnIdentifiers(columnasTabla);
        
        vAlta.jTableAlta.getTableHeader().setResizingAllowed(false);
        vAlta.jTableAlta.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        
        vAlta.jTableAlta.getColumnModel().getColumn(0).setPreferredWidth(40);
        vAlta.jTableAlta.getColumnModel().getColumn(1).setPreferredWidth(150);
        vAlta.jTableAlta.getColumnModel().getColumn(2).setPreferredWidth(350);
        vAlta.jTableAlta.getColumnModel().getColumn(3).setPreferredWidth(100);
        vAlta.jTableAlta.getColumnModel().getColumn(4).setPreferredWidth(60);
    }
    
    public void dibujarTablaActividades(VistaCRUDActividadBaja vBaja) {
        String[] columnasTabla = {"ID", "Nombre", "Descripción", "Precio al mes",
            "Monitor"};
        modeloTablaActividades.setColumnIdentifiers(columnasTabla);
        
        vBaja.jTableBaja.getTableHeader().setResizingAllowed(false);
        vBaja.jTableBaja.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        vBaja.jTableBaja.getColumnModel().getColumn(0).setPreferredWidth(40);
        vBaja.jTableBaja.getColumnModel().getColumn(1).setPreferredWidth(150);
        vBaja.jTableBaja.getColumnModel().getColumn(2).setPreferredWidth(350);
        vBaja.jTableBaja.getColumnModel().getColumn(3).setPreferredWidth(100);
        vBaja.jTableBaja.getColumnModel().getColumn(4).setPreferredWidth(60);
    }
    
    public void rellenarTablaActividadesBaja(ArrayList<Object[]> actividades) {
        Object[] fila = new Object[5];
        for(Object[] actividad : actividades) {
            fila[0] = actividad[0];
            fila[1] = actividad[1];
            fila[2] = actividad[2];
            fila[3] = actividad[3];
            fila[4] = actividad[4];
            modeloTablaActividades.addRow(fila);
        }
    }
    
    public void rellenarTablaActividadesAlta(ArrayList<Actividad> actividades) {
        Object[] fila = new Object[5];
        for(Actividad actividad : actividades) {
            fila[0] = actividad.getIdActividad();
            fila[1] = actividad.getNombre();
            fila[2] = actividad.getDescripcion();
            fila[3] = actividad.getPrecioBaseMes();
            fila[4] = actividad.getMonitorResponsable().getCodMonitor();
            modeloTablaActividades.addRow(fila);
        }
    }
    
    
    
    public void vaciarTablaActividades() {
        while(modeloTablaActividades.getRowCount() > 0) {
            modeloTablaActividades.removeRow(0);
        }
    }
}
