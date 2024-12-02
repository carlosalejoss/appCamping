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
      PackageManager pm = getSourceActivity().getPackageManager();
      boolean app_installed;
      try {
         pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
         app_installed = true ;
      } catch (PackageManager.NameNotFoundException e) {
         app_installed = false ;
      }
      if (app_installed) {
         Uri smsUri = Uri.parse("sms: " + phone );
         Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
         sendIntent.putExtra(Intent.EXTRA_TEXT, message);
         sendIntent.setPackage("com.whatsapp");
         getSourceActivity().startActivity(sendIntent);
      } else {
         Toast.makeText(getSourceActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
      }
   }
}
