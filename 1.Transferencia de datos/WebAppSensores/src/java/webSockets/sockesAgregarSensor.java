/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webSockets;

import Conexion.ConexionBD;
import Interfaces.IConexion;
import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;
import objetos.Invernadero;
import objetos.Sensor;
import org.json.JSONObject;

/**
 *
 * @author ed000
 */
@ServerEndpoint("/agregarSensor")
public class sockesAgregarSensor {

    @OnMessage
    public boolean onMessage(String message) {
        IConexion conexion = new ConexionBD();
        if(conexion.conectar()){
        JSONObject datos = new JSONObject(message);
        int idInvernadero = datos.getInt("idInvernadero");
        int idSensor = datos.getInt("idSensor");
        String marca = datos.getString("marca");
        Sensor sensor = new Sensor(idSensor, new Invernadero(idInvernadero), marca);
        return conexion.insertarSensor(sensor);
    }
        return false;

}
    
}
