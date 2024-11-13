package es.unizar.eina.M12_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Definición de un Data Access Object (DAO) para las operaciones CRUD de las reservas.
 * Proporciona métodos para insertar, actualizar, eliminar y consultar reservas en la base de datos.
 */
@Dao
public interface ReservaDao {

    /**
     * Inserta una nueva reserva en la base de datos.
     * Si ya existe una reserva con el mismo identificador, ignora el conflicto.
     *
     * @param reserva La reserva a insertar.
     * @return El identificador de la reserva insertada.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Reserva reserva);

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva La reserva con los datos actualizados.
     * @return El número de filas afectadas (debería ser 1 si la actualización es exitosa).
     */
    @Update
    int update(Reserva reserva);

    /**
     * Elimina una reserva específica de la base de datos.
     *
     * @param reserva La reserva a eliminar.
     * @return El número de filas afectadas (debería ser 1 si la eliminación es exitosa).
     */
    @Delete
    int delete(Reserva reserva);

    /**
     * Elimina todas las reservas de la base de datos.
     */
    @Query("DELETE FROM Reserva")
    void deleteAll();

    /**
     * Obtiene todas las reservas sin ningún orden específico.
     *
     * @return Un objeto LiveData que contiene una lista de todas las reservas.
     */
    @Query("SELECT * FROM Reserva")
    LiveData<List<Reserva>> getUnOrderedReservas();

    /**
     * Obtiene todas las reservas ordenadas alfabéticamente por nombreCliente.
     *
     * @return Un objeto LiveData que contiene una lista de reservas ordenadas
     * por el nombre de los clientes en orden ascendente.
     */
    @Query("SELECT * FROM Reserva ORDER BY nombreCliente ASC")
    LiveData<List<Reserva>> getOrderedReservasNombreCliente();

    /**
     * Obtiene todas las reservas ordenadas por el número máximo de ocupantes.
     *
     * @return Un objeto LiveData que contiene una lista de reservas ordenadas
     * por el número de movil de los clientes en orden ascendente.
     */
    @Query("SELECT * FROM Reserva ORDER BY telefono ASC")
    LiveData<List<Reserva>> getOrderedReservasTelefono();

    /**
     * Obtiene todas las reservas ordenadas por la fecha de entrada.
     *
     * @return Un objeto LiveData que contiene una lista de reservas ordenadas
     * por la fecha de entrada en orden ascendente.
     */
    @Query("SELECT * FROM Reserva ORDER BY fechaEntrada ASC")
    LiveData<List<Reserva>> getOrderedReservasFechaEntrada();

}