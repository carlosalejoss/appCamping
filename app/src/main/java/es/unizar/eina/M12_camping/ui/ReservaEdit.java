package es.unizar.eina.M12_camping.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.DateConverter;
import es.unizar.eina.M12_camping.database.Parcela;
import es.unizar.eina.M12_camping.database.ParcelaReservada;
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
    private TextView mPrecioTotalText;
    private Integer mRowId;
    private RecyclerView mRecyclerViewParcelas;
    private ParcelaReservadaAdapter mAdapter;
    private RecyclerView mParcelasReservadasRecyclerView;

    private List<ParcelaReservada> mParcelasReservadasTemp = new ArrayList<>();
    private ReservaViewModel mReservaViewModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    Button mSaveButton;

    /**
     * Método que se llama cuando se crea la actividad.
     * Configura los elementos de la interfaz de usuario y carga los datos existentes si están disponibles.
     *
     * @param savedInstanceState Estado anterior de la actividad, si se ha guardado.
     */
    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservaedit);

        // Inicialización de los campos
        mNombreClienteText = findViewById(R.id.nombreCliente);
        mTelefonoText = findViewById(R.id.telefono);
        mFechaEntradaText = findViewById(R.id.fechaEntrada);
        mFechaSalidaText = findViewById(R.id.fechaSalida);
        mPrecioTotalText = findViewById(R.id.precioTotal);

        // Inicializar el RecyclerView
        mParcelasReservadasRecyclerView = findViewById(R.id.recyclerViewParcelas);
        mAdapter = new ParcelaReservadaAdapter(
                parcela -> onParcelaReservadaEdited(parcela), // Editar parcela reservada
                parcela -> onParcelaReservadaDeleted(parcela) // Eliminar parcela reservada
        );
        mParcelasReservadasRecyclerView.setAdapter(mAdapter);
        mParcelasReservadasRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ViewModel para manejar la lógica de datos de las reservas
        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);

        // Observar cambios en las parcelas reservadas
        mReservaViewModel.getParcelasReservadas().observe(this, parcelasReservadas -> {
            mAdapter.submitList(parcelasReservadas); // Actualiza el adaptador con la lista
            actualizarPrecioTotal(parcelasReservadas); // Actualiza el precio total dinámicamente
        });

        // Botón para añadir nuevas parcelas reservadas
        Button addParcelaButton = findViewById(R.id.button_add_parcela);
        addParcelaButton.setOnClickListener(view -> openAddParcelaDialog());

        // Configuración del botón de guardar
        mSaveButton = findViewById(R.id.button_save);
        mSaveButton.setOnClickListener(view -> {
            String nombreCliente = mNombreClienteText.getText().toString();
            String telefonoStr = mTelefonoText.getText().toString();
            String fechaEntradaStr = mFechaEntradaText.getText().toString();
            String fechaSalidaStr = mFechaSalidaText.getText().toString();
            String precioTotalStr = mPrecioTotalText.getText().toString();

            // Validar campos obligatorios
            if (TextUtils.isEmpty(nombreCliente)) {
                showToast(R.string.empty_not_saved_nombreCliente);
                return;
            }
            if (TextUtils.isEmpty(telefonoStr)) {
                showToast(R.string.empty_not_saved_telefono);
                return;
            }
            if (TextUtils.isEmpty(fechaEntradaStr)) {
                showToast(R.string.empty_not_saved_fechaEntrada);
                return;
            }
            if (TextUtils.isEmpty(fechaSalidaStr)) {
                showToast(R.string.empty_not_saved_fechaSalida);
                return;
            }

            try {
                // Convertir datos
                int telefono = Integer.parseInt(telefonoStr);
                Date fechaEntrada = dateFormat.parse(fechaEntradaStr);
                Date fechaSalida = dateFormat.parse(fechaSalidaStr);
                double precioTotal = Double.parseDouble(precioTotalStr);

                // Validar la lógica de las fechas
                assert fechaSalida != null;
                if (fechaSalida.before(fechaEntrada)) {
                    showToast(R.string.invalid_date_logic);
                    return;
                }

                if (!telefonoStr.matches("\\d{9,15}")) {
                    showToast(R.string.invalid_phone_number);
                    return;
                }


                // Crear Intent con los datos
                Intent replyIntent = new Intent();
                replyIntent.putExtra(RESERVA_NOMBRE_CLIENTE, nombreCliente);
                replyIntent.putExtra(RESERVA_TELEFONO, telefono);
                assert fechaEntrada != null;
                replyIntent.putExtra(RESERVA_FECHA_ENTRADA, fechaEntrada.getTime());
                replyIntent.putExtra(RESERVA_FECHA_SALIDA, fechaSalida.getTime());
                replyIntent.putExtra(RESERVA_PRECIO_TOTAL, precioTotal);

                if (mRowId != null) {
                    replyIntent.putExtra(RESERVA_ID, mRowId);
                }

                // Guardar parcelas reservadas en el ViewModel
                mReservaViewModel.saveParcelasReservadas(mAdapter.getCurrentList());

                setResult(RESULT_OK, replyIntent);
                finish();
            } catch (ParseException e) {
                // Manejo del error de conversión de fechas
                showToast(R.string.invalid_date_format);
            }
        });

        // Rellenar campos si es edición
        populateFields();
    }

    /**
     * Muestra un mensaje de error en un Toast.
     *
     * @param resId ID del recurso de cadena a mostrar.
     */
    private void showToast(int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
    }


    private void openAddParcelaDialog() {
        // Diálogo personalizado para seleccionar parcela y número de ocupantes
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Parcela");

        // Inflar el diseño del diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_parcela, null);
        builder.setView(dialogView);

        Spinner parcelaSpinner = dialogView.findViewById(R.id.spinner_parcela);
        EditText numeroOcupantesText = dialogView.findViewById(R.id.numero_ocupantes);

        // Configurar el Spinner con la lista de parcelas
        List<Parcela> parcelas = mReservaViewModel.getAllParcelasSync();
        ArrayAdapter<Parcela> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, parcelas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcelaSpinner.setAdapter(adapter);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            Parcela selectedParcela = (Parcela) parcelaSpinner.getSelectedItem();
            String numeroOcupantesStr = numeroOcupantesText.getText().toString();

            if (TextUtils.isEmpty(numeroOcupantesStr)) {
                Toast.makeText(this, "Número de ocupantes no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            int numeroOcupantes = Integer.parseInt(numeroOcupantesStr);
            if (numeroOcupantes <= 0 || numeroOcupantes > selectedParcela.getMaxOcupantes()) {
                Toast.makeText(this, "Número de ocupantes no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            ParcelaReservada nuevaParcela = new ParcelaReservada(0, selectedParcela.getId(), numeroOcupantes);
            mParcelasReservadasTemp.add(nuevaParcela);
            mAdapter.notifyDataSetChanged();
            updatePrecioTotal();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }


    private void saveReserva() {
        String nombreCliente = mNombreClienteText.getText().toString();
        String telefonoStr = mTelefonoText.getText().toString();
        String fechaEntradaStr = mFechaEntradaText.getText().toString();
        String fechaSalidaStr = mFechaSalidaText.getText().toString();

        // Validaciones
        if (TextUtils.isEmpty(nombreCliente) || TextUtils.isEmpty(telefonoStr)
                || TextUtils.isEmpty(fechaEntradaStr) || TextUtils.isEmpty(fechaSalidaStr)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int telefono = Integer.parseInt(telefonoStr);
            Date fechaEntrada = dateFormat.parse(fechaEntradaStr);
            Date fechaSalida = dateFormat.parse(fechaSalidaStr);

            assert fechaSalida != null;
            if (fechaSalida.before(fechaEntrada)) {
                Toast.makeText(this, "La fecha de salida debe ser posterior a la de entrada", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mParcelasReservadasTemp.isEmpty()) {
                Toast.makeText(this, "Debe haber al menos una parcela en la reserva", Toast.LENGTH_SHORT).show();
                return;
            }

            assert fechaEntrada != null;
            Reserva reserva = new Reserva(nombreCliente, telefono, fechaEntrada, fechaSalida, calcularPrecioTotal());

            if (mRowId != null) {
                reserva.setId(mRowId);
                mReservaViewModel.update(reserva);
            } else {
                long newReservaId = mReservaViewModel.insert(reserva);
                for (ParcelaReservada parcela : mParcelasReservadasTemp) {
                    parcela.setReservaId((int) newReservaId);
                    mReservaViewModel.insertParcelaReservada(parcela);
                }
            }

            setResult(RESULT_OK);
            finish();

        } catch (ParseException e) {
            Toast.makeText(this, "Formato de fecha no válido", Toast.LENGTH_SHORT).show();
        }
    }


    private void onParcelaReservadaEdited(ParcelaReservada parcela) {
        openEditParcelaDialog(parcela);
    }


    private void onParcelaReservadaDeleted(ParcelaReservada parcela) {
        mParcelasReservadasTemp.remove(parcela);
        mAdapter.notifyDataSetChanged();
        updatePrecioTotal();
    }


    private void updatePrecioTotal() {
        double total = calcularPrecioTotal();
        mPrecioTotalText.setText(String.valueOf(total));
    }

    private double calcularPrecioTotal() {
        double total = 0;
        for (ParcelaReservada parcela : mParcelasReservadasTemp) {
            Parcela parcelaDetails = mReservaViewModel.getParcelaById(parcela.getParcelaId());
            total += parcelaDetails.getPrecioXpersona() * parcela.getNumeroOcupantes();
        }
        return total;
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
            mFechaEntradaText.setText(dateFormat.format(new Date(extras.getLong(RESERVA_FECHA_ENTRADA))));
            mFechaSalidaText.setText(dateFormat.format(new Date(extras.getLong(RESERVA_FECHA_SALIDA))));
            mPrecioTotalText.setText(String.valueOf(extras.getDouble(RESERVA_PRECIO_TOTAL, 0.0)));
            mRowId = extras.getInt(RESERVA_ID, -1);

            // Cargar parcelas reservadas
            mParcelasReservadasTemp = mReservaViewModel.getParcelasReservadasByReservaId(mRowId);
            mAdapter.updateData(mParcelasReservadasTemp);
            updatePrecioTotal();
        }
    }

}
