package hr.dbab.planer.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task); //Room će sam generirati tijelo metode

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM task_table ORDER BY time ASC")
        //koristimo LiveData koji promatra listu Task objekata
        // ako dođe do promjene u tablici u bazi podataka automatski će ažurirati
    LiveData<List<Task>> getAllTasks();



}
