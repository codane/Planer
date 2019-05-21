package hr.dbab.planer;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    private TaskViewModel taskViewModel;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TaskAdapter adapter = new TaskAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        setupListeners();

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                //ažuriramo RecyclerView
                adapter.setTasks(tasks);

            }
        });
    }
    public void initWidgets(){
        fab = findViewById(R.id.fab_add_task);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void setupListeners(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                int totalTasks = adapter.getItemCount();
                intent.putExtra("totalTasks", totalTasks);
                startActivityForResult(intent, ADD_TASK_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    //ako je prva pozicija u listi = ima indeks 0, onda ostaje na istoj poziciji
                    if (viewHolder.getAdapterPosition() == 0) {
                        adapter.moveItemLeft(viewHolder.getAdapterPosition(), viewHolder.getAdapterPosition());
                    } else {
                        adapter.moveItemLeft(viewHolder.getAdapterPosition(), viewHolder.getAdapterPosition() - 1);
                    }
                }

                if (direction == ItemTouchHelper.RIGHT) {
                    //ako je posljednja pozicija u listi, onda ostaje na istoj poziciji
                    if (viewHolder.getAdapterPosition() < (adapter.getItemCount() - 1)) {
                        adapter.moveItemRight(viewHolder.getAdapterPosition(), viewHolder.getAdapterPosition() + 1);
                    } else {
                        adapter.moveItemRight(viewHolder.getAdapterPosition(), viewHolder.getAdapterPosition());
                    }
                }
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setCustomOnItemClickListener(new TaskAdapter.CustomOnItemClickListener() {
            @Override
            public void customOnItemClick(Task task) {
                //šaljemo podatke o unesenoj obvezi
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
                intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                intent.putExtra(AddEditTaskActivity.EXTRA_TIME, task.getTime());
                intent.putExtra(AddEditTaskActivity.EXTRA_TAGS, task.getTag());

                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }
        });

        adapter.setCustomLongOnItemClickListener(new TaskAdapter.CustomLongOnItemClickListener() {
            @Override
            public void customLongOnItemClickListener(final Task task) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Brisanje")
                        .setMessage("Jeste li sigurni da želite obrisati?")
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskViewModel.delete(task);
                            }
                        })
                        .setNegativeButton("Ne", null)
                        .show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String desription = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            String time = data.getStringExtra(AddEditTaskActivity.EXTRA_TIME);
            String tags = data.getStringExtra(AddEditTaskActivity.EXTRA_TAGS);

            Task task = new Task(title, desription, time, tags);
            taskViewModel.insert(task);

            Toast.makeText(this, "Obveza spremljena", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);
            if (id == -1) {
                //ne može se ažurirati jer je id invalid
                Toast.makeText(this, "Obveza se ne može ažurirati", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String desription = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            String time = data.getStringExtra(AddEditTaskActivity.EXTRA_TIME);
            String tags = data.getStringExtra(AddEditTaskActivity.EXTRA_TAGS);

            Task task = new Task(title, desription, time, tags);
            task.setId(id);
            taskViewModel.update(task);
            Toast.makeText(this, "Obveza ažurirana", Toast.LENGTH_SHORT).show();
        }
    }

}

