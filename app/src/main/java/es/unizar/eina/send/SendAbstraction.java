package es.unizar.eina.send;

/** Define la interfaz de la abstraccion */
public interface SendAbstraction {

	/** Definicion del metodo que permite realizar el envio del mensaje con texto 'message'
     * @param message cuerpo del mensaje
     */
	public void send(String phone, String message);
}
