package es.unizar.eina.M12_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Esta clase representa una parcela en un camping.
 * La clase incluye el nombre, el numero maximo de ocupantes,
 * el precio por persona y la descripcion de la parcela.
 */
@Entity(tableName = "parcela")
public class Parcela {

    /** ID unico de la parcela */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    /** Nombre de la parcela */
    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    /** Numero maximo de ocupantes de la parcela */
    @ColumnInfo(name = "maxOcupantes")
    private int maxOcupantes;

    /** Precio por persona de la parcela */
    @ColumnInfo(name = "precioXpersona")
    private double precioXpersona;

    /** Descripcion de la parcela */
    @NonNull
    @ColumnInfo(name = "descripcion")
    private String descripcion;

    /**
     * Constructor para inicializar los detalles de la parcela.
     *
     * @param nombre         El nombre de la parcela.
     * @param maxOcupantes   El numero maximo de ocupantes de la parcela.
     * @param precioXpersona El precio por persona para esta parcela.
     * @param descripcion    La descripcion de la parcela.
     */
    public Parcela(@NonNull String nombre, Integer maxOcupantes, Double precioXpersona, @NonNull String descripcion) {
        if (maxOcupantes <= 0) {
            throw new IllegalArgumentException("El numero maximo de ocupantes debe ser mayor que 0.");
        }
        if (precioXpersona <= 0) {
            throw new IllegalArgumentException("El precio por persona debe ser mayor que 0.");
        }
        this.nombre = nombre;
        this.maxOcupantes = maxOcupantes;
        this.precioXpersona = precioXpersona;
        this.descripcion = descripcion;
    }

    /**
     * Devuelve el identificador unico de la parcela.
     *
     * @return El ID de la parcela.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Permite actualizar el identificador unico de una parcela.
     *
     * @param id El nuevo ID de la parcela.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la parcela.
     *
     * @return El nombre de la parcela.
     */
    @NonNull
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Permite actualizar el nombre de una parcela.
     *
     * @param nombre El nuevo nombre de la parcela.
     */
    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el numero maximo de ocupantes permitido en la parcela.
     *
     * @return El numero maximo de ocupantes.
     */
    public int getMaxOcupantes() {
        return this.maxOcupantes;
    }

    /**
     * Permite actualizar el numero maximo de ocupantes de una parcela.
     *
     * @param maxOcupantes El nuevo numero maximo de ocupantes.
     */
    public void setMaxOcupantes(int maxOcupantes) {
        if (maxOcupantes <= 0) {
            throw new IllegalArgumentException("El numero maximo de ocupantes debe ser mayor que 0.");
        }
        this.maxOcupantes = maxOcupantes;
    }

    /**
     * Devuelve el precio por persona de la parcela.
     *
     * @return El precio por persona.
     */
    public double getPrecioXpersona() {
        return this.precioXpersona;
    }

    /**
     * Permite actualizar el precio por persona de una parcela.
     *
     * @param precioXpersona El nuevo precio por persona.
     */
    public void setPrecioXpersona(double precioXpersona) {
        if (precioXpersona <= 0) {
            throw new IllegalArgumentException("El precio por persona debe ser mayor que 0.");
        }
        this.precioXpersona = precioXpersona;
    }

    /**
     * Devuelve la descripcion de la parcela.
     *
     * @return La descripcion de la parcela.
     */
    @NonNull
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Permite actualizar la descripcion de una parcela.
     *
     * @param descripcion La nueva descripcion de la parcela.
     */
    public void setDescripcion(@NonNull String descripcion) {
        this.descripcion = descripcion;
    }

}