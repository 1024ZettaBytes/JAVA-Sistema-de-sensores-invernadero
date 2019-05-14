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
       
        
        if(conexion.conectar()){
       ArrayList<Lectura> lecturas = conexion.consultarLecturas();
       SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
                    JSONObject j;
                   String datos="{lecturas:[";
                    Lectura lectura = lecturas.get(0);
                        int idLectura= lectura.getIdLectura();
                        int idSensor = lectura.getSensor().getIdSensor();
                        String marca = lectura.getSensor().getMarca();
                        int idInvernadero = lectura.getSensor().getInvernadero().getIdInvernadero();
                        float temperatura = lectura.getTemperatura();
                        float humedad= lectura.getHumedad();
                        String fechaHora = formater.format(lectura.getFechaHora().getTime());
                        datos = datos+"{idLectura:"+idLectura+","
                                  +"sensor:{"
                                            +"idSensor:"+idSensor+","
                                            +"marca:"+marca+"},"
                                  +"idInvernadero:"+idInvernadero+","
                                  +"temperatura:"+temperatura+","
                                  +"humedad:"+humedad+","
                                +"fechaHora:'"+fechaHora
                                +"'}"
                          +"]}";
                          j = new JSONObject(datos);
                          
                                  
                        
                    
            System.out.println(j.toString(2));
        }
    }
}
