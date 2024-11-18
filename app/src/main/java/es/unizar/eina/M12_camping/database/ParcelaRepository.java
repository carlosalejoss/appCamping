package es.unizar.eina.M12_camping.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Clase que gestiona el acceso a la fuente de datos para las parcelas.
 * Interactúa con la base de datos a través de las clases {@link CampingRoomDatabase} y {@link ParcelaDao}.
 * Proporciona métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en las parcelas.
 */
public class ParcelaRepository {

    private final ParcelaDao mParcelaDao;
    private final LiveData<List<Parcela>> mAllParcelas;
    private final LiveData<List<Parcela>> mParcelasOrdNombre;
    private final LiveData<List<Parcela>> mParcelasOrdOcupantes;
    private final LiveData<List<Parcela>> mParcelasOrdPrecio;

    /** Tiempo máximo de espera para operaciones de base de datos en milisegundos */
    private final long TIMEOUT = 15000;

    /**
     * Constructor de ParcelaRepository.
     * Utiliza el contexto de la aplicación para obtener la instancia de la base de datos.
     *
     * @param application La aplicación que proporciona el contexto para instanciar la base de datos.
     */
    public ParcelaRepository(Application application) {
        CampingRoomDatabase db = CampingRoomDatabase.getDatabase(application);
        mParcelaDao = db.parcelaDao();
        mAllParcelas = mParcelaDao.getUnOrderedParcelas();
        mParcelasOrdNombre = mParcelaDao.getOrderedParcelasNombre();
        mParcelasOrdOcupantes = mParcelaDao.getOrderedParcelasMaxOcupantes();
        mParcelasOrdPrecio = mParcelaDao.getOrderedParcelasPrecioXpersona();
    }

    /**
     * Obtiene todas las parcelas en la base de datos sin un orden específico.
     * Room ejecuta todas las consultas en un hilo separado, y LiveData notifica a los observadores cuando los datos cambian.
     *
     * @return Un objeto LiveData con la lista de todas las parcelas.
     */
    public LiveData<List<Parcela>> getAllParcelas() {
        return mAllParcelas;
    }

    /**
     * Obtiene todas las parcelas ordenadas alfabéticamente por nombre.
     *
     * @return Un objeto LiveData con la lista de parcelas ordenadas por nombre en orden ascendente.
     */
    public LiveData<List<Parcela>> getParcelasOrderedNombre() {
        return mParcelasOrdNombre;
    }

    /**
     * Obtiene todas las parcelas ordenadas por el número máximo de ocupantes.
     *
     * @return Un objeto LiveData con la lista de parcelas ordenadas por número máximo de ocupantes en orden ascendente.
     */
    public LiveData<List<Parcela>> getParcelasOrderedOcupantes() {
        return mParcelasOrdOcupantes;
    }

    /**
     * Obtiene todas las parcelas ordenadas por precio por persona.
     *
     * @return Un objeto LiveData con la lista de parcelas ordenadas por precio por persona en orden ascendente.
     */
    public LiveData<List<Parcela>> getParcelasOrderedPrecio() {
        return mParcelasOrdPrecio;
    }

    /**
     * Inserta una nueva parcela en la base de datos.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param parcela La parcela a insertar. Debe tener un nombre no nulo y no vacío.
     * @return El identificador de la parcela insertada, o -1 si la inserción falla.
     */
    public long insert(Parcela parcela) {
        Future<Long> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.insert(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Actualiza una parcela en la base de datos.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param parcela La parcela que se desea actualizar. Debe tener un ID mayor que 0, un nombre no nulo y no vacío,
     *                un número máximo de ocupantes, un precio por persona y una descripción.
     * @return El número de filas modificadas (1 si se actualiza correctamente, 0 si no existe una parcela con ese ID).
     */
    public int update(Parcela parcela) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.update(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Elimina una parcela de la base de datos.
     * La operación se ejecuta en un hilo separado y espera un resultado utilizando Future.
     *
     * @param parcela La parcela a eliminar. Debe tener un ID mayor que 0.
     * @return El número de filas eliminadas (1 si se elimina correctamente, 0 si no existe una parcela con ese ID).
     */
    public int delete(Parcela parcela) {
        Future<Integer> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.delete(parcela));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return -1;
        }
    }

    /**
     * Verifica si existe una parcela con el nombre especificado.
     *
     * @param nombre El nombre a verificar.
     * @return true si existe una parcela con el mismo nombre, de lo contrario false.
     */
    public boolean isNombreDuplicado(String nombre) {
        Future<Boolean> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.isNombreDuplicado(nombre));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return false;
        }
    }

    /**
     * Verifica si existe una parcela con el nombre especificado que no tenga el ID proporcionado.
     *
     * @param nombre El nombre a verificar.
     * @param id     El ID de la parcela que se está editando.
     * @return true si existe otra parcela con el mismo nombre, de lo contrario false.
     */
    public boolean isNombreDuplicadoExceptId(String nombre, int id) {
        Future<Boolean> future = CampingRoomDatabase.databaseWriteExecutor.submit(
                () -> mParcelaDao.isNombreDuplicadoExceptId(nombre, id));
        try {
            return future.get(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("ParcelaRepository", ex.getClass().getSimpleName() + ex.getMessage());
            return false;
        }
    }

}