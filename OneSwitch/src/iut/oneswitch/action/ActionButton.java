package iut.oneswitch.action;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

public class ActionButton {

	private static boolean volumeStop = true;
	private static int count;
	private static boolean changeWay = false;

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

	public static void volumeUp(final Context context) throws IOException{
		final Handler handler = new Handler();
		volumeStop = false;
		final SharedPreferences sp;
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		final int iterations = Integer.parseInt(sp.getString("iterations","3"));
		count = 0;

		Runnable a = new Runnable() {

			public void run() {
				
				AudioManager myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				try {
					if(count != iterations && !volumeStop) {
						if(!changeWay) {
							if(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
								myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
			                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
							}
							else {
								changeWay = true;
							}
						}
						if(changeWay && count != iterations) {
							if(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
								myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
				                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
								
							}
							else {
								changeWay = false;
								count++;
							}
							
						}
						if(count != iterations){
							handler.postDelayed(this, 250);
						}
					}
					else {
						changeWay = false;
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		a.run();
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

	public static void stopVolumeChange() {
		volumeStop = true;
	}

	public static boolean getVolumeStop() {
		return volumeStop;
	}
}
