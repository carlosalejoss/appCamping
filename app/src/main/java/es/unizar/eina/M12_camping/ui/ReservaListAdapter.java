package es.unizar.eina.M12_camping.ui;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import es.unizar.eina.M12_camping.database.Reserva;

/**
 * Adaptador para la lista de reservas, utilizado en el RecyclerView.
 * Extiende ListAdapter para optimizar las actualizaciones en la lista.
 * Utiliza un ReservaViewHolder para mostrar cada elemento de la lista.
 */
public class ReservaListAdapter extends ListAdapter<Reserva, ReservaViewHolder> {
    private int position;

    /**
     * Constructor de ReservaListAdapter.
     * Recibe un callback de DiffUtil para comparar los elementos de la lista.
     *
     * @param diffCallback El callback utilizado para calcular diferencias en la lista.
     */
    public ReservaListAdapter(@NonNull DiffUtil.ItemCallback<Reserva> diffCallback) {
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
     * Crea un nuevo ReservaViewHolder para representar un elemento en la lista.
     *
     * @param parent El ViewGroup al que se añadira el nuevo View.
     * @param viewType El tipo de vista de la nueva vista.
     * @return Un ReservaViewHolder que contiene la vista para un elemento de la lista.
     */
    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ReservaViewHolder.create(parent);
    }

    /**
     * Obtiene el elemento actual en la posicion seleccionada.
     *
     * @return La reserva seleccionada.
     */
    public Reserva getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Vincula un ReservaViewHolder con los datos de una reserva especifica.
     *
     * @param holder El ReservaViewHolder que debe ser actualizado con los datos de la reserva.
     * @param position La posicion de la reserva en la lista.
     */
    @Override
    public void onBindViewHolder(ReservaViewHolder holder, int position) {
        Reserva current = getItem(position);
        holder.bind(current.getNombreCliente());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    /**
     * Clase ReservaDiff utilizada para calcular las diferencias entre dos elementos de la lista.
     * Esto permite que ListAdapter optimice las actualizaciones en la lista.
     */
    static class ReservaDiff extends DiffUtil.ItemCallback<Reserva> {

        /**
         * Verifica si dos reservas representan el mismo elemento por su ID.
         *
         * @param oldItem La reserva anterior.
         * @param newItem La nueva reserva.
         * @return true si las reservas tienen el mismo ID, false en caso contrario.
         */
        @Override
        public boolean areItemsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Verifica si los contenidos de dos reservas son los mismos, comparando sus nombres.
         *
         * @param oldItem La reserva anterior.
         * @param newItem La nueva reserva.
         * @return true si los contenidos son los mismos, false en caso contrario.
         */
        @Override
        public boolean areContentsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
            return oldItem.getNombreCliente().equals(newItem.getNombreCliente());
        }
    }
}
