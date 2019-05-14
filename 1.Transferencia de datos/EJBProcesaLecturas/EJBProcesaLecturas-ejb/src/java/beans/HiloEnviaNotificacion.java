/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author ed000
 */
public class HiloEnviaNotificacion implements Runnable{
    String nombreUsuario;
    String correo;
    int idInvernadero;
    float temperatura;
    float humedad;

    public HiloEnviaNotificacion(String nombreUsuario, String correo,int idInvernadero, float temperatura, float humedad) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.idInvernadero = idInvernadero;
        this.temperatura = temperatura;
        this.humedad = humedad;
    }

    @Override
    public void run() {
       String host="smtp.gmail.com";  
  final String user="arquitecturadesoftwareitson@gmail.com";//change accordingly  
  final String password="passarquitectura";//change accordingly  
    
  
  
   //Get the session object  
   Properties props = new Properties();  
   props.put("mail.smtp.host",host);
   props.put("mail.smtp.port", 587);
   props.put("mail.smtp.starttls.enable", "true");
   props.put("mail.smtp.auth", "true");  
     
   Session session = Session.getInstance(props,  
    new javax.mail.Authenticator() {  
      protected PasswordAuthentication getPasswordAuthentication() {  
    return new PasswordAuthentication(user,password);  
      }  
    });  
  
   //Compose the message  
    try {  
     MimeMessage message = new MimeMessage(session);  
     message.setFrom(new InternetAddress(user));  
     message.addRecipient(Message.RecipientType.TO,new InternetAddress(correo));  
     message.setSubject("Notificación invernadero");
     String mensaje = "Eduardo, recibes este correo debido a que el invernadero "
             +idInvernadero+" ha alcanzado los parámetros críticos de temperatura y/o humedad.\n"
             +"- Temperatura: "+String.format("%.2f", temperatura)+" °C\n"
             +"- Humedad: "+String.format("%.2f", humedad)+" RH\n\n"
             +"Considere tomar las medidas necesarias.";
     message.setText(mensaje);  
       
    //send the message  
     Transport.send(message);  
  
     System.out.println("Correo enviado");  
   
     } catch (MessagingException e) {e.printStackTrace();}  
    }
    
}
