package co.edu.ue.secondactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent; // Importamos Intent para cambiar de actividad

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etTask;
    private Button btnAdd;
    private ListView listTask; // Adaptador para la lista de tareas
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

        // Evento para agregar tarea
        this.btnAdd.setOnClickListener(this::addTask);

        // Evento para abrir la actividad de edición al hacer clic en una tarea
        this.listTask.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
            intent.putExtra("task", arrayList.get(position)); // Enviamos la tarea seleccionada
            intent.putExtra("position", position); // Enviamos la posición de la tarea en la lista
            startActivityForResult(intent, 1); // Lanzamos la actividad esperando resultado
        });
    }

    private void addTask(View view) {
        // Capturar el texto introducido por el usuario
        String task = this.etTask.getText().toString().trim();
        if (!task.isEmpty()) {
            this.arrayList.add(task);
            this.arrayAdapter.notifyDataSetChanged(); // Actualizar la lista
            this.etTask.setText(""); // Limpiar el campo de entrada
        } else {
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

    // Método para recibir los resultados de la actividad de edición
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);

            if (data.hasExtra("delete")) {
                // Eliminar tarea
                arrayList.remove(position);
            } else {
                // Editar tarea
                String updatedTask = data.getStringExtra("updatedTask");
                arrayList.set(position, updatedTask);
            }

            arrayAdapter.notifyDataSetChanged(); // Actualizar la lista después de editar o eliminar
        }
    }
}
