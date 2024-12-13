package es.unizar.eina.send;

import android.net.Uri;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.app.Activity;
import android.widget.Toast;

/** Concrete implementor utilizando la aplicacion de WhatsApp. No funciona en el emulador si no se ha configurado previamente */
public class WhatsAppImplementor implements SendImplementor{

   /** actividad desde la cual se abrira la aplicacion de WhatsApp */
   private Activity sourceActivity;
   /** Constructor
    * @param source actividad desde la cual se abrira la aplicacion de Whatsapp
    */
   public WhatsAppImplementor(Activity source){
	   setSourceActivity(source);
   }

   /**  Actualiza la actividad desde la cual se abrira la actividad de gestion de correo */
   public void setSourceActivity(Activity source) {
	   sourceActivity = source;
   }

   /**  Recupera la actividad desde la cual se abrira la aplicacion de Whatsapp */
   public Activity getSourceActivity(){
     return sourceActivity;
   }

    /**
     * Implementacion del metodo send utilizando la aplicacion de WhatsApp
     * @param phone telefono
     * @param message cuerpo del mensaje
     */
    public void send (String phone, String message) {
        try {
            String url = "https://api.whatsapp.com/send?phone=" + phone + "&text=" + Uri.encode(message);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            sendIntent.setPackage("com.whatsapp");
            getSourceActivity().startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(getSourceActivity(), "Error al abrir WhatsApp", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
