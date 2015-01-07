package com.iut.oneswitch.application;

import android.graphics.Point;
import android.view.View;
import android.view.View.OnClickListener;

import com.iut.oneswitch.view.popup.PopUpView;
public class PopUpHandler implements OnClickListener{

	private PopUpView view;
	private Point pos;

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
			//TODO:Action
			ActionGesture actionGesture = new ActionGesture();
			actionGesture.touchAsRoot(view.getPos());
		}
		if(v == view.getButClicLong()){
			//TODO:Action
			ActionGesture actionGesture = new ActionGesture();
			actionGesture.longTouchAsRoot(view.getPos());
		}
		if(v == view.getButGlisser()){
			//TODO:Action
			view.getCtrl().getService().getClickPanelCtrl().setForSwipe(true);
		}
	}

}