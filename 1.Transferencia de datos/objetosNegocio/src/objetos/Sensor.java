/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;

/**
 *
 * @author ed000
 */
public class Sensor {
    private int idSensor;
    private Invernadero invernadero;
    private String marca;

    public Sensor(int idSensor, Invernadero invernadero, String marca) {
        this.idSensor = idSensor;
        this.invernadero = invernadero;
        this.marca = marca;
    }
public Sensor(Sensor sensor) {
    this.idSensor = sensor.getIdSensor();
    this.invernadero = sensor.getInvernadero();
    this.marca = sensor.getMarca();
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public Invernadero getInvernadero() {
        return invernadero;
    }

    public void setInvernadero(Invernadero invernadero) {
        this.invernadero = invernadero;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.idSensor;
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
        final Sensor other = (Sensor) obj;
        if (this.idSensor != other.idSensor) {
            return false;
        }
        return true;
    }
    
    
    
}
