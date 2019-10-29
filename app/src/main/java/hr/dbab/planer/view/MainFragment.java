package hr.dbab.planer.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hr.dbab.planer.R;
import hr.dbab.planer.adapter.TaskAdapter;
import hr.dbab.planer.model.Task;
import hr.dbab.planer.viewmodel.TaskViewModel;


public class MainFragment extends Fragment {
    //deklariranje varijabli
    private FloatingActionButton fab;
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private TaskAdapter adapter = new TaskAdapter();

    //konstruktor
    public MainFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // postavljanje sučelja za ovaj fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        //inicijaliziranje varijabli
        fab = v.findViewById(R.id.fab_add_task);
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
                //spremanje ukupnog broja obveza u varijablu
                taskViewModel.totalTasks = adapter.getItemCount();
            }
        });

        //klikom na FAB varijable postavljamo na prazno i mijenjamo fragment
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskViewModel.id = -1;
                taskViewModel.title = "";
                taskViewModel.description = "";
                taskViewModel.time = "";
                taskViewModel.tags = "";
                //pozivanje metode kojom se zamjenjuje fragment
                changeFragment();
            }
        });
        //kreiranje swipe funkcionalnosti
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

        //koristimo custom sučelje kojim u varijable spremamo podatke o obvezi na koju je kliknuto, te potom mijenjamo fragment
        adapter.setCustomOnItemClickListener(new TaskAdapter.CustomOnItemClickListener() {
            @Override
            public void customOnItemClick(Task task) {
                //dohvaćamo i spremamo podatke o unesenoj obvezi u varijable u TaskViewModel klasi
                taskViewModel.id = task.getId();
                taskViewModel.title = task.getTitle();
                taskViewModel.description = task.getDescription();
                taskViewModel.time = task.getTime();
                taskViewModel.tags = task.getTag();

                //pozivanje metode kojom se zamjenjuje fragment
                changeFragment();
            }
        });

        //dugim klikom otvara se dijalog kojim se može obrisati trenutna obveza
        adapter.setCustomLongOnItemClickListener(new TaskAdapter.CustomLongOnItemClickListener() {
            @Override
            public void customLongOnItemClickListener(final Task task) {
                new AlertDialog.Builder(getActivity())
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
    //metoda za zamijenu fragmenata
    private void changeFragment(){
        Fragment changeFragment = new AddEditFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, changeFragment)
                .addToBackStack(null)
                .commit();
    }
}
