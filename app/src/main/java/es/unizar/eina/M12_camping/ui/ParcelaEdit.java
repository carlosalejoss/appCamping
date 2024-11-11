package es.unizar.eina.M12_camping.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.unizar.eina.M12_camping.R;

/** Pantalla utilizada para la creación o edición de una nota */
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
        setContentView(R.layout.activity_noteedit); // Cambiar nombre de pantalla

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
                replyIntent.putExtra(ParcelaEdit.PARCELA_NOMBRE, mNombreText.getText().toString());
                replyIntent.putExtra(ParcelaEdit.PARCELA_MAXOCUPANTES, mMaxOcupantes.toString());
                replyIntent.putExtra(ParcelaEdit.PARCELA_PRECIOXPERSONA, mPrecioXpersona.toString());
                replyIntent.putExtra(ParcelaEdit.PARCELA_DESCRIPCION, mDescripcionText.getText().toString());
                if (mRowId!=null) {
                    replyIntent.putExtra(ParcelaEdit.PARCELA_ID, mRowId.intValue());
                }
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        populateFields();

    }

    private void populateFields () {
        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mNombreText.setText(extras.getString(ParcelaEdit.PARCELA_NOMBRE));
            mMaxOcupantes.setText(extras.getString(ParcelaEdit.PARCELA_MAXOCUPANTES));
            mPrecioXpersona.setText(extras.getString(ParcelaEdit.PARCELA_PRECIOXPERSONA));
            mDescripcionText.setText(extras.getString(ParcelaEdit.PARCELA_DESCRIPCION));
            mRowId = extras.getInt(ParcelaEdit.PARCELA_ID);
        }
    }

}
