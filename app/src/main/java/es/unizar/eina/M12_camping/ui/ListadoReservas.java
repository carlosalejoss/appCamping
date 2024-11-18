package es.unizar.eina.M12_camping.ui;

import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Objects;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.Reserva;

/**
 * Pantalla principal de la aplicación ListadoReservas.
 * Esta actividad muestra una lista de reservas y permite realizar operaciones como
 * insertar, editar, eliminar y ordenar las reservas.
 */
public class ListadoReservas extends AppCompatActivity {

    private ReservaViewModel mReservaViewModel;

    /** Identificadores para los elementos del menú */
    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;
    static final int CHANGE_ID = Menu.FIRST + 3;
    static final int ORDER_ID_NOMBRECLIENTE = Menu.FIRST + 4;
    static final int ORDER_ID_TELEFONO = Menu.FIRST + 5;
    static final int ORDER_ID_FECHAENTRADA = Menu.FIRST + 6;

    RecyclerView mRecyclerView;
    ReservaListAdapter mAdapter;
    FloatingActionButton mFab;

    /**
     * Método que se llama al crear la actividad.
     * Configura el RecyclerView, el adaptador, el ViewModel y el botón de acción flotante.
     *
     * @param savedInstanceState Estado anterior de la actividad, si se ha guardado.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_parcelas); // Cambiar nombre de pantallas

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ReservaListAdapter(new ReservaListAdapter.ReservaDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReservaViewModel = new ViewModelProvider(this).get(ReservaViewModel.class);

        // Observa los cambios en la lista de reservas y actualiza el adaptador
        mReservaViewModel.getAllReservas().observe(this, reservas -> mAdapter.submitList(reservas));

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> createReserva());

        // Registro para el menú contextual
        registerForContextMenu(mRecyclerView);
    }

    /**
     * Crea el menú de opciones de la actividad.
     *
     * @param menu El menú en el que se agregan las opciones.
     * @return true si el menú fue creado exitosamente.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, CHANGE_ID, Menu.NONE, R.string.cambiar_a_parcelas);
        menu.add(Menu.NONE, ORDER_ID_NOMBRECLIENTE, Menu.NONE, R.string.ordenar_por_nombreCliente);
        menu.add(Menu.NONE, ORDER_ID_TELEFONO, Menu.NONE, R.string.ordenar_por_telefono);
        menu.add(Menu.NONE, ORDER_ID_FECHAENTRADA, Menu.NONE, R.string.ordenar_por_fechaEntrada);
        return result;
    }

    /**
     * Maneja las selecciones de elementos en el menú de opciones.
     *
     * @param item El elemento de menú que fue seleccionado.
     * @return true si el evento fue manejado exitosamente.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CHANGE_ID:
                listadoParcelas();
                break;
            case ORDER_ID_NOMBRECLIENTE:
                mReservaViewModel.getReservasOrderedNombreCliente().observe(this, reservas -> mAdapter.submitList(reservas));
                break;
            case ORDER_ID_TELEFONO:
                mReservaViewModel.getReservasOrderedTelefono().observe(this, reservas -> mAdapter.submitList(reservas));
                break;
            case ORDER_ID_FECHAENTRADA:
                mReservaViewModel.getReservasOrderedFechaEntrada().observe(this, reservas -> mAdapter.submitList(reservas));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Cambia a la pantalla de listado de parcelas.
     */
    private void listadoParcelas() {
        Intent intent = new Intent(this, ListadoParcelas.class); // que redireccione a MenuReservas
        startActivity(intent);
    }

    /**
     * Maneja las selecciones de elementos en el menú contextual.
     *
     * @param item El elemento de menú que fue seleccionado.
     * @return true si el evento fue manejado exitosamente.
     */
    public boolean onContextItemSelected(MenuItem item) {
        Reserva current = mAdapter.getCurrent();
        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(
                        getApplicationContext(),
                        "Deleting " + current.getNombreCliente(),
                        Toast.LENGTH_LONG).show();
                mReservaViewModel.delete(current);
                return true;
            case EDIT_ID:
                editReserva(current);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Inicia la actividad para crear una nueva reserva.
     */
    private void createReserva() {
        mStartCreateReserva.launch(new Intent(this, ReservaEdit.class));
    }

    /**
     * Inicia la actividad para editar una reserva existente.
     *
     * @param current La reserva que se desea editar.
     */
    private void editReserva(Reserva current) {
        Intent intent = new Intent(this, ReservaEdit.class);
        intent.putExtra(ReservaEdit.RESERVA_NOMBRE_CLIENTE, current.getNombreCliente());
        intent.putExtra(ReservaEdit.RESERVA_TELEFONO, current.getNumeroMovil());
        intent.putExtra(ReservaEdit.RESERVA_FECHA_ENTRADA, current.getFechaEntrada().getTime());
        intent.putExtra(ReservaEdit.RESERVA_FECHA_SALIDA, current.getFechaSalida().getTime());
        intent.putExtra(ReservaEdit.RESERVA_ID, current.getId());
        mStartUpdateReserva.launch(intent);
    }

    /**
     * ActivityResultLauncher para la creación de nuevas reservas
     */
    ActivityResultLauncher<Intent> mStartCreateReserva = newActivityResultLauncher(new ExecuteActivityResultReservas() {
        @Override
        public void process(Bundle extras, Reserva reserva) {
            mReservaViewModel.insert(reserva);
        }
    });

    /**
     * ActivityResultLauncher para la actualización de reservas existentes
     */
    ActivityResultLauncher<Intent> mStartUpdateReserva = newActivityResultLauncher(new ExecuteActivityResultReservas() {
        @Override
        public void process(Bundle extras, Reserva reserva) {
            int id = extras.getInt(ReservaEdit.RESERVA_ID);
            reserva.setId(id);
            mReservaViewModel.update(reserva);
        }
    });

    /**
     * Crea un ActivityResultLauncher para manejar los resultados de las actividades de creación y edición de reservas.
     *
     * @param executable La interfaz que define la acción a ejecutar al recibir el resultado.
     * @return Un ActivityResultLauncher configurado para manejar los resultados de actividades.
     */
    ActivityResultLauncher<Intent> newActivityResultLauncher(ExecuteActivityResultReservas executable) {
        return registerForActivityResult(
                new StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Bundle extras = result.getData().getExtras();
                        assert extras != null;
                        Reserva reserva = new Reserva(
                                Objects.requireNonNull(extras.getString(ReservaEdit.RESERVA_NOMBRE_CLIENTE)),
                                extras.getInt(ReservaEdit.RESERVA_TELEFONO),
                                new Date(extras.getLong(ReservaEdit.RESERVA_FECHA_ENTRADA)),
                                new Date(extras.getLong(ReservaEdit.RESERVA_FECHA_SALIDA)),
                                0);
                        executable.process(extras, reserva);
                    }
                });
    }
}

/**
 * Interfaz funcional para definir el procesamiento de los resultados de actividades.
 */
interface ExecuteActivityResultReservas {
    /**
     * Método que se ejecuta al recibir el resultado de una actividad.
     *
     * @param extras  Los datos adicionales de la actividad.
     * @param reserva La reserva creada o editada.
     */
    void process(Bundle extras, Reserva reserva);
}