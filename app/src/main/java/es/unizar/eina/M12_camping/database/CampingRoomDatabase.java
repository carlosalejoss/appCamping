package es.unizar.eina.M12_camping.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clase que representa la base de datos del camping utilizando Room.
 * Define la estructura de la base de datos y proporciona métodos para obtener
 * la instancia de la base de datos y el DAO de Parcela.
 */
@Database(entities = {Parcela.class, Reserva.class}, version = 1, exportSchema = false)
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
                                    CampingRoomDatabase.class, "camping_database")
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

                parcelaDao.deleteAll();
                reservaDao.deleteAll();

                Parcela parcela = new Parcela("Aneto", 8, 17.0, "120m2, SI agua, SI luz");
                parcelaDao.insert(parcela);
                parcela = new Parcela("Cinca", 4, 25.0, "80m2, SI agua, NO luz");
                parcelaDao.insert(parcela);

                Reserva reserva = new Reserva("Cliente 1", 123456789, new Date(), new Date(), 150.0);
                reservaDao.insert(reserva);
                reserva = new Reserva("Cliente 2", 987654321, new Date(), new Date(), 200.0);
                reservaDao.insert(reserva);
            });
        }
    };
}
