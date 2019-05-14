/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objeto;

/**
 *
 * @author ed000
 */
public class Mensaje {

    private int idSensor;
    private float temperatura;
    private float humedad;

    public Mensaje(int id, float temperatura, float humedad) {
        this.idSensor= id;
        this.temperatura = temperatura;
        this.humedad = humedad;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(float humedad) {
        this.humedad = humedad;
    }
    @Override
    public String toString(){
         String msg;
        msg = "{\n"
                + "\t'idSensor':'" + idSensor + "',\n"
                + "\t'temperatura':'" + temperatura + "',\n"
                + "\t'humedad':'" + humedad + "'\n"
                + "}";
        return msg;
    }
    
}
