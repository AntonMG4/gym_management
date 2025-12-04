package Aplicacion;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */



import Controlador.ControladorLogin;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.sql.SQLException;
import javax.swing.UIManager;


/**
 *
 * @author Antón
 */
public class DDSIP2 {

    public static void main(String[] args) throws SQLException, InterruptedException {
        try {
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch(Exception ex) {
        System.err.println( "Error al cargar el tema");
        }
        ControladorLogin cLogin = new ControladorLogin();
        
    }
}
