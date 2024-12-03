package es.unizar.eina.M12_camping.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.Date;

import es.unizar.eina.M12_camping.database.Parcela;
import es.unizar.eina.M12_camping.database.ParcelaRepository;
import es.unizar.eina.M12_camping.database.ParcelaReservada;
import es.unizar.eina.M12_camping.database.Reserva;
import es.unizar.eina.M12_camping.database.ReservaRepository;

/**
 * ViewModel que proporciona la logica de acceso a los datos de reservas.
 * Facilita la comunicacion entre la UI y el repositorio de datos.
 */
public class ReservaViewModel extends AndroidViewModel {

    private final ReservaRepository mRepository;
    private final ParcelaRepository mParcelaRepository;
    private final ExecutorService executorService;

    private final MutableLiveData<Long> insertResult = new MutableLiveData<>();

    private final LiveData<List<Reserva>> mAllReservas;
    private final LiveData<List<Reserva>> mReservasOrdNombreCliente;
    private final LiveData<List<Reserva>> mReservasOrdTelefono;
    private final LiveData<List<Reserva>> mReservasOrdFechaEntrada;

    /**
     * Constructor del ViewModel de reservas.
     * Inicializa el repositorio y obtiene las listas de reservas.
     *
     * @param application La aplicacion actual, que proporciona el contexto para inicializar el repositorio.
     */
    public ReservaViewModel(Application application) {
        super(application);
        mRepository = new ReservaRepository(application);
        mParcelaRepository = new ParcelaRepository(application);
        mAllReservas = mRepository.getAllReservas();
        mReservasOrdNombreCliente = mRepository.getReservasOrderedNombreCliente();
        mReservasOrdTelefono = mRepository.getReservasOrderedTelefono();
        mReservasOrdFechaEntrada = mRepository.getReservasOrderedFechaEntrada();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Inserta una nueva reserva en la base de datos.
     *
     * @param reserva La reserva a insertar.
     */
    public void insert(Reserva reserva) {
        executorService.execute(() -> {
            long id = mRepository.insert(reserva);
            insertResult.postValue(id); // Publicar el resultado de la insercion
        });
    }

    /**
     * Observa el resultado de la ultima operacion de insercion.
     *
     * @return LiveData con el ID de la reserva insertada.
     */
    public LiveData<Long> getInsertResult() {
        return insertResult;
    }

    /**
     * Actualiza una reserva existente en la base de datos.
     *
     * @param reserva La reserva a actualizar.
     */
    public void update(Reserva reserva) {
        executorService.execute(() -> mRepository.update(reserva));
    }

    /**
     * Elimina una reserva existente de la base de datos.
     *
     * @param reserva La reserva a eliminar.
     */
    public void delete(Reserva reserva) {
        executorService.execute(() -> mRepository.delete(reserva));
    }

    /**
     * Obtiene todas las reservas sin un orden especifico.
     *
     * @return Un objeto LiveData que contiene la lista de todas las reservas.
     */
    public LiveData<List<Reserva>> getAllReservas() {
        return mAllReservas;
    }

    /**
     * Obtiene todas las parcelas disponibles.
     *
     * @return LiveData con la lista de parcelas.
     */
    public LiveData<List<Parcela>> getAllParcelas() {
        return mParcelaRepository.getAllParcelas();
    }

    /**
     * Obtiene todas las reservas ordenadas por el nombre del cliente.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas alfabeticamente por el nombre del cliente.
     */
    public LiveData<List<Reserva>> getReservasOrderedNombreCliente() {
        return mReservasOrdNombreCliente;
    }

    /**
     * Obtiene todas las reservas ordenadas por el numero de telefono del cliente.
     *
     * @return Un objeto LiveData con la lista de reservas ordenadas por el numero de telefono en orden ascendente.
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
     * Obtiene una parcela por su ID.
     *
     * @param parcelaId El ID de la parcela.
     * @return La parcela correspondiente.
     */
    public Parcela getParcelaById(int parcelaId) {
        return mRepository.getParcelaById(parcelaId);
    }

    /**
     * Obtiene una reserva especifica por su ID.
     *
     * @param id El ID de la reserva.
     * @return La reserva correspondiente, o null si no se encuentra.
     */
    public Reserva getReservaById(int id) {
        return mRepository.getReservaById(id);
    }

    /**
     * Obtiene las parcelas reservadas asociadas a una reserva especifica.
     *
     * @param reservaId El ID de la reserva.
     * @return Lista de ParcelasReservadas asociadas a la reserva.
     */
    public LiveData<List<ParcelaReservada>> getParcelasReservadasByReservaId(int reservaId) {
        return mRepository.getParcelasReservadasByReservaId(reservaId);
    }

    /**
     * Inserta una parcela reservada en la base de datos.
     *
     * @param parcelaReservada La parcela reservada que se desea insertar.
     */
    public void insertParcelaReservada(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> mRepository.insertParcelaReservada(parcelaReservada));
    }

    /**
     * Actualiza una parcela reservada en la base de datos.
     *
     * @param parcelaReservada La parcela reservada que se desea actualizar.
     */
    public void updateParcelaReservada(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> mRepository.updateParcelaReservada(parcelaReservada));
    }

    /**
     * Elimina una parcela reservada de la base de datos.
     *
     * @param parcelaReservada La parcela reservada que se desea eliminar.
     */
    public void deleteParcelaReservada(ParcelaReservada parcelaReservada) {
        executorService.execute(() -> mRepository.deleteParcelaReservada(parcelaReservada));
    }

    /**
     * Obtiene una lista de parcelas que no estan reservadas en el rango de fechas especificado.
     *
     * @param fechaInicio La fecha de inicio.
     * @param fechaFin La fecha de fin.
     * @return Lista de parcelas disponibles.
     */
    public List<Parcela> getParcelasDisponibles(Date fechaInicio, Date fechaFin) {
        return mRepository.getParcelasDisponibles(fechaInicio, fechaFin);
    }

    /**
     * Obtiene el nombre de una parcela especifica por su ID.
     *
     * @param parcelaId El ID de la parcela.
     * @return El nombre de la parcela, o null si no se encuentra.
     */
    public String getNombreParcelaById(int parcelaId) {
        Log.d("Comprobaciones", "getNombreParcelaById: parcelaId = " + parcelaId);
        String nombreParcela = mParcelaRepository.getNombreParcelaById(parcelaId);
        if (nombreParcela == null) {
            Log.d("Comprobaciones", "getNombreParcelaById: No se encontro nombre para parcelaId = " + parcelaId);
        } else {
            Log.d("Comprobaciones", "getNombreParcelaById: Nombre de parcela = " + nombreParcela);
        }
        return nombreParcela;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown(); // Cierra el pool de hilos al destruir el ViewModel
        Log.d("ViewModelLifecycle", "onCleared llamado: ViewModel destruido");
    }
}
