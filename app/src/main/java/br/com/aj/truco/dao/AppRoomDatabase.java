package br.com.aj.truco.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import br.com.aj.truco.classe.Jogador;
import br.com.aj.truco.classe.Partida;
import br.com.aj.truco.classe.PartidaJogada;
import br.com.aj.truco.classe.PartidaJogador;
import br.com.aj.truco.classe.Time;


@Database(entities = {
        Jogador.class,
        PartidaJogada.class,
        Time.class,
        Partida.class,
        PartidaJogador.class},
        version = AppRoomDatabase.DBVERSION
        //verificar se precisa mesmo de autoMigrations
        //, autoMigrations = {@AutoMigration(from = 6, to = 7), @AutoMigration(from = 7, to = 8)}
)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract JogadorDAO jogadorDAO();

    public abstract PartidaJogadaDAO partidaJogadaDAO();

    public abstract TimeDAO timeDAO();

    public abstract PartidaDAO partidaDAO();

    public abstract PartidaJogadorDAO partidaJogadorDAO();

    private static volatile AppRoomDatabase INSTANCE;
    //static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public static final int DBVERSION = 8;
    public static final String DATABASE_NAME = "truco-db.db";
    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }


    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppRoomDatabase.class, "app_database")
                            .addMigrations(MIGRATION_6_7)
                            .addMigrations(MIGRATION_7_8)
                            // .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE PartidaJogada ADD COLUMN TimeID INTEGER NULL");
          }
    };

    static final Migration MIGRATION_7_8 = new Migration( 7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
           database.execSQL("ALTER TABLE Jogador ADD COLUMN Ativo INTEGER");
        }
    };
}
