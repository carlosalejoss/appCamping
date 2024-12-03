package es.unizar.eina.M12_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

/**
 * DAO para acceder a los datos de la entidad ParcelaReservada.
 * Proporciona metodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la tabla de ParcelasReservadas en la base de datos.
 */
@Dao
public interface ParcelaReservadaDao {

    /**
     * Inserta una nueva ParcelaReservada en la base de datos.
     *
     * @param parcelaReservada La ParcelaReservada a insertar.
     * @return El ID de la ParcelaReservada insertada.
     */
    @Insert
    long insert(ParcelaReservada parcelaReservada);

    /**
     * Actualiza una ParcelaReservada existente en la base de datos.
     *
     * @param parcelaReservada La reserva a actualizar.
     * @return El numero de filas afectadas.
     */
    @Update
    int update(ParcelaReservada parcelaReservada);

    /**
     * Elimina una ParcelaReservada.
     *
     * @param parcelaReservada La ParcelaReservada a eliminar.
     * @return El numero de filas afectadas.
     */
    @Delete
    int delete(ParcelaReservada parcelaReservada);

    /**
     * Elimina todas las reservas de la base de datos.
     */
    @Query("DELETE FROM parcelaReservada")
    void deleteAll();

    /**
     * Obtiene las parcelas reservadas de una reserva especifica.
     *
     * @param reservaId El ID de la reserva.
     * @return Lista de ParcelasReservadas asociadas.
     */
    @Query("SELECT * FROM parcelaReservada WHERE reservaId = :reservaId")
    LiveData<List<ParcelaReservada>> getParcelasReservadasByReservaId(int reservaId);

}
