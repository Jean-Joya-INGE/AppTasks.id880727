package co.edu.ue.secondactivity;

import android.content.Intent;
import android.os.Bundle;
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

        CargarTaskData(); // Obtener los datos de la tarea seleccionada
        GuardarTaskChanges(); // Guardar cambios
        EliminarTask(); // Eliminar tarea

    }
    // Obtener los datos de la tarea seleccionada
    private void CargarTaskData() {
        Intent intent = getIntent();
        String task = intent.getStringExtra("task");
        position = intent.getIntExtra("position", -1);
        etEditTask.setText(task);
    }
    // Guardar cambios
    private void GuardarTaskChanges() {
        btnSave.setOnClickListener(v -> {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedTask", etEditTask.getText().toString());
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();
    });}

    // Eliminar tarea
    private void EliminarTask() {
        btnDelete.setOnClickListener(v -> {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("delete", true);
        resultIntent.putExtra("position", position);
        setResult(RESULT_OK, resultIntent);
        finish();
    });}
}
