package es.unizar.eina.M12_camping.database;

import java.util.Objects;

/**
 * Representa una parcela reservada dentro de una reserva.
 * Contiene el ID de la parcela y el número de ocupantes asignados.
 */
public class ParcelaReservada {

    /** ID de la parcela reservada */
    private int idParcela;

    /** Número de ocupantes en la parcela */
    private int numeroOcupantes;

    /**
     * Constructor para crear una nueva instancia de ParcelaReservada.
     *
     * @param idParcela       El ID de la parcela reservada.
     * @param numeroOcupantes El número de ocupantes en la parcela.
     */
    public ParcelaReservada(int idParcela, int numeroOcupantes) {
        this.idParcela = idParcela;
        this.numeroOcupantes = numeroOcupantes;
    }

    /**
     * Obtiene el ID de la parcela reservada.
     *
     * @return El ID de la parcela.
     */
    public int getIdParcela() {
        return idParcela;
    }

    /**
     * Establece el ID de la parcela reservada.
     *
     * @param idParcela El nuevo ID de la parcela.
     */
    public void setIdParcela(int idParcela) {
        this.idParcela = idParcela;
    }

    /**
     * Obtiene el número de ocupantes en la parcela.
     *
     * @return El número de ocupantes.
     */
    public int getNumeroOcupantes() {
        return numeroOcupantes;
    }

    /**
     * Establece el número de ocupantes en la parcela.
     *
     * @param numeroOcupantes El nuevo número de ocupantes.
     */
    public void setNumeroOcupantes(int numeroOcupantes) {
        this.numeroOcupantes = numeroOcupantes;
    }

    /**
     * Verifica si dos objetos ParcelaReservada son iguales.
     *
     * @param o El objeto a comparar.
     * @return true si son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParcelaReservada that = (ParcelaReservada) o;
        return idParcela == that.idParcela && numeroOcupantes == that.numeroOcupantes;
    }

    /**
     * Genera un código hash para el objeto ParcelaReservada.
     *
     * @return El código hash generado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idParcela, numeroOcupantes);
    }
}