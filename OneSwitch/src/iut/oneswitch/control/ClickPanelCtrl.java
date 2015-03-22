package iut.oneswitch.control;

import iut.oneswitch.action.ActionButton;
import iut.oneswitch.action.ActionGesture;
import iut.oneswitch.action.SpeakAText;
import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.PanelView;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.View;
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
		//thePanel.setColor(0xCC000000);
	}

	private void listener(){
		thePanel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){
				int delay = Integer.parseInt(sp.getString("reboundDelay","200"));
				if (!shortcutMenuVisible){
					if(ActionButton.getVolumeStop()) {
						//PREMIER CLICK
						SpeakAText.playSound(theService);
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
								screenTouch.giveCoord(posX,posY);
							}
							else{
								//setVisible(false);
								thePanel.removeView();
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
				if(!shortcutMenuVisible && !popupVisible){
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
						}
						else if ((!horizLine().isMoving()) && (verticalLine().isMoving()) && (!popupVisible)) {
							verticalLine().setInverse();
						}
						break;
					case 2:
						//Recommence au début
						if((horizLine().isMoving()) && (!verticalLine().isMoving()) && (!popupVisible)) {
							horizLine().restart();
						}
						else if ((!horizLine().isMoving()) && (verticalLine().isMoving()) && (!popupVisible)) {
							verticalLine().restart();
						}
						break;
					case 3:
						if(!popupVisible) {
							removeLines();
						}
						break;
					default:
						break;
					}
				}
				else if(shortcutMenuVisible){
					closeShortcutMenu();
				}
				else if(popupVisible){
					closePopupCtrl();
				}
				return true;
			}
		});
	}

	public void closeScreenTouchDetection(){
		if(screenTouch != null){
			screenTouch.close();
		}
	}

	public void gestureDone(){
		popupVisible = false;
		removeLines();
		removeView();
	}

	public void stopAll(){
		removeView();
		stopMove();
	}

	public void stopMove(){
		removeLines();
		removeTouchDetection();
		closePopupCtrl();
		closeShortcutMenu();
		removeCircle();
	}


	public void removeTouchDetection(){
		if(screenTouch!=null){
			screenTouch.removeView();
			screenTouch = null;
		}
		
	}

	public void removeCircle(){
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
		//setVisible(true);
		thePanel.addView();
		removeTouchDetection();
	}

	public void closePopupCtrl(){
		if(popupVisible){
			if (popupCtrl != null) {
				popupCtrl.removeAllViews();
				popupCtrl = null;
			}
			removeTouchDetection();
			popupVisible = false;
			removeLines();
		}
	}

	public void closeShortcutMenu(){
		if(shortcutMenuVisible){
			if (shortcutMenuCtrl != null) {
				shortcutMenuCtrl.removeView();
				shortcutMenuCtrl = null;
			}
			shortcutMenuVisible = false;
		}
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
		if(thePanel!=null)
			thePanel.setVisible(paramBoolean);
	}
}