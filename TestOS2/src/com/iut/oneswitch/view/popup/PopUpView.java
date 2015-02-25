package com.iut.oneswitch.view.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.oneswitch.R;
import com.iut.oneswitch.application.ClickHandler;
import com.iut.oneswitch.application.PopUpCtrl;
import com.iut.oneswitch.application.PopUpHandler;


/**
 * 
 * @author OneSwitch B
 *
 */
public class PopUpView extends View{

	/**
	 * La popUp devant être affichée.
	 */
	private PopupWindow popUp;
	
	/**
	 * Le bouton de liste en surbrillance.
	 */
	private Button selected;
	
	/**
	 * 
	 */
	private View view;
	
	/**
	 * Le contrôleur de la popUp.
	 */
	private PopUpCtrl theCtrl;
	
	/**
	 * 
	 */
	private PopUpHandler handler;
	
	/**
	 * Le compteur de tour de liste.
	 */
	private int iterations;
	
	/**
	 * Acesseur de l'attribut de contrôle de la popUp.
	 * @return Le contrôleur de la popUp à savoir theCtrl.
	 */
	public PopUpCtrl getCtrl(){
		return theCtrl;
	}
	
	/**
	 * Bouton "Clic" de la popup.
	 */
	private Button butClic;
	
	/**
	 * Bouton "Clic long" de la popup
	 */
	private Button butClicLong;
	
	/**
	 * Bouton "Glisser" de la popup
	 */
	private Button butGlisser;

	/**
	 * Constructeur de popUpView.
	 * @param context Le contexte de l'application
	 * @param ctrl Le contrôleur de la popUp.
	 */
	public PopUpView(Context context, PopUpCtrl ctrl) {
		super(context);
		theCtrl = ctrl;
		popUp = new PopupWindow(this.getContext());
		handler = new PopUpHandler(this);
	}
	
	/**
	 * Acesseur de l'attribut selected.
	 * @return Le bouton de la popUp sélectionné / en surbrillance.
	 */
	public Button getSelected(){
		return selected;
	}
	
	/**
	 * Sélectionne le prochain bouton de la popUp et le met en surbrillance.
	 * Au bout de trois tours de liste, la popUp disparaît.
	 */
	public void selectNext(){
		if(iterations==3){
			stopThread();
			removeView();
			ClickHandler.notifyPopupClosed();
		}
		else{
			Button butClic = (Button)view.findViewById(R.id.but_clic);
			Button butClicLong = (Button)view.findViewById(R.id.but_clic_long);
			Button butGlisser = (Button)view.findViewById(R.id.but_glisser);
	
			if(selected == butGlisser){
				butClic.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				butClicLong.getBackground().clearColorFilter();
				butGlisser.getBackground().clearColorFilter();
				selected = butClic;
			}	
			else if(selected == butClic){
				butClic.getBackground().clearColorFilter();
				butClicLong.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				butGlisser.getBackground().clearColorFilter();
				selected = butClicLong;
			}	
			else if(selected == butClicLong){
				butClic.getBackground().clearColorFilter();
				butClicLong.getBackground().clearColorFilter();
				butGlisser.getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
				selected = butGlisser;
				iterations++;
			}	
			popUp.setContentView(view);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		float density = getResources().getDisplayMetrics().density;

		System.out.println("Drawing popup view");

		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.popup,null);
		popUp.setContentView(view);

		popUp.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0);
		popUp.update((int)density*28, 0, (int)(canvas.getWidth()-(28*density)), canvas.getHeight());

		butClic = (Button)view.findViewById(R.id.but_clic);
		butClicLong = (Button)view.findViewById(R.id.but_clic_long);
		butGlisser = (Button)view.findViewById(R.id.but_glisser);
		
		selected = butGlisser;
		theCtrl.startThread(); //select buttons l'un apres l'autre
		iterations=0;
		
		butClic.setOnClickListener(handler);
		butClicLong.setOnClickListener(handler);
		butGlisser.setOnClickListener(handler);
	}

	public void removeView(){
		theCtrl.removeView();
	}

	/**
	 * Stop le thread du défilement (via PopUpCtrl).	
	 */
	public void stopThread(){
		theCtrl.stopThread();
	}
	
	/**
	 * Accesseur de l'attribut butClic.
	 * @return Le bouton "clic" de la popUp.
	 */
	public Button getButClic() {
		return butClic;
	}
	
	/**
	 * Accesseur de butClicLong.
	 * @return Le bouton "clic long" de la popUp.
	 */
	public Button getButClicLong() {
		return butClicLong;
	}
	
	/**
	 * Le bouton "glisser" de la popUp.
	 * @return
	 */
	public Button getButGlisser() {
		return butGlisser;
	}
	
	/**
	 * 
	 * @return La position de la popUp.
	 */
	public Point getPos(){
		return theCtrl.getPos();
	}
}