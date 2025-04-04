package es.unizar.eina.M12_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

/**
 * Definicion de un Data Access Object (DAO) para las operaciones CRUD de las parcelas.
 * Proporciona metodos para insertar, actualizar, eliminar y consultar parcelas en la base de datos.
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
     * @return El numero de filas afectadas (deberia ser 1 si la actualizacion es exitosa).
     */
    @Update
    int update(Parcela parcela);

    /**
     * Elimina una parcela especifica de la base de datos.
     *
     * @param parcela La parcela a eliminar.
     * @return El numero de filas afectadas (deberia ser 1 si la eliminacion es exitosa).
     */
    @Delete
    int delete(Parcela parcela);

    /**
     * Elimina todas las parcelas de la base de datos.
     */
    @Query("DELETE FROM Parcela")
    void deleteAll();

    /**
     * Obtiene todas las parcelas sin ningun orden especifico.
     *
     * @return Un objeto LiveData que contiene una lista de todas las parcelas.
     */
    @Query("SELECT * FROM Parcela")
    LiveData<List<Parcela>> getUnOrderedParcelas();

    /**
     * Obtiene todas las parcelas ordenadas alfabeticamente por nombre.
     *
     * @return Un objeto LiveData que contiene una lista de parcelas ordenadas por nombre en orden ascendente.
     */
    @Query("SELECT * FROM Parcela ORDER BY nombre ASC")
    LiveData<List<Parcela>> getOrderedParcelasNombre();

    /**
     * Obtiene todas las parcelas ordenadas por el numero maximo de ocupantes.
     *
     * @return Un objeto LiveData que contiene una lista de parcelas ordenadas por el numero maximo de ocupantes en orden ascendente.
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
     * Este metodo es util para la edicion de parcelas, donde se permite que el nombre no cambie.
     *
     * @param nombre El nombre a verificar.
     * @param id     El ID de la parcela que se esta editando.
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

    @Query("SELECT * FROM Parcela WHERE id NOT IN (SELECT pr.parcelaId FROM ParcelaReservada pr " +
            "JOIN Reserva r ON pr.reservaId = r.id " +
            "WHERE (:fechaInicio BETWEEN r.fechaEntrada AND r.fechaSalida) " +
            "OR (:fechaFin BETWEEN r.fechaEntrada AND r.fechaSalida) " +
            "OR (r.fechaEntrada BETWEEN :fechaInicio AND :fechaFin))")
    List<Parcela> getParcelasDisponibles(Date fechaInicio, Date fechaFin);

}