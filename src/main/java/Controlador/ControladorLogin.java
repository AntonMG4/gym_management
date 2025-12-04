/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.VistaMensajes;
import java.sql.SQLException;
import java.util.Scanner;
import org.hibernate.SessionFactory;
import Config.HibernateUtilMariaDB;
import Config.HibernateUtilOracle;
import Vista.VistaLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Antón
 */
public class ControladorLogin implements ActionListener {
    private SessionFactory sessionFactory;
    private final VistaMensajes vm = new VistaMensajes();
    private final VistaLogin vLogin = new VistaLogin();
    private ControladorPrincipal controladorP;
    
    private void conectarBD() {
        boolean conex = false;
        while (!conex) {
            try {
                String servidor = (String) (vLogin.seleccion_BD.getSelectedItem());
                if (servidor.equals("MariaDB")) {
                    sessionFactory = HibernateUtilMariaDB.getSessionFactory();
                    conex = true;
                    vm.Mensaje("Conexion establecida con MariaDB", vLogin);
                } else if (servidor.equals("Oracle")) {
                    sessionFactory = HibernateUtilOracle.getSessionFactory();
                    conex = true;
                    vm.Mensaje("Conexion establecida con Oracle", vLogin);
                }
                vm.mensajeConsola("\n\n\n\n\n\n\n\n");

            } catch (ExceptionInInitializerError e) {
                Throwable cause = e.getCause();
                vm.MensajeError("Error de conexión. Revise el fichero .cfg.xml: " + cause.getMessage(), vLogin);
            }
        }
        
    }
    
    public ControladorLogin() throws SQLException, InterruptedException {
        addListeners();
        vLogin.setLocationRelativeTo(null);
        vLogin.setVisible(true);
        vLogin.seleccion_BD.setSelectedIndex(0);
    }
    
    private void addListeners() {
        vLogin.Boton_Salir.addActionListener(this);
        vLogin.Boton_Aceptar.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            
            case "Aceptar":
                conectarBD();
                vLogin.dispose();
                controladorP = new ControladorPrincipal(sessionFactory);
                break;
                
            case "Salir":
                vLogin.dispose();
                System.exit(0);
                break;

        }
    }
    
}
