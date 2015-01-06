package com.iut.oneswitch.application;

import java.io.IOException;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.iut.oneswitch.view.popup.PopUpView;
public class PopUpHandler implements OnClickListener{

	PopUpView view;
	

	public PopUpHandler(View v){
		view = (PopUpView) v;
	}
	
	/**
	 * Lors d'un clic sur la popup, clic sur le bouton actuellement sélectionné
	 */

	@Override
	public void onClick(View v) {
		//Stop le thread de défilement
		view.stopThread();
		//Enlève la popup
		view.removeView();
		if(v == view.getButClic()){
			System.out.println("Clic");
			//TODO:Action
		}
		if(v == view.getButClicLong()){
			System.out.println("ClicLong");
			//TODO:Action
		}
		if(v == view.getButGlisser()){
			System.out.println("Glisser");
			//TODO:Action
		}
	}

}