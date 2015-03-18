package iut.oneswitch.control;

import iut.oneswitch.action.ActionButton;
import iut.oneswitch.action.ActionGesture;
import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.PanelView;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Toast;

public class ClickPanelCtrl{
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
	private PanelView thePanel;
	private OneSwitchService theService;
	protected long currentClick;
	protected long lastClick = 0;
	private SharedPreferences sp;

	public ClickPanelCtrl(OneSwitchService service){
		theService = service;
		sp = PreferenceManager.getDefaultSharedPreferences(theService);
		init();
		listener();
	}

	private HorizontalLineCtrl horizLine(){
		return theService.getHorizontalLineCtrl();
	}

	private void init(){
		thePanel = new PanelView(theService);
	}

	private void listener(){
		thePanel.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == 62){
					Toast.makeText(theService, "button pressed", Toast.LENGTH_LONG).show();
				}
				return false;
			}
		});

		thePanel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){
				int delay = Integer.parseInt(sp.getString("reboundDelay","200"));
				if (!shortcutMenuVisible){
					if(ActionButton.getVolumeStop()) {
						//PREMIER CLICK
						currentClick = System.currentTimeMillis();

						if(currentClick-lastClick<delay && lastClick != 0){
							Toast.makeText(theService, "Anti-rebonds : click ignoré.", Toast.LENGTH_SHORT).show();
							return;
						}
						else
							lastClick = currentClick;

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
					else {
						ActionButton.stopVolumeChange();
					}
				}
				else{
					if(shortcutMenuCtrl.getSelected()!=null)
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

				switch (Integer.parseInt(sp.getString("longPressAction","1"))) {
				case 0:
					//Ne fait rien
					break;
				case 1:
					//Inverse lignes
					if((horizLine().isMoving()) && (!verticalLine().isMoving()) && (!popupVisible)) {
						horizLine().setInverse();
						return true;
					}
					else if ((!horizLine().isMoving()) && (verticalLine().isMoving()) && (!popupVisible)) {
						verticalLine().setInverse();
						return true;
					}
					break;
				case 2:
					//Recommence au début
					if((horizLine().isMoving()) && (!verticalLine().isMoving()) && (!popupVisible)) {
						horizLine().restart();
						return true;
					}
					else if ((!horizLine().isMoving()) && (verticalLine().isMoving()) && (!popupVisible)) {
						verticalLine().restart();
						return true;
					}
					break;
				case 3:
					if(!popupVisible) {
						removeLines();
						return true;
					}

					break;

				default:
					break;
				}



				return false;
			}
		});
	}

	public void gestureDone(){
		popupVisible = false;
		removeLines();
	}

	public void pause(){
		thePanel.setVisible(false);
		removeLines();
		if(popupVisible){
			closePopupCtrl();
		}
		if(shortcutMenuVisible){
			closeShortcutMenu();
		}
		if(forSwipe){
			popupCtrl.removeCircle();
		}
	}

	public void stopAll(){
		removeLines();
		removeView();
		if(popupVisible){
			closePopupCtrl();
		}
		if(shortcutMenuVisible){
			closeShortcutMenu();
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
		screenTouch = null;
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
		if(thePanel!=null){
			thePanel.reloadPanel();
		}
	}

	public void removeLines(){
		verticalLine().stop();
		horizLine().stop();
	}

	public void removeView(){
		if (thePanel != null) {
			thePanel.removeView();
		}
	}

	public void setForSwipe(boolean paramBoolean){
		forSwipe = paramBoolean;
	}

	public void setVisible(boolean paramBoolean){
		thePanel.setVisible(paramBoolean);
	}
}