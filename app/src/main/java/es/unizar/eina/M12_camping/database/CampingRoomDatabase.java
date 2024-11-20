package es.unizar.eina.M12_camping.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase que representa la base de datos del camping utilizando Room.
 * Define la estructura de la base de datos y proporciona métodos para obtener
 * la instancia de la base de datos y el DAO de Parcela.
 */
@Database(entities = {Parcela.class, Reserva.class, ParcelaReservada.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class CampingRoomDatabase extends RoomDatabase {

    /**
     * Proporciona acceso al DAO de Parcela.
     *
     * @return El DAO de Parcela.
     */
    public abstract ParcelaDao parcelaDao();

    /**
     * Proporciona acceso al DAO de Reserva.
     *
     * @return El DAO de Reserva.
     */
    public abstract ReservaDao reservaDao();

    /**
     * Proporciona acceso al DAO de la relación parcela - reserva.
     *
     * @return El DAO de ParcelaReservada.
     */
    public abstract ParcelaReservadaDao parcelaReservadaDao();

    /** Instancia única de la base de datos */
    private static volatile CampingRoomDatabase INSTANCE;

    /** Número de hilos para las operaciones de base de datos */
    private static final int NUMBER_OF_THREADS = 4;

    /** Executor para realizar operaciones de escritura en la base de datos en segundo plano */
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Obtiene la instancia única de la base de datos.
     * Si la instancia aún no se ha creado, se inicializa en un contexto sincronizado.
     *
     * @param context El contexto de la aplicación.
     * @return La instancia única de CampingRoomDatabase.
     */
    static CampingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CampingRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CampingRoomDatabase.class, "m12_camping_database_p4")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /** Callback para inicializar datos en la base de datos al crearla */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {

                ParcelaDao parcelaDao = INSTANCE.parcelaDao();
                ReservaDao reservaDao = INSTANCE.reservaDao();
                ParcelaReservadaDao parcelaReservadaDao = INSTANCE.parcelaReservadaDao();

                parcelaDao.deleteAll();
                reservaDao.deleteAll();
                parcelaReservadaDao.deleteAll();

                Parcela parcela = new Parcela("Aneto", 8, 17.0, "120m2, SI agua, SI luz");
                parcelaDao.insert(parcela);
                parcela = new Parcela("Cinca", 4, 25.0, "80m2, SI agua, NO luz");
                parcelaDao.insert(parcela);

                // Crear un SimpleDateFormat para analizar las fechas
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    // Insertar reservas
                    Reserva reserva = new Reserva(
                            "Juan",
                            123456789,
                            Objects.requireNonNull(dateFormat.parse("10-01-2024")),
                            Objects.requireNonNull(dateFormat.parse("14-01-2024")),
                            0.0
                    );
                    reservaDao.insert(reserva);

                    reserva = new Reserva(
                            "Luisa",
                            987654321,
                            Objects.requireNonNull(dateFormat.parse("12-02-2024")),
                            Objects.requireNonNull(dateFormat.parse("14-02-2024")),
                            0.0
                    );
                    reservaDao.insert(reserva);

                } catch (Exception e) {
                    // Manejar errores en caso de que las fechas no puedan analizarse
                    e.printStackTrace();
                }
            });
        }
    };
}
