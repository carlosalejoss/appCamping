package es.unizar.eina.M12_camping.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Repositorio que gestiona el acceso a los datos de las reservas.
 * Proporciona métodos para realizar operaciones CRUD, validaciones y cálculos relacionados con las reservas.
 */
public class ReservaRepository {

    private final ReservaDao mReservaDao;
    private final ParcelaDao mParcelaDao;
    private final ParcelaReservadaDao mParcelaReservadaDao;

    private final LiveData<List<Reserva>> mAllReservas;
    private final LiveData<List<Reserva>> mReservasOrdNombreCliente;
    private final LiveData<List<Reserva>> mReservasOrdTelefono;
    private final LiveData<List<Reserva>> mReservasOrdFechaEntrada;

    /** Tiempo máximo de espera para operaciones de base de datos en milisegundos */
    private final long TIMEOUT = 15000;

    /**
     * Constructor del repositorio de reservas.
     *
     * @param application La aplicación que proporciona el contexto para la base de datos.
     */
    public ReservaRepository(Application application) {
        CampingRoomDatabase db = CampingRoomDatabase.getDatabase(application);
        mReservaDao = db.reservaDao();
        mParcelaDao = db.parcelaDao();
        mParcelaReservadaDao = db.parcelaReservadaDao();

        mAllReservas = mReservaDao.getUnOrderedReservas();
        mReservasOrdNombreCliente = mReservaDao.getOrderedReservasNombreCliente();
        mReservasOrdTelefono = mReservaDao.getOrderedReservasTelefono();
        mReservasOrdFechaEntrada = mReservaDao.getOrderedReservasFechaEntrada();
    }

    // Métodos para obtener listas de reservas

    /**
     * Obtiene todas las reservas sin un orden específico.
     *
     * @return LiveData con la lista de todas las reservas.
     */
    public LiveData<List<Reserva>> getAllReservas() {
        return mAllReservas;
    }

    /**
     * Obtiene todas las reservas ordenadas por nombre del cliente.
     *
     * @return LiveData con la lista de reservas ordenadas por nombre.
     */
    public LiveData<List<Reserva>> getReservasOrderedNombreCliente() {
        return mReservasOrdNombreCliente;
    }

    /**
     * Obtiene todas las reservas ordenadas por número de teléfono.
     *
     * @return LiveData con la lista de reservas ordenadas por teléfono.
     */
    public LiveData<List<Reserva>> getReservasOrderedTelefono() {
        return mReservasOrdTelefono;
    }

    /**
     * Obtiene todas las reservas ordenadas por fecha de entrada.
     *
     * @return LiveData con la lista de reservas ordenadas por fecha de entrada.
     */
    public LiveData<List<Reserva>> getReservasOrderedFechaEntrada() {
        return mReservasOrdFechaEntrada;
    }

    // Métodos CRUD

    /**
     * Inserta una nueva reserva en la base de datos.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param reserva La reserva a insertar. Debe tener un todos sus parametros no nulos.
     * @return El identificador de la reserva insertada, o -1 si la inserción falla.
     */
    public long insert(Reserva reserva) {
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mReservaDao.insert(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Actualiza una reserva en la base de datos.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param reserva La reserva que se desea actualizar. Debe tener un ID mayor que 0, y todos sus
     *                parametros no nulos y no vacios.
     * @return El número de filas modificadas (1 si se actualiza correctamente,
     * 0 si no existe una reserva con ese ID).
     */
    public int update(Reserva reserva) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mReservaDao.update(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Elimina una reserva de la base de datos.
     *
     * @param reserva La reserva a eliminar.
     * @return El número de filas afectadas.
     */
    public int delete(Reserva reserva) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mReservaDao.delete(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Obtiene una parcela por su ID.
     *
     * @param idParcela El ID de la parcela.
     * @return La parcela correspondiente, o null si no se encuentra.
     */
    private Parcela obtenerParcelaPorId(int idParcela) {
        Future<Parcela> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.getParcelaById(idParcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return null;
        }
    }

}