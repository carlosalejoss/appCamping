package es.unizar.eina.M12_camping.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.Parcela;

import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;

import java.util.Objects;

/**
 * Pantalla principal de la aplicación ListadoParcelas.
 * Esta actividad muestra una lista de parcelas y permite realizar operaciones como
 * insertar, editar, eliminar y ordenar las parcelas.
 */
public class ListadoParcelas extends AppCompatActivity {

    private ParcelaViewModel mParcelaViewModel;

    /** Identificadores para los elementos del menú */
    static final int INSERT_ID = Menu.FIRST;
    static final int DELETE_ID = Menu.FIRST + 1;
    static final int EDIT_ID = Menu.FIRST + 2;
    static final int CHANGE_ID = Menu.FIRST + 3;
    static final int ORDER_ID_NOMBRE = Menu.FIRST + 4;
    static final int ORDER_ID_MAXOCUPANTES = Menu.FIRST + 5;
    static final int ORDER_ID_PRECIOXPERSONA = Menu.FIRST + 6;

    RecyclerView mRecyclerView;
    ParcelaListAdapter mAdapter;
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
        mAdapter = new ParcelaListAdapter(new ParcelaListAdapter.ParcelaDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mParcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);

        // Observa los cambios en la lista de parcelas y actualiza el adaptador
        mParcelaViewModel.getAllParcelas().observe(this, parcelas -> mAdapter.submitList(parcelas));

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> createParcela());

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
        menu.add(Menu.NONE, CHANGE_ID, Menu.NONE, R.string.cambiar_a_reservas);
        menu.add(Menu.NONE, ORDER_ID_NOMBRE, Menu.NONE, R.string.ordenar_por_nombre);
        menu.add(Menu.NONE, ORDER_ID_MAXOCUPANTES, Menu.NONE, R.string.ordenar_por_maxocupantes);
        menu.add(Menu.NONE, ORDER_ID_PRECIOXPERSONA, Menu.NONE, R.string.ordenar_por_precioxpersona);
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
                listadoReservas();
                break;
            case ORDER_ID_NOMBRE:
                mParcelaViewModel.getParcelasOrderedNombre().observe(this, parcelas -> mAdapter.submitList(parcelas));
                break;
            case ORDER_ID_MAXOCUPANTES:
                mParcelaViewModel.getParcelasOrderedOcupantes().observe(this, parcelas -> mAdapter.submitList(parcelas));
                break;
            case ORDER_ID_PRECIOXPERSONA:
                mParcelaViewModel.getParcelasOrderedPrecio().observe(this, parcelas -> mAdapter.submitList(parcelas));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void listadoReservas() {
        Intent intent = new Intent(this, ListadoReservas.class); // que redireccione a MenuReservas
        startActivity(intent);
    }

    /**
     * Maneja las selecciones de elementos en el menú contextual.
     *
     * @param item El elemento de menú que fue seleccionado.
     * @return true si el evento fue manejado exitosamente.
     */
    public boolean onContextItemSelected(MenuItem item) {
        es.unizar.eina.M12_camping.database.Parcela current = mAdapter.getCurrent();
        switch (item.getItemId()) {
            case DELETE_ID:
                Toast.makeText(
                        getApplicationContext(),
                        "Deleting " + current.getNombre(),
                        Toast.LENGTH_LONG).show();
                mParcelaViewModel.delete(current);
                return true;
            case EDIT_ID:
                editParcela(current);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Inicia la actividad para crear una nueva parcela.
     */
    private void createParcela() {
        mStartCreateParcela.launch(new Intent(this, ParcelaEdit.class));
    }

    /**
     * Inicia la actividad para editar una parcela existente.
     *
     * @param current La parcela que se desea editar.
     */
    private void editParcela(es.unizar.eina.M12_camping.database.Parcela current) {
        Intent intent = new Intent(this, ParcelaEdit.class);
        intent.putExtra(ParcelaEdit.PARCELA_NOMBRE, current.getNombre());
        intent.putExtra(ParcelaEdit.PARCELA_MAXOCUPANTES, current.getMaxOcupantes());
        intent.putExtra(ParcelaEdit.PARCELA_PRECIOXPERSONA, current.getPrecioXpersona());
        intent.putExtra(ParcelaEdit.PARCELA_DESCRIPCION, current.getDescripcion());
        intent.putExtra(ParcelaEdit.PARCELA_ID, current.getId());
        mStartUpdateParcela.launch(intent);
    }

    /** ActivityResultLauncher para la creación de nuevas parcelas */
    ActivityResultLauncher<Intent> mStartCreateParcela = newActivityResultLauncher(new ExecuteActivityResultParcelas() {
        @Override
        public void process(Bundle extras, es.unizar.eina.M12_camping.database.Parcela parcela) {
            mParcelaViewModel.insert(parcela);
        }
    });

    /** ActivityResultLauncher para la actualización de parcelas existentes */
    ActivityResultLauncher<Intent> mStartUpdateParcela = newActivityResultLauncher(new ExecuteActivityResultParcelas() {
        @Override
        public void process(Bundle extras, es.unizar.eina.M12_camping.database.Parcela parcela) {
            int id = extras.getInt(ParcelaEdit.PARCELA_ID);
            parcela.setId(id);
            mParcelaViewModel.update(parcela);
        }
    });

    /**
     * Crea un ActivityResultLauncher para manejar los resultados de las actividades de creación y edición de parcelas.
     *
     * @param executable La interfaz que define la acción a ejecutar al recibir el resultado.
     * @return Un ActivityResultLauncher configurado para manejar los resultados de actividades.
     */
    ActivityResultLauncher<Intent> newActivityResultLauncher(ExecuteActivityResultParcelas executable) {
        return registerForActivityResult(
                new StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Bundle extras = result.getData().getExtras();
                        assert extras != null;
                        Parcela parcela = new es.unizar.eina.M12_camping.database.Parcela(
                                Objects.requireNonNull(extras.getString(ParcelaEdit.PARCELA_NOMBRE)),
                                extras.getInt(ParcelaEdit.PARCELA_MAXOCUPANTES),
                                extras.getDouble(ParcelaEdit.PARCELA_PRECIOXPERSONA),
                                extras.getString(ParcelaEdit.PARCELA_DESCRIPCION));
                        executable.process(extras, parcela);
                    }
                });
    }
}

/**
 * Interfaz funcional para definir el procesamiento de los resultados de actividades.
 */
interface ExecuteActivityResultParcelas {
    /**
     * Método que se ejecuta al recibir el resultado de una actividad.
     *
     * @param extras  Los datos adicionales de la actividad.
     * @param parcela La parcela creada o editada.
     */
    void process(Bundle extras, Parcela parcela);
}