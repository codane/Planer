package hr.dbab.planer;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditTaskActivity extends AppCompatActivity {
    private EditText etTitle;
    private EditText etDescription;
    private EditText etTime;
    private EditText etTags;
    private Button btnSave;
    private TextView tvTotalTasks;

    public static final String EXTRA_ID = "hr.dbab.planer.EXTRA_ID";
    public static final String EXTRA_TITLE = "hr.dbab.planer.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "hr.dbab.planer.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIME = "hr.dbab.planer.EXTRA_TIME";
    public static final String EXTRA_TAGS = "hr.dbab.planer.EXTRA_TAGS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initWidgets();
        setupListeners();

        //prikazuje se strelica za back
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        handleIntent();
    }
    public void initWidgets(){
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etTime = findViewById(R.id.et_time);
        etTags = findViewById(R.id.et_tags);
        btnSave = findViewById(R.id.btn_save_task);
        tvTotalTasks = findViewById(R.id.tv_total_tasks);
    }
    public void setupListeners(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }
    public void handleIntent(){
        //dohvaćamo i prikazujemo ukupan broj obveza
        Intent intent = getIntent();
        if (intent.hasExtra("totalTasks")) {
            int totalTasks = intent.getIntExtra("totalTasks", 0);
            tvTotalTasks.setText("Ukupan broj obveza: " + totalTasks);
        }
        //ako imamo edit situaciju onda će se ovaj dio pod if odraditi
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Izmjena detalja");
            etTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            etTime.setText(intent.getStringExtra(EXTRA_TIME));
            etTags.setText(intent.getStringExtra(EXTRA_TAGS));

        }else {
            setTitle("Detalji");
        }
    }

    public void saveTask(){
        //unesene podatke o obvezi
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String time = etTime.getText().toString();
        String tags = etTags.getText().toString();

        //provjeravamo jesu li svi podaci uneseni
        if (title.trim().isEmpty() || description.trim().isEmpty() || time.trim().isEmpty() || tags.trim().isEmpty()){
            Toast.makeText(this, "Niste unijeli sve", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent dataIntent = new Intent();
        dataIntent.putExtra(EXTRA_TITLE, title);
        dataIntent.putExtra(EXTRA_DESCRIPTION, description);
        dataIntent.putExtra(EXTRA_TIME, time);
        dataIntent.putExtra(EXTRA_TAGS, tags);

        //kad je id -1, id je invalid
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            dataIntent.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, dataIntent);
        finish();
    }
}
