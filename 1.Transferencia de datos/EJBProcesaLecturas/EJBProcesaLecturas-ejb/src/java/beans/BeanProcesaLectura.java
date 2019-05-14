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
import objetos.Usuario;

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
        if (conexion.conectar()) {
            //Inserta la lectura en la bd
            Lectura l = new Lectura(999, new Sensor(idSensor, null, null), temperatura, humedad, Calendar.getInstance());
            conexion.insertarLectura(l);
            // Obtiene el usuario para ver si se necesita enviar notificación
            Usuario u = conexion.consultarUsuario();
            if (u != null && !u.getIdUsuario().equals("ERROR")) {
                if (u.isNotificacionActivada()) {
                    if (temperatura >= u.getTemperaturaCritica() || humedad >= u.getHumedadCritica()) {
                        // Enviar mail
                        // Se crea la clase runnable
                        Invernadero i = conexion.consultarInvernaderoPorSensor(new Sensor(idSensor, null, null));
                        HiloEnviaNotificacion hiloEnvia = new HiloEnviaNotificacion(u.getNombre(), u.getCorreo(), i.getIdInvernadero(), temperatura, humedad);
                        hiloEnvia.run();
                        return true;
                    }
                    
                }
            }
            return true;
        }
        return false;
    }

}
