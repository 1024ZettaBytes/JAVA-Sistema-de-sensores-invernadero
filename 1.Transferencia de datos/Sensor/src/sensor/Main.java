/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import interfaces.ISensor;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import objeto.Mensaje;

/**
 *
 * @author ed000
 */
public class Main {

    

    public static void main(String[] args) throws Exception{
        
        ISensor sensor1 = new Sensor(56);
        sensor1.enviarLectura(21, 10);
        
    
    }

}
