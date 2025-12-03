/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Monitor;
import Modelo.MonitorDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.PanelPrincipal;
import Vista.UtilTablasMonitor;
import Vista.UtilTablasSocio;
import Vista.VistaActividades;
import Vista.VistaMensajes;
import Vista.VistaMonitores;
import Vista.VistaPrincipal;
import Vista.VistaSocios;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


/**
 *
 * @author alber
 */
public class ControladorPrincipal implements ActionListener{
    
    private Session sesion;
    private final SessionFactory sessionFactory;
    private Transaction tr;
    private VistaMensajes vMensaje;
    private final VistaPrincipal vPrincipal;
    private final PanelPrincipal pPrincipal;
    private final VistaSocios vSocio;
    private final VistaMonitores vMonitor;
    private final VistaActividades vActividad;
    private final UtilTablasMonitor uTablasM;
    private final UtilTablasSocio uTablasS;
    private final UtilTablasSocio uTablasS_2;
    private final ControladorMonitor cMonitor;
    private final ControladorSocio cSocio;
    private final ControladorActividad cActividad;
    
    public ControladorPrincipal(SessionFactory sessionFactory) {
        
        this.sessionFactory = sessionFactory;
        vPrincipal = new VistaPrincipal();
        pPrincipal = new PanelPrincipal();
        vSocio = new VistaSocios();
        vMonitor = new VistaMonitores();
        vActividad = new VistaActividades();
        
        uTablasM = new UtilTablasMonitor(vMonitor);
        uTablasS = new UtilTablasSocio(vSocio);
        uTablasS_2 = new UtilTablasSocio(vActividad);
        
        cMonitor = new ControladorMonitor(sessionFactory, vMonitor, uTablasM);
        cSocio = new ControladorSocio(sessionFactory, vSocio, uTablasS);
        cActividad = new ControladorActividad(sessionFactory, vActividad);
        
        addListeners();
        
        vPrincipal.setLocationRelativeTo(null);
        vPrincipal.setVisible(true);
        
        vPrincipal.getContentPane().setLayout(new CardLayout());
        vPrincipal.add(pPrincipal);
        vPrincipal.add(vMonitor);
        vPrincipal.add(vSocio);
        vPrincipal.add(vActividad);
        
        pPrincipal.setVisible(true);
        vMonitor.setVisible(false);
        vSocio.setVisible(false);
        vActividad.setVisible(false);
       
    }
    
    private void addListeners(){
        vPrincipal.GestionMonitores.addActionListener(this);
        vPrincipal.GestionSocios.addActionListener(this);
        vPrincipal.GestionActividades.addActionListener(this);
        vPrincipal.SalirAplicacion.addActionListener(this);
        vPrincipal.Home.addActionListener(this);
    }
    
    private void muestraPanel(JPanel panel){
        pPrincipal.setVisible(false);
        vMonitor.setVisible(false);
        vSocio.setVisible(false);
        vActividad.setVisible(false);
        panel.setVisible(true);
    }   
    
    private ArrayList<Monitor> pideMonitores() throws Exception{
        ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitores(sesion);
        return lMonitores;
    }
    
    private ArrayList<Socio> pideSocios() throws Exception{
        ArrayList<Socio> lSocios = SocioDAO.listaSociosNombrada(sesion);
        return lSocios;
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        switch(e.getActionCommand()){
            case "SalirAplicacion":{
                vPrincipal.dispose();
                System.exit(0);
                break;
            }
            case "Home":{
                muestraPanel(pPrincipal);
                break;
            }
            case "GestionMonitores":{
                muestraPanel(vMonitor);
                uTablasM.dibujarTablaMonitores(vMonitor);
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try{
                    ArrayList<Monitor> lMonitores = pideMonitores();
                    uTablasM.vaciarTablaMonitores();
                    uTablasM.rellenarTablaMonitores(lMonitores);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la petición de Monitores\n" + ex.getMessage(), vPrincipal);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
            case "GestionSocios":{
                muestraPanel(vSocio);
                uTablasS.dibujarTablaSocio(vSocio);
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try{
                    ArrayList<Socio> lSocios = pideSocios();
                    uTablasS.vaciarTablaSocios();
                    uTablasS.rellenarTablaSocios(lSocios);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la petición de Socios\n" + ex.getMessage(), vPrincipal);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
            case "GestionActividades":{
                muestraPanel(vActividad);
                uTablasS_2.dibujarTablaSocio(vActividad);
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try{
                    ArrayList<Socio> lSocios = pideSocios();
                    uTablasS_2.vaciarTablaSocios();
                    uTablasS_2.rellenarTablaSocios(lSocios);
                    tr.commit();
                }catch(Exception ex){
                    tr.rollback();
                    vMensaje.MensajeError("Error en la petición de Socios\n" + ex.getMessage(), vPrincipal);
                }finally{
                    if(sesion != null && sesion.isOpen()){
                        sesion.close();
                    }
                    break;
                }
            }
        }
    }
    
}
