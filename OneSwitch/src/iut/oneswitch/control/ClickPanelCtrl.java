package iut.oneswitch.control;

import iut.oneswitch.action.ActionButton;
import iut.oneswitch.action.ActionGesture;
import iut.oneswitch.action.SpeakAText;
import iut.oneswitch.app.OneSwitchKeyBoard;
import iut.oneswitch.app.OneSwitchService;
import iut.oneswitch.view.PanelView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
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
	private int delayMillis = 900;
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
	private boolean waitingGesture=false;

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

	/**
	 * Raccourcis permettant d'optenir la ligne horizontale
	 * @return La ligne horizontale
	 */
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
	
	/**
	 * Permet de savoir si le clavier OneSwitch est ouvert
	 * @return Retourne True si le clavier OneSwitch est ouvert, False sinon
	 */
	public boolean getKeyboard(){
		return keyboard;
	}
	
	/**
	 * Vérifie si le clavier OneSwitch est activé ou non
	 * @return
	 */
	public boolean isInputMethodEnabled() {
	    String id = Settings.Secure.getString(theService.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);

	    ComponentName defaultInputMethod = ComponentName.unflattenFromString(id);

	    ComponentName myInputMethod = new ComponentName(theService, OneSwitchKeyBoard.class);

	    return myInputMethod.equals(defaultInputMethod);
	}
	
	/**
	 * Listenner du clic sur le panel.
	 */
	private void listener(){
		thePanel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View paramAnonymousView){
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
							openScreenTouchDetection();
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
							if (!forSwipe){ //Clic normal
								posX = verticalLine().getX();
								if(getKeyboard()){
									screenTouch.giveCoord(posX,posY);
									gestureDone();
									ActionGesture.click(posX, posY);
								}
								else{
									openPopupCtrl();
									if(screenTouch!=null)
										screenTouch.giveCoord(posX,posY);
								}
							}
							else{ //Pour le Swipe
								removeView();
								posX2 = verticalLine().getX();
								forSwipe = false;
								popupCtrl.removeCircle();
								removeLines();
								ActionGesture.swipe(posX, posY, posX2, posY2);
								Runnable r = new Runnable(){
									@Override
									public void run() {
										if(waitingGesture())
											clickDone();
									}
								};
								Handler handler = new Handler();
								handler.postDelayed(r, delayMillis);
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
					if(shortcutMenuCtrl!= null){
						if(shortcutMenuCtrl.getSelected()!=null)
							shortcutMenuCtrl.getSelected().performClick();
					}
					closeShortcutMenu();
				}
			}
		});

		thePanel.setOnLongClickListener(new View.OnLongClickListener(){
			public boolean onLongClick(View paramAnonymousView){
				keyboard();
				if(!getKeyboard()){
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
							//Arret du balayage
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
				}
				else{
					removeLines();
					keyboard=false;
					ActionButton.back();
				}
				return true;
			}

		});
	}
	
	
	/**
	 * Vérifie si un clavier est ouvert ou non
	 * @return Retourne True si un clavier est affiché, false sinon
	 */
	public boolean keyboard(){
		//Si le clavier OneSwitch n'est pas activé, on stop l'adaptation du clavier
		if(!isInputMethodEnabled()) return false;
		try {
			String line;
			Process process = Runtime.getRuntime().exec("su");
			OutputStream stdin = process.getOutputStream();
			InputStream stdout = process.getInputStream();

			stdin.write(("dumpsys window InputMethod\n").getBytes());
			stdin.write("exit\n".getBytes());
			stdin.flush();

			stdin.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			while ((line = br.readLine()) != null) {
				if(line.contains("mHasSurface=true")){
					keyboard = true;
					break;
				}
				else{
					keyboard = false;
				}
				//keyboard = Boolean.parseBoolean(line.split(" ")[6].split("=")[1]);
			}
			br.close();
			process.waitFor();
			process.destroy();
		} catch (Exception ex) {
		}
		return keyboard;
	}


	/**
	 * Permet de désactiver le sreenTouch.
	 */
	public void closeScreenTouchDetection(){
		if(screenTouch != null){
			screenTouch.close();
		}
		reloadPanel();
		waitingGesture = false;
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
	 * Permet de savoir si nous sommes dans l'attente d'une gesture
	 * @return True si nous sommes dans l'attente d'une gesture
	 */
	public boolean waitingGesture(){
		return waitingGesture;
	}

	/**
	 * Permet de supprimer le panel.
	 */
	public void stopAll(){
		removeView();
		thePanel = null;
		stopMove();
	}

	/**
	 * Permet de stoper les barres, popUP.
	 */
	public void stopMove(){
		removeLines();
		closeScreenTouchDetection();
		closePopupCtrl();
		closeShortcutMenu();
		removeCircle();
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

	/**
	 * Raccourcis permettant d'obtenir la ligne verticale
	 * @return Retourne la ligne verticale
	 */
	private VerticalLineCtrl verticalLine(){
		return theService.getVerticalLineCtrl();
	}

	/**
	 * Indique au service qu'un clic a été effectué
	 */
	public void clickDone(){
		try{
			thePanel.addView();
		}
		catch(Exception e){}
		if(screenTouch != null){
			screenTouch.removeView();
		}
		waitingGesture = false;
	}

	/**
	 * Ferme la popup de gestes
	 */
	public void closePopupCtrl(){
		if(popupVisible){
			if (popupCtrl != null) {
				popupCtrl.removeAllViews();
			}
			closeScreenTouchDetection();
			thePanel.reloadPanel();
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
			}
			shortcutMenuVisible = false;
			thePanel.reloadPanel();
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
	public void swipeMode(){
		if(sp.getBoolean("vocal", false)) {
			SpeakAText.speak(theService.getApplicationContext(), "Appuyez sur l'écran pour choisir un deuxième point.");
		}
		Toast.makeText(theService, "Appuyez sur l'écran pour choisir un deuxième point.", Toast.LENGTH_LONG).show();
		
		forSwipe = true;
		//this.closeScreenTouchDetection();
		try{
			thePanel.addView();
			//thePanel.reloadPanel();
		}
		catch(Exception e){}
	}
	
	/**
	 * Ouvre la vue qu détecte si un geste est effecué
	 */
	public void openScreenTouchDetection(){
		waitingGesture = true;
		screenTouch = new ScreenTouchDetectorCtrl(theService);
	}

	/**
	 * Modifie la visibilité du panel de capture du clic
	 * @param paramBoolean "True" si le panel doit être visible et actif, "false" sinon
	 */
	public void setVisible(boolean paramBoolean){
		if(thePanel!=null)
			thePanel.setVisible(paramBoolean);
	}
	
	/**
	 * Permet d'optenir le delai de sécurité (dans le cas ou un geste est attendu trop longtemps)
	 * @return Le délai de sécurité
	 */
	public int getDelayMillis(){
		return delayMillis;
	}
}
