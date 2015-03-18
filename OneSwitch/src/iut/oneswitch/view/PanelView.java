package iut.oneswitch.view;

import iut.oneswitch.app.OneSwitchService;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;

public class PanelView{
	private OneSwitchService theService;
	private View thePanel;
	private WindowManager.LayoutParams clickParams;

	public PanelView(OneSwitchService service) {
		theService = service;
		init();
	}

	private void init(){
		thePanel = new View(theService);
		clickParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
				theService.getStatusBarHeight(),
				WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT); 
		clickParams.gravity = Gravity.TOP | Gravity.LEFT;
		clickParams.x = 0;
		clickParams.y = 0;
		clickParams.height = theService.getScreenSize().y;
		clickParams.width = theService.getScreenSize().x;
		theService.addView(thePanel, clickParams);
	}

	public void setOnClickListener(OnClickListener listener){
		thePanel.setOnClickListener(listener);
	}

	public void setOnLongClickListener(OnLongClickListener listener){
		thePanel.setOnLongClickListener(listener);
	}

	public void setOnKeyListener(OnKeyListener listener){
		thePanel.setOnKeyListener(listener);
	}

	public void removeView(){
		if(thePanel!=null){
			if(theService!=null)
				theService.removeView(thePanel);
		}
	}

	public void reloadPanel(){
		if(thePanel!=null){
			if(theService!=null){
				theService.removeView(thePanel);
				theService.addView(thePanel, clickParams);
			}

		}
	}

	public void setVisible(boolean paramBoolean){
		if (paramBoolean){
			thePanel.setVisibility(View.VISIBLE);
		}
		else{
			thePanel.setVisibility(View.INVISIBLE);
		}
	}



}
