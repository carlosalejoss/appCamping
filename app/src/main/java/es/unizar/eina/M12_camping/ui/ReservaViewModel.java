package es.unizar.eina.M12_camping.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.M12_camping.database.Reserva;
import es.unizar.eina.M12_camping.database.ReservaRepository;

/**
 * ViewModel que proporciona la lógica de acceso a los datos de reservas.
 * Facilita la comunicación entre la UI y el repositorio de datos.
 */
public class ReservaViewModel extends AndroidViewModel {

    private final ReservaRepository mRepository;

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
     */
    public void insert(Reserva reserva) {
        mRepository.insert(reserva);
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
}