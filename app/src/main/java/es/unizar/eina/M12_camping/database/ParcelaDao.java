package es.unizar.eina.M12_camping.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/** Definición de un Data Access Object para las notas */
@Dao
public interface ParcelaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Parcela parcela);

    @Update
    int update(Parcela parcela);

    @Delete
    int delete(Parcela parcela);

    @Query("DELETE FROM Parcela")
    void deleteAll();

    @Query("SELECT * FROM Parcela")
    LiveData<List<Parcela>> getUnOrderedParcelas();

    @Query("SELECT * FROM Parcela ORDER BY nombre ASC")
    LiveData<List<Parcela>> getOrderedParcelasNombre();

    @Query("SELECT * FROM Parcela ORDER BY maxOcupantes ASC")
    LiveData<List<Parcela>> getOrderedParcelasMaxOcupantes();

    @Query("SELECT * FROM Parcela ORDER BY precioXpersona ASC")
    LiveData<List<Parcela>> getOrderedParcelasPrecioXpersona();
}

