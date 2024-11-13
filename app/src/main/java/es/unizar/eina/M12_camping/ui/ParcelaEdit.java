package es.unizar.eina.M12_camping.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import es.unizar.eina.M12_camping.R;


/**
 * Pantalla utilizada para la creación o edición de una parcela.
 * Esta actividad permite al usuario ingresar o modificar los detalles de una parcela.
 * Una vez completados los cambios, los datos se envían de vuelta a la actividad principal.
 */
public class ParcelaEdit extends AppCompatActivity {

    /** Constantes para identificar los datos que se pasan en el Intent */
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
    private ParcelaViewModel mParcelaViewModel; // ViewModel para acceder a los datos

    Button mSaveButton;

    /**
     * Método que se llama cuando se crea la actividad.
     * Configura los elementos de la interfaz de usuario y carga los datos existentes si están disponibles.
     *
     * @param savedInstanceState Estado anterior de la actividad, si se ha guardado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelaedit);

        mNombreText = findViewById(R.id.nombre);
        mMaxOcupantes = findViewById(R.id.maxOcupantes);
        mPrecioXpersona = findViewById(R.id.precioXpersona);
        mDescripcionText = findViewById(R.id.descripcion);

        mParcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class); // Inicialización del ViewModel

        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            String nombre = mNombreText.getText().toString();

            // Verificar si el nombre está vacío
            if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_nombre, Toast.LENGTH_LONG).show();
                return;
            }

            // Verificar si ya existe una parcela con el mismo nombre (excluyendo la parcela actual si estamos editando)
            if (mRowId == null) {
                // Si es una nueva parcela
                if (mParcelaViewModel.isNombreDuplicado(nombre)) {
                    Toast.makeText(getApplicationContext(), R.string.duplicate_name_error, Toast.LENGTH_LONG).show();
                    return;
                }
            } else {
                // Si estamos editando, permitir el nombre si pertenece a la parcela actual
                if (mParcelaViewModel.isNombreDuplicadoExceptId(nombre, mRowId)) {
                    Toast.makeText(getApplicationContext(), R.string.duplicate_name_error, Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if (TextUtils.isEmpty(mMaxOcupantes.getText())) {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_ocupantes, Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(mPrecioXpersona.getText())) {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_precio, Toast.LENGTH_LONG).show();
                return;
            }

            Intent replyIntent = new Intent();
            replyIntent.putExtra(PARCELA_NOMBRE, nombre);
            replyIntent.putExtra(PARCELA_MAXOCUPANTES, Integer.parseInt(mMaxOcupantes.getText().toString()));
            replyIntent.putExtra(PARCELA_PRECIOXPERSONA, Double.parseDouble(mPrecioXpersona.getText().toString()));
            replyIntent.putExtra(PARCELA_DESCRIPCION, mDescripcionText.getText().toString());

            if (mRowId != null) {
                replyIntent.putExtra(PARCELA_ID, mRowId.intValue());
            }

            setResult(RESULT_OK, replyIntent);
            finish();
        });

        populateFields();
    }

    /**
     * Rellena los campos de texto con los datos de una parcela existente si están disponibles.
     * Recupera los datos pasados en el Intent y los asigna a los campos correspondientes.
     */
    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mNombreText.setText(extras.getString(PARCELA_NOMBRE));

            mMaxOcupantes.setText(String.valueOf(extras.getInt(PARCELA_MAXOCUPANTES)));

            mPrecioXpersona.setText(String.valueOf(extras.getDouble(PARCELA_PRECIOXPERSONA)));

            mDescripcionText.setText(extras.getString(PARCELA_DESCRIPCION));
            
            mRowId = extras.getInt(PARCELA_ID);
        }
    }
}