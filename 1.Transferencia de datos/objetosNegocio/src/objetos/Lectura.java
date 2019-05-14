/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;

import java.util.Calendar;

/**
 *
 * @author ed000
 */
public class Lectura {
    private int idLectura;
    private Sensor sensor;
    private float temperatura;
    private float humedad;
    private Calendar fechaHora;

    public Lectura(Lectura lectura) {
        this.idLectura = lectura.getIdLectura();
        this.sensor = lectura.getSensor();
        this.temperatura = lectura.getTemperatura();
        this.humedad = lectura.getHumedad();
        this.fechaHora = lectura.getFechaHora();
    }

    public Lectura(int idLectura, Sensor sensor, float temperatura, float humedad, Calendar fechaHora) {
        this.idLectura = idLectura;
        this.sensor = sensor;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.fechaHora = fechaHora;
    }

    public int getIdLectura() {
        return idLectura;
    }

    public void setIdLectura(int idLectura) {
        this.idLectura = idLectura;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public float getHumedad() {
        return humedad;
    }

    public void setHumedad(float humedad) {
        this.humedad = humedad;
    }

    public Calendar getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Calendar fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.idLectura;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Lectura other = (Lectura) obj;
        if (this.idLectura != other.idLectura) {
            return false;
        }
        return true;
    }
    
}
