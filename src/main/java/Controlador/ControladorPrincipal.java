/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Monitor;
import Modelo.MonitorDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Vista.UtilTablasActividad;
import Vista.UtilTablasMonitor;
import Vista.UtilTablasSocio;
import Vista.VistaMensajes;
import Vista.VistaPrincipal;
import Vista.pPrincipal;
import Vista.vActividad;
import Vista.vMonitor;
import Vista.vSocio;
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
 * @author Antón
 */
public class ControladorPrincipal implements ActionListener {
    private Session sesion;
    private Transaction tr;
    private final VistaMensajes vm = new VistaMensajes();
    private final VistaPrincipal vP = new VistaPrincipal();
    private final UtilTablasMonitor uTablasM = new UtilTablasMonitor();
    private final UtilTablasSocio uTablasS = new UtilTablasSocio();
    private final UtilTablasActividad uTablasA = new UtilTablasActividad();
    private final pPrincipal pPrincipal = new pPrincipal();
    private final vMonitor vMonitor = new vMonitor();
    private final vSocio vSocio = new vSocio();
    private final vActividad vActividad = new vActividad();
    private final SessionFactory sessionFactory;
    private final ControladorMonitor cMonitor;
    private final ControladorSocio cSocio;
    private final ControladorActividad cActividad;
    
    public ControladorPrincipal(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        sesion = this.sessionFactory.openSession();
        addListeners();
        vP.setLocationRelativeTo(null);
        vP.setVisible(true);
        vP.getContentPane().setLayout(new CardLayout());
        vP.add(pPrincipal);
        vP.add(vMonitor);
        vP.add(vSocio);
        vP.add(vActividad);
        pPrincipal.setVisible(true);
        vMonitor.setVisible(false);
        vSocio.setVisible(false);
        vActividad.setVisible(false);
        uTablasM.inicializarTablaMonitores(vMonitor);
        uTablasS.inicializarTablaSocios(vSocio);
        uTablasS.inicializarTablaSocios(vActividad);
        cMonitor = new ControladorMonitor(sessionFactory, vMonitor, uTablasM);
        cSocio = new ControladorSocio(sessionFactory, vSocio, uTablasS);
        cActividad = new ControladorActividad(sessionFactory, vActividad, uTablasA);
    }
    
    private void muestraPanel(JPanel v, JPanel v1, JPanel v2, JPanel v3) {
        v1.setVisible(false);
        v2.setVisible(false);
        v3.setVisible(false);
        v.setVisible(true);
    }
    
    private void addListeners() {
        vP.gestionMonitores.addActionListener(this);
        vP.gestionSocios.addActionListener(this);
        vP.gestionActividades.addActionListener(this);
        vP.salirAplicacion.addActionListener(this);
        vP.pPrincipal.addActionListener(this);
    }
    
    private ArrayList<Monitor> pideMonitores() throws Exception {
        MonitorDAO MonitorDAO = new MonitorDAO();
        ArrayList<Monitor> lMonitores = MonitorDAO.listaMonitores(sesion);
        return lMonitores;
    }
    
    private ArrayList<Socio> pideSocios() throws Exception {
        SocioDAO SocioDAO = new SocioDAO();
        ArrayList<Socio> lSocios = SocioDAO.listaSociosHQL(sesion);
        return lSocios;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            
            case "SalirAplicacion":
                vP.dispose();
                System.exit(0);
                break;
                
            case "PantallaPrincipal":
                muestraPanel(pPrincipal, vSocio, vMonitor, vActividad);
                break;
                
            case "GestionSocios":
                muestraPanel(vSocio, pPrincipal, vMonitor, vActividad);
                uTablasS.dibujarTablaSocios(vSocio);
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    ArrayList<Socio> lSocios = pideSocios();
                    uTablasS.vaciarTablaSocios();
                    uTablasS.rellenarTablaSocios(lSocios);
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la tabla de socios\n"+ex.getMessage(), vP);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
                
            case "GestionMonitores":
                muestraPanel(vMonitor, pPrincipal, vSocio, vActividad);
                uTablasM.dibujarTablaMonitores(vMonitor);
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    ArrayList<Monitor> lMonitores = pideMonitores();
                    uTablasM.vaciarTablaMonitores();
                    uTablasM.rellenarTablaMonitores(lMonitores);
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la tabla de monitores\n"+ex.getMessage(), vP);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;
            
            case "GestionActividades":
                muestraPanel(vActividad, pPrincipal, vSocio, vMonitor);
                uTablasS.dibujarTablaSocios(vActividad);
                sesion = sessionFactory.openSession();
                tr = sesion.beginTransaction();
                try {
                    ArrayList<Socio> lSocios = pideSocios();
                    uTablasS.vaciarTablaSocios();
                    uTablasS.rellenarTablaSocios(lSocios);
                    tr.commit();
                } catch (Exception ex) {
                    tr.rollback();
                    vm.MensajeError("Error en la tabla de socios y actividades\n"+ex.getMessage(), vP);
                } finally {
                    if(sesion != null && sesion.isOpen()) {
                        sesion.close();
                    }
                }
                break;

        }
    }
    
}
