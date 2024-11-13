package es.unizar.eina.M12_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.Reserva;

/**
 * Adapter para la lista de reservas que se muestra en la interfaz de usuario.
 * Se encarga de enlazar los datos de las reservas con los elementos visuales del RecyclerView.
 */
public class ReservaListAdapter extends ListAdapter<Reserva, ReservaListAdapter.ReservaViewHolder> {

    /**
     * Constructor que inicializa el ListAdapter utilizando un DiffUtil.ItemCallback personalizado.
     */
    public ReservaListAdapter() {
        super(DIFF_CALLBACK);
    }

    /**
     * Callback para calcular las diferencias entre dos listas de Reservas y optimizar las actualizaciones del RecyclerView.
     */
    private static final DiffUtil.ItemCallback<Reserva> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Reserva>() {
                @Override
                public boolean areItemsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Reserva oldItem, @NonNull Reserva newItem) {
                    return oldItem.getNombreCliente().equals(newItem.getNombreCliente()) &&
                            oldItem.getTelefono().equals(newItem.getTelefono()) &&
                            oldItem.getFechaEntrada().equals(newItem.getFechaEntrada()) &&
                            oldItem.getFechaSalida().equals(newItem.getFechaSalida()) &&
                            oldItem.getPrecioTotal() == newItem.getPrecioTotal();
                }
            };

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reserva_list_item, parent, false);
        return new ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva current = getItem(position);
        holder.nombreClienteItemView.setText(current.getNombreCliente());
        holder.telefonoItemView.setText(String.valueOf(current.getTelefono()));
        holder.fechaEntradaItemView.setText(DateConverter.toString(current.getFechaEntrada()));
        holder.fechaSalidaItemView.setText(DateConverter.toString(current.getFechaSalida()));
        holder.precioTotalItemView.setText(String.valueOf(current.getPrecioTotal()));
    }

    /**
     * ViewHolder para la Reserva, que contiene las vistas para mostrar los detalles de cada reserva.
     */
    class ReservaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreClienteItemView;
        private final TextView telefonoItemView;
        private final TextView fechaEntradaItemView;
        private final TextView fechaSalidaItemView;
        private final TextView precioTotalItemView;

        /**
         * Constructor que inicializa las vistas del ViewHolder.
         *
         * @param itemView La vista que representa un elemento individual de la lista de reservas.
         */
        private ReservaViewHolder(View itemView) {
            super(itemView);
            nombreClienteItemView = itemView.findViewById(R.id.nombreCliente);
            telefonoItemView = itemView.findViewById(R.id.telefono);
            fechaEntradaItemView = itemView.findViewById(R.id.fechaEntrada);
            fechaSalidaItemView = itemView.findViewById(R.id.fechaSalida);
            precioTotalItemView = itemView.findViewById(R.id.precioTotal);
        }
    }
}
