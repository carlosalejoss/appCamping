package es.unizar.eina.M12_camping.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import es.unizar.eina.M12_camping.R;
import es.unizar.eina.M12_camping.database.ParcelaReservada;

/**
 * Adaptador para mostrar una lista de parcelas reservadas en un RecyclerView.
 * Permite la edición y eliminación de parcelas reservadas.
 */
public class ParcelaReservadaAdapter extends RecyclerView.Adapter<ParcelaReservadaAdapter.ParcelaReservadaViewHolder> {

    private List<ParcelaReservada> mParcelasReservadas;
    private final OnParcelaReservadaEditListener onEditListener;
    private final OnParcelaReservadaDeleteListener onDeleteListener;

    /**
     * Constructor para inicializar el adaptador.
     *
     * @param parcelasReservadas Lista de parcelas reservadas.
     * @param editListener       Listener para manejar la edición.
     * @param deleteListener     Listener para manejar la eliminación.
     */
    public ParcelaReservadaAdapter(List<ParcelaReservada> parcelasReservadas,
                                   OnParcelaReservadaEditListener editListener,
                                   OnParcelaReservadaDeleteListener deleteListener) {
        this.mParcelasReservadas = parcelasReservadas;
        this.onEditListener = editListener;
        this.onDeleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ParcelaReservadaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_parcela_reservada, parent, false);
        return new ParcelaReservadaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelaReservadaViewHolder holder, int position) {
        ParcelaReservada parcelaReservada = mParcelasReservadas.get(position);
        //String nombreParcela = idToNombreMap.getOrDefault(parcelaReservada.getParcelaId(), "Parcela desconocida");
        //holder.parcelaNombre.setText(nombreParcela);
        holder.numeroOcupantes.setText(String.valueOf(parcelaReservada.getNumeroOcupantes()));

        holder.editButton.setOnClickListener(v -> onEditListener.onEdit(parcelaReservada));
        holder.deleteButton.setOnClickListener(v -> onDeleteListener.onDelete(parcelaReservada));
    }

    @Override
    public int getItemCount() {
        return mParcelasReservadas.size();
    }

    /**
     * Actualiza la lista de parcelas reservadas y notifica cambios al adaptador.
     *
     * @param nuevasParcelas Nueva lista de parcelas reservadas.
     */
    public void updateData(List<ParcelaReservada> nuevasParcelas) {
        this.mParcelasReservadas = nuevasParcelas;
        notifyDataSetChanged();
    }

    /**
     * Actualiza la lista de parcelas reservadas en el adaptador.
     *
     * @param parcelasReservadas La nueva lista de parcelas reservadas.
     */
    public void setParcelasReservadas(List<ParcelaReservada> parcelasReservadas) {
        this.mParcelasReservadas.clear(); // Limpia la lista actual
        this.mParcelasReservadas.addAll(parcelasReservadas); // Añade los nuevos elementos
        notifyDataSetChanged(); // Notifica al adaptador de los cambios
    }

    /**
     * ViewHolder para gestionar la visualización de cada ítem de parcela reservada.
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
     * Interfaz para manejar la edición de una parcela reservada.
     */
    public interface OnParcelaReservadaEditListener {
        void onEdit(ParcelaReservada parcelaReservada);
    }

    /**
     * Interfaz para manejar la eliminación de una parcela reservada.
     */
    public interface OnParcelaReservadaDeleteListener {
        void onDelete(ParcelaReservada parcelaReservada);
    }
}
