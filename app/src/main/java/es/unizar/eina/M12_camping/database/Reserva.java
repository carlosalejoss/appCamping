package es.unizar.eina.M12_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

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

     /** Lista de parcelas reservadas con número de ocupantes*/
     @NonNull
     @ColumnInfo(name = "parcelasReservadas")
     @TypeConverters(ParcelaReservadaConverter.class)
     private List<ParcelaReservada> parcelasReservadas;

    /** Precio total de la reserva. */
    @ColumnInfo(name = "precioTotal")
    private double precioTotal;

    /**
     * Constructor para la clase Reserva.
     *
     * @param nombreCliente Nombre del cliente que realiza la reserva.
     * @param numeroMovil      Teléfono de contacto del cliente.
     * @param fechaEntrada  Fecha de entrada de la reserva.
     * @param fechaSalida   Fecha de salida de la reserva.
     * @param precioTotal   Precio total de la reserva.
     */
    public Reserva(@NonNull String nombreCliente, @NonNull Integer numeroMovil,
                   @NonNull Date fechaEntrada, @NonNull Date fechaSalida,
                   @NonNull List<ParcelaReservada> parcelasReservadas, double precioTotal) {
        this.nombreCliente = nombreCliente;
        this.numeroMovil = numeroMovil;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.parcelasReservadas = parcelasReservadas;
        this.precioTotal = precioTotal;
    }

    // Métodos getter y setter con sus respectivos comentarios para Javadoc

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(@NonNull String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    @NonNull
    public Integer getNumeroMovil() {
        return numeroMovil;
    }

    public void setNumeroMovil(@NonNull Integer numeroMovil) {
        this.numeroMovil = numeroMovil;
    }

    @NonNull
    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(@NonNull Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    @NonNull
    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(@NonNull Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    @NonNull
    public List<ParcelaReservada> getParcelasReservadas() {
        return parcelasReservadas;
    }

}
