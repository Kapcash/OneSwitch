package com.iut.oneswitch.view.popup;

import java.util.ArrayList;

public class PopUpPage {
	private ArrayList<PopUpItem> items;
	
	public PopUpPage() {
		items = new ArrayList<PopUpItem>();
	}
	
	public void addItem(PopUpItem item){
		items.add(item);
	}
	
	public ArrayList<PopUpItem> getItems(){
		return items;
	}
}
