package hr.dbab.planer.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import hr.dbab.planer.model.TaskDao;
import hr.dbab.planer.model.TaskDatabase;
import hr.dbab.planer.model.Task;

public class TaskRepository {
    //member varijable
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    //konstruktor
    public TaskRepository (Application application){
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    //kreiranje metoda za operacije na bazi, repository ih izlaže prema van pa će ih ViewModel pozvati
    //Room ne dozvoljava operacije na bazi na glavnoj dretvi
    public void insert(Task task){
        new InsertTaskAsynchTask(taskDao).execute(task);
    }

    public void update(Task task){
        new UpdateTaskAsynchTask(taskDao).execute(task);
    }

    public void delete(Task task){
        new DeleteTaskAsynchTask(taskDao).execute(task);
    }


    //Room će automatski izvršiti operacije na bazi što će vraititi LiveData na pozadinskoj dretvi
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    private static class InsertTaskAsynchTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;//trebamo TaskDao kako bismo izvršili operacije na bazi

        //kako je ova klasa static ne možemo pristupiti TaskDao-u direktno iz repository-a već ga proslijeđujemo koristeći konstruktor
        private InsertTaskAsynchTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsynchTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateTaskAsynchTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsynchTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteTaskAsynchTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }
        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }
}
