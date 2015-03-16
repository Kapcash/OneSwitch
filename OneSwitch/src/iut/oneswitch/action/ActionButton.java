package iut.oneswitch.action;

import java.io.IOException;

import android.view.KeyEvent;

public class ActionButton {
	
	public static void back(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_BACK);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void taches(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_APP_SWITCH);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void home(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_HOME);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void menu(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_MENU);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void volumeUp(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_VOLUME_UP);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void volumeDown(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_VOLUME_UP);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void lock(){
		try{
			Runtime.getRuntime().exec("su -c input keyevent " + KeyEvent.KEYCODE_POWER);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void shutdown(){
		try{
			//Runtime.getRuntime().exec("su -c shutdown");
			Runtime.getRuntime().exec("su -c reboot -p");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}


}
