package es.unizar.eina.M12_camping.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

/**
 * DAO para acceder a los datos de ParcelaReservada.
 */
@Dao
public interface ParcelaReservadaDao {

    /**
     * Inserta una nueva ParcelaReservada.
     *
     * @param parcelaReservada La ParcelaReservada a insertar.
     * @return El ID de la ParcelaReservada insertada.
     */
    @Insert
    long insert(ParcelaReservada parcelaReservada);

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param parcelaReservada La reserva a actualizar.
     * @return El número de filas afectadas.
     */
    @Update
    int update(ParcelaReservada parcelaReservada);

    /**
     * Elimina una ParcelaReservada.
     *
     * @param parcelaReservada La ParcelaReservada a eliminar.
     * @return El número de filas afectadas.
     */
    @Delete
    int delete(ParcelaReservada parcelaReservada);

    /**
     * Elimina todas las reservas de la base de datos.
     */
    @Query("DELETE FROM Reserva")
    void deleteAll();

    /**
     * Obtiene las parcelas reservadas de una reserva específica.
     *
     * @param reservaId El ID de la reserva.
     * @return Lista de ParcelasReservadas asociadas.
     */
    @Query("SELECT * FROM parcelaReservada WHERE reservaId = :reservaId")
    List<ParcelaReservada> getParcelasReservadasByReservaId(int reservaId);

    /**
     * Obtiene todas las ParcelasReservadas.
     *
     * @return Lista de todas las ParcelasReservadas.
     */
    @Query("SELECT * FROM parcelaReservada")
    List<ParcelaReservada> getAllParcelasReservadas();
}
