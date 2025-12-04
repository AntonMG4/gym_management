/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Modelo.Socio;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Antón
 */
public class VistaMensajes {
    
    public void Mensaje(String texto, Component c){
        JOptionPane.showMessageDialog(c, texto);
    }

    public void MensajeError(String texto, Component c){
        JOptionPane.showMessageDialog(c, texto, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public int dialogo(Component c, String texto) {
        int opcion = JOptionPane.showConfirmDialog(c, texto, "Atención", JOptionPane.YES_NO_CANCEL_OPTION,
                                                    JOptionPane.WARNING_MESSAGE);
        return opcion;
    }
    
    public void mensajeConsola (String texto) {
        System.out.println(texto);
    }
    
    public void mensajeConsola (String texto, String error) {
        System.out.println(texto);
        System.out.println(error);
    }
    
    
    
}
