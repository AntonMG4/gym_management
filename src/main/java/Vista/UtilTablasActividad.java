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
 * @author alberto
 */
public class UtilTablasActividad {
    DefaultTableModel modeloTablaActividades = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    
    public UtilTablasActividad(AltaActividad vAlta){
        vAlta.jTableActividades.setModel(modeloTablaActividades);
    }
    
    public UtilTablasActividad(BajaActividad vBaja){
        vBaja.jTableActividades.setModel(modeloTablaActividades);
    }
    
    public void dibujarTablaActividades(AltaActividad vAlta){
        String[] columnasTablas = {"ID", "Nombre", "Descripción", "Precio/Mes", "Monitor"};
        modeloTablaActividades.setColumnIdentifiers(columnasTablas);
        
        vAlta.jTableActividades.getTableHeader().setResizingAllowed(false);
        vAlta.jTableActividades.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        vAlta.jTableActividades.getColumnModel().getColumn(0).setPreferredWidth(30);
        vAlta.jTableActividades.getColumnModel().getColumn(1).setPreferredWidth(110);
        vAlta.jTableActividades.getColumnModel().getColumn(2).setPreferredWidth(250);
        vAlta.jTableActividades.getColumnModel().getColumn(3).setPreferredWidth(55);
        vAlta.jTableActividades.getColumnModel().getColumn(4).setPreferredWidth(30);
        
    }
    
    public void dibujarTablaActividades(BajaActividad vBaja){
        String[] columnasTablas = {"ID", "Nombre", "Descripción", "Precio/Mes", "Monitor"};
        modeloTablaActividades.setColumnIdentifiers(columnasTablas);
        
        vBaja.jTableActividades.getTableHeader().setResizingAllowed(false);
        vBaja.jTableActividades.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        vBaja.jTableActividades.getColumnModel().getColumn(0).setPreferredWidth(30);
        vBaja.jTableActividades.getColumnModel().getColumn(1).setPreferredWidth(110);
        vBaja.jTableActividades.getColumnModel().getColumn(2).setPreferredWidth(250);
        vBaja.jTableActividades.getColumnModel().getColumn(3).setPreferredWidth(55);
        vBaja.jTableActividades.getColumnModel().getColumn(4).setPreferredWidth(30);
        
    }
    
    public void vaciarTablaActividades(){
        while(modeloTablaActividades.getRowCount() > 0){
            modeloTablaActividades.removeRow(0);
        }
    }
    
    public void rellenarTablaActividadesBaja(ArrayList<Object[]> actividades){
        Object[] fila = new Object[5];
        for(Object[] actividad : actividades){
            fila[0] = actividad[0];
            fila[1] = actividad[1];
            fila[2] = actividad[2];
            fila[3] = actividad[3];
            fila[4] = actividad[4];
            modeloTablaActividades.addRow(fila);
        }
        
    }
    
    public void rellenarTablaActividadesAlta(ArrayList<Actividad> actividades){
        Object[] fila = new Object[5];
        for(Actividad actividad : actividades){
            fila[0] = actividad.getIdActividad();
            fila[1] = actividad.getNombre();
            fila[2] = actividad.getDescripcion();
            fila[3] = actividad.getPrecioBaseMes();
            fila[4] = actividad.getMonitorResponsable().getCodMonitor();
            modeloTablaActividades.addRow(fila);
        }
        
    }
}
