package es.unizar.eina.M12_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Clase anotada como entidad que representa una parcela y que consta de título y cuerpo */
@Entity(tableName = "parcela")
public class Parcela {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "maxOcupantes")
    private int maxOcupantes;

    @ColumnInfo(name = "precioXpersona")
    private double precioXpersona;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    public Parcela(@NonNull String nombre, int maxOcupantes, double precioXpersona, String descripcion) {
        this.nombre = nombre;
        this.maxOcupantes = maxOcupantes;
        this.precioXpersona = precioXpersona;
        this.descripcion = descripcion;
    }

    /** Devuelve el identificador de la parcela */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el identificador de una parcela */
    public void setId(int id) {
        this.id = id;
    }

    /** Devuelve el nombre de la parcela */
    @NonNull
    public String getNombre(){
        return this.nombre;
    }

    /** Permite actualizar el nombre de una parcela */
    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    /** Devuelve el numero máximo de ocupantes de la parcela */
    public int getMaxOcupantes(){
        return this.maxOcupantes;
    }

    /** Permite actualizar el numero maximo de ocupantes de una parcela */
    public void setMaxOcupantes(int maxOcupantes) {
        this.maxOcupantes = maxOcupantes;
    }

    /** Devuelve el precio por persona de la parcela */
    public double getPrecioXpersona(){
        return this.precioXpersona;
    }

    /** Permite actualizar el precio por persona de una parcela */
    public void setPrecioXpersona(int precioXpersona) {
        this.precioXpersona = precioXpersona;
    }

    /** Devuelve el numero máximo de ocupantes de la parcela */
    public String getDescripcion(){
        return this.descripcion;
    }

    /** Permite actualizar el numero maximo de ocupantes de una parcela */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
