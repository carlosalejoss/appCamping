package es.unizar.eina.M12_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Definición de un Data Access Object (DAO) para las operaciones CRUD de las parcelas.
 * Proporciona métodos para insertar, actualizar, eliminar y consultar parcelas en la base de datos.
 */
@Dao
public interface ParcelaDao {

    /**
     * Inserta una nueva parcela en la base de datos.
     * Si ya existe una parcela con el mismo identificador, ignora el conflicto.
     *
     * @param parcela La parcela a insertar.
     * @return El identificador de la parcela insertada.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Parcela parcela);

    /**
     * Actualiza una parcela existente en la base de datos.
     *
     * @param parcela La parcela con los datos actualizados.
     * @return El número de filas afectadas (debería ser 1 si la actualización es exitosa).
     */
    @Update
    int update(Parcela parcela);

    /**
     * Elimina una parcela específica de la base de datos.
     *
     * @param parcela La parcela a eliminar.
     * @return El número de filas afectadas (debería ser 1 si la eliminación es exitosa).
     */
    @Delete
    int delete(Parcela parcela);

    /**
     * Elimina todas las parcelas de la base de datos.
     */
    @Query("DELETE FROM Parcela")
    void deleteAll();

    /**
     * Obtiene todas las parcelas sin ningún orden específico.
     *
     * @return Un objeto LiveData que contiene una lista de todas las parcelas.
     */
    @Query("SELECT * FROM Parcela")
    LiveData<List<Parcela>> getUnOrderedParcelas();

    /**
     * Obtiene todas las parcelas ordenadas alfabéticamente por nombre.
     *
     * @return Un objeto LiveData que contiene una lista de parcelas ordenadas por nombre en orden ascendente.
     */
    @Query("SELECT * FROM Parcela ORDER BY nombre ASC")
    LiveData<List<Parcela>> getOrderedParcelasNombre();

    /**
     * Obtiene todas las parcelas ordenadas por el número máximo de ocupantes.
     *
     * @return Un objeto LiveData que contiene una lista de parcelas ordenadas por el número máximo de ocupantes en orden ascendente.
     */
    @Query("SELECT * FROM Parcela ORDER BY maxOcupantes ASC")
    LiveData<List<Parcela>> getOrderedParcelasMaxOcupantes();

    /**
     * Obtiene todas las parcelas ordenadas por el precio por persona.
     *
     * @return Un objeto LiveData que contiene una lista de parcelas ordenadas por precio por persona en orden ascendente.
     */
    @Query("SELECT * FROM Parcela ORDER BY precioXpersona ASC")
    LiveData<List<Parcela>> getOrderedParcelasPrecioXpersona();

    /**
     * Verifica si existe una parcela con el nombre especificado.
     *
     * @param nombre El nombre a verificar.
     * @return true si existe una parcela con el mismo nombre, de lo contrario false.
     */
    @Query("SELECT COUNT(*) > 0 FROM Parcela WHERE nombre = :nombre")
    boolean isNombreDuplicado(String nombre);

    /**
     * Verifica si existe una parcela con el nombre especificado que no tenga el ID proporcionado.
     * Este método es útil para la edición de parcelas, donde se permite que el nombre no cambie.
     *
     * @param nombre El nombre a verificar.
     * @param id     El ID de la parcela que se está editando.
     * @return true si existe otra parcela con el mismo nombre, de lo contrario false.
     */
    @Query("SELECT COUNT(*) > 0 FROM Parcela WHERE nombre = :nombre AND id != :id")
    boolean isNombreDuplicadoExceptId(String nombre, int id);

    /**
     * Obtiene una parcela por su ID.
     *
     * @param id El ID de la parcela.
     * @return La parcela correspondiente.
     */
    @Query("SELECT * FROM parcela WHERE id = :id")
    Parcela getParcelaById(int id);

    @Query("SELECT nombre FROM parcela WHERE id = :id")
    String getNombreParcelaById(int id);
}