package es.unizar.eina.M12_camping.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.ParcelaReservada;

/**
 * Adaptador para mostrar una lista de parcelas reservadas en un RecyclerView.
 * Permite la edicion y eliminacion de parcelas reservadas.
 */
public class ParcelaReservadaAdapter extends RecyclerView.Adapter<ParcelaReservadaAdapter.ParcelaReservadaViewHolder> {

    private List<ParcelaReservada> mParcelasReservadas;
    private final OnParcelaReservadaEditListener onEditListener;
    private final OnParcelaReservadaDeleteListener onDeleteListener;
    private final ReservaViewModel mReservaViewModel;

    /**
     * Constructor para inicializar el adaptador.
     *
     * @param parcelasReservadas Lista de parcelas reservadas.
     * @param editListener       Listener para manejar la edicion.
     * @param deleteListener     Listener para manejar la eliminacion.
     */
    public ParcelaReservadaAdapter(List<ParcelaReservada> parcelasReservadas,
                                   OnParcelaReservadaEditListener editListener,
                                   OnParcelaReservadaDeleteListener deleteListener,
                                   ReservaViewModel mReservaViewModel) {
        this.mParcelasReservadas = parcelasReservadas;
        this.onEditListener = editListener;
        this.onDeleteListener = deleteListener;
        this.mReservaViewModel = mReservaViewModel;
    }

    /**
     * Crea un nuevo ParcelaReservadaViewHolder para representar un elemento en la lista.
     *
     * @param parent   El ViewGroup al que se añadira el nuevo View.
     * @param viewType El tipo de vista de la nueva vista.
     * @return Un ParcelaReservadaViewHolder que contiene la vista para un elemento de la lista.
     */
    @NonNull
    @Override
    public ParcelaReservadaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_parcela_reservada, parent, false);
        return new ParcelaReservadaViewHolder(view);
    }

    /**
     * Vincula los datos de una parcela reservada con un ViewHolder.
     *
     * @param holder   El ViewHolder que representa un elemento de la lista.
     * @param position La posicion del elemento en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ParcelaReservadaViewHolder holder, int position) {
        ParcelaReservada parcelaReservada = mParcelasReservadas.get(position);
        Log.d("Comprobaciones", "onBindViewHolder: parcelaReservada = " + parcelaReservada);

        String nombreParcela = mReservaViewModel.getNombreParcelaById(parcelaReservada.getParcelaId());
        if (nombreParcela == null) {
            Log.d("Comprobaciones", "onBindViewHolder: No se encontro nombre para parcelaId = " + parcelaReservada.getParcelaId());
            holder.parcelaNombre.setText("Parcela desconocida");
        } else {
            Log.d("Comprobaciones", "onBindViewHolder: Nombre de parcela = " + nombreParcela);
            holder.parcelaNombre.setText(nombreParcela);
        }
        holder.numeroOcupantes.setText(String.valueOf(parcelaReservada.getNumeroOcupantes()));

        // Listener para el boton de editar
        holder.editButton.setOnClickListener(v -> onEditListener.onEdit(parcelaReservada));

        // Listener para el boton de eliminar
        holder.deleteButton.setOnClickListener(v -> {
            onDeleteListener.onDelete(parcelaReservada); // Llama al listener definido en ReservaEdit
            mParcelasReservadas.remove(position); // Elimina localmente la parcela
            notifyItemRemoved(position); // Notifica al adaptador que el elemento fue eliminado
        });

    }

    /**
     * Obtiene el numero total de elementos en la lista de parcelas reservadas.
     *
     * @return El numero total de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return mParcelasReservadas.size();
    }

    /**
     * Actualiza la lista de parcelas reservadas en el adaptador.
     *
     * @param parcelasReservadas La nueva lista de parcelas reservadas.
     */
    public void setParcelasReservadas(List<ParcelaReservada> parcelasReservadas) {
        this.mParcelasReservadas = new ArrayList<>(parcelasReservadas);
        notifyDataSetChanged(); // Notifica al adaptador de los cambios
    }

    /**
     * ViewHolder para gestionar la visualizacion de cada item de parcela reservada.
     */
    static class ParcelaReservadaViewHolder extends RecyclerView.ViewHolder {
        private final TextView parcelaNombre;
        private final TextView numeroOcupantes;
        private final Button editButton;
        private final Button deleteButton;

        public ParcelaReservadaViewHolder(@NonNull View itemView) {
            super(itemView);
            parcelaNombre = itemView.findViewById(R.id.parcela_nombre);
            numeroOcupantes = itemView.findViewById(R.id.numero_ocupantes);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    /**
     * Interfaz para manejar la edicion de una parcela reservada.
     */
    public interface OnParcelaReservadaEditListener {
        void onEdit(ParcelaReservada parcelaReservada);
    }

    /**
     * Interfaz para manejar la eliminacion de una parcela reservada.
     */
    public interface OnParcelaReservadaDeleteListener {
        void onDelete(ParcelaReservada parcelaReservada);
    }

}
