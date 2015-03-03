package com.example.oneswitch.control;

import java.io.IOException;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.example.oneswitch.appliction.OneSwitchService;

public class ClickPanelCtrl {

	private OneSwitchService theService;
	private View thePanel;
	private WindowManager.LayoutParams clickParams;
	private int posX=0;
	private int posY=0;
	private int posX2=0;
	private int posY2 = 0;
	private boolean forSwipe = false;
	private boolean popupVisible = false;
	private boolean shortcutMenuVisible = false;
	private PopupCtrl popupCtrl;
	private ShortcutMenuCtrl shortcutMenuCtrl;
	private ScreenTouchDetectorCtrl screenTouch;

	public ClickPanelCtrl(OneSwitchService service){
		theService = service;

		init();
		listener();
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

	private HorizontalLineCtrl horizLine(){
		return theService.getHorizontalLineCtrl();
	}

	private VerticalLineCtrl verticalLine(){
		return theService.getVerticalLineCtrl();
	}

	private void listener(){
		//LORS D'UN SIMPLE CLICK SUR LE PANEL
		thePanel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!shortcutMenuVisible){
					//TOUT PREMIER CLIC
					if(!horizLine().isMoving() && !verticalLine().isMoving() && !popupVisible){
						screenTouch = new ScreenTouchDetectorCtrl(theService);
						horizLine().start();
					}
					//DEUXIEME CLIC, QUAND LA LIGNE HORIZ BOUGE
					else if(horizLine().isMoving() && !verticalLine().isMoving() && !popupVisible){
						horizLine().pause();
						verticalLine().start();
						if(!forSwipe) posY = horizLine().getY();
						else posY2 = horizLine().getY();
					}
					//TROISIEME CLIC, QUAND LA LIGNE VERTICALE BOUGE
					else if(!horizLine().isMoving() && verticalLine().isMoving() && !popupVisible){
						verticalLine().pause();
						if(!forSwipe){
							posX = verticalLine().getX();
							openPopupCtrl();
						}
						else{
							setVisible(false);
							posX2 = verticalLine().getX();
							forSwipe = false;
							removeLines();
							try {
								Runtime.getRuntime().exec("su -c input swipe " + posX + " " + posY + " " + posX2 + " " + posY2 + " 300");
							} catch (IOException e) {}
						}
					}
					//QUATRIEME CLIC, QUAND LA POPUP EST AFFICHEE
					else if(!horizLine().isMoving() && !verticalLine().isMoving() && popupVisible){
						Button btClicked = (Button) popupCtrl.getSelected();
						closePopupCtrl();
						if(btClicked!=null) btClicked.performClick();
					}
				}
				else{
					shortcutMenuCtrl.getSelected().performClick();
					closeShortcutMenu();
				}
			}
		});


		//LORS D'UN LONG CLICK SUR LE PANEL
		thePanel.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				if(!horizLine().isMoving() && !verticalLine().isMoving() && !popupVisible){
					openShortcutMenu();
				}
				return false;
			}
		});
	}
	
	public void clickDone(){
		setVisible(true);
		if(screenTouch!=null) screenTouch.removeView();
	}

	public void setForSwipe(boolean bool){
		forSwipe = bool;
	}

	public Point getPos(){
		return new Point(posX, posY);
	}

	public void setVisible(boolean bool){
		if(bool) thePanel.setVisibility(View.VISIBLE);
		else thePanel.setVisibility(View.INVISIBLE);
	}

	public void reloadPanel(){
		theService.removeView(thePanel);
		theService.addView(thePanel, clickParams);
	}

	private void openPopupCtrl(){
		popupCtrl = new PopupCtrl(theService, posX, posY);
		popupVisible = true;
		reloadPanel();
	}

	public void closePopupCtrl(){
		if(popupCtrl!=null)popupCtrl.removeView();
		popupVisible = false;
		removeLines();
	}

	private void openShortcutMenu(){
		shortcutMenuCtrl = new ShortcutMenuCtrl(theService);
		shortcutMenuVisible = true;
		reloadPanel();
	}

	public void closeShortcutMenu(){
		if(shortcutMenuCtrl!=null) shortcutMenuCtrl.removeView();
		shortcutMenuVisible = false;
	}

	public void removeLines(){
		verticalLine().stop();
		horizLine().stop();
	}

	public void removeView(){
		if(thePanel != null) theService.removeView(thePanel);
	}



}
