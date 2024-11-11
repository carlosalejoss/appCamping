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

import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;

import java.util.Objects;

/** Pantalla principal de la aplicación ListadoParcelas */
public class ListadoParcelas extends AppCompatActivity {
    private ParcelaViewModel mParcelaViewModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);  // Cambiar nombre de pantallas
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ParcelaListAdapter(new ParcelaListAdapter.ParcelaDiff());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mParcelaViewModel = new ViewModelProvider(this).get(ParcelaViewModel.class);

        mParcelaViewModel.getAllParcelas().observe(this, parcelas -> {
            // Update the cached copy of the parcelas in the adapter.
            mAdapter.submitList(parcelas);
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> createParcela());

        // It doesn't affect if we comment the following instruction
        registerForContextMenu(mRecyclerView);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, CHANGE_ID, Menu.NONE, R.string.cambiar_a_reservas);
        menu.add(Menu.NONE, ORDER_ID_NOMBRE, Menu.NONE, R.string.ordenar_por_nombre);
        menu.add(Menu.NONE, ORDER_ID_MAXOCUPANTES, Menu.NONE, R.string.ordenar_por_maxocupantes);
        menu.add(Menu.NONE, ORDER_ID_PRECIOXPERSONA, Menu.NONE, R.string.ordenar_por_precioxpersona);

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CHANGE_ID:

                return true;
            case ORDER_ID_NOMBRE:
                ordenarParcelasNombre();
                return true;
            case ORDER_ID_MAXOCUPANTES:
                ordenarParcelasMaxOcupantes();
                return true;
            case ORDER_ID_PRECIOXPERSONA:
                ordenarParcelasPrecioXpersona();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

    private void createParcela() {
        mStartCreateParcela.launch(new Intent(this, ParcelaEdit.class));
    }

    private void ordenarParcelasNombre() {
        mParcelaViewModel.getParcelasOrderedNombre();
    }

    private void ordenarParcelasMaxOcupantes() {
        mParcelaViewModel.getParcelasOrderedOcupantes();
    }

    private void ordenarParcelasPrecioXpersona() {
        mParcelaViewModel.getParcelasOrderedPrecio();
    }

    ActivityResultLauncher<Intent> mStartCreateParcela = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, es.unizar.eina.M12_camping.database.Parcela parcela) {
            mParcelaViewModel.insert(parcela);
        }
    });

    ActivityResultLauncher<Intent> newActivityResultLauncher(ExecuteActivityResult executable) {
        return registerForActivityResult(
                new StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        assert result.getData() != null;
                        Bundle extras = result.getData().getExtras();
                        assert extras != null;
                        es.unizar.eina.M12_camping.database.Parcela parcela = new es.unizar.eina.M12_camping.database.Parcela(
                                Objects.requireNonNull(extras.getString(ParcelaEdit.PARCELA_NOMBRE)),
                                extras.getInt(ParcelaEdit.PARCELA_MAXOCUPANTES),
                                extras.getDouble(ParcelaEdit.PARCELA_PRECIOXPERSONA),
                                extras.getString(ParcelaEdit.PARCELA_DESCRIPCION));
                        executable.process(extras, parcela);
                    }
                });
    }

    private void editParcela(es.unizar.eina.M12_camping.database.Parcela current) {
        Intent intent = new Intent(this, ParcelaEdit.class);
        intent.putExtra(ParcelaEdit.PARCELA_NOMBRE, current.getNombre());
        intent.putExtra(ParcelaEdit.PARCELA_MAXOCUPANTES, current.getMaxOcupantes());
        intent.putExtra(ParcelaEdit.PARCELA_PRECIOXPERSONA, current.getPrecioXpersona());
        intent.putExtra(ParcelaEdit.PARCELA_DESCRIPCION, current.getDescripcion());
        intent.putExtra(ParcelaEdit.PARCELA_ID, current.getId());
        mStartUpdateParcela.launch(intent);
    }

    ActivityResultLauncher<Intent> mStartUpdateParcela = newActivityResultLauncher(new ExecuteActivityResult() {
        @Override
        public void process(Bundle extras, es.unizar.eina.M12_camping.database.Parcela parcela) {
            int id = extras.getInt(ParcelaEdit.PARCELA_ID);
            parcela.setId(id);
            mParcelaViewModel.update(parcela);
        }
    });

}

interface ExecuteActivityResult {
    void process(Bundle extras, es.unizar.eina.M12_camping.database.Parcela parcela);
}