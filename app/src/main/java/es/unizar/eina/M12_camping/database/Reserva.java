package es.unizar.eina.M12_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * Clase que representa una reserva en el sistema de camping.
 * Cada reserva incluye información relevante sobre el cliente, las fechas de entrada y salida, y otros detalles.
 * Esta clase está anotada como una entidad de Room, lo que la convierte en una tabla de la base de datos.
 */
@Entity(tableName = "reserva")
public class Reserva {

    /** ID único de la reserva, autogenerado. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    /** Nombre del cliente que realiza la reserva. */
    @NonNull
    @ColumnInfo(name = "nombreCliente")
    private String nombreCliente;

    /** Teléfono de contacto del cliente. */
    @NonNull
    @ColumnInfo(name = "numeroMovil")
    private Integer numeroMovil;

    /** Fecha de entrada de la reserva. */
    @NonNull
    @ColumnInfo(name = "fechaEntrada")
    @TypeConverters(DateConverter.class)
    private Date fechaEntrada;

    /** Fecha de salida de la reserva. */
    @NonNull
    @ColumnInfo(name = "fechaSalida")
    @TypeConverters(DateConverter.class)
    private Date fechaSalida;

    /** Precio total de la reserva. */
    @ColumnInfo(name = "precioTotal")
    private double precioTotal;

    /**
     * Constructor para la clase Reserva.
     *
     * @param nombreCliente Nombre del cliente que realiza la reserva.
     * @param numeroMovil   Teléfono de contacto del cliente.
     * @param fechaEntrada  Fecha de entrada de la reserva.
     * @param fechaSalida   Fecha de salida de la reserva.
     * @param precioTotal   Precio total de la reserva.
     */
    public Reserva(@NonNull String nombreCliente, @NonNull Integer numeroMovil,
                   @NonNull Date fechaEntrada, @NonNull Date fechaSalida,
                   double precioTotal) {
        this.nombreCliente = nombreCliente;
        this.numeroMovil = numeroMovil;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.precioTotal = precioTotal;
    }

    /**
     * Obtiene el ID único de la reserva.
     *
     * @return El ID de la reserva.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID único de la reserva.
     *
     * @param id El ID de la reserva.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del cliente asociado a la reserva.
     *
     * @return El nombre del cliente.
     */
    @NonNull
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Establece el nombre del cliente asociado a la reserva.
     *
     * @param nombreCliente El nombre del cliente.
     */
    public void setNombreCliente(@NonNull String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * Obtiene el número de contacto del cliente asociado a la reserva.
     *
     * @return El número de contacto del cliente.
     */
    @NonNull
    public Integer getNumeroMovil() {
        return numeroMovil;
    }

    /**
     * Establece el número de contacto del cliente asociado a la reserva.
     *
     * @param numeroMovil El número de contacto del cliente.
     */
    public void setNumeroMovil(@NonNull Integer numeroMovil) {
        this.numeroMovil = numeroMovil;
    }

    /**
     * Obtiene la fecha de entrada de la reserva.
     *
     * @return La fecha de entrada.
     */
    @NonNull
    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    /**
     * Establece la fecha de entrada de la reserva.
     *
     * @param fechaEntrada La fecha de entrada.
     */
    public void setFechaEntrada(@NonNull Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    /**
     * Obtiene la fecha de salida de la reserva.
     *
     * @return La fecha de salida.
     */
    @NonNull
    public Date getFechaSalida() {
        return fechaSalida;
    }

    /**
     * Establece la fecha de salida de la reserva.
     *
     * @param fechaSalida La fecha de salida.
     */
    public void setFechaSalida(@NonNull Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    /**
     * Obtiene el precio total de la reserva.
     *
     * @return El precio total.
     */
    public double getPrecioTotal() {
        return precioTotal;
    }

    /**
     * Establece el precio total de la reserva.
     *
     * @param precioTotal El precio total.
     */
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

}
