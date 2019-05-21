/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webServices;

import Conexion.ConexionBD;
import Interfaces.IConexion;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import objetos.Invernadero;
import objetos.Lectura;
import objetos.Sensor;
import objetos.Usuario;
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
            if (lecturas != null) {
                JSONObject j;
                String datos = "{lecturas:[";
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
                datos += "]}";
                j = new JSONObject(datos);
                return j.toString(1);
            } else {
                return "ERROR_BD";
            }
        } else {
            return "ERROR_BD";
        }
    }

    @POST
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    public String autentica(String datosJson) throws GeneralSecurityException, IOException {
        JSONObject j = new JSONObject(datosJson);
        String idTokenUsuario = j.getString("token");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(getHttpTransport(), getDefaultJsonFactory()).setAudience(Collections.singletonList("340054074881-5aikhj5soo0ifgm55ddraja20cj70qoc.apps.googleusercontent.com"))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        // (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(idTokenUsuario);
        if (idToken != null) {

            Payload payload = idToken.getPayload();

            // Get profile information from payload
            String email = payload.getEmail();
            IConexion c = new ConexionBD();
            if (c.conectar()) {
                if(c.consultarUsuario().getCorreo().equals(email)){
                    return "1";
                }
                else{
                    return "0";
                }
            }
            else{
                return "0";
            }

        } else {
            return "0";
        }

    }

    public NetHttpTransport getHttpTransport() {
        return new NetHttpTransport();
    }

    private static JsonFactory getDefaultJsonFactory() {
        return JacksonFactory.getDefaultInstance();
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
            if (lecturas != null) {
                JSONObject j;
                String datos = "{lecturas:[";
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

                datos += "]}";
                j = new JSONObject(datos);
                return j.toString(1);
            } else {
                return "ERROR_BD";
            }
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
                    if (lecturas != null) {
                        JSONObject j;
                        String datos = "{lecturas:[";
                        Invernadero i = new Invernadero(idInvernadero);
                        SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat formaterHora = new SimpleDateFormat("HH:mm:ss");

                        for (Lectura lectura : lecturas) {
                            String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                            if (lectura.getSensor().getInvernadero().equals(i) && fechaTexto.equals(fecha)) {
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

                        datos += "]}";
                        j = new JSONObject(datos);
                        return j.toString(1);
                    } else {
                        return "ERROR_BD";
                    }
                } else {
                    return "ERROR_BD";
                }
            } else {
                return "ERROR_FORMATO_FECHA";
            }
        }
        return "ERROR_FORMATO_FECHA";
    }

    @GET
    @Path("lecturas/invernadero/fecha/promedio/{idInvernadero}/{fecha}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonLecturasInvernaderoPromedioFecha(@PathParam("idInvernadero") int idInvernadero, @PathParam("fecha") String fecha) {
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

                    if (lecturas != null) {
                        JSONObject j;
                        String datos = "{ idInvernadero:" + idInvernadero + ","
                                + "fecha:" + fecha + ","
                                + "lecturasPromedio:[";
                        SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                        Invernadero i = new Invernadero(idInvernadero);
                        float sumaT = 0;
                        float sumaH = 0;
                        int nLect = 0;
                        for (Lectura lectura : lecturas) {
                            String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                            if (lectura.getSensor().getInvernadero().equals(i) && fechaTexto.equals(fecha)) {
                                float temperatura = lectura.getTemperatura();
                                float humedad = lectura.getHumedad();
                                sumaT += temperatura;
                                sumaH += humedad;
                                nLect++;
                            }
                        }
                        if (nLect > 0) {
                            float promedioT = sumaT / nLect;
                            float promedioH = sumaH / nLect;
                            datos = datos + "{temperaturaPromedio:" + String.format("%.2f", promedioT) + ","
                                    + "humedadPromedio:" + String.format("%.2f", promedioH) + "}";

                        }

                        datos += "]}";
                        j = new JSONObject(datos);
                        return j.toString(1);
                    } else {
                        return "ERROR_BD";
                    }
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
    @POST
    @Path("usuario/alarma/programar")
    @Consumes(MediaType.APPLICATION_JSON)
    public String actualizaAlarmaUsuario(String datosJson) {
        JSONObject j = new JSONObject(datosJson);
        String correo = j.getString("correo");
        if (isValid(correo)) {
            IConexion c = new ConexionBD();
            if (c.conectar()) {
                Usuario u = c.consultarUsuario();
                if (u != null) {
                    if (!u.getIdUsuario().equals("ERROR")) {
                        String id = u.getIdUsuario();
                        boolean notif = j.getBoolean("notificacion");
                        float temp = j.getFloat("temperaturaCritica");
                        float hum = j.getFloat("humedadCritica");
                        u = new Usuario(id, null, correo, notif, temp, hum);
                        if (c.actualizarUsuario(u)) {
                            return "SUCCES";
                        } else {
                            return "ERROR_BD";
                        }
                    } else {
                        return "ERROR_BD";
                    }

                } else {
                    return "ERROR_BD";
                }
            } else {
                return "ERROR_BD";
            }
        } else {
            return "ERROR_FORMATO_CORREO";
        }

    }

    @GET
    @Path("lecturas/sensor/id/{idSensor}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonLecturasSensor(@PathParam("idSensor") int idSensor) {
        IConexion c = new ConexionBD();
        if (c.conectar()) {
            ArrayList<Lectura> lecturas = c.consultarLecturas();
            if (lecturas != null) {
                JSONObject j;
                String datos = "{lecturas:[";
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
                datos += "]}";
                j = new JSONObject(datos);
                return j.toString(1);
            } else {
                return "ERROR_BD";
            }

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
                    if (lecturas != null) {
                        JSONObject j;
                        String datos = "{lecturas:[";
                        Sensor s = new Sensor(idSensor, null, null);
                        SimpleDateFormat formaterFecha = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat formaterHora = new SimpleDateFormat("HH:mm:ss");

                        for (Lectura lectura : lecturas) {
                            String fechaTexto = formaterFecha.format(lectura.getFechaHora().getTime());
                            if (lectura.getSensor().equals(s) && fechaTexto.equals(fecha)) {
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
                        datos += "]}";
                        j = new JSONObject(datos);
                        return j.toString(1);
                    } else {
                        return "ERROR_BD";
                    }

                } else {
                    return "ERROR_BD";
                }
            } else {
                return "ERROR_FORMATO_FECHA";
            }
        }
        return "ERROR_FORMATO_FECHA";
    }

    @GET
    @Path("/sensores/invernadero/id/{idInvernadero}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonSensoresInvernadero(@PathParam("idInvernadero") int idInvernadero) {
        IConexion c = new ConexionBD();
        if (c.conectar()) {
            ArrayList<Sensor> sensores = c.consultarSensores();
            if (sensores != null) {
                JSONObject j;
                String datos = "{sensores:[";
                Invernadero i = new Invernadero(idInvernadero);
                for (Sensor sensor : sensores) {
                    if (sensor.getInvernadero().equals(i)) {
                        int idSensor = sensor.getIdSensor();
                        String marca = sensor.getMarca();
                        datos = datos + "{idSensor:" + idSensor + ","
                                + "idInvernadero:" + idInvernadero + ","
                                + "marca:" + marca
                                + "}";
                        if (sensores.indexOf(sensor) < sensores.size() - 1) {
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
        } else {
            return "ERROR_BD";
        }
    }

    private boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
