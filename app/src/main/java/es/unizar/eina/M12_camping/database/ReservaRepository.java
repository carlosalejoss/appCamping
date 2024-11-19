package es.unizar.eina.M12_camping.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
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
     *
     * @param reserva La reserva a insertar.
     * @return El ID de la reserva recién insertada.
     */
    public long insert(Reserva reserva) {
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> mReservaDao.insert(reserva));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error al insertar la reserva: " + e.getMessage());
            return -1; // Devuelve -1 en caso de error.
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

    /**
     * Obtiene todas las parcelas de manera síncrona.
     *
     * @return Una lista de todas las parcelas.
     */
    public List<Parcela> getAllParcelasSync() {
        try {
            Future<List<Parcela>> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                    () -> mParcelaDao.getUnOrderedParcelas().getValue());
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error obteniendo parcelas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene una parcela por su ID.
     *
     * @param parcelaId El ID de la parcela.
     * @return La parcela correspondiente, o null si no se encuentra.
     */
    public Parcela getParcelaById(int parcelaId) {
        Future<Parcela> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.getParcelaById(parcelaId)
        );
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error obteniendo parcela por ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene una reserva específica por su ID.
     *
     * @param id El ID de la reserva.
     * @return La reserva correspondiente, o null si no se encuentra.
     */
    public Reserva getReservaById(int id) {
        Future<Reserva> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> mReservaDao.getReservaById(id));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error al obtener la reserva por ID: " + e.getMessage());
            return null; // Devuelve null en caso de error.
        }
    }

    /**
     * Obtiene las parcelas reservadas asociadas a una reserva específica.
     *
     * @param reservaId El ID de la reserva.
     * @return Lista de ParcelasReservadas asociadas a la reserva.
     */
    public List<ParcelaReservada> getParcelasReservadasByReservaId(int reservaId) {
        Future<List<ParcelaReservada>> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> mParcelaReservadaDao.getParcelasReservadasByReservaId(reservaId));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error al obtener parcelas reservadas por reserva ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inserta una nueva parcela reservada en la base de datos.
     *
     * @param parcelaReservada La parcela reservada que se desea insertar.
     */
    public void insertParcelaReservada(ParcelaReservada parcelaReservada) {
        CampingRoomDatabase.databaseWriteExecutor.execute(() -> {
            mParcelaReservadaDao.insert(parcelaReservada);
        });
    }

}