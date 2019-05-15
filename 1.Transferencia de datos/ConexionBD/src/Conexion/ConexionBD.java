/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import Interfaces.IConexion;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Invernadero;
import objetos.Lectura;
import objetos.Sensor;
import objetos.Usuario;

/**
 *
 * @author Eduardo Ramírez
 */
public class ConexionBD implements IConexion {

    private final String driver = "com.mysql.jdbc.Driver";

    private final String usuarioBD = "root";
    private final String passBD = "sesamo";
    private final String servidorBD = "localhost";
    private final String nombreBD = "mydb";
    private final String puertoBD = "3306";
    private Connection conn = null;

    public ConexionBD() {
    }

    @Override
    public boolean conectar() {
        boolean correcto = false;
        String url = "jdbc:mysql://" + servidorBD + ":" + puertoBD + "/" + nombreBD + "?useSSL=false";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, usuarioBD, passBD);
            correcto = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            correcto = false;
        }
        return correcto;
    }

    @Override
    public boolean desconectar() {
        try {
            conn.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public boolean hayConexion() {

        try {
            return conn.isValid(1);
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean insertarUsuario(Usuario usuario) {
        String sSQL = "INSERT INTO usuarios (idUsuario, nombre, correo, notificacion, temperaturaCritica, humedadCritica)"
                + " VALUES ('" + usuario.getIdUsuario() + "', '" + usuario.getNombre() + "', '" + usuario.getCorreo() + "', " + "false, null, null)";
        try {
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            pstm.execute();
            pstm.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        String sSQL = "UPDATE usuarios SET idUsuario = '" + usuario.getIdUsuario()
                + "', correo = '" + usuario.getCorreo()
                + "', notificacion = " + usuario.isNotificacionActivada()
                + ", temperaturaCritica = " + usuario.getTemperaturaCritica()
                + ", humedadCritica = " + usuario.getHumedadCritica()
                + " WHERE idUsuario = '"+usuario.getIdUsuario()+"'";
        System.out.println(sSQL);
        try {
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            pstm.execute();
            pstm.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean insertarLectura(Lectura lectura) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(lectura.getFechaHora().getTime());
        String sSQL = "INSERT INTO lecturas (idSensor, temperatura, humedad, fechaHora)"
                + " VALUES (" + lectura.getSensor().getIdSensor() + ", " + lectura.getTemperatura() + ", " + lectura.getHumedad() + ", '" + dateString + "')";
        System.out.println(sSQL);
        try {
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            pstm.execute();
            pstm.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public ArrayList<Lectura> consultarLecturas() {
        String sSQL = "SELECT l.idLectura, l.idSensor, s.idInvernadero, s.marca, l.temperatura, l.humedad, l.fechaHora\n"
                + "FROM lecturas l\n"
                + "JOIN sensores s ON l.idSensor = s.idSensor\n"
                + "JOIN invernaderos i ON i.idInvernadero = s.idInvernadero";
        try {
            ArrayList lecturas = new ArrayList<Lectura>();
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            ResultSet rs = pstm.executeQuery(sSQL);
            while (rs.next()) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(rs.getTimestamp("fechaHora").getTime());
                Lectura lectura = new Lectura(rs.getInt("idLectura"), new Sensor(rs.getInt("idSensor"), new Invernadero(rs.getInt("idInvernadero")), rs.getString("marca")), rs.getFloat("temperatura"), rs.getFloat("humedad"), c);
                lecturas.add(lectura);
            }
            pstm.close();
            return lecturas;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertarSensor(Sensor sensor) {
        String sSQL = "INSERT INTO sensores (idSensor, idInvernadero, marca)"
                + " VALUES (" + sensor.getIdSensor() + ", " + sensor.getInvernadero().getIdInvernadero() + ", '" + sensor.getMarca() + "')";

        try {
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            pstm.execute();
            pstm.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public ArrayList<Sensor> consultarSensores() {
        String sSQL = "SELECT * FROM sensores";
        try {
            ArrayList sensores = new ArrayList<Sensor>();
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            ResultSet rs = pstm.executeQuery(sSQL);
            while (rs.next()) {
                Sensor sensor = new Sensor(rs.getInt("idSensor"), new Invernadero(rs.getInt("idInvernadero")), rs.getString("marca"));
                sensores.add(sensor);
            }
            pstm.close();
            return sensores;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public ArrayList<Sensor> consultarSensoresPorInvernadero(Invernadero invernadero) {
        String sSQL = "SELECT * FROM sensores WHERE idInvernadero = " + invernadero.getIdInvernadero();
        try {
            ArrayList sensores = new ArrayList<Sensor>();
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            ResultSet rs = pstm.executeQuery(sSQL);
            while (rs.next()) {
                Sensor sensor = new Sensor(rs.getInt("idSensor"), invernadero, rs.getString("marca"));
                sensores.add(sensor);
            }
            pstm.close();
            return sensores;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public ArrayList<Invernadero> consultarInvernaderos() {
        String sSQL = "SELECT * FROM invernaderos";
        try {
            ArrayList invernaderos = new ArrayList<Invernadero>();
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            ResultSet rs = pstm.executeQuery(sSQL);
            while (rs.next()) {
                Invernadero inv = new Invernadero(rs.getInt("idInvernadero"));
                invernaderos.add(inv);
            }
            pstm.close();
            return invernaderos;
        } catch (SQLException ex) {
            return null;
        }
    }
    @Override
    public Invernadero consultarInvernaderoPorSensor(Sensor sensor) {
        String sSQL = "SELECT * FROM sensores WHERE idSensor = " + sensor.getIdSensor();
        try {
            Invernadero i = new Invernadero(0);
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            ResultSet rs = pstm.executeQuery(sSQL);
            while (rs.next()) {
                i = new Invernadero(rs.getInt("idInvernadero"));
            }
            pstm.close();
            return i;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public Usuario consultarUsuario() {
        String sSQL = "SELECT * FROM usuarios";
        try {
            Usuario u = new Usuario("ERROR", null, null, false, 0, 0);
            // PreparedStatement
            PreparedStatement pstm = conn.prepareStatement(sSQL);
            ResultSet rs = pstm.executeQuery(sSQL);
            while (rs.next()) {
                u = new Usuario(rs.getString("idUsuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getBoolean("notificacion"),
                        rs.getFloat("temperaturaCritica"),
                rs.getFloat("humedadCritica"));
            }
            pstm.close();
            return u;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    

}
