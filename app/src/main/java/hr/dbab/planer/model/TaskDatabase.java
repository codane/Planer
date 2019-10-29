package hr.dbab.planer.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = Task.class, version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    //instanca TaskDatabase klase
    private static TaskDatabase instance; //moramo kreirati ovu varijablu jer ova klasa mora biti singleton
    // - znači da ne možemo kreirati više instanci iste baze podataka, koristimo istu instancu svugdje u aplikaciji

    //ovu metodu ćemo koristiti kako bi pristupili Dao-u
    public abstract TaskDao taskDao();

    //stvaramo bazu podataka, synchronized znači da samo jedna dretva u svakom trenu može pristupiti ovoj metodi
    public static synchronized TaskDatabase getInstance(Context context){
        //radimo instancu baze samo ako već nemamo instancu
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;

    }
}
