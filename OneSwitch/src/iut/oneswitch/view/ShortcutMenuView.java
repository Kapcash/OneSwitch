package iut.oneswitch.view;

import iut.oneswitch.R;
import iut.oneswitch.action.ActionButton;
import iut.oneswitch.action.SpeakAText;
import iut.oneswitch.control.ClickPanelCtrl;
import iut.oneswitch.control.ShortcutMenuCtrl;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * Classe permettant de dessiner la popUp sur un long clic..
 * @author OneSwitch B
 *
 */
public class ShortcutMenuView extends View{
	private int iterations = 0;
	private PopupWindow popUp;
	private ButtonGroup selected;
	private int selectedIndex;
	private ShortcutMenuCtrl theCtrl;
	private View view;
	private SparseArray<ButtonGroup> btList = new SparseArray<ButtonGroup>();
	private Context context;
	private SharedPreferences sp;

	/**
	 * Constructeur de la classe permettant d'initialiser la popUp Window et d'ajouter les boutons.
	 * @param paramContext
	 * @param paramShortcutMenuCtrl
	 */
	public ShortcutMenuView(Context paramContext, ShortcutMenuCtrl paramShortcutMenuCtrl){
		super(paramContext);
		context = paramContext;
		theCtrl = paramShortcutMenuCtrl;
		popUp = new PopupWindow(getContext());
		sp = PreferenceManager.getDefaultSharedPreferences(context);

		LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.shortcutlayout,null);

		//AJOUT BOUTON MENU
		addButton(R.id.but_menu, R.drawable.menu, R.drawable.menublack);

		//AJOUT BOUTON HOME
		addButton(R.id.but_home, R.drawable.home, R.drawable.homeblack);

		//AJOUT BOUTON RETOUR
		addButton(R.id.but_back, R.drawable.back, R.drawable.backblack);

		//AJOUT BOUTON MULTITASK
		addButton(R.id.but_taches, R.drawable.multitask, R.drawable.multitaskblack);

		//AJOUT BOUTON VOLUMEs
		addButton(R.id.but_volume, R.drawable.volume, R.drawable.volumeblack);

		//AJOUT BOUTON OKGOOGLE
		addButton(R.id.but_okgoogle, R.drawable.okgoogle, R.drawable.okgoogleblack);
	}

	/**
	 * Permet d'ajouter un bouton
	 * @param btId
	 * @param idIconWhite
	 * @param idIconBlack
	 */
	private void addButton(int btId, int idIconWhite, int idIconBlack){
		btList.put(btList.size(), new ButtonGroup(btId, idIconWhite, idIconBlack));
	}

	/**
	 * Les listenners sur tout les boutons du menu.
	 */
	private void listener(){
		//LISTENER MENU
		btList.get(0).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				ActionButton.menu();
			}
		});

		//LISTENER HOME
		btList.get(1).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				ActionButton.home();
			}
		});

		//LISTENER BACK
		btList.get(2).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				ActionButton.back();
			}
		});

		//LISTENER MULTITASK
		btList.get(3).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				ActionButton.taches();
			}
		});

		//LISTENER VOLUME
		btList.get(4).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try {
					ActionButton.volumeUp(ShortcutMenuView.this.getContext());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		//LISTENER OK GOOGLE
		btList.get(5).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setClassName("com.google.android.googlequicksearchbox",
						"com.google.android.googlequicksearchbox.VoiceSearchActivity");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Log.d("OK","Google Search");
				context.startActivity(intent);
			}
		});


	}

	public ClickPanelCtrl clickPanel(){
		return theCtrl.getService().getClickPanelCtrl();
	}


	public Button getSelected(){
		return selected.getButton();
	}

	/**
	 * Permet de dessiner la popUp
	 */
	@Override
	public void onDraw(Canvas canvas){
		try{
			popUp.setContentView(view);
			popUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupbackground));
			popUp.showAtLocation(this, Gravity.CENTER, 0, 0);

			popUp.update(0, 0, (canvas.getWidth()), canvas.getHeight());


			selectedIndex = btList.size()-1;
			selected = btList.get(selectedIndex);
			listener();
			theCtrl.start();
		}catch(Exception e){}
	}


	/**
	 * Permet de changer d'item actuellement sélectionné.
	 */
	public void selectNext(){
		if (iterations == Integer.parseInt(sp.getString("iterations","3"))){
			clickPanel().closeShortcutMenu();
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
			if(sp.getBoolean("vocal", false) && btList.get(selectedIndex).getButton() == btList.get(btList.size()-1).getButton()) {
				SpeakAText.resetSpeak(context, btList.get(selectedIndex).getButton().getText().toString().replaceAll("[^A-Za-z0-9éà\\s]", ""));
			}
			else if(sp.getBoolean("vocal", false)) {
				SpeakAText.speak(context, btList.get(selectedIndex).getButton().getText().toString().replaceAll("[^A-Za-z0-9éà\\s]", ""));
			}

			popUp.setContentView(view);
		}
	}


	/**
	 * Permet d'associer un bouton avec son image, son texte.
	 * @author OneSwitch B
	 */
	private class ButtonGroup {
		private Button button;
		private Drawable blackIcon, whiteIcon;

		public ButtonGroup(int btID, int idIconWhite, int idIconBlack) {
			button = (Button) view.findViewById(btID);
			whiteIcon = getResources().getDrawable(idIconWhite);
			blackIcon = getResources().getDrawable(idIconBlack);
		}

		public Button getButton(){
			return button;
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