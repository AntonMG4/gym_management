/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Config.HibernateUtilMariaDB;
import Config.HibernateUtilOracle;
import Vista.VistaLogin;
import Vista.VistaMensajes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.SessionFactory;

/**
 *
 * @author alber
 */
public class ControladorLogin implements ActionListener{
    
    private ControladorPrincipal controladorP;
    private SessionFactory sessionFactory;
    private final VistaMensajes vMensaje;
    private final VistaLogin vLogin;
    
    private void conectarBD() {
        boolean conexCorrecta = false;
        while(!conexCorrecta){
            try {   
                String server = (String) (vLogin.DesplegableServidor.getSelectedItem());
                if (server.equals("Oracle")) {
                    sessionFactory = HibernateUtilOracle.getSessionFactory();
                } else if (server.equals("MariaDB")) {
                    sessionFactory = HibernateUtilMariaDB.getSessionFactory();
                }
                //vMensaje.mensajeConsola("Conexión Correcta con Hibernate");
                conexCorrecta = true;
                vMensaje.Mensaje("La conexión con " + server + " ha tenido éxito", vLogin);

            }catch (ExceptionInInitializerError e) {
                Throwable cause = e.getCause();
                vMensaje.MensajeError("Error en la conexión. Revise el fichero .cfg.xml: " + cause.getMessage(), vLogin);
                //System.out.println("Error en la conexión. Revise el fichero .cfg.xml: " + cause.getMessage());
            }
        }
    }
    
    public ControladorLogin() {
        vMensaje = new VistaMensajes();
        vLogin = new VistaLogin();
        
        addListeners();
        
        vLogin.setLocationRelativeTo(null);
        vLogin.setVisible(true);
        
        vLogin.DesplegableServidor.setSelectedIndex(0);
    }
    
    private void addListeners(){
        vLogin.BotonSalir.addActionListener(this);
        vLogin.BotonConectar.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "ConectarConBD":{
                conectarBD();
                vLogin.dispose();
                controladorP = new ControladorPrincipal(sessionFactory);
                break;
            }
            case "SalirdeElegirBD":{
                vLogin.dispose();
                System.exit(0);
                break;
            }
        }
    }
    
}
