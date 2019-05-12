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
public class Invernadero {
    private int idInvernadero;
public Invernadero(int id){
    this.idInvernadero = id;
}
public Invernadero(Invernadero invernadero){
    this.idInvernadero = invernadero.getIdInvernadero();
}
    public int getIdInvernadero() {
        return idInvernadero;
    }

    public void setIdInvernadero(int idInvernadero) {
        this.idInvernadero = idInvernadero;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.idInvernadero;
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
        final Invernadero other = (Invernadero) obj;
        if (this.idInvernadero != other.idInvernadero) {
            return false;
        }
        return true;
    }
    
}
