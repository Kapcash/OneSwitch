package iut.oneswitch.action;

import java.io.IOException;

import android.graphics.Point;

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
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void pageUp(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + screenSize.y + " 300");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void pageDown(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + x + " " + 0 + " 300");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public static void pageLeft(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + screenSize.x + " " + y + " 300");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public static void pageRight(int x, int y, Point screenSize){
		try{
			Runtime.getRuntime().exec("su -c input swipe " + x + " " + y + " " + 0 + " " + y + " 300");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
