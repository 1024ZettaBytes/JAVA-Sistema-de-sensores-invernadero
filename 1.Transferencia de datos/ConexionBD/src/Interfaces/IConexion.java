/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import objetos.Invernadero;
import objetos.Lectura;
import objetos.Sensor;
import objetos.Usuario;

/**
 *
 * @author Eduardo Ramírez
 */
public interface IConexion {
    public boolean conectar();
    public boolean desconectar();
    public boolean hayConexion();
    public boolean insertarUsuario(Usuario usuario);
    public boolean actualizarUsuario(Usuario usuario);
    public ArrayList<Usuario> consultarUsuarios();
    public boolean insertarLectura(Lectura lectura);
    public ArrayList<Lectura> consultarLecturas();
    public boolean insertarSensor(Sensor sensor);
    public ArrayList<Sensor> consultarSensores();
    public ArrayList<Sensor> consultarSensoresPorInvernadero(Invernadero invernadero);
    public ArrayList<Invernadero> consultarInvernaderos();
    
}

