package es.unizar.eina.M12_camping.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.M12_camping.database.Parcela;
import es.unizar.eina.M12_camping.database.ParcelaRepository;

/**
 * ViewModel para gestionar la UI de parcelas y almacenar datos para la actividad ListadoParcelas.
 * Proporciona metodos para obtener listas de parcelas y realizar operaciones CRUD a traves del repositorio.
 */
public class ParcelaViewModel extends AndroidViewModel {

    private final ParcelaRepository mRepository;

    private final LiveData<List<Parcela>> mAllParcelas;
    private final LiveData<List<Parcela>> mParcelasOrdNombre;
    private final LiveData<List<Parcela>> mParcelasOrdOcupantes;
    private final LiveData<List<Parcela>> mParcelasOrdPrecio;

    /**
     * Constructor de ParcelaViewModel.
     * Inicializa el repositorio y obtiene las listas de parcelas.
     *
     * @param application La aplicacion actual, que proporciona el contexto para inicializar el repositorio.
     */
    public ParcelaViewModel(Application application) {
        super(application);
        mRepository = new ParcelaRepository(application);
        mAllParcelas = mRepository.getAllParcelas();
        mParcelasOrdNombre = mRepository.getParcelasOrderedNombre();
        mParcelasOrdOcupantes = mRepository.getParcelasOrderedOcupantes();
        mParcelasOrdPrecio = mRepository.getParcelasOrderedPrecio();
    }

    /**
     * Obtiene todas las parcelas sin un orden especifico.
     *
     * @return Un objeto LiveData que contiene la lista de todas las parcelas.
     */
    LiveData<List<Parcela>> getAllParcelas() {
        return mAllParcelas;
    }

    /**
     * Obtiene todas las parcelas ordenadas por nombre.
     *
     * @return Un objeto LiveData que contiene la lista de parcelas ordenadas alfabeticamente.
     */
    LiveData<List<Parcela>> getParcelasOrderedNombre() {
        return mParcelasOrdNombre;
    }

    /**
     * Obtiene todas las parcelas ordenadas por el numero maximo de ocupantes.
     *
     * @return Un objeto LiveData que contiene la lista de parcelas ordenadas por ocupantes.
     */
    LiveData<List<Parcela>> getParcelasOrderedOcupantes() {
        return mParcelasOrdOcupantes;
    }

    /**
     * Obtiene todas las parcelas ordenadas por el precio por persona.
     *
     * @return Un objeto LiveData que contiene la lista de parcelas ordenadas por precio.
     */
    LiveData<List<Parcela>> getParcelasOrderedPrecio() {
        return mParcelasOrdPrecio;
    }

    /**
     * Inserta una nueva parcela en la base de datos.
     *
     * @param parcela La parcela a insertar.
     */
    public void insert(Parcela parcela) {
        mRepository.insert(parcela);
    }

    /**
     * Actualiza una parcela existente en la base de datos.
     *
     * @param parcela La parcela a actualizar.
     */
    public void update(Parcela parcela) {
        mRepository.update(parcela);
    }

    /**
     * Elimina una parcela de la base de datos.
     *
     * @param parcela La parcela a eliminar.
     */
    public void delete(Parcela parcela) {
        mRepository.delete(parcela);
    }

    /**
     * Verifica si ya existe una parcela con el nombre dado.
     *
     * @param nombre El nombre de la parcela a verificar.
     * @return true si ya existe una parcela con ese nombre, false en caso contrario.
     */
    public boolean isNombreDuplicado(String nombre) {
        return mRepository.isNombreDuplicado(nombre);
    }

    /**
     * Verifica si un nombre de parcela ya existe en la base de datos, excluyendo una parcela especifica.
     *
     * @param nombre El nombre de la parcela a verificar.
     * @param id     El ID de la parcela que se esta excluyendo de la verificacion.
     * @return true si el nombre ya existe (excluyendo la parcela especificada), false en caso contrario.
     */
    public boolean isNombreDuplicadoExceptId(String nombre, int id) {
        // Consulta en el repositorio excluyendo el ID actual
        return mRepository.isNombreDuplicadoExceptId(nombre, id);
    }

}
