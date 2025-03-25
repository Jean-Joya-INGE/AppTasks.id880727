package co.edu.ue.secondactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etEditTask;
    private Button btnSave, btnDelete;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etEditTask = findViewById(R.id.etEditTask);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        // Obtener datos de la tarea seleccionada
        Intent intent = getIntent();
        String task = intent.getStringExtra("task");
        position = intent.getIntExtra("position", -1);

        etEditTask.setText(task);

        // Guardar cambios
        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedTask", etEditTask.getText().toString());
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish(); // Cierra la actividad y envía los datos de regreso
        });

        // Eliminar tarea
        btnDelete.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("delete", true);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
            finish(); // Cierra la actividad y envía los datos de eliminación
        });
    }
}
