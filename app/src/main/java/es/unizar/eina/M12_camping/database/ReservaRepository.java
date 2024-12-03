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
 * Proporciona metodos para realizar operaciones CRUD, validaciones y calculos relacionados con las reservas.
 */
public class ReservaRepository {

    private final ReservaDao mReservaDao;
    private final ParcelaDao mParcelaDao;
    private final ParcelaReservadaDao mParcelaReservadaDao;

    private final LiveData<List<Reserva>> mAllReservas;
    private final LiveData<List<Reserva>> mReservasOrdNombreCliente;
    private final LiveData<List<Reserva>> mReservasOrdTelefono;
    private final LiveData<List<Reserva>> mReservasOrdFechaEntrada;

    /** Tiempo maximo de espera para operaciones de base de datos en milisegundos */
    private final long TIMEOUT = 15000;

    /**
     * Constructor del repositorio de reservas.
     *
     * @param application La aplicacion que proporciona el contexto para la base de datos.
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

    /**
     * Obtiene todas las reservas sin un orden especifico.
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
     * Obtiene todas las reservas ordenadas por numero de telefono.
     *
     * @return LiveData con la lista de reservas ordenadas por telefono.
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

    /**
     * Inserta una nueva reserva en la base de datos.
     *
     * @param reserva La reserva a insertar.
     * @return El ID de la reserva recien insertada.
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
     * La operacion se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param reserva La reserva que se desea actualizar. Debe tener un ID mayor que 0, y todos sus
     *                parametros no nulos y no vacios.
     * @return El numero de filas modificadas (1 si se actualiza correctamente,
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
     * @return El numero de filas afectadas.
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
     * Obtiene una reserva especifica por su ID.
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
     * Obtiene las parcelas reservadas asociadas a una reserva especifica.
     *
     * @param reservaId El ID de la reserva.
     * @return Lista de ParcelasReservadas asociadas a la reserva.
     */
    public LiveData<List<ParcelaReservada>> getParcelasReservadasByReservaId(int reservaId) {
        return mParcelaReservadaDao.getParcelasReservadasByReservaId(reservaId);
    }

    /**
     * Inserta una nueva parcela reservada en la base de datos.
     *
     * @param parcelaReservada La parcela reservada que se desea insertar.
     * @return El ID de la parcela reservada recien insertada.
     */
    public long insertParcelaReservada(ParcelaReservada parcelaReservada) {
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> mParcelaReservadaDao.insert(parcelaReservada));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error al insertar la parcela reservada: " + e.getMessage());
            return -1; // Retorna -1 en caso de error.
        }
    }

    /**
     * Actualiza una parcela reservada existente en la base de datos.
     *
     * @param parcelaReservada La parcela reservada con los datos actualizados.
     * @return El numero de filas afectadas por la actualizacion.
     */
    public int updateParcelaReservada(ParcelaReservada parcelaReservada) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaReservadaDao.update(parcelaReservada)
        );
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error al actualizar la parcela reservada: " + e.getMessage());
            return -1; // Retorna -1 en caso de error.
        }
    }

    /**
     * Elimina una parcela reservada de la base de datos.
     *
     * @param parcelaReservada La parcela reservada que se desea eliminar.
     * @return El numero de filas afectadas por la eliminacion.
     */
    public int deleteParcelaReservada(ParcelaReservada parcelaReservada) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaReservadaDao.delete(parcelaReservada)
        );
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error al eliminar la parcela reservada: " + e.getMessage());
            return -1; // Retorna -1 en caso de error.
        }
    }

    /**
     * Obtiene una lista de parcelas que no estan reservadas en el rango de fechas especificado.
     *
     * @param fechaInicio La fecha de inicio del rango.
     * @param fechaFin La fecha de fin del rango.
     * @return Lista de parcelas disponibles.
     */
    public List<Parcela> getParcelasDisponibles(Date fechaInicio, Date fechaFin) {
        Future<List<Parcela>> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> mParcelaDao.getParcelasDisponibles(fechaInicio, fechaFin));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Log.e("ReservaRepository", "Error al obtener parcelas disponibles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}