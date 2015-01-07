package com.iut.oneswitch.application;

import java.io.IOException;

import android.graphics.Point;
import android.os.Handler;

public class ActionGesture {

	private Point pos1, pos2;
	
	public void touchAsRoot(Point pos){
		pos1 = pos;
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				try {
					Runtime.getRuntime().exec("su -c input tap " + pos1.x + " " + pos1.y);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 10);
	}
	
	public void swipeAsRoot(Point posUn, Point posDeux){
		pos1 = posUn;
		pos2 = posDeux;
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				try {
					Runtime.getRuntime().exec("su -c input swipe " + pos1.x +  " " + pos1.y + " " + pos2.x + " " + pos2.y + " 300");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 10);
	}
	
	public void longTouchAsRoot(Point posUn){
		pos1 = posUn;
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				try {
					Runtime.getRuntime().exec("su -c input swipe " + pos1.x +  " " + pos1.y + " " + pos1.x + " " + pos1.y + " 1000");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 10);
	}
}