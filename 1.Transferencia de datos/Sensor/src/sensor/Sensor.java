/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensor;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import objeto.Mensaje;

/**
 *
 * @author ed000
 */
public class Sensor {

    

    public static void main(String[] args) throws Exception{
        
        Mensaje msj = new Mensaje(5, 100f,10f);
        //Cola a la que se enviarán los mensajes
        String nombreCola = "colaLecturas";
        //Dirección del host en la que se encuentra la cola a la que se enviarán los mesajes
        String host = "localhost";
        //Por default el puerto es 5672
        
        // Se crea el factory y se establece el host al que se conectará
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
         // Se establece la conexión
            Connection connection = factory.newConnection();
            // Se crea un canal en la conexión
            Channel channel = connection.createChannel();
        try {
           
            // Se indica el nombre de la cola
            channel.queueDeclare(nombreCola, false, false, false, null);
            String message = msj.toString();
            // Se envía el mensaje al final de la cola
            channel.basicPublish("", nombreCola, null, message.getBytes());
            System.out.println(" [x] Se envío '" + message + "' a '"+host+"'");

        } catch (IOException ex) {
            System.out.println("Error: No se pudo establecer la conexión.");
        }finally{
            channel.close();
            connection.close();
        }
        
    
    }

}
