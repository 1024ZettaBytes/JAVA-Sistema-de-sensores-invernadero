/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webSockets;

import Conexion.ConexionBD;
import Interfaces.IConexion;
import java.util.ArrayList;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import objetos.Invernadero;
import objetos.Sensor;
import org.json.JSONObject;

/**
 *
 * @author ed000
 */
@ServerEndpoint("/consultarDisponibilidadSensor")
public class socketDisponibilidadSensor {

    @OnMessage
    public boolean onMessage(String message) {
        IConexion conexion = new ConexionBD();
        conexion.conectar();

        Sensor s = new Sensor(new Integer(message), null, null);
        ArrayList<Sensor> sensores= conexion.consultarSensores();
        conexion.desconectar();
        return !sensores.contains(s);
    }
    
}
