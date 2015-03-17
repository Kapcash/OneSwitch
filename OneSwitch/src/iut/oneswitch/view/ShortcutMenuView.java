package iut.oneswitch.view;

import java.io.IOException;

import iut.oneswitch.R;
import iut.oneswitch.action.ActionButton;
import iut.oneswitch.control.ClickPanelCtrl;
import iut.oneswitch.control.ShortcutMenuCtrl;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

public class ShortcutMenuView extends View{
	private Button[] buttonList;
	private int iterations;
	private PopupWindow popUp;
	private Button selected;
	private int selectedIndex;
	private ShortcutMenuCtrl theCtrl;
	private View view;

	public ShortcutMenuView(Context paramContext, ShortcutMenuCtrl paramShortcutMenuCtrl){
		super(paramContext);
		theCtrl = paramShortcutMenuCtrl;
		popUp = new PopupWindow(getContext());
	}

	private void listener(){
		buttonList[0].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				ActionButton.back();
			}
		});
		
		buttonList[1].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				ActionButton.taches();
			}
		});
		
		buttonList[2].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				ActionButton.home();
			}
		});
		
		buttonList[3].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				ActionButton.menu();
			}
		});
		
		buttonList[4].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				try {
					ActionButton.volumeUp(ShortcutMenuView.this.getContext());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		buttonList[5].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				ActionButton.lock();
			}
		});
		
		buttonList[6].setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				ActionButton.shutdown();
			}
		});
	}

	public ClickPanelCtrl clickPanel(){
		return theCtrl.getService().getClickPanelCtrl();
	}

	public Button getButton(int index){
		if(index < 0 || index > 6) throw new IllegalArgumentException("Mauvais index");
		return buttonList[index];
	}

	public Button getSelected(){
		return selected;
	}

	public void onDraw(Canvas paramCanvas){
		float density = getResources().getDisplayMetrics().density;

		buttonList = new Button[7];

		LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService
				(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.shortcutlayout,null);
		popUp.setContentView(view);

		popUp.showAtLocation(this, Gravity.CENTER, 0, 0);
		popUp.update(28, 0, (int)(300*density), (int)(360*density));
		
		
		Button butBack = (Button)view.findViewById(R.id.but_back);
		Button butTache = (Button)view.findViewById(R.id.but_taches);
		Button butHome = (Button)view.findViewById(R.id.but_home);
		Button butMenu = (Button)view.findViewById(R.id.but_menu);
		Button butVolup = (Button)view.findViewById(R.id.but_volup);
		Button butLock = (Button)view.findViewById(R.id.but_lock);
		Button butShut = (Button)view.findViewById(R.id.but_shutdown);
		
		selected = butBack;
		selectedIndex = 0;

		buttonList[0] = butBack;
		buttonList[1] = butTache;
		buttonList[2] = butHome;
		buttonList[3] = butMenu;
		buttonList[4] = butVolup;
		buttonList[5] = butLock;
		buttonList[6] = butShut;
		
		buttonList[selectedIndex].getBackground().setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));

		for(int i = 0; i<buttonList.length;i++){
			if(i != selectedIndex){
				buttonList[i].getBackground().clearColorFilter();
			}
		}
		popUp.setContentView(view);
		listener();
		theCtrl.startThread();
	}

	public void selectNext()
	{
		if (iterations == 3)
		{
			clickPanel().closeShortcutMenu();
			return;
		}
		selectedIndex = ((1 + selectedIndex) % 7);
		selected = buttonList[selectedIndex];
		buttonList[selectedIndex].getBackground().setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
		for (int i = 0;; i++)
		{
			if (i >= buttonList.length)
			{
				if (selectedIndex == -1 + buttonList.length) {
					iterations = (1 + iterations);
				}
				popUp.setContentView(view);
				return;
			}
			if (i != selectedIndex) {
				buttonList[i].getBackground().clearColorFilter();
			}
		}
	}
}