/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import Conexion.ConexionBD;
import Interfaces.IConexion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import objetos.Invernadero;
import objetos.Lectura;
import objetos.Sensor;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author ed000
 */
@Path("wsSensores")
public class wsSensores {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of wsSensores
     */
    public wsSensores() {
    }

    @GET
    @Path("/lecturas")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonLecturas() {
        IConexion c = new ConexionBD();
        if (c.conectar()) {
            ArrayList<Lectura> lecturas = c.consultarLecturas();
            JSONObject j;
            String datos = "{lecturas:[";
            if (lecturas != null) {
                SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formaterHora = new SimpleDateFormat("HH:mm:ss");
                for (Lectura lectura : lecturas) {
                    int idLectura = lectura.getIdLectura();
                    int idSensor = lectura.getSensor().getIdSensor();
                    int idInvernadero = lectura.getSensor().getInvernadero().getIdInvernadero();
                    String marca = lectura.getSensor().getMarca();
                    float temperatura = lectura.getTemperatura();
                    float humedad = lectura.getHumedad();
                    String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                    String horaTexto = formaterHora.format(lectura.getFechaHora().getTime());
                    datos = datos + "{idLectura:" + idLectura + ","
                            + "sensor:{"
                            + "idSensor:" + idSensor + ","
                            + "marca:" + marca + "},"
                            + "idInvernadero:" + idInvernadero + ","
                            + "temperatura:" + temperatura + ","
                            + "humedad:" + humedad + ","
                            + "fecha:" + fechaTexto + ","
                            + "hora:'" + horaTexto + "'"
                            + "}";

                    if (lecturas.indexOf(lectura) < lecturas.size() - 1) {
                        datos += ",";
                    }
                }

            }
            datos += "]}";
            j = new JSONObject(datos);

            return j.toString(1);
        } else {
            return "ERROR_BD";
        }
    }

    /**
     * Retrieves representation of an instance of webServices.wsSensores
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/lecturas/invernadero/id/{idInvernadero}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonLecturasInvernadero(@PathParam("idInvernadero") int idInvernadero) {
        IConexion c = new ConexionBD();
        if (c.conectar()) {
            ArrayList<Lectura> lecturas = c.consultarLecturas();
            JSONObject j;
            String datos = "{lecturas:[";
            if (lecturas != null) {

                Invernadero i = new Invernadero(idInvernadero);
                SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formaterHora = new SimpleDateFormat("HH:mm:ss");

                for (Lectura lectura : lecturas) {
                    if (lectura.getSensor().getInvernadero().equals(i)) {
                        int idLectura = lectura.getIdLectura();
                        int idSensor = lectura.getSensor().getIdSensor();
                        String marca = lectura.getSensor().getMarca();
                        float temperatura = lectura.getTemperatura();
                        float humedad = lectura.getHumedad();
                        String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                        String horaTexto = formaterHora.format(lectura.getFechaHora().getTime());
                        datos = datos + "{idLectura:" + idLectura + ","
                                + "sensor:{"
                                + "idSensor:" + idSensor + ","
                                + "marca:" + marca + "},"
                                + "idInvernadero:" + idInvernadero + ","
                                + "temperatura:" + temperatura + ","
                                + "humedad:" + humedad + ","
                                + "fecha:" + fechaTexto + ","
                                + "hora:'" + horaTexto + "'"
                                + "}";

                        if (lecturas.indexOf(lectura) < lecturas.size() - 1) {
                            datos += ",";
                        }
                    }

                }

            }
            datos += "]}";
            j = new JSONObject(datos);
            return j.toString(1);
        } else {
            return "ERROR_BD";
        }
    }

    @GET
    @Path("lecturas/invernadero/fecha/{idInvernadero}/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonLecturasInvernaderoFecha(@PathParam("idInvernadero") int idInvernadero, @PathParam("fecha") String fecha) {
        if (fecha.length() == 10) {
            boolean sonDigitos = Character.isDigit(fecha.charAt(0))
                    && Character.isDigit(fecha.charAt(1))
                    && Character.isDigit(fecha.charAt(3))
                    && Character.isDigit(fecha.charAt(4))
                    && Character.isDigit(fecha.charAt(6))
                    && Character.isDigit(fecha.charAt(7))
                    && Character.isDigit(fecha.charAt(8))
                    && Character.isDigit(fecha.charAt(9));
            boolean estaSeparada = fecha.charAt(2) == '-' && fecha.charAt(5) == '-';
            if (sonDigitos && estaSeparada) {
                IConexion c = new ConexionBD();
                if (c.conectar()) {
                    ArrayList<Lectura> lecturas = c.consultarLecturas();
                    JSONObject j;
                    String datos = "{lecturas:[";
                    if (lecturas != null) {

                        Invernadero i = new Invernadero(idInvernadero);
                        SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat formaterHora = new SimpleDateFormat("HH:mm:ss");

                        for (Lectura lectura : lecturas) {
                            String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                            if (lectura.getSensor().getInvernadero().equals(i) && fechaTexto.equals(fecha) ) {
                                int idLectura = lectura.getIdLectura();
                                int idSensor = lectura.getSensor().getIdSensor();
                                String marca = lectura.getSensor().getMarca();
                                float temperatura = lectura.getTemperatura();
                                float humedad = lectura.getHumedad();
                                
                                String horaTexto = formaterHora.format(lectura.getFechaHora().getTime());
                                datos = datos + "{idLectura:" + idLectura + ","
                                        + "sensor:{"
                                        + "idSensor:" + idSensor + ","
                                        + "marca:" + marca + "},"
                                        + "idInvernadero:" + idInvernadero + ","
                                        + "temperatura:" + temperatura + ","
                                        + "humedad:" + humedad + ","
                                        + "fecha:" + fechaTexto + ","
                                        + "hora:'" + horaTexto + "'"
                                        + "}";

                                if (lecturas.indexOf(lectura) < lecturas.size() - 1) {
                                    datos += ",";
                                }
                            }

                        }

                    }
                    datos += "]}";
                    j = new JSONObject(datos);
                    return j.toString(1);
                } else {
                    return "ERROR_BD";
                }
            } else {
                return "ERROR_FORMATO_FECHA";
            }
        }
        return "ERROR_FORMATO_FECHA";
    }

    /**
     * PUT method for updating or creating an instance of wsSensores
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    @GET
    @Path("lecturas/sensor/id/{idSensor}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonLecturasSensor(@PathParam("idSensor") int idSensor) {
       IConexion c = new ConexionBD();
        if (c.conectar()) {
            ArrayList<Lectura> lecturas = c.consultarLecturas();
            JSONObject j;
            String datos = "{lecturas:[";
            if (lecturas != null) {

                Sensor s = new Sensor(idSensor, null, null);
                SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat formaterHora = new SimpleDateFormat("HH:mm:ss");

                for (Lectura lectura : lecturas) {
                    if (lectura.getSensor().equals(s)) {
                        int idLectura = lectura.getIdLectura();
                        int idInvernadero = lectura.getSensor().getInvernadero().getIdInvernadero();
                        String marca = lectura.getSensor().getMarca();
                        float temperatura = lectura.getTemperatura();
                        float humedad = lectura.getHumedad();
                        String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                        String horaTexto = formaterHora.format(lectura.getFechaHora().getTime());
                        datos = datos + "{idLectura:" + idLectura + ","
                                + "sensor:{"
                                + "idSensor:" + idSensor + ","
                                + "marca:" + marca + "},"
                                + "idInvernadero:" + idInvernadero + ","
                                + "temperatura:" + temperatura + ","
                                + "humedad:" + humedad + ","
                                + "fecha:" + fechaTexto + ","
                                + "hora:'" + horaTexto + "'"
                                + "}";

                        if (lecturas.indexOf(lectura) < lecturas.size() - 1) {
                            datos += ",";
                        }
                    }

                }

            }
            datos += "]}";
            j = new JSONObject(datos);
            return j.toString(1);
        } else {
            return "ERROR_BD";
        }
    }
    
    
@GET
    @Path("lecturas/sensor/fecha/{idSensor}/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonLecturasSensorFecha(@PathParam("idSensor") int idSensor, @PathParam("fecha") String fecha) {
        if (fecha.length() == 10) {
            boolean sonDigitos = Character.isDigit(fecha.charAt(0))
                    && Character.isDigit(fecha.charAt(1))
                    && Character.isDigit(fecha.charAt(3))
                    && Character.isDigit(fecha.charAt(4))
                    && Character.isDigit(fecha.charAt(6))
                    && Character.isDigit(fecha.charAt(7))
                    && Character.isDigit(fecha.charAt(8))
                    && Character.isDigit(fecha.charAt(9));
            boolean estaSeparada = fecha.charAt(2) == '-' && fecha.charAt(5) == '-';
            if (sonDigitos && estaSeparada) {
                IConexion c = new ConexionBD();
                if (c.conectar()) {
                    ArrayList<Lectura> lecturas = c.consultarLecturas();
                    JSONObject j;
                    String datos = "{lecturas:[";
                    if (lecturas != null) {

                        Sensor s = new Sensor(idSensor, null, null);
                        SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat formaterHora = new SimpleDateFormat("HH:mm:ss");

                        for (Lectura lectura : lecturas) {
                            String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                            if (lectura.getSensor().equals(s) && fechaTexto.equals(fecha) ) {
                                int idLectura = lectura.getIdLectura();
                                int idInvernadero = lectura.getSensor().getInvernadero().getIdInvernadero();
                                String marca = lectura.getSensor().getMarca();
                                float temperatura = lectura.getTemperatura();
                                float humedad = lectura.getHumedad();
                                
                                String horaTexto = formaterHora.format(lectura.getFechaHora().getTime());
                                datos = datos + "{idLectura:" + idLectura + ","
                                        + "sensor:{"
                                        + "idSensor:" + idSensor + ","
                                        + "marca:" + marca + "},"
                                        + "idInvernadero:" + idInvernadero + ","
                                        + "temperatura:" + temperatura + ","
                                        + "humedad:" + humedad + ","
                                        + "fecha:" + fechaTexto + ","
                                        + "hora:'" + horaTexto + "'"
                                        + "}";

                                if (lecturas.indexOf(lectura) < lecturas.size() - 1) {
                                    datos += ",";
                                }
                            }

                        }

                    }
                    datos += "]}";
                    j = new JSONObject(datos);
                    return j.toString(1);
                } else {
                    return "ERROR_BD";
                }
            } else {
                return "ERROR_FORMATO_FECHA";
            }
        }
        return "ERROR_FORMATO_FECHA";
    }
}
