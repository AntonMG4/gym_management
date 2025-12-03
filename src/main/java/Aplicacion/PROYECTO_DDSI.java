/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Aplicacion;

import Controlador.ControladorLogin;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author alber
 */
public class PROYECTO_DDSI {

    public static void main(String[] args) {
        try {
        UIManager.setLookAndFeel( new FlatMacDarkLaf());
        } catch(UnsupportedLookAndFeelException ex) {
        System.err.println( "Error al cargar el tema");
        }
        ControladorLogin log = new ControladorLogin();
    }
}
