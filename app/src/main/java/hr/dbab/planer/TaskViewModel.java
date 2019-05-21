package hr.dbab.planer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {//kreiranje member varijabli
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        //instanciranje member varijabli
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    //activity ima referencu samo na ViewModel, tako da moramo stvoriti metode za metode koje rade operacije na bazi iz repositoryija
    public void insert(Task task){
        repository.insert(task);
    }

    public void update(Task task){
        repository.update(task);
    }

    public void delete(Task task){
        repository.delete(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}
