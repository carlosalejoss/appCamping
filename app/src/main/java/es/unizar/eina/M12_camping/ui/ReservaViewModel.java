package es.unizar.eina.M12_camping.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.M12_camping.database.Parcela;
import es.unizar.eina.M12_camping.database.ParcelaRepository;
import es.unizar.eina.M12_camping.database.ParcelaReservada;
import es.unizar.eina.M12_camping.database.Reserva;
import es.unizar.eina.M12_camping.database.ReservaRepository;

/**
 * ViewModel que proporciona la lógica de acceso a los datos de reservas.
 * Facilita la comunicación entre la UI y el repositorio de datos.
 */
public class ReservaViewModel extends AndroidViewModel {

    private final ReservaRepository mRepository;
    private final ParcelaRepository mParcelaRepository;

    private final LiveData<List<Reserva>> mAllReservas;
    private final LiveData<List<Reserva>> mReservasOrdNombreCliente;
    private final LiveData<List<Reserva>> mReservasOrdTelefono;
    private final LiveData<List<Reserva>> mReservasOrdFechaEntrada;

    /**
     * Constructor del ViewModel de reservas.
     * Inicializa el repositorio y obtiene las listas de reservas.
     *
     * @param application La aplicación actual, que proporciona el contexto para inicializar el repositorio.
     */
    public ReservaViewModel(Application application) {
        super(application);
        mRepository = new ReservaRepository(application);
        mParcelaRepository = new ParcelaRepository(application);
        mAllReservas = mRepository.getAllReservas();
        mReservasOrdNombreCliente = mRepository.getReservasOrderedNombreCliente();
        mReservasOrdTelefono = mRepository.getReservasOrderedTelefono();
        mReservasOrdFechaEntrada = mRepository.getReservasOrderedFechaEntrada();
    }

    /**
     * Obtiene todas las reservas sin un orden específico.
     *
     * @return Un objeto LiveData que contiene la lista de todas las reservas.
     */
    public LiveData<List<Reserva>> getAllReservas() {
        return mAllReservas;
    }

    /**
     * Obtiene todas las reservas ordenadas por el nombre del cliente.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas alfabéticamente por el nombre del cliente.
     */
    public LiveData<List<Reserva>> getReservasOrderedNombreCliente() {
        return mReservasOrdNombreCliente;
    }

    /**
     * Obtiene todas las reservas ordenadas por el número de teléfono del cliente.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas por el número de teléfono en orden ascendente.
     */
    public LiveData<List<Reserva>> getReservasOrderedTelefono() {
        return mReservasOrdTelefono;
    }

    /**
     * Obtiene todas las reservas ordenadas por la fecha de entrada.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas por la fecha de entrada en orden ascendente.
     */
    public LiveData<List<Reserva>> getReservasOrderedFechaEntrada() {
        return mReservasOrdFechaEntrada;
    }

    /**
     * Inserta una nueva reserva en la base de datos.
     *
     * @param reserva La reserva a insertar.
     * @return El ID de la reserva recién insertada.
     */
    public long insert(Reserva reserva) {
        return mRepository.insert(reserva);
    }


    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva La reserva que se debe actualizar.
     */
    public void update(Reserva reserva) {
        mRepository.update(reserva);
    }

    /**
     * Elimina una reserva de la base de datos.
     *
     * @param reserva La reserva a eliminar.
     */
    public void delete(Reserva reserva) {
        mRepository.delete(reserva);
    }

    /**
     * Obtiene todas las parcelas disponibles de forma síncrona.
     *
     * @return Una lista de todas las parcelas.
     */
    public List<Parcela> getAllParcelasSync() {
        return mRepository.getAllParcelasSync();
    }

    /**
     * Obtiene una parcela por su ID.
     *
     * @param parcelaId El ID de la parcela.
     * @return La parcela correspondiente.
     */
    public Parcela getParcelaById(int parcelaId) {
        return mRepository.getParcelaById(parcelaId);
    }

    /**
     * Obtiene una reserva específica por su ID.
     *
     * @param id El ID de la reserva.
     * @return La reserva correspondiente, o null si no se encuentra.
     */
    public Reserva getReservaById(int id) {
        return mRepository.getReservaById(id);
    }

    /**
     * Obtiene las parcelas reservadas asociadas a una reserva específica.
     *
     * @param reservaId El ID de la reserva.
     * @return Lista de ParcelasReservadas asociadas a la reserva.
     */
    public List<ParcelaReservada> getParcelasReservadasByReservaId(int reservaId) {
        return mRepository.getParcelasReservadasByReservaId(reservaId);
    }

    /**
     * Inserta una parcela reservada en la base de datos.
     *
     * @param parcelaReservada La parcela reservada que se desea insertar.
     */
    public void insertParcelaReservada(ParcelaReservada parcelaReservada) {
        mRepository.insertParcelaReservada(parcelaReservada);
    }

    public LiveData<List<Parcela>> getAllParcelas() {
        return mParcelaRepository.getAllParcelas();
    }

    public String getNombreParcelaById(int parcelaId) {
        Log.d("ReservaViewModel", "getNombreParcelaById: parcelaId = " + parcelaId);
        String nombreParcela = mParcelaRepository.getNombreParcelaById(parcelaId);
        if (nombreParcela == null) {
            Log.d("ReservaViewModel", "getNombreParcelaById: No se encontró nombre para parcelaId = " + parcelaId);
        } else {
            Log.d("ReservaViewModel", "getNombreParcelaById: Nombre de parcela = " + nombreParcela);
        }
        return nombreParcela;
    }

    public void deleteParcelaReservada(ParcelaReservada parcelaReservada) {
        mRepository.deleteParcelaReservada(parcelaReservada);
    }
}