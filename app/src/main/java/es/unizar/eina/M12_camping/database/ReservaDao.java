package es.unizar.eina.M12_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.Date;
import java.util.List;

/**
 * Interfaz DAO para acceder a los datos de las reservas.
 */
@Dao
public interface ReservaDao {

    /**
     * Inserta una nueva reserva en la base de datos.
     *
     * @param reserva La reserva a insertar.
     * @return El ID de la reserva insertada.
     */
    @Insert
    long insert(Reserva reserva);

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva La reserva a actualizar.
     * @return El número de filas afectadas.
     */
    @Update
    int update(Reserva reserva);

    /**
     * Elimina una reserva de la base de datos.
     *
     * @param reserva La reserva a eliminar.
     * @return El número de filas afectadas.
     */
    @Delete
    int delete(Reserva reserva);

    /**
     * Elimina todas las reservas de la base de datos.
     */
    @Query("DELETE FROM Reserva")
    void deleteAll();

    /**
     * Obtiene todas las reservas sin un orden específico.
     *
     * @return Una lista LiveData de todas las reservas.
     */
    @Query("SELECT * FROM reserva")
    LiveData<List<Reserva>> getUnOrderedReservas();

    /**
     * Obtiene todas las reservas ordenadas por nombre del cliente.
     *
     * @return Una lista LiveData de reservas ordenadas por nombre.
     */
    @Query("SELECT * FROM reserva ORDER BY nombreCliente ASC")
    LiveData<List<Reserva>> getOrderedReservasNombreCliente();

    /**
     * Obtiene todas las reservas ordenadas por número de móvil.
     *
     * @return Una lista LiveData de reservas ordenadas por número de móvil.
     */
    @Query("SELECT * FROM reserva ORDER BY numeroMovil ASC")
    LiveData<List<Reserva>> getOrderedReservasTelefono();

    /**
     * Obtiene todas las reservas ordenadas por fecha de entrada.
     *
     * @return Una lista LiveData de reservas ordenadas por fecha de entrada.
     */
    @Query("SELECT * FROM reserva ORDER BY fechaEntrada ASC")
    LiveData<List<Reserva>> getOrderedReservasFechaEntrada();

    /**
     * Obtiene una reserva por su ID.
     *
     * @param id El ID de la reserva.
     * @return La reserva correspondiente.
     */
    @Query("SELECT * FROM reserva WHERE id = :id")
    Reserva getReservaById(int id);

    // Métodos adicionales para validaciones y consultas específicas...

    @Query("SELECT DISTINCT r.* FROM reserva r " +
            "INNER JOIN parcelaReservada pr ON r.id = pr.reservaId " +
            "WHERE r.id != :reservaId AND " +
            "r.fechaEntrada < :fechaFin AND r.fechaSalida > :fechaInicio AND " +
            "pr.parcelaId IN (:idParcelas)")
    List<Reserva> getReservasSolapadas(Date fechaInicio, Date fechaFin, List<Integer> idParcelas, int reservaId);

}
