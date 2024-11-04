package es.unizar.eina.M12_camping.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Clase anotada como entidad que representa una parcela y que consta de título y cuerpo */
@Entity(tableName = "parcela")
public class Parcela {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "body")
    private String body;

    public Parcela(@NonNull String title, String body) {
        this.title = title;
        this.body = body;
    }

    /** Devuelve el identificador de la parcela */
    public int getId(){
        return this.id;
    }

    /** Permite actualizar el identificador de una parcela */
    public void setId(int id) {
        this.id = id;
    }

    /** Devuelve el título de la parcela */
    public String getTitle(){
        return this.title;
    }

    /** Devuelve el cuerpo de la parcela */
    public String getBody(){
        return this.body;
    }

}
