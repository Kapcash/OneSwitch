package com.example.oneswitch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.oneswitch.R;
import com.example.oneswitch.action.ActionGesture;
import com.example.oneswitch.control.ClickPanelCtrl;
import com.example.oneswitch.control.PopupCtrl;

public class PopupView extends View{

	private int iterations = -1;
	private int selectedIndex = 0;
	private PopupWindow popUp;
	private ButtonGroup selected;
	private PopupCtrl theCtrl;
	private View view;
	private SparseArray<ButtonGroup> btList = new SparseArray<ButtonGroup>();

	public PopupView(Context paramContext, PopupCtrl paramPopupCtrl){
		super(paramContext);
		theCtrl = paramPopupCtrl;
		init();
	}

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

	public void onDraw(Canvas canvas){
		float density = getResources().getDisplayMetrics().density;

		popUp.setContentView(view);
		popUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupbackground));
		popUp.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0);
		popUp.update(0, 0, (int)(canvas.getWidth()), canvas.getHeight());

		selectedIndex = btList.size()-1;
		selected = btList.get(selectedIndex);

		theCtrl.start();
		prevPage();
	}

	private void addButton(int btId){
		btList.put(btList.size(), new ButtonGroup(btId));
	}

	private void prevPage(){
		//BOUTON CLIC
		btList.get(0).setData("Clic", R.drawable.clic, R.drawable.clicblack);
		btList.get(0).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clickPanel().setVisible(false);
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
				clickPanel().setVisible(false);
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

		//BOUTON AUTRES
		btList.get(3).setData("Autres", R.drawable.autres, R.drawable.autresblack);
		btList.get(3).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				theCtrl.removeAllViews();
				clickPanel().gestureDone();
				theCtrl.getService().stopService();
			}
		});

		//BOUTON PAGE SUIVANTE
		btList.get(4).setData("Page suivante >");
		btList.get(4).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//theCtrl.removeAllViews();
				iterations=-1;
				nextPage();
			}
		});
		for(int i=0;i<btList.size()-1;i++)
			btList.get(i).setNotSelectedStyle();
	}

	public void nextPage(){
		//BOUTON EN HAUT
		btList.get(0).setData("En haut", R.drawable.haut, R.drawable.hautblack);
		btList.get(0).getButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clickPanel().setVisible(false);
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
				clickPanel().setVisible(false);
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
				clickPanel().setVisible(false);
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
				clickPanel().setVisible(false);
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
				iterations=-1;
				prevPage();
			}
		});


		for(int i=0;i<btList.size()-1;i++)
			btList.get(i).setNotSelectedStyle();
		

	}

	public void selectNext(){
		if (iterations == 3){
			clickPanel().closePopupCtrl();
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

	class ButtonGroup {
		private Button button;
		private Drawable blackIcon, whiteIcon;

		public ButtonGroup(int btID) {
			button = (Button) view.findViewById(btID);
		}

		public Button getButton(){
			return button;
		}

		public void setData(String textButton, int idIconWhite, int idIconBlack){
			if(button!=null){
				button.setText(textButton);
				whiteIcon = getResources().getDrawable(idIconWhite);
				blackIcon = getResources().getDrawable(idIconBlack);
			}
		}

		public void setData(String textButton){
			if(button!=null){
				button.setText(textButton);
				whiteIcon = null;
				blackIcon = null;
			}
		}

		public void setNotSelectedStyle(){
			button.setBackground(getResources().getDrawable(R.drawable.buttonpopup));
			button.setTextColor(Color.WHITE);
			button.setCompoundDrawablesWithIntrinsicBounds(null, whiteIcon, null, null);
		}

		public void setSelectedStyle(){
			button.setCompoundDrawablesWithIntrinsicBounds(null, blackIcon, null, null);
			button.setBackground(getResources().getDrawable(R.drawable.buttonpopupselected));
			button.setTextColor(Color.BLACK);
		}
	}
}
