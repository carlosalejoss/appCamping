package es.unizar.eina.M12_camping.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import es.unizar.eina.M12_camping.database.Parcela;

/**
 * Adaptador personalizado para poblar un Spinner con una lista de parcelas.
 * Muestra el nombre de cada parcela en las opciones del Spinner.
 */
public class ParcelaSpinnerAdapter extends ArrayAdapter<Parcela> {

    /**
     * Constructor para inicializar el adaptador con el contexto y la lista de parcelas.
     *
     * @param context  El contexto actual.
     * @param parcelas La lista de parcelas para mostrar en el Spinner.
     */
    public ParcelaSpinnerAdapter(@NonNull Context context, @NonNull List<Parcela> parcelas) {
        super(context, android.R.layout.simple_spinner_item, parcelas);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    /**
     * Obtiene la vista para el elemento seleccionado del Spinner.
     *
     * @param position    La posicion del elemento en la lista.
     * @param convertView La vista reutilizable.
     * @param parent      El contenedor padre.
     * @return La vista para el elemento seleccionado.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        Parcela parcela = getItem(position);
        if (parcela != null) {
            textView.setText(parcela.getNombre());
        }
        return textView;
    }

    /**
     * Obtiene la vista para los elementos desplegables del Spinner.
     *
     * @param position    La posicion del elemento en la lista.
     * @param convertView La vista reutilizable.
     * @param parent      El contenedor padre.
     * @return La vista para el elemento desplegable.
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        Parcela parcela = getItem(position);
        if (parcela != null) {
            textView.setText(parcela.getNombre());
        }
        return textView;
    }
}