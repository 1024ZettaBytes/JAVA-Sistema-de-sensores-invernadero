/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import Conexion.ConexionBD;
import Interfaces.IConexion;
import interfaces.IAccesoDatos;
import java.util.Calendar;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import objetos.Invernadero;
import objetos.Lectura;
import objetos.Sensor;

/**
 *
 * @author ed000
 */
@Stateless
@Remote(IAccesoDatos.class)
public class BeanProcesaLectura implements IAccesoDatos {

    @Override
    public boolean agregarLectura(int idSensor, float temperatura, float humedad) {
        IConexion conexion = new ConexionBD();
        Lectura l = new Lectura(999, new Sensor(idSensor, null, null), temperatura, humedad, Calendar.getInstance());
        return conexion.conectar() && conexion.insertarLectura(l) && conexion.desconectar();
    }

}
