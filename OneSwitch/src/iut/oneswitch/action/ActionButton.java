package iut.oneswitch.action;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.view.KeyEvent;

public class ActionButton {

	private static boolean volumeStop = true;

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

		Runnable a = new Runnable() {

			public void run() {

				AudioManager myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				try {

					if(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) { 
						myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
								AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
						System.out.println("UP");
						if(!volumeStop)
							handler.postDelayed(this, 300);

					}



					else {
						myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, AudioManager.FLAG_SHOW_UI);
						System.out.println(myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
						if(!volumeStop)
							handler.postDelayed(this, 250);
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
