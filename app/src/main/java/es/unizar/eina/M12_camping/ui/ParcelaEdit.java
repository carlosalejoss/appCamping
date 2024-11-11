package es.unizar.eina.M12_camping.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unizar.eina.M12_camping.R;

/** Pantalla utilizada para la creación o edición de una parcela */
public class ParcelaEdit extends AppCompatActivity {

    public static final String PARCELA_NOMBRE = "nombre";
    public static final String PARCELA_MAXOCUPANTES = "maxOcupantes";
    public static final String PARCELA_PRECIOXPERSONA = "precioXpersona";
    public static final String PARCELA_DESCRIPCION = "descripcion";
    public static final String PARCELA_ID = "id";

    private EditText mNombreText;
    private EditText mMaxOcupantes;
    private EditText mPrecioXpersona;
    private EditText mDescripcionText;
    private Integer mRowId;

    Button mSaveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteedit); // Asegúrate de que el nombre del layout es correcto

        mNombreText = findViewById(R.id.nombre);
        mMaxOcupantes = findViewById(R.id.maxOcupantes);
        mPrecioXpersona = findViewById(R.id.precioXpersona);
        mDescripcionText = findViewById(R.id.descripcion);

        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mNombreText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
            } else {
                // Enviar los datos de vuelta a la actividad principal
                replyIntent.putExtra(ParcelaEdit.PARCELA_NOMBRE, mNombreText.getText().toString());

                // Convertir los valores a los tipos adecuados antes de enviarlos
                int maxOcupantes = TextUtils.isEmpty(mMaxOcupantes.getText()) ? 0 : Integer.parseInt(mMaxOcupantes.getText().toString());
                replyIntent.putExtra(ParcelaEdit.PARCELA_MAXOCUPANTES, maxOcupantes);

                double precioXpersona = TextUtils.isEmpty(mPrecioXpersona.getText()) ? 0.0 : Double.parseDouble(mPrecioXpersona.getText().toString());
                replyIntent.putExtra(ParcelaEdit.PARCELA_PRECIOXPERSONA, precioXpersona);

                replyIntent.putExtra(ParcelaEdit.PARCELA_DESCRIPCION, mDescripcionText.getText().toString());

                if (mRowId != null) {
                    replyIntent.putExtra(ParcelaEdit.PARCELA_ID, mRowId);
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        populateFields();
    }

    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Log.d("ParcelaEdit", "Extras Nombre: " + extras.getString(ParcelaEdit.PARCELA_NOMBRE));
            Log.d("ParcelaEdit", "Extras MaxOcupantes: " + extras.getInt(ParcelaEdit.PARCELA_MAXOCUPANTES, 0));
            Log.d("ParcelaEdit", "Extras PrecioXpersona: " + extras.getDouble(ParcelaEdit.PARCELA_PRECIOXPERSONA, 0.0));
            Log.d("ParcelaEdit", "Extras Descripcion: " + extras.getString(ParcelaEdit.PARCELA_DESCRIPCION, ""));

            // Llenar los campos con datos existentes si los hay
            mNombreText.setText(extras.getString(ParcelaEdit.PARCELA_NOMBRE));

            // Convertir los datos recibidos a String para mostrarlos en los EditText
            int maxOcupantes = extras.getInt(ParcelaEdit.PARCELA_MAXOCUPANTES, 0);
            mMaxOcupantes.setText(String.valueOf(maxOcupantes));

            double precioXpersona = extras.getDouble(ParcelaEdit.PARCELA_PRECIOXPERSONA, 0.0);
            mPrecioXpersona.setText(String.valueOf(precioXpersona));

            mDescripcionText.setText(extras.getString(ParcelaEdit.PARCELA_DESCRIPCION, ""));
            mRowId = extras.getInt(ParcelaEdit.PARCELA_ID);
        }
    }
}