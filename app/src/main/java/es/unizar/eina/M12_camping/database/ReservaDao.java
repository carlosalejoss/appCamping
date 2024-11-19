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
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) y
 * consultas específicas relacionadas con las reservas en la base de datos.
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
     * @return El número de filas afectadas (1 si la actualización fue exitosa, 0 en caso contrario).
     */
    @Update
    int update(Reserva reserva);

    /**
     * Elimina una reserva específica de la base de datos.
     *
     * @param reserva La reserva a eliminar.
     * @return El número de filas afectadas (1 si la eliminación fue exitosa, 0 en caso contrario).
     */
    @Delete
    int delete(Reserva reserva);

    /**
     * Elimina todas las reservas de la base de datos.
     */
    @Query("DELETE FROM reserva")
    void deleteAll();

    /**
     * Obtiene todas las reservas sin un orden específico.
     *
     * @return Una lista LiveData de todas las reservas.
     */
    @Query("SELECT * FROM reserva")
    LiveData<List<Reserva>> getUnOrderedReservas();

    /**
     * Obtiene todas las reservas ordenadas por nombre del cliente en orden ascendente.
     *
     * @return Una lista LiveData de reservas ordenadas por nombre del cliente.
     */
    @Query("SELECT * FROM reserva ORDER BY nombreCliente ASC")
    LiveData<List<Reserva>> getOrderedReservasNombreCliente();

    /**
     * Obtiene todas las reservas ordenadas por número de móvil en orden ascendente.
     *
     * @return Una lista LiveData de reservas ordenadas por número de móvil.
     */
    @Query("SELECT * FROM reserva ORDER BY numeroMovil ASC")
    LiveData<List<Reserva>> getOrderedReservasTelefono();

    /**
     * Obtiene todas las reservas ordenadas por fecha de entrada en orden ascendente.
     *
     * @return Una lista LiveData de reservas ordenadas por fecha de entrada.
     */
    @Query("SELECT * FROM reserva ORDER BY fechaEntrada ASC")
    LiveData<List<Reserva>> getOrderedReservasFechaEntrada();

    /**
     * Obtiene una reserva específica por su ID.
     *
     * @param id El ID de la reserva.
     * @return La reserva correspondiente, o null si no se encuentra.
     */
    @Query("SELECT * FROM reserva WHERE id = :id")
    Reserva getReservaById(int id);

    /**
     * Obtiene las parcelas reservadas de una reserva específica.
     *
     * @param reservaId El ID de la reserva.
     * @return Lista de ParcelasReservadas asociadas.
     */
    @Query("SELECT * FROM parcelaReservada WHERE reservaId = :reservaId")
    List<ParcelaReservada> getParcelasReservadasByReservaId(int reservaId);


    /**
     * Obtiene una lista de reservas que se solapan con las fechas y parcelas especificadas.
     *
     * @param fechaInicio Fecha de inicio para comprobar solapamientos.
     * @param fechaFin Fecha de fin para comprobar solapamientos.
     * @param idParcelas Lista de IDs de parcelas a comprobar.
     * @param reservaId ID de la reserva que se está creando o modificando para excluirla de la verificación.
     * @return Lista de reservas que tienen conflictos en las fechas y parcelas especificadas.
     */
    @Query("SELECT DISTINCT r.* FROM reserva r " +
            "INNER JOIN parcelaReservada pr ON r.id = pr.reservaId " +
            "WHERE r.id != :reservaId AND " +
            "r.fechaEntrada < :fechaFin AND r.fechaSalida > :fechaInicio AND " +
            "pr.parcelaId IN (:idParcelas)")
    List<Reserva> getReservasSolapadas(Date fechaInicio, Date fechaFin, List<Integer> idParcelas, int reservaId);

}
