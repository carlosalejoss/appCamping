package es.unizar.eina.M12_camping.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
                this::onParcelaReservadaDeleted,
                mReservaViewModel);
        mRecyclerViewParcelas.setAdapter(mAdapter);
        mRecyclerViewParcelas.setLayoutManager(new LinearLayoutManager(this));

        // Configurar observador único para las parcelas disponibles
        mReservaViewModel.getAllParcelas().observe(this, parcelas -> {
            if (parcelas != null) {
                actualizarSpinnerParcelasDisponibles(parcelas);
            }
        });

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
        if (mRowId == null) {
            // Crear una reserva provisional
            String nombreCliente = mNombreClienteText.getText().toString();
            String telefonoStr = mTelefonoText.getText().toString();
            String fechaEntradaStr = mFechaEntradaText.getText().toString();
            String fechaSalidaStr = mFechaSalidaText.getText().toString();

            // Validaciones básicas antes de guardar la reserva
            if (TextUtils.isEmpty(nombreCliente)) {
                Toast.makeText(this, R.string.empty_not_saved_nombre, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(telefonoStr)) {
                Toast.makeText(this, R.string.empty_not_saved_telefono, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(fechaEntradaStr)) {
                Toast.makeText(this, R.string.empty_not_saved_fechaEntrada, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(fechaSalidaStr)) {
                Toast.makeText(this, R.string.empty_not_saved_fechaSalida, Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int telefono = Integer.parseInt(telefonoStr);
                Date fechaEntrada = dateFormat.parse(fechaEntradaStr);
                Date fechaSalida = dateFormat.parse(fechaSalidaStr);

                if (fechaEntrada == null || fechaSalida == null || fechaSalida.before(fechaEntrada)) {
                    Toast.makeText(this, R.string.invalid_date_logic, Toast.LENGTH_SHORT).show();
                    return;
                }

                double precioTotal = calculatePrecioTotal();
                Reserva nuevaReserva = new Reserva(nombreCliente, telefono, fechaEntrada, fechaSalida, precioTotal);

                mReservaViewModel.insert(nuevaReserva); // Insertar reserva provisional
                mReservaViewModel.getInsertResult().observe(this, reservaId -> {
                    if (reservaId != -1) {
                        mRowId = reservaId.intValue();
                        Log.d("Comprobaciones", "Reserva provisional creada con ID: " + mRowId);
                    } else {
                        Toast.makeText(this, "Error al crear reserva provisional", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (ParseException | NumberFormatException e) {
                Toast.makeText(this, R.string.invalid_data, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Log.d("Comprobaciones", "openAddParcelaDialog mRowId actual: " + mRowId);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_parcela));
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_parcela, null);
        builder.setView(dialogView);

        Spinner parcelaSpinner = dialogView.findViewById(R.id.spinner_parcela);
        EditText numeroOcupantesText = dialogView.findViewById(R.id.numero_ocupantes);

        // Filtrar parcelas disponibles (excluir las ya añadidas)
        mReservaViewModel.getAllParcelas().observe(this, parcelas -> {
            if (parcelas != null) {
                List<Parcela> disponibles = filtrarParcelasDisponibles(parcelas);
                // Configurar el Spinner
                ParcelaSpinnerAdapter spinnerAdapter = new ParcelaSpinnerAdapter(this, disponibles);
                parcelaSpinner.setAdapter(spinnerAdapter);
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

            ParcelaReservada nuevaParcela = new ParcelaReservada(mRowId, selectedParcela.getId(), numeroOcupantes);
            mReservaViewModel.insertParcelaReservada(nuevaParcela);
            Log.d("Comprobaciones", "Insertada parcela reservada: " + nuevaParcela);

            mReservaViewModel.getParcelasReservadasByReservaId(mRowId).observe(this, parcelasReservadas -> {
                if (parcelasReservadas != null) {
                    mParcelasReservadasTemp.clear();
                    mParcelasReservadasTemp.addAll(parcelasReservadas);
                    mAdapter.setParcelasReservadas(mParcelasReservadasTemp); // Actualizar la lista en el adaptador
                    mAdapter.notifyDataSetChanged(); // Notificar los cambios al adaptador
                    updatePrecioTotal(); // Recalcular el precio total basado en las parcelas actuales
                }
            });

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_ocupantes)); // Asegúrate de tener esta cadena en strings.xml
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_ocupantes, null); // Diseña un layout simple
        builder.setView(dialogView);

        EditText numeroOcupantesText = dialogView.findViewById(R.id.numero_ocupantes);

        // Prellenar el campo con el número actual de ocupantes
        numeroOcupantesText.setText(String.valueOf(parcelaReservada.getNumeroOcupantes()));

        builder.setPositiveButton(getString(R.string.button_save), (dialog, which) -> {
            String numeroOcupantesStr = numeroOcupantesText.getText().toString();

            if (TextUtils.isEmpty(numeroOcupantesStr)) {
                Toast.makeText(this, R.string.empty_not_saved_ocupantes, Toast.LENGTH_SHORT).show();
                return;
            }

            int numeroOcupantes = Integer.parseInt(numeroOcupantesStr);

            // Validar que el número de ocupantes sea válido
            Parcela parcela = mReservaViewModel.getParcelaById(parcelaReservada.getParcelaId());
            if (numeroOcupantes <= 0 || numeroOcupantes > parcela.getMaxOcupantes()) {
                Toast.makeText(this, R.string.invalid_max_ocupantes, Toast.LENGTH_SHORT).show();
                return;
            }

            // Actualizar el número de ocupantes
            parcelaReservada.setNumeroOcupantes(numeroOcupantes);
            mReservaViewModel.updateParcelaReservada(parcelaReservada); // Asegúrate de tener este método en el ViewModel

            mAdapter.notifyDataSetChanged(); // Reflejar los cambios en el RecyclerView
            updatePrecioTotal(); // Recalcular el precio total
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
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

        // Eliminar la parcela reservada de la base de datos si tiene un ID válido
        if (parcelaReservada.getId() > 0) {
            mReservaViewModel.deleteParcelaReservada(parcelaReservada);
        }

        Log.d("Comprobaciones", "Parcela eliminada y restaurada al listado: " + parcelaReservada.getParcelaId());
    }


    private List<Parcela> filtrarParcelasDisponibles(List<Parcela> todasParcelas) {
        List<Parcela> parcelasDisponibles = new ArrayList<>();
        for (Parcela parcela : todasParcelas) {
            boolean added = false;
            for (ParcelaReservada parcelaReservada : mParcelasReservadasTemp) {
                if (parcela.getId() == parcelaReservada.getParcelaId()) {
                    added = true;
                    break;
                }
            }
            if (!added) {
                parcelasDisponibles.add(parcela);
            }
        }
        return parcelasDisponibles;
    }

    private void actualizarSpinnerParcelasDisponibles(List<Parcela> parcelas) {
        // Filtrar parcelas disponibles (excluir las ya añadidas)
        List<Parcela> disponibles = filtrarParcelasDisponibles(parcelas);

        // Actualizar el adaptador del Spinner en el diálogo de añadir parcela
        if (disponibles.isEmpty()) {
            Log.d("Comprobaciones", "No hay parcelas disponibles.");
        } else {
            Log.d("Comprobaciones", "Parcelas disponibles: " + disponibles.size());
        }
    }


    /**
     * Calcula el precio total basado en las parcelas reservadas.
     */
    private double calculatePrecioTotal() {
        double total = 0;

        try {
            // Parsear las fechas de entrada y salida
            Date fechaEntrada = dateFormat.parse(mFechaEntradaText.getText().toString());
            Date fechaSalida = dateFormat.parse(mFechaSalidaText.getText().toString());

            if (fechaEntrada != null && fechaSalida != null) {
                // Calcular la diferencia en días
                long diffInMillis = fechaSalida.getTime() - fechaEntrada.getTime();
                int dias = (int) (diffInMillis / (1000 * 60 * 60 * 24));

                if (dias <= 0) {
                    Toast.makeText(this, R.string.invalid_date_logic, Toast.LENGTH_SHORT).show();
                    return 0; // Precio total es 0 si las fechas no son válidas
                }

                // Calcular el precio total en función de días, ocupantes y precio por persona
                for (ParcelaReservada parcelaReservada : mParcelasReservadasTemp) {
                    Parcela parcela = mReservaViewModel.getParcelaById(parcelaReservada.getParcelaId());
                    if (parcela != null) {
                        total += parcela.getPrecioXpersona() * parcelaReservada.getNumeroOcupantes() * dias;
                    } else {
                        Log.e("Error", "No se pudo encontrar la parcela con ID: " + parcelaReservada.getParcelaId());
                    }
                }
            }
        } catch (ParseException e) {
            Toast.makeText(this, R.string.invalid_date_format_2, Toast.LENGTH_SHORT).show();
            return 0; // Precio total es 0 si hay un problema de formato de fecha
        }

        return total;
    }

    /**
     * Calcula y actualiza el precio total de la reserva.
     */
    private void updatePrecioTotal() {
        double total = 0;

        try {
            // Parsear las fechas de entrada y salida
            Date fechaEntrada = dateFormat.parse(mFechaEntradaText.getText().toString());
            Date fechaSalida = dateFormat.parse(mFechaSalidaText.getText().toString());

            if (fechaEntrada != null && fechaSalida != null) {
                // Calcular la diferencia en días
                long diffInMillis = fechaSalida.getTime() - fechaEntrada.getTime();
                int dias = (int) (diffInMillis / (1000 * 60 * 60 * 24));

                if (dias <= 0) {
                    Toast.makeText(this, R.string.invalid_date_logic, Toast.LENGTH_SHORT).show();
                    mPrecioTotalText.setText("0");
                    return; // Precio total es 0 si las fechas no son válidas
                }

                // Calcular el precio total en función de días, ocupantes y precio por persona
                for (ParcelaReservada parcelaReservada : mParcelasReservadasTemp) {
                    Parcela parcela = mReservaViewModel.getParcelaById(parcelaReservada.getParcelaId());
                    if (parcela != null) {
                        total += parcela.getPrecioXpersona() * parcelaReservada.getNumeroOcupantes() * dias;
                    } else {
                        Log.e("Error", "No se pudo encontrar la parcela con ID: " + parcelaReservada.getParcelaId());
                    }
                }
            }
        } catch (ParseException e) {
            Toast.makeText(this, R.string.invalid_date_format_2, Toast.LENGTH_SHORT).show();
            mPrecioTotalText.setText("0");
            return; // Precio total es 0 si hay un problema de formato de fecha
        }

        // Actualizar el TextView con el precio total
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

        if (mParcelasReservadasTemp.isEmpty()) { // Si no hay parcelas asignadas a la reserva
            Toast.makeText(this, R.string.empty_not_saved_parcelas, Toast.LENGTH_SHORT).show();
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

            double precioTotal = calculatePrecioTotal();
            Reserva nuevaReserva = new Reserva(nombreCliente, telefono, fechaEntrada, fechaSalida, precioTotal);
            if (mRowId != null) {
                nuevaReserva.setId(mRowId);
                mReservaViewModel.update(nuevaReserva);
                Log.d("Comprobaciones", "Reserva actualizada con ID: " + mRowId);

                Toast.makeText(this, R.string.reserva_saved_successfully, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                mReservaViewModel.insert(nuevaReserva); // Llama al método insert que usa LiveData
                mReservaViewModel.getInsertResult().observe(this, reservaId -> {
                    if (reservaId == -1) {
                        Toast.makeText(this, "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mRowId = reservaId.intValue();
                    Log.d("Comprobaciones", "Reserva insertada con ID: " + reservaId);

                    // Inserta las parcelas reservadas asociadas
                    for (ParcelaReservada parcelaReservada : mParcelasReservadasTemp) {
                        parcelaReservada.setReservaId(reservaId.intValue());
                        mReservaViewModel.insertParcelaReservada(parcelaReservada);
                    }

                    Toast.makeText(this, R.string.reserva_saved_successfully, Toast.LENGTH_SHORT).show();
                });

                Toast.makeText(this, R.string.reserva_saved_successfully, Toast.LENGTH_SHORT).show();
                finish();
            }

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

        if (mRowId == -1) {
            mRowId = null;
            return;
        } else {
            // Recuperar la reserva del ViewModel
            Log.d("Comprobaciones", "populateFields: mRowId = " + mRowId);
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


            mReservaViewModel.getParcelasReservadasByReservaId(mRowId).observe(this, parcelasReservadas -> {
                if (parcelasReservadas != null) {
                    mParcelasReservadasTemp.clear();
                    mParcelasReservadasTemp.addAll(parcelasReservadas);
                    mAdapter.setParcelasReservadas(mParcelasReservadasTemp); // Actualizar la lista en el adaptador
                    mAdapter.notifyDataSetChanged(); // Notificar los cambios al adaptador
                    updatePrecioTotal(); // Recalcular el precio total basado en las parcelas actuales
                }
            });

        }
    }

}
