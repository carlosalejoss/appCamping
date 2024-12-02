package es.unizar.eina.M12_camping.ui;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.M12_camping.R;

/**
 * ViewHolder personalizado para representar un elemento de Parcela en el RecyclerView.
 * Implementa OnCreateContextMenuListener para añadir opciones de edicion y eliminacion en el menu contextual.
 */
class ParcelaViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    private final TextView mParcelaItemView;

    /**
     * Constructor privado para inicializar la vista de la parcela y configurar el listener del menu contextual.
     *
     * @param itemView La vista correspondiente a un elemento de Parcela en el RecyclerView.
     */
    private ParcelaViewHolder(View itemView) {
        super(itemView);
        mParcelaItemView = itemView.findViewById(R.id.textView);

        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Vincula el texto proporcionado al TextView de la vista de la parcela.
     *
     * @param text El texto que representa el nombre o descripcion de la parcela.
     */
    public void bind(String text) {
        mParcelaItemView.setText(text);
    }

    /**
     * Crea una nueva instancia de ParcelaViewHolder.
     *
     * @param parent El ViewGroup padre al que se añadira la vista del ViewHolder.
     * @return Una nueva instancia de ParcelaViewHolder con la vista inflada.
     */
    static ParcelaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ParcelaViewHolder(view);
    }

    /**
     * Crea un menu contextual con opciones para editar y eliminar la parcela.
     *
     * @param menu El menu contextual que se va a construir.
     * @param v La vista sobre la que se muestra el menu.
     * @param menuInfo Informacion adicional sobre el contexto del menu.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, ListadoParcelas.EDIT_ID, Menu.NONE, R.string.edit_parcela);
        menu.add(Menu.NONE, ListadoParcelas.DELETE_ID, Menu.NONE, R.string.delete_parcela);
    }
}
