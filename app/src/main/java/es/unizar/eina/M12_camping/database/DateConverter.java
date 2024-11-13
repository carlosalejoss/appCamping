package es.unizar.eina.M12_camping.database;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Conversor de tipos para Room, que permite almacenar objetos Date como Long en la base de datos.
 */
public class DateConverter {

    /**
     * Convierte un valor Long de la base de datos a un objeto Date.
     *
     * @param timestamp El valor Long que representa una fecha.
     * @return El objeto Date correspondiente, o null si el valor es null.
     */
    @TypeConverter
    public static Date fromTimestamp(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    /**
     * Convierte un objeto Date a un valor Long para almacenar en la base de datos.
     *
     * @param date El objeto Date a convertir.
     * @return El valor Long correspondiente, o null si la fecha es null.
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}