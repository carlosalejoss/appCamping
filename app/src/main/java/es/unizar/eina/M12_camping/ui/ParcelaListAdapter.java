package es.unizar.eina.M12_camping.ui;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.M12_camping.database.Parcela;

/**
 * Adaptador para la lista de parcelas, utilizado en el RecyclerView.
 * Extiende ListAdapter para optimizar las actualizaciones en la lista.
 * Utiliza un ParcelaViewHolder para mostrar cada elemento de la lista.
 */
public class ParcelaListAdapter extends ListAdapter<Parcela, ParcelaViewHolder> {
    private int position;

    /**
     * Constructor de ParcelaListAdapter.
     * Recibe un callback de DiffUtil para comparar los elementos de la lista.
     *
     * @param diffCallback El callback utilizado para calcular diferencias en la lista.
     */
    public ParcelaListAdapter(@NonNull DiffUtil.ItemCallback<Parcela> diffCallback) {
        super(diffCallback);
    }

    /**
     * Obtiene la posicion actual seleccionada en la lista.
     *
     * @return La posicion del elemento actual.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Establece la posicion actual seleccionada en la lista.
     *
     * @param position La posicion del elemento actual.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Crea un nuevo ParcelaViewHolder para representar un elemento en la lista.
     *
     * @param parent El ViewGroup al que se añadira el nuevo View.
     * @param viewType El tipo de vista de la nueva vista.
     * @return Un ParcelaViewHolder que contiene la vista para un elemento de la lista.
     */
    @Override
    public ParcelaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ParcelaViewHolder.create(parent);
    }

    /**
     * Obtiene el elemento actual en la posicion seleccionada.
     *
     * @return La parcela seleccionada.
     */
    public Parcela getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Vincula un ParcelaViewHolder con los datos de una parcela especifica.
     *
     * @param holder El ParcelaViewHolder que debe ser actualizado con los datos de la parcela.
     * @param position La posicion de la parcela en la lista.
     */
    @Override
    public void onBindViewHolder(ParcelaViewHolder holder, int position) {
        Parcela current = getItem(position);
        holder.bind(current.getNombre());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    /**
     * Clase ParcelaDiff utilizada para calcular las diferencias entre dos elementos de la lista.
     * Esto permite que ListAdapter optimice las actualizaciones en la lista.
     */
    static class ParcelaDiff extends DiffUtil.ItemCallback<Parcela> {

        /**
         * Verifica si dos parcelas representan el mismo elemento por su ID.
         *
         * @param oldItem La parcela anterior.
         * @param newItem La nueva parcela.
         * @return true si las parcelas tienen el mismo ID, false en caso contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Verifica si los contenidos de dos parcelas son los mismos, comparando sus nombres.
         *
         * @param oldItem La parcela anterior.
         * @param newItem La nueva parcela.
         * @return true si los contenidos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Parcela oldItem, @NonNull Parcela newItem) {
            return oldItem.getNombre().equals(newItem.getNombre());
        }
    }

}
