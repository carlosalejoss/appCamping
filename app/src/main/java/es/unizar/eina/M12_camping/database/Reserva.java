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
    @ColumnInfo(name = "telefono")
    private Integer telefono;

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
     * @param telefono      Teléfono de contacto del cliente.
     * @param fechaEntrada  Fecha de entrada de la reserva.
     * @param fechaSalida   Fecha de salida de la reserva.
     * @param precioTotal   Precio total de la reserva.
     */
    public Reserva(@NonNull String nombreCliente, @NonNull Integer telefono, @NonNull Date fechaEntrada, @NonNull Date fechaSalida, double precioTotal) {
        this.nombreCliente = nombreCliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.telefono = telefono;
        this.precioTotal = precioTotal;
    }

    // Métodos getter y setter con sus respectivos comentarios para Javadoc

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}
