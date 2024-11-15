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
     * Calcula el precio total antes de la inserción.
     *
     * @param reserva La reserva a insertar.
     * @param parcelasReservadas Lista de parcelas reservadas asociadas a la reserva.
     * @return El ID de la reserva insertada, o -1 si falla.
     */
    public long insert(Reserva reserva, List<ParcelaReservada> parcelasReservadas) {
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> {
            // Insertar la reserva sin el precio total
            long reservaId = mReservaDao.insert(reserva);
            if (reservaId <= 0) {
                return -1L;
            }

            // Asignar el ID de la reserva a cada ParcelaReservada y guardarlas
            for (ParcelaReservada pr : parcelasReservadas) {
                pr.setReservaId((int) reservaId);
                mParcelaReservadaDao.insert(pr);
            }

            // Actualizar la reserva con el precio total
            reserva.setId((int) reservaId);
            double precioTotal = calcularPrecioTotal((int) reservaId, reserva.getFechaEntrada(), reserva.getFechaSalida());
            reserva.setPrecioTotal(precioTotal);
            mReservaDao.update(reserva);

            return reservaId;
        });

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return -1;
        }
    }

    /**
     * Actualiza una reserva existente en la base de datos.
     * Recalcula el precio total si se modifican las parcelas o fechas.
     *
     * @param reserva La reserva a actualizar.
     * @param parcelasReservadas Lista actualizada de parcelas reservadas asociadas a la reserva.
     * @return El número de filas afectadas.
     */
    public int update(Reserva reserva, List<ParcelaReservada> parcelasReservadas) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> {
            // Obtener la reserva original
            Reserva reservaOriginal = mReservaDao.getReservaById(reserva.getId());
            if (reservaOriginal == null) {
                Log.d("ReservaRepository", "Reserva no encontrada con ID: " + reserva.getId());
                return 0;
            }

            // Obtener las parcelas reservadas originales
            List<ParcelaReservada> parcelasOriginales = mParcelaReservadaDao.getParcelasReservadasByReservaId(reserva.getId());

            // Verificar si se han modificado las parcelas o fechas
            boolean parcelasCambiadas = !compararListasParcelas(parcelasOriginales, parcelasReservadas);
            boolean fechasCambiadas = !reservaOriginal.getFechaEntrada().equals(reserva.getFechaEntrada()) ||
                    !reservaOriginal.getFechaSalida().equals(reserva.getFechaSalida());

            if (parcelasCambiadas) {
                // Eliminar las parcelas reservadas originales
                for (ParcelaReservada pr : parcelasOriginales) {
                    mParcelaReservadaDao.delete(pr);
                }
                // Insertar las nuevas parcelas reservadas
                for (ParcelaReservada pr : parcelasReservadas) {
                    pr.setReservaId(reserva.getId());
                    mParcelaReservadaDao.insert(pr);
                }
            }

            if (parcelasCambiadas || fechasCambiadas) {
                // Recalcular el precio total
                double precioTotal = calcularPrecioTotal(reserva.getId(), reserva.getFechaEntrada(), reserva.getFechaSalida());
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
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ": " + ex.getMessage());
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
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(() -> {
            // Eliminar las parcelas reservadas asociadas
            List<ParcelaReservada> parcelasReservadas = mParcelaReservadaDao.getParcelasReservadasByReservaId(reserva.getId());
            for (ParcelaReservada pr : parcelasReservadas) {
                mParcelaReservadaDao.delete(pr);
            }
            // Eliminar la reserva
            return mReservaDao.delete(reserva);
        });

        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ReservaRepository", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return -1;
        }
    }

    // Métodos auxiliares

    /**
     * Calcula el precio total de una reserva.
     *
     * @param reservaId El ID de la reserva.
     * @param fechaEntrada Fecha de entrada de la reserva.
     * @param fechaSalida Fecha de salida de la reserva.
     * @return El precio total calculado.
     */
    private double calcularPrecioTotal(int reservaId, Date fechaEntrada, Date fechaSalida) {
        long diffInMillies = Math.abs(fechaSalida.getTime() - fechaEntrada.getTime());
        long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diffDays == 0) {
            diffDays = 1; // Al menos una noche
        }

        double total = 0;

        // Obtener las parcelas reservadas asociadas a la reserva
        List<ParcelaReservada> parcelasReservadas = mParcelaReservadaDao.getParcelasReservadasByReservaId(reservaId);

        for (ParcelaReservada pr : parcelasReservadas) {
            // Obtener detalles de la parcela
            Parcela parcela = obtenerParcelaPorId(pr.getParcelaId());
            if (parcela != null) {
                total += parcela.getPrecioXpersona() * pr.getNumeroOcupantes() * diffDays;
            } else {
                Log.d("ReservaRepository", "Parcela no encontrada con ID: " + pr.getParcelaId());
            }
        }
        return total;
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
     * Compara dos listas de ParcelaReservada para determinar si son iguales.
     *
     * @param lista1 Primera lista de ParcelaReservada.
     * @param lista2 Segunda lista de ParcelaReservada.
     * @return true si las listas son iguales, false en caso contrario.
     */
    private boolean compararListasParcelas(List<ParcelaReservada> lista1, List<ParcelaReservada> lista2) {
        if (lista1.size() != lista2.size()) {
            return false;
        }

        for (ParcelaReservada pr1 : lista1) {
            boolean found = false;
            for (ParcelaReservada pr2 : lista2) {
                if (pr1.getParcelaId() == pr2.getParcelaId() &&
                        pr1.getNumeroOcupantes() == pr2.getNumeroOcupantes()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    // Método para verificar solapamientos

    /**
     * Verifica si hay solapamientos de reservas para las parcelas y fechas dadas.
     *
     * @param reserva La reserva que se está creando o actualizando.
     * @param parcelasReservadas Lista de parcelas reservadas.
     * @return true si hay solapamientos, false en caso contrario.
     */
    public boolean haySolapamientos(Reserva reserva, List<ParcelaReservada> parcelasReservadas) {
        List<Integer> idParcelas = parcelasReservadas.stream()
                .map(ParcelaReservada::getParcelaId)
                .collect(Collectors.toList());

        List<Reserva> reservasSolapadas = mReservaDao.getReservasSolapadas(
                reserva.getFechaEntrada(),
                reserva.getFechaSalida(),
                idParcelas,
                reserva.getId()
        );

        return !reservasSolapadas.isEmpty();
    }

    // Otros métodos y validaciones adicionales...

}
