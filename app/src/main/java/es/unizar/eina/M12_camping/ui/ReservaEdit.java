package es.unizar.eina.M12_camping.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.Parcela;
import es.unizar.eina.M12_camping.database.ParcelaReservada;
import es.unizar.eina.M12_camping.database.Reserva;

/**
 * Actividad que permite crear o editar una reserva.
 * Incluye la funcionalidad de agregar, editar y eliminar parcelas reservadas asociadas a la reserva.
 */
public class ReservaEdit extends AppCompatActivity {

    public static final String RESERVA_NOMBRECLIENTE = "nombreCliente";
    public static final String RESERVA_TELEFONO = "numeroMovil";
    public static final String RESERVA_FECHAENTRADA = "fechaEntrada";
    public static final String RESERVA_FECHASALIDA = "fechaSalida";
    public static final String RESERVA_PRECIOTOTAL = "precioTotal";
    public static final String RESERVA_ID = "id";

    private EditText mNombreClienteText;
    private EditText mTelefonoText;
    private EditText mFechaEntradaText;
    private EditText mFechaSalidaText;
    private TextView mPrecioTotalText;
    private Integer mRowId;

    private RecyclerView mRecyclerViewParcelas;
    private ParcelaReservadaAdapter mAdapter;

    private List<ParcelaReservada> mParcelasReservadasTemp = new ArrayList<>();
    private ReservaViewModel mReservaViewModel;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


    /**
     * Método que se llama al crear la actividad.
     * Configura los elementos de la interfaz de usuario y carga los datos existentes si están disponibles.
     *
     * @param savedInstanceState El estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservaedit);

        // Inicializar vistas
        mNombreClienteText = findViewById(R.id.nombreCliente);
        mTelefonoText = findViewById(R.id.telefono);
        mFechaEntradaText = findViewById(R.id.fechaEntrada);
        mFechaSalidaText = findViewById(R.id.fechaSalida);
        mPrecioTotalText = findViewById(R.id.precioTotal);

        mRecyclerViewParcelas = findViewById(R.id.recyclerViewParcelas);
        Button addParcelaButton = findViewById(R.id.button_add_parcela);
        Button saveReservaButton = findViewById(R.id.button_save);

        // Configurar RecyclerView y ViewModel
        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);
        mAdapter = new ParcelaReservadaAdapter(mParcelasReservadasTemp,
                this::onParcelaReservadaEdited,
                this::onParcelaReservadaDeleted);
        mRecyclerViewParcelas.setAdapter(mAdapter);
        mRecyclerViewParcelas.setLayoutManager(new LinearLayoutManager(this));

        // Configurar botón para añadir parcelas
        addParcelaButton.setOnClickListener(view -> openAddParcelaDialog());

        // Configurar botón para guardar reserva
        saveReservaButton.setOnClickListener(view -> saveReserva());

        // Rellenar campos si estamos editando
        populateFields();
    }

    /**
     * Abre un diálogo para añadir una nueva parcela reservada a la reserva.
     */
    private void openAddParcelaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_parcela));
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_parcela, null);
        builder.setView(dialogView);

        Spinner parcelaSpinner = dialogView.findViewById(R.id.spinner_parcela);
        EditText numeroOcupantesText = dialogView.findViewById(R.id.numero_ocupantes);

        // Configurar Spinner con parcelas disponibles
        mReservaViewModel.getAllParcelas().observe(this, parcelas -> {
            if (parcelas != null && !parcelas.isEmpty()) {
                ParcelaSpinnerAdapter spinnerAdapter = new ParcelaSpinnerAdapter(this, parcelas);
                parcelaSpinner.setAdapter(spinnerAdapter);
            } else {
                Toast.makeText(this, R.string.no_parcelas_disponibles, Toast.LENGTH_SHORT).show();
            }
        });


        builder.setPositiveButton(getString(R.string.add_parcela), (dialog, which) -> {
            Parcela selectedParcela = (Parcela) parcelaSpinner.getSelectedItem();
            String numeroOcupantesStr = numeroOcupantesText.getText().toString();

            if (TextUtils.isEmpty(numeroOcupantesStr)) {
                Toast.makeText(this, R.string.empty_not_saved_ocupantes, Toast.LENGTH_SHORT).show();
                return;
            }

            int numeroOcupantes = Integer.parseInt(numeroOcupantesStr);
            if (numeroOcupantes <= 0 || numeroOcupantes > selectedParcela.getMaxOcupantes()) {
                Toast.makeText(this, R.string.invalid_max_ocupantes, Toast.LENGTH_SHORT).show();
                return;
            }

            ParcelaReservada nuevaParcela = new ParcelaReservada(0, selectedParcela.getId(), numeroOcupantes);
            mParcelasReservadasTemp.add(nuevaParcela);
            mAdapter.notifyDataSetChanged();
            updatePrecioTotal();
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    /**
     * Método que se ejecuta cuando una parcela reservada es editada.
     *
     * @param parcelaReservada La parcela reservada que se va a editar.
     */
    private void onParcelaReservadaEdited(ParcelaReservada parcelaReservada) {
        // Lógica para editar parcela reservada
        openEditParcelaDialog(parcelaReservada);
    }

    /**
     * Abre un cuadro de diálogo para editar los datos de una parcela reservada existente.
     *
     * @param parcelaReservada La parcela reservada a editar.
     */
    private void openEditParcelaDialog(ParcelaReservada parcelaReservada) {
        // Crear un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_parcela));

        // Inflar la vista personalizada del diálogo
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_parcela, null);
        builder.setView(dialogView);

        // Referencias a los elementos de la interfaz del diálogo
        Spinner parcelaSpinner = dialogView.findViewById(R.id.spinner_parcela);
        EditText numeroOcupantesText = dialogView.findViewById(R.id.numero_ocupantes);

        // Configurar el Spinner con la lista de parcelas existentes
        List<Parcela> parcelas = mReservaViewModel.getAllParcelasSync(); // Método debe estar definido en el ViewModel
        ArrayAdapter<Parcela> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, parcelas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parcelaSpinner.setAdapter(adapter);

        // Seleccionar la parcela actual en el Spinner
        for (int i = 0; i < parcelas.size(); i++) {
            if (parcelas.get(i).getId() == parcelaReservada.getParcelaId()) {
                parcelaSpinner.setSelection(i);
                break;
            }
        }

        // Configurar el campo de número de ocupantes con el valor actual
        numeroOcupantesText.setText(String.valueOf(parcelaReservada.getNumeroOcupantes()));

        // Configurar los botones del diálogo
        builder.setPositiveButton(getString(R.string.edit_parcela), (dialog, which) -> {
            Parcela selectedParcela = (Parcela) parcelaSpinner.getSelectedItem();
            String numeroOcupantesStr = numeroOcupantesText.getText().toString();

            // Validaciones
            if (TextUtils.isEmpty(numeroOcupantesStr)) {
                Toast.makeText(this, getString(R.string.empty_not_saved_ocupantes), Toast.LENGTH_SHORT).show();
                return;
            }

            int numeroOcupantes = Integer.parseInt(numeroOcupantesStr);
            if (numeroOcupantes <= 0 || numeroOcupantes > selectedParcela.getMaxOcupantes()) {
                Toast.makeText(this, getString(R.string.invalid_max_ocupantes), Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar la parcela reservada
            parcelaReservada.setParcelaId(selectedParcela.getId());
            parcelaReservada.setNumeroOcupantes(numeroOcupantes);
            mAdapter.notifyDataSetChanged(); // Notificar cambios al adaptador
            updatePrecioTotal(); // Actualizar el precio total dinámicamente
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        // Mostrar el cuadro de diálogo
        builder.create().show();
    }

    /**
     * Método que se ejecuta cuando una parcela reservada es eliminada.
     *
     * @param parcelaReservada La parcela reservada que se va a eliminar.
     */
    private void onParcelaReservadaDeleted(ParcelaReservada parcelaReservada) {
        mParcelasReservadasTemp.remove(parcelaReservada);
        mAdapter.notifyDataSetChanged();
        updatePrecioTotal();
    }

    /**
     * Calcula y actualiza el precio total de la reserva.
     */
    private void updatePrecioTotal() {
        double total = 0;
        for (ParcelaReservada parcelaReservada : mParcelasReservadasTemp) {
            Parcela parcela = mReservaViewModel.getParcelaById(parcelaReservada.getParcelaId());
            total += parcela.getPrecioXpersona() * parcelaReservada.getNumeroOcupantes();
        }
        mPrecioTotalText.setText(String.valueOf(total));
    }

    /**
     * Guarda la reserva en la base de datos.
     */
    private void saveReserva() {
        String nombreCliente = mNombreClienteText.getText().toString();
        String telefonoStr = mTelefonoText.getText().toString();
        String fechaEntradaStr = mFechaEntradaText.getText().toString();
        String fechaSalidaStr = mFechaSalidaText.getText().toString();

        // Validaciones
        if (TextUtils.isEmpty(nombreCliente)) {
            Toast.makeText(this, R.string.empty_not_saved_nombreCliente, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(telefonoStr)) {
            Toast.makeText(this, R.string.empty_not_saved_telefono, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!fechaEntradaStr.matches("\\d{2}-\\d{2}-\\d{4}") || !fechaSalidaStr.matches("\\d{2}-\\d{2}-\\d{4}")) {
            Toast.makeText(this, R.string.invalid_date_format_2, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int telefono = Integer.parseInt(telefonoStr);
            Date fechaEntrada = dateFormat.parse(fechaEntradaStr);
            Date fechaSalida = dateFormat.parse(fechaSalidaStr);

            if (fechaEntrada == null || fechaSalida == null) {
                return; // Error ya manejado en parseDate()
            }

            if (fechaSalida.before(fechaEntrada)) {
                Toast.makeText(this, R.string.invalid_date_logic, Toast.LENGTH_SHORT).show();
                return;
            }

            if (!telefonoStr.matches("\\d{9,15}")) {
                Toast.makeText(this, R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
                return;
            }

            Reserva nuevaReserva = new Reserva(nombreCliente, telefono, fechaEntrada, fechaSalida, 0);
            if (mRowId != null) {
                nuevaReserva.setId(mRowId);
                mReservaViewModel.update(nuevaReserva);
            } else {
                long reservaId = mReservaViewModel.insert(nuevaReserva);
                if (reservaId == -1) {
                    Toast.makeText(this, "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (ParcelaReservada parcelaReservada : mParcelasReservadasTemp) {
                    parcelaReservada.setReservaId((int) reservaId);
                    mReservaViewModel.insertParcelaReservada(parcelaReservada);
                }
            }
            Toast.makeText(this, R.string.reserva_saved_successfully, Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Rellena los campos de la reserva si se están editando.
     */
    private void populateFields() {
        // Obtener los datos del Intent
        mRowId = getIntent().getIntExtra(RESERVA_ID, -1);
        if (mRowId != -1) {
            // Recuperar la reserva del ViewModel
            Reserva reserva = mReservaViewModel.getReservaById(mRowId);
            if (reserva == null) {
                Toast.makeText(this, "Reserva no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }

            // Asignar los valores a los campos
            mNombreClienteText.setText(reserva.getNombreCliente());
            mTelefonoText.setText(String.valueOf(reserva.getNumeroMovil()));
            mFechaEntradaText.setText(dateFormat.format(reserva.getFechaEntrada()));
            mFechaSalidaText.setText(dateFormat.format(reserva.getFechaSalida()));
            mPrecioTotalText.setText(String.valueOf(reserva.getPrecioTotal()));

            // Recuperar las parcelas reservadas y actualizar el adaptador
            mParcelasReservadasTemp = mReservaViewModel.getParcelasReservadasByReservaId(mRowId);
            mAdapter.setParcelasReservadas(mParcelasReservadasTemp); // Método explícito para actualizar la lista
        }
    }

}
