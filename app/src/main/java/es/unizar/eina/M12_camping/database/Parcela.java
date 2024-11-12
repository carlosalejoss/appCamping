package es.unizar.eina.M12_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Esta clase representa una parcela en un camping.
 * La clase incluye el nombre, el número máximo de ocupantes,
 * el precio por persona y la descripción de la parcela.
 */
@Entity(tableName = "parcela")
public class Parcela {

    /** id de la parcela */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    /** nombre de la parcela */
    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    /** numero maximo de ocupantes de la parcela */
    @ColumnInfo(name = "maxOcupantes")
    private int maxOcupantes;

    /** precio por persona de la parcela */
    @ColumnInfo(name = "precioXpersona")
    private double precioXpersona;

    /** descripcion de la parcela */
    @ColumnInfo(name = "descripcion")
    private String descripcion;

    /**
     * Constructor para inicializar los detalles de la parcela.
     *
     * @param nombre El nombre de la parcela.
     * @param maxOcupantes El número máximo de ocupantes de la parcela.
     * @param precioXpersona El precio por persona para esta parcela.
     * @param descripcion La descripción de la parcela.
     */
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
    public void setPrecioXpersona(double precioXpersona) {
        this.precioXpersona = precioXpersona;
    }

    /** Devuelve la descripcion de la parcela */
    public String getDescripcion(){
        return this.descripcion;
    }

    /** Permite actualizar la descripcion de una parcela */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
