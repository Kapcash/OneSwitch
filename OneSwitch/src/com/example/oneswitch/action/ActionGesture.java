package com.example.oneswitch.action;

import java.io.IOException;

public class ActionGesture {
	
	public static void click(int x, int y){
		try{
			Runtime.getRuntime().exec("su -c input tap " + x + " " + y);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void swipe(int x, int y, int x2, int y2){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x2 + " " + y2 + " 300");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void longClick(int x, int y){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + y + " 800");
			return;
		}
		catch (IOException localIOException){
			localIOException.printStackTrace();
		}
	}
	
	public static void pageUp(){}
	public static void pageDown(){}
	public static void pageLeft(){}
	public static void pageRight(){}
}
