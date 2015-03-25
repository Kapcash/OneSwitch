package iut.oneswitch.app;

import iut.oneswitch.preference.PrefGeneralFragment;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activité principale du projet OneSwitch
 * @author OneSwitch B
 */
public class OneSwitchActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		

		/* Execute une commande "root" dès le démarrage pour que SuperSU (application de root)
		 * demande d'autoriser les droits root pour l'application et éviter une gêne */
		try {
			Runtime.getRuntime().exec("su");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Activité initialisée par le Fragment
		getFragmentManager().beginTransaction().replace(android.R.id.content,new PrefGeneralFragment()).commit();
	}
}