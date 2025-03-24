package co.edu.ue.secondactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etTask;
    private Button btnAdd;
    private ListView listTask; // adaptor para le lista de tareas
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initObjects();
        //Generar evento click
        this.btnAdd.setOnClickListener(this::addTask);
    }


    private void addTask(View view) {
        //1. Capturar el texto que introduce el usuario
        String task = this.etTask.getText().toString().trim();
       if(!task.isEmpty()){
           this.arrayList.add(task);
           this.arrayAdapter.notifyDataSetChanged();
           this.etTask.setText("");
       }else {
           Toast.makeText(this, "Coloque una tarea", Toast.LENGTH_SHORT).show();
       }
    }

    private void initObjects() {
        this.etTask = findViewById(R.id.etTask);
        this.btnAdd = findViewById(R.id.btnAdd);
        this.listTask = findViewById(R.id.listTask);
        this.arrayList = new ArrayList<>();
        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.arrayList);
        this.listTask.setAdapter(this.arrayAdapter);
    }
}