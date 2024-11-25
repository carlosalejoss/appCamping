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

}
