package com.iut.oneswitch.application;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.iut.oneswitch.view.MainActivity;
import com.iut.oneswitch.view.popup.ShortcutMenuView;
public class ButtonMenuHandler implements OnClickListener{

	ShortcutMenuView view;
	MainActivity theActi = MainActivity.getActivity();
	public ButtonMenuHandler(ShortcutMenuView v){
		view = v;
	}
	
	@Override
	public void onClick(View v) {
		//Stop le thread de défilement
		view.stopThread();
		//Enlève la popup
		view.removeView();
		

		if(v == view.getButton(0)){
			System.out.println("Retour");
			try {
				Runtime.getRuntime().exec("su -c input keyevent 4");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//TODO:Action
		}
		
		else if(v == view.getButton(1)){
			System.out.println("Accueil");

			Intent i = new Intent(Intent.ACTION_MAIN);
			i.addCategory(Intent.CATEGORY_HOME);
			theActi.startActivity(i);
		}
		else if(v == view.getButton(2)){
			System.out.println("Menu");
			
			try {
				Runtime.getRuntime().exec("su -c input keyevent 82");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(v == view.getButton(3)){
			System.out.println("Volume+");

			AudioManager myAudioManager = (AudioManager)theActi.getSystemService(Context.AUDIO_SERVICE); 
            	myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);		
        }
		
		else if(v == view.getButton(4)){
			System.out.println("Volume-");
			AudioManager myAudioManager = (AudioManager)theActi.getSystemService(Context.AUDIO_SERVICE); 
        	myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);		
        }
		
		else if(v == view.getButton(5)){
			System.out.println("Verouiller");
			try {
				Runtime.getRuntime().exec("su -c input keyevent 26");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		
		else if(v == view.getButton(6)){
			System.out.println("Eteindre");
			//TODO:Action
		}
	}

}