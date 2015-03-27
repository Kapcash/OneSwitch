package iut.oneswitch.view;

import iut.oneswitch.app.OneSwitchService;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

/**
 * Classe permettant l'implementation du panel clickable de notre sevice.
 */
public class PanelView{
	private OneSwitchService theService;
	private View thePanel;
	private WindowManager.LayoutParams clickParams;

	/**
	 * Constructeur de la classe
	 * @param service
	 */
	public PanelView(OneSwitchService service) {
		theService = service;
		init();
	}

	/**
	 * Initialise le panel
	 */
	private void init(){
		thePanel = new View(theService);
		clickParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);
		
		clickParams.gravity = Gravity.TOP | Gravity.START;
		clickParams.x = 0;
		clickParams.y = 0;
		clickParams.height = theService.getScreenSize().y;
		clickParams.width = theService.getScreenSize().x;
		thePanel.invalidate();
		theService.addView(thePanel, clickParams);
	}
	
	public void forceLayout(){
		//thePanel.invalidate();
	}

	/**
	 * Listener sur un clic normal (sur le panel)
	 * @param listener
	 */
	public void setOnClickListener(OnClickListener listener){
		thePanel.setOnClickListener(listener);
	}

	/**
	 * Listener sur un long click (sur le panel)
	 * @param listener
	 */
	public void setOnLongClickListener(OnLongClickListener listener){
		thePanel.setOnLongClickListener(listener);
	}

	/**
	 * change la couleur de fond du panel
	 * @param color La nouvelle couleur
	 */
	public void setColor(int color){
		thePanel.setBackgroundColor(color);
		theService.updateViewLayout(thePanel, clickParams);
	}

	/**
	 * Permet de supprimer le panel
	 */
	public void removeView(){
		if(thePanel!=null){
			if(theService!=null)
				theService.removeView(thePanel);
		}
	}

	/**
	 * Renvoies un évènement de click sur la vue
	 */
	public void performClick(){
		thePanel.performClick();
	}

	/**
	 * Supprimes et remet le panel
	 */
	public void reloadPanel(){
		if(thePanel!=null){
			if(theService!=null){
				theService.removeView(thePanel);
				theService.addView(thePanel, clickParams);
			}
			thePanel.invalidate();
		}
	}

	/**
	 * Permet de mettre à jour le panel
	 */
	public void updateView(){
		try{
			if(thePanel != null && theService!=  null){
				theService.removeView(thePanel);
				clickParams = new WindowManager.LayoutParams(
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
						WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
						WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
						PixelFormat.TRANSLUCENT); 
				thePanel.invalidate();
				theService.addView(thePanel, clickParams);
			}
		}
		catch(RuntimeException e){}
	}
	
	/**
	 * Met à jour le panel
	 */
	public void updateViewLayout(){
		theService.updateViewLayout(thePanel, clickParams);
	}
	
	/**
	 * Permet de changer la visibilité du panel
	 * @param paramBoolean true, le panel est visible, false le panel est invisible.
	 */
	public void setVisible(boolean paramBoolean){
		if (paramBoolean){
			thePanel.setVisibility(View.VISIBLE);
		}
		else{
			thePanel.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Permet d'ajouter le panel au service.
	 */
	public void addView(){
		theService.addView(thePanel, clickParams);
	}

	public void setOnTouchListener(OnTouchListener listener) {
		thePanel.setOnTouchListener(listener);
	}

}
