package es.unizar.eina.M12_camping.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.Reserva;

/**
 * Pantalla utilizada para la creación o edición de una reserva.
 * Esta actividad permite al usuario ingresar o modificar los detalles de una reserva.
 * Una vez completados los cambios, los datos se envían de vuelta a la actividad principal.
 */
public class ReservaEdit extends AppCompatActivity {

    /** Constantes para identificar los datos que se pasan en el Intent */
    public static final String RESERVA_NOMBRE_CLIENTE = "nombreCliente";
    public static final String RESERVA_TELEFONO = "telefono";
    public static final String RESERVA_FECHA_ENTRADA = "fechaEntrada";
    public static final String RESERVA_FECHA_SALIDA = "fechaSalida";
    public static final String RESERVA_PRECIO_TOTAL = "precioTotal";
    public static final String RESERVA_ID = "id";

    private EditText mNombreClienteText;
    private EditText mTelefonoText;
    private EditText mFechaEntradaText;
    private EditText mFechaSalidaText;
    private EditText mPrecioTotalText;
    private Integer mRowId;
    private ReservaViewModel mReservaViewModel;

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
        setContentView(R.layout.activity_reserva_edit);

        mNombreClienteText = findViewById(R.id.nombreCliente);
        mTelefonoText = findViewById(R.id.telefono);
        mFechaEntradaText = findViewById(R.id.fechaEntrada);
        mFechaSalidaText = findViewById(R.id.fechaSalida);
        mPrecioTotalText = findViewById(R.id.precioTotal);

        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);

        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            String nombreCliente = mNombreClienteText.getText().toString();
            String telefonoStr = mTelefonoText.getText().toString();
            String fechaEntradaStr = mFechaEntradaText.getText().toString();
            String fechaSalidaStr = mFechaSalidaText.getText().toString();
            String precioTotalStr = mPrecioTotalText.getText().toString();

            // Verificar si los campos obligatorios están vacíos
            if (TextUtils.isEmpty(nombreCliente)) {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_nombre_cliente, Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(telefonoStr)) {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_telefono, Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(fechaEntradaStr)) {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_fecha_entrada, Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(fechaSalidaStr)) {
                Toast.makeText(getApplicationContext(), R.string.empty_not_saved_fecha_salida, Toast.LENGTH_LONG).show();
                return;
            }

            // Convertir los datos de entrada
            Integer telefono = Integer.parseInt(telefonoStr);
            Date fechaEntrada = DateConverter.toDate(fechaEntradaStr);
            Date fechaSalida = DateConverter.toDate(fechaSalidaStr);
            float precioTotal = TextUtils.isEmpty(precioTotalStr) ? 0.0f : Float.parseFloat(precioTotalStr);

            // Crear la reserva y enviarla de vuelta
            Intent replyIntent = new Intent();
            replyIntent.putExtra(RESERVA_NOMBRE_CLIENTE, nombreCliente);
            replyIntent.putExtra(RESERVA_TELEFONO, telefono);
            replyIntent.putExtra(RESERVA_FECHA_ENTRADA, fechaEntrada.getTime());
            replyIntent.putExtra(RESERVA_FECHA_SALIDA, fechaSalida.getTime());
            replyIntent.putExtra(RESERVA_PRECIO_TOTAL, precioTotal);

            if (mRowId != null) {
                replyIntent.putExtra(RESERVA_ID, mRowId.intValue());
            }

            setResult(RESULT_OK, replyIntent);
            finish();
        });

        populateFields();
    }

    /**
     * Rellena los campos de texto con los datos de una reserva existente si están disponibles.
     * Recupera los datos pasados en el Intent y los asigna a los campos correspondientes.
     */
    private void populateFields() {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mNombreClienteText.setText(extras.getString(RESERVA_NOMBRE_CLIENTE));
            mTelefonoText.setText(String.valueOf(extras.getInt(RESERVA_TELEFONO)));
            mFechaEntradaText.setText(DateConverter.toString(new Date(extras.getLong(RESERVA_FECHA_ENTRADA))));
            mFechaSalidaText.setText(DateConverter.toString(new Date(extras.getLong(RESERVA_FECHA_SALIDA))));
            mPrecioTotalText.setText(String.valueOf(extras.getFloat(RESERVA_PRECIO_TOTAL)));
            mRowId = extras.getInt(RESERVA_ID);
        }
    }
}

