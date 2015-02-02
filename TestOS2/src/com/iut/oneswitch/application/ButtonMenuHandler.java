package com.iut.oneswitch.application;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.iut.oneswitch.view.MainActivity;
import com.iut.oneswitch.view.popup.ShortcutMenuView;

/**
 * Cette classe est étroitement lié à la classe ButtonMenuCtrl.
 * Elle permet l'écoute des boutons du menu implémentée via la classe ButtonMenuCtrl.
 * @author OneSwitch B
 *
 */
public class ButtonMenuHandler implements OnClickListener{
	
	/**
	 * Le menu devant être mis sur écoute.
	 */
	ShortcutMenuView view;
	
	/**
	 * Variable nous permettant d'effectuer un lien avec l'activité de notre application.
	 */
	MainActivity theActi = MainActivity.getActivity();
	
	/**
	 * Constructeur de la classe ButtonMenuHandler.
	 * Il instancie l'attribut ButtonMenuCtrl avec une vue passée en paramètre.
	 * @param v L'objet de type ShortcutMenuView devant être mis sur écoute.
	 */
	public ButtonMenuHandler(ShortcutMenuView v){
		view = v;
	}
	
	@Override
	public void onClick(View v) {
		//Stop le thread de défilement
		view.stopThread();
		//Enlève la popup
		view.removeView();

	//Retour
		if(v == view.getButton(0)){
			System.out.println("Retour");
			try {
				Runtime.getRuntime().exec("su -c input keyevent 4");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	//Accueil
		else if(v == view.getButton(1)){
			System.out.println("Accueil");

			Intent i = new Intent(Intent.ACTION_MAIN);
			i.addCategory(Intent.CATEGORY_HOME);
			theActi.startActivity(i);
		}
	//Menu
		else if(v == view.getButton(2)){
			System.out.println("Menu");
			
			try {
				Runtime.getRuntime().exec("su -c input keyevent 82");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	//Volume +
		else if(v == view.getButton(3)){
			System.out.println("Volume+");

			AudioManager myAudioManager = (AudioManager)theActi.getSystemService(Context.AUDIO_SERVICE); 
            	myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);		
        }
	//Volume -
		else if(v == view.getButton(4)){
			System.out.println("Volume-");
			AudioManager myAudioManager = (AudioManager)theActi.getSystemService(Context.AUDIO_SERVICE); 
        	myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);		
        }
	//Verrouiller	
		else if(v == view.getButton(5)){
			System.out.println("Verrouiller");
			try {
				Runtime.getRuntime().exec("su -c input keyevent 26");
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
	//Eteindre	
		else if(v == view.getButton(6)){
			System.out.println("Eteindre");
			//TODO:Action
		}
	}

}