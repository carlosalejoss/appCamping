package es.unizar.eina.M12_camping.database;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Convertidor de tipo para almacenar y recuperar listas de ParcelaReservada en la base de datos.
 */
public class ParcelaReservadaConverter {

    /**
     * Convierte una cadena JSON a una lista de ParcelaReservada.
     *
     * @param value La cadena JSON a convertir.
     * @return La lista de ParcelaReservada obtenida.
     */
    @TypeConverter
    public static List<ParcelaReservada> fromString(String value) {
        Type listType = new TypeToken<List<ParcelaReservada>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    /**
     * Convierte una lista de ParcelaReservada a una cadena JSON.
     *
     * @param list La lista de ParcelaReservada a convertir.
     * @return La cadena JSON resultante.
     */
    @TypeConverter
    public static String fromList(List<ParcelaReservada> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}