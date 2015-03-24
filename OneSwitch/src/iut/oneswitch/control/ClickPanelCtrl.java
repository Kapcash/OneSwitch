package iut.oneswitch.control;

import iut.oneswitch.action.ActionButton;
import iut.oneswitch.action.ActionGesture;
import iut.oneswitch.action.SpeakAText;
import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.PanelView;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Classe permettant de gèrer les actions sur le panel.
 * @author OneSwitch B
 *
 */
public class ClickPanelCtrl{
	private boolean forSwipe = false;
	/**
	 * Popup affichant les gestes (clic, clic long, glisser etc.)
	 */
	private PopupCtrl popupCtrl;
	/**
	 * Panel affichant les raccourcis (Menu, Accueil, Retour etc.)
	 */
	private ScreenTouchDetectorCtrl screenTouch;
	private boolean popupVisible = false;
	private int posX = 0;
	private int posX2 = 0;
	private int posY = 0;
	private int posY2 = 0;
	private ShortcutMenuCtrl shortcutMenuCtrl;
	private boolean shortcutMenuVisible = false;
	/**
	 * Panel invisible, au premier plan, capturant les clics
	 */
	private PanelView thePanel;
	private OneSwitchService theService;
	protected long currentClick;
	protected long lastClick = 0;
	private SharedPreferences sp;
	
	/**
	 * Boolean pour savoir si le clavier est présent ou non.
	 */
	private boolean keyboard=false;

	/**
	 * Constructeur de la classe
	 * @param service
	 */
	public ClickPanelCtrl(OneSwitchService service){
		theService = service;
		sp = PreferenceManager.getDefaultSharedPreferences(theService);
		init();
		listener();
	}

	private HorizontalLineCtrl horizLine(){
		return theService.getHorizontalLineCtrl();
	}

	/**
	 * Instancie PanelView
	 * -->Initialise le panel.
	 */
	private void init(){
		thePanel = new PanelView(theService);
		//thePanel.setColor(0xCC000000);
	}
	
	private boolean keyboardOpen(){
		InputMethodManager imm = (InputMethodManager) theService.getSystemService(Context.INPUT_METHOD_SERVICE);

	    if (imm.isAcceptingText()) {
	       keyboard=true;
	       
	    } else {
	        keyboard=false;
	    }
	    return keyboard;
	}

	/**
	 * Listenner du clic sur le panel.
	 */
	private void listener(){
		thePanel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){
				System.out.println("gvfdbgrsgvrdgbrdgvre             "+keyboardOpen());
				int delay = Integer.parseInt(sp.getString("reboundDelay","200"));
				if (!shortcutMenuVisible){
					if(ActionButton.getVolumeStop()) {
						//PREMIER CLICK
						if(sp.getBoolean("vocal", false)) {
							SpeakAText.playSound(theService);
						}
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

	/**
	 * Permet de désactiver le sreenTouch.
	 */
	public void closeScreenTouchDetection(){
		if(screenTouch != null){
			screenTouch.close();
		}
	}

	/**
	 * Permet de supprimer les vues.
	 */
	public void gestureDone(){
		popupVisible = false;
		removeLines();
		removeView();
	}

	/**
	 * Permet de supprimer le panel.
	 */
	public void stopAll(){
		removeView();
		stopMove();
	}

	/**
	 * Permet de stoper les barres, popUP.
	 */
	public void stopMove(){
		removeLines();
		removeTouchDetection();
		closePopupCtrl();
		closeShortcutMenu();
		removeCircle();
	}


	/**
	 * Supprimes le screenTouch.
	 */
	public void removeTouchDetection(){
		if(screenTouch!=null){
			screenTouch.removeView();
			screenTouch = null;
		}

	}

	/**
	 * Permet de supprimer le cercle.
	 */
	public void removeCircle(){
		if(forSwipe){
			popupCtrl.removeCircle();
		}
	}

	/**
	 * Permet de lancer la popUp sur appuie simple.
	 */
	private void openPopupCtrl(){
		popupCtrl = new PopupCtrl(theService, posX, posY);
		popupVisible = true;
		reloadPanel();
	}

	/**
	 * Permet de lancer la popUp sur appuie long.
	 */
	public void openShortcutMenu(){
		shortcutMenuCtrl = new ShortcutMenuCtrl(theService);
		shortcutMenuVisible = true;
		reloadPanel();
	}

	
	private VerticalLineCtrl verticalLine(){
		return theService.getVerticalLineCtrl();
	}

	/**
	 * Indique au service qu'un clic a été effectué
	 */
	public void clickDone(){
		//setVisible(true);
		try{
			thePanel.addView();
		}
		catch(Exception e){

		}
		removeTouchDetection();
	}

	/**
	 * Ferme la popup de gestes
	 */
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

	/**
	 * Ferme la popup de raccourcis
	 */
	public void closeShortcutMenu(){
		if(shortcutMenuVisible){
			if (shortcutMenuCtrl != null) {
				shortcutMenuCtrl.removeView();
				shortcutMenuCtrl = null;
			}
			shortcutMenuVisible = false;
		}
	}

	/**
	 * Donne la position  l'intersection des lignes de pointage
	 * @return Retourne le point formé par les lignes
	 */
	public Point getPos(){
		return new Point(posX, posY);
	}
	
	/**
	 * Rafraichit le panel
	 */
	public void reloadPanel(){
		if(thePanel!=null){
			thePanel.reloadPanel();
		}
	}

	/**
	 * Supprime les lignes de pointage
	 */
	public void removeLines(){
		verticalLine().stop();
		horizLine().stop();
	}

	/**
	 * Supprime le panel de capture du clic
	 */
	public void removeView(){
		if (thePanel != null) {
			thePanel.removeView();
		}
	}

	/**
	 * Indique si le service doit effectuer un glisser
	 * @param paramBoolean "True" si l'action à effectuer est un glisser, "false" sinon
	 */
	public void setForSwipe(boolean paramBoolean){
		forSwipe = paramBoolean;
		this.removeTouchDetection();
		try{
			thePanel.addView();
		}
		catch(Exception e){}
	}

	/**
	 * Modifie la visibilité du panel de capture du clic
	 * @param paramBoolean "True" si le panel doit être visible et actif, "false" sinon
	 */
	public void setVisible(boolean paramBoolean){
		if(thePanel!=null)
			thePanel.setVisible(paramBoolean);
	}
}