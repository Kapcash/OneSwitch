package com.iut.oneswitch.application;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.iut.oneswitch.view.popup.PopUpView;
public class PopUpHandler implements OnClickListener{

	private PopUpView view;

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
			ActionGesture actionGesture = new ActionGesture();
			actionGesture.touchAsRoot(view.getPos());
		}
		if(v == view.getButClicLong()){
			ActionGesture actionGesture = new ActionGesture();
			actionGesture.longTouchAsRoot(view.getPos());
		}
		if(v == view.getButGlisser()){
			Toast.makeText(view.getContext(), "Choisissez un second point", Toast.LENGTH_LONG).show();
			view.getCtrl().getService().getClickPanelCtrl().setForSwipe(true);
		}
	}
}