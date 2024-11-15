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

/**
 * Clase que gestiona el acceso a la fuente de datos para las reservas.
 * Interactúa con la base de datos a través de las clases {@link CampingRoomDatabase}, {@link ReservaDao} y {@link ParcelaDao}.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en las reservas.
 */
public class ReservaRepository {

    private final ReservaDao mReservaDao;
    private final ParcelaDao mParcelaDao;
    private final LiveData<List<Reserva>> mAllReservas;
    private final LiveData<List<Reserva>> mReservasOrdNombreCliente;
    private final LiveData<List<Reserva>> mReservasOrdTelefono;
    private final LiveData<List<Reserva>> mReservasOrdFechaEntrada;

    /** Tiempo máximo de espera para operaciones de base de datos en milisegundos */
    private final long TIMEOUT = 15000;

    /**
     * Constructor de ReservaRepository.
     * Utiliza el contexto de la aplicación para obtener la instancia de la base de datos.
     *
     * @param application La aplicación que proporciona el contexto para instanciar la base de datos.
     */
    public ReservaRepository(Application application) {
        CampingRoomDatabase db = CampingRoomDatabase.getDatabase(application);
        mReservaDao = db.reservaDao();
        mParcelaDao = db.parcelaDao(); // Añadido para acceder a los detalles de las parcelas
        mAllReservas = mReservaDao.getUnOrderedReservas();
        mReservasOrdNombreCliente = mReservaDao.getOrderedReservasNombreCliente();
        mReservasOrdTelefono = mReservaDao.getOrderedReservasTelefono();
        mReservasOrdFechaEntrada = mReservaDao.getOrderedReservasFechaEntrada();
    }

    /**
     * Obtiene todas las reservas en la base de datos sin un orden específico.
     * Room ejecuta todas las consultas en un hilo separado,
     * y LiveData notifica a los observadores cuando los datos cambian.
     * @return Un objeto LiveData con la lista de todas las reservas.
     */
    public LiveData<List<Reserva>> getAllReservas() {
        return mAllReservas;
    }

    /**
     * Obtiene todas las reservas ordenadas alfabéticamente por nombre.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas por nombre en orden ascendente.
     */
    public LiveData<List<Reserva>> getReservasOrderedNombreCliente() {
        return mReservasOrdNombreCliente;
    }

    /**
     * Obtiene todas las reservas ordenadas por el número de teléfono de los clientes.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas
     * por número de teléfono de los clientes en orden ascendente.
     */
    public LiveData<List<Reserva>> getReservasOrderedTelefono() {
        return mReservasOrdTelefono;
    }

    /**
     * Obtiene todas las reservas ordenadas por fecha de entrada.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas
     * por la fecha de entrada en orden ascendente.
     */
    public LiveData<List<Reserva>> getReservasOrderedFechaEntrada() {
        return mReservasOrdFechaEntrada;
    }

    /**
     * Inserta una nueva reserva en la base de datos.
     * Calcula el precio total antes de la inserción.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param reserva La reserva a insertar.
     * @return El identificador de la reserva insertada, o -1 si la inserción falla.
     */
    public long insert(Reserva reserva) {
        // Calcular el precio total
        double precioTotal = calcularPrecioTotal(reserva);
        reserva.setPrecioTotal(precioTotal);

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
     * Calcula el precio total antes de la actualización si se realizan cambios en las fechas o parcelas.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param reserva La reserva que se desea actualizar.
     * @return El número de filas modificadas (1 si se actualiza correctamente, 0 si no existe una reserva con ese ID).
     */
    public int update(Reserva reserva) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> {
            // Obtener la reserva original de la base de datos
            Reserva reservaOriginal = mReservaDao.getReservaById(reserva.getId());
            if (reservaOriginal == null) {
                Log.d("ReservaRepository", "Reserva no encontrada con ID: " + reserva.getId());
                return 0;
            }

            // Comparar las parcelas reservadas y el número de ocupantes
            boolean parcelasCambiadas = !reservaOriginal.getParcelasReservadas().equals(reserva.getParcelasReservadas());
            boolean fechasCambiadas = !reservaOriginal.getFechaEntrada().equals(reserva.getFechaEntrada()) ||
                                      !reservaOriginal.getFechaSalida().equals(reserva.getFechaSalida());

            if (parcelasCambiadas || fechasCambiadas) {
                // Recalcular el precio total
                double precioTotal = calcularPrecioTotal(reserva);
                reserva.setPrecioTotal(precioTotal);
            } else {
                // Mantener el precio total original
                reserva.setPrecioTotal(reservaOriginal.getPrecioTotal());
            }

            return mReservaDao.update(reserva);
        });

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }


    /**
     * Elimina una reserva de la base de datos.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param reserva La reserva a eliminar. Debe tener un ID mayor que 0.
     * @return El número de filas eliminadas (1 si se elimina correctamente, 0 si no existe una reserva con ese ID).
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
     * Calcula el precio total de una reserva basada en las parcelas reservadas y las fechas.
     *
     * @param reserva La reserva para la cual se calcula el precio.
     * @return El precio total de la reserva.
     */
    private double calcularPrecioTotal(Reserva reserva) {
        Date fechaEntrada = reserva.getFechaEntrada();
        Date fechaSalida = reserva.getFechaSalida();

        long diffInMillies = Math.abs(fechaSalida.getTime() - fechaEntrada.getTime());
        long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diffDays == 0) {
            diffDays = 1; // Al menos una noche
        }

        double total = 0;
        List<ParcelaReservada> parcelasReservadas = reserva.getParcelasReservadas();

        for (ParcelaReservada pr : parcelasReservadas) {
            // Obtener detalles de la parcela
            Parcela parcela = obtenerParcelaPorId(pr.getIdParcela());
            if (parcela != null) {
                total += parcela.getPrecioXpersona() * pr.getNumeroOcupantes() * diffDays;
            } else {
                Log.d("ReservaRepository", "Parcela no encontrada con ID: " + pr.getIdParcela());
            }
        }
        return total;
    }

    /**
     * Obtiene una parcela por su ID.
     *
     * @param idParcela El ID de la parcela a obtener.
     * @return La parcela correspondiente o null si no se encuentra.
     */
    private Parcela obtenerParcelaPorId(int idParcela) {
        Future<Parcela> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.getParcelaById(idParcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return null;
        }
    }

    // Otros métodos y validaciones adicionales pueden ser añadidos aquí...
}
