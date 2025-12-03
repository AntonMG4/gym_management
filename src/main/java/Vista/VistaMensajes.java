/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author alber
 */
public class VistaMensajes {
    
    public void Mensaje(String texto, Component c){
        JOptionPane.showMessageDialog(c, texto);
    }
    
    public void MensajeError(String texto, Component c){
        JOptionPane.showMessageDialog(c, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public int Dialogo(Component C, String texto) {
        int opcion = JOptionPane.showConfirmDialog(C, texto,
                "Atención", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        return opcion;
    }
    
}
