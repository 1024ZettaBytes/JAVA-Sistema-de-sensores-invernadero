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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insertarLectura(Lectura lectura) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(lectura.getFechaHora().getTime());
        String sSQL = "INSERT INTO lecturas (idSensor, temperatura, humedad, fechaHora)"
                + " VALUES (" +lectura.getSensor().getIdSensor()+ ", " + lectura.getTemperatura() + ", " + lectura.getHumedad() + ", '" + dateString + "')";
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
        String sSQL = "SELECT l.idLectura, l.idSensor, s.idInvernadero, s.marca, l.temperatura, l.humedad, l.fechaHora\n" +
"FROM lecturas l\n" +
"JOIN sensores s ON l.idSensor = s.idSensor\n" +
"JOIN invernaderos i ON i.idInvernadero = s.idInvernadero";
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
//
//    @Override
//    public boolean insertarUsuario(String nombre, String pass, boolean tipoAdmin) {
//        
//    }
//
//    @Override
//    public boolean actualizarUsuario(int id, String nombre, String pass, boolean tipoAdmin) {
//        String sSQL = "UPDATE usuarios SET nombre = '" + nombre + "', contra = '" + pass + "', tipoAdmin = " + tipoAdmin + " WHERE idUsuario = " + id;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarUsuario(int id) {
//        String sSQL = "DELETE FROM usuarios WHERE idUsuario = " + id;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarUsuarios() {
//        String sSQL = "SELECT * FROM usuarios";
//        try {
//            ArrayList usuarios = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] usuario = {rs.getInt("idUsuario"), rs.getString("nombre"), rs.getString("contra"), rs.getBoolean("tipoAdmin")};
//                usuarios.add(usuario);
//            }
//            pstm.close();
//            return usuarios;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarPlatillo(String nombre) {
//        String sSQL = "INSERT INTO platillos (nombre) VALUES ('" + nombre + "')";
//
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarPlatillo(int id, String nombre) {
//        String sSQL = "UPDATE platillos SET nombre = '" + nombre + "' WHERE idPlatillo = " + id;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarPlatillo(int id) {
//        String sSQL = "DELETE FROM platillos WHERE idPlatillo = " + id;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarPlatillos() {
//        String sSQL = "SELECT * FROM platillos";
//        try {
//            ArrayList platillos = new ArrayList<Object[]>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] platillo = {rs.getInt("idPlatillo"), rs.getString("nombre")};
//                platillos.add(platillo);
//            }
//            pstm.close();
//            return platillos;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarPlatilloMenu(int idPlatillo, int diaSemana, int cantidad, String categoria) {
//        String sSQL = "INSERT INTO platillosmenu (Platillos_idPlatillo, diaSemana, cantidad, categoria) VALUES (" + idPlatillo + ", " + diaSemana + ", " + cantidad + ", '" + categoria + "')";
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarPlatilloMenu(int idPlatilloMenu, int idPlatillo, int diaSemana, int cantidad, String categoria) {
//        String sSQL = "UPDATE platillosmenu SET Platillos_idPlatillo = " + idPlatillo + ", diaSemana = " + diaSemana + ", cantidad = " + cantidad + ", categoria = '" + categoria + "' WHERE idPlatilloMenu = " + idPlatilloMenu;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarPlatilloMenu(int idPlatilloMenu) {
//        String sSQL = "DELETE FROM platillosmenu WHERE idPlatilloMenu = " + idPlatilloMenu;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarPlatillosMenu() {
//        String sSQL = "SELECT * FROM platillosmenu";
//        try {
//            ArrayList platillosMenu = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] platilloMenu = {rs.getInt("idPlatilloMenu"), rs.getInt("Platillos_idPlatillo"), rs.getInt("diaSemana"), rs.getInt("cantidad"), rs.getString("categoria")};
//                platillosMenu.add(platilloMenu);
//            }
//            pstm.close();
//            return platillosMenu;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarCliente(String nombre, int creditoDesayuno, int creditoComida, int creditoCena) {
//        String sSQL = "INSERT INTO clientes (nombre, creditoDesayuno, creditoComida, creditoCena) VALUES ('" + nombre + "', " + creditoDesayuno + ", " + creditoComida + ", " + creditoCena + ")";
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarCliente(int idCliente, String nombre, int creditoDesayuno, int creditoComida, int creditoCena) {
//        String sSQL = "UPDATE clientes SET nombre = '" + nombre + "',creditoDesayuno = " + creditoDesayuno + ", creditoComida = " + creditoComida + ", creditoCena = " + creditoCena + " WHERE idCliente = " + idCliente;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarCliente(int idCliente) {
//        String sSQL = "DELETE FROM clientes WHERE idCliente = " + idCliente;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarClientes() {
//        String sSQL = "SELECT * FROM clientes";
//        try {
//            ArrayList clientes = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] cliente = {rs.getInt("idCliente"), rs.getString("nombre"), rs.getInt("creditoDesayuno"), rs.getInt("creditoComida"), rs.getInt("creditoCena")};
//                clientes.add(cliente);
//
//            }
//            pstm.close();
//            return clientes;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarReservaPlatillo(int idCliente, int idPlatillo, int cantidad, Date fecha, int tipo) {
//        java.sql.Date timestamp = new java.sql.Date(fecha.getTime());
//        String sSQL = "INSERT INTO reservasPlatillo (Clientes_idCliente, Platillos_idPlatillo, cantidad, fechaReserva, TipoReserva_idTipoReserva) VALUES (" + idCliente + ", " + idPlatillo + ", " + cantidad + ", '" + timestamp + "', " + tipo + ")";
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarReservaPlatillo(int idReserva, int idCliente, int idPlatillo, int cantidad, Date fecha, int tipo) {
//        java.sql.Date timestamp = new java.sql.Date(fecha.getTime());
//        String sSQL = "UPDATE reservasPlatillo SET Clientes_idCliente = " + idCliente + ", Platillos_idPlatillo = " + idPlatillo + ", cantidad = " + cantidad + ", fechaReserva = " + timestamp + ", TipoReserva_idTipoReserva = " + tipo + " WHERE idReserva = " + idReserva;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarReservaPlatillo(int idReserva) {
//        String sSQL = "DELETE FROM reservasPlatillo WHERE idReserva = " + idReserva;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarReservasPlatillos() {
//        String sSQL = "SELECT * FROM reservasPlatillo";
//        try {
//            ArrayList reservas = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] reserva = {rs.getInt("idReserva"), rs.getInt("Clientes_idCliente"), rs.getInt("Platillos_idPlatillo"), rs.getInt("cantidad"), rs.getString("fechaReserva"), rs.getInt("TipoReserva_idTipoReserva")};
//                reservas.add(reserva);
//
//            }
//            pstm.close();
//            return reservas;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarVenta(Timestamp fechaHora, int idUsuario) {
//
//        String sSQL = "INSERT INTO ventas (fechaHora, Usuarios_idUsuario) VALUES (" + fechaHora + ", " + idUsuario + ")";
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarVenta(int folioVenta, Timestamp fechaHora, int idUsuario) {
//
//        String sSQL = "UPDATE ventas SET fechaHora = " + fechaHora + ", Usuarios_idUsuario = " + idUsuario + " WHERE folio = " + folioVenta;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarVenta(int folioVenta) {
//        String sSQL = "DELETE FROM ventas WHERE idVenta = " + folioVenta;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarVentas() {
//        String sSQL = "SELECT * FROM ventas";
//        try {
//            ArrayList ventas = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] venta = {rs.getInt("folio"), rs.getString("fechaHora"), rs.getInt("Usuarios_idUsuario")};
//                ventas.add(venta);
//
//            }
//            pstm.close();
//            return ventas;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarVentaPlatillo(int folioVentaGeneral, String nombreCliente, int idPlatillo, float costo) {
//        String sSQL = "INSERT INTO ventasPlatillos (Ventas_Folio, nombreCliente, Platillos_idPlatillo, costo) VALUES (" + folioVentaGeneral + ",'" + nombreCliente + "', " + idPlatillo + ", " + costo + ")";
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarVentaPlatillo(int idVentaPLatillo, int folioVentaGeneral, String nombreCliente, int idPlatillo, float costo) {
//        String sSQL = "UPDATE ventas SET Ventas_folio = " + folioVentaGeneral + ", nombreCliente = '" + nombreCliente + "', Platillos_idPlatillos = " + idPlatillo + ", costo = " + costo
//                + " WHERE idVentaPlatillo = " + idVentaPLatillo;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarVentaPlatillo(int idVentaPlatillo) {
//        String sSQL = "DELETE FROM ventasPlatillos WHERE idVentaPlatillo = " + idVentaPlatillo;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarVentasPlatillos() {
//        String sSQL = "SELECT * FROM ventasPlatillos";
//        try {
//            ArrayList ventasPlatillos = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] ventaPlatillo = {rs.getInt("idVentaPLatillo"), rs.getInt("Ventas_folio"), rs.getString("nombreCliente"), rs.getInt("Platillos_idPlatillo"), rs.getFloat("costo")};
//                ventasPlatillos.add(ventaPlatillo);
//
//            }
//            pstm.close();
//            return ventasPlatillos;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarVentaCredito(int folioVentaGeneral, int idCliente, int cantidadDesayunos, int cantidadComidas, int cantidadCenas, float monto) {
//        String sSQL = "INSERT INTO ventasCredito (Ventas_Folio, Clientes_idCliente, cantidadDesayunos, cantidadComidas, cantidadCenas, monto) VALUES (" + folioVentaGeneral + "," + idCliente + ", " + cantidadDesayunos + ", " + cantidadComidas + ", " + cantidadCenas + ")";
//        try {
//            // PreparedStatementa
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarVentaCredito(int idVentaCredito, int folioVentaGeneral, int idCliente, int cantidadDesayunos, int cantidadComidas, int cantidadCenas, float monto) {
//        String sSQL = "UPDATE ventasCreditos SET Ventas_folio = " + folioVentaGeneral + ", Clientes_idCliente= " + idCliente + ", cantidadDesayunos = " + cantidadDesayunos + ", cantidadComidas = " + cantidadComidas + ", cantidadCenas = " + cantidadCenas + ", monto = " + monto
//                + " WHERE idVentaCredito = " + idVentaCredito;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//
//    }
//
//    @Override
//    public boolean eliminarEliminarCredito(int idVentaCredito) {
//        String sSQL = "DELETE FROM ventasCredito WHERE idVenta = " + idVentaCredito;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarVentasCredito() {
//        String sSQL = "SELECT * FROM ventasCredito";
//        try {
//            ArrayList ventasCredito = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] ventaCredito = {rs.getInt("idVentaCredito"), rs.getInt("Ventas_folio"), rs.getInt("idCliente"), rs.getInt("Platillos_idPlatillo"), rs.getInt("cantidadDesayunos"), rs.getInt("cantidadComidas"), rs.getInt("cantidadCenas"), rs.getFloat("monto")};
//                ventasCredito.add(ventaCredito);
//
//            }
//            pstm.close();
//            return ventasCredito;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarEntradaProducto(int idProducto, float cantidad, float gasto) {
//        String sSQL = "INSERT INTO entradasProductos (Productos_idProductos, cantidad, gasto) VALUES (" + idProducto + "," + cantidad + ", " + gasto + ")";
//        try {
//            // PreparedStatementa
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarEntradaProducto(int idEntradaProducto, int idProducto, float cantidad, float gasto) {
//        String sSQL = "UPDATE entradasProductos SET  Productos_idProductos= " + idProducto + ", cantidad = " + cantidad + ", gasto = " + gasto
//                + " WHERE idEntradasProductos = " + idEntradaProducto;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//
//    }
//
//    @Override
//    public boolean eliminarEntradaProducto(int idEntradaProducto) {
//        String sSQL = "DELETE FROM entradasproductos WHERE idEntradasProductos = " + idEntradaProducto;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarEntradasProducto() {
//        String sSQL = "SELECT * FROM entradasProductos";
//        try {
//            ArrayList entradasProducto = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] entradaProducto = {rs.getInt("idEntradasProductos"), rs.getInt("Productos_idProductos"), rs.getFloat("cantidad"), rs.getInt("Platillos_idPlatillo"), rs.getFloat("costo")};
//                entradasProducto.add(entradaProducto);
//            }
//            pstm.close();
//            return entradasProducto;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarProducto(String nombre, float stock) {
//        String sSQL = "INSERT INTO productos (nombre, stock) VALUES ('" + nombre + "'," + stock + ")";
//        try {
//            // PreparedStatementa
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarProducto(int idProducto, String nombre, float stock) {
//        String sSQL = "UPDATE productos SET nombre = '" + nombre + "', stock = " + stock
//                + " WHERE idProducto= " + idProducto;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//
//    }
//
//    @Override
//    public boolean eliminarProducto(int idProducto) {
//        String sSQL = "DELETE FROM productos WHERE idProducto = " + idProducto;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarProductos() {
//        String sSQL = "SELECT * FROM productos";
//        try {
//            ArrayList productos = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] producto = {rs.getInt("idProducto"), rs.getString("nombre"), rs.getFloat("stock")};
//                productos.add(producto);
//
//            }
//            pstm.close();
//            return productos;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public boolean insertarSalidaProducto(int idProducto, float cantidad) {
//        String sSQL = "INSERT INTO salidasProductos (Productos_idProductos, cantidad) VALUES (" + idProducto + "," + cantidad + ")";
//        try {
//            // PreparedStatementa
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean actualizarSalidaProducto(int idSalidaProducto, int idProducto, float cantidad) {
//        String sSQL = "UPDATE salidasProductos SET  Productos_idProductos= " + idProducto + ", cantidad = " + cantidad
//                + " WHERE idSalidasProductos = " + idSalidaProducto;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean eliminarSalidaProducto(int idSalidaProducto) {
//        String sSQL = "DELETE FROM salidasproductos WHERE idSalidasProductos= " + idSalidaProducto;
//        try {
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            pstm.execute();
//            pstm.close();
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        }
//    }
//
//    @Override
//    public ArrayList<Object[]> consultarSalidasProducto() {
//        String sSQL = "SELECT * FROM salidasProductos";
//        try {
//            ArrayList salidasProductos = new ArrayList<Object>();
//            // PreparedStatement
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                Object[] salidaProducto = {rs.getInt("idSalidasProductos"), rs.getInt("Productos_idProductos"), rs.getFloat("cantidad")};
//                salidasProductos.add(salidaProducto);
//
//            }
//            pstm.close();
//            return salidasProductos;
//        } catch (SQLException ex) {
//            return null;
//        }
//    }
//
//    @Override
//    public int obtenUltimoID() {
//        String sSQL = "SELECT LAST_INSERT_ID();";
//        int n = -1;
//        try {
//            PreparedStatement pstm = conn.prepareStatement(sSQL);
//            ResultSet rs = pstm.executeQuery(sSQL);
//            while (rs.next()) {
//                n = rs.getInt(1);
//            }
//            pstm.close();
//            return n;
//        } catch (SQLException ex) {
//            return -1;
//        }
//    }

    @Override
    public ArrayList<Usuario> consultarUsuarios() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
