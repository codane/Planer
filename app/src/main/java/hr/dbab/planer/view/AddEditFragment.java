package hr.dbab.planer.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import hr.dbab.planer.R;
import hr.dbab.planer.model.Task;
import hr.dbab.planer.viewmodel.TaskViewModel;


public class AddEditFragment extends Fragment {
    //deklariranje varijabli
    private EditText etTitle;
    private EditText etDescription;
    private EditText etTime;
    private EditText etTags;
    private Button btnSave;
    private TextView tvTotalTasks;
    private TaskViewModel taskViewModel;

    //konstruktor
    public AddEditFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // postavljanje sučelja za ovaj fragment
        View v = inflater.inflate(R.layout.fragment_add_edit, container, false);
        //inicijaliziranje varijabli
        etTitle = v.findViewById(R.id.et_title);
        etDescription = v.findViewById(R.id.et_description);
        etTime = v.findViewById(R.id.et_time);
        etTags = v.findViewById(R.id.et_tags);
        btnSave = v.findViewById(R.id.btn_save_task);
        tvTotalTasks = v.findViewById(R.id.tv_total_tasks);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        taskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);

        //dohvaćamo podatke o broju unesenih obveza iz varijable i spremamo u TextView
        tvTotalTasks.setText("Ukupan broj obveza: " + taskViewModel.totalTasks);

        //provjeravamo postoji li id i je li različit od -1, što znači se radi update
        if (taskViewModel.id != -1 ) {
            //podaci iz varijabli u TaskViewModel klasi se stavljaju u EditTextove
            etTitle.setText(taskViewModel.title);
            etDescription.setText(taskViewModel.description);
            etTime.setText(taskViewModel.time);
            etTags.setText(taskViewModel.tags);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dohvaćanje podataka iz EditText-ova i spremanje u varijable
                String title = etTitle.getText().toString();
                String description = etDescription.getText().toString();
                String time = etTime.getText().toString();
                String tags = etTags.getText().toString();

                //provjeravamo jesu li svi podaci uneseni
                if (title.trim().isEmpty() || description.trim().isEmpty() || time.trim().isEmpty() || tags.trim().isEmpty()){
                    Toast.makeText(getActivity(), "Niste unijeli sve", Toast.LENGTH_SHORT).show();
                    return;
                }

                //provjera je li id različit od -1, ako je onda imamo update situaciju
                if (taskViewModel.id != -1){
                    //kreiranje novog Task objekta pomoću varijabli
                    Task updateTask = new Task(title, description, time, tags);
                    //postavljanje id-a za taj Task objekt, kako ne bi dodalo novi Task u listu neko ažuriralo onaj s tim id-em
                    updateTask.setId(taskViewModel.id);

                    //pozivanje public metode update TaskViewModel klase kojom se ažurira Task objekt u bazi
                    taskViewModel.update(updateTask);
                    //prikaz Toast poruke da je obveza ažurirana
                    Toast.makeText(getActivity(), "Obveza ažurirana", Toast.LENGTH_SHORT).show();

                } else{
                    //ovdje nemamo id uopće, što znači da dodajemo novi Task objekt u bazu, pri čemu Room automatski generira id
                    Task newTask = new Task(title, description, time,tags);
                    //pozivanje public metode insert TaskViewModel klase kojom dodajemo novi Task objekt u bazu
                    taskViewModel.insert(newTask);
                    Toast.makeText(getActivity(), "Obveza spremljena", Toast.LENGTH_SHORT).show();
                }
                //pozivanje metode kojom se skriva tipkovnica
                hideKeyboard(getActivity());
                //pozivanje metode kojom se zamjenjuje fragment
                changeFragment();
            }

        });
    }
    //metoda kojom se mijenja fragment
    private void changeFragment(){
        Fragment returnFragment = new MainFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, returnFragment)
                .commit();
    }
    //metoda kojom skrivamo tipkovnicu
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        //provjera ima li neki view fokus
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
