package co.edu.ue.secondactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SearchView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etTask;
    private Button btnAdd;
    private ListView listTask;
    private SearchView searchTask;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override// Ajuste de interfaz
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initObjects(); // Inicializa los objetos de la interfaz.
        CargarTareasSharedPreferences(); // Carga tareas almacenadas previamente.
        arrayAdapter.notifyDataSetChanged(); //adaptador  cambios en la lista.

        // Configuración de eventos
        this.btnAdd.setOnClickListener(this::addTask);// Agrega una tarea al presionar el botón.
        this.listTask.setOnItemClickListener(this::ManejarItemClick);// Maneja clics en elementos de la lista.
        this.searchTask.setOnQueryTextListener(new SearchListener());// Configura la barra de búsqueda.
    }

    // Método que maneja clics en los elementos de la lista.
    private void ManejarItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedTask = (String) parent.getItemAtPosition(position);
        int realPosition = arrayList.indexOf(selectedTask);

        if (realPosition != -1) {
            Intent intent = new Intent(this, EditTaskActivity.class);
            intent.putExtra("task", selectedTask);
            intent.putExtra("position", realPosition);
            startActivityForResult(intent, 1);
        }
    }

    // Clase interna para manejar la búsqueda de tareas.
    private class SearchListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            arrayAdapter.getFilter().filter(newText);
            return false;
        }
    }

    // Método para agregar una tarea a la lista.
    private void addTask(View view) {
        String task = this.etTask.getText().toString().trim();
        if (!task.isEmpty()) {
            this.arrayList.add(task);
            ActualizarLista();
            this.etTask.setText("");
            GuardarTareasSharedPreferences();
        } else {
            Toast.makeText(this, "Coloque una tarea", Toast.LENGTH_SHORT).show();
        }
    }
    // Método para inicializar los elementos de la interfaz.
    private void initObjects() {
        this.etTask = findViewById(R.id.etTask);
        this.btnAdd = findViewById(R.id.btnAdd);
        this.listTask = findViewById(R.id.listTask);
        this.searchTask = findViewById(R.id.searchTask);

        this.arrayList = new ArrayList<>();
        this.arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.arrayList);
        this.listTask.setAdapter(this.arrayAdapter);
    }
    // Método que maneja el resultado de la actividad de edición.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("position", -1);

            if (data.hasExtra("delete")) {
                arrayList.remove(position);
            } else {
                String updatedTask = data.getStringExtra("updatedTask");
                arrayList.set(position, updatedTask);
            }

            ActualizarLista();
            GuardarTareasSharedPreferences();
        }
    }
    // Método para actualizar la lista
    private void ActualizarLista() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listTask.setAdapter(arrayAdapter);
    }
    // Método para guardar las tareas en almacenamiento SharedPreferences
    private void GuardarTareasSharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        JSONArray jsonArray = new JSONArray(arrayList);
        editor.putString("tasks", jsonArray.toString());
        editor.apply();
    }
    // Método para cargar las tareas almacenadas SharedPreferences
    private void CargarTareasSharedPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tasksJson = prefs.getString("tasks", null);

        if (tasksJson != null) {
            try {
                JSONArray jsonArray = new JSONArray(tasksJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}