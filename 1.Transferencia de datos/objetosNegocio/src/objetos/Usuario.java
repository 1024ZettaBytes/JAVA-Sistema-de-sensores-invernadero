/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objetos;

import java.util.Objects;

/**
 *
 * @author ed000
 */
public class Usuario {
    private String idUsuario;
    private String nombre;
    private String correo;
    private boolean notificacionActivada;
    private float temperaturaCritica;
    private float humedadCritica;

    public Usuario(String idUsuario, String nombre, String correo, boolean notificacionActivada, float temperaturaCritica, float humedadCritica) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.notificacionActivada = notificacionActivada;
        this.temperaturaCritica = temperaturaCritica;
        this.humedadCritica = humedadCritica;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isNotificacionActivada() {
        return notificacionActivada;
    }

    public void setNotificacionActivada(boolean notificacionActivada) {
        this.notificacionActivada = notificacionActivada;
    }

    public float getTemperaturaCritica() {
        return temperaturaCritica;
    }

    public void setTemperaturaCritica(float temperaturaCritica) {
        this.temperaturaCritica = temperaturaCritica;
    }

    public float getHumedadCritica() {
        return humedadCritica;
    }

    public void setHumedadCritica(float humedadCritica) {
        this.humedadCritica = humedadCritica;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idUsuario);
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
        final Usuario other = (Usuario) obj;
        if (this.idUsuario != other.idUsuario) {
            return false;
        }
        return true;
    }
    
}
