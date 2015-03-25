package iut.oneswitch.view;

import iut.oneswitch.R;
import iut.oneswitch.action.ActionGesture;
import iut.oneswitch.action.SpeakAText;
import iut.oneswitch.control.ClickPanelCtrl;
import iut.oneswitch.control.PopupCtrl;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Classe permettant de dessiner la popUp sur un clic simple.
 * @author OneSwitch B
 *
 */
public class PopupView extends View{

	/**
	 * Nombre d'itérations effectuées après un lancement
	 */
	private int iterations = 0;
	/**
	 * Index du bouton sélectionné
	 */
	private int selectedIndex = 0;
	private PopupWindow popUp;
	/**
	 * ButtonGroup sélectionné
	 */
	private ButtonGroup selected;
	private PopupCtrl theCtrl;
	private View view;
	private SparseArray<ButtonGroup> btList = new SparseArray<ButtonGroup>();
	private SharedPreferences sp;

	/**
	 * COnstructeur de la classe
	 * @param paramContext
	 * @param paramPopupCtrl
	 */
	public PopupView(Context paramContext, PopupCtrl paramPopupCtrl){
		super(paramContext);
		theCtrl = paramPopupCtrl;
		sp = PreferenceManager.getDefaultSharedPreferences(theCtrl.getService());
		init();
	}

	/**
	 * Initialise la popUp avec les différents boutons.
	 */
	private void init(){
		popUp = new PopupWindow(getContext());
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.popup,null);

		//AJOUT BOUTON CLIC, INDEX 0
		addButton(R.id.but_popup_01);

		//AJOUT BOUTON LONG, INDEX 1
		addButton(R.id.but_popup_02);

		//AJOUT BOUTON GLISSER, INDEX 2
		addButton(R.id.but_popup_03);

		//AJOUT BOUTON AUTRES, INDEX 3
		addButton(R.id.but_popup_04);

		//AJOUT BOUTON PAGE SUIVANTE, INDEX 4
		addButton(R.id.but_popup_05);
	}

	/**
	 * Permet de dessiner la popUp
	 */
	public void onDraw(Canvas canvas){
		popUp.setContentView(view);
		popUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupbackground));
		popUp.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0);
		popUp.update(0, 0,(canvas.getWidth()), canvas.getHeight());

		selectedIndex = btList.size()-1;
		selected = btList.get(selectedIndex);
		
		theCtrl.start();
		prevPage();
	}

	/**
	 * Permet d'ajouter un bouton à la popUp
	 * @param btId l'id du nouveau bouton
	 */
	private void addButton(int btId){
		btList.put(btList.size(), new ButtonGroup(btId));
	}

	/**
	 * Méthode avec les différents listenners sur les boutons : Click, Click Long, Glisser, Autres et pages suivante.
	 */
	private void prevPage(){
		//BOUTON CLIC
		btList.get(0).setData("Clic", R.drawable.clic, R.drawable.clicblack);
		btList.get(0).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				ActionGesture.click(x, y);
			}
		});

		//BOUTON CLIC LONG
		btList.get(1).setData("Long", R.drawable.longclic, R.drawable.longiconblack);
		btList.get(1).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				ActionGesture.longClick(x, y);
			}
		});

		//BOUTON GLISSER
		btList.get(2).setData("Glisser", R.drawable.swipe, R.drawable.swipeblack);
		btList.get(2).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				theCtrl.closePopup();
				clickPanel().gestureDone();
				clickPanel().setForSwipe(true);
			}
		});

		//BOUTON ACTIONS
		btList.get(3).setData("Actions", R.drawable.actions, R.drawable.actionsblack);
		btList.get(3).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				clickPanel().removeTouchDetection();
				clickPanel().openShortcutMenu();
			}
		});

		//BOUTON PAGE SUIVANTE
		btList.get(4).setData("Page suivante >");
		btList.get(4).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//theCtrl.removeAllViews();
				iterations=0;
				nextPage();
			}
		});
		for(int i=0;i<btList.size()-1;i++)
			btList.get(i).setNotSelectedStyle();
	}

	/**
	 * Méthode avec les différents listeners sur les boutons :
	 * En haut, En bas, A gauche, A droite et pages précédente.
	 */
	public void nextPage(){
		//BOUTON EN HAUT
		btList.get(0).setData("En haut", R.drawable.haut, R.drawable.hautblack);
		btList.get(0).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				ActionGesture.pageUp(x, y, theCtrl.getService().getScreenSize());
			}
		});

		//BOUTON EN BAS
		btList.get(1).setData("En bas", R.drawable.bas, R.drawable.basblack);
		btList.get(1).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				ActionGesture.pageDown(x, y, theCtrl.getService().getScreenSize());
			}
		});

		//BOUTON A GAUCHE
		btList.get(2).setData("A gauche", R.drawable.gauche, R.drawable.gaucheblack);
		btList.get(2).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				ActionGesture.pageLeft(x, y, theCtrl.getService().getScreenSize());
			}
		});

		//BOUTON A DROITE
		btList.get(3).setData("A droite", R.drawable.droite, R.drawable.droiteblack);
		btList.get(3).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				int x = clickPanel().getPos().x;
				int y = clickPanel().getPos().y;
				ActionGesture.pageRight(x, y, theCtrl.getService().getScreenSize());
			}
		});

		//BOUTON PAGE PRECEDENTE
		btList.get(4).setData("< Page précédente");
		btList.get(4).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				iterations=0;
				prevPage();
			}
		});

		for(int i=0;i<btList.size()-1;i++)
			btList.get(i).setNotSelectedStyle();

	}

	/**
	 * Permet de sélectionner le bouton suivant sur la page (pop-up).
	 */
	public void selectNext(){
		if (iterations == Integer.parseInt(sp.getString("iterations","3"))){
			clickPanel().closePopupCtrl();
			clickPanel().closeScreenTouchDetection();
		}
		else{
			for(int i=0; i<btList.size();i++)
				btList.get(i).setNotSelectedStyle();
			

			int current = selectedIndex+1;
			if(current>(btList.size()-1)) current = 0;
			
			btList.get(current).setSelectedStyle();
			
			if(selectedIndex<(btList.size()-1)) selectedIndex++;
			else selectedIndex=0;
			
			if(selectedIndex==(btList.size()-1)) iterations++;
			
			selected = btList.get(selectedIndex);
			if(sp.getBoolean("vocal", false)) {
				SpeakAText.speak(theCtrl.getService(), btList.get(selectedIndex).getButton().getText().toString().replaceAll("[^A-Za-z0-9éà ]", ""));
			}
			popUp.setContentView(view);
		}
	}

	public ClickPanelCtrl clickPanel(){
		return theCtrl.getService().getClickPanelCtrl();
	}

	public PopupCtrl getCtrl(){
		return theCtrl;
	}

	public Point getPos(){
		return theCtrl.getPos();
	}

	public Button getSelected(){
		return selected.getButton();
	}

	/**
	 * Permet d'associer un bouton avec son image, son texte.
	 * @author OneSwitch B
	 */
	private class ButtonGroup {
		private Button button;
		private Drawable blackIcon, whiteIcon;

		public ButtonGroup(int btID) {
			button = (Button) view.findViewById(btID);
		}

		public Button getButton(){
			return button;
		}

		/**
		 * Permet de modifier un bouton
		 * @param textButton Change le texte du bouton
		 * @param idIconWhite Icone blanche (quand ce n'est pas sélectionné)
		 * @param idIconBlack Icone noir (quand le bouton est sélectionné)
		 */
		public void setData(String textButton, int idIconWhite, int idIconBlack){
			if(button!=null){
				button.setText(textButton);
				whiteIcon = getResources().getDrawable(idIconWhite);
				blackIcon = getResources().getDrawable(idIconBlack);
			}
		}

		/**
		 * Modifie un bouton
		 * @param textButton
		 */
		public void setData(String textButton){
			if(button!=null){
				button.setText(textButton);
				whiteIcon = null;
				blackIcon = null;
			}
		}

		/**
		 * Change le bouton automatiquement pour un bouton sélectionné
		 */
		public void setNotSelectedStyle(){
			button.setBackground(getResources().getDrawable(R.drawable.buttonpopup));
			button.setTextColor(Color.WHITE);
			button.setCompoundDrawablesWithIntrinsicBounds(null, whiteIcon, null, null);
		}

		/**
		 * Change le bouton automatiquement pour un bouton non sélectionné
		 */
		public void setSelectedStyle(){
			button.setCompoundDrawablesWithIntrinsicBounds(null, blackIcon, null, null);
			button.setBackground(getResources().getDrawable(R.drawable.buttonpopupselected));
			button.setTextColor(Color.BLACK);
		}
	}
}
