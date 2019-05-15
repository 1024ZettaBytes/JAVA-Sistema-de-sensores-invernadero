/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import Interfaces.IConexion;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import objetos.Invernadero;
import objetos.Lectura;
import objetos.Sensor;
import objetos.Usuario;
import org.json.JSONObject;

/**
 *
 * @author Eduardo Ramírez
 */
public class PruebaConexion {

    public static void main(String[] args) throws ParseException {

        IConexion conexion = new ConexionBD();

        if (conexion.conectar()) {
            Usuario u = conexion.consultarUsuario();
            boolean notif = true;
            float temp = 30;
            float hum = 15;
            u = new Usuario(u.getIdUsuario(), u.getNombre(), "ed00001110@gmail.com", notif, temp, hum);
            System.out.println(conexion.actualizarUsuario(u));
        }
    }
}
