package es.unizar.eina.M12_camping.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Representa la relacion entre una reserva y una parcela reservada.
 * Cada instancia de esta clase asocia una parcela especifica a una reserva concreta,
 * incluyendo el numero de ocupantes asignados a esa parcela.
 */
@Entity(tableName = "parcelaReservada",
        foreignKeys = {
                @ForeignKey(entity = Reserva.class,
                        parentColumns = "id",
                        childColumns = "reservaId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Parcela.class,
                        parentColumns = "id",
                        childColumns = "parcelaId",
                        onDelete = ForeignKey.CASCADE)
        })
public class ParcelaReservada {

    /** ID unico de ParcelaReservada */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    /** ID de la reserva asociada */
    @ColumnInfo(name = "reservaId", index = true)
    private int reservaId;

    /** ID de la parcela reservada */
    @ColumnInfo(name = "parcelaId", index = true)
    private int parcelaId;

    /** Numero de ocupantes en la parcela */
    @ColumnInfo(name = "numeroOcupantes")
    private int numeroOcupantes;

    /**
     * Constructor de la clase ParcelaReservada.
     *
     * @param reservaId       ID de la reserva asociada.
     * @param parcelaId       ID de la parcela reservada.
     * @param numeroOcupantes Numero de ocupantes en la parcela.
     */
    public ParcelaReservada(int reservaId, int parcelaId, int numeroOcupantes) {
        this.reservaId = reservaId;
        this.parcelaId = parcelaId;
        this.numeroOcupantes = numeroOcupantes;
    }

    /**
     * Obtiene el ID unico de ParcelaReservada.
     *
     * @return ID de ParcelaReservada.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID unico de ParcelaReservada.
     *
     * @param id ID a establecer.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el ID de la reserva asociada.
     *
     * @return ID de la reserva.
     */
    public int getReservaId() {
        return reservaId;
    }

    /**
     * Establece el ID de la reserva asociada.
     *
     * @param reservaId ID de la reserva a asociar.
     */
    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    /**
     * Obtiene el ID de la parcela reservada.
     *
     * @return ID de la parcela.
     */
    public int getParcelaId() {
        return parcelaId;
    }

    /**
     * Establece el ID de la parcela reservada.
     *
     * @param parcelaId ID de la parcela a asociar.
     */
    public void setParcelaId(int parcelaId) {
        this.parcelaId = parcelaId;
    }

    /**
     * Obtiene el numero de ocupantes en la parcela.
     *
     * @return Numero de ocupantes.
     */
    public int getNumeroOcupantes() {
        return numeroOcupantes;
    }

    /**
     * Establece el numero de ocupantes en la parcela.
     *
     * @param numeroOcupantes Numero de ocupantes a asignar.
     */
    public void setNumeroOcupantes(int numeroOcupantes) {
        this.numeroOcupantes = numeroOcupantes;
    }

}