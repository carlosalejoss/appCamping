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
     * Obtiene la posición actual seleccionada en la lista.
     *
     * @return La posición del elemento actual.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Establece la posición actual seleccionada en la lista.
     *
     * @param position La posición del elemento actual.
     */
    public void setPosition(int position) {
        this.position = position;
    }

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
     * Crea un nuevo ReservaViewHolder para representar un elemento en la lista.
     *
     * @param parent El ViewGroup al que se añadirá el nuevo View.
     * @param viewType El tipo de vista de la nueva vista.
     * @return Un ReservaViewHolder que contiene la vista para un elemento de la lista.
     */
    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ReservaViewHolder.create(parent);
    }

    /**
     * Obtiene el elemento actual en la posición seleccionada.
     *
     * @return La reserva seleccionada.
     */
    public Reserva getCurrent() {
        return getItem(getPosition());
    }

    /**
     * Vincula un ReservaViewHolder con los datos de una reserva específica.
     *
     * @param holder El ReservaViewHolder que debe ser actualizado con los datos de la reserva.
     * @param position La posición de la reserva en la lista.
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
