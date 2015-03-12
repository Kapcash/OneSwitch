package iut.oneswitch.control;

import iut.oneswitch.action.ActionGesture;
import iut.oneswitch.app.OneSwitchService;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.Toast;

public class ClickPanelCtrl
{
	private WindowManager.LayoutParams clickParams;
	private boolean forSwipe = false;
	private PopupCtrl popupCtrl;
	private boolean popupVisible = false;
	private int posX = 0;
	private int posX2 = 0;
	private int posY = 0;
	private int posY2 = 0;
	private ScreenTouchDetectorCtrl screenTouch;
	private ShortcutMenuCtrl shortcutMenuCtrl;
	private boolean shortcutMenuVisible = false;
	private View thePanel;
	private OneSwitchService theService;

	public ClickPanelCtrl(OneSwitchService service){
		theService = service;
		init();
		listener();
	}

	private HorizontalLineCtrl horizLine(){
		return theService.getHorizontalLineCtrl();
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

	private void listener(){
		thePanel.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				System.out.println("RFHERUHVDKHVDRH");
				if(keyCode == 62){
					Toast.makeText(theService, "button pressed", Toast.LENGTH_LONG).show();
				}
				return false;
			}
			
		});
		thePanel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){
				if (!shortcutMenuVisible){
					//PREMIER CLICK
					if ((!horizLine().isMoving()) && (!verticalLine().isMoving()) && (!popupVisible)){
						screenTouch = new ScreenTouchDetectorCtrl(theService);
						horizLine().start();
					}
					//DEUXIEME CLICK, QUAND LA LIGNE HORIZONTALE BOUGE
					else if ((horizLine().isMoving()) && (!verticalLine().isMoving()) && (!popupVisible)){
						horizLine().pause();
						verticalLine().start();
						if (!forSwipe) posY = horizLine().getY();
						else posY2 = horizLine().getY();
					}
					//TROISIEME CLICK QUAND LA LIGNE VERTICAL BOUGE
					else if ((!horizLine().isMoving()) && (verticalLine().isMoving()) && (!popupVisible)){
						verticalLine().pause();
						if (!forSwipe){
							posX = verticalLine().getX();
							openPopupCtrl();
						}
						else{
							setVisible(false);
							posX2 = verticalLine().getX();
							forSwipe = false;
							popupCtrl.removeCircle();
							removeLines();
							ActionGesture.swipe(posX, posY, posX2, posY2);
						}
					}
					//QUATRIEME CLICK QUAND LA POPUP EST AFFICHEE
					else if((!horizLine().isMoving()) && (!verticalLine().isMoving()) && (popupVisible)){
						//closePopupCtrl();
						popupCtrl.getSelected().performClick();
					}
				}
				else{
					shortcutMenuCtrl.getSelected().performClick();
					closeShortcutMenu();
				}
			}
		});
		
		
		
		thePanel.setOnLongClickListener(new View.OnLongClickListener(){
			public boolean onLongClick(View paramAnonymousView){
				if ((!horizLine().isMoving()) && (!verticalLine().isMoving()) && (!popupVisible)) {
					openShortcutMenu();
				}
				else  if((horizLine().isMoving()) && (!verticalLine().isMoving()) && (!popupVisible)) {
					horizLine().setInverse();
					return true;
				}
				else if ((!horizLine().isMoving()) && (verticalLine().isMoving()) && (!popupVisible)) {
					verticalLine().setInverse();
					return true;
				}
				return false;
			}
		});
	}
	
	public void gestureDone(){
		popupVisible = false;
		removeLines();
	}
	
	
	
	public void stopAll(){
		removeLines();
		if(popupVisible){
			closePopupCtrl();
		}
		if(shortcutMenuVisible){
			closeShortcutMenu();
		}
		if(thePanel!=null){
			theService.removeView(thePanel);
		}
		if(forSwipe){
			popupCtrl.removeCircle();
		}
	}

	private void openPopupCtrl(){
		popupCtrl = new PopupCtrl(theService, posX, posY);
		popupVisible = true;
		reloadPanel();
	}

	private void openShortcutMenu(){
		shortcutMenuCtrl = new ShortcutMenuCtrl(theService);
		shortcutMenuVisible = true;
		reloadPanel();
	}

	private VerticalLineCtrl verticalLine(){
		return theService.getVerticalLineCtrl();
	}

	public void clickDone(){
		setVisible(true);
	}

	public void closePopupCtrl(){
		if (popupCtrl != null) {
			popupCtrl.removeAllViews();
			popupCtrl = null;
		}
		popupVisible = false;
		removeLines();
	}

	public void closeShortcutMenu(){
		if (shortcutMenuCtrl != null) {
			shortcutMenuCtrl.removeView();
			shortcutMenuCtrl = null;
		}
		shortcutMenuVisible = false;
	}

	public Point getPos(){
		return new Point(posX, posY);
	}

	public void reloadPanel(){
		if(thePanel!=null)
			theService.removeView(thePanel);
		theService.addView(thePanel, clickParams);
	}

	public void removeLines(){
		verticalLine().stop();
		horizLine().stop();
	}

	public void removeView(){
		if (thePanel != null) {
			theService.removeView(thePanel);
		}
	}

	public void setForSwipe(boolean paramBoolean){
		forSwipe = paramBoolean;
	}

	public void setVisible(boolean paramBoolean){
		if (paramBoolean){
			thePanel.setVisibility(View.VISIBLE);
			return;
		}
		thePanel.setVisibility(View.INVISIBLE);
	}
}
