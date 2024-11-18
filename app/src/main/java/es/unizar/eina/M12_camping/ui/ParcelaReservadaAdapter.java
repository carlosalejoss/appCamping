package es.unizar.eina.M12_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.ParcelaReservada;

/**
 * Adaptador para mostrar una lista de ParcelasReservadas en un RecyclerView.
 */
public class ParcelaReservadaAdapter extends RecyclerView.Adapter<ParcelaReservadaAdapter.ParcelaReservadaViewHolder> {

    private List<ParcelaReservada> mParcelasReservadas;
    private final OnEditParcelaListener onEditParcelaListener;
    private final OnDeleteParcelaListener onDeleteParcelaListener;

    /**
     * Constructor para inicializar los listeners.
     *
     * @param onEditParcelaListener Listener para editar una parcela reservada.
     * @param onDeleteParcelaListener Listener para eliminar una parcela reservada.
     */
    public ParcelaReservadaAdapter(OnEditParcelaListener onEditParcelaListener, OnDeleteParcelaListener onDeleteParcelaListener) {
        this.onEditParcelaListener = onEditParcelaListener;
        this.onDeleteParcelaListener = onDeleteParcelaListener;
    }

    @NonNull
    @Override
    public ParcelaReservadaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_parcela_reservada, parent, false);
        return new ParcelaReservadaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelaReservadaViewHolder holder, int position) {
        if (mParcelasReservadas != null) {
            ParcelaReservada current = mParcelasReservadas.get(position);
            holder.parcelaNombre.setText("Parcela ID: " + current.getParcelaId());
            holder.numeroOcupantes.setText("Ocupantes: " + current.getNumeroOcupantes());

            // Configurar los listeners para editar y eliminar
            holder.editButton.setOnClickListener(v -> onEditParcelaListener.onEdit(current));
            holder.deleteButton.setOnClickListener(v -> onDeleteParcelaListener.onDelete(current));
        }
    }

    @Override
    public int getItemCount() {
        if (mParcelasReservadas != null) {
            return mParcelasReservadas.size();
        } else {
            return 0;
        }
    }

    /**
     * Configura la lista de parcelas reservadas a mostrar.
     *
     * @param parcelasReservadas Lista de ParcelasReservadas.
     */
    public void setParcelasReservadas(List<ParcelaReservada> parcelasReservadas) {
        mParcelasReservadas = parcelasReservadas;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para representar una ParcelaReservada en el RecyclerView.
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
     * Interfaz para manejar la edición de ParcelasReservadas.
     */
    public interface OnEditParcelaListener {
        void onEdit(ParcelaReservada parcelaReservada);
    }

    /**
     * Interfaz para manejar la eliminación de ParcelasReservadas.
     */
    public interface OnDeleteParcelaListener {
        void onDelete(ParcelaReservada parcelaReservada);
    }
}