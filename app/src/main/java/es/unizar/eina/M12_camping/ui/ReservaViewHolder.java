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
 * ViewHolder personalizado para representar un elemento de Reserva en el RecyclerView.
 * Implementa OnCreateContextMenuListener para añadir opciones de edición y eliminación en el menú contextual.
 */
class ReservaViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    private final TextView mReservaItemView;

    /**
     * Constructor privado para inicializar la vista de la parcela y configurar el listener del menú contextual.
     *
     * @param itemView La vista correspondiente a un elemento de Reserva en el RecyclerView.
     */
    private ReservaViewHolder(View itemView) {
        super(itemView);
        mReservaItemView = itemView.findViewById(R.id.textView);

        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Vincula el texto proporcionado al TextView de la vista de la parcela.
     *
     * @param text El texto que representa el nombre o descripción de la parcela.
     */
    public void bind(String text) {
        mReservaItemView.setText(text);
    }

    /**
     * Crea una nueva instancia de ReservaViewHolder.
     *
     * @param parent El ViewGroup padre al que se añadirá la vista del ViewHolder.
     * @return Una nueva instancia de ReservaViewHolder con la vista inflada.
     */
    static ReservaViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ReservaViewHolder(view);
    }

    /**
     * Crea un menú contextual con opciones para editar y eliminar la parcela.
     *
     * @param menu El menú contextual que se va a construir.
     * @param v La vista sobre la que se muestra el menú.
     * @param menuInfo Información adicional sobre el contexto del menú.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, ListadoParcelas.EDIT_ID, Menu.NONE, R.string.edit_reserva);
        menu.add(Menu.NONE, ListadoParcelas.DELETE_ID, Menu.NONE, R.string.delete_reserva);
    }
}
