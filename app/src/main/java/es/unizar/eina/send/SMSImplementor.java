package es.unizar.eina.send;

import android.net.Uri;
import android.content.Intent;

import android.app.Activity;
import android.util.Log;

/** Concrete implementor utilizando la actividad de envio de SMS. No funciona en el emulador si no se ha configurado previamente */
public class SMSImplementor implements SendImplementor {

    /** actividad desde la cual se abrira la actividad de envio de SMS */
    private Activity sourceActivity;

    /** Constructor
     * @param source actividad desde la cual se abrira la actividad de envio de SMS
     */
    public SMSImplementor(Activity source){
        setSourceActivity(source);
    }

    /**  Actualiza la actividad desde la cual se abrira la actividad de envio de SMS */
    public void setSourceActivity(Activity source) {
        sourceActivity = source;
    }

    /**  Recupera la actividad desde la cual se abrira la actividad de envio de SMS */
    public Activity getSourceActivity(){
        return sourceActivity;
    }

    /**
     * Implementacion del metodo send utilizando la aplicacion de envio de SMS
     * @param phone telefono
     * @param message cuerpo del mensaje
     */
    public void send (String phone, String message) {
        Log.d("SMSImplementor", message);
        Uri smsUri = Uri.parse("sms: " + phone);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsUri);
        sendIntent.putExtra("sms_body ", message);
        getSourceActivity().startActivity(sendIntent);
        Log.d("SMSImplementor", "Se ha terminado la funcion send");
   }

}
