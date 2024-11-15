package es.unizar.eina.M12_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Representa la relación entre una reserva y una parcela reservada.
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

    /** ID único de ParcelaReservada */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    /** ID de la reserva asociada */
    @NonNull
    @ColumnInfo(name = "reservaId", index = true)
    private int reservaId;

    /** ID de la parcela reservada */
    @NonNull
    @ColumnInfo(name = "parcelaId", index = true)
    private int parcelaId;

    /** Número de ocupantes en la parcela */
    @NonNull
    @ColumnInfo(name = "numeroOcupantes")
    private int numeroOcupantes;

    // Constructor, getters y setters

    public ParcelaReservada() {

    }

    public ParcelaReservada(int reservaId, int parcelaId, int numeroOcupantes) {
        this.reservaId = reservaId;
        this.parcelaId = parcelaId;
        this.numeroOcupantes = numeroOcupantes;
    }

    // Getters y setters para cada campo...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReservaId() {
        return reservaId;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public int getParcelaId() {
        return parcelaId;
    }

    public void setParcelaId(int parcelaId) {
        this.parcelaId = parcelaId;
    }

    public int getNumeroOcupantes() {
        return numeroOcupantes;
    }

    public void setNumeroOcupantes(int numeroOcupantes) {
        this.numeroOcupantes = numeroOcupantes;
    }

}
